package com.bornfire.xbrl.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bornfire.xbrl.entities.RT_Liquidity_Risk_Data_Template;
import com.bornfire.xbrl.entities.RT_Liquidity_Risk_Data_Template_Repository;

@Service
public class RT_Liquidity_Risk_Data_Service {

	private static final Logger logger = LoggerFactory.getLogger(RT_Liquidity_Risk_Data_Service.class);
	
    @Autowired
    RT_Liquidity_Risk_Data_Template_Repository LiquidityRiskDataRepository;

    @Autowired
    private Environment env;

    // Update existing record
    public boolean updateLiquidityRisk(RT_Liquidity_Risk_Data_Template updatedEntity) {
        Optional<RT_Liquidity_Risk_Data_Template> existingOpt = LiquidityRiskDataRepository.findById(updatedEntity.getSlno());

        if (existingOpt.isPresent()) {
            RT_Liquidity_Risk_Data_Template existing = existingOpt.get();

            existing.setDataDate(updatedEntity.getDataDate());
            existing.setBankName(updatedEntity.getBankName());
            existing.setHeadOfficeSubsidiary(updatedEntity.getHeadOfficeSubsidiary());
            existing.setBankSymbol(updatedEntity.getBankSymbol());
            existing.setConventionalIslamic(updatedEntity.getConventionalIslamic());
            existing.setLocalForeign(updatedEntity.getLocalForeign());
            existing.setCbuaeTiering(updatedEntity.getCbuaeTiering());
            existing.setGlLevel1(updatedEntity.getGlLevel1());
            existing.setGlLevel2(updatedEntity.getGlLevel2());
            existing.setGlLevel3(updatedEntity.getGlLevel3());
            existing.setOptionType(updatedEntity.getOptionType());
            existing.setRateType(updatedEntity.getRateType());
            existing.setReferenceRate(updatedEntity.getReferenceRate());
            existing.setInstrumentCurrency(updatedEntity.getInstrumentCurrency());

            existing.setOutstandingBalance(updatedEntity.getOutstandingBalance());
            existing.setOvernight(updatedEntity.getOvernight());
            existing.setOnTo1m(updatedEntity.getOnTo1m());
            existing.setOneMTo3m(updatedEntity.getOneMTo3m());
            existing.setThreeMTo6m(updatedEntity.getThreeMTo6m());
            existing.setSixMTo9m(updatedEntity.getSixMTo9m());
            existing.setNineMTo1y(updatedEntity.getNineMTo1y());
            existing.setOneYTo1_5y(updatedEntity.getOneYTo1_5y());
            existing.setOne5yTo2y(updatedEntity.getOne5yTo2y());
            existing.setTwoYTo3y(updatedEntity.getTwoYTo3y());
            existing.setThreeYTo4y(updatedEntity.getThreeYTo4y());
            existing.setFourYTo5y(updatedEntity.getFourYTo5y());
            existing.setFiveYTo6y(updatedEntity.getFiveYTo6y());
            existing.setSixYTo7y(updatedEntity.getSixYTo7y());
            existing.setSevenYTo8y(updatedEntity.getSevenYTo8y());
            existing.setEightYTo9y(updatedEntity.getEightYTo9y());
            existing.setNineYTo10y(updatedEntity.getNineYTo10y());
            existing.setTenYTo15y(updatedEntity.getTenYTo15y());
            existing.setFifteenYTo20y(updatedEntity.getFifteenYTo20y());
            existing.setTwentyYAbove(updatedEntity.getTwentyYAbove());
            existing.setNonMaturing(updatedEntity.getNonMaturing());
            // Final details
            existing.setReportSubmitDate(updatedEntity.getReportSubmitDate());
            existing.setModifyTime(new Date());// track update time
            
            
            LiquidityRiskDataRepository.save(existing);
            return true;
        } else {
            return false;
        }
    }
    public byte[] generateLiquidityDataExcel() throws Exception {
        logger.info("Service: Starting Liquidity Risk Excel generation process in memory.");

        List<RT_Liquidity_Risk_Data_Template> dataList = LiquidityRiskDataRepository.getLiquiditylist();
        if (dataList.isEmpty()) {
            logger.warn("Service: No data found. Returning empty byte array.");
            return new byte[0];
        }

        String templateDir = env.getProperty("output.exportpathtemp");
        String templateFileName = "CBUAE_Liquidity Risk_Data_Template.xls";
        Path templatePath = Paths.get(templateDir, templateFileName);
        logger.info("Service: Template path - {}", templatePath.toAbsolutePath());

        if (!Files.exists(templatePath)) throw new FileNotFoundException("Template not found at: " + templatePath.toAbsolutePath());
        if (!Files.isReadable(templatePath)) throw new SecurityException("Template not readable: " + templatePath.toAbsolutePath());

        try (InputStream templateInputStream = Files.newInputStream(templatePath);
             Workbook workbook = WorkbookFactory.create(templateInputStream);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheetAt(0);
            CreationHelper createHelper = workbook.getCreationHelper();

            // Style definitions
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

            int startRow = 1;

            for (int i = 0; i < dataList.size(); i++) {
                RT_Liquidity_Risk_Data_Template record = dataList.get(i);
                Row row = sheet.getRow(startRow + i);
                if (row == null) row = sheet.createRow(startRow + i);

                int col = 0;

                // Date
               
                Cell cell0 = row.createCell(col++);
                if (record.getDataDate() != null) {
                    cell0.setCellValue(record.getDataDate());
                    cell0.setCellStyle(dateStyle);
                } else {
                    cell0.setCellValue("");
                    cell0.setCellStyle(textStyle);
                }
                // Auto-size the last column (col - 1)
                sheet.autoSizeColumn(col - 1);


             // String fields with textStyle
             Cell cell1 = row.createCell(col++);
             cell1.setCellValue(record.getBankName() != null ? record.getBankName() : "");
             cell1.setCellStyle(textStyle);

             Cell cell2 = row.createCell(col++);
             cell2.setCellValue(record.getHeadOfficeSubsidiary() != null ? record.getHeadOfficeSubsidiary() : "");
             cell2.setCellStyle(textStyle);

             Cell cell3 = row.createCell(col++);
             cell3.setCellValue(record.getBankSymbol() != null ? record.getBankSymbol() : "");
             cell3.setCellStyle(textStyle);

             Cell cell4 = row.createCell(col++);
             cell4.setCellValue(record.getConventionalIslamic() != null ? record.getConventionalIslamic() : "");
             cell4.setCellStyle(textStyle);

             Cell cell5 = row.createCell(col++);
             cell5.setCellValue(record.getLocalForeign() != null ? record.getLocalForeign() : "");
             cell5.setCellStyle(textStyle);

             Cell cell6 = row.createCell(col++);
             cell6.setCellValue(record.getCbuaeTiering() != null ? record.getCbuaeTiering() : "");
             cell6.setCellStyle(textStyle);

             Cell cell7 = row.createCell(col++);
             cell7.setCellValue(record.getGlLevel1() != null ? record.getGlLevel1() : "");
             cell7.setCellStyle(textStyle);

             Cell cell8 = row.createCell(col++);
             cell8.setCellValue(record.getGlLevel2() != null ? record.getGlLevel2() : "");
             cell8.setCellStyle(textStyle);

             Cell cell9 = row.createCell(col++);
             cell9.setCellValue(record.getGlLevel3() != null ? record.getGlLevel3() : "");
             cell9.setCellStyle(textStyle);

             Cell cell10 = row.createCell(col++);
             cell10.setCellValue(record.getOptionType() != null ? record.getOptionType() : "");
             cell10.setCellStyle(textStyle);

             Cell cell11 = row.createCell(col++);
             cell11.setCellValue(record.getRateType() != null ? record.getRateType() : "");
             cell11.setCellStyle(textStyle);

             Cell cell12 = row.createCell(col++);
             cell12.setCellValue(record.getReferenceRate() != null ? record.getReferenceRate() : "");
             cell12.setCellStyle(textStyle);

             Cell cell13 = row.createCell(col++);
             cell13.setCellValue(record.getInstrumentCurrency() != null ? record.getInstrumentCurrency() : "");
             cell13.setCellStyle(textStyle);

             // BigDecimal fields with numberStyle
             Cell cell14 = row.createCell(col++);
             cell14.setCellValue(record.getOutstandingBalance() != null ? record.getOutstandingBalance().doubleValue() : 0.0);
             cell14.setCellStyle(numberStyle);

             Cell cell15 = row.createCell(col++);
             cell15.setCellValue(record.getOvernight() != null ? record.getOvernight().doubleValue() : 0.0);
             cell15.setCellStyle(numberStyle);

             Cell cell16 = row.createCell(col++);
             cell16.setCellValue(record.getOnTo1m() != null ? record.getOnTo1m().doubleValue() : 0.0);
             cell16.setCellStyle(numberStyle);

             Cell cell17 = row.createCell(col++);
             cell17.setCellValue(record.getOneMTo3m() != null ? record.getOneMTo3m().doubleValue() : 0.0);
             cell17.setCellStyle(numberStyle);

             Cell cell18 = row.createCell(col++);
             cell18.setCellValue(record.getThreeMTo6m() != null ? record.getThreeMTo6m().doubleValue() : 0.0);
             cell18.setCellStyle(numberStyle);

             Cell cell19 = row.createCell(col++);
             cell19.setCellValue(record.getSixMTo9m() != null ? record.getSixMTo9m().doubleValue() : 0.0);
             cell19.setCellStyle(numberStyle);

             Cell cell20 = row.createCell(col++);
             cell20.setCellValue(record.getNineMTo1y() != null ? record.getNineMTo1y().doubleValue() : 0.0);
             cell20.setCellStyle(numberStyle);

             Cell cell21 = row.createCell(col++);
             cell21.setCellValue(record.getOneYTo1_5y() != null ? record.getOneYTo1_5y().doubleValue() : 0.0);
             cell21.setCellStyle(numberStyle);

             Cell cell22 = row.createCell(col++);
             cell22.setCellValue(record.getOne5yTo2y() != null ? record.getOne5yTo2y().doubleValue() : 0.0);
             cell22.setCellStyle(numberStyle);

             Cell cell23 = row.createCell(col++);
             cell23.setCellValue(record.getTwoYTo3y() != null ? record.getTwoYTo3y().doubleValue() : 0.0);
             cell23.setCellStyle(numberStyle);

             Cell cell24 = row.createCell(col++);
             cell24.setCellValue(record.getThreeYTo4y() != null ? record.getThreeYTo4y().doubleValue() : 0.0);
             cell24.setCellStyle(numberStyle);

             Cell cell25 = row.createCell(col++);
             cell25.setCellValue(record.getFourYTo5y() != null ? record.getFourYTo5y().doubleValue() : 0.0);
             cell25.setCellStyle(numberStyle);

             Cell cell26 = row.createCell(col++);
             cell26.setCellValue(record.getFiveYTo6y() != null ? record.getFiveYTo6y().doubleValue() : 0.0);
             cell26.setCellStyle(numberStyle);

             Cell cell27 = row.createCell(col++);
             cell27.setCellValue(record.getSixYTo7y() != null ? record.getSixYTo7y().doubleValue() : 0.0);
             cell27.setCellStyle(numberStyle);

             Cell cell28 = row.createCell(col++);
             cell28.setCellValue(record.getSevenYTo8y() != null ? record.getSevenYTo8y().doubleValue() : 0.0);
             cell28.setCellStyle(numberStyle);

             Cell cell29 = row.createCell(col++);
             cell29.setCellValue(record.getEightYTo9y() != null ? record.getEightYTo9y().doubleValue() : 0.0);
             cell29.setCellStyle(numberStyle);

             Cell cell30 = row.createCell(col++);
             cell30.setCellValue(record.getNineYTo10y() != null ? record.getNineYTo10y().doubleValue() : 0.0);
             cell30.setCellStyle(numberStyle);

             Cell cell31 = row.createCell(col++);
             cell31.setCellValue(record.getTenYTo15y() != null ? record.getTenYTo15y().doubleValue() : 0.0);
             cell31.setCellStyle(numberStyle);

             Cell cell32 = row.createCell(col++);
             cell32.setCellValue(record.getFifteenYTo20y() != null ? record.getFifteenYTo20y().doubleValue() : 0.0);
             cell32.setCellStyle(numberStyle);

             Cell cell33 = row.createCell(col++);
             cell33.setCellValue(record.getTwentyYAbove() != null ? record.getTwentyYAbove().doubleValue() : 0.0);
             cell33.setCellStyle(numberStyle);

             Cell cell34 = row.createCell(col++);
             cell34.setCellValue(record.getNonMaturing() != null ? record.getNonMaturing().doubleValue() : 0.0);
             cell34.setCellStyle(numberStyle);

                // Report Submit Date
				/*
				 * Cell finalCell = row.createCell(col++); if (record.getReportSubmitDate() !=
				 * null) { finalCell.setCellValue(record.getReportSubmitDate());
				 * finalCell.setCellStyle(dateStyle); } else { finalCell.setCellValue("");
				 * finalCell.setCellStyle(textStyle); }
				 */
                // Apply styles for each cell
                for (int j = 0; j < col; j++) {
                    Cell c = row.getCell(j);
                    if (c != null && c.getCellStyle() == null) {
                        c.setCellStyle(numberStyle); // fallback
                        
                    }
                }
            }

            workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
            workbook.write(out);

            logger.info("Service: Liquidity Risk Excel data successfully written to memory buffer ({} bytes).", out.size());
            return out.toByteArray();
        }
    }



}
