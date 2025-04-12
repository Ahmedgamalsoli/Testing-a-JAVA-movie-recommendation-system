/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Ahmed Gamal
 */
public class RecommendationFileWriter {
    public void generateOutput(List<User> users, List<Movie> movies) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("recommendations.txt"))) {
            for (User user : users) {
                writer.write(user.getName() + ", " + user.getUserId() + "\n");
                List<String> recommendations = Recommendation.generateRecommendations(user, movies);
                writer.write(String.join(", ", recommendations) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing recommendations.txt");
        }
    }
    public void writeError(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("recommendations.txt"))) {
            writer.write(message);
            System.out.println("Error details written to recommendations.txt");
        } catch (IOException e) {
            System.err.println("Critical failure: Could not write error message");
        }
    }
}
