package edu.upf;

import edu.upf.model.SimplifiedTweet;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TwitterLanguageFilterApp {

    public static void main(String[] args){
        // we except 3 or more arguments: the language, the output path, and at least one input path
        if (args.length < 3) {
            System.err.println("Usage: TwitterLanguageFilterApp <language> <outputPath> <inputPath1> <inputPath2> ...");
            System.exit(-1);
        }
        //long startTime = System.currentTimeMillis();

        String language = args[0];
        String outputPath = args[1];
        List<String> inputPaths = Arrays.asList(args).subList(2, args.length);

        SparkConf conf = new SparkConf().setAppName("Twitter Language Filter");
        JavaSparkContext sparkContext = new JavaSparkContext(conf);

        for (String inputPath : inputPaths) {
            //long fileStartTime = System.currentTimeMillis();
            JavaRDD<String> tweets = sparkContext.textFile(inputPath);

            JavaRDD<String> filteredTweets = tweets
                    .map(line -> SimplifiedTweet.fromJson(line))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(tweet -> tweet.getLanguage().equals(language))
                    .map(SimplifiedTweet::toJson);

            String modifiedOutputPath = outputPath + "/" + inputPath.replaceAll("[^a-zA-Z0-9.-]", "_"); // Sanitizing input path for use in directory naming
            filteredTweets.saveAsTextFile(modifiedOutputPath);

            //long fileEndTime = System.currentTimeMillis();
            //System.out.println("Processing time for file " + inputPath + ": " + (fileEndTime - fileStartTime) + " ms");
        }

        sparkContext.close();

        //long endTime = System.currentTimeMillis();
        //System.out.println("Total execution time: " + (endTime - startTime) + " ms");
    }
}
