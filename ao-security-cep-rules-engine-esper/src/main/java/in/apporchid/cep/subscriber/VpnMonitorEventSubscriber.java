package in.apporchid.cep.subscriber;

import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.apporchid.EventSourceType;
import com.apporchid.builder.ActivityBuilder;

import in.apporchid.cep.StartDemo;

/**
 * Wraps Esper Statement and Listener. No dependency on Esper libraries.
 */
@Component
public class VpnMonitorEventSubscriber implements StatementSubscriber {

	private final int timeWindow = 10;
	private final int threshold  = 10;

	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(VpnMonitorEventSubscriber.class);

	/**
	 * {@inheritDoc}
	 */
	public String getStatement() {
		// Example of simple EPL with a Time Window
		return "select count(*) as count from VpnEvent.win:time_batch("+timeWindow+" sec)";
	}

	/**
	 * Listener method called when Esper has detected a pattern match.
	 */
	public void update(Map<String, Integer> eventMap) {

		// average temp over 10 secs

		StringBuilder sb = new StringBuilder();
		sb.append("---------------------------------");
		sb.append("\n- [MONITOR] VPN Access Monitor - total VPN connections " + eventMap.get("count"));
		sb.append("\n---------------------------------");


		StartDemo.fakeBuilder.addActivity(new ActivityBuilder(EventSourceType.CYBER_VPN).withEpochTimeStart(Instant.now())
				.withEpochTimeEnd(Instant.now().minusSeconds(timeWindow))
				.withCount("monitor.all.vpn", eventMap.get("count")).build());
		
		System.err.println(sb.toString());
	}
}
