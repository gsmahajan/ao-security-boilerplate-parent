package com.apporchid.aosecurityfakeproducer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.bouncycastle.util.Strings;

public class FakeEventsProducer {

	enum OutputType {
		TEXT_FILE, CSV_FILE, XSL_FILE, EVENT_STREAM
	};

	String description;
	Long nbEvents;
	String fakeRecordSampleSource;
	Map<String, String> regexCollections;
	OutputType expectOutputType;

	public FakeEventsProducer(String description, Long nbEvents, String fakeRecordSampleSource,
			Map<String, String> regexCollections, OutputType expectOutputType) {
		super();
		this.description = description;
		this.nbEvents = nbEvents;
		this.fakeRecordSampleSource = fakeRecordSampleSource;
		this.regexCollections = regexCollections;
		this.expectOutputType = expectOutputType;
	}

	public FakeEventsProducer() {
		super();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getNbEvents() {
		return nbEvents;
	}

	public void setNbEvents(Long nbEvents) {
		this.nbEvents = nbEvents;
	}

	public String getFakeRecordSampleSource() {
		return fakeRecordSampleSource;
	}

	public void setFakeRecordSampleSource(String fakeRecordSampleSource) {
		this.fakeRecordSampleSource = fakeRecordSampleSource;
	}

	public Map<String, String> getRegexCollections() {
		return regexCollections;
	}

	public void setRegexCollections(Map<String, String> regexCollections) {
		this.regexCollections = regexCollections;
	}

	public OutputType getExpectOutputType() {
		return expectOutputType;
	}

	public void setExpectOutputType(OutputType expectOutputType) {
		this.expectOutputType = expectOutputType;
	}

	public static void main(String[] args) throws IOException {
		FakeEventsProducer events = new FakeEventsProducer();
		events.setDescription("Lenel Sample Fake Records");
		events.setFakeRecordSampleSource("OnGuard 7.2	All Events Over Time	QUERY:  START DATE:  2/1/2018 12:00:00 AM;   END DATE:  2/6/2018 11:59:59 PM;   Daily Time Range of 00:00:00 - 23:59:59;   Badge {@@badgeId@@:java.lang.Long}	Report Date:  2/6/2018 11:55:53AM Eastern Standard Time	Date/Time	Event	Details	Device	Panel	2/5/2018	 3:36:39PM	Access Granted	{@@badgeId@@:java.lang.Long} (0): {@@fullName@@:java.lang.String}	{@@doorName@@:java.lang.String}	{@@premiseName@@:java.lang.String}				Total Events:  33	All Events Over Time	OnGuard 7.2	Page	1");
		events.setRegexCollections(FakeProducerUtil.toRegexCollections(events.getFakeRecordSampleSource()));
		events.setNbEvents(10000L);

		events.setExpectOutputType(OutputType.CSV_FILE);

		manufactureEvents(events);
	}

	public static void manufactureEvents(FakeEventsProducer foo) throws IOException {

		File file = new File ("c:\\foo\\"+System.nanoTime()+".csv");
		System.out.println("Temporary file has been created - "+file.getAbsolutePath() + " - "+file.getName());
		String tempRecord;

		//Here true is to append the content to file
		FileWriter fw = new FileWriter(file,true);
		//BufferedWriter writer give better performance
		BufferedWriter bw = new BufferedWriter(fw);

		for(long x = 0; x<foo.getNbEvents(); ++x) {
			tempRecord = FakeProducerUtil.fillTempRecord(foo);
			bw.write(tempRecord + "\n");
		}
		bw.close();
		
	}


}

class FakeProducerUtil {

	public static String fillTempRecord(FakeEventsProducer foo) {
		String result = new String(foo.getFakeRecordSampleSource());
		for(String q: foo.getRegexCollections().keySet()) {
			result = result.replaceAll("\\{@@"+q+"@@" , rand(q, foo.getRegexCollections().get(q)));
			result.replaceAll("java\\.lang\\.String\\}", "");		//FIXME
			result.replaceAll("java\\.lang\\.Long\\}", "");			//FIXME
		}
		return result;
	}

	private static String rand(String field, String type) {		//FIXME
		return RandomUtils.nextLong(33324, 349494)+"_"+ System.currentTimeMillis() + "_"+ System.nanoTime();
	}

	public static Map<String, String> toRegexCollections(String source) {
		Map<String, String> result = new HashMap<String, String>();
		Arrays.asList(Strings.split(source, '{')).stream().forEach(val -> {
			if (val.contains("@@")) {
				result.put(val.replaceAll("\\}.*", "").split(":")[0].replaceAll("@@", ""),
						val.replaceAll("\\}.*", "").split(":")[1]);
			}
		});
		return result;
	}
}
