package com.proquest.ebd.sc.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value="prototype")
public class GenericResponseBean implements ResponseDTO{
	
	private String fileName;
	private String size;
	private String status;
	private String failureReason;
	private int totalProcessed;
	private int totalDuplicate;
	private String message;
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	public int getTotalProcessed() {
		return totalProcessed;
	}
	public void setTotalProcessed(int totalProcessed) {
		this.totalProcessed = totalProcessed;
	}
	public int getTotalDuplicate() {
		return totalDuplicate;
	}
	public void setTotalDuplicate(int totalDuplicate) {
		this.totalDuplicate = totalDuplicate;
	}
	
	

}
