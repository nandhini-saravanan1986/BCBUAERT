package com.bornfire.xbrl.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="BCBUAE_LIQUIDITY_RISK_DASHBOARD_TEMPLATE")
public class RT_Liquidity_Risk_Dashboard_Template {
	  @Id
	    @Column(name = "SI_NO")
	    private String siNo;

	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    @Column(name = "REPORT_DATE")
	    private Date reportDate;

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
	    private BigDecimal assetBalanceSheetSizeAed;

	    @Column(name = "CASH_AED")
	    private BigDecimal cashAed;

	    @Column(name = "CASH_PERCENT")
	    private BigDecimal cashPercent;

	    @Column(name = "DUE_FROM_CENTRAL_BANK_AED")
	    private BigDecimal dueFromCentralBankAed;

	    @Column(name = "DUE_FROM_CENTRAL_BANK_PERCENT")
	    private BigDecimal dueFromCentralBankPercent;

	    @Column(name = "DUE_FROM_HO_BRANCH_SUBS_AED")
	    private BigDecimal dueFromHoBranchSubsAed;

	    @Column(name = "DUE_FROM_HO_BRANCH_SUBS_PERCENT")
	    private BigDecimal dueFromHoBranchSubsPercent;

	    @Column(name = "DUE_FROM_BANKS_AED")
	    private BigDecimal dueFromBanksAed;

	    @Column(name = "DUE_FROM_BANKS_PERCENT")
	    private BigDecimal dueFromBanksPercent;

	    @Column(name = "DUE_FROM_NBFI_AED")
	    private BigDecimal dueFromNbfiAed;

	    @Column(name = "DUE_FROM_NBFI_PERCENT")
	    private BigDecimal dueFromNbfiPercent;

	    @Column(name = "INVESTMENT_AED")
	    private BigDecimal investmentAed;

	    @Column(name = "INVESTMENT_PERCENT")
	    private BigDecimal investmentPercent;

	    @Column(name = "OTHER_INVESTMENTS_AED")
	    private BigDecimal otherInvestmentsAed;

	    @Column(name = "OTHER_INVESTMENTS_PERCENT")
	    private BigDecimal otherInvestmentsPercent;

	    @Column(name = "NET_CREDIT_PORTFOLIO_AED")
	    private BigDecimal netCreditPortfolioAed;

	    @Column(name = "NET_CREDIT_PORTFOLIO_PERCENT")
	    private BigDecimal netCreditPortfolioPercent;

	    @Column(name = "TRADE_BILLS_AED")
	    private BigDecimal tradeBillsAed;

	    @Column(name = "TRADE_BILLS_PERCENT")
	    private BigDecimal tradeBillsPercent;

	    @Column(name = "NET_FIXED_OTHER_ASSETS_AED")
	    private BigDecimal netFixedOtherAssetsAed;

	    @Column(name = "NET_FIXED_OTHER_ASSETS_PERCENT")
	    private BigDecimal netFixedOtherAssetsPercent;

	    @Column(name = "POSITIVE_FV_DERIVATIVES_AED")
	    private BigDecimal positiveFvDerivativesAed;

	    @Column(name = "POSITIVE_FV_DERIVATIVES_PERCENT")
	    private BigDecimal positiveFvDerivativesPercent;

	    @Column(name = "LIABILITY_BALANCE_SHEET_SIZE_AED")
	    private BigDecimal liabilityBalanceSheetSizeAed;

	    @Column(name = "DUE_TO_CENTRAL_BANK_AED")
	    private BigDecimal dueToCentralBankAed;

	    @Column(name = "DUE_TO_CENTRAL_BANK_PERCENT")
	    private BigDecimal dueToCentralBankPercent;

	    @Column(name = "DUE_TO_HO_BRANCH_SUBS_AED")
	    private BigDecimal dueToHoBranchSubsAed;

	    @Column(name = "DUE_TO_HO_PERCENT")
	    private BigDecimal dueToHoPercent;

	    @Column(name = "DUE_TO_BANKS_AED")
	    private BigDecimal dueToBanksAed;

