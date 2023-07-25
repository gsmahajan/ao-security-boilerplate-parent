package in.apporchid.cep.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apporchid.beans.VpnEvent;

import in.apporchid.cep.handler.VpnAccessEventHandler;

/**
 * Just a simple class to create a number of Random VpnEvent and pass
 * them off to the VpnAccessEventHandler.
 */
@Component
public class RandomVPNAccessGenerator {

	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(RandomTemperatureEventGenerator.class);

	/**
	 * The VpnAccessEventHandler - wraps the Esper engine and processes the Events
	 */
	@Autowired
	private VpnAccessEventHandler vpnAccessEventHandler;

	/**
	 * Creates simple random VPN events and lets the implementation class
	 * handle them.
	 */
	public void startSendingVpnRandomAccess() {

		ExecutorService vpnBombardingExecutor = Executors.newSingleThreadScheduledExecutor();
		vpnBombardingExecutor.submit(new Runnable() {
			long pause = 100;

			public void run() {
				LOG.debug(getStartingMessage());

				while(true) {
					try {
						try {
							for(String vpnRecord :Files.readAllLines(Paths.get("d:\\sample_vpn.csv"), StandardCharsets.UTF_8)) {
								VpnEvent ve = dummyConvert(vpnRecord);
								vpnAccessEventHandler.handle(ve);
								Thread.sleep(pause);
							}
							pause = new Random().nextInt(3300-1000) + 100;
							Thread.sleep(pause);
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
		sb.append("\n* STARTING VPN ACCESS PAYLOADS ");
		sb.append("\n************************************************************\n");
		return sb.toString();
	}


	public static VpnEvent dummyConvert(String vpnRecord) {
		Objects.requireNonNull(vpnRecord, "record can not be null");

		VpnEvent result = new VpnEvent();
		result.setAccountStatusType(vpnRecord.split(",")[1]);
		result.setVpnRecord(vpnRecord);

		if(!vpnRecord.split(",")[1].matches("Start|Stop")) {
			System.out.println("Error record - VPN -> "+vpnRecord);
		}

		return result;
	}
}