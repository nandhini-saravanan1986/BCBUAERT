package com.bornfire.xbrl.entities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_TradeLevelDataDerivativesSimplifiedRepository extends JpaRepository<RT_TradeLevelDataDerivativesSimplified, String> {
	// Add custom queries if needed

	@Query(value = "select * from BCBUAE_TRADE_LEVEL_DERIVATIVES_SIMPLIFIED  ", nativeQuery = true)
	List<RT_TradeLevelDataDerivativesSimplified> getlist();

	@Query(value = "SELECT * FROM BCBUAE_TRADE_LEVEL_DERIVATIVES_SIMPLIFIED ", nativeQuery = true)
	List<RT_TradeLevelDataDerivativesSimplified> getfxriskdatalistdata();

	@Query(value = "SELECT * FROM BCBUAE_TRADE_LEVEL_DERIVATIVES_SIMPLIFIED ", nativeQuery = true)
	List<Object[]> gettradeleveldataderivative1();
	
	
	@Query(value = "SELECT * FROM BCBUAE_TRADE_LEVEL_DERIVATIVES_SIMPLIFIED where SI_NO =?1 ", nativeQuery = true)
	RT_TradeLevelDataDerivativesSimplified getParticularDataBySI_NO(String SI_NO);
	
	@Query(value = "SELECT TRUNC(MAX(REPORT_DATE)) FROM BCBUAE_TRADE_LEVEL_DERIVATIVES_SIMPLIFIED", nativeQuery = true)
	Timestamp findLastReportDate();
	
	@Query(value = "SELECT TRUNC(MAX(REPORT_DATE)) FROM BCBUAE_TRADE_LEVEL_DERIVATIVES_SIMPLIFIED WHERE REPORT_DATE < (SELECT MAX(REPORT_DATE) FROM BCBUAE_TRADE_LEVEL_DERIVATIVES_SIMPLIFIED)", nativeQuery = true)
	Timestamp findSecondLastReportDate();
	
}
