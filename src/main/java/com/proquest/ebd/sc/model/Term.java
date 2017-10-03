package com.proquest.ebd.sc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
* This is the Entity class for terms table. 
* Hibernate will use this class during its CRUD operation.
* 
* @author  Purujit Saha
* @version 1.0
* @since   2017-05-17
*/
@Entity
@Table(name = "terms", uniqueConstraints = {
		@UniqueConstraint(columnNames = "trad"),
		@UniqueConstraint(columnNames = "expected") })
public class Term {
	
	@Id
	@Column(name="term_number")
	private int termNumber;
	
	@Column(nullable=false)
	private String trad;
	
	@Column(nullable=false)
	private String expected;
	
	
	public int getTermNumber() {
		return termNumber;
	}
	public void setTermNumber(int termNumber) {
		this.termNumber = termNumber;
	}
	public String getTrad() {
		return trad;
	}
	public void setTrad(String trad) {
		this.trad = trad;
	}
	public String getExpected() {
		return expected;
	}
	public void setExpected(String expected) {
		this.expected = expected;
	}
}
