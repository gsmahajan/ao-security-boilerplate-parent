package in.apporchid.cep.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.apporchid.beans.VpnEvent;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

import in.apporchid.cep.StartDemo;
import in.apporchid.cep.subscriber.StatementSubscriber;

/**
 * This class handles incoming Vpn Events. It processes them through the EPService, to which
 * it has attached the 3 queries.
 */
@Component
@Scope(value = "singleton")
public class VpnAccessEventHandler implements InitializingBean{

	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(LenelAccessEventHandler.class);

	/** Esper service */
	private EPServiceProvider epService;

	private EPStatement monitorEventStatement;
	private EPStatement monitorConnectEventStatement;
	private EPStatement monitorDisconnectEventStatement;
	private EPStatement warningConnectEventStatement;
	private EPStatement warningDisconnectEventStatement;


	@Autowired
	@Qualifier("vpnMonitorEventSubscriber")
	private StatementSubscriber vpnMonitorEventSubscriber;

	@Autowired
	@Qualifier("vpnMonitorConnectEventSubscriber")
	private StatementSubscriber vpnMonitorConnectEventSubscriber;

	@Autowired
	@Qualifier("vpnMonitorDisconnectEventSubscriber")
	private StatementSubscriber vpnMonitorDisconnectEventSubscriber;


	@Autowired
	@Qualifier("vpnWarningDisconnectEventSubscriber")
	private StatementSubscriber vpnWarningDisconnectEventSubscriber;

	@Autowired
	@Qualifier("vpnWarningConnectEventSubscriber")
	private StatementSubscriber vpnWarningConnectEventSubscriber;


	/**
	 * Configure Esper Statement(s).
	 */
	public void initService() {

		LOG.debug("Initializing Servcie ..");
		Configuration config = new Configuration();
		config.addEventTypeAutoName("com.apporchid.beans");
		epService = EPServiceProviderManager.getDefaultProvider(config);

		//data stream watchers
		createVpnMonitorEventSubscriber();
		createVpnMonitorConnectEventSubscriber();
		createVpnMonitorDisconnectEventSubscriber();
		createVpnWarningConnectEventSubscriber();
		createVpnWarningDisconnectEventSubscriber();
	}


	private void createVpnMonitorEventSubscriber() {
		monitorEventStatement = epService.getEPAdministrator().createEPL(vpnMonitorEventSubscriber.getStatement());
		monitorEventStatement.setSubscriber(vpnMonitorEventSubscriber);
	}

	private void createVpnMonitorConnectEventSubscriber() {
		monitorConnectEventStatement = epService.getEPAdministrator().createEPL(vpnMonitorConnectEventSubscriber.getStatement());
		monitorConnectEventStatement.setSubscriber(vpnMonitorConnectEventSubscriber);
	}
	private void createVpnMonitorDisconnectEventSubscriber() {
		monitorDisconnectEventStatement = epService.getEPAdministrator().createEPL(vpnMonitorDisconnectEventSubscriber.getStatement());
		monitorDisconnectEventStatement.setSubscriber(vpnMonitorDisconnectEventSubscriber);
	}
	private void createVpnWarningConnectEventSubscriber() {
		warningConnectEventStatement = epService.getEPAdministrator().createEPL(vpnWarningConnectEventSubscriber.getStatement());
		warningConnectEventStatement.setSubscriber(vpnWarningConnectEventSubscriber);
	}

	private void createVpnWarningDisconnectEventSubscriber() {
		warningDisconnectEventStatement = epService.getEPAdministrator().createEPL(vpnWarningDisconnectEventSubscriber.getStatement());
		warningDisconnectEventStatement.setSubscriber(vpnWarningDisconnectEventSubscriber);
	}
	/**
	 * Handle the incoming LenelAccessEvent.
	 */
	public void handle(VpnEvent event) {

		LOG.debug(event.toString());
		epService.getEPRuntime().sendEvent(event);
		StartDemo.fakeBuilder.attachVpnEvent(event);
	}

	@Override
	public void afterPropertiesSet() {

		LOG.debug("Configuring..");
		initService();
	}
}
