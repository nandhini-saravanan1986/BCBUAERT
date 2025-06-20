package com.bornfire.xbrl.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_RepoDataTemplateRepository extends JpaRepository<RT_RepoDataTemplate, Long> {

	   @Query(value = "SELECT * FROM BCBUAE_REPO_DATA_TEMPLATE WHERE DEL_FLG != 'Y'", nativeQuery = true)
	    List<RT_RepoDataTemplate> getlist();
}
