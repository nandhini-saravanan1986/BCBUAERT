package com.bornfire.xbrl.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import java.io.InputStream;
import java.nio.file.Files;
import java.io.ByteArrayOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileNotFoundException;

@Service
public class RT_InvestmentRiskDataDashboard_Service {
	
    private static final Logger logger = LoggerFactory.getLogger(RT_InvestmentRiskDataDashboard_Service.class);

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
    
    
    
    public byte[] generateInvestmentriskdataExcel() throws Exception {
        logger.info("Service: Starting Investment Risk Data Dashboard Excel generation process in memory.");

        List<Object[]> investmentriskdata = RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS.getinvestmentriskdata1();

        if (investmentriskdata.isEmpty()) {
            logger.warn("Service: No data found for Investment Risk Data Dashboard report. Returning empty result.");
            return new byte[0];
        }

        String templateDir = env.getProperty("output.exportpathtemp");
        String templateFileName = "CBUAE_Investment Risk Data_Dashboard_Template.xls";
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

            int startRow = 2;

            for (int i = 0; i < investmentriskdata.size(); i++) {
                Object[] mm = investmentriskdata.get(i);
                Row row = sheet.getRow(startRow + i);
                if (row == null) row = sheet.createRow(startRow + i);

                int colIndex = 0;

                // 0 - dataDate (Date)
                Cell cell0 = row.createCell(colIndex++);
                cell0.setCellStyle(dateStyle);
                cell0.setCellValue(mm[0] instanceof Date ? (Date) mm[0] : null);

                // 1 - bankName (String)
                Cell cell1 = row.createCell(colIndex++);
                cell1.setCellStyle(textStyle);
                cell1.setCellValue(mm[1] == null ? "" : mm[1].toString());

                // 2 - groupHeadOfficeSubsidiary (String)
                Cell cell2 = row.createCell(colIndex++);
                cell2.setCellStyle(textStyle);
                cell2.setCellValue(mm[2] == null ? "" : mm[2].toString());

                // 3 - subsidiary (String)
                Cell cell3 = row.createCell(colIndex++);
                cell3.setCellStyle(textStyle);
                cell3.setCellValue(mm[3] == null ? "" : mm[3].toString());

                // 4 - bankSymbol (String)
                Cell cell4 = row.createCell(colIndex++);
                cell4.setCellStyle(textStyle);
                cell4.setCellValue(mm[4] == null ? "" : mm[4].toString());

                // 5 - conventionalIslamic (String)
                Cell cell5 = row.createCell(colIndex++);
                cell5.setCellStyle(textStyle);
                cell5.setCellValue(mm[5] == null ? "" : mm[5].toString());

                // 6 - localForeign (String)
                Cell cell6 = row.createCell(colIndex++);
                cell6.setCellStyle(textStyle);
                cell6.setCellValue(mm[6] == null ? "" : mm[6].toString());

                // 7 - cbuAeTiering (String)
                Cell cell7 = row.createCell(colIndex++);
                cell7.setCellStyle(textStyle);
                cell7.setCellValue(mm[7] == null ? "" : mm[7].toString());

                // 8 - assetBalanceSheetSizeAed (Long)
                Cell cell8 = row.createCell(colIndex++);
                cell8.setCellStyle(numberStyle);
                cell8.setCellValue(mm[8] instanceof Number ? ((Number) mm[8]).doubleValue() : 0);

                // 9 - investmentBookSizeAed (Long)
                Cell cell9 = row.createCell(colIndex++);
                cell9.setCellStyle(numberStyle);
                cell9.setCellValue(mm[9] instanceof Number ? ((Number) mm[9]).doubleValue() : 0);

                // 10 - bookSizeTotalAssetsPct (Long)
                Cell cell10 = row.createCell(colIndex++);
                cell10.setCellStyle(numberStyle);
                cell10.setCellValue(mm[10] instanceof Number ? ((Number) mm[10]).doubleValue() : 0);

                // 11 - ytdNetPnlAed (Long)
                Cell cell11 = row.createCell(colIndex++);
                cell11.setCellStyle(numberStyle);
                cell11.setCellValue(mm[11] instanceof Number ? ((Number) mm[11]).doubleValue() : 0);

                // 12 - ytdFvtociUnrealizedLossAed (Long)
                Cell cell12 = row.createCell(colIndex++);
                cell12.setCellStyle(numberStyle);
                cell12.setCellValue(mm[12] instanceof Number ? ((Number) mm[12]).doubleValue() : 0);

                // 13 - cet1Aed (Long)
                Cell cell13 = row.createCell(colIndex++);
                cell13.setCellStyle(numberStyle);
                cell13.setCellValue(mm[13] instanceof Number ? ((Number) mm[13]).doubleValue() : 0);

                // 14 - uglCoreTier1RelativeImpactPct (Long)
                Cell cell14 = row.createCell(colIndex++);
                cell14.setCellStyle(numberStyle);
                cell14.setCellValue(mm[14] instanceof Number ? ((Number) mm[14]).doubleValue() : 0);
                
             // 15 - ytdAmortizedUnrealizedLossAed (Long)
                Cell cell15 = row.createCell(colIndex++);
                cell15.setCellStyle(numberStyle);
                cell15.setCellValue(mm[15] instanceof Number ? ((Number) mm[15]).doubleValue() : 0);

                // 16 - ytdHqlaAcUnrealizedLossAed (Long)
                Cell cell16 = row.createCell(colIndex++);
                cell16.setCellStyle(numberStyle);
                cell16.setCellValue(mm[16] instanceof Number ? ((Number) mm[16]).doubleValue() : 0);

                // 17 - hqlaAcCoreTier1ImpactPct (Long)
                Cell cell17 = row.createCell(colIndex++);
                cell17.setCellStyle(numberStyle);
                cell17.setCellValue(mm[17] instanceof Number ? ((Number) mm[17]).doubleValue() : 0);

                // 18 - irVarConfidenceInterval (String)
                Cell cell18 = row.createCell(colIndex++);
                cell18.setCellStyle(textStyle);
                cell18.setCellValue(mm[18] == null ? "" : mm[18].toString());

                // 19 - irVarHoldingPeriod (String)
                Cell cell19 = row.createCell(colIndex++);
                cell19.setCellStyle(textStyle);
                cell19.setCellValue(mm[19] == null ? "" : mm[19].toString());

                // 20 - irFixedIncomeExposureAed (Long)
                Cell cell20 = row.createCell(colIndex++);
                cell20.setCellStyle(numberStyle);
                cell20.setCellValue(mm[20] instanceof Number ? ((Number) mm[20]).doubleValue() : 0);

                // 21 - irVarExposureAed (Long)
                Cell cell21 = row.createCell(colIndex++);
                cell21.setCellStyle(numberStyle);
                cell21.setCellValue(mm[21] instanceof Number ? ((Number) mm[21]).doubleValue() : 0);

                // 22 - irVarLimitAed (Long)
                Cell cell22 = row.createCell(colIndex++);
                cell22.setCellStyle(numberStyle);
                cell22.setCellValue(mm[22] instanceof Number ? ((Number) mm[22]).doubleValue() : 0);

                // 23 - irStatus (String)
                Cell cell23 = row.createCell(colIndex++);
                cell23.setCellStyle(textStyle);
                cell23.setCellValue(mm[23] == null ? "" : mm[23].toString());

                // 24 - irVarImpactPct (Long)
                Cell cell24 = row.createCell(colIndex++);
                cell24.setCellStyle(numberStyle);
                cell24.setCellValue(mm[24] instanceof Number ? ((Number) mm[24]).doubleValue() : 0);

                // 25 - csVarConfidenceInterval (String)
                Cell cell25 = row.createCell(colIndex++);
                cell25.setCellStyle(textStyle);
                cell25.setCellValue(mm[25] == null ? "" : mm[25].toString());

                // 26 - csVarHoldingPeriod (String)
                Cell cell26 = row.createCell(colIndex++);
                cell26.setCellStyle(textStyle);
                cell26.setCellValue(mm[26] == null ? "" : mm[26].toString());

                // 27 - csFixedIncomeExposureAed (Long)
                Cell cell27 = row.createCell(colIndex++);
                cell27.setCellStyle(numberStyle);
                cell27.setCellValue(mm[27] instanceof Number ? ((Number) mm[27]).doubleValue() : 0);

                // 28 - csVarExposureAed (Long)
                Cell cell28 = row.createCell(colIndex++);
                cell28.setCellStyle(numberStyle);
                cell28.setCellValue(mm[28] instanceof Number ? ((Number) mm[28]).doubleValue() : 0);

                // 29 - csVarLimitAed (Long)
                Cell cell29 = row.createCell(colIndex++);
                cell29.setCellStyle(numberStyle);
                cell29.setCellValue(mm[29] instanceof Number ? ((Number) mm[29]).doubleValue() : 0);

                // 30 - csStatus (String)
                Cell cell30 = row.createCell(colIndex++);
                cell30.setCellStyle(textStyle);
                cell30.setCellValue(mm[30] == null ? "" : mm[30].toString());

                // 31 - csVarImpactPct (Long)
                Cell cell31 = row.createCell(colIndex++);
                cell31.setCellStyle(numberStyle);
                cell31.setCellValue(mm[31] instanceof Number ? ((Number) mm[31]).doubleValue() : 0);

                // 32 - fxVarConfidenceInterval (String)
                Cell cell32 = row.createCell(colIndex++);
                cell32.setCellStyle(textStyle);
                cell32.setCellValue(mm[32] == null ? "" : mm[32].toString());

                // 33 - fxVarHoldingPeriod (String)
                Cell cell33 = row.createCell(colIndex++);
                cell33.setCellStyle(textStyle);
                cell33.setCellValue(mm[33] == null ? "" : mm[33].toString());

                // 34 - fxExposureAed (Long)
                Cell cell34 = row.createCell(colIndex++);
                cell34.setCellStyle(numberStyle);
                cell34.setCellValue(mm[34] instanceof Number ? ((Number) mm[34]).doubleValue() : 0);

                // 35 - fxVarExposureAed (Long)
                Cell cell35 = row.createCell(colIndex++);
                cell35.setCellStyle(numberStyle);
                cell35.setCellValue(mm[35] instanceof Number ? ((Number) mm[35]).doubleValue() : 0);

                // 36 - fxVarLimitAed (Long)
                Cell cell36 = row.createCell(colIndex++);
                cell36.setCellStyle(numberStyle);
                cell36.setCellValue(mm[36] instanceof Number ? ((Number) mm[36]).doubleValue() : 0);

                // 37 - fxStatus (String)
                Cell cell37 = row.createCell(colIndex++);
                cell37.setCellStyle(textStyle);
                cell37.setCellValue(mm[37] == null ? "" : mm[37].toString());

                // 38 - fxVarImpactPct (Long)
                Cell cell38 = row.createCell(colIndex++);
                cell38.setCellStyle(numberStyle);
                cell38.setCellValue(mm[38] instanceof Number ? ((Number) mm[38]).doubleValue() : 0);

                // 39 - eqVarConfidenceInterval (String)
                Cell cell39 = row.createCell(colIndex++);
                cell39.setCellStyle(textStyle);
                cell39.setCellValue(mm[39] == null ? "" : mm[39].toString());

                // 40 - eqVarHoldingPeriod (String)
                Cell cell40 = row.createCell(colIndex++);
                cell40.setCellStyle(textStyle);
                cell40.setCellValue(mm[40] == null ? "" : mm[40].toString());

                // 41 - eqExposureAed (Long)
                Cell cell41 = row.createCell(colIndex++);
                cell41.setCellStyle(numberStyle);
                cell41.setCellValue(mm[41] instanceof Number ? ((Number) mm[41]).doubleValue() : 0);

                // 42 - eqVarExposureAed (Long)
                Cell cell42 = row.createCell(colIndex++);
                cell42.setCellStyle(numberStyle);
                cell42.setCellValue(mm[42] instanceof Number ? ((Number) mm[42]).doubleValue() : 0);

                // 43 - eqVarLimitAed (Long)
                Cell cell43 = row.createCell(colIndex++);
                cell43.setCellStyle(numberStyle);
                cell43.setCellValue(mm[43] instanceof Number ? ((Number) mm[43]).doubleValue() : 0);

                // 44 - eqStatus (String)
                Cell cell44 = row.createCell(colIndex++);
                cell44.setCellStyle(textStyle);
                cell44.setCellValue(mm[44] == null ? "" : mm[44].toString());
                
             // 45 - eqVarImpactPct (Long)
                Cell cell45 = row.createCell(colIndex++);
                cell45.setCellStyle(numberStyle);
                cell45.setCellValue(mm[45] instanceof Number ? ((Number) mm[45]).doubleValue() : 0);

                // 46 - commVarConfidenceInterval (String)
                Cell cell46 = row.createCell(colIndex++);
                cell46.setCellStyle(textStyle);
                cell46.setCellValue(mm[46] == null ? "" : mm[46].toString());

                // 47 - commVarHoldingPeriod (String)
                Cell cell47 = row.createCell(colIndex++);
                cell47.setCellStyle(textStyle);
                cell47.setCellValue(mm[47] == null ? "" : mm[47].toString());

                // 48 - commExposureAed (Long)
                Cell cell48 = row.createCell(colIndex++);
                cell48.setCellStyle(numberStyle);
                cell48.setCellValue(mm[48] instanceof Number ? ((Number) mm[48]).doubleValue() : 0);

                // 49 - commVarExposureAed (Long)
                Cell cell49 = row.createCell(colIndex++);
                cell49.setCellStyle(numberStyle);
                cell49.setCellValue(mm[49] instanceof Number ? ((Number) mm[49]).doubleValue() : 0);

                // 50 - commVarLimitAed (Long)
                Cell cell50 = row.createCell(colIndex++);
                cell50.setCellStyle(numberStyle);
                cell50.setCellValue(mm[50] instanceof Number ? ((Number) mm[50]).doubleValue() : 0);

                // 51 - commStatus (String)
                Cell cell51 = row.createCell(colIndex++);
                cell51.setCellStyle(textStyle);
                cell51.setCellValue(mm[51] == null ? "" : mm[51].toString());

                // 52 - commVarImpactPct (Long)
                Cell cell52 = row.createCell(colIndex++);
                cell52.setCellStyle(numberStyle);
                cell52.setCellValue(mm[52] instanceof Number ? ((Number) mm[52]).doubleValue() : 0);

                // 53 - overallVarConfidenceInterval (String)
                Cell cell53 = row.createCell(colIndex++);
                cell53.setCellStyle(textStyle);
                cell53.setCellValue(mm[53] == null ? "" : mm[53].toString());

                // 54 - overallVarHoldingPeriod (String)
                Cell cell54 = row.createCell(colIndex++);
                cell54.setCellStyle(textStyle);
                cell54.setCellValue(mm[54] == null ? "" : mm[54].toString());

                // 55 - varExposureAed (Long)
                Cell cell55 = row.createCell(colIndex++);
                cell55.setCellStyle(numberStyle);
                cell55.setCellValue(mm[55] instanceof Number ? ((Number) mm[55]).doubleValue() : 0);

                // 56 - varLimitAed (Long)
                Cell cell56 = row.createCell(colIndex++);
                cell56.setCellStyle(numberStyle);
                cell56.setCellValue(mm[56] instanceof Number ? ((Number) mm[56]).doubleValue() : 0);

                // 57 - overallVarStatus (String)
                Cell cell57 = row.createCell(colIndex++);
                cell57.setCellStyle(textStyle);
                cell57.setCellValue(mm[57] == null ? "" : mm[57].toString());

                // 58 - portfolioRelativeImpactPct (Long)
                Cell cell58 = row.createCell(colIndex++);
                cell58.setCellStyle(numberStyle);
                cell58.setCellValue(mm[58] instanceof Number ? ((Number) mm[58]).doubleValue() : 0);

                // 59 - expectedShortfallExposureAed (Long)
                Cell cell59 = row.createCell(colIndex++);
                cell59.setCellStyle(numberStyle);
                cell59.setCellValue(mm[59] instanceof Number ? ((Number) mm[59]).doubleValue() : 0);

                // 60 - expectedShortfallLimitAed (Long)
                Cell cell60 = row.createCell(colIndex++);
                cell60.setCellStyle(numberStyle);
                cell60.setCellValue(mm[60] instanceof Number ? ((Number) mm[60]).doubleValue() : 0);

                // 61 - expectedShortfallStatus (String)
                Cell cell61 = row.createCell(colIndex++);
                cell61.setCellStyle(textStyle);
                cell61.setCellValue(mm[61] == null ? "" : mm[61].toString());

                // 62 - expectedShortfallImpactPct (Long)
                Cell cell62 = row.createCell(colIndex++);
                cell62.setCellStyle(numberStyle);
                cell62.setCellValue(mm[62] instanceof Number ? ((Number) mm[62]).doubleValue() : 0);

                // 63 - modifiedDuration (Long)
                Cell cell63 = row.createCell(colIndex++);
                cell63.setCellStyle(numberStyle);
                cell63.setCellValue(mm[63] instanceof Number ? ((Number) mm[63]).doubleValue() : 0);

                // 64 - interestRateShockBps (Integer)
                Cell cell64 = row.createCell(colIndex++);
                cell64.setCellStyle(numberStyle);
                cell64.setCellValue(mm[64] instanceof Number ? ((Number) mm[64]).doubleValue() : 0);

                // 65 - interestRateLimit (Long)
                Cell cell65 = row.createCell(colIndex++);
                cell65.setCellStyle(numberStyle);
                cell65.setCellValue(mm[65] instanceof Number ? ((Number) mm[65]).doubleValue() : 0);

                // 66 - interestRateStatus (String)
                Cell cell66 = row.createCell(colIndex++);
                cell66.setCellStyle(textStyle);
                cell66.setCellValue(mm[66] == null ? "" : mm[66].toString());

                // 67 - dv01Aed (Long)
                Cell cell67 = row.createCell(colIndex++);
                cell67.setCellStyle(numberStyle);
                cell67.setCellValue(mm[67] instanceof Number ? ((Number) mm[67]).doubleValue() : 0);

                // 68 - dv01LimitAed (Long)
                Cell cell68 = row.createCell(colIndex++);
                cell68.setCellStyle(numberStyle);
                cell68.setCellValue(mm[68] instanceof Number ? ((Number) mm[68]).doubleValue() : 0);

                // 69 - dv01Status (String)
                Cell cell69 = row.createCell(colIndex++);
                cell69.setCellStyle(textStyle);
                cell69.setCellValue(mm[69] == null ? "" : mm[69].toString());

                // 70 - dv01TenorGapLt3Aed (Long)
                Cell cell70 = row.createCell(colIndex++);
                cell70.setCellStyle(numberStyle);
                cell70.setCellValue(mm[70] instanceof Number ? ((Number) mm[70]).doubleValue() : 0);

                // 71 - dv01TenorGapGt3Aed (Long)
                Cell cell71 = row.createCell(colIndex++);
                cell71.setCellStyle(numberStyle);
                cell71.setCellValue(mm[71] instanceof Number ? ((Number) mm[71]).doubleValue() : 0);

                // 72 - dv01Check (String)
                Cell cell72 = row.createCell(colIndex++);
                cell72.setCellStyle(textStyle);
                cell72.setCellValue(mm[72] == null ? "" : mm[72].toString());
                
             // 73 - cs01Aed (Long)
                Cell cell73 = row.createCell(colIndex++);
                cell73.setCellStyle(numberStyle);
                cell73.setCellValue(mm[73] instanceof Number ? ((Number) mm[73]).doubleValue() : 0);

                // 74 - cs01ShockBps (Integer)
                Cell cell74 = row.createCell(colIndex++);
                cell74.setCellStyle(numberStyle);
                cell74.setCellValue(mm[74] instanceof Number ? ((Number) mm[74]).doubleValue() : 0);

                // 75 - cs01Limit (Long)
                Cell cell75 = row.createCell(colIndex++);
                cell75.setCellStyle(numberStyle);
                cell75.setCellValue(mm[75] instanceof Number ? ((Number) mm[75]).doubleValue() : 0);

                // 76 - cs01Status (String)
                Cell cell76 = row.createCell(colIndex++);
                cell76.setCellStyle(textStyle);
                cell76.setCellValue(mm[76] == null ? "" : mm[76].toString());

                // 77 - cs01InvestmentGradeAed (Long)
                Cell cell77 = row.createCell(colIndex++);
                cell77.setCellStyle(numberStyle);
                cell77.setCellValue(mm[77] instanceof Number ? ((Number) mm[77]).doubleValue() : 0);

                // 78 - cs01SubInvestmentAed (Long)
                Cell cell78 = row.createCell(colIndex++);
                cell78.setCellStyle(numberStyle);
                cell78.setCellValue(mm[78] instanceof Number ? ((Number) mm[78]).doubleValue() : 0);

                // 79 - cs01UnratedAed (Long)
                Cell cell79 = row.createCell(colIndex++);
                cell79.setCellStyle(numberStyle);
                cell79.setCellValue(mm[79] instanceof Number ? ((Number) mm[79]).doubleValue() : 0);

                // 80 - cs01Check (String)
                Cell cell80 = row.createCell(colIndex++);
                cell80.setCellStyle(textStyle);
                cell80.setCellValue(mm[80] == null ? "" : mm[80].toString());

                // 81 - betaEquity (Long)
                Cell cell81 = row.createCell(colIndex++);
                cell81.setCellStyle(numberStyle);
                cell81.setCellValue(mm[81] instanceof Number ? ((Number) mm[81]).doubleValue() : 0);

                // 82 - equityShockBps (Integer)
                Cell cell82 = row.createCell(colIndex++);
                cell82.setCellStyle(numberStyle);
                cell82.setCellValue(mm[82] instanceof Number ? ((Number) mm[82]).doubleValue() : 0);

                // 83 - equityLimit (Long)
                Cell cell83 = row.createCell(colIndex++);
                cell83.setCellStyle(numberStyle);
                cell83.setCellValue(mm[83] instanceof Number ? ((Number) mm[83]).doubleValue() : 0);

                // 84 - equityStatus (String)
                Cell cell84 = row.createCell(colIndex++);
                cell84.setCellStyle(textStyle);
                cell84.setCellValue(mm[84] == null ? "" : mm[84].toString());

                // 85 - creditSpreadImpactAed (Long)
                Cell cell85 = row.createCell(colIndex++);
                cell85.setCellStyle(numberStyle);
                cell85.setCellValue(mm[85] instanceof Number ? ((Number) mm[85]).doubleValue() : 0);

                // 86 - creditSensitiveImpactPct (Long)
                Cell cell86 = row.createCell(colIndex++);
                cell86.setCellStyle(numberStyle);
                cell86.setCellValue(mm[86] instanceof Number ? ((Number) mm[86]).doubleValue() : 0);

                // 87 - interestRateImpactAed (Long)
                Cell cell87 = row.createCell(colIndex++);
                cell87.setCellStyle(numberStyle);
                cell87.setCellValue(mm[87] instanceof Number ? ((Number) mm[87]).doubleValue() : 0);

                // 88 - rateSensitiveImpactPct (Long)
                Cell cell88 = row.createCell(colIndex++);
                cell88.setCellStyle(numberStyle);
                cell88.setCellValue(mm[88] instanceof Number ? ((Number) mm[88]).doubleValue() : 0);

                // 89 - fxImpactAed (Long)
                Cell cell89 = row.createCell(colIndex++);
                cell89.setCellStyle(numberStyle);
                cell89.setCellValue(mm[89] instanceof Number ? ((Number) mm[89]).doubleValue() : 0);

                // 90 - fxSensitiveImpactPct (Long)
                Cell cell90 = row.createCell(colIndex++);
                cell90.setCellStyle(numberStyle);
                cell90.setCellValue(mm[90] instanceof Number ? ((Number) mm[90]).doubleValue() : 0);

                // 91 - equityImpactAed (Long)
                Cell cell91 = row.createCell(colIndex++);
                cell91.setCellStyle(numberStyle);
                cell91.setCellValue(mm[91] instanceof Number ? ((Number) mm[91]).doubleValue() : 0);

                // 92 - equitySensitiveImpactPct (Long)
                Cell cell92 = row.createCell(colIndex++);
                cell92.setCellStyle(numberStyle);
                cell92.setCellValue(mm[92] instanceof Number ? ((Number) mm[92]).doubleValue() : 0);

                // 93 - commoditiesImpactAed (Long)
                Cell cell93 = row.createCell(colIndex++);
                cell93.setCellStyle(numberStyle);
                cell93.setCellValue(mm[93] instanceof Number ? ((Number) mm[93]).doubleValue() : 0);

                // 94 - commoditySensitiveImpactPct (Long)
                Cell cell94 = row.createCell(colIndex++);
                cell94.setCellStyle(numberStyle);
                cell94.setCellValue(mm[94] instanceof Number ? ((Number) mm[94]).doubleValue() : 0);

                // 95 - jtdLossImpactAed (Long)
                Cell cell95 = row.createCell(colIndex++);
                cell95.setCellStyle(numberStyle);
                cell95.setCellValue(mm[95] instanceof Number ? ((Number) mm[95]).doubleValue() : 0);

                // 96 - jtdRelativeImpactPct (Long)
                Cell cell96 = row.createCell(colIndex++);
                cell96.setCellStyle(numberStyle);
                cell96.setCellValue(mm[96] instanceof Number ? ((Number) mm[96]).doubleValue() : 0);

                // 97 - overallImpactAed (Long)
                Cell cell97 = row.createCell(colIndex++);
                cell97.setCellStyle(numberStyle);
                cell97.setCellValue(mm[97] instanceof Number ? ((Number) mm[97]).doubleValue() : 0);

                // 98 - coreTier1RelativeImpactPct (Long)
                Cell cell98 = row.createCell(colIndex++);
                cell98.setCellStyle(numberStyle);
                cell98.setCellValue(mm[98] instanceof Number ? ((Number) mm[98]).doubleValue() : 0);
            }
         // Auto-size all columns
			for (int i = 0; i <= 98; i++) {
			    sheet.autoSizeColumn(i);
			}

            workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
            workbook.write(out);

            String finalPath = env.getProperty("output.exportpathfinal"); // e.g. finaltemp path
            File outputFile = new File(finalPath + "CBUAE_Investment Risk Data_Dashboard_Template.xls");
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(out.toByteArray());
                logger.info("Service: Excel also saved to file: {}", outputFile.getAbsolutePath());
            }

            logger.info("Service: INVESTMENT RISK DATA Excel data successfully written to memory buffer ({} bytes).", out.size());
            return out.toByteArray();
        }
    }

    

    
}
