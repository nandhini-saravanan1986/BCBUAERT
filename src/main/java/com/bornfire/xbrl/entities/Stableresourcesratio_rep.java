package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Stableresourcesratio_rep extends JpaRepository<Stableresourcesratio_entity, Date> {
	
	@Query(value="Select * from BRF7_SUMMARYTABLE where report_date=?1",nativeQuery=true)
	Stableresourcesratio_entity Getstableresourceration(Date Selecteddate);
	
	@Query(value="Select To_char(month_end) as month_end,R27_AMOUNT from (\r\n"
			+ "			With Eligibility_ratio as(\r\n"
			+ "			Select (R27_AMOUNT) as R27_AMOUNT, REPORT_DATE from BRF7_SUMMARYTABLE),\r\n"
			+ "			Month_end_data as (SELECT LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'YEAR'), LEVEL - 1))\r\n"
			+ "			AS month_end FROM dual CONNECT BY LEVEL <= 12 )\r\n"
			+ "			Select month_end,NVL(R27_AMOUNT,0) AS R27_AMOUNT\r\n"
			+ "			from Month_end_data a left join Eligibility_ratio b on a.month_end = b.report_date Order by month_end asc)",nativeQuery=true)
	List<Object[]> GetAsrr_curryear_report();
	
	@Query(value = "Select To_char(month_dates) as month_end,R27_AMOUNT from (\r\n"
			+ "			With Eligibility_ratio as(\r\n"
			+ "			Select (R27_AMOUNT) as R27_AMOUNT, REPORT_DATE from BRF7_SUMMARYTABLE),\r\n"
			+ "			Current_month_date as (SELECT TRUNC(SYSDATE, 'MM') + (LEVEL - 1) AS month_dates FROM dual\r\n"
			+ "			CONNECT BY TRUNC(SYSDATE, 'MM') + (LEVEL - 1) <= LAST_DAY(SYSDATE) )\r\n"
			+ "			Select month_dates,NVL(R27_AMOUNT,0) AS R27_AMOUNT\r\n"
			+ "			from Current_month_date a left join Eligibility_ratio b on a.month_dates = b.report_date Order by month_dates asc)", nativeQuery = true)
	List<Object[]> GetAsrrcurrentmonthgraph();

}
