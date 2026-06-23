package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MC_Service_audit_Repo extends JpaRepository<MC_Service_audit_entity, String> {

	@Query("SELECT a FROM MC_Service_audit_entity a WHERE "
			+ "(LOWER(a.audit_ref_no) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(a.audit_table) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(a.func_code) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(a.entry_user) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(a.entry_time) LIKE LOWER(CONCAT('%', :keyword, '%'))) " + "ORDER BY a.entry_time DESC")
	Page<MC_Service_audit_entity> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

	@Query("SELECT a FROM MC_Service_audit_entity a ORDER BY a.entry_time DESC")
	Page<MC_Service_audit_entity> findAllByOrderByDateDesc(Pageable pageable);

	@Query(value = "SELECT * FROM RT_MC_SERVICE_AUDIT_TABLE WHERE entry_time BETWEEN :fromDate AND :toDate", nativeQuery = true)
	List<MC_Service_audit_entity> findByEntryTimeBetween(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate);
	
}
