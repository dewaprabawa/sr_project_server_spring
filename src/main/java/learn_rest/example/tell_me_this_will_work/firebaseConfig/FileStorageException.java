package learn_rest.example.tell_me_this_will_work.firebaseConfig;

public class FileStorageException extends Exception {
    String message;
    FileStorageException(
            String message
    ){
        this.message = message;
    }
}
