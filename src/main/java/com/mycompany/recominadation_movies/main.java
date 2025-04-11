package com.mycompany.recominadation_movies;

import Classes.Movie;
import Classes.Recommendation;
import Classes.User;
import java.io.*;
import java.util.*;

public class main {
    private static List<Movie> allMovies = new ArrayList<>();
    private static List<User> allUsers = new ArrayList<>();
    private static final Map<String, String> moviesMap = new HashMap<>();
    private static final Set<String> userIds = new HashSet<>();

    public static void main(String[] args) {
        try {
            allMovies = processMovies();
            allUsers = processUsers();
            generateOutput(allUsers, allMovies);
            printAllMovies();
            printAllUsers();

        } catch (InputException e) {
            writeError(e.getMessage());
            printException(e.getMessage());  // New exception printing
        }
    }

    // NEW EXCEPTION PRINTING FUNCTION
    public static void printException(String errorMessage) {
        System.out.println("\n=== ERROR DETAILS ===");
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║           SYSTEM ERROR             ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.printf("║ %-34s ║\n", errorMessage);
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("Please check recommendations.txt for details");
    }

    // Modified error writing to include console output
    private static void writeError(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("recommendations.txt"))) {
            writer.write(message);
            System.out.println("Error details written to recommendations.txt");
        } catch (IOException e) {
            System.err.println("Critical failure: Could not write error message");
        }
    }


    public static void printAllUsers() {
        System.out.println("\n=== ALL USERS ===");
        for (User user : allUsers) {
            System.out.println("User Name: " + user.getName());
            System.out.println("User ID: " + user.getUserId());
            System.out.println("Liked Movies: " + 
                String.join(", ", user.getLikedMovieIds()));
            System.out.println("--------------------");
        }
    }

    public static void printAllMovies() {
        System.out.println("\n=== ALL MOVIES ===");
        for (Movie movie : allMovies) {
            System.out.println("Movie Title: " + movie.getTitle());
            System.out.println("Movie ID: " + movie.getMovieId());
            System.out.println("Genres: " + 
                String.join(", ", movie.getGenres()));
            System.out.println("--------------------");
        }
    }
    private static List<Movie> processMovies() throws InputException {
        List<Movie> movies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("movies.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String titleLine = line.trim();
                String genresLine = reader.readLine();
                if (genresLine == null) throw new InputException("Invalid movies.txt format");

                String[] titleParts = titleLine.split(",");
                if (titleParts.length != 2) {
                    throw new InputException(String.format("ERROR: Movie Title (%s) is wrong", titleParts[0]));
                }

                String title = titleParts[0].trim();
                String movieId = titleParts[1].trim();

                if (!Movie.isValidTitle(title)) {
                    throw new InputException(String.format("ERROR: Movie Title (%s) is wrong", title));
                }

                if (!Movie.isValidMovieId(title, movieId)) {
                    String expected = Movie.getTitleInitials(title);
                    String actual = movieId.substring(0, movieId.length()-3);
                    if (!actual.equals(expected)) {
                        throw new InputException(String.format("ERROR: Movie Id letters (%s) are wrong", movieId));
                    } else {
                        throw new InputException(String.format("ERROR: Movie Id numbers (%s) aren't unique", movieId));
                    }
                }

                if (moviesMap.containsKey(movieId)) {
                    throw new InputException(String.format("ERROR: Movie Id numbers (%s) aren't unique", movieId));
                }

                List<String> genres = Arrays.asList(genresLine.split(","));
                for (int i = 0; i < genres.size(); i++) {
                    genres.set(i, genres.get(i).trim().toLowerCase());
                }

                movies.add(new Movie(title, movieId, genres));
                moviesMap.put(movieId, title);
            }
        } catch (IOException e) {
            throw new InputException("ERROR: Unable to read movies.txt");
        }
        return movies;
    }

    private static List<User> processUsers() throws InputException {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String userLine = line.trim();
                String moviesLine = reader.readLine();
                if (moviesLine == null) throw new InputException("Invalid users.txt format");

                String[] userParts = userLine.split(",");
                if (userParts.length != 2) {
                    throw new InputException(String.format("ERROR: User Name (%s) is wrong", userParts[0]));
                }

                String name = userParts[0].trim();
                String userId = userParts[1].trim();

                if (!User.isValidName(name)) {
                    throw new InputException(String.format("ERROR: User Name (%s) is wrong", name));
                }

                if (!User.isValidUserId(userId)) {
                    throw new InputException(String.format("ERROR: User Id (%s) is wrong", userId));
                }

                if (userIds.contains(userId)) {
                    throw new InputException(String.format("ERROR: User Id (%s) is wrong (duplicate)", userId));
                }

                List<String> likedMovies = Arrays.asList(moviesLine.split(","));
                for (int i = 0; i < likedMovies.size(); i++) {
                    likedMovies.set(i, likedMovies.get(i).trim());
                }

                users.add(new User(name, userId, likedMovies));
                userIds.add(userId);
            }
        } catch (IOException e) {
            throw new InputException("ERROR: Unable to read users.txt");
        }
        return users;
    }

    private static void generateOutput(List<User> users, List<Movie> movies) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("recommendations.txt"))) {
            for (User user : users) {
                writer.write(user.getName() + "," + user.getUserId() + "\n");
                List<String> recommendations = Recommendation.generateRecommendations(user, movies);
                writer.write(String.join(",", recommendations) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing recommendations.txt");
        }
    }

//    private static void writeError(String message) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter("recommendations.txt"))) {
//            writer.write(message);
//        } catch (IOException e) {
//            System.err.println("Error writing error message");
//        }
//    }

    static class InputException extends Exception {
        public InputException(String message) {
            super(message);
        }
    }
}