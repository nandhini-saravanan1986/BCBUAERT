package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Leverage_ratio_rep extends JpaRepository<Leverage_ratio_entity, Date> {

	@Query(value="Select * from BRF96_SUMMARYTABLE Where report_date = ?1",nativeQuery=true)
	Leverage_ratio_entity GetLeverageration(Date Selecteddate);
	
	@Query(value="Select month_end,R23_TOTAL from (With Eligibility_ratio as(Select (R23_TOTAL*100) as R23_TOTAL, REPORT_DATE from BRF96_SUMMARYTABLE),\r\n"
			+ "Month_end_data as (SELECT LAST_DAY(ADD_MONTHS(TRUNC(?1, 'YEAR'), LEVEL - 1))\r\n"
			+ "AS month_end FROM dual CONNECT BY LEVEL <= 12 )\r\n"
			+ "Select To_Char(month_end,'DD-MM-YYYY') AS month_end,NVL(R23_TOTAL,0) AS R23_TOTAL\r\n"
			+ "from Month_end_data a left join Eligibility_ratio b on a.month_end = b.report_date Order by month_end asc)",nativeQuery=true)
	List<Object[]> GetLeverageration_curryear_report(Date Selecteddate);
	
	@Query(value = "Select month_dates,R23_TOTAL from (\r\n"
			+ "With Eligibility_ratio as(Select (R23_TOTAL*100) as R23_TOTAL, REPORT_DATE from BRF96_SUMMARYTABLE),\r\n"
			+ "Current_month_date as (SELECT TRUNC(?1, 'MM') + (LEVEL - 1) AS month_dates FROM dual\r\n"
			+ "CONNECT BY TRUNC(?1, 'MM') + (LEVEL - 1) <= LAST_DAY(?1) )\r\n"
			+ "Select To_char(month_dates,'DD-MM-YYYY') as month_dates,NVL(R23_TOTAL,0) AS R23_TOTAL\r\n"
			+ "from Current_month_date a left join Eligibility_ratio b on \r\n"
			+ "a.month_dates = b.report_date Order by month_dates asc)", nativeQuery = true)
	List<Object[]> GetLeveragerationcurrentmonthgraph(Date Selecteddate);
}
