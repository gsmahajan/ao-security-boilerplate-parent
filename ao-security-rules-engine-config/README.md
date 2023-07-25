# AW - Security - Rules Engine Config

Powered by
[![AppOrchid](http://www.apporchid.com.php56-19.dfw3-1.websitetestlink.com/wp-content/uploads/2015/12/apporchid-logo.png)](https://nodesource.com/products/nsolid)

Example -> 

Annotation example => 
@AppOrchidStreamProcessingRule("vpn.monitor.all")

``<?xml version="1.0" encoding="UTF-8"?>
<rules>
	<rule source="vpn" name="topic://amwater.liveVpnSourceStream" binding="com.apporchid.kafka.datastream.processor.FakeVpnProcessor" stream="com.apporchid.beans.VpnEvent" sink="topic://amwater.archieveVpnSinkStream" collector="/vpn/">
		<ao-process name="vpn.monitor.all" window="time" last="100" />
		<ao-process name="vpn.monitor.connect" window="time" last="100" />
		<ao-process name="vpn.monitor.disconnect" window="time"	last="100" />
		<ao-process name="vpn.monitor.spike.connect" window="length" last="100" threshold="95" />
		<ao-process name="vpn.monitor.spike.disconnect" window="length" last="100" threshold="95" />
	</rule>

	<rule source="lenel" name="topic://amwater.liveVpnSourceStream" binding="com.apporchid.kafka.datastream.processor.FakeLenelProcessor" stream="com.apporchid.beans.LenelAccessEvent" sink="topic://amwater.archieveLenelSinkStream" collector="/lenel/">
		<ao-process name="lenel.monitor.all" window="time" last="180" />
	</rule>

	<rule source="netflow" name="topic://amwater.liveNetFlowSourceStream" binding="com.apporchid.kafka.datastream.processor.FakeNetFlowProcessor" stream="com.apporchid.beans.NetFlowEvent" sink="topic://amwater.archieveNetFlowSinkStream" collector="/netflow/">
		<ao-process name="net.teed" window="time" last="100" />
		<ao-process name="vpn.monitor.connect" window="time" last="100" />
		<ao-process name="vpn.monitor.disconnect" window="time"	last="100" />
		<ao-process name=" vpn.monitor.spike.connect " window="length" last="100" threshold="95" />
		<ao-process name="vpn.monitor.spike.disconnect" window="length" last="100" threshold="95" />
	</rule>
	
</rules>
``