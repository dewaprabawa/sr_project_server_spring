package repository;

import java.util.List;

import models.Tutorial;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TutorialRepository extends MongoRepository<Tutorial, String>{
     List<Tutorial> findByTitleContaining(String title);
     List<Tutorial> findByPublished(boolean published);
}
