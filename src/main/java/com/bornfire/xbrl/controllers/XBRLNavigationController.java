package com.bornfire.xbrl.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ByteArrayResource;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bornfire.xbrl.entities.ASL_BANKMASTER_ENTITY;
import com.bornfire.xbrl.entities.ASL_BANKMASTER_REPO;
import com.bornfire.xbrl.entities.ASL_Report_Entity;
import com.bornfire.xbrl.entities.ASL_Report_Rep;
import com.bornfire.xbrl.entities.AccessAndRoles;
import com.bornfire.xbrl.entities.AccessandRolesRepository;
import com.bornfire.xbrl.entities.BRF095AServiceRepo;
import com.bornfire.xbrl.entities.BRF39_ENTITYREP;
import com.bornfire.xbrl.entities.BRF40_Entity1;
import com.bornfire.xbrl.entities.BRF40_Entity2;
import com.bornfire.xbrl.entities.BRF40_Rep1;
import com.bornfire.xbrl.entities.BRF40_Rep2;
import com.bornfire.xbrl.entities.BRF_095_A_REPORT_ENTITY;
import com.bornfire.xbrl.entities.BankLimit_Entity;
import com.bornfire.xbrl.entities.BankLimit_Rep;
import com.bornfire.xbrl.entities.Counterparty_Entity;
import com.bornfire.xbrl.entities.Counterparty_Rep;
import com.bornfire.xbrl.entities.MIS_SETTLEMENT_ENTITY;
import com.bornfire.xbrl.entities.MIS_SETTLEMENT_ENTITY_REP;
import com.bornfire.xbrl.entities.MIS_TREASURY_LIMITS_ENTITY;
import com.bornfire.xbrl.entities.MIS_TREASURY_LIMITS_ENTITY_REP;
import com.bornfire.xbrl.entities.MIS_TREASURY_PLACEMENT_ENTITY;
import com.bornfire.xbrl.entities.RT_BankNameMaster;
import com.bornfire.xbrl.entities.RT_BankNameMasterRepository;
import com.bornfire.xbrl.entities.RT_CCR_DATA_TEMPLATE;
import com.bornfire.xbrl.entities.RT_CCR_DATA_TEMPLATE_REPOSITORY;
import com.bornfire.xbrl.entities.RT_CountryRiskDropdown;
import com.bornfire.xbrl.entities.RT_CountryRiskDropdownRepo;
import com.bornfire.xbrl.entities.RT_DataControl;
import com.bornfire.xbrl.entities.RT_DatacontrolRepository;
import com.bornfire.xbrl.entities.RT_ForeignCurrencyDeposit;
import com.bornfire.xbrl.entities.RT_ForeignCurrencyDepositRepository;
import com.bornfire.xbrl.entities.RT_FxRiskDataRepository;
import com.bornfire.xbrl.entities.RT_Fxriskdata;
import com.bornfire.xbrl.entities.RT_IRRBB_Data_Discount_Rates;
import com.bornfire.xbrl.entities.RT_IRRBB_Data_Discount_Rates_Repository;
import com.bornfire.xbrl.entities.RT_IRRBB_Data_EAR;
import com.bornfire.xbrl.entities.RT_IRRBB_Data_EAR_Repository;
import com.bornfire.xbrl.entities.RT_IRRBB_Data_Template;
import com.bornfire.xbrl.entities.RT_IRRBB_Data_Template_Repository;
import com.bornfire.xbrl.entities.RT_ImpactAnalysis;
import com.bornfire.xbrl.entities.RT_ImpactAnalysisRepository;
import com.bornfire.xbrl.entities.RT_Investment_Risk_Data_Dashboard_Template;
import com.bornfire.xbrl.entities.RT_Investment_Risk_Data_Dashboard_TemplateRepository;
import com.bornfire.xbrl.entities.RT_Investment_Securities_Data_Template;
import com.bornfire.xbrl.entities.RT_Investment_Securities_Data_Template_Repo;
import com.bornfire.xbrl.entities.RT_Liquidity_Risk_Dashboard_Template;
import com.bornfire.xbrl.entities.RT_Liquidity_Risk_Dashboard_Template_repository;
import com.bornfire.xbrl.entities.RT_Liquidity_Risk_Data_Template;
import com.bornfire.xbrl.entities.RT_Liquidity_Risk_Data_Template_Repository;
import com.bornfire.xbrl.entities.RT_MmData;
import com.bornfire.xbrl.entities.RT_MmDataRepository;
import com.bornfire.xbrl.entities.RT_NostroAccBalData;
import com.bornfire.xbrl.entities.RT_NostroAccBalDataRepository;
import com.bornfire.xbrl.entities.RT_RepoDataTemplate;
import com.bornfire.xbrl.entities.RT_RepoDataTemplateRepository;
import com.bornfire.xbrl.entities.RT_TradeLevelDataDerivatives;
import com.bornfire.xbrl.entities.RT_TradeLevelDataDerivativesSimplified;
import com.bornfire.xbrl.entities.RT_TradeLevelDataDerivativesRepository;
import com.bornfire.xbrl.entities.RT_TradeLevelDataDerivativesSimplifiedRepository;
import com.bornfire.xbrl.entities.RT_TradeMarketRiskData;
import com.bornfire.xbrl.entities.RT_TradeMarketriskDataRepository;
import com.bornfire.xbrl.entities.RT_TreasuryCreditEntity;
import com.bornfire.xbrl.entities.RT_TreasuryCreditRepo;
import com.bornfire.xbrl.entities.TreasuryPlacementRep;
import com.bornfire.xbrl.entities.UserProfile;
import com.bornfire.xbrl.entities.UserProfileRep;
import com.bornfire.xbrl.services.ASL_Excel_Services;
import com.bornfire.xbrl.services.AccessAndRolesServices;
import com.bornfire.xbrl.services.Excel_Services;
import com.bornfire.xbrl.services.LoginServices;
import com.bornfire.xbrl.services.RT_CCR_DATA_Service;
import com.bornfire.xbrl.services.RT_DataControlService;
import com.bornfire.xbrl.services.RT_ForeignCurrencyDepositService;
import com.bornfire.xbrl.services.RT_FxriskdataService;
import com.bornfire.xbrl.services.RT_ImpactAnalysisService;
import com.bornfire.xbrl.services.RT_InvestmentRiskDataDictionaryService;
import com.bornfire.xbrl.services.RT_InvestmentSecurity_Service;
import com.bornfire.xbrl.services.RT_Liquidity_Risk_Data_Service;
import com.bornfire.xbrl.services.RT_MmdataService;
import com.bornfire.xbrl.services.RT_NostroAccBalDataService;
import com.bornfire.xbrl.services.RT_RepoService;
import com.bornfire.xbrl.services.RT_TradeLevelDerivativesService;
import com.bornfire.xbrl.services.RT_TradeLevelDerivativesSimplifiedService;
import com.bornfire.xbrl.services.RT_TradeMarketRiskService;
import com.bornfire.xbrl.services.RT_TreasuryCredit_Service;
import com.bornfire.xbrl.services.counter_services;
@Controller
@ConfigurationProperties("default")
public class XBRLNavigationController {

	private static final Logger logger = LoggerFactory.getLogger(XBRLNavigationController.class);
	/*
	 * @PersistenceContext private EntityManager entityManager;
	 */

	@Autowired
	RT_Investment_Risk_Data_Dashboard_TemplateRepository RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS;
	@Autowired
	UserProfileRep UserProfileReps;
	@Autowired
	private BRF40_Rep1 brf40aRepository;
	@Autowired
	MIS_TREASURY_LIMITS_ENTITY_REP MIS_TREASURY_LIMITS_ENTITY_REPs;
	@Autowired
	private BRF40_Rep2 brf40bRepository;
	@Autowired
	BRF095AServiceRepo BRF095AServiceRepos;
	@Autowired
	BankLimit_Rep banklimit_rep;

	@Autowired
	MIS_SETTLEMENT_ENTITY_REP MIS_SETTLEMENT_ENTITY_REPs;
	@Autowired
	private ASL_BANKMASTER_REPO ASL_BANKMASTER_REPO;
	@Autowired
	private BRF39_ENTITYREP brf39_entityrep;
	@Autowired
	TreasuryPlacementRep TreasuryPlacementReps;
	@Autowired
	ASL_Excel_Services ASL_Excel_Servicess;
	@Autowired
	ASL_Report_Rep ASL_Report_Reps;
	@Autowired
	Excel_Services Excel_Servicess;
	@Autowired
	Counterparty_Rep Counterparty_Reps;
	@Autowired
	counter_services counter_servicess;
	@Autowired
	LoginServices loginServices;
	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private RT_FxriskdataService fxriskdataService;

	@Autowired
	AccessAndRolesServices AccessRoleService;

	@Autowired
	AccessandRolesRepository accessandrolesrepository;

	@Autowired
	RT_NostroAccBalDataRepository nostroAccBalRepo;

	@Autowired
	RT_NostroAccBalDataService nostroService;

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
	RT_RepoDataTemplateRepository repoRepo;

	@Autowired
	RT_TreasuryCreditRepo treasuryCreditRepo;

	@Autowired
	RT_RepoService repoService;

	@Autowired
	RT_TreasuryCredit_Service treasuryCreditService;
	@Autowired
	RT_InvestmentSecurity_Service investmentSecurity_Service;

	@Autowired
	RT_TradeLevelDataDerivativesRepository tradeleveldataderivativesRepo;

	@Autowired
	 RT_TradeLevelDerivativesService tradeleveldataderivativeService;

	@Autowired
	 RT_ForeignCurrencyDepositService foreigncurrencydepositService;

	@Autowired
	RT_ForeignCurrencyDepositRepository foreigncurrencydepositRepo;

	@Autowired
	RT_Investment_Securities_Data_Template_Repo investmentSecuritiesDataTemplateRepo;
	
	@Autowired
	private RT_ImpactAnalysisService impactanalysisService;
	
	@Autowired
	RT_Liquidity_Risk_Data_Service   liquidityRiskDataService;
	
