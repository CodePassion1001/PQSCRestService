package com.proquest.ebd.sc.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.hateoas.ResourceSupport;

/**
* This is the Entity class for CASE table. 
* Hibernate will use this class during its CRUD operation.
* Also this class has been used as REST pay load.
* @author  Purujit Saha
* @version 1.0
* @since   2017-05-10 
*/
@Entity
@Table(name="cases")
public class Cases extends ResourceSupport{
	
	@Id
	@Column(name="full_case_id", unique=true, nullable=false)
	private String caseId;
		
	
	/*@ManyToMany(cascade=CascadeType.PERSIST, mappedBy="caseslist")
	@NotFound(action=NotFoundAction.IGNORE)
	private Set<Manifests> manifests = new HashSet<Manifests>();
	*/
	@ManyToMany(fetch=FetchType.LAZY)
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinTable( name="cases_to_manifests",joinColumns=@JoinColumn(name="full_case_id"),
		    inverseJoinColumns=@JoinColumn(name="manifest_shipping_Id"))
	private Set<Manifests> manifestset = new HashSet<Manifests>();
		
	@Transient	
	private String courtTermDate;
	
	
	@Column(name="term_number")
	private int termNumber;
	
	
	@Column(name="category_type", columnDefinition="ENUM('SD', 'CD')")
	private String categoryType;
	
	private String title;
	
	@Column(name="combined_case")
	private String combinedCase;
	
	@Temporal(TemporalType.DATE)
	@Column(name="order_list_date")
	private Date orderListDate;
	
	private int year;
	
	@Column(name="case_number")
	private int caseNumber;
	
	@Column(name="with_text")
	private String withText;
	
	@Column(name="briefs_per_case")
	private int briefsPerCase;
	
	@Column(name="briefs_received")
	private int briefsReceived;
	
	private String notes;
	
	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	
	public int getTermNumber() {
		return termNumber;
	}

	public void setTermNumber(int termNumber) {
		this.termNumber = termNumber;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCombinedCase() {
		return combinedCase;
	}

	public void setCombinedCase(String combinedCase) {
		this.combinedCase = combinedCase;
	}

	public Date getOrderListDate() {
		return orderListDate;
	}

	public void setOrderListDate(Date orderListDate) {
		this.orderListDate = orderListDate;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(int caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getWithText() {
		return withText;
	}

	public void setWithText(String withText) {
		this.withText = withText;
	}

	public int getBriefsPerCase() {
		return briefsPerCase;
	}

	public void setBriefsPerCase(int briefsPerCase) {
		this.briefsPerCase = briefsPerCase;
	}

	public int getBriefsReceived() {
		return briefsReceived;
	}

	public void setBriefsReceived(int briefsReceived) {
		this.briefsReceived = briefsReceived;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	

	
	public String getCourtTermDate() {
		return courtTermDate;
	}

	public void setCourtTermDate(String courtTermDate) {
		this.courtTermDate = courtTermDate;
	}

	
	
	public Set<Manifests> getManifestset() {
		return manifestset;
	}

	public void setManifestset(Set<Manifests> manifestset) {
		this.manifestset = manifestset;
	}

	@Override
	public String toString() {
		return "Cases [caseId=" + caseId + ", courtTermDate=" + courtTermDate
				+ ", termNumber=" + termNumber + ", categoryType=" + categoryType + ", title=" + title
				+ ", combinedCase=" + combinedCase + ", orderListDate=" + orderListDate + ", year=" + year
				+ ", caseNumber=" + caseNumber + ", withText=" + withText + ", briefsPerCase=" + briefsPerCase
				+ ", briefsReceived=" + briefsReceived + ", notes=" + notes + "]";
	}


	
	
	
	
	
	

	
}
