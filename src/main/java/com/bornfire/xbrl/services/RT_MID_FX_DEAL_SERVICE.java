package com.bornfire.xbrl.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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

import com.bornfire.xbrl.entities.Forward_reveal_manual_rep;
import com.bornfire.xbrl.entities.Forward_reveal_manual_table;
import com.bornfire.xbrl.entities.MIS_TREASURY_PLACEMENT_ENTITY;
import com.bornfire.xbrl.entities.Mis_treasury_placement_repo;
import com.bornfire.xbrl.entities.RT_MID_FX_DEAL_DC;
import com.bornfire.xbrl.entities.RT_MID_FX_DEAL_REPO;
import com.bornfire.xbrl.entities.RT_Treasury_master_tb_entity;
import com.bornfire.xbrl.entities.RT_Treasury_master_tb_repo;
import com.bornfire.xbrl.entities.RT_Treasury_swd_data_entity;
import com.bornfire.xbrl.entities.RT_Treasury_swd_data_repo;

@Service
public class RT_MID_FX_DEAL_SERVICE {

	// Fixed syntax error here: added .class and closing parentheses
	private static final Logger logger = LoggerFactory.getLogger(RT_MID_FX_DEAL_SERVICE.class);

	    @Autowired
	    RT_MID_FX_DEAL_REPO repo;
	    @Autowired
		AuditService auditservice;
	    
	    @Autowired
	    Mis_treasury_placement_repo Placement_Repo;
	    
	    @Autowired
	    RT_Treasury_master_tb_repo Tb_Master_repo;	
	    
	    @Autowired
	    RT_Treasury_swd_data_repo Tr_Swd_Repo;
	    
	    @Autowired
	    Forward_reveal_manual_rep Forward_repo;

	    // Get already uploaded dates
	    public List<String> getUploadedDates() {
	        List<Date> dates = repo.findUploadedDates();
	        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	        return dates.stream()
	                .map(sdf::format)
	                .collect(Collectors.toList());
	    }

	    /** Distinct report dates for Treasury Placement upload (TR_PLC). */
	    public List<String> getTreasuryPlacementUploadedDates() {
	    	return formatReportDates(Placement_Repo.findDistinctReportDates());
	    }

	    /** Distinct report dates for Treasury TB upload (TR_TB). */
	    public List<String> getTreasuryTbUploadedDates() {
	    	return formatReportDates(Tb_Master_repo.findDistinctReportDates());
	    }

	    /** Distinct report dates for Treasury SWD upload (TR_SWD). */
	    public List<String> getTreasurySwdUploadedDates() {
	    	return formatReportDates(Tr_Swd_Repo.findDistinctReportDates());
	    }

	    /** Distinct report dates for Forward Reveal upload (FWD_RVL). */
	    public List<String> getForwardRevealUploadedDates() {
	    	return formatReportDates(Forward_repo.findUploadedDates());
	    }

