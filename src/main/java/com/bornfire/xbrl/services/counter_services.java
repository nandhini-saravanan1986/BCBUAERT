package com.bornfire.xbrl.services;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bornfire.xbrl.entities.UserProfile;
import com.bornfire.xbrl.entities.UserProfileRep;
import com.bornfire.xbrl.entities.ASL_Report_Entity;
import com.bornfire.xbrl.entities.ASL_Report_Rep;
import com.bornfire.xbrl.entities.AccessAndRoles;
import com.bornfire.xbrl.entities.BRF39_ENTITYREP;
import com.bornfire.xbrl.entities.Counterparty_Entity;
import com.bornfire.xbrl.entities.Counterparty_Rep;
import com.bornfire.xbrl.entities.MIS_SETTLEMENT_ENTITY;
import com.bornfire.xbrl.entities.MIS_SETTLEMENT_ENTITY_REP;
import com.bornfire.xbrl.entities.MIS_TREASURY_LIMITS_ENTITY;
import com.bornfire.xbrl.entities.MIS_TREASURY_PLACEMENT_ENTITY;
import com.bornfire.xbrl.entities.TreasuryPlacementRep;
@Service
@Transactional
public class counter_services {
	private static final Logger logger = LoggerFactory.getLogger(counter_services.class);


@PersistenceContext
private EntityManager entityManager;

@Autowired
AuditService auditservice;
@Autowired
UserProfileRep UserProfileReps;
@Autowired
MIS_SETTLEMENT_ENTITY_REP MIS_SETTLEMENT_ENTITY_REPs;
@Autowired
Counterparty_Rep Counterparty_Reps;
@Autowired
private BRF39_ENTITYREP brf39_entityrep;
@Autowired
ASL_Report_Rep ASL_Report_Reps;
	@Autowired
	TreasuryPlacementRep TreasuryPlacementReps;
	@Autowired
	SessionFactory sessionFactory;
	public String addcunter(Counterparty_Entity as, String userid, String formmode) {
	    String msg = "";

	    try {
	        Session hs = sessionFactory.getCurrentSession();

	        if ("edit".equalsIgnoreCase(formmode)) {
	            Counterparty_Entity old = Counterparty_Reps.getBYID(as.getId());

	            if (old != null) {
	                old.setCounterPartyBankCode(as.getCounterPartyBankCode());
	                old.setCounterPartyBank(as.getCounterPartyBank());
	                old.setLocation(as.getLocation());
	                old.setExpiryDate(as.getExpiryDate());
	                old.setRating(as.getRating());
	                old.setRegularLimit(as.getRegularLimit());
	                old.setAdhocLimit(as.getAdhocLimit());
	                old.setCreditLimit(as.getCreditLimit());
	                old.setSettlementLimit(as.getSettlementLimit());
	                old.setMoneyMarketLimit(as.getMoneyMarketLimit());
	                old.setTradeFinanceLimit(as.getTradeFinanceLimit());
	                old.setAdhoc_Limit_exp_date(as.getAdhoc_Limit_exp_date());
	                old.setModifyUser(userid);
	                old.setEarmarkinglimit(as.getEarmarkinglimit());
	                old.setEarmarkingdate(as.getEarmarkingdate());
	               
	                Counterparty_Entity dbUser = Counterparty_Reps.getBYID(as.getId());
	    	        List<String> ignoreFields = Arrays.asList("createUser", "modifyUser", "delFlg");
	    	         
	    	        Map<String, String> changes = new LinkedHashMap<>();

	    	        for (Field field : Counterparty_Entity.class.getDeclaredFields()) {
	    	            field.setAccessible(true);
	    	            try {
	    	                Object oldValue = field.get(dbUser);
	    	                Object newValue = field.get(as);    
	    	                if ((oldValue == null || oldValue.toString().trim().isEmpty()) &&
	    	                    (newValue == null || newValue.toString().trim().isEmpty())) {
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
	    	        }               auditservice.createBusinessAudit(userid,"MODIFY","CounterParty Bank-Modify",changes,"MIS_COUNTER_PARTY_TABLE");

	                
	                
	                hs.update(old);
	                msg = "CounterParty Bank Updated Successfully";
	            } else {
	                msg = "Record not found for update";
	            }

	        } else {
	            BigDecimal id = (BigDecimal) hs.createNativeQuery("SELECT COUNTERPARTY_ID.NEXTVAL FROM DUAL").getSingleResult();
	            as.setId(id.longValue());
	            as.setCreateUser(userid);
	            as.setDelFlg("N");
	            as.setCounterpartyCreatedDate(new Date());
	            auditservice.createBusinessAudit(userid, "ADD", "Counterparty Bank-Add", null,"MIS_COUNTER_PARTY_TABLE");
	            hs.save(as);
	            msg = "CounterParty Bank Added Successfully";
	        }

	    } catch (Exception e) {
	        msg = "Kindly check the data / Please contact Administrator";
	        e.printStackTrace();
	    }

	    return msg;
	}


	
	public List<Map<String, Object>> getTreasury_placement() {
	    logger.info("Fetching Treasury Placement data");

	    Date currentDate = new Date(); // current system date
	    List<MIS_TREASURY_PLACEMENT_ENTITY> listss = new ArrayList<>();

	    try {

LocalDate today = LocalDate.now();
	        java.sql.Date sqlDate = java.sql.Date.valueOf(today);
	        listss = TreasuryPlacementReps.getAllListByDate(sqlDate);
	        logger.info("Retrieved {} treasury placement records for date: {}", listss.size(), sqlDate);
	    } catch (Exception e) {
	        logger.error("Error retrieving treasury placement records", e);
	        return Collections.emptyList(); // Return empty list in case of failure
	    }

	    DecimalFormat decimalFormat = new DecimalFormat("#.00"); // Always show 2 decimal places
	    List<Map<String, Object>> formattedList = new ArrayList<>();

	    for (MIS_TREASURY_PLACEMENT_ENTITY entity : listss) {
	        try {
	            Map<String, Object> map = new HashMap<>();
	            map.put("poste", entity.getPoste() != null ? entity.getPoste() : "");
	            map.put("reportDate", entity.getReportDate() != null ? entity.getReportDate() : "");
	            map.put("titre", entity.getTitre() != null ? entity.getTitre() : "");
	            map.put("dateOperation", entity.getDateOperation() != null ? entity.getDateOperation() : "");
	            map.put("dateValeur", entity.getDateValeur() != null ? entity.getDateValeur() : "");
	            map.put("dateEcheance", entity.getDateEcheance() != null ? entity.getDateEcheance() : "");
	            map.put("portefeuille", entity.getPortefeuille() != null ? entity.getPortefeuille() : "");
	            map.put("contrepartie", entity.getContrepartie() != null ? entity.getContrepartie() : "");
	            map.put("devise1", entity.getDevise1() != null ? entity.getDevise1() : "");
	            map.put("entiteOperation", entity.getEntiteOperation() != null ? entity.getEntiteOperation() : "");
	            map.put("nominal1", entity.getNominal1() != null ? decimalFormat.format(entity.getNominal1()) : "0.00");
	            map.put("valeurTaux1", entity.getValeurTaux1() != null ? decimalFormat.format(entity.getValeurTaux1()) : "0.00");

	            
	            map.put("num_operation", entity.getNumOperation() != null ? entity.getNumOperation() : "");
	            map.put("preavis", entity.getPreavis() != null ? entity.getPreavis() : "");
	            map.put("entite", entity.getEntite() != null ? entity.getEntite() : "");

	            
	            
	            
	            formattedList.add(map);
	        } catch (Exception ex) {
	            logger.warn("Error processing record: {}", entity, ex);
	        }
	    }

	    logger.info("Formatted treasury placement data: {} records ready for response", formattedList.size());
	    return formattedList;
	}


	public List<MIS_TREASURY_LIMITS_ENTITY> getTreasury_limit(String reportDate) {
	    logger.info("Fetching Treasury Limits for date: {}", reportDate);

	    List<MIS_TREASURY_LIMITS_ENTITY> deals = new ArrayList<>();

	    try {
	        // Convert String to java.util.Date
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        Date parsedDate = sdf.parse(reportDate);

	        deals = entityManager
	            .createQuery("SELECT d FROM MIS_TREASURY_LIMITS_ENTITY d WHERE d.reportDate = :reportDate", MIS_TREASURY_LIMITS_ENTITY.class)
	            .setParameter("reportDate", parsedDate)
	            .getResultList();

	        logger.info("Retrieved {} treasury limit records", deals.size());
	    } catch (Exception e) {
	        logger.error("Error while fetching Treasury Limits", e);
	    }

	    return deals;
	}

	public List<Map<String, Object>> getSwap() {
	    logger.info("Fetching swap settlement data...");

	    Date currentDate = new Date(); // current system date
	    List<Map<String, Object>> formattedList = new ArrayList<>();

	    try {

LocalDate today = LocalDate.now();
	        java.sql.Date sqlDate = java.sql.Date.valueOf(today);
	        List<MIS_SETTLEMENT_ENTITY> deals = MIS_SETTLEMENT_ENTITY_REPs.getAllListByDate(sqlDate);

	        logger.info("Number of settlement records fetched: {}", deals.size());

	        DecimalFormat decimalFormat = new DecimalFormat("#.00");

	        for (MIS_SETTLEMENT_ENTITY entity : deals) {
	            Map<String, Object> map = new HashMap<>();
	            map.put("counterpartyName", entity.getCounterpartyName() != null ? entity.getCounterpartyName() : "");
	            map.put("reportDate", entity.getReportDate() != null ? entity.getReportDate() : null);

	            
	            map.put("dealId", entity.getDealId() != null ? entity.getDealId() : null);
	            map.put("amount1", entity.getAmount1() != null ? decimalFormat.format(entity.getAmount1()) : "0.00");
	            map.put("amount2", entity.getAmount2() != null ? decimalFormat.format(entity.getAmount2()) : "0.00");
	            map.put("rateOrPrice", entity.getRateOrPrice() != null ? decimalFormat.format(entity.getRateOrPrice()) : "0.00");
	            map.put("security", entity.getSecurity() != null ? entity.getSecurity() : null);
	            map.put("tradeDate", entity.getTradeDate() != null ? entity.getTradeDate() : null);
	            map.put("valueDate", entity.getValueDate() != null ? entity.getValueDate() : null);
	            map.put("counterpartyCode", entity.getCounterpartyCode() != null ? entity.getCounterpartyCode() : null);

	            
	            map.put("amount", entity.getAmount() != null ? decimalFormat.format(entity.getAmount()) : "0.00");
	            map.put("residualMaturity", entity.getResidualMaturity() != null ? entity.getResidualMaturity() : "0.00");
	            map.put("maturityDate", entity.getMaturityDate() != null ? entity.getMaturityDate() : null);
	            map.put("ccf", entity.getCcf() != null ? entity.getCcf() : "0.00");

	            formattedList.add(map);
	        }

	        logger.info("Formatted {} settlement records", formattedList.size());
	    } catch (Exception e) {
	        logger.error("Error while fetching or formatting swap settlement data", e);
	    }

	    return formattedList;
	}



	public List<Map<String, Object>> getExploreData(Date reportDate, String selected_branch) {
	    logger.info("Starting to fetch explore data for reportDate: {} and branch: {}", reportDate, selected_branch);

	    List<Map<String, Object>> result = new ArrayList<>();
	    try {
	        List<ASL_Report_Entity> list = ASL_Report_Reps.getByReportDateBranch(reportDate, selected_branch);
	        logger.info("Number of records retrieved from repository: {}", list.size());

	         result = list.stream().map(item -> {
	            Map<String, Object> row = new HashMap<>();

	            // Format Report Date
	            LocalDate localDate = item.getReportDate().toInstant()
	                .atZone(ZoneId.systemDefault())
	                .toLocalDate();
	            row.put("formattedDate", localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	            row.put("counterpartyBankName", item.getCounterpartyBankName() != null ? item.getCounterpartyBankName() : "");
	            row.put("buyersCredit", item.getBuyersCredit() != null ? item.getBuyersCredit() : "");
	            row.put("foreignBillDiscounting", item.getForeignBillDiscounting() != null ? item.getForeignBillDiscounting() : "");
	            row.put("localBillDiscounting", item.getLocalBillDiscounting() != null ? item.getLocalBillDiscounting() : "");
	            row.put("guaranteeSblcs", item.getGuaranteeSblcs() != null ? item.getGuaranteeSblcs() : "");
	            row.put("syndicatedLoansInvestment", item.getSyndicatedLoansInvestment() != null ? item.getSyndicatedLoansInvestment() : "");
	            row.put("others", item.getOthers() != null ? item.getOthers() : "");
	            row.put("remarks", item.getRemarks() != null ? item.getRemarks() : "");
	            row.put("total", item.getTotal() != null ? item.getTotal() : "");
	            row.put("iBranchCode", item.getiBranchCode() != null ? item.getiBranchCode() : "");
	            row.put("iBranchName", item.getiBranchName() != null ? item.getiBranchName() : "");

	            return row;
	        }).collect(Collectors.toList());

	        logger.info("Successfully mapped {} records to response format", result.size());
	    } catch (Exception e) {
	        logger.error("Error occurred while fetching explore data for date: {} and branch: {}", reportDate, selected_branch, e);
	    }

	    return result;
	}

	public List<Map<String, Object>> getExploreDataBranch(String selected_branch) {

	    Logger logger = LoggerFactory.getLogger(this.getClass());
	    logger.info("Fetching Explore Data for branch: {}", selected_branch);

	    List<Map<String, Object>> result = new ArrayList<>();

	    try {
	        List<ASL_Report_Entity> list = ASL_Report_Reps.getByReportDateBranch(selected_branch);
	        logger.info("Retrieved {} records for branch: {}", list.size(), selected_branch);

	        result = list.stream().map(item -> {
	            Map<String, Object> row = new HashMap<>();
	            LocalDate localDate = item.getReportDate().toInstant()
	                .atZone(ZoneId.systemDefault()).toLocalDate();

	            row.put("formattedDate", localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	            row.put("counterpartyBankName", item.getCounterpartyBankName() != null ? item.getCounterpartyBankName() : "");
	            row.put("buyersCredit", item.getBuyersCredit() != null ? item.getBuyersCredit() : "");
	            row.put("foreignBillDiscounting", item.getForeignBillDiscounting() != null ? item.getForeignBillDiscounting() : "");
	            row.put("localBillDiscounting", item.getLocalBillDiscounting() != null ? item.getLocalBillDiscounting() : "");
	            row.put("guaranteeSblcs", item.getGuaranteeSblcs() != null ? item.getGuaranteeSblcs() : "");
	            row.put("syndicatedLoansInvestment", item.getSyndicatedLoansInvestment() != null ? item.getSyndicatedLoansInvestment() : "");
	            row.put("others", item.getOthers() != null ? item.getOthers() : "");
	            row.put("remarks", item.getRemarks() != null ? item.getRemarks() : "");
	            row.put("total", item.getTotal() != null ? item.getTotal() : "");
	            row.put("iBranchCode", item.getiBranchCode() != null ? item.getiBranchCode() : "");
	            row.put("iBranchName", item.getiBranchName() != null ? item.getiBranchName() : "");

	            return row;
	        }).collect(Collectors.toList());

	    } catch (Exception e) {
	        logger.error("Error fetching Explore Data for branch: {}", selected_branch, e);
	    }

	    return result;
	}
	
	public List<Map<String, Object>> loadOverallTop10BRF39Data() {
	    List<Map<String, Object>> responseData = new ArrayList<>();
	    
	    try {
	        logger.info("Entered loadOverallTop10BRF39Data method.");

	        List<Object[]> results = brf39_entityrep.top10Bank();
	        logger.info("Fetched top 10 bank records: {}", results.size());

	        for (Object[] row : results) {
	            Map<String, Object> record = new HashMap<>();
	            record.put("name_of_borrower", row[0]);
	            record.put("borrower_type", row[1]);
	            record.put("residency_status", row[2]);
	            record.put("country", row[3]);         
	            record.put("economic_status", row[4]); 
	            record.put("currency", row[5]);
	            record.put("funded_os", row[6]);
	            responseData.add(record);
	        }

	        logger.info("Prepared response data with {} records", responseData.size());
	        
	    } catch (Exception e) {
	        logger.error("Exception occurred in loadOverallTop10BRF39Data: {}", e.getMessage(), e);
	    }

	    return responseData;
	}

	public List<Map<String, Object>> getTre_limit_Datas(Date reportDate) {

	    Logger logger = LoggerFactory.getLogger(this.getClass());
	    logger.info("Fetching Treasury Limit Data for report date: {}", reportDate);

	    List<Map<String, Object>> result = new ArrayList<>();

	    try {
	        List<MIS_TREASURY_LIMITS_ENTITY> deals = entityManager
	            .createQuery("SELECT d FROM MIS_TREASURY_LIMITS_ENTITY d WHERE d.reportDate = :reportDate", MIS_TREASURY_LIMITS_ENTITY.class)
	            .setParameter("reportDate", reportDate)
	            .getResultList();

	        logger.info("Retrieved {} records for Treasury Limits", deals.size());

	        result = deals.stream().map(item -> {
	            Map<String, Object> row = new HashMap<>();
	            java.sql.Date sqlDate = (java.sql.Date) item.getReportDate();
	            LocalDate localDate = sqlDate.toLocalDate();

	            row.put("formattedDate", localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	            row.put("bankName", item.getBankName());
	            row.put("counterparty", item.getCounterparty());
	            row.put("amtAed", item.getAmtAed());
	            row.put("amtGbp", item.getAmtGbp());
	            row.put("amtUsd", item.getAmtUsd());
	            row.put("amtEur", item.getAmtEur());
	            row.put("inUsdMn", item.getInUsdMn());

	            return row;
	        }).collect(Collectors.toList());

	    } catch (Exception e) {
	        logger.error("Error while fetching Treasury Limit Data for report date: {}", reportDate, e);
	    }

	    return result;
	}

	
	public List<Map<String, Object>> getTre_place_Datas(Date reportDate) {

	    Logger logger = LoggerFactory.getLogger(this.getClass());
	    logger.info("Fetching Treasury Placement Data for report date: {}", reportDate);

	    List<MIS_TREASURY_PLACEMENT_ENTITY> deals = new ArrayList<>();
	    try {
	        deals = entityManager
	            .createQuery("SELECT d FROM MIS_TREASURY_PLACEMENT_ENTITY d WHERE d.reportDate = :reportDate", MIS_TREASURY_PLACEMENT_ENTITY.class)
	            .setParameter("reportDate", reportDate)
	            .getResultList();

	        logger.info("Found {} Treasury Placement records for report date {}", deals.size(), reportDate);

	    } catch (Exception e) {
	        logger.error("Error while fetching Treasury Placement Data for report date: {}", reportDate, e);
	    }

	    return deals.stream().map(item -> {
	        Map<String, Object> row = new HashMap<>();
	        java.sql.Date sqlDate = (java.sql.Date) item.getReportDate();
	        LocalDate localDate = sqlDate.toLocalDate();

	        row.put("formattedDate", localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	        row.put("num_operation", item.getNumOperation() != null ? item.getNumOperation() : "");
	        row.put("entiteOperation", item.getEntiteOperation());
	        row.put("poste", item.getPoste() != null ? item.getPoste() : "");
	        row.put("titre", item.getTitre() != null ? item.getTitre() : "");
	        row.put("dateOperation", item.getDateOperation() != null ? item.getDateOperation() : "");
	        row.put("dateValeur", item.getDateValeur() != null ? item.getDateValeur() : "");
	        row.put("dateEcheance", item.getDateEcheance() != null ? item.getDateEcheance() : "");
	        row.put("portefeuille", item.getPortefeuille() != null ? item.getPortefeuille() : "");
	        
	        row.put("contrepartie", item.getContrepartie());
	        row.put("devise1", item.getDevise1());
	        row.put("nominal1", item.getNominal1());
	        row.put("valeurTaux1", item.getValeurTaux1());
	        row.put("preavis", item.getPreavis() != null ? item.getPreavis() : "");
	        row.put("entite", item.getEntite() != null ? item.getEntite() : "");
	        
	        
	        return row;
	    }).collect(Collectors.toList());
	}
	
	
	public List<Map<String, Object>> getSwap_Datas(Date reportDate) {
	    DecimalFormat decimalFormat = new DecimalFormat("#.00"); // Always show 2 decimal places

	    Logger logger = LoggerFactory.getLogger(this.getClass());
	    logger.info("Fetching Swap Settlement Data for report date: {}", reportDate);

	    List<MIS_SETTLEMENT_ENTITY> deals = new ArrayList<>();

	    try {
	        deals = entityManager
	            .createQuery("SELECT d FROM MIS_SETTLEMENT_ENTITY d WHERE d.reportDate = :reportDate", MIS_SETTLEMENT_ENTITY.class)
	            .setParameter("reportDate", reportDate)
	            .getResultList();

	        logger.info("Found {} Swap Settlement records for report date {}", deals.size(), reportDate);

	    } catch (Exception e) {
	        logger.error("Error while fetching Swap Settlement data for report date: {}", reportDate, e);
	    }

	    return deals.stream().map(item -> {
	        Map<String, Object> row = new HashMap<>();
	        java.sql.Date sqlDate = (java.sql.Date) item.getReportDate();
	        LocalDate localDate = sqlDate.toLocalDate();

	        java.sql.Date sqlDate1 = (java.sql.Date) item.getMaturityDate();
	        LocalDate localDate1 = sqlDate1.toLocalDate();

	        row.put("formattedDate", localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

	        row.put("dealId", item.getDealId() != null ? item.getDealId() : null);
	        row.put("amount1", item.getAmount1() != null ? decimalFormat.format(item.getAmount1()) : "0.00");
	        row.put("amount2", item.getAmount2() != null ? decimalFormat.format(item.getAmount2()) : "0.00");
	        row.put("rateOrPrice", item.getRateOrPrice() != null ? decimalFormat.format(item.getRateOrPrice()) : "0.00");
	        row.put("security", item.getSecurity() != null ? item.getSecurity() : null);
	        row.put("tradeDate", item.getTradeDate() != null ? item.getTradeDate() : null);
	        row.put("valueDate", item.getValueDate() != null ? item.getValueDate() : null);
	        row.put("counterpartyCode", item.getCounterpartyCode() != null ? item.getCounterpartyCode() : null);

	        row.put("counterpartyName", item.getCounterpartyName());
	        row.put("amount", item.getAmount() != null ? decimalFormat.format(item.getAmount()) : "0.00");
	        row.put("residualMaturity", item.getResidualMaturity() != null ? decimalFormat.format(item.getResidualMaturity()) : "0.00");
	        row.put("maturityDate", localDate1.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	        row.put("ccf", item.getCcf());

	        return row;
	    }).collect(Collectors.toList());
	}
	public List<Map<String, Object>> getcounterpartylist() {
	    List<Map<String, Object>> responseData = new ArrayList<>();
	    
	    try {
	        logger.info("Entered getcounterpartylist method.");

	        List<Counterparty_Entity> results = Counterparty_Reps.getalllist();
	        logger.info("Fetched all records: {}", results.size());

	        for (Counterparty_Entity up : results) {
	            Map<String, Object> record = new HashMap<>();
	            record.put("counterPartyBank", up.getCounterPartyBank());
	            record.put("regularLimit", up.getRegularLimit());
	            record.put("adhocLimit", up.getAdhocLimit());
	            record.put("Adhoc_Limit_exp_date", up.getAdhoc_Limit_exp_date()); 
	            responseData.add(record);
	        }

	        logger.info("Prepared response data with {} records", responseData.size());
	        
	    } catch (Exception e) {
	        logger.error("Exception occurred in loadOverallTop10BRF39Data: {}", e.getMessage(), e);
	    }

	    return responseData;
	}
	public String getbranch(String code) {
	   System.out.println("code is"+code);
	    List<UserProfile> branches = UserProfileReps.getallbranches();
	    if (branches == null || branches.isEmpty()) {
	        return "";
	    }

	    for (UserProfile branch : branches) {
	        String branchCode = branch.getBranch_code();
	        String branchName = branch.getBranch_name();

	        if (branchCode != null && branchCode.equalsIgnoreCase(code)) {
	            return (branchName != null) ? branchName : "";
	        }
	    }

	    return ""; // or "Not Found"
	}


}
