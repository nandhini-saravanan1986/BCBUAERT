package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_FxRiskDataRepository extends JpaRepository<RT_Fxriskdata, String> {
	// Add custom queries if needed

	@Query(value = "select * from BCBUAE_FX_RISK_DATA  ", nativeQuery = true)
	List<RT_Fxriskdata> getlist();

	@Query(value = "SELECT * FROM BCBUAE_FX_RISK_DATA ", nativeQuery = true)
	List<RT_Fxriskdata> getfxriskdatalistdata();

	@Query(value = "SELECT * FROM BCBUAE_FX_RISK_DATA ", nativeQuery = true)
	List<Object[]> getfxriskdatalistdata1();
	
	
	@Query(value = "SELECT * FROM BCBUAE_FX_RISK_DATA where SI_NO =?1 ", nativeQuery = true)
	RT_Fxriskdata getParticularDataBySI_NO(String SI_NO);
	
}
