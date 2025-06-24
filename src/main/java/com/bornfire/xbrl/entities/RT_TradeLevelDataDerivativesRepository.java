package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_TradeLevelDataDerivativesRepository extends JpaRepository<RT_TradeLevelDataDerivatives, Date> {
	// Add custom queries if needed

	@Query(value = "select * from BCBUAE_TRADE_LEVEL_DERIVATIVES  ", nativeQuery = true)
	List<RT_TradeLevelDataDerivatives> getlist();

	@Query(value = "SELECT * FROM BCBUAE_TRADE_LEVEL_DERIVATIVES ", nativeQuery = true)
	List<RT_TradeLevelDataDerivatives> getfxriskdatalistdata();

	@Query(value = "SELECT * FROM BCBUAE_TRADE_LEVEL_DERIVATIVES ", nativeQuery = true)
	List<Object[]> gettradeleveldataderivative1();
	
	
	@Query(value = "SELECT * FROM BCBUAE_TRADE_LEVEL_DERIVATIVES where SI_NO =?1 ", nativeQuery = true)
	RT_TradeLevelDataDerivatives getParticularDataBySI_NO(String SI_NO);
	
}
