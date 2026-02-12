package com.bornfire.xbrl.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil; // Added missing import
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.xbrl.entities.RT_MID_FX_DEAL_DC;
import com.bornfire.xbrl.entities.RT_MID_FX_DEAL_REPO;

@Service
public class RT_MID_FX_DEAL_SERVICE {

	// Fixed syntax error here: added .class and closing parentheses
	private static final Logger logger = LoggerFactory.getLogger(RT_MID_FX_DEAL_SERVICE.class);

	@Autowired
	RT_MID_FX_DEAL_REPO repo;

	public String uploadMidFxDealData(MultipartFile file, Date fromDate, Date toDate, String username) {

		// 1. FILE NAME VALIDATION
		String fileName = file.getOriginalFilename();
		if (fileName == null || !fileName.toUpperCase().startsWith("MID_FX_DEAL")) {
			return "Validation Failed: File name must start with 'MID_FX_DEAL'.";
		}

		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			List<RT_MID_FX_DEAL_DC> list = new ArrayList<>();

			// To avoid multiple deletes if there are multiple rows for the same date
			Set<Date> processedDates = new HashSet<>();

			// 2. LOOP THROUGH ROWS (Start from row index 2 or 5 depending on where your
			// data begins)
			for (int i = 2; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				Date reportDate = getDate(row.getCell(0));
				if (reportDate == null)
					continue;

				// 3. REPLACE LOGIC: Delete existing data for this date once per upload session
				if (!processedDates.contains(reportDate)) {
					repo.deleteByReportDate(reportDate);
					processedDates.add(reportDate);
				}

				RT_MID_FX_DEAL_DC entity = new RT_MID_FX_DEAL_DC();

				// Audit & Header Info
				entity.setSrlNo(UUID.randomUUID().toString().substring(0, 20));
				entity.setReportDate(reportDate);
				entity.setCreateUser(username);
				entity.setCreateTime(new Date());
				entity.setReportFromDate(fromDate);
				entity.setReportToDate(toDate);
				entity.setRcreUserId(username);
				entity.setRcreTime(new Date());
				entity.setDelFlg("N");
				entity.setEntityFlg("N");
				entity.setModifyFlg("N");

				// --- ACTUAL VALUES (INR) ---
				entity.setActualBonds(getDecimal(row.getCell(1)));
				entity.setActualFxSwaps(getDecimal(row.getCell(2)));
				entity.setActualOutrightForwards(getDecimal(row.getCell(3)));

				// --- ABSOLUTE VALUES (INR) ---
				entity.setAbsBonds(getDecimal(row.getCell(5)).abs());
				entity.setAbsFxSwaps(getDecimal(row.getCell(6)).abs());
				entity.setAbsOutrightForwards(getDecimal(row.getCell(7)).abs());

				list.add(entity);
			}

			if (list.isEmpty()) {
				return "Upload Failed: No valid data found in the spreadsheet.";
			}

			repo.saveAll(list);
			return "Success: " + list.size() + " records processed and replaced successfully.";

		} catch (Exception e) {
			logger.error("Error during Mid FX Deal Upload: ", e);
			return "System Error: " + e.getMessage();
		}
	}

	// Helper for Numbers
	private BigDecimal getDecimal(Cell cell) {
		if (cell == null)
			return BigDecimal.ZERO;
		DataFormatter formatter = new DataFormatter();
		try {
			String val = formatter.formatCellValue(cell).replaceAll(",", "").trim();
			if (val.isEmpty() || "-".equals(val) || "null".equalsIgnoreCase(val)) {
				return BigDecimal.ZERO;
			}
			return new BigDecimal(val);
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

	// Helper for Dates
	private Date getDate(Cell cell) {
		if (cell == null)
			return null;
		try {
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			}
			DataFormatter formatter = new DataFormatter();
			String dateStr = formatter.formatCellValue(cell);
			SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.ENGLISH);
			return sdf.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
	}
}