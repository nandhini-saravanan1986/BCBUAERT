package com.bornfire.xbrl.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_Matrix_monitoring_rep extends JpaRepository<RT_Matrix_monitoring_entity, BigDecimal> {
	
	@Query(value = "Select * from rt_matrix_monitored_table where (S_NO, REPORT_DATE) in (Select S_NO, Max(REPORT_DATE) from rt_matrix_monitored_table\r\n"
			+ "Group by S_NO) Order by S_NO Asc", nativeQuery = true)
	List<RT_Matrix_monitoring_entity> Getcurrentdatematrixcal();
	
	@Query(value = "Select * from rt_matrix_monitored_table Where REPORT_DATE=?1 and S_NO =?2", nativeQuery = true)
	List<RT_Matrix_monitoring_entity> checkdataavail(Date Report_date,String Srl_no);
	
	@Query(value = "Select TO_CHAR(month_end,'DD-MM-YYYY') AS month_end,R15_ELIGI_LIQ_ASSETS from (\r\n"
			+ "With Eligibility_ratio as(\r\n"
			+ "Select (R15_ELIGI_LIQ_ASSETS*100) as R15_ELIGI_LIQ_ASSETS, REPORT_DATE from brf8_summarytable),\r\n"
			+ "Month_end_data as (SELECT LAST_DAY(ADD_MONTHS(TRUNC(?1, 'YEAR'), LEVEL - 1))\r\n"
			+ "AS month_end FROM dual CONNECT BY LEVEL <= 12 )\r\n"
			+ "Select month_end,NVL(R15_ELIGI_LIQ_ASSETS,0) AS R15_ELIGI_LIQ_ASSETS\r\n"
			+ "from Month_end_data a left join Eligibility_ratio b on a.month_end = b.report_date Order by A.month_end asc)", nativeQuery = true)
	List<Object[]> GetElar_curryear_report(Date Selecteddate);

	@Query(value = "Select month_dates,R15_ELIGI_LIQ_ASSETS from (\r\n"
			+ "With Eligibility_ratio as(\r\n"
			+ "Select (R15_ELIGI_LIQ_ASSETS*100) as R15_ELIGI_LIQ_ASSETS, REPORT_DATE from brf8_summarytable),\r\n"
			+ "Current_month_date as (SELECT TRUNC(?1, 'MM') + (LEVEL - 1) AS month_dates FROM dual\r\n"
			+ "CONNECT BY TRUNC(?1, 'MM') + (LEVEL - 1) <= LAST_DAY(?1) )\r\n"
			+ "Select To_char(month_dates,'DD-MM-YYYY') AS month_dates,NVL(R15_ELIGI_LIQ_ASSETS,0) AS R15_ELIGI_LIQ_ASSETS\r\n"
			+ "from Current_month_date a left join Eligibility_ratio b on a.month_dates = b.report_date Order by month_dates asc)", nativeQuery = true)
	List<Object[]> GetElarcurrentmonthgraph(Date Selecteddate);
	
	@Query(value = "SELECT *\r\n"
			+ "FROM (\r\n"
			+ "    WITH TakingWithoutSortedValue AS (\r\n"
			+ "        SELECT *\r\n"
			+ "        FROM rt_matrix_monitored_table\r\n"
			+ "        WHERE (s_no, report_date) IN (\r\n"
			+ "            SELECT s_no, MAX(report_date)\r\n"
			+ "            FROM rt_matrix_monitored_table\r\n"
			+ "            WHERE s_no <> ?2 GROUP BY s_no\r\n"
			+ "        )\r\n"
			+ "    ),\r\n"
			+ "    FetchSortedValue AS (\r\n"
			+ "        SELECT *\r\n"
			+ "        FROM rt_matrix_monitored_table\r\n"
			+ "        WHERE s_no = ?2 AND report_date = ?1)\r\n"
			+ "    SELECT\r\n"
			+ "        NVL(a.primary_num, b.primary_num)                 AS primary_num,\r\n"
			+ "        NVL(a.s_no, b.s_no)                               AS s_no,\r\n"
			+ "        NVL(a.matrix_label, b.matrix_label)               AS matrix_label,\r\n"
			+ "        NVL(a.guidelines_or_policy, b.guidelines_or_policy) AS guidelines_or_policy,\r\n"
			+ "        NVL(a.limit_assigned, b.limit_assigned)           AS limit_assigned,\r\n"
			+ "        NVL(a.position_of_matrix, b.position_of_matrix)   AS position_of_matrix,\r\n"
			+ "        NVL(a.status, b.status)                           AS status,\r\n"
			+ "        NVL(a.report_date, b.report_date)                 AS report_date\r\n"
			+ "    FROM TakingWithoutSortedValue a\r\n"
			+ "    FULL OUTER JOIN FetchSortedValue b\r\n"
			+ "        ON a.s_no = b.s_no\r\n"
			+ ")\r\n"
			+ "ORDER BY s_no ASC\r\n"
			+ "", nativeQuery = true)
	List<RT_Matrix_monitoring_entity> GetMatrixbysortedvalue(Date Report_date,String Serial_no);
}
