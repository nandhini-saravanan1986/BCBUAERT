package com.bornfire.xbrl.entities;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RT_MC_Description_Repo extends JpaRepository<RT_MC_Description_Entity, BigDecimal> {

    List<RT_MC_Description_Entity> findBySectionName(String sectionName);
    
	@Query(value = "SELECT * FROM RT_MC_DESCRIPTION "
			+ "WHERE REGEXP_REPLACE(SECTION_NAME, '[[:space:]]', '') = REGEXP_REPLACE(:sectionName, '[[:space:]]', '') "
			+ "AND REGEXP_REPLACE(DATA_ELEMENT, '[[:space:]]', '') = REGEXP_REPLACE(:dataElement, '[[:space:]]', '') "
			+ "AND ROWNUM = 1", nativeQuery = true)
	RT_MC_Description_Entity findTopBySectionAndElementNative(@Param("sectionName") String sectionName,
			@Param("dataElement") String dataElement);
	
	@Query(value = "SELECT DATA_ELEMENT FROM RT_MC_DESCRIPTION "
			+ "WHERE REGEXP_REPLACE(SECTION_NAME, '[[:space:]]', '') = REGEXP_REPLACE(:sectionName, '[[:space:]]', '') "
			+ "AND REGEXP_REPLACE(CELL_NAME, '[[:space:]]', '') = REGEXP_REPLACE(:cellName, '[[:space:]]', '') "
			+ "AND ROWNUM = 1", nativeQuery = true)
	String findHeaderNameByCode(@Param("sectionName") String sectionName, @Param("cellName") String cellName);

	@Query(value = "SELECT * FROM RT_MC_DESCRIPTION "
			+ "WHERE LOWER(REGEXP_REPLACE(SECTION_NAME, '[[:space:]]', '')) = LOWER(REGEXP_REPLACE(:sectionName, '[[:space:]]', '')) "
			+ "AND (LOWER(REGEXP_REPLACE(DESCRIPTION, '[[:space:]]', '')) = LOWER(REGEXP_REPLACE(:headerText, '[[:space:]]', '')) "
			+ "     OR LOWER(REGEXP_REPLACE(DATA_ELEMENT, '[[:space:]]', '')) = LOWER(REGEXP_REPLACE(:headerText, '[[:space:]]', '')) "
			+ "     OR LOWER(REGEXP_REPLACE(COLUMN_ID, '[[:space:]]', '')) = LOWER(REGEXP_REPLACE(:headerText, '[[:space:]]', ''))) "
			+ "AND ROWNUM = 1", nativeQuery = true)
	RT_MC_Description_Entity findTopBySectionAndDescOrElementNative(@Param("sectionName") String sectionName,
			@Param("headerText") String headerText);

	@Query(value = "SELECT * FROM RT_MC_DESCRIPTION "
			+ "WHERE LOWER(REGEXP_REPLACE(SECTION_NAME, '[[:space:]]', '')) = LOWER(REGEXP_REPLACE(:sectionName, '[[:space:]]', '')) "
			+ "AND LOWER(REGEXP_REPLACE(COLUMN_ID, '[[:space:]]', '')) = LOWER(REGEXP_REPLACE(:columnId, '[[:space:]]', '')) "
			+ "AND ROWNUM = 1", nativeQuery = true)
	RT_MC_Description_Entity findTopBySectionAndColumnIdNative(@Param("sectionName") String sectionName,
			@Param("columnId") String columnId);

	@Query(value = "SELECT * FROM RT_MC_DESCRIPTION "
			+ "WHERE LOWER(REGEXP_REPLACE(COLUMN_ID, '[[:space:]]', '')) = LOWER(REGEXP_REPLACE(:columnId, '[[:space:]]', '')) "
			+ "AND ROWNUM = 1", nativeQuery = true)
	RT_MC_Description_Entity findTopByColumnIdNative(@Param("columnId") String columnId);

	@Query(value = "SELECT * FROM RT_MC_DESCRIPTION "
			+ "WHERE LOWER(REGEXP_REPLACE(SECTION_NAME, '[[:space:]]', '')) = LOWER(REGEXP_REPLACE(:sectionName, '[[:space:]]', '')) "
			+ "AND LOWER(REGEXP_REPLACE(CELL_NAME, '[[:space:]]', '')) = LOWER(REGEXP_REPLACE(:cellName, '[[:space:]]', '')) "
			+ "AND ROWNUM = 1", nativeQuery = true)
	RT_MC_Description_Entity findTopBySectionAndCellNameNative(@Param("sectionName") String sectionName,
			@Param("cellName") String cellName);
}