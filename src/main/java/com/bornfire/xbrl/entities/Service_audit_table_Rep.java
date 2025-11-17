package com.bornfire.xbrl.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface Service_audit_table_Rep extends JpaRepository<Service_audit_table_entity, String> {

	@Query(value = "select * from BCBUAERT_SERVICE_AUDIT_TABLE", nativeQuery = true)
	List<Service_audit_table_entity> getauditListLocalvalues();

	@Query(value = "select * from BCBUAERT_SERVICE_AUDIT_TABLE where AUDIT_TABLE = 'Kyc_corporate'", nativeQuery = true)
	List<Service_audit_table_entity> getauditListLocalvalues1();

	@Query(value = "select * from BTDES_SERVICE_AUDIT_TABLE where TRUNC(AUDIT_DATE) = ?1", nativeQuery = true)
	List<Service_audit_table_entity> getauditListOpeartion(Date audit_date);

	
	  @Query(value = "SELECT BTDES_AUDIT_SEQ.NEXTVAL FROM dual", nativeQuery = true)
	  Long getAuditRefUUID();
	 

}
