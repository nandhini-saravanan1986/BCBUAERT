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
import com.bornfire.xbrl.entities.RT_ImpactAnalysis;
import com.bornfire.xbrl.entities.RT_ImpactAnalysisRepository;
import com.bornfire.xbrl.entities.RT_MmData;
import com.bornfire.xbrl.entities.RT_MmDataRepository;

@Service
public class RT_ImpactAnalysisService {

    @Autowired
    private Environment env;

    @Autowired
    private RT_ImpactAnalysisRepository impactanalysisRepo;

    @Autowired
    private SessionFactory sessionFactory;
    
    public boolean updateImpactAnalysis(RT_ImpactAnalysis updatedData) {
	    System.out.println("Looking for record with SI_NO: " + updatedData.getSI_NO());

	    RT_ImpactAnalysis existing = impactanalysisRepo.getParticularDataBySI_NO(updatedData.getSI_NO());

	    if (existing != null) {
	    	existing.setDate(updatedData.getDate());
	    	existing.setBankName(updatedData.getBankName());
	    	existing.setHeadOfficeSubsidiary(updatedData.getHeadOfficeSubsidiary());
	    	existing.setSubsidiary(updatedData.getSubsidiary());
	    	existing.setBankSymbol(updatedData.getBankSymbol());
	    	existing.setConventionalOrIslamic(updatedData.getConventionalOrIslamic());
	    	existing.setLocalOrForeign(updatedData.getLocalOrForeign());
	    	existing.setCbuaeTiering(updatedData.getCbuaeTiering());
	    	existing.setDealInternalReference(updatedData.getDealInternalReference());
	    	existing.setLegType(updatedData.getLegType());
	    	existing.setInternalCounterpartyReference(updatedData.getInternalCounterpartyReference());
	    	existing.setCptyName(updatedData.getCptyName());
	    	existing.setDerivativesCptyCountryOfRisk(updatedData.getDerivativesCptyCountryOfRisk());
	    	existing.setCbuaeRegionalZone(updatedData.getCbuaeRegionalZone());
	    	existing.setCcyBought(updatedData.getCcyBought());
	    	existing.setAmountBought(updatedData.getAmountBought());
	    	existing.setDealRate(updatedData.getDealRate());
	    	existing.setCcySold(updatedData.getCcySold());
	    	existing.setAmountSold(updatedData.getAmountSold());
	    	existing.setValueDate(updatedData.getValueDate());
	    	existing.setDealDate(updatedData.getDealDate());
	    	existing.setDerivativesTenorMonths(updatedData.getDerivativesTenorMonths());
	    	existing.setMaturityPeriod(updatedData.getMaturityPeriod());
	    	existing.setDealerName(updatedData.getDealerName());
	    	existing.setMtmAedEquivalentReportingDate(updatedData.getMtmAedEquivalentReportingDate());
	    	existing.setMtmAedEquivalentPreviousQuarter(updatedData.getMtmAedEquivalentPreviousQuarter());
	    	existing.setMtmChange(updatedData.getMtmChange());
	    	existing.setFundingOrLoansInternalReference(updatedData.getFundingOrLoansInternalReference());
	    	existing.setOnBalanceSheetItemType(updatedData.getOnBalanceSheetItemType());
	    	existing.setOnBalanceItemCounterparty(updatedData.getOnBalanceItemCounterparty());
	    	existing.setCounterpartyCountryOfRisk(updatedData.getCounterpartyCountryOfRisk());
	    	existing.setCbuaeRegionalZne(updatedData.getCbuaeRegionalZne());
	    	existing.setNominal(updatedData.getNominal());
	    	existing.setCurrency(updatedData.getCurrency());
	    	existing.setOverallImpact(updatedData.getOverallImpact());
	    	existing.setAccrualImpact(updatedData.getAccrualImpact());
	    	existing.setRateType(updatedData.getRateType());
	    	existing.setDepositFixedRateOrAdministrativeRate(updatedData.getDepositFixedRateOrAdministrativeRate());
	    	existing.setFixedDepositRateEquivalent(updatedData.getFixedDepositRateEquivalent());
	    	existing.setFloatingRateType(updatedData.getFloatingRateType());
	    	existing.setBenchmarkFloatingRate(updatedData.getBenchmarkFloatingRate());
	    	existing.setSpreadOverBenchmarkRate(updatedData.getSpreadOverBenchmarkRate());
	    	existing.setMaturityDate(updatedData.getMaturityDate());
	    	existing.setTenorMths(updatedData.getTenorMths());

	    	

	    	impactanalysisRepo.save(existing);
	        return true;
	    } else {
	        System.out.println("No record found for SI_NO: " + updatedData.getSI_NO());
	        return false;
	    }
	}
    
