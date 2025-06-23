package com.bornfire.xbrl.controllers;



import java.time.LocalDate;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bornfire.xbrl.services.Excel_Services;

@RestController
public class MIS_Rest_Controller {
	private static final Logger logger = LoggerFactory.getLogger(MIS_Rest_Controller.class);

    @Autowired
    private Excel_Services excelServices;

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
    public void final_sheet(HttpServletResponse response,
            @RequestParam("ReportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ReportDate) {
        try {
            byte[] fileData = excelServices.generate_final_sheet(ReportDate);
            logger.info("Generated file size: {}", fileData.length);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"Final Sheet.xlsx\"");
            response.getOutputStream().write(fileData);
            response.getOutputStream().flush();
        } catch (Exception e) {
            logger.error("Error while generating Final sheet Excel file", e);
        }
    }
    @GetMapping("/download/Exposure")
    public void Exposure(HttpServletResponse response,
            @RequestParam("ReportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ReportDate,
            @RequestParam(required = false) String branch) {
        try {
            byte[] fileData = excelServices.generate_Exposure(ReportDate, branch);
            logger.info("Generated file size: {}", fileData.length);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"Exposure Data.xlsx\"");
            response.getOutputStream().write(fileData);
            response.getOutputStream().flush();
        } catch (Exception e) {
            logger.error("Error while generating Exposure Excel file", e);
        }
    }
    
    
    @GetMapping("/download/Placement")
    public void Placement(HttpServletResponse response,
            @RequestParam("ReportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ReportDate) {
        try {
            byte[] fileData = excelServices.generate_Placement(ReportDate);
            logger.info("Generated file size: {}", fileData.length);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"Treasury Placement.xlsx\"");
            response.getOutputStream().write(fileData);
            response.getOutputStream().flush();
        } catch (Exception e) {
            logger.error("Error while generating Exposure Excel file", e);
        }
    }

    @GetMapping("/download/treasuryLimit")
    public void treasuryLimit(HttpServletResponse response,
            @RequestParam("ReportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ReportDate) {
        try {
            byte[] fileData = excelServices.treasuryLimit(ReportDate);
            logger.info("Generated file size: {}", fileData.length);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"Treasury Limit.xlsx\"");
            response.getOutputStream().write(fileData);
            response.getOutputStream().flush();
        } catch (Exception e) {
            logger.error("Error while generating Exposure Excel file", e);
        }
    }
    
    
    @GetMapping("/download/SettlementLimit")
    public void SettlementLimit(HttpServletResponse response,
            @RequestParam("ReportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ReportDate) {
        try {
            byte[] fileData = excelServices.SettlementLimit(ReportDate);
            logger.info("Generated file size: {}", fileData.length);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"Settlement Limit.xlsx\"");
            response.getOutputStream().write(fileData);
            response.getOutputStream().flush();
        } catch (Exception e) {
            logger.error("Error while generating Exposure Excel file", e);
        }
    }
    

}


