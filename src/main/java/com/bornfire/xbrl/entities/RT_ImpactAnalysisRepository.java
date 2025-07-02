package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_ImpactAnalysisRepository extends JpaRepository<RT_ImpactAnalysis, Date> {
	// Add custom queries if needed

	@Query(value = "select * from BCBUAE_CROSS_CUR_FUNDING_IMPACT_ANALYSIS  ", nativeQuery = true)
	List<RT_ImpactAnalysis> getlist();

	@Query(value = "SELECT * FROM BCBUAE_CROSS_CUR_FUNDING_IMPACT_ANALYSIS ", nativeQuery = true)
	List<RT_ImpactAnalysis> getfxriskdatalistdata();

	@Query(value = "SELECT * FROM BCBUAE_CROSS_CUR_FUNDING_IMPACT_ANALYSIS ", nativeQuery = true)
	List<Object[]> getfxriskdatalistdata1();
	
	
	@Query(value = "SELECT * FROM BCBUAE_CROSS_CUR_FUNDING_IMPACT_ANALYSIS where SI_NO =?1 ", nativeQuery = true)
	RT_ImpactAnalysis getParticularDataBySI_NO(String SI_NO);
	
}
