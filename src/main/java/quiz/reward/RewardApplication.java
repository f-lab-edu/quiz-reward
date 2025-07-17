package quiz.reward;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RewardApplication {
	public static void main(String[] args) {
		SpringApplication.run(RewardApplication.class, args);
	}
}

