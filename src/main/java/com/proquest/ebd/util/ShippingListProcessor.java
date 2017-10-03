package com.proquest.ebd.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.proquest.ebd.sc.beans.GenericResponseBean;
import com.proquest.ebd.sc.beans.ShippingListRecord;

@Component
@Lazy(true)
public class ShippingListProcessor {

	public HashMap<String, Object> importShippingList(GenericResponseBean docMeta, MultipartFile ufile) {
		String fileName = ufile.getOriginalFilename();
		String shippingManifestId = fileName.substring(0, fileName.lastIndexOf('.'));
		//String shippingManifestId = fileName;
		HashMap<String,Object> returnObj = new HashMap<String,Object>();
		List<ShippingListRecord> shippingRecords = new ArrayList<ShippingListRecord>();
		ArrayList<HashMap<String, Object>> sheetData = null;
		
		try {
				sheetData = ExcelFileProcessor.readExcel(ufile.getInputStream());
		} catch (IOException e) {
			docMeta.setFailureReason("Program Can't Parse the template. Check with Dev Team.");
			e.printStackTrace();
		}
		if (sheetData != null) {
			// Setting Shipping Manifest ID
			
			long sheetDataSize = sheetData.size();
			System.out.println("sheetDataSize ==>" + sheetDataSize);

			for (HashMap<String, Object> map : sheetData) {
				ShippingListRecord shippingRecd = new ShippingListRecord();
				for (String key : map.keySet()) {
					switch (key) {
					case Constants.SHIPPING_LIST_FULLCASE_ID: // 1.File Case No
						String caseID = map.get(key).toString();
						if(caseID.trim().length()>0){
							shippingRecd.setCaseId(caseID);
							shippingRecd.setManifest_shipping_id(shippingManifestId);
						}
						break;
					case Constants.SHIPPING_LIST_BRIEF_RECV: // 1.Title
						String s = map.get(key).toString();
						if(s.trim().length()>0){
							shippingRecd.setBriefsReceived((int)Double.parseDouble(s));
						}
						break;
					}
				}
				if(shippingRecd.getCaseId()!=null && shippingRecd.getCaseId().trim().length()>0)
					shippingRecords.add(shippingRecd);
			}
			
			docMeta.setStatus(Constants.SUCCESS);
			returnObj.put("shippingList", shippingRecords);
			returnObj.put("docMeta", docMeta);
		}else{
			docMeta.setStatus(Constants.FAILURE);
		}
		return returnObj;
	}
}