package com.bornfire.xbrl.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RT_Investment_Risk_Data_Dashboard_TemplateRepository extends JpaRepository<RT_Investment_Risk_Data_Dashboard_Template,  Long>  {

	@Query(value = "SELECT * FROM BCBUAE_INVESTMENT_RISK_DATA_DASHBOARD_TEMPLATE", nativeQuery = true)
    List<RT_Investment_Risk_Data_Dashboard_Template> getlist();
    
    @Query(value = "SELECT * FROM BCBUAE_INVESTMENT_RISK_DATA_DASHBOARD_TEMPLATE where SI_NO =?1 ", nativeQuery = true)
    RT_Investment_Risk_Data_Dashboard_Template getParticularDataBySI_NO(Long SI_no);
    
    @Query(value = "SELECT * FROM BCBUAE_INVESTMENT_RISK_DATA_DASHBOARD_TEMPLATE ", nativeQuery = true)
	List<Object[]> getinvestmentriskdata1();
    
    
	
}
