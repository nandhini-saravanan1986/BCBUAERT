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

import com.bornfire.xbrl.entities.RT_ForeignCurrencyDeposit;
import com.bornfire.xbrl.entities.RT_ForeignCurrencyDepositRepository;
import com.bornfire.xbrl.entities.RT_Fxriskdata;
import com.bornfire.xbrl.entities.RT_MmData;
import com.bornfire.xbrl.entities.RT_MmDataRepository;
import java.io.InputStream;
import java.nio.file.Files;
import java.io.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileNotFoundException;


@Service
public class RT_ForeignCurrencyDepositService {
	
    private static final Logger logger = LoggerFactory.getLogger(RT_ForeignCurrencyDepositService.class);


    @Autowired
    private Environment env;

    @Autowired
    private RT_ForeignCurrencyDepositRepository foreigncurrencydepositRepo;

    @Autowired
    private SessionFactory sessionFactory;
    
    public boolean updateForeignCurrencyDeposit(RT_ForeignCurrencyDeposit updatedData) {
	    System.out.println("Looking for record with SI_NO: " + updatedData.getSI_NO());

	    RT_ForeignCurrencyDeposit existing = foreigncurrencydepositRepo.getParticularDataBySI_NO(updatedData.getSI_NO());

	    if (existing != null) {
	    	existing.setDate(updatedData.getDate());
	    	existing.setBankName(updatedData.getBankName());
	    	existing.setHeadOfficeSubsidiary(updatedData.getHeadOfficeSubsidiary());
	    	existing.setSubsidiary(updatedData.getSubsidiary());
	    	existing.setBankSymbol(updatedData.getBankSymbol());
	    	existing.setConventionalOrIslamic(updatedData.getConventionalOrIslamic());
	    	existing.setLocalOrForeign(updatedData.getLocalOrForeign());
	    	existing.setCbuaeTiering(updatedData.getCbuaeTiering());
	    	existing.setDepositInternalReference(updatedData.getDepositInternalReference());
	    	existing.setOnBalanceSheetDepType(updatedData.getOnBalanceSheetDepType());
	    	existing.setFundingCounterParty(updatedData.getFundingCounterParty());
	    	existing.setCounterpartyType(updatedData.getCounterpartyType());
	    	existing.setIndustryGcis(updatedData.getIndustryGcis());
	    	existing.setCounterpartyCountryRisk(updatedData.getCounterpartyCountryRisk());
	    	existing.setCbuaeRegionalZone(updatedData.getCbuaeRegionalZone());
	    	existing.setNominal(updatedData.getNominal());
	    	existing.setNominalInAed(updatedData.getNominalInAed());
	    	existing.setCurrency(updatedData.getCurrency());
	    	existing.setRateType(updatedData.getRateType());
	    	existing.setDepositFixedRateOrAdministrativeRate(updatedData.getDepositFixedRateOrAdministrativeRate());
	    	existing.setBenchmarkFloatingRate(updatedData.getBenchmarkFloatingRate());
	    	existing.setTenorFloatingRate(updatedData.getTenorFloatingRate());
	    	existing.setSpreadOverBenchmarkRate(updatedData.getSpreadOverBenchmarkRate());
	    	existing.setMaturityDate(updatedData.getMaturityDate());
	    	existing.setTenorMths(updatedData.getTenorMths());
	    	existing.setMaturityPeriod(updatedData.getMaturityPeriod());

	    	
	    	
	    	
	       
	    	
	    	foreigncurrencydepositRepo.save(existing);
	        return true;
	    } else {
	        System.out.println("No record found for SI_NO: " + updatedData.getSI_NO());
	        return false;
	    }
	}
    
