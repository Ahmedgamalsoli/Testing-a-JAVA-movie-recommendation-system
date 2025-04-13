/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;

/**
 *
 * @author seif9
 */
public class UserValidatorTest {
    private Object invokePrivateMethod(String methodName, Class<?>[] paramTypes, Object[] args) throws Exception {
        UserValidator validator = new UserValidator(new ArrayList<>());
        Method method = UserValidator.class.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        return method.invoke(validator, args);
    }
    
    @Test
    public void testIsValidName_ValidFullName() throws Exception {
        assertTrue((boolean) invokePrivateMethod("isValidName", new Class[]{String.class}, new Object[]{"John Doe"}));
    }

    @Test
    public void testIsValidName_StartsWithSpace() throws Exception {
        assertFalse((boolean) invokePrivateMethod("isValidName", new Class[]{String.class}, new Object[]{" John"}));
    }

    @Test
    public void testIsValidName_ContainsNumbers() throws Exception {
        assertFalse((boolean) invokePrivateMethod("isValidName", new Class[]{String.class}, new Object[]{"John123"}));
    }

    @Test
    public void testIsValidName_ContainsSpecialCharacter() throws Exception {
        assertFalse((boolean) invokePrivateMethod("isValidName", new Class[]{String.class}, new Object[]{"John@"}));
    }

    @Test
    public void testIsValidName_StartsWithSpecialCharacter() throws Exception {
        assertFalse((boolean) invokePrivateMethod("isValidName", new Class[]{String.class}, new Object[]{"#John"}));
    }

    @Test
    public void testIsValidName_SingleLowerCaseName() throws Exception {
        assertTrue((boolean) invokePrivateMethod("isValidName", new Class[]{String.class}, new Object[]{"john"}));
    }


    @Test
    public void testIsValidUserId_NineDigits() throws Exception {
        assertTrue((boolean) invokePrivateMethod("isValidUserId", new Class[]{String.class}, new Object[]{"123456789"}));
    }

    @Test
    public void testIsValidUserId_EightDigitsOneLetter() throws Exception {
        assertTrue((boolean) invokePrivateMethod("isValidUserId", new Class[]{String.class}, new Object[]{"12345678A"}));
    }

    @Test
    public void testIsValidUserId_OnlyEightDigits() throws Exception {
        assertFalse((boolean) invokePrivateMethod("isValidUserId", new Class[]{String.class}, new Object[]{"12345678"}));
    }

    @Test
    public void testIsValidUserId_StartsWithLetter() throws Exception {
        assertFalse((boolean) invokePrivateMethod("isValidUserId", new Class[]{String.class}, new Object[]{"I2345678"}));
    }

    @Test
    public void testIsValidUserId_LetterInMiddleAndEnd() throws Exception {
        assertFalse((boolean) invokePrivateMethod("isValidUserId", new Class[]{String.class}, new Object[]{"123I5678A"}));
    }
    @Test
    public void testIsValidUserId_SevenDigitsTwoLetter() throws Exception {
        assertFalse((boolean) invokePrivateMethod("isValidUserId", new Class[]{String.class}, new Object[]{"1234567AA"}));
    }


    @Test
    public void testIsValidFormat_UpperCase_three_characters_ID() throws Exception {
        assertTrue((boolean) invokePrivateMethod("isValidFormat", new Class[]{String.class}, new Object[]{"MOV001"}));
    }

    @Test    
    public void testIsValidFormat_LowerCase_three_characters_ID() throws Exception {
        assertFalse((boolean) invokePrivateMethod("isValidFormat", new Class[]{String.class}, new Object[]{"mov001"}));
    }

    @Test
    public void testIsValidFormat_UpperCase_One_character_ID() throws Exception {
        assertTrue((boolean) invokePrivateMethod("isValidFormat", new Class[]{String.class}, new Object[]{"M001"}));
    }

    @Test
    public void testIsValidFormat_UpperCase_One_character_ID_2_Numbers_only() throws Exception {
        assertFalse((boolean) invokePrivateMethod("isValidFormat", new Class[]{String.class}, new Object[]{"M01"}));
    }

    @Test
    public void testIsValidFormat_UpperCase_One_character_ID_No_Numbers() throws Exception {
        assertFalse((boolean) invokePrivateMethod("isValidFormat", new Class[]{String.class}, new Object[]{"M"}));
    }

