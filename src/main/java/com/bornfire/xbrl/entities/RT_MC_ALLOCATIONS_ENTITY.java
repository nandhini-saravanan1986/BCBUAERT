package com.bornfire.xbrl.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(RT_MC_ALLOCATIONS_ID.class)
@Table(name = "RT_MC_ALLOCATIONS")
public class RT_MC_ALLOCATIONS_ENTITY implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CATEGORY")
	private String category;

	@Id
	@Column(name = "ALLOC_VALUE")
	private String allocValue;

	public RT_MC_ALLOCATIONS_ENTITY() {
		super();
	}

	public RT_MC_ALLOCATIONS_ENTITY(String category, String allocValue) {
		super();
		this.category = category;
		this.allocValue = allocValue;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAllocValue() {
		return allocValue;
	}

	public void setAllocValue(String allocValue) {
		this.allocValue = allocValue;
	}
}
