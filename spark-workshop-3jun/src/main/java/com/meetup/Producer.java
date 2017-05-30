package com.meetup;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Future;

/**
 * Created by Sunny on 09-04-2016.
 */
@Component
public class Producer {

    //This can be used to get a random number to publish a message to particular partition
    private static Integer partitionNumber = 600000000;
    private static Random random= new Random();

    @Value("${bootstrap.servers}")
    private String bootstrapServer;

    @Value("${zookeeper.servers}")
    private String zookeeperServer;

    @Async
    public void produce(String topic, String numberOfEventsPath, String filePath){

        System.out.println("Topic name " + topic);
        //create the properties for kafka consumer
        Properties properties = new Properties();
        //A list of host/port pairs to use for establishing the initial connection to the Kafka cluster
        properties.put("bootstrap.servers", bootstrapServer);
        properties.put("key.serializer", StringSerializer.class.getCanonicalName());
        //Serializer class for key that implements the Serializer interface
        properties.put("value.serializer", OrderSerializer.class.getCanonicalName());


        KafkaProducer producer = new KafkaProducer(properties);
        try {
                System.out.print("Start kafka producer");
                Scanner input = new Scanner(new File(filePath));
                while (input.hasNextLine()) {
                    while(true) {
                        int numberOfEvents = getNumber(numberOfEventsPath);
                        System.out.println("Number of events " + numberOfEvents);
                        while (numberOfEvents != 0) {
                            String line = input.nextLine();
                            ProducerRecord producerRecord = new ProducerRecord(topic, line);
                            Future<RecordMetadata> recordMetadata = producer.send(producerRecord);
                            //Below statement will hold up until it gets response for record send
                            System.out.println("value produced Person");
                            numberOfEvents--;
                        }
                        Thread.currentThread().sleep(10000);
                    }
                }
                input.close();
        } catch (Exception exception) {
            System.out.println("Exception.. Retrying to publish to the queue directly for topic ");
        }
    }

    private int getNumber(String path) throws Exception{
        Scanner scanner = new Scanner(new File(path));
        int tall = 100;
        int i = 0;
        while(scanner.hasNextInt())
        {
            tall = scanner.nextInt();
            break;
        }
        return tall;
    }
}
