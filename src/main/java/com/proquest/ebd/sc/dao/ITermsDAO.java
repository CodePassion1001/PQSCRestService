package com.proquest.ebd.sc.dao;

import com.proquest.ebd.sc.model.Term;

public interface ITermsDAO {
	
	Term findTermbyExpected(String expected);
	Term findTermByTermId(int termId);
}
