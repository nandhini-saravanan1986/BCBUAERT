package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RT_RWA_Fund_base_data_rep extends JpaRepository<RT_RWA_Fund_base_data_entity, String> {

	@Query(value = "Select Distinct cust_id from brf95_rwa_data_fundbased fetch first 100 rows only", nativeQuery = true)
	List<String> getcustomerdetail();

	@Query(value = "Select Distinct cust_id from brf95_rwa_data_fundbased where cust_id like '%' || ?1|| '%'", nativeQuery = true)
	List<String> Getsortingcustomerlist(String CustTermvalue);

	@Query(value = "With ExposureWise as (Select * from (Select Branch_name,Round(sum(Balance)/1000000,2) as ExposureBal from brf95_rwa_data_fundbased Where Report_date = ?1 \r\n"
			+ "Group by Branch_name) Order by ExposureBal Desc),\r\n"
			+ "OverallBal As(Select Round(sum(Balance)/1000000,2) as Overallexposure from brf95_rwa_data_fundbased Where Report_date = ?1 )\r\n"
			+ "Select Branch_name,ExposureBal,Round(((ExposureBal/Overallexposure)*100),2) as Exposureper from ExposureWise a , OverallBal b", nativeQuery = true)
	List<Object[]> Getbranchwiseportfolio(Date Selecteddate);

	@Query(value = "With ExposureWise as (Select * from (Select Category as Category,Round(sum(Balance)/1000000,2) as ExposureBal from brf95_rwa_data_fundbased Where Report_date = ?1 \r\n"
			+ "	and Branch_name = ?2		Group by Category) Order by ExposureBal Desc),\r\n"
			+ "			OverallBal As(Select Round(sum(Balance)/1000000,2) as Overallexposure from brf95_rwa_data_fundbased Where Report_date = ?1 )\r\n"
			+ "			Select Category,ExposureBal,Round(((ExposureBal/Overallexposure)*100),2) as Exposureper from ExposureWise a , OverallBal b", nativeQuery = true)
	List<Object[]> getcatorywiseportfolio(Date Selecteddate, String Branch_name);

	@Query(value = "Select * from(Select BRANCH_NAME, ACCOUNT_NAME, GL_CODE, CONST_ID, PURPOSE, SCHEME, RWA_CLASS, INT_SUSPENSE, \r\n"
			+ "			TOT_PROVISION, LIMIT, BALANCE, EXPOSURE, RW, RWA, TOTAL_DRAWN_RWA,\r\n"
			+ "			TOTAL_RWA from brf95_rwa_data_fundbased where report_date = ?1 and Category = ?2 and BRANCH_NAME = ?3 Order by BALANCE Desc)fetch first 10 Rows only", nativeQuery = true)
	List<Object[]> Gettopexpposure(Date Selecteddate, String Category, String BRANCH_NAME);

	// Get Top10 Total Exposure and Total RWA by branch
	@Query(value = "Select * from(Select BRANCH_NAME, ACCOUNT_NAME, GL_CODE, CONST_ID, PURPOSE, SCHEME, RWA_CLASS, INT_SUSPENSE, \r\n"
			+ "			TOT_PROVISION, LIMIT, BALANCE, EXPOSURE, RW, RWA, TOTAL_DRAWN_RWA,\r\n"
			+ "			TOTAL_RWA from brf95_rwa_data_fundbased where report_date = ?1 and BRANCH_NAME = ?2 Order by BALANCE Desc)fetch first 10 Rows only", nativeQuery = true)
	List<Object[]> Gettotalexpobranch(Date Selecteddate, String BRANCH_NAME);

	// Get Top10 Standard Exposure
	@Query(value = "Select * from(Select BRANCH_NAME, ACCOUNT_NAME, GL_CODE, CONST_ID, PURPOSE, SCHEME, RWA_CLASS, INT_SUSPENSE, \r\n"
			+ "			TOT_PROVISION, LIMIT, BALANCE, EXPOSURE, RW, RWA, TOTAL_DRAWN_RWA,\r\n"
			+ "			TOTAL_RWA from brf95_rwa_data_fundbased where report_date = ?1 and BRANCH_NAME = ?2 and RWA_CLASS = 'STD' Order by BALANCE Desc)fetch first 10 Rows only", nativeQuery = true)
	List<Object[]> Getactiveexpobranch(Date Selecteddate, String BRANCH_NAME);

	// Get Top10 NPA Exposure
	@Query(value = "Select * from(Select BRANCH_NAME, ACCOUNT_NAME, GL_CODE, CONST_ID, PURPOSE, SCHEME, RWA_CLASS, INT_SUSPENSE, \r\n"
			+ "			TOT_PROVISION, LIMIT, BALANCE, EXPOSURE, RW, RWA, TOTAL_DRAWN_RWA,\r\n"
			+ "			TOTAL_RWA from brf95_rwa_data_fundbased where report_date = ?1 and BRANCH_NAME = ?2 and RWA_CLASS <> 'STD' Order by BALANCE Desc)fetch first 10 Rows only", nativeQuery = true)
	List<Object[]> GetNPAexpobranch(Date Selecteddate, String BRANCH_NAME);

	// Get Top10 NPA Exposure
	@Query(value = "Select * from(Select BRANCH_NAME, ACCOUNT_NAME, GL_CODE, CONST_ID, PURPOSE, SCHEME, RWA_CLASS, INT_SUSPENSE, \r\n"
			+ "			TOT_PROVISION, LIMIT, BALANCE, EXPOSURE, RW, RWA, TOTAL_DRAWN_RWA,\r\n"
			+ "			TOTAL_RWA from brf95_rwa_data_fundbased where report_date = ?1 and BRANCH_NAME = ?2 and To_Number(DPD) <=30 Order by BALANCE Desc)fetch first 10 Rows only", nativeQuery = true)
	List<Object[]> GetWatchlistexpobranch(Date Selecteddate, String BRANCH_NAME);

	// Get Top10 NPA Exposure
	@Query(value = "Select * from(Select BRANCH_NAME, ACCOUNT_NAME, GL_CODE, CONST_ID, PURPOSE, SCHEME, RWA_CLASS, INT_SUSPENSE, \r\n"
			+ "			TOT_PROVISION, LIMIT, BALANCE, EXPOSURE, RW, RWA, TOTAL_DRAWN_RWA,\r\n"
			+ "			TOTAL_RWA from brf95_rwa_data_fundbased where report_date = ?1 and BRANCH_NAME = ?2 and To_Number(DPD)  > 30 Order by BALANCE Desc)fetch first 10 Rows only", nativeQuery = true)
	List<Object[]> GetOverdueexpobranch(Date Selecteddate, String BRANCH_NAME);

	@Query(value = "With Exposuredata as (\r\n"
			+ "Select Branch_name,sum(Total_exposure) as Total_exposure,Nvl(Sum(Total_RWA),0) as Total_RWA,\r\n"
			+ "Round((Nvl(Sum(Total_RWA),0) / sum(Total_exposure)),2)*100 as Branchlevelratio,\r\n"
			+ "sum(NVL(Active_account,0)) as Active_account,Sum(NVL(Active_Exposure,0)) as Active_Exposure,\r\n"
			+ "Sum(NVL(Npa_account,0)) as Npa_account,Sum(NVL(Npa_exposure,0)) as Npa_exposure,\r\n"
			+ "NVL(SUM(Watchlist_Count),0) AS Watchlist_Count,NVL(Sum(Watchlist),0) as Watchlist ,\r\n"
			+ "NVL(SUM(Overdue_Count),0) AS Overdue_Count,NVL(Sum(Overdue),0) as Overdue From(\r\n"
			+ "Select Branch_name,Sum(Balance) Total_exposure,Sum(Total_RWA) as Total_rwa,\r\n"
			+ "case When RWA_CLASS = 'STD' Then Count(Branch_name) End as Active_account,\r\n"
			+ "case When RWA_CLASS = 'STD' Then Sum(Balance) End as Active_Exposure,\r\n"
			+ "case When RWA_CLASS <> 'STD' Then Count(Branch_name) End as Npa_account,\r\n"
			+ "case When RWA_CLASS <> 'STD' Then Sum(Balance) End as Npa_exposure,\r\n"
			+ "Case When To_Number(DPD) <= 30 Then Sum(Balance) End as Watchlist,\r\n"
			+ "Case When To_Number(DPD) <= 30 Then COUNT(Balance) End as Watchlist_Count,\r\n"
			+ "Case When To_Number(DPD) > 30 Then Sum(Balance) End as Overdue,\r\n"
			+ "Case When To_Number(DPD) > 30 Then COUNT(Balance) End as Overdue_Count\r\n"
			+ "from brf95_rwa_data_fundbased Where report_date = ?1  Group by RWA_CLASS,Branch_name,DPD) Group by Branch_name)\r\n"
			+ "Select * from Exposuredata Where Branch_name =  ?2 \r\n" + "", nativeQuery = true)
	List<Object[]> Getbranchportfoliosnap(Date Selecteddate, String BRANCH_NAME);

	/// GetSingleandGroupBorrower Detail -- Tier 1 Capital and Percentage value need
	/// to check and change once the Query id getting finalized
	@Query(value = "\r\n" + "Select * from (\r\n" + "WITH FundBase_Group_Expo AS (\r\n" + "    SELECT CUST_ID,\r\n"
			+ "           ACCOUNT_NAME,\r\n" + "           SUM(BALANCE) AS fb_grp_bal,\r\n"
			+ "           GROUP_NAME,Currency\r\n" + "    FROM brf95_rwa_data_fundbased\r\n"
			+ "    WHERE GROUP_NAME IS NOT NULL\r\n" + "      AND report_date = ?1\r\n"
			+ "    GROUP BY CUST_ID, ACCOUNT_NAME, GROUP_NAME,Currency\r\n" + "),\r\n"
			+ "Non_Fundbase_Group_Expo AS (\r\n" + "    SELECT CUST_ID,\r\n" + "           CUST_NAME,\r\n"
			+ "           SUM(LCBG_BALANCE) AS nfb_grp_bal,\r\n" + "           GROUP_NAME,Currency\r\n"
			+ "    FROM brf95_rwa_data_nonfundbased\r\n" + "    WHERE GROUP_NAME IS NOT NULL\r\n"
			+ "      AND report_date = ?1\r\n" + "    GROUP BY CUST_ID, CUST_NAME, GROUP_NAME,Currency\r\n" + "),\r\n"
			+ "Consolidated_GroupExposure AS (\r\n" + "    SELECT\r\n"
			+ "        COALESCE(a.cust_id, b.cust_id)       AS cust_id,\r\n"
			+ "        COALESCE(a.account_name, b.cust_name) AS customer_name,\r\n"
			+ "        COALESCE(a.group_name, b.group_name)  AS group_name,\r\n"
			+ "        COALESCE(a.Currency, b.Currency)  AS Group_Currency,\r\n"
			+ "        NVL(a.fb_grp_bal, 0) + NVL(b.nfb_grp_bal, 0) AS total_group_exposure\r\n"
			+ "    FROM FundBase_Group_Expo a\r\n" + "    FULL JOIN Non_Fundbase_Group_Expo b\r\n"
			+ "      ON a.group_name = b.group_name\r\n" + "     AND a.cust_id   = b.cust_id\r\n" + "),\r\n"
			+ "FundBase_Single_Expo AS (\r\n" + "    SELECT CUST_ID,\r\n" + "           ACCOUNT_NAME,\r\n"
			+ "           SUM(BALANCE) AS fb_sng_bal,Currency\r\n" + "    FROM brf95_rwa_data_fundbased\r\n"
			+ "    WHERE GROUP_NAME IS NULL\r\n" + "      AND report_date = ?1\r\n"
			+ "    GROUP BY CUST_ID, ACCOUNT_NAME,Currency\r\n" + "),\r\n" + "Non_FundBase_Single_Expo AS (\r\n"
			+ "    SELECT CUST_ID,\r\n" + "           CUST_NAME,\r\n"
			+ "           SUM(LCBG_BALANCE) AS nfb_sng_bal,Currency\r\n" + "    FROM brf95_rwa_data_nonfundbased\r\n"
			+ "    WHERE GROUP_NAME IS NULL\r\n" + "      AND report_date = ?1\r\n"
			+ "    GROUP BY CUST_ID, CUST_NAME,Currency\r\n" + "),\r\n" + "Consolidated_SingleExposure AS (\r\n"
			+ "    SELECT *\r\n" + "    FROM (\r\n" + "        SELECT\r\n"
			+ "            COALESCE(a.cust_id, b.cust_id)        AS cust_id,\r\n"
			+ "            COALESCE(a.account_name, b.cust_name) AS customer_name,\r\n"
			+ "            COALESCE(a.Currency, b.Currency) AS Sng_Currency,\r\n"
			+ "            NVL(a.fb_sng_bal, 0) + NVL(b.nfb_sng_bal, 0) AS total_balance\r\n"
			+ "        FROM FundBase_Single_Expo a\r\n" + "        FULL JOIN Non_FundBase_Single_Expo b\r\n"
			+ "          ON a.cust_id = b.cust_id\r\n" + "    )\r\n" + "    WHERE total_balance / 1000 >= ?2\r\n"
			+ ")\r\n" + "SELECT\r\n" + "    COALESCE(g.cust_id,s.Cust_id)                AS cust_id,\r\n"
			+ "    COALESCE(g.customer_name,s.customer_name)    AS customer_name,\r\n"
			+ "    Case When g.group_name is null then 'Single' Else 'Group' End as Borr_type,\r\n"
			+ "    NVL(g.group_name,'NA')                  AS group_name,\r\n"
			+ "     COALESCE(g.Group_Currency,s.Sng_Currency)    AS Currency,\r\n"
			+ "    NVL(g.total_group_exposure,0)+ NVL(s.total_balance,0) As total_balance,\r\n"
			+ "    Round(((NVL(g.total_group_exposure,0)+ NVL(s.total_balance,0))/1000)/3341355,4)*100 As Percentage\r\n"
			+ "FROM Consolidated_GroupExposure g\r\n" + "FULL JOIN Consolidated_SingleExposure s\r\n"
			+ "  ON g.cust_id = s.cust_id) Order by Borr_type,GROUP_NAME, TOTAL_BALANCE Desc\r\n" + "\r\n"
			+ "", nativeQuery = true)
	List<Object[]> GetGetSingleandGroupBorrower(Date Report_date, String Tier1capital);

	// Get Outside Gcc Exposure detail
	@Query(value = "With Countrywise_exposure as (\r\n"
			+ "Select EXPOSURE_COUNTRY,Sum(Balance) as Country_bal,Sum(Total_RWA) as Country_rwa from brf95_rwa_data_fundbased\r\n"
			+ "Where report_Date = ?1 and UPPER(Trim(EXPOSURE_COUNTRY)) not in ('OMAN','SAUDI ARABIA',\r\n"
			+ "'BAHRAIN','QATAR','KUWAIT','UAE') Group by EXPOSURE_COUNTRY),\r\n"
			+ "Total_Exposure_balance as (Select Sum(Balance) as Total_balance,Sum(Total_rwa) as Total_rwa \r\n"
			+ "from brf95_rwa_data_fundbased where report_date = ?1 )\r\n"
			+ "Select EXPOSURE_COUNTRY,Country_bal,Country_rwa,\r\n"
			+ "Round(Country_bal/Total_balance,4)*100,Round(Country_rwa/Total_rwa,4)*100 \r\n"
			+ "from Countrywise_exposure a , Total_Exposure_balance b  ", nativeQuery = true)
	List<Object[]> GetoutsideGccExposure(Date Selecteddate);

	// Get Gcc Exposure detail
	@Query(value = "With Countrywise_exposure as (\r\n"
			+ "Select EXPOSURE_COUNTRY,Sum(Balance) as Country_bal,Sum(Total_RWA) as Country_rwa from brf95_rwa_data_fundbased\r\n"
			+ "Where report_Date = ?1 and UPPER(Trim(EXPOSURE_COUNTRY)) in ('OMAN','SAUDI ARABIA',\r\n"
			+ "'BAHRAIN','QATAR','KUWAIT','UAE') Group by EXPOSURE_COUNTRY),\r\n"
			+ "Total_Exposure_balance as (Select Sum(Balance) as Total_balance,Sum(Total_rwa) as Total_rwa \r\n"
			+ "from brf95_rwa_data_fundbased where report_date = ?1 )\r\n"
			+ "Select EXPOSURE_COUNTRY,Country_bal,Country_rwa,\r\n"
			+ "Round(Country_bal/Total_balance,4)*100,Round(Country_rwa/Total_rwa,4)*100 \r\n"
			+ "from Countrywise_exposure a , Total_Exposure_balance b  ", nativeQuery = true)
	List<Object[]> GetGccExposure(Date Selecteddate);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM BRF95_RWA_DATA_FUNDBASED WHERE REPORT_DATE = ?1", nativeQuery = true)
	void deleteByReportDate(Date reportDate);

	@Transactional
	@Modifying
	void flush();

	// Get Outside Gcc Exposure detail
	@Query(value = "With TotalFundBase as (Select Sum(Balance) as Total_bal,Sum(Total_rwa) as Total_rwa from brf95_rwa_data_fundbased \r\n"
			+ "				where report_date = ?1),\r\n"
			+ "				Only_gcc_countries as (Select Sum(Balance) as Gcc_Exposure,Sum(Total_rwa) as Gcc_rwa from brf95_rwa_data_fundbased \r\n"
			+ "				where report_date = ?1 and UPPER(Trim(EXPOSURE_COUNTRY)) in ('OMAN','SAUDI ARABIA','BAHRAIN','QATAR','KUWAIT','UAE')),\r\n"
			+ "				Outside_gcc_countries as(Select Sum(Balance) as OutGcc_Exposure,Sum(Total_rwa) as OutGcc_rwa from brf95_rwa_data_fundbased \r\n"
			+ "				where report_date = ?1 and UPPER(Trim(EXPOSURE_COUNTRY)) not in ('OMAN','SAUDI ARABIA','BAHRAIN','QATAR','KUWAIT','UAE'))\r\n"
			+ "				Select Round(c.OutGcc_Exposure/a.Total_bal,4)*100 as OutsideGcc_Exposure,Round(b.Gcc_Exposure/a.Total_bal,4)*100 Gcc_Exposure,\r\n"
			+ "				Round(c.OutGcc_rwa/a.Total_rwa,4)*100 as OutsideGcc_Rwa,Round(b.Gcc_rwa/a.Total_rwa,4)*100 Gcc_Rwa\r\n"
			+ "				from TotalFundBase a , Only_gcc_countries b ,Outside_gcc_countries c", nativeQuery = true)
	List<Object[]> CountryExpSummary(Date Selecteddate);

	// Fresh Slippage data Fetching
	@Query(value = "With Total_balance_of_last_year as(Select LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-13)) as Tot_bal_rep_date,\r\n"
			+ "Nvl(Sum(Balance),1) Total_bal from brf95_rwa_data_fundbased where report_date = LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-13))),\r\n"
			+ "First_Quater_Npa as(\r\n"
			+ "Select LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-10)) as First_Qua_Rep_date,Nvl(Sum(Balance),0) First_bal \r\n"
			+ "from brf95_rwa_data_fundbased Where Report_date = LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-10))\r\n"
			+ "and Rwa_class not in ('STD') and NPA_DATE Between LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-13))+1 and \r\n"
			+ "LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-10))),\r\n" + "Second_Quater_Npa as(\r\n"
			+ "Select LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-7)) As Second_Qua_Rep_date,Nvl(Sum(Balance),0) Second_bal\r\n"
			+ "from brf95_rwa_data_fundbased Where Report_date = LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-7))\r\n"
			+ "and Rwa_class not in ('STD') and NPA_DATE Between LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-10))+1 and \r\n"
			+ "LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-7))),\r\n" + "Third_Quater as (\r\n"
			+ "Select LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-4)) As Third_Qua_Rep_date,Nvl(Sum(Balance),0) Third_bal \r\n"
			+ "from brf95_rwa_data_fundbased Where Report_date = LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-4))\r\n"
			+ "and Rwa_class not in ('STD') and NPA_DATE Between LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-7))+1 and \r\n"
			+ "LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-4))),\r\n" + "Final_quater as (\r\n"
			+ "Select LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-1)) As Final_Qua_Rep_date,Nvl(Sum(Balance),0) Final_bal \r\n"
			+ "from brf95_rwa_data_fundbased Where Report_date = LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-1))\r\n"
			+ "and Rwa_class not in ('STD') and NPA_DATE Between LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-4))+1 and \r\n"
			+ "LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'Q'),-1)))\r\n"
			+ "Select Round((e.final_bal+d.third_bal+c.second_bal+b.first_bal)/Total_bal,4)*100 Current_Ratio,\r\n"
			+ "To_char(a.Tot_bal_rep_date,'Mon - YYYY'),a.Total_bal,\r\n"
			+ "To_char(b.First_Qua_Rep_date,'Mon - YYYY'),b.First_bal,\r\n"
			+ "To_char(c.Second_Qua_Rep_date,'Mon - YYYY'),c.Second_bal,\r\n"
			+ "To_char(d.Third_Qua_Rep_date,'Mon - YYYY'),d.Third_bal,\r\n"
			+ "To_char(e.Final_Qua_Rep_date,'Mon - YYYY'),e.Final_bal \r\n"
			+ "from Total_balance_of_last_year a,First_Quater_Npa b,Second_Quater_Npa c,Third_Quater d,Final_quater e\r\n"
			+ "\r\n" + "", nativeQuery = true)
	List<Object[]> Freshslippage();

	// Sector Calculation
	@Query(value = "With Industrial_asset As (Select ABS(SUM(ACT_BALANCE_AMT_LC)) As Sector_balance,sector_classification \r\n"
			+ "from BRF_MIS_FUND_BASED_ADVANCES Where report_date = ?1 and sector_classification = 'Industry' \r\n"
			+ "Group by sector_classification),\r\n"
			+ "Current_Month_bala as (Select ABS(SUM(ACT_BALANCE_AMT_LC)) as Total_assets from BRF_MIS_FUND_BASED_ADVANCES Where \r\n"
			+ "report_date = ?1)\r\n"
			+ "Select Sector_balance,Total_assets,Round(Sector_balance/Total_assets,4)*100 as Percentage\r\n"
			+ "from Industrial_asset a,Current_Month_bala b", nativeQuery = true)
	List<Object[]> Industry_Classifi(Date Selecteddate);

	@Query(value = "With Industrial_asset As (Select ABS(SUM(ACT_BALANCE_AMT_LC)) As Sector_balance,sector_classification \r\n"
			+ "from BRF_MIS_FUND_BASED_ADVANCES Where report_date = ?1 and sector_classification = 'Trading' \r\n"
			+ "Group by sector_classification),\r\n"
			+ "Current_Month_bala as (Select ABS(SUM(ACT_BALANCE_AMT_LC)) as Total_assets from BRF_MIS_FUND_BASED_ADVANCES Where \r\n"
			+ "report_date = ?1)\r\n"
			+ "Select Sector_balance,Total_assets,Round(Sector_balance/Total_assets,4)*100 as Percentage\r\n"
			+ "from Industrial_asset a,Current_Month_bala b", nativeQuery = true)
	List<Object[]> Trading_Classifi(Date Selecteddate);

	@Query(value = "With Industrial_asset As (Select ABS(SUM(ACT_BALANCE_AMT_LC)) As Sector_balance,sector_classification \r\n"
			+ "from BRF_MIS_FUND_BASED_ADVANCES Where report_date = ?1 and sector_classification = 'Services' \r\n"
			+ "Group by sector_classification),\r\n"
			+ "Current_Month_bala as (Select ABS(SUM(ACT_BALANCE_AMT_LC)) as Total_assets from BRF_MIS_FUND_BASED_ADVANCES Where \r\n"
			+ "report_date = ?1)\r\n"
			+ "Select Sector_balance,Total_assets,Round(Sector_balance/Total_assets,4)*100 as Percentage\r\n"
			+ "from Industrial_asset a,Current_Month_bala b", nativeQuery = true)
	List<Object[]> Services_Classifi(Date Selecteddate);

	@Query(value = "With Industrial_asset As (Select ABS(SUM(ACT_BALANCE_AMT_LC)) As Sector_balance,sector_classification \r\n"
			+ "from BRF_MIS_FUND_BASED_ADVANCES Where report_date = ?1 and sector_classification = 'Banks' \r\n"
			+ "Group by sector_classification),\r\n"
			+ "Current_Month_bala as (Select ABS(SUM(ACT_BALANCE_AMT_LC)) as Total_assets from BRF_MIS_FUND_BASED_ADVANCES Where \r\n"
			+ "report_date = ?1)\r\n"
			+ "Select Sector_balance,Total_assets,Round(Sector_balance/Total_assets,4)*100 as Percentage\r\n"
			+ "from Industrial_asset a,Current_Month_bala b", nativeQuery = true)
	List<Object[]> Banks_Classifi(Date Selecteddate);

	@Query(value = "With Industrial_asset As (Select ABS(SUM(ACT_BALANCE_AMT_LC)) As Sector_balance,sector_classification \r\n"
			+ "from BRF_MIS_FUND_BASED_ADVANCES Where report_date = ?1 and sector_classification = 'Real Estate' \r\n"
			+ "Group by sector_classification),\r\n"
			+ "Current_Month_bala as (Select ABS(SUM(ACT_BALANCE_AMT_LC)) as Total_assets from BRF_MIS_FUND_BASED_ADVANCES Where \r\n"
			+ "report_date = ?1)\r\n"
			+ "Select Sector_balance,Total_assets,Round(Sector_balance/Total_assets,4)*100 as Percentage\r\n"
			+ "from Industrial_asset a,Current_Month_bala b", nativeQuery = true)
	List<Object[]> RealEstate_Classifi(Date Selecteddate);

	@Query(value = "With Industrial_asset As (Select ABS(SUM(ACT_BALANCE_AMT_LC)) As Sector_balance\r\n"
			+ "from BRF_MIS_FUND_BASED_ADVANCES Where report_date = ?1 and sector_classification not in (\r\n"
			+ "'Banks','Industry','Real Estate','Services','Trading')),\r\n"
			+ "Current_Month_bala as (Select ABS(SUM(ACT_BALANCE_AMT_LC)) as Total_assets from BRF_MIS_FUND_BASED_ADVANCES Where \r\n"
			+ "report_date = ?1)\r\n"
			+ "Select Sector_balance,Total_assets,Round(Sector_balance/Total_assets,4)*100 as Percentage\r\n"
			+ "from Industrial_asset a,Current_Month_bala b", nativeQuery = true)
	List<Object[]> Others_Classifi(Date Selecteddate);

	// Real Estate Concentration
	@Query(value = "With DateWise_Real_Estate_RWA as(Select Report_date,Nvl(sum(total_rwa),0) as Real_estate_total_rwa  from brf95_rwa_data_fundbased where \r\n"
			+ "is_acct_real_estate_exp = 'Y' Group by Report_date),\r\n" + "Date_wise_total_rwa as (\r\n"
			+ "Select Report_date,Sum(Total_rwa) as Total_rwa from brf95_rwa_data_fundbased Group by Report_date),\r\n"
			+ "DateWise_Percen as (Select a.report_date as Report_date,a.Real_estate_total_rwa/b.Total_rwa as Real_Estate_perce\r\n"
			+ "from DateWise_Real_Estate_RWA a ,Date_wise_total_rwa b \r\n"
			+ "where a.report_date = b.report_date),\r\n"
			+ "Current_Year_dates as(SELECT LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'YEAR'), LEVEL - 1))\r\n"
			+ "AS month_end FROM dual CONNECT BY LEVEL <= 12 )\r\n"
			+ "Select To_char(d.month_end,'DD-Mon-YYYY'),Nvl(Round(c.REAL_ESTATE_PERCE,4)*100,0) from Current_Year_dates d left join \r\n"
			+ "DateWise_Percen c on d.month_end = c.Report_date Order by d.month_end asc", nativeQuery = true)
	List<Object[]> GetCurrentyear_realestate_concen_per();

	@Query(value = "With DateWise_Real_Estate_RWA as(Select Report_date,Nvl(sum(total_rwa),0) as Real_estate_total_rwa  from brf95_rwa_data_fundbased where \r\n"
			+ "is_acct_real_estate_exp = 'Y' Group by Report_date),\r\n" + "Date_wise_total_rwa as (\r\n"
			+ "Select Report_date,Sum(Total_rwa) as Total_rwa from brf95_rwa_data_fundbased Group by Report_date),\r\n"
			+ "DateWise_Percen as (Select a.report_date as Report_date,a.Real_estate_total_rwa/b.Total_rwa as Real_Estate_perce\r\n"
			+ "from DateWise_Real_Estate_RWA a ,Date_wise_total_rwa b \r\n"
			+ "where a.report_date = b.report_date),\r\n"
			+ "Current_month_dates as(SELECT TRUNC(SYSDATE, 'MM') + (LEVEL - 1) AS month_dates FROM dual\r\n"
			+ "CONNECT BY TRUNC(SYSDATE, 'MM') + (LEVEL - 1) <= LAST_DAY(SYSDATE))\r\n"
			+ "Select To_char(d.month_dates,'DD-Mon-YYYY'),Nvl(Round(c.REAL_ESTATE_PERCE,4)*100,0) from Current_month_dates d left join  \r\n"
			+ "DateWise_Percen c on d.month_dates = c.Report_date order by d.month_dates asc", nativeQuery = true)
	List<Object[]> GetCurrentMonth_realestate_concen_per();

	@Query(value = "With Fundbase_Provision_data as(Select Report_date,Sum(balance) as Fundbalance,Sum(Int_suspense) as FundIntSuspen,\r\n"
			+ "Sum(TOT_PROVISION) as TOT_PROVISION from brf95_rwa_data_fundbased where rwa_class <> 'STD' Group by Report_date),\r\n"
			+ "NonFundbase_provision as (Select Report_date,Sum(LCBG_BALANCE) as NfbBalance from brf95_rwa_data_nonfundbased\r\n"
			+ "Where class <> 'STD' Group by Report_date),\r\n"
			+ "Provision_Cover as (Select a.Report_date as Report_date,Round(TOT_PROVISION/((a.Fundbalance+NfbBalance)-FundIntSuspen),4)*100 as Prov_Coverage\r\n"
			+ "from Fundbase_Provision_data a ,NonFundbase_provision b where a.Report_date = b.Report_date),\r\n"
			+ "MonthWise_Prov_Cov as(Select * from Provision_Cover),\r\n"
			+ "Current_Year_dates as(SELECT LAST_DAY(ADD_MONTHS(TRUNC(SYSDATE, 'YEAR'), LEVEL - 1))\r\n"
			+ "AS month_end FROM dual CONNECT BY LEVEL <= 12 )\r\n"
			+ "Select To_char(a.Month_end,'DD-Mon-YYYY'),Nvl(Prov_Coverage,0) from Current_Year_dates a left join Provision_Cover b on a.month_end = b.Report_date\r\n"
			+ "Order by month_end Asc", nativeQuery = true)
	List<Object[]> GetCurrentyear_prov_cover();

	@Query(value = "With Fundbase_Provision_data as(Select Report_date,Sum(balance) as Fundbalance,Sum(Int_suspense) as FundIntSuspen,\r\n"
			+ "Sum(TOT_PROVISION) as TOT_PROVISION from brf95_rwa_data_fundbased where rwa_class <> 'STD' Group by Report_date),\r\n"
			+ "NonFundbase_provision as (Select Report_date,Sum(LCBG_BALANCE) as NfbBalance from brf95_rwa_data_nonfundbased\r\n"
			+ "Where class <> 'STD' Group by Report_date),\r\n"
			+ "Provision_Cover as (Select a.Report_date as Report_date,Round(TOT_PROVISION/((a.Fundbalance+NfbBalance)-FundIntSuspen),4)*100 as Prov_Coverage\r\n"
			+ "from Fundbase_Provision_data a ,NonFundbase_provision b where a.Report_date = b.Report_date),\r\n"
			+ "MonthWise_Prov_Cov as(Select * from Provision_Cover),\r\n"
			+ "Current_month_dates as(SELECT TRUNC(SYSDATE, 'MM') + (LEVEL - 1) AS month_dates FROM dual\r\n"
			+ "CONNECT BY TRUNC(SYSDATE, 'MM') + (LEVEL - 1) <= LAST_DAY(SYSDATE))\r\n"
			+ "Select To_char(a.month_dates,'DD-Mon-YYYY'),Nvl(Prov_Coverage,0) from Current_month_dates a \r\n"
			+ "left join Provision_Cover b on a.month_dates = b.Report_date\r\n"
			+ "Order by month_dates Asc", nativeQuery = true)
	List<Object[]> GetCurrentmonth_prov_cover();

}
