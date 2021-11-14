package learn_rest.example.tell_me_this_will_work.tutorial.repository;

import java.util.List;

import learn_rest.example.tell_me_this_will_work.tutorial.models.Tutorial;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TutorialRepository extends MongoRepository<Tutorial, String>{
     List<Tutorial> findByTitleContaining(String title);
     List<Tutorial> findByPublished(boolean published);
}
