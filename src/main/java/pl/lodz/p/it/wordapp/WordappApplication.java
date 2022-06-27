package pl.lodz.p.it.wordapp;

import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.lodz.p.it.wordapp.model.LearningSet;
import pl.lodz.p.it.wordapp.repository.LearningSetRepository;

@SpringBootApplication
public class WordappApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordappApplication.class, args);
    }
}
