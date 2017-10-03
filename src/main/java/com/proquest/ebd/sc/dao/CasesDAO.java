package com.proquest.ebd.sc.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.proquest.ebd.sc.beans.SearchFilterBean;
import com.proquest.ebd.sc.model.Cases;
import com.proquest.ebd.sc.model.Manifests;
import com.proquest.ebd.sc.model.Term;
import com.proquest.ebd.util.Constants;

/**
 * This is the DAO class for CRUD operation on CASE table This class has been
 * created to handle all DAO functiona related to CRUD operation in CASE table.
 * 
 * @author Purujit Saha
 * @version 1.0
 * @since 2017-05-10
 */
@Transactional
@Repository
@Scope(value = "prototype")
public class CasesDAO implements ICaseDAO {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ITermsDAO termsDao;

	@Autowired
	@Lazy
	private IManifestsDAO manifestDao;

	@Override
	public List<Cases> getAllCases() {
		List<Cases> caseList = (List<Cases>) entityManager.createQuery("from Cases order by caseId", Cases.class).setMaxResults(Constants.MAX_RECORDS_RETURN_CONSTANT)
				.getResultList();
		for (Cases aCase : caseList) {
			Term t = termsDao.findTermByTermId(aCase.getTermNumber());
			aCase.setCourtTermDate(t.getExpected());
		}
		return caseList;
	}

	/**
	 * This method will be used by the picklist download function.
	 * 
	 * @param contentType
	 * @param courtTerm
	 * @param manifestID
	 * @return
	 */
	@Override
	public List<Cases> getPickListForExport(String contentType, String courtTerm) {
		ArrayList<Cases> listOfCases = new ArrayList<Cases>();
		int termID = 0;

		if (courtTerm != null && courtTerm.length() > 0) {
			termID = termsDao.findTermbyExpected(courtTerm).getTermNumber();
		}

		ArrayList<Cases> alistOfCases = (ArrayList<Cases>) getAllCases();
		for (Cases acase : alistOfCases) {
			if (acase.getCategoryType().equalsIgnoreCase(contentType)) {
				if (termID != 0) {
					if (acase.getTermNumber() == termID)
						listOfCases.add(acase);
				} else {
					listOfCases.add(acase);
				}
			}
		}
		return listOfCases;
	}

	@Override
	public List<Cases> getCasesByFilter(SearchFilterBean sf, int maxrecords) {
		int termNumber = 0;
		if(maxrecords > 250 ) maxrecords=Constants.MAX_RECORDS_RETURN_CONSTANT;
		List<Cases> listOfCases = new ArrayList<Cases>();

		if (sf.getCourtTerm() != null && sf.getCourtTerm().trim().length() > 0) {
			termNumber = termsDao.findTermbyExpected(sf.getCourtTerm()).getTermNumber();
		}

		// Create the filter criteria Query
		String queryStr = prepareFilterQuery(sf, termNumber);

		Query query = entityManager.createQuery(queryStr).setMaxResults(maxrecords);
		listOfCases = query.getResultList();

		// Filter by manifestID if Present in Search Criteria
		if (sf.getManifestID() != null && sf.getManifestID().trim().length() > 0) {
			listOfCases = filterByManifestId(listOfCases, sf);
		}

		// Populate the Terms Date
		if (!listOfCases.isEmpty()) {
			populateTermDate(listOfCases);
		}
		return listOfCases;
	}

	private List<Cases> filterByManifestId(List<Cases> listOfCases, SearchFilterBean sf) {
		List<Cases> newlistOfCases = new ArrayList<Cases>();
		for (Cases acase : listOfCases) {			
			for(Manifests manifest: acase.getManifestset()){
				if(manifest.getManifest_shipping_Id().equals(sf.getManifestID())){
					newlistOfCases.add(acase);
					break;
				}
			}			
		}
		return newlistOfCases;
	}

	private void populateTermDate(List<Cases> listOfCases) {
		for (Cases acase : listOfCases) {
			int termNumber = acase.getTermNumber();
			acase.setCourtTermDate(termsDao.findTermByTermId(termNumber).getExpected());
		}
	}

