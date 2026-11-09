package com.bornfire.xbrl.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

import com.bornfire.xbrl.entities.RT_Ecl_upload_Entity;
import com.bornfire.xbrl.util.ExcelUploadHelper;

@Service
public class ECLDataUploadService {

	private static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public String uploadECLdata(MultipartFile file, Date toDate, boolean forceUpload) throws Exception {

		String checkSql = "SELECT COUNT(*) FROM ECL_REPORT_TABLE WHERE TRUNC(REPORT_DATE)=TRUNC(?)";

		Integer count = jdbcTemplate.queryForObject(
				checkSql,
				Integer.class,
				new java.sql.Date(toDate.getTime()));

		if (count != null && count > 0 && !forceUpload) {
			throw new RuntimeException("already uploaded");
		}

		List<RT_Ecl_upload_Entity> list = readExcel(file);

		if (list == null || list.isEmpty()) {
			throw new RuntimeException("Uploaded file is empty");
		}

		LocalDate selectedDate = toLocalDate(toDate);

		for (RT_Ecl_upload_Entity row : list) {
			LocalDate rowDate = toLocalDate(row.getReport_date());
			if (rowDate == null) {
				throw new RuntimeException(
						"Report date is missing in (K column blank). Sample: 30/09/2026, 30-09-2026 or 30-Sep-2026");
			}
			if (!rowDate.equals(selectedDate)) {
				throw new RuntimeException(
						"Report Date Mismatched with the Selected Report Date. File date: "
								+ rowDate.format(DISPLAY_DATE)
								+ ", selected date: "
								+ selectedDate.format(DISPLAY_DATE));
			}
		}

		if (count != null && count > 0 && forceUpload) {
			jdbcTemplate.update(
					"DELETE FROM ECL_REPORT_TABLE WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
					new java.sql.Date(toDate.getTime()));
		}

		int inserted = saveECLBatch(list);
		return "Upload Complete. Rows Inserted: " + inserted;
	}

	public void deleteByReportDate(Date reportDate) {
		if (reportDate == null) {
			throw new RuntimeException("Report date is required.");
		}
		jdbcTemplate.update(
				"DELETE FROM ECL_REPORT_TABLE WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
				new java.sql.Date(reportDate.getTime()));
	}

	private List<RT_Ecl_upload_Entity> readExcel(MultipartFile file) throws Exception {

		List<RT_Ecl_upload_Entity> list = new ArrayList<>();

		try (Workbook workbook = ExcelUploadHelper.openExcelWorkbook(file)) {

			DataFormatter formatter = new DataFormatter();
			Sheet sheet = workbook.getSheetAt(0);

			for (Row row : sheet) {

				if (row.getRowNum() == 0) {
					continue;
				}
				if (isRowEmpty(row)) {
					continue;
				}

				RT_Ecl_upload_Entity e = new RT_Ecl_upload_Entity();

				e.setFacilityid(getString(row.getCell(0), formatter));
				e.setCustomerid(getString(row.getCell(1), formatter));
				e.setCust_name(getString(row.getCell(2), formatter));
				e.setBranch_name(getString(row.getCell(3), formatter));
				e.setProductname(getString(row.getCell(4), formatter));
				e.setLimit(getBigDecimal(row.getCell(5), formatter));
				e.setDrawnamount(getBigDecimal(row.getCell(6), formatter));
				e.setUndrawnamount(getBigDecimal(row.getCell(7), formatter));
				e.setCcf(getBigDecimal(row.getCell(8), formatter));
				e.setEad(getBigDecimal(row.getCell(9), formatter));
				e.setReport_date(getDate(row.getCell(10), row.getRowNum() + 1));
				e.setBasepd(getBigDecimal(row.getCell(11), formatter));
				e.setPositivepd(getBigDecimal(row.getCell(12), formatter));
				e.setNegativepd(getBigDecimal(row.getCell(13), formatter));
				e.setWeighted_pd(getString(row.getCell(14), formatter));
				e.setUnsecuredlgdrate(getBigDecimal(row.getCell(15), formatter));
				e.setLgdrate(getBigDecimal(row.getCell(16), formatter));
				e.setStage(getString(row.getCell(17), formatter));
				e.setFinalweightedecl(getBigDecimal(row.getCell(18), formatter));
				e.setEcl_mgmt_overlay(getBigDecimal(row.getCell(19), formatter));
				e.setSpecific_provision(getBigDecimal(row.getCell(20), formatter));
				e.setInterest_suspense(getBigDecimal(row.getCell(21), formatter));
				e.setInfoflag(getBigDecimal(row.getCell(22), formatter));
				e.setCountry(getString(row.getCell(23), formatter));
				e.setTransfer_stage(getString(row.getCell(24), formatter));
				e.setBrf_1_classification(getString(row.getCell(25), formatter));
				e.setBrf_107(getString(row.getCell(26), formatter));
				e.setBrf_106(getString(row.getCell(27), formatter));
				e.setBasel_2_category(getString(row.getCell(28), formatter));
				e.setTotal_rwa(getBigDecimal(row.getCell(29), formatter));
				e.setFree_text_1(getString(row.getCell(30), formatter));
				e.setFree_text_2(getString(row.getCell(31), formatter));
				e.setFree_text_3(getString(row.getCell(32), formatter));
				e.setFree_text_4(getString(row.getCell(33), formatter));
				e.setFree_text_5(getString(row.getCell(34), formatter));
				e.setTtc_pd(getBigDecimal(row.getCell(35), formatter));
				e.setPit_pd(getBigDecimal(row.getCell(36), formatter));

				list.add(e);
			}
		}

		return list;
	}

