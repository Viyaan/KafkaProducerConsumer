package com.kafka.avro.consumer;



import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.avro.generic.IndexedRecord;
import org.apache.kafka.common.errors.SerializationException;

import io.confluent.kafka.serializers.KafkaAvroDecoder;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.utils.VerifiableProperties;


public class AvroConsumer {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Properties props = new Properties();
		props.put("zookeeper.connect", "localhost:2181");
		props.put("group.id", "group1");
		props.put("schema.registry.url", "http://localhost:8081");

		String topic = "avro";
		Map<String, Integer> topicCountMap = new HashMap<>();
		topicCountMap.put(topic, new Integer(1));

		VerifiableProperties vProps = new VerifiableProperties(props);
		KafkaAvroDecoder keyDecoder = new KafkaAvroDecoder(vProps);
		KafkaAvroDecoder valueDecoder = new KafkaAvroDecoder(vProps);

		ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(props));

		Map<String, List<KafkaStream<Object, Object>>> consumerMap = consumer.createMessageStreams(
		    topicCountMap, keyDecoder, valueDecoder);
		KafkaStream stream = consumerMap.get(topic).get(0);
		ConsumerIterator it = stream.iterator();
		while (it.hasNext()) {
		  MessageAndMetadata messageAndMetadata = it.next();
		  try {
		    String key = (String) messageAndMetadata.key();
		    IndexedRecord value = (IndexedRecord) messageAndMetadata.message();

		    
		  } catch(SerializationException e) {
		    // may need to do something with it
		  }
		}
	}
}