package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_ForeignCurrencyDepositRepository extends JpaRepository<RT_ForeignCurrencyDeposit, String> {
	// Add custom queries if needed

	@Query(value = "select * from BCBUAE_CROSS_CUR_FUNDING_FOREIGN_DEPOSITS  ", nativeQuery = true)
	List<RT_ForeignCurrencyDeposit> getlist();

	@Query(value = "SELECT * FROM BCBUAE_CROSS_CUR_FUNDING_FOREIGN_DEPOSITS ", nativeQuery = true)
	List<RT_ForeignCurrencyDeposit> getfxriskdatalistdata();

	@Query(value = "SELECT * FROM BCBUAE_CROSS_CUR_FUNDING_FOREIGN_DEPOSITS ", nativeQuery = true)
	List<Object[]> getforeigncurrencylistdata1();
	
	
	@Query(value = "SELECT * FROM BCBUAE_CROSS_CUR_FUNDING_FOREIGN_DEPOSITS where SI_NO =?1 ", nativeQuery = true)
	RT_ForeignCurrencyDeposit getParticularDataBySI_NO(String SI_NO);
	
}
