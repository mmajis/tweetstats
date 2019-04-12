package net.majakorpi.tweetstats;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.kstream.WindowedSerdes;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.WindowStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import net.majakorpi.tweetstats.config.KafkaProps;
import net.majakorpi.tweetstats.config.TwitterProps;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

@Component
public class KafkaStreamTopology {

    @Autowired
    private KafkaProps kafkaProps;

    @Autowired
    private StreamsBuilderFactoryBean sbf;

    private Gson gson = new Gson();

    private static final Logger log = LoggerFactory.getLogger(KafkaStreamTopology.class);

    @Bean("tweetStatsKafkaStreamTopology")
    public KStream<String, String> startProcessing(StreamsBuilder builder) {
        
        KStream<String, String> twt = builder.stream("tweettopic");

        KTable<Windowed<String>, Long> wordcounts = twt.flatMapValues(value -> {
            TweetText txt = gson.fromJson(value, TweetText.class);
            List<String> words = Arrays.asList(txt.text.toLowerCase().split("\\s+"));
            // words.stream().forEach((str) -> log.info(str));
            return words;
        })
        .groupBy((key, value) -> value)
        .windowedBy(TimeWindows.of(300000L)
                               .advanceBy(10000L)
                               .until(300000L))
        .count(Materialized.<String, Long, WindowStore<Bytes, byte[]>>as("wordCountsStore").withValueSerde(Serdes.Long()));
        
        wordcounts.toStream().to("windowedwordcounts", Produced.with(WindowedSerdes.timeWindowedSerdeFrom(String.class), Serdes.Long()));
        
        //twt.to("simplewordcount");
        
        // twt.foreach((key, value) -> {
        //     log.info(value);
        //     // gson.fromJson(value, twitter4j.StatusJSONImpl.class);
        // });
        //split tweet by words
        //write each word to next stream
        //count words
        
        // twt.groupByKey().
        // KTable<Windowed<byte[]>, Long> longCounts = 
        // twt.groupByKey()
        //       .count(TimeWindows.of(10000L)
        //                         .advanceBy(1000L)
        //                         .until(10000L),
        //              "long-counts");
        // final KStream<String, Long> toSquare = builder.stream("toSquare",
        // Consumed.with(Serdes.String(), Serdes.Long()));
        // toSquare.map((key, value) -> { // do something with each msg, square the
        // values in our case
        // return KeyValue.pair(key, value * value);
        // }).to("squared", Produced.with(Serdes.String(), Serdes.Long())); // send
        // downstream to another topic

        return twt;
    }
}

class TweetText {
    public String text;
}