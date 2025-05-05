package com.meloman.project.utils_database;

public class test_loader {
    public static void main(String[] args) {
        String[] loaderArgs = {
                "meloman-derby",
                "src/main/resources/DiscoGSdata.test.csv"
        };
        DiscoGSLoader_database.main(loaderArgs);
    }
}