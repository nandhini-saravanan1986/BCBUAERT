package com.bornfire.xbrl.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bornfire.xbrl.entities.*;
import com.microsoft.schemas.office.visio.x2012.main.CellType;

@Service
public class RT_MC_TABLE_ALL_Service {
	private static final Logger logger = LoggerFactory.getLogger(RT_MC_TABLE_ALL_Service.class);

	@Autowired
	private Environment env;

	@Autowired
	AuditService auditservice;

	@Autowired
	RT_MC_TABLE1_REPO RT_MC_TABLE1_REPO;
	@Autowired
	RT_MC_TABLE2_1_REPO RT_MC_TABLE2_1_REPO;
	@Autowired
	RT_MC_TABLE2_2_REPO RT_MC_TABLE2_2_REPO;
	@Autowired
	RT_MC_TABLE3_REPO RT_MC_TABLE3_REPO;
	@Autowired
	RT_MC_TABLE4_1_REPO RT_MC_TABLE4_1_REPO;
	@Autowired
	RT_MC_TABLE4_2_REPO RT_MC_TABLE4_2_REPO;
	@Autowired
	RT_MC_TABLE5_REPO RT_MC_TABLE5_REPO;
	@Autowired
	RT_MC_TABLE6_REPO RT_MC_TABLE6_REPO;
	@Autowired
	RT_MC_TABLE7_1_REPO RT_MC_TABLE7_1_REPO;
	@Autowired
	RT_MC_TABLE7_2_REPO RT_MC_TABLE7_2_REPO;
	@Autowired
	RT_MC_TABLE8_REPO RT_MC_TABLE8_REPO;
	@Autowired
	RT_MC_TABLE9_REPO RT_MC_TABLE9_REPO;

	String templateFileName = "1.Main_RBS_MC_Bank of Baroda_Annual_Data Submission.xlsx";

	public byte[] generateReportFile(String branch, String jobId, Map<String, Integer> progressMap, String formmode,
			String userid, ServletRequestAttributes attr) throws Exception {

		byte[] file = null;

		if ("bankinformation".equalsIgnoreCase(formmode) || formmode == null) {
			auditservice.createBusinessAuditbackground(userid, "DOWNLOAD", "RBS_MC_TABLE1_Bank_Information", null,
					"RT_MC_TABLE1", attr);
			file = GenerateTable_1_Excel(branch, jobId, progressMap, formmode);
		} else if ("bankconsumers".equalsIgnoreCase(formmode)) {
			auditservice.createBusinessAuditbackground(userid, "DOWNLOAD", "RBS_MC_TABLE2_Bank_Consumers", null,
					"RT_MC_TABLE2_1 AND RT_MC_TABLE2_2", attr);
			file = GenerateTable_2_Excel(branch, jobId, progressMap, formmode);
		} else if ("complaints".equalsIgnoreCase(formmode)) {
			auditservice.createBusinessAuditbackground(userid, "DOWNLOAD", "RBS_MC_TABLE3_Complaints", null, "RT_MC_TABLE3", attr);
			file = GenerateTable_3_Excel(branch, jobId, progressMap, formmode);
		} else if ("retailproducts".equalsIgnoreCase(formmode)) {
			auditservice.createBusinessAuditbackground(userid, "DOWNLOAD", "RBS_MC_TABLE4_Retail_Products", null,
					"RT_MC_TABLE4_1 AND RT_MC_TABLE4_2", attr);
			file = GenerateTable_4_Excel(branch, jobId, progressMap, formmode);
		} else if ("bankemployee".equalsIgnoreCase(formmode)) {
			auditservice.createBusinessAuditbackground(userid, "DOWNLOAD", "RBS_MC_TABLE5_Bank_Employee", null, "RT_MC_TABLE5", attr);
			file = GenerateTable_5_Excel(branch, jobId, progressMap, formmode);
		} else if ("trainings".equalsIgnoreCase(formmode)) {
			auditservice.createBusinessAuditbackground(userid, "DOWNLOAD", "RBS_MC_TABLE6_Trainings", null, "RT_MC_TABLE6", attr);
			file = GenerateTable_6_Excel(branch, jobId, progressMap, formmode);
		} else if ("additionalinformation".equalsIgnoreCase(formmode)) {
			auditservice.createBusinessAuditbackground(userid, "DOWNLOAD", "RBS_MC_TABLE7_Additional_Information", null,
					"RT_MC_TABLE7_1 AND RT_MC_TABLE7_2", attr);
			file = GenerateTable_7_Excel(branch, jobId, progressMap, formmode);
		} else if ("islamicbanking".equalsIgnoreCase(formmode)) {
			auditservice.createBusinessAuditbackground(userid, "DOWNLOAD", "RBS_MC_TABLE8_Islamic_Banking", null, "RT_MC_TABLE8", attr);
			file = GenerateTable_8_Excel(branch, jobId, progressMap, formmode);
		} else if ("conductcultureassessment".equalsIgnoreCase(formmode)) {
			auditservice.createBusinessAuditbackground(userid, "DOWNLOAD", "RBS_MC_TABLE9_Conduct_Culture_Assessment", null,
					"RT_MC_TABLE9", attr);
			file = GenerateTable_9_Excel(branch, jobId, progressMap, formmode);
		}

		return file;
	}

