package Classes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class RecommendationTest {

    @Test
    public void testSimpleRecommendation() {
        User user = new User("Ahmed", "pass", List.of("I001"));

        List<Movie> movies = List.of(
            new Movie("Inception", "I001", List.of("Sci-Fi", "Thriller")),
            new Movie("Interstellar", "I002", List.of("Sci-Fi", "Drama")),
            new Movie("La La Land", "LLL003", List.of("Romance", "Music"))
        );

        List<String> result = Recommendation.generateRecommendations(user, movies);
        assertEquals(List.of("Interstellar"), result);
    }

    @Test
    public void testNoMatchingGenres() {
        User user = new User("Sara", "123", List.of("I001"));

        List<Movie> movies = List.of(
            new Movie("Inception", "I001", List.of("Sci-Fi")),
            new Movie("La La Land", "LLL002", List.of("Romance")),
            new Movie("The Notebook", "TN003", List.of("Drama"))
        );

        List<String> result = Recommendation.generateRecommendations(user, movies);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUserLikedAllMovies() {
        User user = new User("Mona", "pass", List.of("I001", "I002"));

        List<Movie> movies = List.of(
            new Movie("Inception", "I001", List.of("Sci-Fi")),
            new Movie("Interstellar", "I002", List.of("Drama"))
        );

        List<String> result = Recommendation.generateRecommendations(user, movies);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMultipleRecommendations() {
        User user = new User("Ali", "pass", List.of("MA001"));

        List<Movie> movies = List.of(
            new Movie("Movie A", "MA001", List.of("Action")),
            new Movie("Movie B", "MB002", List.of("Action", "Thriller")),
            new Movie("Movie C", "MC003", List.of("Action")),
            new Movie("Movie D", "MD004", List.of("Drama"))
        );

        List<String> result = Recommendation.generateRecommendations(user, movies);
        assertEquals(List.of("Movie B", "Movie C"), result);
    }

    @Test
    public void testEmptyMovieList() {
        User user = new User("Ahmed", "123", List.of("M001"));

        List<Movie> movies = new ArrayList<>();

        List<String> result = Recommendation.generateRecommendations(user, movies);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUserLikedNoMovies() {
        User user = new User("Ahmed", "123", new ArrayList<>());

        List<Movie> movies = List.of(
            new Movie("Movie A", "MA001", List.of("Action")),
            new Movie("Movie B", "MB002", List.of("Drama"))
        );

        List<String> result = Recommendation.generateRecommendations(user, movies);
        assertTrue(result.isEmpty());
    }
}
