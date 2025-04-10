package moviesRecommendationSystemTest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;

import org.junit.jupiter.api.Test;

import moviesRecommendationSystem.RecommendationSystem;


public class RecommendationSystemTest {

    @Test
    public void testValidMovieTitle() {
        RecommendationSystem system = new RecommendationSystem();
        assertTrue(system.validateTitle("The Matrix"));
        assertFalse(system.validateTitle("the Matrix"));
    }

    @Test
    public void testMovieIdValidation() {
        RecommendationSystem system = new RecommendationSystem();
        assertTrue(system.validateMovieId("The Matrix", "TM123"));
        assertFalse(system.validateMovieId("The Matrix", "TMA123"));
    }

    @Test
    public void testValidUserName() {
        RecommendationSystem system = new RecommendationSystem();
        assertTrue(system.validateUserName("John Doe"));
        assertFalse(system.validateUserName(" john"));
    }

    @Test
    public void testUserIdValidation() {
        RecommendationSystem system = new RecommendationSystem();
        assertTrue(system.validateUserId("12345678A"));
        assertFalse(system.validateUserId("A12345678"));
        assertFalse(system.validateUserId("1234567890"));
    }

    @Test
    public void testRecommendationOutput() throws Exception {
        RecommendationSystem system = new RecommendationSystem();
        system.readMovies("test_movies.txt");
        system.readUsers("test_users.txt");
        system.generateRecommendations("test_output.txt");

        BufferedReader reader = new BufferedReader(new FileReader("test_output.txt"));
        String firstLine = reader.readLine();
        assertTrue(firstLine.contains("Ali"));
        String secondLine = reader.readLine();
        assertNotNull(secondLine);
        reader.close();
    }
}
