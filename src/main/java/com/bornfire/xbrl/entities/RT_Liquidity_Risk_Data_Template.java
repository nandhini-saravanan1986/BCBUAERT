package com.bornfire.xbrl.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BCBUAE_LIQUIDITY_RISK_DATA_TEMPLATE") 
public class RT_Liquidity_Risk_Data_Template {
	

	    @Id
	    @Column(name = "SI_NO", length = 20)
	    private String siNo;

	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    @Column(name = "DATA_DATE")
	    private Date dataDate;

	    @Column(name = "BANK_NAME", length = 200)
	    private String bankName;

	    @Column(name = "HEAD_OFFICE_SUBSIDIARY", length = 100)
	    private String headOfficeSubsidiary;

	    @Column(name = "BANK_SYMBOL", length = 50)
	    private String bankSymbol;

	    @Column(name = "CONVENTIONAL_ISLAMIC", length = 50)
	    private String conventionalIslamic;

	    @Column(name = "LOCAL_FOREIGN", length = 50)
	    private String localForeign;

	    @Column(name = "CBUAE_TIERING", length = 50)
	    private String cbuaeTiering;

	    @Column(name = "INFLOWS", precision = 20, scale = 2)
	    private BigDecimal inflows;

	    @Column(name = "CURRENCY", length = 10)
	    private String currency;

	    @Column(name = "TOTAL_BALANCE", precision = 20, scale = 2)
	    private BigDecimal totalBalance;

	    @Column(name = "OVERNIGHT", precision = 20, scale = 2)
	    private BigDecimal overnight;

	    @Column(name = "ON_TO_1M", precision = 20, scale = 2)
	    private BigDecimal onTo1M;

	    @Column(name = "ONE_M_TO_3M", precision = 20, scale = 2)
	    private BigDecimal oneMTo3M;

	    @Column(name = "THREE_M_TO_6M", precision = 20, scale = 2)
	    private BigDecimal threeMTo6M;

	    @Column(name = "SIX_M_TO_9M", precision = 20, scale = 2)
	    private BigDecimal sixMTo9M;

	    @Column(name = "NINE_M_TO_1Y", precision = 20, scale = 2)
	    private BigDecimal nineMTo1Y;

	    @Column(name = "ONE_Y_TO_1_5Y", precision = 20, scale = 2)
	    private BigDecimal oneYTo1_5Y;

	    @Column(name = "ONE_5Y_TO_2Y", precision = 20, scale = 2)
	    private BigDecimal one5YTo2Y;

	    @Column(name = "TWO_Y_TO_3Y", precision = 20, scale = 2)
	    private BigDecimal twoYTo3Y;

	    @Column(name = "THREE_Y_TO_4Y", precision = 20, scale = 2)
	    private BigDecimal threeYTo4Y;

	    @Column(name = "FOUR_Y_TO_5Y", precision = 20, scale = 2)
	    private BigDecimal fourYTo5Y;

	    @Column(name = "FIVE_Y_TO_6Y", precision = 20, scale = 2)
	    private BigDecimal fiveYTo6Y;

	    @Column(name = "SIX_Y_TO_7Y", precision = 20, scale = 2)
	    private BigDecimal sixYTo7Y;

	    @Column(name = "SEVEN_Y_TO_8Y", precision = 20, scale = 2)
	    private BigDecimal sevenYTo8Y;

	    @Column(name = "EIGHT_Y_TO_9Y", precision = 20, scale = 2)
	    private BigDecimal eightYTo9Y;

	    @Column(name = "NINE_Y_TO_10Y", precision = 20, scale = 2)
	    private BigDecimal nineYTo10Y;

	    @Column(name = "TEN_Y_TO_15Y", precision = 20, scale = 2)
	    private BigDecimal tenYTo15Y;

	    @Column(name = "FIFTEEN_Y_TO_20Y", precision = 20, scale = 2)
	    private BigDecimal fifteenYTo20Y;

	    @Column(name = "TWENTY_Y_ABOVE", precision = 20, scale = 2)
	    private BigDecimal twentyYAbove;

	    @Column(name = "NON_MATURING", precision = 20, scale = 2)
	    private BigDecimal nonMaturing;

	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    @Column(name = "REPORT_SUBMIT_DATE")
	    private LocalDate reportSubmitDate;

	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    @Column(name = "REPORT_FROM_DATE")
	    private LocalDate reportFromDate;

	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    @Column(name = "REPORT_TO_DATE")
	    private LocalDate reportToDate;

	    @DateTimeFormat(pattern = "dd-MM-yyyy")
	    @Column(name = "REPORT_DATE")
	    private LocalDate reportDate;

	    @Column(name = "ENTITY_FLG", length = 1)
	    private String entityFlg;

	    @Column(name = "MODIFY_FLG", length = 1)
	    private String modifyFlg;

	    @Column(name = "DEL_FLG", length = 1)
	    private String delFlg;

		public String getSiNo() {
			return siNo;
		}

		public void setSiNo(String siNo) {
			this.siNo = siNo;
		}

