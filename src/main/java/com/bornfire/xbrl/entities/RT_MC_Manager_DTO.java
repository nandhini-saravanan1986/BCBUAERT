package com.bornfire.xbrl.entities;

import java.util.regex.Pattern;

public class RT_MC_Manager_DTO {

	private String description;
	private Double q1Value;
	private Double q2Value;
	private Double q3Value;
	private Double q4Value;
	private Double currentYearValue;
	private Double lastYearValue;

	private Double quarterlyComparison;
	private Double yearlyComparison;
	private String makerRemarks;
	private String checkerRemarks;
	private String cellName;

	public RT_MC_Manager_DTO(String description, Object q1, Object q2, Object q3, Object q4, Object currentYear,
			Object lastYear, Object makerRemarks, Object checkerRemarks, String cellName) {

		this.description = description;
		this.cellName = cellName;
		this.q1Value = parseFlexibleNumber(q1);
		this.q2Value = parseFlexibleNumber(q2);
		this.q3Value = parseFlexibleNumber(q3);
		this.q4Value = parseFlexibleNumber(q4);
		this.currentYearValue = parseFlexibleNumber(currentYear);
		this.lastYearValue = parseFlexibleNumber(lastYear);

		double latestQuarter = (this.q4Value != null) ? this.q4Value : 0.0;
		double previousQuarter = (this.q3Value != null) ? this.q3Value : 0.0;
		this.quarterlyComparison = calculateVariation(latestQuarter, previousQuarter);

		double currentY = (this.currentYearValue != null) ? this.currentYearValue : 0.0;
		double lastY = (this.lastYearValue != null) ? this.lastYearValue : 0.0;
		this.yearlyComparison = calculateVariation(currentY, lastY);

		this.makerRemarks = (makerRemarks != null) ? String.valueOf(makerRemarks).trim() : "";
		this.checkerRemarks = (checkerRemarks != null) ? String.valueOf(checkerRemarks).trim() : "";
	}

	private Double calculateVariation(double latest, double previous) {
		if (previous == 0.0) {
			if (latest == 0.0) {
				return 0.0;
			} else {
				return null;
			}
		} else {
			return (latest - previous) / previous;
		}
	}

	private Double parseFlexibleNumber(Object val) {
		if (val == null) {
			return null;
		}
		if (val instanceof Number) {
			return ((Number) val).doubleValue();
		}
		if (val instanceof String) {
			String strVal = (String) val;
			if (strVal.trim().isEmpty() || strVal.trim().equalsIgnoreCase("null")) {
				return null;
			}
			if (strVal.trim().matches("^-?\\d+(\\.\\d+)?$")) {
				return Double.valueOf(strVal.trim());
			}
		}
		return null;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getQ1Value() {
		return q1Value;
	}

	public void setQ1Value(Double q1Value) {
		this.q1Value = q1Value;
	}

	public Double getQ2Value() {
		return q2Value;
	}

	public void setQ2Value(Double q2Value) {
		this.q2Value = q2Value;
	}

	public Double getQ3Value() {
		return q3Value;
	}

	public void setQ3Value(Double q3Value) {
		this.q3Value = q3Value;
	}

	public Double getQ4Value() {
		return q4Value;
	}

	public void setQ4Value(Double q4Value) {
		this.q4Value = q4Value;
	}

	public Double getCurrentYearValue() {
		return currentYearValue;
	}

	public void setCurrentYearValue(Double currentYearValue) {
		this.currentYearValue = currentYearValue;
	}

	public Double getLastYearValue() {
		return lastYearValue;
	}

	public void setLastYearValue(Double lastYearValue) {
		this.lastYearValue = lastYearValue;
	}

	public Double getQuarterlyComparison() {
		return quarterlyComparison;
	}

	public void setQuarterlyComparison(Double quarterlyComparison) {
		this.quarterlyComparison = quarterlyComparison;
	}

	public Double getYearlyComparison() {
		return yearlyComparison;
	}

	public void setYearlyComparison(Double yearlyComparison) {
		this.yearlyComparison = yearlyComparison;
	}

	public String getMakerRemarks() {
		return makerRemarks;
	}

	public void setMakerRemarks(String makerRemarks) {
		this.makerRemarks = makerRemarks;
	}

	public String getCheckerRemarks() {
		return checkerRemarks;
	}

	public void setCheckerRemarks(String checkerRemarks) {
		this.checkerRemarks = checkerRemarks;
	}
	public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }
}
