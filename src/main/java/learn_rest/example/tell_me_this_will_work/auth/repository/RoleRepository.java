package learn_rest.example.tell_me_this_will_work.auth.repository;


import learn_rest.example.tell_me_this_will_work.auth.model.Role;
import learn_rest.example.tell_me_this_will_work.helper.ERole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
