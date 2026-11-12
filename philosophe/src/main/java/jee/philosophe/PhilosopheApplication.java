package jee.philosophe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PhilosopheApplication {

    public static void main(String[] args) {

        SpringApplication.run(PhilosopheApplication.class, args);
    }

}
