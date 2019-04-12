package net.majakorpi.tweetstats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TweetstatsApplication implements CommandLineRunner { 

	@Autowired
	private TwitterPrinter twp;

	public static void main(String[] args) {
		SpringApplication.run(TweetstatsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		twp.doService();
	}

}

