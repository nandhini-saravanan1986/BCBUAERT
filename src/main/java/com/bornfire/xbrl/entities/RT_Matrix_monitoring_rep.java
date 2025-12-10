package com.bornfire.xbrl.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_Matrix_monitoring_rep extends JpaRepository<RT_Matrix_monitoring_entity, BigDecimal> {
	
	@Query(value = "Select * from rt_matrix_monitored_table where (S_NO, REPORT_DATE) in (Select S_NO, Max(REPORT_DATE) from rt_matrix_monitored_table\r\n"
			+ "Group by S_NO) Order by S_NO Asc", nativeQuery = true)
	List<RT_Matrix_monitoring_entity> Getcurrentdatematrixcal();
	
	@Query(value = "Select To_char(month_end) as month_end,R15_ELIGI_LIQ_ASSETS from (\r\n"
			+ "With Eligibility_ratio as(\r\n"
			+ "Select (R15_ELIGI_LIQ_ASSETS*100) as R15_ELIGI_LIQ_ASSETS, REPORT_DATE from brf8_summarytable),\r\n"
			+ "Month_end_data as (SELECT LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'YEAR'), LEVEL - 1))\r\n"
			+ "AS month_end FROM dual CONNECT BY LEVEL <= 12 )\r\n"
			+ "Select month_end,NVL(R15_ELIGI_LIQ_ASSETS,0) AS R15_ELIGI_LIQ_ASSETS\r\n"
			+ "from Month_end_data a left join Eligibility_ratio b on a.month_end = b.report_date Order by month_end asc)\r\n"
			+ "\r\n"
			+ "", nativeQuery = true)
	List<Object[]> GetElar_curryear_report();

	@Query(value = "Select To_char(month_dates) as month_end,R15_ELIGI_LIQ_ASSETS from (\r\n"
			+ "With Eligibility_ratio as(\r\n"
			+ "Select (R15_ELIGI_LIQ_ASSETS*100) as R15_ELIGI_LIQ_ASSETS, REPORT_DATE from brf8_summarytable),\r\n"
			+ "Current_month_date as (SELECT TRUNC(SYSDATE, 'MM') + (LEVEL - 1) AS month_dates FROM dual\r\n"
			+ "CONNECT BY TRUNC(SYSDATE, 'MM') + (LEVEL - 1) <= LAST_DAY(SYSDATE) )\r\n"
			+ "Select month_dates,NVL(R15_ELIGI_LIQ_ASSETS,0) AS R15_ELIGI_LIQ_ASSETS\r\n"
			+ "from Current_month_date a left join Eligibility_ratio b on a.month_dates = b.report_date Order by month_dates asc)", nativeQuery = true)
	List<Object[]> GetElarcurrentmonthgraph();
}
