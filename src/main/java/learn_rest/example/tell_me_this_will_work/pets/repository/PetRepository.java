package learn_rest.example.tell_me_this_will_work.pets.repository;

import java.util.List;

import learn_rest.example.tell_me_this_will_work.pets.models.Pet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PetRepository extends MongoRepository<Pet, String>{
     List<Pet> findByNameContaining(String name);
     List<Pet> findByCategoryContaining(String name);
     List<Pet> findByPublished(boolean published);
}
