package com.bornfire.xbrl.dto;

import java.util.List;

public class AnalyticalPivotRequestDto {

	private String tableName;
	/** Legacy: plain column names without filters. Ignored if rowDimensions is non-empty. */
	private List<String> rowColumns;
	/** Legacy: plain column names without filters. Ignored if columnDimensions is non-empty. */
	private List<String> columnColumns;
	/** Preferred: row dimensions with optional per-column filters. */
	private List<AnalyticalPivotDimensionDto> rowDimensions;
	private List<AnalyticalPivotDimensionDto> columnDimensions;
	/** Optional: filter-only dimensions (applied to WHERE, not GROUP BY). */
	private List<AnalyticalPivotDimensionDto> filterDimensions;
	private String aggregation;
	private String valueColumn;
	/** NONE | DIVIDE | MULTIPLY | PERCENT_OF — applied only when aggregation is SUM. */
	private String aggregateTransform;
	/** Numeric operand (divide by / multiply by / denominator for percent). */
	private String aggregateTransformOperand;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getRowColumns() {
		return rowColumns;
	}

	public void setRowColumns(List<String> rowColumns) {
		this.rowColumns = rowColumns;
	}

	public List<String> getColumnColumns() {
		return columnColumns;
	}

	public void setColumnColumns(List<String> columnColumns) {
		this.columnColumns = columnColumns;
	}

	public String getAggregation() {
		return aggregation;
	}

	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}

	public String getValueColumn() {
		return valueColumn;
	}

	public void setValueColumn(String valueColumn) {
		this.valueColumn = valueColumn;
	}

	public List<AnalyticalPivotDimensionDto> getRowDimensions() {
		return rowDimensions;
	}

	public void setRowDimensions(List<AnalyticalPivotDimensionDto> rowDimensions) {
		this.rowDimensions = rowDimensions;
	}

	public List<AnalyticalPivotDimensionDto> getColumnDimensions() {
		return columnDimensions;
	}

	public void setColumnDimensions(List<AnalyticalPivotDimensionDto> columnDimensions) {
		this.columnDimensions = columnDimensions;
	}

	public List<AnalyticalPivotDimensionDto> getFilterDimensions() {
		return filterDimensions;
	}

	public void setFilterDimensions(List<AnalyticalPivotDimensionDto> filterDimensions) {
		this.filterDimensions = filterDimensions;
	}

	public String getAggregateTransform() {
		return aggregateTransform;
	}

	public void setAggregateTransform(String aggregateTransform) {
		this.aggregateTransform = aggregateTransform;
	}

	public String getAggregateTransformOperand() {
		return aggregateTransformOperand;
	}

	public void setAggregateTransformOperand(String aggregateTransformOperand) {
		this.aggregateTransformOperand = aggregateTransformOperand;
	}
}
