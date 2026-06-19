package com.bornfire.xbrl.controllers;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	
	

}
