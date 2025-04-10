package moviesRecommendationSystem;

import java.io.*;
import java.util.*;

public class RecommendationSystem {
    private Map<String, String> movieTitles = new HashMap<>();          // movieId -> title
    private Map<String, List<String>> movieGenres = new HashMap<>();    // genre -> list of movieIds
    private Map<String, List<String>> userLikes = new HashMap<>();      // userId -> liked movieIds
    private Map<String, String> userNames = new HashMap<>();            // userId -> name

    public void readMovies(String movieFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(movieFilePath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] titleId = line.split(",");
            if (titleId.length < 2) continue;
            String title = titleId[0].trim();
            String id = titleId[1].trim();
            if (!validateTitle(title)) throw new IllegalArgumentException("ERROR: Movie Title " + title + " is wrong");
            if (!validateMovieId(title, id)) throw new IllegalArgumentException("ERROR: Movie Id letters " + id + " are wrong");

            line = reader.readLine();
            if (line == null) break;
            String[] genres = line.split(",");
            movieTitles.put(id, title);
            for (String genre : genres) {
                genre = genre.trim().toLowerCase();
                movieGenres.computeIfAbsent(genre, k -> new ArrayList<>()).add(id);
            }
        }
        reader.close();
    }

    public void readUsers(String userFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(userFilePath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] nameId = line.split(",");
            if (nameId.length < 2) continue;
            String name = nameId[0].trim();
            String id = nameId[1].trim();
            if (!validateUserName(name)) throw new IllegalArgumentException("ERROR: User Name " + name + " is wrong");
            if (!validateUserId(id)) throw new IllegalArgumentException("ERROR: User Id " + id + " is wrong");

            line = reader.readLine();
            if (line == null) break;
            List<String> likedMovies = Arrays.asList(line.split(","));
            userNames.put(id, name);
            userLikes.put(id, likedMovies);
        }
        reader.close();
    }

    public void generateRecommendations(String outputFilePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
        for (String userId : userLikes.keySet()) {
            String userName = userNames.get(userId);
            List<String> liked = userLikes.get(userId);
            Set<String> recommendedIds = new HashSet<>();
            for (String movieId : liked) {
                movieId = movieId.trim();
                for (String genre : movieGenres.keySet()) {
                    if (movieGenres.get(genre).contains(movieId)) {
                        recommendedIds.addAll(movieGenres.get(genre));
                        break;
                    }
                }
            }
            recommendedIds.removeAll(liked); // exclude already liked movies

            writer.write(userName + "," + userId + "\n");
            List<String> recommendedTitles = new ArrayList<>();
            for (String movieId : recommendedIds) {
                recommendedTitles.add(movieTitles.get(movieId));
            }
            writer.write(String.join(",", recommendedTitles) + "\n");
        }
        writer.close();
    }

    public boolean validateTitle(String title) {
        for (String word : title.split(" ")) {
            if (word.isEmpty() || !Character.isUpperCase(word.charAt(0))) return false;
        }
        return true;
    }

    public boolean validateMovieId(String title, String id) {
        StringBuilder expectedPrefix = new StringBuilder();
        for (String word : title.split(" ")) {
            expectedPrefix.append(word.charAt(0));
        }
        return id.startsWith(expectedPrefix.toString()) && id.length() == expectedPrefix.length() + 3;
    }

    public boolean validateUserName(String name) {
        return name.matches("[A-Za-z][A-Za-z ]*");
    }

    public boolean validateUserId(String id) {
        return id.matches("\\d{8}[A-Za-z]?");
    }
}
