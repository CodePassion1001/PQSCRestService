package com.proquest.ebd.sc.dao;

import java.util.List;

import com.proquest.ebd.sc.beans.SearchFilterBean;
import com.proquest.ebd.sc.model.Cases;

public interface ICaseDAO {
	List<Cases> getAllCases();

	Cases getCaseByCaseId(String caseId);

	void updateCase(Cases uCase);

	void addCase(Cases uCase);

	void deleteCase(String caseId);

	List<Cases> getCasesByFilter(SearchFilterBean sf, int maxrecords);

	boolean insertAll(List<Cases> list);

	boolean doesExists(Cases aCase);

	List<Cases> getPickListForExport(String contentType,
			String courtTerm/* , String manifestID */);

	List<String> getContentTypes();

	List<Integer> getTermNumbersByContentType(String contentType);
}
