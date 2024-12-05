package edu.upf;

import edu.upf.model.HashTagCount;
import edu.upf.storage.DynamoHashTagRepository;

import java.util.List;

public class MastodonHashtagReader {
    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Please provide a language as an argument.");
            return;
        }

        String language = args[0];
        DynamoHashTagRepository repository = new DynamoHashTagRepository();

        try {
            List<HashTagCount> top10 = repository.readTop10(language);
            if (top10.isEmpty()) {
                System.out.printf("\nNo hashtags stored for the language: %s\n\n", language);
            } else {
                System.out.printf("\nTop 10 hashtags in: %s\n", language);
                System.out.println("Hashtag | Count");
                top10.forEach(hashtag -> System.out.printf("%s : %d\n", hashtag.getHashtag(), hashtag.getCount()));
            }
        } catch (Exception e) {
            System.err.println("An error occurred while retrieving hashtags: " + e.getMessage());
        }
    }
}
