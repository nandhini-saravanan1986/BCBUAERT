package com.bornfire.xbrl.controllers;

/*
* Project           : Bonfire XBRL
* Author            : VINODKUMAR M
* Date created      : 09-JUL-2019
* Purpose           : To handle all report related http requests - controllers. 
*/

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.xbrl.entities.BankMaster;
import com.bornfire.xbrl.entities.TransactionMasterTable;
import com.bornfire.xbrl.entities.BRSS.*;
import com.bornfire.xbrl.services.*;

import net.sf.jasperreports.engine.JRException;


@Controller
@ConfigurationProperties("default")
@RequestMapping(value = "Reports")
public class XBRLReportsController {

	private static final Logger logger = LoggerFactory.getLogger(XBRLReportsController.class);

	@Autowired
	ReportServices reportServices;
	
	@Autowired
	RegulatoryReportServices regreportServices;

	
	@Autowired
	LoginServices loginServices;

	   


   
	private String pagesize;

	public String getPagesize() {
		return pagesize;
	}

	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}

	DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
	//To show the required report at the first stage
	@RequestMapping(value = "{reportid}", method = RequestMethod.POST)
	public ModelAndView reportView(@PathVariable("reportid") String reportid, @RequestParam(value = "function", required = false) String function,
			@RequestParam("asondate") String asondate, @RequestParam("fromdate") String fromdate,
			@RequestParam("todate") String todate, @RequestParam(value ="currency", required = false) String currency,
			@RequestParam(value = "subreportid", required = false) String subreportid,
			@RequestParam(value = "secid", required = false) String secid,
			@RequestParam(value = "dtltype", required = false) String dtltype,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@RequestParam(value = "reportingTime", required = false) String reportingTime, Model md,HttpServletRequest req) {
		
		
		
		String userid = (String) req.getSession().getAttribute("USERID");
		// Logging Navigation
		if(dtltype.equals("report")) {
			md.addAttribute("menu", "XBRLReports");
			loginServices.SessionLogging("REPORTS"+reportid, "M8", req.getSession().getId(), userid, req.getRemoteAddr(), "ACTIVE");
		}else {
			md.addAttribute("menu", "XBRLArchives");
			loginServices.SessionLogging("ARCHREPORTS"+reportid, "M9", req.getSession().getId(), userid, req.getRemoteAddr(), "ACTIVE");
		}
		
		


		logger.info("Get Report :" + reportid);
		logger.info("Get Report :" + asondate);
		try {
			asondate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(asondate));
			fromdate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(fromdate));
			todate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(todate));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		logger.info("Get Report :" + asondate);
		int currentPage = page.orElse(0);
		int pageSize = size.orElse(Integer.parseInt(pagesize));

		logger.info("Assigning Model Attributes :"+reportid);
		//Assigning required Modal Attributes
		
		md.addAttribute("reportid", reportid);
		md.addAttribute("asondate", asondate);
		md.addAttribute("fromdate", fromdate);
		md.addAttribute("todate", todate);
		md.addAttribute("currency", currency);
		md.addAttribute("dtltype", dtltype);
		md.addAttribute("type", type);
		md.addAttribute("reportingTime", reportingTime);
		md.addAttribute("reportTitle", reportServices.getReportName(reportid));
		
		
		logger.info("Getting ModelandView :"+reportid);
		ModelAndView mv = new ModelAndView();
		
		mv = regreportServices.getReportView(reportid, asondate, fromdate, todate, currency, dtltype, subreportid,
				secid, reportingTime,PageRequest.of(currentPage, pageSize));
		
	//	System.out.println("----------------------");
		
	//	Page<Object> sup0700RepPage = (Page<Object>) mv.getModelMap().get("reportsummary");
		
	//	sup0700RepPage.getContent().forEach((a)-> System.out.println(a.toString()));
		
		
		
		return mv;
		

	}
	
	//To check report data availability and Pending verification before 
	

	
	@RequestMapping(value = "{reportid}/nilReport", method = RequestMethod.GET)
	@ResponseBody
	public String nilReport(@PathVariable("reportid") String reportid,
			@RequestParam("asondate") String asondate,
			@RequestParam("fromdate") String fromdate, @RequestParam("todate") String todate,
			@RequestParam(value = "reportingTime", required = false) String reportingTime, Model md, HttpServletRequest req) {

		logger.info("Get Report Verify Screen" + reportid);
		
		String userid = (String) req.getSession().getAttribute("USERID");
		
		Date asondate1=null;
		Date fromdate1=null;
		Date todate1=null;
		
		try {
			asondate1 = new SimpleDateFormat("dd-MM-yyyy").parse(asondate);
			fromdate1 = new SimpleDateFormat("dd-MM-yyyy").parse(fromdate);
			todate1 = new SimpleDateFormat("dd-MM-yyyy").parse(todate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		try {
			asondate = dateFormat.format(new SimpleDateFormat("dd-MM-yyyy").parse(asondate));
			fromdate = dateFormat.format(new SimpleDateFormat("dd-MM-yyyy").parse(fromdate));
			todate = dateFormat.format(new SimpleDateFormat("dd-MM-yyyy").parse(todate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
			
		
		String status = reportServices.updateReportStatus(reportid,asondate1,fromdate1,todate1,userid,"Y");
		String msg = reportServices.saveReport(reportid, asondate, fromdate, todate, "MUR");

		return status;
		

	}
	

	@RequestMapping(value = "{reportid}/Summary", method = RequestMethod.GET)
	public ModelAndView reportSummay(@PathVariable("reportid") String reportid,
			@RequestParam("asondate") String asondate, @RequestParam("fromdate") String fromdate,
			@RequestParam("todate") String todate, @RequestParam("currency") String currency,
			@RequestParam(value = "subreportid", required = false) String subreportid,
			@RequestParam(value = "secid", required = false) String secid,
			@RequestParam(value = "dtltype", required = false) String dtltype,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@RequestParam(value = "reportingTime",required = false )String reportingTime , Model md) {

		logger.info("Getting Report Summary :"+reportid);
		
		int currentPage = page.orElse(0);
		int pageSize = size.orElse(Integer.parseInt(pagesize));
		
		logger.info("Assigning Model Attributes :"+reportid);
		md.addAttribute("menu", "XBRLReports");
		md.addAttribute("reportid", reportid);
		md.addAttribute("asondate", asondate);
		md.addAttribute("fromdate", fromdate);
		md.addAttribute("todate", todate);
		md.addAttribute("type", type);
		md.addAttribute("currency", currency);
		md.addAttribute("reportingTime", reportingTime);
		md.addAttribute("dtltype", dtltype);
		md.addAttribute("reportTitle", reportServices.getReportName(reportid));
		md.addAttribute("reportingTime", reportingTime);
		md.addAttribute("displaymode", "summary");

		logger.info("Getting ModelandView :"+reportid);
		ModelAndView mv = regreportServices.getReportSummary(reportid, asondate, fromdate, todate, currency, dtltype, subreportid,
				secid,reportingTime,PageRequest.of(currentPage, pageSize));
		

		return mv;

	}

	@RequestMapping(value = "{reportid}/Details", method = RequestMethod.GET)
	public ModelAndView reportDetail(@PathVariable("reportid") String reportid,
			@RequestParam(value = "instancecode", required = false) String instancecode,
			@RequestParam(value = "filter", required = false) String filter,
			@RequestParam("asondate") String asondate, @RequestParam("fromdate") String fromdate,
			@RequestParam("todate") String todate, @RequestParam("currency") String currency,
			@RequestParam(value = "subreportid", required = false) String subreportid,
			@RequestParam(value = "secid", required = false) String secid,
			@RequestParam(value = "dtltype", required = false) String dtltype,
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, 
			@RequestParam(value = "reportingTime", required = false) String reportingTime,Model md) {
		
		logger.info("Getting Report Details :"+reportid);
		logger.info("Assigning Model Attributes :"+reportid);
		
		
		
		md.addAttribute("menu", "XBRLReports");
		md.addAttribute("reportid", reportid);
		md.addAttribute("asondate", asondate);
		md.addAttribute("fromdate", fromdate);
		md.addAttribute("todate", todate);
		md.addAttribute("filter", filter);
		md.addAttribute("currency", currency);
		md.addAttribute("dtltype", dtltype);
		md.addAttribute("reportingTime", reportingTime);
		//md.addAttribute("instancecode", Integer.parseInt(instancecode));
		md.addAttribute("reportTitle", reportServices.getReportName(reportid));
		md.addAttribute("displaymode", "detail");

		int currentPage = page.orElse(0);
		int pageSize = size.orElse(Integer.parseInt(pagesize));

		logger.info("Getting ModelandView :"+reportid);
		ModelAndView mv = regreportServices.getReportDetails(reportid, instancecode, asondate, fromdate, todate, currency,
				reportingTime,dtltype, subreportid, secid, PageRequest.of(currentPage, pageSize),filter);

		return mv;

	}

	@RequestMapping(value = "{reportid}/Download", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public InputStreamResource XBRLDownload(HttpServletResponse response, @PathVariable("reportid") String reportid,
			@RequestParam("asondate") String asondate, @RequestParam("fromdate") String fromdate,
			@RequestParam("todate") String todate, @RequestParam("currency") String currency,
			@RequestParam(value = "subreportid", required = false) String subreportid,
			@RequestParam(value = "secid", required = false) String secid,
			@RequestParam(value = "dtltype", required = false) String dtltype,
			@RequestParam(value = "reportingTime", required = false) String reportingTime,@RequestParam(value = "instancecode", required = false) String instancecode,
			@RequestParam("filetype") String filetype) throws IOException, SQLException {
		response.setContentType("application/octet-stream");

		InputStreamResource resource = null;
		try {
			logger.info("Getting download File :"+reportid+", FileType :"+filetype+", SubreportId :"+subreportid);
		//	System.out.println(asondate);
			File repfile = regreportServices.getDownloadFile(reportid, asondate, fromdate, todate, currency,subreportid, secid,
					dtltype, reportingTime,filetype,instancecode);
			
			
			response.setHeader("Content-Disposition", "attachment; filename=" + repfile.getName());
			resource = new InputStreamResource(new FileInputStream(repfile));
		} catch (JRException e) {
			e.printStackTrace();
		}
		return resource;
	}


	
	@RequestMapping(value = "{reportid}/save", method = RequestMethod.GET)
	@ResponseBody
	public String saveReport(@PathVariable("reportid") String reportid, @RequestParam("asondate") String asondate,
			@RequestParam("fromdate") String fromdate, @RequestParam("todate") String todate, 
			@RequestParam(value = "reportingTime", required = false) String reportingTime,
			@RequestParam("currency") String currency, HttpServletRequest req) {
		
		String userid = (String) req.getSession().getAttribute("USERID");

		String msg="";
		Date asondate1=null;
		Date fromdate1=null;   
		Date todate1=null;
		try {
			
			asondate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(asondate);
			fromdate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(fromdate);
			todate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(todate);
			logger.info("Saving Report :"+reportid);
			
			if(reportid.equals("FIM0500")) {
				msg = reportServices.saveFIM0500Report(reportid, asondate, fromdate, todate, currency, reportingTime);
			}
			
			else {
			msg = reportServices.saveReport(reportid, asondate, fromdate, todate, currency);
			}
			
			
			
			//To update report Status Info table on
			reportServices.updateReportStatus(reportid,asondate1,fromdate1,todate1,userid,"N");
		}catch(Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			msg = "error occured. Please contact Administrator";
		}
		

		return msg;
	}
	

	@GetMapping("/ReportDownloadXLSX")
	public ResponseEntity<InputStreamResource> AMLDownloadExcel(HttpServletResponse response,
			@RequestParam(value = "userid", required = false) String userid,
			@RequestParam(value = "filename", required = false) String filename,
			@RequestParam(value = "ref_id",required = false) String ref_id,
			@RequestParam(value = "Param1",required = false) String input1,
			@RequestParam(value = "Param2",required = false) String input2,
			@RequestParam(value = "Param3",required = false) String input3,
			@RequestParam(value = "Param4",required = false) String input4,
			@RequestParam(value = "Param5",required = false) String input5,
			@RequestParam(value = "rptname",required = false) String reportname) throws IOException, SQLException, JRException, ParseException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");

		String toDAte = null;

				Date ConToDate =new Date();
				String strDate1 = formatter1.format(ConToDate);
				toDAte = formatter1.format(new SimpleDateFormat("dd-MMM-yyyy").parse(strDate1));
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename="+filename+"_"+toDAte+".xlsx");
			
		
		
		return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(reportServices.getDownloadFileExcel(null, ref_id, input1, input2, input3,input4,input5,filename,reportname)));

	
	}



	@RequestMapping(value = "/ReportDownloadByScript", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String AMLDownload(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "userid", required = false) String userid,
			@RequestParam(value = "filename", required = false) String filename,
			@RequestParam(value = "ref_id",required = false) String ref_id,
			@RequestParam(value = "Param1",required = false) String input1,
			@RequestParam(value = "Param2",required = false) String input2,
			@RequestParam(value = "Param3",required = false) String input3,
			@RequestParam(value = "Param4",required = false) String input4,
			@RequestParam(value = "Param5",required = false) String input5,
			@RequestParam(value = "rptname",required = false) String reportname,
			@RequestParam(value = "username",required = false) String username) throws IOException, SQLException {
		response.setContentType("application/octet-stream");
		String currency = null;
		String msg ="";
		InputStreamResource resource = null;
		try {
			String repfile = null;
			File Master =null;
			try {
				msg = reportServices.getDownloadFileFromScript(userid,username, ref_id, input1, input2, input3,input4,input5,filename,reportname);
			//	Master = reportServices.getDownloadFile(repfile);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		//	response.setHeader("Content-Disposition", "attachment; filename=" + Master.getName());
		//	resource = new InputStreamResource(new FileInputStream(Master));
		} catch (JRException e) {
			e.printStackTrace();
		}
		return msg;
	}

	

	@RequestMapping(value = "/ReportDownloadByData", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public InputStreamResource ReportDownloadByData(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "userid", required = false) String userid,
			@RequestParam(value = "filepath", required = false) String filepath,
			@RequestParam(value = "ref_id",required = false) String ref_id,
			@RequestParam(value = "username",required = false) String username,
			@RequestParam(value = "downloadondelete",required = false) String downloadondelete) throws IOException, SQLException {
		response.setContentType("application/octet-stream");
		String currency = null;
		InputStreamResource resource = null;
		try {
			File repfile = null;
			try {
				repfile = reportServices.getDownloadFileFromdata(userid,username, ref_id, filepath,downloadondelete);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			response.setHeader("Content-Disposition", "attachment; filename=" + repfile.getName());
			resource = new InputStreamResource(new FileInputStream(repfile));
		} catch (JRException e) {
			e.printStackTrace();
		}
		return resource;
	}
	
	
	/**********************************RBS*******************************************************************/
	@RequestMapping(value = "{reportid}/PrecheckRbs", method = RequestMethod.GET)
	@ResponseBody
	public String reportPreCheckRBS(@PathVariable("reportid") String reportid,
			
			@RequestParam("fromdate") String fromdate, @RequestParam("todate") String todate) throws ParseException {
		
		
		logger.info("Precheck for Report :"+reportid);
		
		if(todate.length()==10){
			return reportServices.preCheckReportRBS(reportid, fromdate, todate);
		}
		else {
			
			try {
				todate = new SimpleDateFormat("dd-MM-yyyy").format(dateFormat.parse(todate));
				
			} catch (ParseException e) {			
			
				e.printStackTrace();
			}
			
			return reportServices.preCheckReportRBS(reportid, fromdate, todate);
		}
		

	}
	
	
	
						 

			@RequestMapping(value = "RBSDownload", method = RequestMethod.GET)
			@ResponseBody
			public InputStreamResource RBSREPORTGENERATIONDownload(HttpServletResponse response, @RequestParam("reportid") String reportid,
					@RequestParam("asondate") String asondate, @RequestParam("fromdate") String fromdate,
					@RequestParam("todate") String todate, @RequestParam("currency") String currency,
					@RequestParam(value = "subreportid", required = false) String subreportid,
					@RequestParam(value = "secid", required = false) String secid,
					@RequestParam(value = "dtltype", required = false) String dtltype,
					@RequestParam(value = "reportingTime", required = false) String reportingTime,@RequestParam(value = "instancecode", required = false) String instancecode,
					@RequestParam("filetype") String filetype) throws IOException, SQLException, ParseException {
				response.setContentType("application/octet-stream");

				InputStreamResource resource = null;
				try {
					logger.info("Getting download File :"+reportid+", FileType :"+filetype+", SubreportId :"+subreportid);
				//	System.out.println(asondate);
					String reportids = "RBSGENERATION";
					File repfile = reportServices.getDownloadFile(reportids, asondate, fromdate, todate, currency,subreportid, secid,
							dtltype, reportingTime,filetype,instancecode);
					
					
					response.setHeader("Content-Disposition", "attachment; filename=" + repfile.getName());
					resource = new InputStreamResource(new FileInputStream(repfile));
				} catch (JRException e) {
					e.printStackTrace();
				}
				return resource;
			}

@RequestMapping(value = "Downloadcredit", method = RequestMethod.GET)
@ResponseBody
public InputStreamResource REPORTGENERATIONDownload(HttpServletResponse response, @RequestParam("reportid") String reportid,
		@RequestParam("asondate") String asondate, @RequestParam("fromdate") String fromdate,
		@RequestParam("todate") String todate, @RequestParam("currency") String currency,
		@RequestParam(value = "subreportid", required = false) String subreportid,
		@RequestParam(value = "secid", required = false) String secid,
		@RequestParam(value = "dtltype", required = false) String dtltype,
		@RequestParam(value = "reportingTime", required = false) String reportingTime,@RequestParam(value = "instancecode", required = false) String instancecode,
		@RequestParam("filetype") String filetype) throws IOException, SQLException, ParseException {
	response.setContentType("application/octet-stream");

	InputStreamResource resource = null;
	try {
		logger.info("Getting download File :"+reportid+", FileType :"+filetype+", SubreportId :"+subreportid);
	//	System.out.println(asondate);
		String reportids = "RBSGENERATION";
		File repfile = reportServices.getDownloadFile(reportid, asondate, fromdate, todate, currency,subreportid, secid,
				dtltype, reportingTime,filetype,instancecode);
		
		
		response.setHeader("Content-Disposition", "attachment; filename=" + repfile.getName());
		logger.info("Getting download File :"+ repfile.getName());
		resource = new InputStreamResource(new FileInputStream(repfile));
	} catch (JRException e) {
		e.printStackTrace();
	}
	return resource;
}



@RequestMapping(value = "{reportid}/PrecheckRbsMR", method = RequestMethod.GET)
@ResponseBody
public String reportPreCheckRBSMR(@PathVariable("reportid") String reportid,
		
		@RequestParam("fromdate") String fromdate, @RequestParam("todate") String todate) throws ParseException {
	
	
	logger.info("Precheck for Report :"+reportid);
	
if(todate.length()==10){
		return reportServices.preCheckReportRBS(reportid, fromdate, todate);
	}
	 else { 
		
		try {
			todate = new SimpleDateFormat("dd-MM-yyyy").format(dateFormat.parse(todate));
			
		} catch (ParseException e) {			
		
			e.printStackTrace();
		}
		
		return reportServices.preCheckReportRBS(reportid, fromdate, todate);
	}
	

	 } 



	
	/**********************************Regulatory Reports*******************************************************************/
	@RequestMapping(value = "{reportid}/PrecheckRR", method = RequestMethod.GET)
	@ResponseBody
	public String reportPreCheckRR(@PathVariable("reportid") String reportid,
			
			@RequestParam("fromdate") String fromdate, @RequestParam("todate") String todate) throws ParseException {
		
		
		logger.info("Precheck for Report :"+reportid);
		
		if(todate.length()==10){
			return regreportServices.preCheckReportRBS(reportid, fromdate, todate);
		}
		else {
			
			try {
				todate = new SimpleDateFormat("dd-MM-yyyy").format(dateFormat.parse(todate));
				
			} catch (ParseException e) {			
			
				e.printStackTrace();
			}
			
			return regreportServices.preCheckReportRBS(reportid, fromdate, todate);
		}
		
	}
	
	
		
	}
	 

