package com.apporchid.beans;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class VpnEvent {

	
	private String accountStatusType;
	private String vpnRecord;
	public VpnEvent() {
		super();
		// TODO Auto-generated constructor stub
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
