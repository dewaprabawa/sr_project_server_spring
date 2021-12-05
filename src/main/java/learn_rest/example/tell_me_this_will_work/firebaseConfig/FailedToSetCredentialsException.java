package learn_rest.example.tell_me_this_will_work.firebaseConfig;

public class FailedToSetCredentialsException extends Exception {
    String message;
    FailedToSetCredentialsException(
            String message
    ){
        this.message = message;
    }
}
