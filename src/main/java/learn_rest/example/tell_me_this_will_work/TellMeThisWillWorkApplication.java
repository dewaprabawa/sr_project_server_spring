package learn_rest.example.tell_me_this_will_work;
import learn_rest.example.tell_me_this_will_work.firebaseConfig.FailedToSetCredentialsException;
import learn_rest.example.tell_me_this_will_work.firebaseConfig.FileStorageException;
import learn_rest.example.tell_me_this_will_work.firebaseConfig.FirebaseConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.PortInUseException;


@SpringBootApplication
public class TellMeThisWillWorkApplication {

	public static void main(String[] args) throws FailedToSetCredentialsException, FileStorageException {
		FirebaseConfig.initFirebase();
		try{
			SpringApplication.run(TellMeThisWillWorkApplication.class, args);
		}catch (PortInUseException e){
			SpringApplication.run(TellMeThisWillWorkApplication.class, new String[]{"--server.port=8444"});
		}
	}

}




