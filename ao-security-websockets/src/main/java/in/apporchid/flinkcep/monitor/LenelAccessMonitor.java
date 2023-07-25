package in.apporchid.flinkcep.monitor;

import java.time.ZoneOffset;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;

import com.example.websocketdemo.model.SurveillanceGossipMessage;
import com.example.websocketdemo.model.SurveillanceGossipMessage.SeverityType;

import in.apporchid.flinkcep.common.LenelAccessEvent;
import in.apporchid.flinkcep.common.LenelAccessEvent.AccessType;
import in.apporchid.flinkcep.sources.MimicLenelAccessEventSource;

public class LenelAccessMonitor {

	public static final Stack<SurveillanceGossipMessage> broker = new Stack<SurveillanceGossipMessage>();  

	private static final long PAUSE = 40000;
	private static final double TEMP_STD = 20;
	private static final double TEMP_MEAN = 80;

	public static void begin() throws Exception {

		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		env.setParallelism(1);
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

		// Input stream of alarm events, event creation time is take as timestamp
		// Setting the Watermark to same as creation time of the event.
		DataStream<LenelAccessEvent> inputEventStream = env
				.addSource(new MimicLenelAccessEventSource(PAUSE, TEMP_STD, TEMP_MEAN))
				.assignTimestampsAndWatermarks(new AssignerWithPunctuatedWatermarks<LenelAccessEvent>() {

					@Override
					public long extractTimestamp(LenelAccessEvent event, long currentTimestamp) {
						return event.getEventReceivedAt().toEpochSecond(ZoneOffset.UTC);
					}

					@Override
					public Watermark checkAndGetNextWatermark(LenelAccessEvent lastElement, long extractedTimestamp) {
						return new Watermark(extractedTimestamp);
					}
				});

		//Continuously prints the input events
		inputEventStream.print();    

		Pattern<LenelAccessEvent, ?> lenelPattern = Pattern.<LenelAccessEvent>begin("first")
				.where(evt -> evt.getAccessType() == AccessType.LENEL_ENTER_GRANTED)
				/*.where(evt -> StringUtils.equals(evt.getBadgeId(),"172467435"))*/
				.within(Time.seconds(30));


		DataStream<String> result = CEP.pattern(inputEventStream, lenelPattern).
				select(new PatternSelectFunction<LenelAccessEvent, String>() {
					public String select(Map<String, LenelAccessEvent> pattern) {
						StringBuilder builder = new StringBuilder();

						builder.append(pattern.get("first").toString());
						System.err.println("Gotcha => " + builder);

						SurveillanceGossipMessage gossipMessage = new SurveillanceGossipMessage();
						gossipMessage.setContent("Event occured - "+RandomStringUtils.randomAlphanumeric(10)+builder);
						gossipMessage.setSeverityType(SeverityType.MONITOR);
						gossipMessage.setSender(String.valueOf(RandomUtils.nextDouble()));

						//broker.push(gossipMessage);							// plan 2 

						return builder.toString();
					}
				});
		env.execute("Lenel monitoring job");
	}
}