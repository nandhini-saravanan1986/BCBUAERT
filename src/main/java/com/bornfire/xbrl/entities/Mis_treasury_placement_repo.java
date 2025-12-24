package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Mis_treasury_placement_repo extends JpaRepository<MIS_TREASURY_PLACEMENT_ENTITY,String > {

	@Query(value="Select * from MIS_TREASURY_PLACEMENT where report_date =?1",nativeQuery=true)
	List<MIS_TREASURY_PLACEMENT_ENTITY> Gettreasurydata(Date Report_date);
	
	@Query(value = "SELECT * FROM MIS_TREASURY_PLACEMENT", nativeQuery = true)
    List<MIS_TREASURY_PLACEMENT_ENTITY> findall();
    
    @Modifying
	@Query("DELETE FROM MIS_TREASURY_PLACEMENT_ENTITY e WHERE e.report_date = :report_date AND e.branch_name = :branch_name")
	void deleteByReportDate(@Param("report_date") Date report_date, @Param("branch_name") String branchName);
    
    @Query(value = "SELECT * FROM MIS_TREASURY_PLACEMENT WHERE REPORT_DATE = ?1", nativeQuery = true)
    List<MIS_TREASURY_PLACEMENT_ENTITY> getAllListByDate(Date report_date);
    
    @Query(value = "SELECT * FROM MIS_TREASURY_PLACEMENT WHERE REPORT_DATE = ?1 AND BRANCH_CODE = ?2 ORDER BY NUM_OPERATION", nativeQuery = true)
    List<MIS_TREASURY_PLACEMENT_ENTITY> getByReportDateAndBR(Date report_date, String branch_code);
	
}
