/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Classes;

import java.util.List;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;
import java.util.*;
import org.mockito.Mockito;

/**
 *
 * @author aliom
 */
public class MovieValidatorTest {
    
     // Helper to invoke the private method using reflection
    private boolean invokeIsValidTitle(String title) throws Exception {
        MovieValidator validator = new MovieValidator(null); // rawData is not used
        Method method = MovieValidator.class.getDeclaredMethod("isValidTitle", String.class);
        method.setAccessible(true);
        return (boolean) method.invoke(validator, title);
    }
    
    private boolean invokeIsValidMovieId(String title, String movieId) {
        MovieValidator validator = new MovieValidator(null); // rawData not needed
        return validator.isValidMovieId(title, movieId);
    }
    
    private String invokeGetTitleInitials(String title) throws Exception {
        MovieValidator validator = new MovieValidator(null);
        Method method = MovieValidator.class.getDeclaredMethod("getTitleInitials", String.class);
        method.setAccessible(true);
        return (String) method.invoke(validator, title);
    }

    // ✅ Valid titles
    @Test
    public void testValidTitle_SingleWord() throws Exception {
        assertTrue(invokeIsValidTitle("Inception"));
    }

    @Test
    public void testValidTitle_MultipleWords() throws Exception {
        assertTrue(invokeIsValidTitle("The Godfather"));
        assertTrue(invokeIsValidTitle("Finding Nemo"));
        assertTrue(invokeIsValidTitle("A Beautiful Mind"));
    }
    
    @Test
    public void testValidTitle_UppercaseInside() throws Exception {
        assertFalse(invokeIsValidTitle("A BeAUtiful MInd"));
    }
    
    @Test
    public void testValidTitle_ExtraSpaces() throws Exception {
        assertTrue(invokeIsValidTitle("The  Matrix")); // double space
    }
    
    // ❌ Invalid titles
    @Test
    public void testInvalidTitle_LowercaseFirstLetter() throws Exception {
        assertFalse(invokeIsValidTitle("inception"));
        assertFalse(invokeIsValidTitle("the Godfather"));
    }

    @Test
    public void testInvalidTitle_MixedCase() throws Exception {
        assertFalse(invokeIsValidTitle("fInding Nemo"));
        assertFalse(invokeIsValidTitle("A beautiful Mind"));
    }

    @Test
    public void testInvalidTitle_SpecialCharacters() throws Exception {
        assertFalse(invokeIsValidTitle("Spider-Man")); // Hyphen not allowed
        assertFalse(invokeIsValidTitle("Star_Wars"));  // Underscore not allowed
    }

    @Test
    public void testInvalidTitle_NumbersInside() throws Exception {
        assertFalse(invokeIsValidTitle("Se7en"));
    }
    
    @Test
    public void testInvalidTitle_NumbersOutside() throws Exception {
        assertFalse(invokeIsValidTitle("Fast 5"));
    }
    
    @Test
    public void testInvalidTitle_OneSpace() throws Exception {
        assertFalse(invokeIsValidTitle(""));
    }

    @Test
    public void testInvalidTitle_Empty() throws Exception {
        assertFalse(invokeIsValidTitle(""));
    }
    
    @Test
    public void testValidMovieId_Single() {
        try (MockedStatic<MovieValidator> mockedStatic = mockStatic(MovieValidator.class)) {
            // Mock getTitleInitials("Inception") to return "I"
            mockedStatic.when(() -> MovieValidator.getTitleInitials("Inception")).thenReturn("I");

            // Mock static method isValidMovieId to call real method (partial mock)
            mockedStatic.when(() -> MovieValidator.isValidMovieId("Inception", "I123"))
                        .thenCallRealMethod();

            // Call the actual method, which internally uses mocked getTitleInitials
            assertTrue(MovieValidator.isValidMovieId("Inception", "I123"));
        }
    }

