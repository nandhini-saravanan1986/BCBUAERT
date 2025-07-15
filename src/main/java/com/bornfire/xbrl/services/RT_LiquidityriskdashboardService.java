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
import com.bornfire.xbrl.entities.RT_Liquidity_Risk_Dashboard_Template;
import com.bornfire.xbrl.entities.RT_Liquidity_Risk_Dashboard_Template_repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileNotFoundException;



@Service
public class RT_LiquidityriskdashboardService {

	@Autowired
	private Environment env;
	
    private static final Logger logger = LoggerFactory.getLogger(RT_LiquidityriskdashboardService.class);


    @Autowired 
    RT_Liquidity_Risk_Dashboard_Template_repository LiquidityRiskDashboardRepo;

	@Autowired
	private SessionFactory sessionFactory;

	public boolean updateliquidityriskdashboard(RT_Liquidity_Risk_Dashboard_Template updatedData) {
	    System.out.println("Looking for record with SI_NO: " + updatedData.getSI_NO());

	    RT_Liquidity_Risk_Dashboard_Template existing = LiquidityRiskDashboardRepo.getParticularDataBySI_NO(updatedData.getSI_NO());

	    if (existing != null) {
	        // Update fields
	    	existing.setReportDate(updatedData.getReportDate());
	    	existing.setBankName(updatedData.getBankName());
	    	existing.setGroupHeadOfficeSubsidiary(updatedData.getGroupHeadOfficeSubsidiary());
	    	existing.setSubsidiary(updatedData.getSubsidiary());
	    	existing.setBankSymbol(updatedData.getBankSymbol());
	    	existing.setConventionalIslamic(updatedData.getConventionalIslamic());
	    	existing.setLocalForeign(updatedData.getLocalForeign());
	    	existing.setCbuAeTiering(updatedData.getCbuAeTiering());
	    	existing.setAssetBalanceSheetSizeAed(updatedData.getAssetBalanceSheetSizeAed());
	    	existing.setCashAed(updatedData.getCashAed());
	    	existing.setCashPercent(updatedData.getCashPercent());
	    	existing.setDueFromCentralBankAed(updatedData.getDueFromCentralBankAed());
	    	existing.setDueFromCentralBankPercent(updatedData.getDueFromCentralBankPercent());
	    	existing.setDueFromHoBranchSubsAed(updatedData.getDueFromHoBranchSubsAed());
	    	existing.setDueFromHoBranchSubsPercent(updatedData.getDueFromHoBranchSubsPercent());
	    	existing.setDueFromBanksAed(updatedData.getDueFromBanksAed());
	    	existing.setDueFromBanksPercent(updatedData.getDueFromBanksPercent());
	    	existing.setDueFromNbfiAed(updatedData.getDueFromNbfiAed());
	    	existing.setDueFromNbfiPercent(updatedData.getDueFromNbfiPercent());
	    	existing.setInvestmentAed(updatedData.getInvestmentAed());
	    	existing.setInvestmentPercent(updatedData.getInvestmentPercent());
	    	existing.setOtherInvestmentsAed(updatedData.getOtherInvestmentsAed());
	    	existing.setOtherInvestmentsPercent(updatedData.getOtherInvestmentsPercent());
	    	existing.setNetCreditPortfolioAed(updatedData.getNetCreditPortfolioAed());
	    	existing.setNetCreditPortfolioPercent(updatedData.getNetCreditPortfolioPercent());
	    	existing.setTradeBillsAed(updatedData.getTradeBillsAed());
	    	existing.setTradeBillsPercent(updatedData.getTradeBillsPercent());
	    	existing.setNetFixedOtherAssetsAed(updatedData.getNetFixedOtherAssetsAed());
	    	existing.setNetFixedOtherAssetsPercent(updatedData.getNetFixedOtherAssetsPercent());
	    	existing.setPositiveFvDerivativesAed(updatedData.getPositiveFvDerivativesAed());
	    	existing.setPositiveFvDerivativesPercent(updatedData.getPositiveFvDerivativesPercent());
	    	existing.setLiabilityBalanceSheetSizeAed(updatedData.getLiabilityBalanceSheetSizeAed());
	    	existing.setDueToCentralBankAed(updatedData.getDueToCentralBankAed());
	    	existing.setDueToCentralBankPercent(updatedData.getDueToCentralBankPercent());
	    	existing.setDueToHoBranchSubsAed(updatedData.getDueToHoBranchSubsAed());
	    	existing.setDueToHoPercent(updatedData.getDueToHoPercent());
	    	existing.setDueToBanksAed(updatedData.getDueToBanksAed());
	    	existing.setDueToBanksPercent(updatedData.getDueToBanksPercent());
	    	existing.setDueToNbfiAed(updatedData.getDueToNbfiAed());
	    	existing.setDueToNbfiPercent(updatedData.getDueToNbfiPercent());
	    	existing.setCustomerDepositAed(updatedData.getCustomerDepositAed());
	    	existing.setCustomerDepositPercent(updatedData.getCustomerDepositPercent());
	    	existing.setLongTermFundingAed(updatedData.getLongTermFundingAed());
	    	existing.setLongTermFundingPercent(updatedData.getLongTermFundingPercent());
	    	existing.setOtherLiabilitiesAed(updatedData.getOtherLiabilitiesAed());
	    	existing.setOtherLiabilitiesPercent(updatedData.getOtherLiabilitiesPercent());
	    	existing.setNegativeFvDerivativesAed(updatedData.getNegativeFvDerivativesAed());
	    	existing.setNegativeFvDerivativesPercent(updatedData.getNegativeFvDerivativesPercent());
	    	existing.setCapitalReservesAed(updatedData.getCapitalReservesAed());
	    	existing.setCapitalReservesPercent(updatedData.getCapitalReservesPercent());
	    	existing.setShortTermRegRatioReq(updatedData.getShortTermRegRatioReq());
	    	existing.setHqAssetsConvertedAed(updatedData.getHqAssetsConvertedAed());
	    	existing.setOutflows30dAed(updatedData.getOutflows30dAed());
	    	existing.setInflows30dAed(updatedData.getInflows30dAed());
	    	existing.setLcr(updatedData.getLcr());
	    	existing.setAedHqAssets(updatedData.getAedHqAssets());
	    	existing.setAedOutflows30d(updatedData.getAedOutflows30d());
	    	existing.setAedInflows30d(updatedData.getAedInflows30d());
	    	existing.setAedLcr(updatedData.getAedLcr());
	    	existing.setUsdHqAssets(updatedData.getUsdHqAssets());
	    	existing.setUsdOutflows30d(updatedData.getUsdOutflows30d());
	    	existing.setUsdInflows30d(updatedData.getUsdInflows30d());
	    	existing.setUsdLcr(updatedData.getUsdLcr());
	    	existing.setElarHqAssetsConvertedAed(updatedData.getElarHqAssetsConvertedAed());
	    	existing.setTotalLiabilitiesConvertedAed(updatedData.getTotalLiabilitiesConvertedAed());
	    	existing.setEligibleAssetRatio(updatedData.getEligibleAssetRatio());
	    	existing.setAedElarHqAssets(updatedData.getAedElarHqAssets());
	    	existing.setAedTotalLiabilities(updatedData.getAedTotalLiabilities());
	    	existing.setAedEligiblrAssetRatio(updatedData.getAedEligiblrAssetRatio());
	    	existing.setUsdElarHqAssets(updatedData.getUsdElarHqAssets());
	    	existing.setUsdTotalLiabilities(updatedData.getUsdTotalLiabilities());
	    	existing.setUsdEigibleAssetRatio(updatedData.getUsdEigibleAssetRatio());
	    	existing.setAsfConvertedAed(updatedData.getAsfConvertedAed());
	    	existing.setRsfConvertedAed(updatedData.getRsfConvertedAed());
	    	existing.setNsfr(updatedData.getNsfr());
	    	existing.setAedAsf(updatedData.getAedAsf());
	    	existing.setAedRsf(updatedData.getAedRsf());
	    	existing.setAedNsfr(updatedData.getAedNsfr());
	    	existing.setUsdAsf(updatedData.getUsdAsf());
	    	existing.setUsdRsf(updatedData.getUsdRsf());
	    	existing.setUsdNsfr(updatedData.getUsdNsfr());
	    	existing.setLoansAdvancesConvertedAed(updatedData.getLoansAdvancesConvertedAed());
	    	existing.setStableResourcesConvertedAed(updatedData.getStableResourcesConvertedAed());
	    	existing.setLoansToStableResourcesRatio(updatedData.getLoansToStableResourcesRatio());
	    	existing.setAedLoansAdvances(updatedData.getAedLoansAdvances());
	    	existing.setAedStableResources(updatedData.getAedStableResources());
	    	existing.setAedLtsRatio(updatedData.getAedLtsRatio());
	    	existing.setUsdLoansAdvances(updatedData.getUsdLoansAdvances());
	    	existing.setUsdStableResources(updatedData.getUsdStableResources());
	    	existing.setUsdLtsRatio(updatedData.getUsdLtsRatio());
	    	existing.setStableTermDepositAed(updatedData.getStableTermDepositAed());
	    	existing.setVolatileTermDepositAed(updatedData.getVolatileTermDepositAed());
	    	existing.setStableCasaAed(updatedData.getStableCasaAed());
	    	existing.setVolatileCasaAed(updatedData.getVolatileCasaAed());
	    	existing.setTermDepositAed(updatedData.getTermDepositAed());
	    	existing.setCasaAed(updatedData.getCasaAed());
	    	existing.setLoansToDepositsRatio(updatedData.getLoansToDepositsRatio());
	    	existing.setStableTermDepositToAssets(updatedData.getStableTermDepositToAssets());
	    	existing.setVolatileTermDepositToAssets(updatedData.getVolatileTermDepositToAssets());
	    	existing.setStableCasaToAssets(updatedData.getStableCasaToAssets());
	    	existing.setVolatileCasaToAssets(updatedData.getVolatileCasaToAssets());
	    	existing.setLiquidAssetsToTotalDeposits(updatedData.getLiquidAssetsToTotalDeposits());
	    	existing.setLiquidAssetsToTotalCasa(updatedData.getLiquidAssetsToTotalCasa());
	    	existing.setSixMonthCashFlowGapAed(updatedData.getSixMonthCashFlowGapAed());
	    	existing.setTotalLiabilitiesExclCapital(updatedData.getTotalLiabilitiesExclCapital());
	    	existing.setSixMonthGapRatio(updatedData.getSixMonthGapRatio());
	    	existing.setAedSixMonthCashFlowGap(updatedData.getAedSixMonthCashFlowGap());
	    	existing.setAedLiabilitiesExclCapital(updatedData.getAedLiabilitiesExclCapital());
	    	existing.setAedSixMonthGapRatio(updatedData.getAedSixMonthGapRatio());
	    	existing.setUsdSixMonthCashFlowGap(updatedData.getUsdSixMonthCashFlowGap());
	    	existing.setUsdLiabilitiesExclCapital(updatedData.getUsdLiabilitiesExclCapital());
	    	existing.setUsdSixMonthGapRatio(updatedData.getUsdSixMonthGapRatio());
	    	existing.setThreeMonthCashFlowGapAed(updatedData.getThreeMonthCashFlowGapAed());
	    	existing.setThreeMonthGapRatio(updatedData.getThreeMonthGapRatio());
	    	existing.setOneMonthCashFlowGapAed(updatedData.getOneMonthCashFlowGapAed());
	    	existing.setOneMonthGapRatio(updatedData.getOneMonthGapRatio());
	    	existing.setSevenDayCashFlowGapAed(updatedData.getSevenDayCashFlowGapAed());
	    	existing.setSevenDayGapRatio(updatedData.getSevenDayGapRatio());
	    	existing.setUnencumberedLiquidAssetsAed(updatedData.getUnencumberedLiquidAssetsAed());
	    	existing.setOneMonthCashFlowGapCopy(updatedData.getOneMonthCashFlowGapCopy());
	    	existing.setUnencumberedTo1mGapRatio(updatedData.getUnencumberedTo1mGapRatio());
	    	existing.setCbuaeCashBalances(updatedData.getCbuaeCashBalances());
	    	existing.setCbuaeCashBalancesPercent(updatedData.getCbuaeCashBalancesPercent());
	    	existing.setMoneyMktPlacementsLt6m(updatedData.getMoneyMktPlacementsLt6m());
	    	existing.setMoneyMktPlacementsLt6mPercent(updatedData.getMoneyMktPlacementsLt6mPercent());
	    	existing.setLocalCurrencyGovtBonds(updatedData.getLocalCurrencyGovtBonds());
	    	existing.setLocalCurrencyGovtBondsPercent(updatedData.getLocalCurrencyGovtBondsPercent());
	    	existing.setHardCurrencyGovtBonds(updatedData.getHardCurrencyGovtBonds());
	    	existing.setHardCurrencyGovtBondsPercent(updatedData.getHardCurrencyGovtBondsPercent());
	    	existing.setForeignGovtBonds(updatedData.getForeignGovtBonds());
	    	existing.setForeignGovtBondsPercent(updatedData.getForeignGovtBondsPercent());
	    	existing.setPledgeSecurities(updatedData.getPledgeSecurities());
	    	existing.setTotalPledgeableSecurities(updatedData.getTotalPledgeableSecurities());
	    	existing.setPledgeRatio(updatedData.getPledgeRatio());
	    	existing.setTotalComplexFinInstruments(updatedData.getTotalComplexFinInstruments());
	    	existing.setTotalTradableAssets(updatedData.getTotalTradableAssets());
	    	existing.setComplexToTradableRatio(updatedData.getComplexToTradableRatio());
	    	existing.setTop10Deposits(updatedData.getTop10Deposits());
	    	existing.setTotalDepositsAed1(updatedData.getTotalDepositsAed1());
	    	existing.setTop10ToTotalRatio(updatedData.getTop10ToTotalRatio());
	    	existing.setStIbBorrowingLt3m(updatedData.getStIbBorrowingLt3m());
	    	existing.setStRepoLt3m(updatedData.getStRepoLt3m());
	    	existing.setInterbankBorrowing(updatedData.getInterbankBorrowing());
	    	existing.setRepoAgreements(updatedData.getRepoAgreements());
	    	existing.setTotalDepositsAed2(updatedData.getTotalDepositsAed2());
	    	existing.setLongTermFundingCopy(updatedData.getLongTermFundingCopy());
	    	existing.setStIbRepoToTotalFundingRatio(updatedData.getStIbRepoToTotalFundingRatio());
	    	existing.setLargestSingleDepositor(updatedData.getLargestSingleDepositor());
	    	existing.setIndividualCounterpartyToTotalFundingRatio(updatedData.getIndividualCounterpartyToTotalFundingRatio());
	    	existing.setUnutilizedLoansCreditLines(updatedData.getUnutilizedLoansCreditLines());
	    	existing.setDerivativesExpectedNegExposure(updatedData.getDerivativesExpectedNegExposure());
	    	existing.setDerivativesExpectedNegExposureProxy(updatedData.getDerivativesExpectedNegExposureProxy());
	    	existing.setContingentLiabilities(updatedData.getContingentLiabilities());
	    	existing.setContingentLiabilitiesToTotalFundingRatio(updatedData.getContingentLiabilitiesToTotalFundingRatio());
	    	existing.setTop5IndustryDeposits(updatedData.getTop5IndustryDeposits());
	    	existing.setTop5ToTotalDepositsRatio(updatedData.getTop5ToTotalDepositsRatio());
	    	existing.setLongTermLiabilitiesToTotalLiabilitiesRatio(updatedData.getLongTermLiabilitiesToTotalLiabilitiesRatio());



	        LiquidityRiskDashboardRepo.save(existing);
	        return true;
	    } else {
	        System.out.println("No record found for SI_NO: " + updatedData.getSI_NO());
	        return false;
	    }
	}



