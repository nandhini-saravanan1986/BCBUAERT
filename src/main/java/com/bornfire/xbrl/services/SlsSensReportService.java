package com.bornfire.xbrl.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bornfire.xbrl.dto.SlsSensDayTotalsDto;
import com.bornfire.xbrl.dto.SlsSensPositionDateGroupDto;
import com.bornfire.xbrl.dto.SlsSensScenarioDto;
import com.bornfire.xbrl.entities.RT_SLS_SENS_ENTITIES;
import com.bornfire.xbrl.entities.RT_SLS_SENS_Repository;

@Service
public class SlsSensReportService {

	private static final SimpleDateFormat DISPLAY_DATE = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

	@Autowired
	private RT_SLS_SENS_Repository sensRepository;

	public List<SlsSensPositionDateGroupDto> buildPositionDateGroups() {
		List<RT_SLS_SENS_ENTITIES> all = sensRepository.findAllForGroupedList();
		Map<String, SlsSensPositionDateGroupDto> groups = new LinkedHashMap<>();

		for (RT_SLS_SENS_ENTITIES row : all) {
			if (row.getREPORT_DATE() == null) {
				continue;
			}
			String currency = row.getREPORT_CURRENCY() != null ? row.getREPORT_CURRENCY() : "";
			String key = row.getREPORT_DATE().getTime() + "|" + currency;
			SlsSensPositionDateGroupDto group = groups.get(key);
			if (group == null) {
				group = new SlsSensPositionDateGroupDto();
				group.setPositionDate(row.getREPORT_DATE());
				group.setPositionDateFormatted(formatDate(row.getREPORT_DATE()));
				group.setReportCurrency(currency);
				groups.put(key, group);
			}
			group.getScenarios().add(toScenarioDto(row));
		}

		List<SlsSensPositionDateGroupDto> result = new ArrayList<>(groups.values());
		for (SlsSensPositionDateGroupDto g : result) {
			g.getScenarios().sort(Comparator.comparing(s -> s.getSensDayOffset() != null ? s.getSensDayOffset() : 0));
			g.setScenarioCount(g.getScenarios().size());
		}
		result.sort((a, b) -> b.getPositionDate().compareTo(a.getPositionDate()));
		return result;
	}

	public List<SlsSensScenarioDto> findScenariosForPositionDate(Date positionDate, String currency) {
		List<RT_SLS_SENS_ENTITIES> rows = sensRepository.findScenariosByPositionDateAndCurrency(positionDate, currency);
		List<SlsSensScenarioDto> out = new ArrayList<>();
		for (RT_SLS_SENS_ENTITIES row : rows) {
			out.add(toScenarioDto(row));
		}
		out.sort(Comparator.comparing(s -> s.getSensDayOffset() != null ? s.getSensDayOffset() : 0));
		return out;
	}

	public Optional<RT_SLS_SENS_ENTITIES> findScenarioRow(Date positionDate, Integer dayOffset, String currency) {
		if (dayOffset != null) {
			return sensRepository.findByPositionDateAndDayOffsetAndCurrency(positionDate, dayOffset, currency);
		}
		return Optional.empty();
	}

	public Optional<RT_SLS_SENS_ENTITIES> findScenarioRowByAsOf(Date positionDate, Date asOfDate, String currency) {
		return sensRepository.findByPositionDateAndAsOfDateAndCurrency(positionDate, asOfDate, currency);
	}

	public SlsSensScenarioDto toScenarioDto(RT_SLS_SENS_ENTITIES row) {
		SlsSensScenarioDto dto = new SlsSensScenarioDto();
		Date position = row.getREPORT_DATE();
		Integer offset = row.getSENS_DAY_OFFSET();
		if (offset == null) {
			offset = 0;
		}
		Date asOf = row.getAS_OF_DATE();
		if (asOf == null && position != null) {
			asOf = addCalendarDays(position, offset);
		}
		dto.setPositionDate(position);
		dto.setPositionDateFormatted(formatDate(position));
		dto.setAsOfDate(asOf);
		dto.setAsOfDateFormatted(formatDate(asOf));
		dto.setSensDayOffset(offset);
		dto.setReportCurrency(row.getREPORT_CURRENCY());
		dto.setBaseScenario(offset == 0);
		dto.setScenarioLabel(scenarioLabel(offset));
		return dto;
	}

	public static String scenarioLabel(Integer offset) {
		if (offset == null || offset == 0) {
			return "Base (Day 0)";
		}
		return "Day +" + offset;
	}

	public static Date addCalendarDays(Date base, int days) {
		if (base == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(base);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}

	public static String formatDate(Date d) {
		if (d == null) {
			return "";
		}
		synchronized (DISPLAY_DATE) {
			return DISPLAY_DATE.format(d);
		}
	}

	public Map<String, Object> buildSevenDayTotalsSummary(Date positionDate, String currency) {
		Map<String, Object> result = new LinkedHashMap<>();
		List<SlsSensDayTotalsDto> days = new ArrayList<>();
		String outflowLabel = "Total Outflows";
		String inflowLabel = "Total Inflows";

		for (int offset = 0; offset <= 6; offset++) {
			Optional<RT_SLS_SENS_ENTITIES> rowOpt = findScenarioRow(positionDate, offset, currency);
			SlsSensDayTotalsDto day = new SlsSensDayTotalsDto();
			day.setSensDayOffset(offset);
			day.setScenarioLabel(scenarioLabel(offset));

			if (rowOpt.isPresent()) {
				RT_SLS_SENS_ENTITIES row = rowOpt.get();
				SlsSensScenarioDto scenario = toScenarioDto(row);
				day.setAsOfDateFormatted(scenario.getAsOfDateFormatted());
				day.setTotalOutflows(row.getR39_TOTAL());
				day.setTotalInflows(row.getR70_TOTAL());
				if (offset == 0) {
					if (row.getR39_PRODUCT() != null && !row.getR39_PRODUCT().trim().isEmpty()) {
						outflowLabel = row.getR39_PRODUCT().trim();
					}
					if (row.getR70_PRODUCT() != null && !row.getR70_PRODUCT().trim().isEmpty()) {
						inflowLabel = row.getR70_PRODUCT().trim();
					}
				}
			} else {
				day.setAsOfDateFormatted(formatDate(addCalendarDays(positionDate, offset)));
				day.setTotalOutflows(null);
				day.setTotalInflows(null);
			}
			days.add(day);
		}

		result.put("success", true);
		result.put("positionDate", formatDate(positionDate));
		result.put("currency", currency);
		result.put("outflowLabel", outflowLabel);
		result.put("inflowLabel", inflowLabel);
		result.put("days", days);
		return result;
	}
}
