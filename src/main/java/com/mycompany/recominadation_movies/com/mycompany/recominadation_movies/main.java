package com.mycompany.recominadation_movies;

import java.util.List;

import Classes.MovieValidator;
import Classes.MovieFileHandler;
import Classes.Movie;
import Classes.InputException;
import Classes.User;
import Classes.UserFileHandler;
import Classes.UserValidator;
import Classes.RecommendationFileWriter;

public class main{
    
    public static void main(String[] args) throws InputException {
        RecommendationFileWriter Recommendation = new RecommendationFileWriter();
        try {
            List<String[]> Movie_rawData = MovieFileHandler.readMovieDataFromFile();        // Step 1
            MovieValidator Movie_validator = new MovieValidator(Movie_rawData);             // Step 2
            List<Movie> movies = Movie_validator.validateMovieData();                       // Step 3

            List<String[]> User_rawData = UserFileHandler.readUserDataFromFile();           // Step 1
            UserValidator User_validator = new UserValidator(User_rawData);                 // Step 2
            List<User> users = User_validator.validateUserData();                           // Step 3
            
            Recommendation.generateOutput(users, movies);

        } catch (InputException e) {
            Recommendation.writeError(e.getMessage());

        }
    }    
}

