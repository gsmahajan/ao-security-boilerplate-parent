package com.apporchid.aosecurityfakeproducer;

import java.time.LocalDateTime;
import java.util.Properties;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;

import uk.co.jemos.podam.api.PodamFactoryImpl;


@EnableBinding(Processor.class)
@SpringBootApplication
public class AoSecurityFakeProducerApplication {

	private final static String BOOTSTRAP_SERVERS = "192.168.100.1:9092";
	private final static String TOPIC = "foopipe";

	public static void main(String[] args)throws Exception {
		SpringApplication.run(AoSecurityFakeProducerApplication.class, args);
		if (args.length == 0) {
			runProducer(5);
		} else {
			runProducer(Integer.parseInt(args[0]));
		}
	}

	@ServiceActivator(inputChannel=Sink.INPUT)
	public void loggerSink(VPNPojo payload) {
		System.out.println("Received: " + payload);
	}


	private static Producer<Long, String> createProducer() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				BOOTSTRAP_SERVERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "AppOrchidFakeSecurityEventsProducer");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class.getName());
		return new KafkaProducer<>(props);
	}

	static void runProducer(final int sendMessageCount) throws Exception {
		final Producer<Long, String> producer = createProducer();
		long time = System.currentTimeMillis();
		try {
			for (long index = time; index < time + sendMessageCount; index++) {

				final ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC, index, new PodamFactoryImpl().manufacturePojo(VPNPojo.class).toString());
				RecordMetadata metadata = producer.send(record).get();
				long elapsedTime = System.currentTimeMillis() - time;
				System.out.printf("sent record(key=%s value=%s) " +
						"meta(partition=%d, offset=%d) time=%d\n",
						record.key(), record.value(), metadata.partition(),
						metadata.offset(), elapsedTime);
			}
		} finally {
			producer.flush();
			producer.close();
		}
	}
}

class FakeProducer{

	@StreamListener
	@Input(Processor.INPUT)
	public static void in(@Input(Processor.INPUT) VPNPojo  input) {

	}

	@StreamListener
	@Output(Processor.OUTPUT)
	public static void send(@Input(Processor.INPUT) VPNPojo input,
			@Output(Processor.OUTPUT) VPNPojo output) {
	}
}


class VPNPojo {
	LocalDateTime eventArrivedAT = LocalDateTime.now();
	String vpnInformation;

	public VPNPojo(String vpnInformation) {
		super();
		this.vpnInformation = vpnInformation;
	}

	public VPNPojo() {
		super();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}