	    private List<String> formatReportDates(List<Date> dates) {
	    	if (dates == null || dates.isEmpty()) {
	    		return new ArrayList<>();
	    	}
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	    	return dates.stream().filter(Objects::nonNull).map(sdf::format).collect(Collectors.toList());
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
	        
	        auditservice.createBusinessAudit(entity.getSrlNo(), "UPLOAD", " Regulatory_Data_Ingestion_MIS_FX_DEAL", null,
					"RT_MID_FX_DEAL");
	        
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

	
	
	@Transactional
	public String UploadTrplorTb(MultipartFile file, Date toDate, String username, String Report_type)
			throws Exception {
		String Response = "" ;
		Date today = new Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");

		
		int No_of_Records = 0;

		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			DataFormatter formatter = new DataFormatter();
			if (Report_type.equals("TR_PLC")) {
				
				boolean exists = repo.existsByReportDate(toDate);

				// If today upload again → replace
				if (exists) {
					Placement_Repo.Deletebydaterecord(toDate);
					logger.info("Existing placement data removed. New insert started for date: " + toDate);
				}

				
				for (Sheet sheet : workbook) {
					for (Row row : sheet) {

						// Skip header
						if (row.getRowNum() == 0)
							continue;

						MIS_TREASURY_PLACEMENT_ENTITY placement = new MIS_TREASURY_PLACEMENT_ENTITY();
						

						placement.setNum_operation(formatter.formatCellValue(row.getCell(0)));
						System.out.println("Operation number is : " + formatter.formatCellValue(row.getCell(0)));
						placement.setEntite_operation(formatter.formatCellValue(row.getCell(1)));
						placement.setPoste(formatter.formatCellValue(row.getCell(2)));
						placement.setTitre(formatter.formatCellValue(row.getCell(3)));
						placement.setDevise_1(formatter.formatCellValue(row.getCell(4)));
						placement.setNominal_1(new BigDecimal(formatter.formatCellValue(row.getCell(5))));
						System.out.println("Date of Operation is : " + formatter.formatCellValue(row.getCell(6)));
						// Date of operation
						placement.setDate_operation(java.sql.Date.valueOf(getLocalDateFromCell(row.getCell(6))));
						System.out.println("Date of valuer is : " + formatter.formatCellValue(row.getCell(7)));
						// Date of value
						placement.setDate_valeur(java.sql.Date.valueOf(getLocalDateFromCell(row.getCell(7))));
						System.out.println("Date of exchange is : " + formatter.formatCellValue(row.getCell(8)));
						// Date of exchange
						placement.setDate_echeance(java.sql.Date.valueOf(getLocalDateFromCell(row.getCell(8))));

						placement.setPreavis(formatter.formatCellValue(row.getCell(9)));
						placement.setEntite(formatter.formatCellValue(row.getCell(10)));
						placement.setPortefeuille(formatter.formatCellValue(row.getCell(11)));
						placement.setContrepartie(formatter.formatCellValue(row.getCell(12)));
						placement.setTitre(formatter.formatCellValue(row.getCell(13)));
						placement.setStatut(formatter.formatCellValue(row.getCell(14)));
						placement.setValeur_taux_1(new BigDecimal(formatter.formatCellValue(row.getCell(15))));
						placement.setTaux_1(formatter.formatCellValue(row.getCell(16)));
						placement.setPeriodicite_1(formatter.formatCellValue(row.getCell(17)));
						placement.setAffaire(formatter.formatCellValue(row.getCell(18)));
						placement.setDuree_init(new BigDecimal(formatter.formatCellValue(row.getCell(19))));
						placement.setDuree_restant(new BigDecimal(formatter.formatCellValue(row.getCell(20))));
						placement.setTri_1(formatter.formatCellValue(row.getCell(21)));
						placement.setTri_2(formatter.formatCellValue(row.getCell(22)));
						placement.setTri_3(formatter.formatCellValue(row.getCell(23)));
						placement.setOp_reference(formatter.formatCellValue(row.getCell(24)));
						placement.setFiltrage(formatter.formatCellValue(row.getCell(25)));
						placement.setSigne(formatter.formatCellValue(row.getCell(26)));

						// Other date columns
						placement.setDate_fin(java.sql.Date.valueOf(getLocalDateFromCell(row.getCell(27))));
						placement.setDate_debut(java.sql.Date.valueOf(getLocalDateFromCell(row.getCell(29))));

						placement.setStatut(formatter.formatCellValue(row.getCell(30)));
						placement.setOp_finance(formatter.formatCellValue(row.getCell(31)));
						placement.setReport_date(toDate);
						placement.setCreate_user(username);
						placement.setCreate_time(new Date());
						Placement_Repo.save(placement);
						No_of_Records++;
					}
				}
				Response = "Placement details processed successfully for Report Date: " + toDate + " Record Count: "
				+ No_of_Records;
			} else if (Report_type.equals("TR_TB")) {
				
				Tb_Master_repo.Deletebydate(toDate);
								
				for (Sheet sheet : workbook) {
					for (Row row : sheet) {
						if (row.getRowNum() == 0)
							continue;
						
						RT_Treasury_master_tb_entity RT_Treasury_master_tb_entity = new RT_Treasury_master_tb_entity();
						
						RT_Treasury_master_tb_entity.setEntity(formatter.formatCellValue(row.getCell(0)));
						RT_Treasury_master_tb_entity.setAcct_no(formatter.formatCellValue(row.getCell(1)));
						System.out.println("Account No is : "+formatter.formatCellValue(row.getCell(1)));
						RT_Treasury_master_tb_entity.setAmount_ac(new BigDecimal(formatter.formatCellValue(row.getCell(3))));
						RT_Treasury_master_tb_entity.setCurrency(formatter.formatCellValue(row.getCell(2)));
						RT_Treasury_master_tb_entity.setAmount_lc(new BigDecimal(formatter.formatCellValue(row.getCell(4))));
						RT_Treasury_master_tb_entity.setAcct_name(formatter.formatCellValue(row.getCell(5)));
						RT_Treasury_master_tb_entity.setDel_flg(formatter.formatCellValue(row.getCell(6)));
						RT_Treasury_master_tb_entity.setReport_date(toDate);
						RT_Treasury_master_tb_entity.setRcre_user_id(username);
						RT_Treasury_master_tb_entity.setRcre_time(new Date());
						No_of_Records++;
						Tb_Master_repo.save(RT_Treasury_master_tb_entity);
						
					}
					
					
				}
				
				Response = "Mater Tb details processed successfully for Report Date: " + toDate + " Record Count: "
				+ No_of_Records;
			} else if (Report_type.equals("TR_SWD")) {

			    Tr_Swd_Repo.Deletebyreportdate(toDate);

			    List<RT_Treasury_swd_data_entity> Swdlistdata = new ArrayList<>();

			    Sheet sheet = workbook.getSheetAt(0);
			    System.out.println("Sheet loaded: " + sheet.getSheetName());

			    int headerRowIndex = -1;

			    for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			        Row row = sheet.getRow(i);
			        if (row == null) continue;

			        String firstCell = formatter.formatCellValue(row.getCell(0));
			        if ("Category".equalsIgnoreCase(firstCell)) {
			            headerRowIndex = i;
			            break;
			        }
			    }

			    if (headerRowIndex == -1) {
			        throw new RuntimeException("Header row not found in Excel!");
			    }

			    Row headerRow = sheet.getRow(headerRowIndex);
			    Map<String, Integer> columnMap = new HashMap<>();

			    for (Cell cell : headerRow) {
			        if (cell != null) {
			            String header = formatter.formatCellValue(cell).trim();
			            if (!header.isEmpty()) {
			                columnMap.put(header, cell.getColumnIndex());
			            }
			        }
			    }

			    for (int i = headerRowIndex + 1; i <= sheet.getLastRowNum(); i++) {

			        Row row = sheet.getRow(i);
			        if (row == null) continue;

			        String category = formatter.formatCellValue(row.getCell(columnMap.get("Category")));

			        if (category == null || category.trim().isEmpty()
			                || !(category.trim().startsWith("BOND") || category.trim().startsWith("EQ"))) {
			            continue;
			        }

			        RT_Treasury_swd_data_entity Swddata = new RT_Treasury_swd_data_entity();

			        Swddata.setData_category(category);
			        Swddata.setGl_code(formatter.formatCellValue(row.getCell(columnMap.get("GL Code"))));
			        Swddata.setPortfolio(formatter.formatCellValue(row.getCell(columnMap.get("Portfolio"))));
			        Swddata.setInstrument(formatter.formatCellValue(row.getCell(columnMap.get("Instr."))));
			        Swddata.setIsin_number(formatter.formatCellValue(row.getCell(columnMap.get("ISIN Number"))));
			        Swddata.setSecurity_id(formatter.formatCellValue(row.getCell(columnMap.get("Security ID"))));
			        Swddata.setSecurity_description(formatter.formatCellValue(row.getCell(columnMap.get("Security Description"))));

			        LocalDate maturityDate = getDateFromCell(row.getCell(columnMap.get("Maturity Dt.")));
			        if (maturityDate != null) {
			            Swddata.setMaturity_date(java.sql.Date.valueOf(maturityDate));
			        }

			        Swddata.setIssuer_id(formatter.formatCellValue(row.getCell(columnMap.get("Issuer ID"))));
			        Swddata.setCpn_rate(formatter.formatCellValue(row.getCell(columnMap.get("Cpn. Rate"))));
			        Swddata.setCpn_freq(formatter.formatCellValue(row.getCell(columnMap.get("Cpn. Freq."))));
			        Swddata.setBasis(formatter.formatCellValue(row.getCell(columnMap.get("Basis"))));

			        if(row.getCell(columnMap.get("No. of Securities")) != null) {
			        	Swddata.setNo_of_securities(new BigDecimal(
				                formatter.formatCellValue(row.getCell(columnMap.get("No. of Securities"))).replace(",", "")));

			        }
			        
			        Swddata.setCurr(formatter.formatCellValue(row.getCell(columnMap.get("Curr."))));
			        
			        if(row.getCell(columnMap.get("FV Per Sec.")) != null) {
			        	Swddata.setFv_per_sec(new BigDecimal(
				                formatter.formatCellValue(row.getCell(columnMap.get("FV Per Sec."))).replace(",", "")));

			        }
			        
			        if(row.getCell(columnMap.get("Face Value")) != null) {
			        	Swddata.setFace_value(new BigDecimal(
				                formatter.formatCellValue(row.getCell(columnMap.get("Face Value"))).replace(",", "")));

			        }
			        if(row.getCell(columnMap.get("Book Value")) != null) {
			        	 Swddata.setBook_value(new BigDecimal(
					                formatter.formatCellValue(row.getCell(columnMap.get("Book Value"))).replace(",", "")));

			        }
			        
			        if(row.getCell(columnMap.get("Curr. Mkt. Rate")) != null) {
			        	Swddata.setCurr_mkt_rate(new BigDecimal(
				                formatter.formatCellValue(row.getCell(columnMap.get("Curr. Mkt. Rate"))).replace(",", "")));

			        }
			       
			        
			        LocalDate currRateDate = getDateFromCell(row.getCell(columnMap.get("Curr. Rate Dt.")));
			        if (currRateDate != null) {
			            Swddata.setCurr_rate_date(java.sql.Date.valueOf(currRateDate));
			        }

			        if(row.getCell(columnMap.get("Market Value")) != null) {
			        	Swddata.setMarket_value(new BigDecimal(
				                formatter.formatCellValue(row.getCell(columnMap.get("Market Value"))).replace(",", "")));

			        }
			        if(row.getCell(columnMap.get("App./ Dep. / Prov.")) != null) {
			        	Swddata.setApp_dep_prov(new BigDecimal(
				                formatter.formatCellValue(row.getCell(columnMap.get("App./ Dep. / Prov."))).replace(",", "")));
			        }
			        
			        if(row.getCell(columnMap.get("Accrued Interest")) != null) {
			        	Swddata.setAccrued_interest(new BigDecimal(
				                formatter.formatCellValue(row.getCell(columnMap.get("Accrued Interest"))).replace(",", "")));
			        }

			        if(row.getCell(columnMap.get("Asset Class")) != null) {
			        	Swddata.setAsset_class(new BigDecimal(
				                formatter.formatCellValue(row.getCell(columnMap.get("Asset Class"))).replace(",", "")));
			        }

			        

			        Swddata.setAsset_class_description(
			                formatter.formatCellValue(row.getCell(columnMap.get("Asset Class Description"))));

			        LocalDate npiDate = getDateFromCell(row.getCell(columnMap.get("Date Of NPI")));
			        if (npiDate != null) {
			            Swddata.setDate_of_npi(java.sql.Date.valueOf(npiDate));
			        }

			        if(row.getCell(columnMap.get("Provision Amt.")) != null) {
			        	Swddata.setProvision_amt(new BigDecimal(
				                formatter.formatCellValue(row.getCell(columnMap.get("Provision Amt."))).replace(",", "")));
			        }
			        

			        Swddata.setIssuer_group(formatter.formatCellValue(row.getCell(columnMap.get("Issuer Group"))));

			        LocalDate optStartDate = getDateFromCell(row.getCell(columnMap.get("Opt Start Date")));
			        if (optStartDate != null) {
			            Swddata.setOpt_start_date(java.sql.Date.valueOf(optStartDate));
			        }

			        LocalDate optEndDate = getDateFromCell(row.getCell(columnMap.get("Opt End Date")));
			        if (optEndDate != null) {
			            Swddata.setOpt_end_date(java.sql.Date.valueOf(optEndDate));
			        }

			        Swddata.setPan_no(formatter.formatCellValue(row.getCell(columnMap.get("Pan No"))));
			        Swddata.setOption_type(formatter.formatCellValue(row.getCell(columnMap.get("Option Type"))));
			        Swddata.setIssuer_country_id(formatter.formatCellValue(row.getCell(columnMap.get("Issuer Country ID"))));
			        Swddata.setIssuer_country_name(formatter.formatCellValue(row.getCell(columnMap.get("Issuer Country Name"))));
			        Swddata.setGroup_name(formatter.formatCellValue(row.getCell(columnMap.get("Group"))));
			        Swddata.setLevel_no(formatter.formatCellValue(row.getCell(columnMap.get("Level"))));

			        if(row.getCell(columnMap.get("Amort/Prem")) != null) {
			        	Swddata.setAmort_prem(new BigDecimal(
				                formatter.formatCellValue(row.getCell(columnMap.get("Amort/Prem"))).replace(",", "")));
			        }
			        
			        if(row.getCell(columnMap.get("MTM/Reserve")) != null) {
			        	Swddata.setMtm_reserve(new BigDecimal(
				                formatter.formatCellValue(row.getCell(columnMap.get("MTM/Reserve"))).replace(",", "")));
			        }

			        if(row.getCell(columnMap.get("Deal Value")) != null) {
			        	Swddata.setDeal_value(new BigDecimal(
				                formatter.formatCellValue(row.getCell(columnMap.get("Deal Value"))).replace(",", "")));
			        }
			        

			        Swddata.setSector_code(formatter.formatCellValue(row.getCell(columnMap.get("Sector Code"))));

			        Swddata.setReport_date(toDate);
			        Swddata.setRcre_user_id(username);
			        Swddata.setRcre_time(new Date());

			        Swdlistdata.add(Swddata);
			    }

			    Tr_Swd_Repo.saveAll(Swdlistdata);

			    Response = "SWD details processed successfully for Report Date: " + toDate;
			} else if (Report_type.equals("FWD_RVL")) {

				logger.info("Forward reval data upload started for report date: {}", toDate);

				Forward_repo.Deletebyreportdate(toDate);

				List<Forward_reveal_manual_table> forwardRows = new ArrayList<>();

				for (Sheet sheet : workbook) {
					for (Row row : sheet) {
						if (row.getRowNum() == 0) {
							continue;
						}

						Forward_reveal_manual_table fwddata = new Forward_reveal_manual_table();

						fwddata.setNum_operation(new BigDecimal(
								formatter.formatCellValue(row.getCell(0)).replace(",", "").trim()));
						fwddata.setGl_values(new BigDecimal(
								formatter.formatCellValue(row.getCell(1)).replace(",", "").trim()));
						fwddata.setOp_reference(formatter.formatCellValue(row.getCell(2)));
						fwddata.setPoste(formatter.formatCellValue(row.getCell(3)));

						LocalDate dealDate = getDateFromCell(row.getCell(4));
						LocalDate valueDate = getDateFromCell(row.getCell(5));
						LocalDate maturityDate = getDateFromCell(row.getCell(6));
						if (dealDate == null || valueDate == null || maturityDate == null) {
							throw new RuntimeException("Invalid or empty date in columns 5–7 at Excel row "
									+ (row.getRowNum() + 1));
						}
						fwddata.setDealdate(java.sql.Date.valueOf(dealDate));
						fwddata.setValuedate(java.sql.Date.valueOf(valueDate));
						fwddata.setMaturitydate(java.sql.Date.valueOf(maturityDate));

						fwddata.setCurrency1(formatter.formatCellValue(row.getCell(7)));
						fwddata.setCurrency2(formatter.formatCellValue(row.getCell(8)));
						fwddata.setNominal_1(new BigDecimal(
								formatter.formatCellValue(row.getCell(9)).replace(",", "").trim()));
						fwddata.setRamount(new BigDecimal(
								formatter.formatCellValue(row.getCell(10)).replace(",", "").trim()));
						fwddata.setNo_of_days(new BigDecimal(
								formatter.formatCellValue(row.getCell(11)).replace(",", "").trim()));
						fwddata.setAmount1(new BigDecimal(
								formatter.formatCellValue(row.getCell(12)).replace(",", "").trim()));
						fwddata.setDealrate(new BigDecimal(
								formatter.formatCellValue(row.getCell(13)).replace(",", "").trim()));
						fwddata.setAmount2(new BigDecimal(
								formatter.formatCellValue(row.getCell(14)).replace(",", "").trim()));
						fwddata.setReval_rate(new BigDecimal(
								formatter.formatCellValue(row.getCell(15)).replace(",", "").trim()));
						fwddata.setReval_amount(new BigDecimal(
								formatter.formatCellValue(row.getCell(16)).replace(",", "").trim()));
						fwddata.setProfitloss(new BigDecimal(
								formatter.formatCellValue(row.getCell(17)).replace(",", "").trim()));
						fwddata.setContrepartie(formatter.formatCellValue(row.getCell(18)));
						fwddata.setBranch_code(formatter.formatCellValue(row.getCell(19)));
						fwddata.setLcyrate(new BigDecimal(
								formatter.formatCellValue(row.getCell(20)).replace(",", "").trim()));
						fwddata.setLcyprofitloss(new BigDecimal(
								formatter.formatCellValue(row.getCell(21)).replace(",", "").trim()));

						fwddata.setReport_date(toDate);
						fwddata.setEntry_user(username);
						fwddata.setEntry_time(new Date());
						fwddata.setDel_flg("N");
						fwddata.setEntity_flg("N");
						fwddata.setModify_flg("N");

						forwardRows.add(fwddata);
						No_of_Records++;
					}
				}
				Forward_repo.saveAll(forwardRows);
				Response = "Forward reval details processed successfully for Report Date: " + toDate + " Record Count: "
						+ No_of_Records;
			}
		}catch (Exception e) {
			e.getLocalizedMessage();
			return e.getLocalizedMessage();
		}

		return Response;
	}

	
	
	private LocalDate getLocalDateFromCell(Cell cell) {
	    if (cell == null) return null;

	    if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
	        if (DateUtil.isCellDateFormatted(cell)) {
	            Date date = cell.getDateCellValue();
	            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	        }
	    }
	    return null;
	}
	
	
	private LocalDate getDateFromCell(Cell cell) {
		if (cell == null) {
			return null;
		}

		try {
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			}

			// Same text Excel shows (handles strings like "09/03/2026 00:00:00", formulas, etc.)
			DataFormatter dataFormatter = new DataFormatter();
			String dateStr = dataFormatter.formatCellValue(cell).trim();
			if (dateStr.isEmpty()) {
				return null;
			}

			DateTimeFormatter[] formatters = new DateTimeFormatter[] {
					DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
					DateTimeFormatter.ofPattern("dd/MM/yyyy"),
					DateTimeFormatter.ofPattern("dd-MM-yyyy"),
					DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH),
			};

			for (DateTimeFormatter f : formatters) {
				try {
					return LocalDate.parse(dateStr, f);
				} catch (DateTimeParseException ignored) {
					// try next pattern
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
}