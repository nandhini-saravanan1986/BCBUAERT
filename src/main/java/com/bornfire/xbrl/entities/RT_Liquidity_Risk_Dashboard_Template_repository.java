package com.bornfire.xbrl.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_Liquidity_Risk_Dashboard_Template_repository  extends JpaRepository<RT_Liquidity_Risk_Dashboard_Template,String>{
	
	@Query(value="SELECT * FROM BCBUAE_LIQUIDITY_RISK_DASHBOARD_TEMPLATE" ,nativeQuery=true)
	List<RT_Liquidity_Risk_Dashboard_Template> getAlldetails();

}
