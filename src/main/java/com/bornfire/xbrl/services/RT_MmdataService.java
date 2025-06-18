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
import com.bornfire.xbrl.entities.RT_MmData;
import com.bornfire.xbrl.entities.RT_MmDataRepository;

@Service
public class RT_MmdataService {

    @Autowired
    private Environment env;

    @Autowired
    private RT_MmDataRepository mmdataRepo;

    @Autowired
    private SessionFactory sessionFactory;
    
    public boolean updateMmdata(RT_MmData updatedData) {
	    System.out.println("Looking for record with SI_NO: " + updatedData.getSI_NO());

	    RT_MmData existing = mmdataRepo.getParticularDataBySI_NO(updatedData.getSI_NO());

	    if (existing != null) {
	        // Update fields
	       
	    	existing.setBank_name(updatedData.getBank_name());
	    	existing.setHead_office_subsidiary(updatedData.getHead_office_subsidiary());
	    	existing.setSubsidiary(updatedData.getSubsidiary());
	    	existing.setBank_symbol(updatedData.getBank_symbol());
	    	existing.setConventional_islamic(updatedData.getConventional_islamic());
	    	existing.setLocal_foreign(updatedData.getLocal_foreign());
	    	existing.setCbuae_tiering(updatedData.getCbuae_tiering());
	    	existing.setDeal_no(updatedData.getDeal_no());
	    	existing.setCustomer_id(updatedData.getCustomer_id());
	    	existing.setCounterparty_name(updatedData.getCounterparty_name());
	    	existing.setFinal_rating_banks(updatedData.getFinal_rating_banks());
	    	existing.setFinal_rating_cbuae(updatedData.getFinal_rating_cbuae());
	    	existing.setCountry_of_risk(updatedData.getCountry_of_risk());
	    	existing.setCbuae_geographical_zone(updatedData.getCbuae_geographical_zone());
	    	existing.setDeal_type(updatedData.getDeal_type());
	    	existing.setValue_date(updatedData.getValue_date());
	    	existing.setMaturity_date(updatedData.getMaturity_date());
	    	existing.setInitial_maturity(updatedData.getInitial_maturity());
	    	existing.setInitial_maturity_rounded(updatedData.getInitial_maturity_rounded());
	    	existing.setInitial_maturity_period(updatedData.getInitial_maturity_period());
	    	existing.setResidual_maturity(updatedData.getResidual_maturity());
	    	existing.setResidual_maturity_rounded(updatedData.getResidual_maturity_rounded());
	    	existing.setMaturity_period(updatedData.getMaturity_period());
	    	existing.setCurrency(updatedData.getCurrency());
	    	existing.setPrincipal(updatedData.getPrincipal());
	    	existing.setPrincipal_aed(updatedData.getPrincipal_aed());
	    	existing.setInterest_profit_rate(updatedData.getInterest_profit_rate());


	        mmdataRepo.save(existing);
	        return true;
	    } else {
	        System.out.println("No record found for SI_NO: " + updatedData.getSI_NO());
	        return false;
	    }
	}
    
    

