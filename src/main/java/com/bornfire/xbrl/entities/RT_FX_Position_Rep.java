package com.bornfire.xbrl.entities;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_FX_Position_Rep extends JpaRepository<RT_FX_Position_Entity, String> {
	@Query(value = "SELECT * FROM RT_FX_POSITION_TABLE WHERE TRUNC(report_date) = TRUNC(?1) AND position_type = ?2", nativeQuery = true)
	RT_FX_Position_Entity findExistingRecord(Date reportDate, String positionType);

	@Query(value = "SELECT MAX(TO_NUMBER(srl_no)) FROM RT_FX_POSITION_TABLE", nativeQuery = true)
	Integer findMaxSrlNo();

}
