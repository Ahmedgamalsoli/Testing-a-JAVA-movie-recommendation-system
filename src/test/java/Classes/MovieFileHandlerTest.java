/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Classes;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ahmed Gamal
 */
public class MovieFileHandlerTest {
    
    @Test
    public void MovieFileHandlerTest_testReadMovieDataFromValidFile() throws Exception {
        String testFile = "movies.txt";
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("The Shawshank Redemption, TSR001\n");
            writer.write("Drama\n");
            writer.write("The Godfather, TG002\n");
            writer.write("CrimeDrama, Drama\n");
        }

            List<String[]> result = MovieFileHandler.readMovieDataFromFile();

        assertEquals(2, result.size());

        assertEquals("The Shawshank Redemption, TSR001", result.get(0)[0]);
        assertEquals("Drama", result.get(0)[1]);

        assertEquals("The Godfather, TG002", result.get(1)[0]);
        assertEquals("CrimeDrama, Drama", result.get(1)[1]);
    }

    @Test(expected = InputException.class)
    public void testMissingSecondLine_throwsInputException() throws Exception {

        // Create a temporary test file
        File testFile = new File("movies.txt");
        
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("The Shawshank Redemption, TSR001\n");
            // Only one line, second line is missing
        }        

        // Now call the method, which reads from users.txt
        MovieFileHandler.readMovieDataFromFile();
    }

    @Test(expected = InputException.class)
    public void testUnreadableFile_throwsInputException() throws Exception {
        File file = new File("movies.txt");
        if (file.exists()) {
            file.delete();
        }

        MovieFileHandler.readMovieDataFromFile();
    }

    @Test
    public void testEmptyFile_returnsEmptyList() throws Exception {
        String testFile = "movies.txt";
        new FileWriter(testFile).close(); // Create empty file

        List<String[]> result = MovieFileHandler.readMovieDataFromFile();

        assertEquals(0, result.size());
    }
}
