package com.bornfire.xbrl.controllers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bornfire.xbrl.entities.MC_Service_audit_Repo;
import com.bornfire.xbrl.entities.MC_Service_audit_entity;
import com.bornfire.xbrl.entities.Service_audit_table_Rep;
import com.bornfire.xbrl.entities.Service_audit_table_entity;
import com.bornfire.xbrl.services.AuditService;

@Controller
public class AuditController {
	
	@Autowired
	AuditService auditService;
	  @Autowired
	Service_audit_table_Rep Service_audit_table_Reps;
		
	
	@RequestMapping(value = "User_Audit", method = RequestMethod.GET)
	public String userAudit(Model md, HttpServletRequest req,@RequestParam(required = false)
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    Date reportdata) {
		String userid = (String) req.getSession().getAttribute("USERID");
		System.out.println("The login userid is : " + userid);

		LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		System.out.println("The time is " + localDateTime);

		md.addAttribute("menu", "Audit");
		
		if(reportdata==null) {
			reportdata=new Date();
		}
		
		 md.addAttribute("reportdata", reportdata);
		 System.out.println("reportdata="+reportdata);
		 
		 md.addAttribute("menu", "Audit");
		// Add both lists to the model
		md.addAttribute("auditlogs", auditService.getUserServices(reportdata));
		//md.addAttribute("userAuditLevels", auditService.getUserAuditLevelList());

		return "User_Audit";
	}
	
	 	
	@RequestMapping(value = "Service_Audit", method = RequestMethod.GET)
	public String ServiceAudit(Model md, HttpServletRequest req, @RequestParam(defaultValue = "0") int page,@RequestParam(value = "date", required = false) String filterDate) {
		String userid = (String) req.getSession().getAttribute("USERID");
		System.out.println("The login userid is : " + userid);
		md.addAttribute("activeMenu", "Admin");
		md.addAttribute("activePage", "Service_Audit");

		LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		System.out.println("The time is " + localDateTime);

		md.addAttribute("menu", "Audit");
		
		if (filterDate == null || filterDate.trim().isEmpty()) {
	        filterDate = LocalDate.now().toString();
	    }
		Pageable pageable = PageRequest.of(page, 10); 
	    
	    Page<Map<String, Object>> masterGroups = Service_audit_table_Reps.findChronologicalGroups(filterDate, pageable);
	    
	    List<Map<String, Object>> groupedAuditList = new ArrayList<>();

	    for (Map<String, Object> groupDbMap : masterGroups.getContent()) {
	        Map<String, Object> groupNode = new HashMap<>();
	        
	        String table = (String) groupDbMap.get("audit_table");
	        String func = (String) groupDbMap.get("func_code");
	        String user = (String) groupDbMap.get("entry_user");
	        Date minTime = parseOracleDate(groupDbMap.get("min_time"));
	        Date maxTime = parseOracleDate(groupDbMap.get("max_time"));

	        groupNode.put("audit_table", table);
	        groupNode.put("func_code", func);
	        groupNode.put("entry_user", user);

	        List<Service_audit_table_entity> details = Service_audit_table_Reps
	            .findDetailRows(table, func, user, minTime, maxTime);
	        
	        groupNode.put("details", details);
	        groupedAuditList.add(groupNode);
	    }

	    md.addAttribute("GroupedAuditList", groupedAuditList);
	    md.addAttribute("currentPage", page);
	    md.addAttribute("totalPages", masterGroups.getTotalPages());
	    
	    md.addAttribute("selectedDate", filterDate);

		return "Service_Audit.html";
	}

