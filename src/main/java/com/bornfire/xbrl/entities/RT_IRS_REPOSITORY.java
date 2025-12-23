package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import oracle.sql.DATE;

public interface RT_IRS_REPOSITORY extends JpaRepository<RT_IRS_ENTITY, String> {
	 
	@Query(value = "select * from RT_IRS_SUMMARY_TABLE1 order by REPORT_DATE", nativeQuery = true)
	 List<RT_IRS_ENTITY> rtirslist();
	 
	 
		/*
		 * @Query(value =
		 * "select * from RT_IRS_SUMMARY_TABLE1 where REPORT_DATE=?1 AND REPORT_CURRENCY=?2 "
		 * , nativeQuery = true) List<RT_IRS_ENTITY> rtirslistbydate(Date
		 * reportdate,String currency);
		 */
	 
	 
	 @Query(value = "select * from RT_IRS_SUMMARY_TABLE1 where TRUNC(REPORT_DATE)=?1 AND REPORT_CURRENCY=?2", nativeQuery = true)
	 List<RT_IRS_ENTITY> rtirslistbydate(Date reportdate,String currency);

	 
	 @Query(value = "select * from RT_IRS_SUMMARY_TABLE1 where REPORT_DATE=?1", nativeQuery = true)
	 List<RT_IRS_ENTITY> getIrsCurrencyByDate(Date reportdate);
		/*
		 * @Query(value =
		 * "select * from RT_SLS_USD where REPORT_DATE=?1 AND REPORT_CURRENCY=?2 ",
		 * nativeQuery = true) List<RT_IRS_ENTITY> rtslslistbydate(Date
		 * reportdate,String currency);
		 * 
		 * @Query(value = "select * from RT_SLS_USD where REPORT_DATE=?1", nativeQuery =
		 * true) List<RT_IRS_ENTITY> rtslslistonlydate(Date reportdate);
		 */
}
