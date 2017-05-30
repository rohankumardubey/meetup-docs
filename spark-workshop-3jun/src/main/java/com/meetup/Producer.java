package com.meetup;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.Random;
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
    public void produce(String topic, String path){

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
            while(true) {
                int numberOfEvents = getNumber(path);
                System.out.println("Number of events " + numberOfEvents);
                while(numberOfEvents != 0) {
                        //lets create new order randomly assign city randomly


                        // Below are there different ways for producing records

                        ProducerRecord producerRecord = new ProducerRecord(topic, getNextJsonData());

                        Future<RecordMetadata> recordMetadata = producer.send(producerRecord);
                        //Below statement will hold up until it gets response for record send
                        System.out.println("value produced Person");
                        numberOfEvents--;
                }
                Thread.currentThread().sleep(10000);
            }
        } catch (Exception exception) {
            System.out.println("Exception.. Retrying to publish to the queue directly for topic ");
        }
    }

    private String getNextJsonData() {
        int randomNumber = random.nextInt(partitionNumber);
        return Integer.toString(randomNumber);
    }

    private int getNumber(String path) throws Exception{
        /*Scanner scanner = new Scanner(new File(path));
        int tall = 100;
        int i = 0;
        while(scanner.hasNextInt())
        {
            tall = scanner.nextInt();
            break;
        }
        return tall;*/
        return 10;
    }
}
