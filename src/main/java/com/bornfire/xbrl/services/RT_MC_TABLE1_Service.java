package com.bornfire.xbrl.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bornfire.xbrl.entities.RT_MC_TABLE1_ENTITY;
import com.bornfire.xbrl.entities.RT_MC_TABLE1_REPO;

@Service
public class RT_MC_TABLE1_Service {
	private static final Logger logger = LoggerFactory.getLogger(RT_MC_TABLE1_Service.class);

	@Autowired
	private Environment env;

	@Autowired
	RT_MC_TABLE1_REPO RT_MC_TABLE1_REPO;

	public byte[] generateReportFile(String branch) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		List<RT_MC_TABLE1_ENTITY> dataList = RT_MC_TABLE1_REPO.findBybranchcode(branch);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for MC report. Returning empty result.");
			return new byte[0];
		}
		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = "Table_1.xlsx";
		System.out.println(templateFileName);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			// This specific exception will be caught by the controller.
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			// A specific exception for permission errors.
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		// This try-with-resources block is perfect. It guarantees all resources are
		// closed automatically.
		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

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

			// Create the font
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 8); // size 8
			font.setFontName("Arial");

			CellStyle numberStyle = workbook.createCellStyle();
			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

			int startRow = 21;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					RT_MC_TABLE1_ENTITY record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row22
					// Column F
					Cell cell6 = row.createCell(5);
					if (record.getR22_NO_EMP_SPC_TP() != null) {
						cell6.setCellValue(record.getR22_NO_EMP_SPC_TP());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Column G
					Cell cell7 = row.createCell(6);
					if (record.getR22_NO_CON_SPC_TP() != null) {
						cell7.setCellValue(record.getR22_NO_CON_SPC_TP());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column H
					Cell cell8 = row.createCell(7);
					if (record.getR22_AVG_NO_EMP_SPC_TP() != null) {
						cell8.setCellValue(record.getR22_AVG_NO_EMP_SPC_TP());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Column I
					Cell cell9 = row.createCell(8);
					if (record.getR22_NO_BRN_SPC_TP() != null) {
						cell9.setCellValue(record.getR22_NO_BRN_SPC_TP());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Column J
					Cell cell10 = row.createCell(9);
					if (record.getR22_TOT_NO_BRN_POD() != null) {
						cell10.setCellValue(record.getR22_TOT_NO_BRN_POD());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Column K
					Cell cell11 = row.createCell(10);
					if (record.getR22_NO_ATM_SPC_TP() != null) {
						cell11.setCellValue(record.getR22_NO_ATM_SPC_TP());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Column L
					Cell cell12 = row.createCell(11);
					if (record.getR22_NO_ATM_DET_SPC_TP() != null) {
						cell12.setCellValue(record.getR22_NO_ATM_DET_SPC_TP());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Column M
					Cell cell13 = row.createCell(12);
					if (record.getR22_NO_AUT_SPC_TP() != null) {
						cell13.setCellValue(record.getR22_NO_AUT_SPC_TP());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}
					// Column N
					Cell cell14 = row.createCell(13);
					if (record.getR22_NO_INS_BNK_SPC_TP() != null) {
						cell14.setCellValue(record.getR22_NO_INS_BNK_SPC_TP());
						cell14.setCellStyle(numberStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}
					// Column O
					Cell cell15 = row.createCell(14);
					if (record.getR22_NO_INS_DWN_SPC_TP() != null) {
						cell15.setCellValue(record.getR22_NO_INS_DWN_SPC_TP());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}
					// Column P
					Cell cell16 = row.createCell(15);
					if (record.getR22_NO_EMP_RSK_SPC_DTE() != null) {
						cell16.setCellValue(record.getR22_NO_EMP_RSK_SPC_DTE());
						cell16.setCellStyle(numberStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}
					// Column Q
					Cell cell17 = row.createCell(16);
					if (record.getR22_NO_EXT_FRD_SPC_TP() != null) {
						cell17.setCellValue(record.getR22_NO_EXT_FRD_SPC_TP());
						cell17.setCellStyle(numberStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}
					// Column R
					Cell cell18 = row.createCell(17);
					if (record.getR22_NO_INT_FRD_SPC_TP() != null) {
						cell18.setCellValue(record.getR22_NO_INT_FRD_SPC_TP());
						cell18.setCellStyle(numberStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}
					// Column S
					Cell cell19 = row.createCell(18);
					if (record.getR22_NO_FRD_SPC_TP() != null) {
						cell19.setCellValue(record.getR22_NO_FRD_SPC_TP());
						cell19.setCellStyle(numberStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}
					// Column T
					Cell cell20 = row.createCell(19);
					if (record.getR22_NO_FRD_PP() != null) {
						cell20.setCellValue(record.getR22_NO_FRD_PP());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}
					// Column U
					Cell cell21 = row.createCell(20);
					if (record.getR22_TOT_REV_SPC_TP() != null) {
						cell21.setCellValue(record.getR22_TOT_REV_SPC_TP());
						cell21.setCellStyle(numberStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}
					// Column V
					Cell cell22 = row.createCell(21);
					if (record.getR22_TOT_INS_UNP_SPC_TP() != null) {
						cell22.setCellValue(record.getR22_TOT_INS_UNP_SPC_TP());
						cell22.setCellStyle(numberStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}
					// Column W
					Cell cell23 = row.createCell(22);
					if (record.getR22_TOT_NO_HRS_BNK() != null) {
						cell23.setCellValue(record.getR22_TOT_NO_HRS_BNK());
						cell23.setCellStyle(numberStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					} // Column X
					Cell cell24 = row.createCell(23);
					if (record.getR22_TOT_NO_INC_SPC_TP() != null) {
						cell24.setCellValue(record.getR22_TOT_NO_INC_SPC_TP());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					} // Column Y
					Cell cell25 = row.createCell(24);
					if (record.getR22_TOT_NO_PEN_SPC_TP() != null) {
						cell25.setCellValue(record.getR22_TOT_NO_PEN_SPC_TP());
						cell25.setCellStyle(numberStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					} // Column Z
					Cell cell26 = row.createCell(25);
					if (record.getR22_TOT_NO_AUT_SPC_TP() != null) {
						cell26.setCellValue(record.getR22_TOT_NO_AUT_SPC_TP());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					} // Column AA
					Cell cell27 = row.createCell(26);
					if (record.getR22_TOT_NO_SAL_SPC_TP() != null) {
						cell27.setCellValue(record.getR22_TOT_NO_SAL_SPC_TP());
						cell27.setCellStyle(numberStyle);
					} else {
						cell27.setCellValue("");
						cell27.setCellStyle(textStyle);
					} // Column AB
					Cell cell28 = row.createCell(27);
					if (record.getR22_TOT_NO_MER_SPC_TP() != null) {
						cell28.setCellValue(record.getR22_TOT_NO_MER_SPC_TP());
						cell28.setCellStyle(numberStyle);
					} else {
						cell28.setCellValue("");
						cell28.setCellStyle(textStyle);
					} // Column AC
					Cell cell29 = row.createCell(28);
					if (record.getR22_NO_INQ_SPC_TP() != null) {
						cell29.setCellValue(record.getR22_NO_INQ_SPC_TP());
						cell29.setCellStyle(numberStyle);
					} else {
						cell29.setCellValue("");
						cell29.setCellStyle(textStyle);
					} // Column AD
					Cell cell30 = row.createCell(29);
					if (record.getR22_NO_SER_SPC_TP() != null) {
						cell30.setCellValue(record.getR22_NO_SER_SPC_TP());
						cell30.setCellStyle(numberStyle);
					} else {
						cell30.setCellValue("");
						cell30.setCellStyle(textStyle);
					}

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}
}