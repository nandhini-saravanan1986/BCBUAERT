package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BcbuaeTradeMarketriskDataRepository extends JpaRepository<BcbuaeTradeMarketriskData, Date> {

	@Query(value = "select * from BCBUAE_TRADE_MARKET_RISK_DATA where DEL_FLG != 'Y'", nativeQuery = true)
	List<BcbuaeTradeMarketriskData> getlist();

}
