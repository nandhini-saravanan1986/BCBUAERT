package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_MC_TABLE3_REPO extends JpaRepository<RT_MC_TABLE3_ENTITY, MCReportId> {

	@Query(value = "SELECT * FROM RT_MC_TABLE3 WHERE BRANCH_CODE = :branch", nativeQuery = true)
	List<RT_MC_TABLE3_ENTITY> findBybranchcode(@Param("branch") String branch);

	@Query(value = "SELECT * FROM RT_MC_TABLE3 WHERE REPORT_DATE = :reportDate AND BRANCH_CODE = :branchCode", nativeQuery = true)
	RT_MC_TABLE3_ENTITY findByReportDateAndBranchCode(@Param("reportDate") Date reportDate,
			@Param("branchCode") String branchCode);
}