	public byte[] GenerateTable_1_Excel(String branch, String jobId, Map<String, Integer> progressMap, String formmode)
			throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		List<RT_MC_TABLE1_ENTITY> dataList = RT_MC_TABLE1_REPO.findBybranchcode(branch);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for MC report. Returning empty result.");
			return new byte[0];
		}
		String templateDir = env.getProperty("output.exportpathtemp");
		// String templateFileName = "Table_1.xlsx";
		System.out.println(templateFileName);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(3);

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
					progressMap.put(jobId, 5);
					updateCellPreserveStyle(row, 5, record.getR22_NO_EMP_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR22_NO_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR22_AVG_NO_EMP_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR22_NO_BRN_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR22_TOT_NO_BRN_POD(), null, createHelper);
					progressMap.put(jobId, 15);
					updateCellPreserveStyle(row, 10, record.getR22_NO_ATM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR22_NO_ATM_DET_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR22_NO_AUT_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR22_NO_INS_BNK_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR22_NO_INS_DWN_SPC_TP(), null, createHelper);
					progressMap.put(jobId, 35);
					updateCellPreserveStyle(row, 15, record.getR22_NO_EMP_RSK_SPC_DTE(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR22_NO_EXT_FRD_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR22_NO_INT_FRD_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR22_NO_FRD_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR22_NO_FRD_PP(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR22_TOT_REV_SPC_TP(), null, createHelper);
					progressMap.put(jobId, 65);
					updateCellPreserveStyle(row, 21, record.getR22_TOT_INS_UNP_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR22_TOT_NO_HRS_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR22_TOT_NO_INC_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR22_TOT_NO_PEN_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR22_TOT_NO_AUT_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR22_TOT_NO_SAL_SPC_TP(), null, createHelper);
					progressMap.put(jobId, 85);
					updateCellPreserveStyle(row, 27, record.getR22_TOT_NO_MER_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR22_NO_INQ_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR22_NO_SER_SPC_TP(), null, createHelper);

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	public byte[] GenerateTable_2_Excel(String branch, String jobId, Map<String, Integer> progressMap, String formmode)
			throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		List<RT_MC_TABLE2_1_ENTITY> dataList1 = RT_MC_TABLE2_1_REPO.findBybranchcode(branch);
		List<RT_MC_TABLE2_2_ENTITY> dataList2 = RT_MC_TABLE2_2_REPO.findBybranchcode(branch);

		if (dataList1.isEmpty() || dataList2.isEmpty()) {
			logger.warn("Service: No data found for MC report. Returning empty result.");
			return new byte[0];
		}
		String templateDir = env.getProperty("output.exportpathtemp");
		// String templateFileName = "Table_2.xlsx";
		System.out.println(templateFileName);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(4);

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

			int startRow = 5;

			if (!dataList1.isEmpty() && !dataList2.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					RT_MC_TABLE2_1_ENTITY record1 = dataList1.get(i);
					RT_MC_TABLE2_2_ENTITY record2 = dataList2.get(i);
					System.out.println("rownumber=" + (startRow + i));
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					progressMap.put(jobId, 5);
					updateCellPreserveStyle(row, 5, record1.getR6_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR6_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR6_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR6_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR6_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR6_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR6_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR6_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR6_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR6_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR6_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR6_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR6_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR6_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR6_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR6_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR6_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR6_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR6_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR6_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR6_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR6_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR6_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR6_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR6_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR6_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR6_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR6_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR6_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR6_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR6_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR6_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR6_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR6_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR6_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR6_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR6_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR6_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR6_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR6_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR6_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR6_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR6_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record1.getR6_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record1.getR6_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record1.getR6_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR6_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR6_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR6_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR6_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR6_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR6_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR6_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR6_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR6_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR6_NO_CON_END(), null, createHelper);

					row = sheet.getRow(6);
					updateCellPreserveStyle(row, 5, record1.getR7_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR7_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR7_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR7_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR7_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR7_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR7_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR7_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR7_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR7_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR7_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR7_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR7_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR7_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR7_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR7_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR7_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR7_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR7_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR7_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR7_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR7_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR7_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR7_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR7_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR7_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR7_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR7_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR7_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR7_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR7_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR7_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR7_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR7_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR7_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR7_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR7_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR7_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR7_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR7_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR7_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR7_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR7_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record1.getR7_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record1.getR7_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record1.getR7_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR7_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR7_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR7_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR7_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR7_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR7_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR7_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR7_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR7_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR7_NO_CON_END(), null, createHelper);

					progressMap.put(jobId, 15);
					row = sheet.getRow(7);
					updateCellPreserveStyle(row, 5, record1.getR8_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR8_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR8_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR8_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR8_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR8_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR8_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR8_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR8_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR8_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR8_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR8_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR8_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR8_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR8_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR8_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR8_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR8_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR8_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR8_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR8_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR8_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR8_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR8_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR8_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR8_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR8_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR8_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR8_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR8_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR8_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR8_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR8_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR8_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR8_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR8_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR8_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR8_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR8_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR8_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR8_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR8_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR8_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record1.getR8_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record1.getR8_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record1.getR8_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR8_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR8_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR8_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR8_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR8_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR8_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR8_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR8_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR8_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR8_NO_CON_END(), null, createHelper);

					row = sheet.getRow(8);
					updateCellPreserveStyle(row, 5, record1.getR9_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR9_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR9_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR9_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR9_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR9_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR9_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR9_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR9_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR9_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR9_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR9_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR9_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR9_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR9_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR9_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR9_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR9_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR9_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR9_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR9_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR9_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR9_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR9_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR9_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR9_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR9_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR9_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR9_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR9_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR9_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR9_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR9_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR9_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR9_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR9_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR9_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR9_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR9_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR9_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR9_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR9_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR9_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record1.getR9_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record1.getR9_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record1.getR9_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR9_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR9_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR9_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR9_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR9_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR9_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR9_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR9_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR9_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR9_NO_CON_END(), null, createHelper);

					progressMap.put(jobId, 25);
					row = sheet.getRow(9);
					updateCellPreserveStyle(row, 5, record1.getR10_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR10_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR10_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR10_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR10_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR10_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR10_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR10_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR10_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR10_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR10_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR10_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR10_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR10_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR10_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR10_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR10_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR10_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR10_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR10_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR10_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR10_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR10_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR10_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR10_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR10_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR10_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR10_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR10_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR10_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR10_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR10_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR10_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR10_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR10_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR10_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR10_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR10_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR10_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR10_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR10_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR10_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR10_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record1.getR10_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record1.getR10_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record1.getR10_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR10_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR10_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR10_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR10_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR10_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR10_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR10_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR10_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR10_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR10_NO_CON_END(), null, createHelper);

					row = sheet.getRow(10);
					updateCellPreserveStyle(row, 5, record1.getR11_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR11_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR11_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR11_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR11_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR11_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR11_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR11_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR11_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR11_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR11_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR11_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR11_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR11_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR11_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR11_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR11_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR11_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR11_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR11_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR11_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR11_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR11_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR11_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR11_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR11_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR11_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR11_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR11_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR11_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR11_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR11_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR11_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR11_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR11_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR11_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR11_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR11_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR11_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR11_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR11_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR11_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR11_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record1.getR11_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record1.getR11_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record1.getR11_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR11_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR11_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR11_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR11_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR11_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR11_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR11_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR11_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR11_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR11_NO_CON_END(), null, createHelper);

					progressMap.put(jobId, 35);
					row = sheet.getRow(11);
					updateCellPreserveStyle(row, 5, record1.getR12_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR12_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR12_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR12_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR12_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR12_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR12_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR12_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR12_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR12_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR12_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR12_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR12_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR12_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR12_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR12_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR12_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR12_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR12_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR12_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR12_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR12_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR12_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR12_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR12_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR12_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR12_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR12_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR12_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR12_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR12_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR12_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR12_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR12_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR12_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR12_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR12_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR12_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR12_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR12_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR12_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR12_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR12_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record1.getR12_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record1.getR12_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record1.getR12_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR12_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR12_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR12_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR12_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR12_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR12_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR12_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR12_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR12_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR12_NO_CON_END(), null, createHelper);

					row = sheet.getRow(12);
					updateCellPreserveStyle(row, 5, record1.getR13_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR13_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR13_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR13_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR13_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR13_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR13_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR13_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR13_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR13_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR13_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR13_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR13_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR13_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR13_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR13_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR13_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR13_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR13_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR13_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR13_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR13_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR13_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR13_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR13_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR13_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR13_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR13_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR13_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR13_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR13_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR13_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR13_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR13_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR13_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR13_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR13_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR13_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR13_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR13_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR13_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR13_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR13_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record1.getR13_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record1.getR13_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record1.getR13_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR13_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR13_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR13_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR13_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR13_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR13_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR13_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR13_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR13_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR13_NO_CON_END(), null, createHelper);

					progressMap.put(jobId, 45);
					row = sheet.getRow(13);
					updateCellPreserveStyle(row, 5, record1.getR14_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR14_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR14_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR14_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR14_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR14_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR14_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR14_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR14_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR14_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR14_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR14_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR14_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR14_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR14_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR14_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR14_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR14_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR14_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR14_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR14_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR14_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR14_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR14_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR14_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR14_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR14_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR14_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR14_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR14_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR14_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR14_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR14_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR14_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR14_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR14_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR14_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR14_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR14_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR14_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR14_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR14_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR14_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record1.getR14_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record1.getR14_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record1.getR14_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR14_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR14_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR14_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR14_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR14_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR14_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR14_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR14_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR14_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR14_NO_CON_END(), null, createHelper);

					row = sheet.getRow(14);
					updateCellPreserveStyle(row, 5, record2.getR15_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR15_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR15_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR15_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR15_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR15_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR15_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR15_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR15_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR15_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR15_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR15_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR15_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR15_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR15_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR15_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR15_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR15_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR15_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR15_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR15_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR15_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR15_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR15_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR15_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR15_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR15_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR15_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR15_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR15_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR15_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR15_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR15_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR15_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR15_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR15_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR15_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR15_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR15_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR15_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR15_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR15_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR15_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record2.getR15_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record2.getR15_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record2.getR15_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR15_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR15_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR15_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR15_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR15_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR15_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR15_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR15_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR15_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR15_NO_CON_END(), null, createHelper);

					progressMap.put(jobId, 55);
					row = sheet.getRow(15);
					updateCellPreserveStyle(row, 5, record2.getR16_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR16_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR16_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR16_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR16_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR16_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR16_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR16_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR16_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR16_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR16_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR16_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR16_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR16_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR16_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR16_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR16_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR16_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR16_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR16_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR16_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR16_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR16_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR16_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR16_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR16_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR16_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR16_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR16_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR16_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR16_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR16_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR16_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR16_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR16_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR16_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR16_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR16_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR16_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR16_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR16_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR16_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR16_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record2.getR16_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record2.getR16_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record2.getR16_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR16_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR16_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR16_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR16_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR16_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR16_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR16_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR16_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR16_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR16_NO_CON_END(), null, createHelper);

					row = sheet.getRow(16);
					updateCellPreserveStyle(row, 5, record2.getR17_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR17_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR17_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR17_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR17_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR17_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR17_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR17_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR17_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR17_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR17_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR17_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR17_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR17_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR17_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR17_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR17_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR17_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR17_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR17_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR17_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR17_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR17_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR17_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR17_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR17_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR17_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR17_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR17_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR17_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR17_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR17_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR17_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR17_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR17_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR17_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR17_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR17_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR17_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR17_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR17_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR17_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR17_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record2.getR17_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record2.getR17_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record2.getR17_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR17_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR17_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR17_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR17_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR17_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR17_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR17_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR17_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR17_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR17_NO_CON_END(), null, createHelper);

					progressMap.put(jobId, 65);
					row = sheet.getRow(17);
					updateCellPreserveStyle(row, 5, record2.getR18_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR18_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR18_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR18_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR18_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR18_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR18_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR18_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR18_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR18_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR18_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR18_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR18_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR18_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR18_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR18_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR18_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR18_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR18_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR18_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR18_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR18_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR18_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR18_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR18_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR18_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR18_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR18_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR18_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR18_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR18_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR18_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR18_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR18_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR18_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR18_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR18_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR18_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR18_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR18_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR18_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR18_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR18_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record2.getR18_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record2.getR18_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record2.getR18_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR18_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR18_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR18_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR18_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR18_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR18_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR18_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR18_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR18_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR18_NO_CON_END(), null, createHelper);

					row = sheet.getRow(18);
					updateCellPreserveStyle(row, 5, record2.getR19_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR19_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR19_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR19_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR19_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR19_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR19_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR19_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR19_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR19_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR19_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR19_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR19_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR19_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR19_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR19_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR19_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR19_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR19_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR19_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR19_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR19_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR19_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR19_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR19_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR19_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR19_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR19_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR19_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR19_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR19_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR19_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR19_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR19_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR19_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR19_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR19_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR19_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR19_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR19_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR19_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR19_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR19_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record2.getR19_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record2.getR19_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record2.getR19_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR19_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR19_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR19_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR19_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR19_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR19_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR19_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR19_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR19_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR19_NO_CON_END(), null, createHelper);

					progressMap.put(jobId, 75);
					row = sheet.getRow(19);
					updateCellPreserveStyle(row, 5, record2.getR20_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR20_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR20_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR20_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR20_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR20_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR20_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR20_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR20_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR20_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR20_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR20_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR20_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR20_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR20_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR20_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR20_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR20_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR20_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR20_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR20_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR20_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR20_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR20_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR20_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR20_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR20_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR20_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR20_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR20_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR20_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR20_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR20_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR20_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR20_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR20_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR20_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR20_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR20_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR20_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR20_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR20_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR20_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record2.getR20_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record2.getR20_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record2.getR20_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR20_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR20_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR20_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR20_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR20_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR20_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR20_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR20_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR20_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR20_NO_CON_END(), null, createHelper);

					row = sheet.getRow(20);
					updateCellPreserveStyle(row, 5, record2.getR21_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR21_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR21_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR21_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR21_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR21_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR21_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR21_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR21_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR21_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR21_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR21_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR21_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR21_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR21_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR21_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR21_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR21_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR21_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR21_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR21_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR21_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR21_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR21_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR21_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR21_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR21_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR21_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR21_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR21_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR21_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR21_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR21_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR21_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR21_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR21_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR21_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR21_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR21_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR21_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR21_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR21_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR21_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record2.getR21_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record2.getR21_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record2.getR21_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR21_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR21_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR21_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR21_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR21_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR21_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR21_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR21_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR21_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR21_NO_CON_END(), null, createHelper);

					progressMap.put(jobId, 85);
					row = sheet.getRow(21);
					updateCellPreserveStyle(row, 5, record2.getR22_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR22_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR22_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR22_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR22_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR22_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR22_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR22_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR22_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR22_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR22_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR22_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR22_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR22_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR22_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR22_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR22_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR22_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR22_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR22_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR22_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR22_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR22_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR22_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR22_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR22_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR22_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR22_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR22_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR22_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR22_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR22_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR22_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR22_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR22_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR22_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR22_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR22_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR22_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR22_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR22_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR22_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR22_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record2.getR22_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record2.getR22_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record2.getR22_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR22_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR22_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR22_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR22_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR22_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR22_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR22_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR22_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR22_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR22_NO_CON_END(), null, createHelper);

					row = sheet.getRow(22);
					updateCellPreserveStyle(row, 5, record2.getR23_NO_WO_SD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR23_NO_CON_POD_SD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR23_NO_CON_UNAU_SP_TP(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR23_NO_CON_45_50(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR23_NO_CON_50(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR23_NO_OF_LES_50(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR23_NO_OF_BNK_18(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR23_NO_OF_BNK_18_25(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR23_NO_OF_BNK_26_35(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR23_NO_OF_BNK_36_50(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR23_NO_CON_BNK_50(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR23_NO_CON_SAL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR23_NO_CON_BUS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR23_NO_CON_PRO_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR23_NO_CON_INC_5K(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR23_NO_CON_INC_5K_10K(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR23_NO_CON_INC_10K_20K(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR23_NO_CON_INC_20K_35K(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR23_NO_CON_INC_35K(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR23_NO_CON_BEL_SCL(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR23_NO_CON_BAC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR23_NO_CON_ABV_MAS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR23_NO_CON_ABU_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR23_NO_CON_DUB_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR23_NO_CON_SHJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR23_NO_CON_AJM_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR23_NO_CON_FUJ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR23_NO_CON_RAK_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR23_NO_CON_UAQ_KYC(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR23_NO_CON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR23_NO_CON_DBR_LIM(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR23_NO_UAE_CON_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR23_NO_UAE_CON_NLS(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR23_VAL_LOS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR23_NO_CON_ONB_90(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR23_NO_CON_BAL_PAY(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR23_NO_CON_BAL_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR23_NO_CON_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR23_NO_CON_ONB_REF(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR23_NO_CON_ONB_AGE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR23_NO_CON_AFF_UNAU(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR23_NO_OF_ACCS_POD(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR23_NO_DEL_CON(), null, createHelper);
					updateCellPreserveStyle(row, 48, record2.getR23_NO_INS_FUN_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 49, record2.getR23_NO_CON_COL_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record2.getR23_TOT_NO_NEW_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR23_TOT_NO_NEW_NOSGN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR23_NO_CON_START(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR23_NO_NEW_CON_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR23_NO_CON_DEBT_COUN(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR23_NO_CON_DEL_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR23_NO_CON_GOV_50(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR23_NO_SME_SOLE(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR23_NO_CON_TAG_VUL(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR23_NO_NEW_CON_ONB_GNP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR23_NO_CON_END(), null, createHelper);

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	public byte[] GenerateTable_3_Excel(String branch, String jobId, Map<String, Integer> progressMap, String formmode)
			throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		List<RT_MC_TABLE3_ENTITY> dataList = RT_MC_TABLE3_REPO.findBybranchcode(branch);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for MC report. Returning empty result.");
			return new byte[0];
		}
		String templateDir = env.getProperty("output.exportpathtemp");
		// String templateFileName = "Table_3.xlsx";
		System.out.println(templateFileName);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(5);

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

			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					RT_MC_TABLE3_ENTITY record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					progressMap.put(jobId, 5);

					row = sheet.getRow(5);
					updateCellPreserveStyle(row, 5, record.getR6_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR6_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR6_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR6_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR6_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR6_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR6_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR6_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR6_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR6_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR6_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR6_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR6_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR6_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR6_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR6_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR6_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR6_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR6_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR6_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR6_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR6_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR6_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR6_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR6_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR6_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR6_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR6_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR6_NO_COM_SANA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 34, record.getR6_NO_COM_CBUAE_SD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 35, record.getR6_NO_RES_COM_7(), null, createHelper);
					updateCellPreserveStyle(row, 36, record.getR6_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR6_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR6_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR6_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR6_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR6_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR6_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR6_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR6_NO_COM_REP_DTE(), null, createHelper);

					row = sheet.getRow(6);
					updateCellPreserveStyle(row, 5, record.getR7_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR7_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR7_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR7_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR7_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR7_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR7_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR7_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR7_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR7_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR7_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR7_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR7_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR7_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR7_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR7_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR7_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR7_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR7_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR7_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR7_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR7_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR7_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR7_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR7_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR7_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR7_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR7_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR7_NO_COM_SANA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 34, record.getR7_NO_COM_CBUAE_SD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 35, record.getR7_NO_RES_COM_7(), null, createHelper);
					updateCellPreserveStyle(row, 36, record.getR7_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR7_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR7_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR7_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR7_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR7_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR7_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR7_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR7_NO_COM_REP_DTE(), null, createHelper);

					progressMap.put(jobId, 15);
					row = sheet.getRow(7);
					updateCellPreserveStyle(row, 5, record.getR8_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR8_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR8_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR8_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR8_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR8_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR8_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR8_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR8_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR8_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR8_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR8_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR8_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR8_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR8_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR8_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR8_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR8_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR8_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR8_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR8_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR8_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR8_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR8_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR8_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR8_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR8_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR8_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR8_NO_COM_SANA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 34, record.getR8_NO_COM_CBUAE_SD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 35, record.getR8_NO_RES_COM_7(), null, createHelper);
					updateCellPreserveStyle(row, 36, record.getR8_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR8_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR8_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR8_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR8_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR8_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR8_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR8_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR8_NO_COM_REP_DTE(), null, createHelper);

					row = sheet.getRow(8);
					updateCellPreserveStyle(row, 5, record.getR9_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR9_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR9_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR9_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR9_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR9_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR9_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR9_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR9_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR9_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR9_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR9_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR9_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR9_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR9_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR9_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR9_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR9_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR9_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR9_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR9_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR9_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR9_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR9_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR9_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR9_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR9_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR9_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR9_NO_COM_SANA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 34, record.getR9_NO_COM_CBUAE_SD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 35, record.getR9_NO_RES_COM_7(), null, createHelper);
					updateCellPreserveStyle(row, 36, record.getR9_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR9_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR9_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR9_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR9_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR9_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR9_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR9_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR9_NO_COM_REP_DTE(), null, createHelper);
					row = sheet.getRow(9);
					progressMap.put(jobId, 25);

					updateCellPreserveStyle(row, 5, record.getR10_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR10_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR10_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR10_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR10_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR10_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR10_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR10_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR10_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR10_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR10_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR10_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR10_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR10_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR10_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR10_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR10_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR10_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR10_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR10_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR10_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR10_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR10_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR10_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR10_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR10_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR10_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR10_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR10_NO_COM_SANA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 34, record.getR10_NO_COM_CBUAE_SD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 35, record.getR10_NO_RES_COM_7(), null, createHelper);
					updateCellPreserveStyle(row, 36, record.getR10_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR10_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR10_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR10_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR10_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR10_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR10_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR10_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR10_NO_COM_REP_DTE(), null, createHelper);
					row = sheet.getRow(10);

					updateCellPreserveStyle(row, 5, record.getR11_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR11_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR11_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR11_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR11_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR11_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR11_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR11_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR11_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR11_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR11_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR11_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR11_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR11_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR11_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR11_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR11_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR11_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR11_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR11_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR11_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR11_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR11_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR11_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR11_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR11_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR11_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR11_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR11_NO_COM_SANA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 34, record.getR11_NO_COM_CBUAE_SD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 35, record.getR11_NO_RES_COM_7(), null, createHelper);
					updateCellPreserveStyle(row, 36, record.getR11_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR11_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR11_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR11_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR11_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR11_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR11_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR11_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR11_NO_COM_REP_DTE(), null, createHelper);
					row = sheet.getRow(11);

					progressMap.put(jobId, 35);

					updateCellPreserveStyle(row, 5, record.getR12_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR12_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR12_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR12_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR12_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR12_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR12_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR12_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR12_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR12_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR12_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR12_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR12_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR12_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR12_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR12_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR12_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR12_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR12_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR12_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR12_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR12_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR12_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR12_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR12_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR12_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR12_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR12_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR12_NO_COM_SANA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 34, record.getR12_NO_COM_CBUAE_SD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 35, record.getR12_NO_RES_COM_7(), null, createHelper);
					updateCellPreserveStyle(row, 36, record.getR12_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR12_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR12_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR12_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR12_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR12_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR12_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR12_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR12_NO_COM_REP_DTE(), null, createHelper);
					row = sheet.getRow(12);

					updateCellPreserveStyle(row, 5, record.getR13_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR13_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR13_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR13_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR13_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR13_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR13_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR13_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR13_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR13_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR13_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR13_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR13_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR13_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR13_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR13_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR13_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR13_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR13_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR13_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR13_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR13_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR13_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR13_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR13_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR13_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR13_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR13_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR13_NO_COM_SANA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 34, record.getR13_NO_COM_CBUAE_SD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 35, record.getR13_NO_RES_COM_7(), null, createHelper);
					updateCellPreserveStyle(row, 36, record.getR13_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR13_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR13_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR13_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR13_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR13_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR13_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR13_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR13_NO_COM_REP_DTE(), null, createHelper);
					row = sheet.getRow(13);

					progressMap.put(jobId, 45);
					updateCellPreserveStyle(row, 5, record.getR14_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR14_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR14_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR14_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR14_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR14_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR14_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR14_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR14_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR14_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR14_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR14_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR14_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR14_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR14_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR14_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR14_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR14_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR14_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR14_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR14_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR14_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR14_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR14_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR14_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR14_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR14_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR14_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR14_NO_COM_SANA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 34, record.getR14_NO_COM_CBUAE_SD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 35, record.getR14_NO_RES_COM_7(), null, createHelper);
					updateCellPreserveStyle(row, 36, record.getR14_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR14_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR14_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR14_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR14_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR14_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR14_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR14_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR14_NO_COM_REP_DTE(), null, createHelper);
					row = sheet.getRow(14);

					updateCellPreserveStyle(row, 5, record.getR15_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR15_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR15_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR15_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR15_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR15_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR15_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR15_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR15_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR15_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR15_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR15_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR15_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR15_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR15_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR15_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR15_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR15_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR15_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR15_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR15_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR15_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR15_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR15_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR15_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR15_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR15_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR15_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR15_NO_COM_SANA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 34, record.getR15_NO_COM_CBUAE_SD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 35, record.getR15_NO_RES_COM_7(), null, createHelper);
					updateCellPreserveStyle(row, 36, record.getR15_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR15_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR15_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR15_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR15_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR15_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR15_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR15_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR15_NO_COM_REP_DTE(), null, createHelper);
					row = sheet.getRow(15);

					progressMap.put(jobId, 55);
					updateCellPreserveStyle(row, 5, record.getR16_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR16_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR16_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR16_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR16_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR16_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR16_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR16_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR16_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR16_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR16_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR16_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR16_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR16_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR16_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR16_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR16_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR16_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR16_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR16_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR16_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR16_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR16_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR16_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR16_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR16_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR16_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR16_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR16_NO_COM_SANA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 34, record.getR16_NO_COM_CBUAE_SD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 35, record.getR16_NO_RES_COM_7(), null, createHelper);
					updateCellPreserveStyle(row, 36, record.getR16_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR16_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR16_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR16_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR16_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR16_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR16_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR16_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR16_NO_COM_REP_DTE(), null, createHelper);
					row = sheet.getRow(16);

					updateCellPreserveStyle(row, 5, record.getR17_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR17_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR17_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR17_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR17_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR17_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR17_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR17_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR17_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR17_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR17_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR17_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR17_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR17_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR17_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR17_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR17_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR17_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR17_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR17_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR17_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR17_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR17_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR17_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR17_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR17_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR17_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR17_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR17_NO_COM_SANA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 34, record.getR17_NO_COM_CBUAE_SD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 35, record.getR17_NO_RES_COM_7(), null, createHelper);
					updateCellPreserveStyle(row, 36, record.getR17_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR17_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR17_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR17_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR17_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR17_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR17_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR17_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR17_NO_COM_REP_DTE(), null, createHelper);
					row = sheet.getRow(17);

					progressMap.put(jobId, 65);
					updateCellPreserveStyle(row, 5, record.getR18_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR18_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR18_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR18_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR18_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR18_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR18_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR18_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR18_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR18_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR18_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR18_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR18_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR18_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR18_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR18_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR18_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR18_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR18_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR18_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR18_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR18_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR18_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR18_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR18_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR18_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR18_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR18_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR18_NO_COM_SANA(), null,
					// createHelper);
					updateCellPreserveStyle(row, 34, record.getR18_NO_COM_CBUAE_SD(), null, createHelper);
					updateCellPreserveStyle(row, 35, record.getR18_NO_RES_COM_7(), null, createHelper);
					updateCellPreserveStyle(row, 36, record.getR18_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR18_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR18_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR18_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR18_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR18_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR18_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR18_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR18_NO_COM_REP_DTE(), null, createHelper);
					row = sheet.getRow(18);

					updateCellPreserveStyle(row, 5, record.getR19_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR19_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR19_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR19_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR19_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR19_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR19_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR19_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR19_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR19_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR19_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR19_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR19_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR19_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR19_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR19_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR19_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR19_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR19_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR19_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR19_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR19_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR19_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR19_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR19_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR19_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR19_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR19_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR19_NO_COM_SANA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 34, record.getR19_NO_COM_CBUAE_SD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 35, record.getR19_NO_RES_COM_7(), null, createHelper);
					updateCellPreserveStyle(row, 36, record.getR19_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR19_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR19_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR19_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR19_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR19_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR19_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR19_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR19_NO_COM_REP_DTE(), null, createHelper);
					row = sheet.getRow(19);

					progressMap.put(jobId, 75);
					updateCellPreserveStyle(row, 5, record.getR20_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR20_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR20_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR20_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR20_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR20_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR20_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR20_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR20_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR20_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR20_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR20_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR20_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR20_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR20_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR20_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR20_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR20_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR20_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR20_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR20_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR20_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR20_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR20_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR20_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR20_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR20_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR20_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR20_NO_COM_SANA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 34, record.getR20_NO_COM_CBUAE_SD(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 35, record.getR20_NO_RES_COM_7(), null,
					// createHelper);
					updateCellPreserveStyle(row, 36, record.getR20_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR20_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR20_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR20_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR20_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR20_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR20_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR20_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR20_NO_COM_REP_DTE(), null, createHelper);
					row = sheet.getRow(20);

					updateCellPreserveStyle(row, 5, record.getR21_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR21_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR21_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR21_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR21_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR21_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR21_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR21_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR21_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR21_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR21_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR21_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR21_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR21_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR21_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR21_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR21_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR21_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR21_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR21_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR21_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR21_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR21_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR21_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR21_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR21_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR21_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR21_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR21_NO_COM_SANA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 34, record.getR21_NO_COM_CBUAE_SD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 35, record.getR21_NO_RES_COM_7(), null, createHelper);
					updateCellPreserveStyle(row, 36, record.getR21_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR21_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR21_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR21_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR21_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR21_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR21_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR21_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR21_NO_COM_REP_DTE(), null, createHelper);
					row = sheet.getRow(21);

					progressMap.put(jobId, 85);
					updateCellPreserveStyle(row, 5, record.getR22_NO_COM_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR22_NO_COM_PEN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR22_AMT_FIN_AED(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR22_NO_COM_ADV_FEES(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR22_NO_COM_DET_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR22_NO_COM_DEBT(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR22_NO_COM_AGNT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR22_NO_COM_MISSEL(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR22_NO_COM_FILCOM(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR22_NO_COM_PRO_SER(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR22_NO_ASO_PROD(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR22_NO_CON_PHN(), null, createHelper);
					updateCellPreserveStyle(row, 17, record.getR22_NO_CON_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 18, record.getR22_NO_CON_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 19, record.getR22_NO_OUT_UNRES(), null, createHelper);
					updateCellPreserveStyle(row, 20, record.getR22_NO_COM_RES_CON(), null, createHelper);
					updateCellPreserveStyle(row, 21, record.getR22_NO_RES_30_COM(), null, createHelper);
					updateCellPreserveStyle(row, 22, record.getR22_AVG_TME_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 23, record.getR22_NO_COM_STF_MIS(), null, createHelper);
					updateCellPreserveStyle(row, 24, record.getR22_NO_COM_ATM_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 25, record.getR22_NO_COM_FIN_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 26, record.getR22_NO_COM_FAV_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 27, record.getR22_NO_COM_PRV_PER(), null, createHelper);
					updateCellPreserveStyle(row, 28, record.getR22_NO_RES_COM(), null, createHelper);
					updateCellPreserveStyle(row, 29, record.getR22_NO_STEP_LOS(), null, createHelper);
					updateCellPreserveStyle(row, 30, record.getR22_NO_CUST_LEGAL(), null, createHelper);
					updateCellPreserveStyle(row, 31, record.getR22_NO_COM_FAIL(), null, createHelper);
					// updateCellPreserveStyle(row, 32, record.getR22_NO_COM_EXP_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 33, record.getR22_NO_COM_SANA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 34, record.getR22_NO_COM_CBUAE_SD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 35, record.getR22_NO_RES_COM_7(), null, createHelper);
					updateCellPreserveStyle(row, 36, record.getR22_NO_RES_COM_15(), null, createHelper);
					updateCellPreserveStyle(row, 37, record.getR22_NO_COM_UPHEL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record.getR22_NO_COM_BNK_STF(), null, createHelper);
					updateCellPreserveStyle(row, 39, record.getR22_NO_COM_INHOU(), null, createHelper);
					updateCellPreserveStyle(row, 40, record.getR22_NO_COM_ACK_TT(), null, createHelper);
					updateCellPreserveStyle(row, 41, record.getR22_NO_COM_DEBT_COLL(), null, createHelper);
					updateCellPreserveStyle(row, 42, record.getR22_NO_INST_CRE_RATE(), null, createHelper);
					updateCellPreserveStyle(row, 43, record.getR22_NO_CON_DELAY(), null, createHelper);
					updateCellPreserveStyle(row, 44, record.getR22_NO_COM_REP_DTE(), null, createHelper);

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	public byte[] GenerateTable_4_Excel(String branch, String jobId, Map<String, Integer> progressMap, String formmode)
			throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		List<RT_MC_TABLE4_1_ENTITY> dataList1 = RT_MC_TABLE4_1_REPO.findBybranchcode(branch);
		List<RT_MC_TABLE4_2_ENTITY> dataList2 = RT_MC_TABLE4_2_REPO.findBybranchcode(branch);

		if (dataList1.isEmpty() || dataList2.isEmpty()) {
			logger.warn("Service: No data found for MC report. Returning empty result.");
			return new byte[0];
		}
		String templateDir = env.getProperty("output.exportpathtemp");
		// String templateFileName = "Table_2.xlsx";
		System.out.println(templateFileName);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(6);

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

			int startRow = 5;

			if (!dataList1.isEmpty() && !dataList2.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					RT_MC_TABLE4_1_ENTITY record1 = dataList1.get(i);
					RT_MC_TABLE4_2_ENTITY record2 = dataList2.get(i);
					System.out.println("rownumber=" + (startRow + i));
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					progressMap.put(jobId, 5);

					row = sheet.getRow(5);
					updateCellPreserveStyle(row, 5, record1.getR6_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR6_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR6_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR6_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR6_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR6_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR6_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR6_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR6_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR6_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR6_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR6_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR6_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR6_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR6_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR6_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR6_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR6_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR6_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR6_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR6_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR6_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR6_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR6_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR6_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR6_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR6_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR6_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR6_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR6_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR6_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR6_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR6_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR6_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR6_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR6_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR6_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR6_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR6_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR6_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR6_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR6_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR6_NO_RESTU_ONBO(), null, createHelper);
					// updateCellPreserveStyle(row, 48, record1.getR6_TOT_DELAY_TAT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 49, record1.getR6_TOT_FND_TRAN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 50, record1.getR6_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR6_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR6_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR6_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR6_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR6_TOT_CRD_ISU_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR6_NO_ACC_BLK_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR6_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR6_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR6_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR6_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR6_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR6_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR6_AVG_NOD_LOWRSK(), null, createHelper);
					// updateCellPreserveStyle(row, 64, record1.getR6_NO_CUST_REISS_LIA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 65, record1.getR6_NO_INS_CRRATE_CON(), null,
					// createHelper);

					row = sheet.getRow(6);
					updateCellPreserveStyle(row, 5, record1.getR7_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR7_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR7_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR7_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR7_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR7_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR7_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR7_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR7_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR7_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR7_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR7_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR7_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR7_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR7_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR7_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR7_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR7_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR7_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR7_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR7_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR7_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR7_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR7_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR7_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR7_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR7_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR7_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR7_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR7_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR7_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR7_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR7_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR7_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR7_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR7_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR7_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR7_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR7_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR7_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR7_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR7_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR7_NO_RESTU_ONBO(), null, createHelper);
					// updateCellPreserveStyle(row, 48, record1.getR7_TOT_DELAY_TAT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 49, record1.getR7_TOT_FND_TRAN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 50, record1.getR7_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR7_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR7_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR7_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR7_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR7_TOT_CRD_ISU_SPT(), null, createHelper);
					// updateCellPreserveStyle(row, 56, record1.getR7_NO_ACC_BLK_FIN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 57, record1.getR7_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR7_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR7_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR7_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR7_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR7_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR7_AVG_NOD_LOWRSK(), null, createHelper);
					// updateCellPreserveStyle(row, 64, record1.getR7_NO_CUST_REISS_LIA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 65, record1.getR7_NO_INS_CRRATE_CON(), null,
					// createHelper);

					progressMap.put(jobId, 15);
					row = sheet.getRow(7);
					updateCellPreserveStyle(row, 5, record1.getR8_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR8_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR8_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR8_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR8_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR8_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR8_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR8_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR8_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR8_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR8_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR8_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR8_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR8_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR8_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR8_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR8_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR8_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR8_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR8_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR8_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR8_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR8_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR8_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR8_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR8_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR8_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR8_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR8_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR8_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR8_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR8_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR8_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR8_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR8_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR8_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR8_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR8_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR8_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR8_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR8_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR8_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR8_NO_RESTU_ONBO(), null, createHelper);
					// updateCellPreserveStyle(row, 48, record1.getR8_TOT_DELAY_TAT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 49, record1.getR8_TOT_FND_TRAN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 50, record1.getR8_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR8_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR8_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR8_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR8_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR8_TOT_CRD_ISU_SPT(), null, createHelper);
					// updateCellPreserveStyle(row, 56, record1.getR8_NO_ACC_BLK_FIN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 57, record1.getR8_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR8_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR8_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR8_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR8_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR8_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR8_AVG_NOD_LOWRSK(), null, createHelper);
					// updateCellPreserveStyle(row, 64, record1.getR8_NO_CUST_REISS_LIA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 65, record1.getR8_NO_INS_CRRATE_CON(), null,
					// createHelper);

					row = sheet.getRow(8);
					updateCellPreserveStyle(row, 5, record1.getR9_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR9_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR9_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR9_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR9_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR9_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR9_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR9_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR9_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR9_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR9_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR9_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR9_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR9_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR9_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR9_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR9_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR9_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR9_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR9_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR9_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR9_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR9_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR9_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR9_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR9_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR9_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR9_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR9_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR9_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR9_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR9_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR9_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR9_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR9_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR9_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR9_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR9_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR9_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR9_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR9_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR9_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR9_NO_RESTU_ONBO(), null, createHelper);
					// updateCellPreserveStyle(row, 48, record1.getR9_TOT_DELAY_TAT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 49, record1.getR9_TOT_FND_TRAN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 50, record1.getR9_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR9_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR9_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR9_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR9_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR9_TOT_CRD_ISU_SPT(), null, createHelper);
					// updateCellPreserveStyle(row, 56, record1.getR9_NO_ACC_BLK_FIN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 57, record1.getR9_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR9_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR9_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR9_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR9_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR9_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR9_AVG_NOD_LOWRSK(), null, createHelper);
					// updateCellPreserveStyle(row, 64, record1.getR9_NO_CUST_REISS_LIA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 65, record1.getR9_NO_INS_CRRATE_CON(), null,
					// createHelper);

					progressMap.put(jobId, 25);
					row = sheet.getRow(9);
					updateCellPreserveStyle(row, 5, record1.getR10_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR10_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR10_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR10_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR10_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR10_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR10_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR10_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR10_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR10_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR10_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR10_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR10_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR10_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR10_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR10_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR10_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR10_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR10_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR10_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR10_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR10_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR10_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR10_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR10_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR10_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR10_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR10_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR10_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR10_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR10_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR10_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR10_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR10_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR10_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR10_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR10_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR10_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR10_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR10_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR10_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR10_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR10_NO_RESTU_ONBO(), null, createHelper);
					// updateCellPreserveStyle(row, 48, record1.getR10_TOT_DELAY_TAT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 49, record1.getR10_TOT_FND_TRAN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 50, record1.getR10_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR10_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR10_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR10_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR10_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR10_TOT_CRD_ISU_SPT(), null, createHelper);
					// updateCellPreserveStyle(row, 56, record1.getR10_NO_ACC_BLK_FIN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 57, record1.getR10_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR10_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR10_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR10_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR10_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR10_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR10_AVG_NOD_LOWRSK(), null, createHelper);
					// updateCellPreserveStyle(row, 64, record1.getR10_NO_CUST_REISS_LIA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 65, record1.getR10_NO_INS_CRRATE_CON(), null,
					// createHelper);

					row = sheet.getRow(10);
					updateCellPreserveStyle(row, 5, record1.getR11_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR11_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR11_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR11_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR11_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR11_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR11_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR11_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR11_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR11_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR11_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR11_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR11_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR11_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR11_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR11_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR11_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR11_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR11_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR11_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR11_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR11_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR11_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR11_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR11_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR11_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR11_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR11_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR11_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR11_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR11_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR11_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR11_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR11_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR11_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR11_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR11_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR11_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR11_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR11_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR11_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR11_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR11_NO_RESTU_ONBO(), null, createHelper);
					// updateCellPreserveStyle(row, 48, record1.getR11_TOT_DELAY_TAT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 49, record1.getR11_TOT_FND_TRAN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 50, record1.getR11_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR11_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR11_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR11_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR11_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR11_TOT_CRD_ISU_SPT(), null, createHelper);
					// updateCellPreserveStyle(row, 56, record1.getR11_NO_ACC_BLK_FIN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 57, record1.getR11_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR11_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR11_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR11_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR11_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR11_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR11_AVG_NOD_LOWRSK(), null, createHelper);
					// updateCellPreserveStyle(row, 64, record1.getR11_NO_CUST_REISS_LIA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 65, record1.getR11_NO_INS_CRRATE_CON(), null,
					// createHelper);

					progressMap.put(jobId, 35);
					row = sheet.getRow(11);
					updateCellPreserveStyle(row, 5, record1.getR12_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR12_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR12_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR12_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR12_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR12_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR12_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR12_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR12_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR12_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR12_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR12_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR12_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR12_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR12_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR12_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR12_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR12_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR12_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR12_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR12_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR12_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR12_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR12_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR12_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR12_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR12_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR12_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR12_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR12_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR12_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR12_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR12_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR12_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR12_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR12_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR12_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR12_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR12_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR12_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR12_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR12_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR12_NO_RESTU_ONBO(), null, createHelper);
					// updateCellPreserveStyle(row, 48, record1.getR12_TOT_DELAY_TAT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 49, record1.getR12_TOT_FND_TRAN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 50, record1.getR12_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR12_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR12_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR12_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR12_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR12_TOT_CRD_ISU_SPT(), null, createHelper);
					// updateCellPreserveStyle(row, 56, record1.getR12_NO_ACC_BLK_FIN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 57, record1.getR12_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR12_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR12_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR12_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR12_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR12_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR12_AVG_NOD_LOWRSK(), null, createHelper);
					// updateCellPreserveStyle(row, 64, record1.getR12_NO_CUST_REISS_LIA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 65, record1.getR12_NO_INS_CRRATE_CON(), null,
					// createHelper);
					row = sheet.getRow(12);
					updateCellPreserveStyle(row, 5, record1.getR13_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR13_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR13_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR13_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR13_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR13_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR13_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR13_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR13_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR13_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR13_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR13_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR13_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR13_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR13_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR13_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR13_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR13_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR13_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR13_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR13_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR13_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR13_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR13_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR13_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR13_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR13_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR13_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR13_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR13_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR13_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR13_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR13_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR13_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR13_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR13_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR13_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR13_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR13_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR13_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR13_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR13_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR13_NO_RESTU_ONBO(), null, createHelper);
					// updateCellPreserveStyle(row, 48, record1.getR13_TOT_DELAY_TAT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 49, record1.getR13_TOT_FND_TRAN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 50, record1.getR13_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR13_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR13_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR13_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR13_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR13_TOT_CRD_ISU_SPT(), null, createHelper);
					// updateCellPreserveStyle(row, 56, record1.getR13_NO_ACC_BLK_FIN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 57, record1.getR13_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR13_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR13_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR13_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR13_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR13_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR13_AVG_NOD_LOWRSK(), null, createHelper);
					// updateCellPreserveStyle(row, 64, record1.getR13_NO_CUST_REISS_LIA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 65, record1.getR13_NO_INS_CRRATE_CON(), null,
					// createHelper);
					row = sheet.getRow(13);

					progressMap.put(jobId, 45);
					updateCellPreserveStyle(row, 5, record1.getR14_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record1.getR14_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record1.getR14_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record1.getR14_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR14_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR14_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR14_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR14_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR14_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR14_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR14_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR14_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR14_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR14_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR14_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR14_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR14_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR14_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record1.getR14_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record1.getR14_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record1.getR14_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record1.getR14_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record1.getR14_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record1.getR14_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record1.getR14_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record1.getR14_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record1.getR14_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record1.getR14_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record1.getR14_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record1.getR14_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record1.getR14_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record1.getR14_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record1.getR14_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record1.getR14_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record1.getR14_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record1.getR14_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record1.getR14_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record1.getR14_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record1.getR14_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record1.getR14_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record1.getR14_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record1.getR14_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record1.getR14_NO_RESTU_ONBO(), null, createHelper);
					// updateCellPreserveStyle(row, 48, record1.getR14_TOT_DELAY_TAT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 49, record1.getR14_TOT_FND_TRAN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 50, record1.getR14_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record1.getR14_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR14_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR14_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR14_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR14_TOT_CRD_ISU_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR14_NO_ACC_BLK_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR14_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR14_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR14_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR14_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR14_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR14_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR14_AVG_NOD_LOWRSK(), null, createHelper);
					// updateCellPreserveStyle(row, 64, record1.getR14_NO_CUST_REISS_LIA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 65, record1.getR14_NO_INS_CRRATE_CON(), null,
					// createHelper);

					row = sheet.getRow(14);

					updateCellPreserveStyle(row, 5, record2.getR15_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR15_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR15_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR15_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR15_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR15_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR15_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR15_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR15_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR15_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR15_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR15_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR15_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR15_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR15_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR15_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR15_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR15_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR15_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR15_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR15_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR15_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR15_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR15_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR15_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR15_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR15_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR15_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR15_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR15_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR15_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR15_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR15_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR15_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR15_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR15_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR15_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR15_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR15_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR15_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR15_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR15_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR15_NO_RESTU_ONBO(), null, createHelper);
					// updateCellPreserveStyle(row, 48, record2.getR15_TOT_DELAY_TAT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 49, record2.getR15_TOT_FND_TRAN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 50, record2.getR15_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR15_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR15_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR15_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR15_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR15_TOT_CRD_ISU_SPT(), null, createHelper);
					// updateCellPreserveStyle(row, 56, record2.getR15_NO_ACC_BLK_FIN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 57, record2.getR15_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR15_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR15_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR15_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record2.getR15_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record2.getR15_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record2.getR15_AVG_NOD_LOWRSK(), null, createHelper);
					// updateCellPreserveStyle(row, 64, record2.getR15_NO_CUST_REISS_LIA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 65, record2.getR15_NO_INS_CRRATE_CON(), null,
					// createHelper);

					progressMap.put(jobId, 55);
					row = sheet.getRow(15);
					updateCellPreserveStyle(row, 5, record2.getR16_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR16_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR16_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR16_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR16_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR16_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR16_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR16_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR16_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR16_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR16_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR16_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR16_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR16_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR16_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR16_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR16_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR16_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR16_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR16_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR16_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR16_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR16_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR16_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR16_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR16_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR16_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR16_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR16_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR16_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR16_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR16_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR16_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR16_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR16_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR16_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR16_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR16_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR16_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR16_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR16_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR16_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR16_NO_RESTU_ONBO(), null, createHelper);
					// updateCellPreserveStyle(row, 48, record2.getR16_TOT_DELAY_TAT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 49, record2.getR16_TOT_FND_TRAN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 50, record2.getR16_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR16_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR16_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR16_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR16_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR16_TOT_CRD_ISU_SPT(), null, createHelper);
					// updateCellPreserveStyle(row, 56, record2.getR16_NO_ACC_BLK_FIN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 57, record2.getR16_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR16_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR16_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR16_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record2.getR16_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record2.getR16_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record2.getR16_AVG_NOD_LOWRSK(), null, createHelper);
					// updateCellPreserveStyle(row, 64, record2.getR16_NO_CUST_REISS_LIA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 65, record2.getR16_NO_INS_CRRATE_CON(), null,
					// createHelper);

					row = sheet.getRow(16);
					updateCellPreserveStyle(row, 5, record2.getR17_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR17_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR17_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR17_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR17_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR17_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR17_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR17_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR17_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR17_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR17_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR17_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR17_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR17_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR17_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR17_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR17_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR17_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR17_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR17_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR17_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR17_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR17_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR17_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR17_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR17_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR17_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR17_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR17_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR17_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR17_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR17_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR17_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR17_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR17_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR17_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR17_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR17_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR17_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR17_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR17_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR17_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR17_NO_RESTU_ONBO(), null, createHelper);
					// updateCellPreserveStyle(row, 48, record2.getR17_TOT_DELAY_TAT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 49, record2.getR17_TOT_FND_TRAN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 50, record2.getR17_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR17_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR17_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR17_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR17_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR17_TOT_CRD_ISU_SPT(), null, createHelper);
					// updateCellPreserveStyle(row, 56, record2.getR17_NO_ACC_BLK_FIN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 57, record2.getR17_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR17_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR17_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR17_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record2.getR17_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record2.getR17_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record2.getR17_AVG_NOD_LOWRSK(), null, createHelper);
					// updateCellPreserveStyle(row, 64, record2.getR17_NO_CUST_REISS_LIA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 65, record2.getR17_NO_INS_CRRATE_CON(), null,
					// createHelper);

					progressMap.put(jobId, 65);
					row = sheet.getRow(17);
					updateCellPreserveStyle(row, 5, record2.getR18_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR18_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR18_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR18_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR18_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR18_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR18_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR18_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR18_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR18_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR18_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR18_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR18_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR18_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR18_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR18_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR18_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR18_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR18_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR18_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR18_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR18_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR18_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR18_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR18_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR18_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR18_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR18_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR18_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR18_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR18_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR18_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR18_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR18_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR18_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR18_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR18_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR18_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR18_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR18_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR18_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR18_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR18_NO_RESTU_ONBO(), null, createHelper);
					// updateCellPreserveStyle(row, 48, record2.getR18_TOT_DELAY_TAT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 49, record2.getR18_TOT_FND_TRAN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 50, record2.getR18_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR18_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR18_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR18_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR18_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR18_TOT_CRD_ISU_SPT(), null, createHelper);
					// updateCellPreserveStyle(row, 56, record2.getR18_NO_ACC_BLK_FIN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 57, record2.getR18_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR18_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR18_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR18_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record2.getR18_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record2.getR18_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record2.getR18_AVG_NOD_LOWRSK(), null, createHelper);
					// updateCellPreserveStyle(row, 64, record2.getR18_NO_CUST_REISS_LIA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 65, record2.getR18_NO_INS_CRRATE_CON(), null,
					// createHelper);

					row = sheet.getRow(18);
					updateCellPreserveStyle(row, 5, record2.getR19_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR19_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR19_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR19_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR19_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR19_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR19_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR19_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR19_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR19_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR19_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR19_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR19_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR19_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR19_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR19_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR19_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR19_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR19_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR19_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR19_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR19_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR19_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR19_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR19_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR19_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR19_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR19_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR19_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR19_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR19_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR19_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR19_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR19_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR19_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR19_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR19_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR19_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR19_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR19_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR19_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR19_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR19_NO_RESTU_ONBO(), null, createHelper);
					// updateCellPreserveStyle(row, 48, record2.getR19_TOT_DELAY_TAT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 49, record2.getR19_TOT_FND_TRAN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 50, record2.getR19_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR19_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR19_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR19_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR19_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR19_TOT_CRD_ISU_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR19_NO_ACC_BLK_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR19_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR19_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR19_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR19_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record2.getR19_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record2.getR19_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record2.getR19_AVG_NOD_LOWRSK(), null, createHelper);
					// updateCellPreserveStyle(row, 64, record2.getR19_NO_CUST_REISS_LIA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 65, record2.getR19_NO_INS_CRRATE_CON(), null,
					// createHelper);

					progressMap.put(jobId, 75);
					row = sheet.getRow(19);
					updateCellPreserveStyle(row, 5, record2.getR20_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR20_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR20_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR20_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR20_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR20_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR20_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR20_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR20_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR20_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR20_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR20_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR20_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR20_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR20_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR20_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR20_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR20_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR20_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR20_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR20_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR20_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR20_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR20_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR20_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR20_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR20_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR20_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR20_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR20_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR20_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR20_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR20_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR20_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR20_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR20_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR20_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR20_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR20_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR20_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR20_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR20_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR20_NO_RESTU_ONBO(), null, createHelper);
					// updateCellPreserveStyle(row, 48, record2.getR20_TOT_DELAY_TAT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 49, record2.getR20_TOT_FND_TRAN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 50, record2.getR20_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR20_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR20_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR20_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR20_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR20_TOT_CRD_ISU_SPT(), null, createHelper);
					// updateCellPreserveStyle(row, 56, record2.getR20_NO_ACC_BLK_FIN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 57, record2.getR20_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR20_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR20_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR20_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record2.getR20_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record2.getR20_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record2.getR20_AVG_NOD_LOWRSK(), null, createHelper);
					// updateCellPreserveStyle(row, 64, record2.getR20_NO_CUST_REISS_LIA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 65, record2.getR20_NO_INS_CRRATE_CON(), null,
					// createHelper);

					row = sheet.getRow(20);
					updateCellPreserveStyle(row, 5, record2.getR21_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR21_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR21_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR21_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR21_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR21_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR21_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR21_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR21_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR21_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR21_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR21_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR21_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR21_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR21_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR21_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR21_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR21_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR21_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR21_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR21_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR21_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR21_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR21_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR21_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR21_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR21_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR21_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR21_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR21_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR21_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR21_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR21_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR21_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR21_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR21_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR21_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR21_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR21_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR21_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR21_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR21_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR21_NO_RESTU_ONBO(), null, createHelper);
					// updateCellPreserveStyle(row, 48, record2.getR21_TOT_DELAY_TAT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 49, record2.getR21_TOT_FND_TRAN(), null,
					// createHelper);
					updateCellPreserveStyle(row, 50, record2.getR21_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR21_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR21_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR21_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR21_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR21_TOT_CRD_ISU_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR21_NO_ACC_BLK_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR21_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR21_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR21_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR21_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record2.getR21_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record2.getR21_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record2.getR21_AVG_NOD_LOWRSK(), null, createHelper);
					// updateCellPreserveStyle(row, 64, record2.getR21_NO_CUST_REISS_LIA(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 65, record2.getR21_NO_INS_CRRATE_CON(), null,
					// createHelper);

					progressMap.put(jobId, 85);
					row = sheet.getRow(21);
					updateCellPreserveStyle(row, 5, record2.getR22_AVG_TME_LWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR22_AVG_NO_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR22_AVG_NO_LWRSK_SME(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR22_AVG_NO_DYS_SME(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR22_NO_REQ_CLO(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR22_NO_PRD_VAR_SPC_TP(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR22_NO_REQ_RES_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR22_NO_REQ_PAYDEF(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR22_AVG_TME_REPAY(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR22_NO_INST_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR22_NET_FEE_AED(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR22_NET_PROF_AED(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR22_TOT_NO_ACC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR22_TOT_ACCS_DOR(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR22_TOT_NEW_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR22_TOT_VAL_NEWACCS_AED(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR22_TOT_NO_LOAN_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR22_TOT_REQ_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR22_TOT_LOAN_OTBNK(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR22_NO_CRACC_MIN(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR22_AVG_TME_CLSACC(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR22_N0_CON_MORE1_LOAN(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR22_NO_INST_WITH90(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR22_TOT_GROSS_ASET(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR22_RET_LIAB_AED(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR22_HIG_PER_PROD_CAT(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR22_NO_ACCS_TAG_NPA(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR22_NO_CHG_FEE_GRT5(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR22_NO_CON_FLEX_INT(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR22_NO_CON_FIXED_INT(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR22_NO_LOAN_MAY_2011(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR22_VAL_LEGA_MAY_AED(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR22_AVG_DAY_NLL(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR22_PER_DOR_ACCS(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR22_INT_RET_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR22_FEE_COMM_PROF(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR22_TOT_UNCLM_90(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR22_TOT_VAL_UNBAL(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR22_NO_INST_CONTRACT(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR22_TOT_DORACC_NOTICE(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR22_NO_CONS_LIAB_7DAY(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR22_TOT_REQ_LIAB_NONLIA(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR22_NO_RESTU_ONBO(), null, createHelper);
					updateCellPreserveStyle(row, 48, record2.getR22_TOT_DELAY_TAT(), null, createHelper);
					updateCellPreserveStyle(row, 49, record2.getR22_TOT_FND_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 50, record2.getR22_NO_FUND_RET24(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR22_NO_FAIL_TRAN(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR22_CUS_FND_SVF_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR22_CUS_FND_SVF_PRE(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR22_TOT_CRDR_UND_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR22_TOT_CRD_ISU_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR22_NO_ACC_BLK_FIN(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR22_NO_INQ_AVL_BAL(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR22_TOT_PRE_EXP_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR22_TOT_PRE_POS_EXP(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR22_TOT_PRE_CARD_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 61, record2.getR22_TOT_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 62, record2.getR22_NO_LANG_VOICE(), null, createHelper);
					updateCellPreserveStyle(row, 63, record2.getR22_AVG_NOD_LOWRSK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record2.getR22_NO_CUST_REISS_LIA(), null, createHelper);
					updateCellPreserveStyle(row, 65, record2.getR22_NO_INS_CRRATE_CON(), null, createHelper);

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	public byte[] GenerateTable_5_Excel(String branch, String jobId, Map<String, Integer> progressMap, String formmode)
			throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		List<RT_MC_TABLE5_ENTITY> dataList = RT_MC_TABLE5_REPO.findBybranchcode(branch);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for MC report. Returning empty result.");
			return new byte[0];
		}
		String templateDir = env.getProperty("output.exportpathtemp");
		// String templateFileName = "Table_3.xlsx";
		System.out.println(templateFileName);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(7);

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

			int startRow = 7;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					RT_MC_TABLE5_ENTITY record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					progressMap.put(jobId, 5);

					updateCellPreserveStyle(row, 5, record.getR8_NO_CON_EMP_SPCD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR8_NO_EMP_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR8_NO_DIS_POOR(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR8_NO_CON_AGS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR8_NO_SAL_STF_EMP(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR8_NO_FXD_RENUM(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR8_NO_SAL_REN_50(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR8_AVG_BON_CON(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR8_NO_EMP_DIS_ACT(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR8_NO_COM_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR8_NO_INS_COC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR8_NO_WSTLE_RSK(), null, createHelper);
					progressMap.put(jobId, 25);
					row = sheet.getRow(8);
					updateCellPreserveStyle(row, 5, record.getR9_NO_CON_EMP_SPCD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR9_NO_EMP_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR9_NO_DIS_POOR(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR9_NO_CON_AGS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR9_NO_SAL_STF_EMP(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR9_NO_FXD_RENUM(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR9_NO_SAL_REN_50(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR9_AVG_BON_CON(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR9_NO_EMP_DIS_ACT(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR9_NO_COM_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR9_NO_INS_COC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR9_NO_WSTLE_RSK(), null, createHelper);
					progressMap.put(jobId, 45);
					row = sheet.getRow(9);
					updateCellPreserveStyle(row, 5, record.getR10_NO_CON_EMP_SPCD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR10_NO_EMP_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR10_NO_DIS_POOR(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR10_NO_CON_AGS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR10_NO_SAL_STF_EMP(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR10_NO_FXD_RENUM(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR10_NO_SAL_REN_50(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR10_AVG_BON_CON(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR10_NO_EMP_DIS_ACT(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR10_NO_COM_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR10_NO_INS_COC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR10_NO_WSTLE_RSK(), null, createHelper);
					progressMap.put(jobId, 65);
					row = sheet.getRow(10);
					// updateCellPreserveStyle(row, 5, record.getR11_NO_CON_EMP_SPCD(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 6, record.getR11_NO_EMP_LFI(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 7, record.getR11_NO_DIS_POOR(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 8, record.getR11_NO_CON_AGS(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 9, record.getR11_NO_SAL_STF_EMP(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 10, record.getR11_NO_FXD_RENUM(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 11, record.getR11_NO_SAL_REN_50(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 12, record.getR11_AVG_BON_CON(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 13, record.getR11_NO_EMP_DIS_ACT(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 14, record.getR11_NO_COM_SPC_PT(), null,
					// createHelper);
					updateCellPreserveStyle(row, 15, record.getR11_NO_INS_COC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR11_NO_WSTLE_RSK(), null, createHelper);
					progressMap.put(jobId, 85);
					row = sheet.getRow(11);
					updateCellPreserveStyle(row, 5, record.getR12_NO_CON_EMP_SPCD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR12_NO_EMP_LFI(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR12_NO_DIS_POOR(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR12_NO_CON_AGS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR12_NO_SAL_STF_EMP(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR12_NO_FXD_RENUM(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR12_NO_SAL_REN_50(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR12_AVG_BON_CON(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR12_NO_EMP_DIS_ACT(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR12_NO_COM_SPC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR12_NO_INS_COC_PT(), null, createHelper);
					updateCellPreserveStyle(row, 16, record.getR12_NO_WSTLE_RSK(), null, createHelper);

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	public byte[] GenerateTable_6_Excel(String branch, String jobId, Map<String, Integer> progressMap, String formmode)
			throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		List<RT_MC_TABLE6_ENTITY> dataList = RT_MC_TABLE6_REPO.findBybranchcode(branch);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for MC report. Returning empty result.");
			return new byte[0];
		}
		String templateDir = env.getProperty("output.exportpathtemp");
		// String templateFileName = "Table_3.xlsx";
		System.out.println(templateFileName);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(8);

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

			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					RT_MC_TABLE6_ENTITY record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					progressMap.put(jobId, 5);

					updateCellPreserveStyle(row, 5, record.getR7_NO_PRO_SNR(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR7_NO_TRA_SNR_SPT(), null, createHelper);
					// updateCellPreserveStyle(row, 7, record.getR7_NO_CONS_VC(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 8, record.getR7_NO_CON_FRA(), null,
					// createHelper);
					updateCellPreserveStyle(row, 9, record.getR7_NO_CON_MRK_FRD(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR7_TOT_CON_PRO_CBUAE(), null, createHelper);
					// updateCellPreserveStyle(row, 11, record.getR7_TOT_TRA_CBUAE(), null,
					// createHelper);
					progressMap.put(jobId, 20);
					row = sheet.getRow(7);
					updateCellPreserveStyle(row, 5, record.getR8_NO_PRO_SNR(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR8_NO_TRA_SNR_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR8_NO_CONS_VC(), null, createHelper);
					// updateCellPreserveStyle(row, 8, record.getR8_NO_CON_FRA(), null,
					// createHelper);
					updateCellPreserveStyle(row, 9, record.getR8_NO_CON_MRK_FRD(), null, createHelper);
					// updateCellPreserveStyle(row, 10, record.getR8_TOT_CON_PRO_CBUAE(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 11, record.getR8_TOT_TRA_CBUAE(), null,
					// createHelper);
					progressMap.put(jobId, 35);
					row = sheet.getRow(8);
					updateCellPreserveStyle(row, 5, record.getR9_NO_PRO_SNR(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR9_NO_TRA_SNR_SPT(), null, createHelper);
					// updateCellPreserveStyle(row, 7, record.getR9_NO_CONS_VC(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 8, record.getR9_NO_CON_FRA(), null,
					// createHelper);
					updateCellPreserveStyle(row, 9, record.getR9_NO_CON_MRK_FRD(), null, createHelper);
					// updateCellPreserveStyle(row, 10, record.getR9_TOT_CON_PRO_CBUAE(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 11, record.getR9_TOT_TRA_CBUAE(), null,
					// createHelper);
					progressMap.put(jobId, 50);
					row = sheet.getRow(9);
					updateCellPreserveStyle(row, 5, record.getR10_NO_PRO_SNR(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR10_NO_TRA_SNR_SPT(), null, createHelper);
					// updateCellPreserveStyle(row, 7, record.getR10_NO_CONS_VC(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 8, record.getR10_NO_CON_FRA(), null,
					// createHelper);
					updateCellPreserveStyle(row, 9, record.getR10_NO_CON_MRK_FRD(), null, createHelper);
					// updateCellPreserveStyle(row, 10, record.getR10_TOT_CON_PRO_CBUAE(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 11, record.getR10_TOT_TRA_CBUAE(), null,
					// createHelper);
					progressMap.put(jobId, 65);
					row = sheet.getRow(10);
					updateCellPreserveStyle(row, 5, record.getR11_NO_PRO_SNR(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR11_NO_TRA_SNR_SPT(), null, createHelper);
					// updateCellPreserveStyle(row, 7, record.getR11_NO_CONS_VC(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 8, record.getR11_NO_CON_FRA(), null,
					// createHelper);
					updateCellPreserveStyle(row, 9, record.getR11_NO_CON_MRK_FRD(), null, createHelper);
					// updateCellPreserveStyle(row, 10, record.getR11_TOT_CON_PRO_CBUAE(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 11, record.getR11_TOT_TRA_CBUAE(), null,
					// createHelper);

					progressMap.put(jobId, 85);
					row = sheet.getRow(11);
					updateCellPreserveStyle(row, 5, record.getR12_NO_PRO_SNR(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR12_NO_TRA_SNR_SPT(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR12_NO_CONS_VC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR12_NO_CON_FRA(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR12_NO_CON_MRK_FRD(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR12_TOT_CON_PRO_CBUAE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR12_TOT_TRA_CBUAE(), null, createHelper);

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	public byte[] GenerateTable_7_Excel(String branch, String jobId, Map<String, Integer> progressMap, String formmode)
			throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		List<RT_MC_TABLE7_1_ENTITY> dataList1 = RT_MC_TABLE7_1_REPO.findBybranchcode(branch);
		List<RT_MC_TABLE7_2_ENTITY> dataList2 = RT_MC_TABLE7_2_REPO.findBybranchcode(branch);

		if (dataList1.isEmpty() || dataList2.isEmpty()) {
			logger.warn("Service: No data found for MC report. Returning empty result.");
			return new byte[0];
		}
		String templateDir = env.getProperty("output.exportpathtemp");
		// String templateFileName = "Table_2.xlsx";
		System.out.println(templateFileName);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(9);

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

			int startRow = 6;

			if (!dataList1.isEmpty() && !dataList2.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					RT_MC_TABLE7_1_ENTITY record1 = dataList1.get(i);
					RT_MC_TABLE7_2_ENTITY record2 = dataList2.get(i);
					System.out.println("rownumber=" + (startRow + i));
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					progressMap.put(jobId, 5);

					updateCellPreserveStyle(row, 9, record1.getR7_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR7_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR7_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR7_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR7_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR7_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR7_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR7_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR7_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR7_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR7_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR7_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR7_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR7_TOT_BRAN_PREV(), null, createHelper);

					// updateCellPreserveStyle(row, 51, record1.getR7_NPL_SPD(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 52, record1.getR7_RECO_NPL_SPD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 53, record1.getR7_NO_CON_ONBO_DIGI(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR7_NO_ACCS_AML(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR7_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR7_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR7_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR7_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR7_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR7_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR7_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR7_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR7_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record1.getR7_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record1.getR7_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record1.getR7_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record1.getR7_NO_CON_HIGH_RSK(), null, createHelper);
					row = sheet.getRow(7);
					updateCellPreserveStyle(row, 8, record1.getR8_RET_LOAN_DIS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR8_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR8_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR8_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR8_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR8_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR8_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR8_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR8_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR8_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR8_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR8_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR8_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR8_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR8_TOT_BRAN_PREV(), null, createHelper);

					// updateCellPreserveStyle(row, 51, record1.getR8_NPL_SPD(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 52, record1.getR8_RECO_NPL_SPD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 53, record1.getR8_NO_CON_ONBO_DIGI(), null, createHelper);
					updateCellPreserveStyle(row, 54, record1.getR8_NO_ACCS_AML(), null, createHelper);
					updateCellPreserveStyle(row, 55, record1.getR8_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR8_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR8_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR8_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR8_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR8_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR8_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR8_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR8_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record1.getR8_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record1.getR8_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record1.getR8_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record1.getR8_NO_CON_HIGH_RSK(), null, createHelper);
					row = sheet.getRow(8);

					progressMap.put(jobId, 12);
					updateCellPreserveStyle(row, 8, record1.getR9_RET_LOAN_DIS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR9_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR9_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR9_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR9_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR9_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR9_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR9_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR9_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR9_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR9_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR9_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR9_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR9_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR9_TOT_BRAN_PREV(), null, createHelper);

					// updateCellPreserveStyle(row, 51, record1.getR9_NPL_SPD(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 52, record1.getR9_RECO_NPL_SPD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 53, record1.getR9_NO_CON_ONBO_DIGI(), null, createHelper);
					// updateCellPreserveStyle(row, 54, record1.getR9_NO_ACCS_AML(), null,
					// createHelper);
					updateCellPreserveStyle(row, 55, record1.getR9_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR9_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR9_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR9_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR9_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR9_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR9_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR9_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR9_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record1.getR9_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record1.getR9_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record1.getR9_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record1.getR9_NO_CON_HIGH_RSK(), null, createHelper);
					row = sheet.getRow(9);

					updateCellPreserveStyle(row, 8, record1.getR10_RET_LOAN_DIS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR10_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR10_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR10_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR10_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR10_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR10_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR10_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR10_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR10_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR10_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR10_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR10_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR10_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR10_TOT_BRAN_PREV(), null, createHelper);

					updateCellPreserveStyle(row, 51, record1.getR10_NPL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR10_RECO_NPL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR10_NO_CON_ONBO_DIGI(), null, createHelper);
					// updateCellPreserveStyle(row, 54, record1.getR10_NO_ACCS_AML(), null,
					// createHelper);
					updateCellPreserveStyle(row, 55, record1.getR10_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR10_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR10_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR10_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR10_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR10_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR10_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR10_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR10_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record1.getR10_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record1.getR10_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record1.getR10_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record1.getR10_NO_CON_HIGH_RSK(), null, createHelper);
					row = sheet.getRow(10);

					progressMap.put(jobId, 24);
					updateCellPreserveStyle(row, 8, record1.getR11_RET_LOAN_DIS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR11_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR11_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR11_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR11_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR11_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR11_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR11_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR11_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR11_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR11_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR11_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR11_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR11_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR11_TOT_BRAN_PREV(), null, createHelper);

					updateCellPreserveStyle(row, 51, record1.getR11_NPL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR11_RECO_NPL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR11_NO_CON_ONBO_DIGI(), null, createHelper);
					// updateCellPreserveStyle(row, 54, record1.getR11_NO_ACCS_AML(), null,
					// createHelper);
					updateCellPreserveStyle(row, 55, record1.getR11_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR11_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR11_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR11_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR11_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR11_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR11_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR11_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR11_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record1.getR11_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record1.getR11_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record1.getR11_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record1.getR11_NO_CON_HIGH_RSK(), null, createHelper);
					row = sheet.getRow(11);

					updateCellPreserveStyle(row, 8, record1.getR12_RET_LOAN_DIS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR12_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR12_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR12_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR12_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR12_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR12_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR12_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR12_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR12_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR12_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR12_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR12_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR12_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR12_TOT_BRAN_PREV(), null, createHelper);

					updateCellPreserveStyle(row, 51, record1.getR12_NPL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR12_RECO_NPL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR12_NO_CON_ONBO_DIGI(), null, createHelper);
					// updateCellPreserveStyle(row, 54, record1.getR12_NO_ACCS_AML(), null,
					// createHelper);
					updateCellPreserveStyle(row, 55, record1.getR12_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR12_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR12_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR12_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR12_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR12_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR12_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR12_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR12_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record1.getR12_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record1.getR12_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record1.getR12_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record1.getR12_NO_CON_HIGH_RSK(), null, createHelper);
					row = sheet.getRow(12);

					progressMap.put(jobId, 36);
					updateCellPreserveStyle(row, 8, record1.getR13_RET_LOAN_DIS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR13_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR13_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR13_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR13_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR13_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR13_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR13_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR13_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR13_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR13_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR13_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR13_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR13_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR13_TOT_BRAN_PREV(), null, createHelper);

					updateCellPreserveStyle(row, 51, record1.getR13_NPL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR13_RECO_NPL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR13_NO_CON_ONBO_DIGI(), null, createHelper);
					// updateCellPreserveStyle(row, 54, record1.getR13_NO_ACCS_AML(), null,
					// createHelper);
					updateCellPreserveStyle(row, 55, record1.getR13_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR13_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR13_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR13_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR13_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR13_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR13_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR13_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR13_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record1.getR13_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record1.getR13_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record1.getR13_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record1.getR13_NO_CON_HIGH_RSK(), null, createHelper);
					row = sheet.getRow(13);

					progressMap.put(jobId, 48);
					updateCellPreserveStyle(row, 8, record1.getR14_RET_LOAN_DIS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR14_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR14_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR14_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR14_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR14_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR14_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR14_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR14_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR14_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR14_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR14_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR14_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR14_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR14_TOT_BRAN_PREV(), null, createHelper);

					updateCellPreserveStyle(row, 51, record1.getR14_NPL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR14_RECO_NPL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR14_NO_CON_ONBO_DIGI(), null, createHelper);
					// updateCellPreserveStyle(row, 54, record1.getR14_NO_ACCS_AML(), null,
					// createHelper);
					updateCellPreserveStyle(row, 55, record1.getR14_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR14_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR14_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR14_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR14_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR14_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR14_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR14_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR14_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record1.getR14_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record1.getR14_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record1.getR14_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record1.getR14_NO_CON_HIGH_RSK(), null, createHelper);
					row = sheet.getRow(14);

					updateCellPreserveStyle(row, 8, record1.getR15_RET_LOAN_DIS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record1.getR15_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record1.getR15_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record1.getR15_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record1.getR15_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record1.getR15_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record1.getR15_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record1.getR15_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record1.getR15_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record1.getR15_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record1.getR15_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record1.getR15_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record1.getR15_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record1.getR15_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record1.getR15_TOT_BRAN_PREV(), null, createHelper);

					updateCellPreserveStyle(row, 51, record1.getR15_NPL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 52, record1.getR15_RECO_NPL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 53, record1.getR15_NO_CON_ONBO_DIGI(), null, createHelper);
					// updateCellPreserveStyle(row, 54, record1.getR15_NO_ACCS_AML(), null,
					// createHelper);
					updateCellPreserveStyle(row, 55, record1.getR15_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record1.getR15_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record1.getR15_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record1.getR15_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record1.getR15_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record1.getR15_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record1.getR15_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record1.getR15_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record1.getR15_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record1.getR15_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record1.getR15_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record1.getR15_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record1.getR15_NO_CON_HIGH_RSK(), null, createHelper);

					row = sheet.getRow(15);

					progressMap.put(jobId, 60);
					updateCellPreserveStyle(row, 8, record2.getR16_RET_LOAN_DIS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR16_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR16_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR16_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR16_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR16_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR16_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR16_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR16_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR16_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR16_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR16_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR16_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR16_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR16_TOT_BRAN_PREV(), null, createHelper);

					// updateCellPreserveStyle(row, 51, record2.getR16_NPL_SPD(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 52, record2.getR16_RECO_NPL_SPD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 53, record2.getR16_NO_CON_ONBO_DIGI(), null, createHelper);
					// updateCellPreserveStyle(row, 54, record2.getR16_NO_ACCS_AML(), null,
					// createHelper);
					updateCellPreserveStyle(row, 55, record2.getR16_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR16_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR16_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR16_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR16_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR16_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record2.getR16_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record2.getR16_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record2.getR16_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record2.getR16_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record2.getR16_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record2.getR16_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record2.getR16_NO_CON_HIGH_RSK(), null, createHelper);
					row = sheet.getRow(16);

					updateCellPreserveStyle(row, 8, record2.getR17_RET_LOAN_DIS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR17_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR17_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR17_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR17_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR17_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR17_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR17_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR17_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR17_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR17_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR17_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR17_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR17_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR17_TOT_BRAN_PREV(), null, createHelper);

					// updateCellPreserveStyle(row, 51, record2.getR17_NPL_SPD(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 52, record2.getR17_RECO_NPL_SPD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 53, record2.getR17_NO_CON_ONBO_DIGI(), null, createHelper);
					// updateCellPreserveStyle(row, 54, record2.getR17_NO_ACCS_AML(), null,
					// createHelper);
					updateCellPreserveStyle(row, 55, record2.getR17_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR17_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR17_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR17_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR17_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR17_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record2.getR17_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record2.getR17_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record2.getR17_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record2.getR17_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record2.getR17_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record2.getR17_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record2.getR17_NO_CON_HIGH_RSK(), null, createHelper);
					row = sheet.getRow(17);

					progressMap.put(jobId, 72);
					updateCellPreserveStyle(row, 8, record2.getR18_RET_LOAN_DIS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR18_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR18_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR18_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR18_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR18_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR18_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR18_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR18_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR18_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR18_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR18_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR18_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR18_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR18_TOT_BRAN_PREV(), null, createHelper);

					// updateCellPreserveStyle(row, 51, record2.getR18_NPL_SPD(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 52, record2.getR18_RECO_NPL_SPD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 53, record2.getR18_NO_CON_ONBO_DIGI(), null, createHelper);
					// updateCellPreserveStyle(row, 54, record2.getR18_NO_ACCS_AML(), null,
					// createHelper);
					updateCellPreserveStyle(row, 55, record2.getR18_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR18_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR18_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR18_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR18_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR18_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record2.getR18_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record2.getR18_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record2.getR18_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record2.getR18_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record2.getR18_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record2.getR18_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record2.getR18_NO_CON_HIGH_RSK(), null, createHelper);
					row = sheet.getRow(18);

					updateCellPreserveStyle(row, 8, record2.getR19_RET_LOAN_DIS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR19_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR19_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR19_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR19_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR19_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR19_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR19_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR19_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR19_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR19_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR19_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR19_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR19_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR19_TOT_BRAN_PREV(), null, createHelper);

					// updateCellPreserveStyle(row, 51, record2.getR19_NPL_SPD(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 52, record2.getR19_RECO_NPL_SPD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 53, record2.getR19_NO_CON_ONBO_DIGI(), null, createHelper);
					// updateCellPreserveStyle(row, 54, record2.getR19_NO_ACCS_AML(), null,
					// createHelper);
					updateCellPreserveStyle(row, 55, record2.getR19_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR19_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR19_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR19_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR19_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR19_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record2.getR19_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record2.getR19_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record2.getR19_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record2.getR19_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record2.getR19_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record2.getR19_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record2.getR19_NO_CON_HIGH_RSK(), null, createHelper);
					row = sheet.getRow(19);

					progressMap.put(jobId, 84);
					updateCellPreserveStyle(row, 8, record2.getR20_RET_LOAN_DIS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR20_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR20_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR20_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR20_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR20_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR20_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR20_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR20_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR20_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR20_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR20_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR20_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR20_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR20_TOT_BRAN_PREV(), null, createHelper);

					// updateCellPreserveStyle(row, 51, record2.getR20_NPL_SPD(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 52, record2.getR20_RECO_NPL_SPD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 53, record2.getR20_NO_CON_ONBO_DIGI(), null, createHelper);
					// updateCellPreserveStyle(row, 54, record2.getR20_NO_ACCS_AML(), null,
					// createHelper);
					updateCellPreserveStyle(row, 55, record2.getR20_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR20_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR20_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR20_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR20_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR20_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record2.getR20_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record2.getR20_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record2.getR20_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record2.getR20_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record2.getR20_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record2.getR20_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record2.getR20_NO_CON_HIGH_RSK(), null, createHelper);
					row = sheet.getRow(20);

					updateCellPreserveStyle(row, 8, record2.getR21_RET_LOAN_DIS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR21_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR21_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR21_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR21_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR21_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR21_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR21_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR21_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR21_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR21_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR21_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR21_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR21_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR21_TOT_BRAN_PREV(), null, createHelper);

					// updateCellPreserveStyle(row, 51, record2.getR21_NPL_SPD(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 52, record2.getR21_RECO_NPL_SPD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 53, record2.getR21_NO_CON_ONBO_DIGI(), null, createHelper);
					// updateCellPreserveStyle(row, 54, record2.getR21_NO_ACCS_AML(), null,
					// createHelper);
					updateCellPreserveStyle(row, 55, record2.getR21_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR21_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR21_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR21_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR21_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR21_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record2.getR21_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record2.getR21_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record2.getR21_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record2.getR21_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record2.getR21_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record2.getR21_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record2.getR21_NO_CON_HIGH_RSK(), null, createHelper);
					row = sheet.getRow(21);

					progressMap.put(jobId, 90);
					updateCellPreserveStyle(row, 8, record2.getR22_RET_LOAN_DIS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR22_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR22_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR22_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR22_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR22_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR22_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR22_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR22_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR22_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR22_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR22_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR22_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR22_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR22_TOT_BRAN_PREV(), null, createHelper);

					// updateCellPreserveStyle(row, 51, record2.getR22_NPL_SPD(), null,
					// createHelper);
					// updateCellPreserveStyle(row, 52, record2.getR22_RECO_NPL_SPD(), null,
					// createHelper);
					updateCellPreserveStyle(row, 53, record2.getR22_NO_CON_ONBO_DIGI(), null, createHelper);
					// updateCellPreserveStyle(row, 54, record2.getR22_NO_ACCS_AML(), null,
					// createHelper);
					updateCellPreserveStyle(row, 55, record2.getR22_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR22_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR22_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR22_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR22_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR22_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record2.getR22_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record2.getR22_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record2.getR22_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record2.getR22_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record2.getR22_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record2.getR22_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record2.getR22_NO_CON_HIGH_RSK(), null, createHelper);
					row = sheet.getRow(22);

					updateCellPreserveStyle(row, 5, record2.getR23_RET_ASET_LTS(), null, createHelper);
					updateCellPreserveStyle(row, 6, record2.getR23_RET_ASET_LTSME(), null, createHelper);
					updateCellPreserveStyle(row, 7, record2.getR23_RET_ASET_SME_GUR(), null, createHelper);
					updateCellPreserveStyle(row, 8, record2.getR23_RET_LOAN_DIS(), null, createHelper);
					updateCellPreserveStyle(row, 9, record2.getR23_RET_ASET_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 10, record2.getR23_PER_LOAN_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 11, record2.getR23_LOAN_SME_WEB_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 12, record2.getR23_NO_COMPDIGI_MOB(), null, createHelper);
					updateCellPreserveStyle(row, 13, record2.getR23_NO_COMP_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 14, record2.getR23_PER_COMPDIGI_WEB(), null, createHelper);
					updateCellPreserveStyle(row, 15, record2.getR23_PER_COM_TRADCALL(), null, createHelper);
					updateCellPreserveStyle(row, 16, record2.getR23_TOT_REV_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 17, record2.getR23_PROF_LOSS_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 18, record2.getR23_PROF_MARG_PORTFOLIO(), null, createHelper);
					updateCellPreserveStyle(row, 19, record2.getR23_TOT_RET_ASET_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 20, record2.getR23_NP_LOAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 21, record2.getR23_NON_CON_ONLAST_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 22, record2.getR23_TOT_BRAN_PREV(), null, createHelper);
					updateCellPreserveStyle(row, 23, record2.getR23_TOT_REV_RET_CUR(), null, createHelper);
					updateCellPreserveStyle(row, 24, record2.getR23_PROF_LOS_PORT_CUR(), null, createHelper);
					updateCellPreserveStyle(row, 25, record2.getR23_PROF_MARG_PERC_CUR(), null, createHelper);
					updateCellPreserveStyle(row, 26, record2.getR23_TOT_RET_ASET_CUR(), null, createHelper);
					updateCellPreserveStyle(row, 27, record2.getR23_NON_PER_RET_PORT_CUR(), null, createHelper);
					updateCellPreserveStyle(row, 28, record2.getR23_NO_CON_LAST_CUR(), null, createHelper);
					updateCellPreserveStyle(row, 29, record2.getR23_TOT_BRANCH_CUR(), null, createHelper);
					updateCellPreserveStyle(row, 30, record2.getR23_TOT_REV_T1(), null, createHelper);
					updateCellPreserveStyle(row, 31, record2.getR23_PROF_LOS_T1(), null, createHelper);
					updateCellPreserveStyle(row, 32, record2.getR23_PROF_MARG_T1(), null, createHelper);
					updateCellPreserveStyle(row, 33, record2.getR23_TOT_RET_ASET_T1(), null, createHelper);
					updateCellPreserveStyle(row, 34, record2.getR23_NON_PERF_PORT_T1(), null, createHelper);
					updateCellPreserveStyle(row, 35, record2.getR23_NO_CON_LAST_T1(), null, createHelper);
					updateCellPreserveStyle(row, 36, record2.getR23_TOT_BRANCH_T1(), null, createHelper);
					updateCellPreserveStyle(row, 37, record2.getR23_TOT_REV_T2(), null, createHelper);
					updateCellPreserveStyle(row, 38, record2.getR23_PROF_LOS_T2(), null, createHelper);
					updateCellPreserveStyle(row, 39, record2.getR23_PROF_MARG_T2(), null, createHelper);
					updateCellPreserveStyle(row, 40, record2.getR23_TOT_RET_ASET_T2(), null, createHelper);
					updateCellPreserveStyle(row, 41, record2.getR23_NON_PERF_PORT_T2(), null, createHelper);
					updateCellPreserveStyle(row, 42, record2.getR23_NO_CON_LAST_T2(), null, createHelper);
					updateCellPreserveStyle(row, 43, record2.getR23_TOT_BRANCH_T2(), null, createHelper);
					updateCellPreserveStyle(row, 44, record2.getR23_TOT_REV_T3(), null, createHelper);
					updateCellPreserveStyle(row, 45, record2.getR23_PROF_LOS_T3(), null, createHelper);
					updateCellPreserveStyle(row, 46, record2.getR23_PROF_MARG_T3(), null, createHelper);
					updateCellPreserveStyle(row, 47, record2.getR23_TOT_RET_ASET_T3(), null, createHelper);
					updateCellPreserveStyle(row, 48, record2.getR23_NON_PERF_PORT_T3(), null, createHelper);
					updateCellPreserveStyle(row, 49, record2.getR23_NO_CON_LAST_T3(), null, createHelper);
					updateCellPreserveStyle(row, 50, record2.getR23_TOT_BRANCH_T3(), null, createHelper);
					updateCellPreserveStyle(row, 51, record2.getR23_NPL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 52, record2.getR23_RECO_NPL_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 53, record2.getR23_NO_CON_ONBO_DIGI(), null, createHelper);
					updateCellPreserveStyle(row, 54, record2.getR23_NO_ACCS_AML(), null, createHelper);
					updateCellPreserveStyle(row, 55, record2.getR23_TOT_ACC_LOAN_AML(), null, createHelper);
					updateCellPreserveStyle(row, 56, record2.getR23_TOT_NEW_ACCS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 57, record2.getR23_TOT_VALS_LOANS_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 58, record2.getR23_NO_INQ_WEBMOB(), null, createHelper);
					updateCellPreserveStyle(row, 59, record2.getR23_NO_INQ_TRAD(), null, createHelper);
					updateCellPreserveStyle(row, 60, record2.getR23_NO_RET_POS(), null, createHelper);
					updateCellPreserveStyle(row, 61, record2.getR23_AMT_POS_GATE(), null, createHelper);
					updateCellPreserveStyle(row, 62, record2.getR23_NO_TRAN_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 63, record2.getR23_AMT_RET_SVF_BNK(), null, createHelper);
					updateCellPreserveStyle(row, 64, record2.getR23_TOT_WEB_MOB_VIRT(), null, createHelper);
					updateCellPreserveStyle(row, 65, record2.getR23_VAL_UNCL_SVF(), null, createHelper);
					updateCellPreserveStyle(row, 66, record2.getR23_NO_CON_LOW_RSK(), null, createHelper);
					updateCellPreserveStyle(row, 67, record2.getR23_NO_CON_HIGH_RSK(), null, createHelper);

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	public byte[] GenerateTable_8_Excel(String branch, String jobId, Map<String, Integer> progressMap, String formmode)
			throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		List<RT_MC_TABLE8_ENTITY> dataList = RT_MC_TABLE8_REPO.findBybranchcode(branch);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for MC report. Returning empty result.");
			return new byte[0];
		}
		String templateDir = env.getProperty("output.exportpathtemp");
		// String templateFileName = "Table_1.xlsx";
		System.out.println(templateFileName);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(10);

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

			int startRow = 7;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					RT_MC_TABLE8_ENTITY record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					progressMap.put(jobId, 5);

					updateCellPreserveStyle(row, 5, record.getR8_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR8_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR8_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR8_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR8_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR8_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR8_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR8_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR8_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR8_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR8_NO_COMP_ISLAMIC(), null, createHelper);

					row = sheet.getRow(8);
					updateCellPreserveStyle(row, 5, record.getR9_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR9_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR9_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR9_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR9_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR9_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR9_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR9_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR9_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR9_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR9_NO_COMP_ISLAMIC(), null, createHelper);

					progressMap.put(jobId, 12);
					row = sheet.getRow(9);
					updateCellPreserveStyle(row, 5, record.getR10_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR10_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR10_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR10_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR10_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR10_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR10_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR10_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR10_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR10_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR10_NO_COMP_ISLAMIC(), null, createHelper);

					row = sheet.getRow(10);
					updateCellPreserveStyle(row, 5, record.getR11_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR11_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR11_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR11_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR11_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR11_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR11_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR11_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR11_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR11_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR11_NO_COMP_ISLAMIC(), null, createHelper);

					progressMap.put(jobId, 24);
					row = sheet.getRow(11);
					updateCellPreserveStyle(row, 5, record.getR12_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR12_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR12_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR12_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR12_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR12_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR12_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR12_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR12_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR12_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR12_NO_COMP_ISLAMIC(), null, createHelper);
					row = sheet.getRow(12);
					updateCellPreserveStyle(row, 5, record.getR13_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR13_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR13_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR13_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR13_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR13_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR13_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR13_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR13_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR13_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR13_NO_COMP_ISLAMIC(), null, createHelper);

					progressMap.put(jobId, 36);
					row = sheet.getRow(13);
					updateCellPreserveStyle(row, 5, record.getR14_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR14_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR14_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR14_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR14_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR14_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR14_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR14_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR14_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR14_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR14_NO_COMP_ISLAMIC(), null, createHelper);

					row = sheet.getRow(14);
					updateCellPreserveStyle(row, 5, record.getR15_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR15_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR15_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR15_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR15_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR15_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR15_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR15_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR15_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR15_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR15_NO_COMP_ISLAMIC(), null, createHelper);

					progressMap.put(jobId, 48);
					row = sheet.getRow(15);
					updateCellPreserveStyle(row, 5, record.getR16_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR16_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR16_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR16_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR16_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR16_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR16_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR16_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR16_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR16_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR16_NO_COMP_ISLAMIC(), null, createHelper);

					row = sheet.getRow(16);
					updateCellPreserveStyle(row, 5, record.getR17_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR17_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR17_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR17_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR17_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR17_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR17_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR17_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR17_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR17_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR17_NO_COMP_ISLAMIC(), null, createHelper);

					progressMap.put(jobId, 60);
					row = sheet.getRow(17);
					updateCellPreserveStyle(row, 5, record.getR18_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR18_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR18_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR18_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR18_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR18_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR18_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR18_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR18_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR18_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR18_NO_COMP_ISLAMIC(), null, createHelper);

					row = sheet.getRow(18);
					updateCellPreserveStyle(row, 5, record.getR19_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR19_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR19_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR19_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR19_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR19_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR19_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR19_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR19_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR19_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR19_NO_COMP_ISLAMIC(), null, createHelper);

					progressMap.put(jobId, 72);
					row = sheet.getRow(19);
					updateCellPreserveStyle(row, 5, record.getR20_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR20_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR20_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR20_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR20_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR20_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR20_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR20_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR20_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR20_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR20_NO_COMP_ISLAMIC(), null, createHelper);

					row = sheet.getRow(20);
					updateCellPreserveStyle(row, 5, record.getR21_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR21_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR21_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR21_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR21_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR21_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR21_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR21_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR21_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR21_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR21_NO_COMP_ISLAMIC(), null, createHelper);

					progressMap.put(jobId, 84);
					row = sheet.getRow(21);
					updateCellPreserveStyle(row, 5, record.getR22_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR22_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR22_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR22_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR22_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR22_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR22_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR22_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR22_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR22_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR22_NO_COMP_ISLAMIC(), null, createHelper);

					row = sheet.getRow(22);
					updateCellPreserveStyle(row, 5, record.getR23_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR23_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR23_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR23_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR23_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR23_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR23_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR23_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR23_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR23_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR23_NO_COMP_ISLAMIC(), null, createHelper);

					progressMap.put(jobId, 96);
					row = sheet.getRow(23);
					updateCellPreserveStyle(row, 5, record.getR24_NO_RET_CON_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 6, record.getR24_NO_ISC_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 7, record.getR24_NO_SME_CON_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 8, record.getR24_NO_SME_ISC(), null, createHelper);
					updateCellPreserveStyle(row, 9, record.getR24_TOT_AGE_OUT(), null, createHelper);
					updateCellPreserveStyle(row, 10, record.getR24_TOT_PONT_SALE(), null, createHelper);
					updateCellPreserveStyle(row, 11, record.getR24_TOT_MER_SPD(), null, createHelper);
					updateCellPreserveStyle(row, 12, record.getR24_RET_SIZE_ISC_AED(), null, createHelper);
					updateCellPreserveStyle(row, 13, record.getR24_RET_ASSE_LON_RET(), null, createHelper);
					updateCellPreserveStyle(row, 14, record.getR24_RET_ASSE_LOAN_SME(), null, createHelper);
					updateCellPreserveStyle(row, 15, record.getR24_NO_COMP_ISLAMIC(), null, createHelper);

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	public byte[] GenerateTable_9_Excel(String branch, String jobId, Map<String, Integer> progressMap, String formmode)
			throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		List<RT_MC_TABLE9_ENTITY> dataList = RT_MC_TABLE9_REPO.findBybranchcode(branch);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for MC report. Returning empty result.");
			return new byte[0];
		}
		String templateDir = env.getProperty("output.exportpathtemp");
		// String templateFileName = "Table_1.xlsx";
		System.out.println(templateFileName);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(11);

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

			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					RT_MC_TABLE9_ENTITY record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					progressMap.put(jobId, 5);

					updateCellPreserveStyle(row, 4, record.getR9_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR9_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(9);

					updateCellPreserveStyle(row, 4, record.getR10_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR10_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(10);

					updateCellPreserveStyle(row, 4, record.getR11_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR11_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(11);

					updateCellPreserveStyle(row, 4, record.getR12_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR12_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(12);

					updateCellPreserveStyle(row, 4, record.getR13_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR13_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(13);

					updateCellPreserveStyle(row, 4, record.getR14_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR14_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(14);

					progressMap.put(jobId, 10);
					updateCellPreserveStyle(row, 4, record.getR15_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR15_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(15);

					updateCellPreserveStyle(row, 4, record.getR16_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR16_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(16);

					updateCellPreserveStyle(row, 4, record.getR17_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR17_EVID_PRO_LFI(), null, createHelper);

					row = sheet.getRow(19);

					progressMap.put(jobId, 15);
					updateCellPreserveStyle(row, 4, record.getR20_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR20_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(20);

					updateCellPreserveStyle(row, 4, record.getR21_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR21_EVID_PRO_LFI(), null, createHelper);

					row = sheet.getRow(23);

					updateCellPreserveStyle(row, 4, record.getR24_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR24_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(24);

					updateCellPreserveStyle(row, 4, record.getR25_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR25_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(25);

					updateCellPreserveStyle(row, 4, record.getR26_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR26_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(26);

					updateCellPreserveStyle(row, 4, record.getR27_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR27_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(27);

					updateCellPreserveStyle(row, 4, record.getR28_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR28_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(28);

					updateCellPreserveStyle(row, 4, record.getR29_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR29_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(29);

					progressMap.put(jobId, 35);
					updateCellPreserveStyle(row, 4, record.getR30_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR30_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(30);

					updateCellPreserveStyle(row, 4, record.getR31_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR31_EVID_PRO_LFI(), null, createHelper);

					row = sheet.getRow(33);

					updateCellPreserveStyle(row, 4, record.getR34_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR34_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(34);

					updateCellPreserveStyle(row, 4, record.getR35_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR35_EVID_PRO_LFI(), null, createHelper);

					row = sheet.getRow(37);

					updateCellPreserveStyle(row, 4, record.getR38_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR38_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(38);

					updateCellPreserveStyle(row, 4, record.getR39_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR39_EVID_PRO_LFI(), null, createHelper);

					row = sheet.getRow(41);

					updateCellPreserveStyle(row, 4, record.getR42_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR42_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(42);

					updateCellPreserveStyle(row, 4, record.getR43_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR43_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(43);

					updateCellPreserveStyle(row, 4, record.getR44_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR44_EVID_PRO_LFI(), null, createHelper);

					row = sheet.getRow(46);

					updateCellPreserveStyle(row, 4, record.getR47_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR47_EVID_PRO_LFI(), null, createHelper);

					row = sheet.getRow(49);

					progressMap.put(jobId, 70);
					updateCellPreserveStyle(row, 4, record.getR50_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR50_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(50);

					updateCellPreserveStyle(row, 4, record.getR51_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR51_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(51);

					updateCellPreserveStyle(row, 4, record.getR52_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR52_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(52);

					updateCellPreserveStyle(row, 4, record.getR53_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR53_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(53);

					updateCellPreserveStyle(row, 4, record.getR54_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR54_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(54);

					updateCellPreserveStyle(row, 4, record.getR55_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR55_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(55);

					updateCellPreserveStyle(row, 4, record.getR56_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR56_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(56);

					updateCellPreserveStyle(row, 4, record.getR57_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR57_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(57);

					updateCellPreserveStyle(row, 4, record.getR58_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR58_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(58);

					updateCellPreserveStyle(row, 4, record.getR59_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR59_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(59);

					progressMap.put(jobId, 90);
					updateCellPreserveStyle(row, 4, record.getR60_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR60_EVID_PRO_LFI(), null, createHelper);
					row = sheet.getRow(60);

					updateCellPreserveStyle(row, 4, record.getR61_LFI_RESP(), null, createHelper);
					updateCellPreserveStyle(row, 5, record.getR61_EVID_PRO_LFI(), null, createHelper);

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	/*
	 * private void updateCellPreserveStyle(Row row, int colIndex, Object value,
	 * String formatStr, CreationHelper helper) { Cell cell = (row.getCell(colIndex)
	 * != null) ? row.getCell(colIndex) : row.createCell(colIndex); CellStyle style
	 * = cell.getCellStyle(); if (value != null && formatStr != null) {
	 * style.setDataFormat(helper.createDataFormat().getFormat(formatStr));
	 * cell.setCellStyle(style); } if (value == null) { cell.setCellValue(""); }
	 * else if (value instanceof Number) { cell.setCellValue(((Number)
	 * value).doubleValue()); } else { cell.setCellValue(value.toString()); } }
	 */
	private void updateCellPreserveStyle(Row row, int colIndex, Object value, String formatStr, CreationHelper helper) {
		Cell cell = (row.getCell(colIndex) != null) ? row.getCell(colIndex) : row.createCell(colIndex);

		// System.out.println("value : " + value);

		if (formatStr != null && !formatStr.isEmpty()) {
			CellStyle newStyle = row.getSheet().getWorkbook().createCellStyle();
			newStyle.cloneStyleFrom(cell.getCellStyle());
			newStyle.setDataFormat(helper.createDataFormat().getFormat(formatStr));
			cell.setCellStyle(newStyle);
		}

		if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			cell.setCellFormula(null);
		}
		if (value == null) {
			cell.setCellType(Cell.CELL_TYPE_BLANK);
		} else if (value instanceof Number) {
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(((Number) value).doubleValue());
		} else {
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(value.toString());
		}
	}

	public void MC_TABLE1_Modify(RT_MC_TABLE1_ENTITY updatedData) {
		RT_MC_TABLE1_ENTITY existing = RT_MC_TABLE1_REPO.findByReportDateAndBranchCode(updatedData.getREPORT_DATE(),
				updatedData.getBRANCH_CODE());
		List<String> ignoreFields = Arrays.asList("createUser", "modifyUser", "delFlg","VERIFY_FLG");
		Map<String, String> changes = new LinkedHashMap<>();
		for (Field field : RT_MC_TABLE1_ENTITY.class.getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object oldValue = field.get(existing);
				Object newValue = field.get(updatedData);
				if ((oldValue == null || oldValue.toString().trim().isEmpty())
						&& (newValue == null || newValue.toString().trim().isEmpty())) {
					continue;
				}
				if (ignoreFields.contains(field.getName()) && newValue == null) {
					continue;
				}
				if (oldValue instanceof Date || newValue instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String oldDateStr = (oldValue != null) ? sdf.format(oldValue) : null;
					String newDateStr = (newValue != null) ? sdf.format(newValue) : null;
					if (Objects.equals(oldDateStr, newDateStr)) {
						continue;
					}
				} else {
					if (Objects.equals(oldValue, newValue)) {
						continue;
					}
				}
				if (newValue == null) {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: null");
				} else {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: " + newValue);
				}
				if (newValue != null) {
					field.set(existing, newValue);
				}
			} catch (IllegalAccessException e) {
				System.err.println("Access error for field: " + field.getName() + " - " + e.getMessage());
			}
		}
		System.out.println("changes : " + changes);
		// Audit only if any field was changed
		if (!changes.isEmpty()) {
			auditservice.createBusinessAudit(updatedData.getREPORT_DATE() + " - " + updatedData.getBRANCH_CODE(), // Unique
																													// ID
					"MODIFY", // Action
					"RBS_MC_TABLE1_Bank_Information", // Screen name
					changes, // Changed fields map
					"RT_MC_TABLE1" // Table name
			);
		}
	}

	public void MC_TABLE2_1_Modify(RT_MC_TABLE2_1_ENTITY updatedData) {
		RT_MC_TABLE2_1_ENTITY existing = RT_MC_TABLE2_1_REPO.findByReportDateAndBranchCode(updatedData.getREPORT_DATE(),
				updatedData.getBRANCH_CODE());
		List<String> ignoreFields = Arrays.asList("createUser", "modifyUser", "delFlg","VERIFY_FLG");
		Map<String, String> changes = new LinkedHashMap<>();
		for (Field field : RT_MC_TABLE2_1_ENTITY.class.getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object oldValue = field.get(existing);
				Object newValue = field.get(updatedData);
				if ((oldValue == null || oldValue.toString().trim().isEmpty())
						&& (newValue == null || newValue.toString().trim().isEmpty())) {
					continue;
				}
				if (ignoreFields.contains(field.getName()) && newValue == null) {
					continue;
				}
				if (oldValue instanceof Date || newValue instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String oldDateStr = (oldValue != null) ? sdf.format(oldValue) : null;
					String newDateStr = (newValue != null) ? sdf.format(newValue) : null;
					if (Objects.equals(oldDateStr, newDateStr)) {
						continue;
					}
				} else {
					if (Objects.equals(oldValue, newValue)) {
						continue;
					}
				}
				if (newValue == null) {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: null");
				} else {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: " + newValue);
				}
				if (newValue != null) {
					field.set(existing, newValue);
				}
			} catch (IllegalAccessException e) {
				System.err.println("Access error for field: " + field.getName() + " - " + e.getMessage());
			}
		}
		System.out.println("changes : " + changes);
		// Audit only if any field was changed
		if (!changes.isEmpty()) {
			auditservice.createBusinessAudit(updatedData.getREPORT_DATE() + " - " + updatedData.getBRANCH_CODE(), // Unique
																													// ID
					"MODIFY", // Action
					"RBS_MC_TABLE2_Bank_Consumers", // Screen name
					changes, // Changed fields map
					"RT_MC_TABLE2_1" // Table name
			);
		}
	}

	public void MC_TABLE2_2_Modify(RT_MC_TABLE2_2_ENTITY updatedData) {
		RT_MC_TABLE2_2_ENTITY existing = RT_MC_TABLE2_2_REPO.findByReportDateAndBranchCode(updatedData.getREPORT_DATE(),
				updatedData.getBRANCH_CODE());
		List<String> ignoreFields = Arrays.asList("createUser", "modifyUser", "delFlg","VERIFY_FLG");
		Map<String, String> changes = new LinkedHashMap<>();
		for (Field field : RT_MC_TABLE2_2_ENTITY.class.getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object oldValue = field.get(existing);
				Object newValue = field.get(updatedData);
				if ((oldValue == null || oldValue.toString().trim().isEmpty())
						&& (newValue == null || newValue.toString().trim().isEmpty())) {
					continue;
				}
				if (ignoreFields.contains(field.getName()) && newValue == null) {
					continue;
				}
				if (oldValue instanceof Date || newValue instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String oldDateStr = (oldValue != null) ? sdf.format(oldValue) : null;
					String newDateStr = (newValue != null) ? sdf.format(newValue) : null;
					if (Objects.equals(oldDateStr, newDateStr)) {
						continue;
					}
				} else {
					if (Objects.equals(oldValue, newValue)) {
						continue;
					}
				}
				if (newValue == null) {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: null");
				} else {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: " + newValue);
				}
				if (newValue != null) {
					field.set(existing, newValue);
				}
			} catch (IllegalAccessException e) {
				System.err.println("Access error for field: " + field.getName() + " - " + e.getMessage());
			}
		}
		System.out.println("changes : " + changes);
		// Audit only if any field was changed
		if (!changes.isEmpty()) {
			auditservice.createBusinessAudit(updatedData.getREPORT_DATE() + " - " + updatedData.getBRANCH_CODE(), // Unique
																													// ID
					"MODIFY", // Action
					"RBS_MC_TABLE2_Bank_Consumers", // Screen name
					changes, // Changed fields map
					"RT_MC_TABLE2_2" // Table name
			);
		}
	}

	public void MC_TABLE3_Modify(RT_MC_TABLE3_ENTITY updatedData) {
		RT_MC_TABLE3_ENTITY existing = RT_MC_TABLE3_REPO.findByReportDateAndBranchCode(updatedData.getREPORT_DATE(),
				updatedData.getBRANCH_CODE());
		List<String> ignoreFields = Arrays.asList("createUser", "modifyUser", "delFlg","VERIFY_FLG");
		Map<String, String> changes = new LinkedHashMap<>();
		for (Field field : RT_MC_TABLE3_ENTITY.class.getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object oldValue = field.get(existing);
				Object newValue = field.get(updatedData);
				if ((oldValue == null || oldValue.toString().trim().isEmpty())
						&& (newValue == null || newValue.toString().trim().isEmpty())) {
					continue;
				}
				if (ignoreFields.contains(field.getName()) && newValue == null) {
					continue;
				}
				if (oldValue instanceof Date || newValue instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String oldDateStr = (oldValue != null) ? sdf.format(oldValue) : null;
					String newDateStr = (newValue != null) ? sdf.format(newValue) : null;
					if (Objects.equals(oldDateStr, newDateStr)) {
						continue;
					}
				} else {
					if (Objects.equals(oldValue, newValue)) {
						continue;
					}
				}
				if (newValue == null) {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: null");
				} else {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: " + newValue);
				}
				if (newValue != null) {
					field.set(existing, newValue);
				}
			} catch (IllegalAccessException e) {
				System.err.println("Access error for field: " + field.getName() + " - " + e.getMessage());
			}
		}
		System.out.println("changes : " + changes);
		// Audit only if any field was changed
		if (!changes.isEmpty()) {
			auditservice.createBusinessAudit(updatedData.getREPORT_DATE() + " - " + updatedData.getBRANCH_CODE(), // Unique
																													// ID
					"MODIFY", // Action
					"RBS_MC_TABLE3_Complaints", // Screen name
					changes, // Changed fields map
					"RT_MC_TABLE3" // Table name
			);
		}
	}

	public void MC_TABLE4_1_Modify(RT_MC_TABLE4_1_ENTITY updatedData) {
		RT_MC_TABLE4_1_ENTITY existing = RT_MC_TABLE4_1_REPO.findByReportDateAndBranchCode(updatedData.getREPORT_DATE(),
				updatedData.getBRANCH_CODE());
		List<String> ignoreFields = Arrays.asList("createUser", "modifyUser", "delFlg","VERIFY_FLG");
		Map<String, String> changes = new LinkedHashMap<>();
		for (Field field : RT_MC_TABLE4_1_ENTITY.class.getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object oldValue = field.get(existing);
				Object newValue = field.get(updatedData);
				if ((oldValue == null || oldValue.toString().trim().isEmpty())
						&& (newValue == null || newValue.toString().trim().isEmpty())) {
					continue;
				}
				if (ignoreFields.contains(field.getName()) && newValue == null) {
					continue;
				}
				if (oldValue instanceof Date || newValue instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String oldDateStr = (oldValue != null) ? sdf.format(oldValue) : null;
					String newDateStr = (newValue != null) ? sdf.format(newValue) : null;
					if (Objects.equals(oldDateStr, newDateStr)) {
						continue;
					}
				} else {
					if (Objects.equals(oldValue, newValue)) {
						continue;
					}
				}
				if (newValue == null) {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: null");
				} else {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: " + newValue);
				}
				if (newValue != null) {
					field.set(existing, newValue);
				}
			} catch (IllegalAccessException e) {
				System.err.println("Access error for field: " + field.getName() + " - " + e.getMessage());
			}
		}
		System.out.println("changes : " + changes);
		// Audit only if any field was changed
		if (!changes.isEmpty()) {
			auditservice.createBusinessAudit(updatedData.getREPORT_DATE() + " - " + updatedData.getBRANCH_CODE(), // Unique
																													// ID
					"MODIFY", // Action
					"RBS_MC_TABLE4_Retail_Products", // Screen name
					changes, // Changed fields map
					"RT_MC_TABLE4_1" // Table name
			);
		}
	}

	public void MC_TABLE4_2_Modify(RT_MC_TABLE4_2_ENTITY updatedData) {
		RT_MC_TABLE4_2_ENTITY existing = RT_MC_TABLE4_2_REPO.findByReportDateAndBranchCode(updatedData.getREPORT_DATE(),
				updatedData.getBRANCH_CODE());
		List<String> ignoreFields = Arrays.asList("createUser", "modifyUser", "delFlg","VERIFY_FLG");
		Map<String, String> changes = new LinkedHashMap<>();
		for (Field field : RT_MC_TABLE4_2_ENTITY.class.getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object oldValue = field.get(existing);
				Object newValue = field.get(updatedData);
				if ((oldValue == null || oldValue.toString().trim().isEmpty())
						&& (newValue == null || newValue.toString().trim().isEmpty())) {
					continue;
				}
				if (ignoreFields.contains(field.getName()) && newValue == null) {
					continue;
				}
				if (oldValue instanceof Date || newValue instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String oldDateStr = (oldValue != null) ? sdf.format(oldValue) : null;
					String newDateStr = (newValue != null) ? sdf.format(newValue) : null;
					if (Objects.equals(oldDateStr, newDateStr)) {
						continue;
					}
				} else {
					if (Objects.equals(oldValue, newValue)) {
						continue;
					}
				}
				if (newValue == null) {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: null");
				} else {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: " + newValue);
				}
				if (newValue != null) {
					field.set(existing, newValue);
				}
			} catch (IllegalAccessException e) {
				System.err.println("Access error for field: " + field.getName() + " - " + e.getMessage());
			}
		}
		System.out.println("changes : " + changes);
		// Audit only if any field was changed
		if (!changes.isEmpty()) {
			auditservice.createBusinessAudit(updatedData.getREPORT_DATE() + " - " + updatedData.getBRANCH_CODE(), // Unique
																													// ID
					"MODIFY", // Action
					"RBS_MC_TABLE4_Retail_Products", // Screen name
					changes, // Changed fields map
					"RT_MC_TABLE4_2" // Table name
			);
		}
	}

	public void MC_TABLE5_Modify(RT_MC_TABLE5_ENTITY updatedData) {
		RT_MC_TABLE5_ENTITY existing = RT_MC_TABLE5_REPO.findByReportDateAndBranchCode(updatedData.getREPORT_DATE(),
				updatedData.getBRANCH_CODE());
		List<String> ignoreFields = Arrays.asList("createUser", "modifyUser", "delFlg","VERIFY_FLG");
		Map<String, String> changes = new LinkedHashMap<>();
		for (Field field : RT_MC_TABLE5_ENTITY.class.getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object oldValue = field.get(existing);
				Object newValue = field.get(updatedData);
				if ((oldValue == null || oldValue.toString().trim().isEmpty())
						&& (newValue == null || newValue.toString().trim().isEmpty())) {
					continue;
				}
				if (ignoreFields.contains(field.getName()) && newValue == null) {
					continue;
				}
				if (oldValue instanceof Date || newValue instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String oldDateStr = (oldValue != null) ? sdf.format(oldValue) : null;
					String newDateStr = (newValue != null) ? sdf.format(newValue) : null;
					if (Objects.equals(oldDateStr, newDateStr)) {
						continue;
					}
				} else {
					if (Objects.equals(oldValue, newValue)) {
						continue;
					}
				}
				if (newValue == null) {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: null");
				} else {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: " + newValue);
				}
				if (newValue != null) {
					field.set(existing, newValue);
				}
			} catch (IllegalAccessException e) {
				System.err.println("Access error for field: " + field.getName() + " - " + e.getMessage());
			}
		}
		System.out.println("changes : " + changes);
		// Audit only if any field was changed
		if (!changes.isEmpty()) {
			auditservice.createBusinessAudit(updatedData.getREPORT_DATE() + " - " + updatedData.getBRANCH_CODE(), // Unique
																													// ID
					"MODIFY", // Action
					"RBS_MC_TABLE5_Bank_Employee", // Screen name
					changes, // Changed fields map
					"RT_MC_TABLE5" // Table name
			);
		}
	}

	public void MC_TABLE6_Modify(RT_MC_TABLE6_ENTITY updatedData) {
		RT_MC_TABLE6_ENTITY existing = RT_MC_TABLE6_REPO.findByReportDateAndBranchCode(updatedData.getREPORT_DATE(),
				updatedData.getBRANCH_CODE());
		List<String> ignoreFields = Arrays.asList("createUser", "modifyUser", "delFlg","VERIFY_FLG");
		Map<String, String> changes = new LinkedHashMap<>();
		for (Field field : RT_MC_TABLE6_ENTITY.class.getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object oldValue = field.get(existing);
				Object newValue = field.get(updatedData);
				if ((oldValue == null || oldValue.toString().trim().isEmpty())
						&& (newValue == null || newValue.toString().trim().isEmpty())) {
					continue;
				}
				if (ignoreFields.contains(field.getName()) && newValue == null) {
					continue;
				}
				if (oldValue instanceof Date || newValue instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String oldDateStr = (oldValue != null) ? sdf.format(oldValue) : null;
					String newDateStr = (newValue != null) ? sdf.format(newValue) : null;
					if (Objects.equals(oldDateStr, newDateStr)) {
						continue;
					}
				} else {
					if (Objects.equals(oldValue, newValue)) {
						continue;
					}
				}
				if (newValue == null) {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: null");
				} else {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: " + newValue);
				}
				if (newValue != null) {
					field.set(existing, newValue);
				}
			} catch (IllegalAccessException e) {
				System.err.println("Access error for field: " + field.getName() + " - " + e.getMessage());
			}
		}
		System.out.println("changes : " + changes);
		// Audit only if any field was changed
		if (!changes.isEmpty()) {
			auditservice.createBusinessAudit(updatedData.getREPORT_DATE() + " - " + updatedData.getBRANCH_CODE(), // Unique
																													// ID
					"MODIFY", // Action
					"RBS_MC_TABLE6_Trainings", // Screen name
					changes, // Changed fields map
					"RT_MC_TABLE6" // Table name
			);
		}
	}

	public void MC_TABLE7_1_Modify(RT_MC_TABLE7_1_ENTITY updatedData) {
		RT_MC_TABLE7_1_ENTITY existing = RT_MC_TABLE7_1_REPO.findByReportDateAndBranchCode(updatedData.getREPORT_DATE(),
				updatedData.getBRANCH_CODE());
		List<String> ignoreFields = Arrays.asList("createUser", "modifyUser", "delFlg","VERIFY_FLG");
		Map<String, String> changes = new LinkedHashMap<>();
		for (Field field : RT_MC_TABLE7_1_ENTITY.class.getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object oldValue = field.get(existing);
				Object newValue = field.get(updatedData);
				if ((oldValue == null || oldValue.toString().trim().isEmpty())
						&& (newValue == null || newValue.toString().trim().isEmpty())) {
					continue;
				}
				if (ignoreFields.contains(field.getName()) && newValue == null) {
					continue;
				}
				if (oldValue instanceof Date || newValue instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String oldDateStr = (oldValue != null) ? sdf.format(oldValue) : null;
					String newDateStr = (newValue != null) ? sdf.format(newValue) : null;
					if (Objects.equals(oldDateStr, newDateStr)) {
						continue;
					}
				} else {
					if (Objects.equals(oldValue, newValue)) {
						continue;
					}
				}
				if (newValue == null) {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: null");
				} else {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: " + newValue);
				}
				if (newValue != null) {
					field.set(existing, newValue);
				}
			} catch (IllegalAccessException e) {
				System.err.println("Access error for field: " + field.getName() + " - " + e.getMessage());
			}
		}
		System.out.println("changes : " + changes);
		// Audit only if any field was changed
		if (!changes.isEmpty()) {
			auditservice.createBusinessAudit(updatedData.getREPORT_DATE() + " - " + updatedData.getBRANCH_CODE(), // Unique
																													// ID
					"MODIFY", // Action
					"RBS_MC_TABLE7_Additional_Information", // Screen name
					changes, // Changed fields map
					"RT_MC_TABLE7_1" // Table name
			);
		}
	}

	public void MC_TABLE7_2_Modify(RT_MC_TABLE7_2_ENTITY updatedData) {
		RT_MC_TABLE7_2_ENTITY existing = RT_MC_TABLE7_2_REPO.findByReportDateAndBranchCode(updatedData.getREPORT_DATE(),
				updatedData.getBRANCH_CODE());
		List<String> ignoreFields = Arrays.asList("createUser", "modifyUser", "delFlg","VERIFY_FLG");
		Map<String, String> changes = new LinkedHashMap<>();
		for (Field field : RT_MC_TABLE7_2_ENTITY.class.getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object oldValue = field.get(existing);
				Object newValue = field.get(updatedData);
				if ((oldValue == null || oldValue.toString().trim().isEmpty())
						&& (newValue == null || newValue.toString().trim().isEmpty())) {
					continue;
				}
				if (ignoreFields.contains(field.getName()) && newValue == null) {
					continue;
				}
				if (oldValue instanceof Date || newValue instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String oldDateStr = (oldValue != null) ? sdf.format(oldValue) : null;
					String newDateStr = (newValue != null) ? sdf.format(newValue) : null;
					if (Objects.equals(oldDateStr, newDateStr)) {
						continue;
					}
				} else {
					if (Objects.equals(oldValue, newValue)) {
						continue;
					}
				}
				if (newValue == null) {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: null");
				} else {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: " + newValue);
				}
				if (newValue != null) {
					field.set(existing, newValue);
				}
			} catch (IllegalAccessException e) {
				System.err.println("Access error for field: " + field.getName() + " - " + e.getMessage());
			}
		}
		System.out.println("changes : " + changes);
		// Audit only if any field was changed
		if (!changes.isEmpty()) {
			auditservice.createBusinessAudit(updatedData.getREPORT_DATE() + " - " + updatedData.getBRANCH_CODE(), // Unique
																													// ID
					"MODIFY", // Action
					"RBS_MC_TABLE7_Additional_Information", // Screen name
					changes, // Changed fields map
					"RT_MC_TABLE7_2" // Table name
			);
		}
	}

	public void MC_TABLE8_Modify(RT_MC_TABLE8_ENTITY updatedData) {
		RT_MC_TABLE8_ENTITY existing = RT_MC_TABLE8_REPO.findByReportDateAndBranchCode(updatedData.getREPORT_DATE(),
				updatedData.getBRANCH_CODE());
		List<String> ignoreFields = Arrays.asList("createUser", "modifyUser", "delFlg","VERIFY_FLG");
		Map<String, String> changes = new LinkedHashMap<>();
		for (Field field : RT_MC_TABLE8_ENTITY.class.getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object oldValue = field.get(existing);
				Object newValue = field.get(updatedData);
				if ((oldValue == null || oldValue.toString().trim().isEmpty())
						&& (newValue == null || newValue.toString().trim().isEmpty())) {
					continue;
				}
				if (ignoreFields.contains(field.getName()) && newValue == null) {
					continue;
				}
				if (oldValue instanceof Date || newValue instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String oldDateStr = (oldValue != null) ? sdf.format(oldValue) : null;
					String newDateStr = (newValue != null) ? sdf.format(newValue) : null;
					if (Objects.equals(oldDateStr, newDateStr)) {
						continue;
					}
				} else {
					if (Objects.equals(oldValue, newValue)) {
						continue;
					}
				}
				if (newValue == null) {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: null");
				} else {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: " + newValue);
				}
				if (newValue != null) {
					field.set(existing, newValue);
				}
			} catch (IllegalAccessException e) {
				System.err.println("Access error for field: " + field.getName() + " - " + e.getMessage());
			}
		}
		System.out.println("changes : " + changes);
		// Audit only if any field was changed
		if (!changes.isEmpty()) {
			auditservice.createBusinessAudit(updatedData.getREPORT_DATE() + " - " + updatedData.getBRANCH_CODE(), // Unique
																													// ID
					"MODIFY", // Action
					"RBS_MC_TABLE8_Islamic_Banking", // Screen name
					changes, // Changed fields map
					"RT_MC_TABLE8" // Table name
			);
		}
	}

	public void MC_TABLE9_Modify(RT_MC_TABLE9_ENTITY updatedData) {
		RT_MC_TABLE9_ENTITY existing = RT_MC_TABLE9_REPO.findByReportDateAndBranchCode(updatedData.getREPORT_DATE(),
				updatedData.getBRANCH_CODE());
		List<String> ignoreFields = Arrays.asList("createUser", "modifyUser", "delFlg","VERIFY_FLG");
		Map<String, String> changes = new LinkedHashMap<>();
		for (Field field : RT_MC_TABLE9_ENTITY.class.getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object oldValue = field.get(existing);
				Object newValue = field.get(updatedData);
				if ((oldValue == null || oldValue.toString().trim().isEmpty())
						&& (newValue == null || newValue.toString().trim().isEmpty())) {
					continue;
				}
				if (ignoreFields.contains(field.getName()) && newValue == null) {
					continue;
				}
				if (oldValue instanceof Date || newValue instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String oldDateStr = (oldValue != null) ? sdf.format(oldValue) : null;
					String newDateStr = (newValue != null) ? sdf.format(newValue) : null;
					if (Objects.equals(oldDateStr, newDateStr)) {
						continue;
					}
				} else {
					if (Objects.equals(oldValue, newValue)) {
						continue;
					}
				}
				if (newValue == null) {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: null");
				} else {
					changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: " + newValue);
				}
				if (newValue != null) {
					field.set(existing, newValue);
				}
			} catch (IllegalAccessException e) {
				System.err.println("Access error for field: " + field.getName() + " - " + e.getMessage());
			}
		}
		System.out.println("changes : " + changes);
		// Audit only if any field was changed
		if (!changes.isEmpty()) {
			auditservice.createBusinessAudit(updatedData.getREPORT_DATE() + " - " + updatedData.getBRANCH_CODE(), // Unique
																													// ID
					"MODIFY", // Action
					"RBS_MC_TABLE9_Conduct_Culture_Assessment", // Screen name
					changes, // Changed fields map
					"RT_MC_TABLE9" // Table name
			);
		}
	}
}