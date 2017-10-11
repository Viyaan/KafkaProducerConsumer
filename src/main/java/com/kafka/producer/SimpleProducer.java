package com.kafka.producer;
import java.util.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
public class SimpleProducer {
  
   public static void main(String[] args) throws Exception{
           
      String topicName = "one";
	  String key = "Key1";
	  String value = "{\r\n" + 
	  		"    \"id\": 1,\r\n" + 
	  		"    \"name\": \"A green door\",\r\n" + 
	  		"    \"price\": 12.50,";
      
      Properties props = new Properties();
      props.put("bootstrap.servers", "localhost:9092");
      props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");         
      props.put("value.serializer", StringSerializer.class);
	        
      Producer<String, String> producer = new KafkaProducer <String, String>(props);
	
	  ProducerRecord<String, String> record = new ProducerRecord<String, String>(topicName,key,value);
	  
	  for(int i=1;i<=100000;i++) {
		  producer.send(record);	
		  System.out.println(record);
	  }
	       
      producer.close();
	  
	  System.out.println("SimpleProducer Completed.");
   }
}