package com.bornfire.xbrl.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
	@Modifying
	@Query("UPDATE RT_MC_DATA_RECORD_ENTITY r SET r.verifyFlg = :verifyFlg , r.remarks = :remarks WHERE r.formMode = :formMode AND r.reportDate = :reportDate AND r.cellName = :cellName")
	int updateVerifyFlg(@Param("verifyFlg") String verifyFlg, @Param("formMode") String formMode,
			@Param("reportDate") Date reportDate, @Param("cellName") String cellName,@Param("remarks") String remarks);

	@Modifying
	@Query("UPDATE RT_MC_DATA_RECORD_ENTITY r SET r.verifyFlg = :verifyFlg WHERE r.formMode = :formMode AND r.reportDate = :reportDate AND r.cellName = :cellName")
	int updateVerifyFlgwithoutremarks(@Param("verifyFlg") String verifyFlg, @Param("formMode") String formMode,
			@Param("reportDate") Date reportDate, @Param("cellName") String cellName);
	
	@Modifying
	@Query("UPDATE RT_MC_DATA_RECORD_ENTITY r SET r.verifyFlg = 'Y' WHERE r.formMode = :formMode AND r.reportDate = :reportDate AND r.verifyFlg = 'MR'")
	int revertMrCellsToVerified(@Param("formMode") String formMode, @Param("reportDate") Date reportDate);

	@Query(value = "SELECT COALESCE(remarks, ' - ') FROM RT_MC_DATA_RECORD " + "WHERE form_mode = :formMode "
			+ "AND report_date = :reportDate " + "AND cell_name = :cellName "
			+ "ORDER BY id DESC LIMIT 1", nativeQuery = true)
	String findRemarksByFormModeAndReportDateAndCellName(@Param("formMode") String formMode,
			@Param("reportDate") Date reportDate, @Param("cellName") String cellName);
	
	public interface RecordMetadataProjection {
	    BigDecimal getId();
	    String getDataValue();
	    String getJustification();
	    String getSource();
	    Date getReportDate();
	    Date getSubmittedReportDate();
	    String getVerifyFlg();
	    String getRemarks();
	    String getModifyFlg();
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