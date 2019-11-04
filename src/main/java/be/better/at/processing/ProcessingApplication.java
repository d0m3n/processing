package be.better.at.processing;

import be.better.at.processing.config.PersistenceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@Import(value = {
        PersistenceConfig.class
})
public class ProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessingApplication.class, args);
    }

}
