package com.bornfire.xbrl.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_IRRBB_Data_EVE_Template_Repository  extends JpaRepository <RT_IRRBB_Data_EVE_Template,Long>{
	
	@Query(value = "select * from BCBUAE_IRRBB_DATA_TEMPLATE ", nativeQuery = true)
	List<RT_IRRBB_Data_EVE_Template> getAlldetails();
	
	@Query(value = "SELECT * FROM BCBUAE_IRRBB_DATA_TEMPLATE where SI_NO =?1 ", nativeQuery = true)
	RT_IRRBB_Data_EVE_Template getParticularDataBySI_NO(String SI_NO);
	
	
	@Query(value = "SELECT * FROM BCBUAE_IRRBB_DATA_TEMPLATE ", nativeQuery = true)
	List<Object[]> getirrbbevedatalistdata1();
	
	
	
	
	
	

}
