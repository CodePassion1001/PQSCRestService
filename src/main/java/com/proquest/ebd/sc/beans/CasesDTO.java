package com.proquest.ebd.sc.beans;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

@Component
public class CasesDTO implements ResponseDTO{
	private Set<String> manifestIds = new HashSet<String>();
	private String fullCaseId;
	private String courtTermDate;
	private int termNumber;
	private String categoryType;
	private String title;
	private String combinedCase;
	@JsonFormat(pattern="yyyy-MM-dd",locale = "pt-us", timezone = "America/New_York")
	private Date date;
	@JsonFormat(pattern="yyyy-MM-dd",locale = "pt-us", timezone = "America/New_York")
	private Date orderListDate;
	private int year;
	private int caseNumber;
	private String withText;
	private int briefsPerCase;
	private int briefsReceived;
	private String notes;
	
	
	public Set<String> getManifestIds() {
		return manifestIds;
	}
	public void setManifestIds(Set<String> manifestIds) {
		this.manifestIds = manifestIds;
	}
	
	
	public String getFullCaseId() {
		return fullCaseId;
	}
	public void setFullCaseId(String fullCaseId) {
		this.fullCaseId = fullCaseId;
	}
	public String getCourtTermDate() {
		return courtTermDate;
	}
	public void setCourtTermDate(String courtTermDate) {
		this.courtTermDate = courtTermDate;
	}
	public int getTermNumber() {
		return termNumber;
	}
	public void setTermNumber(int termNumber) {
		this.termNumber = termNumber;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCombinedCase() {
		return combinedCase;
	}
	public void setCombinedCase(String combinedCase) {
		this.combinedCase = combinedCase;
	}
	public Date getOrderListDate() {
		return orderListDate;
	}
	public void setOrderListDate(Date orderListDate) {
		this.orderListDate = orderListDate;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(int caseNumber) {
		this.caseNumber = caseNumber;
	}
	public String getWithText() {
		return withText;
	}
	public void setWithText(String withText) {
		this.withText = withText;
	}
	public int getBriefsPerCase() {
		return briefsPerCase;
	}
	public void setBriefsPerCase(int briefsPerCase) {
		this.briefsPerCase = briefsPerCase;
	}
	public int getBriefsReceived() {
		return briefsReceived;
	}
	public void setBriefsReceived(int briefsReceived) {
		this.briefsReceived = briefsReceived;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	@Override
	public String toString() {
		return "CasesDTO [manifestIds=" + manifestIds.iterator().toString() + ", fullCaseId=" + fullCaseId + ", courtTermDate="
				+ courtTermDate + ", termNumber=" + termNumber + ", categoryType=" + categoryType + ", title=" + title
				+ ", combinedCase=" + combinedCase + ", orderListDate=" + orderListDate + ", year=" + year
				+ ", caseNumber=" + caseNumber + ", withText=" + withText + ", briefsPerCase=" + briefsPerCase
				+ ", briefsReceived=" + briefsReceived + ", notes=" + notes + "]";
	}
	
}
