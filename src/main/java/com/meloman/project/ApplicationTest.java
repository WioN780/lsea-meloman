package com.meloman.project;

import com.meloman.project.database_model.Artist;
import com.meloman.project.repositories.ArtistRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *  Spring and derby initialisation
 */

@SpringBootApplication(scanBasePackages = "com.meloman.project")
public class ApplicationTest {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApplicationTest.class, args);

        ArtistRepository artistRepository = context.getBean(ArtistRepository.class);

        Artist artist1 = new Artist("A1", "Artist One", "Real Name One", "BLABLA", "BLABLA");
        Artist artist2 = new Artist("A2", "Artist Two", "Real Name Two", "BLABLA", "BLABLA");

        artistRepository.save(artist1);
        artistRepository.save(artist2);
    }
}