    @Test
    public void testIsValidFormat_UpperCase_One_character_ID_No_Numbers__() throws Exception {
        assertFalse((boolean) invokePrivateMethod("isValidFormat", new Class[]{String.class}, new Object[]{"123M"}));
    }
    @Test
    public void testIsValidFormat_UpperCase_One_character_ID_No_Numbers___() throws Exception {
        assertFalse((boolean) invokePrivateMethod("isValidFormat", new Class[]{String.class}, new Object[]{"&*@890"}));
    }

    
    /*****************************     USER LINE TESTS     *****************************/
    
    @Test
    void testValidUserData() throws InputException {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"Hassan Ali, 12345678X", "TSR001, TDK003, TG002"});

        try (MockedStatic<UserValidator> mocked = Mockito.mockStatic(UserValidator.class)) {
            mocked.when(() -> UserValidator.isValidName("Hassan Ali")).thenReturn(true);
            mocked.when(() -> UserValidator.isValidUserId("12345678X")).thenReturn(true);
            mocked.when(() -> UserValidator.isValidFormat("TSR001")).thenReturn(true);
            mocked.when(() -> UserValidator.isValidFormat("TDK003")).thenReturn(true);
            mocked.when(() -> UserValidator.isValidFormat("TG002")).thenReturn(true);

            UserValidator validator = new UserValidator(rawData);
            List<User> users = validator.validateUserData();

            assertEquals(1, users.size());
            User user = users.get(0);
            assertEquals("Hassan Ali", user.getName());
            assertEquals("12345678X", user.getUserId());
            assertEquals(Arrays.asList("TSR001", "TDK003", "TG002"), user.getLikedMovieIds());
        }
    }


    @Test
    void testInvalidUserDataFormat() {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"Hassan Ali 12345678X", "TSR001, TDK003, TG002"});

        assertThrows(InputException.class, () -> new UserValidator(rawData).validateUserData());
    }

    
    @Test
    void testInvalidUserIdFormat() {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"Hassan Ali, abc123", "TSR001, TDK003, TG002"});

        try (MockedStatic<UserValidator> mocked = Mockito.mockStatic(UserValidator.class)) {
            mocked.when(() -> UserValidator.isValidName("Hassan Ali")).thenReturn(true);
            mocked.when(() -> UserValidator.isValidUserId("abc123")).thenReturn(false);

            assertThrows(InputException.class, () -> new UserValidator(rawData).validateUserData());
        }
    }

    
    @Test //INVALID USER_NAME
    public void testInvalidUserName() throws InputException {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"H@ssan Ali, 12345678X", "TSR001, TDK003, TG002"});

        try (MockedStatic<UserValidator> mockedStatic = Mockito.mockStatic(UserValidator.class)) {
            mockedStatic.when(() -> UserValidator.isValidName("H@ssan Ali")).thenReturn(false);
            assertThrows(InputException.class, () -> new UserValidator(rawData).validateUserData());
        }
    }

    
    @Test //INVALID USER_NAME and ID ... returns only first error
    public void testInvalidUserName_ID() throws InputException {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"H@ssan Ali, abc123", "TSR001, TDK003, TG002"});

        try (MockedStatic<UserValidator> mockedStatic = Mockito.mockStatic(UserValidator.class)) {
            mockedStatic.when(() -> UserValidator.isValidName("H@ssan Ali")).thenReturn(false);
            assertThrows(InputException.class, () -> new UserValidator(rawData).validateUserData());
        }
    }

    
    @Test //EMPTY INPUT LIST
    public void testEmptyInput() throws InputException {
        List<String[]> rawData = new ArrayList<>(); 
        UserValidator validator = new UserValidator(rawData);
        List<User> users = validator.validateUserData();
        assertEquals(0, users.size());
    }

    
    @Test
    public void testMultipleValidUsers() throws InputException {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"Hassan Ali, 12345678X", "TSR001, TDK003, TG002"});
        rawData.add(new String[]{"Alice Smith, 98765432Y", "ABC001, DEF002"});

        try (MockedStatic<UserValidator> mockedStatic = Mockito.mockStatic(UserValidator.class)) {
            // First user
            mockedStatic.when(() -> UserValidator.isValidName("Hassan Ali")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidUserId("12345678X")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidFormat("TSR001")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidFormat("TDK003")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidFormat("TG002")).thenReturn(true);

            // Second user
            mockedStatic.when(() -> UserValidator.isValidName("Alice Smith")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidUserId("98765432Y")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidFormat("ABC001")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidFormat("DEF002")).thenReturn(true);

            UserValidator validator = new UserValidator(rawData);
            List<User> users = validator.validateUserData();

            assertEquals(2, users.size());
            User user1 = users.get(0);
            assertEquals("Hassan Ali", user1.getName());
            assertEquals("12345678X", user1.getUserId());
            assertEquals(Arrays.asList("TSR001", "TDK003", "TG002"), user1.getLikedMovieIds());

            User user2 = users.get(1);
            assertEquals("Alice Smith", user2.getName());
            assertEquals("98765432Y", user2.getUserId());
            assertEquals(Arrays.asList("ABC001", "DEF002"), user2.getLikedMovieIds());
        }
    }

    @Test //USERNAME STARTS WITH WHITESPACE (Handled to throw error)
    public void testValidUserDataWithWhitespaceEnd() throws InputException {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{" Hassan Ali, 12345678X", "TSR001, TDK003, TG002"});

        try (MockedStatic<UserValidator> mockedStatic = Mockito.mockStatic(UserValidator.class)) {
            mockedStatic.when(() -> UserValidator.isValidName(" Hassan Ali")).thenReturn(false);
            assertThrows(InputException.class, () -> new UserValidator(rawData).validateUserData());
        }
    }
    
    @Test //USERNAME ENDS WITH WHITESPACE (Handled to return output)
    public void testValidUserDataWithWhitespaceStop() throws InputException {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"Hassan Ali , 12345678X", "TSR001, TDK003, TG002"});

        try (MockedStatic<UserValidator> mockedStatic = Mockito.mockStatic(UserValidator.class)) {
            mockedStatic.when(() -> UserValidator.isValidName("Hassan Ali ")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidUserId("12345678X")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidFormat("TSR001")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidFormat("TDK003")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidFormat("TG002")).thenReturn(true);

            UserValidator validator = new UserValidator(rawData);
            List<User> users = validator.validateUserData();
            assertEquals("Hassan Ali ", users.get(0).getName());
        }
    }

    /*****************************     MOVIE_ID LINE TESTS     *****************************/
    
    @Test
    public void testMissingCommaIn2Movies() {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"Hassan Ali, 12345678X", "TSR001 TDK003"});

        try (MockedStatic<UserValidator> mockedStatic = Mockito.mockStatic(UserValidator.class)) {
            mockedStatic.when(() -> UserValidator.isValidName("Hassan Ali")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidUserId("12345678X")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidFormat("TSR001 TDK003")).thenReturn(false);

            assertThrows(InputException.class, () -> new UserValidator(rawData).validateUserData());
        }
    }
    
    @Test
    public void testMissingCommaIn3Movies() throws InputException {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"Hassan Ali, 12345678X", "TSR001 TDK003 TG002"});

        try (MockedStatic<UserValidator> mockedStatic = Mockito.mockStatic(UserValidator.class)) {
            mockedStatic.when(() -> UserValidator.isValidName("Hassan Ali")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidUserId("12345678X")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidFormat("TSR001 TDK003 TG002")).thenReturn(false);

            assertThrows(InputException.class, () -> new UserValidator(rawData).validateUserData());
        }
    }

    @Test
    public void testInvalidMovieFormat() throws InputException {
        List<String[]> rawData = new ArrayList<>();
        rawData.add(new String[]{"Hassan Ali, 12345678X", "TSR001, , TDK003"});

        try (MockedStatic<UserValidator> mockedStatic = Mockito.mockStatic(UserValidator.class)) {
            mockedStatic.when(() -> UserValidator.isValidName("Hassan Ali")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidUserId("12345678X")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidFormat("TSR001")).thenReturn(true);
            mockedStatic.when(() -> UserValidator.isValidFormat("")).thenReturn(false);  // Invalid/empty movie ID
            mockedStatic.when(() -> UserValidator.isValidFormat("TDK003")).thenReturn(true);

            assertThrows(InputException.class, () -> new UserValidator(rawData).validateUserData());
        }
    }
}