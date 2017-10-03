package com.proquest.ebd.sc.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.proquest.ebd.sc.beans.GenericResponseBean;
import com.proquest.ebd.sc.service.ReportExportService;

/**
 * This is the Rest Controller class for FileDownload Operation
 * 
 * @author Purujit Saha
 * @version 1.0
 * @since 2017-06-01
 */
@CrossOrigin("*")
@RestController
@RequestMapping("api")
public class DownloadController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ReportExportService reportExporter;

	/**
	 * Sending the Picklist as email
	 * 
	 * @param courtTerm
	 * @param contentType
	 * @param manifestId
	 * 
	 */
	@GetMapping(value = "/report/email/picklist/")
	@ResponseBody
	public ResponseEntity<GenericResponseBean> emailPickListFile(@RequestParam("courtTerm") String courtTerm,
			@RequestParam("contentType") String contentType, /*@RequestParam("manifestId") String manifestId,*/@RequestParam("user") String user) {
		GenericResponseBean genericResponse = reportExporter.sendReport(courtTerm, contentType, /*manifestId, */user);
		return new ResponseEntity<GenericResponseBean>(genericResponse, HttpStatus.OK);
	}

	/**
	 * Sending the picklist as attachment
	 * 
	 * @param courtTerm
	 * @param contentType
	 * @param manifestId
	 * @return
	 */
	@GetMapping(value = "/report/download/picklist/")
	@ResponseBody
	public ResponseEntity<InputStreamResource> downloadPickListFile(@RequestParam("courtTerm") String courtTerm,
			@RequestParam("contentType") String contentType, @RequestParam("manifestId") String manifestId) {

		HttpHeaders respHeaders = new HttpHeaders();
		respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		respHeaders.setContentDispositionFormData("attachment", "picklist.xlsx");

		InputStreamResource isr = null;
		try {
			isr = new InputStreamResource(
					new FileInputStream(reportExporter.getPickListReport(courtTerm, contentType, manifestId)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
	}

	/*
	 * @GetMapping(value = "/report/shippinglist/")
	 * 
	 * @ResponseBody public ResponseEntity<InputStreamResource>
	 * getShippingListFile() { HttpHeaders respHeaders = new HttpHeaders();
	 * respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	 * respHeaders.setContentDispositionFormData("attachment", "picklist.xlsx");
	 * 
	 * InputStreamResource isr = null; try { isr = new InputStreamResource(new
	 * FileInputStream(downloadService.getPickListReport())); } catch
	 * (FileNotFoundException e) { e.printStackTrace(); } return new
	 * ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK); }
	 */

}
