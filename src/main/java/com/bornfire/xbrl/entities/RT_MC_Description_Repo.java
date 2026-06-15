package com.bornfire.xbrl.entities;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RT_MC_Description_Repo extends JpaRepository<RT_MC_Description_Entity, BigDecimal> {

    List<RT_MC_Description_Entity> findBySectionName(String sectionName);
    
	@Query(value = "SELECT * FROM RT_MC_DESCRIPTION " + "WHERE SECTION_NAME = :sectionName "
			+ "AND DATA_ELEMENT = :dataElement " + "AND ROWNUM = 1", nativeQuery = true)
	RT_MC_Description_Entity findTopBySectionAndElementNative(@Param("sectionName") String sectionName,
			@Param("dataElement") String dataElement);
}