    public byte[] generateForeignCurrencyDepositExcel() throws Exception {
        logger.info("Service: Starting Foreign Currency Deposit Excel generation process in memory.");

        List<Object[]> foreigncurrencyList = foreigncurrencydepositRepo.getforeigncurrencylistdata1();

        if (foreigncurrencyList.isEmpty()) {
            logger.warn("Service: No data found for Foreign Currency report. Returning empty result.");
            return new byte[0];
        }

        String templateDir = env.getProperty("output.exportpathtemp"); // Config property key
        String templateFileName = "CBUAE_Foreign_Currency_Deposit_Template.xls";
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

			int startRow = 2; // assuming data starts from row 3 (index 2)

			if (!foreigncurrencyList.isEmpty()) {
				for (int i = 0; i < foreigncurrencyList.size(); i++) {
					Object[] fx = foreigncurrencyList.get(i);
					Row row = sheet.getRow(startRow + i);
					if (row == null)
						row = sheet.createRow(startRow + i);
					
					// 0 - Date column
					Cell cell0 = row.getCell(0);
					if (cell0 == null)
						cell0 = row.createCell(0);
					if (fx[0] instanceof Date) {
						cell0.setCellValue((Date) fx[0]);
						cell0.setCellStyle(dateStyle);
					} else {
						cell0.setCellValue("");
						
					}
					
					// 1 - String
					Cell cell1 = row.getCell(1);
					if (cell1 == null)
						cell1 = row.createCell(1);
					cell1.setCellValue(fx[1] == null ? "" : fx[1].toString());
					
				
					// 2 – HEAD_OFFICE_SUBSIDIARY (String)
					Cell cell2 = row.getCell(2);
					if (cell2 == null) cell2 = row.createCell(2);
					cell2.setCellValue(fx[2] == null ? "" : fx[2].toString());

					// 3 – SUBSIDIARY (String)
					Cell cell3 = row.getCell(3);
					if (cell3 == null) cell3 = row.createCell(3);
					cell3.setCellValue(fx[3] == null ? "" : fx[3].toString());

					// 4 – BANK_SYMBOL (String)
					Cell cell4 = row.getCell(4);
					if (cell4 == null) cell4 = row.createCell(4);
					cell4.setCellValue(fx[4] == null ? "" : fx[4].toString());

					// 5 – CONVENTIONAL_OR_ISLAMIC (String)
					Cell cell5 = row.getCell(5);
					if (cell5 == null) cell5 = row.createCell(5);
					cell5.setCellValue(fx[5] == null ? "" : fx[5].toString());

					// 6 – LOCAL_OR_FOREIGN (String)
					Cell cell6 = row.getCell(6);
					if (cell6 == null) cell6 = row.createCell(6);
					cell6.setCellValue(fx[6] == null ? "" : fx[6].toString());

					// 7 – CBUAE_TIERING (String)
					Cell cell7 = row.getCell(7);
					if (cell7 == null) cell7 = row.createCell(7);
					cell7.setCellValue(fx[7] == null ? "" : fx[7].toString());

					// 8 – DEPOSIT_INTERNAL_REFERENCE (String)
					Cell cell8 = row.getCell(8);
					if (cell8 == null) cell8 = row.createCell(8);
					cell8.setCellValue(fx[8] == null ? "" : fx[8].toString());

					// 9 – ON_BALANCE_SHEET_DEP_TYPE (String)
					Cell cell9 = row.getCell(9);
					if (cell9 == null) cell9 = row.createCell(9);
					cell9.setCellValue(fx[9] == null ? "" : fx[9].toString());

					// 10 – FUNDING_COUNTERPARTY (String)
					Cell cell10 = row.getCell(10);
					if (cell10 == null) cell10 = row.createCell(10);
					cell10.setCellValue(fx[10] == null ? "" : fx[10].toString());

					// 11 – COUNTERPARTY_TYPE (String)
					Cell cell11 = row.getCell(11);
					if (cell11 == null) cell11 = row.createCell(11);
					cell11.setCellValue(fx[11] == null ? "" : fx[11].toString());

					// 12 – INDUSTRY_GCIS (String)
					Cell cell12 = row.getCell(12);
					if (cell12 == null) cell12 = row.createCell(12);
					cell12.setCellValue(fx[12] == null ? "" : fx[12].toString());

					// 13 – COUNTERPARTY_COUNTRY_RISK (String)
					Cell cell13 = row.getCell(13);
					if (cell13 == null) cell13 = row.createCell(13);
					cell13.setCellValue(fx[13] == null ? "" : fx[13].toString());

					// 14 – CBUAE_REGIONAL_ZONE (String)
					Cell cell14 = row.getCell(14);
					if (cell14 == null) cell14 = row.createCell(14);
					cell14.setCellValue(fx[14] == null ? "" : fx[14].toString());

					// 15 – NOMINAL (BigDecimal → numeric)
					Cell cell15 = row.getCell(15);
					if (cell15 == null) cell15 = row.createCell(15);
					if (fx[15] instanceof BigDecimal)
					    cell15.setCellValue(((BigDecimal) fx[15]).doubleValue());
					else
					    cell15.setCellValue("");

					// 16 – NOMINAL_IN_AED (BigDecimal)
					Cell cell16 = row.getCell(16);
					if (cell16 == null) cell16 = row.createCell(16);
					if (fx[16] instanceof BigDecimal)
					    cell16.setCellValue(((BigDecimal) fx[16]).doubleValue());
					else
					    cell16.setCellValue("");

					// 17 – CURRENCY (String)
					Cell cell17 = row.getCell(17);
					if (cell17 == null) cell17 = row.createCell(17);
					cell17.setCellValue(fx[17] == null ? "" : fx[17].toString());

					// 18 – RATE_TYPE (String)
					Cell cell18 = row.getCell(18);
					if (cell18 == null) cell18 = row.createCell(18);
					cell18.setCellValue(fx[18] == null ? "" : fx[18].toString());

					// 19 – DEP_FIXED_RATE_OR_ADMINIS_RATE (BigDecimal)
					Cell cell19 = row.getCell(19);
					if (cell19 == null) cell19 = row.createCell(19);
					if (fx[19] instanceof BigDecimal)
					    cell19.setCellValue(((BigDecimal) fx[19]).doubleValue());
					else
					    cell19.setCellValue("");

					// 20 – BENCHMARK_FLOATING_RATE (String)
					Cell cell20 = row.getCell(20);
					if (cell20 == null) cell20 = row.createCell(20);
					cell20.setCellValue(fx[20] == null ? "" : fx[20].toString());

					// 21 – TENOR_FLOATING_RATE (String)
					Cell cell21 = row.getCell(21);
					if (cell21 == null) cell21 = row.createCell(21);
					cell21.setCellValue(fx[21] == null ? "" : fx[21].toString());

					// 22 – SPREAD_OVER_BENCHMARK_RATE (BigDecimal)
					Cell cell22 = row.getCell(22);
					if (cell22 == null) cell22 = row.createCell(22);
					if (fx[22] instanceof BigDecimal)
					    cell22.setCellValue(((BigDecimal) fx[22]).doubleValue());
					else
					    cell22.setCellValue("");

					// 23 – MATURITY_DATE (Date)
					Cell cell23 = row.getCell(23);
					if (cell23 == null) cell23 = row.createCell(23);
					if (fx[23] instanceof Date) {
					    cell23.setCellValue((Date) fx[23]);
					    cell23.setCellStyle(dateStyle);
					} else {
					    cell23.setCellValue("");
					}

					// 24 – TENOR_MTHS (BigDecimal)
					Cell cell24 = row.getCell(24);
					if (cell24 == null) cell24 = row.createCell(24);
					if (fx[24] instanceof BigDecimal)
					    cell24.setCellValue(((BigDecimal) fx[24]).doubleValue());
					else
					    cell24.setCellValue("");

					// 25 – MATURITY_PERIOD (BigDecimal)
					Cell cell25 = row.getCell(25);
					if (cell25 == null) cell25 = row.createCell(25);
					if (fx[25] instanceof BigDecimal)
					    cell25.setCellValue(((BigDecimal) fx[25]).doubleValue());
					else
					    cell25.setCellValue("");
				}
				// Auto-size all 31 columns
				for (int i = 0; i <= 30; i++) {
				    sheet.autoSizeColumn(i);
				}

				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {
				System.out.println("No Fx Risk data found to generate the Excel file.");
			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}
    

    
}
