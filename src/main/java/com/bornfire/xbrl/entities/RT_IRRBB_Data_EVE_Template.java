package com.bornfire.xbrl.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="BCBUAE_IRRBB_DATA_TEMPLATE")
public class RT_IRRBB_Data_EVE_Template {
	
	@Id
	private String SI_NO;
	
	@Column(name = "REPORT_DATE")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	
	@Column(name = "BANK_NAME")
	private String bankName;
	
	@Column(name = "GROUP_HEAD_OFFICE_SUBSIDIARY")
	private String groupHeadOfficeSubsidiary;
	
	@Column(name = "BANK_SYMBOL")
	private String bankSymbol;
	
	@Column(name = "CONVENTIONAL_OR_ISLAMIC")
	private String conventionalOrIslamic;
	
	@Column(name = "LOCAL_OR_FOREIGN")
	private String localOrForeign;
	
	@Column(name = "CBUAE_TIERING")
	private String cbuaeTiering;
	
	@Column(name = "SUBSIDIARY")
	private String subsidiary;
	
	@Column(name = "SCENARIO")
	private String scenario;
	
	@Column(name = "GL_LEVEL_1")
	private String glLevel1;
	
	@Column(name = "GL_LEVEL_2")
	private String glLevel2;
	
	@Column(name = "GL_LEVEL_3")
	private String glLevel3;
	
	@Column(name = "OPTION_TYPE")
	private String optionType;
	
	@Column(name = "RATE_TYPE")
	private String rateType;
	
	@Column(name = "REFERENCE_RATE")
	private String referenceRate;
	
	@Column(name = "INSTRUMENT_CURRENCY")
	private String instrumentCurrency;
	
	@Column(name = "OUTSTANDING_BALANCE")
	private BigDecimal outstandingBalance;
	
	@Column(name = "OVERNIGHT")
	private BigDecimal overnight;
	
	@Column(name = "ON_TO_ONE_M")
	private BigDecimal onToOneM;
	
	@Column(name = "ONE_M_TO_THREE_M")
	private BigDecimal oneMToThreeM;
	
	@Column(name = "THREE_M_TO_SIX_M")
	private BigDecimal threeMToSixM;
	
	@Column(name = "SIX_M_TO_NINE_M")
	private BigDecimal sixMonthToNineM;
	
	@Column(name = "NINE_M_TO_ONE_Y")
	private BigDecimal nineMonthToOneY;
	
	@Column(name = "ONE_Y_TO_FIVE_Y")
	private BigDecimal oneYToFiveY;
	
	@Column(name = "ONE_POINT_FIVE_Y_TO_TWO_Y")
	private BigDecimal onePointFiveYToTwoY;
	
	@Column(name = "TWO_Y_TO_THREE_Y")
	private BigDecimal twoYToThreeY;
	
	@Column(name = "THREE_Y_TO_FOUR_Y")
	private BigDecimal threeYToFourY;
	
	@Column(name = "FOUR_Y_TO_FIVE_Y")
	private BigDecimal fourYToFiveY;
	
	@Column(name = "FIVE_Y_TO_SIX_Y")
	private BigDecimal fiveYToSixY;
	
	@Column(name = "SIX_Y_TO_SEVEN_Y")
	private BigDecimal sixYToSevenY;
	
	@Column(name = "SEVEN_Y_TO_EIGHT_Y")
	private BigDecimal sevenYToEightY;
	
	@Column(name = "EIGHT_Y_TO_NINE_Y")
	private BigDecimal eightYToNineY;
	
	@Column(name = "NINE_Y_TO_TEN_Y")
	private BigDecimal nineYToTenY;
	
	@Column(name = "TEN_Y_TO_FIFTEEN_Y")
	private BigDecimal tenYToFifteenY;
	
	@Column(name = "FIFTEEN_Y_TO_TWENTY_Y")
	private BigDecimal fifteenYToTwentyY;
	
	@Column(name = "TWENTY_YEAR_ABOVE")
	private BigDecimal twentyYearAbove;
	
	@Column(name = "NON_RATE_SENSITIVE")
	private BigDecimal nonRateSensitive;
	
	@Column(name = "REPORT_FROM_DATE")
	private Date reportFromDate;

	@Column(name = "REPORT_TO_DATE")
	private Date reportToDate;

