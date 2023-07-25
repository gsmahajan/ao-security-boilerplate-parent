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
public class LenelMonitorEventSubscriber implements StatementSubscriber {
	
    /** Logger */
    private static Logger LOG = LoggerFactory.getLogger(LenelMonitorEventSubscriber.class);

    /**
     * {@inheritDoc}
     */
    public String getStatement() {
        // Example of simple EPL with a Time Window
    	return "select count(*) as count from LenelAccessEvent.win:time_batch(10 sec)";
    }

    /**
     * Listener method called when Esper has detected a pattern match.
     */
    public void update(Map<String, Integer> eventMap) {

        // average temp over 10 secs

        StringBuilder sb = new StringBuilder();
        sb.append("---------------------------------");
        sb.append("\n- [MONITOR] Lenel Access Monitor (Count of how many access since last 10 seconds) " + eventMap.get("count"));
        sb.append("\n---------------------------------");
        

		StartDemo.fakeBuilder.addActivity(new ActivityBuilder(EventSourceType.LENEL).withEpochTimeStart(Instant.now())
				.withEpochTimeEnd(Instant.now().minusSeconds(10))
				.withCount("monitor.standard.lenel", eventMap.get("count")).build());
        
        
        System.err.println(sb.toString());
    }
}