	private String prepareFilterQuery(SearchFilterBean sf, int termNumber) {
		StringBuilder prefix= new StringBuilder("FROM Cases");
		
		if (sf.getFieldCount() > 0) {
			prefix.replace(0, prefix.length(), "FROM Cases WHERE");
		}
		if((sf.getFieldCount() ==1) && sf.getManifestID()!=null && sf.getManifestID().length()>0){
			prefix.replace(0, prefix.length(), "FROM Cases");	
		}
		
		System.out.println(prefix.toString());
		if (termNumber != 0) {
			if (prefix.toString().endsWith("WHERE"))
				prefix.append(" term_number=" + termNumber);
			else
				prefix.append(" AND term_number=" + termNumber);
		}

		if (sf.getFullCaseNo() != null && sf.getFullCaseNo().trim().length() > 0) {
			
			if (prefix.toString().endsWith("WHERE"))
				prefix.append(" full_case_id='" + sf.getFullCaseNo() + "'");
			else
				prefix.append(" AND full_case_id='" + sf.getFullCaseNo() + "'");
		}

		if (sf.getCaseTitle() != null && sf.getCaseTitle().trim().length() > 0) {
			if (prefix.toString().endsWith("WHERE"))
				prefix.append(" title LIKE '%" + sf.getCaseTitle() + "%'");
			else
				prefix.append(" AND title LIKE '%" + sf.getCaseTitle() + "%'");
		}

		if (sf.getCombinedCase() != null && sf.getCombinedCase().trim().length() > 0) {
			if (prefix.toString().endsWith("WHERE"))
				prefix.append(" combined_case='" + sf.getCombinedCase() + "'");
			else
				prefix.append(" AND combined_case='" + sf.getCombinedCase() + "'");
		}

		if (sf.getOrderListDate() != null && sf.getOrderListDate().toString().length() > 0) {
			if (prefix.toString().endsWith("WHERE"))
				prefix.append(" order_list_date='" + sf.getOrderListDate() + "'");
			else
				prefix.append(" AND order_list_date='" + sf.getOrderListDate() + "'");
		}

		if (sf.getContentType() != null && sf.getContentType().trim().length() > 0) {
			if (prefix.toString().endsWith("WHERE"))
				prefix.append(" category_type='" + sf.getContentType() + "'");
			else
				prefix.append(" AND category_type='" + sf.getContentType() + "'");
		}
		

		return prefix.toString();
	}

	@Override
	public Cases getCaseByCaseId(String caseId) {
		Cases aCase = entityManager.find(Cases.class, caseId);
		if (aCase != null) {
			Term t = termsDao.findTermByTermId(aCase.getTermNumber());
			aCase.setCourtTermDate(t.getExpected());
		}
		return (aCase!=null)? aCase:null;
	}

	@Override
	public boolean doesExists(Cases aCase) {

		Cases acase = getCaseByCaseId(aCase.getCaseId());
		if (acase != null) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public void addCase(Cases uCase) {
		Term t = termsDao.findTermbyExpected(uCase.getCourtTermDate());
		uCase.setTermNumber(t.getTermNumber());
		entityManager.persist(uCase);
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public void updateCase(Cases uCase) {
		entityManager.merge(uCase);
	}

	@Override
	public void deleteCase(String caseId) {
		entityManager.remove(getCaseByCaseId(caseId));

	}

	@Override
	public boolean insertAll(List<Cases> list) {
		// To be implemented when needed.
		return true;

	}

	@Override
	public List<String> getContentTypes() {
		List<String> listOfContentType = entityManager
				.createQuery("select categoryType from Cases group by categoryType", String.class).getResultList();
		return listOfContentType;
	}

	@Override
	public List<Integer> getTermNumbersByContentType(String contentType) {

		Query query = entityManager
				.createQuery("select distinct termNumber from Cases where categoryType =:contentType", Integer.class);
		query.setParameter("contentType", contentType);
		List<Integer> listOfContentType = query.getResultList();
		return listOfContentType;
	}

}
