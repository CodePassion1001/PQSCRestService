package com.proquest.ebd.sc.service;

import java.io.File;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.proquest.ebd.sc.beans.GenericResponseBean;
import com.proquest.ebd.sc.dao.ICaseDAO;
import com.proquest.ebd.sc.dao.IUserDAO;
import com.proquest.ebd.sc.model.Cases;
import com.proquest.ebd.util.Constants;
import com.proquest.ebd.util.PickListProcessor;

@Service
public class ReportExportService implements Constants {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Environment env;

	@Autowired
	private PickListProcessor picklistProc;

	@Autowired
	private GenericResponseBean response;

	@Autowired
	private ICaseDAO casesDao;
	
	@Autowired
	private IUserDAO userDao;
	
	
	public File getFileFor(String fileName) {
		File f = new File(env.getProperty(REPORT_DOWNLOAD_LOCATION + fileName));
		return f;
	}

	public File getPickListReport(String courtTerm, String contentType, String manifestId) {
		File f = new File(env.getProperty(REPORT_DOWNLOAD_LOCATION) + "picklist.xlsx");
		return f;
	}

	public GenericResponseBean sendReport(String courtTerm, String contentType, /*String manifestId,*/String user) {
		if (courtTerm != null && contentType != null) {
			if (courtTerm.length() == 0 && contentType.length() == 0/* && manifestId.length() == 0*/) {
				response.setStatus(FAILURE);
				response.setMessage("Court Term, Content Type & ManifestId all can't be empty!");
			} else {
				String userEmail = userDao.findByUserName(user).getEmail();
				if(userEmail==null && userEmail.length()==0){
					userEmail = env.getProperty(EMAIL_RECEIVER);
				}
				ArrayList<Cases> listOfCases = (ArrayList<Cases>) casesDao.getPickListForExport(contentType, courtTerm/*, manifestId*/);
				String fileAbsolutePath="";
				if(listOfCases!=null && listOfCases.size()>0){
					fileAbsolutePath = picklistProc.createPickList(listOfCases,env.getProperty(REPORT_DOWNLOAD_LOCATION),userEmail);
					response.setStatus(SUCCESS);
					response.setMessage("The Picklist Report has been scheduled & will be sent through email!");
				}
				
			}
		} else {
			response.setStatus(FAILURE);
			response.setMessage("Court Term, Content Type can't be Null!");
		}

		return response;
	}

}
