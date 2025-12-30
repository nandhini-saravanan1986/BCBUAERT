package com.bornfire.xbrl.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.xbrl.entities.RT_FX_Position_Entity;
import com.bornfire.xbrl.entities.RT_FX_Position_Rep;

@Service
public class RT_FX_Position_Service {

	@Autowired
	RT_FX_Position_Rep rtFxPositionRep;

	public String uploadFxData(MultipartFile file, Date fromDate, Date toDate, String user) throws Exception {
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		Sheet sheet = workbook.getSheetAt(2);
		List<RT_FX_Position_Entity> list = new ArrayList<>();
		DataFormatter formatter = new DataFormatter(); // Use formatter to avoid nulls

		// Row indices: FxSwap=1, Spot=5, Total=9
		int[] rowIndices = { 3, 10, 17 };
		String[] types = { "FxSwap Position", "Spot Position", "Total" };

		for (int i = 0; i < rowIndices.length; i++) {
			Row row = sheet.getRow(rowIndices[i]);
			if (row == null) continue;

			RT_FX_Position_Entity entity = new RT_FX_Position_Entity();
			entity.setSrl_no(String.valueOf(i + 1));
			entity.setPosition_type(types[i]);

			// Mapping values from columns C, E, G, I, K, M, O, Q, S, U, W (2,4,6,8,10,12,14,16,18,20,22)
			entity.setGbp(getSafeBigDecimal(row, 2, formatter));
			entity.setUsd(getSafeBigDecimal(row, 4, formatter));
			entity.setJpy(getSafeBigDecimal(row, 6, formatter));
			entity.setChf(getSafeBigDecimal(row, 8, formatter));
			entity.setEur(getSafeBigDecimal(row, 10, formatter));
			entity.setInr(getSafeBigDecimal(row, 12, formatter));
			entity.setSgd(getSafeBigDecimal(row, 14, formatter));
			entity.setSar(getSafeBigDecimal(row, 16, formatter));
			entity.setAed(getSafeBigDecimal(row, 18, formatter));
			entity.setKwd(getSafeBigDecimal(row, 20, formatter));
			entity.setQar(getSafeBigDecimal(row, 22, formatter));

			entity.setReport_from_date(fromDate);
			entity.setReport_to_date(toDate);
			entity.setReport_date(toDate);
			entity.setCreate_user(user);
			entity.setCreate_time(new Date());
			entity.setEntity_flg("N");
			entity.setModify_flg("N");
			entity.setDel_flg("N");

			list.add(entity);
		}

		rtFxPositionRep.saveAll(list);
		workbook.close();
		return "FX Position data processed successfully!";
	}

	// This method is now robust against null cells and NumberFormatExceptions
	private BigDecimal getSafeBigDecimal(Row row, int colIdx, DataFormatter formatter) {
		Cell cell = row.getCell(colIdx);
		if (cell == null) {
			return BigDecimal.ZERO;
		}

		try {
			// formatCellValue never returns null; it returns empty string if cell is blank
			String val = formatter.formatCellValue(cell);
			
			// Clean the string (remove commas from Indian numbering system and spaces)
			val = val.replaceAll(",", "").trim();

			if (val.isEmpty() || val.equalsIgnoreCase("null")) {
				return BigDecimal.ZERO;
			}

			return new BigDecimal(val);
		} catch (Exception e) {
			// Log error if needed: System.out.println("Error at col " + colIdx + ": " + e.getMessage());
			return BigDecimal.ZERO;
		}
	}
}