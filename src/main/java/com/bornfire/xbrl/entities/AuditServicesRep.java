package com.bornfire.xbrl.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface AuditServicesRep extends JpaRepository<AuditServicesEntity , UUID>{
	@Query(value = "select * from USER_AUDIT ", nativeQuery = true)
	List<AuditServicesEntity> getauditService();

		
		@Query(value = "select * from USER_AUDIT where AUDIT_TABLE = 'XBRLUSERPROFILETABLE'", nativeQuery = true)
		List<AuditServicesEntity> getauditListLocalvalues();
		
		@Query(value = "select * from USER_AUDIT where AUDIT_TABLE = 'Kyc_corporate'", nativeQuery = true)
		List<AuditServicesEntity> getauditListLocalvalues1();
		
		@Query(value = "select * from USER_AUDIT where TRUNC(AUDIT_DATE) = ?1", nativeQuery = true)
		List<AuditServicesEntity> getauditListOpeartion(Date audit_date);
		
		@Query(value = "select * from USER_AUDIT where AUDIT_TABLE = 'XBRLUSERPROFILETABLE' AND TRUNC(AUDIT_DATE) = ?1", nativeQuery = true)
		List<AuditServicesEntity> getauditListLocalvaluesbusiness(Date fromDateToUse);
		
		@Query(value = "SELECT * FROM USER_AUDIT WHERE AUDIT_TABLE IN ('Kyc_corporate', 'KYC_indidual') AND TRUNC(AUDIT_DATE) = TRUNC(?1)", nativeQuery = true)
		List<AuditServicesEntity> getauditListLocalvaluesbusiness1(Date fromDateToUse);
		@Query(value = "SELECT change_details FROM KYC_AUDIT_TABLE WHERE AUDIT_TABLE IN ('Kyc_corporate', 'KYC_indidual') and audit_ref_no = ?1", nativeQuery = true)
		String getchanges(String audit_ref_no);
		

}
