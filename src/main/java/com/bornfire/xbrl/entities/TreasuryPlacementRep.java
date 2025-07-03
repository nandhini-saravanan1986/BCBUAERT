package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TreasuryPlacementRep extends JpaRepository<MIS_TREASURY_PLACEMENT_ENTITY, String> {
	

    @Query(value = "SELECT * FROM MIS_TREASURY_PLACEMENT", nativeQuery = true)
    List<MIS_TREASURY_PLACEMENT_ENTITY> findall();
	

	
	@Modifying
	@Query("DELETE FROM MIS_TREASURY_PLACEMENT_ENTITY e WHERE e.reportDate = :reportDate AND e.branchName = :branchName")
	void deleteByReportDate(@Param("reportDate") Date reportDate, @Param("branchName") String branchName);

	
@Query(value = "SELECT * FROM MIS_TREASURY_PLACEMENT WHERE REPORT_DATE = ?1", nativeQuery = true)
List<MIS_TREASURY_PLACEMENT_ENTITY> getAllListByDate(Date reportDate);

@Query(value = "SELECT * FROM MIS_TREASURY_PLACEMENT WHERE REPORT_DATE = ?1 AND BRANCH_CODE = ?2 ORDER BY NUM_OPERATION", nativeQuery = true)
List<MIS_TREASURY_PLACEMENT_ENTITY> getByReportDateAndBR(Date reportDate, String branchCode);

    
    
}