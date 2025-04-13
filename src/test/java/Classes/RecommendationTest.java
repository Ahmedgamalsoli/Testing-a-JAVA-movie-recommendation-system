package Classes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class RecommendationTest {

    @Test
    public void testSimpleRecommendation() {
        User user = new User("Ahmed", "pass", List.of("M1"));

        List<Movie> movies = List.of(
            new Movie("Inception", "M1", List.of("Sci-Fi", "Thriller")),
            new Movie("Interstellar", "M2", List.of("Sci-Fi", "Drama")),
            new Movie("La La Land", "M3", List.of("Romance", "Music"))
        );

        List<String> result = Recommendation.generateRecommendations(user, movies);
        assertEquals(List.of("Interstellar"), result);
    }

    @Test
    public void testNoMatchingGenres() {
        User user = new User("Sara", "123", List.of("M1"));

        List<Movie> movies = List.of(
            new Movie("Inception", "M1", List.of("Sci-Fi")),
            new Movie("La La Land", "M2", List.of("Romance")),
            new Movie("The Notebook", "M3", List.of("Drama"))
        );

        List<String> result = Recommendation.generateRecommendations(user, movies);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUserLikedAllMovies() {
        User user = new User("Mona", "pass", List.of("M1", "M2"));

        List<Movie> movies = List.of(
            new Movie("Inception", "M1", List.of("Sci-Fi")),
            new Movie("Interstellar", "M2", List.of("Drama"))
        );

        List<String> result = Recommendation.generateRecommendations(user, movies);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMultipleRecommendations() {
        User user = new User("Ali", "pass", List.of("M1"));

        List<Movie> movies = List.of(
            new Movie("Movie A", "M1", List.of("Action")),
            new Movie("Movie B", "M2", List.of("Action", "Thriller")),
            new Movie("Movie C", "M3", List.of("Action")),
            new Movie("Movie D", "M4", List.of("Drama"))
        );

        List<String> result = Recommendation.generateRecommendations(user, movies);
        assertEquals(List.of("Movie B", "Movie C"), result);
    }

    @Test
    public void testEmptyMovieList() {
        User user = new User("Ahmed", "123", List.of("M1"));

        List<Movie> movies = new ArrayList<>();

        List<String> result = Recommendation.generateRecommendations(user, movies);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUserLikedNoMovies() {
        User user = new User("Ahmed", "123", new ArrayList<>());

        List<Movie> movies = List.of(
            new Movie("Movie A", "M1", List.of("Action")),
            new Movie("Movie B", "M2", List.of("Drama"))
        );

        List<String> result = Recommendation.generateRecommendations(user, movies);
        assertTrue(result.isEmpty());
    }
}
