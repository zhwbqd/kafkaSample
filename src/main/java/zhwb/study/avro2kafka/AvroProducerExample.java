package zhwb.study.avro2kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import zhwb.study.avro.User;

import java.util.Properties;


public class AvroProducerExample {

    public static void main(String[] args) throws Exception {
        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        User user = User.newBuilder()
                .setName("James")
                .setFavoriteNumber(10)
                .setFavoriteColor("Black")
                .build();

        DatumWriter<User> datumWriter = new SpecificDatumWriter<>(User.class);
        BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(fos, null);
        datumWriter.write(user, binaryEncoder);
        binaryEncoder.flush();

        String toString = fos.toString();
        System.out.println(toString);
        IOUtils.closeQuietly(fos);

        send2KfK(toString);
    }

    private static void send2KfK(String messageStr) {
        Properties props = new Properties();
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", "localhost:9092");
        // Use random partitioner. Don't need the key type. Just set it to Integer.
        // The message is of type String.
        Producer<String, String> producer = new Producer<>(new ProducerConfig(props));

        producer.send(new KeyedMessage<String,String>("avro-topic", messageStr));
    }
}