package com.bornfire.xbrl.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.ss.usermodel.BorderStyle;
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

import com.bornfire.xbrl.entities.RT_TreasuryCreditEntity;
import com.bornfire.xbrl.entities.RT_TreasuryCreditRepo;

@Service
public class RT_TreasuryCredit_Service {

	private static final Logger logger = LoggerFactory.getLogger(RT_TreasuryCredit_Service.class);
	
    @Autowired
    RT_TreasuryCreditRepo treasuryRepo;

    @Autowired
    private Environment env;

    // Update existing record
    public boolean updateTreasuryCredit(RT_TreasuryCreditEntity updatedEntity) {
        Optional<RT_TreasuryCreditEntity> existingOpt = treasuryRepo.findById(updatedEntity.getSlNo());

        if (existingOpt.isPresent()) {
            RT_TreasuryCreditEntity existing = existingOpt.get();
            // Basic Information
            existing.setReportDate(updatedEntity.getReportDate());
            existing.setBankName(updatedEntity.getBankName());
            existing.setHeadOfficeSubsidiary(updatedEntity.getHeadOfficeSubsidiary());
            existing.setSubsidiary(updatedEntity.getSubsidiary());
            existing.setBankSymbol(updatedEntity.getBankSymbol());
            existing.setConventionalIslamic(updatedEntity.getConventionalIslamic());
            existing.setLocalForeign(updatedEntity.getLocalForeign());
            existing.setCbuaeTiering(updatedEntity.getCbuaeTiering());
            // Counterparty Info
            existing.setCounterpartyName(updatedEntity.getCounterpartyName());
            existing.setCounterpartyIntRef(updatedEntity.getCounterpartyIntRef());
            existing.setCounterpartyRiskRating(updatedEntity.getCounterpartyRiskRating());
            existing.setFinalRatingCbuae(updatedEntity.getFinalRatingCbuae());
            existing.setCountryOfRisk(updatedEntity.getCountryOfRisk());
            existing.setCbuaeGeographicalZone(updatedEntity.getCbuaeGeographicalZone());
            existing.setCounterpartyType(updatedEntity.getCounterpartyType());

            // MM
            existing.setLimitAedMoneymarket(updatedEntity.getLimitAedMoneymarket());
            existing.setUtilizationAedMoneymarket(updatedEntity.getUtilizationAedMoneymarket());
            existing.setMoneymarketPercent(updatedEntity.getMoneymarketPercent());

            // Repo
            existing.setLimitAedRepo(updatedEntity.getLimitAedRepo());
            existing.setUtilizationAedRepo(updatedEntity.getUtilizationAedRepo());
            existing.setRepoPercent(updatedEntity.getRepoPercent());

            // Bonds
            existing.setLimitAedBonds(updatedEntity.getLimitAedBonds());
            existing.setUtilizationAedBonds(updatedEntity.getUtilizationAedBonds());
            existing.setBondsPercent(updatedEntity.getBondsPercent());

            // Credit
            existing.setLimitAedCredit(updatedEntity.getLimitAedCredit());
            existing.setUtilizationAedCredit(updatedEntity.getUtilizationAedCredit());
            existing.setCreditPercent(updatedEntity.getCreditPercent());

            // Other
            existing.setLimitAedOther(updatedEntity.getLimitAedOther());
            existing.setUtilizationAedOther(updatedEntity.getUtilizationAedOther());
            existing.setOtherPercent(updatedEntity.getOtherPercent());

            // Nostro
            existing.setLimitAedNostro(updatedEntity.getLimitAedNostro());
            existing.setUtilizationAedNostro(updatedEntity.getUtilizationAedNostro());
            existing.setNostroPercent(updatedEntity.getNostroPercent());

            // Derivatives
            existing.setLimitAedDerivatives(updatedEntity.getLimitAedDerivatives());
            existing.setUtilizationAedDerivatives(updatedEntity.getUtilizationAedDerivatives());
            existing.setDerivativesPercent(updatedEntity.getDerivativesPercent());

            // FX
            existing.setLimitAedFxsettlement(updatedEntity.getLimitAedFxsettlement());
            existing.setUtilizationAedFxsettlement(updatedEntity.getUtilizationAedFxsettlement());
            existing.setFxsettlementPercent(updatedEntity.getFxsettlementPercent());

            // Bond Settlement
            existing.setLimitAedBondsettlement(updatedEntity.getLimitAedBondsettlement());
            existing.setUtilizationAedBondsettlement(updatedEntity.getUtilizationAedBondsettlement());
            existing.setBondsettlementPercent(updatedEntity.getBondsettlementPercent());

            // Overall Treasury
            existing.setTreasuryLmtAed(updatedEntity.getTreasuryLmtAed());
            existing.setTreasuryLmt(updatedEntity.getTreasuryLmt());

            // Exposure
            existing.setExposureAed(updatedEntity.getExposureAed());
            existing.setExposure(updatedEntity.getExposure());

            // Final details
            existing.setReportSubmitDate(updatedEntity.getReportSubmitDate());
            existing.setModifyTime(new Date());// track update time

            treasuryRepo.save(existing);
            return true;
        } else {
            return false;
        }
    }