	private Date parseOracleDate(Object dateObject) {
		if (dateObject == null) {
			return null;
		}
		if (dateObject instanceof Timestamp) {
			return new Date(((Timestamp) dateObject).getTime());
		}
		if (dateObject instanceof Date) {
			return (Date) dateObject;
		}
		throw new IllegalArgumentException("Unknown date format returned from database: " + dateObject.getClass());
	}
	@RequestMapping(value = "getchanges", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String fetchChanges(@RequestParam(required = false) String audit_ref_no) {

		// Fetch data from the database using the repository
		String changeDetails = auditService.fetchChanges(audit_ref_no); // Example of getting data
		System.out.println(changeDetails);
		// Process the change details to format as required

		return changeDetails; // Return the formatted changes
	}
	
	@GetMapping("/details")
    public ResponseEntity<List<Map<String, String>>> getModifyDetails(@RequestParam("id") String id) {
        
		Optional<Service_audit_table_entity> entity=Service_audit_table_Reps.findById(id);
		System.out.println(entity.get().getModi_details());
        String modify_details = entity.get().getModi_details(); 
        List<Map<String, String>> parsedList = new ArrayList<>();

        if (modify_details != null && !modify_details.trim().isEmpty()) {
            String[] records = modify_details.split("\\|\\|\\|");
            
            for (String record : records) {
                if (record.trim().isEmpty()) continue;
                
                try {
                    int oldValIdx = record.indexOf(": OldValue: ");
                    int newValIdx = record.indexOf(", NewValue: ");
                    
                    String columnName = record.substring(0, oldValIdx).trim();
                    String oldValue = record.substring(oldValIdx + 12, newValIdx).trim();
                    String newValue = record.substring(newValIdx + 12).trim();
                    
                    Map<String, String> rowMap = new HashMap<>();
                    rowMap.put("columnName", columnName);
                    rowMap.put("oldValue", oldValue);
                    rowMap.put("newValue", newValue);
                    
                    parsedList.add(rowMap);
                } catch (Exception e) {
                    System.err.println("Could not parse record fragment: " + record);
                }
            }
        }
        
        return ResponseEntity.ok(parsedList);
	}

	@Autowired
	MC_Service_audit_Repo mc_service_audit_repo;
	@PersistenceContext
	EntityManager entityManager;

	@RequestMapping(value = "MC_Service_Audit", method = RequestMethod.GET)
	public String getServiceAuditLogs(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "sortCol", required = false) String sortCol,
			@RequestParam(value = "sortDir", required = false) String sortDir,
			@RequestParam Map<String, String> allParams, Model model) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<MC_Service_audit_entity> query = cb.createQuery(MC_Service_audit_entity.class);
		Root<MC_Service_audit_entity> root = query.from(MC_Service_audit_entity.class);

		List<Predicate> predicates = buildPredicates(cb, root, keyword, allParams);
		if (!predicates.isEmpty()) {
			query.where(cb.and(predicates.toArray(new Predicate[0])));
		}

		if (sortCol != null && !sortCol.trim().isEmpty()) {
			if ("desc".equalsIgnoreCase(sortDir)) {
				query.orderBy(cb.desc(root.get(sortCol)));
			} else {
				query.orderBy(cb.asc(root.get(sortCol)));
			}
		} else {
			query.orderBy(cb.desc(root.get("entry_time")));
		}

		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<MC_Service_audit_entity> countRoot = countQuery.from(MC_Service_audit_entity.class);

		List<Predicate> countPredicates = buildPredicates(cb, countRoot, keyword, allParams);
		countQuery.select(cb.count(countRoot));
		if (!countPredicates.isEmpty()) {
			countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
		}

		Long totalRows = entityManager.createQuery(countQuery).getSingleResult();

		List<MC_Service_audit_entity> results = entityManager.createQuery(query).setFirstResult(page * size)
				.setMaxResults(size).getResultList();

		Pageable pageable = PageRequest.of(page, size);
		Page<MC_Service_audit_entity> auditPage = new PageImpl<>(results, pageable, totalRows);

		model.addAttribute("page", auditPage);
		model.addAttribute("keyword", keyword);

