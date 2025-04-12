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

    // Getters
    public String getName() { return name; }
    public String getUserId() { return userId; }
    public List<String> getLikedMovieIds() { return likedMovieIds; }
}
