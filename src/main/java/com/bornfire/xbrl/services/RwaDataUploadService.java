package com.bornfire.xbrl.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.xbrl.entities.*;

@Service
public class RwaDataUploadService {

	private static final Logger logger = LoggerFactory.getLogger(RwaDataUploadService.class);

	@Autowired
	private RT_RWA_Fund_base_data_rep fundRepo;

	@Autowired
	private RT_RWA_NonFund_base_data_rep nonFundRepo;

	@Transactional
	public String uploadRwaTextFile(MultipartFile file, String reportType, Date toDate) throws Exception {

		try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			String line;
			br.readLine(); // Skip Header

			if ("RWAFUND".equals(reportType)) {
				return processFunded(br);
			} else if ("RWANONFUND".equals(reportType)) {
				return processNonFunded(br);
			} else {
				throw new Exception("Unsupported Report Type");
			}
		}
	}

	// ================= NON-FUNDED PROCESSING =================
	private String processNonFunded(BufferedReader br) throws Exception {
		Map<String, RT_RWA_NonFund_base_data_entity> nonFundMap = new LinkedHashMap<>();
		Set<Date> datesToDelete = new HashSet<>();
		String line;
		int rowCount = 1;

		while ((line = br.readLine()) != null) {
			rowCount++;
			if (line.trim().isEmpty())
				continue;

			// Detect Delimiter (Pipe or Tab)
			String delimiter = line.contains("|") ? "\\|" : "\t";
			String[] data = line.split(delimiter, -1);

			// Expecting 58 columns based on your Row 1 & 2 analysis (Index 0 to 57)
			if (data.length < 58) {
				logger.warn("Non-Fund Row {} skipped: Insufficient columns ({})", rowCount, data.length);
				continue;
			}

			try {
				RT_RWA_NonFund_base_data_entity ent = new RT_RWA_NonFund_base_data_entity();

				Date rowDate = parseDateSafe(data[0]);
				if (rowDate == null)
					continue;
				ent.setReport_date(rowDate);
				datesToDelete.add(rowDate);

				ent.setMoc(parseDecimal(data[1]));
				ent.setTerritory(data[2]);
				ent.setBranch_name(data[3]);
				ent.setCust_id(data[4]);
				ent.setCust_name(data[5]);
				ent.setType(data[6]);
				ent.setLcbg_id(data[7].trim()); // PRIMARY KEY
				ent.setGl_code(data[8]);
				ent.setGl_code_description(data[9]);
				ent.setConst_id(data[10]);
				ent.setConst_description(data[11]);
				ent.setPurpose(data[12]);
				ent.setPurpose_description(data[13]);
				ent.setNationality(data[14]);
				ent.setLegal_entity(data[15]);
				ent.setClass_val(data[16]);
				ent.setSub_class(data[17]);
				ent.setNpa_date(parseDateSafe(data[18]));
				ent.setTot_provision(parseDecimal(data[19]));
				ent.setLcbg_prov(parseDecimal(data[20]));
				ent.setProvision_percent(data[21]);
				ent.setCategory(data[22]);
				ent.setSub_category(data[23]);
				ent.setBs_turnover(parseDecimal(data[24]));
				ent.setNet_worth(parseDecimal(data[25]));
				ent.setLcbg_limit(parseDecimal(data[26]));
				ent.setSecurity_fd_primary(parseDecimal(data[27]));
				ent.setSecurity_fd_collat(parseDecimal(data[28]));
				ent.setCurrency(data[29]);
				ent.setLcbg_balance(parseDecimal(data[30]));
				ent.setLcbg_exposure(parseDecimal(data[31]));
				ent.setCcf(parseDecimal(data[32]));
				ent.setLcbg_ccf_adj_amt(parseDecimal(data[33]));
				ent.setLcbg_crm(parseDecimal(data[34]));
				ent.setCrm(parseDecimal(data[35]));
				ent.setCrm_adj_bal(parseDecimal(data[36]));
				ent.setAcceptance_amount(parseDecimal(data[37]));
				ent.setRisk_pcnt(parseDecimal(data[38]));
				ent.setRwa(parseDecimal(data[39]));
				ent.setAc_rwp(parseDecimal(data[40]));

				ent.setRating(data[41]); // Taking first rating
				ent.setUndrwn_balance(parseDecimal(data[42]));
				ent.setUndrwn_ccf(parseDecimal(data[43]));
				ent.setUndrwn_ccf_balance(parseDecimal(data[44]));
				ent.setUndrwn_crm(parseDecimal(data[45]));
				ent.setUndrwn_rwa(parseDecimal(data[46]));
				ent.setBg_type(data[47]);
				ent.setRrp_exp(parseDecimal(data[48]));
				ent.setLc_nature(data[49]);

				// Index 50 is duplicate rating -> Skip
				ent.setRating_date(parseDateSafe(data[51]));
				ent.setRating_expiry(parseDateSafe(data[52]));

				// Index 53 is the "UNDEFINED" blank gap in your file -> Skip

				ent.setInd_report_order(parseDecimal(data[54]));
				ent.setInd_desc(data[55]);
				ent.setNo_of_employees(parseDecimal(data[56]));
				ent.setMaturity_date(parseDateSafe(data[57]));

				nonFundMap.put(ent.getLcbg_id(), ent);
			} catch (Exception e) {
				logger.error("Error at Non-Fund Row {}: {}", rowCount, e.getMessage());
			}
		}

		// REPLACE LOGIC
		for (Date d : datesToDelete) {
			nonFundRepo.deleteByReportDate(d);
		}
		nonFundRepo.flush();
		nonFundRepo.saveAll(nonFundMap.values());

		return "Non-Funded: Successfully uploaded " + nonFundMap.size() + " records.";
	}

	// ================= FUNDED PROCESSING =================
	private String processFunded(BufferedReader br) throws Exception {
		Map<String, RT_RWA_Fund_base_data_entity> fundMap = new LinkedHashMap<>();
		Set<Date> datesToDelete = new HashSet<>();
		String line;
		int rowCount = 1;

		while ((line = br.readLine()) != null) {
			rowCount++;
			if (line.trim().isEmpty())
				continue;

			String delimiter = line.contains("|") ? "\\|" : "\t";
			String[] data = line.split(delimiter, -1);

			if (data.length < 71)
				continue;

			try {
				RT_RWA_Fund_base_data_entity ent = new RT_RWA_Fund_base_data_entity();
				Date rowDate = parseDateSafe(data[0]);
				if (rowDate == null)
					continue;
				ent.setReport_date(rowDate);
				datesToDelete.add(rowDate);

				ent.setMoc(parseDecimal(data[1]));
				ent.setTerritory(data[2]);
				ent.setBranch_name(data[3]);
				ent.setCust_id(data[4]);
				ent.setAccount_id(data[5].trim());
				ent.setAccount_name(data[6]);
				ent.setGl_code(data[7]);
				ent.setGl_code_description(data[8]);
				ent.setConst_id(data[9]);
				ent.setConst_description(data[10]);
				ent.setPurpose(data[11]);
				ent.setPurpose_description(data[12]);
				ent.setScheme(data[13]);
				ent.setSchm_code_desc(data[14]);
				ent.setNationality(data[15]);
				ent.setLegal_entity(data[16]);
				ent.setOpen_date(parseDateSafe(data[17]));
				ent.setRwa_class(data[18]);
				ent.setSub_class(data[19]);
				ent.setNpa_date(parseDateSafe(data[20]));
				ent.setInt_suspense(parseDecimal(data[21]));
				ent.setTot_provision(parseDecimal(data[22]));
				ent.setProv_pcnt(parseDecimal(data[23]));
				ent.setCategory(data[24]);
				ent.setSub_category(data[25]);
				ent.setBs_turnover(parseDecimal(data[26]));
				ent.setNet_worth(parseDecimal(data[27]));
				ent.setReshdl_date(parseDateSafe(data[28]));
				ent.setRes_sec_value(parseDecimal(data[29]));
				ent.setLtv(data[30]);
				ent.setGov_guarantee(parseDecimal(data[31]));
				ent.setCurrency(data[32]);
				ent.setLimit(parseDecimal(data[33]));
				ent.setBalance(parseDecimal(data[34]));
				ent.setExposure(parseDecimal(data[35]));
				ent.setSecurity_fdr(parseDecimal(data[36]));
				ent.setFdr_currency(data[37]);
				ent.setAdjusted_fdr(parseDecimal(data[38]));
				ent.setCrm(parseDecimal(data[39]));
				ent.setCrm_adj_bal(parseDecimal(data[40]));
				ent.setCrm_gnt_adj_bal(parseDecimal(data[41]));
				ent.setRw(parseDecimal(data[42]));
				ent.setRwa(parseDecimal(data[43]));
				ent.setBill_amount(parseDecimal(data[44]));
				ent.setBill_disc_rwa(parseDecimal(data[45]));
				ent.setTotal_drawn_rwa(parseDecimal(data[46]));
				ent.setBill_disc_dtls(data[47]);
				ent.setConditional_cancel(data[48]);
				ent.setUndrwn_balance(parseDecimal(data[49]));
				ent.setDisb_amt1(parseDecimal(data[50]));
				ent.setDisb_amt2(parseDecimal(data[51]));
				ent.setLoan_disb_amt(parseDecimal(data[52]));
				ent.setUndrwn_ccf(parseDecimal(data[53]));
				ent.setUndrwn_crm(parseDecimal(data[54]));
				ent.setUndrwn_ccf_balance(parseDecimal(data[55]));
				ent.setUndrwn_rwa(parseDecimal(data[56]));
				ent.setUndrwn_disb(data[57]);
				ent.setAc_rwp(parseDecimal(data[58]));

				ent.setRating(data[59]);
				ent.setRating_date(parseDateSafe(data[61]));
				ent.setRating_expiry(parseDateSafe(data[62]));
				ent.setPan_number(data[63]);
				ent.setInd_report_order(parseDecimal(data[64]));
				ent.setInd_desc(data[65]);
				ent.setInt_rate(parseDecimal(data[66]));
				ent.setNo_of_employees(parseDecimal(data[67]));
				ent.setInt_tbl_code(data[68]);
				ent.setMaturity_date(parseDateSafe(data[69]));
				ent.setFrequency(data[70]);

				fundMap.put(ent.getAccount_id(), ent);
			} catch (Exception e) {
				logger.error("Error at Funded Row {}: {}", rowCount, e.getMessage());
			}
		}

		for (Date d : datesToDelete) {
			fundRepo.deleteByReportDate(d);
		}
		fundRepo.flush();
		fundRepo.saveAll(fundMap.values());

		return "Funded: Successfully uploaded " + fundMap.size() + " records.";
	}

	// ================= UTIL METHODS =================

	private BigDecimal parseDecimal(String val) {
		if (val == null || val.trim().isEmpty() || val.equalsIgnoreCase("NULL") || val.equals(".")
				|| val.equals("PJSC")) {
			return BigDecimal.ZERO;
		}
		try {
			return new BigDecimal(val.trim().replace(",", ""));
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

	private Date parseDateSafe(String val) {
		if (val == null || val.trim().isEmpty() || val.equalsIgnoreCase("NULL"))
			return null;
		String s = val.trim();
		try {
			// Handles both 30-11-25 and 30-11-2025
			if (s.length() <= 8) {
				return new SimpleDateFormat("dd-MM-yy").parse(s);
			}
			return new SimpleDateFormat("dd-MM-yyyy").parse(s);
		} catch (Exception e) {
			return null;
		}
	}
}