    @Test
    public void testValidMovieId_Multiple() {
        try (MockedStatic<MovieValidator> mockedStatic = mockStatic(MovieValidator.class)) {
            mockedStatic.when(() -> MovieValidator.getTitleInitials("A Beautiful Mind")).thenReturn("ABM");
            mockedStatic.when(() -> MovieValidator.isValidMovieId("A Beautiful Mind", "ABM789"))
            .thenCallRealMethod();
            assertTrue(MovieValidator.isValidMovieId("A Beautiful Mind", "ABM789"));
        }
    }

    @Test
    public void testValidMovieId_CapsInWords() {
        try (MockedStatic<MovieValidator> mockedStatic = mockStatic(MovieValidator.class)) {
            mockedStatic.when(() -> MovieValidator.getTitleInitials("X MEn FIrRst CLAss")).thenReturn("XMFC");
            mockedStatic.when(() -> MovieValidator.isValidMovieId("X MEn FIrRst CLAss", "XMFC321"))
                        .thenCallRealMethod();
            assertTrue(MovieValidator.isValidMovieId("X MEn FIrRst CLAss", "XMFC321"));
        }
    }

    @Test
    public void testInvalidMovieId_ExtraLetter() {
        try (MockedStatic<MovieValidator> mockedStatic = mockStatic(MovieValidator.class)) {
            mockedStatic.when(() -> MovieValidator.getTitleInitials("The Godfather")).thenReturn("TG");
            mockedStatic.when(() -> MovieValidator.isValidMovieId("The Godfather", "TGX123"))
                        .thenCallRealMethod();
            assertFalse(MovieValidator.isValidMovieId("The Godfather", "TGX123"));
        }
    }

    @Test
    public void testInvalidMovieId_MissingLetter() {
        try (MockedStatic<MovieValidator> mockedStatic = mockStatic(MovieValidator.class)) {
            mockedStatic.when(() -> MovieValidator.getTitleInitials("A Beautiful Mind")).thenReturn("ABM");
            mockedStatic.when(() -> MovieValidator.isValidMovieId("A Beautiful Mind", "AB123"))
                        .thenCallRealMethod();
            assertFalse(MovieValidator.isValidMovieId("A Beautiful Mind", "AB123"));
        }
    }

    @Test
    public void testInvalidMovieId_ExtraNumber() {
        try (MockedStatic<MovieValidator> mockedStatic = mockStatic(MovieValidator.class)) {
            mockedStatic.when(() -> MovieValidator.getTitleInitials("Finding Nemo")).thenReturn("FN");
            mockedStatic.when(() -> MovieValidator.isValidMovieId("Finding Nemo", "FN1234"))
                        .thenCallRealMethod();
            assertFalse(MovieValidator.isValidMovieId("Finding Nemo", "FN1234"));
        }
    }

    @Test
    public void testInvalidMovieId_MissingNumber() {
        try (MockedStatic<MovieValidator> mockedStatic = mockStatic(MovieValidator.class)) {
            mockedStatic.when(() -> MovieValidator.getTitleInitials("The Godfather")).thenReturn("TG");
            mockedStatic.when(() -> MovieValidator.isValidMovieId("The Godfather", "TG12"))
                        .thenCallRealMethod();
            assertFalse(MovieValidator.isValidMovieId("The Godfather", "TG12"));
        }
    }

    @Test
    public void testInvalidMovieId_CharacterInsteadOfFirstNumber() {
        try (MockedStatic<MovieValidator> mockedStatic = mockStatic(MovieValidator.class)) {
            mockedStatic.when(() -> MovieValidator.getTitleInitials("Finding Nemo")).thenReturn("FN");
            mockedStatic.when(() -> MovieValidator.isValidMovieId("Finding Nemo", "FNx23"))
                        .thenCallRealMethod();
            assertFalse(MovieValidator.isValidMovieId("Finding Nemo", "FNx23"));
        }
    }

