package learn_rest.example.tell_me_this_will_work.auth.model;

import learn_rest.example.tell_me_this_will_work.helper.ERole;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
public class Role {
    @Id
    private String id;

    private ERole name;

    private Role() {

    }

    private Role(ERole name){
        this.name = name;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setName(ERole ERoleName) {
        this.name = ERoleName;
    }

    public ERole getName() {
        return name;
    }
}
