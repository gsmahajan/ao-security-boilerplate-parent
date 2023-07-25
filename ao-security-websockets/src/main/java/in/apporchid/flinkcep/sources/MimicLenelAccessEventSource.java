package in.apporchid.flinkcep.sources;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import com.github.javafaker.Faker;

import in.apporchid.flinkcep.common.LenelAccessEvent;
import in.apporchid.flinkcep.common.LenelAccessEvent.AccessType;
import in.apporchid.flinkcep.monitor.LenelAccessMonitor;

/**
 * Source to generate events. 
 * simulating events from a network elements with random
 * severity. 
 * 
 *
 */
public class MimicLenelAccessEventSource extends RichParallelSourceFunction<LenelAccessEvent> {
	private static final long serialVersionUID = 3589767994783688247L;

	private boolean running = true;

	private final long pause;
	private final double temperatureStd;
	private final double temperatureMean;
	private Random random;

	public MimicLenelAccessEventSource(long pause, double temperatureStd, double temperatureMean) {
		this.pause = pause;
		this.temperatureMean = temperatureMean;
		this.temperatureStd = temperatureStd;
	}

	@Override
	public void open(Configuration configuration) {
		random = new Random();
	}

	public void run(SourceContext<LenelAccessEvent> sourceContext) throws Exception {
		while (running) {
			for(String lenelRecord :Files.readAllLines(Paths.get("D:\\sample.csv"), StandardCharsets.UTF_8)) {
				LenelAccessEvent event = dummyConvert(lenelRecord.split(";")[3]);
				sourceContext.collect(event);
			}
			Thread.sleep(pause);
		}
	}

	public static LenelAccessEvent dummyConvert(String lenelRecord) {
		Objects.requireNonNull(lenelRecord, "record can not be null");

		LenelAccessEvent result = parseRecord(lenelRecord);
		if(RandomUtils.nextBoolean()) {
			result.setAccessInformation(result.getAccessInformation().replaceAll("Granted", "Declined"));
			result.setAccessInformation(result.getAccessInformation().replaceAll("Rao, Sanjay", "Mahajan, Girish"));
			result.setAccessType(AccessType.random());
		}

		return result;
	}

	public void cancel() {
		running = false;
	}

	public static String generateFakeName() {
		Faker faker = new Faker();

		String name = faker.name().fullName();
		// String firstName = faker.name().firstName();
		// String lastName = faker.name().lastName();

		// String streetAddress = faker.address().streetAddress();
		return name;
	}

	public static LenelAccessEvent parseRecord(String lenelRecord) {
		Objects.requireNonNull(lenelRecord, "record can not be null");

		LenelAccessEvent result = new LenelAccessEvent();
		result.setAccessInformation(lenelRecord);		// dump entire record as it is as lenel record, we will let js perform computation over it on client's machine

		boolean accessGranted = StringUtils.containsIgnoreCase(lenelRecord, "Access Granted");

		if(accessGranted) {
			result.setAccessType(StringUtils.containsIgnoreCase(lenelRecord, "Entry")? AccessType.LENEL_ENTER_GRANTED:AccessType.LENEL_EXIT_GRANTED);
		}else {
			result.setAccessType(StringUtils.containsIgnoreCase(lenelRecord, "Exit") ? AccessType.LENEL_EXIT_DECLINE:AccessType.LENEL_ENTER_DECLINED);
		}

		result.setBadgeId(extractBadgeId(lenelRecord));
		result.setAccessTime(extractAccessTimeFromLenelRecord(lenelRecord));
		result.setPersonName(generateFakeName());

		return result;
	}


	public static LocalDateTime extractAccessTimeFromLenelRecord(String lenelRecord) {
		String fixture = lenelRecord.replaceAll(".*Panel	|	Access.*|\\t\\n\\x0B\\f\\r","");
		try {
			try {
				return LocalDateTime.parse(fixture, DateTimeFormatter.ofPattern("d/M/yyyy	hh:mm:ssa"));
			}catch(DateTimeParseException e) {
				return LocalDateTime.parse(fixture, DateTimeFormatter.ofPattern("d/M/yyyy	h:mm:ssa"));
			}
		}catch(Exception e) {
			return LocalDateTime.now();		//FIXME - Girish 001
		}
	}

	public static String extractBadgeId(String record) {
		Objects.requireNonNull(record);
		return record.replaceAll(".* Badge (\\d+)|.*", "$1");
	}

}
