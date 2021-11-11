package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import models.Tutorial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.TutorialRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TutorialController {
    @Autowired
    TutorialRepository tutorialRepository;

    @GetMapping("/tutorials")
    public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {
        try {
            List<Tutorial> tutorials = new ArrayList<Tutorial>();
            if (title == null)
                tutorialRepository.findAll().forEach(tutorials::add);
            else
                tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tutorials, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<FinalResult<Tutorial>> getTutorialById(@PathVariable("id") String id) {
        Optional<Tutorial> tutorial = tutorialRepository.findById(id);
        if(tutorial.isPresent()){
            String message = "SUCCESS GET SPECIFIC TUTORIAL";
            String statusCode = "SUCCESS_GET_TUTORIAL";
            Tutorial data = tutorial.get();
            var result = new FinalResult(message, statusCode, data);
           return new ResponseEntity<>(result, HttpStatus.OK);
        }else{
            String message = "TUTORIAL NOT FOUND";
            String statusCode = "FAILED_GET_TUTORIAL";
            var result = new FinalResult(message, statusCode, null);
           return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tutorials")
    public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
        try {
            var _t = new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false);
            Tutorial _tutorial = tutorialRepository.save(_t);
            return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") String id, @RequestBody Tutorial tutorial) {
       try{
           Optional<Tutorial> _tutorial = tutorialRepository.findById(id);
           if(_tutorial.isPresent()){
               var t = _tutorial.get();
               t.setTitle(tutorial.getTitle());
               t.setDescription(tutorial.getDescription());
               t.setPublished(tutorial.isPublished());
               return new ResponseEntity<>(t, HttpStatus.OK);
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
            var result = new FinalResult(message, statusCode, null);
         return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            System.out.print(e.toString());
            String message = "DELETING FILE FAILED";
            String statusCode = "FAILED_DELETE_TUTORIAL";
            var result = new FinalResult(message, statusCode, null);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tutorials")
    public ResponseEntity<HttpStatus> deletingAllTutorial() {
        return null;
    }

    @GetMapping("/tutorial")
    public ResponseEntity<List<Tutorial>> findByPublished() {
        return null;
    }
}

class FinalResult<T>{
   String statusCode;
   String message;
   T data;

   FinalResult(String statusCode, String message, T data){
       this.statusCode = statusCode;
       this.message = message;
       this.data = data;
   }
}
