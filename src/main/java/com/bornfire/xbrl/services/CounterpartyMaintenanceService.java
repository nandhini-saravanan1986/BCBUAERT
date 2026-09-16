package com.bornfire.xbrl.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.bornfire.xbrl.dto.CounterpartyMaintenanceDto;

@Service
public class CounterpartyMaintenanceService {

	private final ConcurrentHashMap<String, CounterpartyMaintenanceDto> maintained = new ConcurrentHashMap<String, CounterpartyMaintenanceDto>();
	private final List<CounterpartyMaintenanceDto> outstandingSource = new ArrayList<CounterpartyMaintenanceDto>();

	public CounterpartyMaintenanceService() {
		seedMaintained();
		seedOutstanding();
	}

	private void seedMaintained() {
		saveInternal(new CounterpartyMaintenanceDto("CP001", "HSBC-DXB", "HSBC Bank Middle East", "HSBC Dubai"),
				"SYSTEM");
		saveInternal(new CounterpartyMaintenanceDto("CP002", "SCB-UAE", "Standard Chartered Bank", "SCB UAE"),
				"SYSTEM");
	}

	private void seedOutstanding() {
		outstandingSource.add(new CounterpartyMaintenanceDto("CP001", "HSBC-DXB", "HSBC Bank Middle East", "HSBC Dubai"));
		outstandingSource.add(new CounterpartyMaintenanceDto("CP002", "SCB-UAE", "Standard Chartered Bank", "SCB UAE"));
		outstandingSource.add(new CounterpartyMaintenanceDto("CP003", "", "Deutsche Bank AG", "Deutsche Bank"));
		outstandingSource.add(new CounterpartyMaintenanceDto("CP004", "", "BNP Paribas", "BNP Paribas UAE"));
		outstandingSource.add(new CounterpartyMaintenanceDto("CP005", "", "First Abu Dhabi Bank", "FAB"));
		outstandingSource.add(new CounterpartyMaintenanceDto("CP006", "", "Mashreq Bank PSC", "Mashreq"));
	}

	private void saveInternal(CounterpartyMaintenanceDto dto, String userid) {
		CounterpartyMaintenanceDto stored = dto.copy();
		stored.setCreateUser(userid);
		stored.setCreateTime(new Date());
		stored.setPresentInMaintenance(true);
		maintained.put(normalize(stored.getCounterpartyCode()), stored);
	}

	public List<CounterpartyMaintenanceDto> getAllMaintained() {
		List<CounterpartyMaintenanceDto> list = new ArrayList<CounterpartyMaintenanceDto>();
		for (CounterpartyMaintenanceDto dto : maintained.values()) {
			list.add(dto.copy());
		}
		list.sort(Comparator.comparing(CounterpartyMaintenanceDto::getCounterpartyCode,
				Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));
		return list;
	}

	public List<CounterpartyMaintenanceDto> getOutstanding(String reportDate) {
		List<CounterpartyMaintenanceDto> list = new ArrayList<CounterpartyMaintenanceDto>();
		for (CounterpartyMaintenanceDto source : outstandingSource) {
			CounterpartyMaintenanceDto row = source.copy();
			row.setReportDate(reportDate);
			boolean inList = isMaintained(row.getCounterpartyCode());
			row.setPresentInMaintenance(inList);
			if (inList) {
				CounterpartyMaintenanceDto saved = getByCode(row.getCounterpartyCode());
				if (saved != null) {
					row.setAliasCounterCode(saved.getAliasCounterCode());
					row.setCounterpartyName(saved.getCounterpartyName());
					row.setAslBankName(saved.getAslBankName());
				}
			}
			list.add(row);
		}
		return list;
	}

	public CounterpartyMaintenanceDto getByCode(String counterpartyCode) {
		CounterpartyMaintenanceDto dto = maintained.get(normalize(counterpartyCode));
		return dto == null ? null : dto.copy();
	}

	public CounterpartyMaintenanceDto getOutstandingByCode(String counterpartyCode, String reportDate) {
		if (counterpartyCode == null) {
			return null;
		}
		String key = normalize(counterpartyCode);
		for (CounterpartyMaintenanceDto source : outstandingSource) {
			if (key.equals(normalize(source.getCounterpartyCode()))) {
				CounterpartyMaintenanceDto row = source.copy();
				row.setReportDate(reportDate);
				row.setPresentInMaintenance(isMaintained(counterpartyCode));
				return row;
			}
		}
		return null;
	}

	public boolean isMaintained(String counterpartyCode) {
		return maintained.containsKey(normalize(counterpartyCode));
	}

	public String save(CounterpartyMaintenanceDto dto, String userid) {
		if (dto == null || isBlank(dto.getCounterpartyCode())) {
			return "Counterparty Code is required";
		}
		if (isBlank(dto.getAliasCounterCode())) {
			return "Alias Counter Code is required";
		}
		if (isBlank(dto.getCounterpartyName())) {
			return "Counterparty Name is required";
		}
		if (isBlank(dto.getAslBankName())) {
			return "ASL Bank Name is required";
		}

		String key = normalize(dto.getCounterpartyCode());
		CounterpartyMaintenanceDto existing = maintained.get(key);
		CounterpartyMaintenanceDto stored = dto.copy();
		stored.setCounterpartyCode(dto.getCounterpartyCode().trim());
		stored.setAliasCounterCode(dto.getAliasCounterCode().trim());
		stored.setCounterpartyName(dto.getCounterpartyName().trim());
		stored.setAslBankName(dto.getAslBankName().trim());
		stored.setPresentInMaintenance(true);

		if (existing == null) {
			stored.setCreateUser(userid);
			stored.setCreateTime(new Date());
			maintained.put(key, stored);
			return "Counterparty saved successfully";
		}

		stored.setCreateUser(existing.getCreateUser());
		stored.setCreateTime(existing.getCreateTime());
		stored.setModifyUser(userid);
		stored.setModifyTime(new Date());
		maintained.put(key, stored);
		return "Counterparty updated successfully";
	}

	private String normalize(String value) {
		return value == null ? "" : value.trim().toUpperCase(Locale.ENGLISH);
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}
}
