package com.bornfire.xbrl.dto;

import java.math.BigDecimal;

/**
 * Medium/long-term outflows (R39) and inflows (R70) with liquidity ratios for one scenario day.
 */
public class SlsSensAnalyticalSnapshotDto {

	private Integer sensDayOffset;
	private String scenarioLabel;
	private String asOfDateFormatted;
	private BigDecimal outflowMediumTerm;
	private BigDecimal outflowLongTerm;
	private BigDecimal inflowMediumTerm;
	private BigDecimal inflowLongTerm;
	private BigDecimal longTermRatio;
	private BigDecimal medLongTermRatio;

	public Integer getSensDayOffset() {
		return sensDayOffset;
	}

	public void setSensDayOffset(Integer sensDayOffset) {
		this.sensDayOffset = sensDayOffset;
	}

	public String getScenarioLabel() {
		return scenarioLabel;
	}

	public void setScenarioLabel(String scenarioLabel) {
		this.scenarioLabel = scenarioLabel;
	}

	public String getAsOfDateFormatted() {
		return asOfDateFormatted;
	}

	public void setAsOfDateFormatted(String asOfDateFormatted) {
		this.asOfDateFormatted = asOfDateFormatted;
	}

	public BigDecimal getOutflowMediumTerm() {
		return outflowMediumTerm;
	}

	public void setOutflowMediumTerm(BigDecimal outflowMediumTerm) {
		this.outflowMediumTerm = outflowMediumTerm;
	}

	public BigDecimal getOutflowLongTerm() {
		return outflowLongTerm;
	}

	public void setOutflowLongTerm(BigDecimal outflowLongTerm) {
		this.outflowLongTerm = outflowLongTerm;
	}

	public BigDecimal getInflowMediumTerm() {
		return inflowMediumTerm;
	}

	public void setInflowMediumTerm(BigDecimal inflowMediumTerm) {
		this.inflowMediumTerm = inflowMediumTerm;
	}

	public BigDecimal getInflowLongTerm() {
		return inflowLongTerm;
	}

	public void setInflowLongTerm(BigDecimal inflowLongTerm) {
		this.inflowLongTerm = inflowLongTerm;
	}

	public BigDecimal getLongTermRatio() {
		return longTermRatio;
	}

	public void setLongTermRatio(BigDecimal longTermRatio) {
		this.longTermRatio = longTermRatio;
	}

	public BigDecimal getMedLongTermRatio() {
		return medLongTermRatio;
	}

	public void setMedLongTermRatio(BigDecimal medLongTermRatio) {
		this.medLongTermRatio = medLongTermRatio;
	}
}