    // Export Excel from DB data   
	/*
	 * public File generateTreasuryExcel() { File outputFile = null;
	 */
        

        public byte[] generateTreasuryExcel() throws Exception {
            logger.info("Service: Starting Treasury Credit Excel generation process in memory.");

            List<RT_TreasuryCreditEntity> dataList = treasuryRepo.getTClist();

            if (dataList.isEmpty()) {
                logger.warn("Service: No data found. Returning empty byte array.");
                return new byte[0];
            }

            String templateDir = env.getProperty("output.exportpathtemp");
            String templateFileName = "CBUAE_Treasury_Credit_Limit_Management_Data_Template.xls";
            Path templatePath = Paths.get(templateDir, templateFileName);

            logger.info("Service: Template path - {}", templatePath.toAbsolutePath());

            if (!Files.exists(templatePath)) {
                throw new FileNotFoundException("Template not found at: " + templatePath.toAbsolutePath());
            }
            if (!Files.isReadable(templatePath)) {
                throw new SecurityException("Template not readable: " + templatePath.toAbsolutePath());
            }

    		try (InputStream templateInputStream = Files.newInputStream(templatePath);
    				Workbook workbook = WorkbookFactory.create(templateInputStream);
    				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

    			Sheet sheet = workbook.getSheetAt(0);

    			CreationHelper createHelper = workbook.getCreationHelper();

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

    			CellStyle percentStyle = workbook.createCellStyle();
    			percentStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.00%"));
    			percentStyle.setBorderBottom(BorderStyle.THIN);
    			percentStyle.setBorderTop(BorderStyle.THIN);
    			percentStyle.setBorderLeft(BorderStyle.THIN);
    			percentStyle.setBorderRight(BorderStyle.THIN);

    			int startRow = 3;

    			for (int i = 0; i < dataList.size(); i++) {
    			    RT_TreasuryCreditEntity record = dataList.get(i);
    			    Row row = sheet.getRow(startRow + i);
    			    if (row == null) row = sheet.createRow(startRow + i);

    			    int col = 0;

    			    // 0 - REPORT_DATE
    			    Cell cell0 = row.createCell(col++);
    			    if (record.getReportDate() != null) {
    			        cell0.setCellValue(record.getReportDate());
    			        cell0.setCellStyle(dateStyle);
    			    } else {
    			        cell0.setCellValue("");
    			        cell0.setCellStyle(textStyle);
    			    }

    			    // 1 - BANK_NAME
    			    Cell cell1 = row.createCell(col++);
    			    cell1.setCellValue(record.getBankName() != null ? record.getBankName() : "");
    			    cell1.setCellStyle(textStyle);

    			    // 2 - HEAD_OFFICE_SUBSIDIARY
    			    Cell cell2 = row.createCell(col++);
    			    cell2.setCellValue(record.getHeadOfficeSubsidiary() != null ? record.getHeadOfficeSubsidiary() : "");
    			    cell2.setCellStyle(textStyle);

    			    // 3 - SUBSIDIARY
    			    Cell cell3 = row.createCell(col++);
    			    cell3.setCellValue(record.getSubsidiary() != null ? record.getSubsidiary() : "");
    			    cell3.setCellStyle(textStyle);

    			    // 4 - BANK_SYMBOL
    			    Cell cell4 = row.createCell(col++);
    			    cell4.setCellValue(record.getBankSymbol() != null ? record.getBankSymbol() : "");
    			    cell4.setCellStyle(textStyle);

    			    // 5 - CONVENTIONAL_ISLAMIC
    			    Cell cell5 = row.createCell(col++);
    			    cell5.setCellValue(record.getConventionalIslamic() != null ? record.getConventionalIslamic() : "");
    			    cell5.setCellStyle(textStyle);

    			    // 6 - LOCAL_FOREIGN
    			    Cell cell6 = row.createCell(col++);
    			    cell6.setCellValue(record.getLocalForeign() != null ? record.getLocalForeign() : "");
    			    cell6.setCellStyle(textStyle);

    			    // 7 - CBUAE_TIERING
    			    Cell cell7 = row.createCell(col++);
    			    cell7.setCellValue(record.getCbuaeTiering() != null ? record.getCbuaeTiering() : "");
    			    cell7.setCellStyle(textStyle);

    			    // 8 - COUNTERPARTY_NAME
    			    Cell cell8 = row.createCell(col++);
    			    cell8.setCellValue(record.getCounterpartyName() != null ? record.getCounterpartyName() : "");
    			    cell8.setCellStyle(textStyle);

    			    // 9 - COUNTERPARTY_INT_REF
    			    Cell cell9 = row.createCell(col++);
    			    cell9.setCellValue(record.getCounterpartyIntRef() != null ? record.getCounterpartyIntRef() : "");
    			    cell9.setCellStyle(textStyle);

    			    // 10 - COUNTERPARTY_RISK_RATING
    			    Cell cell10 = row.createCell(col++);
    			    cell10.setCellValue(record.getCounterpartyRiskRating() != null ? record.getCounterpartyRiskRating() : "");
    			    cell10.setCellStyle(textStyle);

    			    // 11 - FINAL_RATING_CBUAE
    			    Cell cell11 = row.createCell(col++);
    			    cell11.setCellValue(record.getFinalRatingCbuae() != null ? record.getFinalRatingCbuae() : "");
    			    cell11.setCellStyle(textStyle);

    			    // 12 - COUNTRY_OF_RISK
    			    Cell cell12 = row.createCell(col++);
    			    cell12.setCellValue(record.getCountryOfRisk() != null ? record.getCountryOfRisk() : "");
    			    cell12.setCellStyle(textStyle);

    			    // 13 - CBUAE_GEOGRAPHICAL_ZONE
    			    Cell cell13 = row.createCell(col++);
    			    cell13.setCellValue(record.getCbuaeGeographicalZone() != null ? record.getCbuaeGeographicalZone() : "");
    			    cell13.setCellStyle(textStyle);

    			    // 14 - COUNTERPARTY_TYPE
    			    Cell cell14 = row.createCell(col++);
    			    cell14.setCellValue(record.getCounterpartyType() != null ? record.getCounterpartyType() : "");
    			    cell14.setCellStyle(textStyle);

    			    // 15 onwards - BigDecimal columns (style: numberStyle or percentStyle as needed)
    			    Cell cell15 = row.createCell(col++);
    			    cell15.setCellValue(record.getLimitAedMoneymarket() != null ? record.getLimitAedMoneymarket().doubleValue() : 0.0);
    			    cell15.setCellStyle(numberStyle);

    			    Cell cell16 = row.createCell(col++);
    			    cell16.setCellValue(record.getUtilizationAedMoneymarket() != null ? record.getUtilizationAedMoneymarket().doubleValue() : 0.0);
    			    cell16.setCellStyle(numberStyle);

    			    Cell cell17 = row.createCell(col++);
    			    cell17.setCellValue(record.getMoneymarketPercent() != null ? record.getMoneymarketPercent().doubleValue() : 0.0);
    			    cell17.setCellStyle(percentStyle);

    			    Cell cell18 = row.createCell(col++);
    			    cell18.setCellValue(record.getLimitAedRepo() != null ? record.getLimitAedRepo().doubleValue() : 0.0);
    			    cell18.setCellStyle(numberStyle);

    			    Cell cell19 = row.createCell(col++);
    			    cell19.setCellValue(record.getUtilizationAedRepo() != null ? record.getUtilizationAedRepo().doubleValue() : 0.0);
    			    cell19.setCellStyle(numberStyle);

    			    Cell cell20 = row.createCell(col++);
    			    cell20.setCellValue(record.getRepoPercent() != null ? record.getRepoPercent().doubleValue() : 0.0);
    			    cell20.setCellStyle(percentStyle);

    			    Cell cell21 = row.createCell(col++);
    			    cell21.setCellValue(record.getLimitAedBonds() != null ? record.getLimitAedBonds().doubleValue() : 0.0);
    			    cell21.setCellStyle(numberStyle);

    			    Cell cell22 = row.createCell(col++);
    			    cell22.setCellValue(record.getUtilizationAedBonds() != null ? record.getUtilizationAedBonds().doubleValue() : 0.0);
    			    cell22.setCellStyle(numberStyle);

    			    Cell cell23 = row.createCell(col++);
    			    cell23.setCellValue(record.getBondsPercent() != null ? record.getBondsPercent().doubleValue() : 0.0);
    			    cell23.setCellStyle(percentStyle);

    			    Cell cell24 = row.createCell(col++);
    			    cell24.setCellValue(record.getLimitAedEquity() != null ? record.getLimitAedEquity().doubleValue() : 0.0);
    			    cell24.setCellStyle(numberStyle);

    			    Cell cell25 = row.createCell(col++);
    			    cell25.setCellValue(record.getUtilizationAedEquity() != null ? record.getUtilizationAedEquity().doubleValue() : 0.0);
    			    cell25.setCellStyle(numberStyle);

    			    Cell cell26 = row.createCell(col++);
    			    cell26.setCellValue(record.getEquityPercent() != null ? record.getEquityPercent().doubleValue() : 0.0);
    			    cell26.setCellStyle(percentStyle);

    			    Cell cell27 = row.createCell(col++);
    			    cell27.setCellValue(record.getLimitAedCredit() != null ? record.getLimitAedCredit().doubleValue() : 0.0);
    			    cell27.setCellStyle(numberStyle);

    			    Cell cell28 = row.createCell(col++);
    			    cell28.setCellValue(record.getUtilizationAedCredit() != null ? record.getUtilizationAedCredit().doubleValue() : 0.0);
    			    cell28.setCellStyle(numberStyle);

    			    Cell cell29 = row.createCell(col++);
    			    cell29.setCellValue(record.getCreditPercent() != null ? record.getCreditPercent().doubleValue() : 0.0);
    			    cell29.setCellStyle(percentStyle);

    			    Cell cell30 = row.createCell(col++);
    			    cell30.setCellValue(record.getLimitAedOther() != null ? record.getLimitAedOther().doubleValue() : 0.0);
    			    cell30.setCellStyle(numberStyle);

    			    Cell cell31 = row.createCell(col++);
    			    cell31.setCellValue(record.getUtilizationAedOther() != null ? record.getUtilizationAedOther().doubleValue() : 0.0);
    			    cell31.setCellStyle(numberStyle);

    			    Cell cell32 = row.createCell(col++);
    			    cell32.setCellValue(record.getOtherPercent() != null ? record.getOtherPercent().doubleValue() : 0.0);
    			    cell32.setCellStyle(percentStyle);

    			    Cell cell33 = row.createCell(col++);
    			    cell33.setCellValue(record.getLimitAedNostro() != null ? record.getLimitAedNostro().doubleValue() : 0.0);
    			    cell33.setCellStyle(numberStyle);

    			    Cell cell34 = row.createCell(col++);
    			    cell34.setCellValue(record.getUtilizationAedNostro() != null ? record.getUtilizationAedNostro().doubleValue() : 0.0);
    			    cell34.setCellStyle(numberStyle);

    			    Cell cell35 = row.createCell(col++);
    			    cell35.setCellValue(record.getNostroPercent() != null ? record.getNostroPercent().doubleValue() : 0.0);
    			    cell35.setCellStyle(percentStyle);

    			    Cell cell36 = row.createCell(col++);
    			    cell36.setCellValue(record.getLimitAedDerivatives() != null ? record.getLimitAedDerivatives().doubleValue() : 0.0);
    			    cell36.setCellStyle(numberStyle);

    			    Cell cell37 = row.createCell(col++);
    			    cell37.setCellValue(record.getUtilizationAedDerivatives() != null ? record.getUtilizationAedDerivatives().doubleValue() : 0.0);
    			    cell37.setCellStyle(numberStyle);

    			    Cell cell38 = row.createCell(col++);
    			    cell38.setCellValue(record.getDerivativesPercent() != null ? record.getDerivativesPercent().doubleValue() : 0.0);
    			    cell38.setCellStyle(percentStyle);

    			    Cell cell39 = row.createCell(col++);
    			    cell39.setCellValue(record.getLimitAedFxsettlement() != null ? record.getLimitAedFxsettlement().doubleValue() : 0.0);
    			    cell39.setCellStyle(numberStyle);

    			    Cell cell40 = row.createCell(col++);
    			    cell40.setCellValue(record.getUtilizationAedFxsettlement() != null ? record.getUtilizationAedFxsettlement().doubleValue() : 0.0);
    			    cell40.setCellStyle(numberStyle);

    			    Cell cell41 = row.createCell(col++);
    			    cell41.setCellValue(record.getFxsettlementPercent() != null ? record.getFxsettlementPercent().doubleValue() : 0.0);
    			    cell41.setCellStyle(percentStyle);

    			    Cell cell42 = row.createCell(col++);
    			    cell42.setCellValue(record.getLimitAedBondsettlement() != null ? record.getLimitAedBondsettlement().doubleValue() : 0.0);
    			    cell42.setCellStyle(numberStyle);

    			    Cell cell43 = row.createCell(col++);
    			    cell43.setCellValue(record.getUtilizationAedBondsettlement() != null ? record.getUtilizationAedBondsettlement().doubleValue() : 0.0);
    			    cell43.setCellStyle(numberStyle);

    			    Cell cell44 = row.createCell(col++);
    			    cell44.setCellValue(record.getBondsettlementPercent() != null ? record.getBondsettlementPercent().doubleValue() : 0.0);
    			    cell44.setCellStyle(percentStyle);

    			    Cell cell45 = row.createCell(col++);
    			    cell45.setCellValue(record.getTreasuryLmtAed() != null ? record.getTreasuryLmtAed().doubleValue() : 0.0);
    			    cell45.setCellStyle(numberStyle);

    			    Cell cell46 = row.createCell(col++);
    			    cell46.setCellValue(record.getTreasuryLmt() != null ? record.getTreasuryLmt().doubleValue() : 0.0);
    			    cell46.setCellStyle(numberStyle);

    			    Cell cell47 = row.createCell(col++);
    			    cell47.setCellValue(record.getExposureAed() != null ? record.getExposureAed().doubleValue() : 0.0);
    			    cell47.setCellStyle(numberStyle);

    			    Cell cell48 = row.createCell(col++);
    			    cell48.setCellValue(record.getExposure() != null ? record.getExposure().doubleValue() : 0.0);
    			    cell48.setCellStyle(percentStyle);
    			}

    			int[] percentCols = {17, 20, 23, 26, 29, 32, 35, 38, 41, 44 ,48};
    			for (int col : percentCols) {
    			    sheet.autoSizeColumn(col);
    			}
                workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
                workbook.write(out);

				/*
				 * logger.info("Service: Excel file written successfully. Size: {} bytes",
				 * out.size()); return out.toByteArray();
				 */

                String finalPath = env.getProperty("output.exportpathfinal"); // e.g. finaltemp path
                File outputFile = new File(finalPath + "CBUAE_Treasury_Credit_Limit_Management_Data_Template.xls");
                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    fos.write(out.toByteArray());
                    logger.info("Service: Excel also saved to file: {}", outputFile.getAbsolutePath());
                }

                logger.info("Service: Treasury Credit Excel data successfully written to memory buffer ({} bytes).", out.size());
                return out.toByteArray();

            }
        }


}