	private int saveECLBatch(List<RT_Ecl_upload_Entity> list) {

		String sql = "INSERT INTO ECL_REPORT_TABLE ("
				+ "FACILITYID, CUSTOMERID, CUST_NAME, BRANCH_NAME, PRODUCTNAME, "
				+ "LIMIT, DRAWNAMOUNT, UNDRAWNAMOUNT, CCF, EAD, REPORT_DATE, "
				+ "BASEPD, POSITIVEPD, NEGATIVEPD, WEIGHTED_PD, UNSECUREDLGDRATE, LGDRATE, "
				+ "STAGE, FINALWEIGHTEDECL, ECL_MGMT_OVERLAY, SPECIFIC_PROVISION, INTEREST_SUSPENSE, "
				+ "INFOFLAG, COUNTRY, TRANSFER_STAGE, BRF_1_CLASSIFICATION, BRF_107, BRF_106, "
				+ "BASEL_2_CATEGORY, TOTAL_RWA, FREE_TEXT_1, FREE_TEXT_2, FREE_TEXT_3, "
				+ "FREE_TEXT_4, FREE_TEXT_5, TTC_PD, PIT_PD) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		int inserted = 0;

		for (RT_Ecl_upload_Entity e : list) {
			jdbcTemplate.update(sql,
					e.getFacilityid(),
					e.getCustomerid(),
					e.getCust_name(),
					e.getBranch_name(),
					e.getProductname(),
					e.getLimit(),
					e.getDrawnamount(),
					e.getUndrawnamount(),
					e.getCcf(),
					e.getEad(),
					e.getReport_date(),
					e.getBasepd(),
					e.getPositivepd(),
					e.getNegativepd(),
					e.getWeighted_pd(),
					e.getUnsecuredlgdrate(),
					e.getLgdrate(),
					e.getStage(),
					e.getFinalweightedecl(),
					e.getEcl_mgmt_overlay(),
					e.getSpecific_provision(),
					e.getInterest_suspense(),
					e.getInfoflag(),
					e.getCountry(),
					e.getTransfer_stage(),
					e.getBrf_1_classification(),
					e.getBrf_107(),
					e.getBrf_106(),
					e.getBasel_2_category(),
					e.getTotal_rwa(),
					e.getFree_text_1(),
					e.getFree_text_2(),
					e.getFree_text_3(),
					e.getFree_text_4(),
					e.getFree_text_5(),
					e.getTtc_pd(),
					e.getPit_pd());
			inserted++;
		}

		return inserted;
	}

