package edu.upf;

import edu.upf.util.LanguageMapUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import com.github.tukaaa.MastodonDStream;
import com.github.tukaaa.config.AppConfig;
import com.github.tukaaa.model.SimplifiedTweetWithHashtags;
import scala.Tuple2;

public class MastodonWindows {
        public static void main(String[] args) throws InterruptedException {
                String input = args[0];

                SparkConf conf = new SparkConf().setAppName("Real-time Mastodon Stateful with Windows Exercise");
                AppConfig appConfig = AppConfig.getConfig();

                StreamingContext sc = new StreamingContext(conf, Durations.seconds(10));
                JavaStreamingContext jsc = new JavaStreamingContext(sc);
                jsc.checkpoint("/tmp/checkpoint");

                JavaDStream<SimplifiedTweetWithHashtags> stream = new MastodonDStream(sc, appConfig).asJStream();

                JavaRDD<String> languageMapLines = jsc.sparkContext().textFile(input);
                JavaPairRDD<String, String> languageMap = LanguageMapUtils.buildLanguageMap(languageMapLines);

                JavaPairDStream<String, Integer> languagesCount = stream
                        .mapToPair(tweet -> new Tuple2<>(tweet.getLanguage(), 1))
                        .reduceByKeyAndWindow(Integer::sum, Durations.minutes(1), Durations.seconds(20));

                JavaPairDStream<String, Integer> languagesCountWithNames = languagesCount
                        .transformToPair(rdd -> rdd.join(languageMap))
                        .mapToPair(tuple -> new Tuple2<>(tuple._2()._2(), tuple._2()._1())); // (Language Name, Count)

                JavaPairDStream<Integer, String> swappedPair = languagesCountWithNames.mapToPair(Tuple2::swap);

                JavaPairDStream<Integer, String> sortedCount = swappedPair.transformToPair(rdd -> rdd.sortByKey(false));

                // Printing window calculations
                sortedCount.foreachRDD(rdd -> {
                        System.out.println("--- New window ---");
                        rdd.take(15).forEach(record ->
                                System.out.println(record._2 + " : " + record._1));
                });

                stream.mapToPair(tweet -> new Tuple2<>(tweet.getLanguage(), 1))
                        .reduceByKey(Integer::sum)
                        .transformToPair(rdd -> rdd.join(languageMap))
                        .mapToPair(tuple -> new Tuple2<>(tuple._2()._2(), tuple._2()._1()))
                        .mapToPair(tuple -> tuple.swap())
                        .transformToPair(rdd -> rdd.sortByKey(false))
                        .mapToPair(tuple -> tuple.swap())
                        .foreachRDD(rdd -> {
                                System.out.println("--- Batch computation ---");
                                rdd.take(15).forEach(record ->
                                        System.out.println(record._1 + " : " + record._2));
                        });

                sortedCount.foreachRDD(rdd -> {
                        System.out.println("--- New window ---");
                        System.out.println("Window Duration: " + Durations.minutes(1).milliseconds() + " ms");
                        System.out.println("Slide Duration: " + Durations.seconds(20).milliseconds() + " ms");
                        rdd.take(15).forEach(record ->
                                System.out.println(record._2 + " : " + record._1));
                });

                // Start the application and wait for termination signal
                jsc.start();
                jsc.awaitTermination();
        }
}
