package com.bornfire.xbrl.services;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ECLDataUploadService {
	
	public String UploadECLdata(MultipartFile file, String reportType, Date toDate) {
		return reportType;
		
	}

}
