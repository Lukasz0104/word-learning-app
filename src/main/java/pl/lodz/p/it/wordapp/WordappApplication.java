package pl.lodz.p.it.wordapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import pl.lodz.p.it.wordapp.model.LearningSet;
import pl.lodz.p.it.wordapp.repository.LearningSetRepository;

@SpringBootApplication
public class WordappApplication {

	public static void main(String[] args) {
		SpringApplication.run(WordappApplication.class, args);
	}

	@Bean
	@Order(1)
	public CommandLineRunner initLearningSets(LearningSetRepository learningSetRepository) {
		return args -> {
			learningSetRepository.save(new LearningSet(true, "DE", "PL"));
			learningSetRepository.save(new LearningSet(false, "DE", "EN"));
		};
	}
}