    @Test
    public void testInvalidMovieId_CharacterInsteadOfMiddleNumber() {
        try (MockedStatic<MovieValidator> mockedStatic = mockStatic(MovieValidator.class)) {
            mockedStatic.when(() -> MovieValidator.getTitleInitials("Finding Nemo")).thenReturn("FN");
            mockedStatic.when(() -> MovieValidator.isValidMovieId("Finding Nemo", "FN1x3"))
                        .thenCallRealMethod();
            assertFalse(MovieValidator.isValidMovieId("Finding Nemo", "FN1x3"));
        }
    }

    @Test
    public void testInvalidMovieId_CharacterInsteadOfLastNumber() {
        try (MockedStatic<MovieValidator> mockedStatic = mockStatic(MovieValidator.class)) {
            mockedStatic.when(() -> MovieValidator.getTitleInitials("Finding Nemo")).thenReturn("FN");
            mockedStatic.when(() -> MovieValidator.isValidMovieId("Finding Nemo", "FN12x"))
                        .thenCallRealMethod();
            assertFalse(MovieValidator.isValidMovieId("Finding Nemo", "FN12x"));
        }
    }

    @Test
    public void testInvalidMovieId_Empty() {
        try (MockedStatic<MovieValidator> mockedStatic = mockStatic(MovieValidator.class)) {
            mockedStatic.when(() -> MovieValidator.getTitleInitials("")).thenReturn("");
            mockedStatic.when(() -> MovieValidator.getTitleInitials("Inception")).thenReturn("I");

            mockedStatic.when(() -> MovieValidator.isValidMovieId("", ""))
                        .thenCallRealMethod();
            mockedStatic.when(() -> MovieValidator.isValidMovieId("Inception", ""))
                        .thenCallRealMethod();

            assertFalse(MovieValidator.isValidMovieId("", ""));
            assertFalse(MovieValidator.isValidMovieId("Inception", ""));
        }
    }
    
    // ✅ Valid title initial cases
    @Test
    public void testGetTitleInitials_SingleWord() throws Exception {
        assertEquals("I", invokeGetTitleInitials("Inception"));
        assertEquals("U", invokeGetTitleInitials("Up"));
    }

    @Test
    public void testGetTitleInitials_MultipleWords() throws Exception {
        assertEquals("TG", invokeGetTitleInitials("The Godfather"));
        assertEquals("FN", invokeGetTitleInitials("Finding Nemo"));
        assertEquals("ABM", invokeGetTitleInitials("A Beautiful Mind"));
    }

    @Test
    public void testGetTitleInitials_WithExtraSpaces() throws Exception {
        assertEquals("TG", invokeGetTitleInitials("  The   Godfather  "));
        assertEquals("HPM", invokeGetTitleInitials("  Harry   Potter  Movie"));
    }

    @Test
    public void testGetTitleInitials_WithLowercaseWords() throws Exception {
        assertEquals("tG", invokeGetTitleInitials("the Godfather"));
        assertEquals("fb", invokeGetTitleInitials("frozen begins"));  // Just gets the first letters
    }

    // ❌ Edge cases
    @Test
    public void testGetTitleInitials_EmptyString() throws Exception {
        assertEquals("", invokeGetTitleInitials(""));
    }

    @Test
    public void testGetTitleInitials_OnlySpaces() throws Exception {
        assertEquals("", invokeGetTitleInitials("     "));
    }

    @Test
    public void testGetTitleInitials_SingleLettersWithSpaces() throws Exception {
        assertEquals("ABC", invokeGetTitleInitials("A B C"));
    }
    
    @Test
    public void testGetTitleInitials_NumbersInside() throws Exception {
        assertEquals("S", invokeGetTitleInitials("Star3Wars"));
    }

    @Test
    public void testGetTitleInitials_NumbersOutside() throws Exception {
        assertEquals("S3", invokeGetTitleInitials("SpiderMan 3"));
    }
    
    @Test
    public void testGetTitleInitials_SpecialCharactersInside() throws Exception {
        assertEquals("S", invokeGetTitleInitials("Spider-Man"));
    }
    