	    @Column(name = "DUE_TO_BANKS_PERCENT")
	    private BigDecimal dueToBanksPercent;

	    @Column(name = "DUE_TO_NBFI_AED")
	    private BigDecimal dueToNbfiAed;

	    @Column(name = "DUE_TO_NBFI_PERCENT")
	    private BigDecimal dueToNbfiPercent;

	    @Column(name = "CUSTOMER_DEPOSIT_AED")
	    private BigDecimal customerDepositAed;

	    @Column(name = "CUSTOMER_DEPOSIT_PERCENT")
	    private BigDecimal customerDepositPercent;

	    @Column(name = "LONG_TERM_FUNDING_AED")
	    private BigDecimal longTermFundingAed;

	    @Column(name = "LONG_TERM_FUNDING_PERCENT")
	    private BigDecimal longTermFundingPercent;

	    @Column(name = "OTHER_LIABILITIES_AED")
	    private BigDecimal otherLiabilitiesAed;

	    @Column(name = "OTHER_LIABILITIES_PERCENT")
	    private BigDecimal otherLiabilitiesPercent;

	    @Column(name = "NEGATIVE_FV_DERIVATIVES_AED")
	    private BigDecimal negativeFvDerivativesAed;

	    @Column(name = "NEGATIVE_FV_DERIVATIVES_PERCENT")
	    private BigDecimal negativeFvDerivativesPercent;

	    @Column(name = "CAPITAL_RESERVES_AED")
	    private BigDecimal capitalReservesAed;

	    @Column(name = "CAPITAL_RESERVES_PERCENT")
	    private BigDecimal capitalReservesPercent;

	    @Column(name = "SHORT_TERM_REG_RATIO_REQ")
	    private BigDecimal shortTermRegRatioReq;
	    
	    
	    @Column(name = "HQ_ASSETS_CONVERTED_AED")
	    private BigDecimal hqAssetsConvertedAed;

	    @Column(name = "OUTFLOWS_30D_AED")
	    private BigDecimal outflows30dAed;

	    @Column(name = "INFLOWS_30D_AED")
	    private BigDecimal inflows30dAed;

	    @Column(name = "LCR")
	    private BigDecimal lcr;

	    @Column(name = "AED_HQ_ASSETS")
	    private BigDecimal aedHqAssets;

	    @Column(name = "AED_OUTFLOWS_30D")
	    private BigDecimal aedOutflows30d;

	    @Column(name = "AED_INFLOWS_30D")
	    private BigDecimal aedInflows30d;

	    @Column(name = "AED_LCR")
	    private BigDecimal aedLcr;

	    @Column(name = "USD_HQ_ASSETS")
	    private BigDecimal usdHqAssets;

	    @Column(name = "USD_OUTFLOWS_30D")
	    private BigDecimal usdOutflows30d;

	    @Column(name = "USD_INFLOWS_30D")
	    private BigDecimal usdInflows30d;

	    @Column(name = "USD_LCR")
	    private BigDecimal usdLcr;

	    @Column(name = "ELAR_HQ_ASSETS_CONVERTED_AED")
	    private BigDecimal elarHqAssetsConvertedAed;

	    @Column(name = "TOTAL_LIABILITIES_CONVERTED_AED")
	    private BigDecimal totalLiabilitiesConvertedAed;

	    @Column(name = "ELIGIBLE_ASSET_RATIO")
	    private BigDecimal eligibleAssetRatio;

	    @Column(name = "AED_ELAR_HQ_ASSETS")
	    private BigDecimal aedElarHqAssets;

	    @Column(name = "AED_TOTAL_LIABILITIES")
	    private BigDecimal aedTotalLiabilities;

	    @Column(name = "AED_ELIGIBLR_ASSET_RATIO")
	    private BigDecimal aedEligiblrAssetRatio;

	    @Column(name = "USD_ELAR_HQ_ASSETS")
	    private BigDecimal usdElarHqAssets;

	    @Column(name = "USD_TOTAL_LIABILITIES")
	    private BigDecimal usdTotalLiabilities;

	    @Column(name = "USD_EIGIBLE_ASSET_RATIO")
	    private BigDecimal usdEigibleAssetRatio;

