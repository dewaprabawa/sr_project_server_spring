package controller;

import java.util.List;
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
    public ResponseEntity<Tutorial>getAllTutorials(@RequestParam(required = false) String title){
        return null;
    }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial>getTutorialById(@PathVariable("id") String id){
     return null;
    }

    @PostMapping("/tutorials")
    public ResponseEntity<Tutorial>createTutorial(@RequestBody Tutorial tutorial){
        try{
            var _t = new Tutorial(tutorial.getTitle(),tutorial.getDescription(), false);
            Tutorial _tutorial = tutorialRepository.save(_t);
            return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        }
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial>updateTutorial(@PathVariable("id") String id, Tutorial tutorial){
     return  null;
    }

    @DeleteMapping("/tutorial/{id}")
    public ResponseEntity<HttpStatus>deleteTutorial(@PathVariable("id") String id){
        return null;
    }

    @DeleteMapping("/tutorials")
    public ResponseEntity<HttpStatus>deletingAllTutorial(){
        return null;
    }

    @GetMapping("/tutorial")
    public ResponseEntity<List<Tutorial>> findByPublished(){
        return null;
    }
}
