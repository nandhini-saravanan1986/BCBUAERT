package com.bornfire.xbrl.entities;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_MC_ALLOCATIONS_REPO extends JpaRepository<RT_MC_ALLOCATIONS_ENTITY, RT_MC_ALLOCATIONS_ID> {

	@Query(value = "SELECT ALLOC_VALUE FROM RT_MC_ALLOCATIONS WHERE UPPER(TRIM(CATEGORY)) = 'DEPT'", nativeQuery = true)
	List<String> findDepartments();

	@Query(value = "SELECT ALLOC_VALUE FROM RT_MC_ALLOCATIONS WHERE UPPER(TRIM(CATEGORY)) = 'FUNC'", nativeQuery = true)
	List<String> findFunctions();
}
