package learn_rest.example.tell_me_this_will_work.pets.controller;

import learn_rest.example.tell_me_this_will_work.helper.FinalResult;
import learn_rest.example.tell_me_this_will_work.pets.models.Category;
import learn_rest.example.tell_me_this_will_work.pets.models.Pet;
import learn_rest.example.tell_me_this_will_work.pets.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:8080")
@RestController
@RequestMapping(value = "/api")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping(value = "/categories")
    public ResponseEntity<FinalResult<List<Category>>> getCategory (){
        List<Category> category = new ArrayList<Category>();
        try{
            categoryRepository.findAll().forEach(category::add);
            String message = "CATEGORY SUCCESSFULLY LOADED";
            String statusCode = "SUCCESS_LOADED";
            boolean success = true;

            var result = new FinalResult(success, message, statusCode,
                    category
            );


            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            System.out.print(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
