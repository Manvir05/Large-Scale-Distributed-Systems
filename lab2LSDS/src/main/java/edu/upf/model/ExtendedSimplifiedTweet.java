package edu.upf.model;

import com.google.common.base.Splitter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.util.Optional;

public class ExtendedSimplifiedTweet implements Serializable {

    private static JsonParser parser = new JsonParser(); //to parse the tweet
    private final long tweetId; // the id of the tweet (’id’)
    private final String text; // the content of the tweet (’text’)
    private final long userId; // the user id (’user->id’)
    private final String userName; // the user name (’user’->’name’)
    private final long followersCount; // the number of followers (’user’->’followers_count’)
    private final String language; // the language of a tweet (’lang’)
    private final boolean isRetweeted; // is it a retweet? (the object ’retweeted_status’ exists?)
    private final Long retweetedUserId; // [if retweeted] (’retweeted_status’->’user’->’id’)
    private final Long retweetedTweetId; // [if retweeted] (’retweeted_status’->’id’)
    private final long timestampMs; // seconds from epoch (’timestamp_ms’)

    public ExtendedSimplifiedTweet(long tweetId, String text, long userId, String userName,
                                   long followersCount, String language, boolean isRetweeted,
                                   long retweetedUserId, long retweetedTweetId, long timestampMs) {
        // IMPLEMENT ME
        this.tweetId = tweetId;
        this.text = text;
        this.userId = userId;
        this.userName = userName;
        this.followersCount = followersCount;
        this.isRetweeted = isRetweeted;
        this.language = language;
        this.retweetedUserId = retweetedUserId;
        this.retweetedTweetId = retweetedTweetId;
        this.timestampMs = timestampMs;
    }

    /**
     * Returns a {@link ExtendedSimplifiedTweet} from a JSON String.
     * If parsing fails, for any reason, return an {@link Optional#empty()}
     *
     * @param jsonStr
     * @return an {@link Optional} of a {@link ExtendedSimplifiedTweet}
     */
    public static Optional<ExtendedSimplifiedTweet> fromJson(String jsonStr) {
        try {

            JsonElement je = parser.parse(jsonStr);
            JsonObject jo = je.getAsJsonObject();

            long tweetId = jo.get("id").getAsLong();
            String text = jo.get("text").getAsString();
            JsonObject user = jo.getAsJsonObject("user");
            long userId = user.get("id").getAsLong();
            String userName = user.get("name").getAsString();
            long followersCount = user.get("followers_count").getAsLong();
            String language = jo.get("lang").getAsString();
            long timestampMs = jo.get("timestamp_ms").getAsLong();

            boolean isRetweeted = jo.has("retweeted_status");
            Long retweetedUserId = null;
            Long retweetedTweetId = null;

            if (isRetweeted) {
                JsonObject retweetedStatus = jo.getAsJsonObject("retweeted_status");
                if (retweetedStatus != null && retweetedStatus.isJsonObject()) {
                    JsonObject retweetedUser = retweetedStatus.getAsJsonObject("user");
                    if (retweetedUser != null && retweetedUser.isJsonObject()) {
                        retweetedUserId = retweetedUser.has("id") ? retweetedUser.get("id").getAsLong() : null;
                    }
                    retweetedTweetId = retweetedStatus.has("id") ? retweetedStatus.get("id").getAsLong() : null;
                }
            }
            return Optional.of(new ExtendedSimplifiedTweet(tweetId, text, userId, userName,
                    followersCount, language, isRetweeted, retweetedUserId, retweetedTweetId, timestampMs));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "ExtendedSimplifiedTweet{" +
                "tweetId=" + tweetId +
                ", text='" + text + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", followersCount=" + followersCount +
                ", language='" + language + '\'' +
                ", isRetweeted=" + isRetweeted +
                ", retweetedUserId=" + (retweetedUserId != null ? retweetedUserId : "null") +
                ", retweetedTweetId=" + (retweetedTweetId != null ? retweetedTweetId : "null") +
                ", timestampMs=" + timestampMs +
                '}';
    }

    public boolean getRetweeted() {
        return this.isRetweeted;
    }
    public String getLanguage() {return this.language;}
    public String getText() {
        return this.text;
    }

    public long getTweetId(){ return this.tweetId; }

    public long getUserId() { return this.userId; }

    public long getRetweetedUserId() { return this.retweetedUserId; }

    public long getRetweetedTweetId() { return this.retweetedTweetId;}
}











