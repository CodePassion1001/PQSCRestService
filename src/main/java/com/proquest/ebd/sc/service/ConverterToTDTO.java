package com.proquest.ebd.sc.service;

import java.util.ArrayList;
import java.util.List;

import com.proquest.ebd.sc.beans.CasesDTO;
import com.proquest.ebd.sc.beans.ResponseDTO;
import com.proquest.ebd.sc.model.Cases;
import com.proquest.ebd.sc.model.Manifests;

public class ConverterToTDTO {

	static List<ResponseDTO> convertToDTOList(List<Cases> listOfCases) {

		List<ResponseDTO> listOfResDto = new ArrayList<ResponseDTO>();

		for (Cases acase : listOfCases) {
			listOfResDto.add(convertTODTO(acase));
		}
		return listOfResDto;
	}

	static ResponseDTO convertTODTO(Cases acase) {
		CasesDTO caseDTO = new CasesDTO();
		caseDTO.setFullCaseId(acase.getCaseId());
		caseDTO.setCaseNumber(acase.getCaseNumber());
		caseDTO.setCombinedCase(acase.getCombinedCase());
		caseDTO.setCourtTermDate(acase.getOrderListDate().toString());
		caseDTO.setBriefsPerCase(acase.getBriefsPerCase());
		caseDTO.setBriefsReceived(acase.getBriefsReceived());
		caseDTO.setTitle(acase.getTitle());
		caseDTO.setYear(acase.getYear());
		caseDTO.setNotes(acase.getNotes());
		caseDTO.setWithText(acase.getWithText());
		caseDTO.setCategoryType(acase.getCategoryType());
		caseDTO.setTermNumber(acase.getTermNumber());
		caseDTO.setOrderListDate(acase.getOrderListDate());
		caseDTO.setCourtTermDate(acase.getCourtTermDate());
		for (Manifests manifest : acase.getManifestset()) {
			caseDTO.getManifestIds().add(manifest.toString());
		}
		return caseDTO;
	}
	

}
