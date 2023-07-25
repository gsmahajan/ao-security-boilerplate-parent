package in.apporchid.cep.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.apporchid.EventSourceType;
import com.apporchid.beans.LenelAccessEvent;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

import in.apporchid.cep.StartDemo;
import in.apporchid.cep.subscriber.StatementSubscriber;

/**
 * This class handles incoming LenelAccess Events. It processes them through the EPService, to which
 * it has attached the 3 queries.
 */
@Component
@Scope(value = "singleton")
public class LenelAccessEventHandler implements InitializingBean{

	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(LenelAccessEventHandler.class);

	/** Esper service */
	private EPServiceProvider epService;
	private EPStatement criticalEventStatement;
	private EPStatement monitorEventStatement;

	@Autowired
	@Qualifier("lenelCriticalEventSubscriber")
	private StatementSubscriber lenelCriticalEventSubscriber;

	@Autowired
	@Qualifier("lenelMonitorEventSubscriber")
	private StatementSubscriber lenelMonitorEventSubscriber;

	/**
	 * Configure Esper Statement(s).
	 */
	public void initService() {

		LOG.debug("Initializing Servcie ..");
		Configuration config = new Configuration();
		config.addEventTypeAutoName("com.apporchid.beans");
		epService = EPServiceProviderManager.getDefaultProvider(config);

		//createCriticalLenelAccessCheckExpression();
		createLenelAccessMonitorExpression();

	}

	/**
	 * EPL to check for a sudden critical rise across 4 events, where the last event is 1.5x greater
	 * than the first event. This is checking for a sudden, sustained escalating rise in the
	 * LenelAccess
	 */
//	private void createCriticalLenelAccessCheckExpression() {
//
//		LOG.debug("create Critical LenelAccess Check Expression");
//		criticalEventStatement = epService.getEPAdministrator().createEPL(lenelCriticalEventSubscriber.getStatement());
//		criticalEventStatement.setSubscriber(lenelCriticalEventSubscriber);
//	}

	/**
	 * EPL to monitor the average LenelAccess every 10 seconds. Will call listener on every event.
	 */
	private void createLenelAccessMonitorExpression() {

		LOG.debug("create Timed Average Monitor");
		monitorEventStatement = epService.getEPAdministrator().createEPL(lenelMonitorEventSubscriber.getStatement());
		monitorEventStatement.setSubscriber(lenelMonitorEventSubscriber);
	}

	/**
	 * Handle the incoming LenelAccessEvent.
	 */
	public void handle(LenelAccessEvent event) {

		LOG.debug(event.toString());
		epService.getEPRuntime().sendEvent(event);
		StartDemo.fakeBuilder.attachLenelEvent(event);
	}

	@Override
	public void afterPropertiesSet() {

		LOG.debug("Configuring..");
		initService();
	}
}
