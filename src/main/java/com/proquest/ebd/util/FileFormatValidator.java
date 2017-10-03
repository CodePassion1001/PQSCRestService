package com.proquest.ebd.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.proquest.ebd.sc.beans.GenericResponseBean;

@Component
public class FileFormatValidator {

	@Autowired
	private Environment env;
	
	private String supportedExt="";

	public GenericResponseBean validateFormat(GenericResponseBean docMeta, MultipartFile ufile) {
		// Set the file details to DocumenMeta Object
		docMeta.setFileName(ufile.getOriginalFilename());
		docMeta.setSize(ufile.getSize() +" bytes");
		
		String filename = ufile.getOriginalFilename();
		String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

		
		// Validate the size of the file
		if (ufile.getSize() == 0) {
			docMeta.setFailureReason("The Attached document size is '0' byte!");
			docMeta.setStatus(Constants.FAILURE);
			return docMeta;
		}

		// Validate the file extension as supported by the system
		List<String> exts = getsupportedExtentions();
		
		if(!exts.contains(extension)){
			docMeta.setFailureReason("The Attached document format if not a valid file (."+extension+"); Supported extensions "+supportedExt);
			docMeta.setStatus(Constants.FAILURE);
			return docMeta;
		}

		// If needed Store the file locally for further processing
		// docMeta = saveUploadedFile(ufile, docMeta);
		docMeta.setStatus(Constants.SUCCESS);
		return docMeta;

	}

	
	private GenericResponseBean saveUploadedFile(MultipartFile ufile, GenericResponseBean docMeta) {
		String directory = env.getProperty("paths.uploadedFiles");
		try {
				Path rootLocation = Paths.get(directory);
				Files.copy(ufile.getInputStream(), rootLocation.resolve(ufile.getOriginalFilename()));
		} catch (Exception e) {
			e.printStackTrace();
			docMeta.setFailureReason("Internal Server Error!==>" + e.getMessage());
			docMeta.setStatus(Constants.FAILURE);
			return docMeta;
		}
		docMeta.setFailureReason("File Stored locally for futher Processing! ");
		docMeta.setStatus(Constants.SUCCESS);
		return docMeta;
	}
	
	private List<String> getsupportedExtentions(){
		supportedExt = env.getProperty("picklist.extention");
		List<String> exts = Arrays.asList(supportedExt.split("\\s*,\\s*"));
		return exts;
	}

}
