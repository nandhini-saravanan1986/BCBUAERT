package com.bornfire.xbrl.controllers;

import java.io.File;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bornfire.xbrl.entities.AccessAndRoles;
import com.bornfire.xbrl.entities.AccessandRolesRepository;

import com.bornfire.xbrl.entities.RT_FxRiskDataRepository;
import com.bornfire.xbrl.entities.RT_Fxriskdata;
import com.bornfire.xbrl.entities.RT_MmDataRepository;
import com.bornfire.xbrl.entities.RT_DatacontrolRepository;

import com.bornfire.xbrl.entities.BcbuaeTradeMarketriskData;
import com.bornfire.xbrl.entities.BcbuaeTradeMarketriskDataRepository;
import com.bornfire.xbrl.entities.RT_BankNameMaster;
import com.bornfire.xbrl.entities.RT_BankNameMasterRepository;
import com.bornfire.xbrl.entities.RT_CountryRiskDropdown;
import com.bornfire.xbrl.entities.RT_CountryRiskDropdownRepo;
import com.bornfire.xbrl.entities.RT_NostroAccBalData;
import com.bornfire.xbrl.entities.RT_NostroAccBalDataRepository;
import com.bornfire.xbrl.entities.RT_DataControl;
import com.bornfire.xbrl.entities.RT_Fxriskdata;
import com.bornfire.xbrl.entities.UserProfile;
import com.bornfire.xbrl.services.AccessAndRolesServices;

import com.bornfire.xbrl.services.RT_FxriskdataService;
import com.bornfire.xbrl.services.RT_MmdataService;
import com.bornfire.xbrl.entities.RT_MmData;

import com.bornfire.xbrl.services.RT_DataControlService;

import com.bornfire.xbrl.services.BCBUAE_NostroExcelDownload;
import com.bornfire.xbrl.services.LoginServices;
import com.bornfire.xbrl.services.NostroAccBalDataService;

@Controller
@ConfigurationProperties("default")
public class XBRLNavigationController {

	private static final Logger logger = LoggerFactory.getLogger(XBRLNavigationController.class);

	@Autowired
	private RT_FxriskdataService fxriskdataService;

	@Autowired
	LoginServices loginServices;

	@Autowired
	AccessAndRolesServices AccessRoleService;

	@Autowired
	AccessandRolesRepository accessandrolesrepository;

	@Autowired
	RT_NostroAccBalDataRepository nostroAccBalRepo;

	@Autowired
	NostroAccBalDataService nostroService;

	@Autowired
	BCBUAE_NostroExcelDownload bcbuaeNostroExcelDownload;

	@Autowired
	RT_DataControlService RT_DataControlService;

	@Autowired
	RT_DatacontrolRepository RT_DatacontrolRepository;

	@Autowired
	RT_FxRiskDataRepository friskdataRepo;

	@Autowired
	RT_BankNameMasterRepository bankRepo;
	
	@Autowired
	RT_CountryRiskDropdownRepo countryRepo;
	
	@Autowired
	RT_MmDataRepository mmdataRepo;
	
	
	@Autowired
	private RT_MmdataService mmdataService;
	
	@Autowired
	SessionFactory sessionFactory;

	private String pagesize;

