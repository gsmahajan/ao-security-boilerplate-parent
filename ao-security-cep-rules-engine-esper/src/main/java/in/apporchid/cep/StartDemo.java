package in.apporchid.cep;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.apporchid.builder.ActivityListBuilder;

import in.apporchid.cep.util.RandomLennelAccessGenerator;
import in.apporchid.cep.util.RandomTemperatureEventGenerator;
import in.apporchid.cep.util.RandomVPNAccessGenerator;

/**
 * Entry point for the Demo. Run this from your IDE, or from the command line using 'mvn exec:java'.
 */ 
public class StartDemo {

	/** Logger */ 
	private static Logger LOG = LoggerFactory.getLogger(StartDemo.class);
 
	public static ActivityListBuilder fakeBuilder = new ActivityListBuilder();
	/**
	 * Main method - start the Demo!
	 */
	public static void main(String[] args) throws Exception {

		// Load spring config
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] { "application-context.xml" });
		BeanFactory factory = (BeanFactory) appContext;

		// Start Demo
			RandomTemperatureEventGenerator generator = (RandomTemperatureEventGenerator) factory.getBean("eventGenerator");
			generator.startSendingTemperatureReadings(50000);

		RandomLennelAccessGenerator lenelEventGenerator = (RandomLennelAccessGenerator) factory.getBean("lenelEventGenerator");
		lenelEventGenerator.startSendingLenelRandomAccess();

		RandomVPNAccessGenerator vpnEventGenerator = (RandomVPNAccessGenerator) factory.getBean("vpnEventGenerator");
		vpnEventGenerator.startSendingVpnRandomAccess();


		System.err.println("\n ================================================ "+ new Date());
		
		//System.out.close();
		
	}
}

