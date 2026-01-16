package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_Noop_net_position_summ_rep extends JpaRepository<RT_Noop_net_position_summ_entity, Date> {

	@Query(value="Select REPORT_DATE,OVER_BOUGHT_AMOUNT,OVER_SOLD_AMOUNT,"
			+ "NOOP_AMOUNT_IN_INR,LIMIT_AMOUNT_IN_INR,HEADROOM_AVAL_AMOUNT_IN_INR"
			+ " from rt_net_position_limit_noop_summ where report_date =?1",nativeQuery=true)
	List<Object[]> Get_Noop_netposition(Date Selecteddate);
	
	@Query(value="With Available_head_room as (Select Report_date,Round(HEADROOM_AVAL_AMOUNT_IN_INR/10000000,2) as HEADROOM_AVAL_AMOUNT_IN_INR\r\n"
			+ "from rt_net_position_limit_noop_summ where report_date =?1),\r\n"
			+ "Current_year_dates as (SELECT LAST_DAY(ADD_MONTHS(TRUNC(?1, 'YEAR'), LEVEL - 1))\r\n"
			+ "AS month_end FROM dual CONNECT BY LEVEL <= 12 )\r\n"
			+ "Select To_char(a.month_end,'DD-MM-YYYY'),Nvl(HEADROOM_AVAL_AMOUNT_IN_INR,0) from \r\n"
			+ "Current_year_dates a left join Available_head_room b on a.month_end = b.Report_date Order by a.month_end asc",nativeQuery=true)
	List<Object[]> GetCurrentYear_NoopGraph(Date Selecteddate);
	
	@Query(value="With Available_head_room as (Select Report_date,Round(HEADROOM_AVAL_AMOUNT_IN_INR/10000000,2) as HEADROOM_AVAL_AMOUNT_IN_INR\r\n"
			+ "from rt_net_position_limit_noop_summ where report_date = ?1),\r\n"
			+ "Currentmonth as (SELECT TRUNC(?1, 'MM') + (LEVEL - 1) AS month_dates FROM dual\r\n"
			+ "CONNECT BY TRUNC(?1, 'MM') + (LEVEL - 1) <= LAST_DAY(?1))\r\n"
			+ "Select To_char(a.month_dates,'DD-MM-YYYY'),Nvl(HEADROOM_AVAL_AMOUNT_IN_INR,0) from \r\n"
			+ "Currentmonth a left join Available_head_room b on a.month_dates = b.Report_date Order by a.month_dates asc",nativeQuery=true)
	List<Object[]> GetCurrentMonth_NoopGraph(Date Selecteddate);
	
}
