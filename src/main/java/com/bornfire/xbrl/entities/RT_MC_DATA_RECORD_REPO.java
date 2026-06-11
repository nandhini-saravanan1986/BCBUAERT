package com.bornfire.xbrl.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_MC_DATA_RECORD_REPO extends JpaRepository<RT_MC_DATA_RECORD_ENTITY, BigDecimal> {

	@Query("SELECT COALESCE(MAX(r.id), 0) FROM RT_MC_DATA_RECORD_ENTITY r")
	BigDecimal findMaxId();

	RT_MC_DATA_RECORD_ENTITY findTopByFormModeAndReportDateAndCellNameOrderByIdDesc(String formMode, Date reportDate,
			String cellName);

    RecordMetadataProjection findTopProjectionByFormModeAndReportDateAndCellNameOrderByIdDesc(
            String formMode, Date reportDate, String cellName);
    
    List<RecordMetadataProjection> findTop4ByFormModeAndCellNameOrderByReportDateDesc(String formMode, String cellName);
    
	public interface RecordMetadataProjection {
	    BigDecimal getId();
	    String getDataValue();
	    String getJustification();
	    String getSource();
	    Date getReportDate();
	    Date getSubmittedReportDate();
	    String getVerifyFlg();
	    String getRemarks();
	    
	    String getFileName1();
	    String getFileName2();
	    String getFileName3();
	    String getFileName4();
	    String getFileName5();
	    String getFileName6();
	    String getFileName7();
	    String getFileName8();
	    String getFileName9();
	    String getFileName10();
	}
	
}