package com.bornfire.xbrl.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_Noop_net_position_rep extends JpaRepository<RT_Noop_net_position_entity, String> {

	@Query(value="Select * from RT_NET_POSITION_LIMIT_NOOP",nativeQuery=true)
	List<Object[]> GetNoopcalculationdetail();
	
}
