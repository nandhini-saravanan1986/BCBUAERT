/*
 * What this file is
 * -----------------
 * File upload for "Stage Classification BRF40".
 *
 * Why this file is here
 * ---------------------
 * The weekly stage classification Excel has to be saved into
 * BRF_STAGE_CLASSIFICATION_DETAIL.
 * This is one more option on the existing File Upload screen.
 * The other uploads are not changed.
 *
 * How it works
 * ------------
 * 1. On the File Upload screen the user selects "Stage Classification BRF40",
 *    picks the report date, and chooses an Excel file (.xlsx or .xls).
 * 2. The Excel file name and the sheet name are ignored.
 *    The columns inside the sheet are what matter.
 * 3. The sheet must have these columns (same as the weekly ECL file):
 *       ACCT_NUMBER, STAGE, ECL_AMOUNT, SUB_STAGE_CLASS, REPORT_DATE
 *    The sample file spells the stage header as "Stgae". That spelling is accepted.
 * 4. If that report date is already in the table, the screen asks
 *    whether the old rows should be replaced.
 * 5. Each data row is inserted into the table.
 *    Blank rows at the bottom of the sheet are skipped.
 * 6. If a cell has an Excel error such as #REF!, the upload stops and the
 *    message lists the Excel row numbers where that error was found.
 */

package com.bornfire.xbrl.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.xbrl.util.ExcelUploadHelper;

@Service
public class StageClassificationBrf40UploadService {

	private static final String TABLE_NAME = "BRF_STAGE_CLASSIFICATION_DETAIL";

	private static final DateTimeFormatter SHOW_DATE = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Read the Excel and save it for the selected report date.
	 * forceUpload = true means delete the old rows for that date and load again.
	 */
	public String uploadStageClassification(MultipartFile file, Date toDate, boolean forceUpload) throws Exception {

		if (toDate == null) {
			throw new RuntimeException("Reference Date is required.");
		}

		java.sql.Date reportDate = new java.sql.Date(toDate.getTime());

		// Same date already loaded? Ask the screen before replacing it.
		String checkSql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		Integer alreadyThere = jdbcTemplate.queryForObject(checkSql, Integer.class, reportDate);

		if (alreadyThere != null && alreadyThere > 0 && !forceUpload) {
			throw new RuntimeException("already uploaded");
		}

		List<StageRow> rows = readExcel(file);

		if (rows.isEmpty()) {
			throw new RuntimeException("Uploaded file has no data rows.");
		}

		LocalDate selectedDate = toLocalDate(toDate);

		for (StageRow row : rows) {
			LocalDate fileDate = toLocalDate(row.reportDate);
			if (fileDate == null || !fileDate.equals(selectedDate)) {
				String fileDateText = (fileDate == null) ? "blank" : fileDate.format(SHOW_DATE);
				throw new RuntimeException(
						"Report Date Mismatched with the Selected Report Date. File date: "
								+ fileDateText
								+ ", selected date: "
								+ selectedDate.format(SHOW_DATE)
								+ " (Excel row " + row.excelRow + ").");
			}
		}

		if (alreadyThere != null && alreadyThere > 0 && forceUpload) {
			deleteByReportDate(toDate);
		}

		String insertSql = "INSERT INTO " + TABLE_NAME
				+ " (ACCT_NUMBER, STAGE, SUB_STAGE_CLASS, REPORT_DATE, ECL_AMOUNT) VALUES (?,?,?,?,?)";

		int inserted = 0;
		for (StageRow row : rows) {
			jdbcTemplate.update(insertSql,
					row.accountNumber,
					row.stage,
					row.subStageClass,
					row.reportDate,
					row.eclAmount);
			inserted++;
		}

		return "Upload Complete. Rows Inserted: " + inserted;
	}

