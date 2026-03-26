package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_Noop_net_position_rep extends JpaRepository<RT_Noop_net_position_entity, RT_Noop_net_position_id_class> {

	@Query(value="Select * from RT_NET_POSITION_LIMIT_NOOP",nativeQuery=true)
	List<Object[]> GetNoopcalculationdetail();
	
	@Query(value="Select REPORT_DATE,CURRENCY, READY_EXCHAGE_POSITION_IN_AC, CBS_FX_POSITION_AC,\r\n"
			+ "MTM_AC, TOTAL_NOOP_IN_AC, TOTAL_NOOP_IN_LC from RT_NET_POSITION_LIMIT_NOOP\r\n"
			+ "where REPORT_DATE = ?1",nativeQuery=true)
	List<Object[]> Getnoopdetail(Date Report_date);
	
}
