package com.bornfire.xbrl.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_IRRBB_Data_Discount_Rates_Repository extends JpaRepository<RT_IRRBB_Data_Discount_Rates,Long> {
	
	@Query(value = "select * from BCBUAE_IRRBB_DISCOUNT_RATES ", nativeQuery = true)
	List<RT_IRRBB_Data_Discount_Rates> getAlldetails();

}
