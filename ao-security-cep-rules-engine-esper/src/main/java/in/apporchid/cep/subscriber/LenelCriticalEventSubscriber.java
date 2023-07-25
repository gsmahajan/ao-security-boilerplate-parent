package in.apporchid.cep.subscriber;

import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.apporchid.EventSourceType;
import com.apporchid.beans.LenelAccessEvent;
import com.apporchid.builder.ActivityBuilder;

import in.apporchid.cep.StartDemo;

/**
 * Wraps Esper Statement and Listener. No dependency on Esper libraries.
 */
@Deprecated
@Component
public class LenelCriticalEventSubscriber implements StatementSubscriber {

	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(LenelCriticalEventSubscriber.class);

	/**
	 * {@inheritDoc}
	 */
	public String getStatement() {

		// Example using 'Match Recognise' syntax.
		//		String crtiticalEventExpression = "select * from LenelAccessEvent "
		//				+ "match_recognize ( "
		//				+ "       measures A as temp1, B as temp2, C as temp3, D as temp4 "
		//				+ "       pattern (A B C D) " 
		//				+ "       define "
		//				+ "               A as A.temperature > " + CRITICAL_EVENT_THRESHOLD + ", "
		//				+ "               B as (A.temperature < B.temperature), "
		//				+ "               C as (B.temperature < C.temperature), "
		//				+ "               D as (C.temperature < D.temperature) and D.temperature > (A.temperature * " + CRITICAL_EVENT_MULTIPLIER + ")" + ")";
		//String intrusionEventExpression = "select * from LenelAccessEvent where accessType like "+ AccessType.LENEL_EXIT_DECLINE;
		String intrusionEventExpression = "select * from LenelAccessEvent where accessInformation like '%Granted%'";


		return intrusionEventExpression;
	}

	/**
	 * Listener method called when Esper has detected a pattern match.
	 */
	public void update(Map<String, LenelAccessEvent> eventMap) {

		// 1st Temperature in the Critical Sequence
		LenelAccessEvent temp1 = (LenelAccessEvent) eventMap.get("stream_1");

		StringBuilder sb = new StringBuilder();
		sb.append("***************************************");
		sb.append("\n* [ALERT] : CRITICAL EVENT DETECTED! ");
		sb.append(temp1);
		sb.append("\n***************************************");

//		StartDemo.fakeBuilder.addActivity(new ActivityBuilder(EventSourceType.LENEL).withEpochTimeStart(Instant.now())
//				.withEpochTimeEnd(Instant.now().minusSeconds(10))
//				.withCount("alert.standard.lenel", eventMap.size()).build());
//		
		LOG.debug(sb.toString());
	}


}
