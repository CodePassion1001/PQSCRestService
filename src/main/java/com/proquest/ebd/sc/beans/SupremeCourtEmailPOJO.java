package com.proquest.ebd.sc.beans;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value="prototype")
public class SupremeCourtEmailPOJO {
	// Total Records in Spreed Sheet
	private int totalRecords;
	// Total Processed
	private int totalProcessed;
	// Total Duplciate
	private int duplicateCount;
	// Total Failed.
	private int failedCount;
	
	private ArrayList<String> dupList;
	
	private ArrayList<String> failedList;
	
	private String toEmail;
	private String subjectLine;
	
	private String errorMessage;
	
	public int getTotalProcessed() {
		return totalProcessed;
	}
	public void setTotalProcessed(int totalProcessed) {
		this.totalProcessed = totalProcessed;
	}
	public int getDuplicateCount() {
		return duplicateCount;
	}
	public void setDuplicateCount(int duplicateCount) {
		this.duplicateCount = duplicateCount;
	}
	public int getFailedCount() {
		return failedCount;
	}
	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}
	
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	public ArrayList<String> getDupList() {
		return dupList;
	}
	public void setDupList(ArrayList<String> dupList) {
		this.dupList = dupList;
	}
	public String getToEmail() {
		return toEmail;
	}
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	public String getSubjectLine() {
		return subjectLine;
	}
	public void setSubjectLine(String subjectLine) {
		this.subjectLine = subjectLine;
	}
	public ArrayList<String> getFailedList() {
		return failedList;
	}
	public void setFailedList(ArrayList<String> failedList) {
		this.failedList = failedList;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
