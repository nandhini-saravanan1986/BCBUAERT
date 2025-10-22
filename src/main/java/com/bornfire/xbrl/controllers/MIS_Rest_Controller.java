package com.bornfire.xbrl.controllers;



import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bornfire.xbrl.entities.Mis_exposure_bill_detail_entity;
import com.bornfire.xbrl.services.AuditService;
import com.bornfire.xbrl.services.Excel_Services;
import com.bornfire.xbrl.services.counter_services;

@RestController
public class MIS_Rest_Controller {
	private static final Logger logger = LoggerFactory.getLogger(MIS_Rest_Controller.class);

    @Autowired
    private Excel_Services excelServices;
    
    @Autowired
    AuditService auditService;
    
    @Autowired
    counter_services counter_services;

    @GetMapping("/download/excel")
    public void downloadExcel(HttpServletResponse response,@RequestParam(required = false) String mode) {


    	if(mode.equals("exposure")) {
        try {
            logger.info("Template download for Exposure Data...");
            byte[] fileData = excelServices.generateExcel();
            logger.info("Generated file size: {}", fileData.length);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"Exposure Data.xlsx\"");
            response.getOutputStream().write(fileData);
            response.getOutputStream().flush();
        } catch (Exception e) {
            logger.error("Error while generating  Excel file For Exposure Data", e);
        }
        }else if(mode.equals("placement")) {
        	  try {
                  logger.info("Template download for Treasury Placement...");
                  byte[] fileData = excelServices.generateExcel_placement();
                  logger.info("Generated file size: {}", fileData.length);

                  response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                  response.setHeader("Content-Disposition", "attachment; filename=\"Treasury Placements.xlsx\"");
                  response.getOutputStream().write(fileData);
                  response.getOutputStream().flush();
              } catch (Exception e) {
                  logger.error("Error while generating for Treasury Placement", e);
              }
        	
        }else if(mode.equals("settlement")) {
        	  try {
                  logger.info("Template download for settlement...");
                  byte[] fileData = excelServices.generateExcel_Settlement();
                  logger.info("Generated file size: {}", fileData.length);

                  response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                  response.setHeader("Content-Disposition", "attachment; filename=\"Settlement.xlsx\"");
                  response.getOutputStream().write(fileData);
                  response.getOutputStream().flush();
              } catch (Exception e) {
                  logger.error("Error while generating  Excel file for settlement", e);
              }
        	
        }
    }
    
    @GetMapping("/download/final")
    public void final_sheet(HttpServletResponse response,HttpServletRequest req,
            @RequestParam("ReportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ReportDate) {
        try {
            byte[] fileData = excelServices.generate_final_sheet(ReportDate);
            logger.info("Generated file size: {}", fileData.length);
            String userid = (String) req.getSession().getAttribute("USERID");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition",
            	    "attachment; filename=\"Counterparty Bank Exposure (ASL) statement " + ReportDate + ".xlsx\"");
            response.getOutputStream().write(fileData);
            response.getOutputStream().flush();
            auditService.createBusinessAudit(userid, "DOWNLOAD", "Final Sheet", null,"MIS_BANK_LIMITS");
        } catch (Exception e) {
            logger.error("Error while generating Final sheet Excel file", e);
        }
    }
    
    @GetMapping("/download/Download_detail_report")
    public void Download_detail_report(HttpServletResponse response,HttpServletRequest req,
            @RequestParam("ReportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ReportDate) {
        try {
            byte[] fileData = excelServices.Generate_detail_statement(ReportDate);
            logger.info("Generated file size: {}", fileData.length);
            String userid = (String) req.getSession().getAttribute("USERID");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition",
            	    "attachment; filename=\"Counterparty Bank Exposure (ASL) detail statement " + ReportDate + ".xlsx\"");
            response.getOutputStream().write(fileData);
            response.getOutputStream().flush();
            auditService.createBusinessAudit(userid, "DOWNLOAD", "Final Sheet", null,"MIS_BANK_LIMITS");
        } catch (Exception e) {
            logger.error("Error while generating Final sheet Excel file", e);
        }
    }
    
    @GetMapping("/download/Exposure")
    public void Exposure(HttpServletResponse response,HttpServletRequest req,
            @RequestParam("ReportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ReportDate,
            @RequestParam(required = false) String branch) {
    	String userid = (String) req.getSession().getAttribute("USERID");
        try {
            byte[] fileData = excelServices.generate_Exposure(ReportDate, branch);
            logger.info("Generated file size: {}", fileData.length);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"Exposure Data.xlsx\"");
            response.getOutputStream().write(fileData);
            response.getOutputStream().flush();
            auditService.createBusinessAudit(userid, "DOWNLOAD", "Exposure Data", null,"MIS_ASL_DETAIL_REPORT");
        } catch (Exception e) {
            logger.error("Error while generating Exposure Excel file", e);
        }
    }
    
    
    @GetMapping("/download/Placement")
    public void Placement(HttpServletResponse response,HttpServletRequest req,
            @RequestParam("ReportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ReportDate) {
    	
        try {
            byte[] fileData = excelServices.generate_Placement(ReportDate);
            logger.info("Generated file size: {}", fileData.length);
            String userid = (String) req.getSession().getAttribute("USERID");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"Treasury Placement.xlsx\"");
            response.getOutputStream().write(fileData);
            response.getOutputStream().flush();
            auditService.createBusinessAudit(userid, "DOWNLOAD", "Treasury Placement", null,"MIS_TREASURY_PLACEMENT");
        } catch (Exception e) {
            logger.error("Error while generating Exposure Excel file", e);
        }
    }

    @GetMapping("/download/treasuryLimit")
    public void treasuryLimit(HttpServletResponse response,HttpServletRequest req,
            @RequestParam("ReportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ReportDate) {
    	String userid = (String) req.getSession().getAttribute("USERID");
        try {
            byte[] fileData = excelServices.treasuryLimit(ReportDate);
            logger.info("Generated file size: {}", fileData.length);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"Treasury Limit.xlsx\"");
            response.getOutputStream().write(fileData);
            response.getOutputStream().flush();
            auditService.createBusinessAudit(userid, "DOWNLOAD", "Treasury Limit", null,"MIS_TREASURY_LIMITS");
        } catch (Exception e) {
            logger.error("Error while generating Exposure Excel file", e);
        }
    }
    
    
    @GetMapping("/download/SettlementLimit")
    public void SettlementLimit(HttpServletResponse response,HttpServletRequest req,
            @RequestParam("ReportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ReportDate) {
    	String userid = (String) req.getSession().getAttribute("USERID");
        try {
            byte[] fileData = excelServices.SettlementLimit(ReportDate);
            logger.info("Generated file size: {}", fileData.length);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"Settlement Limit.xlsx\"");
            response.getOutputStream().write(fileData);
            response.getOutputStream().flush();
            auditService.createBusinessAudit(userid, "DOWNLOAD", "Settlement Limit", null,"MIS_SETTLEMENT");
        } catch (Exception e) {
            logger.error("Error while generating Exposure Excel file", e);
        }
    }
    
	@PostMapping("/Exposurebillservice")
	@ResponseBody
	public String Exposurebillservice(@ModelAttribute Mis_exposure_bill_detail_entity Mis_exposure_bill_detail_entity, Model md,
			HttpServletResponse response,HttpServletRequest rq, @RequestParam(required = false) String formmode) {
		logger.info("==> Entered Exposure bill service api controller");
		
		try {
			
			String msg = counter_services.Exposurebillservice(Mis_exposure_bill_detail_entity, formmode, rq);
			logger.info("✅ Returning message to UI: {}", msg);
			return msg;
		} catch (Exception e) {
			logger.error("Error occurred while Add  New Bill: {}", e.getMessage(), e);
			return e.getMessage();
		}
	}

}


