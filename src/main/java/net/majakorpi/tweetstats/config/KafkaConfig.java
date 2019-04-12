package net.majakorpi.tweetstats.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.Serdes.StringSerde;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaConfig {

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs(KafkaProps props) {
        Map<String, Object> config = new HashMap<>();
        // config.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-deduplication");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, props.bootstrapBrokerString);
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "tweetStats");
        // config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // config.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
        // FailOnInvalidTimestamp.class);
        // config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG,
        // StreamsConfig.EXACTLY_ONCE);
        // config.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 30000);
        // config.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, threads);
        // config.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, replicationFactor);
        // config.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
        // LogAndContinueExceptionHandler.class);
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, StringSerde.class);
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, StringSerde.class);
        return new KafkaStreamsConfiguration(config);
    }

    // @Bean
    // public StreamsBuilderFactoryBean tweetStatstreamBuilderFactoryBean(KafkaStreamsConfiguration config) {
    //     return new StreamsBuilderFactoryBean(config);
    // }

    @Bean
    public KafkaProducer<String, String> getKafkaProducer(KafkaProps props) {
        Properties producerProperties = new Properties();
        System.out.println("boootstrap: " + props.bootstrapBrokerString);
        producerProperties.put("bootstrap.servers", props.bootstrapBrokerString);
        producerProperties.put("acks", "all");
        producerProperties.put("retries", 0);
        producerProperties.put("batch.size", 16384);
        producerProperties.put("linger.ms", 1);
        producerProperties.put("buffer.memory", 33554432);
        producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(producerProperties);
    }
}