	public String getPagesize() {
		return pagesize;
	}

	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}

	@RequestMapping(value = "Dashboard", method = { RequestMethod.GET, RequestMethod.POST })
	public String dashboard(Model md, HttpServletRequest req) {

		String domainid = (String) req.getSession().getAttribute("DOMAINID");
		String userid = (String) req.getSession().getAttribute("USERID");

		md.addAttribute("changepassword", loginServices.checkPasswordChangeReq(userid));
		md.addAttribute("checkpassExpiry", loginServices.checkpassexpirty(userid));
		md.addAttribute("checkAcctExpiry", loginServices.checkAcctexpirty(userid));
		int completed = 0;
		int uncompleted = 0;

		md.addAttribute("completed", completed);
		md.addAttribute("uncompleted", uncompleted);
		md.addAttribute("menu", "Dashboard");
		return "XBRLDashboard";
	}

	@RequestMapping(value = "AccessandRoles", method = { RequestMethod.GET, RequestMethod.POST })
	public String IPSAccessandRoles(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req) {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (formmode == null || formmode.equals("list")) {
			md.addAttribute("menu", "ACCESS AND ROLES");
			md.addAttribute("menuname", "ACCESS AND ROLES");
			md.addAttribute("formmode", "list");
			md.addAttribute("AccessandRoles", accessandrolesrepository.rulelist());
		} else if (formmode.equals("add")) {
			md.addAttribute("menuname", "ACCESS AND ROLES - ADD");
			md.addAttribute("formmode", "add");
		} else if (formmode.equals("edit")) {
			md.addAttribute("menuname", "ACCESS AND ROLES - EDIT");
			md.addAttribute("formmode", formmode);
			md.addAttribute("IPSAccessRole", AccessRoleService.getRoleId(userid));
		} else if (formmode.equals("view")) {
			md.addAttribute("menuname", "ACCESS AND ROLES - INQUIRY");
			md.addAttribute("formmode", formmode);
			md.addAttribute("IPSAccessRole", AccessRoleService.getRoleId(userid));

		} else if (formmode.equals("verify")) {
			md.addAttribute("menuname", "ACCESS AND ROLES - VERIFY");
			md.addAttribute("formmode", formmode);
			md.addAttribute("IPSAccessRole", AccessRoleService.getRoleId(userid));

		} else if (formmode.equals("delete")) {
			md.addAttribute("menuname", "ACCESS AND ROLES - DELETE");
			md.addAttribute("formmode", formmode);
			md.addAttribute("IPSAccessRole", AccessRoleService.getRoleId(userid));
		}

		md.addAttribute("adminflag", "adminflag");
		md.addAttribute("userprofileflag", "userprofileflag");

		return "AccessandRoles";
	}

	@RequestMapping(value = "createAccessRole", method = RequestMethod.POST)
	@ResponseBody
	public String createAccessRoleEn(@RequestParam("formmode") String formmode,
			@RequestParam(value = "adminValue", required = false) String adminValue,
			@RequestParam(value = "BRF_ReportsValue", required = false) String BRF_ReportsValue,
			@RequestParam(value = "Basel_ReportsValue", required = false) String Basel_ReportsValue,
			@RequestParam(value = "ArchivalValue", required = false) String ArchivalValue,
			@RequestParam(value = "Audit_InquiriesValue", required = false) String Audit_InquiriesValue,
			@RequestParam(value = "RBR_ReportsValue", required = false) String RBR_ReportsValue,
			@RequestParam(value = "VAT_LedgerValue", required = false) String VAT_LedgerValue,
			@RequestParam(value = "Invoice_DataValue", required = false) String Invoice_DataValue,
			@RequestParam(value = "finalString", required = false) String finalString,

			@ModelAttribute AccessAndRoles alertparam, Model md, HttpServletRequest rq) {

		String userid = (String) rq.getSession().getAttribute("USERID");
		String roleId = (String) rq.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String msg = AccessRoleService.addPARAMETER(alertparam, formmode, adminValue, BRF_ReportsValue,
				Basel_ReportsValue, ArchivalValue, Audit_InquiriesValue, RBR_ReportsValue, VAT_LedgerValue,
				Invoice_DataValue, finalString, userid);

		return msg;

	}

	@RequestMapping(value = "UserProfile", method = { RequestMethod.GET, RequestMethod.POST })
	public String userprofile(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid,
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req) {

		int currentPage = page.orElse(0);
		int pageSize = size.orElse(Integer.parseInt(pagesize));

		String loginuserid = (String) req.getSession().getAttribute("USERID");
		String WORKCLASSAC = (String) req.getSession().getAttribute("WORKCLASS");
		String ROLEIDAC = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("RuleIDType", accessandrolesrepository.roleidtype());

		System.out.println("work class is : " + WORKCLASSAC);
		// Logging Navigation
		loginServices.SessionLogging("USERPROFILE", "M2", req.getSession().getId(), loginuserid, req.getRemoteAddr(),
				"ACTIVE");
		Session hs1 = sessionFactory.getCurrentSession();
		md.addAttribute("menu", "USER PROFILE"); // To highlight the menu

		if (formmode == null || formmode.equals("list")) {

			md.addAttribute("formmode", "list");// to set which form - valid values are "edit" , "add" & "list"
			md.addAttribute("WORKCLASSAC", WORKCLASSAC);
			md.addAttribute("ROLEIDAC", ROLEIDAC);
			md.addAttribute("loginuserid", loginuserid);

			Iterable<UserProfile> user = loginServices.getUsersList();

			md.addAttribute("userProfiles", user);

		} else if (formmode.equals("edit")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("userProfile", loginServices.getUser(userid));

		} else if (formmode.equals("verify")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("userProfile", loginServices.getUser(userid));

		} else {

			md.addAttribute("formmode", formmode);

			md.addAttribute("userProfile", loginServices.getUser(""));

		}

		return "XBRLUserprofile";
	}

	@RequestMapping(value = "createUser", method = RequestMethod.POST)
	@ResponseBody
	public String createUser(@RequestParam("formmode") String formmode, @ModelAttribute UserProfile userprofile,
			Model md, HttpServletRequest rq) {
		String MOB = (String) rq.getSession().getAttribute("MOBILENUMBER");
		String ROLE = (String) rq.getSession().getAttribute("ROLEDESC");
		String userid = (String) rq.getSession().getAttribute("USERID");
		String username = (String) rq.getSession().getAttribute("USERNAME");
		String msg = loginServices.addUser(userprofile, formmode, userid, username, MOB, ROLE);

		return msg;

	}

	@RequestMapping(value = "Nostro_Account_Bal", method = RequestMethod.GET)
	public String NostroAccountBal(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String accountNo, Model md, HttpServletRequest req) {

		if ("edit".equalsIgnoreCase(formmode) && accountNo != null && !accountNo.isEmpty()) {
			RT_NostroAccBalData data = nostroAccBalRepo.findById(accountNo).orElse(null);
			md.addAttribute("nostroData", data);
			System.out.println("edit is formmode");
			md.addAttribute("formmode", "edit");
		} else if ("list".equalsIgnoreCase(formmode)) {
			md.addAttribute("branchList", nostroAccBalRepo.getlist());
			System.out.println("list is formmode");
			md.addAttribute("formmode", "list");
		} else {
			md.addAttribute("formmode", "add");
			md.addAttribute("formmode", "null");
		}
		
		List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
	    List<RT_CountryRiskDropdown> countryList = countryRepo.findAllByOrderByCountryOfRiskAsc();

	    md.addAttribute("bankList", bankList);
	    md.addAttribute("countryList", countryList);
	    
		return "Nostro_Account_Bal";
	}

	@RequestMapping(value = "getBankDetails", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> getBankDetails(@RequestParam String bankName) {
      
		RT_BankNameMaster bank = bankRepo.findByBankName(bankName);
		System.out.println("came to controller with bank name ");
        if (bank == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> bankDetails = new HashMap<>();
        bankDetails.put("bankSymbol", bank.getBankSymbol());
        bankDetails.put("conventionalIslamic", bank.getConventionalIslamic());
        bankDetails.put("localForeign", bank.getLocalForeign());
        bankDetails.put("cbuaeTiering", bank.getCbuaeTiering());

        return ResponseEntity.ok(bankDetails);
    }
	
	@RequestMapping(value = "getCountryDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> getCountryDetails(@RequestParam String countryName) {
	    RT_CountryRiskDropdown country = countryRepo.findByCountryOfRisk(countryName);
	    System.out.println("Fetched country details for: " + countryName);

	    if (country == null) {
	        return ResponseEntity.notFound().build();
	    }

	    Map<String, String> response = new HashMap<>();
	    response.put("cbuaeGeographicalZone", country.getCbuaeGeographicalZone());

	    return ResponseEntity.ok(response);
	}


	@PostMapping("/createDataControl")
	@ResponseBody
	public String createDataControl(
			@RequestParam(name = "formmode", required = false, defaultValue = "add") String formmode,
			@RequestParam(name = "report_name", required = false, defaultValue = "add") String report_name,
			@ModelAttribute RT_DataControl dto) {
		System.out.println("Controller: createOrUpdateNostro() called");
		System.out.println("report name is: " + report_name);
		return RT_DataControlService.createOrUpdate(dto, formmode, report_name);
	}

	/*@PostMapping("/updateNostro")
	public String updateNostro(@ModelAttribute RT_NostroAccBalData nostroData, Model model, RedirectAttributes redirectAttributes) {
	    
	    boolean updated = nostroService.updateNostro(nostroData);
	    System.out.println("msg is : " + updated);
	    
	    if (updated) {
	        System.out.println("Update successful");
	        redirectAttributes.addFlashAttribute("msg", "Update successful");
	    } else {
	        System.out.println("Update Record not found for update");
	        redirectAttributes.addFlashAttribute("msg", "Record not found for update");
	    }
	    
	    return "redirect:Nostro_Account_Bal?formmode=list";
	}
*/
	@RequestMapping("/updateNostro")
	@ResponseBody
public String updateNostro(@ModelAttribute RT_NostroAccBalData nostroData,HttpServletRequest req) {
	    
	    boolean updated = nostroService.updateNostro(nostroData);
	    System.out.println("msg is : " + updated);
	    
	    if (updated) {
	        System.out.println("Update successful");
	        return "Updated successful";
	    } else {
	        System.out.println("Update Record not found for update");
	        return "Record not found for update";
	    }
	    
	    
	}
	
	@PostMapping("/updateFxriskdata")
	@ResponseBody
	public String updateFxriskdata(@ModelAttribute RT_Fxriskdata fxriskData) {
	    boolean updated = fxriskdataService.updateFxriskdata(fxriskData);
	    
	    if (updated) {
	        return "Update successful";
	    } else {
	        return "Record not found for update";
	    }
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}

	@RequestMapping(value = "Fx_Risk_Data", method = RequestMethod.GET)
	public String Fxriskdata(
	        @RequestParam(required = false) String formmode,
	        @RequestParam(required = false) String SI_NO,
	        Model md, HttpServletRequest req) {

	    if ("edit".equalsIgnoreCase(formmode) && SI_NO != null && !SI_NO.isEmpty()) {
	        RT_Fxriskdata data = friskdataRepo.getParticularDataBySI_NO(SI_NO);
			md.addAttribute("fxriskData", data);
			System.out.println("edit is formmode");
			md.addAttribute("formmode", "edit");

	    } else if ("list".equalsIgnoreCase(formmode)) {
	        md.addAttribute("branchList", friskdataRepo.getlist());
	        System.out.println("list is formmode");
	        md.addAttribute("formmode", "list");

	    } else {
	        md.addAttribute("formmode", "add");
	        md.addAttribute("formmode", "null");

	        // You had md.addAttribute("formmode", "null"); — removed this line because it would overwrite the previous one
	    }
	    
	    List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
		md.addAttribute("bankList", bankList);

	    return "Fx_Risk_Data";
	}

	// For download excel for fxriskdata

	@RequestMapping(value = "downloadFxriskExcel", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadFxriskExcel() throws IOException {
		System.out.println("Entered controller downloadFxriskExcel");

		File excelFile = fxriskdataService.generateFxRiskExcel();
		byte[] excelData = java.nio.file.Files.readAllBytes(excelFile.toPath());

		HttpHeaders headersResponse = new HttpHeaders();
		headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headersResponse.setContentDispositionFormData("attachment", "FxriskData.xls");

		return ResponseEntity.ok().headers(headersResponse).body(excelData);
	}
	// For download excel for downloadNostroExcel

	@RequestMapping(value = "downloadNostroExcel", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadNostroExcel() throws IOException {
		System.out.println("Entered controller downloadNostroExcel");

		File excelFile = nostroService.generateNostroExcel();
		byte[] excelData = java.nio.file.Files.readAllBytes(excelFile.toPath());

		HttpHeaders headersResponse = new HttpHeaders();
		headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headersResponse.setContentDispositionFormData("attachment", "NostroAccBalance.xls");

		return ResponseEntity.ok().headers(headersResponse).body(excelData);
	}

	@Autowired
	private BcbuaeTradeMarketriskDataRepository trade_market_risk_repo;

	@RequestMapping(value = "/Trade_Market_Risk", method = RequestMethod.GET)
	public String tradeMarketRisk(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date reportDate, Model model,
			HttpServletRequest request) {

		if ("edit".equalsIgnoreCase(formmode) && reportDate != null) {
			BcbuaeTradeMarketriskData data = trade_market_risk_repo.findById(reportDate).orElse(null);
			model.addAttribute("data", data);
			model.addAttribute("formmode", "edit");
			System.out.println("Edit mode activated");
		} else if ("list".equalsIgnoreCase(formmode)) {
			List<BcbuaeTradeMarketriskData> list = trade_market_risk_repo.findAll();
			model.addAttribute("dataList", list);
			model.addAttribute("formmode", "list");
			System.out.println("List mode activated");
		} else {
			model.addAttribute("data", new BcbuaeTradeMarketriskData());
			model.addAttribute("formmode", "add");
			System.out.println("Add mode activated");
		}

		return "Trade_Market_Risk"; // Thymeleaf template name: Trade_Market_Risk.html
	}
	
	@RequestMapping(value = "Mm_Data", method = RequestMethod.GET)
	public String Mmdata(
	        @RequestParam(required = false) String formmode,
	        @RequestParam(required = false) String bank_date,
	        Model md, HttpServletRequest req) {

	    if ("edit".equalsIgnoreCase(formmode) && bank_date != null && !bank_date.isEmpty()) {
	        try {
	            // Assuming your DB stores Date without time (java.sql.Date or java.util.Date)
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // Adjust pattern if needed
	            Date bankDateValue = sdf.parse(bank_date);

	            RT_MmData data = mmdataRepo.findById(bankDateValue).orElse(null);
	            md.addAttribute("fxriskData", data);
	            System.out.println("edit is formmode");
	            md.addAttribute("formmode", "edit");
	        } catch (ParseException e) {
	            e.printStackTrace();
	            md.addAttribute("mmData", null);
	            md.addAttribute("formmode", "error");  // Optionally show an error mode
	        }

	    } else if ("list".equalsIgnoreCase(formmode)) {
	        md.addAttribute("branchList", mmdataRepo.getlist());
	        System.out.println("list is formmode");
	        md.addAttribute("formmode", "list");

	    } else {
	        md.addAttribute("formmode", "add");
	        md.addAttribute("formmode", "null");

	        // You had md.addAttribute("formmode", "null"); — removed this line because it would overwrite the previous one
	    }

	    return "Mm_Data";
	}
	
	@RequestMapping(value = "downloadMmExcel", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadMmExcel() throws IOException {
		System.out.println("Entered controller downloadMmExcel");

		File excelFile = mmdataService.generateMmExcel();
		byte[] excelData = java.nio.file.Files.readAllBytes(excelFile.toPath());

		HttpHeaders headersResponse = new HttpHeaders();
		headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headersResponse.setContentDispositionFormData("attachment", "Mmdata.xls");

		return ResponseEntity.ok().headers(headersResponse).body(excelData);
	}
	

}
