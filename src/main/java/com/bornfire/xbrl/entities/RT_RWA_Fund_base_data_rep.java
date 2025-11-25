package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_RWA_Fund_base_data_rep extends JpaRepository<RT_RWA_Fund_base_data_entity,String> {
	
	@Query(value="With ExposureWise as (Select * from (Select Branch_name,Round(sum(Balance)/1000000,2) as ExposureBal from brf95_rwa_data_fundbased Where Report_date = ?1 \r\n"
			+ "Group by Branch_name) Order by ExposureBal Desc),\r\n"
			+ "OverallBal As(Select Round(sum(Balance)/1000000,2) as Overallexposure from brf95_rwa_data_fundbased Where Report_date = ?1 )\r\n"
			+ "Select Branch_name,ExposureBal,Round(((ExposureBal/Overallexposure)*100),2) as Exposureper from ExposureWise a , OverallBal b",nativeQuery=true)
	List<Object[]> Getbranchwiseportfolio(Date Selecteddate);
	
	@Query(value="With ExposureWise as (Select * from (Select Category as Category,Round(sum(Balance)/1000000,2) as ExposureBal from brf95_rwa_data_fundbased Where Report_date = ?1 \r\n"
			+ "	and Branch_name = ?2		Group by Category) Order by ExposureBal Desc),\r\n"
			+ "			OverallBal As(Select Round(sum(Balance)/1000000,2) as Overallexposure from brf95_rwa_data_fundbased Where Report_date = ?1 )\r\n"
			+ "			Select Category,ExposureBal,Round(((ExposureBal/Overallexposure)*100),2) as Exposureper from ExposureWise a , OverallBal b",nativeQuery=true)
	List<Object[]> getcatorywiseportfolio(Date Selecteddate,String Branch_name);
	
	@Query(value="Select * from(Select BRANCH_NAME, ACCOUNT_NAME, GL_CODE, CONST_ID, PURPOSE, SCHEME, RWA_CLASS, INT_SUSPENSE, \r\n"
			+ "			TOT_PROVISION, LIMIT, BALANCE, EXPOSURE, RW, RWA, TOTAL_DRAWN_RWA,\r\n"
			+ "			TOTAL_RWA from brf95_rwa_data_fundbased where report_date = ?1 and Category = ?2 and BRANCH_NAME = ?3 Order by BALANCE Desc)fetch first 10 Rows only",nativeQuery=true)
	List<Object[]> Gettopexpposure(Date Selecteddate,String Category,String BRANCH_NAME);
	
	
	//Get Top10 Total Exposure and Total RWA by branch
	@Query(value="Select * from(Select BRANCH_NAME, ACCOUNT_NAME, GL_CODE, CONST_ID, PURPOSE, SCHEME, RWA_CLASS, INT_SUSPENSE, \r\n"
			+ "			TOT_PROVISION, LIMIT, BALANCE, EXPOSURE, RW, RWA, TOTAL_DRAWN_RWA,\r\n"
			+ "			TOTAL_RWA from brf95_rwa_data_fundbased where report_date = ?1 and BRANCH_NAME = ?2 Order by BALANCE Desc)fetch first 10 Rows only",nativeQuery=true)
	List<Object[]> Gettotalexpobranch(Date Selecteddate,String BRANCH_NAME);
	
	//Get Top10 Standard Exposure
	@Query(value="Select * from(Select BRANCH_NAME, ACCOUNT_NAME, GL_CODE, CONST_ID, PURPOSE, SCHEME, RWA_CLASS, INT_SUSPENSE, \r\n"
			+ "			TOT_PROVISION, LIMIT, BALANCE, EXPOSURE, RW, RWA, TOTAL_DRAWN_RWA,\r\n"
			+ "			TOTAL_RWA from brf95_rwa_data_fundbased where report_date = ?1 and BRANCH_NAME = ?2 and RWA_CLASS = 'STD' Order by BALANCE Desc)fetch first 10 Rows only",nativeQuery=true)
	List<Object[]> Getactiveexpobranch(Date Selecteddate,String BRANCH_NAME);
	
	//Get Top10 NPA Exposure
		@Query(value="Select * from(Select BRANCH_NAME, ACCOUNT_NAME, GL_CODE, CONST_ID, PURPOSE, SCHEME, RWA_CLASS, INT_SUSPENSE, \r\n"
				+ "			TOT_PROVISION, LIMIT, BALANCE, EXPOSURE, RW, RWA, TOTAL_DRAWN_RWA,\r\n"
				+ "			TOTAL_RWA from brf95_rwa_data_fundbased where report_date = ?1 and BRANCH_NAME = ?2 and RWA_CLASS <> 'STD' Order by BALANCE Desc)fetch first 10 Rows only",nativeQuery=true)
	List<Object[]> GetNPAexpobranch(Date Selecteddate,String BRANCH_NAME);
	
	//Get Top10 NPA Exposure
	@Query(value="Select * from(Select BRANCH_NAME, ACCOUNT_NAME, GL_CODE, CONST_ID, PURPOSE, SCHEME, RWA_CLASS, INT_SUSPENSE, \r\n"
					+ "			TOT_PROVISION, LIMIT, BALANCE, EXPOSURE, RW, RWA, TOTAL_DRAWN_RWA,\r\n"
					+ "			TOTAL_RWA from brf95_rwa_data_fundbased where report_date = ?1 and BRANCH_NAME = ?2 and To_Number(DPD) <=30 Order by BALANCE Desc)fetch first 10 Rows only",nativeQuery=true)
	List<Object[]> GetWatchlistexpobranch(Date Selecteddate,String BRANCH_NAME);
	
	//Get Top10 NPA Exposure
	@Query(value="Select * from(Select BRANCH_NAME, ACCOUNT_NAME, GL_CODE, CONST_ID, PURPOSE, SCHEME, RWA_CLASS, INT_SUSPENSE, \r\n"
						+ "			TOT_PROVISION, LIMIT, BALANCE, EXPOSURE, RW, RWA, TOTAL_DRAWN_RWA,\r\n"
						+ "			TOTAL_RWA from brf95_rwa_data_fundbased where report_date = ?1 and BRANCH_NAME = ?2 and To_Number(DPD)  > 30 Order by BALANCE Desc)fetch first 10 Rows only",nativeQuery=true)
	List<Object[]> GetOverdueexpobranch(Date Selecteddate,String BRANCH_NAME);
	
	@Query(value="With Exposuredata as (\r\n"
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
			+ "Select * from Exposuredata Where Branch_name =  ?2 \r\n"
			+ "",nativeQuery=true)
	List<Object[]> Getbranchportfoliosnap(Date Selecteddate,String BRANCH_NAME);
}