    @Test
    public void testGetTitleInitials_SpecialCharactersOutside() throws Exception {
        assertEquals("S_", invokeGetTitleInitials("Star _Wars"));
    }
    
    //More
    @Test
    void testValidMovie() throws InputException {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"The Dark Knight,TDK123", "Action,Drama"});

        try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) {
            mocked.when(() -> MovieValidator.isValidTitle("The Dark Knight")).thenReturn(true);
            mocked.when(() -> MovieValidator.isValidMovieId("The Dark Knight", "TDK123")).thenReturn(true);

            // Optional: mock getTitleInitials if needed
            mocked.when(() -> MovieValidator.getTitleInitials("The Dark Knight")).thenReturn("TDK");

            MovieValidator validator = new MovieValidator(rawData);
            List<Movie> movies = validator.validateMovieData();

            assertEquals(1, movies.size());
            Movie movie = movies.get(0);
            assertEquals("The Dark Knight", movie.getTitle());
            assertEquals("TDK123", movie.getMovieId());
            assertTrue(movie.getGenres().contains("action"));
            assertTrue(movie.getGenres().contains("drama"));
        }
    }

    @Test 
    void testAllDefPath_validateMovieData(){
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"The Godfather,TG002", "Crime,Drama"});
        try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) {
            mocked.when(() -> MovieValidator.isValidTitle("The Godfather")).thenReturn(true);
            mocked.when(() -> MovieValidator.isValidMovieId("The Godfather", "TG002")).thenReturn(true);
            mocked.when(() -> MovieValidator.getTitleInitials("The Godfather")).thenReturn("TG");

            MovieValidator validator = new MovieValidator(rawData);
            List<Movie> movies = validator.validateMovieData();

            assertEquals(1, movies.size());
            assertEquals("The Godfather", movies.get(0).getTitle());
            assertEquals("TG002", movies.get(0).getMovieId());
            assertTrue(movies.get(0).getGenres().contains("crime"));
            assertTrue(movies.get(0).getGenres().contains("drama"));
        }
    }
