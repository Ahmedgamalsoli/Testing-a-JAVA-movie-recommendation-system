/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ahmed Gamal
 */
public class UserValidator {
    private List<String[]> rawData;

    public UserValidator(List<String[]> rawData) {
        this.rawData = rawData;
    }
    public List<User> validateUserData() throws InputException {
        List<User> users = new ArrayList<>();
        int idx = 0;
        while (idx<rawData.size()) {
            String[] UserEntry = rawData.get(idx); // Get the array (String[2])
            String userLine     = UserEntry[0];
            String moviesLine   = UserEntry[1];
            /**/
            idx++;
            String[] userParts = userLine.split(",");
            if (userParts.length != 2) {
                throw new InputException(String.format("Invalid First Line Data in User (%d) ", idx));
            }
            
            String name = userParts[0];
            String userId = userParts[1].trim();

            if (!isValidName(name)) {
                throw new InputException(String.format("ERROR: User Name (%s) is wrong", name));
            }

            if (!isValidUserId(userId)) {
                throw new InputException(String.format("ERROR: User Id (%s) is wrong", userId));
            }


            List<String> likedMovies = Arrays.asList(moviesLine.split(","));
            for (int i = 0; i < likedMovies.size(); i++) {
                likedMovies.set(i, likedMovies.get(i).trim());
            }

            users.add(new User(name, userId, likedMovies));
        }
        return users;
    }
    
    private boolean isValidName(String name) {
        return name.matches("^[A-Za-z]+(\\s+[A-Za-z]+)*\\s*$") && !name.startsWith(" ");
    }


    private boolean isValidUserId(String userId) {
        return userId.matches("^([0-9]{9})$|^([0-9]{8}[A-Za-z])$");
    }
}
