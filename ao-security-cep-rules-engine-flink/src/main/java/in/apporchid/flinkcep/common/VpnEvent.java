package in.apporchid.flinkcep.common;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class VpnEvent {

	
	private String accountStatusType;
	private String vpnRecord;
	
	private LocalDateTime eventReceivedAt = LocalDateTime.now();
	
	public LocalDateTime getEventReceivedAt() {
		return eventReceivedAt;
	}


	public VpnEvent() {
		super();
	}
	
	
	public VpnEvent(String accountStatusType, String vpnRecord) {
		super();
		this.accountStatusType = accountStatusType;
		this.vpnRecord = vpnRecord;
	}


	public String getAccountStatusType() {
		return accountStatusType;
	}
	public void setAccountStatusType(String accountStatusType) {
		this.accountStatusType = accountStatusType;
	}
	public String getVpnRecord() {
		return vpnRecord;
	}
	public void setVpnRecord(String vpnRecord) {
		this.vpnRecord = vpnRecord;
	}
	@Override
	public String toString() {
		return "VpnEvent =>" + ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
