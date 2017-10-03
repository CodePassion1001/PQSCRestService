package com.proquest.ebd.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.proquest.ebd.sc.beans.GenericResponseBean;
import com.proquest.ebd.sc.model.Cases;
@Component
@Lazy(true)
public class PickListProcessor implements Constants{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Environment env;
	
	@Autowired
	private EmailHelper emailHelper;

	public HashMap<String, Object> importPickList(GenericResponseBean docMeta, MultipartFile ufile) {
		docMeta.setStatus(Constants.FAILURE);
		
		List<Cases> caseRecords = new ArrayList<Cases>();
		HashMap<String,Object> returnObj = new HashMap<String,Object>();
		ArrayList<HashMap<String,Object>> sheetData = null;
		
		try {
			sheetData = ExcelFileProcessor.readExcel(ufile.getInputStream());			
		} catch (IOException e) {
			docMeta.setFailureReason("Program Can't Parse the template. Check with Dev Team.");
			e.printStackTrace();
		}

		if (sheetData != null) {
			long sheetDataSize = sheetData.size();
			System.out.println("sheetDataSize ==>"+sheetDataSize);
			for(HashMap<String,Object> map:sheetData){
				Cases nCase = new Cases();
				//CasesToManifests casesToManifest = new CasesToManifests();
				for(String key : map.keySet()){
					switch (key) {
					case PICKLIST_FULLCASENO: // 1.File Case No
						nCase.setCaseId(map.get(key).toString());
						//casesToManifest.setCaseId(map.get(key).toString());
						break;
					case PICKLIST_CASETITLE: // 1.Title
						nCase.setTitle(map.get(key).toString());
						break;
					case PICKLIST_COURTTERMDATE:// 2.Court Term Date
						nCase.setCourtTermDate(map.get(key).toString());
						break;
					case PICKLIST_CONTENTTYPE: // 3.Content Type
						nCase.setCategoryType(map.get(key).toString());
						break;
					case PICKLIST_COMBINEDCASE: // 4.COMBINED CASE
						nCase.setCombinedCase(map.get(key).toString());
						break;
					case PICKLIST_ORDERLISTDATE: // 5.Order List Date
						logger.info("OrderListDate ==>"+map.get(key).toString());
						nCase.setOrderListDate(formatDate(map.get(key).toString()));
						break;
					case PICKLIST_CASEYEAR: // 6.Case Year
						int year = (int)Double.parseDouble(map.get(key).toString());
						nCase.setYear(year);
						break;
					case PICKLIST_CASENO: // 7.Case No
						int caseNo = (int)Double.parseDouble(map.get(key).toString());
						nCase.setCaseNumber(caseNo);
						break;
					case PICKLIST_WITHTEXT: // 8.With Text
						String withText = "NO";
						if(map.get(key).toString()!=null && map.get(key).toString().length()>0){
							withText = map.get(key).toString();
						}
						nCase.setWithText(withText);
						break;
					case PICKLIST_MANIFEXTID: // 9.Manifest ID						
						//-- Do Nothing for the time being, will implement later as we get more clarity on this--
						break;
					case PICKLIST_BRIEF_PER_CASE: // 10.No. Briefs/Case
						int briefCase =0;
						if(map.get(key).toString()!=null && map.get(key).toString().length()>0){
							briefCase = (int)Double.parseDouble(map.get(key).toString());
						}						
						nCase.setBriefsPerCase(briefCase);
						break;
					case PICKLIST_BRIEF_RECV: // 11.No. Briefs Rec'd
						int briefRecv =0;
						if(map.get(key).toString()!=null && map.get(key).toString().length()>0){
							briefRecv = (int)Double.parseDouble(map.get(key).toString());
						}
						nCase.setBriefsReceived(briefRecv);
						break;
					case PICKLIST_NOTES: // 11.Notes
						String notes = "";
						if(map.get(key).toString()!=null && map.get(key).toString().length()>0){
							notes = map.get(key).toString();
						}
						nCase.setNotes(notes);
						break;
					}
				}
				//nCase.getCasesToManifestList().add(casesToManifest);
				//casesToManifest.setAcase(nCase);
				caseRecords.add(nCase);
			}
			returnObj.put("caseRecords", caseRecords);
			docMeta.setStatus(Constants.SUCCESS);
		}else{
			docMeta.setStatus(Constants.FAILURE);
		}
		returnObj.put("docMeta", docMeta);
		return returnObj;
	}

