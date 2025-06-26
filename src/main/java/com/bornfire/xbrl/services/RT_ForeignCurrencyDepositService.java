package com.bornfire.xbrl.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bornfire.xbrl.entities.RT_ForeignCurrencyDeposit;
import com.bornfire.xbrl.entities.RT_ForeignCurrencyDepositRepository;
import com.bornfire.xbrl.entities.RT_Fxriskdata;
import com.bornfire.xbrl.entities.RT_MmData;
import com.bornfire.xbrl.entities.RT_MmDataRepository;

@Service
public class RT_ForeignCurrencyDepositService {

    @Autowired
    private Environment env;

    @Autowired
    private RT_ForeignCurrencyDepositRepository foreigncurrencydepositRepo;

    @Autowired
    private SessionFactory sessionFactory;
    
    public boolean updateForeignCurrencyDeposit(RT_ForeignCurrencyDeposit updatedData) {
	    System.out.println("Looking for record with SI_NO: " + updatedData.getSI_NO());

	    RT_ForeignCurrencyDeposit existing = foreigncurrencydepositRepo.getParticularDataBySI_NO(updatedData.getSI_NO());

	    if (existing != null) {
	    	existing.setDate(updatedData.getDate());
	    	existing.setBankName(updatedData.getBankName());
	    	existing.setHeadOfficeSubsidiary(updatedData.getHeadOfficeSubsidiary());
	    	existing.setSubsidiary(updatedData.getSubsidiary());
	    	existing.setBankSymbol(updatedData.getBankSymbol());
	    	existing.setConventionalOrIslamic(updatedData.getConventionalOrIslamic());
	    	existing.setLocalOrForeign(updatedData.getLocalOrForeign());
	    	existing.setCbuaeTiering(updatedData.getCbuaeTiering());
	    	existing.setDepositInternalReference(updatedData.getDepositInternalReference());
	    	existing.setOnBalanceSheetDepType(updatedData.getOnBalanceSheetDepType());
	    	existing.setFundingCounterParty(updatedData.getFundingCounterParty());
	    	existing.setCounterpartyType(updatedData.getCounterpartyType());
	    	existing.setIndustryGcis(updatedData.getIndustryGcis());
	    	existing.setCounterpartyCountryRisk(updatedData.getCounterpartyCountryRisk());
	    	existing.setCbuaeRegionalZone(updatedData.getCbuaeRegionalZone());
	    	existing.setNominal(updatedData.getNominal());
	    	existing.setNominalInAed(updatedData.getNominalInAed());
	    	existing.setCurrency(updatedData.getCurrency());
	    	existing.setRateType(updatedData.getRateType());
	    	existing.setDepositFixedRateOrAdministrativeRate(updatedData.getDepositFixedRateOrAdministrativeRate());
	    	existing.setBenchmarkFloatingRate(updatedData.getBenchmarkFloatingRate());
	    	existing.setTenorFloatingRate(updatedData.getTenorFloatingRate());
	    	existing.setSpreadOverBenchmarkRate(updatedData.getSpreadOverBenchmarkRate());
	    	existing.setMaturityDate(updatedData.getMaturityDate());
	    	existing.setTenorMths(updatedData.getTenorMths());
	    	existing.setMaturityPeriod(updatedData.getMaturityPeriod());

	    	
	    	
	    	
	       
	    	
	    	foreigncurrencydepositRepo.save(existing);
	        return true;
	    } else {
	        System.out.println("No record found for SI_NO: " + updatedData.getSI_NO());
	        return false;
	    }
	}
    
    

    
}
