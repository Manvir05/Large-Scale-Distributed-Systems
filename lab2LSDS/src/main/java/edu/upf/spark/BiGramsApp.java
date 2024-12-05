package edu.upf.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import edu.upf.model.ExtendedSimplifiedTweet;
import scala.Tuple2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BiGramsApp {
    public static void main(String[] args){
        if (args.length < 3) {
            System.err.println("Usage: BiGramsApp <language> <output> <inputs>");
            System.exit(1);
        }
        String language = args[0];
        String outputpath = args[1];
        List<String> inputpaths = Arrays.asList(args).subList(2, args.length);

        SparkConf conf = new SparkConf().setAppName("BiGrams");
        JavaSparkContext sparkContext = new JavaSparkContext(conf);

        JavaPairRDD<String, Integer> combinedBiGrams = null;

        for (String inputpath : inputpaths) {
            JavaRDD<String> file = sparkContext.textFile(inputpath);
            //map and filter the tweets
            JavaRDD<ExtendedSimplifiedTweet> tweets = file
                    .map(line -> ExtendedSimplifiedTweet.fromJson(line).orElse(null))
                    .filter(tweet -> tweet != null && tweet.getLanguage().equals(language));

            JavaPairRDD<String, Integer> biGrams = tweets
                    .flatMapToPair(tweet -> {
                        String[] words = tweet.getText().toLowerCase().trim().replaceAll("[^a-zA-Z0-9\\s]", "").split("\\s+");
                        List<Tuple2<String, Integer>> bigrams = new ArrayList<>();
                        for (int i = 0; i < words.length - 1; i++) {
                            bigrams.add(new Tuple2<>(words[i] + " " + words[i+1], 1));
                        }
                        return bigrams.iterator();
                    })
                    .reduceByKey((a, b) -> a + b);

            if (combinedBiGrams == null) {
                combinedBiGrams = biGrams;
            } else {
                combinedBiGrams = combinedBiGrams.union(biGrams).reduceByKey((a, b) -> a + b);
            }
        }
        //select top 10
        List<Tuple2<String, Integer>> top10BiGrams = combinedBiGrams
                .mapToPair(item -> item.swap()) // Swap to make the count as key
                .sortByKey(false) // Sort by key
                .mapToPair(item -> item.swap()) // Swap back
                .take(10);

        JavaRDD<Tuple2<String, Integer>> top10BiGramsRDD = sparkContext.parallelize(top10BiGrams);

        if (combinedBiGrams != null) {
            top10BiGramsRDD.saveAsTextFile(outputpath);
        }
        sparkContext.stop();
    }
}

