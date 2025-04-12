/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Ahmed Gamal
 */
public class MovieValidator {
    private List<String[]> rawData;
    
    public MovieValidator(List<String[]> rawData) {
        this.rawData = rawData;
    }

    public List<Movie> validateMovieData() throws InputException {
        List<Movie> movies = new ArrayList<>();
        int idx = 0;
        while (idx<rawData.size()) {

            String[] movieEntry = rawData.get(idx); // Get the array (String[2])
            String titleLine = movieEntry[0];
            String genresLine = movieEntry[1];
            idx++;
            String[] titleParts = titleLine.split(",");
            if (titleParts.length != 2) {
                throw new InputException(String.format("Invalid First Line Data in Movie (%d) ", idx));
            }

            String title = titleParts[0].trim();
            String movieId = titleParts[1].trim();

            if (!isValidTitle(title)) {
                throw new InputException(String.format("ERROR: Movie Title (%s) is wrong", title));
            }

            if (!isValidMovieId(title, movieId)) {
                String expected = getTitleInitials(title);
                String actual = movieId.substring(0, movieId.length()-3);
                if (!actual.equals(expected)) {
                    throw new InputException(String.format("ERROR: Movie Id letters (%s) are wrong", movieId));
                } else {
                    throw new InputException(String.format("ERROR: Movie Id numbers (%s) aren't unique", movieId));
                }
            }
            //we make all genres small letters
            List<String> genres = Arrays.asList(genresLine.split(","));
            for (int i = 0; i < genres.size(); i++) {
                genres.set(i, genres.get(i).trim().toLowerCase());
            }

            movies.add(new Movie(title, movieId, genres));
            
        }
        //Here returns movies which will be used by generateOutput Function
        return movies;
    }

    private boolean isValidTitle(String title) {
        return title.matches("^([A-Z][a-z]*)(\\s+[A-Z][a-z]*)*$");
    }

    private boolean isValidMovieId(String title, String movieId) {
        // Split movie ID into letters and numbers
        int splitIndex = movieId.length() - 3;
        if (splitIndex <= 0) return false;
        
        String letters = movieId.substring(0, splitIndex);
        String numbers = movieId.substring(splitIndex);
        
        // Validate numbers
        if (!numbers.matches("\\d{3}")) return false;
        // Validate letters match title initials
        return letters.equals(getTitleInitials(title));
    }

    private String getTitleInitials(String title) {
        StringBuilder initials = new StringBuilder();
        for (String word : title.split(" ")) {
            if (!word.isEmpty()) {
                initials.append(word.charAt(0));
            }
        }
        return initials.toString();
    }

}
