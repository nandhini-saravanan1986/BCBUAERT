package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RT_MC_TABLE8_REPO extends JpaRepository<RT_MC_TABLE8_ENTITY, MCReportId> {

	@Query(value = "SELECT * FROM RT_MC_TABLE8 WHERE REPORT_DATE = :reporttDate", nativeQuery = true)
	List<RT_MC_TABLE8_ENTITY> findByReportDate(@Param("reporttDate") Date reporttDate);

	@Query(value = "SELECT * FROM RT_MC_TABLE8 WHERE BRANCH_CODE = :branch", nativeQuery = true)
	List<RT_MC_TABLE8_ENTITY> findBybranchcode(@Param("branch") String branch);

	@Query(value = "SELECT * FROM RT_MC_TABLE8 WHERE REPORT_DATE = :reportDate AND BRANCH_CODE = :branchCode", nativeQuery = true)
	RT_MC_TABLE8_ENTITY findByReportDateAndBranchCode(@Param("reportDate") Date reportDate,
			@Param("branchCode") String branchCode);
}
