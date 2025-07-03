package com.bornfire.xbrl.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bornfire.xbrl.services.AuditService;

@Controller
public class AuditController {
	
	@Autowired
	AuditService auditService;

	@RequestMapping(value = "User_Audit", method = RequestMethod.GET)
	public String Service_Audit(Model md, HttpServletRequest req) {
		String userid = (String) req.getSession().getAttribute("USERID");
		System.out.println("The login userid is : " + userid);

		LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		System.out.println("The time is " + localDateTime);

		md.addAttribute("menu", "Audit");

		// Add both lists to the model
		md.addAttribute("auditlogss", auditService.getAuditservices());
		//md.addAttribute("userAuditLevels", auditService.getUserAuditLevelList());

		return "User_Audit";
	}

}