    public File generateMmExcel() {
        File outputFile = null;

        try {
            // Fetch data from repository
            List<Object[]> mmDataList = mmdataRepo.getmmdatalistdata1();

            // Load Excel template file
            File templateFile = new File(env.getProperty("output.exportpathtemp") + "CBUAE_Mm_Data_Template.xls");
            Workbook workbook = WorkbookFactory.create(new FileInputStream(templateFile));
            Sheet sheet = workbook.getSheetAt(0);

            // Create date cell style
            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

            int startRow = 2; // Assuming data starts from row index 2 (3rd row)

            if (!mmDataList.isEmpty()) {
                for (int i = 0; i < mmDataList.size(); i++) {
                    Object[] mm = mmDataList.get(i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) row = sheet.createRow(startRow + i);

                    // 0 - Date
                    Cell cell0 = row.getCell(0);
                    if (cell0 == null) cell0 = row.createCell(0);
                    if (mm[0] instanceof Date) {
                        cell0.setCellValue((Date) mm[0]);
                        cell0.setCellStyle(dateStyle);
                    } else {
                        cell0.setCellValue("");
                    }

                    // 1 - VARCHAR2
                    Cell cell1 = row.getCell(1);
                    if (cell1 == null) cell1 = row.createCell(1);
                    cell1.setCellValue(mm[1] == null ? "" : mm[1].toString());

                    // 2 - VARCHAR2
                    Cell cell2 = row.getCell(2);
                    if (cell2 == null) cell2 = row.createCell(2);
                    cell2.setCellValue(mm[2] == null ? "" : mm[2].toString());

                    // 3 - VARCHAR2
                    Cell cell3 = row.getCell(3);
                    if (cell3 == null) cell3 = row.createCell(3);
                    cell3.setCellValue(mm[3] == null ? "" : mm[3].toString());

                    // 4 - VARCHAR2
                    Cell cell4 = row.getCell(4);
                    if (cell4 == null) cell4 = row.createCell(4);
                    cell4.setCellValue(mm[4] == null ? "" : mm[4].toString());

                    // 5 - VARCHAR2
                    Cell cell5 = row.getCell(5);
                    if (cell5 == null) cell5 = row.createCell(5);
                    cell5.setCellValue(mm[5] == null ? "" : mm[5].toString());

                    // 6 - VARCHAR2
                    Cell cell6 = row.getCell(6);
                    if (cell6 == null) cell6 = row.createCell(6);
                    cell6.setCellValue(mm[6] == null ? "" : mm[6].toString());

                    // 7 - VARCHAR2
                    Cell cell7 = row.getCell(7);
                    if (cell7 == null) cell7 = row.createCell(7);
                    cell7.setCellValue(mm[7] == null ? "" : mm[7].toString());

                    // 8 - NUMBER(20,4) BigDecimal
                    Cell cell8 = row.getCell(8);
                    if (cell8 == null) cell8 = row.createCell(8);
                    if (mm[8] instanceof BigDecimal) {
                        cell8.setCellValue(((BigDecimal) mm[8]).doubleValue());
                    } else {
                        cell8.setCellValue(0);
                    }

                    // 9 - VARCHAR2
                    Cell cell9 = row.getCell(9);
                    if (cell9 == null) cell9 = row.createCell(9);
                    cell9.setCellValue(mm[9] == null ? "" : mm[9].toString());

                    // 10 - VARCHAR2
                    Cell cell10 = row.getCell(10);
                    if (cell10 == null) cell10 = row.createCell(10);
                    cell10.setCellValue(mm[10] == null ? "" : mm[10].toString());

                    // 11 - VARCHAR2
                    Cell cell11 = row.getCell(11);
                    if (cell11 == null) cell11 = row.createCell(11);
                    cell11.setCellValue(mm[11] == null ? "" : mm[11].toString());

                    // 12 - VARCHAR2
                    Cell cell12 = row.getCell(12);
                    if (cell12 == null) cell12 = row.createCell(12);
                    cell12.setCellValue(mm[12] == null ? "" : mm[12].toString());

                    // 13 - VARCHAR2
                    Cell cell13 = row.getCell(13);
                    if (cell13 == null) cell13 = row.createCell(13);
                    cell13.setCellValue(mm[13] == null ? "" : mm[13].toString());

                    // 14 - VARCHAR2
                    Cell cell14 = row.getCell(14);
                    if (cell14 == null) cell14 = row.createCell(14);
                    cell14.setCellValue(mm[14] == null ? "" : mm[14].toString());

                    // 15 - VARCHAR2
                    Cell cell15 = row.getCell(15);
                    if (cell15 == null) cell15 = row.createCell(15);
                    cell15.setCellValue(mm[15] == null ? "" : mm[15].toString());

                    // 16 - DATE
                    Cell cell16 = row.getCell(16);
                    if (cell16 == null) cell16 = row.createCell(16);
                    if (mm[16] instanceof Date) {
                        cell16.setCellValue((Date) mm[16]);
                        cell16.setCellStyle(dateStyle);
                    } else {
                        cell16.setCellValue("");
                    }

                    // 17 - DATE
                    Cell cell17 = row.getCell(17);
                    if (cell17 == null) cell17 = row.createCell(17);
                    if (mm[17] instanceof Date) {
                        cell17.setCellValue((Date) mm[17]);
                        cell17.setCellStyle(dateStyle);
                    } else {
                        cell17.setCellValue("");
                    }

                    // 18 - VARCHAR2
                    Cell cell18 = row.getCell(18);
                    if (cell18 == null) cell18 = row.createCell(18);
                    cell18.setCellValue(mm[18] == null ? "" : mm[18].toString());

                    // 19 - VARCHAR2
                    Cell cell19 = row.getCell(19);
                    if (cell19 == null) cell19 = row.createCell(19);
                    cell19.setCellValue(mm[19] == null ? "" : mm[19].toString());

                    // 20 - VARCHAR2
                    Cell cell20 = row.getCell(20);
                    if (cell20 == null) cell20 = row.createCell(20);
                    cell20.setCellValue(mm[20] == null ? "" : mm[20].toString());

                    // 21 - VARCHAR2
                    Cell cell21 = row.getCell(21);
                    if (cell21 == null) cell21 = row.createCell(21);
                    cell21.setCellValue(mm[21] == null ? "" : mm[21].toString());

                    // 22 - VARCHAR2
                    Cell cell22 = row.getCell(22);
                    if (cell22 == null) cell22 = row.createCell(22);
                    cell22.setCellValue(mm[22] == null ? "" : mm[22].toString());

                    // 23 - VARCHAR2
                    Cell cell23 = row.getCell(23);
                    if (cell23 == null) cell23 = row.createCell(23);
                    cell23.setCellValue(mm[23] == null ? "" : mm[23].toString());

                    // 24 - VARCHAR2
                    Cell cell24 = row.getCell(24);
                    if (cell24 == null) cell24 = row.createCell(24);
                    cell24.setCellValue(mm[24] == null ? "" : mm[24].toString());

                    // 25 - VARCHAR2
                    Cell cell25 = row.getCell(25);
                    if (cell25 == null) cell25 = row.createCell(25);
                    cell25.setCellValue(mm[25] == null ? "" : mm[25].toString());

                    // 26 - VARCHAR2
                    Cell cell26 = row.getCell(26);
                    if (cell26 == null) cell26 = row.createCell(26);
                    cell26.setCellValue(mm[26] == null ? "" : mm[26].toString());

                    // 27 - VARCHAR2
                    Cell cell27 = row.getCell(27);
                    if (cell27 == null) cell27 = row.createCell(27);
                    cell27.setCellValue(mm[27] == null ? "" : mm[27].toString());
                }

                // Evaluate formulas
                workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

                // Write output to file
                outputFile = new File(env.getProperty("output.exportpathfinal") + "MmData.xls");
                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    workbook.write(fos);
                    System.out.println("FxRisk Excel generated: " + outputFile.getAbsolutePath());
                }

                workbook.close();
            } else {
                System.out.println("No Mm data found.");
                workbook.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputFile;
    }
}
