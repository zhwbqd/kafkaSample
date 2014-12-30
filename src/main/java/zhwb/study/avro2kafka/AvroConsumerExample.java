package zhwb.study.avro2kafka;

import kafka.consumer.*;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import zhwb.study.kafka08.KafkaProperties;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * Date: 14/12/31
 * Time: 上午12:33
 *
 * @author jack.zhang
 */
public class AvroConsumerExample {
    public static void main(String[] args) throws UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("zookeeper.connect", KafkaProperties.zkConnect);
        props.put("group.id", "avro-group");
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(
                new ConsumerConfig(props));

        List<KafkaStream<byte[], byte[]>> partitions = consumer.createMessageStreamsByFilter(new Whitelist("avro-topic"));
        for (KafkaStream<byte[], byte[]> partition : partitions) {

            ConsumerIterator<byte[], byte[]> iterator = partition.iterator();
            while (iterator.hasNext()) {
                MessageAndMetadata<byte[], byte[]> next = iterator.next();
                System.out.println("partiton:" + next.partition());
                System.out.println("offset:" + next.offset());
                System.out.println("message:" + new String(next.message(), "utf-8"));
            }
        }
    }
}
