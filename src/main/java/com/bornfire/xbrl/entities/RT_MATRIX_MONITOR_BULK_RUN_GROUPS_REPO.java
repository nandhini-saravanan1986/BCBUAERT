package com.bornfire.xbrl.entities;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_MATRIX_MONITOR_BULK_RUN_GROUPS_REPO
		extends JpaRepository<RT_MATRIX_MONITOR_BULK_RUN_GROUPS_ENTITY, Long> {

	@Query("SELECT COALESCE(MAX(b.id), 0) FROM RT_MATRIX_MONITOR_BULK_RUN_GROUPS_ENTITY b")
	Long getMaxId();

	@Query("SELECT e FROM RT_MATRIX_MONITOR_BULK_RUN_GROUPS_ENTITY e WHERE e.delFlg = 'N'")
	List<RT_MATRIX_MONITOR_BULK_RUN_GROUPS_ENTITY> findAllActiveGroups();

	@Modifying
	@Transactional
	@Query("UPDATE RT_MATRIX_MONITOR_BULK_RUN_GROUPS_ENTITY e SET e.delFlg = 'Y' WHERE e.groupName = ?1")
	void softDeleteByGroupName(String groupName);
}