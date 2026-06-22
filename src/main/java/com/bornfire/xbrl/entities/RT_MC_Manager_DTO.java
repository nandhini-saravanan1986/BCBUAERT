package com.bornfire.xbrl.entities;

public class RT_MC_Manager_DTO {

	private String description;
	private Double date1Value;
	private Double date2Value;
	private Double date3Value;
	private Double date4Value;
	private Double date5Value;
	private Double comparison;

	public RT_MC_Manager_DTO(String description, Object d1, Object d2, Object d3, Object d4, Object d5) {
		this.description = description;

		this.date1Value = parseFlexibleNumber(d1);
		this.date2Value = parseFlexibleNumber(d2);
		this.date3Value = parseFlexibleNumber(d3);
		this.date4Value = parseFlexibleNumber(d4);
		this.date5Value = parseFlexibleNumber(d5);

		double latest = (this.date1Value != null) ? this.date1Value : 0.0;
		double previous = (this.date2Value != null) ? this.date2Value : 0.0;

		if (previous == 0.0) {
			if (latest == 0.0) {
				this.comparison = 0.0;
			} else {
				this.comparison = null;
			}
		} else {
			this.comparison = (latest - previous) / previous;
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

	public Double getDate1Value() {
		return date1Value;
	}

	public void setDate1Value(Double date1Value) {
		this.date1Value = date1Value;
	}

	public Double getDate2Value() {
		return date2Value;
	}

	public void setDate2Value(Double date2Value) {
		this.date2Value = date2Value;
	}

	public Double getDate3Value() {
		return date3Value;
	}

	public void setDate3Value(Double date3Value) {
		this.date3Value = date3Value;
	}

	public Double getDate4Value() {
		return date4Value;
	}

	public void setDate4Value(Double date4Value) {
		this.date4Value = date4Value;
	}

	public Double getDate5Value() {
		return date5Value;
	}

	public void setDate5Value(Double date5Value) {
		this.date5Value = date5Value;
	}

	public Double getComparison() {
		return comparison;
	}

	public void setComparison(Double comparison) {
		this.comparison = comparison;
	}

}