		public Date getDataDate() {
			return dataDate;
		}

		public void setDataDate(Date dataDate) {
			this.dataDate = dataDate;
		}

		public String getBankName() {
			return bankName;
		}

		public void setBankName(String bankName) {
			this.bankName = bankName;
		}

		public String getHeadOfficeSubsidiary() {
			return headOfficeSubsidiary;
		}

		public void setHeadOfficeSubsidiary(String headOfficeSubsidiary) {
			this.headOfficeSubsidiary = headOfficeSubsidiary;
		}

		public String getBankSymbol() {
			return bankSymbol;
		}

		public void setBankSymbol(String bankSymbol) {
			this.bankSymbol = bankSymbol;
		}

		public String getConventionalIslamic() {
			return conventionalIslamic;
		}

		public void setConventionalIslamic(String conventionalIslamic) {
			this.conventionalIslamic = conventionalIslamic;
		}

		public String getLocalForeign() {
			return localForeign;
		}

		public void setLocalForeign(String localForeign) {
			this.localForeign = localForeign;
		}

		public String getCbuaeTiering() {
			return cbuaeTiering;
		}

		public void setCbuaeTiering(String cbuaeTiering) {
			this.cbuaeTiering = cbuaeTiering;
		}

		public BigDecimal getInflows() {
			return inflows;
		}

