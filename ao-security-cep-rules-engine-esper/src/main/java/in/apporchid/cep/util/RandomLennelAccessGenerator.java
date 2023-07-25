package in.apporchid.cep.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apporchid.beans.LenelAccessEvent;
import com.apporchid.beans.LenelAccessEvent.AccessType;
import com.github.javafaker.Faker;

import in.apporchid.cep.handler.LenelAccessEventHandler;

/**
 * Just a simple class to create a number of Random TemperatureEvents and pass
 * them off to the TemperatureEventHandler.
 */
@Component
public class RandomLennelAccessGenerator {

	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(RandomTemperatureEventGenerator.class);

	/**
	 * The TemperatureEventHandler - wraps the Esper engine and processes the Events
	 */
	@Autowired
	private LenelAccessEventHandler lenelEventHandler;

	public static String generateFakeName() {
		Faker faker = new Faker();

		String name = faker.name().fullName();
		// String firstName = faker.name().firstName();
		// String lastName = faker.name().lastName();

		// String streetAddress = faker.address().streetAddress();
		return name;
	}

	/**
	 * Creates simple random Temperature events and lets the implementation class
	 * handle them.
	 */
	public void startSendingLenelRandomAccess() {

		ExecutorService lenelBombardingExecutors = Executors.newSingleThreadScheduledExecutor();
		lenelBombardingExecutors.submit(new Runnable() {
			long pause = 1000;

			public void run() {

				LOG.debug(getStartingMessage());
				while(true) {
					try {
						try {
							for(String lenelRecord :Files.readAllLines(Paths.get("C:\\sample.csv"), StandardCharsets.UTF_8)) {
								LenelAccessEvent ve = dummyConvert(lenelRecord.split(";")[3]);
								lenelEventHandler.handle(ve);
								Thread.sleep(pause);
								pause = new Random().nextInt(3300-1000) + 100;

							}
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							LOG.error("Thread Interrupted", e);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private String getStartingMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\n************************************************************");
		sb.append("\n* STARTING LENEL ACCESS CONTROL PAYLOADS ");
		sb.append("\n************************************************************\n");
		return sb.toString();
	}


	public static void main(String[] args) {
		for(String format: Arrays.asList("2/6/2018	 12:09:42AM", "2/6/2018	 11:49:42AM", "2/6/2018	 9:09:42AM", "2/6/2018	 11:29:42AM", "2/6/2018	 7:09:42PM"))
			System.out.println(LocalDateTime.parse(format, DateTimeFormatter.ofPattern("d/M/yyyy	 h:mm:ssa")));
	}

	public static void mainq(String[] args) throws Exception{
		for(String lenelRecord :Files.readAllLines(Paths.get("C:\\sample.csv"), StandardCharsets.UTF_8)) {
			LenelAccessEvent e = dummyConvert(lenelRecord.split(";")[3]);
			System.err.println(e);
		}

	}

	public static LenelAccessEvent dummyConvert(String lenelRecord) {
		Objects.requireNonNull(lenelRecord, "record can not be null");

		LenelAccessEvent result = parseRecord(lenelRecord);
		if(RandomUtils.nextBoolean()) {
			result.setAccessInformation(result.getAccessInformation().replaceAll("Granted", "Declined"));
			result.setAccessInformation(result.getAccessInformation().replaceAll("Rao, Sanjay", "Mahajan, Girish"));
		}

		return result;
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
				LogManager.getRootLogger().error(e, e.getCause());
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