package com.proquest.ebd.util;

public interface Constants {

	String SUCCESS = "success";
	String FAILURE = "failed";

	// Constants for PickList template header
	String PICKLIST_FULLCASENO 	= 	"Full Case No.";
	String PICKLIST_CASETITLE 		= 	"CASE TITLE";
	String PICKLIST_COURTTERMDATE 	=	"Court Term Date";
	String PICKLIST_CONTENTTYPE 	= 	"Content Type";
	String PICKLIST_COMBINEDCASE 	= 	"COMBINED CASE";
	String PICKLIST_ORDERLISTDATE 	= 	"Order List Date";
	String PICKLIST_CASEYEAR	 	= 	"Case Year";
	String PICKLIST_CASENO		 	= 	"Case No.";
	String PICKLIST_WITHTEXT	 	= 	"With Text";
	String PICKLIST_MANIFEXTID	 	= 	"Manifest ID";
	String PICKLIST_BRIEF_PER_CASE	= 	"No. Briefs/Case";
	String PICKLIST_BRIEF_RECV	 	= 	"No. Briefs Rec'd";
	String PICKLIST_NOTES 	= 	"Notes";

	// Constants for ShippingList header
	String SHIPPING_LIST_FULLCASE_ID = "Full Case No.";
	String SHIPPING_LIST_BRIEF_RECV = "No. Briefs Rec'd";


	String EMAIL_SENDER="spring.mail.username";
	String EMAIL_RECEIVER="smtp.server.receiver";
	String EMAIL_RECEIVER_CC="smtp.server.receiver.cc";
	String PICKLIST_UPLOAD_EMAIL_SUBJECT = "picklist.upload.email.subject";
	String SHIPPINGLIST_UPLOAD_EMAIL_SUBJECT = "shippinglist.upload.email.subject";
	String PICKLIST_CREATE_EMAIL_SUBJECT = "picklist.create.email.subject";

	String REPORT_DOWNLOAD_LOCATION = "paths.localtion.reports";

	String UPLOAD_FILE_TYPE_PICKLIST = "picklist";
	String UPLOAD_FILE_TYPE_SHIPPING_LIST="shippingList";
	String DEFAULT_PICKLIST_EXP_FILE_NAME="PICKLIST";
	String DATE_FORMAT_YYYYMMDD_HHMMSS="yyyyMMdd_HHmmss";
	
	// Max records to be returned by search service
	// Ideally UI will send a max value, but if UI sends more than 200 as max value, we will use this to reset max value.
	Integer MAX_RECORDS_RETURN_CONSTANT = 250;
	
}
