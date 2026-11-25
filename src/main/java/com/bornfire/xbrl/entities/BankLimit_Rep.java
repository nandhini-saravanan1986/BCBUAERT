package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BankLimit_Rep extends CrudRepository<BankLimit_Entity,String>{
	
	@Query(
			  value = "SELECT * FROM MIS_BANK_LIMITS " +
			          "WHERE TRUNC(REPORT_DATE) = TRUNC(:reportDate) and TOTAL_EXPOSURE <> 0 " +
			          "ORDER BY S_NO",
			  nativeQuery = true
			)
	List<BankLimit_Entity> getalllist(@Param("reportDate") Date reportDate);

	@Query(value = "select * from MIS_BANK_LIMITS WHERE REPORT_DATE=?1 ORDER BY BANK_NAME desc", nativeQuery = true)
	List<BankLimit_Entity> getalllists(Date REPORT_DATE);
	
	@Query(value = "SELECT * FROM MIS_BANK_LIMITS a "
			+ "WHERE a.REPORT_DATE = (SELECT MAX(REPORT_DATE) FROM MIS_BANK_LIMITS) "
			+ "AND a.S_NO = (SELECT MIN(b.S_NO) FROM MIS_BANK_LIMITS b "
			+ "WHERE b.REPORT_DATE = a.REPORT_DATE AND b.BANK_NAME = a.BANK_NAME) "
			+ "ORDER BY a.BANK_NAME", nativeQuery = true)
	List<BankLimit_Entity> getallbanknamemaxdate();
	
}