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
import com.bornfire.xbrl.entities.RT_IRRBB_Data_EVE_Template;
import com.bornfire.xbrl.entities.RT_IRRBB_Data_EVE_Template_Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileNotFoundException;



@Service
public class RT_Irrbb_Eve_Service {

	@Autowired
	private Environment env;
	
    private static final Logger logger = LoggerFactory.getLogger(RT_Irrbb_Eve_Service.class);


    @Autowired
    RT_IRRBB_Data_EVE_Template_Repository IRRB_EVE_Repo;

	@Autowired
	private SessionFactory sessionFactory;

	public boolean updateirrbbeve(RT_IRRBB_Data_EVE_Template updatedData) {
	    System.out.println("Looking for record with SI_NO: " + updatedData.getSI_NO());

	    RT_IRRBB_Data_EVE_Template existing = IRRB_EVE_Repo.getParticularDataBySI_NO(updatedData.getSI_NO());

	    if (existing != null) {
	        // Update fields
	    	existing.setDate(updatedData.getDate());
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
	    	existing.setOutstandingBalance(updatedData.getOutstandingBalance());
	    	existing.setOvernight(updatedData.getOvernight());
	    	existing.setOnToOneM(updatedData.getOnToOneM());
	    	existing.setOneMToThreeM(updatedData.getOneMToThreeM());
	    	existing.setThreeMToSixM(updatedData.getThreeMToSixM());
	    	existing.setSixMonthToNineM(updatedData.getSixMonthToNineM());
	    	existing.setNineMonthToOneY(updatedData.getNineMonthToOneY());
	    	existing.setOneYToFiveY(updatedData.getOneYToFiveY());
	    	existing.setOnePointFiveYToTwoY(updatedData.getOnePointFiveYToTwoY());
	    	existing.setTwoYToThreeY(updatedData.getTwoYToThreeY());
	    	existing.setThreeYToFourY(updatedData.getThreeYToFourY());
	    	existing.setFourYToFiveY(updatedData.getFourYToFiveY());
	    	existing.setFiveYToSixY(updatedData.getFiveYToSixY());
	    	existing.setSixYToSevenY(updatedData.getSixYToSevenY());
	    	existing.setSevenYToEightY(updatedData.getSevenYToEightY());
	    	existing.setEightYToNineY(updatedData.getEightYToNineY());
	    	existing.setNineYToTenY(updatedData.getNineYToTenY());
	    	existing.setTenYToFifteenY(updatedData.getTenYToFifteenY());
	    	existing.setFifteenYToTwentyY(updatedData.getFifteenYToTwentyY());
	    	existing.setTwentyYearAbove(updatedData.getTwentyYearAbove());
	    	existing.setNonRateSensitive(updatedData.getNonRateSensitive());

	    	

	    	IRRB_EVE_Repo.save(existing);
	        return true;
	    } else {
	        System.out.println("No record found for SI_NO: " + updatedData.getSI_NO());
	        return false;
	    }
	}
}
