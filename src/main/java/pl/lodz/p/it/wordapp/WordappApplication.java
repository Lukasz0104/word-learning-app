package pl.lodz.p.it.wordapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.lodz.p.it.wordapp.model.LearningSet;
import pl.lodz.p.it.wordapp.repository.LearningSetRepository;

@SpringBootApplication
public class WordappApplication {

    public static void main(String[] args) {
        System.out.println("http://localhost:8080/swagger-ui/index.html");
        SpringApplication.run(WordappApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(LearningSetRepository learningSetRepository) {
        return args -> {
            LearningSet ls1 = new LearningSet(true, "DE", "PL");
            LearningSet ls2 = new LearningSet(false, "DE", "EN");

            learningSetRepository.save(ls1);
            learningSetRepository.save(ls2);
        };
    }
}