	/** Used when an approved delete removes this upload for one report date. */
	public void deleteByReportDate(Date reportDate) {
		if (reportDate == null) {
			throw new RuntimeException("Report date is required.");
		}
		jdbcTemplate.update(
				"DELETE FROM " + TABLE_NAME + " WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				new java.sql.Date(reportDate.getTime()));
	}

	/**
	 * Open the workbook, find the sheet that has the stage classification columns,
	 * and turn each data row into a StageRow.
	 */
	private List<StageRow> readExcel(MultipartFile file) throws Exception {

		List<StageRow> rows = new ArrayList<StageRow>();
		DataFormatter formatter = new DataFormatter();

		try (Workbook workbook = ExcelUploadHelper.openExcelWorkbook(file)) {

			Sheet sheet = null;
			int headerRowIndex = -1;
			int accountCol = -1;
			int stageCol = -1;
			int amountCol = -1;
			int subStageCol = -1;
			int dateCol = -1;

			// Sheet name does not matter. Use the first sheet that has the columns.
			for (int s = 0; s < workbook.getNumberOfSheets(); s++) {
				Sheet current = workbook.getSheetAt(s);
				int[] found = findColumns(current, formatter);
				if (found != null) {
					sheet = current;
					headerRowIndex = found[0];
					accountCol = found[1];
					stageCol = found[2];
					amountCol = found[3];
					subStageCol = found[4];
					dateCol = found[5];
					break;
				}
			}

			if (sheet == null) {
				throw new RuntimeException(
						"This Excel is not a Stage Classification file. "
								+ "Required columns: ACCT_NUMBER, STAGE, ECL_AMOUNT, SUB_STAGE_CLASS, REPORT_DATE.");
			}

			// problem text -> Excel row numbers, so one message can list every bad line
			Map<String, List<Integer>> problems = new LinkedHashMap<String, List<Integer>>();

			for (int r = headerRowIndex + 1; r <= sheet.getLastRowNum(); r++) {
				Row excelRow = sheet.getRow(r);
				if (excelRow == null) {
					continue;
				}

				int displayRow = r + 1;
				Cell accountCell = excelRow.getCell(accountCol);
				Cell stageCell = excelRow.getCell(stageCol);
				Cell subStageCell = excelRow.getCell(subStageCol);
				Cell amountCell = excelRow.getCell(amountCol);
				Cell dateCell = excelRow.getCell(dateCol);

				boolean rowProblem = false;
				rowProblem = noteExcelError(problems, accountCell, formatter, "ACCT_NUMBER", displayRow) || rowProblem;
				rowProblem = noteExcelError(problems, stageCell, formatter, "STAGE", displayRow) || rowProblem;
				rowProblem = noteExcelError(problems, subStageCell, formatter, "SUB_STAGE_CLASS", displayRow) || rowProblem;
				rowProblem = noteExcelError(problems, amountCell, formatter, "ECL_AMOUNT", displayRow) || rowProblem;
				rowProblem = noteExcelError(problems, dateCell, formatter, "REPORT_DATE", displayRow) || rowProblem;

				String accountNumber = null;
				String stage = null;
				String subStage = null;
				BigDecimal eclAmount = null;

				if (!rowProblem) {
					accountNumber = readAccountNumber(accountCell);
					stage = readPlainText(stageCell, formatter);
					subStage = readPlainText(subStageCell, formatter);
					try {
						eclAmount = readAmount(amountCell, formatter);
					} catch (RuntimeException ex) {
						addProblem(problems, ex.getMessage(), displayRow);
						rowProblem = true;
					}
				}

				boolean dateBlank = isCellBlank(dateCell, formatter);

				// Empty line at the end of the sheet.
				if (!rowProblem && accountNumber == null && stage == null && subStage == null
						&& eclAmount == null && dateBlank) {
					continue;
				}

				if (rowProblem) {
					continue;
				}

				if (accountNumber == null) {
					addProblem(problems, "Account number is empty", displayRow);
					continue;
				}
				if (accountNumber.length() > 14) {
					addProblem(problems, "Account number is longer than 14 characters: " + accountNumber, displayRow);
					continue;
				}
				if (stage != null && stage.length() > 100) {
					addProblem(problems, "STAGE is longer than 100 characters", displayRow);
					continue;
				}
				if (subStage != null && subStage.length() > 100) {
					addProblem(problems, "SUB_STAGE_CLASS is longer than 100 characters", displayRow);
					continue;
				}

				StageRow row = new StageRow();
				row.excelRow = displayRow;
				row.accountNumber = accountNumber;
				row.stage = stage;
				row.subStageClass = subStage;
				row.eclAmount = eclAmount;
				try {
					row.reportDate = readReportDate(dateCell, formatter);
				} catch (RuntimeException ex) {
					addProblem(problems, ex.getMessage(), displayRow);
					continue;
				}
				rows.add(row);
			}

			if (!problems.isEmpty()) {
				throw new RuntimeException(buildProblemMessage(problems));
			}
		}

		return rows;
	}

	/** Remember one problem and the Excel row where it was found. */
	private void addProblem(Map<String, List<Integer>> problems, String problem, int excelRow) {
		if (problem == null || problem.trim().isEmpty()) {
			problem = "Invalid value";
		}
		List<Integer> rowNumbers = problems.get(problem);
		if (rowNumbers == null) {
			rowNumbers = new ArrayList<Integer>();
			problems.put(problem, rowNumbers);
		}
		rowNumbers.add(Integer.valueOf(excelRow));
	}

	/**
	 * Excel formula errors such as #REF! are not numbers.
	 * Returns true when this cell is an error, and stores the Excel row number.
	 */
	private boolean noteExcelError(Map<String, List<Integer>> problems, Cell cell, DataFormatter formatter,
			String columnName, int excelRow) {
		String errorText = excelErrorText(cell, formatter);
		if (errorText == null) {
			return false;
		}
		addProblem(problems, "Invalid " + columnName + " '" + errorText + "'", excelRow);
		return true;
	}

	/** #REF!, #N/A, #VALUE!, #DIV/0! and the other Excel cell errors. */
	private String excelErrorText(Cell cell, DataFormatter formatter) {
		if (cell == null) {
			return null;
		}

		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_FORMULA) {
			cellType = cell.getCachedFormulaResultType();
		}
		if (cellType != Cell.CELL_TYPE_ERROR) {
			return null;
		}

		String shown;
		try {
			shown = formatter.formatCellValue(cell);
		} catch (Exception ex) {
			shown = "Excel formula error";
		}
		if (shown == null || shown.trim().isEmpty()) {
			return "Excel formula error";
		}
		return shown.trim();
	}

