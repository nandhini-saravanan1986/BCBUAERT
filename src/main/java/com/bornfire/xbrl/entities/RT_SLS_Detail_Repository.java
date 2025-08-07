package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RT_SLS_Detail_Repository extends JpaRepository<RT_SLS_Detail_Enitity,String>  {

	@Query(value = "select * from RT_SLS_DETAILTABLE where REPORT_DATE=?1 offset ?2 rows fetch next ?3 rows only", nativeQuery = true)
	 List<RT_SLS_Detail_Enitity> slsdetaillist(Date REPORT_DATE,int startpage,int endpage);
	 

	@Query(value = "select count(*) from RT_SLS_DETAILTABLE where REPORT_DATE=?1", nativeQuery = true)
	 int  slsdetaillistcount(Date REPORT_DATE);
}
