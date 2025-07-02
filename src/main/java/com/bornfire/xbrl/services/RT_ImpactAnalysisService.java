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
import com.bornfire.xbrl.entities.RT_ImpactAnalysis;
import com.bornfire.xbrl.entities.RT_ImpactAnalysisRepository;
import com.bornfire.xbrl.entities.RT_MmData;
import com.bornfire.xbrl.entities.RT_MmDataRepository;

@Service
public class RT_ImpactAnalysisService {

    @Autowired
    private Environment env;

    @Autowired
    private RT_ImpactAnalysisRepository impactanalysisRepo;

    @Autowired
    private SessionFactory sessionFactory;
    
    public boolean updateImpactAnalysis(RT_ImpactAnalysis updatedData) {
	    System.out.println("Looking for record with SI_NO: " + updatedData.getSI_NO());

	    RT_ImpactAnalysis existing = impactanalysisRepo.getParticularDataBySI_NO(updatedData.getSI_NO());

	    if (existing != null) {
	    	existing.setDate(updatedData.getDate());
	    	existing.setBankName(updatedData.getBankName());
	    	existing.setHeadOfficeSubsidiary(updatedData.getHeadOfficeSubsidiary());
	    	existing.setSubsidiary(updatedData.getSubsidiary());
	    	existing.setBankSymbol(updatedData.getBankSymbol());
	    	existing.setConventionalOrIslamic(updatedData.getConventionalOrIslamic());
	    	existing.setLocalOrForeign(updatedData.getLocalOrForeign());
	    	existing.setCbuaeTiering(updatedData.getCbuaeTiering());
	    	existing.setDealInternalReference(updatedData.getDealInternalReference());
	    	existing.setLegType(updatedData.getLegType());
	    	existing.setInternalCounterpartyReference(updatedData.getInternalCounterpartyReference());
	    	existing.setCptyName(updatedData.getCptyName());
	    	existing.setDerivativesCptyCountryOfRisk(updatedData.getDerivativesCptyCountryOfRisk());
	    	existing.setCbuaeRegionalZone(updatedData.getCbuaeRegionalZone());
	    	existing.setCcyBought(updatedData.getCcyBought());
	    	existing.setAmountBought(updatedData.getAmountBought());
	    	existing.setDealRate(updatedData.getDealRate());
	    	existing.setCcySold(updatedData.getCcySold());
	    	existing.setAmountSold(updatedData.getAmountSold());
	    	existing.setValueDate(updatedData.getValueDate());
	    	existing.setDealDate(updatedData.getDealDate());
	    	existing.setDerivativesTenorMonths(updatedData.getDerivativesTenorMonths());
	    	existing.setMaturityPeriod(updatedData.getMaturityPeriod());
	    	existing.setDealerName(updatedData.getDealerName());
	    	existing.setMtmAedEquivalentReportingDate(updatedData.getMtmAedEquivalentReportingDate());
	    	existing.setMtmAedEquivalentPreviousQuarter(updatedData.getMtmAedEquivalentPreviousQuarter());
	    	existing.setMtmChange(updatedData.getMtmChange());
	    	existing.setFundingOrLoansInternalReference(updatedData.getFundingOrLoansInternalReference());
	    	existing.setOnBalanceSheetItemType(updatedData.getOnBalanceSheetItemType());
	    	existing.setOnBalanceItemCounterparty(updatedData.getOnBalanceItemCounterparty());
	    	existing.setCounterpartyCountryOfRisk(updatedData.getCounterpartyCountryOfRisk());
	    	existing.setCbuaeRegionalZne(updatedData.getCbuaeRegionalZne());
	    	existing.setNominal(updatedData.getNominal());
	    	existing.setCurrency(updatedData.getCurrency());
	    	existing.setOverallImpact(updatedData.getOverallImpact());
	    	existing.setAccrualImpact(updatedData.getAccrualImpact());
	    	existing.setRateType(updatedData.getRateType());
	    	existing.setDepositFixedRateOrAdministrativeRate(updatedData.getDepositFixedRateOrAdministrativeRate());
	    	existing.setFixedDepositRateEquivalent(updatedData.getFixedDepositRateEquivalent());
	    	existing.setFloatingRateType(updatedData.getFloatingRateType());
	    	existing.setBenchmarkFloatingRate(updatedData.getBenchmarkFloatingRate());
	    	existing.setSpreadOverBenchmarkRate(updatedData.getSpreadOverBenchmarkRate());
	    	existing.setMaturityDate(updatedData.getMaturityDate());
	    	existing.setTenorMths(updatedData.getTenorMths());

	    	

	    	impactanalysisRepo.save(existing);
	        return true;
	    } else {
	        System.out.println("No record found for SI_NO: " + updatedData.getSI_NO());
	        return false;
	    }
	}
    
    

    
}
