package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_Mis_Fund_Based_Adv_Rep extends JpaRepository<RT_Mis_Fund_based_Advances_entity, RT_Mis_Fund_based_Advances_Id_Class> {
	
	@Query(value="With Top_up_loan_exp As(Select SCHM_CODE,ABS(Sum(ACT_BALANCE_AMT_LC)) as Mortgage_Loans_Exp from BRF_MIS_FUND_BASED_ADVANCES\r\n"
			+ "								Where report_date = ?1 and SCHM_CODE in ('LA110') group by SCHM_CODE),\r\n"
			+ "								Housing_loan_exp As(Select SCHM_CODE,ABS(Sum(ACT_BALANCE_AMT_LC)) as Mortgage_Loans_Exp from BRF_MIS_FUND_BASED_ADVANCES\r\n"
			+ "								Where report_date = ?1 and SCHM_CODE in ('LA106') group by SCHM_CODE),\r\n"
			+ "								Total_exposure as (Select ABS(Sum(ACT_BALANCE_AMT_LC)) as Total_Exp from BRF_MIS_FUND_BASED_ADVANCES where report_date = ?1)\r\n"
			+ "								Select b.Mortgage_Loans_Exp as Housing_loan_exp,a.Mortgage_Loans_Exp as Top_up_loan_exp,\r\n"
			+ "				                (b.Mortgage_Loans_Exp+a.Mortgage_Loans_Exp) Total_mortgageloan,\r\n"
			+ "				                Round(( (b.Mortgage_Loans_Exp+a.Mortgage_Loans_Exp) / c.Total_Exp),4)*100 as Top_up_loan_per \r\n"
			+ "								from Top_up_loan_exp a,Housing_loan_exp b,Total_exposure c",nativeQuery=true)
	List<Object[]> GetMortageloanappetite(Date Selecteddate);
	
	@Query(value="Select CUST_ID, acct_name, HOSUINGLOAN_EXP, Round((HOSUINGLOAN_EXP/TOTAL_BALANCE),4)*100 as Percentage from (\r\n"
			+ "			With Total_Balance_val as (Select abs(Sum(act_balance_amt_lc)) As Total_balance from BRF_MIS_FUND_BASED_ADVANCES where report_date = ?1  )\r\n"
			+ "			Select CUST_ID, acct_name, abs(Sum(act_balance_amt_lc)) as Hosuingloan_exp,b.Total_balance as  from BRF_MIS_FUND_BASED_ADVANCES a, Total_balance_val b\r\n"
			+ "			where report_date = ?1  AND schm_code in ('LA106') Group by CUST_ID, acct_name,Total_balance\r\n"
			+ "			)Order by Hosuingloan_exp desc Fetch first 10 Rows only",nativeQuery=true)
	List<Object[]> GetHousingloantoptencust(Date Selecteddate);
	
	@Query(value="Select CUST_ID, acct_name, HOSUINGLOAN_EXP, Round((HOSUINGLOAN_EXP/TOTAL_BALANCE),4)*100 as Percentage from (\r\n"
			+ "			With Total_Balance_val as (Select abs(Sum(act_balance_amt_lc)) As Total_balance from BRF_MIS_FUND_BASED_ADVANCES where report_date = ?1  )\r\n"
			+ "			Select CUST_ID, acct_name, abs(Sum(act_balance_amt_lc)) as Hosuingloan_exp,b.Total_balance as  from BRF_MIS_FUND_BASED_ADVANCES a, Total_balance_val b\r\n"
			+ "			where report_date = ?1  AND schm_code in ('LA110') Group by CUST_ID, acct_name,Total_balance\r\n"
			+ "			)Order by Hosuingloan_exp desc Fetch first 10 Rows only",nativeQuery=true)
	List<Object[]> GetTopuploantoptencust(Date Selecteddate);

	@Query(value="Select CUST_ID, acct_name, HOSUINGLOAN_EXP, Round((HOSUINGLOAN_EXP/TOTAL_BALANCE),4)*100 as Percentage from (\r\n"
			+ "With Total_Balance_val as (Select abs(Sum(act_balance_amt_lc)) As Total_balance from BRF_MIS_FUND_BASED_ADVANCES where report_date = ?1  )\r\n"
			+ "Select CUST_ID, acct_name, abs(Sum(act_balance_amt_lc)) as Hosuingloan_exp,b.Total_balance as  from BRF_MIS_FUND_BASED_ADVANCES a, Total_balance_val b\r\n"
			+ "where report_date = ?1  AND schm_code in ('LA110','LA106') Group by CUST_ID, acct_name,Total_balance\r\n"
			+ ")Order by Hosuingloan_exp desc Fetch first 10 Rows only",nativeQuery=true)
	List<Object[]> GetMortgageloantencust(Date Selecteddate);
	
	
	// Get Data for Graph 
	// Monthly
	@Query(value="Select To_char(month_end,'DD-MM-YYYY') as month_end,Percentage from(\r\n"
			+ "With Total_balanceP_fb as (Select Report_date,abs(sum(act_balance_amt_lc)) as Tot_bal from BRF_MIS_FUND_BASED_ADVANCES \r\n"
			+ "Where report_date in (SELECT LAST_DAY(ADD_MONTHS(TRUNC(?1, 'YEAR'), LEVEL - 1))\r\n"
			+ "AS month_end FROM dual CONNECT BY LEVEL <= 12 )Group by Report_date),\r\n"
			+ "Mortgage_loan as (Select Report_date,abs(sum(act_balance_amt_lc)) as Mortgage_bal from BRF_MIS_FUND_BASED_ADVANCES \r\n"
			+ "where schm_code in('LA106','LA110') and report_date in (SELECT LAST_DAY(ADD_MONTHS(TRUNC(?1, 'YEAR'), LEVEL - 1))\r\n"
			+ "AS month_end FROM dual CONNECT BY LEVEL <= 12 ) Group by Report_Date),\r\n"
			+ "Mortgage_loan_cal as( Select a.Report_date as Report_date,Round(b.Mortgage_bal/a.Tot_bal,4)*100 as Percentage\r\n"
			+ "from Total_balanceP_fb a left join Mortgage_loan b on a.report_date = b.Report_date Order by a.report_date asc),\r\n"
			+ "Current_year_Dates as (SELECT LAST_DAY(ADD_MONTHS(TRUNC(?1, 'YEAR'), LEVEL - 1))\r\n"
			+ "AS month_end FROM dual CONNECT BY LEVEL <= 12 ) \r\n"
			+ "Select a.month_end as month_end,Nvl(Percentage,0) as Percentage\r\n"
			+ "from Current_year_Dates a left join Mortgage_loan_cal b on a.month_end = b.Report_date Order by a.month_end Asc) ",nativeQuery=true)
	List<Object[]> GetMortgageratio_curryear_report(Date Selecteddate);
	
	
	
	//Daily Query
	@Query(value="Select To_char(month_dates,'DD-MM-YYYY') as month_dates,Percentage from(\r\n"
			+ "With Total_balanceP_fb as (Select Report_date,abs(sum(act_balance_amt_lc)) as Tot_bal from BRF_MIS_FUND_BASED_ADVANCES \r\n"
			+ "Where report_date in (SELECT TRUNC(?1, 'MM') + (LEVEL - 1) AS month_dates FROM dual\r\n"
			+ "CONNECT BY TRUNC(?1, 'MM') + (LEVEL - 1) <= LAST_DAY(?1)) Group by Report_date),\r\n"
			+ "Mortgage_loan as (Select Report_date,abs(sum(act_balance_amt_lc)) as Mortgage_bal from BRF_MIS_FUND_BASED_ADVANCES \r\n"
			+ "where schm_code in('LA106','LA110') and report_date in (SELECT TRUNC(?1, 'MM') + (LEVEL - 1) AS month_dates FROM dual\r\n"
			+ "CONNECT BY TRUNC(?1, 'MM') + (LEVEL - 1) <= LAST_DAY(?1)) Group by Report_Date),\r\n"
			+ "Mortgage_loan_cal as( Select a.Report_date as Report_date,Round(b.Mortgage_bal/a.Tot_bal,4)*100 as Percentage\r\n"
			+ "from Total_balanceP_fb a left join Mortgage_loan b on a.report_date = b.Report_date Order by a.report_date asc),\r\n"
			+ "Current_Month as (SELECT TRUNC(?1, 'MM') + (LEVEL - 1) AS month_dates FROM dual\r\n"
			+ "CONNECT BY TRUNC(?1, 'MM') + (LEVEL - 1) <= LAST_DAY(?1)) \r\n"
			+ "Select a.month_dates as month_dates,Nvl(Percentage,0) as Percentage\r\n"
			+ "from Current_Month a left join Mortgage_loan_cal b on a.month_dates = b.Report_date Order by a.month_dates Asc)",nativeQuery=true)
	List<Object[]> GetMortgageratio_currentmonthgraph(Date Selecteddate);
	
}
