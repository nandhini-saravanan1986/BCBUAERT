package com.bornfire.xbrl.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.poi.ss.usermodel.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bornfire.xbrl.entities.RT_Fxriskdata;
import com.bornfire.xbrl.entities.RT_Investment_Risk_Data_Dashboard_Template;
import com.bornfire.xbrl.entities.RT_Investment_Risk_Data_Dashboard_TemplateRepository;
import com.bornfire.xbrl.entities.RT_MmData;
import com.bornfire.xbrl.entities.RT_MmDataRepository;
import java.io.InputStream;
import java.nio.file.Files;
import java.io.ByteArrayOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileNotFoundException;

@Service
public class RT_InvestmentRiskDataDashboard_Service {
	
    private static final Logger logger = LoggerFactory.getLogger(RT_InvestmentRiskDataDashboard_Service.class);

    /**
     * Grey (automatic) columns on the CBUAE Investment Risk Data sheet.
     * These cells already contain Excel formulas (VLOOKUP / ratios / OK-BREACH / CHECK),
     * so download must not overwrite them with database values.
     */
    private static final Set<Integer> AUTOMATIC_FORMULA_COLUMNS = new HashSet<Integer>(Arrays.asList(
            4, 5, 6, 7, 10, 14, 17, 23, 24, 27, 30, 31, 37, 38, 44, 45, 51, 52,
            57, 58, 61, 62, 66, 69, 72, 76, 80, 84, 86, 88, 90, 92, 94, 96, 98));

    @Autowired
    private Environment env;

    @Autowired
	RT_Investment_Risk_Data_Dashboard_TemplateRepository RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS;

    @Autowired
    private SessionFactory sessionFactory;
    
	@Autowired
	AuditService auditservice;    
    
