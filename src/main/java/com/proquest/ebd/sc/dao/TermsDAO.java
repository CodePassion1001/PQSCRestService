package com.proquest.ebd.sc.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.proquest.ebd.sc.model.Term;

@Repository
@Scope(value = "prototype")
public class TermsDAO implements ITermsDAO {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext	
	private EntityManager entityManager;

	@Override
	public Term findTermbyExpected(String expected) {
		Query query = entityManager.createQuery("from Term where expected = :expected");
		query.setParameter("expected", expected);
		
		return (Term)query.getSingleResult();
	}

	
	@Override
	public Term findTermByTermId(int termId) {		
		return entityManager.find(Term.class, termId);
	}	
	
	
}
