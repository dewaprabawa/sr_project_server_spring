package learn_rest.example.tell_me_this_will_work.pets.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import learn_rest.example.tell_me_this_will_work.helper.FinalResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import learn_rest.example.tell_me_this_will_work.pets.models.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import learn_rest.example.tell_me_this_will_work.pets.repository.PetRepository;

@CrossOrigin(origins = "http://127.0.0.1:8080")
@RestController
@RequestMapping(value = "/api" , consumes = MediaType.APPLICATION_JSON_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
public class PetController {
    private static final Logger logger = LoggerFactory.getLogger(PetController.class);

    @Autowired
    PetRepository petRepository;

    @GetMapping(value = "/pets")
    public ResponseEntity<FinalResult<List<Pet>>> getAllPets(@RequestParam(required = false) String name) {

        logger.debug("requestParam " +  name);
        try {
            List<Pet> pets = new ArrayList<Pet>();
            if (name == null)
                petRepository.findAll().forEach(pets::add);
            else
                petRepository.findByNameContaining(name).forEach(pets::add);

            if (pets.isEmpty()) {
                final String message = "THE DATA IS EMPTY";
                final String statusCode = "THE_DATA_IS_EMPTY";
                var res = new FinalResult(true, message, statusCode, null);

                return new ResponseEntity<>(res, HttpStatus.OK);
            }

            final String message = "THE DATA IS EXISTING";
            final String statusCode = "THE_DATA_IS_EXISTING";
            var res = new FinalResult(true, message, statusCode, pets);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            System.out.print(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pet/{id}")
    public ResponseEntity<FinalResult<Pet>> getPetById(@PathVariable("id") String id) {
       try{
           Optional<Pet> pet = petRepository.findById(id);
           if(pet.isPresent()){
               String message = "SUCCESS GET SPECIFIC PET";
               String statusCode = "SUCCESS_GET_PET";
               Pet data = pet.get();
               var result = new FinalResult(true, message, statusCode, data);
               return new ResponseEntity<>(result, HttpStatus.OK);
           }else{
               String message = "PET NOT FOUND";
               String statusCode = "FAILED_GET_PET";
               var result = new FinalResult(true, message, statusCode, null);
               return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
           }
       }catch (Exception e){
           System.out.print(e.toString());
           return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @PostMapping(value = "/pet", consumes = MediaType.APPLICATION_JSON_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FinalResult<Pet>> createPetData(@RequestBody Pet pet) {
        try {
            var _t = new Pet(pet.getName(), pet.getDescription(), pet.getAge(), pet.getCategory(),pet.getImageURL(),false, pet.getKind());
            Pet _pet = petRepository.save(_t);
            String message = "SUCCESS CREATE PET";
            String statusCode = "SUCCESS_CREATE_PET";
            var result = new FinalResult(true, message, statusCode, _t);
            System.out.print(result.message);
            System.out.print(result.statusCode);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.print(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/pet/{id}")
    public ResponseEntity<FinalResult<Pet>> updatePetData(@PathVariable("id") String id, @RequestBody Pet tutorial) {
       try{
           Optional<Pet> _tutorial = petRepository.findById(id);
           if(_tutorial.isPresent()){
               var updatedTutorial = _tutorial.get();
               updatedTutorial.setName(updatedTutorial.getName());
               updatedTutorial.setDescription(updatedTutorial.getDescription());
               updatedTutorial.setPublished(updatedTutorial.isPublished());
               final String message = "SUCCESS UPDATE DATA";
               final String statusCode = "SUCCESS_UPDATE_DATA";
               var result = new FinalResult(true, message, statusCode, updatedTutorial);

               return new ResponseEntity<>(result, HttpStatus.OK);
           }else{
               return new ResponseEntity<>(HttpStatus.NOT_FOUND);
           }
       }catch (Exception e){
           return  new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @DeleteMapping("/pet/{id}")
    public ResponseEntity<FinalResult<Pet>> deleteDeletePet(@PathVariable("id") String id) {
        try {
         petRepository.deleteById(id);
            String message = "SUCCESS DELETING FILE";
            String statusCode = "SUCCESS_DELETE_PET";
            var result = new FinalResult(true, message, statusCode, null);
         return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            System.out.print(e.toString());
            String message = "DELETING FILE FAILED";
            String statusCode = "FAILED_DELETE_PET";
            var result = new FinalResult(false, message, statusCode, null);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/pets")
    public ResponseEntity<FinalResult<Pet>> deleteAllPets() {

        try{
            petRepository.deleteAll();
            final String message = "SUCCESS DELETING ALL PET";
            final String statusCode = "SUCCESS_DELETE_ALL_PET";
            var result = new FinalResult(true, message, statusCode, null);
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }catch (Exception e){
            System.out.print(e.toString());
            final String message = "DELETING FILE FAILED";
            final String statusCode = "FAILED_DELETE_PET";
            var result = new FinalResult(false, message, statusCode, null);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/pet")
    public ResponseEntity<FinalResult<List<Pet>>> findByPublished() {
        try {
            List<Pet> tutorials = petRepository.findByPublished(true);

            if (tutorials.isEmpty()) {
                final String message = "THE DATA IS EMPTY";
                final String statusCode = "THE_DATA_IS_EMPTY";
                var res = new FinalResult(true, message, statusCode, null);

                return new ResponseEntity<>(res, HttpStatus.NO_CONTENT);
            }
            final String message = "THE DATA IS EXISTING";
            final String statusCode = "THE_DATA_IS_EXISTING";
            var res = new FinalResult(true, message, statusCode, tutorials);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            System.out.print(e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