	@Autowired
	RT_Liquidity_Risk_Data_Template_Repository   LiquidityRiskDataRepository;
	
@Autowired
	RT_ImpactAnalysisRepository impactanalysisRepo;

@Autowired 
RT_Liquidity_Risk_Dashboard_Template_repository LiquidityRiskDashboardRepo;

@Autowired

RT_TradeLevelDataDerivativesSimplifiedRepository tradeleveldataderivativessimplifiedRepo;

@Autowired
 RT_TradeLevelDerivativesSimplifiedService tradeleveldataderivativesimplifiedService;


@Autowired
RT_InvestmentRiskDataDictionaryService investmentriskdatadictionaryService;


@Autowired
RT_IRRBB_Data_Template_Repository IRRB_EVE_Repo;

@Autowired 
RT_IRRBB_Data_EAR_Repository IRRBB_EAR_Repository;

@Autowired 
RT_IRRBB_Data_Discount_Rates_Repository IRRBB_Data_Template_DiscountRate_repo;

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
		System.out.println("role id is : " + roleId);
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
	        @RequestParam(value = "RT_ReportsValue", required = false) String RT_ReportsValue,
	        @RequestParam(value = "aslValue", required = false) String aslValue,
	        @RequestParam(value = "aslUploadValue", required = false) String aslUploadValue,
	        @RequestParam(value = "auditUsValue", required = false) String auditUsValue,
	        @RequestParam(value = "finalString", required = false) String finalString,
	        @ModelAttribute AccessAndRoles alertparam, Model md, HttpServletRequest rq) {

	    String userid = (String) rq.getSession().getAttribute("USERID");
	    String roleId = (String) rq.getSession().getAttribute("ROLEID");
	    md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

	    String msg = AccessRoleService.addPARAMETER(alertparam, formmode, adminValue, RT_ReportsValue,
	            aslValue, aslUploadValue, auditUsValue, finalString, userid);

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

		} else if (formmode.equals("add")) {
			md.addAttribute("formmode", formmode);
			md.addAttribute("userProfile", loginServices.getUser(""));
		} else if (formmode.equals("verify")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("userProfile", loginServices.getUser(userid));

		} else {

			md.addAttribute("formmode", formmode);
			md.addAttribute("FinUserProfiles", loginServices.getFinUsersList());
			md.addAttribute("userProfile", loginServices.getUser(""));

		}

		return "XBRLUserprofile";
	}
	
	
	@RequestMapping(value = "verifyUser", method = RequestMethod.POST)
	@ResponseBody
	public String verifyUser(@ModelAttribute UserProfile userprofile, Model md, HttpServletRequest rq) {
		String userid = (String) rq.getSession().getAttribute("USERID");
		String msg = loginServices.verifyUser(userprofile, userid);

		return msg;

	}

	@GetMapping("/getRoleDetails")
	@ResponseBody
	public AccessAndRoles getRoleDetails(@RequestParam String roleId) {
		System.out.println("role id for fetching is : " + roleId);
		return accessandrolesrepository.findById(roleId).orElse(null);
	}

	@RequestMapping(value = "createUser", method = RequestMethod.POST)
	@ResponseBody
	public String createUser(@RequestParam("formmode") String formmode, @ModelAttribute UserProfile userprofile,
			Model md, HttpServletRequest rq) {

		String mob = (String) rq.getSession().getAttribute("MOBILENUMBER");
		String role = (String) rq.getSession().getAttribute("ROLEDESC");
		String userId = (String) rq.getSession().getAttribute("USERID");
		String userName = (String) rq.getSession().getAttribute("USERNAME");
		String msg = loginServices.addUser(userprofile, formmode, userId, userName, mob, role);

		return msg;
	}

	@RequestMapping(value = "deleteuser", method = RequestMethod.POST)
	@ResponseBody
	public String deleteuser(@RequestParam("formmode") String userid, Model md, HttpServletRequest rq) {

		String msg = loginServices.deleteuser(userid);

		return msg;

	}

	// In Reports
	// In Modify section If we select the Bank name other details are fetching code
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

	// In Modify section If we select the Country of Risk, cbuaeGeographicalZone
	// fetching code
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

	// Creating Data Control
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

	// Nostro Report Codes Given Below
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

		return "RT/Nostro_Account_Bal";
	}

	// Update Submit code in nostro
	@RequestMapping("/updateNostro")
	@ResponseBody
	public String updateNostro(@ModelAttribute RT_NostroAccBalData nostroData, HttpServletRequest req) {

		boolean updated = nostroService.updateNostro(nostroData);
		System.out.println("msg is : " + updated);

		if (updated) {
			System.out.println("Update successful");
			return "Updated successfully";
		} else {
			System.out.println("Update Record not found for update");
			return "Record not found for update";
		}

	}