	public static Date formatDate(String strDate) {

		Date date = new Date();
		String pattern = "MM/dd/yyyy";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			date = format.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	@Async
	public String createPickList(ArrayList<Cases> listOfCases,String downloadLocation, String email) {
		
		String fName = generatePickListExportFileName();
		String pickListPath = downloadLocation+fName; 
		int rowNumber = 1;
		
		SXSSFWorkbook wb = new SXSSFWorkbook(1000);
		Sheet pSheet = wb.createSheet("pickList");
		Row headerRow = pSheet.createRow(0);
		
		try {
			FileOutputStream fileOut = new FileOutputStream(pickListPath);
			prepareHeaderRow(wb, headerRow);
			if(!listOfCases.isEmpty()) 
			{
				for(Cases acase:listOfCases)
					rowNumber = writeRow(wb, pSheet, acase, rowNumber);
			}
				
			wb.write(fileOut);
			fileOut.close();
			emailHelper.sendPickListRepost(pickListPath,email);
		}catch(IOException e){}
		return pickListPath;
	}
	
	private int writeRow(SXSSFWorkbook wb, Sheet pSheet, Cases acase, int rowNumber) {
		Row dataRow = pSheet.createRow(rowNumber);
		try {
			Cell dataCell0 = dataRow.createCell(0);
			dataCell0.setCellValue(acase.getCaseId());

			Cell dataCell1 = dataRow.createCell(1);
			dataCell1.setCellValue(acase.getTitle());

			Cell dataCell2 = dataRow.createCell(2);
			dataCell2.setCellValue(acase.getCourtTermDate());

			Cell dataCell3 = dataRow.createCell(3);
			dataCell3.setCellValue(acase.getCategoryType());

			Cell dataCell4 = dataRow.createCell(4);
			dataCell4.setCellValue(acase.getCombinedCase());

			Cell dataCell5 = dataRow.createCell(5);
			logger.debug("Export Date Format before write ==>"+acase.getOrderListDate().toString());
			dataCell5.setCellValue(acase.getOrderListDate().toString());

			Cell dataCell6 = dataRow.createCell(6);
			dataCell6.setCellValue(acase.getYear());

			Cell dataCell7 = dataRow.createCell(7);
			dataCell7.setCellValue(acase.getCaseNumber());

			Cell dataCell8 = dataRow.createCell(8);
			dataCell8.setCellValue(acase.getWithText());

			Cell dataCell9 = dataRow.createCell(9);
			dataCell9.setCellValue(acase.getBriefsPerCase());

			Cell dataCell10 = dataRow.createCell(10);
			dataCell10.setCellValue(acase.getBriefsReceived());

			Cell dataCell11 = dataRow.createCell(11);
			dataCell11.setCellValue(acase.getNotes());

			
		} catch (Exception e) {
			e.printStackTrace();
		}

		rowNumber = rowNumber + 1;

		return rowNumber;
	}

	private void prepareHeaderRow(SXSSFWorkbook wb, Row headerRow) {
		// -----Header row------
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		// XL: Filename,,Title,Pubdate,Pagination,Corp_author,SuDoc,
		// Accession No.,Series_TITLE,Notes,Manifest,PDF Ready?,Agency,Module
		Cell headerCell0 = headerRow.createCell(0);
		headerCell0.setCellValue(PICKLIST_FULLCASENO);
		headerCell0.setCellStyle(style);
		Cell headerCell1 = headerRow.createCell(1);
		headerCell1.setCellValue(PICKLIST_CASETITLE);
		headerCell1.setCellStyle(style);
		Cell headerCell2 = headerRow.createCell(2);
		headerCell2.setCellValue(PICKLIST_COURTTERMDATE);
		headerCell2.setCellStyle(style);
		Cell headerCell3 = headerRow.createCell(3);
		headerCell3.setCellValue(PICKLIST_CONTENTTYPE);
		headerCell3.setCellStyle(style);
		Cell headerCell4 = headerRow.createCell(4);
		headerCell4.setCellValue(PICKLIST_COMBINEDCASE);
		headerCell4.setCellStyle(style);
		Cell headerCell5 = headerRow.createCell(5);
		headerCell5.setCellValue(PICKLIST_ORDERLISTDATE);
		headerCell5.setCellStyle(style);
		Cell headerCell6 = headerRow.createCell(6);
		headerCell6.setCellValue(PICKLIST_CASEYEAR);
		headerCell6.setCellStyle(style);
		Cell headerCell7 = headerRow.createCell(7);
		headerCell7.setCellValue(PICKLIST_CASENO);
		headerCell7.setCellStyle(style);
		Cell headerCell8 = headerRow.createCell(8);
		headerCell8.setCellValue(PICKLIST_WITHTEXT);
		headerCell8.setCellStyle(style);
		Cell headerCell9 = headerRow.createCell(9);
		headerCell9.setCellValue(PICKLIST_BRIEF_PER_CASE);
		headerCell9.setCellStyle(style);
		Cell headerCell10 = headerRow.createCell(10);
		headerCell10.setCellValue(PICKLIST_BRIEF_RECV);
		headerCell10.setCellStyle(style);
		Cell headerCell11 = headerRow.createCell(11);
		headerCell11.setCellValue(PICKLIST_NOTES);
		headerCell11.setCellStyle(style);
	}

	private String generatePickListExportFileName() {
		String fileName = Constants.DEFAULT_PICKLIST_EXP_FILE_NAME; // DEFAULT_EXP_FILE_NAME;
		SimpleDateFormat sdfd = new SimpleDateFormat(Constants.DATE_FORMAT_YYYYMMDD_HHMMSS);
		String dateStr = sdfd.format(new Date());
		fileName += "_" + dateStr + ".xlsx";
		return fileName;
	}

}
