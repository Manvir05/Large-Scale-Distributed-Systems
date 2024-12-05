package edu.upf.filter;

import edu.upf.parser.SimplifiedTweet;

import java.io.*;
import java.util.Optional;
public class FileLanguageFilter {
    final String inputFile;
    final String outputFile;

    public FileLanguageFilter(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void filterLanguage(String language) throws Exception {
        //Clock to measure time
        long startTime = System.nanoTime();
        // Initialize the tweet counter
        int tweetCount = 0;

        try (FileReader reader = new FileReader(inputFile);
             FileWriter writer = new FileWriter(outputFile);
             BufferedReader bReader = new BufferedReader(reader);
             BufferedWriter bWriter = new BufferedWriter(writer)) {

            String line;

            while ((line = bReader.readLine()) != null) { // Read one line of content

                // Parse the line into an Optional<SimplifiedTweet>
                Optional<SimplifiedTweet> myParse = SimplifiedTweet.fromJson(line);

                // If myParse is present and the language matches, write the line to the output file
                if (myParse.isPresent() && myParse.get().getLanguage().equals(language)) {
                    bWriter.write(line); // Write the original one line of content
                    bWriter.newLine();
                    tweetCount++; // Increment the tweet counter
                }
                bReader.readLine();
            }
            bReader.close();
            bWriter.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //Stop clock and compute the time
        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/1_000_000; // convert nanoseconds to milliseconds

        System.out.println("Execution Time: " + duration + "ms");
        System.out.println("Number of tweets in \"" + language + "\" language: " + tweetCount);
    }
}