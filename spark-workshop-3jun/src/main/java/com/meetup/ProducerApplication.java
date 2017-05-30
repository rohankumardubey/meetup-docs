package com.meetup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ProducerApplication implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	Producer producer;
	private static String topicName;
	private static String numberOfEventsPath;
	private static String filePath;

	public static void main(String[] args) {
		topicName = args[0];
		numberOfEventsPath=args[1];
		filePath=args[2];
		SpringApplication.run(ProducerApplication.class, args);


	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

		producer.produce(topicName,numberOfEventsPath,filePath);
	}
}