    public boolean updateinvestmentriskdatadictionary(RT_Investment_Risk_Data_Dashboard_Template updatedData) {
	    System.out.println("Looking for record with SI_NO: " + updatedData.getSI_NO());

	    RT_Investment_Risk_Data_Dashboard_Template existing = RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS.getParticularDataBySI_NO(updatedData.getSI_NO());


	    RT_Investment_Risk_Data_Dashboard_Template dbUser = new RT_Investment_Risk_Data_Dashboard_Template();
		org.springframework.beans.BeanUtils.copyProperties(existing, dbUser);
		
	    if (existing != null) {
	        // Update fields
	       
	    	// --- copy every simple field from updatedData to existing -------------------
	    	existing.setDataDate(updatedData.getDataDate());
	    	existing.setBankName(updatedData.getBankName());
	    	existing.setGroupHeadOfficeSubsidiary(updatedData.getGroupHeadOfficeSubsidiary());
	    	existing.setSubsidiary(updatedData.getSubsidiary());
	    	existing.setBankSymbol(updatedData.getBankSymbol());
	    	existing.setConventionalIslamic(updatedData.getConventionalIslamic());
	    	existing.setLocalForeign(updatedData.getLocalForeign());
	    	existing.setCbuAeTiering(updatedData.getCbuAeTiering());

	    	existing.setAssetBalanceSheetSizeAed(updatedData.getAssetBalanceSheetSizeAed());
	    	existing.setInvestmentBookSizeAed(updatedData.getInvestmentBookSizeAed());
	    	existing.setBookSizeTotalAssetsPct(updatedData.getBookSizeTotalAssetsPct());

	    	existing.setYtdNetPnlAed(updatedData.getYtdNetPnlAed());
	    	existing.setYtdFvtociUnrealizedLossAed(updatedData.getYtdFvtociUnrealizedLossAed());
	    	existing.setCet1Aed(updatedData.getCet1Aed());
	    	existing.setUglCoreTier1RelativeImpactPct(updatedData.getUglCoreTier1RelativeImpactPct());
	    	existing.setYtdAmortizedUnrealizedLossAed(updatedData.getYtdAmortizedUnrealizedLossAed());
	    	existing.setYtdHqlaAcUnrealizedLossAed(updatedData.getYtdHqlaAcUnrealizedLossAed());
	    	existing.setHqlaAcCoreTier1ImpactPct(updatedData.getHqlaAcCoreTier1ImpactPct());

	    	existing.setIrVarConfidenceInterval(updatedData.getIrVarConfidenceInterval());
	    	existing.setIrVarHoldingPeriod(updatedData.getIrVarHoldingPeriod());
	    	existing.setIrFixedIncomeExposureAed(updatedData.getIrFixedIncomeExposureAed());
	    	existing.setIrVarExposureAed(updatedData.getIrVarExposureAed());
	    	existing.setIrVarLimitAed(updatedData.getIrVarLimitAed());
	    	existing.setIrStatus(updatedData.getIrStatus());
	    	existing.setIrVarImpactPct(updatedData.getIrVarImpactPct());

	    	existing.setCsVarConfidenceInterval(updatedData.getCsVarConfidenceInterval());
	    	existing.setCsVarHoldingPeriod(updatedData.getCsVarHoldingPeriod());
	    	existing.setCsFixedIncomeExposureAed(updatedData.getCsFixedIncomeExposureAed());
	    	existing.setCsVarExposureAed(updatedData.getCsVarExposureAed());
	    	existing.setCsVarLimitAed(updatedData.getCsVarLimitAed());
	    	existing.setCsStatus(updatedData.getCsStatus());
	    	existing.setCsVarImpactPct(updatedData.getCsVarImpactPct());

	    	existing.setFxVarConfidenceInterval(updatedData.getFxVarConfidenceInterval());
	    	existing.setFxVarHoldingPeriod(updatedData.getFxVarHoldingPeriod());
	    	existing.setFxExposureAed(updatedData.getFxExposureAed());
	    	existing.setFxVarExposureAed(updatedData.getFxVarExposureAed());
	    	existing.setFxVarLimitAed(updatedData.getFxVarLimitAed());
	    	existing.setFxStatus(updatedData.getFxStatus());
	    	existing.setFxVarImpactPct(updatedData.getFxVarImpactPct());

	    	existing.setEqVarConfidenceInterval(updatedData.getEqVarConfidenceInterval());
	    	existing.setEqVarHoldingPeriod(updatedData.getEqVarHoldingPeriod());
	    	existing.setEqExposureAed(updatedData.getEqExposureAed());
	    	existing.setEqVarExposureAed(updatedData.getEqVarExposureAed());
	    	existing.setEqVarLimitAed(updatedData.getEqVarLimitAed());
	    	existing.setEqStatus(updatedData.getEqStatus());
	    	existing.setEqVarImpactPct(updatedData.getEqVarImpactPct());

	    	existing.setCommVarConfidenceInterval(updatedData.getCommVarConfidenceInterval());
	    	existing.setCommVarHoldingPeriod(updatedData.getCommVarHoldingPeriod());
	    	existing.setCommExposureAed(updatedData.getCommExposureAed());
	    	existing.setCommVarExposureAed(updatedData.getCommVarExposureAed());
	    	existing.setCommVarLimitAed(updatedData.getCommVarLimitAed());
	    	existing.setCommStatus(updatedData.getCommStatus());
	    	existing.setCommVarImpactPct(updatedData.getCommVarImpactPct());

	    	existing.setOverallVarConfidenceInterval(updatedData.getOverallVarConfidenceInterval());
	    	existing.setOverallVarHoldingPeriod(updatedData.getOverallVarHoldingPeriod());
	    	existing.setVarExposureAed(updatedData.getVarExposureAed());
	    	existing.setVarLimitAed(updatedData.getVarLimitAed());
	    	existing.setOverallVarStatus(updatedData.getOverallVarStatus());
	    	existing.setPortfolioRelativeImpactPct(updatedData.getPortfolioRelativeImpactPct());

	    	existing.setExpectedShortfallExposureAed(updatedData.getExpectedShortfallExposureAed());
	    	existing.setExpectedShortfallLimitAed(updatedData.getExpectedShortfallLimitAed());
	    	existing.setExpectedShortfallStatus(updatedData.getExpectedShortfallStatus());
	    	existing.setExpectedShortfallImpactPct(updatedData.getExpectedShortfallImpactPct());

	    	existing.setModifiedDuration(updatedData.getModifiedDuration());
	    	existing.setInterestRateShockBps(updatedData.getInterestRateShockBps());
	    	existing.setInterestRateLimit(updatedData.getInterestRateLimit());
	    	existing.setInterestRateStatus(updatedData.getInterestRateStatus());

	    	existing.setDv01Aed(updatedData.getDv01Aed());
	    	existing.setDv01LimitAed(updatedData.getDv01LimitAed());
	    	existing.setDv01Status(updatedData.getDv01Status());
	    	existing.setDv01TenorGapLt3Aed(updatedData.getDv01TenorGapLt3Aed());
	    	existing.setDv01TenorGapGt3Aed(updatedData.getDv01TenorGapGt3Aed());
	    	existing.setDv01Check(updatedData.getDv01Check());

	    	existing.setCs01Aed(updatedData.getCs01Aed());
	    	existing.setCs01ShockBps(updatedData.getCs01ShockBps());
	    	existing.setCs01Limit(updatedData.getCs01Limit());
	    	existing.setCs01Status(updatedData.getCs01Status());
	    	existing.setCs01InvestmentGradeAed(updatedData.getCs01InvestmentGradeAed());
	    	existing.setCs01SubInvestmentAed(updatedData.getCs01SubInvestmentAed());
	    	existing.setCs01UnratedAed(updatedData.getCs01UnratedAed());
	    	existing.setCs01Check(updatedData.getCs01Check());

	    	existing.setBetaEquity(updatedData.getBetaEquity());
	    	existing.setEquityShockBps(updatedData.getEquityShockBps());
	    	existing.setEquityLimit(updatedData.getEquityLimit());
	    	existing.setEquityStatus(updatedData.getEquityStatus());

	    	existing.setCreditSpreadImpactAed(updatedData.getCreditSpreadImpactAed());
	    	existing.setCreditSensitiveImpactPct(updatedData.getCreditSensitiveImpactPct());
	    	existing.setInterestRateImpactAed(updatedData.getInterestRateImpactAed());
	    	existing.setRateSensitiveImpactPct(updatedData.getRateSensitiveImpactPct());

	    	existing.setFxImpactAed(updatedData.getFxImpactAed());
	    	existing.setFxSensitiveImpactPct(updatedData.getFxSensitiveImpactPct());
	    	existing.setEquityImpactAed(updatedData.getEquityImpactAed());
	    	existing.setEquitySensitiveImpactPct(updatedData.getEquitySensitiveImpactPct());

	    	existing.setCommoditiesImpactAed(updatedData.getCommoditiesImpactAed());
	    	existing.setCommoditySensitiveImpactPct(updatedData.getCommoditySensitiveImpactPct());

	    	existing.setJtdLossImpactAed(updatedData.getJtdLossImpactAed());
	    	existing.setJtdRelativeImpactPct(updatedData.getJtdRelativeImpactPct());

	    	existing.setOverallImpactAed(updatedData.getOverallImpactAed());
	    	existing.setCoreTier1RelativeImpactPct(updatedData.getCoreTier1RelativeImpactPct());
	    	// ---------------------------------------------------------------------------


			List<String> ignoreFields = Arrays.asList("createUser", "modifyUser", "delFlg");

			Map<String, String> changes = new LinkedHashMap<>();

			for (Field field : RT_Investment_Risk_Data_Dashboard_Template.class.getDeclaredFields()) {
				field.setAccessible(true);
				try {
					Object oldValue = field.get(dbUser);
					Object newValue = field.get(existing);
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
						field.set(dbUser, newValue);
					}

				} catch (IllegalAccessException e) {
					System.err.println("Access error for field: " + field.getName() + " - " + e.getMessage());
				}
			}
			

	    	RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS.save(existing);

	        System.out.println("changes : "+changes);

	        // Audit only if any field was changed
	        if (!changes.isEmpty()) {
	            auditservice.createBusinessAudit(
	            		String.valueOf(updatedData.getSI_NO()),           // Unique ID
	                "MODIFY",                             // Action
	                "INVESTMENTRISK_DATA_DICTIONARY_TEMPLATE_EDIT_SCREEN",                  // Screen name
	                changes,                              // Changed fields map
	                "BCBUAE_INVESTMENT_RISK_DATA_DASHBOARD_TEMPLATE"              // Table name
	            );
	        }
	        
	        return true;
	    } else {
	        System.out.println("No record found for SI_NO: " + updatedData.getSI_NO());
	        return false;
	    }
	}
    
    
    
    public byte[] generateInvestmentriskdataExcel(Date Report_date) throws Exception {
        logger.info("Service: Starting Investment Risk Data Dashboard Excel generation process in memory.");

        List<Object[]> investmentriskdata = RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS.getinvestmentriskdata1(Report_date);

        if (investmentriskdata.isEmpty()) {
            logger.warn("Service: No data found for Investment Risk Data Dashboard report. Returning empty result.");
            return new byte[0];
        }

        String templateDir = env.getProperty("output.exportpathtemp");
        String templateFileName = "CBUAE_Investment Risk Data_Dashboard_Template.xlsx";
        Path templatePath = Paths.get(templateDir, templateFileName);

        logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

        if (!Files.exists(templatePath)) {
            throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
        }

        if (!Files.isReadable(templatePath)) {
            throw new SecurityException("Template file exists but is not readable: " + templatePath.toAbsolutePath());
        }

        try (InputStream templateInputStream = Files.newInputStream(templatePath);
             Workbook workbook = WorkbookFactory.create(templateInputStream);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheet("Data");
            if (sheet == null) {
                sheet = workbook.getSheetAt(2);
            }
            CreationHelper createHelper = workbook.getCreationHelper();

            // Define cell styles
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

            int startRow = 2;

            for (int i = 0; i < investmentriskdata.size(); i++) {
                Object[] mm = investmentriskdata.get(i);
                Row row = sheet.getRow(startRow + i);
                if (row == null) row = sheet.createRow(startRow + i);

                writeInvestmentRiskInputCells(row, mm, dateStyle, textStyle, numberStyle);
            }
         // Auto-size input columns only; leave grey/formula columns at template width
			for (int i = 0; i <= 98; i++) {
			    if (!AUTOMATIC_FORMULA_COLUMNS.contains(i)) {
			        sheet.autoSizeColumn(i);
			    }
			}

            workbook.setForceFormulaRecalculation(true);
            try {
                workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
            } catch (Exception e) {
                logger.warn("Service: Formula evaluation could not complete; Excel will recalculate on open. {}", e.getMessage());
            }
            workbook.write(out);

            String finalPath = env.getProperty("output.exportpathfinal"); // e.g. finaltemp path
            File outputFile = new File(finalPath + "CBUAE_Investment Risk Data_Dashboard_Template.xlsx");
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(out.toByteArray());
                logger.info("Service: Excel also saved to file: {}", outputFile.getAbsolutePath());
            }

            logger.info("Service: INVESTMENT RISK DATA Excel data successfully written to memory buffer ({} bytes).", out.size());
            return out.toByteArray();
        }
    }

    /**
     * Write only user-input (non-grey) cells. Grey cells keep the template formulas:
     * Bank Symbol/Islamic/Local-Foreign/Tiering (VLOOKUP), book-size and impact %,
     * Status OK/BREACH, Check, and CS Fixed Income (= IR Fixed Income).
     */
    private void writeInvestmentRiskInputCells(Row row, Object[] mm, CellStyle dateStyle,
            CellStyle textStyle, CellStyle numberStyle) {
        setDateCell(row, 0, mm, dateStyle);
        setTextCell(row, 1, mm, textStyle);
        setTextCell(row, 2, mm, textStyle);
        setTextCell(row, 3, mm, textStyle);
        setNumberCell(row, 8, mm, numberStyle);
        setNumberCell(row, 9, mm, numberStyle);
        setNumberCell(row, 11, mm, numberStyle);
        setNumberCell(row, 12, mm, numberStyle);
        setNumberCell(row, 13, mm, numberStyle);
        setNumberCell(row, 15, mm, numberStyle);
        setNumberCell(row, 16, mm, numberStyle);
        setTextCell(row, 18, mm, textStyle);
        setTextCell(row, 19, mm, textStyle);
        setNumberCell(row, 20, mm, numberStyle);
        setNumberCell(row, 21, mm, numberStyle);
        setNumberCell(row, 22, mm, numberStyle);
        setTextCell(row, 25, mm, textStyle);
        setTextCell(row, 26, mm, textStyle);
        setNumberCell(row, 28, mm, numberStyle);
        setNumberCell(row, 29, mm, numberStyle);
        setTextCell(row, 32, mm, textStyle);
        setTextCell(row, 33, mm, textStyle);
        setNumberCell(row, 34, mm, numberStyle);
        setNumberCell(row, 35, mm, numberStyle);
        setNumberCell(row, 36, mm, numberStyle);
        setTextCell(row, 39, mm, textStyle);
        setTextCell(row, 40, mm, textStyle);
        setNumberCell(row, 41, mm, numberStyle);
        setNumberCell(row, 42, mm, numberStyle);
        setNumberCell(row, 43, mm, numberStyle);
        setTextCell(row, 46, mm, textStyle);
        setTextCell(row, 47, mm, textStyle);
        setNumberCell(row, 48, mm, numberStyle);
        setNumberCell(row, 49, mm, numberStyle);
        setNumberCell(row, 50, mm, numberStyle);
        setTextCell(row, 53, mm, textStyle);
        setTextCell(row, 54, mm, textStyle);
        setNumberCell(row, 55, mm, numberStyle);
        setNumberCell(row, 56, mm, numberStyle);
        setNumberCell(row, 59, mm, numberStyle);
        setNumberCell(row, 60, mm, numberStyle);
        setNumberCell(row, 63, mm, numberStyle);
        setNumberCell(row, 64, mm, numberStyle);
        setNumberCell(row, 65, mm, numberStyle);
        setNumberCell(row, 67, mm, numberStyle);
        setNumberCell(row, 68, mm, numberStyle);
        setNumberCell(row, 70, mm, numberStyle);
        setNumberCell(row, 71, mm, numberStyle);
        setNumberCell(row, 73, mm, numberStyle);
        setNumberCell(row, 74, mm, numberStyle);
        setNumberCell(row, 75, mm, numberStyle);
        setNumberCell(row, 77, mm, numberStyle);
        setNumberCell(row, 78, mm, numberStyle);
        setNumberCell(row, 79, mm, numberStyle);
        setNumberCell(row, 81, mm, numberStyle);
        setNumberCell(row, 82, mm, numberStyle);
        setNumberCell(row, 83, mm, numberStyle);
        setNumberCell(row, 85, mm, numberStyle);
        setNumberCell(row, 87, mm, numberStyle);
        setNumberCell(row, 89, mm, numberStyle);
        setNumberCell(row, 91, mm, numberStyle);
        setNumberCell(row, 93, mm, numberStyle);
        setNumberCell(row, 95, mm, numberStyle);
        setNumberCell(row, 97, mm, numberStyle);
    }

    private boolean skipAutomaticCell(Row row, int col) {
        if (AUTOMATIC_FORMULA_COLUMNS.contains(col)) {
            return true;
        }
        Cell existing = row.getCell(col);
        return existing != null && existing.getCellTypeEnum() == CellType.FORMULA;
    }

    private Cell writableCell(Row row, int col) {
        if (skipAutomaticCell(row, col)) {
            return null;
        }
        Cell cell = row.getCell(col);
        if (cell == null) {
            cell = row.createCell(col);
        }
        return cell;
    }

    private void setDateCell(Row row, int col, Object[] mm, CellStyle dateStyle) {
        Cell cell = writableCell(row, col);
        if (cell == null) {
            return;
        }
        cell.setCellStyle(dateStyle);
        cell.setCellValue(valueAt(mm, col) instanceof Date ? (Date) valueAt(mm, col) : null);
    }

    private void setTextCell(Row row, int col, Object[] mm, CellStyle textStyle) {
        Cell cell = writableCell(row, col);
        if (cell == null) {
            return;
        }
        cell.setCellStyle(textStyle);
        Object value = valueAt(mm, col);
        cell.setCellValue(value == null ? "" : value.toString());
    }

    private void setNumberCell(Row row, int col, Object[] mm, CellStyle numberStyle) {
        Cell cell = writableCell(row, col);
        if (cell == null) {
            return;
        }
        cell.setCellStyle(numberStyle);
        Object value = valueAt(mm, col);
        cell.setCellValue(value instanceof Number ? ((Number) value).doubleValue() : 0);
    }

    private Object valueAt(Object[] mm, int col) {
        if (mm == null || col < 0 || col >= mm.length) {
            return null;
        }
        return mm[col];
    }
}