	private boolean isRowEmpty(Row row) {
		if (row == null) {
			return true;
		}

		DataFormatter formatter = new DataFormatter();

		for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			if (cell != null) {
				String value = formatter.formatCellValue(cell).trim();
				if (!value.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	private String getString(Cell cell, DataFormatter formatter) {
		if (cell == null) {
			return null;
		}
		String value = formatter.formatCellValue(cell);
		if (value != null && !value.trim().isEmpty()) {
			return value.trim();
		}
		return null;
	}

	private BigDecimal getBigDecimal(Cell cell, DataFormatter formatter) {

		if (cell == null) {
			return BigDecimal.ZERO;
		}

		String value = formatter.formatCellValue(cell);

		if (value == null || value.trim().isEmpty()) {
			return BigDecimal.ZERO;
		}

		try {
			value = value.replace(",", "").trim();
			return new BigDecimal(value);
		} catch (Exception e) {
			throw new RuntimeException("Invalid numeric value: " + value);
		}
	}

	private Date getDate(Cell cell, int excelRow) {

		if (cell == null) {
			throw new RuntimeException(missingReportDate("blank", excelRow));
		}

		try {
			int cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_FORMULA) {
				cellType = cell.getCachedFormulaResultType();
			}

			if (cellType == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)) {
				Calendar cal = DateUtil.getJavaCalendar(cell.getNumericCellValue(), false);
				if (cal != null) {
					LocalDate localDate = LocalDate.of(
							cal.get(Calendar.YEAR),
							cal.get(Calendar.MONTH) + 1,
							cal.get(Calendar.DAY_OF_MONTH));
					return java.sql.Date.valueOf(localDate);
				}
			}

			String text = "";
			if (cellType == Cell.CELL_TYPE_STRING) {
				text = cell.getStringCellValue();
			} else {
				text = new DataFormatter(Locale.UK).formatCellValue(cell);
			}

			LocalDate localDate = parseReportDate(text);
			if (localDate == null) {
				throw new RuntimeException(missingReportDate(text, excelRow));
			}
			return java.sql.Date.valueOf(localDate);

		} catch (RuntimeException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException(missingReportDate("blank", excelRow));
		}
	}

	private LocalDate parseReportDate(String text) {

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

		try {
			return LocalDate.parse(text, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} catch (Exception ex) {
		}

		try {
			return LocalDate.parse(text, DateTimeFormatter.ofPattern("d/M/yyyy"));
		} catch (Exception ex) {
		}

		try {
			return LocalDate.parse(text, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		} catch (Exception ex) {
		}

		try {
			return LocalDate.parse(text, DateTimeFormatter.ofPattern("d-M-yyyy"));
		} catch (Exception ex) {
		}

		try {
			return LocalDate.parse(text, DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH));
		} catch (Exception ex) {
		}

		try {
			return LocalDate.parse(text, DateTimeFormatter.ofPattern("d-MMM-yyyy", Locale.ENGLISH));
		} catch (Exception ex) {
		}

		String fixedMonth = fixMonthText(text);
		if (fixedMonth != null) {
			try {
				return LocalDate.parse(fixedMonth, DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH));
			} catch (Exception ex) {
			}
			try {
				return LocalDate.parse(fixedMonth, DateTimeFormatter.ofPattern("d-MMM-yyyy", Locale.ENGLISH));
			} catch (Exception ex) {
			}
		}

		return null;
	}

	private String fixMonthText(String text) {
		if (text == null || !text.contains("-")) {
			return null;
		}
		String[] parts = text.split("-");
		if (parts.length != 3) {
			return null;
		}
		String month = parts[1];
		if (month.length() < 3) {
			return null;
		}
		month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
		return parts[0] + "-" + month + "-" + parts[2];
	}

	private String missingReportDate(String sample, int excelRow) {
		if (sample == null || sample.trim().isEmpty()) {
			sample = "blank";
		} else {
			sample = sample.trim();
		}
		return "Report date is missing in (K column " + sample + ") at row " + excelRow
				+ ". Sample: 30/09/2026, 30-09-2026 or 30-Sep-2026";
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
		return LocalDate.of(
				cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH));
	}
}
