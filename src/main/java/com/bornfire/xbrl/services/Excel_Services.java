package com.bornfire.xbrl.services;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bornfire.xbrl.entities.ASL_Report_Entity;
import com.bornfire.xbrl.entities.ASL_Report_Rep;
import com.bornfire.xbrl.entities.BankLimit_Entity;
import com.bornfire.xbrl.entities.BankLimit_Rep;
import com.bornfire.xbrl.entities.Counterparty_Rep;
import com.bornfire.xbrl.entities.MIS_SETTLEMENT_ENTITY;
import com.bornfire.xbrl.entities.MIS_SETTLEMENT_ENTITY_REP;
import com.bornfire.xbrl.entities.MIS_TREASURY_LIMITS_ENTITY;
import com.bornfire.xbrl.entities.MIS_TREASURY_LIMITS_ENTITY_REP;
import com.bornfire.xbrl.entities.MIS_TREASURY_PLACEMENT_ENTITY;
import com.bornfire.xbrl.entities.TreasuryPlacementRep;

@Service
@Transactional
public class Excel_Services {
	private static final Logger logger = LoggerFactory.getLogger(Excel_Services.class);
	@Autowired
	MIS_SETTLEMENT_ENTITY_REP MIS_SETTLEMENT_ENTITY_REPs;
	@Autowired
	MIS_TREASURY_LIMITS_ENTITY_REP MIS_TREASURY_LIMITS_ENTITY_REPs;
	@Autowired
	TreasuryPlacementRep TreasuryPlacementReps;
	@Autowired
	ASL_Report_Rep ASL_Report_Reps;
	@Autowired
	BankLimit_Rep BankLimit_Reps;
    @Autowired
    Counterparty_Rep Counterparty_Reps;
    public byte[] generateExcel() {
        try {
            logger.info("Generating Excel file For Exposure Template ...");

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Total");

            CellStyle unlockedStyle = workbook.createCellStyle();
            unlockedStyle.setLocked(false);

            CellStyle lockedStyle = workbook.createCellStyle();
            lockedStyle.setLocked(true);

            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 9);
            headerFont.setBold(true);
            lockedStyle.setFont(headerFont);

            CellStyle lockedCenterStyle = workbook.createCellStyle();
            lockedCenterStyle.setLocked(true);
            lockedCenterStyle.setFont(headerFont);
            lockedCenterStyle.setAlignment(HorizontalAlignment.CENTER);

            // Header row
            XSSFRow headerRow = sheet.createRow(0);
            String[] headers = {
                "BRANCH NAME", "SRL. NO.", "NAME OF THE COUNTERPARTY BANK",
                "BUYERS CREDIT", "FOREIGN BILL DISCOUNTING", "LOCAL BILL DISCOUNTING",
                "GUARANTEE SBLC'S", "SYNDICATED LOANS AND INVESTMENT", "OTHERS", "REMARKS"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(lockedStyle);
                sheet.setColumnWidth(i, (i == 1) ? 2000 : 7500);
            }

            List<String> counterbanklist = Counterparty_Reps.getall();
            logger.info("Retrieved {} counterparties", counterbanklist.size());

            int serialNumber = 1;
            int rowIndex = 1;

            for (String counterparty : counterbanklist) {
                XSSFRow row = sheet.createRow(rowIndex++);
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = row.createCell(i);
                    if (i == 1) {
                        cell.setCellValue(serialNumber++);
                        cell.setCellStyle(lockedCenterStyle);
                    } else if (i == 2) {
                        cell.setCellValue(counterparty);
                        cell.setCellStyle(lockedStyle);
                    } else {
                        cell.setCellValue("");
                        cell.setCellStyle(unlockedStyle);
                    }
                }
            }

            for (int i = rowIndex; i <= 50; i++) {
                XSSFRow row = sheet.createRow(i);
                for (int j = 0; j < headers.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(lockedStyle);
                }
            }

