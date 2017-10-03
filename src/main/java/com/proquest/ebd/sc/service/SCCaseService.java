package com.proquest.ebd.sc.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proquest.ebd.sc.beans.CasesDTO;
import com.proquest.ebd.sc.beans.GenericResponseBean;
import com.proquest.ebd.sc.beans.ResponseDTO;
import com.proquest.ebd.sc.beans.SearchFilterBean;
import com.proquest.ebd.sc.dao.ICaseDAO;
import com.proquest.ebd.sc.dao.ITermsDAO;
import com.proquest.ebd.sc.dao.ManifestsDAO;
import com.proquest.ebd.sc.model.Cases;
import com.proquest.ebd.sc.model.Manifests;
import com.proquest.ebd.sc.model.Term;

/**
 * This is the Service Layer calss for CRUD operation on CASE table This class
 * will act as Proxy between controller & DAO layer
 * 
 * @author Purujit Saha
 * @version 1.0
 * @since 2017-05-10
 */
@Service
public class SCCaseService implements ISCCaseService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ICaseDAO caseDao;

	@Autowired
	private ITermsDAO termsDao;
	
	@Autowired
	private ManifestsDAO manifestDao;

	@Override
	public List<ResponseDTO> getAllCases() {
		List<Cases> listOfCases = caseDao.getAllCases();
		List<ResponseDTO> casesdto = ConverterToTDTO.convertToDTOList(listOfCases);
		return casesdto;
	}

	@Override
	public ResponseDTO getCaseByCaseId(String caseId) {
		Cases acase = caseDao.getCaseByCaseId(caseId);
		return ConverterToTDTO.convertTODTO(acase);
	}
	

	@Override
	public boolean addCase(Cases nCase) {
		if (caseDao.getCaseByCaseId(nCase.getCaseId()) == null) {
			caseDao.addCase(nCase);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ResponseDTO updateCase(String caseId, ResponseDTO resDTO) {
		CasesDTO caseDTO = (CasesDTO) resDTO;
		
		// Retrieve the case details by casefullId
		final Cases uCase = caseDao.getCaseByCaseId(caseId);
		
		// Update all fields except Manifest
		updateCaseFromDTO(uCase, caseDTO);
		
		// Update the Manifest
		if(isValid((HashSet<String>)caseDTO.getManifestIds())){
			Set<Manifests> currentManifests = uCase.getManifestset();
			ArrayList<String> missing = new ArrayList<String>();
			ArrayList<Manifests> futureManifests = new ArrayList<Manifests>();

			caseDTO.getManifestIds().stream()
									.collect(Collectors.toMap((e)-> e,(e)->manifestDao.find(e)))
									.forEach((k,v)-> {
										if(v==null)
											missing.add(k);
										else
											futureManifests.add(v);
									});

			// Remove the Manifest for the case if the Manifests is absent in new Manifests list
			if(!currentManifests.isEmpty()){
				for(Manifests cm : currentManifests){
					boolean matchfound = false;
					for(Manifests fm:futureManifests){
						if(cm.isSame(fm)){
							matchfound = true;
						}
					}
					if(!matchfound){
						cm.getCaseslist().remove(uCase);
					}
				}
			}
			
			// Add new manifests if the Manifests is not already mapped to the Case
			for(Manifests oneManifest : futureManifests){
				boolean matchfound = false;
				for(Cases ac: oneManifest.getCaseslist()){
					if(ac.getCaseId().equals(uCase.getCaseId()))
						matchfound = true;
				}
				if(!matchfound)
					oneManifest.getCaseslist().add(uCase);
			}
		}
		
		// Handle Manifest
		/*if(!caseDTO.getManifestIds().isEmpty()){
			for(String manifestID:caseDTO.getManifestIds()){
				if(manifestDao.find(manifestID)==null){
					
				}
			}	
									
		}*/
		
		return ConverterToTDTO.convertTODTO(caseDao.getCaseByCaseId(caseId));
	}
	
	private Manifests getManifest(String manifestId){
		return manifestDao.find(manifestId);
	}
	
	
	
	@Override
	public void deleteCase(String caseId) {
		caseDao.deleteCase(caseId);
	}

	@Override
	public List<String> getDistinctContentTypes() {
		return caseDao.getContentTypes();
	}

	@Override
	public List<String> getAllCourtTermsByContentType(String contentType) {

		List<String> courtTerms = new ArrayList<String>();

		List<Integer> termNumbers = caseDao.getTermNumbersByContentType(contentType);
		for (Integer i : termNumbers) {
			Term term = termsDao.findTermByTermId(i.intValue());
			courtTerms.add(term.getExpected());
		}
		return courtTerms;
	}

	@Override
	public List<ResponseDTO> getFilteredCases(SearchFilterBean searchFilter, int maxrecords) {
		List<Cases> listOfCases = caseDao.getCasesByFilter(searchFilter, maxrecords);
		List<ResponseDTO> casesdto = ConverterToTDTO.convertToDTOList(listOfCases);
		return casesdto;
	}

}
