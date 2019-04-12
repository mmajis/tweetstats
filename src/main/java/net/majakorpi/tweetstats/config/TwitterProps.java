package net.majakorpi.tweetstats.config;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("twitter4j")
public class TwitterProps {

    Logger log = LoggerFactory.getLogger(this.getClass());
    
    public String oauthConsumerKey;

    public String oauthConsumerSecret;

    public String oauthAccessToken;

    public String oauthAccessTokenSecret;

    public boolean isDebug;

    public double[][] tweetStreamLocations;

    /**
     * @return the oauthConsumerKey
     */
    public String getOauthConsumerKey() {
        return oauthConsumerKey;
    }

    /**
     * @param oauthConsumerKey the oauthConsumerKey to set
     */
    public void setOauthConsumerKey(String oauthConsumerKey) {
        this.oauthConsumerKey = oauthConsumerKey;
    }

    /**
     * @return the oauthConsumerSecret
     */
    public String getOauthConsumerSecret() {
        return oauthConsumerSecret;
    }

    /**
     * @param oauthConsumerSecret the oauthConsumerSecret to set
     */
    public void setOauthConsumerSecret(String oauthConsumerSecret) {
        this.oauthConsumerSecret = oauthConsumerSecret;
    }

    /**
     * @return the oauthAccessToken
     */
    public String getOauthAccessToken() {
        return oauthAccessToken;
    }

    /**
     * @param oauthAccessToken the oauthAccessToken to set
     */
    public void setOauthAccessToken(String oauthAccessToken) {
        this.oauthAccessToken = oauthAccessToken;
    }

    /**
     * @return the oauthAccessTokenSecret
     */
    public String getOauthAccessTokenSecret() {
        return oauthAccessTokenSecret;
    }

    /**
     * @param oauthAccessTokenSecret the oauthAccessTokenSecret to set
     */
    public void setOauthAccessTokenSecret(String oauthAccessTokenSecret) {
        this.oauthAccessTokenSecret = oauthAccessTokenSecret;
    }

    /**
     * @return the isDebug
     */
    public boolean isDebug() {
        return isDebug;
    }

    /**
     * @param isDebug the isDebug to set
     */
    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    /**
     * @return the tweetStreamLocations
     */
    public double[][] getTweetStreamLocations() {
        return tweetStreamLocations;
    }

    /**
     * @param tweetStreamLocations the tweetStreamLocations to set
     */
    public void setTweetStreamLocations(String tweetStreamLocations) {
        log.info(tweetStreamLocations);
        double[] doubles = Stream.of(tweetStreamLocations.split(",")).mapToDouble(Double::parseDouble).toArray();
        List<double[]> tuples = new ArrayList<>();
        double[] acc = new double[2];
        int count = 0;
        for (double d : doubles) {
            acc[count++] = d;
            if (count > 1) {
                tuples.add(acc);
                acc = new double[2];
                count = 0;
            }
        }
        this.tweetStreamLocations = tuples.toArray(new double[0][]);
    }
}
