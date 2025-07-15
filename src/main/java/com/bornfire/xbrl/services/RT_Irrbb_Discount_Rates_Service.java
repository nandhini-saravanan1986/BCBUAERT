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
import com.bornfire.xbrl.entities.RT_IRRBB_Data_Discount_Rates;
import com.bornfire.xbrl.entities.RT_IRRBB_Data_Discount_Rates_Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileNotFoundException;



@Service
public class RT_Irrbb_Discount_Rates_Service {

	@Autowired
	private Environment env;
	
    private static final Logger logger = LoggerFactory.getLogger(RT_Irrbb_Discount_Rates_Service.class);


    @Autowired 
    RT_IRRBB_Data_Discount_Rates_Repository IRRBB_Data_Template_DiscountRate_repo;

	@Autowired
	private SessionFactory sessionFactory;

	public boolean updateirrbbdiscount(RT_IRRBB_Data_Discount_Rates updatedData) {
	    System.out.println("Looking for record with SI_NO: " + updatedData.getSI_NO());

	    RT_IRRBB_Data_Discount_Rates existing = IRRBB_Data_Template_DiscountRate_repo.getParticularDataBySI_NO(updatedData.getSI_NO());

	    if (existing != null) {
	        // Update fields
	    	existing.setDerivedTenor(updatedData.getDerivedTenor());
	    	existing.setOvernight(updatedData.getOvernight());
	    	existing.setOneWeek(updatedData.getOneWeek());
	    	existing.setOneMonth(updatedData.getOneMonth());
	    	existing.setTwoMonth(updatedData.getTwoMonth());
	    	existing.setThreeMonth(updatedData.getThreeMonth());
	    	existing.setSixMonth(updatedData.getSixMonth());
	    	existing.setNineMonth(updatedData.getNineMonth());
	    	existing.setOneYear(updatedData.getOneYear());
	    	existing.setOnePointFiveYear(updatedData.getOnePointFiveYear());
	    	existing.setTwoYear(updatedData.getTwoYear());
	    	existing.setThreeYear(updatedData.getThreeYear());
	    	existing.setFourYear(updatedData.getFourYear());
	    	existing.setFiveYear(updatedData.getFiveYear());
	    	existing.setSixYear(updatedData.getSixYear());
	    	existing.setSevenYear(updatedData.getSevenYear());
	    	existing.setEightYear(updatedData.getEightYear());
	    	existing.setNineYear(updatedData.getNineYear());
	    	existing.setTenYear(updatedData.getTenYear());
	    	existing.setTwelveYear(updatedData.getTwelveYear());
	    	existing.setFifteenYear(updatedData.getFifteenYear());
	    	existing.setTwentyYear(updatedData.getTwentyYear());
	    	existing.setThirtyYear(updatedData.getThirtyYear());
	

	        IRRBB_Data_Template_DiscountRate_repo.save(existing);
	        return true;
	    } else {
	        System.out.println("No record found for SI_NO: " + updatedData.getSI_NO());
	        return false;
	    }
	}



	
}
