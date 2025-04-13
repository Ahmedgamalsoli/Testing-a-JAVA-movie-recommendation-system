package Classes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;


import org.mockito.MockedStatic;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;
class RecommendationFileWriterTest {


@Test
public void testGenerateOutput() throws FileNotFoundException, IOException {
    try (MockedStatic<Recommendation> mockedStatic = mockStatic(Recommendation.class)) {

        // Setup data
        List<User> users = new ArrayList<>();
        users.add(new User("Hassan Ali", "12345678X", Arrays.asList("TSR001", "TDK003")));

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("The Shawshank Redemption", "TSR001", Arrays.asList("Drama")));
        movies.add(new Movie("The Godfather", "TG002", Arrays.asList("Crime", "Drama")));
        movies.add(new Movie("The Dark Knight", "TDK003", Arrays.asList("Action", "Crime", "Drama")));

        mockedStatic.when(() -> Recommendation.generateRecommendations(users.get(0), movies))
                    .thenReturn(Arrays.asList("The Godfather"));

        RecommendationFileWriter writer = new RecommendationFileWriter();
        writer.generateOutput(users, movies);

        File file = new File("recommendations.txt");
        assertTrue(file.exists());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            assertTrue(reader.readLine().contains("Hassan Ali"));
            assertTrue(reader.readLine().contains("The Godfather"));
        }
    }
}

    private final String outputFile = "recommendations.txt";
    private RecommendationFileWriter writer;

    @BeforeEach
    void setUp() {
        writer = new RecommendationFileWriter();
        // Clean up the file before each test
        File file = new File(outputFile);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testWriteError_createsFileWithMessage() {
        String errorMessage = "Test error occurred.";

        writer.writeError(errorMessage);

        File file = new File(outputFile);
        assertTrue(file.exists(), "recommendations.txt should be created");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String content = reader.readLine();
            assertEquals(errorMessage, content, "File content should match the error message");
        } catch (IOException e) {
            fail("Failed to read recommendations.txt: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        // Clean up the file after each test
        File file = new File(outputFile);
        if (file.exists()) {
            file.delete();
        }
    }
}