		public void setInflows(BigDecimal inflows) {
			this.inflows = inflows;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public BigDecimal getTotalBalance() {
			return totalBalance;
		}

		public void setTotalBalance(BigDecimal totalBalance) {
			this.totalBalance = totalBalance;
		}

		public BigDecimal getOvernight() {
			return overnight;
		}

		public void setOvernight(BigDecimal overnight) {
			this.overnight = overnight;
		}

		public BigDecimal getOnTo1M() {
			return onTo1M;
		}

		public void setOnTo1M(BigDecimal onTo1M) {
			this.onTo1M = onTo1M;
		}

		public BigDecimal getOneMTo3M() {
			return oneMTo3M;
		}

		public void setOneMTo3M(BigDecimal oneMTo3M) {
			this.oneMTo3M = oneMTo3M;
		}

		public BigDecimal getThreeMTo6M() {
			return threeMTo6M;
		}

		public void setThreeMTo6M(BigDecimal threeMTo6M) {
			this.threeMTo6M = threeMTo6M;
		}

		public BigDecimal getSixMTo9M() {
			return sixMTo9M;
		}

		public void setSixMTo9M(BigDecimal sixMTo9M) {
			this.sixMTo9M = sixMTo9M;
		}

		public BigDecimal getNineMTo1Y() {
			return nineMTo1Y;
		}

		public void setNineMTo1Y(BigDecimal nineMTo1Y) {
			this.nineMTo1Y = nineMTo1Y;
		}

		public BigDecimal getOneYTo1_5Y() {
			return oneYTo1_5Y;
		}

		public void setOneYTo1_5Y(BigDecimal oneYTo1_5Y) {
			this.oneYTo1_5Y = oneYTo1_5Y;
		}

		public BigDecimal getOne5YTo2Y() {
			return one5YTo2Y;
		}

		public void setOne5YTo2Y(BigDecimal one5yTo2Y) {
			one5YTo2Y = one5yTo2Y;
		}

		public BigDecimal getTwoYTo3Y() {
			return twoYTo3Y;
		}

		public void setTwoYTo3Y(BigDecimal twoYTo3Y) {
			this.twoYTo3Y = twoYTo3Y;
		}

		public BigDecimal getThreeYTo4Y() {
			return threeYTo4Y;
		}

		public void setThreeYTo4Y(BigDecimal threeYTo4Y) {
			this.threeYTo4Y = threeYTo4Y;
		}

		public BigDecimal getFourYTo5Y() {
			return fourYTo5Y;
		}

		public void setFourYTo5Y(BigDecimal fourYTo5Y) {
			this.fourYTo5Y = fourYTo5Y;
		}

		public BigDecimal getFiveYTo6Y() {
			return fiveYTo6Y;
		}

		public void setFiveYTo6Y(BigDecimal fiveYTo6Y) {
			this.fiveYTo6Y = fiveYTo6Y;
		}

		public BigDecimal getSixYTo7Y() {
			return sixYTo7Y;
		}

		public void setSixYTo7Y(BigDecimal sixYTo7Y) {
			this.sixYTo7Y = sixYTo7Y;
		}

		public BigDecimal getSevenYTo8Y() {
			return sevenYTo8Y;
		}

		public void setSevenYTo8Y(BigDecimal sevenYTo8Y) {
			this.sevenYTo8Y = sevenYTo8Y;
		}

		public BigDecimal getEightYTo9Y() {
			return eightYTo9Y;
		}

		public void setEightYTo9Y(BigDecimal eightYTo9Y) {
			this.eightYTo9Y = eightYTo9Y;
		}

		public BigDecimal getNineYTo10Y() {
			return nineYTo10Y;
		}

		public void setNineYTo10Y(BigDecimal nineYTo10Y) {
			this.nineYTo10Y = nineYTo10Y;
		}

		public BigDecimal getTenYTo15Y() {
			return tenYTo15Y;
		}

		public void setTenYTo15Y(BigDecimal tenYTo15Y) {
			this.tenYTo15Y = tenYTo15Y;
		}

		public BigDecimal getFifteenYTo20Y() {
			return fifteenYTo20Y;
		}

		public void setFifteenYTo20Y(BigDecimal fifteenYTo20Y) {
			this.fifteenYTo20Y = fifteenYTo20Y;
		}

		public BigDecimal getTwentyYAbove() {
			return twentyYAbove;
		}

		public void setTwentyYAbove(BigDecimal twentyYAbove) {
			this.twentyYAbove = twentyYAbove;
		}

		public BigDecimal getNonMaturing() {
			return nonMaturing;
		}

		public void setNonMaturing(BigDecimal nonMaturing) {
			this.nonMaturing = nonMaturing;
		}

		public LocalDate getReportSubmitDate() {
			return reportSubmitDate;
		}

		public void setReportSubmitDate(LocalDate reportSubmitDate) {
			this.reportSubmitDate = reportSubmitDate;
		}

		public LocalDate getReportFromDate() {
			return reportFromDate;
		}

		public void setReportFromDate(LocalDate reportFromDate) {
			this.reportFromDate = reportFromDate;
		}

		public LocalDate getReportToDate() {
			return reportToDate;
		}

		public void setReportToDate(LocalDate reportToDate) {
			this.reportToDate = reportToDate;
		}

		public LocalDate getReportDate() {
			return reportDate;
		}

		public void setReportDate(LocalDate reportDate) {
			this.reportDate = reportDate;
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

		public RT_Liquidity_Risk_Data_Template(String siNo, Date dataDate, String bankName,
				String headOfficeSubsidiary, String bankSymbol, String conventionalIslamic, String localForeign,
				String cbuaeTiering, BigDecimal inflows, String currency, BigDecimal totalBalance, BigDecimal overnight,
				BigDecimal onTo1M, BigDecimal oneMTo3M, BigDecimal threeMTo6M, BigDecimal sixMTo9M,
				BigDecimal nineMTo1Y, BigDecimal oneYTo1_5Y, BigDecimal one5yTo2Y, BigDecimal twoYTo3Y,
				BigDecimal threeYTo4Y, BigDecimal fourYTo5Y, BigDecimal fiveYTo6Y, BigDecimal sixYTo7Y,
				BigDecimal sevenYTo8Y, BigDecimal eightYTo9Y, BigDecimal nineYTo10Y, BigDecimal tenYTo15Y,
				BigDecimal fifteenYTo20Y, BigDecimal twentyYAbove, BigDecimal nonMaturing, LocalDate reportSubmitDate,
				LocalDate reportFromDate, LocalDate reportToDate, LocalDate reportDate, String entityFlg,
				String modifyFlg, String delFlg) {
			super();
			this.siNo = siNo;
			this.dataDate = dataDate;
			this.bankName = bankName;
			this.headOfficeSubsidiary = headOfficeSubsidiary;
			this.bankSymbol = bankSymbol;
			this.conventionalIslamic = conventionalIslamic;
			this.localForeign = localForeign;
			this.cbuaeTiering = cbuaeTiering;
			this.inflows = inflows;
			this.currency = currency;
			this.totalBalance = totalBalance;
			this.overnight = overnight;
			this.onTo1M = onTo1M;
			this.oneMTo3M = oneMTo3M;
			this.threeMTo6M = threeMTo6M;
			this.sixMTo9M = sixMTo9M;
			this.nineMTo1Y = nineMTo1Y;
			this.oneYTo1_5Y = oneYTo1_5Y;
			one5YTo2Y = one5yTo2Y;
			this.twoYTo3Y = twoYTo3Y;
			this.threeYTo4Y = threeYTo4Y;
			this.fourYTo5Y = fourYTo5Y;
			this.fiveYTo6Y = fiveYTo6Y;
			this.sixYTo7Y = sixYTo7Y;
			this.sevenYTo8Y = sevenYTo8Y;
			this.eightYTo9Y = eightYTo9Y;
			this.nineYTo10Y = nineYTo10Y;
			this.tenYTo15Y = tenYTo15Y;
			this.fifteenYTo20Y = fifteenYTo20Y;
			this.twentyYAbove = twentyYAbove;
			this.nonMaturing = nonMaturing;
			this.reportSubmitDate = reportSubmitDate;
			this.reportFromDate = reportFromDate;
			this.reportToDate = reportToDate;
			this.reportDate = reportDate;
			this.entityFlg = entityFlg;
			this.modifyFlg = modifyFlg;
			this.delFlg = delFlg;
		}

		public RT_Liquidity_Risk_Data_Template() {
			super();
			// TODO Auto-generated constructor stub
		}

	


}
