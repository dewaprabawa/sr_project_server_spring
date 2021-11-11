package models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "roles")
public class Role {
    @Id
    private String id;

    private Role roleName;

    private Role() {

    }

    private Role(Role roleName){
        this.roleName = roleName;
    }

    public String getId() {
        return id;
    }

    public void setRoleName(Role roleName) {
        this.roleName = roleName;
    }

    public Role getRoleName() {
        return roleName;
    }


}
