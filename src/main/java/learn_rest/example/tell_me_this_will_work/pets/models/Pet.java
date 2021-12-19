package learn_rest.example.tell_me_this_will_work.pets.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pets")
public class Pet {
    @Id
    private String id;

    private String name;
    private String description;
    private String age;
    private String category;
    private String kind;

    public void setId(String id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    private String imageURL;

    private boolean published;

    public Pet() {

    }

    public Pet(String name, String description,String age,String category, String imageURL ,boolean published, String kind){
        this.name = name;
        this.age = age;
        this.category = category;
        this.imageURL = imageURL;
        this.description = description;
        this.published = published;
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id='" + id + '\'' +
                ", title='" + name + '\'' +
                ", description='" + description + '\'' +
                ", published=" + published +
                '}';
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
