package com.bornfire.xbrl.controllers;



import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bornfire.xbrl.entities.Elar_summary_report_entity;
import com.bornfire.xbrl.entities.Elar_summary_report_rep;
import com.bornfire.xbrl.entities.Mis_exposure_bill_detail_entity;
import com.bornfire.xbrl.entities.RT_Chart_pojo;
import com.bornfire.xbrl.entities.RT_Matrix_monitoring_rep;
import com.bornfire.xbrl.entities.RT_RWA_Fund_base_data_entity;
import com.bornfire.xbrl.entities.RT_RWA_Fund_base_data_rep;
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
    
    @Autowired
    RT_Matrix_monitoring_rep RT_Matrix_monitoring_rep;
    
    @Autowired
    Elar_summary_report_rep Elar_summary_report_rep;
    
    @Autowired
    RT_RWA_Fund_base_data_rep RT_RWA_Fund_base_data_rep;

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
	
	@GetMapping("/Getbarchart")
	public List<RT_Chart_pojo> Getbarchart() {
		System.out.println("Bar chart Entered");
	    List<Object[]> getchartval = RT_Matrix_monitoring_rep.GetElar_curryear_report();

	    // Convert Object[] → RT_Chart_pojo
	    List<RT_Chart_pojo> finalList = getchartval.stream()
	            .map(row -> new RT_Chart_pojo(
	                    row[0].toString(),          
	                    (BigDecimal) row[1]         
	            ))
	            .collect(Collectors.toList());
	    System.out.println(finalList.get(0));
	    return finalList;   
	}
	
	@GetMapping("/Getprogresschart")
	public List<RT_Chart_pojo> Getprogresschart() {
		System.out.println("Progress Chart Entered");
	    List<Object[]> getchartval = RT_Matrix_monitoring_rep.GetElarcurrentmonthgraph();

	    // Convert Object[] → RT_Chart_pojo
	    List<RT_Chart_pojo> finalList = getchartval.stream()
	            .map(row -> new RT_Chart_pojo(
	                    row[0].toString(),          
	                    (BigDecimal) row[1]         
	            ))
	            .collect(Collectors.toList());
	    System.out.println(finalList.get(0));
	    return finalList;   
	}
	
	@GetMapping("/GetElarreport")
	public Elar_summary_report_entity GetElarreport(@RequestParam("Report_date") String Report_date) throws ParseException {
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date Selecteddate = dateFormat.parse(Report_date);
		
		Elar_summary_report_entity Elar_summary_report_entity = Elar_summary_report_rep.Getelarreport(Selecteddate);
		
		
		
		return Elar_summary_report_entity;
		
	}
	
	@GetMapping("/Getcategorychart")
	public List<RT_Chart_pojo> Getcategorychart(@RequestParam("Report_date") String Report_date,@RequestParam("Branch_name") String Branch_name) throws ParseException {
		
		List<RT_Chart_pojo> chartList = new ArrayList<>();
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date Selecteddate = dateFormat.parse(Report_date);
		
		List<Object[]> RT_Chart_Data = RT_RWA_Fund_base_data_rep.getcatorywiseportfolio(Selecteddate,Branch_name);
		
		for(Object[] RT_data : RT_Chart_Data) {
			
			RT_Chart_pojo RT_Chart_pojo = new RT_Chart_pojo();
			
			RT_Chart_pojo.setClassification(String.valueOf(RT_data[0] != null ? RT_data[0] : ""));
			RT_Chart_pojo.setExposurebal(new BigDecimal(String.valueOf(RT_data[1] != null ? RT_data[1] : "0.00")));
			RT_Chart_pojo.setExposureperc(new BigDecimal(String.valueOf(RT_data[2] != null ? RT_data[2] : "0.00")));
			
			chartList.add(RT_Chart_pojo);
		}
		
		
		return chartList;
		
	}
	
	@PostMapping("/GetExposuredata")
	@ResponseBody
	public List<RT_RWA_Fund_base_data_entity> GetExposuredata(
	        @RequestParam(value="Report_date",required=false) String reportDate,
	        @RequestParam(value="classification",required=false) String classification,
	        @RequestParam(value="Branch_name",required=false) String Branch_name,
	        @RequestParam(value="Data_table_type",required=false) String Data_table_type) throws ParseException {
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date Selecteddate = dateFormat.parse(reportDate);
		
		System.out.println(Selecteddate);
		List<Object[]>  TopExposuredata = new ArrayList<>();
		
		
		System.out.println(TopExposuredata.size());
		List<RT_RWA_Fund_base_data_entity> RT_RWA_Fund_base = new ArrayList<>();
		
		if(Data_table_type.equals("TopExposuredata")) {
			TopExposuredata = RT_RWA_Fund_base_data_rep.Gettopexpposure(Selecteddate,classification,Branch_name);
		for(Object[] loopingExposuredata : TopExposuredata) {
			
			RT_RWA_Fund_base_data_entity Entitydata = new RT_RWA_Fund_base_data_entity();
			System.out.println(String.valueOf(loopingExposuredata[0]));
			Entitydata.setBranch_name(String.valueOf(loopingExposuredata[0] != null ? loopingExposuredata[0] : ""));
			Entitydata.setAccount_name(String.valueOf(loopingExposuredata[1] != null ? loopingExposuredata[1] : ""));
			Entitydata.setGl_code(String.valueOf(loopingExposuredata[2] != null ? loopingExposuredata[2] : ""));
			Entitydata.setConst_id(String.valueOf(loopingExposuredata[3] != null ? loopingExposuredata[3] : ""));
			Entitydata.setPurpose(String.valueOf(loopingExposuredata[4] != null ? loopingExposuredata[4] : ""));
			Entitydata.setScheme(String.valueOf(loopingExposuredata[5] != null ? loopingExposuredata[5] : ""));
			Entitydata.setRwa_class(String.valueOf(loopingExposuredata[6] != null ? loopingExposuredata[6] : ""));
			Entitydata.setInt_suspense(new BigDecimal(String.valueOf(loopingExposuredata[7] != null ? loopingExposuredata[7] : "0.00")));
			Entitydata.setTot_provision(new BigDecimal(String.valueOf(loopingExposuredata[8] != null ? loopingExposuredata[8] : "0.00")));
			Entitydata.setLimit(new BigDecimal(String.valueOf(loopingExposuredata[9] != null ? loopingExposuredata[9] : "0.00")));
			Entitydata.setBalance(new BigDecimal(String.valueOf(loopingExposuredata[10] != null ? loopingExposuredata[10] : "0.00")));
			Entitydata.setExposure(new BigDecimal(String.valueOf(loopingExposuredata[11] != null ? loopingExposuredata[11] : "0.00")));
			Entitydata.setRw(new BigDecimal(String.valueOf(loopingExposuredata[12] != null ? loopingExposuredata[12] : "0.00")));
			Entitydata.setRwa(new BigDecimal(String.valueOf(loopingExposuredata[13] != null ? loopingExposuredata[13] : "0.00")));
			Entitydata.setTotal_drawn_rwa(new BigDecimal(String.valueOf(loopingExposuredata[14] != null ? loopingExposuredata[14] : "0.00")));
			Entitydata.setTotal_rwa(new BigDecimal(String.valueOf(loopingExposuredata[15] != null ? loopingExposuredata[15] : "0.00")));
			RT_RWA_Fund_base.add(Entitydata);
		}
		}else if(Data_table_type.equals("Branchsnapshot")) {
			
			TopExposuredata = RT_RWA_Fund_base_data_rep.Getbranchportfoliosnap(Selecteddate,Branch_name);
			
			System.out.println(TopExposuredata.size() + " Selected Data Size");
			
			for(Object[] loopingExposuredata : TopExposuredata) {
			RT_RWA_Fund_base_data_entity Entitydata = new RT_RWA_Fund_base_data_entity();
				
				Entitydata.setBranch_name(String.valueOf(loopingExposuredata[0] != null ? loopingExposuredata[0] : ""));
				Entitydata.setBalance(new BigDecimal(String.valueOf(loopingExposuredata[1] != null ? loopingExposuredata[1] : "0.00"))); // This balance is having the total Exposure value (if branch or overall as per query)
				Entitydata.setTotal_rwa(new BigDecimal(String.valueOf(loopingExposuredata[2] != null ? loopingExposuredata[2] : "0.00"))); // This balance is having the total RWA (if branch or overall as per query)
				Entitydata.setLimit(new BigDecimal(String.valueOf(loopingExposuredata[3] != null ? loopingExposuredata[3] : "0.00"))); // Branch Level ratio
				Entitydata.setAdjusted_fdr(new BigDecimal(String.valueOf(loopingExposuredata[4] != null ? loopingExposuredata[4] : "0.00")));
				Entitydata.setCrm(new BigDecimal(String.valueOf(loopingExposuredata[5] != null ? loopingExposuredata[5] : "0.00")));
				Entitydata.setCrm_adj_bal(new BigDecimal(String.valueOf(loopingExposuredata[6] != null ? loopingExposuredata[6] : "0.00")));
				Entitydata.setCrm_gnt_adj_bal(new BigDecimal(String.valueOf(loopingExposuredata[7] != null ? loopingExposuredata[7] : "0.00")));
				Entitydata.setRw(new BigDecimal(String.valueOf(loopingExposuredata[8] != null ? loopingExposuredata[8] : "0.00")));
				Entitydata.setRwa(new BigDecimal(String.valueOf(loopingExposuredata[9] != null ? loopingExposuredata[9] : "0.00")));
				Entitydata.setBill_amount(new BigDecimal(String.valueOf(loopingExposuredata[10] != null ? loopingExposuredata[10] : "0.00")));
				Entitydata.setBill_disc_rwa(new BigDecimal(String.valueOf(loopingExposuredata[11] != null ? loopingExposuredata[11] : "0.00")));
				RT_RWA_Fund_base.add(Entitydata);
			}
			
		}else if (Data_table_type.equals("Branchsnapshotdetail")) {
			
			if(classification.equals("BranchTotExp")) {
				//Take the value as per the classification
				System.out.println("Selected Report Date : "+ Selecteddate +" and Branch name : "+Branch_name);
				TopExposuredata = RT_RWA_Fund_base_data_rep.Gettotalexpobranch(Selecteddate,Branch_name);
			}else if(classification.equals("BranchTotRWA")) {
				TopExposuredata = RT_RWA_Fund_base_data_rep.Gettotalexpobranch(Selecteddate,Branch_name);
			}else if(classification.equals("BranchActvExp")) {
				TopExposuredata = RT_RWA_Fund_base_data_rep.Getactiveexpobranch(Selecteddate,Branch_name);
			}else if(classification.equals("BranchNPAExp")) {
				TopExposuredata = RT_RWA_Fund_base_data_rep.GetNPAexpobranch(Selecteddate,Branch_name);
			}else if(classification.equals("BranchWatchExp")) {
				TopExposuredata = RT_RWA_Fund_base_data_rep.GetWatchlistexpobranch(Selecteddate,Branch_name);
			}else if(classification.equals("BranchOverdueExp")) {
				TopExposuredata = RT_RWA_Fund_base_data_rep.GetOverdueexpobranch(Selecteddate,Branch_name);
			}
			
			//Then loop the data to show in front end
			for(Object[] loopingExposuredata : TopExposuredata) {
				
				RT_RWA_Fund_base_data_entity Entitydata = new RT_RWA_Fund_base_data_entity();
				System.out.println(String.valueOf(loopingExposuredata[0]));
				Entitydata.setBranch_name(String.valueOf(loopingExposuredata[0] != null ? loopingExposuredata[0] : ""));
				Entitydata.setAccount_name(String.valueOf(loopingExposuredata[1] != null ? loopingExposuredata[1] : ""));
				Entitydata.setGl_code(String.valueOf(loopingExposuredata[2] != null ? loopingExposuredata[2] : ""));
				Entitydata.setConst_id(String.valueOf(loopingExposuredata[3] != null ? loopingExposuredata[3] : ""));
				Entitydata.setPurpose(String.valueOf(loopingExposuredata[4] != null ? loopingExposuredata[4] : ""));
				Entitydata.setScheme(String.valueOf(loopingExposuredata[5] != null ? loopingExposuredata[5] : ""));
				Entitydata.setRwa_class(String.valueOf(loopingExposuredata[6] != null ? loopingExposuredata[6] : ""));
				Entitydata.setInt_suspense(new BigDecimal(String.valueOf(loopingExposuredata[7] != null ? loopingExposuredata[7] : "0.00")));
				Entitydata.setTot_provision(new BigDecimal(String.valueOf(loopingExposuredata[8] != null ? loopingExposuredata[8] : "0.00")));
				Entitydata.setLimit(new BigDecimal(String.valueOf(loopingExposuredata[9] != null ? loopingExposuredata[9] : "0.00")));
				Entitydata.setBalance(new BigDecimal(String.valueOf(loopingExposuredata[10] != null ? loopingExposuredata[10] : "0.00")));
				Entitydata.setExposure(new BigDecimal(String.valueOf(loopingExposuredata[11] != null ? loopingExposuredata[11] : "0.00")));
				Entitydata.setRw(new BigDecimal(String.valueOf(loopingExposuredata[12] != null ? loopingExposuredata[12] : "0.00")));
				Entitydata.setRwa(new BigDecimal(String.valueOf(loopingExposuredata[13] != null ? loopingExposuredata[13] : "0.00")));
				Entitydata.setTotal_drawn_rwa(new BigDecimal(String.valueOf(loopingExposuredata[14] != null ? loopingExposuredata[14] : "0.00")));
				Entitydata.setTotal_rwa(new BigDecimal(String.valueOf(loopingExposuredata[15] != null ? loopingExposuredata[15] : "0.00")));
				RT_RWA_Fund_base.add(Entitydata);
			}
			
		}
		return RT_RWA_Fund_base;

	}

}


