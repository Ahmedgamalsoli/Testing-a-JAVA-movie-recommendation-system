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

    // Getters
    public String getTitle() { return title; }
    public String getMovieId() { return movieId; }
    public List<String> getGenres() { return genres; }
}
