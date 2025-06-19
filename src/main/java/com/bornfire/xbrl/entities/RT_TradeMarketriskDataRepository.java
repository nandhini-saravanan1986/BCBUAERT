package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_TradeMarketriskDataRepository extends JpaRepository<RT_TradeMarketRiskData, Date> {

	@Query(value = "select * from BCBUAE_TRADE_MARKET_RISK_DATA where DEL_FLG != 'Y'", nativeQuery = true)
	List<RT_TradeMarketRiskData> getlist();

}
