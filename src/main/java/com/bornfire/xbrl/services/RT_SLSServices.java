package com.bornfire.xbrl.services;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import com.bornfire.xbrl.entities.RT_SLS_ENTITIES;
import com.bornfire.xbrl.entities.RT_SLS_Repository;



@Service
public class RT_SLSServices {
	private static final Logger logger = LoggerFactory.getLogger(RT_SLSServices.class);
	

	@Autowired
	private Environment env;
	
	@Autowired
	RT_SLS_Repository rt_sls_repository;
	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	
	public byte[] getSlsExcel(String filename,String reportdate, String currency,String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		

		List<RT_SLS_ENTITIES> dataList = rt_sls_repository.rtslslistbydate(dateformat.parse(reportdate),currency);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for sls report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
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
			font.setFontHeightInPoints((short) 11); // size 8
			font.setFontName("Calibri");

			CellStyle numberStyle = workbook.createCellStyle();
			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

				
			
			  Row daterow=sheet.getRow(4);
			  Cell datecell =daterow.getCell(3);
			  datecell.setCellValue(reportdate);
			 
			
			
			int startRow = 10;

			if (!dataList.isEmpty()) {
				
				String [] fieldSuffixes= {"DAY1","DAY2_7","DAY8_14","DAY15_30","DAY31_TO_2M","MORE2M_TO_3M",
											"OVER3M_TO_6M","OVER6M_TO_1Y","OVER1Y_TO_3Y","OVER3Y_TO_5Y","OVER5Y"};
				
				
				
				for (int rowIndex = 10; rowIndex < 85; rowIndex++) {
					
					RT_SLS_ENTITIES record = dataList.get(0);
					
					System.out.println(rowIndex);
					if(rowIndex==40 || rowIndex==41 || rowIndex==42 || rowIndex==43 || rowIndex==44 ||rowIndex==45 || rowIndex==74 
							|| rowIndex==75 || rowIndex==76 ||rowIndex==77|| rowIndex==78 ||rowIndex==79 || rowIndex==80 || rowIndex==82 ||rowIndex==85 ) {
						continue;
					}

					
					for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
						
						
					String fieldName = "R"+(rowIndex+1)+"_" + fieldSuffixes[colIndex];
					Row row = sheet.getRow(rowIndex);
					Cell cell = row.getCell(colIndex+3);
					
					 try {
				          Field field = RT_SLS_ENTITIES.class.getDeclaredField(fieldName);
				          field.setAccessible(true);
				          Object value = field.get(record);
				          System.out.println("fieldname"+value);
				          if (value instanceof BigDecimal) {
				              cell.setCellValue(((BigDecimal) value).doubleValue());
				              //cell.setCellStyle(numberStyle);
				          } else {
				              cell.setCellValue(0.00);
				              //cell.setCellStyle(textStyle);
				          }
				      } catch (NoSuchFieldException | IllegalAccessException e) {
				          cell.setCellValue("");
				          cell.setCellStyle(textStyle);
				          LoggerFactory.getLogger(getClass()).warn("Field not found or inaccessible: {}", fieldName);
				      }
						
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