	/** One message that lists every Excel row for each problem. */
	private String buildProblemMessage(Map<String, List<Integer>> problems) {
		StringBuilder message = new StringBuilder();
		for (Map.Entry<String, List<Integer>> entry : problems.entrySet()) {
			if (message.length() > 0) {
				message.append(" ");
			}
			message.append(entry.getKey()).append(" at Excel row ");
			List<Integer> rowNumbers = entry.getValue();
			for (int i = 0; i < rowNumbers.size(); i++) {
				if (i > 0) {
					message.append(", ");
				}
				message.append(rowNumbers.get(i));
			}
			message.append(".");
		}
		return message.toString();
	}

	/**
	 * Look at the top of the sheet for the header row.
	 * Returns header row and column numbers, or null when this sheet is not the file we want.
	 * Column order in the array: header, account, stage, amount, sub stage, report date.
	 */
	private int[] findColumns(Sheet sheet, DataFormatter formatter) {

		int lastLook = sheet.getLastRowNum();
		if (lastLook > 15) {
			lastLook = 15;
		}

		for (int r = 0; r <= lastLook; r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			}

			int accountCol = -1;
			int stageCol = -1;
			int amountCol = -1;
			int subStageCol = -1;
			int dateCol = -1;

			int lastCell = row.getLastCellNum();
			for (int c = 0; c < lastCell; c++) {
				String text = readPlainText(row.getCell(c), formatter);
				if (text == null) {
					continue;
				}

				String key = text.toUpperCase(Locale.ENGLISH).replace(" ", "").replace("_", "").replace("-", "");
				// The weekly file spells this header as "Stgae".
				if ("STGAE".equals(key)) {
					key = "STAGE";
				}

				if ("ACCTNUMBER".equals(key) || "ACCOUNTNUMBER".equals(key) || "ACCOUNTNO".equals(key)) {
					accountCol = c;
				} else if ("STAGE".equals(key)) {
					stageCol = c;
				} else if ("ECLAMOUNT".equals(key) || "ECLAMT".equals(key)) {
					amountCol = c;
				} else if ("SUBSTAGECLASS".equals(key) || "SUBSTAGE".equals(key)) {
					subStageCol = c;
				} else if ("REPORTDATE".equals(key) || "REPORTINGDATE".equals(key)) {
					dateCol = c;
				}
			}

			if (accountCol >= 0 && stageCol >= 0 && amountCol >= 0 && subStageCol >= 0 && dateCol >= 0) {
				return new int[] { r, accountCol, stageCol, amountCol, subStageCol, dateCol };
			}
		}