/*
    @Test  
    void testAllUsePath1(){ // 2 movies 
        List<String[]> rawData = new ArrayList<>(); 
        rawData.add(new String[]{"The Godfather,TG002", "Crime,Drama"}); 
        rawData.add(new String[]{"The Matrix,TM123", ""}); 
        try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) { 
            mocked.when(() -> MovieValidator.isValidTitle("The Godfather")).thenReturn(true); 
            mocked.when(() -> MovieValidator.isValidMovieId("The Godfather", "TG002")).thenReturn(true); 
            mocked.when(() -> MovieValidator.getTitleInitials("The Godfather")).thenReturn("TG"); 
            mocked.when(() -> MovieValidator.isValidTitle("The Matrix")).thenReturn(true); 
            mocked.when(() -> MovieValidator.isValidMovieId("The Matrix", "TM123")).thenReturn(true); 
            mocked.when(() -> MovieValidator.getTitleInitials("The Matrix")).thenReturn("TM"); 
            
            MovieValidator validator = new MovieValidator(rawData); 
            List<Movie> movies = validator.validateMovieData(); 
            
            assertEquals(2, movies.size()); 
            assertEquals("The Godfather", movies.get(0).getTitle()); 
            assertEquals("TG002", movies.get(0).getMovieId()); 
            assertTrue(movies.get(0).getGenres().contains("crime")); 
            assertTrue(movies.get(0).getGenres().contains("drama")); 
            assertEquals("The Matrix", movies.get(1).getTitle()); 
            assertEquals("TM123", movies.get(1).getMovieId()); 
        } 
    } 

    @Test  
    void testAllUsePath2 (){ //1 movie no genre 
        List<String[]> rawData = new ArrayList<>(); 
        rawData.add(new String[]{"The Godfather,TG002", ""}); 
         try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) { 
            mocked.when(() -> MovieValidator.isValidTitle("The Godfather")).thenReturn(true); 
            mocked.when(() -> MovieValidator.isValidMovieId("The Godfather", "TG002")).thenReturn(true); 
            mocked.when(() -> MovieValidator.getTitleInitials("The Godfather")).thenReturn("TG"); 
    
            MovieValidator validator = new MovieValidator(rawData); 
            List<Movie> movies = validator.validateMovieData(); 
             
            assertEquals(1, movies.size()); 
            assertEquals("The Godfather", movies.get(0).getTitle()); 
            assertEquals("TG002", movies.get(0).getMovieId()); 
        } 
    } 

    @Test  
    void testAllUsePath3 (){ //Invalid Title 
        List<String[]> rawData = new ArrayList<>(); 
        rawData.add(new String[]{"123 Godfather,TG002", ""}); 
         try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) { 
            mocked.when(() -> MovieValidator.isValidTitle("123 Godfather")).thenReturn(false); 
            mocked.when(() -> MovieValidator.isValidMovieId("The Godfather", "TG002")).thenReturn(true); 
            mocked.when(() -> MovieValidator.getTitleInitials("The Godfather")).thenReturn("TG"); 
             
            MovieValidator validator = new MovieValidator(rawData); 
    
            assertThrows(InputException.class, validator::validateMovieData); 
        } 
    } 

    @Test  
    void testAllUsePath4 (){ //Invalid Movie ID 
        List<String[]> rawData = new ArrayList<>(); 
        rawData.add(new String[]{"The Godfather,XYZ", ""}); 
         try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) { 
            mocked.when(() -> MovieValidator.isValidTitle("The Godfather")).thenReturn(true); 
            mocked.when(() -> MovieValidator.isValidMovieId("The Godfather", "XYZ")).thenReturn(false); 
            mocked.when(() -> MovieValidator.getTitleInitials("The Godfather")).thenReturn("TG"); 
    
            MovieValidator validator = new MovieValidator(rawData); 
    
            assertThrows(InputException.class, validator::validateMovieData); 
        } 
    } 

    @Test  
    void testAllUsePath5 (){ //1 movie with genre 
        List<String[]> rawData = new ArrayList<>(); 
        rawData.add(new String[]{"123 Godfather,TG002", "Crime"}); 
         try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) { 
            mocked.when(() -> MovieValidator.isValidTitle("The Godfather")).thenReturn(true); 
            mocked.when(() -> MovieValidator.isValidMovieId("The Godfather", "TG002")).thenReturn(true); 
            mocked.when(() -> MovieValidator.getTitleInitials("The Godfather")).thenReturn("TG"); 
    
            MovieValidator validator = new MovieValidator(rawData); 
            List<Movie> movies = validator.validateMovieData(); 
    
            assertEquals(1, movies.size()); 
            assertEquals("The Godfather", movies.get(0).getTitle()); 
            assertEquals("TG002", movies.get(0).getMovieId()); 
            assertTrue(movies.get(0).getGenres().contains("crime")); 
        } 
    } 
*/
        @Test
    void testAllDefUse1(){ //Valid 1 movie scenario
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"The Dark Knight,TDK123", "Action,Drama"});

        try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) {
            mocked.when(() -> MovieValidator.isValidTitle("The Dark Knight")).thenReturn(true);
            mocked.when(() -> MovieValidator.isValidMovieId("The Dark Knight", "TDK123")).thenReturn(true);
            mocked.when(() -> MovieValidator.getTitleInitials("The Dark Knight")).thenReturn("TDK");

            MovieValidator validator = new MovieValidator(rawData);
            List<Movie> movies = validator.validateMovieData();

            assertEquals(1, movies.size());
            Movie movie = movies.get(0);
            assertEquals("The Dark Knight", movie.getTitle());
            assertEquals("TDK123", movie.getMovieId());
            assertTrue(movie.getGenres().contains("action"));
            assertTrue(movie.getGenres().contains("drama"));
        }
    }

    @Test
    void testAllDefUse2(){ //Valid 2 movies scenario
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"The Dark Knight,TDK123", "Action"});
        rawData.add(new String[]{"Inception,INC456", "Sci-Fi"});
        
        try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) {
            mocked.when(() -> MovieValidator.isValidTitle("The Dark Knight")).thenReturn(true);
            mocked.when(() -> MovieValidator.isValidMovieId("The Dark Knight", "TDK123")).thenReturn(true);
            mocked.when(() -> MovieValidator.getTitleInitials("The Dark Knight")).thenReturn("TDK");

            mocked.when(() -> MovieValidator.isValidTitle("Inception")).thenReturn(true);
            mocked.when(() -> MovieValidator.isValidMovieId("Inception", "INC456")).thenReturn(true);
            mocked.when(() -> MovieValidator.getTitleInitials("Inception")).thenReturn("INC");

            MovieValidator validator = new MovieValidator(rawData);
            List<Movie> movies = validator.validateMovieData();

            assertEquals(2, movies.size());

            Movie movie1 = movies.get(0);
            assertEquals("The Dark Knight", movie1.getTitle());
            assertEquals("TDK123", movie1.getMovieId());
            assertTrue(movie1.getGenres().contains("action"));

            Movie movie2 = movies.get(1);
            assertEquals("Inception", movie2.getTitle());
            assertEquals("INC456", movie2.getMovieId());
            assertTrue(movie2.getGenres().contains("sci-fi"));
        }
    }


    @Test
    void testAllDefUse3(){ //title parts length !=2
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"The Dark Knight,TDK123", "Action", "random"});
        
        try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) {
            mocked.when(() -> MovieValidator.isValidTitle("The Dark Knight")).thenReturn(true);
            mocked.when(() -> MovieValidator.isValidMovieId("The Dark Knight", "TDK123")).thenReturn(true);
            mocked.when(() -> MovieValidator.getTitleInitials("The Dark Knight")).thenReturn("TDK");

            MovieValidator validator = new MovieValidator(rawData);
            assertThrows(InputException.class, validator::validateMovieData);
        }
    }

    @Test
    void testAllDefUse4(){ //None valid title
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"The dark Knight,TDK123", "Action"});
        
        try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) {
            mocked.when(() -> MovieValidator.isValidTitle("The Dark Knight")).thenReturn(true);
            mocked.when(() -> MovieValidator.isValidMovieId("The Dark Knight", "TDK123")).thenReturn(true);
            mocked.when(() -> MovieValidator.getTitleInitials("The Dark Knight")).thenReturn("TDK");

            MovieValidator validator = new MovieValidator(rawData);
            assertThrows(InputException.class, validator::validateMovieData);
        }
    }


    @Test
    void testAllDefUse5(){ //Invalid movie ID
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"The dark Knight,TdK123", "Action"});
        
        try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) {
            mocked.when(() -> MovieValidator.isValidTitle("The dark Knight")).thenReturn(true);
            mocked.when(() -> MovieValidator.isValidMovieId("The dark Knight", "TdK123")).thenReturn(false); // ID is invalid

            MovieValidator validator = new MovieValidator(rawData);
            assertThrows(InputException.class, validator::validateMovieData);
        }
    }
    @Test
    void testInvalidTitleFormat() {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"the dark knight,TDK123", "Action,Drama"});

        try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) {
            mocked.when(() -> MovieValidator.isValidTitle("the dark knight")).thenReturn(false);

            MovieValidator validator = new MovieValidator(rawData);
            InputException ex = assertThrows(InputException.class, validator::validateMovieData);
            assertTrue(ex.getMessage().contains("Movie Title"));
        }
    }


    @Test
    void testInvalidMovieIdPrefix() {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"The Dark Knight,WRG999", "Action,Drama"});

        try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) {
            mocked.when(() -> MovieValidator.isValidTitle("The Dark Knight")).thenReturn(true);
            mocked.when(() -> MovieValidator.isValidMovieId("The Dark Knight", "WRG999")).thenReturn(false);
            mocked.when(() -> MovieValidator.getTitleInitials("The Dark Knight")).thenReturn("TDK");

            MovieValidator validator = new MovieValidator(rawData);
            InputException ex = assertThrows(InputException.class, validator::validateMovieData);
            assertEquals("ERROR: Movie Id letters (WRG999) are wrong", ex.getMessage());
        }
    }


    @Test
    void testInvalidMovieIdNumberLength() {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"The Dark Knight,TDK12", "Action,Drama"});

        try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) {
            mocked.when(() -> MovieValidator.isValidTitle("The Dark Knight")).thenReturn(true);
            mocked.when(() -> MovieValidator.getTitleInitials("The Dark Knight")).thenReturn("TDK");
            mocked.when(() -> MovieValidator.isValidMovieId("The Dark Knight", "TDK12")).thenReturn(false);

            MovieValidator validator = new MovieValidator(rawData);
            InputException ex = assertThrows(InputException.class, validator::validateMovieData);
            assertTrue(ex.getMessage().contains("aren't 3"));
        }
    }


    @Test
    void testInvalidGenresFormat() {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"The Dark Knight,TDK123", "Action Drama"});

        MovieValidator validator = new MovieValidator(rawData);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, validator::validateMovieData);
        assertTrue(ex.getMessage().contains("Genres must be comma-separated"));
    }


    @Test
    void testEmptyGenreLine() throws InputException {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"The Dark Knight,TDK123", ""});

        try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) {
            mocked.when(() -> MovieValidator.isValidTitle("The Dark Knight")).thenReturn(true);
            mocked.when(() -> MovieValidator.getTitleInitials("The Dark Knight")).thenReturn("TDK");
            mocked.when(() -> MovieValidator.isValidMovieId("The Dark Knight", "TDK123")).thenReturn(true);

            MovieValidator validator = new MovieValidator(rawData);
            List<Movie> movies = validator.validateMovieData();

            assertEquals(1, movies.size());
            assertTrue(movies.get(0).getGenres().get(0).isEmpty());
        }
    }


    @Test
    void testMultipleValidMovies() throws InputException {
        List<String[]> rawData = List.of(
                new String[]{"The Dark Knight,TDK123", "Action,Drama"},
                new String[]{"The Godfather, TG002", "Sci-Fi,Thriller"}
        );

        try (MockedStatic<MovieValidator> mocked = Mockito.mockStatic(MovieValidator.class)) {
            mocked.when(() -> MovieValidator.isValidTitle("The Dark Knight")).thenReturn(true);
            mocked.when(() -> MovieValidator.getTitleInitials("The Dark Knight")).thenReturn("TDK");
            mocked.when(() -> MovieValidator.isValidMovieId("The Dark Knight", "TDK123")).thenReturn(true);

            mocked.when(() -> MovieValidator.isValidTitle("The Godfather")).thenReturn(true);
            mocked.when(() -> MovieValidator.getTitleInitials("The Godfather")).thenReturn("TG");
            mocked.when(() -> MovieValidator.isValidMovieId("The Godfather", "TG002")).thenReturn(true);

            MovieValidator validator = new MovieValidator(rawData);
            List<Movie> movies = validator.validateMovieData();

            assertEquals(2, movies.size());
            assertEquals("The Godfather", movies.get(1).getTitle());
            assertEquals("TG002", movies.get(1).getMovieId());
        }
    }


    @Test
    void testInvalidTitleLineFormat() {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"The Dark Knight TDK123", "Action,Drama"});

        MovieValidator validator = new MovieValidator(rawData);
        InputException ex = assertThrows(InputException.class, validator::validateMovieData);
        assertTrue(ex.getMessage().contains("Invalid First Line Data"));
    }

}
