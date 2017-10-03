package com.proquest.ebd.sc.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="manifests")
public class Manifests {
	@Id
	private String manifest_shipping_Id;

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable( name="cases_to_manifests",joinColumns=@JoinColumn(name="manifest_shipping_Id"/*, insertable=false*/),
			    inverseJoinColumns=@JoinColumn(name="full_case_id"/*, insertable=false*/),
				uniqueConstraints = { @UniqueConstraint(columnNames={"full_case_id", "manifest_shipping_Id"})})
	private Collection<Cases> caseslist = new ArrayList<Cases>();
	
	
	public Collection<Cases> getCaseslist() {
		return caseslist;
	}

	public void setCaseslist(Collection<Cases> cases) {
		this.caseslist = cases;
	}
	public String getManifest_shipping_Id() {
		return manifest_shipping_Id;
	}

	public void setManifest_shipping_Id(String manifest_shipping_Id) {
		this.manifest_shipping_Id = manifest_shipping_Id;
	}

	@Override
	public String toString() {
		return manifest_shipping_Id;
	}
	
	public boolean exists(Cases acase){
		boolean returnVal = false;
		if(acase!=null){
			for(Cases c:getCaseslist()){
				if(c.getCaseId().equals(acase.getCaseId())){
					returnVal = true;
				}
			}
		}
		return returnVal;
	}
	
	public boolean isSame(Manifests m){
		return manifest_shipping_Id.equals(m.manifest_shipping_Id);
	}
	
}
