package in.apporchid.cep.subscriber;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Wraps Esper Statement and Listener. No dependency on Esper libraries.
 */
@Component
public class TemperatureMonitorEventSubscriber implements StatementSubscriber {

    /** Logger */
    private static Logger LOG = LoggerFactory.getLogger(TemperatureMonitorEventSubscriber.class);

    /**
     * {@inheritDoc}
     */
    public String getStatement() {

        // Example of simple EPL with a Time Window
        return "select avg(temperature) as avg_val from TemperatureEvent.win:time_batch(5 sec)";
    }

    /**
     * Listener method called when Esper has detected a pattern match.
     */
    public void update(Map<String, Double> eventMap) {

        // average temp over 10 secs
        Double avg = (Double) eventMap.get("avg_val");

        StringBuilder sb = new StringBuilder();
        sb.append("---------------------------------");
        sb.append("\n- [MONITOR] What is an average temperature in last 5 seconds =>  " + avg);
        sb.append("\n---------------------------------");

        System.err.println(sb.toString());
    }
}
