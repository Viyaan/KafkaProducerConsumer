package com.kafka.consumer;
import java.util.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.*;
/**
 * @author Viyaan
 * Responsibility of this class
 * 1) Maintain a list of offsets that are processed and ready to be committed. 
 *    i.e Consumer need to maintain list of offset instead of relying on the current offset that are maange by kafka
 * 2) Commit the offsets when partitions are going away.
 *
 * When new consumer added/crashed from the consumer group following things can happen
 * A rebalance will be triggered
 * Kafka will revoke all partitions,  onPartitionsRevoked() calls here 
 * New partition assignment, onPartitionsAssigned() calls here
 */
public class RebalanceListner implements ConsumerRebalanceListener {
    private KafkaConsumer consumer;
    private Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap();

    public RebalanceListner(KafkaConsumer con){
        this.consumer=con;
    }
    
    public void addOffset(String topic, int partition, long offset){
        currentOffsets.put(new TopicPartition(topic, partition),new OffsetAndMetadata(offset,"Commit"));
    }
    
    public Map<TopicPartition, OffsetAndMetadata> getCurrentOffsets(){
        return currentOffsets;
    }
    
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        System.out.println("Following Partitions Assigned ....");
        for(TopicPartition partition: partitions)                
            System.out.println(partition.partition()+",");
    }

    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        System.out.println("Following Partitions Revoked ....");
        for(TopicPartition partition: partitions)                
            System.out.println(partition.partition()+",");
                
        
        System.out.println("Following Partitions commited ...." );
        for(TopicPartition tp: currentOffsets.keySet())
            System.out.println(tp.partition());
        
        consumer.commitSync(currentOffsets);
        currentOffsets.clear();
    }
}