package edu.upf;

import edu.upf.parser.SimplifiedTweet;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class TwitterFilterTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    // Place your code here
    @Test
    public void testParseRealTweet() {
        String realTweetJson = "{\"created_at\":\"Sat May 12 22:59:04 +0000 2018\",\"id\":995438237975007233,\"id_str\":\"995438237975007233\",\"text\":\"RT @Philip_Lawrence: Well done Israel. Let the record state I called this weeks ago. #EUROVISION\",\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\/download\\/android\\\" rel=\\\"nofollow\\\"\\u003eTwitter for Android\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":123,\"name\":\"User Name\"},\"lang\":\"en\",\"timestamp_ms\":\"1526164744000\"}";

        Optional<SimplifiedTweet> tweetOpt = SimplifiedTweet.fromJson(realTweetJson);
        assertTrue(tweetOpt.isPresent());
        SimplifiedTweet tweet = tweetOpt.get();

        assertEquals(995438237975007233L, tweet.getId());
        assertEquals("RT @Philip_Lawrence: Well done Israel. Let the record state I called this weeks ago. #EUROVISION", tweet.getText()); // Adjust as per your class's method
        assertEquals("en", tweet.getLanguage());
    }

    @Test
    public void testParseInvalidJSON() {
        String invalidJson = "{\"id\":1097502938476503041,\"text\":\"What a beautiful day\",\"lang\":\"en\"}";
        Optional<SimplifiedTweet> tweetOpt = SimplifiedTweet.fromJson(invalidJson);
        assertFalse(tweetOpt.isPresent());
    }

    @Test
    public void testParseValidJSONWithMissingField() {
        // this JSON lacks lang.
        String missingFieldJson = "{\"created_at\":\"Sat May 12 22:59:04 +0000 2018\",\"id\":995438237975007233,\"id_str\":\"995438237975007233\",\"text\":\"RT @Philip_Lawrence: Well done Israel. Let the record state I called this weeks ago. #EUROVISION\",\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\/download\\/android\\\" rel=\\\"nofollow\\\"\\u003eTwitter for Android\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":123,\"name\":\"User Name\"},\"timestamp_ms\":\"1526164744000\"}";
        Optional<SimplifiedTweet> tweetOpt = SimplifiedTweet.fromJson(missingFieldJson);
        assertFalse(tweetOpt.isPresent());
    }

}
