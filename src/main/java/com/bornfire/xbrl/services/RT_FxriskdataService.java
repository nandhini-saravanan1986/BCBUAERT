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
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bornfire.xbrl.entities.RT_FxRiskDataRepository;
import com.bornfire.xbrl.entities.RT_Fxriskdata;

@Service
public class RT_FxriskdataService {

	@Autowired
	private Environment env;

	@Autowired
	private RT_FxRiskDataRepository friskdataRepo;

	@Autowired
	private SessionFactory sessionFactory;

	public boolean updateFxriskdata(RT_Fxriskdata updatedData) {
		Optional<RT_Fxriskdata> existingOpt = friskdataRepo.findById(updatedData.getBank_date()); // or your correct
																										// @Id field

		if (existingOpt.isPresent()) {
			RT_Fxriskdata existing = existingOpt.get();

			// Only update fields that should be updated:
			existing.setBank_name(null);

			// Save updated entity
			friskdataRepo.save(existing);
			return true;
		} else {
			return false; // record not found
		}
	}

	public File generateFxRiskExcel() {
		File outputFile = null;

		try {
			// Fetch data from repository
			List<Object[]> fxDataList = friskdataRepo.getfxriskdatalistdata1();

			// Load Excel template file
			File templateFile = new File(env.getProperty("output.exportpathtemp") + "CBUAE_FX_Risk_Data_Template.xls");
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

					// 2 - String
					Cell cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					cell2.setCellValue(fx[2] == null ? "" : fx[2].toString());

					// 3 - String
					Cell cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					cell3.setCellValue(fx[3] == null ? "" : fx[3].toString());

					// 4 - String
					Cell cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					cell4.setCellValue(fx[4] == null ? "" : fx[4].toString());

					// 5 - String
					Cell cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					cell5.setCellValue(fx[5] == null ? "" : fx[5].toString());

					// 6 - String
					Cell cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					cell6.setCellValue(fx[6] == null ? "" : fx[6].toString());

					// 7 - BigDecimal
					Cell cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					cell7.setCellValue(fx[7] instanceof BigDecimal ? ((BigDecimal) fx[7]).doubleValue() : 0);

					// 8 - BigDecimal
					Cell cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					cell8.setCellValue(fx[8] instanceof BigDecimal ? ((BigDecimal) fx[8]).doubleValue() : 0);

					// 9 - BigDecimal
					Cell cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					cell9.setCellValue(fx[9] instanceof BigDecimal ? ((BigDecimal) fx[9]).doubleValue() : 0);

					// 10 - BigDecimal
					Cell cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					cell10.setCellValue(fx[10] instanceof BigDecimal ? ((BigDecimal) fx[10]).doubleValue() : 0);

					// 11 - BigDecimal
					Cell cell11 = row.getCell(11);
					if (cell11 == null)
						cell11 = row.createCell(11);
					cell11.setCellValue(fx[11] instanceof BigDecimal ? ((BigDecimal) fx[11]).doubleValue() : 0);

					// 12 - String
					Cell cell12 = row.getCell(12);
					if (cell12 == null)
						cell12 = row.createCell(12);
					cell12.setCellValue(fx[12] == null ? "" : fx[12].toString());

					// 13 - BigDecimal
					Cell cell13 = row.getCell(13);
					if (cell13 == null)
						cell13 = row.createCell(13);
					cell13.setCellValue(fx[13] instanceof BigDecimal ? ((BigDecimal) fx[13]).doubleValue() : 0);

					// 14 - String
					Cell cell14 = row.getCell(14);
					if (cell14 == null)
						cell14 = row.createCell(14);
					cell14.setCellValue(fx[14] == null ? "" : fx[14].toString());

					// 15 - BigDecimal
					Cell cell15 = row.getCell(15);
					if (cell15 == null)
						cell15 = row.createCell(15);
					cell15.setCellValue(fx[15] instanceof BigDecimal ? ((BigDecimal) fx[15]).doubleValue() : 0);

					// 16 - BigDecimal
					Cell cell16 = row.getCell(16);
					if (cell16 == null)
						cell16 = row.createCell(16);
					cell16.setCellValue(fx[16] instanceof BigDecimal ? ((BigDecimal) fx[16]).doubleValue() : 0);

					// 17 - BigDecimal
					Cell cell17 = row.getCell(17);
					if (cell17 == null)
						cell17 = row.createCell(17);
					cell17.setCellValue(fx[17] instanceof BigDecimal ? ((BigDecimal) fx[17]).doubleValue() : 0);

					// Add more columns as needed...
				}

				// Evaluate all formulas in the workbook
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

				// Write output to file
				outputFile = new File(env.getProperty("output.exportpathfinal") + "FxRiskData.xls");
				try (FileOutputStream fos = new FileOutputStream(outputFile)) {
					workbook.write(fos);
					System.out.println("FxRisk Excel generated: " + outputFile.getAbsolutePath());
				}

				workbook.close();

			} else {
				System.out.println("No FX Risk data found.");
				workbook.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return outputFile;
	}

}
