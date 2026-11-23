package com.bornfire.xbrl.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_MC_DEPARTMENT_SIGNOFF_REPO extends JpaRepository<RT_MC_DEPARTMENT_SIGNOFF_ENTITY, BigDecimal> {

	@Query("SELECT COALESCE(MAX(r.id), 0) FROM RT_MC_DEPARTMENT_SIGNOFF_ENTITY r")
	BigDecimal findMaxId();

	/** Sign-off rows of one table / report date / period. */
	List<RT_MC_DEPARTMENT_SIGNOFF_ENTITY> findByFormModeAndReportDateAndTimeperiodAndDelFlgOrderBySnoAsc(
			String formMode, Date reportDate, String timeperiod, String delFlg);

	/** Cell name, modify flag and verify flag of every saved cell of one table. */
	@Query(value = "SELECT CELL_NAME, MODIFY_FLG, VERIFY_FLG FROM RT_MC_DATA_RECORD WHERE FORM_MODE = :formMode "
			+ "AND REPORT_DATE = TO_DATE(:reportDate, 'DD-MM-YYYY') AND TIMEPERIOD = :timeperiod", nativeQuery = true)
	List<Object[]> findCellStates(@Param("formMode") String formMode, @Param("reportDate") String reportDate,
			@Param("timeperiod") String timeperiod);

	/** Column headers of the given cells (duplicates are removed in the controller). */
	@Query(value = "SELECT COLUMN_HEADER FROM RT_MC_DATA_RECORD WHERE FORM_MODE = :formMode "
			+ "AND REPORT_DATE = TO_DATE(:reportDate, 'DD-MM-YYYY') AND TIMEPERIOD = :timeperiod "
			+ "AND CELL_NAME IN (:cells)", nativeQuery = true)
	List<String> findColumnHeaders(@Param("formMode") String formMode, @Param("reportDate") String reportDate,
			@Param("timeperiod") String timeperiod, @Param("cells") List<String> cells);

	/** Data-entry audit entries of one table screen and report date, newest first, made by any user. */
	@Query("SELECT a FROM MC_Service_audit_entity a WHERE a.audit_screen = :screen AND a.domain_id = :reportDate "
			+ "AND a.func_code IN (:funcCodes) ORDER BY a.entry_time DESC")
	List<MC_Service_audit_entity> findMakerEntries(@Param("screen") String screen,
			@Param("reportDate") String reportDate, @Param("funcCodes") List<String> funcCodes);

	/** Department of a login, from the user profile. */
	@Query("SELECT u.department FROM UserProfile u WHERE u.userid = :userId")
	String findDepartmentByUserId(@Param("userId") String userId);

	/** Display name of a login, from the user profile. */
	@Query("SELECT u.username FROM UserProfile u WHERE u.userid = :userId")
	String findUserNameByUserId(@Param("userId") String userId);
}