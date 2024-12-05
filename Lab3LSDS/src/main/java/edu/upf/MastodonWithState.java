package edu.upf;

import com.github.tukaaa.MastodonDStream;
import com.github.tukaaa.config.AppConfig;
import com.github.tukaaa.model.SimplifiedTweetWithHashtags;
import edu.upf.util.LanguageMapUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.List;

public class MastodonWithState {
    public static void main(String[] args) throws InterruptedException {
        String targetLanguage = args[0]; // Assuming language code is passed as the first argument

        SparkConf conf = new SparkConf().setAppName("Real-time Mastodon With State");
        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(10));
        jsc.checkpoint("/tmp/checkpoint"); // Necessary for stateful transformations

        JavaDStream<SimplifiedTweetWithHashtags> stream = new MastodonDStream(jsc.ssc(), AppConfig.getConfig()).asJStream();
        String input= "lab3-mastodon_noanswer/src/main/resources/map.tsv";
        //TODO implement
        //JavaRDD<String> languageMapLines = jsc.sparkContext().textFile("/Users/manvirkaur/Desktop/finalDistributedSystems/LSDS/LSDS2024-seed/lab3-mastodon_noanswer/src/main/resources/map.tsv");
        JavaRDD<String> languageMapLines = jsc.sparkContext().textFile(input);
        JavaPairRDD<String, String> languageMap = LanguageMapUtils.buildLanguageMap(languageMapLines);

        JavaPairDStream<String, Integer> tuits = stream
                .mapToPair(tweet-> new Tuple2<>(tweet.getLanguage(), tweet.getUserName()))
                .transformToPair(rdd-> rdd.join(languageMap))
                //.filter(pair -> pair._2._2.equals("targetLanguage"))
                .mapToPair(tuple -> new Tuple2<>(tuple._2._1, 1))
                .reduceByKey((a, b) -> a + b);


        JavaPairDStream<Integer, String> sortedTuits = tuits
                .mapToPair(tuple -> new Tuple2<>(tuple._2(), tuple._1()))
                .transformToPair(rdd -> {
                    JavaPairRDD<Integer, String> sortedRDD = rdd.sortByKey(false);
                    return sortedRDD;
                });

        sortedTuits.print();

        jsc.start();
        jsc.awaitTermination();
    }
}
