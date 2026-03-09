package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RT_MC_TABLE4_2_REPO extends JpaRepository<RT_MC_TABLE4_2_ENTITY, MCReportId> {

	 @Query(value = "SELECT * FROM RT_MC_TABLE4_2 WHERE BRANCH_CODE = :branch", nativeQuery = true)
	    List<RT_MC_TABLE4_2_ENTITY> findBybranchcode(@Param("branch") String branch);
}
