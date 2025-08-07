package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import oracle.sql.DATE;

public interface RT_SLS_Repository extends JpaRepository<RT_SLS_ENTITIES, String> {
	 
	@Query(value = "select * from RT_SLS_USD order by REPORT_DATE", nativeQuery = true)
	 List<RT_SLS_ENTITIES> rtslslist();
	 
	 @Query(value = "select * from RT_SLS_USD where REPORT_DATE=?1 AND REPORT_CURRENCY=?2 ", nativeQuery = true)
	 List<RT_SLS_ENTITIES> rtslslistbydate(Date reportdate,String currency);
	 
	 @Query(value = "select * from RT_SLS_USD where REPORT_DATE=?1", nativeQuery = true)
	 List<RT_SLS_ENTITIES> rtslslistonlydate(Date reportdate);
}
