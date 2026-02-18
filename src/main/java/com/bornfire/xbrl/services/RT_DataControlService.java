package com.bornfire.xbrl.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.bornfire.xbrl.entities.RT_DataControl;
import com.bornfire.xbrl.entities.RT_DatacontrolRepository;



@Service
public class RT_DataControlService {
	
	@Autowired
	RT_DatacontrolRepository RT_DatacontrolRepository;

	
	public String createOrUpdate(RT_DataControl dto, String formmode,String report_name) {
	    try {
	        System.out.println("Service: createOrUpdate() called");

	        if (dto.getPortfolioDate() == null) {
	            return "ERROR: Portfolio date is required.";
	        }

	        if ("edit".equalsIgnoreCase(formmode)) {
	            System.out.println("Form mode is EDIT — updating record.");
	            dto.setModifyFlg("Y");    // Mark that it's been modified
	        } else {
	            // New entry
	            dto.setEntityFlg("Y");    // Mark as new entity
	            dto.setDelFlg("N");       // Not deleted
	            dto.setModifyFlg("N");    // Not modified
	            dto.setReport_name(report_name);
				LocalDate localDate = dto.getPortfolioDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				String DateString = localDate.format(formatter); 
	            RT_DataControl data= RT_DatacontrolRepository.getdata(DateString,report_name);
	            if(data != null && !data.equals(null)) {
	            	System.out.println("Service: createOrUpdate() called  ---  Existing");
	            	data.setContactEmployee1Email1(dto.getContactEmployee1Email1());
	            	data.setContactEmployee1Name1(dto.getContactEmployee1Name1());
	            	data.setContactEmployee1Title1(dto.getContactEmployee1Title1());
	            	data.setContactEmployee2Email2(dto.getContactEmployee2Email2());
	            	data.setContactEmployee2Name2(dto.getContactEmployee2Name2());
	            	data.setContactEmployee2Title2(dto.getContactEmployee2Title2());
	            	data.setSignOffCfoEmail(dto.getSignOffCfoEmail());
	            	data.setSignOffCfoName(dto.getSignOffCfoName());
	            	data.setSignOffCroEmail(dto.getSignOffCroEmail());
	            	data.setSignOffCroName(dto.getSignOffCroName());
	            	data.setSignOffHeadOfTreasuryName(dto.getSignOffHeadOfTreasuryName());
	            	data.setSignOffHeadOfTreasuryEmail(dto.getSignOffHeadOfTreasuryEmail());
	            	RT_DatacontrolRepository.save(data);
	            }
	            else {
	            	System.out.println("Service: createOrUpdate() called  ---  New");
	            	RT_DatacontrolRepository.save(dto);
	            }
	            
	        }

	        return "Saved successfully.";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "ERROR: " + e.getMessage();
	    }
	}

}