            sheet.protectSheet("");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed successfully.");
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating Excel file", e);
            return null;
        }
    }
    
    
    public byte[] generateExcel_placement() {
        try {
            logger.info("Generating Excel file For Treasury Placement Template ...");

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Treasury Placements");

            // Styles
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 9);
            headerFont.setBold(true);

            CellStyle lockedStyle = workbook.createCellStyle(); // For headers
            lockedStyle.setLocked(true);
            lockedStyle.setFont(headerFont);
            lockedStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle unlockedStyle = workbook.createCellStyle(); // For data rows
            unlockedStyle.setLocked(false);

            // Header row (locked)
            String[] headers = {
                "Titre", "Device 1", "Nominal 1", "Date Operation", "Date Valeur", 
                "Date Echeance", "Portefeuille", "CounterParty"
            };

            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(lockedStyle);
                sheet.setColumnWidth(i, 6000); // Adjust width as needed
            }

            // Data rows (1000 rows editable)
            for (int i = 1; i <= 1000; i++) {
                XSSFRow row = sheet.createRow(i);
                for (int j = 0; j < headers.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(unlockedStyle); // Editable
                }
            }

            // Lock the sheet (so locked cells take effect)
            sheet.protectSheet("placement");

            // Write to byte stream
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed successfully for Treasury Placement.");
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating Excel file for Treasury Placement.", e);
            return null;
        }
    }


    public byte[] generateExcel_Settlement() {
        try {
            logger.info("Generating Excel file For Settlement Template ...");

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Settlement");

            // Styles
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 9);
            headerFont.setBold(true);

            CellStyle lockedStyle = workbook.createCellStyle(); // For headers
            lockedStyle.setLocked(true);
            lockedStyle.setFont(headerFont);
            lockedStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle unlockedStyle = workbook.createCellStyle(); // For data rows
            unlockedStyle.setLocked(false);

            // Header row (locked)
            String[] headers = {
                "Deal Id", "Amount 1", "Amount 2", "Rate / Price", "Security", 
                "Trade Date", "Value Date", "Mat. Date", "Cpty."
            };

            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(lockedStyle);
                sheet.setColumnWidth(i, 6000); // Adjust width as needed
            }

            // Data rows (1000 rows editable)
            for (int i = 1; i <= 1000; i++) {
                XSSFRow row = sheet.createRow(i);
                for (int j = 0; j < headers.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(unlockedStyle); // Editable
                }
            }

            // Lock the sheet (so locked cells take effect)
            sheet.protectSheet("Settlement");

            // Write to byte stream
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed successfully for Settlement.");
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating Excel file for Settlement.", e);
            return null;
        }
    }
    
    
    public byte[] generate_final_sheet(LocalDate reportDate) {
        try {
            logger.info("Generating Excel for date: {}", reportDate);

      
            logger.info("Generating Excel file for final sheet...");

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Final Sheet");

            // Styles
            CellStyle lockedStyle = workbook.createCellStyle();
            lockedStyle.setLocked(true);
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 9);
            headerFont.setBold(true);
            lockedStyle.setFont(headerFont);

            CellStyle lockedCenterStyle = workbook.createCellStyle();
            lockedCenterStyle.setLocked(true);
            lockedCenterStyle.setFont(headerFont);
            lockedCenterStyle.setAlignment(HorizontalAlignment.CENTER);

            // Headers
            String[] headers = {
                "SRL NO", "TERRITORY NAME", "BANK NAME", "COUNTRY OF ORIGIN", "RATING",
                "REG LIMIT", "ADHOC LIMIT", "TOTAL LIMIT", "TOTAL EXPOSURE", "CREDIT LIMIT",
                "CREDIT EXPOSURE", "SETTLEMENT LIMIT", "SETTLEMENT EXPOSURE", "MONEY MARKET LIMIT",
                "MONEY MARKET EXPOSURE", "TRADE FINANCE LIMITS", "TF LIMITS AVAILABLE",
                "TRADE FINANCE EXPOSURE", "FX EXPOSURE", "CHECK FOR TOTAL LIMIT",
                "EXPOSURE TO CAPITAL", "CREDIT LIMIT CHECK", "MONEY MARKET LIMIT CHECK",
                "SETTLEMENT LIMIT CHECK", "TF LIMIT CHECK", "LIMIT UTILIZATION",
                "REPORT DATE", "BRANCH CODE", "BRANCH NAME"
            };

            // Create header row
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(lockedStyle);
                sheet.setColumnWidth(i, 7500);
            }

            Date parsedDate = java.sql.Date.valueOf(reportDate);
            // Fetch data
            List<BankLimit_Entity> dataList = BankLimit_Reps.getalllists(parsedDate);
            logger.info("Retrieved {} records", dataList.size());

            int rowIndex = 1;
            int serialNo = 1;
            for (BankLimit_Entity b : dataList) {
                XSSFRow row = sheet.createRow(rowIndex++);
                int col = 0;

                row.createCell(col++).setCellValue(serialNo++);
                row.createCell(col++).setCellValue(b.getTerritory_name());
                row.createCell(col++).setCellValue(b.getBank_name());
                row.createCell(col++).setCellValue(b.getCountry_of_origin());
                row.createCell(col++).setCellValue(b.getRating());
                row.createCell(col++).setCellValue(valueOf(b.getReg_limit()));
                row.createCell(col++).setCellValue(valueOf(b.getAdhoc_limit()));
                row.createCell(col++).setCellValue(valueOf(b.getTotal_limit()));
                row.createCell(col++).setCellValue(valueOf(b.getTotal_exposure()));
                row.createCell(col++).setCellValue(valueOf(b.getCredit_limit()));
                row.createCell(col++).setCellValue(valueOf(b.getCredit_exposure()));
                row.createCell(col++).setCellValue(valueOf(b.getSettlement_limit()));
                row.createCell(col++).setCellValue(valueOf(b.getSettlement_exposure()));
                row.createCell(col++).setCellValue(valueOf(b.getMoney_market_limit()));
                row.createCell(col++).setCellValue(valueOf(b.getMoney_market_exposure()));
                row.createCell(col++).setCellValue(valueOf(b.getTrade_finance_limits()));
                row.createCell(col++).setCellValue(valueOf(b.getTf_limits_avlble()));
                row.createCell(col++).setCellValue(valueOf(b.getTrade_finance_exposure()));
                row.createCell(col++).setCellValue(valueOf(b.getFx_exposure()));
                row.createCell(col++).setCellValue(valueOf(b.getCheck_for_total_limit()));
                row.createCell(col++).setCellValue(valueOf(b.getExposure_to_capital()));
                row.createCell(col++).setCellValue(valueOf(b.getCredit_limit_check()));
                row.createCell(col++).setCellValue(valueOf(b.getMoney_market_limit_check()));
                row.createCell(col++).setCellValue(valueOf(b.getSettlement_limit_check()));
                row.createCell(col++).setCellValue(valueOf(b.getTf_limit_check()));
                row.createCell(col++).setCellValue(b.getLimit_utilization());
                row.createCell(col++).setCellValue(formatDate(b.getReport_date()));
                row.createCell(col++).setCellValue(b.getBranch_code());
                row.createCell(col++).setCellValue(b.getBranch_name());
            }

            sheet.protectSheet(""); // Optional protection

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed.");
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating Excel file", e);
            return null;
        }
    }

    // Helper for BigDecimal nulls
    private String valueOf(BigDecimal val) {
        return val != null ? val.toPlainString() : "";
    }

    // Helper for Date formatting
    private String formatDate(Date date) {
        return date != null ? new SimpleDateFormat("dd-MM-yyyy").format(date) : "";
    }
    

    public byte[] generate_Exposure(LocalDate reportDate, String ibranch) {
        try {
            logger.info("Generating Excel file for Exposure sheet...");

            logger.info("Report date and Branch is ..."+reportDate+ibranch);

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Exposure");

            // Styles
            CellStyle lockedStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 9);
            headerFont.setBold(true);
            lockedStyle.setFont(headerFont);

            // Headers
            String[] headers = {
                "SRL NO",
                "BRANCH CODE",
                "BRANCH NAME",
                "COUNTERPARTYBANK NAME",
                "BUYERS CREDIT",
                "FOREIGN BILL DISCOUNTING",
                "LOCAL BILL DISCOUNTING",
                "GUARANTEE SBLCS",
                "SYNDICATED LOANS INVESTMENT",
                "OTHERS",
                "REMARKS",
                "TOTAL",
                "COUNTERPARTY BANK",
                "UPLOADED BY",
                "REPORT DATE"
            };

            // Create header row
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(lockedStyle);
                sheet.setColumnWidth(i, 7000);
            }

            // Fetch data
            Date parsedDate = java.sql.Date.valueOf(reportDate);
            List<ASL_Report_Entity> dataList = ASL_Report_Reps.getListByDate(parsedDate, ibranch);
            logger.info("Retrieved {} records", dataList.size());

            int rowIndex = 1;
            for (ASL_Report_Entity b : dataList) {
                XSSFRow row = sheet.createRow(rowIndex++);
                int col = 0;

                row.createCell(col++).setCellValue(b.getSrlNo());
                row.createCell(col++).setCellValue(nullSafe(b.getiBranchCode()));
                row.createCell(col++).setCellValue(nullSafe(b.getiBranchName()));
                row.createCell(col++).setCellValue(nullSafe(b.getCounterpartyBankName()));

                row.createCell(col++).setCellValue(formatDecimal(b.getBuyersCredit()));
                row.createCell(col++).setCellValue(formatDecimal(b.getForeignBillDiscounting()));
                row.createCell(col++).setCellValue(formatDecimal(b.getLocalBillDiscounting()));
                row.createCell(col++).setCellValue(formatDecimal(b.getGuaranteeSblcs()));
                row.createCell(col++).setCellValue(formatDecimal(b.getSyndicatedLoansInvestment()));
                row.createCell(col++).setCellValue(formatDecimal(b.getOthers()));

                row.createCell(col++).setCellValue(nullSafe(b.getRemarks())); 
                row.createCell(col++).setCellValue(formatDecimal(b.getTotal()));
                row.createCell(col++).setCellValue(nullSafe(b.getCounterpartyBank()));
                row.createCell(col++).setCellValue(nullSafe(b.getUploadedBy()));
                row.createCell(col++).setCellValue(b.getReportDate() != null ? formatDate(b.getReportDate()) : "");
            }


            sheet.protectSheet(""); // Optional

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed for  Exposure.");
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating Excel file for Exposure", e);
            return null;
        }
    }

    public byte[] generate_Placement(LocalDate reportDate) {
        try {
            logger.info("Generating Excel file for Treasury Placement sheet...");
            logger.info("Report date is ..." + reportDate);

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Treasury Placement");

            // Styles
            CellStyle lockedStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 9);
            headerFont.setBold(true);
            lockedStyle.setFont(headerFont);

            String[] headers = {
                "NUM OPERATION", "ENTITE OPERATION", "POSTE", "TITRE", "DEVISE 1", "NOMINAL 1",
                "DATE OPERATION", "DATE VALEUR", "DATE ECHEANCE", "PREAVIS", "ENTITE",
                "PORTEFEUILLE", "CONTREPARTIE", "STATUT", "VALEUR TAUX 1", "TAUX 1", "PERIODICITE 1",
                "AFFAIRE", "DUREE INIT", "DUREE RESTANT", "TRI 1", "TRI 2", "TRI 3", "OP REFERENCE",
                "FILTRAGE", "SIGNE", "DATE FIN", "DATE DEBUT", "OP FINANCE", "REPORT DATE", "BRANCH CODE", "BRANCH NAME"
            };

            // Create header row
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(lockedStyle);
                sheet.setColumnWidth(i, 7000);
            }

            // Fetch data
            Date parsedDate = java.sql.Date.valueOf(reportDate);
            List<MIS_TREASURY_PLACEMENT_ENTITY> dataList = TreasuryPlacementReps.getAllListByDate(parsedDate);
            logger.info("Retrieved {} records", dataList.size());

            int rowIndex = 1;
            for (MIS_TREASURY_PLACEMENT_ENTITY b : dataList) {
                XSSFRow row = sheet.createRow(rowIndex++);
                int col = 0;

                row.createCell(col++).setCellValue(formatDecimalString(b.getNumOperation()));
                row.createCell(col++).setCellValue(formatDecimalString(b.getEntiteOperation()));
                row.createCell(col++).setCellValue(nullSafe(b.getPoste()));
                row.createCell(col++).setCellValue(nullSafe(b.getTitre()));
                row.createCell(col++).setCellValue(nullSafe(b.getDevise1()));
                row.createCell(col++).setCellValue(formatDecimal(b.getNominal1()));
                row.createCell(col++).setCellValue(formatDate(b.getDateOperation()));
                row.createCell(col++).setCellValue(formatDate(b.getDateValeur()));
                row.createCell(col++).setCellValue(formatDate(b.getDateEcheance()));
                row.createCell(col++).setCellValue(nullSafe(b.getPreavis()));
                row.createCell(col++).setCellValue(nullSafe(b.getEntite()));
                row.createCell(col++).setCellValue(nullSafe(b.getPortefeuille()));
                row.createCell(col++).setCellValue(nullSafe(b.getContrepartie()));
                row.createCell(col++).setCellValue(nullSafe(b.getStatut()));
                row.createCell(col++).setCellValue(formatDecimal(b.getValeurTaux1()));
                row.createCell(col++).setCellValue(nullSafe(b.getTaux1()));
                row.createCell(col++).setCellValue(nullSafe(b.getPeriodicite1()));
                row.createCell(col++).setCellValue(nullSafe(b.getAffaire()));

                row.createCell(col++).setCellValue(
                	    String.format("%.2f", b.getDureeInit() != null ? (double) b.getDureeInit() : 0.00)
                	);

                	row.createCell(col++).setCellValue(
                	    String.format("%.2f", b.getDureeRestant() != null ? (double) b.getDureeRestant() : 0.00)
                	);


                row.createCell(col++).setCellValue(nullSafe(b.getTri1()));
                row.createCell(col++).setCellValue(nullSafe(b.getTri2()));
                row.createCell(col++).setCellValue(nullSafe(b.getTri3()));
                row.createCell(col++).setCellValue(nullSafe(b.getOpReference()));
                row.createCell(col++).setCellValue(nullSafe(b.getFiltrage()));
                row.createCell(col++).setCellValue(formatDecimalString(b.getSigne()));
                row.createCell(col++).setCellValue(formatDate(b.getDateFin()));
                row.createCell(col++).setCellValue(formatDate(b.getDateDebut()));
                row.createCell(col++).setCellValue(formatDecimalString(b.getOpFinance()));
                  
				/*
				 * row.createCell(col++).setCellValue(nullSafe(b.getCreateUser()));
				 * row.createCell(col++).setCellValue(formatDateTime(b.getCreateTime()));
				 * row.createCell(col++).setCellValue(nullSafe(b.getModifyUser()));
				 * row.createCell(col++).setCellValue(formatDateTime(b.getModifyTime()));
				 * row.createCell(col++).setCellValue(nullSafe(b.getVerifyUser()));
				 * row.createCell(col++).setCellValue(formatDateTime(b.getVerifyTime()));
				 * row.createCell(col++).setCellValue(nullSafe(b.getEntityFlg()));
				 * row.createCell(col++).setCellValue(nullSafe(b.getModifyFlg()));
				 */
                
                row.createCell(col++).setCellValue(formatDate(b.getReportDate()));
                row.createCell(col++).setCellValue(nullSafe(b.getBranchCode()));
                row.createCell(col++).setCellValue(nullSafe(b.getBranchName()));
            }

            sheet.protectSheet(""); // Optional

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed for Treasury Placement.");
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating Excel file for Treasury Placement", e);
            return null;
        }
    }

    // Utilities

    private String nullSafe(String value) {
        return value != null ? value : "";
    }

    private String nullSafeDouble(Double value) {
        return new java.text.DecimalFormat("#0.00").format(value != null ? value : 0.0);
    }

    private int nullSafeInt(Integer value) {
        return value != null ? value : 0;
    }


    
    private String formatDecimal(Double value) {
        return String.format("%.2f", value != null ? value : 0.0);
    }
    
    private String formatDecimalString(String value) {
        try {
            double d = Double.parseDouble(value);
            return String.format("%.2f", d);
        } catch (Exception e) {
            return "0.00"; // if value is null, empty, or not a number
        }
    }


    private String formatDateTime(Date dateTime) {
        if (dateTime == null) return "";
        return new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(dateTime);
    }
    
    
    public byte[] treasuryLimit(LocalDate reportDate) {
        try {
            logger.info("Generating Excel file for Treasury Limit sheet...");
            logger.info("Report date is ..." + reportDate);

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Treasury Limit");

            // Styles
            CellStyle lockedStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 9);
            headerFont.setBold(true);
            lockedStyle.setFont(headerFont);

			String[] headers = { "BANK NAME", "COUNTERPARTY", "PORTEFEUILLE", "AMT AED", "AMT GBP", "AMT USD",
					"AMT EUR", "IN USD MN", "REPORT DATE", "BRANCH CODE", "BRANCH NAME" };


            // Create header row
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(lockedStyle);
                sheet.setColumnWidth(i, 7000);
            }

            // Fetch data
            Date parsedDate = java.sql.Date.valueOf(reportDate);
            List<MIS_TREASURY_LIMITS_ENTITY> dataList = MIS_TREASURY_LIMITS_ENTITY_REPs.getAllListByDate(parsedDate);
            logger.info("Retrieved {} records", dataList.size());

            int rowIndex = 1;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");

            for (MIS_TREASURY_LIMITS_ENTITY b : dataList) {
                XSSFRow row = sheet.createRow(rowIndex++);
                int col = 0;

                row.createCell(col++).setCellValue(b.getBankName() != null ? b.getBankName() : "");
                row.createCell(col++).setCellValue(b.getCounterparty() != null ? b.getCounterparty() : "");
                row.createCell(col++).setCellValue(b.getPortefeuille() != null ? b.getPortefeuille() : "");

                row.createCell(col++).setCellValue(b.getAmtAed() != null ? decimalFormat.format(b.getAmtAed()) : "0.00");
                row.createCell(col++).setCellValue(b.getAmtGbp() != null ? decimalFormat.format(b.getAmtGbp()) : "0.00");
                row.createCell(col++).setCellValue(b.getAmtUsd() != null ? decimalFormat.format(b.getAmtUsd()) : "0.00");
                row.createCell(col++).setCellValue(b.getAmtEur() != null ? decimalFormat.format(b.getAmtEur()) : "0.00");
                row.createCell(col++).setCellValue(b.getInUsdMn() != null ? decimalFormat.format(b.getInUsdMn()) : "0.00");

				/*
				 * row.createCell(col++).setCellValue(b.getCreateUser() != null ?
				 * b.getCreateUser() : ""); row.createCell(col++).setCellValue(b.getCreateTime()
				 * != null ? dateTimeFormat.format(b.getCreateTime()) : "");
				 * 
				 * row.createCell(col++).setCellValue(b.getModifyUser() != null ?
				 * b.getModifyUser() : ""); row.createCell(col++).setCellValue(b.getModifyTime()
				 * != null ? dateTimeFormat.format(b.getModifyTime()) : "");
				 * 
				 * row.createCell(col++).setCellValue(b.getVerifyUser() != null ?
				 * b.getVerifyUser() : ""); row.createCell(col++).setCellValue(b.getVerifyTime()
				 * != null ? dateTimeFormat.format(b.getVerifyTime()) : "");
				 * 
				 * row.createCell(col++).setCellValue(b.getEntityFlg() != null ?
				 * b.getEntityFlg() : ""); row.createCell(col++).setCellValue(b.getModifyFlg()
				 * != null ? b.getModifyFlg() : "");
				 */
                row.createCell(col++).setCellValue(b.getReportDate() != null ? dateFormat.format(b.getReportDate()) : "");

                row.createCell(col++).setCellValue(b.getBranchCode() != null ? b.getBranchCode() : "");
                row.createCell(col++).setCellValue(b.getBranchName() != null ? b.getBranchName() : "");
            }


            sheet.protectSheet(""); // Optional

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed for Treasury Limit.");
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating Excel file for Treasury Limit", e);
            return null;
        }
    }
    
    

    public byte[] SettlementLimit(LocalDate reportDate) {
        try {
            logger.info("Generating Excel file for Settlement Limit sheet...");
            logger.info("Report date is ..." + reportDate);

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Settlement Limit");

            // Styles
            CellStyle lockedStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 9);
            headerFont.setBold(true);
            lockedStyle.setFont(headerFont);

			String[] headers = { "DEAL ID", "AMOUNT 1", "AMOUNT 2", "RATE / PRICE", "SECURITY", "TRADE DATE",
					"VALUE DATE", "MATURITY DATE", "DAY", "COUNTERPARTY CODE", "COUNTERPARTY NAME", "AMOUNT",
					"RESIDUAL MATURITY", "CCF", "CCF AMOUNT", "REPORT DATE","BRANCH CODE", "BRANCH NAME" };


            // Create header row
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(lockedStyle);
                sheet.setColumnWidth(i, 7000);
            }

            // Fetch data
            Date parsedDate = java.sql.Date.valueOf(reportDate);
            List<MIS_SETTLEMENT_ENTITY> dataList = MIS_SETTLEMENT_ENTITY_REPs.getAllListByDate(parsedDate);
            logger.info("Retrieved {} records", dataList.size());

            int rowIndex = 1;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");

            for (MIS_SETTLEMENT_ENTITY s : dataList) {
                XSSFRow row = sheet.createRow(rowIndex++);
                int col = 0;

                row.createCell(col++).setCellValue(s.getDealId() != null ? s.getDealId() : "");
                row.createCell(col++).setCellValue(s.getAmount1() != null ? decimalFormat.format(s.getAmount1()) : "0.00");
                row.createCell(col++).setCellValue(s.getAmount2() != null ? decimalFormat.format(s.getAmount2()) : "0.00");
                row.createCell(col++).setCellValue(s.getRateOrPrice() != null ? decimalFormat.format(s.getRateOrPrice()) : "0.0000");
                row.createCell(col++).setCellValue(s.getSecurity() != null ? s.getSecurity() : "");
                row.createCell(col++).setCellValue(s.getTradeDate() != null ? dateFormat.format(s.getTradeDate()) : "");
                row.createCell(col++).setCellValue(s.getValueDate() != null ? dateFormat.format(s.getValueDate()) : "");
                row.createCell(col++).setCellValue(s.getMaturityDate() != null ? dateFormat.format(s.getMaturityDate()) : "");
                row.createCell(col++).setCellValue(s.getDay() != null ? s.getDay() : 0);
                row.createCell(col++).setCellValue(s.getCounterpartyCode() != null ? s.getCounterpartyCode() : "");
                row.createCell(col++).setCellValue(s.getCounterpartyName() != null ? s.getCounterpartyName() : "");
                row.createCell(col++).setCellValue(s.getAmount() != null ? decimalFormat.format(s.getAmount()) : "0.00");
                row.createCell(col++).setCellValue(s.getResidualMaturity() != null ? s.getResidualMaturity() : 0);
                row.createCell(col++).setCellValue(s.getCcf() != null ? s.getCcf() : "");
                row.createCell(col++).setCellValue(s.getCcfAmount() != null ? decimalFormat.format(s.getCcfAmount()) : "0.00");
				/*
				 * row.createCell(col++).setCellValue(s.getCreateUser() != null ?
				 * s.getCreateUser() : ""); row.createCell(col++).setCellValue(s.getCreateTime()
				 * != null ? dateTimeFormat.format(s.getCreateTime()) : "");
				 * row.createCell(col++).setCellValue(s.getModifyUser() != null ?
				 * s.getModifyUser() : ""); row.createCell(col++).setCellValue(s.getModifyTime()
				 * != null ? dateTimeFormat.format(s.getModifyTime()) : "");
				 * row.createCell(col++).setCellValue(s.getVerifyUser() != null ?
				 * s.getVerifyUser() : ""); row.createCell(col++).setCellValue(s.getVerifyTime()
				 * != null ? dateTimeFormat.format(s.getVerifyTime()) : "");
				 * 
				 * row.createCell(col++).setCellValue(s.getEntityFlg() != null ?
				 * s.getEntityFlg() : ""); row.createCell(col++).setCellValue(s.getModifyFlg()
				 * != null ? s.getModifyFlg() : "");
				 */
                row.createCell(col++).setCellValue(s.getReportDate() != null ? dateFormat.format(s.getReportDate()) : "");
                row.createCell(col++).setCellValue(s.getBranchCode() != null ? s.getBranchCode() : "");
                row.createCell(col++).setCellValue(s.getBranchName() != null ? s.getBranchName() : "");
            }


            sheet.protectSheet(""); // Optional

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed for Settlement Limit.");
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating Excel file for Settlement Limit", e);
            return null;
        }
    }

}
