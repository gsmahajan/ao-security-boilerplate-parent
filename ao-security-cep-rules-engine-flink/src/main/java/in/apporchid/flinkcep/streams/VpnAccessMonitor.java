package in.apporchid.flinkcep.streams;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.serialization.TypeInformationSerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.typeutils.TypeInfoParser;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer08;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer08;
import org.apache.flink.util.Collector;

public class VpnAccessMonitor {

	private static DataStream<String> getTextDataStream(StreamExecutionEnvironment env) {
		return env.readTextFile("D:\\sample-vpn.csv");
	}

	public static void begin(String[] args) throws Exception {

		StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(1);

		env.enableCheckpointing(5000); // checkpoint every 5000 msecs
		env.setParallelism(1);

		env.getConfig().disableSysoutLogging();
		env.setRestartStrategy(RestartStrategies.noRestart());

		String timeCharacteristic = "EventTime";
		if (args.length > 0) {
			timeCharacteristic = args[0];
		}
		if (timeCharacteristic.equals("EventTime")) {
			env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		} else if (timeCharacteristic.equals("ProcessingTime")) {
			env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
		} else if (timeCharacteristic.equals("IngestionTime")) {
			env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
		}


		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", System.getProperty("awsecurity.kafka.brokers","localhost:9092"));
		properties.setProperty("zookeeper.connect", System.getProperty("awsecurity.kafka.zookeepers","localhost:2181"));
		properties.setProperty("group.id", "awsecurity");
		properties.setProperty("log.dir", "d:\\tmp\\foo");

		TypeInformation<Tuple3<String, String, String>> longStringType = TypeInfoParser.parse("Tuple3<String, String, String>>");

		TypeInformationSerializationSchema<Tuple3<String, String, String>> sourceSchema = new TypeInformationSerializationSchema<>(longStringType, env.getConfig());

		FlinkKafkaConsumer08<String> kafkaSource = new FlinkKafkaConsumer08<String>("topic", new SimpleStringSchema(), properties);
		kafkaSource.setStartFromEarliest();
		DataStream<String> source = env.addSource(kafkaSource);
		source.print();


		DataStream<String> vpnEvents = getTextDataStream(env);

		//vpnEvents.print();

		DataStream<Tuple3<String, String, String>> inputEventStream = vpnEvents.flatMap(new VpnLineSplitter());    

		Pattern<Tuple3<String, String, String>, ?> vpnPattern = Pattern.<Tuple3<String, String, String>>begin("first")
				.where(new IterativeCondition<Tuple3<String,String,String>>() {

					private static final long serialVersionUID = 6983017051219526358L;

					@Override
					public boolean filter(Tuple3<String, String, String> arg0, Context<Tuple3<String, String, String>> arg1)
							throws Exception {
						return arg0.f1.matches("Start");
					}
				}).within(Time.seconds(1));


		DataStream<String> result = CEP.pattern(inputEventStream, vpnPattern).
				select(new PatternSelectFunction<Tuple3<String, String, String>, String>() {

					private static final long serialVersionUID = -5942005603152490602L;

					@Override
					public String select(Map<String, List<Tuple3<String, String, String>>> pattern) throws Exception {
						StringBuilder builder = new StringBuilder();
						builder.append(pattern.get("first").toString());
						System.out.println(builder);
						return builder.toString();
					}
				});

		//Iterator<Tuple2<String, Integer>> myOutput = DataStreamUtils.collect(result);
		result.print();
		result.addSink(new FlinkKafkaProducer08<>("localhost:9092", "vpnTopic", new SimpleStringSchema()));

		env.execute("VPN access");
	}

	public static void main(String[] args) throws Exception {
		new VpnAccessMonitor().begin(args);
	}
}


class VpnLineSplitter implements FlatMapFunction<String, Tuple3<String, String, String>> {

	private static final long serialVersionUID = 1L;

	@Override
	public void flatMap(String value, Collector<Tuple3<String, String, String>> out) {
		String[] tokens = value.split(",");
		out.collect(new Tuple3<String, String, String>(tokens[0], tokens[1], value.substring(tokens[0].length() + tokens[1].length() + 2)));
	}
}