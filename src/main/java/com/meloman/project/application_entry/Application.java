package com.meloman.project.application_entry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *  Spring and derby initialisation
 */

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        String jdbcURL = "jdbc:derby:melomandb;create=true";
        try {
            Connection connection = DriverManager.getConnection(jdbcURL);
            System.out.println("New derby database created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
