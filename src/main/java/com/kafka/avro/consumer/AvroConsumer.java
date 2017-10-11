package com.kafka.avro.consumer;



import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.kafka.avro.model.Employee;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;


public class AvroConsumer {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args)  {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
		props.put("schema.registry.url", "http://localhost:8081");
		props.put("specific.avro.reader", "true");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,KafkaAvroDeserializer.class);

		String topic = "avro";
		
		KafkaConsumer<String,Employee> consumer = new  KafkaConsumer<String,Employee>(props);
		consumer.subscribe(Arrays.asList(topic));
		try {
			while(true) {
				ConsumerRecords<String,Employee> records = consumer.poll(0);
				for(ConsumerRecord<String,Employee>record:records) {
					System.out.println(record.value().getEid() + " "+record.value().getSalary());
				}
			}
		}finally {
			consumer.close();
		}

	}
}
