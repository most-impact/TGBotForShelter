package pro.dev.TGBotForShelter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TgBotForShelterApplication {
	public static void main(String[] args) {
		SpringApplication.run(TgBotForShelterApplication.class, args);
	}
}
