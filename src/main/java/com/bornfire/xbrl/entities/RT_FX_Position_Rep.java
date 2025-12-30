package com.bornfire.xbrl.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_FX_Position_Rep extends JpaRepository<RT_FX_Position_Entity, String> {

}
