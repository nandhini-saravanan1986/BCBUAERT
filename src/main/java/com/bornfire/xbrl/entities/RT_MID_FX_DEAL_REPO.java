package com.bornfire.xbrl.entities;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_MID_FX_DEAL_REPO extends JpaRepository<RT_MID_FX_DEAL_DC, String> {

	// Deletes records for a specific date to prevent duplicates
	@Transactional
	@Modifying
	@Query("DELETE FROM RT_MID_FX_DEAL_DC a WHERE a.reportDate = ?1")
	void deleteByReportDate(Date reportDate);
}