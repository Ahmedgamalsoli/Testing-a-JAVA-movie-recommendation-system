/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Ahmed Gamal
 */
public class MovieFileHandler {
    public static List<String[]> readMovieDataFromFile() throws InputException {
        List<String[]> rawData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("movies.txt"))) {
            String titleLine;
            while ((titleLine = reader.readLine()) != null) {
                String genresLine = reader.readLine();
                if (genresLine == null) throw new InputException("Invalid movies.txt format - missing genres line");        
                rawData.add(new String[]{titleLine.trim(), genresLine.trim()});
            }
        } catch (IOException e) {
            throw new InputException("ERROR: Unable to read movies.txt");
        }
        return rawData;
    }
}

