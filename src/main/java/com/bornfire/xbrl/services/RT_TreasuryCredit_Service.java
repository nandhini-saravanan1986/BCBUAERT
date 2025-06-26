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

            existing.setBankName(updatedEntity.getBankName());
            existing.setHeadOfficeSubsidiary(updatedEntity.getHeadOfficeSubsidiary());
            existing.setSubsidiary(updatedEntity.getSubsidiary());
            existing.setBankSymbol(updatedEntity.getBankSymbol());
            existing.setConventionalIslamic(updatedEntity.getConventionalIslamic());
            existing.setLocalForeign(updatedEntity.getLocalForeign());
            existing.setCbuaeTiering(updatedEntity.getCbuaeTiering());
            existing.setCounterpartyName(updatedEntity.getCounterpartyName());
            existing.setCounterpartyIntRef(updatedEntity.getCounterpartyIntRef());
            existing.setCounterpartyRiskRating(updatedEntity.getCounterpartyRiskRating());
            existing.setFinalRatingCbuae(updatedEntity.getFinalRatingCbuae());
            existing.setCountryOfRisk(updatedEntity.getCountryOfRisk());
            existing.setCbuaeGeographicalZone(updatedEntity.getCbuaeGeographicalZone());
            existing.setCounterpartyType(updatedEntity.getCounterpartyType());

            // You can update more fields as per need
            existing.setReportSubmitDate(updatedEntity.getReportSubmitDate());
            existing.setModifyTime(new Date()); // track update time

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
        			List<Object[]> treasuryList = treasuryRepo.getTreasuryData();
        			File templateFile = new File(
        					env.getProperty("output.exportpathtemp") + "TreasuryCreditReport.xls");
        			Workbook workbook = WorkbookFactory.create(new FileInputStream(templateFile));
        			Sheet sheet = workbook.getSheetAt(0);

        			CreationHelper createHelper = workbook.getCreationHelper();
        			CellStyle dateStyle = workbook.createCellStyle();
        			dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

            int startRow = 4;
            if (!treasuryList.isEmpty()) {
                for (int i = 0; i < treasuryList.size(); i++) {
                    Object[] data = treasuryList.get(i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null)
                        row = sheet.createRow(startRow + i);

                    // 0 - SL_NO
                    Cell cell0 = row.getCell(0);
                    if (cell0 == null)
                        cell0 = row.createCell(0);
                    cell0.setCellValue(data[0] instanceof BigDecimal ? ((BigDecimal) data[0]).doubleValue() : 0);

                    // 1 - REPORT_DATE
                    Cell cell1 = row.getCell(1);
                    if (cell1 == null)
                        cell1 = row.createCell(1);
                    if (data[1] instanceof Date) {
                        cell1.setCellValue((Date) data[1]);
                        cell1.setCellStyle(dateStyle);
                    } else {
                        cell1.setCellValue("");
                    }

                    // 2 - BANK_NAME
                    Cell cell2 = row.getCell(2);
                    if (cell2 == null)
                        cell2 = row.createCell(2);
                    cell2.setCellValue(data[2] == null ? "" : data[2].toString());

                    // 3 - HEAD_OFFICE_SUBSIDIARY
                    Cell cell3 = row.getCell(3);
                    if (cell3 == null)
                        cell3 = row.createCell(3);
                    cell3.setCellValue(data[3] == null ? "" : data[3].toString());

                    // 4 - SUBSIDIARY
                    Cell cell4 = row.getCell(4);
                    if (cell4 == null)
                        cell4 = row.createCell(4);
                    cell4.setCellValue(data[4] == null ? "" : data[4].toString());

                    // 5 - BANK_SYMBOL
                    Cell cell5 = row.getCell(5);
                    if (cell5 == null)
                        cell5 = row.createCell(5);
                    cell5.setCellValue(data[5] == null ? "" : data[5].toString());

                    // 6 - CONVENTIONAL_ISLAMIC
                    Cell cell6 = row.getCell(6);
                    if (cell6 == null)
                        cell6 = row.createCell(6);
                    cell6.setCellValue(data[6] == null ? "" : data[6].toString());

                    // 7 - LOCAL_FOREIGN
                    Cell cell7 = row.getCell(7);
                    if (cell7 == null)
                        cell7 = row.createCell(7);
                    cell7.setCellValue(data[7] == null ? "" : data[7].toString());

                    // 8 - CBUAE_TIERING
                    Cell cell8 = row.getCell(8);
                    if (cell8 == null)
                        cell8 = row.createCell(8);
                    cell8.setCellValue(data[8] == null ? "" : data[8].toString());

                    // 9 - COUNTERPARTY_NAME
                    Cell cell9 = row.getCell(9);
                    if (cell9 == null)
                        cell9 = row.createCell(9);
                    cell9.setCellValue(data[9] == null ? "" : data[9].toString());

                    // 10 - COUNTERPARTY_INT_REF
                    Cell cell10 = row.getCell(10);
                    if (cell10 == null)
                        cell10 = row.createCell(10);
                    cell10.setCellValue(data[10] == null ? "" : data[10].toString());

                    // 11 - COUNTERPARTY_RISK_RATING
                    Cell cell11 = row.getCell(11);
                    if (cell11 == null)
                        cell11 = row.createCell(11);
                    cell11.setCellValue(data[11] == null ? "" : data[11].toString());

                    // 12 - FINAL_RATING_CBUAE
                    Cell cell12 = row.getCell(12);
                    if (cell12 == null)
                        cell12 = row.createCell(12);
                    cell12.setCellValue(data[12] == null ? "" : data[12].toString());

                    // 13 - COUNTRY_OF_RISK
                    Cell cell13 = row.getCell(13);
                    if (cell13 == null)
                        cell13 = row.createCell(13);
                    cell13.setCellValue(data[13] == null ? "" : data[13].toString());

                    // 14 - CBUAE_GEOGRAPHICAL_ZONE
                    Cell cell14 = row.getCell(14);
                    if (cell14 == null)
                        cell14 = row.createCell(14);
                    cell14.setCellValue(data[14] == null ? "" : data[14].toString());

                    // 15 - COUNTERPARTY_TYPE
                    Cell cell15 = row.getCell(15);
                    if (cell15 == null)
                        cell15 = row.createCell(15);
                    cell15.setCellValue(data[15] == null ? "" : data[15].toString());

                    // 16 - LIMIT_AED_MONEYMARKET
                    Cell cell16 = row.getCell(16);
                    if (cell16 == null)
                        cell16 = row.createCell(16);
                    cell16.setCellValue(data[16] instanceof BigDecimal ? ((BigDecimal) data[16]).doubleValue() : 0);

                    // 17 - UTILIZATION_AED_MONEYMARKET
                    Cell cell17 = row.getCell(17);
                    if (cell17 == null)
                        cell17 = row.createCell(17);
                    cell17.setCellValue(data[17] instanceof BigDecimal ? ((BigDecimal) data[17]).doubleValue() : 0);

                    // 18 - MONEYMARKET_PERCENT
                    Cell cell18 = row.getCell(18);
                    if (cell18 == null)
                        cell18 = row.createCell(18);
                    cell18.setCellValue(data[18] instanceof BigDecimal ? ((BigDecimal) data[18]).doubleValue() : 0);

                    // 19 - LIMIT_AED_REPO
                    Cell cell19 = row.getCell(19);
                    if (cell19 == null)
                        cell19 = row.createCell(19);
                    cell19.setCellValue(data[19] instanceof BigDecimal ? ((BigDecimal) data[19]).doubleValue() : 0);

                    // 20 - UTILIZATION_AED_REPO
                    Cell cell20 = row.getCell(20);
                    if (cell20 == null)
                        cell20 = row.createCell(20);
                    cell20.setCellValue(data[20] instanceof BigDecimal ? ((BigDecimal) data[20]).doubleValue() : 0);

                    // 21 - REPO_PERCENT
                    Cell cell21 = row.getCell(21);
                    if (cell21 == null)
                        cell21 = row.createCell(21);
                    cell21.setCellValue(data[21] instanceof BigDecimal ? ((BigDecimal) data[21]).doubleValue() : 0);
                }
            }


            outputFile = new File(env.getProperty("output.exportpathfinal") + "TreasuryCreditReport.xls");
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                workbook.write(fos);
            }
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputFile;
    }
}
