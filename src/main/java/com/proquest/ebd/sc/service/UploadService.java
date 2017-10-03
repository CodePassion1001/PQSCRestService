package com.proquest.ebd.sc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.proquest.ebd.sc.beans.GenericResponseBean;
import com.proquest.ebd.sc.beans.ShippingListRecord;
import com.proquest.ebd.sc.beans.SupremeCourtEmailPOJO;
import com.proquest.ebd.sc.dao.ICaseDAO;
import com.proquest.ebd.sc.dao.IUserDAO;
import com.proquest.ebd.sc.dao.ManifestsDAO;
import com.proquest.ebd.sc.model.Cases;
import com.proquest.ebd.sc.model.Manifests;
import com.proquest.ebd.util.Constants;
import com.proquest.ebd.util.EmailHelper;
import com.proquest.ebd.util.FileFormatValidator;
import com.proquest.ebd.util.PickListProcessor;
import com.proquest.ebd.util.ShippingListProcessor;

@Service
public class UploadService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EmailHelper emailHelper;

	@Autowired
	private GenericResponseBean docMeta;
	
	@Autowired
	private SupremeCourtEmailPOJO emailPojo;
	
	@Autowired
	private FileFormatValidator validator;

	@Autowired
	private PickListProcessor pickListProc;

	@Autowired
	private ShippingListProcessor shippingListProc;


	@Autowired
	private ICaseDAO caseDao;

	@Autowired
	private IUserDAO userDao;
	
	@Autowired
	private Environment env;

	@Autowired
	private ManifestsDAO manifestDao;

	public GenericResponseBean processUploadedFile(MultipartFile ufile, String uploadType, String userEmail) {
		// validate the file
		docMeta = validator.validateFormat(docMeta, ufile);

		if (docMeta.getStatus().equalsIgnoreCase(Constants.SUCCESS)) {
			processFile(ufile, uploadType, userEmail, docMeta);
		} else {
			return docMeta;
		}
		docMeta.setMessage("The Upload is in Progress!, You will get an email once completed.");
		return docMeta;
	}

	
	@Async
	public void processFile(MultipartFile ufile, String uploadType, String user,GenericResponseBean docMeta) {
		ArrayList<String> dupList = new ArrayList<String>();
		ArrayList<String> failedList = new ArrayList<String>();
		int totalCasesUploaded = 0;
		int totalProcessedCount = 0;
		int duplicateCount = 0;
		int failedCount =0;
		String userEmail = userDao.findByUserName(user).getEmail();
		if(userEmail==null && userEmail.length()==0){
			userEmail = env.getProperty(Constants.EMAIL_RECEIVER);
		}
		String subjectStatus = "";

		switch (uploadType) {

		case Constants.UPLOAD_FILE_TYPE_PICKLIST: {
			HashMap<String, Object> mapObj = pickListProc.importPickList(docMeta, ufile);
			docMeta = (GenericResponseBean) mapObj.get("docMeta");
			if (docMeta.getStatus().equalsIgnoreCase(Constants.SUCCESS)) {
				List<Cases> caseList = (List<Cases>) mapObj.get("caseRecords");
				for (Cases nCase : caseList) {
					// CasesToManifests casesToManifest = (CasesToManifests)
					// nCase.getCasesToManifestList().toArray()[0];
					totalCasesUploaded++;
					if (nCase.getCaseId() != null) {
						if (caseDao.getCaseByCaseId(nCase.getCaseId()) == null) {
							caseDao.addCase(nCase);
							totalProcessedCount++;
						} else {
							dupList.add(nCase.getCaseId());
							duplicateCount++;
						}
					}
				}
				docMeta.setTotalProcessed(totalProcessedCount);
				docMeta.setTotalDuplicate(duplicateCount);
				if (totalProcessedCount == 0) {
					subjectStatus = " Failed";
				} else {
					subjectStatus = " Successfully Completed";
				}
			}
			emailPojo.setSubjectLine(env.getProperty(Constants.PICKLIST_UPLOAD_EMAIL_SUBJECT) + subjectStatus);
			break;
		}
		case Constants.UPLOAD_FILE_TYPE_SHIPPING_LIST: {
			HashMap<String, Object> mapObj1 = shippingListProc.importShippingList(docMeta, ufile);
			String manifestId = "";
			if (!mapObj1.isEmpty()) {
				docMeta = (GenericResponseBean) mapObj1.get("docMeta");
				if (docMeta.getStatus().equalsIgnoreCase(Constants.SUCCESS)) {
					List<ShippingListRecord> shippingRcdList = (List<ShippingListRecord>) mapObj1.get("shippingList");
					
					manifestId = shippingRcdList.get(0).getManifest_shipping_id();
					Manifests existingManifest = manifestDao.find(manifestId);
					if(existingManifest!=null){
						emailPojo.setErrorMessage("Duplicate ManifestShippingID not allowed! Already Exist ID ==>"+manifestId);
						docMeta.setStatus(Constants.FAILURE);
						docMeta.setFailureReason("Manifests_Shipping_ID already exists ==>" + manifestId);
					
					}else{
						ArrayList<Cases> listOfCases = new ArrayList<Cases>();
						for (ShippingListRecord shippingRecord : shippingRcdList) {
							totalCasesUploaded++;
							if (shippingRecord.getCaseId() != null) {
								Cases existingCase = caseDao.getCaseByCaseId(shippingRecord.getCaseId());
								if (existingCase != null) {
									totalProcessedCount++;
									int newBriefs = existingCase.getBriefsPerCase() + shippingRecord.getBriefsReceived();
									existingCase.setBriefsReceived(newBriefs);
									listOfCases.add(existingCase);									
								} else {
									failedCount++;
									failedList.add(shippingRecord.getCaseId());
								}
							}
						}
						Manifests manifest = new Manifests();
						manifest.setManifest_shipping_Id(manifestId);
						manifest.setCaseslist(listOfCases);
						manifestDao.addManifest(manifest);
						docMeta.setTotalProcessed(totalProcessedCount);
						docMeta.setTotalDuplicate(duplicateCount);
						
					}
			} else {
				docMeta.setStatus(Constants.FAILURE);
			}
			if (totalProcessedCount == 0) {
				subjectStatus = " Failed";
			} else {
				subjectStatus = " Successfully Completed";
			}
			emailPojo.setSubjectLine(env.getProperty(Constants.SHIPPINGLIST_UPLOAD_EMAIL_SUBJECT) + subjectStatus);
			break;
			}
			}
		}
		emailPojo.setTotalRecords(totalCasesUploaded);
		emailPojo.setTotalProcessed(totalProcessedCount);
		emailPojo.setDuplicateCount(duplicateCount);
		emailPojo.setDupList(dupList);
		emailPojo.setToEmail(userEmail);

		emailHelper.send(emailPojo);

		//return CompletableFuture.completedFuture(docMeta);
		}
		}
