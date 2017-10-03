package com.proquest.ebd.sc.service;

import java.util.List;
import java.util.Set;

import com.proquest.ebd.sc.beans.CasesDTO;
import com.proquest.ebd.sc.beans.ResponseDTO;
import com.proquest.ebd.sc.beans.SearchFilterBean;
import com.proquest.ebd.sc.model.Cases;
import com.proquest.ebd.util.PickListProcessor;

import java.util.Date;
import java.util.HashSet;

public interface ISCCaseService {
	
	List<ResponseDTO> getAllCases();

	ResponseDTO getCaseByCaseId(String caseId);

	boolean addCase(Cases nCase);

	ResponseDTO updateCase(String caseId, ResponseDTO uCase);

	void deleteCase(String caseId);

	List<String> getDistinctContentTypes();

	List<String> getAllCourtTermsByContentType(String contentType);

	List<ResponseDTO> getFilteredCases(SearchFilterBean searchFilter, int maxrecords);
	
	default boolean isValid(String value){
		return value!=null && value.length()>0;
	}
	
	default boolean isValid(int val){
		return val!=0;
	}
	
	default boolean isValid(Set<String> set){
		HashSet<String> hashSet = (HashSet<String>)set;
		return set!=null && set.size()>0;
	}
	default boolean isValid(Date date){
		return date!=null;
	}
	
	default void updateCaseFromDTO(Cases uCase, CasesDTO caseDTO) {
		
		if(uCase.getCaseId().equals(caseDTO.getFullCaseId())){
			// Title
			if(isValid(caseDTO.getTitle())){
				uCase.setTitle(caseDTO.getTitle());
			}
			// No. Briefs/Case
			if(isValid(caseDTO.getBriefsPerCase())){
				uCase.setBriefsPerCase(caseDTO.getBriefsPerCase());
			}
			// No. Briefs Rec'd
			if(isValid(caseDTO.getBriefsReceived())){
				uCase.setBriefsReceived(caseDTO.getBriefsReceived());
			}
			// With Text
			if(caseDTO.getWithText()!=null){
				uCase.setWithText(caseDTO.getWithText());
			}
			// Notes
			if(isValid(caseDTO.getNotes())){
				uCase.setNotes(caseDTO.getNotes());
			}			
			// Combined Case
			if(isValid(caseDTO.getCombinedCase())){
				uCase.setCombinedCase(caseDTO.getCombinedCase());
			}			
			// Case Year
			if(isValid(caseDTO.getYear())){
				uCase.setYear(caseDTO.getYear());
			}
			// Case No.
			if(isValid(caseDTO.getCaseNumber())){
				uCase.setCaseNumber(caseDTO.getCaseNumber());
			}
			// Order List Date
			if(isValid(caseDTO.getOrderListDate())){
				uCase.setOrderListDate(caseDTO.getOrderListDate());
			}
			
			
			
		}
	}

}
