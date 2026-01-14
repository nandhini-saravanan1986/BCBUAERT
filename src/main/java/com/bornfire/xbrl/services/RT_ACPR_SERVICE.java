package com.bornfire.xbrl.services;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.bornfire.xbrl.entities.*;

@Service
@Transactional
public class RT_ACPR_SERVICE {

	@Autowired
	private RT_ACPR_REPO rtAcprRepo;

	@Autowired
	private RT_ACPRNF_REPO rtAcprnfRepo;

	// 1. Method for ACPR (Fund Based - 55 Columns)
	public String uploadAcprFile(MultipartFile file, Date fromDate, Date toDate, String username) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		List<RT_ACPR_ENTITY> list = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			String line;
			int rowNum = 0;
			while ((line = br.readLine()) != null) {
				rowNum++;
				if (rowNum == 1 || line.startsWith("-") || line.trim().isEmpty()) continue;

				String[] data = line.split("\\|", -1);
				// Inside uploadAcprFile while loop:
				if (data.length >= 55) {
				    RT_ACPR_ENTITY entity = new RT_ACPR_ENTITY();
				    
				    // 0-10
				    entity.setReportDate(parseDate(data[0], sdf));
				    entity.setTerritoryName(data[1].trim());
				    entity.setSolId(data[2].trim());
				    entity.setCustId(data[3].trim());
				    entity.setAccountId(data[4].trim());
				    entity.setAccountName(data[5].trim());
				    entity.setOpeningDate(parseDate(data[6], sdf));
				    entity.setSanctDate(parseDate(data[7], sdf));
				    entity.setGlHead(data[8].trim());
				    entity.setScheme(data[9].trim());
				    entity.setConstCode(data[10].trim());
				    
				    // 11-20
				    entity.setDocuDate(parseDate(data[11], sdf));
				    entity.setLadDate(parseDate(data[12], sdf));
				    entity.setNextReview(parseDate(data[13], sdf));
				    entity.setLimitExpiry(parseDate(data[14], sdf));
				    entity.setAssetClass(data[15].trim());
				    entity.setNpaDate(parseDate(data[16], sdf));
				    entity.setFraud(data[17].trim());
				    entity.setRestrDate(parseDate(data[18], sdf));
				    entity.setIntRate(parseNum(data[19]));
				    entity.setCurrency(data[20].trim());
				    
				    // 21-30
				    entity.setSanctionLimit(parseNum(data[21]));
				    entity.setAdhocLimit(parseNum(data[22]));
				    entity.setBalance(parseNum(data[23]));
				    entity.setInterestSuspense(parseNum(data[24]));
				    entity.setDeduction(parseNum(data[25]));
				    entity.setStockDate(parseDate(data[26], sdf));
				    entity.setStockAmount(parseNum(data[27]));
				    entity.setBookDebtAmount(parseNum(data[28]));
				    entity.setFdAmount(parseNum(data[29]));
				    entity.setResidentialAmount(parseNum(data[30]));
				    
				    // 31-40
				    entity.setOthersSecurityAmount(parseNum(data[31]));
				    entity.setSecValDate(parseDate(data[32], sdf));
				    entity.setAllocSecAmount(parseNum(data[33]));
				    entity.setSecuredAmount(parseNum(data[34]));
				    entity.setHomeSecProv(parseNum(data[35]));
				    entity.setHomeUnsecProv(parseNum(data[36]));
				    entity.setHomeTotProv(parseNum(data[37]));
				    entity.setHostSecProv(parseNum(data[38]));
				    entity.setHostUnsecProv(parseNum(data[39]));
				    entity.setHostTotProv(parseNum(data[40]));
				    
				    // 41-50
				    entity.setStringentSecProv(parseNum(data[41]));
				    entity.setStringentUnsecProv(parseNum(data[42]));
				    entity.setStringentProv(parseNum(data[43]));
				    entity.setAdditionalProvision(parseNum(data[44]));
				    entity.setTotalProvision(parseNum(data[45]));
				    entity.setNetAmount(parseNum(data[46]));
				    entity.setSecured(parseNum(data[47]));
				    entity.setBankGovtGtee(parseNum(data[48]));
				    entity.setUnsecured(parseNum(data[49]));
				    entity.setSectorCode(data[50].trim());
				    
				    // 51-54
				    entity.setActivityCode(data[51].trim());
				    entity.setIndustryDetails(data[52].trim());
				    entity.setPanNumber(data[53].trim());
				    entity.setCustomerHealth(data[54].trim());
				    
				    // Audit Fields (Already correct in your code)
				    entity.setSrlNo("ACPR-" + timeStamp + "-" + rowNum);
				    entity.setReportFromDate(fromDate);
				    entity.setReportToDate(toDate);
				    entity.setCreateUser(username);
				    entity.setRcreUserId(username);
				    entity.setCreateTime(new Date());
				    entity.setRcreTime(new Date());
				    entity.setDelFlg("N");
				    entity.setEntityFlg("Y");
				    entity.setModifyFlg("N");
				    
				    list.add(entity);
				}
			}
			rtAcprRepo.saveAll(list);
			return "ACPR (Fund) processed successfully: " + list.size() + " records.";
		}
	}

	// 2. Method for ACPRNF (Non-Fund Based - 11 Columns)
	public String uploadAcprnfFile(MultipartFile file, Date fromDate, Date toDate, String username) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		List<RT_ACPRNF_ENTITY> list = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			String line;
			int rowNum = 0;
			while ((line = br.readLine()) != null) {
				rowNum++;
				if (rowNum == 1 || line.startsWith("-") || line.trim().isEmpty()) continue;

				String[] data = line.split("\\|", -1);
				if (data.length >= 11) {
					RT_ACPRNF_ENTITY entity = new RT_ACPRNF_ENTITY();
					entity.setReportDate(parseDate(data[0], sdf));
					entity.setSolId(data[1].trim());
					entity.setLcbgId(data[2].trim());
					entity.setCurrency(data[3].trim());
					entity.setIssueDate(parseDate(data[4], sdf));
					entity.setDueDate(parseDate(data[5], sdf));
					entity.setLgbcAmount(parseNum(data[6]));
					entity.setOutstandingAmount(parseNum(data[7]));
					entity.setAcceptedAmount(parseNum(data[8]));
					entity.setSecuredTangibleAsset(parseNum(data[9]));
					entity.setTotalProvision(parseNum(data[10]));

					entity.setSrlNo("ACPRNF-" + timeStamp + "-" + rowNum);
					entity.setReportFromDate(fromDate);
					entity.setReportToDate(toDate);
					entity.setCreateUser(username);
					entity.setRcreUserId(username);
					entity.setCreateTime(new Date());
					entity.setRcreTime(new Date());
					entity.setDelFlg("N");
					entity.setEntityFlg("Y");
					list.add(entity);
				}
			}
			rtAcprnfRepo.saveAll(list);
			return "ACPRNF (Non-Fund) processed successfully: " + list.size() + " records.";
		}
	}

	private Date parseDate(String s, SimpleDateFormat sdf) {
		try { return (s == null || s.trim().isEmpty()) ? null : sdf.parse(s.trim()); } catch (Exception e) { return null; }
	}

	private BigDecimal parseNum(String s) {
		try { return (s == null || s.trim().isEmpty()) ? BigDecimal.ZERO : new BigDecimal(s.trim().replace(",", "")); } catch (Exception e) { return BigDecimal.ZERO; }
	}
}