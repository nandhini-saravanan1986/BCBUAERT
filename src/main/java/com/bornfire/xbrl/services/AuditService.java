package com.bornfire.xbrl.services;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import javax.servlet.http.HttpServletRequest;

import javax.persistence.Table;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bornfire.xbrl.entities.AuditServicesEntity;
import com.bornfire.xbrl.entities.AuditServicesRep;
import com.bornfire.xbrl.entities.MC_Service_audit_entity;
import com.bornfire.xbrl.entities.Service_audit_table_Rep;
import com.bornfire.xbrl.entities.Service_audit_table_entity;
import com.bornfire.xbrl.entities.UserProfile;
import com.bornfire.xbrl.entities.MC_Service_audit_Repo;

@Service
public class AuditService {

	@Autowired
	private AuditServicesRep auditServicesRep;
	
	  @Autowired
	Service_audit_table_Rep Service_audit_table_Reps;
	@Autowired
	MC_Service_audit_Repo MC_Service_audit_Repo;

	public List<AuditServicesEntity> getUserServices(Date reportdata) {
//		System.out.println(auditServicesRep.getUserAudit());
		return auditServicesRep.getUserAudit(reportdata);
	}

	public List<AuditServicesEntity> getAuditServices() {
//		System.out.println(auditServicesRep.getServiceAudit());
		return auditServicesRep.getServiceAudit();	
	}

