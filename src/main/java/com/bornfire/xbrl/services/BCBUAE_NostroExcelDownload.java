// Package as per your structure
package com.bornfire.xbrl.services;

import com.bornfire.xbrl.entities.BcbuaeFxRiskDataRepository;
import com.bornfire.xbrl.entities.BcbuaeFxriskdata;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BCBUAE_NostroExcelDownload {

	@Autowired
	BcbuaeFxRiskDataRepository friskdataRepo;

	@Autowired
	
	
    public byte[] generateFxRiskDataExcel() throws IOException {

        List<BcbuaeFxriskdata> fxriskdataEntity = friskdataRepo.getfxriskdatalistdata();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Fx Risk Data");

        // Header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // Cell style
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        String[] headers = { "Date", "Bank Name", "Bank Symbol", "Conventional/Islamic", "Local/Foreign", "CBUAE Tiering",
                "Currency", "FX Net Open Position (USD equivalent 000)", "Off Balance Sheet Position (USD equivalent 000)",
                "On Balance Sheet Position (USD equivalent 000)", "FX Net Open Position Foreign Currency (000)",
                "Internal Long Limit (USD equivalent 000)", "Internal Long Limit Trigger Status",
                "Internal Short Limit (USD equivalent 000)", "Internal Short Limit Trigger Status",
                "Bank Core Tier 1 Capital (000 USD)", "Bank Core Tier 1 Capital LC(000)", "% of Group Capital" };

        // Create header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Fill data rows
        int rowIndex = 1;
        for (BcbuaeFxriskdata friskdata : fxriskdataEntity) {
            Row row = sheet.createRow(rowIndex++);

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = formatter.format(friskdata.getBank_date());

            row.createCell(0).setCellValue(formattedDate);
            row.createCell(1).setCellValue(friskdata.getBank_name());
            row.createCell(2).setCellValue(friskdata.getBank_symbol());
            row.createCell(3).setCellValue(friskdata.getConventional_islamic());
            row.createCell(4).setCellValue(friskdata.getLocal_foreign());
            row.createCell(5).setCellValue(friskdata.getCbuae_tiering());
            row.createCell(6).setCellValue(friskdata.getCurrency());
            row.createCell(7).setCellValue(friskdata.getFx_net_openposition() != null ? friskdata.getFx_net_openposition().doubleValue() : 0.0);
			row.createCell(8).setCellValue(friskdata.getOff_balance_sheet_position() != null ? friskdata.getOff_balance_sheet_position().doubleValue() : 0.0);

			row.createCell(9).setCellValue(friskdata.getOn_balance_sheet_position() != null ? friskdata.getOn_balance_sheet_position().doubleValue() : 0.0);

			row.createCell(10).setCellValue(friskdata.getFx_net_openposition_foreign_currency() != null ? friskdata.getFx_net_openposition_foreign_currency().doubleValue() : 0.0);

			row.createCell(11).setCellValue(friskdata.getInternal_long_limit() != null ? friskdata.getInternal_long_limit().doubleValue() : 0.0);

			row.createCell(12).setCellValue(friskdata.getInternal_long_limit_trigger_status());  // String, no conversion needed

			row.createCell(13).setCellValue(friskdata.getInternal_short_limit() != null ? friskdata.getInternal_short_limit().doubleValue() : 0.0);

			row.createCell(14).setCellValue(friskdata.getInternal_short_limit_trigger_status() != null ? friskdata.getInternal_short_limit_trigger_status().doubleValue() : 0.0);

			row.createCell(15).setCellValue(friskdata.getBank_core_tier1_capital() != null ? friskdata.getBank_core_tier1_capital().doubleValue() : 0.0);

			row.createCell(16).setCellValue(friskdata.getBank_core_tier1_capital_lc() != null ? friskdata.getBank_core_tier1_capital_lc().doubleValue() : 0.0);

			row.createCell(17).setCellValue(friskdata.getPercent_of_group_capital() != null ? friskdata.getPercent_of_group_capital().doubleValue() : 0.0);

			//row.createCell(18).setCellValue(friskdata.getBank_core_tier1_capital_aed() != null ? friskdata.getBank_core_tier1_capital_aed().doubleValue() : 0.0);

            // Apply cell style
            for (int i = 0; i < headers.length; i++) {
                Cell cell = row.getCell(i);
                if (cell == null) {
                    cell = row.createCell(i);
                }
                cell.setCellStyle(cellStyle);
            }
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    private double toDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : 0.0;
    }
}
