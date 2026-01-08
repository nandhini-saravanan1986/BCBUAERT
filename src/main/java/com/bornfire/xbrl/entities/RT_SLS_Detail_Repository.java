package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RT_SLS_Detail_Repository extends JpaRepository<RT_SLS_Detail_Enitity,String>  {

	/*
	 * @Query(value =
	 * "select * from RT_SLS_DETAILTABLE where REPORT_DATE=?1 AND Cust_id is NOT null  offset ?2 rows fetch next ?3 rows only"
	 * , nativeQuery = true) List<RT_SLS_Detail_Enitity> slsdetaillist(Date
	 * REPORT_DATE,int startpage,int endpage);
	 */

	/* @Query(value = "select * from RT_SLS_DETAILTABLE where REPORT_DATE=?1 and ACCT_CRNCY_CODE = ?2 AND Cust_id is NOT null AND offset ?3 rows fetch next ?4 rows only", nativeQuery = true)
	 List<RT_SLS_Detail_Enitity> slsdetaillist(Date REPORT_DATE, String currency, int offset, int pageSize);*/

	 
	@Query(value = " SELECT * FROM RT_SLS_DETAILTABLE  WHERE TRUNC(REPORT_DATE) = TRUNC(?1) AND ACCT_CRNCY_CODE = ?2 AND CUST_ID IS NOT NULL  ORDER BY FORACID OFFSET ?3 ROWS FETCH NEXT ?4 ROWS ONLY", nativeQuery = true)
		List<RT_SLS_Detail_Enitity> slsdetaillist(
		        Date reportDate,
		        String currency,
		        int offset,
		        int pageSize
		);


	 
	 @Query(value = "select * from RT_SLS_DETAILTABLE where REPORT_DATE=?1 ", nativeQuery = true)
	 List<RT_SLS_Detail_Enitity> slsdetaillistdate(Date REPORT_DATE);

	 @Query(value = "select * from RT_SLS_DETAILTABLE where REPORT_DATE=?1 AND REPORT_LABEL=?2 and ACCT_CRNCY_CODE = ?3", nativeQuery = true)
	 List<RT_SLS_Detail_Enitity> slsdetaillistrowid(Date REPORT_DATE,String REPORT_LABEL,String currency);
	
	@Query(value = "select count(*) from RT_SLS_DETAILTABLE where REPORT_DATE=?1 and  ACCT_CRNCY_CODE = ?2", nativeQuery = true)
	 int  slsdetaillistcount(Date REPORT_DATE, String currency);
	
		/*
		 * @Query(value =
		 * "select count(*) from RT_SLS_DETAILTABLE where REPORT_DATE=?1 and  ACCT_CRNCY_CODE = ?2 AND REPORT_LABEL=?3"
		 * , nativeQuery = true) int slsdetaillistcountROWID(Date REPORT_DATE, String
		 * currency,String REPORT_LABEL);
		 */
	
	
	
	@Query(value = "SELECT COUNT(*) FROM RT_SLS_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?1)AND ROWID = ?2 AND ACCT_CRNCY_CODE = ?3 AND CUST_ID IS NOT NULL", nativeQuery = true)
		int slsdetaillistcountROWID(
		        Date reportDate,
		        String rowid,
		        String currency
		);

}
