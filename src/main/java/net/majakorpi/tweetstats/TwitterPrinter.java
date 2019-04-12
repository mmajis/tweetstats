package net.majakorpi.tweetstats;

import com.google.gson.Gson;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

import net.majakorpi.tweetstats.config.TwitterProps;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

@Service
public class TwitterPrinter {

    @Autowired
    private TwitterStream twitterStream;
    @Autowired
    private TwitterProps twitterProps;
    @Autowired
    private KafkaProducer<String, String> kafkaProducer;
    @Autowired
    private StreamsBuilderFactoryBean sbf;
    @Autowired
    private StreamsBuilder builder;

    private static Logger log = LoggerFactory.getLogger(TwitterPrinter.class);

    public void doService() {
        System.out.println("service!");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ReadOnlyWindowStore<String, Long> store = sbf.getKafkaStreams().store("wordCountsStore", QueryableStoreTypes.windowStore());

        twitterStream.addListener(new StatusListener() {

            @Override
            public void onException(Exception ex) {

            }

            @Override
            public void onStatus(Status status) {
                Gson gson = new Gson();
                String json = gson.toJson(status);
                System.out.println(json);
                System.out.println(
                        "[" + status.getUser().getName() + ", " + status.getPlace().getName() + "]" + status.getText());
                kafkaProducer.send(new ProducerRecord<>("tweettopic", json));

                // KeyValueIterator<Windowed<String>, Long> iter = store.all();
                // while (iter.hasNext()) {
                //     KeyValue<Windowed<String>, Long> next = iter.next();
                //     System.out.println("count for " + next.key + ": " + next.value);
                // }
                long now = System.currentTimeMillis();
                
                log.info("count of ja: " + store.fetch("ja", 0, now).next());
                log.info("count of se: " + store.fetch("se", now - 1000, now).next().value);
                log.info("count of ei: " + store.fetch("ei", now - 1000, now).next().value);
                log.info("count of joka: " + store.fetch("joka", now - 1000, now).next().value);
                log.info("count of että: " + store.fetch("että", now - 1000, now).next().value);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {

            }

            @Override
            public void onStallWarning(StallWarning warning) {

            }

        });
        System.out.println(this.twitterProps.tweetStreamLocations);
        twitterStream.filter(new FilterQuery().locations(this.twitterProps.tweetStreamLocations));
        // twitterStream.filter(new
        // FilterQuery().track("trump").locations(this.twitterProps.tweetStreamLocations));
    }
}