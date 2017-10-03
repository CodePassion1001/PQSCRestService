package com.proquest.ebd.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.proquest.ebd.sc.beans.SupremeCourtEmailPOJO;

@Component
public class EmailHelper implements Constants {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Environment env;

	@Autowired
	private JavaMailSender mailSender;

	public void send(SupremeCourtEmailPOJO emailPojo) {
		SimpleMailMessage message = new SimpleMailMessage();
		// Setting Sender Email
		message.setFrom(env.getProperty(EMAIL_SENDER));
		
		// Setting Receiver Email
		String userEmail = emailPojo.getToEmail();
		if (userEmail != null && userEmail.length() > 0) {
			message.setTo(userEmail);
		} else {
			message.setTo(env.getProperty(EMAIL_RECEIVER).split(","));
			message.setCc(env.getProperty(EMAIL_RECEIVER_CC).split(","));
		}
		// Setting the Email Subject Line
		message.setSubject(emailPojo.getSubjectLine());
		
		message.setText(createMailBodyText(emailPojo));
		mailSender.send(message);
		
	}

	private String createMailBodyText(SupremeCourtEmailPOJO emailPojo) {
		StringBuilder strBuilder = new StringBuilder();

		
		// Total Records in Spreed Sheet
		int totalRecords = emailPojo.getTotalRecords();
		
		// Total Processed
		int totalProcessed = emailPojo.getTotalProcessed();
		
		// Total Duplicate
		int duplicateCount = emailPojo.getDuplicateCount();
		
		// Total Failed.
		int failedCount = emailPojo.getFailedCount();
		
		// Writing individual Section to the email
		if (totalRecords > 0) {
			strBuilder.append("Record found on uploaded Spread Sheet :" + totalRecords + "\n");
		}
		if (totalProcessed >= 0) {
			strBuilder.append("Total Record Processed :" + totalProcessed + "\n");
		}
		if (duplicateCount > 0) {
			strBuilder.append("Duplicate Records ( Have not Processed ) :" + duplicateCount + "\n\n");
			strBuilder.append("The Following Records are Duplicates\n");
			for (String dupId : emailPojo.getDupList()) {
				strBuilder.append(dupId + "\n");
			}
		}
		if (failedCount > 0) {
			strBuilder.append("Total failed Records :" + failedCount + "\n");
		}
		if (duplicateCount > 0) {
			strBuilder.append("Total Duplicate count :" + duplicateCount + "\n");
			
		}
		if(emailPojo.getErrorMessage()!=null && emailPojo.getErrorMessage().length()>0){
			strBuilder.append(emailPojo.getErrorMessage());
		}
		if(emailPojo.getFailedList()!=null && emailPojo.getFailedList().size()>0){
			strBuilder.append("Failed to Processed the follwing Records\n\n");
			for (String failed : emailPojo.getFailedList()) {
				strBuilder.append(failed + "\n");
			}
		}
		return strBuilder.toString();
	}

	
	
	public void sendPickListRepost(String fileAbsolutePath, String email) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(env.getProperty(EMAIL_SENDER));
			
			if(email!=null && email.length()>0)
				helper.setTo(email);
			else
				helper.setTo(env.getProperty(EMAIL_RECEIVER).split(","));
			
			helper.setSubject(env.getProperty(PICKLIST_CREATE_EMAIL_SUBJECT));
			if(fileAbsolutePath!=null && fileAbsolutePath.length()>0){
				helper.setText("PickList Report as Attachment");
				FileSystemResource file = new FileSystemResource(fileAbsolutePath);
				helper.addAttachment(file.getFilename(), file);
			}else{
				helper.setText("PickList Report generation Failed. Check with Dev team.");
			}
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mailSender.send(message);
		
	}

	
}
