package com.bornfire.xbrl.entities;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Counterparty_Rep extends CrudRepository<Counterparty_Entity,Long>{
	

	@Query(value = "select * from MIS_COUNTER_PARTY_TABLE WHERE ID=?1 ORDER BY ID", nativeQuery = true)
	Counterparty_Entity getBYID(Long ID);

	@Query(value = "select * from MIS_COUNTER_PARTY_TABLE ORDER BY ID", nativeQuery = true)
	List<Counterparty_Entity> getalllist();
	
	@Query(value = "select COUNTER_PARTY_BANK from MIS_COUNTER_PARTY_TABLE", nativeQuery = true)
	List<String> getall();
	
	@Query(value = "select Distinct COUNTER_PARTY_BANK from MIS_COUNTER_PARTY_TABLE where REGULAR_LIMIT <> 0 or ADHOC_LIMIT <> 0", nativeQuery = true)
	List<String> Getcounterpartyname();
	
	@Query(value = "SELECT DISTINCT COUNTERPARTY_CODE FROM MIS_COUNTER_PARTY_TABLE WHERE COUNTERPARTY_CODE IS NOT NULL", nativeQuery = true)
	List<String> getcodes();

	
	@Query(value = "SELECT MAX(TO_NUMBER(REGEXP_SUBSTR(SRL_NO, '\\d+$'))) FROM MIS_COUNTER_PARTY_TABLE", nativeQuery = true)
	Integer findMaxSrlNoSuffix();

}
