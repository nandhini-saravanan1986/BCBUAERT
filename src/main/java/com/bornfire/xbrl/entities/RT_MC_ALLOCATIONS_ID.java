package com.bornfire.xbrl.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RT_MC_ALLOCATIONS_ID implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "CATEGORY")
	private String category;

	@Column(name = "ALLOC_VALUE")
	private String allocValue;

	public RT_MC_ALLOCATIONS_ID() {
		super();
	}

	public RT_MC_ALLOCATIONS_ID(String category, String allocValue) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RT_MC_ALLOCATIONS_ID that = (RT_MC_ALLOCATIONS_ID) o;
		return Objects.equals(category, that.category) && Objects.equals(allocValue, that.allocValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(category, allocValue);
	}
}