	public byte[] generateLiquidityriskdashboardExcel() throws Exception {
        logger.info("Service: Starting MM Excel generation process in memory.");

        List<Object[]> liquidityriskdashboard = LiquidityRiskDashboardRepo.getliquidityriskdashboarddata1();

        if (liquidityriskdashboard.isEmpty()) {
            logger.warn("Service: No data found for LRD report. Returning empty result.");
            return new byte[0];
        }

        String templateDir = env.getProperty("output.exportpathtemp");  // Corrected property key
        String templateFileName = "CBUAE_Liquidity_Risk_Dashboard_Template.xls";
        Path templatePath = Paths.get(templateDir, templateFileName);

        logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

        if (!Files.exists(templatePath)) {
            throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
        }

        if (!Files.isReadable(templatePath)) {
            throw new SecurityException("Template file exists but is not readable: " + templatePath.toAbsolutePath());
        }

        try (InputStream templateInputStream = Files.newInputStream(templatePath);
             Workbook workbook = WorkbookFactory.create(templateInputStream);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheetAt(0);
            CreationHelper createHelper = workbook.getCreationHelper();

            // Define cell styles
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
            dateStyle.setBorderBottom(BorderStyle.THIN);
            dateStyle.setBorderTop(BorderStyle.THIN);
            dateStyle.setBorderLeft(BorderStyle.THIN);
            dateStyle.setBorderRight(BorderStyle.THIN);

            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setBorderBottom(BorderStyle.THIN);
            textStyle.setBorderTop(BorderStyle.THIN);
            textStyle.setBorderLeft(BorderStyle.THIN);
            textStyle.setBorderRight(BorderStyle.THIN);

            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
            numberStyle.setBorderBottom(BorderStyle.THIN);
            numberStyle.setBorderTop(BorderStyle.THIN);
            numberStyle.setBorderLeft(BorderStyle.THIN);
            numberStyle.setBorderRight(BorderStyle.THIN);

            int startRow = 2; // Assuming data starts from row index 3 (4rd row)

            if (!liquidityriskdashboard.isEmpty()) {
                for (int i = 0; i < liquidityriskdashboard.size(); i++) {
                    Object[] lrd = liquidityriskdashboard.get(i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) row = sheet.createRow(startRow + i);
                 // 0 - REPORT_DATE (Date)
                    Cell cell0 = row.getCell(0);
                    if (cell0 == null) cell0 = row.createCell(0);
                    if (lrd[0] instanceof Date) {
                        cell0.setCellValue((Date) lrd[0]);
                        cell0.setCellStyle(dateStyle);
                    } else {
                        cell0.setCellValue("");
                    }

                    // 1 - BANK_NAME (String)
                    Cell cell1 = row.getCell(1);
                    if (cell1 == null) cell1 = row.createCell(1);
                    cell1.setCellValue(lrd[1] == null ? "" : lrd[1].toString());

                    // 2 - GROUP_HEAD_OFFICE_SUBSIDIARY (String)
                    Cell cell2 = row.getCell(2);
                    if (cell2 == null) cell2 = row.createCell(2);
                    cell2.setCellValue(lrd[2] == null ? "" : lrd[2].toString());

                    // 3 - SUBSIDIARY (String)
                    Cell cell3 = row.getCell(3);
                    if (cell3 == null) cell3 = row.createCell(3);
                    cell3.setCellValue(lrd[3] == null ? "" : lrd[3].toString());

                    // 4 - BANK_SYMBOL (String)
                    Cell cell4 = row.getCell(4);
                    if (cell4 == null) cell4 = row.createCell(4);
                    cell4.setCellValue(lrd[4] == null ? "" : lrd[4].toString());

                    // 5 - CONVENTIONAL_ISLAMIC (String)
                    Cell cell5 = row.getCell(5);
                    if (cell5 == null) cell5 = row.createCell(5);
                    cell5.setCellValue(lrd[5] == null ? "" : lrd[5].toString());

                    // 6 - LOCAL_FOREIGN (String)
                    Cell cell6 = row.getCell(6);
                    if (cell6 == null) cell6 = row.createCell(6);
                    cell6.setCellValue(lrd[6] == null ? "" : lrd[6].toString());

                    // 7 - CBUAE_TIERING (String)
                    Cell cell7 = row.getCell(7);
                    if (cell7 == null) cell7 = row.createCell(7);
                    cell7.setCellValue(lrd[7] == null ? "" : lrd[7].toString());

                    // 8 - ASSET_BALANCE_SHEET_SIZE_AED (BigDecimal)
                    Cell cell8 = row.getCell(8);
                    if (cell8 == null) cell8 = row.createCell(8);
                    cell8.setCellStyle(numberStyle);
                    cell8.setCellValue(lrd[8] instanceof BigDecimal ? ((BigDecimal) lrd[8]).doubleValue() : 0);

                    // 9 - CASH_AED (BigDecimal)
                    Cell cell9 = row.getCell(9);
                    if (cell9 == null) cell9 = row.createCell(9);
                    cell9.setCellStyle(numberStyle);
                    cell9.setCellValue(lrd[9] instanceof BigDecimal ? ((BigDecimal) lrd[9]).doubleValue() : 0);

                    // 10 - CASH_PERCENT (BigDecimal)
                    Cell cell10 = row.getCell(10);
                    if (cell10 == null) cell10 = row.createCell(10);
                    cell10.setCellStyle(numberStyle);
                    cell10.setCellValue(lrd[10] instanceof BigDecimal ? ((BigDecimal) lrd[10]).doubleValue() : 0);

                    // 11 - DUE_FROM_CENTRAL_BANK_AED (BigDecimal)
                    Cell cell11 = row.getCell(11);
                    if (cell11 == null) cell11 = row.createCell(11);
                    cell11.setCellStyle(numberStyle);
                    cell11.setCellValue(lrd[11] instanceof BigDecimal ? ((BigDecimal) lrd[11]).doubleValue() : 0);

                    // 12 - DUE_FROM_CENTRAL_BANK_PERCENT (BigDecimal)
                    Cell cell12 = row.getCell(12);
                    if (cell12 == null) cell12 = row.createCell(12);
                    cell12.setCellStyle(numberStyle);
                    cell12.setCellValue(lrd[12] instanceof BigDecimal ? ((BigDecimal) lrd[12]).doubleValue() : 0);

                    // 13 - DUE_FROM_HO_BRANCH_SUBS_AED (BigDecimal)
                    Cell cell13 = row.getCell(13);
                    if (cell13 == null) cell13 = row.createCell(13);
                    cell13.setCellStyle(numberStyle);
                    cell13.setCellValue(lrd[13] instanceof BigDecimal ? ((BigDecimal) lrd[13]).doubleValue() : 0);

                    // 14 - DUE_FROM_HO_BRANCH_SUBS_PERCENT (BigDecimal)
                    Cell cell14 = row.getCell(14);
                    if (cell14 == null) cell14 = row.createCell(14);
                    cell14.setCellStyle(numberStyle);
                    cell14.setCellValue(lrd[14] instanceof BigDecimal ? ((BigDecimal) lrd[14]).doubleValue() : 0);

                    // 15 - DUE_FROM_BANKS_AED (BigDecimal)
                    Cell cell15 = row.getCell(15);
                    if (cell15 == null) cell15 = row.createCell(15);
                    cell15.setCellStyle(numberStyle);
                    cell15.setCellValue(lrd[15] instanceof BigDecimal ? ((BigDecimal) lrd[15]).doubleValue() : 0);

                    // 16 - DUE_FROM_BANKS_PERCENT (BigDecimal)
                    Cell cell16 = row.getCell(16);
                    if (cell16 == null) cell16 = row.createCell(16);
                    cell16.setCellStyle(numberStyle);
                    cell16.setCellValue(lrd[16] instanceof BigDecimal ? ((BigDecimal) lrd[16]).doubleValue() : 0);

                    // 17 - DUE_FROM_NBFI_AED (BigDecimal)
                    Cell cell17 = row.getCell(17);
                    if (cell17 == null) cell17 = row.createCell(17);
                    cell17.setCellStyle(numberStyle);
                    cell17.setCellValue(lrd[17] instanceof BigDecimal ? ((BigDecimal) lrd[17]).doubleValue() : 0);

                    // 18 - DUE_FROM_NBFI_PERCENT (BigDecimal)
                    Cell cell18 = row.getCell(18);
                    if (cell18 == null) cell18 = row.createCell(18);
                    cell18.setCellStyle(numberStyle);
                    cell18.setCellValue(lrd[18] instanceof BigDecimal ? ((BigDecimal) lrd[18]).doubleValue() : 0);

                    // 19 - INVESTMENT_AED (BigDecimal)
                    Cell cell19 = row.getCell(19);
                    if (cell19 == null) cell19 = row.createCell(19);
                    cell19.setCellStyle(numberStyle);
                    cell19.setCellValue(lrd[19] instanceof BigDecimal ? ((BigDecimal) lrd[19]).doubleValue() : 0);

                    // 20 - INVESTMENT_PERCENT (BigDecimal)
                    Cell cell20 = row.getCell(20);
                    if (cell20 == null) cell20 = row.createCell(20);
                    cell20.setCellStyle(numberStyle);
                    cell20.setCellValue(lrd[20] instanceof BigDecimal ? ((BigDecimal) lrd[20]).doubleValue() : 0);

                    // 21 - OTHER_INVESTMENTS_AED (BigDecimal)
                    Cell cell21 = row.getCell(21);
                    if (cell21 == null) cell21 = row.createCell(21);
                    cell21.setCellStyle(numberStyle);
                    cell21.setCellValue(lrd[21] instanceof BigDecimal ? ((BigDecimal) lrd[21]).doubleValue() : 0);
                    
                 // 22 - OTHER_INVESTMENTS_PERCENT
                    Cell cell22 = row.getCell(22);
                    if (cell22 == null) cell22 = row.createCell(22);
                    cell22.setCellStyle(numberStyle);
                    cell22.setCellValue(lrd[22] instanceof BigDecimal ? ((BigDecimal) lrd[22]).doubleValue() : 0);

                    // 23 - NET_CREDIT_PORTFOLIO_AED
                    Cell cell23 = row.getCell(23);
                    if (cell23 == null) cell23 = row.createCell(23);
                    cell23.setCellStyle(numberStyle);
                    cell23.setCellValue(lrd[23] instanceof BigDecimal ? ((BigDecimal) lrd[23]).doubleValue() : 0);

                    // 24 - NET_CREDIT_PORTFOLIO_PERCENT
                    Cell cell24 = row.getCell(24);
                    if (cell24 == null) cell24 = row.createCell(24);
                    cell24.setCellStyle(numberStyle);
                    cell24.setCellValue(lrd[24] instanceof BigDecimal ? ((BigDecimal) lrd[24]).doubleValue() : 0);

                    // 25 - TRADE_BILLS_AED
                    Cell cell25 = row.getCell(25);
                    if (cell25 == null) cell25 = row.createCell(25);
                    cell25.setCellStyle(numberStyle);
                    cell25.setCellValue(lrd[25] instanceof BigDecimal ? ((BigDecimal) lrd[25]).doubleValue() : 0);

                    // 26 - TRADE_BILLS_PERCENT
                    Cell cell26 = row.getCell(26);
                    if (cell26 == null) cell26 = row.createCell(26);
                    cell26.setCellStyle(numberStyle);
                    cell26.setCellValue(lrd[26] instanceof BigDecimal ? ((BigDecimal) lrd[26]).doubleValue() : 0);

                    // 27 - NET_FIXED_OTHER_ASSETS_AED
                    Cell cell27 = row.getCell(27);
                    if (cell27 == null) cell27 = row.createCell(27);
                    cell27.setCellStyle(numberStyle);
                    cell27.setCellValue(lrd[27] instanceof BigDecimal ? ((BigDecimal) lrd[27]).doubleValue() : 0);

                    // 28 - NET_FIXED_OTHER_ASSETS_PERCENT
                    Cell cell28 = row.getCell(28);
                    if (cell28 == null) cell28 = row.createCell(28);
                    cell28.setCellStyle(numberStyle);
                    cell28.setCellValue(lrd[28] instanceof BigDecimal ? ((BigDecimal) lrd[28]).doubleValue() : 0);

                    // 29 - POSITIVE_FV_DERIVATIVES_AED
                    Cell cell29 = row.getCell(29);
                    if (cell29 == null) cell29 = row.createCell(29);
                    cell29.setCellStyle(numberStyle);
                    cell29.setCellValue(lrd[29] instanceof BigDecimal ? ((BigDecimal) lrd[29]).doubleValue() : 0);

                    // 30 - POSITIVE_FV_DERIVATIVES_PERCENT
                    Cell cell30 = row.getCell(30);
                    if (cell30 == null) cell30 = row.createCell(30);
                    cell30.setCellStyle(numberStyle);
                    cell30.setCellValue(lrd[30] instanceof BigDecimal ? ((BigDecimal) lrd[30]).doubleValue() : 0);

                    // 31 - LIABILITY_BALANCE_SHEET_SIZE_AED
                    Cell cell31 = row.getCell(31);
                    if (cell31 == null) cell31 = row.createCell(31);
                    cell31.setCellStyle(numberStyle);
                    cell31.setCellValue(lrd[31] instanceof BigDecimal ? ((BigDecimal) lrd[31]).doubleValue() : 0);

                    // 32 - DUE_TO_CENTRAL_BANK_AED
                    Cell cell32 = row.getCell(32);
                    if (cell32 == null) cell32 = row.createCell(32);
                    cell32.setCellStyle(numberStyle);
                    cell32.setCellValue(lrd[32] instanceof BigDecimal ? ((BigDecimal) lrd[32]).doubleValue() : 0);

                    // 33 - DUE_TO_CENTRAL_BANK_PERCENT
                    Cell cell33 = row.getCell(33);
                    if (cell33 == null) cell33 = row.createCell(33);
                    cell33.setCellStyle(numberStyle);
                    cell33.setCellValue(lrd[33] instanceof BigDecimal ? ((BigDecimal) lrd[33]).doubleValue() : 0);

                    // 34 - DUE_TO_HO_BRANCH_SUBS_AED
                    Cell cell34 = row.getCell(34);
                    if (cell34 == null) cell34 = row.createCell(34);
                    cell34.setCellStyle(numberStyle);
                    cell34.setCellValue(lrd[34] instanceof BigDecimal ? ((BigDecimal) lrd[34]).doubleValue() : 0);

                    // 35 - DUE_TO_HO_PERCENT
                    Cell cell35 = row.getCell(35);
                    if (cell35 == null) cell35 = row.createCell(35);
                    cell35.setCellStyle(numberStyle);
                    cell35.setCellValue(lrd[35] instanceof BigDecimal ? ((BigDecimal) lrd[35]).doubleValue() : 0);

                    // 36 - DUE_TO_BANKS_AED
                    Cell cell36 = row.getCell(36);
                    if (cell36 == null) cell36 = row.createCell(36);
                    cell36.setCellStyle(numberStyle);
                    cell36.setCellValue(lrd[36] instanceof BigDecimal ? ((BigDecimal) lrd[36]).doubleValue() : 0);

                    // 37 - DUE_TO_BANKS_PERCENT
                    Cell cell37 = row.getCell(37);
                    if (cell37 == null) cell37 = row.createCell(37);
                    cell37.setCellStyle(numberStyle);
                    cell37.setCellValue(lrd[37] instanceof BigDecimal ? ((BigDecimal) lrd[37]).doubleValue() : 0);

                    // 38 - DUE_TO_NBFI_AED
                    Cell cell38 = row.getCell(38);
                    if (cell38 == null) cell38 = row.createCell(38);
                    cell38.setCellStyle(numberStyle);
                    cell38.setCellValue(lrd[38] instanceof BigDecimal ? ((BigDecimal) lrd[38]).doubleValue() : 0);

                    // 39 - DUE_TO_NBFI_PERCENT
                    Cell cell39 = row.getCell(39);
                    if (cell39 == null) cell39 = row.createCell(39);
                    cell39.setCellStyle(numberStyle);
                    cell39.setCellValue(lrd[39] instanceof BigDecimal ? ((BigDecimal) lrd[39]).doubleValue() : 0);

                    // 40 - CUSTOMER_DEPOSIT_AED
                    Cell cell40 = row.getCell(40);
                    if (cell40 == null) cell40 = row.createCell(40);
                    cell40.setCellStyle(numberStyle);
                    cell40.setCellValue(lrd[40] instanceof BigDecimal ? ((BigDecimal) lrd[40]).doubleValue() : 0);

                    // 41 - CUSTOMER_DEPOSIT_PERCENT
                    Cell cell41 = row.getCell(41);
                    if (cell41 == null) cell41 = row.createCell(41);
                    cell41.setCellStyle(numberStyle);
                    cell41.setCellValue(lrd[41] instanceof BigDecimal ? ((BigDecimal) lrd[41]).doubleValue() : 0);

                    // 42 - LONG_TERM_FUNDING_AED
                    Cell cell42 = row.getCell(42);
                    if (cell42 == null) cell42 = row.createCell(42);
                    cell42.setCellStyle(numberStyle);
                    cell42.setCellValue(lrd[42] instanceof BigDecimal ? ((BigDecimal) lrd[42]).doubleValue() : 0);

                    // 43 - LONG_TERM_FUNDING_PERCENT
                    Cell cell43 = row.getCell(43);
                    if (cell43 == null) cell43 = row.createCell(43);
                    cell43.setCellStyle(numberStyle);
                    cell43.setCellValue(lrd[43] instanceof BigDecimal ? ((BigDecimal) lrd[43]).doubleValue() : 0);

                    // 44 - OTHER_LIABILITIES_AED
                    Cell cell44 = row.getCell(44);
                    if (cell44 == null) cell44 = row.createCell(44);
                    cell44.setCellStyle(numberStyle);
                    cell44.setCellValue(lrd[44] instanceof BigDecimal ? ((BigDecimal) lrd[44]).doubleValue() : 0);

                    // 45 - OTHER_LIABILITIES_PERCENT
                    Cell cell45 = row.getCell(45);
                    if (cell45 == null) cell45 = row.createCell(45);
                    cell45.setCellStyle(numberStyle);
                    cell45.setCellValue(lrd[45] instanceof BigDecimal ? ((BigDecimal) lrd[45]).doubleValue() : 0);

                    // 46 - NEGATIVE_FV_DERIVATIVES_AED
                    Cell cell46 = row.getCell(46);
                    if (cell46 == null) cell46 = row.createCell(46);
                    cell46.setCellStyle(numberStyle);
                    cell46.setCellValue(lrd[46] instanceof BigDecimal ? ((BigDecimal) lrd[46]).doubleValue() : 0);

                 // 47 - NEGATIVE_FV_DERIVATIVES_PERCENT
                    Cell cell47 = row.getCell(47);
                    if (cell47 == null) cell47 = row.createCell(47);
                    cell47.setCellStyle(numberStyle);
                    cell47.setCellValue(lrd[47] instanceof BigDecimal ? ((BigDecimal) lrd[47]).doubleValue() : 0);

                    // 48 - CAPITAL_RESERVES_AED
                    Cell cell48 = row.getCell(48);
                    if (cell48 == null) cell48 = row.createCell(48);
                    cell48.setCellStyle(numberStyle);
                    cell48.setCellValue(lrd[48] instanceof BigDecimal ? ((BigDecimal) lrd[48]).doubleValue() : 0);

                    // 49 - CAPITAL_RESERVES_PERCENT
                    Cell cell49 = row.getCell(49);
                    if (cell49 == null) cell49 = row.createCell(49);
                    cell49.setCellStyle(numberStyle);
                    cell49.setCellValue(lrd[49] instanceof BigDecimal ? ((BigDecimal) lrd[49]).doubleValue() : 0);

                    // 50 - SHORT_TERM_REG_RATIO_REQ
                    Cell cell50 = row.getCell(50);
                    if (cell50 == null) cell50 = row.createCell(50);
                    cell50.setCellStyle(numberStyle);
                    cell50.setCellValue(lrd[50] instanceof BigDecimal ? ((BigDecimal) lrd[50]).doubleValue() : 0);

                    // 51 - HQ_ASSETS_CONVERTED_AED
                    Cell cell51 = row.getCell(51);
                    if (cell51 == null) cell51 = row.createCell(51);
                    cell51.setCellStyle(numberStyle);
                    cell51.setCellValue(lrd[51] instanceof BigDecimal ? ((BigDecimal) lrd[51]).doubleValue() : 0);

                    // 52 - OUTFLOWS_30D_AED
                    Cell cell52 = row.getCell(52);
                    if (cell52 == null) cell52 = row.createCell(52);
                    cell52.setCellStyle(numberStyle);
                    cell52.setCellValue(lrd[52] instanceof BigDecimal ? ((BigDecimal) lrd[52]).doubleValue() : 0);

                    // 53 - INFLOWS_30D_AED
                    Cell cell53 = row.getCell(53);
                    if (cell53 == null) cell53 = row.createCell(53);
                    cell53.setCellStyle(numberStyle);
                    cell53.setCellValue(lrd[53] instanceof BigDecimal ? ((BigDecimal) lrd[53]).doubleValue() : 0);

                    // 54 - LCR
                    Cell cell54 = row.getCell(54);
                    if (cell54 == null) cell54 = row.createCell(54);
                    cell54.setCellStyle(numberStyle);
                    cell54.setCellValue(lrd[54] instanceof BigDecimal ? ((BigDecimal) lrd[54]).doubleValue() : 0);

                    // 55 - AED_HQ_ASSETS
                    Cell cell55 = row.getCell(55);
                    if (cell55 == null) cell55 = row.createCell(55);
                    cell55.setCellStyle(numberStyle);
                    cell55.setCellValue(lrd[55] instanceof BigDecimal ? ((BigDecimal) lrd[55]).doubleValue() : 0);

                    // 56 - AED_OUTFLOWS_30D
                    Cell cell56 = row.getCell(56);
                    if (cell56 == null) cell56 = row.createCell(56);
                    cell56.setCellStyle(numberStyle);
                    cell56.setCellValue(lrd[56] instanceof BigDecimal ? ((BigDecimal) lrd[56]).doubleValue() : 0);

                    // 57 - AED_INFLOWS_30D
                    Cell cell57 = row.getCell(57);
                    if (cell57 == null) cell57 = row.createCell(57);
                    cell57.setCellStyle(numberStyle);
                    cell57.setCellValue(lrd[57] instanceof BigDecimal ? ((BigDecimal) lrd[57]).doubleValue() : 0);

                    // 58 - AED_LCR
                    Cell cell58 = row.getCell(58);
                    if (cell58 == null) cell58 = row.createCell(58);
                    cell58.setCellStyle(numberStyle);
                    cell58.setCellValue(lrd[58] instanceof BigDecimal ? ((BigDecimal) lrd[58]).doubleValue() : 0);

                    // 59 - USD_HQ_ASSETS
                    Cell cell59 = row.getCell(59);
                    if (cell59 == null) cell59 = row.createCell(59);
                    cell59.setCellStyle(numberStyle);
                    cell59.setCellValue(lrd[59] instanceof BigDecimal ? ((BigDecimal) lrd[59]).doubleValue() : 0);

                    // 60 - USD_OUTFLOWS_30D
                    Cell cell60 = row.getCell(60);
                    if (cell60 == null) cell60 = row.createCell(60);
                    cell60.setCellStyle(numberStyle);
                    cell60.setCellValue(lrd[60] instanceof BigDecimal ? ((BigDecimal) lrd[60]).doubleValue() : 0);

                    // 61 - USD_INFLOWS_30D
                    Cell cell61 = row.getCell(61);
                    if (cell61 == null) cell61 = row.createCell(61);
                    cell61.setCellStyle(numberStyle);
                    cell61.setCellValue(lrd[61] instanceof BigDecimal ? ((BigDecimal) lrd[61]).doubleValue() : 0);

                    // 62 - USD_LCR
                    Cell cell62 = row.getCell(62);
                    if (cell62 == null) cell62 = row.createCell(62);
                    cell62.setCellStyle(numberStyle);
                    cell62.setCellValue(lrd[62] instanceof BigDecimal ? ((BigDecimal) lrd[62]).doubleValue() : 0);

                    // 63 - ELAR_HQ_ASSETS_CONVERTED_AED
                    Cell cell63 = row.getCell(63);
                    if (cell63 == null) cell63 = row.createCell(63);
                    cell63.setCellStyle(numberStyle);
                    cell63.setCellValue(lrd[63] instanceof BigDecimal ? ((BigDecimal) lrd[63]).doubleValue() : 0);

                    // 64 - TOTAL_LIABILITIES_CONVERTED_AED
                    Cell cell64 = row.getCell(64);
                    if (cell64 == null) cell64 = row.createCell(64);
                    cell64.setCellStyle(numberStyle);
                    cell64.setCellValue(lrd[64] instanceof BigDecimal ? ((BigDecimal) lrd[64]).doubleValue() : 0);

                    // 65 - ELIGIBLE_ASSET_RATIO
                    Cell cell65 = row.getCell(65);
                    if (cell65 == null) cell65 = row.createCell(65);
                    cell65.setCellStyle(numberStyle);
                    cell65.setCellValue(lrd[65] instanceof BigDecimal ? ((BigDecimal) lrd[65]).doubleValue() : 0);

                    // 66 - AED_ELAR_HQ_ASSETS
                    Cell cell66 = row.getCell(66);
                    if (cell66 == null) cell66 = row.createCell(66);
                    cell66.setCellStyle(numberStyle);
                    cell66.setCellValue(lrd[66] instanceof BigDecimal ? ((BigDecimal) lrd[66]).doubleValue() : 0);

                    // 67 - AED_TOTAL_LIABILITIES
                    Cell cell67 = row.getCell(67);
                    if (cell67 == null) cell67 = row.createCell(67);
                    cell67.setCellStyle(numberStyle);
                    cell67.setCellValue(lrd[67] instanceof BigDecimal ? ((BigDecimal) lrd[67]).doubleValue() : 0);

                    // 68 - AED_ELIGIBLR_ASSET_RATIO
                    Cell cell68 = row.getCell(68);
                    if (cell68 == null) cell68 = row.createCell(68);
                    cell68.setCellStyle(numberStyle);
                    cell68.setCellValue(lrd[68] instanceof BigDecimal ? ((BigDecimal) lrd[68]).doubleValue() : 0);

                    // 69 - USD_ELAR_HQ_ASSETS
                    Cell cell69 = row.getCell(69);
                    if (cell69 == null) cell69 = row.createCell(69);
                    cell69.setCellStyle(numberStyle);
                    cell69.setCellValue(lrd[69] instanceof BigDecimal ? ((BigDecimal) lrd[69]).doubleValue() : 0);

                    // 70 - USD_TOTAL_LIABILITIES
                    Cell cell70 = row.getCell(70);
                    if (cell70 == null) cell70 = row.createCell(70);
                    cell70.setCellStyle(numberStyle);
                    cell70.setCellValue(lrd[70] instanceof BigDecimal ? ((BigDecimal) lrd[70]).doubleValue() : 0);

                    // 71 - USD_EIGIBLE_ASSET_RATIO
                    Cell cell71 = row.getCell(71);
                    if (cell71 == null) cell71 = row.createCell(71);
                    cell71.setCellStyle(numberStyle);
                    cell71.setCellValue(lrd[71] instanceof BigDecimal ? ((BigDecimal) lrd[71]).doubleValue() : 0);

                    // 72 - ASF_CONVERTED_AED
                    Cell cell72 = row.getCell(72);
                    if (cell72 == null) cell72 = row.createCell(72);
                    cell72.setCellStyle(numberStyle);
                    cell72.setCellValue(lrd[72] instanceof BigDecimal ? ((BigDecimal) lrd[72]).doubleValue() : 0);

                    // 73 - RSF_CONVERTED_AED
                    Cell cell73 = row.getCell(73);
                    if (cell73 == null) cell73 = row.createCell(73);
                    cell73.setCellStyle(numberStyle);
                    cell73.setCellValue(lrd[73] instanceof BigDecimal ? ((BigDecimal) lrd[73]).doubleValue() : 0);

                    // 74 - NSFR
                    Cell cell74 = row.getCell(74);
                    if (cell74 == null) cell74 = row.createCell(74);
                    cell74.setCellStyle(numberStyle);
                    cell74.setCellValue(lrd[74] instanceof BigDecimal ? ((BigDecimal) lrd[74]).doubleValue() : 0);

                 // 75 - AED_ASF
                    Cell cell75 = row.getCell(75);
                    if (cell75 == null) cell75 = row.createCell(75);
                    cell75.setCellStyle(numberStyle);
                    cell75.setCellValue(lrd[75] instanceof BigDecimal ? ((BigDecimal) lrd[75]).doubleValue() : 0);

                    // 76 - AED_RSF
                    Cell cell76 = row.getCell(76);
                    if (cell76 == null) cell76 = row.createCell(76);
                    cell76.setCellStyle(numberStyle);
                    cell76.setCellValue(lrd[76] instanceof BigDecimal ? ((BigDecimal) lrd[76]).doubleValue() : 0);

                    // 77 - AED_NSFR
                    Cell cell77 = row.getCell(77);
                    if (cell77 == null) cell77 = row.createCell(77);
                    cell77.setCellStyle(numberStyle);
                    cell77.setCellValue(lrd[77] instanceof BigDecimal ? ((BigDecimal) lrd[77]).doubleValue() : 0);

                    // 78 - USD_ASF
                    Cell cell78 = row.getCell(78);
                    if (cell78 == null) cell78 = row.createCell(78);
                    cell78.setCellStyle(numberStyle);
                    cell78.setCellValue(lrd[78] instanceof BigDecimal ? ((BigDecimal) lrd[78]).doubleValue() : 0);

                    // 79 - USD_RSF
                    Cell cell79 = row.getCell(79);
                    if (cell79 == null) cell79 = row.createCell(79);
                    cell79.setCellStyle(numberStyle);
                    cell79.setCellValue(lrd[79] instanceof BigDecimal ? ((BigDecimal) lrd[79]).doubleValue() : 0);

                    // 80 - USD_NSFR
                    Cell cell80 = row.getCell(80);
                    if (cell80 == null) cell80 = row.createCell(80);
                    cell80.setCellStyle(numberStyle);
                    cell80.setCellValue(lrd[80] instanceof BigDecimal ? ((BigDecimal) lrd[80]).doubleValue() : 0);

                    // 81 - LOANS_ADVANCES_CONVERTED_AED
                    Cell cell81 = row.getCell(81);
                    if (cell81 == null) cell81 = row.createCell(81);
                    cell81.setCellStyle(numberStyle);
                    cell81.setCellValue(lrd[81] instanceof BigDecimal ? ((BigDecimal) lrd[81]).doubleValue() : 0);

                    // 82 - STABLE_RESOURCES_CONVERTED_AED
                    Cell cell82 = row.getCell(82);
                    if (cell82 == null) cell82 = row.createCell(82);
                    cell82.setCellStyle(numberStyle);
                    cell82.setCellValue(lrd[82] instanceof BigDecimal ? ((BigDecimal) lrd[82]).doubleValue() : 0);

                    // 83 - LOANS_TO_STABLE_RESOURCES_RATIO
                    Cell cell83 = row.getCell(83);
                    if (cell83 == null) cell83 = row.createCell(83);
                    cell83.setCellStyle(numberStyle);
                    cell83.setCellValue(lrd[83] instanceof BigDecimal ? ((BigDecimal) lrd[83]).doubleValue() : 0);

                    // 84 - AED_LOANS_ADVANCES
                    Cell cell84 = row.getCell(84);
                    if (cell84 == null) cell84 = row.createCell(84);
                    cell84.setCellStyle(numberStyle);
                    cell84.setCellValue(lrd[84] instanceof BigDecimal ? ((BigDecimal) lrd[84]).doubleValue() : 0);

                    // 85 - AED_STABLE_RESOURCES
                    Cell cell85 = row.getCell(85);
                    if (cell85 == null) cell85 = row.createCell(85);
                    cell85.setCellStyle(numberStyle);
                    cell85.setCellValue(lrd[85] instanceof BigDecimal ? ((BigDecimal) lrd[85]).doubleValue() : 0);

                    // 86 - AED_LTS_RATIO
                    Cell cell86 = row.getCell(86);
                    if (cell86 == null) cell86 = row.createCell(86);
                    cell86.setCellStyle(numberStyle);
                    cell86.setCellValue(lrd[86] instanceof BigDecimal ? ((BigDecimal) lrd[86]).doubleValue() : 0);

                    // 87 - USD_LOANS_ADVANCES
                    Cell cell87 = row.getCell(87);
                    if (cell87 == null) cell87 = row.createCell(87);
                    cell87.setCellStyle(numberStyle);
                    cell87.setCellValue(lrd[87] instanceof BigDecimal ? ((BigDecimal) lrd[87]).doubleValue() : 0);

                    // 88 - USD_STABLE_RESOURCES
                    Cell cell88 = row.getCell(88);
                    if (cell88 == null) cell88 = row.createCell(88);
                    cell88.setCellStyle(numberStyle);
                    cell88.setCellValue(lrd[88] instanceof BigDecimal ? ((BigDecimal) lrd[88]).doubleValue() : 0);

                    // 89 - USD_LTS_RATIO
                    Cell cell89 = row.getCell(89);
                    if (cell89 == null) cell89 = row.createCell(89);
                    cell89.setCellStyle(numberStyle);
                    cell89.setCellValue(lrd[89] instanceof BigDecimal ? ((BigDecimal) lrd[89]).doubleValue() : 0);

                    // 90 - STABLE_TERM_DEPOSIT_AED
                    Cell cell90 = row.getCell(90);
                    if (cell90 == null) cell90 = row.createCell(90);
                    cell90.setCellStyle(numberStyle);
                    cell90.setCellValue(lrd[90] instanceof BigDecimal ? ((BigDecimal) lrd[90]).doubleValue() : 0);

                    // 91 - VOLATILE_TERM_DEPOSIT_AED
                    Cell cell91 = row.getCell(91);
                    if (cell91 == null) cell91 = row.createCell(91);
                    cell91.setCellStyle(numberStyle);
                    cell91.setCellValue(lrd[91] instanceof BigDecimal ? ((BigDecimal) lrd[91]).doubleValue() : 0);

                    // 92 - STABLE_CASA_AED
                    Cell cell92 = row.getCell(92);
                    if (cell92 == null) cell92 = row.createCell(92);
                    cell92.setCellStyle(numberStyle);
                    cell92.setCellValue(lrd[92] instanceof BigDecimal ? ((BigDecimal) lrd[92]).doubleValue() : 0);

                    // 93 - VOLATILE_CASA_AED
                    Cell cell93 = row.getCell(93);
                    if (cell93 == null) cell93 = row.createCell(93);
                    cell93.setCellStyle(numberStyle);
                    cell93.setCellValue(lrd[93] instanceof BigDecimal ? ((BigDecimal) lrd[93]).doubleValue() : 0);

                    // 94 - TERM_DEPOSIT_AED
                    Cell cell94 = row.getCell(94);
                    if (cell94 == null) cell94 = row.createCell(94);
                    cell94.setCellStyle(numberStyle);
                    cell94.setCellValue(lrd[94] instanceof BigDecimal ? ((BigDecimal) lrd[94]).doubleValue() : 0);

                    // 95 - CASA_AED
                    Cell cell95 = row.getCell(95);
                    if (cell95 == null) cell95 = row.createCell(95);
                    cell95.setCellStyle(numberStyle);
                    cell95.setCellValue(lrd[95] instanceof BigDecimal ? ((BigDecimal) lrd[95]).doubleValue() : 0);

                    // 96 - LOANS_TO_DEPOSITS_RATIO
                    Cell cell96 = row.getCell(96);
                    if (cell96 == null) cell96 = row.createCell(96);
                    cell96.setCellStyle(numberStyle);
                    cell96.setCellValue(lrd[96] instanceof BigDecimal ? ((BigDecimal) lrd[96]).doubleValue() : 0);

                    // 97 - STABLE_TERM_DEPOSIT_TO_ASSETS
                    Cell cell97 = row.getCell(97);
                    if (cell97 == null) cell97 = row.createCell(97);
                    cell97.setCellStyle(numberStyle);
                    cell97.setCellValue(lrd[97] instanceof BigDecimal ? ((BigDecimal) lrd[97]).doubleValue() : 0);

                    // 98 - VOLATILE_TERM_DEPOSIT_TO_ASSETS
                    Cell cell98 = row.getCell(98);
                    if (cell98 == null) cell98 = row.createCell(98);
                    cell98.setCellStyle(numberStyle);
                    cell98.setCellValue(lrd[98] instanceof BigDecimal ? ((BigDecimal) lrd[98]).doubleValue() : 0);

                    // 99 - STABLE_CASA_TO_ASSETS
                    Cell cell99 = row.getCell(99);
                    if (cell99 == null) cell99 = row.createCell(99);
                    cell99.setCellStyle(numberStyle);
                    cell99.setCellValue(lrd[99] instanceof BigDecimal ? ((BigDecimal) lrd[99]).doubleValue() : 0);

                    // 100 - VOLATILE_CASA_TO_ASSETS
                    Cell cell100 = row.getCell(100);
                    if (cell100 == null) cell100 = row.createCell(100);
                    cell100.setCellStyle(numberStyle);
                    cell100.setCellValue(lrd[100] instanceof BigDecimal ? ((BigDecimal) lrd[100]).doubleValue() : 0);
                    
                 // 101 - LIQUID_ASSETS_TO_TOTAL_DEPOSITS
                    Cell cell101 = row.getCell(101);
                    if (cell101 == null) cell101 = row.createCell(101);
                    cell101.setCellStyle(numberStyle);
                    cell101.setCellValue(lrd[101] instanceof BigDecimal ? ((BigDecimal) lrd[101]).doubleValue() : 0);

                    // 102 - LIQUID_ASSETS_TO_TOTAL_CASA
                    Cell cell102 = row.getCell(102);
                    if (cell102 == null) cell102 = row.createCell(102);
                    cell102.setCellStyle(numberStyle);
                    cell102.setCellValue(lrd[102] instanceof BigDecimal ? ((BigDecimal) lrd[102]).doubleValue() : 0);

                    // 103 - SIX_MONTH_CASH_FLOW_GAP_AED
                    Cell cell103 = row.getCell(103);
                    if (cell103 == null) cell103 = row.createCell(103);
                    cell103.setCellStyle(numberStyle);
                    cell103.setCellValue(lrd[103] instanceof BigDecimal ? ((BigDecimal) lrd[103]).doubleValue() : 0);

                    // 104 - TOTAL_LIABILITIES_EXCL_CAPITAL
                    Cell cell104 = row.getCell(104);
                    if (cell104 == null) cell104 = row.createCell(104);
                    cell104.setCellStyle(numberStyle);
                    cell104.setCellValue(lrd[104] instanceof BigDecimal ? ((BigDecimal) lrd[104]).doubleValue() : 0);

                    // 105 - SIX_MONTH_GAP_RATIO
                    Cell cell105 = row.getCell(105);
                    if (cell105 == null) cell105 = row.createCell(105);
                    cell105.setCellStyle(numberStyle);
                    cell105.setCellValue(lrd[105] instanceof BigDecimal ? ((BigDecimal) lrd[105]).doubleValue() : 0);

                 // 106 - AED_SIX_MONTH_CASH_FLOW_GAP
                    Cell cell106 = row.getCell(106);
                    if (cell106 == null) cell106 = row.createCell(106);
                    cell106.setCellStyle(numberStyle);
                    cell106.setCellValue(lrd[106] instanceof BigDecimal ? ((BigDecimal) lrd[106]).doubleValue() : 0);

                    // 107 - AED_LIABILITIES_EXCL_CAPITAL
                    Cell cell107 = row.getCell(107);
                    if (cell107 == null) cell107 = row.createCell(107);
                    cell107.setCellStyle(numberStyle);
                    cell107.setCellValue(lrd[107] instanceof BigDecimal ? ((BigDecimal) lrd[107]).doubleValue() : 0);

                    // 108 - AED_SIX_MONTH_GAP_RATIO
                    Cell cell108 = row.getCell(108);
                    if (cell108 == null) cell108 = row.createCell(108);
                    cell108.setCellStyle(numberStyle);
                    cell108.setCellValue(lrd[108] instanceof BigDecimal ? ((BigDecimal) lrd[108]).doubleValue() : 0);

                    // 109 - USD_SIX_MONTH_CASH_FLOW_GAP
                    Cell cell109 = row.getCell(109);
                    if (cell109 == null) cell109 = row.createCell(109);
                    cell109.setCellStyle(numberStyle);
                    cell109.setCellValue(lrd[109] instanceof BigDecimal ? ((BigDecimal) lrd[109]).doubleValue() : 0);

                    // 110 - USD_LIABILITIES_EXCL_CAPITAL
                    Cell cell110 = row.getCell(110);
                    if (cell110 == null) cell110 = row.createCell(110);
                    cell110.setCellStyle(numberStyle);
                    cell110.setCellValue(lrd[110] instanceof BigDecimal ? ((BigDecimal) lrd[110]).doubleValue() : 0);

                    // 111 - USD_SIX_MONTH_GAP_RATIO
                    Cell cell111 = row.getCell(111);
                    if (cell111 == null) cell111 = row.createCell(111);
                    cell111.setCellStyle(numberStyle);
                    cell111.setCellValue(lrd[111] instanceof BigDecimal ? ((BigDecimal) lrd[111]).doubleValue() : 0);

                    // 112 - THREE_MONTH_CASH_FLOW_GAP_AED
                    Cell cell112 = row.getCell(112);
                    if (cell112 == null) cell112 = row.createCell(112);
                    cell112.setCellStyle(numberStyle);
                    cell112.setCellValue(lrd[112] instanceof BigDecimal ? ((BigDecimal) lrd[112]).doubleValue() : 0);

                    // 113 - THREE_MONTH_GAP_RATIO
                    Cell cell113 = row.getCell(113);
                    if (cell113 == null) cell113 = row.createCell(113);
                    cell113.setCellStyle(numberStyle);
                    cell113.setCellValue(lrd[113] instanceof BigDecimal ? ((BigDecimal) lrd[113]).doubleValue() : 0);

                    // 114 - ONE_MONTH_CASH_FLOW_GAP_AED
                    Cell cell114 = row.getCell(114);
                    if (cell114 == null) cell114 = row.createCell(114);
                    cell114.setCellStyle(numberStyle);
                    cell114.setCellValue(lrd[114] instanceof BigDecimal ? ((BigDecimal) lrd[114]).doubleValue() : 0);

                    // 115 - ONE_MONTH_GAP_RATIO
                    Cell cell115 = row.getCell(115);
                    if (cell115 == null) cell115 = row.createCell(115);
                    cell115.setCellStyle(numberStyle);
                    cell115.setCellValue(lrd[115] instanceof BigDecimal ? ((BigDecimal) lrd[115]).doubleValue() : 0);

                    // 116 - SEVEN_DAY_CASH_FLOW_GAP_AED
                    Cell cell116 = row.getCell(116);
                    if (cell116 == null) cell116 = row.createCell(116);
                    cell116.setCellStyle(numberStyle);
                    cell116.setCellValue(lrd[116] instanceof BigDecimal ? ((BigDecimal) lrd[116]).doubleValue() : 0);

                    // 117 - SEVEN_DAY_GAP_RATIO
                    Cell cell117 = row.getCell(117);
                    if (cell117 == null) cell117 = row.createCell(117);
                    cell117.setCellStyle(numberStyle);
                    cell117.setCellValue(lrd[117] instanceof BigDecimal ? ((BigDecimal) lrd[117]).doubleValue() : 0);

                    // 118 - UNENCUMBERED_LIQUID_ASSETS_AED
                    Cell cell118 = row.getCell(118);
                    if (cell118 == null) cell118 = row.createCell(118);
                    cell118.setCellStyle(numberStyle);
                    cell118.setCellValue(lrd[118] instanceof BigDecimal ? ((BigDecimal) lrd[118]).doubleValue() : 0);

                    // 119 - ONE_MONTH_CASH_FLOW_GAP_COPY
                    Cell cell119 = row.getCell(119);
                    if (cell119 == null) cell119 = row.createCell(119);
                    cell119.setCellStyle(numberStyle);
                    cell119.setCellValue(lrd[119] instanceof BigDecimal ? ((BigDecimal) lrd[119]).doubleValue() : 0);

                    // 120 - UNENCUMBERED_TO_1M_GAP_RATIO
                    Cell cell120 = row.getCell(120);
                    if (cell120 == null) cell120 = row.createCell(120);
                    cell120.setCellStyle(numberStyle);
                    cell120.setCellValue(lrd[120] instanceof BigDecimal ? ((BigDecimal) lrd[120]).doubleValue() : 0);

                    
                 // 121 - CBUAE_CASH_BALANCES
                    Cell cell121 = row.getCell(121);
                    if (cell121 == null) cell121 = row.createCell(121);
                    cell121.setCellStyle(numberStyle);
                    cell121.setCellValue(lrd[121] instanceof BigDecimal ? ((BigDecimal) lrd[121]).doubleValue() : 0);

                    // 122 - CBUAE_CASH_BALANCES_PERCENT
                    Cell cell122 = row.getCell(122);
                    if (cell122 == null) cell122 = row.createCell(122);
                    cell122.setCellStyle(numberStyle);
                    cell122.setCellValue(lrd[122] instanceof BigDecimal ? ((BigDecimal) lrd[122]).doubleValue() : 0);

                    // 123 - MONEY_MKT_PLACEMENTS_LT_6M
                    Cell cell123 = row.getCell(123);
                    if (cell123 == null) cell123 = row.createCell(123);
                    cell123.setCellStyle(numberStyle);
                    cell123.setCellValue(lrd[123] instanceof BigDecimal ? ((BigDecimal) lrd[123]).doubleValue() : 0);

                    // 124 - MONEY_MKT_PLACEMENTS_LT_6M_PERCENT
                    Cell cell124 = row.getCell(124);
                    if (cell124 == null) cell124 = row.createCell(124);
                    cell124.setCellStyle(numberStyle);
                    cell124.setCellValue(lrd[124] instanceof BigDecimal ? ((BigDecimal) lrd[124]).doubleValue() : 0);

                    // 125 - LOCAL_CURRENCY_GOVT_BONDS
                    Cell cell125 = row.getCell(125);
                    if (cell125 == null) cell125 = row.createCell(125);
                    cell125.setCellStyle(numberStyle);
                    cell125.setCellValue(lrd[125] instanceof BigDecimal ? ((BigDecimal) lrd[125]).doubleValue() : 0);

                    // 126 - LOCAL_CURRENCY_GOVT_BONDS_PERCENT
                    Cell cell126 = row.getCell(126);
                    if (cell126 == null) cell126 = row.createCell(126);
                    cell126.setCellStyle(numberStyle);
                    cell126.setCellValue(lrd[126] instanceof BigDecimal ? ((BigDecimal) lrd[126]).doubleValue() : 0);

                    // 127 - HARD_CURRENCY_GOVT_BONDS
                    Cell cell127 = row.getCell(127);
                    if (cell127 == null) cell127 = row.createCell(127);
                    cell127.setCellStyle(numberStyle);
                    cell127.setCellValue(lrd[127] instanceof BigDecimal ? ((BigDecimal) lrd[127]).doubleValue() : 0);

                    // 128 - HARD_CURRENCY_GOVT_BONDS_PERCENT
                    Cell cell128 = row.getCell(128);
                    if (cell128 == null) cell128 = row.createCell(128);
                    cell128.setCellStyle(numberStyle);
                    cell128.setCellValue(lrd[128] instanceof BigDecimal ? ((BigDecimal) lrd[128]).doubleValue() : 0);

                    // 129 - FOREIGN_GOVT_BONDS
                    Cell cell129 = row.getCell(129);
                    if (cell129 == null) cell129 = row.createCell(129);
                    cell129.setCellStyle(numberStyle);
                    cell129.setCellValue(lrd[129] instanceof BigDecimal ? ((BigDecimal) lrd[129]).doubleValue() : 0);

                    // 130 - FOREIGN_GOVT_BONDS_PERCENT
                    Cell cell130 = row.getCell(130);
                    if (cell130 == null) cell130 = row.createCell(130);
                    cell130.setCellStyle(numberStyle);
                    cell130.setCellValue(lrd[130] instanceof BigDecimal ? ((BigDecimal) lrd[130]).doubleValue() : 0);

                    // 131 - PLEDGE_SECURITIES
                    Cell cell131 = row.getCell(131);
                    if (cell131 == null) cell131 = row.createCell(131);
                    cell131.setCellStyle(numberStyle);
                    cell131.setCellValue(lrd[131] instanceof BigDecimal ? ((BigDecimal) lrd[131]).doubleValue() : 0);

                    // 132 - TOTAL_PLEDGEABLE_SECURITIES
                    Cell cell132 = row.getCell(132);
                    if (cell132 == null) cell132 = row.createCell(132);
                    cell132.setCellStyle(numberStyle);
                    cell132.setCellValue(lrd[132] instanceof BigDecimal ? ((BigDecimal) lrd[132]).doubleValue() : 0);

                    // 133 - PLEDGE_RATIO
                    Cell cell133 = row.getCell(133);
                    if (cell133 == null) cell133 = row.createCell(133);
                    cell133.setCellStyle(numberStyle);
                    cell133.setCellValue(lrd[133] instanceof BigDecimal ? ((BigDecimal) lrd[133]).doubleValue() : 0);

                    // 134 - TOTAL_COMPLEX_FIN_INSTRUMENTS
                    Cell cell134 = row.getCell(134);
                    if (cell134 == null) cell134 = row.createCell(134);
                    cell134.setCellStyle(numberStyle);
                    cell134.setCellValue(lrd[134] instanceof BigDecimal ? ((BigDecimal) lrd[134]).doubleValue() : 0);

                    // 135 - TOTAL_TRADABLE_ASSETS
                    Cell cell135 = row.getCell(135);
                    if (cell135 == null) cell135 = row.createCell(135);
                    cell135.setCellStyle(numberStyle);
                    cell135.setCellValue(lrd[135] instanceof BigDecimal ? ((BigDecimal) lrd[135]).doubleValue() : 0);

                    // 136 - COMPLEX_TO_TRADABLE_RATIO
                    Cell cell136 = row.getCell(136);
                    if (cell136 == null) cell136 = row.createCell(136);
                    cell136.setCellStyle(numberStyle);
                    cell136.setCellValue(lrd[136] instanceof BigDecimal ? ((BigDecimal) lrd[136]).doubleValue() : 0);

                    // 137 - TOP_10_DEPOSITS
                    Cell cell137 = row.getCell(137);
                    if (cell137 == null) cell137 = row.createCell(137);
                    cell137.setCellStyle(numberStyle);
                    cell137.setCellValue(lrd[137] instanceof BigDecimal ? ((BigDecimal) lrd[137]).doubleValue() : 0);

                    // 138 - TOTAL_DEPOSITS_AED1
                    Cell cell138 = row.getCell(138);
                    if (cell138 == null) cell138 = row.createCell(138);
                    cell138.setCellStyle(numberStyle);
                    cell138.setCellValue(lrd[138] instanceof BigDecimal ? ((BigDecimal) lrd[138]).doubleValue() : 0);

                    // 139 - TOP_10_TO_TOTAL_RATIO
                    Cell cell139 = row.getCell(139);
                    if (cell139 == null) cell139 = row.createCell(139);
                    cell139.setCellStyle(numberStyle);
                    cell139.setCellValue(lrd[139] instanceof BigDecimal ? ((BigDecimal) lrd[139]).doubleValue() : 0);

                    
                    
                 // 140 - ST_IB_BORROWING_LT_3M
                    Cell cell140 = row.getCell(140);
                    if (cell140 == null) cell140 = row.createCell(140);
                    cell140.setCellStyle(numberStyle);
                    cell140.setCellValue(lrd[140] instanceof BigDecimal ? ((BigDecimal) lrd[140]).doubleValue() : 0);

                    // 141 - ST_REPO_LT_3M
                    Cell cell141 = row.getCell(141);
                    if (cell141 == null) cell141 = row.createCell(141);
                    cell141.setCellStyle(numberStyle);
                    cell141.setCellValue(lrd[141] instanceof BigDecimal ? ((BigDecimal) lrd[141]).doubleValue() : 0);

                    // 142 - INTERBANK_BORROWING
                    Cell cell142 = row.getCell(142);
                    if (cell142 == null) cell142 = row.createCell(142);
                    cell142.setCellStyle(numberStyle);
                    cell142.setCellValue(lrd[142] instanceof BigDecimal ? ((BigDecimal) lrd[142]).doubleValue() : 0);

                    // 143 - REPO_AGREEMENTS
                    Cell cell143 = row.getCell(143);
                    if (cell143 == null) cell143 = row.createCell(143);
                    cell143.setCellStyle(numberStyle);
                    cell143.setCellValue(lrd[143] instanceof BigDecimal ? ((BigDecimal) lrd[143]).doubleValue() : 0);

                    // 144 - TOTAL_DEPOSITS_AED2
                    Cell cell144 = row.getCell(144);
                    if (cell144 == null) cell144 = row.createCell(144);
                    cell144.setCellStyle(numberStyle);
                    cell144.setCellValue(lrd[144] instanceof BigDecimal ? ((BigDecimal) lrd[144]).doubleValue() : 0);

                    // 145 - LONG_TERM_FUNDING_COPY
                    Cell cell145 = row.getCell(145);
                    if (cell145 == null) cell145 = row.createCell(145);
                    cell145.setCellStyle(numberStyle);
                    cell145.setCellValue(lrd[145] instanceof BigDecimal ? ((BigDecimal) lrd[145]).doubleValue() : 0);

                    // 146 - ST_IB_REPO_TO_TOTAL_FUNDING_RATIO
                    Cell cell146 = row.getCell(146);
                    if (cell146 == null) cell146 = row.createCell(146);
                    cell146.setCellStyle(numberStyle);
                    cell146.setCellValue(lrd[146] instanceof BigDecimal ? ((BigDecimal) lrd[146]).doubleValue() : 0);

                    // 147 - LARGEST_SINGLE_DEPOSITOR
                    Cell cell147 = row.getCell(147);
                    if (cell147 == null) cell147 = row.createCell(147);
                    cell147.setCellStyle(numberStyle);
                    cell147.setCellValue(lrd[147] instanceof BigDecimal ? ((BigDecimal) lrd[147]).doubleValue() : 0);

                    // 148 - INDIVIDUAL_COUNTERPARTY_TO_TOTAL_FUNDING_RATIO
                    Cell cell148 = row.getCell(148);
                    if (cell148 == null) cell148 = row.createCell(148);
                    cell148.setCellStyle(numberStyle);
                    cell148.setCellValue(lrd[148] instanceof BigDecimal ? ((BigDecimal) lrd[148]).doubleValue() : 0);

                    // 149 - UNUTILIZED_LOANS_CREDIT_LINES
                    Cell cell149 = row.getCell(149);
                    if (cell149 == null) cell149 = row.createCell(149);
                    cell149.setCellStyle(numberStyle);
                    cell149.setCellValue(lrd[149] instanceof BigDecimal ? ((BigDecimal) lrd[149]).doubleValue() : 0);

                    // 150 - DERIVATIVES_EXPECTED_NEG_EXPOSURE
                    Cell cell150 = row.getCell(150);
                    if (cell150 == null) cell150 = row.createCell(150);
                    cell150.setCellStyle(numberStyle);
                    cell150.setCellValue(lrd[150] instanceof BigDecimal ? ((BigDecimal) lrd[150]).doubleValue() : 0);

                    // 151 - DERIVATIVES_EXPECTED_NEG_EXPOSURE_PROXY
                    Cell cell151 = row.getCell(151);
                    if (cell151 == null) cell151 = row.createCell(151);
                    cell151.setCellStyle(numberStyle);
                    cell151.setCellValue(lrd[151] instanceof BigDecimal ? ((BigDecimal) lrd[151]).doubleValue() : 0);

                    // 152 - CONTINGENT_LIABILITIES
                    Cell cell152 = row.getCell(152);
                    if (cell152 == null) cell152 = row.createCell(152);
                    cell152.setCellStyle(numberStyle);
                    cell152.setCellValue(lrd[152] instanceof BigDecimal ? ((BigDecimal) lrd[152]).doubleValue() : 0);

                    // 153 - CONTINGENT_LIABILITIES_TO_TOTAL_FUNDING_RATIO
                    Cell cell153 = row.getCell(153);
                    if (cell153 == null) cell153 = row.createCell(153);
                    cell153.setCellStyle(numberStyle);
                    cell153.setCellValue(lrd[153] instanceof BigDecimal ? ((BigDecimal) lrd[153]).doubleValue() : 0);

                    // 154 - TOP_5_INDUSTRY_DEPOSITS
                    Cell cell154 = row.getCell(154);
                    if (cell154 == null) cell154 = row.createCell(154);
                    cell154.setCellStyle(numberStyle);
                    cell154.setCellValue(lrd[154] instanceof BigDecimal ? ((BigDecimal) lrd[154]).doubleValue() : 0);

                    // 155 - TOP_5_TO_TOTAL_DEPOSITS_RATIO
                    Cell cell155 = row.getCell(155);
                    if (cell155 == null) cell155 = row.createCell(155);
                    cell155.setCellStyle(numberStyle);
                    cell155.setCellValue(lrd[155] instanceof BigDecimal ? ((BigDecimal) lrd[155]).doubleValue() : 0);

                    // 156 - LONG_TERM_LIABILITIES_TO_TOTAL_LIABILITIES_RATIO
                    Cell cell156 = row.getCell(156);
                    if (cell156 == null) cell156 = row.createCell(156);
                    cell156.setCellStyle(numberStyle);
                    cell156.setCellValue(lrd[156] instanceof BigDecimal ? ((BigDecimal) lrd[156]).doubleValue() : 0);


                }
			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
		} else {
			System.out.println("No Mm data found to generate the Excel file.");
		}

		// Write the final workbook content to the in-memory stream.
		workbook.write(out);

		logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

		return out.toByteArray();
	}
}
}
