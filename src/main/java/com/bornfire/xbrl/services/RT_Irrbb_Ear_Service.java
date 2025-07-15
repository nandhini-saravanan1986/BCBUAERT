package com.bornfire.xbrl.services;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.io.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.bornfire.xbrl.entities.RT_FxRiskDataRepository;
import com.bornfire.xbrl.entities.RT_Fxriskdata;
import com.bornfire.xbrl.entities.RT_IRRBB_Data_EAR;
import com.bornfire.xbrl.entities.RT_IRRBB_Data_EAR_Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileNotFoundException;



@Service
public class RT_Irrbb_Ear_Service {

	@Autowired
	private Environment env;
	
    private static final Logger logger = LoggerFactory.getLogger(RT_Irrbb_Ear_Service.class);


    @Autowired 
    RT_IRRBB_Data_EAR_Repository IRRBB_EAR_Repository;

	@Autowired
	private SessionFactory sessionFactory;

	public boolean updateirrbbear(RT_IRRBB_Data_EAR updatedData) {
	    System.out.println("Looking for record with SI_NO: " + updatedData.getSI_NO());

	    RT_IRRBB_Data_EAR existing = IRRBB_EAR_Repository.getParticularDataBySI_NO(updatedData.getSI_NO());

	    if (existing != null) {
	        // Update fields
	    	existing.setReportDate(updatedData.getReportDate());
	    	existing.setBankName(updatedData.getBankName());
	    	existing.setGroupHeadOfficeSubsidiary(updatedData.getGroupHeadOfficeSubsidiary());
	    	existing.setBankSymbol(updatedData.getBankSymbol());
	    	existing.setConventionalOrIslamic(updatedData.getConventionalOrIslamic());
	    	existing.setLocalOrForeign(updatedData.getLocalOrForeign());
	    	existing.setCbuaeTiering(updatedData.getCbuaeTiering());
	    	existing.setSubsidiary(updatedData.getSubsidiary());
	    	existing.setScenario(updatedData.getScenario());
	    	existing.setGlLevel1(updatedData.getGlLevel1());
	    	existing.setGlLevel2(updatedData.getGlLevel2());
	    	existing.setGlLevel3(updatedData.getGlLevel3());
	    	existing.setOptionType(updatedData.getOptionType());
	    	existing.setRateType(updatedData.getRateType());
	    	existing.setReferenceRate(updatedData.getReferenceRate());
	    	existing.setInstrumentCurrency(updatedData.getInstrumentCurrency());
	    	existing.setPassThroughRate(updatedData.getPassThroughRate());
	    	existing.setOvernight(updatedData.getOvernight());
	    	existing.setOnToTwoW(updatedData.getOnToTwoW());
	    	existing.setTwoWToOneM(updatedData.getTwoWToOneM());
	    	existing.setOneMToTwoM(updatedData.getOneMToTwoM());
	    	existing.setTwoMToThreeM(updatedData.getTwoMToThreeM());
	    	existing.setThreeMToFourM(updatedData.getThreeMToFourM());
	    	existing.setFourMToFiveM(updatedData.getFourMToFiveM());
	    	existing.setFiveMToSixM(updatedData.getFiveMToSixM());
	    	existing.setSixMToSevenM(updatedData.getSixMToSevenM());
	    	existing.setSevenMToEightM(updatedData.getSevenMToEightM());
	    	existing.setEightMToNineM(updatedData.getEightMToNineM());
	    	existing.setNineMToTenM(updatedData.getNineMToTenM());
	    	existing.setTenMToElevenM(updatedData.getTenMToElevenM());
	    	existing.setElevenMToTwelveM(updatedData.getElevenMToTwelveM());

	  

	        IRRBB_EAR_Repository.save(existing);
	        return true;
	    } else {
	        System.out.println("No record found for SI_NO: " + updatedData.getSI_NO());
	        return false;
	    }
	}



	
}
