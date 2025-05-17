package Classes;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BottomUpIntegrationTest {

    private static final String MOVIES_FILE = "movies.txt";
    private static final String USERS_FILE  = "users.txt";
    private static final String OUTPUT_FILE = "recommendations.txt";

     //1) MovieFileHandler + MovieValidator
    @Test
    void testMovieFileHandlerAndValidator() throws Exception {
        // Prepare  movies.txt
        String testFile = "movies.txt";
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("Inception,I123\n");
            writer.write("Sci-Fi, Thriller\n");

        }

        // Read and validate
        List<String[]> raw = MovieFileHandler.readMovieDataFromFile();
        MovieValidator mv = new MovieValidator(raw);
        List<Movie> moviesList = mv.validateMovieData();

        assertEquals(1, moviesList.size());
        Movie m = moviesList.get(0);
        assertEquals("Inception", m.getTitle());
        assertEquals("I123", m.getMovieId());
        assertTrue(m.getGenres().contains("sci-fi"));
    }


     //2) UserFileHandler + UserValidator

    @Test
    void testUserFileHandlerAndValidator() throws Exception {
        // Prepare users.txt
        String testFile = "users.txt";
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("Alice Smith,98765432A\n");
            writer.write("I123\n");
        }

        List<String[]> rawU = UserFileHandler.readUserDataFromFile();
        UserValidator uv = new UserValidator(rawU);
        List<User> usersList = uv.validateUserData();

        assertEquals(1, usersList.size());
        User u = usersList.get(0);
        assertEquals("Alice Smith", u.getName());
        assertEquals("98765432A", u.getUserId());
        assertEquals(List.of("I123"), u.getLikedMovieIds());
    }


    //  3) MovieValidator + UserValidator + Recommendation logic

    @Test
    void testRecommendationsIntegration() throws Exception {
        // 1) Build rawData
        List<String[]> rawMovies = List.of(
                new String[]{"Inception,I123", "Sci-Fi,Thriller"},
                new String[]{"Interstellar,I456", "Sci-Fi"}
        );

        List<String[]> rawUsers = List.of(
                new String[]{ "Bob,11122233C", "I123" },
                new String[]{ "Smith,98765432A", "I123" }
        );

        List<Movie> movies   = new MovieValidator(rawMovies).validateMovieData();
        List<User>  userList = new UserValidator(rawUsers).validateUserData();

        List<String> recs = Recommendation.generateRecommendations(userList.get(0), movies);

        // 4) Assert on recs

        assertEquals(1, recs.size());
        assertEquals("Interstellar", recs.get(0));
    }


    //  4) Full end-to-end     File input → Validation → Recommendation → Output

    @Test
    void testFullPipelineWritesRecommendations() throws Exception {
        // movies.txt
        String testFile = "movies.txt";
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("Inception,I123\n");
            writer.write("Sci-Fi\n");
            writer.write("Interstellar,I456\n");
            writer.write("Sci-Fi\n");
        }

        // users.txt
        File testFile2 = new File("users.txt");

        try (FileWriter writer = new FileWriter(testFile2)) {
            writer.write("Carol,33344455D\n");
            writer.write("I123\n");
        }


        // run main logic
        RecommendationFileWriter Recommendation = new RecommendationFileWriter();
        List<Movie> mvList = new MovieValidator(MovieFileHandler.readMovieDataFromFile()).validateMovieData();
        List<User>  usList = new UserValidator(UserFileHandler.readUserDataFromFile()).validateUserData();
        Recommendation.generateOutput(usList, mvList);

        // verify recommendations.txt
        File file = new File("recommendations.txt");
        assertTrue(file.exists());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            assertTrue(reader.readLine().contains("Carol, 33344455D"));
            assertTrue(reader.readLine().contains("Interstellar"));
        }

    }
    
     // 5) Missing second line in users.txt should write an error into recommendations.txt

    @Test
    void testMissingSecondLineInUsersFileWritesError() throws Exception {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            pw.println("Dave,44455566E");
        }
        new File(MOVIES_FILE).createNewFile();

        RecommendationFileWriter writer = new RecommendationFileWriter();

        List<Movie> mvList = List.of();
        List<User>  usList = List.of();
        assertDoesNotThrow(() -> {

            try {
                MovieFileHandler.readMovieDataFromFile();
                UserFileHandler.readUserDataFromFile();
            } catch (InputException e) {
                writer.writeError(e.getMessage());
            }
        });

        File out = new File(OUTPUT_FILE);
        assertTrue(out.exists());
        String errorLine = new BufferedReader(new FileReader(out)).readLine();
        assertTrue(errorLine.contains("Invalid users.txt format"), "Should write users format error");
    }


     //6) Invalid movie entry mid-file should write error.

    @Test
    void testMalformedMovieEntryHaltsPipeline() throws Exception {
        try (PrintWriter pw = new PrintWriter(new FileWriter(MOVIES_FILE))) {
            pw.println("Good Movie,GM001");
            pw.println("Drama");
            pw.println("Invalid Entry WithoutComma");
            pw.println("Action");
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            pw.println("Eve,77788899F");
            pw.println("GM001");
        }

        RecommendationFileWriter writer = new RecommendationFileWriter();

        InputException ex = assertThrows(InputException.class, () -> {
            List<String[]> raw = MovieFileHandler.readMovieDataFromFile();
            new MovieValidator(raw).validateMovieData();
        });


        writer.writeError(ex.getMessage());
        File out = new File(OUTPUT_FILE);
        assertTrue(out.exists());
        String msg = new BufferedReader(new FileReader(out)).readLine();
        assertTrue(msg.contains("Invalid First Line Data"), "Error message should reflect malformed line");
    }


     //7) Multiple users & movies
    @Test
    void testMultipleUsersMultipleMovies() throws Exception {
        // movies.txt
        try (PrintWriter pw = new PrintWriter(new FileWriter(MOVIES_FILE))) {
            pw.println("Amovie,A100");
            pw.println("GenreA");
            pw.println("Bmovie,B200");
            pw.println("GenreA");
        }
        // users.txt
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            pw.println("Alice,11111111A");
            pw.println("A100");
            pw.println("Bob,22222222B");
            pw.println("B200");
        }

        RecommendationFileWriter writer = new RecommendationFileWriter();
        List<Movie> mvList = new MovieValidator(MovieFileHandler.readMovieDataFromFile()).validateMovieData();
        List<User>  usList = new UserValidator(UserFileHandler.readUserDataFromFile()).validateUserData();
        writer.generateOutput(usList, mvList);

        List<String> lines;
        try (BufferedReader br = new BufferedReader(new FileReader(OUTPUT_FILE))) {
            lines = br.lines().toList();
        }

        assertEquals(4, lines.size());
        assertEquals("Alice, 11111111A", lines.get(0));
        assertEquals("Bmovie", lines.get(1));
        assertEquals("Bob, 22222222B", lines.get(2));
        assertEquals("Amovie", lines.get(3));
    }


    //  8) User likes a non-existent movie: no recommendations

    @Test
    void testUserLikesNonExistentMovie() throws Exception {
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(MOVIES_FILE))) {
            pw.println("Movie One,MO111");
            pw.println("X");
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            pw.println("Charlie,33333333C");
            pw.println("MO211");
        }

        RecommendationFileWriter writer = new RecommendationFileWriter();
        List<Movie> mvList = new MovieValidator(MovieFileHandler.readMovieDataFromFile()).validateMovieData();
        List<User>  usList = new UserValidator(UserFileHandler.readUserDataFromFile()).validateUserData();
        writer.generateOutput(usList, mvList);

        List<String> lines;
        try (BufferedReader br = new BufferedReader(new FileReader(OUTPUT_FILE))) {
            lines = br.lines().toList();
        }
        // User line + empty recommendation line
        assertEquals(2, lines.size());
        assertEquals("Charlie, 33333333C", lines.get(0));
        assertTrue(lines.get(1).isEmpty(), "No recommendations should be listed");
    }

    
     //9) success then second user error
     
    @Test
    void testPartialThenErrorStopsPipeline() throws Exception {
        // movies.txt
        try (PrintWriter pw = new PrintWriter(new FileWriter(MOVIES_FILE))) {
            pw.println("Good, G001");
            pw.println("Y");
        }
        // users.txt first, second user Error
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            pw.println("First,11111111A");
            pw.println("G001");
            pw.println("BadUserLineWithoutComma");
            pw.println("G001");
        }

        RecommendationFileWriter writer = new RecommendationFileWriter();
        
        InputException ex = assertThrows(InputException.class, () -> {
            List<Movie> mvList = new MovieValidator(MovieFileHandler.readMovieDataFromFile()).validateMovieData();
            new UserValidator(UserFileHandler.readUserDataFromFile()).validateUserData();
        });

       
        writer.writeError(ex.getMessage());
        String msg = new BufferedReader(new FileReader(OUTPUT_FILE)).readLine();
        assertTrue(msg.contains("Invalid First Line Data"), "Should report the bad user line");
    }

}
