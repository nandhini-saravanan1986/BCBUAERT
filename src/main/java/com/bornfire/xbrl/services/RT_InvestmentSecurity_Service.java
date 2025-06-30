package com.bornfire.xbrl.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bornfire.xbrl.entities.RT_Investment_Securities_Data_Template;
import com.bornfire.xbrl.entities.RT_Investment_Securities_Data_Template_Repo;

@Service
public class RT_InvestmentSecurity_Service {

    @Autowired
    RT_Investment_Securities_Data_Template_Repo investmentSecuritiesRepo;

    @Autowired
    private Environment env;

    // Update existing record
    public boolean updateInvestmentSecurity(RT_Investment_Securities_Data_Template updatedEntity) {
        Optional<RT_Investment_Securities_Data_Template> existingOpt =  investmentSecuritiesRepo.findById(updatedEntity.getSiNo());

        if (existingOpt.isPresent()) {
            RT_Investment_Securities_Data_Template existing = existingOpt.get();

            // Copy all fields
            existing.setDataDate(updatedEntity.getDataDate());
            existing.setBankName(updatedEntity.getBankName());
            existing.setHeadOfficeSubsidiary(updatedEntity.getHeadOfficeSubsidiary());
            existing.setSubsidiary(updatedEntity.getSubsidiary());
            existing.setBankSymbol(updatedEntity.getBankSymbol());
            existing.setConventionalIslamic(updatedEntity.getConventionalIslamic());
            existing.setLocalForeign(updatedEntity.getLocalForeign());
            existing.setCbuAeTiering(updatedEntity.getCbuAeTiering());
            existing.setAccountingClassification(updatedEntity.getAccountingClassification());
            existing.setInstrumentType(updatedEntity.getInstrumentType());
            existing.setInstrumentSubcategoryType(updatedEntity.getInstrumentSubcategoryType());
            existing.setIsinCode(updatedEntity.getIsinCode());
            existing.setBankInternalPortfolioCode(updatedEntity.getBankInternalPortfolioCode());
            existing.setCbuAeSecurityId(updatedEntity.getCbuAeSecurityId());
            existing.setCbuAeAccessPrimaryKey(updatedEntity.getCbuAeAccessPrimaryKey());
            existing.setSecurityDescription(updatedEntity.getSecurityDescription());
            existing.setAssetCurrency(updatedEntity.getAssetCurrency());
            existing.setObligor(updatedEntity.getObligor());
            existing.setCountryOfRisk(updatedEntity.getCountryOfRisk());
            existing.setCbuAeGeographicalZone(updatedEntity.getCbuAeGeographicalZone());
            existing.setIndustry(updatedEntity.getIndustry());
            existing.setSector(updatedEntity.getSector());
            existing.setIssuerType(updatedEntity.getIssuerType());
            existing.setIssueSize(updatedEntity.getIssueSize());
            existing.setPercentIssueSize(updatedEntity.getPercentIssueSize());
            existing.setNominalTradeCurrency(updatedEntity.getNominalTradeCurrency());
            existing.setNominalAedEquivalent(updatedEntity.getNominalAedEquivalent());
            existing.setPremDiscAedEquivalent(updatedEntity.getPremDiscAedEquivalent());
            existing.setSpecificProvisionAed(updatedEntity.getSpecificProvisionAed());
            existing.setFixedIncomeInfoNetBookPrice(updatedEntity.getFixedIncomeInfoNetBookPrice());
            existing.setNetBookValueAed(updatedEntity.getNetBookValueAed());
            existing.setPurchaseYieldToMaturity(updatedEntity.getPurchaseYieldToMaturity());
            existing.setCleanPrice(updatedEntity.getCleanPrice());
            existing.setCleanMarketValueAed(updatedEntity.getCleanMarketValueAed());
            existing.setCurrentYieldToMaturity(updatedEntity.getCurrentYieldToMaturity());
            existing.setUnrealizedGainLossAed(updatedEntity.getUnrealizedGainLossAed());
            existing.setSubordinatedDebtSukuk(updatedEntity.getSubordinatedDebtSukuk());
            existing.setFinalRatingBanks(updatedEntity.getFinalRatingBanks());
            existing.setFinalRatingCbuae(updatedEntity.getFinalRatingCbuae());
            existing.setCreditQuality(updatedEntity.getCreditQuality());
            existing.setMaturityDate(updatedEntity.getMaturityDate());
            existing.setResidualMaturity(updatedEntity.getResidualMaturity());
            existing.setMaturityPeriod(updatedEntity.getMaturityPeriod());
            existing.setPercentHoldingsFi(updatedEntity.getPercentHoldingsFi());
            existing.setQtyEquityFunds(updatedEntity.getQtyEquityFunds());
            existing.setEquityFloatingFundAssets(updatedEntity.getEquityFloatingFundAssets());
            existing.setTotalFloatingShareFundAum(updatedEntity.getTotalFloatingShareFundAum());
            existing.setSpecificProvision(updatedEntity.getSpecificProvision());
            existing.setNetBookValue(updatedEntity.getNetBookValue());
            existing.setOtherSecInfoNetBookPrice(updatedEntity.getOtherSecInfoNetBookPrice());
            existing.setMarketPrice(updatedEntity.getMarketPrice());
            existing.setFairValueAmountAed(updatedEntity.getFairValueAmountAed());
            existing.setUnrealizedGainLossAgain(updatedEntity.getUnrealizedGainLossAgain());
            existing.setPercentHoldingsOther(updatedEntity.getPercentHoldingsOther());
            existing.setTotalNetBookValueAed(updatedEntity.getTotalNetBookValueAed());
            existing.setTotalMarketValueAed(updatedEntity.getTotalMarketValueAed());
            existing.setTotalUnrealizedGainLoss(updatedEntity.getTotalUnrealizedGainLoss());
            existing.setUnrealizedGainLossContrib(updatedEntity.getUnrealizedGainLossContrib());
            existing.setPercentHoldings(updatedEntity.getPercentHoldings());
            existing.setExistingPledgeStatus(updatedEntity.getExistingPledgeStatus());
            existing.setPledgedAmountAed(updatedEntity.getPledgedAmountAed());
            existing.setUnencumberedAmountAed(updatedEntity.getUnencumberedAmountAed());
            existing.setProportionUnencumbered(updatedEntity.getProportionUnencumbered());
            existing.setHqlaElarEligibility(updatedEntity.getHqlaElarEligibility());
            existing.setLiquidityType(updatedEntity.getLiquidityType());
            existing.setSukukType(updatedEntity.getSukukType());
            existing.setTtcPd(updatedEntity.getTtcPd());
            existing.setPitPd(updatedEntity.getPitPd());
            existing.setEcl1y(updatedEntity.getEcl1y());
            existing.setEclLifetime(updatedEntity.getEclLifetime());
            existing.setEclFinal(updatedEntity.getEclFinal());
            existing.setIfrs9Staging(updatedEntity.getIfrs9Staging());
            existing.setBankCoreTier1CapitalAed(updatedEntity.getBankCoreTier1CapitalAed());

            existing.setReportSubmitDate(updatedEntity.getReportSubmitDate());                     


            investmentSecuritiesRepo.save(existing);
            return true;
        } else {
            return false;
        }
    }


    // Export Excel from DB data   
  

}
