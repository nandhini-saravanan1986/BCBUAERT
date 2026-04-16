package com.bornfire.xbrl.entities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_MmDataRepository extends JpaRepository<RT_MmData,  mc_mmreportid> {
	// Add custom queries if needed

	@Query(value = "select * from BCBUAE_MM_DATA where REPORT_DATE=?1", nativeQuery = true)
	List<RT_MmData> getlist(Date REPORT_DATE);

	/*
	 * @Query(value = "SELECT * FROM BCBUAE_MM_DATA ", nativeQuery = true)
	 * List<RT_MmData> getfxriskdatalistdata();
	 */

	@Query(value = "SELECT * FROM BCBUAE_MM_DATA ", nativeQuery = true)
	List<Object[]> getmmdatalistdata1();
	
	
	@Query(value = "SELECT * FROM BCBUAE_MM_DATA where deal_no =?1 ", nativeQuery = true)
	RT_MmData getParticularDataBySI_NO(String deal_no);
	
	
	@Query(value = "SELECT TRUNC(MAX(REPORT_DATE)) FROM BCBUAE_MM_DATA", nativeQuery = true)
	Timestamp findLastReportDate();
	
	@Query(value = "SELECT TRUNC(MAX(REPORT_DATE)) FROM BCBUAE_MM_DATA WHERE REPORT_DATE < (SELECT MAX(REPORT_DATE) FROM BCBUAE_MM_DATA)", nativeQuery = true)
	Timestamp findSecondLastReportDate();
}
