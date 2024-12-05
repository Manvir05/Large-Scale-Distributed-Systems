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

public class MastodonStateless {
        public static void main(String[] args) {
                String input = args[0];

                SparkConf conf = new SparkConf().setAppName("Real-time Twitter Stateless Exercise");
                AppConfig appConfig = AppConfig.getConfig();

                StreamingContext sc = new StreamingContext(conf, Durations.seconds(10));
                JavaStreamingContext jsc = new JavaStreamingContext(sc);
                jsc.checkpoint("/tmp/checkpoint");

                JavaDStream<SimplifiedTweetWithHashtags> stream = new MastodonDStream(sc, appConfig).asJStream();

                // TODO IMPLEMENT ME
                //JavaRDD<String> languageMapLines = jsc.sparkContext().textFile("/Users/manvirkaur/Desktop/finalDistributedSystems/LSDS/LSDS2024-seed/lab3-mastodon_noanswer/src/main/resources/map.tsv");
                JavaRDD<String> languageMapLines = jsc.sparkContext().textFile(input);
                JavaPairRDD<String, String> languageMap = LanguageMapUtils.buildLanguageMap(languageMapLines);

                JavaPairDStream<String, Integer> tuits = stream
                        .mapToPair(tweet -> new Tuple2<>(tweet.getLanguage(), 1))
                        .transformToPair(rdd -> rdd.join(languageMap))
                        .mapToPair(tuple -> new Tuple2<>(tuple._2()._2(), 1))
                        .reduceByKey((a,b) -> a + b)
                        .transformToPair(rdd -> rdd.mapToPair(Tuple2::swap).sortByKey(false).mapToPair(Tuple2::swap));;

                tuits.print();

                // Start the application and wait for termination signal
                sc.start();
                sc.awaitTermination();
        }
}