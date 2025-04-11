/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.List;

public class User {
    private final String name;
    private final String userId;
    private final List<String> likedMovieIds;

    public User(String name, String userId, List<String> likedMovieIds) {
        this.name = name;
        this.userId = userId;
        this.likedMovieIds = likedMovieIds;
    }

    public static boolean isValidName(String name) {
        return name.matches("^[A-Za-z]+(\\s+[A-Za-z]+)*$") && !name.startsWith(" ");
    }

    public static boolean isValidUserId(String userId) {
        return userId.matches("^([0-9]{9})$|^([0-9]{8}[A-Za-z])$");
    }

    // Getters
    public String getName() { return name; }
    public String getUserId() { return userId; }
    public List<String> getLikedMovieIds() { return likedMovieIds; }
}
