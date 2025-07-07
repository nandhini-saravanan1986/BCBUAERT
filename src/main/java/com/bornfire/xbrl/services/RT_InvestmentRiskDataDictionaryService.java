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

import com.bornfire.xbrl.entities.RT_Fxriskdata;
import com.bornfire.xbrl.entities.RT_Investment_Risk_Data_Dashboard_Template;
import com.bornfire.xbrl.entities.RT_Investment_Risk_Data_Dashboard_TemplateRepository;
import com.bornfire.xbrl.entities.RT_MmData;
import com.bornfire.xbrl.entities.RT_MmDataRepository;

@Service
public class RT_InvestmentRiskDataDictionaryService {

    @Autowired
    private Environment env;

    @Autowired
	RT_Investment_Risk_Data_Dashboard_TemplateRepository RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS;

    @Autowired
    private SessionFactory sessionFactory;
    
    public boolean updateinvestmentriskdatadictionary(RT_Investment_Risk_Data_Dashboard_Template updatedData) {
	    System.out.println("Looking for record with SI_NO: " + updatedData.getSI_NO());

	    RT_Investment_Risk_Data_Dashboard_Template existing = RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS.getParticularDataBySI_NO(updatedData.getSI_NO());

	    if (existing != null) {
	        // Update fields
	       
	    	// --- copy every simple field from updatedData to existing -------------------
	    	existing.setDataDate(updatedData.getDataDate());
	    	existing.setBankName(updatedData.getBankName());
	    	existing.setGroupHeadOfficeSubsidiary(updatedData.getGroupHeadOfficeSubsidiary());
	    	existing.setSubsidiary(updatedData.getSubsidiary());
	    	existing.setBankSymbol(updatedData.getBankSymbol());
	    	existing.setConventionalIslamic(updatedData.getConventionalIslamic());
	    	existing.setLocalForeign(updatedData.getLocalForeign());
	    	existing.setCbuAeTiering(updatedData.getCbuAeTiering());

	    	existing.setAssetBalanceSheetSizeAed(updatedData.getAssetBalanceSheetSizeAed());
	    	existing.setInvestmentBookSizeAed(updatedData.getInvestmentBookSizeAed());
	    	existing.setBookSizeTotalAssetsPct(updatedData.getBookSizeTotalAssetsPct());

	    	existing.setYtdNetPnlAed(updatedData.getYtdNetPnlAed());
	    	existing.setYtdFvtociUnrealizedLossAed(updatedData.getYtdFvtociUnrealizedLossAed());
	    	existing.setCet1Aed(updatedData.getCet1Aed());
	    	existing.setUglCoreTier1RelativeImpactPct(updatedData.getUglCoreTier1RelativeImpactPct());
	    	existing.setYtdAmortizedUnrealizedLossAed(updatedData.getYtdAmortizedUnrealizedLossAed());
	    	existing.setYtdHqlaAcUnrealizedLossAed(updatedData.getYtdHqlaAcUnrealizedLossAed());
	    	existing.setHqlaAcCoreTier1ImpactPct(updatedData.getHqlaAcCoreTier1ImpactPct());

	    	existing.setIrVarConfidenceInterval(updatedData.getIrVarConfidenceInterval());
	    	existing.setIrVarHoldingPeriod(updatedData.getIrVarHoldingPeriod());
	    	existing.setIrFixedIncomeExposureAed(updatedData.getIrFixedIncomeExposureAed());
	    	existing.setIrVarExposureAed(updatedData.getIrVarExposureAed());
	    	existing.setIrVarLimitAed(updatedData.getIrVarLimitAed());
	    	existing.setIrStatus(updatedData.getIrStatus());
	    	existing.setIrVarImpactPct(updatedData.getIrVarImpactPct());

	    	existing.setCsVarConfidenceInterval(updatedData.getCsVarConfidenceInterval());
	    	existing.setCsVarHoldingPeriod(updatedData.getCsVarHoldingPeriod());
	    	existing.setCsFixedIncomeExposureAed(updatedData.getCsFixedIncomeExposureAed());
	    	existing.setCsVarExposureAed(updatedData.getCsVarExposureAed());
	    	existing.setCsVarLimitAed(updatedData.getCsVarLimitAed());
	    	existing.setCsStatus(updatedData.getCsStatus());
	    	existing.setCsVarImpactPct(updatedData.getCsVarImpactPct());

	    	existing.setFxVarConfidenceInterval(updatedData.getFxVarConfidenceInterval());
	    	existing.setFxVarHoldingPeriod(updatedData.getFxVarHoldingPeriod());
	    	existing.setFxExposureAed(updatedData.getFxExposureAed());
	    	existing.setFxVarExposureAed(updatedData.getFxVarExposureAed());
	    	existing.setFxVarLimitAed(updatedData.getFxVarLimitAed());
	    	existing.setFxStatus(updatedData.getFxStatus());
	    	existing.setFxVarImpactPct(updatedData.getFxVarImpactPct());

	    	existing.setEqVarConfidenceInterval(updatedData.getEqVarConfidenceInterval());
	    	existing.setEqVarHoldingPeriod(updatedData.getEqVarHoldingPeriod());
	    	existing.setEqExposureAed(updatedData.getEqExposureAed());
	    	existing.setEqVarExposureAed(updatedData.getEqVarExposureAed());
	    	existing.setEqVarLimitAed(updatedData.getEqVarLimitAed());
	    	existing.setEqStatus(updatedData.getEqStatus());
	    	existing.setEqVarImpactPct(updatedData.getEqVarImpactPct());

	    	existing.setCommVarConfidenceInterval(updatedData.getCommVarConfidenceInterval());
	    	existing.setCommVarHoldingPeriod(updatedData.getCommVarHoldingPeriod());
	    	existing.setCommExposureAed(updatedData.getCommExposureAed());
	    	existing.setCommVarExposureAed(updatedData.getCommVarExposureAed());
	    	existing.setCommVarLimitAed(updatedData.getCommVarLimitAed());
	    	existing.setCommStatus(updatedData.getCommStatus());
	    	existing.setCommVarImpactPct(updatedData.getCommVarImpactPct());

	    	existing.setOverallVarConfidenceInterval(updatedData.getOverallVarConfidenceInterval());
	    	existing.setOverallVarHoldingPeriod(updatedData.getOverallVarHoldingPeriod());
	    	existing.setVarExposureAed(updatedData.getVarExposureAed());
	    	existing.setVarLimitAed(updatedData.getVarLimitAed());
	    	existing.setOverallVarStatus(updatedData.getOverallVarStatus());
	    	existing.setPortfolioRelativeImpactPct(updatedData.getPortfolioRelativeImpactPct());

	    	existing.setExpectedShortfallExposureAed(updatedData.getExpectedShortfallExposureAed());
	    	existing.setExpectedShortfallLimitAed(updatedData.getExpectedShortfallLimitAed());
	    	existing.setExpectedShortfallStatus(updatedData.getExpectedShortfallStatus());
	    	existing.setExpectedShortfallImpactPct(updatedData.getExpectedShortfallImpactPct());

	    	existing.setModifiedDuration(updatedData.getModifiedDuration());
	    	existing.setInterestRateShockBps(updatedData.getInterestRateShockBps());
	    	existing.setInterestRateLimit(updatedData.getInterestRateLimit());
	    	existing.setInterestRateStatus(updatedData.getInterestRateStatus());

	    	existing.setDv01Aed(updatedData.getDv01Aed());
	    	existing.setDv01LimitAed(updatedData.getDv01LimitAed());
	    	existing.setDv01Status(updatedData.getDv01Status());
	    	existing.setDv01TenorGapLt3Aed(updatedData.getDv01TenorGapLt3Aed());
	    	existing.setDv01TenorGapGt3Aed(updatedData.getDv01TenorGapGt3Aed());
	    	existing.setDv01Check(updatedData.getDv01Check());

	    	existing.setCs01Aed(updatedData.getCs01Aed());
	    	existing.setCs01ShockBps(updatedData.getCs01ShockBps());
	    	existing.setCs01Limit(updatedData.getCs01Limit());
	    	existing.setCs01Status(updatedData.getCs01Status());
	    	existing.setCs01InvestmentGradeAed(updatedData.getCs01InvestmentGradeAed());
	    	existing.setCs01SubInvestmentAed(updatedData.getCs01SubInvestmentAed());
	    	existing.setCs01UnratedAed(updatedData.getCs01UnratedAed());
	    	existing.setCs01Check(updatedData.getCs01Check());

	    	existing.setBetaEquity(updatedData.getBetaEquity());
	    	existing.setEquityShockBps(updatedData.getEquityShockBps());
	    	existing.setEquityLimit(updatedData.getEquityLimit());
	    	existing.setEquityStatus(updatedData.getEquityStatus());

	    	existing.setCreditSpreadImpactAed(updatedData.getCreditSpreadImpactAed());
	    	existing.setCreditSensitiveImpactPct(updatedData.getCreditSensitiveImpactPct());
	    	existing.setInterestRateImpactAed(updatedData.getInterestRateImpactAed());
	    	existing.setRateSensitiveImpactPct(updatedData.getRateSensitiveImpactPct());

	    	existing.setFxImpactAed(updatedData.getFxImpactAed());
	    	existing.setFxSensitiveImpactPct(updatedData.getFxSensitiveImpactPct());
	    	existing.setEquityImpactAed(updatedData.getEquityImpactAed());
	    	existing.setEquitySensitiveImpactPct(updatedData.getEquitySensitiveImpactPct());

	    	existing.setCommoditiesImpactAed(updatedData.getCommoditiesImpactAed());
	    	existing.setCommoditySensitiveImpactPct(updatedData.getCommoditySensitiveImpactPct());

	    	existing.setJtdLossImpactAed(updatedData.getJtdLossImpactAed());
	    	existing.setJtdRelativeImpactPct(updatedData.getJtdRelativeImpactPct());

	    	existing.setOverallImpactAed(updatedData.getOverallImpactAed());
	    	existing.setCoreTier1RelativeImpactPct(updatedData.getCoreTier1RelativeImpactPct());
	    	// ---------------------------------------------------------------------------



	    	RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS.save(existing);
	        return true;
	    } else {
	        System.out.println("No record found for SI_NO: " + updatedData.getSI_NO());
	        return false;
	    }
	}
    
    

    
}
