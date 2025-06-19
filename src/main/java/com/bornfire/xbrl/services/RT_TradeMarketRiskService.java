package com.bornfire.xbrl.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

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

import com.bornfire.xbrl.entities.RT_TradeMarketRiskData;
import com.bornfire.xbrl.entities.RT_TradeMarketriskDataRepository;

@Service
public class RT_TradeMarketRiskService {

	@Autowired
	private Environment env;

	@Autowired
	private RT_TradeMarketriskDataRepository tradeMarketRiskDataRepo;

	public File generateTradeMarketRiskExcel() {
		File outputFile = null;

		try {
			List<RT_TradeMarketRiskData> dataList = tradeMarketRiskDataRepo.getlist();

			File templateFile = new File(
					env.getProperty("output.exportpathtemp") + "CBUAE_Trade_Market_Risk_Data_Template.xls");
			Workbook workbook = WorkbookFactory.create(new FileInputStream(templateFile));
			Sheet sheet = workbook.getSheetAt(0);

			// --- Style Definitions ---
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
			// --- End of Style Definitions ---

			int startRow = 2;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					RT_TradeMarketRiskData record = dataList.get(i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// --- Populate Cells Line by Line ---

					// Column 0: Date
					Cell cell0 = row.createCell(0);
					if (record.getReportDate() != null) {
						cell0.setCellValue(record.getReportDate());
						cell0.setCellStyle(dateStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

					// Column 1: Bank Name
					Cell cell1 = row.createCell(1);
					cell1.setCellValue(record.getBankName() != null ? record.getBankName() : "");
					cell1.setCellStyle(textStyle);

					// Column 2: Group/HO/Subsidiary
					Cell cell2 = row.createCell(2);
					cell2.setCellValue(
							record.getGroupHeadOfficeSubsidiary() != null ? record.getGroupHeadOfficeSubsidiary() : "");
					cell2.setCellStyle(textStyle);

					// Column 3: Subsidiary
					Cell cell3 = row.createCell(3);
					cell3.setCellValue(record.getSubsidiary() != null ? record.getSubsidiary() : "");
					cell3.setCellStyle(textStyle);

					// Column 4: Bank Symbol
					Cell cell4 = row.createCell(4);
					cell4.setCellValue(record.getBankSymbol() != null ? record.getBankSymbol() : "");
					cell4.setCellStyle(textStyle);

					// Column 5: Conventional/Islamic
					Cell cell5 = row.createCell(5);
					cell5.setCellValue(record.getConventionalIslamic() != null ? record.getConventionalIslamic() : "");
					cell5.setCellStyle(textStyle);

					// Column 6: Local/Foreign
					Cell cell6 = row.createCell(6);
					cell6.setCellValue(record.getLocalForeign() != null ? record.getLocalForeign() : "");
					cell6.setCellStyle(textStyle);

					// Column 7: CBUAE Tiering
					Cell cell7 = row.createCell(7);
					cell7.setCellValue(record.getCbuaeTiering() != null ? record.getCbuaeTiering() : "");
					cell7.setCellStyle(textStyle);

					// Column 8: Trading Book/Desks
					Cell cell8 = row.createCell(8);
					cell8.setCellValue(record.getTradingBookDesks() != null ? record.getTradingBookDesks() : "");
					cell8.setCellStyle(textStyle);

					// Column 9: CBUAE Internal Desk Reference
					Cell cell9 = row.createCell(9);
					cell9.setCellValue(
							record.getCbuaeInternalDeskReference() != null ? record.getCbuaeInternalDeskReference()
									: "");
					cell9.setCellStyle(textStyle);

					// Column 10: Annual Budget (AED)
					Cell cell10 = row.createCell(10);
					cell10.setCellValue(
							record.getAnnualBudgetAed() != null ? record.getAnnualBudgetAed().doubleValue() : 0.0);
					cell10.setCellStyle(numberStyle);

					// ... Continue this pattern for all columns ...

					Cell cell11 = row.createCell(11);
					cell11.setCellValue(record.getTradingDeskBookSizeMarketValueAed() != null
							? record.getTradingDeskBookSizeMarketValueAed().doubleValue()
							: 0.0);
					cell11.setCellStyle(numberStyle);

					Cell cell12 = row.createCell(12);
					cell12.setCellValue(record.getRelativeSizePercentTotalAssetsOrPercentTotalTradingBook() != null
							? record.getRelativeSizePercentTotalAssetsOrPercentTotalTradingBook().doubleValue()
							: 0.0);
					cell12.setCellStyle(numberStyle);

					Cell cell13 = row.createCell(13);
					cell13.setCellValue(record.getYtdPandLAed() != null ? record.getYtdPandLAed().doubleValue() : 0.0);
					cell13.setCellStyle(numberStyle);

					Cell cell14 = row.createCell(14);
					cell14.setCellValue(record.getMaximumLossOverThePeriodAed() != null
							? record.getMaximumLossOverThePeriodAed().doubleValue()
							: 0.0);
					cell14.setCellStyle(numberStyle);

					Cell cell15 = row.createCell(15);
					cell15.setCellValue(record.getConfidenceInterval() != null ? record.getConfidenceInterval() : "");
					cell15.setCellStyle(textStyle);

					Cell cell16 = row.createCell(16);
					cell16.setCellValue(
							record.getHoldingPeriod() != null ? record.getHoldingPeriod().doubleValue() : 0.0);
					cell16.setCellStyle(numberStyle);

					Cell cell17 = row.createCell(17);
					cell17.setCellValue(
							record.getVarExposureAed() != null ? record.getVarExposureAed().doubleValue() : 0.0);
					cell17.setCellStyle(numberStyle);

					Cell cell18 = row.createCell(18);
					cell18.setCellValue(record.getVarLimitAed() != null ? record.getVarLimitAed().doubleValue() : 0.0);
					cell18.setCellStyle(numberStyle);

					Cell cell19 = row.createCell(19);
					cell19.setCellValue(record.getStatus() != null ? record.getStatus() : "");
					cell19.setCellStyle(textStyle);

					Cell cell20 = row.createCell(20);
					cell20.setCellValue(
							record.getExpectedShortfallAed() != null ? record.getExpectedShortfallAed().doubleValue()
									: 0.0);
					cell20.setCellStyle(numberStyle);

					Cell cell21 = row.createCell(21);
					cell21.setCellValue(record.getExpectedShortfallLimitAed() != null
							? record.getExpectedShortfallLimitAed().doubleValue()
							: 0.0);
					cell21.setCellStyle(numberStyle);

					Cell cell22 = row.createCell(22);
					cell22.setCellValue(record.getStatus2() != null ? record.getStatus2() : "");
					cell22.setCellStyle(textStyle);

					Cell cell23 = row.createCell(23);
					cell23.setCellValue(
							record.getModifiedDuration() != null ? record.getModifiedDuration().doubleValue() : 0.0);
					cell23.setCellStyle(numberStyle);

					Cell cell24 = row.createCell(24);
					cell24.setCellValue(record.getInterestRateShockAppliedBps() != null
							? record.getInterestRateShockAppliedBps().doubleValue()
							: 0.0);
					cell24.setCellStyle(numberStyle);

					Cell cell25 = row.createCell(25);
					cell25.setCellValue(record.getLimit1() != null ? record.getLimit1().doubleValue() : 0.0);
					cell25.setCellStyle(numberStyle);

					Cell cell26 = row.createCell(26);
					cell26.setCellValue(record.getStatus3() != null ? record.getStatus3() : "");
					cell26.setCellStyle(textStyle);

					Cell cell27 = row.createCell(27);
					cell27.setCellValue(record.getDv01() != null ? record.getDv01().doubleValue() : 0.0);
					cell27.setCellStyle(numberStyle);

					Cell cell28 = row.createCell(28);
					cell28.setCellValue(record.getLimit2() != null ? record.getLimit2().doubleValue() : 0.0);
					cell28.setCellStyle(numberStyle);

					Cell cell29 = row.createCell(29);
					cell29.setCellValue(record.getStatus4() != null ? record.getStatus4() : "");
					cell29.setCellStyle(textStyle);

					Cell cell30 = row.createCell(30);
					cell30.setCellValue(
							record.getDv01TenorGapsLt3Yr() != null ? record.getDv01TenorGapsLt3Yr().doubleValue()
									: 0.0);
					cell30.setCellStyle(numberStyle);

					Cell cell31 = row.createCell(31);
					cell31.setCellValue(
							record.getDv01TenorGapsGt3Yr() != null ? record.getDv01TenorGapsGt3Yr().doubleValue()
									: 0.0);
					cell31.setCellStyle(numberStyle);

					Cell cell32 = row.createCell(32);
					cell32.setCellValue(
							record.getCreditSpreadCs01() != null ? record.getCreditSpreadCs01().doubleValue() : 0.0);
					cell32.setCellStyle(numberStyle);

					Cell cell33 = row.createCell(33);
					cell33.setCellValue(record.getCreditSpreadShockAppliedBps() != null
							? record.getCreditSpreadShockAppliedBps().doubleValue()
							: 0.0);
					cell33.setCellStyle(numberStyle);

					Cell cell34 = row.createCell(34);
					cell34.setCellValue(record.getLimit3() != null ? record.getLimit3().doubleValue() : 0.0);
					cell34.setCellStyle(numberStyle);

					Cell cell35 = row.createCell(35);
					cell35.setCellValue(record.getStatus5() != null ? record.getStatus5() : "");
					cell35.setCellStyle(textStyle);

					Cell cell36 = row.createCell(36);
					cell36.setCellValue(record.getCreditSpreadCs01InvestmentGrade() != null
							? record.getCreditSpreadCs01InvestmentGrade().doubleValue()
							: 0.0);
					cell36.setCellStyle(numberStyle);

					Cell cell37 = row.createCell(37);
					cell37.setCellValue(record.getCreditSpreadCs01SubInvestment() != null
							? record.getCreditSpreadCs01SubInvestment().doubleValue()
							: 0.0);
					cell37.setCellStyle(numberStyle);

					Cell cell38 = row.createCell(38);
					cell38.setCellValue(record.getCreditSpreadCs01Unrated() != null
							? record.getCreditSpreadCs01Unrated().doubleValue()
							: 0.0);
					cell38.setCellStyle(numberStyle);

					Cell cell39 = row.createCell(39);
					cell39.setCellValue(record.getBetaEquity() != null ? record.getBetaEquity().doubleValue() : 0.0);
					cell39.setCellStyle(numberStyle);

					Cell cell40 = row.createCell(40);
					cell40.setCellValue(
							record.getEquityShockAppliedBps() != null ? record.getEquityShockAppliedBps().doubleValue()
									: 0.0);
					cell40.setCellStyle(numberStyle);

					Cell cell41 = row.createCell(41);
					cell41.setCellValue(record.getLimit4() != null ? record.getLimit4().doubleValue() : 0.0);
					cell41.setCellStyle(numberStyle);

					Cell cell42 = row.createCell(42);
					cell42.setCellValue(record.getStatus6() != null ? record.getStatus6() : "");
					cell42.setCellStyle(textStyle);

					Cell cell43 = row.createCell(43);
					cell43.setCellValue(record.getBr01() != null ? record.getBr01().doubleValue() : 0.0);
					cell43.setCellStyle(numberStyle);

					Cell cell44 = row.createCell(44);
					cell44.setCellValue(record.getInterestRateDifferentialShockAppliedBps() != null
							? record.getInterestRateDifferentialShockAppliedBps().doubleValue()
							: 0.0);
					cell44.setCellStyle(numberStyle);

					Cell cell45 = row.createCell(45);
					cell45.setCellValue(record.getLimit5() != null ? record.getLimit5().doubleValue() : 0.0);
					cell45.setCellStyle(numberStyle);

					Cell cell46 = row.createCell(46);
					cell46.setCellValue(record.getStatus7() != null ? record.getStatus7() : "");
					cell46.setCellStyle(textStyle);

					Cell cell47 = row.createCell(47);
					cell47.setCellValue(record.getDollarDelta() != null ? record.getDollarDelta().doubleValue() : 0.0);
					cell47.setCellStyle(numberStyle);

					Cell cell48 = row.createCell(48);
					cell48.setCellValue(
							record.getShockAppliedBps1() != null ? record.getShockAppliedBps1().doubleValue() : 0.0);
					cell48.setCellStyle(numberStyle);

					Cell cell49 = row.createCell(49);
					cell49.setCellValue(record.getLimit6() != null ? record.getLimit6().doubleValue() : 0.0);
					cell49.setCellStyle(numberStyle);

					Cell cell50 = row.createCell(50);
					cell50.setCellValue(record.getStatus8() != null ? record.getStatus8() : "");
					cell50.setCellStyle(textStyle);

					Cell cell51 = row.createCell(51);
					cell51.setCellValue(record.getDollarGamma() != null ? record.getDollarGamma().doubleValue() : 0.0);
					cell51.setCellStyle(numberStyle);

					Cell cell52 = row.createCell(52);
					cell52.setCellValue(
							record.getShockAppliedBps2() != null ? record.getShockAppliedBps2().doubleValue() : 0.0);
					cell52.setCellStyle(numberStyle);

					Cell cell53 = row.createCell(53);
					cell53.setCellValue(record.getLimit7() != null ? record.getLimit7().doubleValue() : 0.0);
					cell53.setCellStyle(numberStyle);

					Cell cell54 = row.createCell(54);
					cell54.setCellValue(record.getStatus9() != null ? record.getStatus9() : "");
					cell54.setCellStyle(textStyle);

					Cell cell55 = row.createCell(55);
					cell55.setCellValue(record.getDollarVega() != null ? record.getDollarVega().doubleValue() : 0.0);
					cell55.setCellStyle(numberStyle);

					Cell cell56 = row.createCell(56);
					cell56.setCellValue(
							record.getImpliedVolatilityType() != null ? record.getImpliedVolatilityType() : "");
					cell56.setCellStyle(textStyle);

					Cell cell57 = row.createCell(57);
					cell57.setCellValue(
							record.getShockAppliedBps3() != null ? record.getShockAppliedBps3().doubleValue() : 0.0);
					cell57.setCellStyle(numberStyle);

					Cell cell58 = row.createCell(58);
					cell58.setCellValue(record.getLimit8() != null ? record.getLimit8().doubleValue() : 0.0);
					cell58.setCellStyle(numberStyle);

					Cell cell59 = row.createCell(59);
					cell59.setCellValue(record.getStatus10() != null ? record.getStatus10() : "");
					cell59.setCellStyle(textStyle);

					Cell cell60 = row.createCell(60);
					cell60.setCellValue(record.getDollarTheta() != null ? record.getDollarTheta().doubleValue() : 0.0);
					cell60.setCellStyle(numberStyle);

					Cell cell61 = row.createCell(61);
					cell61.setCellValue(record.getLimit9() != null ? record.getLimit9().doubleValue() : 0.0);
					cell61.setCellStyle(numberStyle);

					Cell cell62 = row.createCell(62);
					cell62.setCellValue(record.getStatus11() != null ? record.getStatus11() : "");
					cell62.setCellStyle(textStyle);

					Cell cell63 = row.createCell(63);
					cell63.setCellValue(record.getDollarRho() != null ? record.getDollarRho().doubleValue() : 0.0);
					cell63.setCellStyle(numberStyle);

					Cell cell64 = row.createCell(64);
					cell64.setCellValue(
							record.getShockAppliedBps4() != null ? record.getShockAppliedBps4().doubleValue() : 0.0);
					cell64.setCellStyle(numberStyle);

					Cell cell65 = row.createCell(65);
					cell65.setCellValue(record.getLimit10() != null ? record.getLimit10().doubleValue() : 0.0);
					cell65.setCellStyle(numberStyle);

					Cell cell66 = row.createCell(66);
					cell66.setCellValue(record.getStatus12() != null ? record.getStatus12() : "");
					cell66.setCellStyle(textStyle);

					Cell cell67 = row.createCell(67);
					cell67.setCellValue(record.getDollarVanna() != null ? record.getDollarVanna().doubleValue() : 0.0);
					cell67.setCellStyle(numberStyle);

					Cell cell68 = row.createCell(68);
					cell68.setCellValue(
							record.getShockAppliedBps5() != null ? record.getShockAppliedBps5().doubleValue() : 0.0);
					cell68.setCellStyle(numberStyle);

					Cell cell69 = row.createCell(69);
					cell69.setCellValue(record.getLimit11() != null ? record.getLimit11().doubleValue() : 0.0);
					cell69.setCellStyle(numberStyle);

					Cell cell70 = row.createCell(70);
					cell70.setCellValue(record.getStatus13() != null ? record.getStatus13() : "");
					cell70.setCellStyle(textStyle);

					Cell cell71 = row.createCell(71);
					cell71.setCellValue(record.getDollarVolga() != null ? record.getDollarVolga().doubleValue() : 0.0);
					cell71.setCellStyle(numberStyle);

					Cell cell72 = row.createCell(72);
					cell72.setCellValue(
							record.getShockAppliedBps6() != null ? record.getShockAppliedBps6().doubleValue() : 0.0);
					cell72.setCellStyle(numberStyle);

					Cell cell73 = row.createCell(73);
					cell73.setCellValue(record.getLimit12() != null ? record.getLimit12().doubleValue() : 0.0);
					cell73.setCellStyle(numberStyle);

					Cell cell74 = row.createCell(74);
					cell74.setCellValue(record.getStatus14() != null ? record.getStatus14() : "");
					cell74.setCellStyle(textStyle);

					Cell cell75 = row.createCell(75);
					cell75.setCellValue(
							record.getCreditSpreadAed() != null ? record.getCreditSpreadAed().doubleValue() : 0.0);
					cell75.setCellStyle(numberStyle);

					Cell cell76 = row.createCell(76);
					cell76.setCellValue(record.getCreditSensitiveImpactPercent() != null
							? record.getCreditSensitiveImpactPercent().doubleValue()
							: 0.0);
					cell76.setCellStyle(numberStyle);

					Cell cell77 = row.createCell(77);
					cell77.setCellValue(record.getInterestRateInTheTradingBookAed() != null
							? record.getInterestRateInTheTradingBookAed().doubleValue()
							: 0.0);
					cell77.setCellStyle(numberStyle);

					Cell cell78 = row.createCell(78);
					cell78.setCellValue(record.getRateSensitiveImpactPercent() != null
							? record.getRateSensitiveImpactPercent().doubleValue()
							: 0.0);
					cell78.setCellStyle(numberStyle);

					Cell cell79 = row.createCell(79);
					cell79.setCellValue(
							record.getForeignExchangeAed() != null ? record.getForeignExchangeAed().doubleValue()
									: 0.0);
					cell79.setCellStyle(numberStyle);

					Cell cell80 = row.createCell(80);
					cell80.setCellValue(record.getFxSensitiveImpactPercent() != null
							? record.getFxSensitiveImpactPercent().doubleValue()
							: 0.0);
					cell80.setCellStyle(numberStyle);

					Cell cell81 = row.createCell(81);
					cell81.setCellValue(record.getEquityAed() != null ? record.getEquityAed().doubleValue() : 0.0);
					cell81.setCellStyle(numberStyle);

					Cell cell82 = row.createCell(82);
					cell82.setCellValue(record.getEquitySensitiveRelativeImpactPercent() != null
							? record.getEquitySensitiveRelativeImpactPercent().doubleValue()
							: 0.0);
					cell82.setCellStyle(numberStyle);

					Cell cell83 = row.createCell(83);
					cell83.setCellValue(
							record.getCommoditiesAed() != null ? record.getCommoditiesAed().doubleValue() : 0.0);
					cell83.setCellStyle(numberStyle);

					Cell cell84 = row.createCell(84);
					cell84.setCellValue(record.getCommoditySensitiveRelativeImpactPercent() != null
							? record.getCommoditySensitiveRelativeImpactPercent().doubleValue()
							: 0.0);
					cell84.setCellStyle(numberStyle);

					Cell cell85 = row.createCell(85);
					cell85.setCellValue(
							record.getCreditDerivativesAed() != null ? record.getCreditDerivativesAed().doubleValue()
									: 0.0);
					cell85.setCellStyle(numberStyle);

					Cell cell86 = row.createCell(86);
					cell86.setCellValue(
							record.getRelativeImpactPercent() != null ? record.getRelativeImpactPercent().doubleValue()
									: 0.0);
					cell86.setCellStyle(numberStyle);

					Cell cell87 = row.createCell(87);
					cell87.setCellValue(
							record.getJumpToDefaultLossAed() != null ? record.getJumpToDefaultLossAed().doubleValue()
									: 0.0);
					cell87.setCellStyle(numberStyle);

					Cell cell88 = row.createCell(88);
					cell88.setCellValue(record.getJtdRelativeImpactPercent() != null
							? record.getJtdRelativeImpactPercent().doubleValue()
							: 0.0);
					cell88.setCellStyle(numberStyle);

					Cell cell89 = row.createCell(89);
					cell89.setCellValue(
							record.getOverallImpactAed() != null ? record.getOverallImpactAed().doubleValue() : 0.0);
					cell89.setCellStyle(numberStyle);

					Cell cell90 = row.createCell(90);
					cell90.setCellValue(record.getOverallRelativeImpactPercent() != null
							? record.getOverallRelativeImpactPercent().doubleValue()
							: 0.0);
					cell90.setCellStyle(numberStyle);
				}

				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

				outputFile = new File(env.getProperty("output.exportpathfinal") + "TradeMarketRiskData.xls");
				try (FileOutputStream fos = new FileOutputStream(outputFile)) {
					workbook.write(fos);
					System.out
							.println("Trade Market Risk Excel generated successfully: " + outputFile.getAbsolutePath());
				}

			} else {
				System.out.println("No Trade Market Risk data found to generate the Excel file.");
			}

			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return outputFile;
	}
}