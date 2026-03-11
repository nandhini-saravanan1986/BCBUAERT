package com.bornfire.xbrl.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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

	    // Get already uploaded dates
	    public List<String> getUploadedDates() {
	        List<Date> dates = repo.findUploadedDates();
	        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	        return dates.stream()
	                .map(sdf::format)
	                .collect(Collectors.toList());
	    }


	    // Upload MFD File
	    @Transactional
	    public String uploadMidFxDealData(MultipartFile file, Date toDate, String username) throws Exception {

	        Date today = new Date();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	        boolean exists = repo.existsByReportDate(toDate);
	        //  Past date already uploaded → block
	        if (exists && !sdf.format(today).equals(sdf.format(toDate))) {
	            throw new RuntimeException("Data already uploaded for this report date: " + toDate);
	        }
	        //  If today upload again → replace
	        if (exists) {
	            repo.deleteByReportDate(toDate);
	        }

	        Workbook workbook = new XSSFWorkbook(file.getInputStream());

	        RT_MID_FX_DEAL_DC entity = new RT_MID_FX_DEAL_DC();

	        int sheetCount = workbook.getNumberOfSheets();

	        for (int i = 0; i < sheetCount; i++) {

	            String sheetName = workbook.getSheetName(i);
	            Sheet sheet = workbook.getSheetAt(i);

	            if (sheetName.contains("Bonds")) {

	                String value = processSheet(sheet);

	                entity.setActualBonds(new BigDecimal(value.replaceAll("[^0-9.-]", "")));
	                entity.setAbsBonds(new BigDecimal(value.replaceAll("[^0-9.]", "")));

	            } 
	            else if (sheetName.contains("FxSwaps")) {

	                String value = processSheet(sheet);

	                entity.setActualFxSwaps(new BigDecimal(value.replaceAll("[^0-9.-]", "")));
	                entity.setAbsFxSwaps(new BigDecimal(value.replaceAll("[^0-9.]", "")));

	            } 
	            else if (sheetName.contains("Outright Forwards")) {

	                String value = processSheet(sheet);

	                entity.setActualOutrightForwards(new BigDecimal(value.replaceAll("[^0-9.-]", "")));
	                entity.setAbsOutrightForwards(new BigDecimal(value.replaceAll("[^0-9.]", "")));

	            } 
	            else if (sheetName.contains("IRS CIRS")) {

	                String value = processSheet(sheet);

	                entity.setActualIrsCirs(new BigDecimal(value.replaceAll("[^0-9.-]", "")));
	                entity.setAbsIrsCirs(new BigDecimal(value.replaceAll("[^0-9.]", "")));

	            }
	        }

	        entity.setSrlNo(UUID.randomUUID().toString().substring(0, 20));
	        entity.setReportDate(toDate);
	        entity.setReportToDate(toDate);

	        entity.setCreateUser(username);
	        entity.setCreateTime(new Date());

	        entity.setRcreUserId(username);
	        entity.setRcreTime(new Date());

	        entity.setDelFlg("N");
	        entity.setEntityFlg("N");
	        entity.setModifyFlg("N");

	        repo.save(entity);

	        workbook.close();

	        return "AE_MID_FX_DEAL data processed successfully for Report Date: " + toDate;
	    }
	


	private String processSheet(Sheet sheet) {

	    DataFormatter formatter = new DataFormatter();
	    boolean totalFound = false;
	    String result = "";
	    for (Row row : sheet) {
	        for (Cell cell : row) {

	            String value = formatter.formatCellValue(cell).trim();

	            // Step 1: Find Total
	            if ("Total".equalsIgnoreCase(value)) {
	                totalFound = true;
	                continue;
	            }

	            // Step 2: After Total, find BPV (K)
	            if (totalFound && "BPV (K)".equalsIgnoreCase(value)) {

	                int rowIndex = row.getRowNum();
	                int colIndex = cell.getColumnIndex();

	                Row nextRow = sheet.getRow(rowIndex + 1);

	                if (nextRow != null) {
	                    Cell nextCell = nextRow.getCell(colIndex);

	                    if (nextCell != null) {
	                        result = formatter.formatCellValue(nextCell);
	                       
	                    }
	                }
	            }
	        }
	    }
	    
		return result;
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