	    @Column(name = "ASF_CONVERTED_AED")
	    private BigDecimal asfConvertedAed;

	    @Column(name = "RSF_CONVERTED_AED")
	    private BigDecimal rsfConvertedAed;

	    @Column(name = "NSFR")
	    private BigDecimal nsfr;

	    @Column(name = "AED_ASF")
	    private BigDecimal aedAsf;

	    @Column(name = "AED_RSF")
	    private BigDecimal aedRsf;

	    @Column(name = "AED_NSFR")
	    private BigDecimal aedNsfr;

	    @Column(name = "USD_ASF")
	    private BigDecimal usdAsf;

	    @Column(name = "USD_RSF")
	    private BigDecimal usdRsf;

	    @Column(name = "USD_NSFR")
	    private BigDecimal usdNsfr;

	    @Column(name = "LOANS_ADVANCES_CONVERTED_AED")
	    private BigDecimal loansAdvancesConvertedAed;

	    @Column(name = "STABLE_RESOURCES_CONVERTED_AED")
	    private BigDecimal stableResourcesConvertedAed;

	    @Column(name = "LOANS_TO_STABLE_RESOURCES_RATIO")
	    private BigDecimal loansToStableResourcesRatio;

	    @Column(name = "AED_LOANS_ADVANCES")
	    private BigDecimal aedLoansAdvances;

	    @Column(name = "AED_STABLE_RESOURCES")
	    private BigDecimal aedStableResources;

	    @Column(name = "AED_LTS_RATIO")
	    private BigDecimal aedLtsRatio;

	    @Column(name = "USD_LOANS_ADVANCES")
	    private BigDecimal usdLoansAdvances;

	    @Column(name = "USD_STABLE_RESOURCES")
	    private BigDecimal usdStableResources;

	    @Column(name = "USD_LTS_RATIO")
	    private BigDecimal usdLtsRatio;

	    @Column(name = "STABLE_TERM_DEPOSIT_AED")
	    private BigDecimal stableTermDepositAed;

	    @Column(name = "VOLATILE_TERM_DEPOSIT_AED")
	    private BigDecimal volatileTermDepositAed;

	    @Column(name = "STABLE_CASA_AED")
	    private BigDecimal stableCasaAed;

	    @Column(name = "VOLATILE_CASA_AED")
	    private BigDecimal volatileCasaAed;

	    @Column(name = "TERM_DEPOSIT_AED")
	    private BigDecimal termDepositAed;

	    @Column(name = "CASA_AED")
	    private BigDecimal casaAed;

	    @Column(name = "LOANS_TO_DEPOSITS_RATIO")
	    private BigDecimal loansToDepositsRatio;

	    @Column(name = "STABLE_TERM_DEPOSIT_TO_ASSETS")
	    private BigDecimal stableTermDepositToAssets;

	    @Column(name = "VOLATILE_TERM_DEPOSIT_TO_ASSETS")
	    private BigDecimal volatileTermDepositToAssets;

	    @Column(name = "STABLE_CASA_TO_ASSETS")
	    private BigDecimal stableCasaToAssets;

	    @Column(name = "VOLATILE_CASA_TO_ASSETS")
	    private BigDecimal volatileCasaToAssets;

	    @Column(name = "LIQUID_ASSETS_TO_TOTAL_DEPOSITS")
	    private BigDecimal liquidAssetsToTotalDeposits;

	    @Column(name = "LIQUID_ASSETS_TO_TOTAL_CASA")
	    private BigDecimal liquidAssetsToTotalCasa;

	    @Column(name = "SIX_MONTH_CASH_FLOW_GAP_AED")
	    private BigDecimal sixMonthCashFlowGapAed;

	    @Column(name = "TOTAL_LIABILITIES_EXCL_CAPITAL")
	    private BigDecimal totalLiabilitiesExclCapital;

	    @Column(name = "SIX_MONTH_GAP_RATIO")
	    private BigDecimal sixMonthGapRatio;

	    @Column(name = "AED_SIX_MONTH_CASH_FLOW_GAP")
	    private BigDecimal aedSixMonthCashFlowGap;

