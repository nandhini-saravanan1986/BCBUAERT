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

import com.bornfire.xbrl.entities.RT_TreasuryCreditEntity;
import com.bornfire.xbrl.entities.RT_TreasuryCreditRepo;

@Service
public class RT_TreasuryCredit_Service {

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
    public File generateTreasuryExcel() {
        File outputFile = null;

        try {
            // Fetch data from repository
            List<Object[]> treasuryList = treasuryRepo.getTreasuryData();

            // Load Excel template file
            File templateFile = new File(env.getProperty("output.exportpathtemp") + "CBUAE_Treasury_Credit_Limit_Management_Data_Template.xls");
            Workbook workbook = WorkbookFactory.create(new FileInputStream(templateFile));
            Sheet sheet = workbook.getSheetAt(0);

            // Create date cell style
            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

            int startRow = 3;

            if (!treasuryList.isEmpty()) {
                for (int i = 0; i < treasuryList.size(); i++) {
                    Object[] data = treasuryList.get(i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null)
                        row = sheet.createRow(startRow + i);

                    Cell cell1 = row.getCell(1);
                    if (cell1 == null) cell1 = row.createCell(1);
                    if (data[1] instanceof Date) {
                        cell1.setCellValue((Date) data[1]);
                        cell1.setCellStyle(dateStyle);
                    } else {
                        cell1.setCellValue("");
                    }

                    Cell cell2 = row.getCell(2);
                    if (cell2 == null) cell2 = row.createCell(2);
                    cell2.setCellValue(data[2] == null ? "" : data[2].toString());

                    Cell cell3 = row.getCell(3);
                    if (cell3 == null) cell3 = row.createCell(3);
                    cell3.setCellValue(data[3] == null ? "" : data[3].toString());

                    Cell cell4 = row.getCell(4);
                    if (cell4 == null) cell4 = row.createCell(4);
                    cell4.setCellValue(data[4] == null ? "" : data[4].toString());

                    Cell cell5 = row.getCell(5);
                    if (cell5 == null) cell5 = row.createCell(5);
                    cell5.setCellValue(data[5] == null ? "" : data[5].toString());

                    Cell cell6 = row.getCell(6);
                    if (cell6 == null) cell6 = row.createCell(6);
                    cell6.setCellValue(data[6] == null ? "" : data[6].toString());

                    Cell cell7 = row.getCell(7);
                    if (cell7 == null) cell7 = row.createCell(7);
                    cell7.setCellValue(data[7] == null ? "" : data[7].toString());

                    Cell cell8 = row.getCell(8);
                    if (cell8 == null) cell8 = row.createCell(8);
                    cell8.setCellValue(data[8] == null ? "" : data[8].toString());

                    Cell cell9 = row.getCell(9);
                    if (cell9 == null) cell9 = row.createCell(9);
                    cell9.setCellValue(data[9] == null ? "" : data[9].toString());

                    Cell cell10 = row.getCell(10);
                    if (cell10 == null) cell10 = row.createCell(10);
                    cell10.setCellValue(data[10] == null ? "" : data[10].toString());

                    Cell cell11 = row.getCell(11);
                    if (cell11 == null) cell11 = row.createCell(11);
                    cell11.setCellValue(data[11] == null ? "" : data[11].toString());

                    Cell cell12 = row.getCell(12);
                    if (cell12 == null) cell12 = row.createCell(12);
                    cell12.setCellValue(data[12] == null ? "" : data[12].toString());

                    Cell cell13 = row.getCell(13);
                    if (cell13 == null) cell13 = row.createCell(13);
                    cell13.setCellValue(data[13] == null ? "" : data[13].toString());

                    Cell cell14 = row.getCell(14);
                    if (cell14 == null) cell14 = row.createCell(14);
                    cell14.setCellValue(data[14] == null ? "" : data[14].toString());

                    Cell cell15 = row.getCell(15);
                    if (cell15 == null) cell15 = row.createCell(15);
                    cell15.setCellValue(data[15] == null ? "" : data[15].toString());

                    Cell cell16 = row.getCell(16);
                    if (cell16 == null) cell16 = row.createCell(16);
                    cell16.setCellValue(data[16] instanceof BigDecimal ? ((BigDecimal) data[16]).doubleValue() : 0);

                    Cell cell17 = row.getCell(17);
                    if (cell17 == null) cell17 = row.createCell(17);
                    cell17.setCellValue(data[17] instanceof BigDecimal ? ((BigDecimal) data[17]).doubleValue() : 0);

                    Cell cell18 = row.getCell(18);
                    if (cell18 == null) cell18 = row.createCell(18);
                    cell18.setCellValue(data[18] instanceof BigDecimal ? ((BigDecimal) data[18]).doubleValue() : 0);

                    Cell cell19 = row.getCell(19);
                    if (cell19 == null) cell19 = row.createCell(19);
                    cell19.setCellValue(data[19] instanceof BigDecimal ? ((BigDecimal) data[19]).doubleValue() : 0);

                    Cell cell20 = row.getCell(20);
                    if (cell20 == null) cell20 = row.createCell(20);
                    cell20.setCellValue(data[20] instanceof BigDecimal ? ((BigDecimal) data[20]).doubleValue() : 0);

                    Cell cell21 = row.getCell(21);
                    if (cell21 == null) cell21 = row.createCell(21);
                    cell21.setCellValue(data[21] instanceof BigDecimal ? ((BigDecimal) data[21]).doubleValue() : 0);

                    Cell cell22 = row.getCell(22);
                    if (cell22 == null) cell22 = row.createCell(22);
                    cell22.setCellValue(data[22] instanceof BigDecimal ? ((BigDecimal) data[22]).doubleValue() : 0);

                    Cell cell23 = row.getCell(23);
                    if (cell23 == null) cell23 = row.createCell(23);
                    cell23.setCellValue(data[23] instanceof BigDecimal ? ((BigDecimal) data[23]).doubleValue() : 0);

                    Cell cell24 = row.getCell(24);
                    if (cell24 == null) cell24 = row.createCell(24);
                    cell24.setCellValue(data[24] instanceof BigDecimal ? ((BigDecimal) data[24]).doubleValue() : 0);

                    Cell cell25 = row.getCell(25);
                    if (cell25 == null) cell25 = row.createCell(25);
                    cell25.setCellValue(data[25] instanceof BigDecimal ? ((BigDecimal) data[25]).doubleValue() : 0);

                    Cell cell26 = row.getCell(26);
                    if (cell26 == null) cell26 = row.createCell(26);
                    cell26.setCellValue(data[26] instanceof BigDecimal ? ((BigDecimal) data[26]).doubleValue() : 0);

                    Cell cell27 = row.getCell(27);
                    if (cell27 == null) cell27 = row.createCell(27);
                    cell27.setCellValue(data[27] instanceof BigDecimal ? ((BigDecimal) data[27]).doubleValue() : 0);

                    Cell cell28 = row.getCell(28);
                    if (cell28 == null) cell28 = row.createCell(28);
                    cell28.setCellValue(data[28] instanceof BigDecimal ? ((BigDecimal) data[28]).doubleValue() : 0);

                    Cell cell29 = row.getCell(29);
                    if (cell29 == null) cell29 = row.createCell(29);
                    cell29.setCellValue(data[29] instanceof BigDecimal ? ((BigDecimal) data[29]).doubleValue() : 0);

                    Cell cell30 = row.getCell(30);
                    if (cell30 == null) cell30 = row.createCell(30);
                    cell30.setCellValue(data[30] instanceof BigDecimal ? ((BigDecimal) data[30]).doubleValue() : 0);

                    Cell cell31 = row.getCell(31);
                    if (cell31 == null) cell31 = row.createCell(31);
                    cell31.setCellValue(data[31] instanceof BigDecimal ? ((BigDecimal) data[31]).doubleValue() : 0);

                    Cell cell32 = row.getCell(32);
                    if (cell32 == null) cell32 = row.createCell(32);
                    cell32.setCellValue(data[32] instanceof BigDecimal ? ((BigDecimal) data[32]).doubleValue() : 0);

                    Cell cell33 = row.getCell(33);
                    if (cell33 == null) cell33 = row.createCell(33);
                    cell33.setCellValue(data[33] instanceof BigDecimal ? ((BigDecimal) data[33]).doubleValue() : 0);

                    Cell cell34 = row.getCell(34);
                    if (cell34 == null) cell34 = row.createCell(34);
                    cell34.setCellValue(data[34] instanceof BigDecimal ? ((BigDecimal) data[34]).doubleValue() : 0);

                    Cell cell35 = row.getCell(35);
                    if (cell35 == null) cell35 = row.createCell(35);
                    cell35.setCellValue(data[35] instanceof BigDecimal ? ((BigDecimal) data[35]).doubleValue() : 0);

                    Cell cell36 = row.getCell(36);
                    if (cell36 == null) cell36 = row.createCell(36);
                    cell36.setCellValue(data[36] instanceof BigDecimal ? ((BigDecimal) data[36]).doubleValue() : 0);

                    Cell cell37 = row.getCell(37);
                    if (cell37 == null) cell37 = row.createCell(37);
                    cell37.setCellValue(data[37] instanceof BigDecimal ? ((BigDecimal) data[37]).doubleValue() : 0);

                    Cell cell38 = row.getCell(38);
                    if (cell38 == null) cell38 = row.createCell(38);
                    cell38.setCellValue(data[38] instanceof BigDecimal ? ((BigDecimal) data[38]).doubleValue() : 0);

                    Cell cell39 = row.getCell(39);
                    if (cell39 == null) cell39 = row.createCell(39);
                    cell39.setCellValue(data[39] instanceof BigDecimal ? ((BigDecimal) data[39]).doubleValue() : 0);

                    Cell cell40 = row.getCell(40);
                    if (cell40 == null) cell40 = row.createCell(40);
                    cell40.setCellValue(data[40] instanceof BigDecimal ? ((BigDecimal) data[40]).doubleValue() : 0);

                    Cell cell41 = row.getCell(41);
                    if (cell41 == null) cell41 = row.createCell(41);
                    cell41.setCellValue(data[41] instanceof BigDecimal ? ((BigDecimal) data[41]).doubleValue() : 0);

                    Cell cell42 = row.getCell(42);
                    if (cell42 == null) cell42 = row.createCell(42);
                    cell42.setCellValue(data[42] instanceof BigDecimal ? ((BigDecimal) data[42]).doubleValue() : 0);

                    Cell cell43 = row.getCell(43);
                    if (cell43 == null) cell43 = row.createCell(43);
                    cell43.setCellValue(data[43] instanceof BigDecimal ? ((BigDecimal) data[43]).doubleValue() : 0);

                    Cell cell44 = row.getCell(44);
                    if (cell44 == null) cell44 = row.createCell(44);
                    cell44.setCellValue(data[44] instanceof BigDecimal ? ((BigDecimal) data[44]).doubleValue() : 0);

                    Cell cell45 = row.getCell(45);
                    if (cell45 == null) cell45 = row.createCell(45);
                    cell45.setCellValue(data[45] instanceof BigDecimal ? ((BigDecimal) data[45]).doubleValue() : 0);

                    Cell cell46 = row.getCell(46);
                    if (cell46 == null) cell46 = row.createCell(46);
                    cell46.setCellValue(data[46] instanceof BigDecimal ? ((BigDecimal) data[46]).doubleValue() : 0);

                    Cell cell47 = row.getCell(47);
                    if (cell47 == null) cell47 = row.createCell(47);
                    cell47.setCellValue(data[47] instanceof BigDecimal ? ((BigDecimal) data[47]).doubleValue() : 0);

                    Cell cell48 = row.getCell(48);
                    if (cell48 == null) cell48 = row.createCell(48);
                    cell48.setCellValue(data[48] instanceof BigDecimal ? ((BigDecimal) data[48]).doubleValue() : 0);

                }

                // Evaluate all formulas in the workbook
                workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

                // Write output to file         
                outputFile = new File(env.getProperty("output.exportpathfinal") + "CBUAE_Treasury_Credit_Limit_Management_Data_Template.xls");

                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    workbook.write(fos);
                    System.out.println("Treasury Excel generated: " + outputFile.getAbsolutePath());
                    
                    System.out.println("Template path: " + env.getProperty("output.exportpathtemp") + "CBUAE_Treasury_Credit_Limit_Management_Data_Template.xls");

                }

                workbook.close();
            } else {
                System.out.println("No Treasury data found.");
                workbook.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputFile;
    }

}
