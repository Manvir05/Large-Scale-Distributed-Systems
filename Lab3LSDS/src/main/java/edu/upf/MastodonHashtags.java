package edu.upf;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import com.github.tukaaa.MastodonDStream;
import com.github.tukaaa.config.AppConfig;
import com.github.tukaaa.model.SimplifiedTweetWithHashtags;

import edu.upf.storage.DynamoHashTagRepository;

public class MastodonHashtags {

        public static void main(String[] args) throws InterruptedException {

                SparkConf conf = new SparkConf().setAppName("Real-time Mastodon Hashtags");
                AppConfig appConfig = AppConfig.getConfig();
                StreamingContext sc = new StreamingContext(conf, Durations.seconds(10));
                JavaStreamingContext jsc = new JavaStreamingContext(sc);
                jsc.checkpoint("/tmp/checkpoint");

                JavaDStream<SimplifiedTweetWithHashtags> stream = new MastodonDStream(sc, appConfig).asJStream();

                stream.foreachRDD(rdd -> {
                        rdd.filter(tweet -> !tweet.getHashtags().isEmpty())
                                .foreachPartition(iter -> {
                                        DynamoHashTagRepository repository = new DynamoHashTagRepository();
                                        while (iter.hasNext()) {
                                                SimplifiedTweetWithHashtags tweet = iter.next();
                                                repository.write(tweet);
                                        }
                                });
                });

                System.out.println("... Reading toots ...\n");

                // Start the application and wait for termination signal
                jsc.start();
                jsc.awaitTermination();
        }
}
