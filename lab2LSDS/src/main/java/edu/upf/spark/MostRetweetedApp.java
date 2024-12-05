package edu.upf.spark;

import edu.upf.model.ExtendedSimplifiedTweet;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.*;
import java.util.stream.Collectors;

public class MostRetweetedApp {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: MostRetweetedApp <output> <inputFile>");
            System.exit(1);
        }

        String outputPath = args[0];
        List<String> inputPaths = Arrays.asList(args).subList(1, args.length);

        SparkConf sparkConf = new SparkConf().setAppName("MostRetweetedApp");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        JavaRDD<ExtendedSimplifiedTweet> combinedTweets = sc.emptyRDD();

        for (String inputPath : inputPaths) {
            JavaRDD<String> file = sc.textFile(inputPath);
            JavaRDD<ExtendedSimplifiedTweet> tweets = file
                    .map(line -> ExtendedSimplifiedTweet.fromJson(line).orElse(null))
                    .filter(Objects::nonNull);

            combinedTweets = combinedTweets.union(tweets);
        }
        // Identify the top 10 most retweeted users
        JavaPairRDD<Long, Integer> retweetCountsByUser = combinedTweets
                .filter(ExtendedSimplifiedTweet::getRetweeted)
                .mapToPair(tweet -> new Tuple2<>(tweet.getRetweetedUserId(), 1))
                .reduceByKey(Integer::sum);

       // tweets.saveAsTextFile("tweets2");

        List<Long> topRetweetedUserIds = retweetCountsByUser
                .mapToPair(Tuple2::swap)
                .sortByKey(false)
                .mapToPair(Tuple2::swap)
                .keys()
                .take(10);

        // Find the most retweeted tweet for each of the top 10 users
        JavaPairRDD<Long, Tuple2<Long, Integer>> mostRetweetedTweetByTopUsers = combinedTweets
                .filter(tweet -> tweet.getRetweeted() && topRetweetedUserIds.contains(tweet.getRetweetedUserId()))
                .mapToPair(tweet -> new Tuple2<>(tweet.getRetweetedUserId(), new Tuple2<>(tweet.getRetweetedTweetId(), 1)))
                .reduceByKey((a, b) -> new Tuple2<>(a._1, a._2 + b._2))
                .mapToPair(x -> new Tuple2<>(x._1, new Tuple2<>(x._2._1, x._2._2))) // UserID, (TweetID, Count)
                .reduceByKey((a, b) -> a._2 > b._2 ? a : b);

        //action
        List<Tuple2<Long, Tuple2<Long, Integer>>> mostRetweetedTweetsByTopUsers = mostRetweetedTweetByTopUsers.collect();

        List<String> formattedResults = mostRetweetedTweetsByTopUsers.stream()
                .map(tuple -> "User: " + tuple._1 + ", Tweet: " + tuple._2._1 + ", Retweets: " + tuple._2._2)
                .collect(Collectors.toList());

        // Save the results
        JavaRDD<String> resultsRDD = sc.parallelize(formattedResults);


        resultsRDD.saveAsTextFile(outputPath);
    }
}