//-------------------------------------Repo Report Start---------------------------------------
// Repo Report code
	@RequestMapping(value = "Repo_Data_Template", method = RequestMethod.GET)
	public String RepoDataTemplate(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) Long slNo, // changed from accountNo to slNo
			Model md, HttpServletRequest req) {

		if ("edit".equalsIgnoreCase(formmode) && slNo != null) {
			RT_RepoDataTemplate data = repoRepo.findById(slNo).orElse(null); // make sure entity class matches
			md.addAttribute("repoData", data);
			System.out.println("edit is formmode");
			md.addAttribute("formmode", "edit");
		} else if ("list".equalsIgnoreCase(formmode)) {
			md.addAttribute("repoList", repoRepo.getlist());
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

		return "RT/Repo_Data_Template";
	}

//Updated data saving code for Repo
	@RequestMapping("/updateRepo")
	@ResponseBody
	public String updateRepo(@ModelAttribute RT_RepoDataTemplate repoData, HttpServletRequest request) {

		// Call the update logic from service or directly use the repository if simple
		boolean updated = repoService.updateRepoData(repoData);

		if (updated) {
			System.out.println("Update successful for SL_NO: " + repoData.getSlNo());
			return "Updated successfully";
		} else {
			System.out.println("Record not found for update for SL_NO: " + repoData.getSlNo());
			return "Record not found for update";
		}
	}

//Downloading Excel for Repo
	@RequestMapping(value = "downloadRepoExcel", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadRepoExcel() throws IOException {
		System.out.println("Entered controller downloadRepoExcel");

		File excelFile = repoService.generateRepoExcel();
		byte[] excelData = java.nio.file.Files.readAllBytes(excelFile.toPath());

		HttpHeaders headersResponse = new HttpHeaders();
		headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headersResponse.setContentDispositionFormData("attachment", "RepoDataTemplate.xls");

		return ResponseEntity.ok().headers(headersResponse).body(excelData);
	}

//-------------------------------------Repo Report End---------------------------------------

	// -------------------------------------Treasury_Credit_Limit_Management---------------------------------------

	 @RequestMapping(value = "Treasury_Credit_Limit_Management", method = RequestMethod.GET)
	    public String TreasuryCredit(@RequestParam(required = false) String formmode,
	                                 @RequestParam(required = false) Integer slNo,
	                                 Model model) {

	        if ("edit".equalsIgnoreCase(formmode) && slNo != null) {
	            model.addAttribute("formmode", "edit");
	            model.addAttribute("creditData", treasuryCreditRepo.findById(slNo)
	                .orElse(new RT_TreasuryCreditEntity()));
	        } else if ("list".equalsIgnoreCase(formmode)) {
	            model.addAttribute("formmode", "list");
	            model.addAttribute("TClist", treasuryCreditRepo.getTClist());
	        } else {
	            model.addAttribute("formmode", "add");
	        }

	        List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
	        List<RT_CountryRiskDropdown> countryList = countryRepo.findAllByOrderByCountryOfRiskAsc();
	        model.addAttribute("bankList", bankList);
	        model.addAttribute("countryList", countryList);

	        return "RT/Treasury_Credit.html";
	    }


	@PostMapping("/updateTreasuryCredit")
	@ResponseBody
	public String updateTreasuryCredit(@ModelAttribute("creditData") RT_TreasuryCreditEntity creditData,
			RedirectAttributes redirectAttributes) {

		boolean updated = treasuryCreditService.updateTreasuryCredit(creditData);

		/*
		 * if (updated) { redirectAttributes.addFlashAttribute("updateMsg",
		 * "Updated Successfully"); } else {
		 * redirectAttributes.addFlashAttribute("updateMsg",
		 * "Record not found for update"); }
		 * 
		 * return "redirect:/Treasury_Credit_Limit_Management?formmode=list";
		 */

		if (updated) {
			return "Updated successfully";
		} else {
			return "Record not found for update";
		}
	}

	
	
	@RequestMapping(value = "/downloadTreasuryCreditExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadTreasuryCreditExcel() {
		logger.info("Controller: Received request for Trade Market Risk Excel download.");

		try {
			byte[] excelData = treasuryCreditService.generateTreasuryExcel();

			if (excelData.length == 0) {
				logger.warn("Controller: Service returned no data. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			HttpHeaders headers = new HttpHeaders();
			String filename = "CBUAE_Treasury_Credit_Limit_Management_Data_Template.xls";
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (FileNotFoundException e) {
			logger.error("Controller ERROR: A required template file was not found.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			logger.error("Controller ERROR: A critical error occurred during file generation.", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	// -------------------------------------Treasury_Credit_Limit_Management
	// End---------------------------------------

	// -------------------------------------Investent_Securities_Data
	// START---------------------------------------

	/*
	 * @RequestMapping(value = "Investment_Securities_Data", method =
	 * RequestMethod.GET) public String TreasuryCredit(@RequestParam(required =
	 * false) BigDecimal siNo,String formmode,
	 * 
	 * @RequestParam(required = false) Model model) {
	 * 
	 * if ("edit".equalsIgnoreCase(formmode) && siNo != null) {
	 * model.addAttribute("formmode", "edit"); model.addAttribute("creditData",
	 * treasuryCreditRepo.findById(siNo).orElse(new RT_TreasuryCreditEntity())); }
	 * else if ("list".equalsIgnoreCase(formmode)) { model.addAttribute("formmode",
	 * "list"); model.addAttribute("TClist", treasuryCreditRepo.getTClist()); } else
	 * { model.addAttribute("formmode", "add"); }
	 * 
	 * return "RT/InvestmentSecurityData.html";
	 */

	@RequestMapping(value = "Investment_Securities_Data", method = RequestMethod.GET)
	public String treasuryCredit(@RequestParam(required = false) String siNo,
	                             @RequestParam(required = false) String formmode,
	                             Model model) {

	    if ("edit".equalsIgnoreCase(formmode) && siNo != null) {
			model.addAttribute("formmode", "edit");
			model.addAttribute("InvestmentData", investmentSecuritiesDataTemplateRepo.findById(siNo).orElse(new RT_Investment_Securities_Data_Template()));
		}else if ("list".equalsIgnoreCase(formmode)) {
	        List<RT_Investment_Securities_Data_Template> list =
	            investmentSecuritiesDataTemplateRepo.getsecDatalist();

	        model.addAttribute("formmode", "list");
	        model.addAttribute("ISList", list); // Used in HTML table
		} /*
			 * else { model.addAttribute("formmode", "add");
			 * model.addAttribute("securityData", new
			 * RT_Investment_Securities_Data_Template()); }
			 */else {
			model.addAttribute("formmode", "add");
		}
 
        List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
        List<RT_CountryRiskDropdown> countryList = countryRepo.findAllByOrderByCountryOfRiskAsc();
        model.addAttribute("bankList", bankList);
        model.addAttribute("countryList", countryList);
        
	    return "RT/Investment_SecurityData";
	}

	@PostMapping("/updateInvestmentSecurity")
	@ResponseBody
	public String updateInvestmentSecurity(
			@ModelAttribute("InvestmentData") RT_Investment_Securities_Data_Template InvestmentData) {

		boolean updated = investmentSecurity_Service.updateInvestmentSecurity(InvestmentData);

		if (updated) {
			return "Updated successfully";
		} else {
			return "Record not found for update";
		}
	}

	
	
	// -------------------------------------Liquidity_Risk_Data -start---------------------------------------

	@RequestMapping(value = "Liquidity_Risk_Data", method = RequestMethod.GET)
	public String liquidityData(@RequestParam(required = false) String formmode,
	                            @RequestParam(required = false) BigDecimal slno,
	                            Model model) {

	    if ("edit".equalsIgnoreCase(formmode) && slno != null) {
	        model.addAttribute("formmode", "edit");
	        model.addAttribute("liquidityData", LiquidityRiskDataRepository.findById(slno)
	                                .orElse(new RT_Liquidity_Risk_Data_Template()));
	    } else if ("list".equalsIgnoreCase(formmode)) {
	        model.addAttribute("formmode", "list");
	        model.addAttribute("liquidityList", LiquidityRiskDataRepository.getLiquiditylist());
	    } else {
	        model.addAttribute("formmode", "add");
	        model.addAttribute("liquidityData", new RT_Liquidity_Risk_Data_Template());
	    }

	    List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
        List<RT_CountryRiskDropdown> countryList = countryRepo.findAllByOrderByCountryOfRiskAsc();
        model.addAttribute("bankList", bankList);
        model.addAttribute("countryList", countryList);

	    return "RT/Liquidity_Risk_Data";
	}


	@PostMapping("/updateLiquidityRiskData")
    @ResponseBody
    public String updateLiquidityRiskData(@ModelAttribute RT_Liquidity_Risk_Data_Template LiquidityData) {
        boolean updated = liquidityRiskDataService.updateLiquidityRisk(LiquidityData);

        if (updated) {
            return "Updated successfully";
        } else {
            return "Record not found for update";
        }
    }
		// -------------------------------------Liquidity_Risk_Data End---------------------------------------
	
	@Autowired
	private RT_InvestmentSecurity_Service rtInvestmentSecuritiesService;

	// ... other controller methods ...

	@RequestMapping(value = "/downloadInvestmentSecuritiesExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadInvestmentSecuritiesExcel() {
		logger.info("Controller: Received request for Investment Securities Excel download.");

		try {
			byte[] excelData = rtInvestmentSecuritiesService.generateInvestmentSecuritiesExcel();

			if (excelData.length == 0) {
				logger.warn(
						"Controller: Service returned no data for Investment Securities. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			HttpHeaders headers = new HttpHeaders();
			String filename = "CBUAE_Investment_Securities_Data_Template_Pillar2.xls";
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);

			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (FileNotFoundException e) {
			logger.error("Controller ERROR: The CBUAE_Investment_Securities_Data_Template_Pillar2.xls template file was not found.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			logger.error("Controller ERROR: A critical error occurred during Investment Securities file generation.",
					e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	// -------------------------------------Investent_Securities_Data
	// End---------------------------------------

//-------------------------------------Investment Risk Data Report Start---------------------------------------
	// Investment Report code
	@RequestMapping(value = "Investment_Risk_Data_Dashboard_Template", method = RequestMethod.GET)
	public String InvestmentRiskDataDashboardTemplate(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String SI_NO, // changed from accountNo to slNo
			Model md, HttpServletRequest req) {
		if ("edit".equalsIgnoreCase(formmode) && SI_NO != null && !SI_NO.isEmpty()) {
			Long Serialnumber=Long.parseLong(SI_NO);
			RT_Investment_Risk_Data_Dashboard_Template data = RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS.getParticularDataBySI_NO(Serialnumber);
			md.addAttribute("investmentriskdatadashboard", data);
			System.out.println("edit is formmode");
			md.addAttribute("formmode", "edit");
		} else if ("list".equalsIgnoreCase(formmode)) {

			System.out.println("RT" + RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS.getlist().size());
			md.addAttribute("InvestmentRiskDatalist", RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS.getlist());

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

		return "RT/Investment_Risk_Data_Dashboard_Template";
	}
	
	
	@PostMapping("/updateinvestmentriskdatadictionary")
	@ResponseBody
	public String updateinvestmentriskdatadictionary(
			@ModelAttribute RT_Investment_Risk_Data_Dashboard_Template investmentriskdatadashboard ) {
		boolean updated = investmentriskdatadictionaryService.updateinvestmentriskdatadictionary(investmentriskdatadashboard);

		if (updated) {
			return "Updated successfully";
		} else {
			return "Record not found for update";
		}

	}

	// Updated data saving code for Repo
	@RequestMapping("/updateInvenstment")
	@ResponseBody
	public String updateInvenstment(@ModelAttribute RT_RepoDataTemplate repoData, HttpServletRequest request) {

		// Call the update logic from service or directly use the repository if simple
		boolean updated = repoService.updateRepoData(repoData);

		if (updated) {
			System.out.println("Update successful for SL_NO: " + repoData.getSlNo());
			return "Updated successful";
		} else {
			System.out.println("Record not found for update for SL_NO: " + repoData.getSlNo());
			return "Record not found for update";
		}
	}

	// Downloading Excel for Repo
	@RequestMapping(value = "downloadInvestmentExcel", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadInvenstmentExcel() throws IOException {
		System.out.println("Entered controller downloadRepoExcel");

		File excelFile = repoService.generateRepoExcel();
		byte[] excelData = java.nio.file.Files.readAllBytes(excelFile.toPath());

		HttpHeaders headersResponse = new HttpHeaders();
		headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headersResponse.setContentDispositionFormData("attachment", "RepoDataTemplate.xls");

		return ResponseEntity.ok().headers(headersResponse).body(excelData);
	}

//-------------------------------------Repo Report End---------------------------------------		
	@PostMapping("/updateFxriskdata")
	@ResponseBody
	public String updateFxriskdata(@ModelAttribute RT_Fxriskdata fxriskData) {
		boolean updated = fxriskdataService.updateFxriskdata(fxriskData);

		if (updated) {
			return "Updated successful";
		} else {
			return "Record not found for update";
		}
	}

	@PostMapping("/updateMmdata")
	@ResponseBody
	public String updateMmdata(@ModelAttribute RT_MmData mmData) {
		boolean updated = mmdataService.updateMmdata(mmData);

		if (updated) {
			return "Updated successful";
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
	public String Fxriskdata(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String SI_NO, Model md, HttpServletRequest req) {

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

			// You had md.addAttribute("formmode", "null"); — removed this line because it
			// would overwrite the previous one
		}

		List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
		md.addAttribute("bankList", bankList);

		return "RT/Fx_Risk_Data";
	}

	// For download excel for fxriskdata

	@RequestMapping(value = "/downloadFxriskExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadFxriskExcel() {
	    logger.info("Controller: Received request for Fx Risk Excel download.");

	    try {
	        byte[] excelData = fxriskdataService.generateFxRiskExcel();

	        if (excelData.length == 0) {
	            logger.warn("Controller: Service returned no data. Responding with 204 No Content.");
	            return ResponseEntity.noContent().build();
	        }

	        ByteArrayResource resource = new ByteArrayResource(excelData);

	        HttpHeaders headers = new HttpHeaders();
	        String filename = "FxriskData.xls";
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

	        logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
	        return ResponseEntity.ok()
	                .headers(headers)
	                .contentLength(excelData.length)
	                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	                .body(resource);

	    } catch (FileNotFoundException e) {
	        logger.error("Controller ERROR: A required template file was not found.", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } catch (Exception e) {
	        logger.error("Controller ERROR: A critical error occurred during file generation.", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
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
	private RT_TradeMarketriskDataRepository trade_market_risk_repo;

	@RequestMapping(value = "/Trade_Market_Risk", method = RequestMethod.GET)
	public String tradeMarketRisk(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date reportDate, Model model,
			HttpServletRequest request) {

		if ("edit".equalsIgnoreCase(formmode) && reportDate != null) {
			RT_TradeMarketRiskData data = trade_market_risk_repo.findById(reportDate).orElse(null);
			model.addAttribute("data", data);
			model.addAttribute("formmode", "edit");
			System.out.println("Edit mode activated");
		} else if ("list".equalsIgnoreCase(formmode)) {
			List<RT_TradeMarketRiskData> list = trade_market_risk_repo.findAll();
			model.addAttribute("dataList", list);
			model.addAttribute("formmode", "list");
			System.out.println("List mode activated");
		} else {
			model.addAttribute("data", new RT_TradeMarketRiskData());
			model.addAttribute("formmode", "add");
			model.addAttribute("formmode", "null");
			System.out.println("Add mode activated");
		}

		return "RT/Trade_Market_Risk";
	}

	@RequestMapping(value = "updateTradeMarketRisk", method = RequestMethod.POST)
	@ResponseBody
	public String updateTradeMarketRisk(@ModelAttribute RT_TradeMarketRiskData data, HttpServletRequest request) {

		System.out.println("User [" + request.getSession().getAttribute("USERID")
				+ "] requested update for Report Date: " + data.getReportDate());

		String userid = (String) request.getSession().getAttribute("USERID");

		try {
			if (data.getReportDate() == null) {
				return "Error: Report Date (ID) is missing.";
			}

			if (!trade_market_risk_repo.existsById(data.getReportDate())) {
				return "Error: Record not found for update.";
			}

			data.setModifyFlg("Y");
			data.setEntityFlg("Y");
			data.setDelFlg("N");
			data.setModifyUser(userid);

			trade_market_risk_repo.save(data);

			return "Updated successfully";
		} catch (Exception e) {
			return "Error while updating: " + e.getMessage();
		}
	}

	@Autowired
	private RT_TradeMarketRiskService rtTradeMarketRiskService;

	@RequestMapping(value = "/downloadTradeMarketRiskExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadTradeMarketRiskExcel() {
		logger.info("Controller: Received request for Trade Market Risk Excel download.");

		try {
			byte[] excelData = rtTradeMarketRiskService.generateTradeMarketRiskExcel();

			if (excelData.length == 0) {
				logger.warn("Controller: Service returned no data. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			HttpHeaders headers = new HttpHeaders();
			String filename = "TradeMarketRiskData.xls";
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (FileNotFoundException e) {
			logger.error("Controller ERROR: A required template file was not found.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			logger.error("Controller ERROR: A critical error occurred during file generation.", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@RequestMapping(value = "Mm_Data", method = RequestMethod.GET)
	public String Mmdata(@RequestParam(required = false) String formmode, @RequestParam(required = false) String SI_NO,
			Model md, HttpServletRequest req) {

		if ("edit".equalsIgnoreCase(formmode) && SI_NO != null && !SI_NO.isEmpty()) {
			RT_MmData data = mmdataRepo.getParticularDataBySI_NO(SI_NO);
			md.addAttribute("mmData", data);
			System.out.println("edit is formmode");
			md.addAttribute("formmode", "edit");

		} else if ("list".equalsIgnoreCase(formmode)) {
			md.addAttribute("branchList", mmdataRepo.getlist());
			System.out.println("list is formmode");
			md.addAttribute("formmode", "list");

		} else {
			md.addAttribute("formmode", "add");
			md.addAttribute("formmode", "null");

			// You had md.addAttribute("formmode", "null"); — removed this line because it
			// would overwrite the previous one
		}

		List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
		List<RT_CountryRiskDropdown> countryList = countryRepo.findAllByOrderByCountryOfRiskAsc();
		md.addAttribute("bankList", bankList);
		md.addAttribute("countryList", countryList);

		return "RT/Mm_Data";
	}

	@RequestMapping(value = "/downloadMmExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadMmExcel() {
	    logger.info("Controller: Received request for MM Excel download.");

	    try {
	    	
	        byte[] excelData = mmdataService.generateMmExcel();

        if (excelData.length == 0) {
	            logger.warn("Controller: MM Excel file has no data. Returning 204.");
	            return ResponseEntity.noContent().build();
	        }

	        ByteArrayResource resource = new ByteArrayResource(excelData);
	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Mmdata.xls");

	        return ResponseEntity.ok()
	                .headers(headers)
	                .contentLength(excelData.length)
	                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	                .body(resource);

	    } catch (FileNotFoundException e) {
	        logger.error("Controller ERROR: MM template file not found.", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } catch (Exception e) {
	        logger.error("Controller ERROR: Error generating MM Excel file.", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	@RequestMapping(value = "counterparty", method = { RequestMethod.GET, RequestMethod.POST })
	public String counterparty(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String id, @RequestParam(required = false) String userid, Model md,
			HttpServletRequest req) {
		logger.info("Enter controller of counterparty");

		md.addAttribute("menu", "List Of Counterparty Bank"); // To highlight the menu
		String domIds = ((String) req.getSession().getAttribute("DOMAINID")).trim();
		md.addAttribute("dom_ids", domIds); // To highlight the menu
		if (formmode == null || formmode.equals("list")) {
			md.addAttribute("formmode", "list");
			md.addAttribute("currentDate", new Date()); // java.util.Date
			md.addAttribute("listall", Counterparty_Reps.getalllist());
		} else if (formmode.equals("edit")) {
			md.addAttribute("formmode", formmode);
			logger.info("modify screen id is: " + id);
			md.addAttribute("list", Counterparty_Reps.getBYID(Long.valueOf(id)));
			List<String> counterList = Counterparty_Reps.getall();
			md.addAttribute("counterlist", counterList);
			List<String> getcode = Counterparty_Reps.getcodes();
			md.addAttribute("getcode", getcode);
		} else {
			logger.info("Adding new Counterparty Bank form initialization started.");
			md.addAttribute("menu", "Counterparty Bank - Add");
			md.addAttribute("formmode", formmode);

			List<String> counterList = Counterparty_Reps.getall();
			md.addAttribute("counterlist", counterList);
			List<String> getcode = Counterparty_Reps.getcodes();
			md.addAttribute("getcode", getcode);
			logger.info("Fetched counterparty list: size={}", counterList.size());

			Integer maxSuffix = Counterparty_Reps.findMaxSrlNoSuffix(); // e.g., returns 1
			int nextSuffix = (maxSuffix != null) ? maxSuffix + 1 : 1;
			String serialNo = String.format("%s%03d", "BOBUAE", nextSuffix); // BOBUAE002
			md.addAttribute("serialNo", serialNo);
			logger.info("Generated serial number: {}", serialNo);
		}
		return "MIS/counterparty.html";
	}

	@RequestMapping(value = "Addcounter", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String Addcounter(@ModelAttribute Counterparty_Entity Counterparty_Entity,
			@RequestParam(required = false) String formmode, Model md, HttpServletRequest rq) {
		logger.info("Add counter party...");
		String userid = (String) rq.getSession().getAttribute("USERID");
		String msg = counter_servicess.addcunter(Counterparty_Entity, userid, formmode);
		return msg;

	}

	@RequestMapping(value = "counterparty_list", method = { RequestMethod.GET, RequestMethod.POST })
	public String counterparty_list(@RequestParam(required = false) String formmode, Model md, HttpServletRequest req) {
		String userid = (String) req.getSession().getAttribute("USERID");
		String BRANCHCODE = (String) req.getSession().getAttribute("BRANCHCODE");
		String BRANCHNAME = (String) req.getSession().getAttribute("BRANCHNAME");
		String ROLEID = (String) req.getSession().getAttribute("ROLEID");
		String domIds = ((String) req.getSession().getAttribute("DOMAINID")).trim();
		logger.info("User '{}' accessed counterparty_list with formmode='{}'", userid, formmode);

		md.addAttribute("dom_ids", domIds); // To highlight the menu

		if ("list".equalsIgnoreCase(formmode)) {
			logger.info("Loading exposure data list view for branch '{}'", BRANCHNAME);

			md.addAttribute("menu", "List Of Exposure Data"); // Default menu label
			md.addAttribute("formmode", "list");
			Date currentDate = new Date(); // current system date
			md.addAttribute("currentDate", currentDate);
			List<String> branchList = ASL_Report_Reps.getAllBranchNames();
			md.addAttribute("branches", branchList);
			md.addAttribute("currentBranch", BRANCHNAME);
			List<String> branchesl;
			if ("ADM".equalsIgnoreCase(ROLEID)) {
				branchesl = ASL_Report_Reps.getAllBranchNames();
			} else {
				branchesl = Collections.singletonList(BRANCHNAME);
			}

			md.addAttribute("branchesl", branchesl);
			LocalDate today = LocalDate.now();
			java.sql.Date sqlDate = java.sql.Date.valueOf(today);
			List<ASL_Report_Entity> list = ASL_Report_Reps.getAlls(sqlDate, BRANCHNAME.trim());
			logger.info("Fetched {} exposure records for branch '{}' date '{}'", list.size(), BRANCHNAME, sqlDate);
			md.addAttribute("listall", list);

		} else {
			logger.info("Opening file upload mode for counterparty. User: '{}', Branch: '{}'", userid, BRANCHNAME);
			md.addAttribute("menu", "List Of Counterparty"); // Default menu label
			md.addAttribute("menu", "Upload File Of Counterparty");
			md.addAttribute("formmode", "add");
			md.addAttribute("userid", userid);

			if ("ADM".equalsIgnoreCase(ROLEID)) {
				List<String> codes = UserProfileReps.getallcodes();
				logger.info("Counter party bank code Size: '{}', Role id is :'{}'", codes.size(), ROLEID);
				md.addAttribute("codes", codes);
			}
			// Add for all roles
			md.addAttribute("BRANCHCODE", BRANCHCODE);
			md.addAttribute("BRANCHNAME", BRANCHNAME);
			md.addAttribute("ROLEID", ROLEID);

		}

		return "MIS/counterpart_list.html";
	}

	@RequestMapping(value = "/asl_dashboard", method = { RequestMethod.GET, RequestMethod.POST })
	public String asl_dashboard(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String reportDate, Model md, HttpServletRequest req) {
		String domIds = ((String) req.getSession().getAttribute("DOMAINID")).trim();
		md.addAttribute("dom_ids", domIds); // To highlight the menu
		if (formmode == null || formmode.equals("list")) {
			logger.info("list of Final Risk View.");
			md.addAttribute("formmode", "list");
			Date currentDate = new Date(); // current system date
			List<BankLimit_Entity> banklist = banklimit_rep.getalllist(currentDate);
			md.addAttribute("list", banklist);
			md.addAttribute("currentDate", currentDate);

			// Bank Limit
			md.addAttribute("Bank_limit_size", banklist.size());
			// Exposure Datas:-
			List<ASL_Report_Entity> Exposure = ASL_Report_Reps.getAllListByDate(currentDate);
			md.addAttribute("Exposure_size", Exposure.size());
			// List Of Treasury Placements
			List<MIS_TREASURY_PLACEMENT_ENTITY> placements = TreasuryPlacementReps.getAllListByDate(currentDate);
			md.addAttribute("placements_size", placements.size());
			/// List Of Treasury Limits
			// List<MIS_TREASURY_LIMITS_ENTITY> limits =
			/// MIS_TREASURY_LIMITS_ENTITY_REPs.getAllListByDate(currentDate);
			// md.addAttribute("Treasry_size", limits.size());
			/// swap_settlement
			List<MIS_SETTLEMENT_ENTITY> swapList = MIS_SETTLEMENT_ENTITY_REPs.getAllListByDate(currentDate);
			md.addAttribute("swap_size", swapList.size());

		} else if (formmode.equals("search")) {
			logger.info("list of Final Risk View.");
			md.addAttribute("formmode", "list");
			Date reportDateConverted = java.sql.Date.valueOf(reportDate); // current system date
			md.addAttribute("list", banklimit_rep.getalllist(reportDateConverted));
			md.addAttribute("currentDate", reportDateConverted);

			// Exposure Datas:-
			List<ASL_Report_Entity> Exposure = ASL_Report_Reps.getAllListByDate(reportDateConverted);
			md.addAttribute("Exposure_size", Exposure.size());
			// List Of Treasury Placements
			List<MIS_TREASURY_PLACEMENT_ENTITY> placements = TreasuryPlacementReps
					.getAllListByDate(reportDateConverted);
			md.addAttribute("placements_size", placements.size());
			/// List Of Treasury Limits
			List<MIS_TREASURY_LIMITS_ENTITY> limits = MIS_TREASURY_LIMITS_ENTITY_REPs
					.getAllListByDate(reportDateConverted);
			md.addAttribute("Treasry_size", limits.size());
			/// swap_settlement
			List<MIS_SETTLEMENT_ENTITY> swapList = MIS_SETTLEMENT_ENTITY_REPs.getAllListByDate(reportDateConverted);
			md.addAttribute("swap_size", swapList.size());

		}
		logger.info("Returning view: Asl_dashboard");

		return "MIS/Asl_dashboard";
	}

	@RequestMapping(value = "/CallmanualReconprocedurerun", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> CallmanualReconprocedurerun(
			@RequestParam("report_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date reportDate) {

		String msg = "";
		System.out.println("Reconciliation Started");

		// Format the date to '27-MAY-2025'
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
		String formattedDate = sdf.format(reportDate).toUpperCase();

		// Call stored procedure with 1 VARCHAR2 parameter
		/*
		 * StoredProcedureQuery query =
		 * entityManager.createStoredProcedureQuery("MIS_REPORT_WORKING");
		 * query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		 * query.setParameter(1, formattedDate); query.execute();
		 */

		msg = "Reconciliation completed for " + formattedDate;

		return ResponseEntity.ok(msg);
	}

	@RequestMapping(value = "Add_ASL", method = { RequestMethod.POST })
	@ResponseBody
	public String Add_ASL(@RequestParam("iBranchCode") String iBranchCode,
			@RequestParam("iBranchName") String iBranchName,
			@RequestParam("reportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate,
			@RequestParam("file") MultipartFile file, @RequestParam("uploadedBy") String uploadedBy,
			HttpServletRequest request, @RequestParam(required = false) String mode) {
		String msg = "";
		String userid = (String) request.getSession().getAttribute("USERID");
		if (mode.equals("exposure")) {
			logger.info("Enter theExposure data Upload File: " + "Branch: " + iBranchCode + ", File: "
					+ file.getOriginalFilename());
			msg = ASL_Excel_Servicess.addASL(iBranchCode, iBranchName, reportDate, uploadedBy, file, userid, mode);
		} else if (mode.equals("placement")) {
			logger.info("Enter theExposure data Upload File: " + "Branch: " + iBranchCode + ", File: "
					+ file.getOriginalFilename());
			msg = ASL_Excel_Servicess.addASL(iBranchCode, iBranchName, reportDate, uploadedBy, file, userid, mode);
		} else if (mode.equals("settlement")) {
			logger.info("Enter theExposure data Upload File: " + "Branch: " + iBranchCode + ", File: "
					+ file.getOriginalFilename());
			msg = ASL_Excel_Servicess.addASL(iBranchCode, iBranchName, reportDate, uploadedBy, file, userid, mode);

		}
		return msg;
	}

	@RequestMapping(value = "/getvalues", method = RequestMethod.POST)
	@ResponseBody
	public List<BRF_095_A_REPORT_ENTITY> getvalues(@RequestParam(required = false) String year) {
		logger.info("Received request to fetch BRF095A values for year: {}", year);

		List<BRF_095_A_REPORT_ENTITY> resultList = null;
		try {
			resultList = BRF095AServiceRepos.getvalues(year);
			logger.info("Successfully fetched {} records for year: {}", (resultList != null ? resultList.size() : 0),
					year);
		} catch (Exception e) {
			logger.error("Error while fetching BRF095A values for year {}: {}", year, e.getMessage(), e);
		}

		return resultList;
	}

	// Fetch bank details by bank name
	@RequestMapping(value = "/loadbankdata", method = RequestMethod.POST)
	@ResponseBody
	public List<ASL_BANKMASTER_ENTITY> getBankDetailss(@RequestParam(required = false) String name) {
		logger.info("Fetching bank details for bank name: {}", name);

		List<ASL_BANKMASTER_ENTITY> bankDetails = ASL_BANKMASTER_REPO.getBankDetails(name);

		logger.info("Found {} bank record(s) for name: {}", bankDetails.size(), name);

		return bankDetails;
	}

	@RequestMapping(value = "/banks", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getAllBanks() {
		logger.info("Fetching all distinct bank names");

		List<String> bankNames = ASL_BANKMASTER_REPO.findDistinctBankNames();

		logger.info("Total banks retrieved: {}", bankNames.size());

		return bankNames;
	}

	@RequestMapping(value = "/loadOverallTop10BRF39", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> loadOverallTop10BRF39Data(HttpServletRequest req) {
		logger.info("Entered /loadOverallTop10BRF39 ");

		List<Map<String, Object>> list = counter_servicess.loadOverallTop10BRF39Data();
		return list;
	}

	@RequestMapping(value = "/getcounterpartylist", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getcounterpartylist(HttpServletRequest req) {
		logger.info("Entered /getcounterpartylist ");

		List<Map<String, Object>> list = counter_servicess.getcounterpartylist();
		return list;
	}

	private String getCurrentYear() {
		return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
	}

	@RequestMapping(value = "/individualraw", method = RequestMethod.GET)
	@ResponseBody
	public List<BRF40_Entity1> getRawIndividualExposureData(
			@RequestParam(name = "year", required = false) String yearParam) {

		String year = (yearParam == null || yearParam.trim().isEmpty()) ? getCurrentYear() : yearParam;
		logger.info("Fetching individual raw exposure data for year: {}", year);

		try {
			List<BRF40_Entity1> data = brf40aRepository.getassetvalues(year);
			if (data == null || data.isEmpty()) {
				logger.warn("No data found for year: {}", year);
				return new ArrayList<>();
			}
			logger.info("Fetched {} records for year: {}", data.size(), year);
			return data;
		} catch (Exception e) {
			logger.error("Error while fetching individual raw exposure data for year: {}", year, e);
			return new ArrayList<>();
		}
	}

	@RequestMapping(value = "/corporateraw", method = RequestMethod.GET)
	@ResponseBody
	public List<BRF40_Entity1> getRawCorporateExposureData(
			@RequestParam(name = "year", required = false) String yearParam) {

		String year = (yearParam == null || yearParam.trim().isEmpty()) ? getCurrentYear() : yearParam;
		logger.info("Fetching corporate raw exposure data for year: {}", year);

		try {
			List<BRF40_Entity1> data = brf40aRepository.getassetvalues(year);
			if (data == null || data.isEmpty()) {
				logger.warn("No corporate exposure data found for year: {}", year);
				return new ArrayList<>();
			}
			logger.info("Fetched {} corporate exposure records for year: {}", data.size(), year);
			return data;
		} catch (Exception e) {
			logger.error("Error while fetching corporate exposure data for year: {}", year, e);
			return new ArrayList<>();
		}
	}

	@RequestMapping(value = "/economicsectorsraw", method = RequestMethod.GET)
	@ResponseBody
	public List<BRF40_Entity2> getRawEconomicSectorsExposureData(
			@RequestParam(name = "year", required = false) String yearParam) {

		String year = (yearParam == null || yearParam.trim().isEmpty()) ? getCurrentYear() : yearParam;
		logger.info("Fetching economic sector exposure data for year: {}", year);

		try {
			List<BRF40_Entity2> data = brf40bRepository.getasseteconomicvalues(year);
			if (data == null || data.isEmpty()) {
				logger.warn("No economic sector data found for year: {}", year);
				return new ArrayList<>();
			}
			logger.info("Fetched {} economic sector exposure records for year: {}", data.size(), year);
			return data;
		} catch (Exception e) {
			logger.error("Error occurred while fetching economic sector exposure data for year: {}", year, e);
			return new ArrayList<>();
		}
	}

	@RequestMapping(value = "Interbank_placement", method = { RequestMethod.GET, RequestMethod.POST })
	public String Interbank_placement(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String reportDate, Model md, HttpServletRequest req) {
		String domIds = ((String) req.getSession().getAttribute("DOMAINID")).trim();
		String userid = (String) req.getSession().getAttribute("USERID");
		String BRANCHCODE = (String) req.getSession().getAttribute("BRANCHCODE");
		String BRANCHNAME = (String) req.getSession().getAttribute("BRANCHNAME");
		String ROLEID = (String) req.getSession().getAttribute("ROLEID");

		md.addAttribute("dom_ids", domIds); // To highlight the menu
		logger.info("Accessing Interbank_placement with formmode: {}", formmode);

		try {
			if (formmode == null || "list".equalsIgnoreCase(formmode)) {
				md.addAttribute("menu", "List Of Treasury Placements");
				md.addAttribute("formmode", "list");
				List<?> placements = counter_servicess.getTreasury_placement();
				md.addAttribute("listss", placements);
				logger.info("Fetched {} treasury placements.", placements.size());
			} else if ("treasury_list".equalsIgnoreCase(formmode)) {
				md.addAttribute("formmode", formmode);
				System.out.println("Reportdate is" + reportDate);
				md.addAttribute("reportdate", reportDate);
				md.addAttribute("menu", "List Of Treasury Limits");
				List<?> limits = counter_servicess.getTreasury_limit(reportDate);
				md.addAttribute("listall", limits);
				logger.info("Fetched {} treasury limits.", limits.size());
			} else if ("add".equalsIgnoreCase(formmode)) {
				logger.info("Opening file upload mode for Treasury Placement. User: '{}', Branch: '{}'", userid,
						BRANCHNAME);
				md.addAttribute("menu", "List Of Treasury Placement"); // Default menu label
				md.addAttribute("menu", "Upload File Of Treasury Placement");
				md.addAttribute("formmode", "add");
				md.addAttribute("userid", userid);

				if ("ADM".equalsIgnoreCase(ROLEID)) {
					List<String> codes = UserProfileReps.getallcodes();
					logger.info("Counter party bank code Size: '{}', Role id is :'{}'", codes.size(), ROLEID);
					md.addAttribute("codes", codes);
				}
				// Add for all roles
				md.addAttribute("BRANCHCODE", BRANCHCODE);
				md.addAttribute("BRANCHNAME", BRANCHNAME);
				md.addAttribute("ROLEID", ROLEID);
			}
		} catch (Exception e) {
			logger.error("Error in Interbank_placement controller for formmode: {}", formmode, e);
		}

		return "MIS/Interbank_Placements.html";
	}

	@RequestMapping(value = "swap_settlement", method = { RequestMethod.GET, RequestMethod.POST })
	public String swap_settlement(@RequestParam(required = false) String formmode, Model md, HttpServletRequest req) {
		logger.info("Entered swap_settlement controller with formmode: {}", formmode);

		String userid = (String) req.getSession().getAttribute("USERID");
		String BRANCHCODE = (String) req.getSession().getAttribute("BRANCHCODE");
		String BRANCHNAME = (String) req.getSession().getAttribute("BRANCHNAME");
		String ROLEID = (String) req.getSession().getAttribute("ROLEID");

		try {
			md.addAttribute("menu", "List Of Settlement"); // Default menu label
			String domIds = ((String) req.getSession().getAttribute("DOMAINID")).trim();
			md.addAttribute("dom_ids", domIds); // To highlight the menu
			logger.debug("DOMAINID from session: {}", domIds);

			if (formmode == null || "list".equalsIgnoreCase(formmode)) {
				md.addAttribute("formmode", "list");
				List<Map<String, Object>> swapList = counter_servicess.getSwap();
				md.addAttribute("listall", swapList);
				logger.info("Loaded {} swap settlement records", swapList.size());
			} else if ("add".equalsIgnoreCase(formmode)) {
				logger.info("Opening file upload mode for Swap Settlement. User: '{}', Branch: '{}'", userid,
						BRANCHNAME);
				md.addAttribute("menu", "List Of Swap Settlement"); // Default menu label
				md.addAttribute("menu", "Upload File Of Swap Settlement");
				md.addAttribute("formmode", "add");
				md.addAttribute("userid", userid);
				if ("ADM".equalsIgnoreCase(ROLEID)) {
					List<String> codes = UserProfileReps.getallcodes();
					logger.info("Counter party bank code Size: '{}', Role id is :'{}'", codes.size(), ROLEID);
					md.addAttribute("codes", codes);
				}
				// Add for all roles
				md.addAttribute("BRANCHCODE", BRANCHCODE);
				md.addAttribute("BRANCHNAME", BRANCHNAME);
				md.addAttribute("ROLEID", ROLEID);
			}

		} catch (Exception e) {
			logger.error("Error occurred in swap_settlement method", e);
		}

		return "MIS/Swap_Settlement.html";
	}

	@GetMapping("/getExplosure_datas")
	@ResponseBody
	public List<Map<String, Object>> getExplosureDatas(
			@RequestParam("reportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate,
			@RequestParam("selected_branch") String selected_branch) {

		logger.info("Received request to getExplosureDatas with reportDate: {} and selected_branch: {}", reportDate,
				selected_branch);

		Date reportDateConverted = java.sql.Date.valueOf(reportDate);

		List<Map<String, Object>> response = new ArrayList<>();
		try {
			response = counter_servicess.getExploreData(reportDateConverted, selected_branch);
			logger.info("Fetched {} records for reportDate {} and branch {}", response.size(), reportDate,
					selected_branch);
		} catch (Exception e) {
			logger.error("Error fetching exposure data for reportDate {} and branch {}", reportDate, selected_branch,
					e);
		}

		return response;
	}

	@GetMapping("/getExploreDataBranch")
	@ResponseBody
	public List<Map<String, Object>> getExploreDataBranch(@RequestParam("selected_branch") String selected_branch) {

		Logger logger = LoggerFactory.getLogger(this.getClass());

		logger.info("Received request for branch: {}", selected_branch);

		List<Map<String, Object>> result = new ArrayList<>();
		try {
			result = counter_servicess.getExploreDataBranch(selected_branch);
			logger.info("Fetched {} records for branch: {}", result.size(), selected_branch);
		} catch (Exception e) {
			logger.error("Error while fetching data for branch: {}", selected_branch, e);
		}

		return result;
	}

	@GetMapping("/getTre_limit_Datas")
	@ResponseBody
	public List<Map<String, Object>> getTre_limit_Datas(
			@RequestParam("reportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate) {

		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("Received request to fetch Treasury Limit Data for report date: {}", reportDate);

		Date reportDateConverted = java.sql.Date.valueOf(reportDate);

		List<Map<String, Object>> result = new ArrayList<>();
		try {
			result = counter_servicess.getTre_limit_Datas(reportDateConverted);
			logger.info("Fetched {} treasury limit records for report date {}", result.size(), reportDate);
		} catch (Exception e) {
			logger.error("Error fetching treasury limit data for report date: {}", reportDate, e);
		}

		return result;
	}

	@GetMapping("/getTre_place_Datas")
	@ResponseBody
	public List<Map<String, Object>> getTre_place_Datas(
			@RequestParam("reportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate) {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("Received request to fetch Placement Limit Data for report date: {}", reportDate);

		Date reportDateConverted = java.sql.Date.valueOf(reportDate);

		List<Map<String, Object>> result = new ArrayList<>();
		try {
			result = counter_servicess.getTre_place_Datas(reportDateConverted);
			logger.info("Fetched {} Placement limit records for report date {}", result.size(), reportDate);
		} catch (Exception e) {
			logger.error("Error fetching Placement limit data for report date: {}", reportDate, e);
		}
		return result;
	}

	@GetMapping("/getSwap_Datas")
	@ResponseBody
	public List<Map<String, Object>> getSwap_Datas(
			@RequestParam("reportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate) {
		logger.info("Received request to fetch Swap settlement Data for report date: {}", reportDate);

		Date reportDateConverted = java.sql.Date.valueOf(reportDate);

		List<Map<String, Object>> result = new ArrayList<>();
		try {
			result = counter_servicess.getSwap_Datas(reportDateConverted);
			logger.info("Fetched {}  Swap settlement records for report date {}", result.size(), reportDate);
		} catch (Exception e) {
			logger.error("Error fetching  Swap settlement data for report date: {}", reportDate, e);
		}
		return result;

	}

	@RequestMapping(value = "search_date", method = RequestMethod.GET)
	@ResponseBody
	public int search_date(
			@RequestParam("reportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate,
			@RequestParam("iBranchCode") String iBranchCode, @RequestParam(required = false) String mode) {

		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("Searching report data for date: {} and branch code: {}", reportDate, iBranchCode);

		int size = 0;
		if (mode.equals("exposure")) {
			try {
				List<ASL_Report_Entity> list = ASL_Report_Reps.getByReportDateAndBR(java.sql.Date.valueOf(reportDate),
						iBranchCode);
				size = list.size();
				logger.info("Number of records found is Exposure Data: {}", size);
			} catch (Exception e) {
				logger.error("Error occurred while searching Exposure Data for date: {} and branch code: {}",
						reportDate, iBranchCode, e);
			}
		} else if (mode.equals("placement")) {
			try {
				List<MIS_TREASURY_PLACEMENT_ENTITY> list = TreasuryPlacementReps
						.getByReportDateAndBR(java.sql.Date.valueOf(reportDate), iBranchCode);
				size = list.size();
				logger.info("Number of records found for placement: {}", size);
			} catch (Exception e) {
				logger.error("Error occurred while searching placement data for date: {} and branch code: {}",
						reportDate, iBranchCode, e);
			}
		} else if (mode.equals("settlement")) {
			try {
				List<MIS_SETTLEMENT_ENTITY> list = MIS_SETTLEMENT_ENTITY_REPs
						.getByReportDateAndBR(java.sql.Date.valueOf(reportDate), iBranchCode);
				size = list.size();
				logger.info("Number of records found for settlement: {}", size);
			} catch (Exception e) {
				logger.error("Error occurred while searching settlement data for date: {} and branch code: {}",
						reportDate, iBranchCode, e);
			}
		}
		return size; // returns 0 if no report, >0 if exists
	}

	@RequestMapping(value = "Replace_data", method = { RequestMethod.POST })
	@ResponseBody
	public String Replace_data(@RequestParam("iBranchCode") String iBranchCode,
			@RequestParam("iBranchName") String iBranchName,
			@RequestParam("reportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate,
			@RequestParam("file") MultipartFile file, @RequestParam("uploadedBy") String uploadedBy,
			HttpServletRequest request, @RequestParam(required = false) String mode) {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		String userid = (String) request.getSession().getAttribute("USERID");

		logger.info(
				"Replace_data called - BranchCode: {}, BranchName: {}, ReportDate: {}, File: {}, UploadedBy: {}, UserID: {}",
				iBranchCode, iBranchName, reportDate, file.getOriginalFilename(), uploadedBy, userid);
		String result;
		try {
			result = ASL_Excel_Servicess.Replace_data(iBranchCode, iBranchName, reportDate, uploadedBy, file, userid,
					mode);
			logger.info("Replace_data processed successfully for BranchCode: {}", iBranchCode);
		} catch (Exception e) {
			logger.error("Error during Replace_data for BranchCode: {}", iBranchCode, e);
			result = "Failed to process replacement data.";
		}

		return result;
	}

	@RequestMapping(value = "getbranch", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getbranch(@RequestParam(required = false) String selected_branch) {
		return counter_servicess.getbranch(selected_branch);
	}

	@RequestMapping(value = "/downloadTradeleveldataderivativeExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadTradeleveldataderivativeExcel() {
	    logger.info("Controller: Received request for Trade Level Data Derivative Excel download.");

	    try {
	        byte[] excelData = tradeleveldataderivativeService.generateTradeleveldataderivativeExcel();


	        if (excelData.length == 0) {
	            logger.warn("Controller: No data found for Trade Level Data Derivative report. Responding with 204 No Content.");
	            return ResponseEntity.noContent().build();
	        }

	        ByteArrayResource resource = new ByteArrayResource(excelData);

	        String filename = "TradeLevelDataDerivative.xls";
	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

	        logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);

	        return ResponseEntity.ok()
	                .headers(headers)
	                .contentLength(excelData.length)
	                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	                .body(resource);

	    } catch (FileNotFoundException e) {
	        logger.error("Controller ERROR: Trade Level Data Derivative Excel template file not found.", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } catch (Exception e) {
	        logger.error("Controller ERROR: Unexpected error occurred during file generation.", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	@RequestMapping(value = "Trade_Level_Data_Derivatives", method = RequestMethod.GET)
	public String Tradeleveldataderivatives(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String SI_NO, Model md, HttpServletRequest req) {

		if ("edit".equalsIgnoreCase(formmode) && SI_NO != null && !SI_NO.isEmpty()) {
			RT_TradeLevelDataDerivatives data = tradeleveldataderivativesRepo.getParticularDataBySI_NO(SI_NO);
			md.addAttribute("tradeleveldataderivative", data);
			System.out.println("edit is formmode");
			md.addAttribute("formmode", "edit");

		} else if ("list".equalsIgnoreCase(formmode)) {
			md.addAttribute("branchList", tradeleveldataderivativesRepo.getlist());
			System.out.println("list is formmode");
			md.addAttribute("formmode", "list");

		} else {
			md.addAttribute("formmode", "add");
			md.addAttribute("formmode", "null");

			// You had md.addAttribute("formmode", "null"); — removed this line because it
			// would overwrite the previous one
		}

		List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
		md.addAttribute("bankList", bankList);

		return "RT/Trade_Level_Data_Derivatives";
	}

	@PostMapping("/updatetradeleveldataderivative")
	@ResponseBody
	public String updatetradeleveldataderivative(
			@ModelAttribute RT_TradeLevelDataDerivatives tradeleveldataderivative) {
		boolean updated = tradeleveldataderivativeService.updatetradeleveldataderivative(tradeleveldataderivative);

		if (updated) {
			return "Updated successfully";
		} else {
			return "Record not found for update";
		}

	}

	@Autowired
	RT_CCR_DATA_TEMPLATE_REPOSITORY ccr_data_template_repository;

	/*
	 * CCR_Data_Templates CREATED BY : SANJEEVI S
	 * 
	 */
	@PersistenceContext
	private EntityManager entityManager;

	@RequestMapping(value = "CCR_Data_Templates", method = RequestMethod.GET)
	public String CCR_Data_Templates(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String siNo, // changed from accountNo to slNo
			Model md, HttpServletRequest req) {

		if ("edit".equalsIgnoreCase(formmode) && siNo != null) {
			RT_CCR_DATA_TEMPLATE data = ccr_data_template_repository.editccr(siNo); // make sure entity class matches
			md.addAttribute("repoData", data);
			System.out.println("edit is formmode");
			md.addAttribute("formmode", "edit");
		} else if ("list".equalsIgnoreCase(formmode)) {

			List<RT_CCR_DATA_TEMPLATE> repoList = ccr_data_template_repository.getlist();
			System.out.println("testing count" + ccr_data_template_repository.getlist().size());
			;
			// System.out.println("the count" +CCrrepo.getlist().size());
			/*
			 * for (RT_CCR_Data_Template r : repoList) { System.out.println("Data: " +
			 * r.getSiNo()); // just to confirm data presence } md.addAttribute("repoList1",
			 * repoList);
			 */

			md.addAttribute("repoList1", repoList);
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

		return "RT/CCR_Data_Templates";
	}

	@PostMapping("/updateCCRData")
	@ResponseBody
	public String updateCCRData(@ModelAttribute("repoData") RT_CCR_DATA_TEMPLATE repoData,
			RedirectAttributes redirectAttributes) {

		boolean updated = rtCCRDataService.updateCCRData(repoData);

		/*
		 * if (updated) { redirectAttributes.addFlashAttribute("updateMsg",
		 * "Updated Successfully"); } else {
		 * redirectAttributes.addFlashAttribute("updateMsg",
		 * "Record not found for update"); }
		 * 
		 * return "redirect:/Treasury_Credit_Limit_Management?formmode=list";
		 */

		if (updated) {
			return "Updated successfully";
		} else {
			return "Record not found for update";
		}
	}
	
	@Autowired
	private RT_CCR_DATA_Service rtCCRDataService;

	@RequestMapping(value = "/downloadCCRDataExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadCCRDataExcel() {
		logger.info("Controller: Received request for CCR DATA Excel download.");

		try {
			byte[] excelData = rtCCRDataService.generateCCRDataExcel();

			if (excelData.length == 0) {
				logger.warn("Controller: Service returned no data for CCR DATA. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			HttpHeaders headers = new HttpHeaders();
			String filename = "CCR_DATA.xls";
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);

			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (FileNotFoundException e) {
			logger.error("Controller ERROR: The CCR_DATA.xls template file was not found.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			logger.error("Controller ERROR: A critical error occurred during CCR_DATA file generation.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@RequestMapping(value = "Foreign_Currency_Deposit", method = RequestMethod.GET)
	public String Foreigncurrencydeposit(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String SI_NO, Model md, HttpServletRequest req) {

		if ("edit".equalsIgnoreCase(formmode) && SI_NO != null && !SI_NO.isEmpty()) {
			RT_ForeignCurrencyDeposit data = foreigncurrencydepositRepo.getParticularDataBySI_NO(SI_NO);
			md.addAttribute("foreigncurrencydeposit", data);
			System.out.println("edit is formmode");
			md.addAttribute("formmode", "edit");

		} else if ("list".equalsIgnoreCase(formmode)) {
			md.addAttribute("branchList", foreigncurrencydepositRepo.getlist());
			System.out.println("list is formmode");
			md.addAttribute("formmode", "list");

		} else {
			md.addAttribute("formmode", "add");
			md.addAttribute("formmode", "null");

			// You had md.addAttribute("formmode", "null"); — removed this line because it
			// would overwrite the previous one
		}

		List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
		List<RT_CountryRiskDropdown> countryList = countryRepo.findAllByOrderByCountryOfRiskAsc();
		md.addAttribute("bankList", bankList);
		md.addAttribute("countryList", countryList);

		return "RT/Foreign_Currency_Deposit";
	}

	@PostMapping("/updateForeignCurrencyDeposit")
	@ResponseBody
	public String updateForeignCurrencyDeposit(@ModelAttribute RT_ForeignCurrencyDeposit fxriskData) {
		boolean updated = foreigncurrencydepositService.updateForeignCurrencyDeposit(fxriskData);

		if (updated) {
			return "Update successful";
		} else {
			return "Record not found for update";
		}
	}
	
	@RequestMapping(value = "Impact_Analysis", method = RequestMethod.GET)
	public String ImpactAnalysis(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String SI_NO, Model md, HttpServletRequest req) {

		if ("edit".equalsIgnoreCase(formmode) && SI_NO != null && !SI_NO.isEmpty()) {
			RT_ImpactAnalysis data = impactanalysisRepo.getParticularDataBySI_NO(SI_NO);
			md.addAttribute("impactanalysis", data);
			System.out.println("edit is formmode");
			md.addAttribute("formmode", "edit");

		} else if ("list".equalsIgnoreCase(formmode)) {
			md.addAttribute("branchList", impactanalysisRepo.getlist());
			System.out.println("list is formmode");
			md.addAttribute("formmode", "list");

		} else {
			md.addAttribute("formmode", "add");
			md.addAttribute("formmode", "null");

			// You had md.addAttribute("formmode", "null"); — removed this line because it
			// would overwrite the previous one
		}

		List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
		List<RT_CountryRiskDropdown> countryList = countryRepo.findAllByOrderByCountryOfRiskAsc();
		md.addAttribute("bankList", bankList);
		md.addAttribute("countryList", countryList);

		return "RT/ImpactAnalysis";
	}
	
	@PostMapping("/updateImpactAnalysis")
	@ResponseBody
	public String updateImpactAnalysis(@ModelAttribute RT_ImpactAnalysis impactanalysis) {
		boolean updated = impactanalysisService.updateImpactAnalysis(impactanalysis);

		if (updated) {
			return "Updated successful";
		} else {
			return "Record not found for update";
		}
	}	
	
	/*iquidity risk Dashboard template*/
	@RequestMapping(value = "Liquidity_Risk_Dashboard_Template", method = RequestMethod.GET)
	public String Liquidity_Risk_Dashboard_Template(@RequestParam(required = false) String siNo,
			@RequestParam(required = false) String formmode, Model model) {

		if ("edit".equalsIgnoreCase(formmode) && siNo != null) {
			model.addAttribute("formmode", "edit");
			model.addAttribute("InvestmentData", investmentSecuritiesDataTemplateRepo.findById(siNo)
					.orElse(new RT_Investment_Securities_Data_Template()));
			
		} else if ("list".equalsIgnoreCase(formmode)) {
			List<RT_Liquidity_Risk_Dashboard_Template> list = LiquidityRiskDashboardRepo.getAlldetails();
         
			System.out.println("The Liquidity Risk Dashboard"  +LiquidityRiskDashboardRepo.getAlldetails().size());
			model.addAttribute("formmode", "list");
			model.addAttribute("ISList", list); // Used in HTML table
		} /*
			 * else { model.addAttribute("formmode", "add");
			 * model.addAttribute("securityData", new
			 * RT_Investment_Securities_Data_Template()); }
			 */else {
			model.addAttribute("formmode", "add");
		}

		return "RT/Liquidity_Risk_Dashboard_Template";
	}
	
	/*--IRRBB Data template--*/
	@RequestMapping(value = "IRRBB_data_Template", method = RequestMethod.GET)
	public String IRRBB_data_Template(@RequestParam(required = false) String siNo,
			@RequestParam(required = false) String formmode, Model model) {

		if ("edit".equalsIgnoreCase(formmode) && siNo != null) {
			model.addAttribute("formmode", "edit");
			model.addAttribute("InvestmentData", investmentSecuritiesDataTemplateRepo.findById(siNo)
					.orElse(new RT_Investment_Securities_Data_Template()));
			
		} else if ("list".equalsIgnoreCase(formmode)) {
			List<RT_IRRBB_Data_Template> list = IRRB_EVE_Repo.getAlldetails();
         
			System.out.println("IRRBB EVE "  +IRRB_EVE_Repo.getAlldetails().size());
			model.addAttribute("formmode", "list");
			model.addAttribute("ISList", list); // Used in HTML table
		} else if ("EAR".equalsIgnoreCase(formmode)) {
			System.out.println("THE EAR REPORT START");
			List<RT_IRRBB_Data_EAR> list = IRRBB_EAR_Repository.getAlldetails();
            System.out.println("IRRBB EAR "  +IRRBB_EAR_Repository.getAlldetails().size());
			model.addAttribute("formmode", "EAR");
			model.addAttribute("ISListEar", list); // Used in HTML table
			System.out.println("Formmode" +formmode);
		}
		else if ("DiscountRate".equalsIgnoreCase(formmode)) {
			System.out.println("---- Discount Rate---");
			List<RT_IRRBB_Data_Discount_Rates> list = IRRBB_Data_Template_DiscountRate_repo.getAlldetails();
            System.out.println("IRRBB EAR "  +IRRBB_Data_Template_DiscountRate_repo.getAlldetails().size());
			model.addAttribute("formmode", "DiscountRate");
			model.addAttribute("ISListDiscount", list); // Used in HTML table
			System.out.println("Formmode" +formmode);
		}
		
		/*
			 * else { model.addAttribute("formmode", "add");
			 * model.addAttribute("securityData", new
			 * RT_Investment_Securities_Data_Template()); }
			 */else {
			model.addAttribute("formmode", "add");
		}

		return "RT/IRRBB_data_Template";
	}
	
	/*--IRRBB Data template--*/
	@RequestMapping(value = "IRRBB_data_Template_EAR", method = RequestMethod.GET)
	public String IRRBB_data_Template_EAR(@RequestParam(required = false) String siNo,
			@RequestParam(required = false) String formmode, Model model) {

		if ("edit".equalsIgnoreCase(formmode) && siNo != null) {
			model.addAttribute("formmode", "edit");
			model.addAttribute("InvestmentData", investmentSecuritiesDataTemplateRepo.findById(siNo)
					.orElse(new RT_Investment_Securities_Data_Template()));
			
		} else if ("list".equalsIgnoreCase(formmode)) {
			List<RT_IRRBB_Data_Template> list = IRRB_EVE_Repo.getAlldetails();
         
			System.out.println("IRRBB EVE "  +IRRB_EVE_Repo.getAlldetails().size());
			model.addAttribute("formmode", "list");
			model.addAttribute("ISList", list); // Used in HTML table
		} else {
			model.addAttribute("formmode", "add");
		}

		return "RT/IRRBB_data_Template_EAR";
	}
	
	
	
	

	@RequestMapping(value = "/downloadForeigncurrencyExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadForeigncurrencyExcel() {
	    logger.info("Controller: Received request for Foreign Currency Deposit Excel download.");

	    try {
	        byte[] excelData = foreigncurrencydepositService.generateForeignCurrencyDepositExcel();

	        if (excelData.length == 0) {
	            logger.warn("Controller: No data found for Foreign Currency report. Responding with 204 No Content.");
	            return ResponseEntity.noContent().build();
	        }

	        ByteArrayResource resource = new ByteArrayResource(excelData);

	        String filename = "Foreigncurrencydeposit.xls";
	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

	        logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);

	        return ResponseEntity.ok()
	                .headers(headers)
	                .contentLength(excelData.length)
	                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	                .body(resource);

	    } catch (FileNotFoundException e) {
	        logger.error("Controller ERROR: Foreign Currency Excel template file not found.", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } catch (Exception e) {
	        logger.error("Controller ERROR: Unexpected error occurred during file generation.", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}


	
	
	
	@RequestMapping(value = "/downloadImpactanalysisExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadImpactanalysisExcel() {
	    logger.info("Controller: Received request for Impact Analysis Excel download.");

	    try {
	        byte[] excelData = impactanalysisService.generateImpactAnalysisExcel();

	        if (excelData.length == 0) {
	            logger.warn("Controller: No data found for Impact Analysis report. Responding with 204 No Content.");
	            return ResponseEntity.noContent().build();
	        }

	        ByteArrayResource resource = new ByteArrayResource(excelData);

	        String filename = "Impactanalysis.xls";
	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

	        logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);

	        return ResponseEntity.ok()
	                .headers(headers)
	                .contentLength(excelData.length)
	                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	                .body(resource);

	    } catch (FileNotFoundException e) {
	        logger.error("Controller ERROR: Impact Analysis Excel template file not found.", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } catch (Exception e) {
	        logger.error("Controller ERROR: Unexpected error occurred during file generation.", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	
	@RequestMapping(value = "Trade_Level_Data_Derivatives_Simplified", method = RequestMethod.GET)
	public String TradeleveldataderivativesSimplified(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String SI_NO, Model md, HttpServletRequest req) {

		if ("edit".equalsIgnoreCase(formmode) && SI_NO != null && !SI_NO.isEmpty()) {
			RT_TradeLevelDataDerivativesSimplified data = tradeleveldataderivativessimplifiedRepo.getParticularDataBySI_NO(SI_NO);
			md.addAttribute("tradeleveldataderivative", data);
			System.out.println("edit is formmode");
			md.addAttribute("formmode", "edit");

		} else if ("list".equalsIgnoreCase(formmode)) {
			md.addAttribute("branchList", tradeleveldataderivativessimplifiedRepo.getlist());
			System.out.println("list is formmode");
			md.addAttribute("formmode", "list");

		} else {
			md.addAttribute("formmode", "add");
			md.addAttribute("formmode", "null");

			// You had md.addAttribute("formmode", "null"); — removed this line because it
			// would overwrite the previous one
		}

		List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
		md.addAttribute("bankList", bankList);

		return "RT/Trade_Level_Data_Derivatives_Simplified";
	}

	@PostMapping("/updatetradeleveldataderivativesimplified")
	@ResponseBody
	public String updatetradeleveldataderivativesimplified(
			@ModelAttribute RT_TradeLevelDataDerivatives tradeleveldataderivative) {
		boolean updated = tradeleveldataderivativesimplifiedService.updatetradeleveldataderivative(tradeleveldataderivative);

		if (updated) {
			return "Updated successfully";
		} else {
			return "Record not found for update";
		}

	}

	
	@RequestMapping(value = "/downloadTradeleveldataderivativesimplifiedExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadTradeleveldataderivativesimplifiedExcel() {
	    logger.info("Controller: Received request for Trade Level Data Derivative Simplified Excel download.");

	    try {
	        byte[] excelData = tradeleveldataderivativesimplifiedService.generateTradeleveldataderivativesimplifiedExcel();


	        if (excelData.length == 0) {
	            logger.warn("Controller: No data found for Trade Level Data Derivative Simplified report. Responding with 204 No Content.");
	            return ResponseEntity.noContent().build();
	        }

	        ByteArrayResource resource = new ByteArrayResource(excelData);

	        String filename = "TradeLevelDataDerivativeSimplified.xls";
	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

	        logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);

	        return ResponseEntity.ok()
	                .headers(headers)
	                .contentLength(excelData.length)
	                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	                .body(resource);

	    } catch (FileNotFoundException e) {
	        logger.error("Controller ERROR: Trade Level Data Derivative Simplified Excel template file not found.", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } catch (Exception e) {
	        logger.error("Controller ERROR: Unexpected error occurred during file generation.", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	
	
	
	@RequestMapping(value = "/downloadInvestmentriskdatadashboardExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadInvestmentriskdatadashboardExcel() {
	    logger.info("Controller: Received request for Investment Risk Data Dashboard Excel download.");

	    try {
	        byte[] excelData = investmentriskdatadictionaryService.generateInvestmentriskdataExcel();


	        if (excelData.length == 0) {
	            logger.warn("Controller: No data found for Investment Risk Data Dashboard report. Responding with 204 No Content.");
	            return ResponseEntity.noContent().build();
	        }

	        ByteArrayResource resource = new ByteArrayResource(excelData);

	        String filename = "InvestmentRiskDataDashboard.xls";
	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

	        logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);

	        return ResponseEntity.ok()
	                .headers(headers)
	                .contentLength(excelData.length)
	                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	                .body(resource);

	    } catch (FileNotFoundException e) {
	        logger.error("Controller ERROR: Investment Risk Data Dashboard Excel template file not found.", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } catch (Exception e) {
	        logger.error("Controller ERROR: Unexpected error occurred during file generation.", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	
}
