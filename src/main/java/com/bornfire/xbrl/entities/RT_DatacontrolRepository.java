package com.bornfire.xbrl.entities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bornfire.xbrl.entities.RT_DataControl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Repository
public interface RT_DatacontrolRepository extends JpaRepository<RT_DataControl, Integer> {
    
    // You can define custom queries here if needed, for example:
    // List<BcbuaeNostroAccBalDatacontrol> findByEntityFlg(String entityFlg);
	
	@Query(value = "select * from BCBUAE_DATACONTROL where PORTFOLIO_DATE=?1 AND REPORT_NAME=?2 AND  DEL_FLG != 'Y'", nativeQuery = true)
	RT_DataControl getdata(Timestamp lastdatetimestamp,String reporttype);
}
