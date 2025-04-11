/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Movie {
    private final String title;
    private final String movieId;
    private final List<String> genres;

    public Movie(String title, String movieId, List<String> genres) {
        this.title = title;
        this.movieId = movieId;
        this.genres = genres;
    }

    public static boolean isValidTitle(String title) {
        return title.matches("^([A-Z][a-z]*)(\\s+[A-Z][a-z]*)*$");
    }

    public static boolean isValidMovieId(String title, String movieId) {
        // Split movie ID into letters and numbers
        int splitIndex = movieId.length() - 3;
        if (splitIndex <= 0) return false;
        
        String letters = movieId.substring(0, splitIndex);
        String numbers = movieId.substring(splitIndex);
        
        // Validate numbers
        if (!numbers.matches("\\d{3}")) return false;
//        Set<Character> uniqueDigits = new HashSet<>();
//        for (char c : numbers.toCharArray()) {
//            if (!uniqueDigits.add(c)) return false;
//        }
        
        // Validate letters match title initials
        return letters.equals(getTitleInitials(title));
    }

    public static String getTitleInitials(String title) {
        StringBuilder initials = new StringBuilder();
        for (String word : title.split(" ")) {
            if (!word.isEmpty()) {
                initials.append(word.charAt(0));
            }
        }
        return initials.toString();
    }
    public String getFullInfo() {
        return String.format(
            "Title: %s\nID: %s\nGenres: %s",
            title,
            movieId,
            String.join(", ", genres)
        );
    }

    // Getters
    public String getTitle() { return title; }
    public String getMovieId() { return movieId; }
    public List<String> getGenres() { return genres; }
}
