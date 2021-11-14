package learn_rest.example.tell_me_this_will_work.auth.model;

import learn_rest.example.tell_me_this_will_work.helper.ROLE;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
public class Role {
    @Id
    private String id;

    private ROLE roleName;

    private Role() {

    }

    private Role(ROLE roleName){
        this.roleName = roleName;
    }

    public String getId() {
        return id;
    }

    public void setRoleName(ROLE roleName) {
        this.roleName = roleName;
    }

    public ROLE getRoleName() {
        return roleName;
    }
}
