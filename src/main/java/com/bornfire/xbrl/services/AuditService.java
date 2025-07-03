package com.bornfire.xbrl.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bornfire.xbrl.entities.AuditServicesEntity;
import com.bornfire.xbrl.entities.AuditServicesRep;

@Service
public class AuditService {
	
	@Autowired
	AuditServicesRep auditServicesRep;
	
	public List<AuditServicesEntity> getAuditservices() {
		System.out.println(" inside services");
		List<AuditServicesEntity> is = auditServicesRep.getauditService();
		System.out.println(" size is : " + is.size());
		return is;
	}
	
//	public List<UserAuditLevel_Entity> getUserAuditLevelList() {
//        System.out.println("Fetching USER_AUDIT_LEVEL data...");
//        List<UserAuditLevel_Entity> result = userAuditRepo.getUserAuditList();
//        System.out.println("Size: " + result.size());
//        return result;
//    }


}
