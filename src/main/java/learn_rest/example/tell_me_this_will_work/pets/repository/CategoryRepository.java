package learn_rest.example.tell_me_this_will_work.pets.repository;

import learn_rest.example.tell_me_this_will_work.pets.models.Category;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {

}