	@Column(name = "ENTITY_FLG")
	private String entityFlg;

	@Column(name = "MODIFY_FLG")
	private String modifyFlg;

	@Column(name = "DEL_FLG")
	private String delFlg;

	@Column(name = "REPORT_CODE")
	private String reportCode;

	@Column(name = "REPORT_SUBMIT_DATE")
	private Date reportSubmitDate;

	@Column(name = "ENTRY_USER")
	private String entryUser;

	@Column(name = "MODIFY_USER")
	private String modifyUser;

	@Column(name = "VERIFY_USER")
	private String verifyUser;

	@Column(name = "ENTRY_TIME")
	private Date entryTime;

	@Column(name = "MODIFY_TIME")
	private Date modifyTime;

	@Column(name = "VERIFY_TIME")
	private Date verifyTime;

	public String getSI_NO() {
		return SI_NO;
	}

	public void setSI_NO(String sI_NO) {
		SI_NO = sI_NO;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getGroupHeadOfficeSubsidiary() {
		return groupHeadOfficeSubsidiary;
	}

	public void setGroupHeadOfficeSubsidiary(String groupHeadOfficeSubsidiary) {
		this.groupHeadOfficeSubsidiary = groupHeadOfficeSubsidiary;
	}

	public String getBankSymbol() {
		return bankSymbol;
	}

	public void setBankSymbol(String bankSymbol) {
		this.bankSymbol = bankSymbol;
	}

	public String getConventionalOrIslamic() {
		return conventionalOrIslamic;
	}

	public void setConventionalOrIslamic(String conventionalOrIslamic) {
		this.conventionalOrIslamic = conventionalOrIslamic;
	}

	public String getLocalOrForeign() {
		return localOrForeign;
	}

	public void setLocalOrForeign(String localOrForeign) {
		this.localOrForeign = localOrForeign;
	}

	public String getCbuaeTiering() {
		return cbuaeTiering;
	}

	public void setCbuaeTiering(String cbuaeTiering) {
		this.cbuaeTiering = cbuaeTiering;
	}

	public String getSubsidiary() {
		return subsidiary;
	}

	public void setSubsidiary(String subsidiary) {
		this.subsidiary = subsidiary;
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public String getGlLevel1() {
		return glLevel1;
	}

	public void setGlLevel1(String glLevel1) {
		this.glLevel1 = glLevel1;
	}

	public String getGlLevel2() {
		return glLevel2;
	}

	public void setGlLevel2(String glLevel2) {
		this.glLevel2 = glLevel2;
	}

	public String getGlLevel3() {
		return glLevel3;
	}

	public void setGlLevel3(String glLevel3) {
		this.glLevel3 = glLevel3;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public String getRateType() {
		return rateType;
	}

	public void setRateType(String rateType) {
		this.rateType = rateType;
	}

	public String getReferenceRate() {
		return referenceRate;
	}

	public void setReferenceRate(String referenceRate) {
		this.referenceRate = referenceRate;
	}

	public String getInstrumentCurrency() {
		return instrumentCurrency;
	}

	public void setInstrumentCurrency(String instrumentCurrency) {
		this.instrumentCurrency = instrumentCurrency;
	}

	public BigDecimal getOutstandingBalance() {
		return outstandingBalance;
	}

	public void setOutstandingBalance(BigDecimal outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}

	public BigDecimal getOvernight() {
		return overnight;
	}

	public void setOvernight(BigDecimal overnight) {
		this.overnight = overnight;
	}

	public BigDecimal getOnToOneM() {
		return onToOneM;
	}

	public void setOnToOneM(BigDecimal onToOneM) {
		this.onToOneM = onToOneM;
	}

	public BigDecimal getOneMToThreeM() {
		return oneMToThreeM;
	}

	public void setOneMToThreeM(BigDecimal oneMToThreeM) {
		this.oneMToThreeM = oneMToThreeM;
	}

	public BigDecimal getThreeMToSixM() {
		return threeMToSixM;
	}

	public void setThreeMToSixM(BigDecimal threeMToSixM) {
		this.threeMToSixM = threeMToSixM;
	}

	public BigDecimal getSixMonthToNineM() {
		return sixMonthToNineM;
	}

	public void setSixMonthToNineM(BigDecimal sixMonthToNineM) {
		this.sixMonthToNineM = sixMonthToNineM;
	}

	public BigDecimal getNineMonthToOneY() {
		return nineMonthToOneY;
	}

	public void setNineMonthToOneY(BigDecimal nineMonthToOneY) {
		this.nineMonthToOneY = nineMonthToOneY;
	}

	public BigDecimal getOneYToFiveY() {
		return oneYToFiveY;
	}

	public void setOneYToFiveY(BigDecimal oneYToFiveY) {
		this.oneYToFiveY = oneYToFiveY;
	}

	public BigDecimal getOnePointFiveYToTwoY() {
		return onePointFiveYToTwoY;
	}

	public void setOnePointFiveYToTwoY(BigDecimal onePointFiveYToTwoY) {
		this.onePointFiveYToTwoY = onePointFiveYToTwoY;
	}

	public BigDecimal getTwoYToThreeY() {
		return twoYToThreeY;
	}

	public void setTwoYToThreeY(BigDecimal twoYToThreeY) {
		this.twoYToThreeY = twoYToThreeY;
	}

	public BigDecimal getThreeYToFourY() {
		return threeYToFourY;
	}

	public void setThreeYToFourY(BigDecimal threeYToFourY) {
		this.threeYToFourY = threeYToFourY;
	}

	public BigDecimal getFourYToFiveY() {
		return fourYToFiveY;
	}

	public void setFourYToFiveY(BigDecimal fourYToFiveY) {
		this.fourYToFiveY = fourYToFiveY;
	}

	public BigDecimal getFiveYToSixY() {
		return fiveYToSixY;
	}

	public void setFiveYToSixY(BigDecimal fiveYToSixY) {
		this.fiveYToSixY = fiveYToSixY;
	}

	public BigDecimal getSixYToSevenY() {
		return sixYToSevenY;
	}

	public void setSixYToSevenY(BigDecimal sixYToSevenY) {
		this.sixYToSevenY = sixYToSevenY;
	}

	public BigDecimal getSevenYToEightY() {
		return sevenYToEightY;
	}

	public void setSevenYToEightY(BigDecimal sevenYToEightY) {
		this.sevenYToEightY = sevenYToEightY;
	}

	public BigDecimal getEightYToNineY() {
		return eightYToNineY;
	}

	public void setEightYToNineY(BigDecimal eightYToNineY) {
		this.eightYToNineY = eightYToNineY;
	}

	public BigDecimal getNineYToTenY() {
		return nineYToTenY;
	}

	public void setNineYToTenY(BigDecimal nineYToTenY) {
		this.nineYToTenY = nineYToTenY;
	}

	public BigDecimal getTenYToFifteenY() {
		return tenYToFifteenY;
	}

	public void setTenYToFifteenY(BigDecimal tenYToFifteenY) {
		this.tenYToFifteenY = tenYToFifteenY;
	}

	public BigDecimal getFifteenYToTwentyY() {
		return fifteenYToTwentyY;
	}

	public void setFifteenYToTwentyY(BigDecimal fifteenYToTwentyY) {
		this.fifteenYToTwentyY = fifteenYToTwentyY;
	}

	public BigDecimal getTwentyYearAbove() {
		return twentyYearAbove;
	}

	public void setTwentyYearAbove(BigDecimal twentyYearAbove) {
		this.twentyYearAbove = twentyYearAbove;
	}

	public BigDecimal getNonRateSensitive() {
		return nonRateSensitive;
	}

	public void setNonRateSensitive(BigDecimal nonRateSensitive) {
		this.nonRateSensitive = nonRateSensitive;
	}

	public Date getReportFromDate() {
		return reportFromDate;
	}

	public void setReportFromDate(Date reportFromDate) {
		this.reportFromDate = reportFromDate;
	}

	public Date getReportToDate() {
		return reportToDate;
	}

	public void setReportToDate(Date reportToDate) {
		this.reportToDate = reportToDate;
	}

	public String getEntityFlg() {
		return entityFlg;
	}

	public void setEntityFlg(String entityFlg) {
		this.entityFlg = entityFlg;
	}

	public String getModifyFlg() {
		return modifyFlg;
	}

	public void setModifyFlg(String modifyFlg) {
		this.modifyFlg = modifyFlg;
	}

	public String getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public Date getReportSubmitDate() {
		return reportSubmitDate;
	}

	public void setReportSubmitDate(Date reportSubmitDate) {
		this.reportSubmitDate = reportSubmitDate;
	}

	public String getEntryUser() {
		return entryUser;
	}

	public void setEntryUser(String entryUser) {
		this.entryUser = entryUser;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(String verifyUser) {
		this.verifyUser = verifyUser;
	}

	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public RT_IRRBB_Data_EVE_Template(String sI_NO, Date date, String bankName, String groupHeadOfficeSubsidiary,
			String bankSymbol, String conventionalOrIslamic, String localOrForeign, String cbuaeTiering,
			String subsidiary, String scenario, String glLevel1, String glLevel2, String glLevel3, String optionType,
			String rateType, String referenceRate, String instrumentCurrency, BigDecimal outstandingBalance,
			BigDecimal overnight, BigDecimal onToOneM, BigDecimal oneMToThreeM, BigDecimal threeMToSixM,
			BigDecimal sixMonthToNineM, BigDecimal nineMonthToOneY, BigDecimal oneYToFiveY,
			BigDecimal onePointFiveYToTwoY, BigDecimal twoYToThreeY, BigDecimal threeYToFourY, BigDecimal fourYToFiveY,
			BigDecimal fiveYToSixY, BigDecimal sixYToSevenY, BigDecimal sevenYToEightY, BigDecimal eightYToNineY,
			BigDecimal nineYToTenY, BigDecimal tenYToFifteenY, BigDecimal fifteenYToTwentyY, BigDecimal twentyYearAbove,
			BigDecimal nonRateSensitive, Date reportFromDate, Date reportToDate, String entityFlg, String modifyFlg,
			String delFlg, String reportCode, Date reportSubmitDate, String entryUser, String modifyUser,
			String verifyUser, Date entryTime, Date modifyTime, Date verifyTime) {
		super();
		SI_NO = sI_NO;
		this.date = date;
		this.bankName = bankName;
		this.groupHeadOfficeSubsidiary = groupHeadOfficeSubsidiary;
		this.bankSymbol = bankSymbol;
		this.conventionalOrIslamic = conventionalOrIslamic;
		this.localOrForeign = localOrForeign;
		this.cbuaeTiering = cbuaeTiering;
		this.subsidiary = subsidiary;
		this.scenario = scenario;
		this.glLevel1 = glLevel1;
		this.glLevel2 = glLevel2;
		this.glLevel3 = glLevel3;
		this.optionType = optionType;
		this.rateType = rateType;
		this.referenceRate = referenceRate;
		this.instrumentCurrency = instrumentCurrency;
		this.outstandingBalance = outstandingBalance;
		this.overnight = overnight;
		this.onToOneM = onToOneM;
		this.oneMToThreeM = oneMToThreeM;
		this.threeMToSixM = threeMToSixM;
		this.sixMonthToNineM = sixMonthToNineM;
		this.nineMonthToOneY = nineMonthToOneY;
		this.oneYToFiveY = oneYToFiveY;
		this.onePointFiveYToTwoY = onePointFiveYToTwoY;
		this.twoYToThreeY = twoYToThreeY;
		this.threeYToFourY = threeYToFourY;
		this.fourYToFiveY = fourYToFiveY;
		this.fiveYToSixY = fiveYToSixY;
		this.sixYToSevenY = sixYToSevenY;
		this.sevenYToEightY = sevenYToEightY;
		this.eightYToNineY = eightYToNineY;
		this.nineYToTenY = nineYToTenY;
		this.tenYToFifteenY = tenYToFifteenY;
		this.fifteenYToTwentyY = fifteenYToTwentyY;
		this.twentyYearAbove = twentyYearAbove;
		this.nonRateSensitive = nonRateSensitive;
		this.reportFromDate = reportFromDate;
		this.reportToDate = reportToDate;
		this.entityFlg = entityFlg;
		this.modifyFlg = modifyFlg;
		this.delFlg = delFlg;
		this.reportCode = reportCode;
		this.reportSubmitDate = reportSubmitDate;
		this.entryUser = entryUser;
		this.modifyUser = modifyUser;
		this.verifyUser = verifyUser;
		this.entryTime = entryTime;
		this.modifyTime = modifyTime;
		this.verifyTime = verifyTime;
	}

	public RT_IRRBB_Data_EVE_Template() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	

}
