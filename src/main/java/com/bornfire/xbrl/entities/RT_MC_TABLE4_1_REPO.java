package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RT_MC_TABLE4_1_REPO extends JpaRepository<RT_MC_TABLE4_1_ENTITY, MCReportId> {

	@Query(value = "SELECT * FROM RT_MC_TABLE4_1 WHERE BRANCH_CODE = :branch", nativeQuery = true)
	List<RT_MC_TABLE4_1_ENTITY> findBybranchcode(@Param("branch") String branch);

	@Query(value = "SELECT * FROM RT_MC_TABLE4_1 WHERE REPORT_DATE = TO_DATE(:reportDate, 'DD-MM-YYYY') ORDER BY REPORT_DATE DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
	List<RT_MC_TABLE4_1_ENTITY> findByReportDate(@Param("reportDate") String reportDate);
	
	@Query(value = "SELECT * FROM RT_MC_TABLE4_1 WHERE REPORT_DATE = :reportDate AND BRANCH_CODE = :branchCode", nativeQuery = true)
	RT_MC_TABLE4_1_ENTITY findByReportDateAndBranchCode(@Param("reportDate") Date reportDate,
			@Param("branchCode") String branchCode);

	@Modifying
	@Transactional
	@Query("UPDATE RT_MC_TABLE4_1_ENTITY r SET r.VERIFY_FLG = :verifyFlg ,  r.REMARKS = :remarks "
			+ "WHERE r.REPORT_DATE = :reportDate ")
	int updateVerifyFlgAndRemarks(@Param("verifyFlg") String verifyFlg, @Param("remarks") String remarks,
			@Param("reportDate") Date reportDate);
}
