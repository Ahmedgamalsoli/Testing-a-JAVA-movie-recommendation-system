/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */






//البلبل





package Classes;

import java.util.*;

public class Recommendation {
    public static List<String> generateRecommendations(User user, List<Movie> movies) {
        Set<String> likedGenres = new HashSet<>();
        Set<String> likedMovieIds = new HashSet<>(user.getLikedMovieIds());
        
        // Collect genres from liked movies
        for (String movieId : user.getLikedMovieIds()) {
            for (Movie movie : movies) {
                if (movie.getMovieId().equals(movieId)) {
                    likedGenres.addAll(movie.getGenres());
                }
            }
        }
        
        // Find recommendations
        List<String> recommendations = new ArrayList<>();
        for (Movie movie : movies) {
            if (!likedMovieIds.contains(movie.getMovieId())) {
                for (String genre : movie.getGenres()) {
                    if (likedGenres.contains(genre)) {
                        recommendations.add(movie.getTitle());
                        break;
                    }
                }
            }
        }
        
        return recommendations;
    }
}
