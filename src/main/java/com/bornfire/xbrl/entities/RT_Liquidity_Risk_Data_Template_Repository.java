package com.bornfire.xbrl.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_Liquidity_Risk_Data_Template_Repository extends JpaRepository<RT_Liquidity_Risk_Data_Template, BigDecimal> {

	@Query(value = "select * from BCBUAE_LIQUIDITY_RISK_DATA_TEMPLATE where DEL_FLG != 'Y'", nativeQuery = true)
	List<RT_Liquidity_Risk_Data_Template> getLiquiditylist();
	
	@Query(value = "SELECT TRUNC(MAX(REPORT_DATE)) FROM BCBUAE_LIQUIDITY_RISK_DATA_TEMPLATE", nativeQuery = true)
	Timestamp findLastReportDate();
		
     @Query(value = "SELECT TRUNC(MAX(REPORT_DATE)) FROM BCBUAE_LIQUIDITY_RISK_DATA_TEMPLATE WHERE REPORT_DATE < (SELECT MAX(REPORT_DATE) FROM BCBUAE_LIQUIDITY_RISK_DATA_TEMPLATE)", nativeQuery = true)
    Timestamp findSecondLastReportDate();
}
