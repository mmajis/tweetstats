package net.majakorpi.tweetstats.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("kafka")
public class KafkaProps {
    
    public String bootstrapBrokerString;

    /**
     * @return the bootstrapBrokerString
     */
    public String getBootstrapBrokerString() {
        return bootstrapBrokerString;
    }

    /**
     * @param bootstrapBrokerString the bootstrapBrokerString to set
     */
    public void setBootstrapBrokerString(String bootstrapBrokerString) {
        this.bootstrapBrokerString = bootstrapBrokerString;
    }

}