    public File generateImpactAnalysisExcel() {
		File outputFile = null;

		try {
			// Fetch data from repository
			List<Object[]> fxDataList = impactanalysisRepo.getimpactanalysislistdata1();

			// Load Excel template file
			File templateFile = new File(env.getProperty("output.exportpathtemp") + "CBUAE_Impact_Analysis_Template.xls");
			Workbook workbook = WorkbookFactory.create(new FileInputStream(templateFile));
			Sheet sheet = workbook.getSheetAt(0);

			// Create date cell style
			CreationHelper createHelper = workbook.getCreationHelper();
			CellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

			int startRow = 2; // assuming data starts from row 3 (index 2)

			if (!fxDataList.isEmpty()) {
				for (int i = 0; i < fxDataList.size(); i++) {
					Object[] fx = fxDataList.get(i);
					Row row = sheet.getRow(startRow + i);
					if (row == null)
						row = sheet.createRow(startRow + i);

					// --------------------------------------------------
					// 0 – REPORT_DATE (Date)
					Cell cell0 = row.getCell(0);
					if (cell0 == null) cell0 = row.createCell(0);
					if (fx[0] instanceof Date) {
					    cell0.setCellValue((Date) fx[0]);
					    cell0.setCellStyle(dateStyle);
					} else {
					    cell0.setCellValue("");
					}

					// 1 – BANK_NAME (String)
					Cell cell1 = row.getCell(1);
					if (cell1 == null) cell1 = row.createCell(1);
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

					// 8 – DEAL_INTERNAL_REFERENCE (String)
					Cell cell8 = row.getCell(8);
					if (cell8 == null) cell8 = row.createCell(8);
					cell8.setCellValue(fx[8] == null ? "" : fx[8].toString());

					// 9 – LEG_TYPE (String)
					Cell cell9 = row.getCell(9);
					if (cell9 == null) cell9 = row.createCell(9);
					cell9.setCellValue(fx[9] == null ? "" : fx[9].toString());

					// 10 – INTERNAL_COUNTERPARTY_REFERENCE (String)
					Cell cell10 = row.getCell(10);
					if (cell10 == null) cell10 = row.createCell(10);
					cell10.setCellValue(fx[10] == null ? "" : fx[10].toString());

					// 11 – CPTY_NAME (String)
					Cell cell11 = row.getCell(11);
					if (cell11 == null) cell11 = row.createCell(11);
					cell11.setCellValue(fx[11] == null ? "" : fx[11].toString());

					// 12 – DERIVATIVES_CPTY_COUNTRY_OF_RISK (String)
					Cell cell12 = row.getCell(12);
					if (cell12 == null) cell12 = row.createCell(12);
					cell12.setCellValue(fx[12] == null ? "" : fx[12].toString());

					// 13 – CBUAE_REGIONAL_ZONE (String)
					Cell cell13 = row.getCell(13);
					if (cell13 == null) cell13 = row.createCell(13);
					cell13.setCellValue(fx[13] == null ? "" : fx[13].toString());

					// 14 – CCY_BOUGHT (String)
					Cell cell14 = row.getCell(14);
					if (cell14 == null) cell14 = row.createCell(14);
					cell14.setCellValue(fx[14] == null ? "" : fx[14].toString());

					// 15 – AMOUNT_BOUGHT (BigDecimal)
					Cell cell15 = row.getCell(15);
					if (cell15 == null) cell15 = row.createCell(15);
					if (fx[15] instanceof BigDecimal)
					    cell15.setCellValue(((BigDecimal) fx[15]).doubleValue());
					else
					    cell15.setCellValue("");

					// 16 – DEAL_RATE (BigDecimal)
					Cell cell16 = row.getCell(16);
					if (cell16 == null) cell16 = row.createCell(16);
					if (fx[16] instanceof BigDecimal)
					    cell16.setCellValue(((BigDecimal) fx[16]).doubleValue());
					else
					    cell16.setCellValue("");

					// 17 – CCY_SOLD (String)
					Cell cell17 = row.getCell(17);
					if (cell17 == null) cell17 = row.createCell(17);
					cell17.setCellValue(fx[17] == null ? "" : fx[17].toString());

					// 18 – AMOUNT_SOLD (BigDecimal)
					Cell cell18 = row.getCell(18);
					if (cell18 == null) cell18 = row.createCell(18);
					if (fx[18] instanceof BigDecimal)
					    cell18.setCellValue(((BigDecimal) fx[18]).doubleValue());
					else
					    cell18.setCellValue("");

					// 19 – VALUE_DATE (Date)
					Cell cell19 = row.getCell(19);
					if (cell19 == null) cell19 = row.createCell(19);
					if (fx[19] instanceof Date) {
					    cell19.setCellValue((Date) fx[19]);
					    cell19.setCellStyle(dateStyle);
					} else {
					    cell19.setCellValue("");
					}

					// 20 – DEAL_DATE (Date)
					Cell cell20 = row.getCell(20);
					if (cell20 == null) cell20 = row.createCell(20);
					if (fx[20] instanceof Date) {
					    cell20.setCellValue((Date) fx[20]);
					    cell20.setCellStyle(dateStyle);
					} else {
					    cell20.setCellValue("");
					}

					// 21 – DERIVATIVES_TENOR_MONTHS (BigDecimal)
					Cell cell21 = row.getCell(21);
					if (cell21 == null) cell21 = row.createCell(21);
					if (fx[21] instanceof BigDecimal)
					    cell21.setCellValue(((BigDecimal) fx[21]).doubleValue());
					else
					    cell21.setCellValue("");

					// 22 – MATURITY_PERIOD (BigDecimal)
					Cell cell22 = row.getCell(22);
					if (cell22 == null) cell22 = row.createCell(22);
					if (fx[22] instanceof BigDecimal)
					    cell22.setCellValue(((BigDecimal) fx[22]).doubleValue());
					else
					    cell22.setCellValue("");

					// 23 – DEALER_NAME (String)
					Cell cell23 = row.getCell(23);
					if (cell23 == null) cell23 = row.createCell(23);
					cell23.setCellValue(fx[23] == null ? "" : fx[23].toString());

					// 24 – MTM_AED_EQUIVALENT_REPORTING_DATE (Date)
					Cell cell24 = row.getCell(24);
					if (cell24 == null) cell24 = row.createCell(24);
					if (fx[24] instanceof Date) {
					    cell24.setCellValue((Date) fx[24]);
					    cell24.setCellStyle(dateStyle);
					} else {
					    cell24.setCellValue("");
					}

					// 25 – MTM_AED_EQUIVALENT_PREVIOUS_QUARTER (BigDecimal)
					Cell cell25 = row.getCell(25);
					if (cell25 == null) cell25 = row.createCell(25);
					if (fx[25] instanceof BigDecimal)
					    cell25.setCellValue(((BigDecimal) fx[25]).doubleValue());
					else
					    cell25.setCellValue("");

					// 26 – MTM_CHANGE (BigDecimal)
					Cell cell26 = row.getCell(26);
					if (cell26 == null) cell26 = row.createCell(26);
					if (fx[26] instanceof BigDecimal)
					    cell26.setCellValue(((BigDecimal) fx[26]).doubleValue());
					else
					    cell26.setCellValue("");

					// 27 – FUNDING_OR_LOANS_INTERNAL_REFERENCE (String)
					Cell cell27 = row.getCell(27);
					if (cell27 == null) cell27 = row.createCell(27);
					cell27.setCellValue(fx[27] == null ? "" : fx[27].toString());

					// 28 – ON_BALANCE_SHEET_ITEM_TYPE (String)
					Cell cell28 = row.getCell(28);
					if (cell28 == null) cell28 = row.createCell(28);
					cell28.setCellValue(fx[28] == null ? "" : fx[28].toString());

					// 29 – ON_BALANCE_ITEM_COUNTERPARTY (String)
					Cell cell29 = row.getCell(29);
					if (cell29 == null) cell29 = row.createCell(29);
					cell29.setCellValue(fx[29] == null ? "" : fx[29].toString());

					// 30 – COUNTERPARTY_COUNTRY_OF_RISK (String)
					Cell cell30 = row.getCell(30);
					if (cell30 == null) cell30 = row.createCell(30);
					cell30.setCellValue(fx[30] == null ? "" : fx[30].toString());

					// 31 – CBUAE_REGIONAL_ZNE (String)
					Cell cell31 = row.getCell(31);
					if (cell31 == null) cell31 = row.createCell(31);
					cell31.setCellValue(fx[31] == null ? "" : fx[31].toString());

					// 32 – NOMINAL (BigDecimal)
					Cell cell32 = row.getCell(32);
					if (cell32 == null) cell32 = row.createCell(32);
					if (fx[32] instanceof BigDecimal)
					    cell32.setCellValue(((BigDecimal) fx[32]).doubleValue());
					else
					    cell32.setCellValue("");

					// 33 – CURRENCY (String)
					Cell cell33 = row.getCell(33);
					if (cell33 == null) cell33 = row.createCell(33);
					cell33.setCellValue(fx[33] == null ? "" : fx[33].toString());

					// 34 – OVERALL_IMPACT (BigDecimal)
					Cell cell34 = row.getCell(34);
					if (cell34 == null) cell34 = row.createCell(34);
					if (fx[34] instanceof BigDecimal)
					    cell34.setCellValue(((BigDecimal) fx[34]).doubleValue());
					else
					    cell34.setCellValue("");

					// 35 – ACCRUAL_IMPACT (BigDecimal)
					Cell cell35 = row.getCell(35);
					if (cell35 == null) cell35 = row.createCell(35);
					if (fx[35] instanceof BigDecimal)
					    cell35.setCellValue(((BigDecimal) fx[35]).doubleValue());
					else
					    cell35.setCellValue("");

					// 36 – RATE_TYPE (String)
					Cell cell36 = row.getCell(36);
					if (cell36 == null) cell36 = row.createCell(36);
					cell36.setCellValue(fx[36] == null ? "" : fx[36].toString());

					// 37 – DEP_FIXED_RATE_OR_ADMINIS_RATE (BigDecimal)
					Cell cell37 = row.getCell(37);
					if (cell37 == null) cell37 = row.createCell(37);
					if (fx[37] instanceof BigDecimal)
					    cell37.setCellValue(((BigDecimal) fx[37]).doubleValue());
					else
					    cell37.setCellValue("");

					// 38 – FIXED_DEPOSIT_RATE_EQUIVALENT (BigDecimal)
					Cell cell38 = row.getCell(38);
					if (cell38 == null) cell38 = row.createCell(38);
					if (fx[38] instanceof BigDecimal)
					    cell38.setCellValue(((BigDecimal) fx[38]).doubleValue());
					else
					    cell38.setCellValue("");

					// 39 – FLOATING_RATE_TYPE (String)
					Cell cell39 = row.getCell(39);
					if (cell39 == null) cell39 = row.createCell(39);
					cell39.setCellValue(fx[39] == null ? "" : fx[39].toString());

					// 40 – BENCHMARK_FLOATING_RATE (String)
					Cell cell40 = row.getCell(40);
					if (cell40 == null) cell40 = row.createCell(40);
					cell40.setCellValue(fx[40] == null ? "" : fx[40].toString());

					// 41 – SPREAD_OVER_BENCHMARK_RATE (BigDecimal)
					Cell cell41 = row.getCell(41);
					if (cell41 == null) cell41 = row.createCell(41);
					if (fx[41] instanceof BigDecimal)
					    cell41.setCellValue(((BigDecimal) fx[41]).doubleValue());
					else
					    cell41.setCellValue("");

					// 42 – MATURITY_DATE (Date)
					Cell cell42 = row.getCell(42);
					if (cell42 == null) cell42 = row.createCell(42);
					if (fx[42] instanceof Date) {
					    cell42.setCellValue((Date) fx[42]);
					    cell42.setCellStyle(dateStyle);
					} else {
					    cell42.setCellValue("");
					}

					// 43 – TENOR_MTHS (BigDecimal)
					Cell cell43 = row.getCell(43);
					if (cell43 == null) cell43 = row.createCell(43);
					if (fx[43] instanceof BigDecimal)
					    cell43.setCellValue(((BigDecimal) fx[43]).doubleValue());
					else
					    cell43.setCellValue("");

					
					
					
				}

				// Evaluate all formulas in the workbook
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

				// Write output to file
				outputFile = new File(env.getProperty("output.exportpathfinal") + "ImpactAnalysisData.xls");
				try (FileOutputStream fos = new FileOutputStream(outputFile)) {
					workbook.write(fos);
					System.out.println("ImpactAnalysis Excel generated: " + outputFile.getAbsolutePath());
				}

				workbook.close();

			} else {
				System.out.println("No ImpactAnalysis data found.");
				workbook.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return outputFile;
	}
    
    

    
}
