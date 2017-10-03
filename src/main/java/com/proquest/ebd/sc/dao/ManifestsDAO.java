package com.proquest.ebd.sc.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.proquest.ebd.sc.model.Cases;
import com.proquest.ebd.sc.model.Manifests;

@Repository
@Scope(value = "prototype")
public class ManifestsDAO implements IManifestsDAO{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext	
	private EntityManager entityManager;	
	
	@Autowired
	private ICaseDAO casesDao;
	
	@Transactional
	public void addManifest(Manifests manifest) {
		if(manifest!=null){
			//entityManager.getTransaction().begin();
			entityManager.persist(manifest);
			for(Cases acase: manifest.getCaseslist()){
				casesDao.updateCase(acase);
			}
		}	
	}
	
	
	@Transactional
	public Manifests find(String manifestID){
		return entityManager.find(Manifests.class, manifestID);
	}



	@Override
	public Manifests update(Manifests manifest) {
		if(manifest!=null){
			entityManager.merge(manifest);
			for(Cases acase: manifest.getCaseslist()){
				casesDao.updateCase(acase);
			}
		}	
		return manifest;
	}

	
}
