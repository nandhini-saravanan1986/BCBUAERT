package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_MmDataRepository extends JpaRepository<RT_MmData, Date> {
	// Add custom queries if needed

	@Query(value = "select * from BCBUAE_MM_DATA  ", nativeQuery = true)
	List<RT_MmData> getlist();

	/*
	 * @Query(value = "SELECT * FROM BCBUAE_MM_DATA ", nativeQuery = true)
	 * List<RT_MmData> getfxriskdatalistdata();
	 */

	@Query(value = "SELECT * FROM BCBUAE_MM_DATA ", nativeQuery = true)
	List<Object[]> getmmdatalistdata1();
	
	
	@Query(value = "SELECT * FROM BCBUAE_MM_DATA where SI_NO =?1 ", nativeQuery = true)
	RT_MmData getParticularDataBySI_NO(String SI_NO);
	
	

}
