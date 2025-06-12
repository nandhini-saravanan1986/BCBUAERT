package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BcbuaeFxRiskDataRepository extends JpaRepository<BcbuaeFxriskdata, Date> {
	// Add custom queries if needed

	@Query(value = "select * from BCBUAE_FX_RISK_DATA where DEL_FLG != 'Y' ", nativeQuery = true)
	List<BcbuaeFxriskdata> getlist();

	@Query(value = "SELECT * FROM BCBUAE_FX_RISK_DATA ", nativeQuery = true)
	List<BcbuaeFxriskdata> getfxriskdatalistdata();

	@Query(value = "SELECT * FROM BCBUAE_FX_RISK_DATA ", nativeQuery = true)
	List<Object[]> getfxriskdatalistdata1();

}
