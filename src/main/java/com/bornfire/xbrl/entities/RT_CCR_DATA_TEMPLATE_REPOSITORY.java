package com.bornfire.xbrl.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_CCR_DATA_TEMPLATE_REPOSITORY extends JpaRepository<RT_CCR_DATA_TEMPLATE, String> {

	@Query(value = "select * from BCBUAE_CCR_DATA_TABLE where SI_NO=?1  ", nativeQuery = true)
	RT_CCR_DATA_TEMPLATE editccr(String sino);

	@Query(value = "SELECT * FROM BCBUAE_CCR_DATA_TABLE WHERE DEL_FLG = 'N'", nativeQuery = true)
	List<RT_CCR_DATA_TEMPLATE> getlist();

}
