package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface Capitaladequacyratio_rep extends JpaRepository<Capitaladequacyratio_entity, Date> {

	@Query(value="Select * from BRF95_SUMMARYTABLE where report_date = ?1",nativeQuery=true)
	Capitaladequacyratio_entity Getcapitalrationdetail(Date Selecteddate);
	
	@Query(value="Select To_char(month_end,'DD-MM-YYYY') as month_end,R1_RATIOS2 from (\r\n"
			+ "With Eligibility_ratio as(Select (R1_RATIOS2) as R1_RATIOS2, REPORT_DATE from BRF95_SUMMARYTABLE),\r\n"
			+ "Month_end_data as (SELECT LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'YEAR'), LEVEL - 1))\r\n"
			+ "AS month_end FROM dual CONNECT BY LEVEL <= 12 )\r\n"
			+ "Select month_end,NVL(R1_RATIOS2,0) AS R1_RATIOS2\r\n"
			+ "from Month_end_data a left join Eligibility_ratio b on a.month_end = b.report_date Order by month_end asc)",nativeQuery=true)
	List<Object[]> GetCapitalratio_curryear_report();
	
	@Query(value = "Select To_char(month_dates,'DD-MM-YYYY') as month_end,R1_RATIOS2 from (\r\n"
			+ "With Eligibility_ratio as(Select (R1_RATIOS2) as R1_RATIOS2, REPORT_DATE from BRF95_SUMMARYTABLE),\r\n"
			+ "Current_month_date as (SELECT TRUNC(SYSDATE, 'MM') + (LEVEL - 1) AS month_dates FROM dual\r\n"
			+ "CONNECT BY TRUNC(SYSDATE, 'MM') + (LEVEL - 1) <= LAST_DAY(SYSDATE) )\r\n"
			+ "Select month_dates,NVL(R1_RATIOS2,0) AS R1_RATIOS2\r\n"
			+ "from Current_month_date a left join Eligibility_ratio b on a.month_dates = b.report_date Order by month_dates asc)", nativeQuery = true)
	List<Object[]> GetCapitalratio_currentmonthgraph();
	
	@Query(value = "Select r15_ratios1,r22_ratios2,r16_ratios1,r18_ratios1,r23_ratios1,r23_ratios2,r45_ratios2,"
			+ "r51_ratios2 from BRF95_SUMMARYTABLE Where report_date = ?1", nativeQuery = true)
	List<Object[]> GetCapitalAdequecyvalues(Date Selecteddate);
	
	@Query(value = "Select r15_ratios1 from BRF95_SUMMARYTABLE Where report_date in ("
			+ "Select max(report_date) from BRF95_SUMMARYTABLE)", nativeQuery = true)
	String GetCurrentyear_tier_1_capital();
	
}