	    @Column(name = "AED_LIABILITIES_EXCL_CAPITAL")
	    private BigDecimal aedLiabilitiesExclCapital;

	    @Column(name = "AED_SIX_MONTH_GAP_RATIO")
	    private BigDecimal aedSixMonthGapRatio;

	    @Column(name = "USD_SIX_MONTH_CASH_FLOW_GAP")
	    private BigDecimal usdSixMonthCashFlowGap;

	    @Column(name = "USD_LIABILITIES_EXCL_CAPITAL")
	    private BigDecimal usdLiabilitiesExclCapital;

	    @Column(name = "USD_SIX_MONTH_GAP_RATIO")
	    private BigDecimal usdSixMonthGapRatio;

	    @Column(name = "THREE_MONTH_CASH_FLOW_GAP_AED")
	    private BigDecimal threeMonthCashFlowGapAed;

	    @Column(name = "THREE_MONTH_GAP_RATIO")
	    private BigDecimal threeMonthGapRatio;

	    @Column(name = "ONE_MONTH_CASH_FLOW_GAP_AED")
	    private BigDecimal oneMonthCashFlowGapAed;

	    @Column(name = "ONE_MONTH_GAP_RATIO")
	    private BigDecimal oneMonthGapRatio;

	    @Column(name = "SEVEN_DAY_CASH_FLOW_GAP_AED")
	    private BigDecimal sevenDayCashFlowGapAed;

	    @Column(name = "SEVEN_DAY_GAP_RATIO")
	    private BigDecimal sevenDayGapRatio;

	    @Column(name = "UNENCUMBERED_LIQUID_ASSETS_AED")
	    private BigDecimal unencumberedLiquidAssetsAed;

	    @Column(name = "ONE_MONTH_CASH_FLOW_GAP_COPY")
	    private BigDecimal oneMonthCashFlowGapCopy;

	    @Column(name = "UNENCUMBERED_TO_1M_GAP_RATIO")
	    private BigDecimal unencumberedTo1mGapRatio;

	    @Column(name = "CBUAE_CASH_BALANCES")
	    private BigDecimal cbuaeCashBalances;

	    @Column(name = "CBUAE_CASH_BALANCES_PERCENT")
	    private BigDecimal cbuaeCashBalancesPercent;

	    @Column(name = "MONEY_MKT_PLACEMENTS_LT_6M")
	    private BigDecimal moneyMktPlacementsLt6m;

	    @Column(name = "MONEY_MKT_PLACEMENTS_LT_6M_PERCENT")
	    private BigDecimal moneyMktPlacementsLt6mPercent;

	    @Column(name = "LOCAL_CURRENCY_GOVT_BONDS")
	    private BigDecimal localCurrencyGovtBonds;

	    @Column(name = "LOCAL_CURRENCY_GOVT_BONDS_PERCENT")
	    private BigDecimal localCurrencyGovtBondsPercent;

	    @Column(name = "HARD_CURRENCY_GOVT_BONDS")
	    private BigDecimal hardCurrencyGovtBonds;

	    @Column(name = "HARD_CURRENCY_GOVT_BONDS_PERCENT")
	    private BigDecimal hardCurrencyGovtBondsPercent;

	    @Column(name = "FOREIGN_GOVT_BONDS")
	    private BigDecimal foreignGovtBonds;

	    @Column(name = "FOREIGN_GOVT_BONDS_PERCENT")
	    private BigDecimal foreignGovtBondsPercent;

	    @Column(name = "PLEDGE_SECURITIES")
	    private BigDecimal pledgeSecurities;

	    @Column(name = "TOTAL_PLEDGEABLE_SECURITIES")
	    private BigDecimal totalPledgeableSecurities;

	    @Column(name = "PLEDGE_RATIO")
	    private BigDecimal pledgeRatio;

	    @Column(name = "TOTAL_COMPLEX_FIN_INSTRUMENTS")
	    private BigDecimal totalComplexFinInstruments;

	    @Column(name = "TOTAL_TRADABLE_ASSETS")
	    private BigDecimal totalTradableAssets;

