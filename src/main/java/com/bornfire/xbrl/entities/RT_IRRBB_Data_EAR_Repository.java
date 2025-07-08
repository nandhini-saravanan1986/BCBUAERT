package com.bornfire.xbrl.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_IRRBB_Data_EAR_Repository extends JpaRepository <RT_IRRBB_Data_EAR,Long> {
	@Query(value = "select * from BCBUAE_IRRBB_EAR ", nativeQuery = true)
	List<RT_IRRBB_Data_EAR> getAlldetails();

}
