package com.proquest.ebd.sc.beans;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class SearchFilterBean {
	private String fullCaseNo;
	private String caseTitle;
	private String manifestID;
	private String courtTerm;
	private String combinedCase;
	private String orderListDate;
	private int fieldCount;
	
	
	public int getFieldCount() {
		return fieldCount;
	}
	public void setFieldCount(int fieldCount) {
		this.fieldCount = fieldCount;
	}
	public String getOrderListDate() {
		return orderListDate;
	}
	public void setOrderListDate(String orderListDate) {
		this.orderListDate = orderListDate;
	}
	private String contentType;
		
	public String getFullCaseNo() {
		return fullCaseNo;
	}
	public void setFullCaseNo(String fullCaseNo) {
		this.fullCaseNo = fullCaseNo;
	}
	public String getCaseTitle() {
		return caseTitle;
	}
	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}
	public String getManifestID() {
		return manifestID;
	}
	public void setManifestID(String manifestID) {
		this.manifestID = manifestID;
	}
	public String getCourtTerm() {
		return courtTerm;
	}
	public void setCourtTerm(String courtTerm) {
		this.courtTerm = courtTerm;
	}
	public String getCombinedCase() {
		return combinedCase;
	}
	public void setCombinedCase(String combinedCase) {
		this.combinedCase = combinedCase;
	}
	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	private Date formatDate(String strDate) {

		Date date = new Date();
		String pattern = "MM/dd/yyyy";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			date = format.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	@Override
	public String toString() {
		return "SearchFilterBean [fullCaseNo=" + fullCaseNo + ", caseTitle=" + caseTitle + ", manifestID=" + manifestID
				+ ", courtTerm=" + courtTerm + ", combinedCase=" + combinedCase + ", orderListDate=" + orderListDate
				+ ", contentType=" + contentType + "]";
	}
	
	
	
}