		return "MC_Service_Audit";
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<MC_Service_audit_entity> root, String keyword,
			Map<String, String> allParams) {
		List<Predicate> predicates = new ArrayList<>();

		if (keyword != null && !keyword.trim().isEmpty()) {
			String likePattern = "%" + keyword.trim().toLowerCase() + "%";
			predicates.add(cb.or(cb.like(cb.lower(root.get("audit_screen")), likePattern),
					cb.like(cb.lower(root.get("func_code")), likePattern),
					cb.like(cb.lower(root.get("entry_user")), likePattern)));
		}

		List<String> ignoredParams = Arrays.asList("page", "size", "keyword", "sortCol", "sortDir", "groupBy", "_");
		for (Map.Entry<String, String> param : allParams.entrySet()) {
			String key = param.getKey();
			String val = param.getValue();

			if (!ignoredParams.contains(key) && val != null && !val.trim().isEmpty()) {

				if ("entry_date".equals(key)) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						Date parsedDate = sdf.parse(val);

						Calendar cal = Calendar.getInstance();
						cal.setTime(parsedDate);
						cal.set(Calendar.HOUR_OF_DAY, 0);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						Date startOfDay = cal.getTime();

						cal.set(Calendar.HOUR_OF_DAY, 23);
						cal.set(Calendar.MINUTE, 59);
						cal.set(Calendar.SECOND, 59);
						Date endOfDay = cal.getTime();

						predicates.add(cb.between(root.get("entry_time"), startOfDay, endOfDay));

					} catch (Exception e) {
						System.out.println("Invalid date format received from UI: " + val);
					}
				} else if ("entry_time".equals(key)) {

				} else {
					if (val.contains(",")) {
						String[] valuesArray = val.split(",");
						CriteriaBuilder.In<String> inClause = cb.in(root.get(key));
						for (String v : valuesArray) {
							inClause.value(v.trim());
						}
						predicates.add(inClause);
					} else {
						predicates.add(cb.equal(root.get(key), val));
					}
				}
			}
		}
		return predicates;
	}

	@ResponseBody
	@RequestMapping(value = "/MC_Service_Audit/distinct", method = RequestMethod.GET)
	public List<String> getDistinctValues(@RequestParam("targetColumn") String targetColumn,
			@RequestParam Map<String, String> allParams) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<String> query = cb.createQuery(String.class);
		Root<MC_Service_audit_entity> root = query.from(MC_Service_audit_entity.class);

		query.select(root.get(targetColumn)).distinct(true);

		List<Predicate> predicates = new ArrayList<>();

		List<String> ignoredParams = Arrays.asList("targetColumn", "page", "size", "keyword", "sortCol", "sortDir",
				"groupBy", "_");

		for (Map.Entry<String, String> param : allParams.entrySet()) {
			String key = param.getKey();
			String val = param.getValue();

			if (!ignoredParams.contains(key) && val != null && !val.trim().isEmpty()) {

				if ("entry_date".equals(key)) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						Date parsedDate = sdf.parse(val);

						Calendar cal = Calendar.getInstance();
						cal.setTime(parsedDate);
						cal.set(Calendar.HOUR_OF_DAY, 0);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						Date startOfDay = cal.getTime();

						cal.set(Calendar.HOUR_OF_DAY, 23);
						cal.set(Calendar.MINUTE, 59);
						cal.set(Calendar.SECOND, 59);
						Date endOfDay = cal.getTime();

						predicates.add(cb.between(root.get("entry_time"), startOfDay, endOfDay));
					} catch (Exception e) {
						System.out.println("Invalid date format in distinct API: " + val);
					}
				} else if ("entry_time".equals(key)) {
				} else {
					if (val.contains(",")) {
						String[] valuesArray = val.split(",");
						CriteriaBuilder.In<String> inClause = cb.in(root.get(key));
						for (String v : valuesArray) {
							inClause.value(v.trim());
						}
						predicates.add(inClause);
					} else {
						predicates.add(cb.equal(root.get(key), val));
					}
				}
			}
		}

		if (!predicates.isEmpty()) {
			query.where(cb.and(predicates.toArray(new Predicate[0])));
		}

		query.orderBy(cb.asc(root.get(targetColumn)));

		return entityManager.createQuery(query).getResultList();
	}
}
