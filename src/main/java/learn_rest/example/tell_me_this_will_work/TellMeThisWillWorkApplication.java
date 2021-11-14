package learn_rest.example.tell_me_this_will_work;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class TellMeThisWillWorkApplication {

	public static void main(String[] args) {
		try{
			SpringApplication.run(TellMeThisWillWorkApplication.class, args);
		}catch (org.springframework.boot.web.server.PortInUseException e){
			SpringApplication.run(TellMeThisWillWorkApplication.class, new String[]{"--server.port=8444"});
		}
	}

}
