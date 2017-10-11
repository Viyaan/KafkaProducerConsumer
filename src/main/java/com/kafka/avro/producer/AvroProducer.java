package com.kafka.avro.producer;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import com.kafka.avro.model.Employee;

import io.confluent.kafka.serializers.KafkaAvroSerializer;


public class AvroProducer {

	public static void main(String[] args) throws IOException {

		final String topicName = "avro";
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
		props.put("schema.registry.url", "http://localhost:8081");

		Producer<String, Employee> producer = new KafkaProducer<String, Employee>(props);

		Employee avroRecord = new Employee();
		avroRecord.put("eid", 1);
		avroRecord.put("name", "one");
		avroRecord.put("salary", 45000.00);
		avroRecord.put("age", 26);
		avroRecord.put("gender", "Male");
		avroRecord.put("designation", "develeoper2");
		producer.send(new ProducerRecord<String, Employee>(topicName, "employee", avroRecord), new AvroProducerCallback());
		producer.close();
		System.out.println("Data successfully serialized pushed into kafka Broker");
	}
}


class AvroProducerCallback implements Callback{

    @Override
    public  void onCompletion(RecordMetadata recordMetadata, Exception e) {
     if (e != null) {
         System.out.println("AsynchronousProducer failed with an exception");
     }
             else {
                     System.out.println("AsynchronousProducer call Success:" +recordMetadata.partition()  );
             }
    }
}

