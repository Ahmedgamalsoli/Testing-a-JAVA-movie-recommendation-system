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
public class UserFileHandler {
    public static List<String[]> readUserDataFromFile() throws InputException {
        List<String[]> rawData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String moviesLine = reader.readLine();
                if (moviesLine == null) throw new InputException("Invalid users.txt format");
                rawData.add(new String[]{line.trim(), moviesLine.trim()});
        }} 
            catch (IOException e) {
            throw new InputException("ERROR: Unable to read users.txt");
            }
        return rawData;
        }    
    }
