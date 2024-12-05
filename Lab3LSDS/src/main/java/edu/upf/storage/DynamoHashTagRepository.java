package edu.upf.storage;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.github.tukaaa.model.SimplifiedTweetWithHashtags;

import edu.upf.model.HashTagCount;

public class DynamoHashTagRepository implements IHashtagRepository, Serializable {

  final static String endpoint = "dynamodb.us-east-1.amazonaws.com";
  final static String region = "us-east-1";
  final static String tableName = "LsdsTwitterHashtags"; // Table name
  final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
          .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
          .withCredentials(new ProfileCredentialsProvider("default"))
          .build();
  final DynamoDB dynamoDB = new DynamoDB(client);
  final Table dynamoDBTable = dynamoDB.getTable(tableName);

  @Override
  public void write(SimplifiedTweetWithHashtags h) {
    for (String hashtag : h.getHashtags()) {
      String updateExpression = "ADD TweetIds :tweetId, #ctr :inc";

      NameMap nameMap = new NameMap().with("#ctr", "Counter");

      ValueMap valueMap = new ValueMap()
              .withStringSet(":tweetId", String.valueOf(h.getTweetId())) // Ensure TweetIds is a Set type in DynamoDB
              .withNumber(":inc", 1);

      if (h.getLanguage() != null) {
        updateExpression += " SET #lang = if_not_exists(#lang, :lang)";
        nameMap.with("#lang", "Language");
        valueMap.withString(":lang", h.getLanguage());
      }

      UpdateItemSpec updateItemSpec = new UpdateItemSpec()
              .withPrimaryKey("HashTag", hashtag)
              .withUpdateExpression(updateExpression)
              .withNameMap(nameMap)
              .withConditionExpression("attribute_not_exists(HashTag) OR attribute_exists(HashTag)")
              .withValueMap(valueMap);
      try {
        dynamoDBTable.updateItem(updateItemSpec);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  @Override
  public List<HashTagCount> readTop10(String lang) {

    Map<String, Long> hashtagCounts = new HashMap<>();

    ScanSpec scanSpec = new ScanSpec()
            .withFilterExpression("#lang = :v_lang")
            .withNameMap(new NameMap().with("#lang", "Language")) // Using Expression Attribute Names
            .withValueMap(new ValueMap().withString(":v_lang", lang));

    ItemCollection<ScanOutcome> items = dynamoDBTable.scan(scanSpec);
    for (Item item : items) {
      String hashtag = item.getString("HashTag");
      Long count = item.getLong("Counter");

      hashtagCounts.put(hashtag, hashtagCounts.getOrDefault(hashtag, 0L) + count);
    }

    List<HashTagCount> results = hashtagCounts.entrySet().stream()
            .map(entry -> new HashTagCount(entry.getKey(), lang, entry.getValue()))
            .collect(Collectors.toList());

    List<HashTagCount> top10 = results.stream()
            .sorted(Comparator.comparingLong(HashTagCount::getCount).reversed())
            .limit(10)
            .collect(Collectors.toList());

    return top10;
  }
}