	public void createLoginAudit(final String customerId, final String functionCode, final String screenName,
			final Map<String, String> changeDetails, final String tableName, final UserProfile user) {
		try {
			final UUID auditID = UUID.randomUUID();
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			String userId = null;
			String username = null;
			if (attr != null) {
				HttpServletRequest request = attr.getRequest();
				userId = (String) request.getSession().getAttribute("USERID");
				username = (String) request.getSession().getAttribute("USERNAME");
			}
			final Date currentDate = new Date();

			AuditServicesEntity audit = new AuditServicesEntity();
			audit.setAudit_ref_no(auditID.toString());
			audit.setAudit_date(currentDate);
			audit.setEntry_time(currentDate);
			audit.setEntry_user(user.getUserid());
			audit.setEntry_user_name(user.getUsername());
			audit.setFunc_code(functionCode);
			audit.setAudit_table(tableName);
			audit.setAudit_screen(screenName);
			audit.setEvent_id(user.getUserid());
			audit.setEvent_name(user.getUsername());

			if (changeDetails != null && !changeDetails.isEmpty()) {
				StringBuilder changes = new StringBuilder();
				changeDetails
						.forEach((field, value) -> changes.append(field).append(": ").append(value).append("||| "));
			
	            audit.setChange_details(changes.toString()); 
			}

			if ("VERIFY".equalsIgnoreCase(functionCode)) {
				audit.setAuth_user(userId);
				audit.setAuth_user_name(username);
				audit.setAuth_time(currentDate);
			}

			audit.setReport_id(customerId);

//			System.out.println(audit);
			auditServicesRep.save(audit);

		} catch (Exception e) {
			System.err.println("Error creating business audit: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void createBusinessAudit(final String customerId, final String functionCode, final String screenName,
			final Map<String, String> changeDetails, final String tableName) {
		try {
			System.out.println("Entered Service Audit");
			final UUID auditID = UUID.randomUUID();
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			//System.out.println("ServletRequestAttributes : "+attr);
			String userId = null;
			String username = null;
			if (attr != null) {
				HttpServletRequest request = attr.getRequest();
				userId = (String) request.getSession().getAttribute("USERID");
				username = (String) request.getSession().getAttribute("USERNAME");
			}
			final Date currentDate = new Date();

			Service_audit_table_entity audit = new Service_audit_table_entity();
			audit.setAudit_ref_no(auditID.toString());
			audit.setAudit_date(currentDate);
			audit.setEntry_time(currentDate);
			audit.setEntry_user(userId);
			audit.setEntry_user_name(username);
			audit.setFunc_code(functionCode);
			audit.setAudit_table(tableName);
			audit.setAudit_screen(screenName);
			audit.setEvent_id(userId);
			audit.setEvent_name(username);

			if (changeDetails != null && !changeDetails.isEmpty()) {
				StringBuilder changes = new StringBuilder();
				changeDetails
						.forEach((field, value) -> changes.append(field).append(": ").append(value).append("||| "));
			
	            audit.setModi_details(changes.toString()); 
			}

			if ("VERIFY".equalsIgnoreCase(functionCode)) {
				audit.setAuth_user(userId);
				audit.setAuth_user_name(username);
				audit.setAuth_time(currentDate);
			}

			audit.setDomain_id(customerId);

//			System.out.println(audit);
			Service_audit_table_Reps.save(audit);

		} catch (Exception e) {
			System.err.println("Error creating business audit: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void createBusinessAuditbackground(final String customerId, final String functionCode, final String screenName,
			final Map<String, String> changeDetails, final String tableName,ServletRequestAttributes attr) {
		try {
			System.out.println("Entered Service Audit");
			final UUID auditID = UUID.randomUUID();
			//ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			//System.out.println("ServletRequestAttributes : "+attr);
			String userId = null;
			String username = null;
			if (attr != null) {
				HttpServletRequest request = attr.getRequest();
				userId = (String) request.getSession().getAttribute("USERID");
				username = (String) request.getSession().getAttribute("USERNAME");
			}
			final Date currentDate = new Date();

			Service_audit_table_entity audit = new Service_audit_table_entity();
			audit.setAudit_ref_no(auditID.toString());
			audit.setAudit_date(currentDate);
			audit.setEntry_time(currentDate);
			audit.setEntry_user(userId);
			audit.setEntry_user_name(username);
			audit.setFunc_code(functionCode);
			audit.setAudit_table(tableName);
			audit.setAudit_screen(screenName);
			audit.setEvent_id(userId);
			audit.setEvent_name(username);

			if (changeDetails != null && !changeDetails.isEmpty()) {
				StringBuilder changes = new StringBuilder();
				changeDetails
						.forEach((field, value) -> changes.append(field).append(": ").append(value).append("||| "));
			
	            audit.setModi_details(changes.toString()); 
			}

			if ("VERIFY".equalsIgnoreCase(functionCode)) {
				audit.setAuth_user(userId);
				audit.setAuth_user_name(username);
				audit.setAuth_time(currentDate);
			}

			audit.setDomain_id(customerId);

//			System.out.println(audit);
			Service_audit_table_Reps.save(audit);

		} catch (Exception e) {
			System.err.println("Error creating business audit: " + e.getMessage());
			e.printStackTrace();
		}
	}


	private boolean areEqual(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null)
			return true;
		if (obj1 == null || obj2 == null)
			return false;
		return obj1.equals(obj2);
	}

	public String fetchChanges(@RequestParam(required = false) String audit_ref_no) {

	return auditServicesRep.getchanges(audit_ref_no); 
	}

	public <T> void compareEntitiesmanual(T oldEntity, T newEntity, String id_values, String Screenname,
			String tableName) {
		System.out.println("Screen Name: " + Screenname);

		if (oldEntity == null || newEntity == null) {
			throw new IllegalArgumentException("Entities cannot be null");
		}

		// Check if both entities are of the same type
		if (!oldEntity.getClass().equals(newEntity.getClass())) {
			throw new IllegalArgumentException(
					"Entities must be of the same type. " + "Old entity type: " + oldEntity.getClass().getSimpleName()
							+ ", New entity type: " + newEntity.getClass().getSimpleName());
		}
		List<String> ignoreFields = Arrays.asList("createdAt", "updatedAt", "modifiedAt", "createdBy", "modifiedBy",
				"lastModifiedDate", "lastModifiedBy", "version");
		List<String> lowerCaseIgnoreFields = new ArrayList<String>();
		if (ignoreFields != null) {
			for (String field : ignoreFields) {
				lowerCaseIgnoreFields.add(field.toLowerCase());
			}
		}

		// Compare entities and build changes string
		StringBuilder changes = new StringBuilder();
		Class<?> clazz = oldEntity.getClass();
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			// Skip if field is in ignore list (case-insensitive)
			if (lowerCaseIgnoreFields.contains(field.getName().toLowerCase())) {
				continue;
			}

			field.setAccessible(true);
			try {
				Object oldValue = field.get(oldEntity);
				Object newValue = field.get(newEntity);

				if (!areEqual(oldValue, newValue)) {
					if (changes.length() > 0) {
						changes.append("|||");
					}
					changes.append(field.getName()).append(": OldValue: ").append(oldValue).append(", NewValue: ")
							.append(newValue);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Error accessing field: " + field.getName(), e);
			}
		}

		System.out.println("Changes in Audit : " + changes);

		System.out.println("Entered Dynamic  Modify Service Audit");
		final UUID auditID = UUID.randomUUID();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		String userId = null;
		String username = null;
		if (attr != null) {
			HttpServletRequest request = attr.getRequest();
			userId = (String) request.getSession().getAttribute("USERID");
			username = (String) request.getSession().getAttribute("USERNAME");
		}
		final Date currentDate = new Date();

		Service_audit_table_entity audit = new Service_audit_table_entity();
		audit.setAudit_ref_no(auditID.toString());
		audit.setAudit_date(currentDate);
		audit.setEntry_time(currentDate);
		audit.setEntry_user(userId);
		audit.setEntry_user_name(username);
		audit.setFunc_code("MODIFY");
		audit.setAudit_table(tableName);
		audit.setAudit_screen(Screenname);
		audit.setEvent_id(userId);
		audit.setEvent_name(username);
		audit.setModi_details(changes.toString());
		audit.setDomain_id(id_values);

		Service_audit_table_Reps.save(audit);

	}

	public <T> void compareMCEntitiesmanual(T oldEntity, T newEntity, String id_values,String auditType, String Screenname,
			String tableName, String modifyheader, String remarks) {
		System.out.println("Screen Name: " + Screenname);

		if (oldEntity == null || newEntity == null) {
			throw new IllegalArgumentException("Entities cannot be null");
		}

		if (!oldEntity.getClass().equals(newEntity.getClass())) {
			throw new IllegalArgumentException(
					"Entities must be of the same type. " + "Old entity type: " + oldEntity.getClass().getSimpleName()
							+ ", New entity type: " + newEntity.getClass().getSimpleName());
		}

		List<String> ignoreFields = Arrays.asList("createdAt", "updatedAt", "modifiedAt", "createdBy", "modifiedBy",
				"lastModifiedDate", "lastModifiedBy", "version", "modifyFlg", "verifyFlg", "columnHeader",
				"rowCol2Value", "formMode", "reportDate", "cellId", "cellName");
		List<String> lowerCaseIgnoreFields = new ArrayList<>();
		if (ignoreFields != null) {
			for (String field : ignoreFields) {
				lowerCaseIgnoreFields.add(field.toLowerCase());
			}
		}

		List<String> fieldNamesList = Arrays.asList("justification", "dataValue", "source", "remarks");
		List<String> equivalentValuesList = Arrays.asList("Justification", "Value", "Source", "Checker Remarks");
		StringBuilder changes = new StringBuilder();

		List<String> changedFieldNames = new ArrayList<>();
		List<Object> changedOldValues = new ArrayList<>();
		List<Object> changedNewValues = new ArrayList<>();

		Class<?> clazz = oldEntity.getClass();
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			if (lowerCaseIgnoreFields.contains(field.getName().toLowerCase())) {
				continue;
			}

			field.setAccessible(true);
			try {
				Object oldValue = field.get(oldEntity);
				Object newValue = field.get(newEntity);

				boolean isOldEmpty = (oldValue == null || oldValue.toString().trim().isEmpty());
				boolean isNewEmpty = (newValue == null || newValue.toString().trim().isEmpty());

				if (isOldEmpty && isNewEmpty) {
					continue;
				}
				
				if (!areEqual(oldValue, newValue)) {

					String finalFieldName = field.getName();
					int matchIndex = fieldNamesList.indexOf(finalFieldName);

					if (matchIndex != -1 && matchIndex < equivalentValuesList.size()) {
						finalFieldName = equivalentValuesList.get(matchIndex);
					}

					if (changes.length() > 0) {
						changes.append("|||");
					}

					changes.append(finalFieldName).append(": OldValue: ").append(oldValue).append(", NewValue: ")
							.append(newValue);

					changedFieldNames.add(finalFieldName);
					changedOldValues.add(oldValue);
					changedNewValues.add(newValue);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Error accessing field: " + field.getName(), e);
			}
		}

		System.out.println("Entered MC Modify Service Audit");
		final UUID auditID = UUID.randomUUID();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		String userId = null;
		String username = null;
		String sessionId = null;
		if (attr != null) {
			HttpServletRequest request = attr.getRequest();
			userId = (String) request.getSession().getAttribute("USERID");
			username = (String) request.getSession().getAttribute("USERNAME");
			sessionId = request.getSession().getId();
		}

		final Date currentDate = new Date();

		MC_Service_audit_entity audit = new MC_Service_audit_entity();
		audit.setAudit_ref_no(auditID.toString());
		audit.setAudit_date(currentDate);
		audit.setEntry_time(currentDate);
		audit.setEntry_user(userId);
		audit.setEntry_user_name(username);
		audit.setFunc_code(auditType);
		audit.setAudit_table(tableName);
		audit.setAudit_screen(Screenname);
		audit.setEvent_id(userId);
		audit.setEvent_name(username);
		audit.setModi_details(changes.toString());
		audit.setDomain_id(id_values);
		audit.setRemarks(remarks);
		audit.setFIELD_HEADER(modifyheader);
		audit.setSession_id(sessionId);
		boolean ismodified = (changes.toString() == null || changes.toString().trim().isEmpty());

		if (ismodified) {
			audit.setISMODIFIED("N");
		} else {
			audit.setISMODIFIED("Y");
		}
		MC_Service_audit_Repo.save(audit);
	}

	public <T> void auditAddMCEntitymanual(T newEntity, String id_values, String Screenname, String tableName,
			String modifyheader, String remarks) {
		System.out.println("Screen Name: " + Screenname);

		if (newEntity == null) {
			throw new IllegalArgumentException("Entity cannot be null for ADD operation");
		}

		List<String> ignoreFields = Arrays.asList("id","createdAt", "updatedAt", "modifiedAt", "createdBy", "modifiedBy",
				"lastModifiedDate", "lastModifiedBy", "version", "modifyFlg", "verifyFlg", "columnHeader",
				"rowCol2Value", "formMode", "reportDate", "cellId", "cellName");

		List<String> lowerCaseIgnoreFields = new ArrayList<>();
		if (ignoreFields != null) {
			for (String field : ignoreFields) {
				lowerCaseIgnoreFields.add(field.toLowerCase());
			}
		}

		List<String> fieldNamesList = Arrays.asList("justification", "dataValue", "source", "remarks");
		List<String> equivalentValuesList = Arrays.asList("Justification", "Value", "Source", "Checker Remarks");

		StringBuilder addedDetails = new StringBuilder();

		Class<?> clazz = newEntity.getClass();
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			if (lowerCaseIgnoreFields.contains(field.getName().toLowerCase())) {
				continue;
			}

			field.setAccessible(true);
			try {
				Object newValue = field.get(newEntity);

				if (newValue != null && !String.valueOf(newValue).trim().isEmpty()) {

					String finalFieldName = field.getName();
					int matchIndex = fieldNamesList.indexOf(finalFieldName);

					if (matchIndex != -1 && matchIndex < equivalentValuesList.size()) {
						finalFieldName = equivalentValuesList.get(matchIndex);
					}

					if (addedDetails.length() > 0) {
						addedDetails.append("|||");
					}

					addedDetails.append(finalFieldName).append(": OldValue: -").append(", NewValue: ").append(newValue);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Error accessing field: " + field.getName(), e);
			}
		}

		System.out.println("Entered MC Add Service Audit");
		final UUID auditID = UUID.randomUUID();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		String userId = null;
		String username = null;
		String sessionId = null;
		if (attr != null) {
			HttpServletRequest request = attr.getRequest();
			userId = (String) request.getSession().getAttribute("USERID");
			username = (String) request.getSession().getAttribute("USERNAME");
			sessionId = request.getSession().getId();
		}

		final Date currentDate = new Date();

		MC_Service_audit_entity audit = new MC_Service_audit_entity();
		audit.setAudit_ref_no(auditID.toString());
		audit.setAudit_date(currentDate);
		audit.setEntry_time(currentDate);
		audit.setEntry_user(userId);
		audit.setEntry_user_name(username);
		audit.setFunc_code("ADD");
		audit.setAudit_table(tableName);
		audit.setAudit_screen(Screenname);
		audit.setEvent_id(userId);
		audit.setEvent_name(username);
		audit.setModi_details(addedDetails.toString());
		audit.setDomain_id(id_values);
		audit.setRemarks(remarks);
		audit.setFIELD_HEADER(modifyheader);
		audit.setSession_id(sessionId);
		audit.setISMODIFIED("Y");
		MC_Service_audit_Repo.save(audit);
	}

	public void auditMCEntitymanual(String fun_code, String id_values, String Screenname, String tableName,
			String modifyheader, String remarks, String changedRemarks) {
		System.out.println("Screen Name: " + Screenname);
		System.out.println("Entered MC Common Service Audit");
		final UUID auditID = UUID.randomUUID();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		String userId = null;
		String username = null;
		String sessionId = null;
		if (attr != null) {
			HttpServletRequest request = attr.getRequest();
			userId = (String) request.getSession().getAttribute("USERID");
			username = (String) request.getSession().getAttribute("USERNAME");
			sessionId = request.getSession().getId();
		}

		final Date currentDate = new Date();

		MC_Service_audit_entity audit = new MC_Service_audit_entity();
		audit.setAudit_ref_no(auditID.toString());
		audit.setAudit_date(currentDate);
		audit.setEntry_time(currentDate);
		audit.setEntry_user(userId);
		audit.setEntry_user_name(username);
		audit.setFunc_code(fun_code);
		audit.setAudit_table(tableName);
		audit.setAudit_screen(Screenname);
		audit.setEvent_id(userId);
		audit.setEvent_name(username);
		audit.setDomain_id(id_values);
		audit.setRemarks(remarks);
		audit.setFIELD_HEADER(modifyheader);
		audit.setSession_id(sessionId);
		boolean ismodified = (changedRemarks == null || changedRemarks.toString().trim().isEmpty());
		if (ismodified) {
			audit.setISMODIFIED("N");
		} else {
			audit.setISMODIFIED("Y");
		}

		MC_Service_audit_Repo.save(audit);
	}

}
