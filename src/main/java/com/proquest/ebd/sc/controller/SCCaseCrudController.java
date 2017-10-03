package com.proquest.ebd.sc.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.proquest.ebd.sc.beans.CasesDTO;
import com.proquest.ebd.sc.beans.ResponseDTO;
import com.proquest.ebd.sc.beans.SearchFilterBean;
import com.proquest.ebd.sc.model.Cases;
import com.proquest.ebd.sc.service.ISCCaseService;

/**
* This is the Rest Controller class for SpringBoot for CRUD operation on CASE table
* This class has been created to handle all CRUD operation for CASE table. 
* However there might be other services may get exposed down the line as needed by the system.
*
* @author  Purujit Saha
* @version 1.0
* @since   2017-05-10 
*/

@CrossOrigin("*")
@RestController
@RequestMapping("api")
public class SCCaseCrudController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ISCCaseService service;
	
	@GetMapping("/cases/{caseId}")
	public ResponseEntity<ResponseDTO> getCase(@PathVariable("caseId") String caseId){
		ResponseDTO sccase = service.getCaseByCaseId(caseId);
		return new ResponseEntity<ResponseDTO>(sccase, HttpStatus.OK);
	}
	
	@GetMapping("/cases/contentType")
	public ResponseEntity<List<String>> getContentTypes(){
		logger.debug("Entered inside the /cases/contentType");
		List<String> listOfContentType = service.getDistinctContentTypes();
		return new ResponseEntity<List<String>>(listOfContentType, HttpStatus.OK);
	}
	
	@GetMapping("/cases/{contentType}/courtTerms")
	public ResponseEntity<List<String>> getMatchingCourtTerm(@PathVariable("contentType") String contentType){
		logger.debug("Entered inside the /cases/"+contentType+"/courtTerms");
		List<String> listOfCourtTerms = service.getAllCourtTermsByContentType(contentType);
		return new ResponseEntity<List<String>>(listOfCourtTerms, HttpStatus.OK);
	}
	
	
	//http://localhost:9090/api/cases/?fullCaseNo=null&caseTitle=null&manifestID=null&courtTerm=2015-2016&combinedCase=null&orderListDate=null&contentType=CD 

	@GetMapping("/cases")
	public ResponseEntity<List<ResponseDTO>> getFilteredCases(@RequestParam("fullCaseNo") String fullCaseNo,
														@RequestParam("caseTitle") String caseTitle,
														@RequestParam("manifestID") String manifestID,
														@RequestParam("courtTerm") String courtTerm,
														@RequestParam("combinedCase") String combinedCase,
														@RequestParam("orderListDate") String orderListDate,
														@RequestParam("contentType") String contentType,
														@RequestParam("maxrecords") Integer maxrecords) {
		logger.debug("Entered inside the /cases");
		SearchFilterBean searchFilter = new SearchFilterBean();
		int i=0;
		if(!fullCaseNo.equals("null") && fullCaseNo!=null && fullCaseNo.trim().length()>0){
			searchFilter.setFullCaseNo(fullCaseNo);
			i++;
		}
		if(!caseTitle.equals("null") && caseTitle!=null && caseTitle.trim().length()>0){
			caseTitle = caseTitle.replaceAll("\"", "");
			searchFilter.setCaseTitle(caseTitle);
			i++;
		}
		if(!manifestID.equals("null") && manifestID!=null && manifestID.trim().length()>0){
			searchFilter.setManifestID(manifestID);
			i++;
		}
		if(!courtTerm.equals("null") && courtTerm!=null && courtTerm.trim().length()>0){
			searchFilter.setCourtTerm(courtTerm);
			i++;
		}
		if(!combinedCase.equals("null") && combinedCase!=null && combinedCase.trim().length()>0){
			searchFilter.setCombinedCase(combinedCase);
			i++;
		}
		if(!orderListDate.equals("null") && orderListDate!=null && orderListDate.trim().length()>0){
			searchFilter.setOrderListDate(orderListDate);
			i++;
		}
		if(!contentType.equals("null") && contentType!=null && contentType.trim().length()>0){
			searchFilter.setContentType(contentType);
			i++;
		}
		searchFilter.setFieldCount(i);
		logger.debug(searchFilter.toString());
		List<ResponseDTO> list = service.getFilteredCases(searchFilter, maxrecords);
		return new ResponseEntity<List<ResponseDTO>>(list, HttpStatus.OK);
	}
	
	@GetMapping("/allcases")
	public ResponseEntity<List<ResponseDTO>> getAllCases() {
		logger.debug("Entered inside the /allcases");
		List<ResponseDTO> list = service.getAllCases();
		return new ResponseEntity<List<ResponseDTO>>(list, HttpStatus.OK);
	}
	
	/*@PostMapping("/cases")
	public ResponseEntity<Void> addScCase(@RequestBody Cases nCase, UriComponentsBuilder builder) {
                boolean flag = service.addCase(nCase);
                if (flag == false) {
        	    return new ResponseEntity<Void>(HttpStatus.CONFLICT);
                }
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(builder.path("/case/{caseId}").buildAndExpand(nCase.getCaseId()).toUri());
                return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}*/
	
	@PutMapping("/cases/{caseId}")
	public ResponseEntity<ResponseDTO> updateCase(@PathVariable("caseId") String caseId,
											@RequestBody CasesDTO caseDTO) {
		ResponseDTO uCase = service.updateCase(caseId,caseDTO);
		return new ResponseEntity<ResponseDTO>(uCase, HttpStatus.OK);
	}
	
	/*@DeleteMapping("cases/{caseId}")
	public ResponseEntity<Void> deleteArticle(@PathVariable("caseId") String caseId) {
		service.deleteCase(caseId);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}	*/
	
	
}
