package learn_rest.example.tell_me_this_will_work.tutorial.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import learn_rest.example.tell_me_this_will_work.auth.security.JwtUtils;
import learn_rest.example.tell_me_this_will_work.helper.FinalResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import learn_rest.example.tell_me_this_will_work.tutorial.models.Tutorial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import learn_rest.example.tell_me_this_will_work.tutorial.repository.TutorialRepository;

@CrossOrigin(origins = "http://127.0.0.1:8080")
@RestController
@RequestMapping(value = "/api" , consumes = MediaType.APPLICATION_JSON_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
public class TutorialController {
    private static final Logger logger = LoggerFactory.getLogger(TutorialController.class);

    @Autowired
    TutorialRepository tutorialRepository;

    @GetMapping(value = "/tutorials")
    public ResponseEntity<FinalResult<List<Tutorial>>> getAllTutorials(@RequestParam(required = false) String title) {

        logger.debug("requestParam " +  title);
        try {
            List<Tutorial> tutorials = new ArrayList<Tutorial>();
            if (title == null)
                tutorialRepository.findAll().forEach(tutorials::add);
            else
                tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);

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
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<FinalResult<Tutorial>> getTutorialById(@PathVariable("id") String id) {
       try{
           Optional<Tutorial> tutorial = tutorialRepository.findById(id);
           if(tutorial.isPresent()){
               String message = "SUCCESS GET SPECIFIC TUTORIAL";
               String statusCode = "SUCCESS_GET_TUTORIAL";
               Tutorial data = tutorial.get();
               var result = new FinalResult(true, message, statusCode, data);
               return new ResponseEntity<>(result, HttpStatus.OK);
           }else{
               String message = "TUTORIAL NOT FOUND";
               String statusCode = "FAILED_GET_TUTORIAL";
               var result = new FinalResult(true, message, statusCode, null);
               return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
           }
       }catch (Exception e){
           System.out.print(e.toString());
           return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @PostMapping(value = "/tutorials", consumes = MediaType.APPLICATION_JSON_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FinalResult<Tutorial>> createTutorial(@RequestBody Tutorial tutorial) {
        try {
            var _t = new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false);
            Tutorial _tutorial = tutorialRepository.save(_t);
            String message = "SUCCESS CREATE TUTORIAL";
            String statusCode = "SUCCESS_CREATE_TUTORIAL";
            var result = new FinalResult(true, message, statusCode, _t);
            System.out.print(result.message);
            System.out.print(result.statusCode);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.print(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<FinalResult<Tutorial>> updateTutorial(@PathVariable("id") String id, @RequestBody Tutorial tutorial) {
       try{
           Optional<Tutorial> _tutorial = tutorialRepository.findById(id);
           if(_tutorial.isPresent()){
               var updatedTutorial = _tutorial.get();
               updatedTutorial.setTitle(updatedTutorial.getTitle());
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

    @DeleteMapping("/tutorial/{id}")
    public ResponseEntity<FinalResult<Tutorial>> deleteTutorial(@PathVariable("id") String id) {
        try {
         tutorialRepository.deleteById(id);
            String message = "SUCCESS DELETING FILE";
            String statusCode = "SUCCESS_DELETE_TUTORIAL";
            var result = new FinalResult(true, message, statusCode, null);
         return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            System.out.print(e.toString());
            String message = "DELETING FILE FAILED";
            String statusCode = "FAILED_DELETE_TUTORIAL";
            var result = new FinalResult(false, message, statusCode, null);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tutorials")
    public ResponseEntity<FinalResult<Tutorial>> deleteAllTutorial() {

        try{
            tutorialRepository.deleteAll();
            final String message = "SUCCESS DELETING ALL FILE";
            final String statusCode = "SUCCESS_DELETE_ALL_FILE";
            var result = new FinalResult(true, message, statusCode, null);
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }catch (Exception e){
            System.out.print(e.toString());
            final String message = "DELETING FILE FAILED";
            final String statusCode = "FAILED_DELETE_TUTORIAL";
            var result = new FinalResult(false, message, statusCode, null);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tutorial")
    public ResponseEntity<FinalResult<List<Tutorial>>> findByPublished() {
        try {
            List<Tutorial> tutorials = tutorialRepository.findByPublished(true);

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

