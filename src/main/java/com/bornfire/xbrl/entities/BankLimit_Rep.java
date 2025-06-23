package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BankLimit_Rep extends CrudRepository<BankLimit_Entity,String>{
	
	@Query(value = "SELECT * FROM MIS_BANK_LIMITS WHERE REPORT_DATE = :reportDate ORDER BY S_NO", nativeQuery = true)
	List<BankLimit_Entity> getalllist(@Param("reportDate") Date reportDate);

	@Query(value = "select * from MIS_BANK_LIMITS WHERE REPORT_DATE=?1 ORDER BY S_NO", nativeQuery = true)
	List<BankLimit_Entity> getalllists(Date REPORT_DATE);
	
}