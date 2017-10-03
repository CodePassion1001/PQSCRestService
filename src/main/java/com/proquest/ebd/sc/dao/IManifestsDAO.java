package com.proquest.ebd.sc.dao;

import com.proquest.ebd.sc.model.Manifests;

public interface IManifestsDAO {
	public void addManifest(Manifests manifest);
	public Manifests find(String manifestID);	
	public Manifests update(Manifests manifest);
}
