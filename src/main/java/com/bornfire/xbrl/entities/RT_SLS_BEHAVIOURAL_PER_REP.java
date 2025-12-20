package com.bornfire.xbrl.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_SLS_BEHAVIOURAL_PER_REP extends JpaRepository<RT_SLS_BEHAVIOURAL_PER_ENTITY, String> {
    // Standard CRUD methods are included automatically
}