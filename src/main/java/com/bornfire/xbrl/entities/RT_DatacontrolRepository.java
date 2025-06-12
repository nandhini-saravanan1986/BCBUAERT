package com.bornfire.xbrl.entities;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.bornfire.xbrl.entities.RT_DataControl;

import java.util.Date;


@Repository
public interface RT_DatacontrolRepository extends JpaRepository<RT_DataControl, Date> {
    
    // You can define custom queries here if needed, for example:
    // List<BcbuaeNostroAccBalDatacontrol> findByEntityFlg(String entityFlg);
}
