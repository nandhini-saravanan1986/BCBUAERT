package com.bornfire.xbrl.entities;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BCBUAE_INVESTMENT_RISK_DATA_DASHBOARD_TEMPLATE")
public class RT_Investment_Risk_Data_Dashboard_Template {
	
	@Id
    @Column(name = "SL_NO")
    private Long slNo;

    @Column(name = "DATA_DATE")
    private Date dataDate;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "GROUP_HEAD_OFFICE_SUBSIDIARY")
    private String groupHeadOfficeSubsidiary;

    @Column(name = "SUBSIDIARY")
    private String subsidiary;

    @Column(name = "BANK_SYMBOL")
    private String bankSymbol;

    @Column(name = "CONVENTIONAL_ISLAMIC")
    private String conventionalIslamic;

    @Column(name = "LOCAL_FOREIGN")
    private String localForeign;

    @Column(name = "CBUAE_TIERING")
    private String cbuAeTiering;

    @Column(name = "ASSET_BALANCE_SHEET_SIZE_AED")
    private Long assetBalanceSheetSizeAed;

    @Column(name = "INVESTMENT_BOOK_SIZE_AED")
    private Long investmentBookSizeAed;

    @Column(name = "BOOK_SIZE_TOTAL_ASSETS_PCT")
    private Long bookSizeTotalAssetsPct;

    @Column(name = "YTD_NET_PNL_AED")
    private Long ytdNetPnlAed;

    @Column(name = "YTD_FVTOCI_UNREALIZED_LOSS_AED")
    private Long ytdFvtociUnrealizedLossAed;

    @Column(name = "CET1_AED")
    private Long cet1Aed;

    @Column(name = "UGL_CORE_TIER1_RELATIVE_IMPACT_PCT")
    private Long uglCoreTier1RelativeImpactPct;

    @Column(name = "YTD_AMORTIZED_UNREALIZED_LOSS_AED")
    private Long ytdAmortizedUnrealizedLossAed;

    @Column(name = "YTD_HQLA_AC_UNREALIZED_LOSS_AED")
    private Long ytdHqlaAcUnrealizedLossAed;

    @Column(name = "HQLA_AC_CORE_TIER1_IMPACT_PCT")
    private Long hqlaAcCoreTier1ImpactPct;

    @Column(name = "IR_VAR_CONFIDENCE_INTERVAL")
    private String irVarConfidenceInterval;

    @Column(name = "IR_VAR_HOLDING_PERIOD")
    private String irVarHoldingPeriod;

    @Column(name = "IR_FIXED_INCOME_EXPOSURE_AED")
    private Long irFixedIncomeExposureAed;

    @Column(name = "IR_VAR_EXPOSURE_AED")
    private Long irVarExposureAed;

    @Column(name = "IR_VAR_LIMIT_AED")
    private Long irVarLimitAed;

    @Column(name = "IR_STATUS")
    private String irStatus;

    @Column(name = "IR_VAR_IMPACT_PCT")
    private Long irVarImpactPct;

    @Column(name = "CS_VAR_CONFIDENCE_INTERVAL")
    private String csVarConfidenceInterval;

    @Column(name = "CS_VAR_HOLDING_PERIOD")
    private String csVarHoldingPeriod;

    @Column(name = "CS_FIXED_INCOME_EXPOSURE_AED")
    private Long csFixedIncomeExposureAed;

    @Column(name = "CS_VAR_EXPOSURE_AED")
    private Long csVarExposureAed;

    @Column(name = "CS_VAR_LIMIT_AED")
    private Long csVarLimitAed;

    @Column(name = "CS_STATUS")
    private String csStatus;

    @Column(name = "CS_VAR_IMPACT_PCT")
    private Long csVarImpactPct;

    @Column(name = "FX_VAR_CONFIDENCE_INTERVAL")
    private String fxVarConfidenceInterval;

    @Column(name = "FX_VAR_HOLDING_PERIOD")
    private String fxVarHoldingPeriod;

    @Column(name = "FX_EXPOSURE_AED")
    private Long fxExposureAed;

    @Column(name = "FX_VAR_EXPOSURE_AED")
    private Long fxVarExposureAed;

    @Column(name = "FX_VAR_LIMIT_AED")
    private Long fxVarLimitAed;

    @Column(name = "FX_STATUS")
    private String fxStatus;

    @Column(name = "FX_VAR_IMPACT_PCT")
    private Long fxVarImpactPct;

    @Column(name = "EQ_VAR_CONFIDENCE_INTERVAL")
    private String eqVarConfidenceInterval;

    @Column(name = "EQ_VAR_HOLDING_PERIOD")
    private String eqVarHoldingPeriod;

    @Column(name = "EQ_EXPOSURE_AED")
    private Long eqExposureAed;

    @Column(name = "EQ_VAR_EXPOSURE_AED")
    private Long eqVarExposureAed;

    @Column(name = "EQ_VAR_LIMIT_AED")
    private Long eqVarLimitAed;

    @Column(name = "EQ_STATUS")
    private String eqStatus;

    @Column(name = "EQ_VAR_IMPACT_PCT")
    private Long eqVarImpactPct;

    @Column(name = "COMM_VAR_CONFIDENCE_INTERVAL")
    private String commVarConfidenceInterval;

    @Column(name = "COMM_VAR_HOLDING_PERIOD")
    private String commVarHoldingPeriod;

    @Column(name = "COMM_EXPOSURE_AED")
    private Long commExposureAed;

    @Column(name = "COMM_VAR_EXPOSURE_AED")
    private Long commVarExposureAed;

    @Column(name = "COMM_VAR_LIMIT_AED")
    private Long commVarLimitAed;

    @Column(name = "COMM_STATUS")
    private String commStatus;

    @Column(name = "COMM_VAR_IMPACT_PCT")
    private Long commVarImpactPct;

    @Column(name = "OVERALL_VAR_CONFIDENCE_INTERVAL")
    private String overallVarConfidenceInterval;

    @Column(name = "OVERALL_VAR_HOLDING_PERIOD")
    private String overallVarHoldingPeriod;

    @Column(name = "VAR_EXPOSURE_AED")
    private Long varExposureAed;

    @Column(name = "VAR_LIMIT_AED")
    private Long varLimitAed;

    @Column(name = "OVERALL_VAR_STATUS")
    private String overallVarStatus;

    @Column(name = "PORTFOLIO_RELATIVE_IMPACT_PCT")
    private Long portfolioRelativeImpactPct;

    @Column(name = "EXPECTED_SHORTFALL_EXPOSURE_AED")
    private Long expectedShortfallExposureAed;

    @Column(name = "EXPECTED_SHORTFALL_LIMIT_AED")
    private Long expectedShortfallLimitAed;

    @Column(name = "EXPECTED_SHORTFALL_STATUS")
    private String expectedShortfallStatus;

    @Column(name = "EXPECTED_SHORTFALL_IMPACT_PCT")
    private Long expectedShortfallImpactPct;

    @Column(name = "MODIFIED_DURATION")
    private Long modifiedDuration;

    @Column(name = "INTEREST_RATE_SHOCK_BPS")
    private Integer interestRateShockBps;

    @Column(name = "INTEREST_RATE_LIMIT")
    private Long interestRateLimit;

    @Column(name = "INTEREST_RATE_STATUS")
    private String interestRateStatus;

    @Column(name = "DV01_AED")
    private Long dv01Aed;

    @Column(name = "DV01_LIMIT_AED")
    private Long dv01LimitAed;

    @Column(name = "DV01_STATUS")
    private String dv01Status;

    @Column(name = "DV01_TENOR_GAP_LT3_AED")
    private Long dv01TenorGapLt3Aed;

    @Column(name = "DV01_TENOR_GAP_GT3_AED")
    private Long dv01TenorGapGt3Aed;

    @Column(name = "DV01_CHECK")
    private String dv01Check;

    @Column(name = "CS01_AED")
    private Long cs01Aed;

    @Column(name = "CS01_SHOCK_BPS")
    private Integer cs01ShockBps;

    @Column(name = "CS01_LIMIT")
    private Long cs01Limit;

    @Column(name = "CS01_STATUS")
    private String cs01Status;

    @Column(name = "CS01_INVESTMENT_GRADE_AED")
    private Long cs01InvestmentGradeAed;

    @Column(name = "CS01_SUB_INVESTMENT_AED")
    private Long cs01SubInvestmentAed;

    @Column(name = "CS01_UNRATED_AED")
    private Long cs01UnratedAed;

    @Column(name = "CS01_CHECK")
    private String cs01Check;

    @Column(name = "BETA_EQUITY")
    private Long betaEquity;

    @Column(name = "EQUITY_SHOCK_BPS")
    private Integer equityShockBps;

    @Column(name = "EQUITY_LIMIT")
    private Long equityLimit;

    @Column(name = "EQUITY_STATUS")
    private String equityStatus;

    @Column(name = "CREDIT_SPREAD_IMPACT_AED")
    private Long creditSpreadImpactAed;

    @Column(name = "CREDIT_SENSITIVE_IMPACT_PCT")
    private Long creditSensitiveImpactPct;

    @Column(name = "INTEREST_RATE_IMPACT_AED")
    private Long interestRateImpactAed;

    @Column(name = "RATE_SENSITIVE_IMPACT_PCT")
    private Long rateSensitiveImpactPct;

    @Column(name = "FX_IMPACT_AED")
    private Long fxImpactAed;

    @Column(name = "FX_SENSITIVE_IMPACT_PCT")
    private Long fxSensitiveImpactPct;

    @Column(name = "EQUITY_IMPACT_AED")
    private Long equityImpactAed;

    @Column(name = "EQUITY_SENSITIVE_IMPACT_PCT")
    private Long equitySensitiveImpactPct;

    @Column(name = "COMMODITIES_IMPACT_AED")
    private Long commoditiesImpactAed;

    @Column(name = "COMMODITY_SENSITIVE_IMPACT_PCT")
    private Long commoditySensitiveImpactPct;

    @Column(name = "JTD_LOSS_IMPACT_AED")
    private Long jtdLossImpactAed;

    @Column(name = "JTD_RELATIVE_IMPACT_PCT")
    private Long jtdRelativeImpactPct;

    @Column(name = "OVERALL_IMPACT_AED")
    private Long overallImpactAed;

    @Column(name = "CORE_TIER1_RELATIVE_IMPACT_PCT")
    private Long coreTier1RelativeImpactPct;

    @Column(name = "REPORT_SUBMIT_DATE")
    private Date reportSubmitDate;

    @Column(name = "ENTITY_FLG")
    private String entityFlg;

    @Column(name = "MODIFY_FLG")
    private String modifyFlg;

    @Column(name = "DEL_FLG")
    private String delFlg;

    @Column(name = "REPORT_FROM_DATE")
    private Date reportFromDate;

    @Column(name = "REPORT_TO_DATE")
    private Date reportToDate;

    @Column(name = "REPORT_DATE")
    private Date reportDate;

	public Long getSlNo() {
		return slNo;
	}

	public void setSlNo(Long slNo) {
		this.slNo = slNo;
	}

	public Date getDataDate() {
		return dataDate;
	}

	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getGroupHeadOfficeSubsidiary() {
		return groupHeadOfficeSubsidiary;
	}

	public void setGroupHeadOfficeSubsidiary(String groupHeadOfficeSubsidiary) {
		this.groupHeadOfficeSubsidiary = groupHeadOfficeSubsidiary;
	}

	public String getSubsidiary() {
		return subsidiary;
	}

	public void setSubsidiary(String subsidiary) {
		this.subsidiary = subsidiary;
	}

	public String getBankSymbol() {
		return bankSymbol;
	}

	public void setBankSymbol(String bankSymbol) {
		this.bankSymbol = bankSymbol;
	}

	public String getConventionalIslamic() {
		return conventionalIslamic;
	}

	public void setConventionalIslamic(String conventionalIslamic) {
		this.conventionalIslamic = conventionalIslamic;
	}

	public String getLocalForeign() {
		return localForeign;
	}

	public void setLocalForeign(String localForeign) {
		this.localForeign = localForeign;
	}

	public String getCbuAeTiering() {
		return cbuAeTiering;
	}

	public void setCbuAeTiering(String cbuAeTiering) {
		this.cbuAeTiering = cbuAeTiering;
	}

	public Long getAssetBalanceSheetSizeAed() {
		return assetBalanceSheetSizeAed;
	}

	public void setAssetBalanceSheetSizeAed(Long assetBalanceSheetSizeAed) {
		this.assetBalanceSheetSizeAed = assetBalanceSheetSizeAed;
	}

	public Long getInvestmentBookSizeAed() {
		return investmentBookSizeAed;
	}

	public void setInvestmentBookSizeAed(Long investmentBookSizeAed) {
		this.investmentBookSizeAed = investmentBookSizeAed;
	}

	public Long getBookSizeTotalAssetsPct() {
		return bookSizeTotalAssetsPct;
	}

	public void setBookSizeTotalAssetsPct(Long bookSizeTotalAssetsPct) {
		this.bookSizeTotalAssetsPct = bookSizeTotalAssetsPct;
	}

	public Long getYtdNetPnlAed() {
		return ytdNetPnlAed;
	}

	public void setYtdNetPnlAed(Long ytdNetPnlAed) {
		this.ytdNetPnlAed = ytdNetPnlAed;
	}

	public Long getYtdFvtociUnrealizedLossAed() {
		return ytdFvtociUnrealizedLossAed;
	}

	public void setYtdFvtociUnrealizedLossAed(Long ytdFvtociUnrealizedLossAed) {
		this.ytdFvtociUnrealizedLossAed = ytdFvtociUnrealizedLossAed;
	}

	public Long getCet1Aed() {
		return cet1Aed;
	}

	public void setCet1Aed(Long cet1Aed) {
		this.cet1Aed = cet1Aed;
	}

	public Long getUglCoreTier1RelativeImpactPct() {
		return uglCoreTier1RelativeImpactPct;
	}

	public void setUglCoreTier1RelativeImpactPct(Long uglCoreTier1RelativeImpactPct) {
		this.uglCoreTier1RelativeImpactPct = uglCoreTier1RelativeImpactPct;
	}

	public Long getYtdAmortizedUnrealizedLossAed() {
		return ytdAmortizedUnrealizedLossAed;
	}

	public void setYtdAmortizedUnrealizedLossAed(Long ytdAmortizedUnrealizedLossAed) {
		this.ytdAmortizedUnrealizedLossAed = ytdAmortizedUnrealizedLossAed;
	}

	public Long getYtdHqlaAcUnrealizedLossAed() {
		return ytdHqlaAcUnrealizedLossAed;
	}

	public void setYtdHqlaAcUnrealizedLossAed(Long ytdHqlaAcUnrealizedLossAed) {
		this.ytdHqlaAcUnrealizedLossAed = ytdHqlaAcUnrealizedLossAed;
	}

	public Long getHqlaAcCoreTier1ImpactPct() {
		return hqlaAcCoreTier1ImpactPct;
	}

	public void setHqlaAcCoreTier1ImpactPct(Long hqlaAcCoreTier1ImpactPct) {
		this.hqlaAcCoreTier1ImpactPct = hqlaAcCoreTier1ImpactPct;
	}

	public String getIrVarConfidenceInterval() {
		return irVarConfidenceInterval;
	}

	public void setIrVarConfidenceInterval(String irVarConfidenceInterval) {
		this.irVarConfidenceInterval = irVarConfidenceInterval;
	}

	public String getIrVarHoldingPeriod() {
		return irVarHoldingPeriod;
	}

	public void setIrVarHoldingPeriod(String irVarHoldingPeriod) {
		this.irVarHoldingPeriod = irVarHoldingPeriod;
	}

	public Long getIrFixedIncomeExposureAed() {
		return irFixedIncomeExposureAed;
	}

	public void setIrFixedIncomeExposureAed(Long irFixedIncomeExposureAed) {
		this.irFixedIncomeExposureAed = irFixedIncomeExposureAed;
	}

	public Long getIrVarExposureAed() {
		return irVarExposureAed;
	}

	public void setIrVarExposureAed(Long irVarExposureAed) {
		this.irVarExposureAed = irVarExposureAed;
	}

	public Long getIrVarLimitAed() {
		return irVarLimitAed;
	}

	public void setIrVarLimitAed(Long irVarLimitAed) {
		this.irVarLimitAed = irVarLimitAed;
	}

	public String getIrStatus() {
		return irStatus;
	}

	public void setIrStatus(String irStatus) {
		this.irStatus = irStatus;
	}

	public Long getIrVarImpactPct() {
		return irVarImpactPct;
	}

	public void setIrVarImpactPct(Long irVarImpactPct) {
		this.irVarImpactPct = irVarImpactPct;
	}

	public String getCsVarConfidenceInterval() {
		return csVarConfidenceInterval;
	}

	public void setCsVarConfidenceInterval(String csVarConfidenceInterval) {
		this.csVarConfidenceInterval = csVarConfidenceInterval;
	}

	public String getCsVarHoldingPeriod() {
		return csVarHoldingPeriod;
	}

	public void setCsVarHoldingPeriod(String csVarHoldingPeriod) {
		this.csVarHoldingPeriod = csVarHoldingPeriod;
	}

	public Long getCsFixedIncomeExposureAed() {
		return csFixedIncomeExposureAed;
	}

	public void setCsFixedIncomeExposureAed(Long csFixedIncomeExposureAed) {
		this.csFixedIncomeExposureAed = csFixedIncomeExposureAed;
	}

	public Long getCsVarExposureAed() {
		return csVarExposureAed;
	}

	public void setCsVarExposureAed(Long csVarExposureAed) {
		this.csVarExposureAed = csVarExposureAed;
	}

	public Long getCsVarLimitAed() {
		return csVarLimitAed;
	}

	public void setCsVarLimitAed(Long csVarLimitAed) {
		this.csVarLimitAed = csVarLimitAed;
	}

	public String getCsStatus() {
		return csStatus;
	}

	public void setCsStatus(String csStatus) {
		this.csStatus = csStatus;
	}

	public Long getCsVarImpactPct() {
		return csVarImpactPct;
	}

	public void setCsVarImpactPct(Long csVarImpactPct) {
		this.csVarImpactPct = csVarImpactPct;
	}

	public String getFxVarConfidenceInterval() {
		return fxVarConfidenceInterval;
	}

	public void setFxVarConfidenceInterval(String fxVarConfidenceInterval) {
		this.fxVarConfidenceInterval = fxVarConfidenceInterval;
	}

	public String getFxVarHoldingPeriod() {
		return fxVarHoldingPeriod;
	}

	public void setFxVarHoldingPeriod(String fxVarHoldingPeriod) {
		this.fxVarHoldingPeriod = fxVarHoldingPeriod;
	}

	public Long getFxExposureAed() {
		return fxExposureAed;
	}

	public void setFxExposureAed(Long fxExposureAed) {
		this.fxExposureAed = fxExposureAed;
	}

	public Long getFxVarExposureAed() {
		return fxVarExposureAed;
	}

	public void setFxVarExposureAed(Long fxVarExposureAed) {
		this.fxVarExposureAed = fxVarExposureAed;
	}

	public Long getFxVarLimitAed() {
		return fxVarLimitAed;
	}

	public void setFxVarLimitAed(Long fxVarLimitAed) {
		this.fxVarLimitAed = fxVarLimitAed;
	}

	public String getFxStatus() {
		return fxStatus;
	}

	public void setFxStatus(String fxStatus) {
		this.fxStatus = fxStatus;
	}

	public Long getFxVarImpactPct() {
		return fxVarImpactPct;
	}

	public void setFxVarImpactPct(Long fxVarImpactPct) {
		this.fxVarImpactPct = fxVarImpactPct;
	}

	public String getEqVarConfidenceInterval() {
		return eqVarConfidenceInterval;
	}

	public void setEqVarConfidenceInterval(String eqVarConfidenceInterval) {
		this.eqVarConfidenceInterval = eqVarConfidenceInterval;
	}

	public String getEqVarHoldingPeriod() {
		return eqVarHoldingPeriod;
	}

	public void setEqVarHoldingPeriod(String eqVarHoldingPeriod) {
		this.eqVarHoldingPeriod = eqVarHoldingPeriod;
	}

	public Long getEqExposureAed() {
		return eqExposureAed;
	}

	public void setEqExposureAed(Long eqExposureAed) {
		this.eqExposureAed = eqExposureAed;
	}

	public Long getEqVarExposureAed() {
		return eqVarExposureAed;
	}

	public void setEqVarExposureAed(Long eqVarExposureAed) {
		this.eqVarExposureAed = eqVarExposureAed;
	}

	public Long getEqVarLimitAed() {
		return eqVarLimitAed;
	}

	public void setEqVarLimitAed(Long eqVarLimitAed) {
		this.eqVarLimitAed = eqVarLimitAed;
	}

	public String getEqStatus() {
		return eqStatus;
	}

	public void setEqStatus(String eqStatus) {
		this.eqStatus = eqStatus;
	}

	public Long getEqVarImpactPct() {
		return eqVarImpactPct;
	}

	public void setEqVarImpactPct(Long eqVarImpactPct) {
		this.eqVarImpactPct = eqVarImpactPct;
	}

	public String getCommVarConfidenceInterval() {
		return commVarConfidenceInterval;
	}

	public void setCommVarConfidenceInterval(String commVarConfidenceInterval) {
		this.commVarConfidenceInterval = commVarConfidenceInterval;
	}

	public String getCommVarHoldingPeriod() {
		return commVarHoldingPeriod;
	}

	public void setCommVarHoldingPeriod(String commVarHoldingPeriod) {
		this.commVarHoldingPeriod = commVarHoldingPeriod;
	}

	public Long getCommExposureAed() {
		return commExposureAed;
	}

	public void setCommExposureAed(Long commExposureAed) {
		this.commExposureAed = commExposureAed;
	}

	public Long getCommVarExposureAed() {
		return commVarExposureAed;
	}

	public void setCommVarExposureAed(Long commVarExposureAed) {
		this.commVarExposureAed = commVarExposureAed;
	}

	public Long getCommVarLimitAed() {
		return commVarLimitAed;
	}

	public void setCommVarLimitAed(Long commVarLimitAed) {
		this.commVarLimitAed = commVarLimitAed;
	}

	public String getCommStatus() {
		return commStatus;
	}

	public void setCommStatus(String commStatus) {
		this.commStatus = commStatus;
	}

	public Long getCommVarImpactPct() {
		return commVarImpactPct;
	}

	public void setCommVarImpactPct(Long commVarImpactPct) {
		this.commVarImpactPct = commVarImpactPct;
	}

	public String getOverallVarConfidenceInterval() {
		return overallVarConfidenceInterval;
	}

	public void setOverallVarConfidenceInterval(String overallVarConfidenceInterval) {
		this.overallVarConfidenceInterval = overallVarConfidenceInterval;
	}

	public String getOverallVarHoldingPeriod() {
		return overallVarHoldingPeriod;
	}

	public void setOverallVarHoldingPeriod(String overallVarHoldingPeriod) {
		this.overallVarHoldingPeriod = overallVarHoldingPeriod;
	}

	public Long getVarExposureAed() {
		return varExposureAed;
	}

	public void setVarExposureAed(Long varExposureAed) {
		this.varExposureAed = varExposureAed;
	}

	public Long getVarLimitAed() {
		return varLimitAed;
	}

	public void setVarLimitAed(Long varLimitAed) {
		this.varLimitAed = varLimitAed;
	}

	public String getOverallVarStatus() {
		return overallVarStatus;
	}

	public void setOverallVarStatus(String overallVarStatus) {
		this.overallVarStatus = overallVarStatus;
	}

	public Long getPortfolioRelativeImpactPct() {
		return portfolioRelativeImpactPct;
	}

	public void setPortfolioRelativeImpactPct(Long portfolioRelativeImpactPct) {
		this.portfolioRelativeImpactPct = portfolioRelativeImpactPct;
	}

	public Long getExpectedShortfallExposureAed() {
		return expectedShortfallExposureAed;
	}

	public void setExpectedShortfallExposureAed(Long expectedShortfallExposureAed) {
		this.expectedShortfallExposureAed = expectedShortfallExposureAed;
	}

	public Long getExpectedShortfallLimitAed() {
		return expectedShortfallLimitAed;
	}

	public void setExpectedShortfallLimitAed(Long expectedShortfallLimitAed) {
		this.expectedShortfallLimitAed = expectedShortfallLimitAed;
	}

	public String getExpectedShortfallStatus() {
		return expectedShortfallStatus;
	}

	public void setExpectedShortfallStatus(String expectedShortfallStatus) {
		this.expectedShortfallStatus = expectedShortfallStatus;
	}

	public Long getExpectedShortfallImpactPct() {
		return expectedShortfallImpactPct;
	}

	public void setExpectedShortfallImpactPct(Long expectedShortfallImpactPct) {
		this.expectedShortfallImpactPct = expectedShortfallImpactPct;
	}

	public Long getModifiedDuration() {
		return modifiedDuration;
	}

	public void setModifiedDuration(Long modifiedDuration) {
		this.modifiedDuration = modifiedDuration;
	}

	public Integer getInterestRateShockBps() {
		return interestRateShockBps;
	}

	public void setInterestRateShockBps(Integer interestRateShockBps) {
		this.interestRateShockBps = interestRateShockBps;
	}

	public Long getInterestRateLimit() {
		return interestRateLimit;
	}

	public void setInterestRateLimit(Long interestRateLimit) {
		this.interestRateLimit = interestRateLimit;
	}

	public String getInterestRateStatus() {
		return interestRateStatus;
	}

	public void setInterestRateStatus(String interestRateStatus) {
		this.interestRateStatus = interestRateStatus;
	}

	public Long getDv01Aed() {
		return dv01Aed;
	}

	public void setDv01Aed(Long dv01Aed) {
		this.dv01Aed = dv01Aed;
	}

	public Long getDv01LimitAed() {
		return dv01LimitAed;
	}

	public void setDv01LimitAed(Long dv01LimitAed) {
		this.dv01LimitAed = dv01LimitAed;
	}

	public String getDv01Status() {
		return dv01Status;
	}

	public void setDv01Status(String dv01Status) {
		this.dv01Status = dv01Status;
	}

	public Long getDv01TenorGapLt3Aed() {
		return dv01TenorGapLt3Aed;
	}

	public void setDv01TenorGapLt3Aed(Long dv01TenorGapLt3Aed) {
		this.dv01TenorGapLt3Aed = dv01TenorGapLt3Aed;
	}

	public Long getDv01TenorGapGt3Aed() {
		return dv01TenorGapGt3Aed;
	}

	public void setDv01TenorGapGt3Aed(Long dv01TenorGapGt3Aed) {
		this.dv01TenorGapGt3Aed = dv01TenorGapGt3Aed;
	}

	public String getDv01Check() {
		return dv01Check;
	}

	public void setDv01Check(String dv01Check) {
		this.dv01Check = dv01Check;
	}

	public Long getCs01Aed() {
		return cs01Aed;
	}

	public void setCs01Aed(Long cs01Aed) {
		this.cs01Aed = cs01Aed;
	}

	public Integer getCs01ShockBps() {
		return cs01ShockBps;
	}

	public void setCs01ShockBps(Integer cs01ShockBps) {
		this.cs01ShockBps = cs01ShockBps;
	}

	public Long getCs01Limit() {
		return cs01Limit;
	}

	public void setCs01Limit(Long cs01Limit) {
		this.cs01Limit = cs01Limit;
	}

	public String getCs01Status() {
		return cs01Status;
	}

	public void setCs01Status(String cs01Status) {
		this.cs01Status = cs01Status;
	}

	public Long getCs01InvestmentGradeAed() {
		return cs01InvestmentGradeAed;
	}

	public void setCs01InvestmentGradeAed(Long cs01InvestmentGradeAed) {
		this.cs01InvestmentGradeAed = cs01InvestmentGradeAed;
	}

	public Long getCs01SubInvestmentAed() {
		return cs01SubInvestmentAed;
	}

	public void setCs01SubInvestmentAed(Long cs01SubInvestmentAed) {
		this.cs01SubInvestmentAed = cs01SubInvestmentAed;
	}

	public Long getCs01UnratedAed() {
		return cs01UnratedAed;
	}

	public void setCs01UnratedAed(Long cs01UnratedAed) {
		this.cs01UnratedAed = cs01UnratedAed;
	}

	public String getCs01Check() {
		return cs01Check;
	}

	public void setCs01Check(String cs01Check) {
		this.cs01Check = cs01Check;
	}

	public Long getBetaEquity() {
		return betaEquity;
	}

	public void setBetaEquity(Long betaEquity) {
		this.betaEquity = betaEquity;
	}

	public Integer getEquityShockBps() {
		return equityShockBps;
	}

	public void setEquityShockBps(Integer equityShockBps) {
		this.equityShockBps = equityShockBps;
	}

	public Long getEquityLimit() {
		return equityLimit;
	}

	public void setEquityLimit(Long equityLimit) {
		this.equityLimit = equityLimit;
	}

	public String getEquityStatus() {
		return equityStatus;
	}

	public void setEquityStatus(String equityStatus) {
		this.equityStatus = equityStatus;
	}

	public Long getCreditSpreadImpactAed() {
		return creditSpreadImpactAed;
	}

	public void setCreditSpreadImpactAed(Long creditSpreadImpactAed) {
		this.creditSpreadImpactAed = creditSpreadImpactAed;
	}

	public Long getCreditSensitiveImpactPct() {
		return creditSensitiveImpactPct;
	}

	public void setCreditSensitiveImpactPct(Long creditSensitiveImpactPct) {
		this.creditSensitiveImpactPct = creditSensitiveImpactPct;
	}

	public Long getInterestRateImpactAed() {
		return interestRateImpactAed;
	}

	public void setInterestRateImpactAed(Long interestRateImpactAed) {
		this.interestRateImpactAed = interestRateImpactAed;
	}

	public Long getRateSensitiveImpactPct() {
		return rateSensitiveImpactPct;
	}

	public void setRateSensitiveImpactPct(Long rateSensitiveImpactPct) {
		this.rateSensitiveImpactPct = rateSensitiveImpactPct;
	}

	public Long getFxImpactAed() {
		return fxImpactAed;
	}

	public void setFxImpactAed(Long fxImpactAed) {
		this.fxImpactAed = fxImpactAed;
	}

	public Long getFxSensitiveImpactPct() {
		return fxSensitiveImpactPct;
	}

	public void setFxSensitiveImpactPct(Long fxSensitiveImpactPct) {
		this.fxSensitiveImpactPct = fxSensitiveImpactPct;
	}

	public Long getEquityImpactAed() {
		return equityImpactAed;
	}

	public void setEquityImpactAed(Long equityImpactAed) {
		this.equityImpactAed = equityImpactAed;
	}

	public Long getEquitySensitiveImpactPct() {
		return equitySensitiveImpactPct;
	}

	public void setEquitySensitiveImpactPct(Long equitySensitiveImpactPct) {
		this.equitySensitiveImpactPct = equitySensitiveImpactPct;
	}

	public Long getCommoditiesImpactAed() {
		return commoditiesImpactAed;
	}

	public void setCommoditiesImpactAed(Long commoditiesImpactAed) {
		this.commoditiesImpactAed = commoditiesImpactAed;
	}

	public Long getCommoditySensitiveImpactPct() {
		return commoditySensitiveImpactPct;
	}

	public void setCommoditySensitiveImpactPct(Long commoditySensitiveImpactPct) {
		this.commoditySensitiveImpactPct = commoditySensitiveImpactPct;
	}

	public Long getJtdLossImpactAed() {
		return jtdLossImpactAed;
	}

	public void setJtdLossImpactAed(Long jtdLossImpactAed) {
		this.jtdLossImpactAed = jtdLossImpactAed;
	}

	public Long getJtdRelativeImpactPct() {
		return jtdRelativeImpactPct;
	}

	public void setJtdRelativeImpactPct(Long jtdRelativeImpactPct) {
		this.jtdRelativeImpactPct = jtdRelativeImpactPct;
	}

	public Long getOverallImpactAed() {
		return overallImpactAed;
	}

	public void setOverallImpactAed(Long overallImpactAed) {
		this.overallImpactAed = overallImpactAed;
	}

	public Long getCoreTier1RelativeImpactPct() {
		return coreTier1RelativeImpactPct;
	}

	public void setCoreTier1RelativeImpactPct(Long coreTier1RelativeImpactPct) {
		this.coreTier1RelativeImpactPct = coreTier1RelativeImpactPct;
	}

	public Date getReportSubmitDate() {
		return reportSubmitDate;
	}

	public void setReportSubmitDate(Date reportSubmitDate) {
		this.reportSubmitDate = reportSubmitDate;
	}

	public String getEntityFlg() {
		return entityFlg;
	}

	public void setEntityFlg(String entityFlg) {
		this.entityFlg = entityFlg;
	}

	public String getModifyFlg() {
		return modifyFlg;
	}

	public void setModifyFlg(String modifyFlg) {
		this.modifyFlg = modifyFlg;
	}

	public String getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}

	public Date getReportFromDate() {
		return reportFromDate;
	}

	public void setReportFromDate(Date reportFromDate) {
		this.reportFromDate = reportFromDate;
	}

	public Date getReportToDate() {
		return reportToDate;
	}

	public void setReportToDate(Date reportToDate) {
		this.reportToDate = reportToDate;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public RT_Investment_Risk_Data_Dashboard_Template(Long slNo, Date dataDate, String bankName,
			String groupHeadOfficeSubsidiary, String subsidiary, String bankSymbol, String conventionalIslamic,
			String localForeign, String cbuAeTiering, Long assetBalanceSheetSizeAed, Long investmentBookSizeAed,
			Long bookSizeTotalAssetsPct, Long ytdNetPnlAed, Long ytdFvtociUnrealizedLossAed, Long cet1Aed,
			Long uglCoreTier1RelativeImpactPct, Long ytdAmortizedUnrealizedLossAed, Long ytdHqlaAcUnrealizedLossAed,
			Long hqlaAcCoreTier1ImpactPct, String irVarConfidenceInterval, String irVarHoldingPeriod,
			Long irFixedIncomeExposureAed, Long irVarExposureAed, Long irVarLimitAed, String irStatus,
			Long irVarImpactPct, String csVarConfidenceInterval, String csVarHoldingPeriod,
			Long csFixedIncomeExposureAed, Long csVarExposureAed, Long csVarLimitAed, String csStatus,
			Long csVarImpactPct, String fxVarConfidenceInterval, String fxVarHoldingPeriod, Long fxExposureAed,
			Long fxVarExposureAed, Long fxVarLimitAed, String fxStatus, Long fxVarImpactPct,
			String eqVarConfidenceInterval, String eqVarHoldingPeriod, Long eqExposureAed, Long eqVarExposureAed,
			Long eqVarLimitAed, String eqStatus, Long eqVarImpactPct, String commVarConfidenceInterval,
			String commVarHoldingPeriod, Long commExposureAed, Long commVarExposureAed, Long commVarLimitAed,
			String commStatus, Long commVarImpactPct, String overallVarConfidenceInterval,
			String overallVarHoldingPeriod, Long varExposureAed, Long varLimitAed, String overallVarStatus,
			Long portfolioRelativeImpactPct, Long expectedShortfallExposureAed, Long expectedShortfallLimitAed,
			String expectedShortfallStatus, Long expectedShortfallImpactPct, Long modifiedDuration,
			Integer interestRateShockBps, Long interestRateLimit, String interestRateStatus, Long dv01Aed,
			Long dv01LimitAed, String dv01Status, Long dv01TenorGapLt3Aed, Long dv01TenorGapGt3Aed, String dv01Check,
			Long cs01Aed, Integer cs01ShockBps, Long cs01Limit, String cs01Status, Long cs01InvestmentGradeAed,
			Long cs01SubInvestmentAed, Long cs01UnratedAed, String cs01Check, Long betaEquity, Integer equityShockBps,
			Long equityLimit, String equityStatus, Long creditSpreadImpactAed, Long creditSensitiveImpactPct,
			Long interestRateImpactAed, Long rateSensitiveImpactPct, Long fxImpactAed, Long fxSensitiveImpactPct,
			Long equityImpactAed, Long equitySensitiveImpactPct, Long commoditiesImpactAed,
			Long commoditySensitiveImpactPct, Long jtdLossImpactAed, Long jtdRelativeImpactPct, Long overallImpactAed,
			Long coreTier1RelativeImpactPct, Date reportSubmitDate, String entityFlg, String modifyFlg, String delFlg,
			Date reportFromDate, Date reportToDate, Date reportDate) {
		super();
		this.slNo = slNo;
		this.dataDate = dataDate;
		this.bankName = bankName;
		this.groupHeadOfficeSubsidiary = groupHeadOfficeSubsidiary;
		this.subsidiary = subsidiary;
		this.bankSymbol = bankSymbol;
		this.conventionalIslamic = conventionalIslamic;
		this.localForeign = localForeign;
		this.cbuAeTiering = cbuAeTiering;
		this.assetBalanceSheetSizeAed = assetBalanceSheetSizeAed;
		this.investmentBookSizeAed = investmentBookSizeAed;
		this.bookSizeTotalAssetsPct = bookSizeTotalAssetsPct;
		this.ytdNetPnlAed = ytdNetPnlAed;
		this.ytdFvtociUnrealizedLossAed = ytdFvtociUnrealizedLossAed;
		this.cet1Aed = cet1Aed;
		this.uglCoreTier1RelativeImpactPct = uglCoreTier1RelativeImpactPct;
		this.ytdAmortizedUnrealizedLossAed = ytdAmortizedUnrealizedLossAed;
		this.ytdHqlaAcUnrealizedLossAed = ytdHqlaAcUnrealizedLossAed;
		this.hqlaAcCoreTier1ImpactPct = hqlaAcCoreTier1ImpactPct;
		this.irVarConfidenceInterval = irVarConfidenceInterval;
		this.irVarHoldingPeriod = irVarHoldingPeriod;
		this.irFixedIncomeExposureAed = irFixedIncomeExposureAed;
		this.irVarExposureAed = irVarExposureAed;
		this.irVarLimitAed = irVarLimitAed;
		this.irStatus = irStatus;
		this.irVarImpactPct = irVarImpactPct;
		this.csVarConfidenceInterval = csVarConfidenceInterval;
		this.csVarHoldingPeriod = csVarHoldingPeriod;
		this.csFixedIncomeExposureAed = csFixedIncomeExposureAed;
		this.csVarExposureAed = csVarExposureAed;
		this.csVarLimitAed = csVarLimitAed;
		this.csStatus = csStatus;
		this.csVarImpactPct = csVarImpactPct;
		this.fxVarConfidenceInterval = fxVarConfidenceInterval;
		this.fxVarHoldingPeriod = fxVarHoldingPeriod;
		this.fxExposureAed = fxExposureAed;
		this.fxVarExposureAed = fxVarExposureAed;
		this.fxVarLimitAed = fxVarLimitAed;
		this.fxStatus = fxStatus;
		this.fxVarImpactPct = fxVarImpactPct;
		this.eqVarConfidenceInterval = eqVarConfidenceInterval;
		this.eqVarHoldingPeriod = eqVarHoldingPeriod;
		this.eqExposureAed = eqExposureAed;
		this.eqVarExposureAed = eqVarExposureAed;
		this.eqVarLimitAed = eqVarLimitAed;
		this.eqStatus = eqStatus;
		this.eqVarImpactPct = eqVarImpactPct;
		this.commVarConfidenceInterval = commVarConfidenceInterval;
		this.commVarHoldingPeriod = commVarHoldingPeriod;
		this.commExposureAed = commExposureAed;
		this.commVarExposureAed = commVarExposureAed;
		this.commVarLimitAed = commVarLimitAed;
		this.commStatus = commStatus;
		this.commVarImpactPct = commVarImpactPct;
		this.overallVarConfidenceInterval = overallVarConfidenceInterval;
		this.overallVarHoldingPeriod = overallVarHoldingPeriod;
		this.varExposureAed = varExposureAed;
		this.varLimitAed = varLimitAed;
		this.overallVarStatus = overallVarStatus;
		this.portfolioRelativeImpactPct = portfolioRelativeImpactPct;
		this.expectedShortfallExposureAed = expectedShortfallExposureAed;
		this.expectedShortfallLimitAed = expectedShortfallLimitAed;
		this.expectedShortfallStatus = expectedShortfallStatus;
		this.expectedShortfallImpactPct = expectedShortfallImpactPct;
		this.modifiedDuration = modifiedDuration;
		this.interestRateShockBps = interestRateShockBps;
		this.interestRateLimit = interestRateLimit;
		this.interestRateStatus = interestRateStatus;
		this.dv01Aed = dv01Aed;
		this.dv01LimitAed = dv01LimitAed;
		this.dv01Status = dv01Status;
		this.dv01TenorGapLt3Aed = dv01TenorGapLt3Aed;
		this.dv01TenorGapGt3Aed = dv01TenorGapGt3Aed;
		this.dv01Check = dv01Check;
		this.cs01Aed = cs01Aed;
		this.cs01ShockBps = cs01ShockBps;
		this.cs01Limit = cs01Limit;
		this.cs01Status = cs01Status;
		this.cs01InvestmentGradeAed = cs01InvestmentGradeAed;
		this.cs01SubInvestmentAed = cs01SubInvestmentAed;
		this.cs01UnratedAed = cs01UnratedAed;
		this.cs01Check = cs01Check;
		this.betaEquity = betaEquity;
		this.equityShockBps = equityShockBps;
		this.equityLimit = equityLimit;
		this.equityStatus = equityStatus;
		this.creditSpreadImpactAed = creditSpreadImpactAed;
		this.creditSensitiveImpactPct = creditSensitiveImpactPct;
		this.interestRateImpactAed = interestRateImpactAed;
		this.rateSensitiveImpactPct = rateSensitiveImpactPct;
		this.fxImpactAed = fxImpactAed;
		this.fxSensitiveImpactPct = fxSensitiveImpactPct;
		this.equityImpactAed = equityImpactAed;
		this.equitySensitiveImpactPct = equitySensitiveImpactPct;
		this.commoditiesImpactAed = commoditiesImpactAed;
		this.commoditySensitiveImpactPct = commoditySensitiveImpactPct;
		this.jtdLossImpactAed = jtdLossImpactAed;
		this.jtdRelativeImpactPct = jtdRelativeImpactPct;
		this.overallImpactAed = overallImpactAed;
		this.coreTier1RelativeImpactPct = coreTier1RelativeImpactPct;
		this.reportSubmitDate = reportSubmitDate;
		this.entityFlg = entityFlg;
		this.modifyFlg = modifyFlg;
		this.delFlg = delFlg;
		this.reportFromDate = reportFromDate;
		this.reportToDate = reportToDate;
		this.reportDate = reportDate;
	}

	public RT_Investment_Risk_Data_Dashboard_Template() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    
}