		return null;
	}

	/** Account number is stored as a number in Excel. Keep all 14 digits, no decimal. */
	private String readAccountNumber(Cell cell) {
		if (cell == null) {
			return null;
		}

		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_FORMULA) {
			cellType = cell.getCachedFormulaResultType();
		}

		String value;
		if (cellType == Cell.CELL_TYPE_NUMERIC) {
			value = BigDecimal.valueOf(Math.round(cell.getNumericCellValue())).toPlainString();
		} else if (cellType == Cell.CELL_TYPE_STRING) {
			value = cell.getStringCellValue();
		} else {
			return null;
		}

		if (value == null) {
			return null;
		}
		value = value.trim().replace(",", "").replace(" ", "");
		if (value.endsWith(".0")) {
			value = value.substring(0, value.length() - 2);
		}
		if (value.isEmpty()) {
			return null;
		}
		return value;
	}

	/** Stage and sub stage. A numeric stage such as 1, 2, 3 is saved as text. */
	private String readPlainText(Cell cell, DataFormatter formatter) {
		if (cell == null) {
			return null;
		}

		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_FORMULA) {
			cellType = cell.getCachedFormulaResultType();
		}

		String value;
		if (cellType == Cell.CELL_TYPE_NUMERIC && !DateUtil.isCellDateFormatted(cell)) {
			double number = cell.getNumericCellValue();
			if (number == Math.rint(number)) {
				value = BigDecimal.valueOf((long) number).toPlainString();
			} else {
				value = BigDecimal.valueOf(number).stripTrailingZeros().toPlainString();
			}
		} else if (cellType == Cell.CELL_TYPE_STRING) {
			value = cell.getStringCellValue();
		} else if (cellType == Cell.CELL_TYPE_BLANK) {
			return null;
		} else {
			value = formatter.formatCellValue(cell);
		}

		if (value == null) {
			return null;
		}
		value = value.trim();
		if (value.isEmpty()) {
			return null;
		}
		return value;
	}

	/** ECL amount goes into NUMBER(24,2). */
	private BigDecimal readAmount(Cell cell, DataFormatter formatter) {
		if (cell == null || isCellBlank(cell, formatter)) {
			return null;
		}

		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_FORMULA) {
			cellType = cell.getCachedFormulaResultType();
		}

		try {
			if (cellType == Cell.CELL_TYPE_ERROR) {
				throw new RuntimeException("not a number");
			}
			if (cellType == Cell.CELL_TYPE_NUMERIC) {
				return BigDecimal.valueOf(cell.getNumericCellValue()).setScale(2, RoundingMode.HALF_UP);
			}

			String value = formatter.formatCellValue(cell);
			if (value == null) {
				return null;
			}
			value = value.trim().replace(",", "").replace("(", "-").replace(")", "");
			if (value.isEmpty() || "-".equals(value)) {
				return null;
			}
			return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
		} catch (Exception ex) {
			String shown = cellText(cell, formatter);
			throw new RuntimeException("Invalid ECL amount '" + shown + "'");
		}
	}

	/**
	 * Report date in the sample file is an Excel date (for example 30-10-2026).
	 * Text dates such as 30-10-2026 or 30/10/2026 are also accepted.
	 */
	private java.sql.Date readReportDate(Cell cell, DataFormatter formatter) {
		if (cell == null || isCellBlank(cell, formatter)) {
			throw new RuntimeException("Report date is empty");
		}

		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_FORMULA) {
			cellType = cell.getCachedFormulaResultType();
		}

		if (cellType == Cell.CELL_TYPE_NUMERIC) {
			double serial = cell.getNumericCellValue();
			// Date-formatted cells, and plain Excel serial numbers such as 46325.
			if (DateUtil.isCellDateFormatted(cell) || (serial > 20000 && serial < 80000)) {
				Calendar cal = DateUtil.getJavaCalendar(serial, false);
				if (cal != null) {
					LocalDate localDate = LocalDate.of(
							cal.get(Calendar.YEAR),
							cal.get(Calendar.MONTH) + 1,
							cal.get(Calendar.DAY_OF_MONTH));
					return java.sql.Date.valueOf(localDate);
				}
			}
		}

		String text = formatter.formatCellValue(cell);
		LocalDate parsed = parseDateText(text);
		if (parsed == null) {
			String shown = cellText(cell, formatter);
			throw new RuntimeException(
					"Report date is not valid ('" + shown + "'). Use an Excel date, or text like 30-10-2026");
		}
		return java.sql.Date.valueOf(parsed);
	}

	/** Text shown in the cell, used when the error message names the bad value. */
	private String cellText(Cell cell, DataFormatter formatter) {
		if (cell == null) {
			return "blank";
		}
		try {
			String shown = formatter.formatCellValue(cell);
			if (shown == null || shown.trim().isEmpty()) {
				return "blank";
			}
			return shown.trim();
		} catch (Exception ex) {
			return "blank";
		}
	}

	private LocalDate parseDateText(String text) {
		if (text == null) {
			return null;
		}
		text = text.trim();
		if (text.isEmpty()) {
			return null;
		}
		if (text.contains(" ")) {
			text = text.substring(0, text.indexOf(' ')).trim();
		}

		String[] patterns = new String[] { "dd/MM/yyyy", "d/M/yyyy", "dd-MM-yyyy", "d-M-yyyy", "yyyy-MM-dd" };
		for (int i = 0; i < patterns.length; i++) {
			try {
				return LocalDate.parse(text, DateTimeFormatter.ofPattern(patterns[i]));
			} catch (Exception ignore) {
				// try the next pattern
			}
		}

		try {
			return LocalDate.parse(text, DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH));
		} catch (Exception ignore) {
			return null;
		}
	}

	private boolean isCellBlank(Cell cell, DataFormatter formatter) {
		if (cell == null) {
			return true;
		}
		if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			return true;
		}
		String value = formatter.formatCellValue(cell);
		return value == null || value.trim().isEmpty();
	}

	private LocalDate toLocalDate(Date date) {
		if (date == null) {
			return null;
		}
		if (date instanceof java.sql.Date) {
			return ((java.sql.Date) date).toLocalDate();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
	}

	/** One Excel data row, ready to insert. */
	private static class StageRow {
		int excelRow;
		String accountNumber;
		String stage;
		String subStageClass;
		java.sql.Date reportDate;
		BigDecimal eclAmount;
	}
}
