package com.proquest.ebd.sc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.proquest.ebd.sc.beans.GenericResponseBean;
import com.proquest.ebd.sc.service.UploadService;
import com.proquest.ebd.util.Constants;



/**
 * This is the Rest Controller class for FileUpload Operation
 * 
 * @author Purujit Saha
 * @version 1.0
 * @since 2017-05-12
 */

@CrossOrigin("*")
@RestController
@RequestMapping("api")
@EnableAsync
public class UploadController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UploadService uploadService;
	
	
	@PostMapping(value = "cases/picklistupload")
	public ResponseEntity<GenericResponseBean> uploadPickListFile(@RequestParam("ufile") MultipartFile ufile,@RequestParam("user") String user) {
		logger.debug("Inside UploadController.uploadPickListFile() ");
		GenericResponseBean metaData = uploadService.processUploadedFile(ufile,Constants.UPLOAD_FILE_TYPE_PICKLIST,user);
	    return new ResponseEntity<GenericResponseBean>(metaData,HttpStatus.OK);
	  } 
	

	@PostMapping(value = "cases/shippinglistupload")
	public ResponseEntity<GenericResponseBean> uploadShippingListFile(@RequestParam("ufile") MultipartFile ufile, @RequestParam("user") String user) {
		logger.debug("Inside UploadController.uploadShippingListFile()");
		GenericResponseBean metaData = uploadService.processUploadedFile(ufile,Constants.UPLOAD_FILE_TYPE_SHIPPING_LIST, user);
	    return new ResponseEntity<GenericResponseBean>(metaData,HttpStatus.OK);
	  } 
	
	
}
