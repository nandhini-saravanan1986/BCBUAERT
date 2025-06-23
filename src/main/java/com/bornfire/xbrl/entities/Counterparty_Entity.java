package com.bornfire.xbrl.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "MIS_COUNTER_PARTY_TABLE")
public class Counterparty_Entity{


    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSrlNo() {
		return srlNo;
	}

	public void setSrlNo(String srlNo) {
		this.srlNo = srlNo;
	}

	public String getCounterPartyBank() {
		return counterPartyBank;
	}

	public void setCounterPartyBank(String counterPartyBank) {
		this.counterPartyBank = counterPartyBank;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}

	@Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "SRL_NO")
    private String srlNo;

    @Column(name = "COUNTER_PARTY_BANK", length = 100)
    private String counterPartyBank;
    

    @Column(name = "COUNTERPARTY_CODE", length = 50)
    private String counterPartyBankCode;

    public String getCounterPartyBankCode() {
		return counterPartyBankCode;
	}

	public void setCounterPartyBankCode(String counterPartyBankCode) {
		this.counterPartyBankCode = counterPartyBankCode;
	}

	@Column(name = "LOCATION", length = 100)
    private String location;

    @Column(name = "CREATE_USER", length = 100)
    private String createUser;

    @Column(name = "MODIFY_USER", length = 100)
    private String modifyUser;

    @Column(name = "DEL_FLG", length = 1)
    private String delFlg;

    @Column(name = "RATING", length = 1)
    private String rating;
    
    
    

    public Date getCounterpartyCreatedDate() {
		return counterpartyCreatedDate;
	}

	public void setCounterpartyCreatedDate(Date counterpartyCreatedDate) {
		this.counterpartyCreatedDate = counterpartyCreatedDate;
	}

	public BigDecimal getRegularLimit() {
		return regularLimit;
	}

	public void setRegularLimit(BigDecimal regularLimit) {
		this.regularLimit = regularLimit;
	}

	public BigDecimal getAdhocLimit() {
		return adhocLimit;
	}

	public void setAdhocLimit(BigDecimal adhocLimit) {
		this.adhocLimit = adhocLimit;
	}

	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	public BigDecimal getSettlementLimit() {
		return settlementLimit;
	}

	public void setSettlementLimit(BigDecimal settlementLimit) {
		this.settlementLimit = settlementLimit;
	}

	public BigDecimal getMoneyMarketLimit() {
		return moneyMarketLimit;
	}

	public void setMoneyMarketLimit(BigDecimal moneyMarketLimit) {
		this.moneyMarketLimit = moneyMarketLimit;
	}

	public BigDecimal getTradeFinanceLimit() {
		return tradeFinanceLimit;
	}

	public void setTradeFinanceLimit(BigDecimal tradeFinanceLimit) {
		this.tradeFinanceLimit = tradeFinanceLimit;
	}

	@Column(name = "COUNTERPARTY_CREATED_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date counterpartyCreatedDate;

    @Column(name = "REGULAR_LIMIT", precision = 24, scale = 2)
    private BigDecimal regularLimit;

    @Column(name = "ADHOC_LIMIT", precision = 24, scale = 2)
    private BigDecimal adhocLimit;

    @Column(name = "CREDIT_LIMIT", precision = 24, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "SETTLEMENT_LIMIT", precision = 24, scale = 2)
    private BigDecimal settlementLimit;

    @Column(name = "MONEY_MARKET_LIMIT", precision = 24, scale = 2)
    private BigDecimal moneyMarketLimit;

    @Column(name = "TRADE_FINANCE_LIMIT", precision = 24, scale = 2)
    private BigDecimal tradeFinanceLimit;
    
    
    
    public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	@Column(name = "EXPIRY_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

	@Column(name = "ADHOC_LIMIT_EXP_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date Adhoc_Limit_exp_date;
	

	public Date getAdhoc_Limit_exp_date() {
		return Adhoc_Limit_exp_date;
	}

	public void setAdhoc_Limit_exp_date(Date adhoc_Limit_exp_date) {
		Adhoc_Limit_exp_date = adhoc_Limit_exp_date;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
    
}

