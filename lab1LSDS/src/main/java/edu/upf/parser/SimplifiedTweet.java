package edu.upf.parser;

import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class SimplifiedTweet {

  // All classes use the same instance
  private static JsonParser parser = new JsonParser();



  private final long tweetId;			  // the id of the tweet ('id')
  private final String text;  		      // the content of the tweet ('text')
  private final long userId;			  // the user id ('user->id')
  private final String userName;		  // the user name ('user'->'name')
  private final String language;          // the language of a tweet ('lang')
  private final long timestampMs;		  // seconduserIds from epoch ('timestamp_ms')

  public SimplifiedTweet(long tweetId, String text, long userId, String userName,
                         String language, long timestampMs) {

    this.tweetId = tweetId;
    this.text = text;
    this.userId = userId;
    this.userName = userName;
    this.language = language;
    this.timestampMs = timestampMs;
    // PLACE YOUR CODE HERE!

  }

  /**
   * Returns a {@link SimplifiedTweet} from a JSON String.
   * If parsing fails, for any reason, return an {@link Optional#empty()}
   *
   * @param jsonStr
   * @return an {@link Optional} of a {@link SimplifiedTweet}
   */
  public static Optional<SimplifiedTweet> fromJson(String jsonStr) {
    try {

      JsonElement je = JsonParser.parseString(jsonStr);
      JsonObject jo = je.getAsJsonObject();

      long tweetId = jo.get("id").getAsLong();
      String text = jo.get("text").getAsString();

      long userId = 0; // Default value
      String userName = null;

      if (jo.has("user")) {
        JsonObject userObj = jo.getAsJsonObject("user");
        if (userObj.has("id")) {
          userId = userObj.get("id").getAsLong();
        }
        if (userObj.has("name")) {
          userName = userObj.get("name").getAsString();
        }
      }

      String language = jo.get("lang").getAsString();
      long timestampMs = jo.get("timestamp_ms").getAsLong();

      if (tweetId > 0 && !text.isEmpty() && userId > 0 && userName != null && !language.isEmpty() && timestampMs > 0) {
        return Optional.of(new SimplifiedTweet(tweetId, text, userId, userName, language, timestampMs));
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      // Parsing failed
      return Optional.empty();
    }
  }

  @Override
  public String toString() {
    return "SimplifiedTweet{" +
            "tweetId=" + tweetId +
            ", text='" + text + '\'' +
            ", userId=" + userId +
            ", userName='" + userName + '\'' +
            ", language='" + language + '\'' +
            ", timestampMs=" + timestampMs +
            '}';
    //return new Gson().toJson(this);
  }

  public String getLanguage(){
    return this.language;
  }
  public String getText() {
    return this.text;
  }
  public long getId() {
    return this.tweetId;
  }

}