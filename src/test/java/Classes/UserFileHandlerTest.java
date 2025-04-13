/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Classes;

//import java.io.File;
//import java.io.FileWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ahmed Gamal
 */
public class UserFileHandlerTest {

// private final String testFile = "users_test.txt";



    @Test
    public void UserFileHandlerTest_testReadUserDataFromValidFile() throws Exception {
        String testFile = "users.txt";
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("Hassan Ali, 12345678X\n");
            writer.write("TSR001,TDK003\n");
            writer.write("Ali Mohamed, 87654321W\n");
            writer.write("TG002\n");
        }

            List<String[]> result = UserFileHandler.readUserDataFromFile();

        assertEquals(2, result.size());

        assertEquals("Hassan Ali, 12345678X", result.get(0)[0]);
        assertEquals("TSR001,TDK003", result.get(0)[1]);

        assertEquals("Ali Mohamed, 87654321W", result.get(1)[0]);
        assertEquals("TG002", result.get(1)[1]);
    }

    @Test(expected = InputException.class)
    public void testMissingSecondLine_throwsInputException() throws Exception {

        // Create a temporary test file
        File testFile = new File("users.txt");
        
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("Hassan Ali, 12345678X\n");
            // Only one line, second line is missing
        }        

        // Now call the method, which reads from users.txt
        UserFileHandler.readUserDataFromFile();
    }

    @Test(expected = InputException.class)
    public void testUnreadableFile_throwsInputException() throws Exception {
        File file = new File("users.txt");
        if (file.exists()) {
            file.delete();
        }

        UserFileHandler.readUserDataFromFile();
    }

    @Test
    public void testEmptyFile_returnsEmptyList() throws Exception {
        String testFile = "users.txt";
        new FileWriter(testFile).close(); // Create empty file

        List<String[]> result = UserFileHandler.readUserDataFromFile();

        assertEquals(0, result.size());
    }
}
