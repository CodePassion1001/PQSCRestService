package com.proquest.ebd.sc.beans;

public class ShippingListRecord {
	private String caseId;
	private int briefsReceived;
	private String manifest_shipping_id;
	
	public String getManifest_shipping_id() {
		return manifest_shipping_id;
	}
	public void setManifest_shipping_id(String manifest_shipping_id) {
		this.manifest_shipping_id = manifest_shipping_id;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public int getBriefsReceived() {
		return briefsReceived;
	}
	public void setBriefsReceived(int briefsReceived) {
		this.briefsReceived = briefsReceived;
	}
	@Override
	public String toString() {
		return "ShippingListRecord [caseId=" + caseId + ", briefsReceived=" + briefsReceived + ", manifest_shipping_id="
				+ manifest_shipping_id + "]";
	}
	
	
}