	    @Column(name = "COMPLEX_TO_TRADABLE_RATIO")
	    private BigDecimal complexToTradableRatio;

	    @Column(name = "TOP_10_DEPOSITS")
	    private BigDecimal top10Deposits;

	    @Column(name = "TOTAL_DEPOSITS_AED1")
	    private BigDecimal totalDepositsAed1;

	    @Column(name = "TOP_10_TO_TOTAL_RATIO")
	    private BigDecimal top10ToTotalRatio;

	    @Column(name = "ST_IB_BORROWING_LT_3M")
	    private BigDecimal stIbBorrowingLt3m;

	    @Column(name = "ST_REPO_LT_3M")
	    private BigDecimal stRepoLt3m;

	    @Column(name = "INTERBANK_BORROWING")
	    private BigDecimal interbankBorrowing;

	    @Column(name = "REPO_AGREEMENTS")
	    private BigDecimal repoAgreements;

	    @Column(name = "TOTAL_DEPOSITS_AED2")
	    private BigDecimal totalDepositsAed2;

	    @Column(name = "LONG_TERM_FUNDING_COPY")
	    private BigDecimal longTermFundingCopy;

	    @Column(name = "ST_IB_REPO_TO_TOTAL_FUNDING_RATIO")
	    private BigDecimal stIbRepoToTotalFundingRatio;

	    @Column(name = "LARGEST_SINGLE_DEPOSITOR")
	    private BigDecimal largestSingleDepositor;

	    @Column(name = "INDIVIDUAL_COUNTERPARTY_TO_TOTAL_FUNDING_RATIO")
	    private BigDecimal individualCounterpartyToTotalFundingRatio;

	    @Column(name = "UNUTILIZED_LOANS_CREDIT_LINES")
	    private BigDecimal unutilizedLoansCreditLines;

	    @Column(name = "DERIVATIVES_EXPECTED_NEG_EXPOSURE")
	    private BigDecimal derivativesExpectedNegExposure;

	    @Column(name = "DERIVATIVES_EXPECTED_NEG_EXPOSURE_PROXY")
	    private BigDecimal derivativesExpectedNegExposureProxy;

	    @Column(name = "CONTINGENT_LIABILITIES")
	    private BigDecimal contingentLiabilities;

	    @Column(name = "CONTINGENT_LIABILITIES_TO_TOTAL_FUNDING_RATIO")
	    private BigDecimal contingentLiabilitiesToTotalFundingRatio;

	    @Column(name = "TOP_5_INDUSTRY_DEPOSITS")
	    private BigDecimal top5IndustryDeposits;

	    @Column(name = "TOP_5_TO_TOTAL_DEPOSITS_RATIO")
	    private BigDecimal top5ToTotalDepositsRatio;

	    @Column(name = "LONG_TERM_LIABILITIES_TO_TOTAL_LIABILITIES_RATIO")
	    private BigDecimal longTermLiabilitiesToTotalLiabilitiesRatio;

	    // Date and meta info fields
	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    @Column(name = "REPORT_FROM_DATE")
	    private Date reportFromDate;

	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    @Column(name = "REPORT_TO_DATE")
	    private Date reportToDate;

	    @Column(name = "ENTITY_FLG")
	    private String entityFlg;

	    @Column(name = "MODIFY_FLG")
	    private String modifyFlg;

	    @Column(name = "DEL_FLG")
	    private String delFlg;

	    @Column(name = "REPORT_CODE")
	    private String reportCode;

	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    @Column(name = "REPORT_SUBMIT_DATE")
	    private Date reportSubmitDate;

	    @Column(name = "ENTRY_USER")
	    private String entryUser;

	    @Column(name = "MODIFY_USER")
	    private String modifyUser;

	    @Column(name = "VERIFY_USER")
	    private String verifyUser;

	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    @Column(name = "ENTRY_TIME")
	    private Date entryTime;

	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    @Column(name = "MODIFY_TIME")
	    private Date modifyTime;

	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    @Column(name = "VERIFY_TIME")
	    private Date verifyTime;

}
