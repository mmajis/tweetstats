package net.majakorpi.tweetstats.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class TwitterConfig {

  @Bean
  public Twitter getTwitterClient(TwitterProps props) {
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setDebugEnabled(props.isDebug).setOAuthConsumerKey(props.oauthConsumerKey)
        .setOAuthConsumerSecret(props.oauthConsumerSecret)
        .setOAuthAccessToken(props.oauthAccessToken)
        .setOAuthAccessTokenSecret(props.oauthAccessTokenSecret);
    TwitterFactory tf = new TwitterFactory(cb.build());
    return tf.getInstance();
  }

  @Bean
  public TwitterStream getTwitterStream(TwitterProps props) {
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setDebugEnabled(props.isDebug).setOAuthConsumerKey(props.oauthConsumerKey)
        .setOAuthConsumerSecret(props.oauthConsumerSecret)
        .setOAuthAccessToken(props.oauthAccessToken)
        .setOAuthAccessTokenSecret(props.oauthAccessTokenSecret);
      return new TwitterStreamFactory(cb.build()).getInstance();
  }
}