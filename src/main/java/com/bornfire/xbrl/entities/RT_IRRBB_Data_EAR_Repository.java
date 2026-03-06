package com.bornfire.xbrl.entities;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_IRRBB_Data_EAR_Repository extends JpaRepository <RT_IRRBB_Data_EAR,RT_IRRBB_Data_EVE_Template_Idclass> {
	@Query(value = "select * from BCBUAE_IRRBB_EAR ", nativeQuery = true)
	List<RT_IRRBB_Data_EAR> getAlldetails();
	
	/*
	 * @Query(value = "SELECT * FROM BCBUAE_IRRBB_EAR where SI_NO =?1 ", nativeQuery
	 * = true) RT_IRRBB_Data_EAR getParticularDataBySI_NO(BigDecimal SI_NO);
	 */
	
	
	 @Query(value = "SELECT * FROM BCBUAE_IRRBB_EAR ", nativeQuery = true)
	 List<Object[]> getirrbbeardatalistdata1();
	
}
