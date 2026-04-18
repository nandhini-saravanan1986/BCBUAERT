package com.bornfire.xbrl.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bornfire.xbrl.services.ASL_Excel_Services;
import com.bornfire.xbrl.services.AccessAndRolesServices;
import com.bornfire.xbrl.services.AuditService;
import com.bornfire.xbrl.services.Excel_Services;
import com.bornfire.xbrl.services.LoginServices;
import com.bornfire.xbrl.services.RT_ACPR_SERVICE;
import com.bornfire.xbrl.services.RT_CCR_DATA_Service;
import com.bornfire.xbrl.services.RT_DataControlService;
import com.bornfire.xbrl.services.RT_FX_Position_Service;
import com.bornfire.xbrl.services.RT_ForeignCurrencyDepositService;
import com.bornfire.xbrl.services.RT_FxriskdataService;
import com.bornfire.xbrl.services.RT_IRSService;
import com.bornfire.xbrl.services.RT_ImpactAnalysisService;
import com.bornfire.xbrl.services.RT_InvestmentRiskDataDashboard_Service;
import com.bornfire.xbrl.services.RT_InvestmentSecurity_Service;
import com.bornfire.xbrl.services.RT_Irrbb_Discount_Rates_Service;
import com.bornfire.xbrl.services.RT_Irrbb_Ear_Service;
import com.bornfire.xbrl.services.RT_Irrbb_Eve_Service;
import com.bornfire.xbrl.services.RT_Liquidity_Risk_Data_Service;
import com.bornfire.xbrl.services.RT_LiquidityriskdashboardService;
import com.bornfire.xbrl.services.RT_MC_TABLE_ALL_Service;
import com.bornfire.xbrl.services.RT_MID_FX_DEAL_SERVICE;
import com.bornfire.xbrl.services.RT_MmdataService;
import com.bornfire.xbrl.services.RT_NostroAccBalDataService;
import com.bornfire.xbrl.services.RT_RepoService;
import com.bornfire.xbrl.services.RT_SLSServices;
import com.bornfire.xbrl.services.RT_SLS_BEHAVIOURAL_PER_SERVICES;
import com.bornfire.xbrl.services.RT_TradeLevelDerivativesService;
import com.bornfire.xbrl.services.RT_TradeLevelDerivativesSimplifiedService;
import com.bornfire.xbrl.services.RT_TradeMarketRiskService;
import com.bornfire.xbrl.services.RT_TreasuryCredit_Service;
import com.bornfire.xbrl.services.RwaDataUploadService;
import com.bornfire.xbrl.services.counter_services;
import com.bornfire.xbrl.services.RtInvestmentDealDataDump_Service;
import com.bornfire.xbrl.services.UploadMonitorService;
import com.bornfire.xbrl.services.Bloomberg_services;
import com.bornfire.xbrl.services.ECLDataUploadService;
import com.bornfire.xbrl.services.ProvisioningFileUploadService;
import com.bornfire.xbrl.services.VarPortfolioUploadService;
import com.bornfire.xbrl.services.SMAFileUploadService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bornfire.xbrl.entities.*;

@Controller
@ConfigurationProperties("default")
public class XBRLNavigationController {

	private static final Logger logger = LoggerFactory.getLogger(XBRLNavigationController.class);
	/*
	 * @PersistenceContext private EntityManager entityManager;
	 * 
	 */
	@Autowired
	Bloomberg_services bloombergService;
	
	@Autowired
	VarPortfolioUploadService varportfoliouploadservice;
	
	@Autowired
	ECLDataUploadService ecldatauploadservice;
	
	@Autowired
	RtVarReportLimits_Rep rtvarreportlimits_rep;
	@Autowired
	KriMasterTable_Rep krimastertablerep;
	
	@Autowired
	RtInvestmentRiskDataDetail_Rep rtinvestmentriskdatadetailrep;
	@Autowired
	RtInvestmentDealDataDump_Service rtinvestmentdealdatadump_service;
	
	@Autowired
	RtBloombergData_Rep rtBloombergdata_rep;

	@Autowired
	RT_IRRBB_Data_EVE_Template_Detail_Rep rt_irrbb_data_eve_template_detail_rep;
	@Autowired
	RT_SLSServices RT_SLSServices;
	
	@Autowired
	Limit_Request_Rep limit_request_rep;
	
	@Autowired
	RT_RWA_RATING_GUIDELINES_Rep rt_rwa_rating_guidelines_rep;

	@Autowired
	RT_IRSService rtIrsService;

	@Autowired
	RT_SLS_Detail_Repository rt_sls_detail_repository;

	@Autowired
	RT_SLS_Repository rt_sls_repository;
	@Autowired
	AuditService auditService;
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
	Mis_treasury_placement_repo Mis_treasury_placement_repo;
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
	RT_Liquidity_Risk_Data_Service liquidityRiskDataService;

	@Autowired
	RT_Liquidity_Risk_Data_Template_Repository LiquidityRiskDataRepository;

	@Autowired
	RT_ImpactAnalysisRepository impactanalysisRepo;

	@Autowired
	RT_Liquidity_Risk_Dashboard_Template_repository LiquidityRiskDashboardRepo;

	@Autowired

	RT_TradeLevelDataDerivativesSimplifiedRepository tradeleveldataderivativessimplifiedRepo;

	@Autowired
	RT_TradeLevelDerivativesSimplifiedService tradeleveldataderivativesimplifiedService;

	@Autowired
	RT_InvestmentRiskDataDashboard_Service investmentriskdatadictionaryService;

	@Autowired
	RT_IRRBB_Data_EVE_Template_Repository IRRB_EVE_Repo;
	@Autowired
	RT_IRRBB_Data_EAR_Repository IRRBB_EAR_Repository;

	@Autowired
	RT_IRRBB_Data_Discount_Rates_Repository IRRBB_Data_Template_DiscountRate_repo;

	@Autowired
	RT_LiquidityriskdashboardService liquidityriskdashboardService;

	@Autowired
	RT_Irrbb_Eve_Service irrbbeveService;

	@Autowired
	RT_Irrbb_Ear_Service irrbbearService;

	@Autowired
	RT_Irrbb_Discount_Rates_Service discountratesService;

	@Autowired
	RT_Matrix_monitoring_rep RT_Matrix_monitoring_rep;

	@Autowired
	RT_RWA_Fund_base_data_rep RT_RWA_Fund_base_data_rep;

	@Autowired
	Groupexp_cust_maintain_rep Groupexp_cust_maintain_rep;

	@Autowired
	Capitaladequacyratio_rep Capitaladequacyratio_rep;

	@Autowired
	RT_IRS_REPOSITORY RT_irs_repository;

	@Autowired
	RT_IRS2_REPOSITORY RT_IRS2_REPOSITORY;

	@Autowired
	RT_IRS_DETAIL_REPO RT_IRS_DETAIL_REPO;

	@Autowired
	MIS_COUNTER_PARTY_LIMIT_DETAILS_REPO misCounterPartyLimitDetailsRepo;
	
	@Autowired
	RT_MC_TABLE1_REPO RT_MC_TABLE1_REPO;
	@Autowired
	RT_MC_TABLE2_1_REPO RT_MC_TABLE2_1_REPO;
	@Autowired
	RT_MC_TABLE2_2_REPO RT_MC_TABLE2_2_REPO;
	@Autowired
	RT_MC_TABLE3_REPO RT_MC_TABLE3_REPO;
	@Autowired
	RT_MC_TABLE4_1_REPO RT_MC_TABLE4_1_REPO;
	@Autowired
	RT_MC_TABLE4_2_REPO RT_MC_TABLE4_2_REPO;	
	@Autowired
	RT_MC_TABLE5_REPO RT_MC_TABLE5_REPO;
	@Autowired
	RT_MC_TABLE6_REPO RT_MC_TABLE6_REPO;	
	@Autowired
	RT_MC_TABLE7_1_REPO RT_MC_TABLE7_1_REPO;
	@Autowired
	RT_MC_TABLE7_2_REPO RT_MC_TABLE7_2_REPO;
	@Autowired
	RT_MC_TABLE8_REPO RT_MC_TABLE8_REPO;
	@Autowired
	RT_MC_TABLE9_REPO RT_MC_TABLE9_REPO;
	@Autowired
	RT_MC_TABLE_ALL_Service rT_MC_TABLE_Service;
	
	@Autowired
	SMAFileUploadService SMAFileUploadService;

	@Autowired
	ProvisioningFileUploadService ProvisioningFileUploadService;
	
	@Autowired
	private UploadMonitorService uploadMonitorService;
	
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
		String ROLEID = (String) req.getSession().getAttribute("ROLEID");
		String BRANCH_NAME = (String) req.getSession().getAttribute("BRANCHNAME");

		LocalDate today = LocalDate.now(); // Get today's date
		Date Todaydate = java.sql.Date.valueOf(today.minusDays(0));

		md.addAttribute("changepassword", loginServices.checkPasswordChangeReq(userid));
		md.addAttribute("checkpassExpiry", loginServices.checkpassexpirty(userid));
		md.addAttribute("checkAcctExpiry", loginServices.checkAcctexpirty(userid));
		int completed = 0;
		int uncompleted = 0;

		// Retrive Current date Matrix data
		List<RT_Matrix_monitoring_entity> Matrixdata = RT_Matrix_monitoring_rep.Getcurrentdatematrixcal();

		md.addAttribute("completed", completed);
		md.addAttribute("uncompleted", uncompleted);
		md.addAttribute("Matrixlistdata", Matrixdata);
		md.addAttribute("menu", "Dashboard");
		md.addAttribute("CurrentYear_Tier_1_capital", Capitaladequacyratio_rep.GetCurrentyear_tier_1_capital());
		return "XBRLDashboard";
	}

	@RequestMapping(value = "AccessandRoles", method = { RequestMethod.GET, RequestMethod.POST })
	public String IPSAccessandRoles(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req) {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		// System.out.println("role id is : " + roleId);
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (formmode == null || formmode.equals("list")) {
			md.addAttribute("menu", "ACCESS AND ROLES");
			md.addAttribute("menuname", "ACCESS AND ROLES");
			md.addAttribute("formmode", "list");
			md.addAttribute("AccessandRoles", accessandrolesrepository.rulelist());

		} else if (formmode.equals("add")) {

		} /*
			 * else if (formmode.equals("add")) { md.addAttribute("menuname",
			 * "ACCESS AND ROLES - ADD"); md.addAttribute("formmode", "add"); }
			 */
		else if (formmode.equals("add")) {

		    md.addAttribute("menuname", "ACCESS AND ROLES - ADD");
		    md.addAttribute("formmode", "add");

		    // Create a new empty AccessAndRoles object
		    AccessAndRoles newRole = new AccessAndRoles();
		    md.addAttribute("IPSAccessRole", newRole);
		}else if (formmode.equals("edit")) {
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
	
	
	@RequestMapping(value = "BloombergData", method = { RequestMethod.GET, RequestMethod.POST })
	public String BloombergData(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) String isin,
			 Model md, HttpServletRequest req) {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		// System.out.println("role id is : " + roleId);
		md.addAttribute("roleId", roleId);

		if (formmode == null || formmode.equals("list")) {
			md.addAttribute("menu", "Bloomberg Data");
			md.addAttribute("menuname", "Bloomberg Data");
			md.addAttribute("formmode", "list");
			md.addAttribute("BloombergDatalist", rtBloombergdata_rep.getrtbloombergdatalist());
			

		} else if (formmode.equals("add")) {
			md.addAttribute("menu", "Bloomberg Data");
			md.addAttribute("menuname", "Bloomberg Data");
			md.addAttribute("formmode", "add");
			md.addAttribute("BloombergData", new RtBloombergData());
		}
		
		else if (formmode.equals("edit")) {
			md.addAttribute("menu", "Bloomberg Data");
			md.addAttribute("menuname", "Bloomberg Data");
			md.addAttribute("formmode", "edit");
			md.addAttribute("BloombergData",rtBloombergdata_rep.getrtbloombergdatabyisin(isin));
		}
		

		return "BloombergData";
	}
	
	@PostMapping("/saveBloomberg")
	@ResponseBody
	public String saveBloomberg(
	        @ModelAttribute RtBloombergData data,
	        @RequestParam("formmode") String formmode,
	        HttpServletRequest rq) {

	    String userid = (String) rq.getSession().getAttribute("USERID");

	    if (userid == null) {
	        return "Session Expired. Please login again.";
	    }

	    String msg = bloombergService.addData(data, formmode, userid);

	    System.out.println("msg=" + msg);

	    return msg;
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

		String msg = AccessRoleService.addPARAMETER(alertparam, formmode, adminValue, RT_ReportsValue, aslValue,
				aslUploadValue, auditUsValue, finalString, userid);

		return msg;
	}

	@PostMapping("verifyRole")
	@ResponseBody
	public String verifyRole(@RequestParam("roleId") String roleId, HttpServletRequest rq) {
		String userId = (String) rq.getSession().getAttribute("USERID");

		Optional<AccessAndRoles> optionalRole = accessandrolesrepository.findById(roleId);

		if (!optionalRole.isPresent()) {
			return "Role not found. Cannot verify.";
		}

		AccessAndRoles role = optionalRole.get();

		if ("Y".equalsIgnoreCase(role.getEntityFlg())) {
			return "Role already verified.";
		}

		role.setEntityFlg("Y");
		role.setEntityFlg("N");
		role.setAuthUser(userId);
		role.setAuthTime(new Date());

		accessandrolesrepository.save(role);

		return "Role verified successfully.";
	}

	@GetMapping("/checkRoleExists")
	@ResponseBody
	public String checkRoleExists(@RequestParam("roleId") String roleId) {
		boolean exists = accessandrolesrepository.findById(roleId).isPresent();
		return exists ? "exists" : "not_exists";
	}

	@RequestMapping(value = "UserProfile", method = { RequestMethod.GET, RequestMethod.POST })
	public String userprofile(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, Model md, HttpServletRequest req) {

		String loginuserid = (String) req.getSession().getAttribute("USERID");
		String WORKCLASSAC = (String) req.getSession().getAttribute("WORKCLASS");
		String ROLEIDAC = (String) req.getSession().getAttribute("ROLEID");

		// Always provide these for dropdowns/logic
		md.addAttribute("RuleIDType", accessandrolesrepository.roleidtype());
		md.addAttribute("WORKCLASSAC", WORKCLASSAC);
		md.addAttribute("ROLEIDAC", ROLEIDAC);
		md.addAttribute("loginuserid", loginuserid);

		if (formmode == null || formmode.equals("list")) {
			md.addAttribute("formmode", "list");
			md.addAttribute("userProfiles", loginServices.getUsersList());
		} else {
			md.addAttribute("formmode", formmode);
			UserProfile user;
			if (formmode.equals("add")) {
				user = new UserProfile();
				user.setLogin_low("00:00");
				user.setLogin_high("23:59");
			} else {
				user = loginServices.getUser(userid);
			}
			md.addAttribute("userProfile", user);
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
		// System.out.println("role id for fetching is : " + roleId);
		AccessAndRoles access = accessandrolesrepository.findById(roleId).orElse(null);

		/*
		 * if (access != null) { System.out.println("roleDesc = " +
		 * access.getRoleDesc()); // <== ADD THIS System.out.println("workClass = " +
		 * access.getWorkClass()); // <== ADD THIS System.out.println("permission = " +
		 * access.getPermissions()); // <== ADD THIS System.out.println("module = " +
		 * access.getDomainId()); // <== ADD THIS }
		 */

		return access;
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
			@ModelAttribute RT_DataControl dto, HttpServletRequest req) {
		System.out.println("Controller: createOrUpdateNostro() called");
		System.out.println("report name is: " + report_name);

		String userid = (String) req.getSession().getAttribute("USERID");
		//auditService.createBusinessAudit(userid, "ADD", "DATACONTROL", null, "BCBUAE_DATACONTROL");  -- call in service

		return RT_DataControlService.createOrUpdate(dto, formmode, report_name,userid);
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
			Timestamp lastdatetimestamp = nostroAccBalRepo.findLastReportDate();
			Timestamp secondlastdatetimestamp = nostroAccBalRepo.findSecondLastReportDate();			
			LocalDate lastDate=lastdatetimestamp.toLocalDateTime().toLocalDate();		
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String lastDateString = (lastdatetimestamp == null) ? null
					: lastdatetimestamp.toLocalDateTime().format(formatter);
			String secondLastDateString =(secondlastdatetimestamp == null) ? null
					:  secondlastdatetimestamp.toLocalDateTime().format(formatter);
			RT_DataControl data= RT_DatacontrolRepository.getdata(lastDateString,"CBUAE_Nostro Account_Balance_Data_Template");
			RT_DataControl secondlastdata= RT_DatacontrolRepository.getdata(secondLastDateString,"CBUAE_Nostro Account_Balance_Data_Template");
			if (data != null && !data.equals(null)) {
				md.addAttribute("data", data);
				md.addAttribute("formmode", "exist");
			}
			else if(secondlastdata != null && !secondlastdata.equals(null)){
				md.addAttribute("data", secondlastdata);
				md.addAttribute("formmode", "exist");
			}else {
				md.addAttribute("formmode", "add");
				md.addAttribute("formmode", "null");
			}
			md.addAttribute("lastDate", lastDate);
			md.addAttribute("bankname", "Bank of Baroda");
			
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
			Timestamp lastdatetimestamp = repoRepo.findLastReportDate();
			Timestamp secondlastdatetimestamp = repoRepo.findSecondLastReportDate();			
			LocalDate lastDate=lastdatetimestamp.toLocalDateTime().toLocalDate();		
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String lastDateString = (lastdatetimestamp == null) ? null
					: lastdatetimestamp.toLocalDateTime().format(formatter);
			String secondLastDateString =(secondlastdatetimestamp == null) ? null
					:  secondlastdatetimestamp.toLocalDateTime().format(formatter);
			RT_DataControl data= RT_DatacontrolRepository.getdata(lastDateString,"CBUAE_Repo_Data_Template");
			RT_DataControl secondlastdata= RT_DatacontrolRepository.getdata(secondLastDateString,"CBUAE_Repo_Data_Template");
			if (data != null && !data.equals(null)) {
				md.addAttribute("data", data);
				md.addAttribute("formmode", "exist");
			}
			else if(secondlastdata != null && !secondlastdata.equals(null)){
				md.addAttribute("data", secondlastdata);
				md.addAttribute("formmode", "exist");
			}else {
				md.addAttribute("formmode", "add");
				md.addAttribute("formmode", "null");
			}
			md.addAttribute("lastDate", lastDate);
			md.addAttribute("bankname", "Bank of Baroda");
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
	public ResponseEntity<byte[]> downloadRepoExcel(HttpServletRequest req) throws IOException {
		System.out.println("Entered controller downloadRepoExcel");

		File excelFile = repoService.generateRepoExcel();
		byte[] excelData = java.nio.file.Files.readAllBytes(excelFile.toPath());

		HttpHeaders headersResponse = new HttpHeaders();
		headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headersResponse.setContentDispositionFormData("attachment", "CBUAE_Repo_Data_Template.xls");
		

		String userid = (String) req.getSession().getAttribute("USERID");

		auditService.createBusinessAudit(userid, "DOWNLOAD", "REPO_DATA_TEMPLATE_EXCEL", null, "BCBUAE_REPO_DATA_TEMPLATE");


		return ResponseEntity.ok().headers(headersResponse).body(excelData);
	}

//-------------------------------------Repo Report End---------------------------------------

	// -------------------------------------Treasury_Credit_Limit_Management---------------------------------------

	@RequestMapping(value = "Treasury_Credit_Limit_Management", method = RequestMethod.GET)
	public String TreasuryCredit(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) Integer slNo, Model model) {

		if ("edit".equalsIgnoreCase(formmode) && slNo != null) {
			model.addAttribute("formmode", "edit");
			model.addAttribute("creditData", treasuryCreditRepo.findById(slNo).orElse(new RT_TreasuryCreditEntity()));
		} else if ("list".equalsIgnoreCase(formmode)) {
			model.addAttribute("formmode", "list");
			model.addAttribute("TClist", treasuryCreditRepo.getTClist());
		} else {
			Timestamp lastdatetimestamp = treasuryCreditRepo.findLastReportDate();
			Timestamp secondlastdatetimestamp = treasuryCreditRepo.findSecondLastReportDate();			
			LocalDate lastDate=lastdatetimestamp.toLocalDateTime().toLocalDate();		
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String lastDateString = (lastdatetimestamp == null) ? null
					: lastdatetimestamp.toLocalDateTime().format(formatter);
			String secondLastDateString =(secondlastdatetimestamp == null) ? null
					:  secondlastdatetimestamp.toLocalDateTime().format(formatter);
			RT_DataControl data= RT_DatacontrolRepository.getdata(lastDateString,"CBUAE_Treasury_Credit_Limit_Management_Data_Template");
			RT_DataControl secondlastdata= RT_DatacontrolRepository.getdata(secondLastDateString,"CBUAE_Treasury_Credit_Limit_Management_Data_Template");
			if (data != null && !data.equals(null)) {
				model.addAttribute("data", data);
				model.addAttribute("formmode", "exist");
			}
			else if(secondlastdata != null && !secondlastdata.equals(null)){
				model.addAttribute("data", secondlastdata);
				model.addAttribute("formmode", "exist");
			}else {
				model.addAttribute("formmode", "add");
				model.addAttribute("formmode", "null");
			}
			model.addAttribute("lastDate", lastDate);
			model.addAttribute("bankname", "Bank of Baroda");
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
	public ResponseEntity<ByteArrayResource> downloadTreasuryCreditExcel(HttpServletRequest req) {
		logger.info("Controller: Received request for Treasury credit Excel download.");

		try {
			byte[] excelData = treasuryCreditService.generateTreasuryExcel();

			if (excelData.length == 0) {
				logger.warn("Controller: Service returned no data. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			HttpHeaders headers = new HttpHeaders();
			String filename = "CBUAE_Treasury_Credit_Limit_Management_Data_Template.xlsx";
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			
			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "TREASURY_CREDIT_LIMIT_MANAGEMENT_EXCEL", null, "BCBUAE_TREASURY_CRE_LMT_MANAGEMENT");
			
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
			@RequestParam(required = false) String formmode, Model model) {

		if ("edit".equalsIgnoreCase(formmode) && siNo != null) {
			model.addAttribute("formmode", "edit");
			model.addAttribute("InvestmentData", investmentSecuritiesDataTemplateRepo.findById(siNo));
		} else if ("list".equalsIgnoreCase(formmode)) {
			List<RT_Investment_Securities_Data_Template> list = investmentSecuritiesDataTemplateRepo.getsecDatalist();

			model.addAttribute("formmode", "list");
			model.addAttribute("ISList", list); // Used in HTML table
		} /*
			 * else { model.addAttribute("formmode", "add");
			 * model.addAttribute("securityData", new
			 * RT_Investment_Securities_Data_Template()); }
			 */else {
				 Timestamp lastdatetimestamp = investmentSecuritiesDataTemplateRepo.findLastReportDate();
					Timestamp secondlastdatetimestamp = investmentSecuritiesDataTemplateRepo.findSecondLastReportDate();			
					LocalDate lastDate=lastdatetimestamp.toLocalDateTime().toLocalDate();		
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
					String lastDateString = (lastdatetimestamp == null) ? null
							: lastdatetimestamp.toLocalDateTime().format(formatter);
					String secondLastDateString =(secondlastdatetimestamp == null) ? null
							:  secondlastdatetimestamp.toLocalDateTime().format(formatter);
					RT_DataControl data= RT_DatacontrolRepository.getdata(lastDateString,"CBUAE_Investment_Securities_Data_Template");
					RT_DataControl secondlastdata= RT_DatacontrolRepository.getdata(secondLastDateString,"CBUAE_Investment_Securities_Data_Template");
					if (data != null && !data.equals(null)) {
						model.addAttribute("data", data);
						model.addAttribute("formmode", "exist");
					}
					else if(secondlastdata != null && !secondlastdata.equals(null)){
						model.addAttribute("data", secondlastdata);
						model.addAttribute("formmode", "exist");
					}else {
						model.addAttribute("formmode", "add");
						model.addAttribute("formmode", "null");
					}
					model.addAttribute("lastDate", lastDate);
					model.addAttribute("bankname", "Bank of Baroda");
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

	// -------------------------------------Liquidity_Risk_Data
	// -start---------------------------------------

	@RequestMapping(value = "Liquidity_Risk_Data", method = RequestMethod.GET)
	public String liquidityData(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) BigDecimal slno, Model model) {

		if ("edit".equalsIgnoreCase(formmode) && slno != null) {
			model.addAttribute("formmode", "edit");
			model.addAttribute("liquidityData",
					LiquidityRiskDataRepository.findById(slno).orElse(new RT_Liquidity_Risk_Data_Template()));
		} else if ("list".equalsIgnoreCase(formmode)) {
			model.addAttribute("formmode", "list");
			model.addAttribute("liquidityList", LiquidityRiskDataRepository.getLiquiditylist());
		} else {
			Timestamp lastdatetimestamp = LiquidityRiskDataRepository.findLastReportDate();
			Timestamp secondlastdatetimestamp = LiquidityRiskDataRepository.findSecondLastReportDate();			
			LocalDate lastDate=lastdatetimestamp.toLocalDateTime().toLocalDate();		
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String lastDateString = (lastdatetimestamp == null) ? null
					: lastdatetimestamp.toLocalDateTime().format(formatter);
			String secondLastDateString =(secondlastdatetimestamp == null) ? null
					:  secondlastdatetimestamp.toLocalDateTime().format(formatter);
			RT_DataControl data= RT_DatacontrolRepository.getdata(lastDateString,"CBUAE_Liquidity_Risk_Data_Template");
			RT_DataControl secondlastdata= RT_DatacontrolRepository.getdata(secondLastDateString,"CBUAE_Liquidity_Risk_Data_Template");
			if (data != null && !data.equals(null)) {
				model.addAttribute("data", data);
				model.addAttribute("formmode", "exist");
			}
			else if(secondlastdata != null && !secondlastdata.equals(null)){
				model.addAttribute("data", secondlastdata);
				model.addAttribute("formmode", "exist");
			}else {
				model.addAttribute("formmode", "add");
				model.addAttribute("formmode", "null");
			}
			model.addAttribute("lastDate", lastDate);
			model.addAttribute("bankname", "Bank of Baroda");
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

	@RequestMapping(value = "/downloadLiquidityRiskData", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadLiquidityRiskData(HttpServletRequest req) {
		logger.info("Controller: Received request for Trade Market Risk Excel download.");

		try {
			byte[] excelData = liquidityRiskDataService.generateLiquidityDataExcel();

			if (excelData.length == 0) {
				logger.warn("Controller: Service returned no data. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			HttpHeaders headers = new HttpHeaders();
			String filename = "CBUAE_Liquidity Risk_Data_Template.xlsx";
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			
			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "LIQUIDITY_RISK_DATA_EXCEL", null, "BCBUAE_LIQUIDITY_RISK_DATA_TEMPLATE");
			
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
	// -------------------------------------Liquidity_Risk_Data
	// End---------------------------------------

	@Autowired
	private RT_InvestmentSecurity_Service rtInvestmentSecuritiesService;

	// ... other controller methods ...

	@RequestMapping(value = "/downloadInvestmentSecuritiesExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadInvestmentSecuritiesExcel(HttpServletRequest req) {
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
			String filename = "CBUAE_Investment_Securities_Data_Template_Pillar2.xlsx";
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			
			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "INVESTMENT_SECURITIES_DATA_EXCEL", null, "BCBUAE_INVESTMENT_SECURITIES_DATA");

			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (FileNotFoundException e) {
			logger.error(
					"Controller ERROR: The CBUAE_Investment_Securities_Data_Template_Pillar2.xlsx template file was not found.",
					e);
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
			@RequestParam(required = false) String reportdate,
			@RequestParam(required = false) String SI_NO,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size,
			@RequestParam(required = false) String columnId,
			Model md, HttpServletRequest req) {
		if ("edit".equalsIgnoreCase(formmode) && SI_NO != null && !SI_NO.isEmpty()) {
			Long Serialnumber = Long.parseLong(SI_NO);
			RT_Investment_Risk_Data_Dashboard_Template data = RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS
					.getParticularDataBySI_NO(Serialnumber);
			md.addAttribute("investmentriskdatadashboard", data);
			System.out.println("edit is formmode");
			md.addAttribute("formmode", "edit");
		} else if ("list".equalsIgnoreCase(formmode)) {
			md.addAttribute("InvestmentRiskDatalist", RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS.getlist());
			md.addAttribute("formmode", "list");
		}
		
		else if ("limits".equalsIgnoreCase(formmode)) {
			md.addAttribute("formmode", "limits");
		}
		
		else if ("Detail".equalsIgnoreCase(formmode)) {
			
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date reportdatefor = null;

			try {
				if (reportdate != null) {
					reportdatefor = dateFormat.parse(reportdate);
				}
			} catch (ParseException e) {
				e.printStackTrace();
				md.addAttribute("error", "Invalid date format. Expected dd/MM/yyyy");
				return "RT/Investment_Risk_Data_Dashboard_Template";
			}
			
			int pageSize = size;
			int offset = page;
			int totalRows=0;
			int totalPages=0;
			totalRows = rtinvestmentriskdatadetailrep.RtInvestmentRiskDatacountROWID(reportdatefor, columnId);
			System.out.println("columnId="+columnId);
			totalPages = (int) Math.ceil((double) totalRows / pageSize);
			List<RtInvestmentRiskDataDetail> RtInvestmentRiskdetaillist = rtinvestmentriskdatadetailrep.RtInvestmentRiskDatalistrowid(reportdatefor,columnId, offset, pageSize);
			System.out.println("size="+RtInvestmentRiskdetaillist.size());
			md.addAttribute("formmode", "Detail");
			md.addAttribute("invriskdetaillist", RtInvestmentRiskdetaillist);
			md.addAttribute("reportdate", reportdate);
			md.addAttribute("pagination", "YES");
			md.addAttribute("currentPage", page);
			md.addAttribute("totalPages", totalPages);
			md.addAttribute("columnId", columnId);
		}
		
		
		else {
			Timestamp lastdatetimestamp = RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS.findLastReportDate();
			Timestamp secondlastdatetimestamp = RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS.findSecondLastReportDate();			
			LocalDate lastDate=lastdatetimestamp.toLocalDateTime().toLocalDate();		
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String lastDateString = (lastdatetimestamp == null) ? null
					: lastdatetimestamp.toLocalDateTime().format(formatter);
			String secondLastDateString =(secondlastdatetimestamp == null) ? null
					:  secondlastdatetimestamp.toLocalDateTime().format(formatter);
			RT_DataControl data= RT_DatacontrolRepository.getdata(lastDateString,"Investment_Risk_Data_Dashboard_Template");
			RT_DataControl secondlastdata= RT_DatacontrolRepository.getdata(secondLastDateString,"Investment_Risk_Data_Dashboard_Template");
			if (data != null && !data.equals(null)) {
				md.addAttribute("data", data);
				md.addAttribute("formmode", "exist");
			}
			else if(secondlastdata != null && !secondlastdata.equals(null)){
				md.addAttribute("data", secondlastdata);
				md.addAttribute("formmode", "exist");
			}else {
				md.addAttribute("formmode", "add");
				md.addAttribute("formmode", "null");
			}
			md.addAttribute("lastDate", lastDate);
			md.addAttribute("bankname", "Bank of Baroda");
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
			@ModelAttribute RT_Investment_Risk_Data_Dashboard_Template investmentriskdatadashboard) {
		boolean updated = investmentriskdatadictionaryService
				.updateinvestmentriskdatadictionary(investmentriskdatadashboard);

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
			Timestamp lastdatetimestamp = friskdataRepo.findLastReportDate();
			Timestamp secondlastdatetimestamp = friskdataRepo.findSecondLastReportDate();			
			LocalDate lastDate=lastdatetimestamp.toLocalDateTime().toLocalDate();		
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String lastDateString = (lastdatetimestamp == null) ? null
					: lastdatetimestamp.toLocalDateTime().format(formatter);
			String secondLastDateString =(secondlastdatetimestamp == null) ? null
					:  secondlastdatetimestamp.toLocalDateTime().format(formatter);
			RT_DataControl data= RT_DatacontrolRepository.getdata(lastDateString,"CBUAE_Fx_Risk_Data_Template");
			RT_DataControl secondlastdata= RT_DatacontrolRepository.getdata(secondLastDateString,"CBUAE_Fx_Risk_Data_Template");
			if (data != null && !data.equals(null)) {
				md.addAttribute("data", data);
				md.addAttribute("formmode", "exist");
			}
			else if(secondlastdata != null && !secondlastdata.equals(null)){
				md.addAttribute("data", secondlastdata);
				md.addAttribute("formmode", "exist");
			}else {
				md.addAttribute("formmode", "add");
				md.addAttribute("formmode", "null");
			}
			md.addAttribute("lastDate", lastDate);
			md.addAttribute("bankname", "Bank of Baroda");

			// You had md.addAttribute("formmode", "null"); — removed this line because it
			// would overwrite the previous one
		}

		List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
		md.addAttribute("bankList", bankList);

		return "RT/Fx_Risk_Data";
	}

	// For download excel for fxriskdata

	@RequestMapping(value = "/downloadFxriskExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadFxriskExcel(HttpServletRequest req) {
		logger.info("Controller: Received request for Fx Risk Excel download.");

		try {
			byte[] excelData = fxriskdataService.generateFxRiskExcel();

			if (excelData.length == 0) {
				logger.warn("Controller: Service returned no data. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			HttpHeaders headers = new HttpHeaders();
			String filename = "CBUAE_FX_Risk_Data_Template.xlsx";
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			

			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "FX_RISK_DATA_EXCEL", null, "BCBUAE_FX_RISK_DATA");

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

	// For download excel for downloadNostroExcel

	@RequestMapping(value = "downloadNostroExcel", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadNostroExcel(HttpServletRequest req) throws IOException {
		System.out.println("Entered controller downloadNostroExcel");

		String userid = (String) req.getSession().getAttribute("USERID");

		auditService.createBusinessAudit(userid, "DOWNLOAD", "NOSTRO_EXCEL", null, "BCBUAE_NOSTRO_BALANCE");

		// ✅ Generate Excel file and prepare response
		File excelFile = nostroService.generateNostroExcel();
		byte[] excelData = java.nio.file.Files.readAllBytes(excelFile.toPath());

		HttpHeaders headersResponse = new HttpHeaders();
		headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headersResponse.setContentDispositionFormData("attachment", "NostroAccBalance.xlsx");

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
			Timestamp lastdatetimestamp = trade_market_risk_repo.findLastReportDate();
			Timestamp secondlastdatetimestamp = trade_market_risk_repo.findSecondLastReportDate();			
			LocalDate lastDate=lastdatetimestamp.toLocalDateTime().toLocalDate();		
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String lastDateString = (lastdatetimestamp == null) ? null
					: lastdatetimestamp.toLocalDateTime().format(formatter);
			String secondLastDateString =(secondlastdatetimestamp == null) ? null
					:  secondlastdatetimestamp.toLocalDateTime().format(formatter);
			RT_DataControl data= RT_DatacontrolRepository.getdata(lastDateString,"CBUAE_Trade_Market_Risk_Data_Template");
			RT_DataControl secondlastdata= RT_DatacontrolRepository.getdata(secondLastDateString,"CBUAE_Trade_Market_Risk_Data_Template");
			if (data != null && !data.equals(null)) {
				model.addAttribute("data", data);
				model.addAttribute("formmode", "exist");
			}
			else if(secondlastdata != null && !secondlastdata.equals(null)){
				model.addAttribute("data", secondlastdata);
				model.addAttribute("formmode", "exist");
			}else {
				model.addAttribute("formmode", "add");
				model.addAttribute("formmode", "null");
			}
			model.addAttribute("lastDate", lastDate);
			model.addAttribute("bankname", "Bank of Baroda");
		}
		List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
		List<RT_CountryRiskDropdown> countryList = countryRepo.findAllByOrderByCountryOfRiskAsc();
		model.addAttribute("bankList", bankList);
		model.addAttribute("countryList", countryList);

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
			
			Optional<RT_TradeMarketRiskData> existingData = trade_market_risk_repo.findById(data.getReportDate());
			
			RT_TradeMarketRiskData existing= existingData.get();
			
		    RT_TradeMarketRiskData dbUser = new RT_TradeMarketRiskData();
			org.springframework.beans.BeanUtils.copyProperties(existing, dbUser);
			List<String> ignoreFields = Arrays.asList("createUser", "modifyUser", "delFlg");

			Map<String, String> changes = new LinkedHashMap<>();

			for (Field field : RT_TradeMarketRiskData.class.getDeclaredFields()) {
				field.setAccessible(true);
				try {
					Object oldValue = field.get(dbUser);
					Object newValue = field.get(data);
					if ((oldValue == null || oldValue.toString().trim().isEmpty())
							&& (newValue == null || newValue.toString().trim().isEmpty())) {
						continue;
					}
					if (ignoreFields.contains(field.getName()) && newValue == null) {
						continue;
					}

					if (oldValue instanceof Date || newValue instanceof Date) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String oldDateStr = (oldValue != null) ? sdf.format(oldValue) : null;
						String newDateStr = (newValue != null) ? sdf.format(newValue) : null;

						if (Objects.equals(oldDateStr, newDateStr)) {
							continue;
						}
					} else {
						if (Objects.equals(oldValue, newValue)) {
							continue;
						}
					}

					if (newValue == null) {
						changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: null");
					} else {
						changes.put(field.getName(), "OldValue: " + oldValue + ", NewValue: " + newValue);
					}

					if (newValue != null) {
						field.set(dbUser, newValue);
					}

				} catch (IllegalAccessException e) {
					System.err.println("Access error for field: " + field.getName() + " - " + e.getMessage());
				}
			}

			trade_market_risk_repo.save(data);
			
			 System.out.println("changes : "+changes);

		        // Audit only if any field was changed
		        if (!changes.isEmpty()) {
		        	auditService.createBusinessAudit(
		        			String.valueOf(data.getReportDate()),           // Unique ID
		                "MODIFY",                             // Action
		                "TRADE_MARKET_RISK_DATA_EDIT_SCREEN",                  // Screen name
		                changes,                              // Changed fields map
		                "BCBUAE_TRADE_MARKET_RISK_DATA"              // Table name
		            );
		        }
		        

			return "Updated successfully";
		} catch (Exception e) {
			return "Error while updating: " + e.getMessage();
		}
	}

	@Autowired
	private RT_TradeMarketRiskService rtTradeMarketRiskService;

	@RequestMapping(value = "/downloadTradeMarketRiskExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadTradeMarketRiskExcel(HttpServletRequest req) {
		logger.info("Controller: Received request for Trade Market Risk Excel download.");

		try {
			byte[] excelData = rtTradeMarketRiskService.generateTradeMarketRiskExcel();

			if (excelData.length == 0) {
				logger.warn("Controller: Service returned no data. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			HttpHeaders headers = new HttpHeaders();
			String filename = "CBUAE_Trade_Market_Risk_Data_Template.xls";
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			

			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "TRADE_MARKET_RISK_DATA_EXCEL", null, "BCBUAE_TRADE_MARKET_RISK_DATA");

			
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
	public String Mmdata(@RequestParam(required = false) String formmode,
	                     @RequestParam(required = false) String deal_no,
	                     Model md,
	                     HttpServletRequest req,
	                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date Report_date) {

	    LocalDate today = LocalDate.now();
	    String formattedDate = null;
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	    // ✅ Convert Report_date → String safely
	    if (Report_date != null) {
	        formattedDate = sdf.format(Report_date);
	    }

	    // ================= EDIT =================
	    if ("edit".equalsIgnoreCase(formmode) && deal_no != null && !deal_no.isEmpty()) {

	        RT_MmData data = mmdataRepo.getParticularDataBySI_NO(deal_no);
	        md.addAttribute("mmData", data);
	        md.addAttribute("formmode", "edit");

	    }

	    // ================= LIST =================
	    else if ("list".equalsIgnoreCase(formmode)) {

	        md.addAttribute("branchList", mmdataRepo.getlist(Report_date));
	        md.addAttribute("formmode", "list");
	        md.addAttribute("lastDate",LocalDate.parse(formattedDate, formatter) );

	    }

	    // ================= DEFAULT =================
	    else {

	        Timestamp lastdatetimestamp = mmdataRepo.findLastReportDate();
	        Timestamp secondlastdatetimestamp = mmdataRepo.findSecondLastReportDate();

	        

	        String lastDateString = null;
	        String secondLastDateString = null;
	        LocalDate lastDate = null;

	        // ✅ NULL SAFE
	        if (lastdatetimestamp != null) {
	            lastDate = lastdatetimestamp.toLocalDateTime().toLocalDate();
	            lastDateString = lastdatetimestamp.toLocalDateTime().format(formatter);
	        }

	        if (secondlastdatetimestamp != null) {
	            secondLastDateString = secondlastdatetimestamp.toLocalDateTime().format(formatter);
	        }

	        RT_DataControl data = RT_DatacontrolRepository.getdata(lastDateString, "CBUAE_Mm_Data_Template");
	        RT_DataControl secondlastdata = RT_DatacontrolRepository.getdata(secondLastDateString, "CBUAE_Mm_Data_Template");

	        RT_DataControl report_datedata = null;

	        if (formattedDate != null) {
	            report_datedata = RT_DatacontrolRepository.getdata(formattedDate, "CBUAE_Mm_Data_Template");
	        }
	        
	        System.out.println(formattedDate);

	        // ✅ FIXED NULL CHECKS
	        if (report_datedata != null) {
	        	System.out.println(formattedDate);
	        	lastDate = LocalDate.parse(formattedDate, formatter);
	            md.addAttribute("data", report_datedata);
	            md.addAttribute("formmode", "exist");

	        } else if (data != null) {
	        	
	            md.addAttribute("data", data);
	            md.addAttribute("formmode", "exist");

	        } else if (secondlastdata != null) {
	        	lastDate = LocalDate.parse(secondLastDateString, formatter);
	            md.addAttribute("data", secondlastdata);
	            md.addAttribute("formmode", "exist");

	        } else {

	            md.addAttribute("formmode", "add");
	        }
	        
	        md.addAttribute("lastDate", lastDate);
	        md.addAttribute("bankname", "Bank of Baroda");
	    }

	    // ================= DROPDOWNS =================
	    List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
	    List<RT_CountryRiskDropdown> countryList = countryRepo.findAllByOrderByCountryOfRiskAsc();

	    md.addAttribute("bankList", bankList);
	    md.addAttribute("countryList", countryList);

	    return "RT/Mm_Data";
	}

	@RequestMapping(value = "/downloadMmExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadMmExcel(HttpServletRequest req) {
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
			
			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "MM_DATA_EXCEL", null, "BCBUAE_MM_DATA");

			
			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

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

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("roleId", roleId);

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
			md.addAttribute("LOCATION", Counterparty_Reps.getallLOCATION());
			List<String> getcode = Counterparty_Reps.getcodes();
			md.addAttribute("ratinglist", rt_rwa_rating_guidelines_rep.getratinglist());
			md.addAttribute("getcode", getcode);
		}

		else if (formmode.equals("view")) {
			md.addAttribute("formmode", formmode);
			logger.info("modify screen id is: " + id);
			md.addAttribute("list", Counterparty_Reps.getBYID(Long.valueOf(id)));
			List<String> counterList = Counterparty_Reps.getall();
			md.addAttribute("counterlist", counterList);
			List<String> getcode = Counterparty_Reps.getcodes();
			md.addAttribute("getcode", getcode);
		}

		else {
			logger.info("Adding new Counterparty Bank form initialization started.");
			md.addAttribute("menu", "Counterparty Bank - Add");
			md.addAttribute("formmode", formmode);

			List<String> counterList = Counterparty_Reps.getall();
			md.addAttribute("counterlist", counterList);
			List<String> getcode = Counterparty_Reps.getcodes();
			md.addAttribute("getcode", getcode);
			md.addAttribute("LOCATION", Counterparty_Reps.getallLOCATION());
			
			logger.info("Fetched counterparty list: size={}", counterList.size());

			Integer maxSuffix = Counterparty_Reps.findMaxSrlNoSuffix(); // e.g., returns 1
			int nextSuffix = (maxSuffix != null) ? maxSuffix + 1 : 1;
			String serialNo = String.format("%s%03d", "BOBUAE", nextSuffix); // BOBUAE002
			md.addAttribute("serialNo", serialNo);
			md.addAttribute("ratinglist", rt_rwa_rating_guidelines_rep.getratinglist());
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

	@Autowired
	Mis_exposure_bill_detail_rep Mis_exposure_bill_detail_rep;

	@RequestMapping(value = "counterparty_list", method = { RequestMethod.GET, RequestMethod.POST })
	public String counterparty_list(@RequestParam(required = false) String formmode,
			@RequestParam(value = "Exposurebillid", required = false) String Exposurebillid,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date Report_date, Model md,
			HttpServletRequest req) {
		String userid = (String) req.getSession().getAttribute("USERID");
		String BRANCHCODE = (String) req.getSession().getAttribute("BRANCHCODE");
		String BRANCHNAME = (String) req.getSession().getAttribute("BRANCHNAME");
		String ROLEID = (String) req.getSession().getAttribute("ROLEID");
		String domIds = ((String) req.getSession().getAttribute("DOMAINID")).trim();
		logger.info("User '{}' accessed counterparty_list with formmode='{}'", userid, formmode);
		LocalDate today = LocalDate.now();
		md.addAttribute("dom_ids", domIds); // To highlight the menu

		if ("list".equalsIgnoreCase(formmode)) {
			logger.info("Loading exposure data list view for branch '{}'", BRANCHNAME);

			md.addAttribute("menu", "List Of Exposure Data"); // Default menu label
			md.addAttribute("formmode", "list");
			Date currentDate = new Date(); // current system date
			md.addAttribute("currentDate", currentDate);
			List<String> branchList = ASL_Report_Reps.getAllBranchNames();
			md.addAttribute("branches", branchList);
			md.addAttribute("BRANCHCODE", BRANCHCODE);
			md.addAttribute("ROLEID", ROLEID);

			System.out.println("ROLEID--" + ROLEID);
			md.addAttribute("currentBranch", BRANCHNAME);
			List<String> branchesl;
			if ("ADM".equalsIgnoreCase(ROLEID)) {
				branchesl = ASL_Report_Reps.getAllBranchNames();
			} else {
				branchesl = Collections.singletonList(BRANCHNAME);
			}

			md.addAttribute("branchesl", branchesl);

			java.sql.Date sqlDate = java.sql.Date.valueOf(today);
			List<ASL_Report_Entity> list = ASL_Report_Reps.getAlls(sqlDate, BRANCHNAME.trim());
			logger.info("Fetched {} exposure records for branch '{}' date '{}'", list.size(), BRANCHNAME, sqlDate);
			md.addAttribute("listall", list);

		} else if ("Billdetaillist".equalsIgnoreCase(formmode)) {

			if (Report_date != null) {
				Report_date = java.sql.Date.valueOf(normalizeDate(Report_date.toString()));
			} else {
				Report_date = java.sql.Date.valueOf(today.minusDays(0));
			}
			md.addAttribute("Todateselected", Report_date);
			md.addAttribute("formmode", formmode);
			md.addAttribute("menuname", "Bill detail operation - list");

			if ("USR-M".equalsIgnoreCase(ROLEID) || "USR-C".equalsIgnoreCase(ROLEID)) {
				System.out.println("Selected Report : " + Report_date + ": and user id is : " + ROLEID);
				md.addAttribute("Activebilldetails",
						Mis_exposure_bill_detail_rep.getbilldetailsbranchwise(BRANCHNAME, Report_date));
			} else {
				System.out.println("Selected Report : " + Report_date + ": and user id is : " + ROLEID
						+ " and Branch Name is : " + BRANCHNAME);
				md.addAttribute("Activebilldetails", Mis_exposure_bill_detail_rep.getbilldetails(Report_date));
			}

		} else if ("Addbilldetail".equalsIgnoreCase(formmode)) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("menuname", "Bill detail operation - Add");
			Mis_exposure_bill_detail_entity Mis_exposure_bill_detail_entity = new Mis_exposure_bill_detail_entity();

			Mis_exposure_bill_detail_entity.setDate_of_loan(new Date());
			Mis_exposure_bill_detail_entity.setDue_date(new Date());
			Mis_exposure_bill_detail_entity.setSrl_no(String.valueOf(Mis_exposure_bill_detail_rep.Generatesrl_no()));

			if ("USR-M".equalsIgnoreCase(ROLEID) || "USR-C".equalsIgnoreCase(ROLEID)) {

				Mis_exposure_bill_detail_entity.setBranch_name(BRANCHNAME);
			}

			md.addAttribute("billdata", Mis_exposure_bill_detail_entity);
			md.addAttribute("Counterpartynamelist", Counterparty_Reps.Getcounterpartyname());

		} else if ("Editbilldetail".equalsIgnoreCase(formmode)) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("menuname", "Bill detail operation - Edit");
			md.addAttribute("Todateselected", Report_date);
			System.out.println("Edit Exposure Menu Received bill detail is : " + Exposurebillid);
			System.out.println("Report date for bill : " + Report_date);
			md.addAttribute("billdata", Mis_exposure_bill_detail_rep.getbilldetail(Exposurebillid, Report_date));
			md.addAttribute("Counterpartynamelist", Counterparty_Reps.Getcounterpartyname());

		} else if ("Deletebilldetail".equalsIgnoreCase(formmode)) {

			md.addAttribute("formmode", formmode);

			md.addAttribute("menuname", "Bill detail operation - Delete");
			md.addAttribute("billdata", Mis_exposure_bill_detail_rep.getbilldetail(Exposurebillid, Report_date));
			md.addAttribute("Counterpartynamelist", Counterparty_Reps.Getcounterpartyname());
		} else if ("Verifybilldetail".equalsIgnoreCase(formmode)) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("menuname", "Bill detail operation - Verify");
			md.addAttribute("billdata", Mis_exposure_bill_detail_rep.getbilldetail(Exposurebillid, Report_date));
			md.addAttribute("Counterpartynamelist", Counterparty_Reps.Getcounterpartyname());
		} else {
			logger.info("Opening file upload mode for counterparty. User: '{}', Branch: '{}'", userid, BRANCHNAME);
			md.addAttribute("menu", "List Of Counterparty"); // Default menu label
			md.addAttribute("menu", "Upload File Of Counterparty");
			md.addAttribute("formmode", "add");
			md.addAttribute("userid", userid);

			if ("ADM-M".equalsIgnoreCase(ROLEID) || "ADM-C".equalsIgnoreCase(ROLEID)
					|| "MGR".equalsIgnoreCase(ROLEID)) {
				List<String> codes = UserProfileReps.getallcodes();
				logger.info("Counter party bank code Size: '{}', Role id is :'{}'", codes.size(), ROLEID);
				md.addAttribute("codes", codes);
			}
			// Add for all roles
			md.addAttribute("BRANCHCODE", BRANCHCODE);
			md.addAttribute("BRANCHNAME", BRANCHNAME);
			md.addAttribute("ROLEID", ROLEID);

			System.out.println("ROLEID--" + ROLEID);

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
			List<MIS_TREASURY_PLACEMENT_ENTITY> placements = Mis_treasury_placement_repo.getAllListByDate(currentDate);
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
			List<MIS_TREASURY_PLACEMENT_ENTITY> placements = Mis_treasury_placement_repo
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

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("RT_ASL_FINAL_STATEMENT_GENERATE_PROCEDURE");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.setParameter(1, formattedDate);
		query.execute();

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
		mode = (mode == null) ? "" : mode.trim().toLowerCase();

		logger.info("Upload mode received: [{}]", mode);
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
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date Report_date, Model md,
			HttpServletRequest req) {

		/// From Attributes
		String domIds = ((String) req.getSession().getAttribute("DOMAINID")).trim();
		String userid = (String) req.getSession().getAttribute("USERID");
		String BRANCHCODE = (String) req.getSession().getAttribute("BRANCHCODE");
		String BRANCHNAME = (String) req.getSession().getAttribute("BRANCHNAME");
		String ROLEID = (String) req.getSession().getAttribute("ROLEID");

		LocalDate today = LocalDate.now(); // Get today's date

		if (Report_date != null) {
			Report_date = java.sql.Date.valueOf(normalizeDate(Report_date.toString()));
		} else {
			Report_date = java.sql.Date.valueOf(today.minusDays(0));
		}

		try {

			if (formmode == null || "list".equalsIgnoreCase(formmode)) {
				md.addAttribute("menu", "List Of Treasury Placements");
				md.addAttribute("formmode", "list");
				// Fecth Treasury detail as per date
				List<MIS_TREASURY_PLACEMENT_ENTITY> Treasurydata = Mis_treasury_placement_repo
						.Gettreasurydata(Report_date);
				md.addAttribute("listss", Treasurydata);
				md.addAttribute("Report_date", Report_date);
				logger.info("Fetched {} treasury placements.", Report_date + " " + Treasurydata.size());
			} else if ("add".equalsIgnoreCase(formmode)) {
				logger.info("Opening file upload mode for Treasury Placement. User: '{}', Branch: '{}'", userid,
						BRANCHNAME);
				md.addAttribute("menu", "List Of Treasury Placement"); // Default menu label
				md.addAttribute("menu", "Upload File Of Treasury Placement");
				md.addAttribute("formmode", "add");
				md.addAttribute("userid", userid);

				if ("ADM-M".equalsIgnoreCase(ROLEID) || "ADM-C".equalsIgnoreCase(ROLEID)
						|| "MGR".equalsIgnoreCase(ROLEID)) {
					List<String> codes = UserProfileReps.getallcodes();
					logger.info("Counter party bank code Size: '{}', Role id is :'{}'", codes.size(), ROLEID);
					md.addAttribute("codes", codes);
				}
				// Add for all roles
				md.addAttribute("BRANCHCODE", BRANCHCODE);
				md.addAttribute("BRANCHNAME", BRANCHNAME);
				md.addAttribute("ROLEID", ROLEID);
				md.addAttribute("Report_date", Report_date);
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
				if ("ADM-M".equalsIgnoreCase(ROLEID) || "ADM-C".equalsIgnoreCase(ROLEID)
						|| "MGR".equalsIgnoreCase(ROLEID)) {
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

		mode = (mode == null) ? "" : mode.trim().toLowerCase();
		logger.info("Searching data | Date: {} | Branch: {} | Mode: {}", reportDate, iBranchCode, mode);

		int size = 0;

		try {
			if ("exposure".equals(mode)) {
				size = ASL_Report_Reps.getByReportDateAndBR(java.sql.Date.valueOf(reportDate), iBranchCode).size();

			} else if ("placement".equals(mode)) {
				size = Mis_treasury_placement_repo.getByReportDateAndBR(java.sql.Date.valueOf(reportDate), iBranchCode)
						.size();

			} else if ("settlement".equals(mode)) {
				size = MIS_SETTLEMENT_ENTITY_REPs.getByReportDateAndBR(java.sql.Date.valueOf(reportDate), iBranchCode)
						.size();
			}
		} catch (Exception e) {
			logger.error("Error while checking existing data", e);
			return 0;
		}

		logger.info("Final record count returned to UI = {}", size);
		return size; // ✔ 0 = NOT EXISTS | >0 = EXISTS
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
	public ResponseEntity<ByteArrayResource> downloadTradeleveldataderivativeExcel(HttpServletRequest req) {
		logger.info("Controller: Received request for Trade Level Data Derivative Excel download.");

		try {
			byte[] excelData = tradeleveldataderivativeService.generateTradeleveldataderivativeExcel();

			if (excelData.length == 0) {
				logger.warn(
						"Controller: No data found for Trade Level Data Derivative report. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			String filename = "CBUAE_Trade_Level_Data_Derivatives_Template.xlsx";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			
			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "TRADE_LEVEL_DATA_DERIVATVE_EXCEL", null, "BCBUAE_TRADE_LEVEL_DERIVATIVES");

			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

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
			Timestamp lastdatetimestamp = tradeleveldataderivativesRepo.findLastReportDate();
			Timestamp secondlastdatetimestamp = tradeleveldataderivativesRepo.findSecondLastReportDate();			
			LocalDate lastDate=lastdatetimestamp.toLocalDateTime().toLocalDate();		
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String lastDateString = (lastdatetimestamp == null) ? null
					: lastdatetimestamp.toLocalDateTime().format(formatter);
			String secondLastDateString =(secondlastdatetimestamp == null) ? null
					:  secondlastdatetimestamp.toLocalDateTime().format(formatter);
			RT_DataControl data= RT_DatacontrolRepository.getdata(lastDateString,"CBUAE_Trade_Level_Data_Derivative_Template");
			RT_DataControl secondlastdata= RT_DatacontrolRepository.getdata(secondLastDateString,"CBUAE_Trade_Level_Data_Derivatives_Template");
			if (data != null && !data.equals(null)) {
				md.addAttribute("data", data);
				md.addAttribute("formmode", "exist");
			}
			else if(secondlastdata != null && !secondlastdata.equals(null)){
				md.addAttribute("data", secondlastdata);
				md.addAttribute("formmode", "exist");
			}else {
				md.addAttribute("formmode", "add");
				md.addAttribute("formmode", "null");
			}
			md.addAttribute("lastDate", lastDate);
			md.addAttribute("bankname", "Bank of Baroda");

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
			@RequestParam(required = false) String siNo, @RequestParam(required = false) String tab, // changed from
																										// accountNo to
																										// slNo
			Model md, HttpServletRequest req) {

		if ("edit".equalsIgnoreCase(formmode) && siNo != null) {
			// RT_CCR_DATA_TEMPLATE data = ccr_data_template_repository.editccr(siNo);
			RT_CCR_DATA_TEMPLATE data = ccr_data_template_repository.findById(siNo).orElse(null);
// make sure entity class matches
			md.addAttribute("repoData", data);
			System.out.println("edit is formmode");
			md.addAttribute("formmode", "edit");
			System.out.println("marginCallFrequency = " + data.getMarginCallFrequency());
			System.out.println("netCollateralOutstandingAed = " + data.getNetCollateralOutstandingAed());

		} else if ("list".equalsIgnoreCase(formmode)) {

			List<RT_CCR_DATA_TEMPLATE> repoList = ccr_data_template_repository.getlist();
			System.out.println("testing count" + ccr_data_template_repository.getlist().size());
			;
			md.addAttribute("repoList1", repoList);
			System.out.println("list is formmode");
			md.addAttribute("formmode", "list");
		} else {
			/*
			 * md.addAttribute("formmode", "add");
			 * 
			 * if ("template".equalsIgnoreCase(tab)) { md.addAttribute("tab", "template"); }
			 * else { md.addAttribute("tab", "datacontrols"); // fallback default }
			 */
			Timestamp lastdatetimestamp = ccr_data_template_repository.findLastReportDate();
			Timestamp secondlastdatetimestamp = ccr_data_template_repository.findSecondLastReportDate();			
			LocalDate lastDate=(lastdatetimestamp == null) ? null
					: lastdatetimestamp.toLocalDateTime().toLocalDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String lastDateString = (lastdatetimestamp == null) ? null
					: lastdatetimestamp.toLocalDateTime().format(formatter);
			String secondLastDateString =(secondlastdatetimestamp == null) ? null
					:  secondlastdatetimestamp.toLocalDateTime().format(formatter);
			RT_DataControl data= RT_DatacontrolRepository.getdata(lastDateString,"CBUAE_CCR_Data_Template");
			RT_DataControl secondlastdata= RT_DatacontrolRepository.getdata(secondLastDateString,"CBUAE_CCR_Data_Template");
			if (data != null && !data.equals(null)) {
				md.addAttribute("data", data);
				md.addAttribute("formmode", "exist");
			}
			else if(secondlastdata != null && !secondlastdata.equals(null)){
				md.addAttribute("data", secondlastdata);
				md.addAttribute("formmode", "exist");
			}else {
				md.addAttribute("formmode", "add");
				md.addAttribute("formmode", "null");
			}
			md.addAttribute("lastDate", lastDate);
			md.addAttribute("bankname", "Bank of Baroda");
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
	public ResponseEntity<ByteArrayResource> downloadCCRDataExcel(HttpServletRequest req) {
		logger.info("Controller: Received request for CCR DATA Excel download.");

		try {
			byte[] excelData = rtCCRDataService.generateCCRDataExcel();

			if (excelData.length == 0) {
				logger.warn("Controller: Service returned no data for CCR DATA. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			HttpHeaders headers = new HttpHeaders();
			String filename = "CBUAE_CCR_Data_Template.xlsx";
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			
			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "CCR_DATA_TEMPLATE_EXCEL", null, "BCBUAE_CCR_DATA_TABLE");

			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (FileNotFoundException e) {
			logger.error("Controller ERROR: The CBUAE_CCR_Data_Template.xls template file was not found.", e);
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
			Timestamp lastdatetimestamp = foreigncurrencydepositRepo.findLastReportDate();
			Timestamp secondlastdatetimestamp = foreigncurrencydepositRepo.findSecondLastReportDate();			
			LocalDate lastDate=(lastdatetimestamp == null) ? null
					: lastdatetimestamp.toLocalDateTime().toLocalDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String lastDateString = (lastdatetimestamp == null) ? null
					: lastdatetimestamp.toLocalDateTime().format(formatter);
			String secondLastDateString =(secondlastdatetimestamp == null) ? null
					:  secondlastdatetimestamp.toLocalDateTime().format(formatter);
			RT_DataControl data= RT_DatacontrolRepository.getdata(lastDateString,"CBUAE_Cross_Currency_Funding_Spread_Template");
			RT_DataControl secondlastdata= RT_DatacontrolRepository.getdata(secondLastDateString,"CBUAE_Cross_Currency_Funding_Spread_Template");
			if (data != null && !data.equals(null)) {
				md.addAttribute("data", data);
				md.addAttribute("formmode", "exist");
			}
			else if(secondlastdata != null && !secondlastdata.equals(null)){
				md.addAttribute("data", secondlastdata);
				md.addAttribute("formmode", "exist");
			}else {
				md.addAttribute("formmode", "add");
				md.addAttribute("formmode", "null");
			}
			md.addAttribute("lastDate", lastDate);
			md.addAttribute("bankname", "Bank of Baroda");

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
			md.addAttribute("ImpactAnalysis", data);
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

	/*--IRRBB Data template--*/
	@RequestMapping(value = "IRRBB_data_Template", method = RequestMethod.GET)
	public String IRRBB_data_Template(@RequestParam(required = false) BigDecimal SI_NO, @RequestParam(required = false) Date reportDate,
			@RequestParam(required = false) String scenario,@RequestParam(required = false) String glLevel1,@RequestParam(required = false) String glLevel2,
			@RequestParam(required = false) String glLevel3,@RequestParam(required = false) String optionType,@RequestParam(required = false) String rateType,
			@RequestParam(required = false) String referenceRate,@RequestParam(required = false) String instrumentCurrency,
			@RequestParam(required = false) String formmode, Model md) {

		if ("edit".equalsIgnoreCase(formmode) || "editear".equalsIgnoreCase(formmode)) {
			//RT_IRRBB_Data_EAR data = IRRBB_EAR_Repository.getParticularDataBySI_NO(SI_NO);
			
			//RT_IRRBB_Data_EAR data =IRRBB_EAR_Repository.findById(SI_NO).orElse(null);
			RT_IRRBB_Data_EAR data =null;

			md.addAttribute("irrbbear", data);
			System.out.println("edit is formmode");
			md.addAttribute("formmode", "editear");

		}

		else if ("editeve".equalsIgnoreCase(formmode)) {
			System.out.println("EVE is formmode");
			//RT_IRRBB_Data_EVE_Template data = IRRB_EVE_Repo.getParticularDataBySI_NO(SI_NO);
			//RT_IRRBB_Data_EVE_Template data =IRRB_EVE_Repo.findById(SI_NO).orElse(null);
			RT_IRRBB_Data_EVE_Template data =null;
			md.addAttribute("irrbbeve", data);
			md.addAttribute("formmode", "editeve");
		}

		else if ("editdiscount".equalsIgnoreCase(formmode)) {
			RT_IRRBB_Data_Discount_Rates data = IRRBB_Data_Template_DiscountRate_repo.getParticularDataBySI_NO(SI_NO);
			md.addAttribute("irrbbdiscount", data); // Same variable name used, if form is reused
			md.addAttribute("formmode", "editdiscount");
		}

		else if ("list".equalsIgnoreCase(formmode)) {
			List<RT_IRRBB_Data_EVE_Template> list = IRRB_EVE_Repo.getAlldetails();

			System.out.println("IRRBB EVE " + IRRB_EVE_Repo.getAlldetails().size());
			md.addAttribute("formmode", "list");
			md.addAttribute("ISList", list); // Used in HTML table
		} else if ("EAR".equalsIgnoreCase(formmode)) {
			System.out.println("THE EAR REPORT START");
			List<RT_IRRBB_Data_EAR> list = IRRBB_EAR_Repository.getAlldetails();
			System.out.println("IRRBB EAR " + IRRBB_EAR_Repository.getAlldetails().size());
			md.addAttribute("formmode", "EAR");
			md.addAttribute("ISListEar", list); // Used in HTML table
			System.out.println("Formmode" + formmode);
		} else if ("DiscountRate".equalsIgnoreCase(formmode)) {
			System.out.println("---- Discount Rate---");
			List<RT_IRRBB_Data_Discount_Rates> list = IRRBB_Data_Template_DiscountRate_repo.getAlldetails();
			System.out.println("IRRBB EAR " + IRRBB_Data_Template_DiscountRate_repo.getAlldetails().size());
			md.addAttribute("formmode", "DiscountRate");
			md.addAttribute("ISListDiscount", list); // Used in HTML table
			System.out.println("Formmode" + formmode);
		}
		else if ("Detaillist".equalsIgnoreCase(formmode)) {
			List<RT_IRRBB_Data_EVE_Template_Detail> list =rt_irrbb_data_eve_template_detail_rep.getAlldetails(reportDate,scenario,glLevel1,glLevel2,glLevel3,optionType,rateType,referenceRate,instrumentCurrency);
			System.out.println("IRRBB EVE " + list.size());
			md.addAttribute("formmode", "Detaillist");
			md.addAttribute("ISList", list); // Used in HTML table
		}

		/*
		 * else { model.addAttribute("formmode", "add");
		 * model.addAttribute("securityData", new
		 * RT_Investment_Securities_Data_Template()); }
		 */else {
			 Timestamp lastdatetimestamp = IRRB_EVE_Repo.findLastReportDate();
				Timestamp secondlastdatetimestamp = IRRB_EVE_Repo.findSecondLastReportDate();			
				LocalDate lastDate=lastdatetimestamp.toLocalDateTime().toLocalDate();		
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				String lastDateString = (lastdatetimestamp == null) ? null
						: lastdatetimestamp.toLocalDateTime().format(formatter);
				String secondLastDateString =(secondlastdatetimestamp == null) ? null
						:  secondlastdatetimestamp.toLocalDateTime().format(formatter);
				RT_DataControl data= RT_DatacontrolRepository.getdata(lastDateString,"CBUAE_IRRBB_Data_Template");
				RT_DataControl secondlastdata= RT_DatacontrolRepository.getdata(secondLastDateString,"CBUAE_IRRBB_Data_Template");
				if (data != null && !data.equals(null)) {
					md.addAttribute("data", data);
					md.addAttribute("formmode", "exist");
				}
				else if(secondlastdata != null && !secondlastdata.equals(null)){
					md.addAttribute("data", secondlastdata);
					md.addAttribute("formmode", "exist");
				}else {					
					
					md.addAttribute("formmode", "add");
					md.addAttribute("formmode", "null");
				}
				md.addAttribute("lastDate", lastDate);
				md.addAttribute("bankname", "Bank of Baroda");			 
		}

		List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
		List<RT_CountryRiskDropdown> countryList = countryRepo.findAllByOrderByCountryOfRiskAsc();

		md.addAttribute("bankList", bankList);
		md.addAttribute("countryList", countryList);
		return "RT/IRRBB_data_Template";
	}

	@RequestMapping(value = "/downloadForeigncurrencyExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadForeigncurrencyExcel(HttpServletRequest req) {
		logger.info("Controller: Received request for Foreign Currency Deposit Excel download.");

		try {
			byte[] excelData = foreigncurrencydepositService.generateForeignCurrencyDepositExcel();

			if (excelData.length == 0) {
				logger.warn("Controller: No data found for Foreign Currency report. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);
			
			String filename = "CBUAE_Cross_Currency_Funding_Spread_Template.xlsx";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			
			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "CROSS_CURRENCY_FUNDING_SPREAD_EXCEL", null, "BCBUAE_CROSS_CUR_FUNDING_FOREIGN_DEPOSITS");

			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (FileNotFoundException e) {
			logger.error("Controller ERROR: Foreign Currency Excel template file not found.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			logger.error("Controller ERROR: Unexpected error occurred during file generation.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@RequestMapping(value = "/downloadImpactanalysisExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadImpactanalysisExcel(HttpServletRequest req) {
		logger.info("Controller: Received request for Impact Analysis Excel download.");

		try {
			byte[] excelData = impactanalysisService.generateImpactAnalysisExcel();

			if (excelData.length == 0) {
				logger.warn("Controller: No data found for Impact Analysis report. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			String filename ="CBUAE_Cross_Currency_Funding_Spread_Template.xlsx";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			
			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "IMPACT_ANALYSIS_EXCEL", null, "BCBUAE_CROSS_CUR_FUNDING_IMPACT_ANALYSIS");

			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

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
			RT_TradeLevelDataDerivativesSimplified data = tradeleveldataderivativessimplifiedRepo
					.getParticularDataBySI_NO(SI_NO);
			md.addAttribute("tradeleveldataderivative", data);
			System.out.println("edit is formmode");
			md.addAttribute("formmode", "edit");

		} else if ("list".equalsIgnoreCase(formmode)) {
			md.addAttribute("branchList", tradeleveldataderivativessimplifiedRepo.getlist());
			System.out.println("list is formmode");
			md.addAttribute("formmode", "list");

		} else {
			Timestamp lastdatetimestamp = tradeleveldataderivativessimplifiedRepo.findLastReportDate();
			Timestamp secondlastdatetimestamp = tradeleveldataderivativessimplifiedRepo.findSecondLastReportDate();			
			LocalDate lastDate=(lastdatetimestamp == null) ? null
					: lastdatetimestamp.toLocalDateTime().toLocalDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String lastDateString = (lastdatetimestamp == null) ? null
					: lastdatetimestamp.toLocalDateTime().format(formatter);
			String secondLastDateString =(secondlastdatetimestamp == null) ? null
					:  secondlastdatetimestamp.toLocalDateTime().format(formatter);
			RT_DataControl data= RT_DatacontrolRepository.getdata(lastDateString,"CBUAE_Trade_Level_Data_Derivative_Simplified_Template");
			RT_DataControl secondlastdata= RT_DatacontrolRepository.getdata(secondLastDateString,"CBUAE_Trade_Level_Data_Derivative_Simplified_Template");
			if (data != null && !data.equals(null)) {
				md.addAttribute("data", data);
				md.addAttribute("formmode", "exist");
			}
			else if(secondlastdata != null && !secondlastdata.equals(null)){
				md.addAttribute("data", secondlastdata);
				md.addAttribute("formmode", "exist");
			}else {
				md.addAttribute("formmode", "add");
				md.addAttribute("formmode", "null");
			}
			md.addAttribute("lastDate", lastDate);
			md.addAttribute("bankname", "Bank of Baroda");



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
		boolean updated = tradeleveldataderivativesimplifiedService
				.updatetradeleveldataderivative(tradeleveldataderivative);

		if (updated) {
			return "Updated successfully";
		} else {
			return "Record not found for update";
		}

	}

	@RequestMapping(value = "/downloadTradeleveldataderivativesimplifiedExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadTradeleveldataderivativesimplifiedExcel(HttpServletRequest req) {
		logger.info("Controller: Received request for Trade Level Data Derivative Simplified Excel download.");

		try {
			byte[] excelData = tradeleveldataderivativesimplifiedService
					.generateTradeleveldataderivativesimplifiedExcel();

			if (excelData.length == 0) {
				logger.warn(
						"Controller: No data found for Trade Level Data Derivative Simplified report. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			String filename = "CBUAE_Trade_Level_Data_Derivative_Simplified_Template.xlsx";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			
			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "TRADE_LEVEL_DATA_DERIVATVE_SIMPLIFIED_EXCEL", null, "BCBUAE_TRADE_LEVEL_DERIVATIVES_SIMPLIFIED");

			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (FileNotFoundException e) {
			logger.error("Controller ERROR: Trade Level Data Derivative Simplified Excel template file not found.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			logger.error("Controller ERROR: Unexpected error occurred during file generation.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@RequestMapping(value = "/downloadInvestmentriskdatadashboardExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadInvestmentriskdatadashboardExcel(HttpServletRequest req) {
		logger.info("Controller: Received request for Investment Risk Data Dashboard Excel download.");

		try {
			byte[] excelData = investmentriskdatadictionaryService.generateInvestmentriskdataExcel();

			if (excelData.length == 0) {
				logger.warn(
						"Controller: No data found for Investment Risk Data Dashboard report. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			String filename = "CBUAE_Investment Risk Data_Dashboard_Template.xlsx";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			
			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "INVESTMENT_RISK_DATA_DASHBOARD_TEMPLATE_EXCEL", null, "BCBUAE_INVESTMENT_RISK_DATA_DASHBOARD_TEMPLATE");

			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (FileNotFoundException e) {
			logger.error("Controller ERROR: Investment Risk Data Dashboard Excel template file not found.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			logger.error("Controller ERROR: Unexpected error occurred during file generation.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/* iquidity risk Dashboard template */
	 @Autowired
	    private JdbcTemplate jdbcTemplate;
	private static final Map<String, byte[]> backgroundFileStore = new java.util.concurrent.ConcurrentHashMap<>();

	@RequestMapping(value = "Liquidity_Risk_Dashboard_Template", method = RequestMethod.GET)
	public String Liquidity_Risk_Dashboard_Template(
	        @RequestParam(required = false) String SI_NO,
	        @RequestParam(required = false) String formmode, 
	        @RequestParam(required = false) String report_date,
	        @RequestParam(required = false) String rowid, @RequestParam(required = false) String tablename,
	        Model model) {

	    // 1. DETAIL SCREEN (Cash -> ROW101 / Due from Banks -> ROW102)
		if ("detail".equalsIgnoreCase(formmode)) {
	    	System.out.println("rowid : "+rowid);
	    	 String sql;
	    	
	    	if("BRF8_MAPPING_TABLE".equals(tablename) || tablename=="BRF8_MAPPING_TABLE") {
	    		sql = "SELECT CUST_ID, FORACID, ACCT_NAME, ACT_BALANCE_AMT_LC AS ACCT_BALANCE_LC, "
		                + "ROWIDTOCHAR(ROWID) AS RID, REPORT_DATE " 
		                + "FROM " + tablename  
		                + " WHERE TO_CHAR(REPORT_DATE, 'DD-MM-YYYY') = ? AND REPORT_LABEL_1 = ? ";
	    		
	    	}
	    	else {   	
	        sql = "SELECT CUST_ID, FORACID, ACCT_NAME, ACCT_BALANCE_LC, "
	                + "ROWIDTOCHAR(ROWID) AS RID, REPORT_DATE " 
	                + "FROM " + tablename  
	                + " WHERE TO_CHAR(REPORT_DATE, 'DD-MM-YYYY') = ? AND REPORT_LABLE_1 = ?";
	    	}
	        
			List<Map<String, Object>> allData = new ArrayList<>();
			String[] rowidarray = rowid.replace(" ", "").split(",");

			for (String singlerowid : rowidarray) {
				Object[] args = new Object[] { report_date, singlerowid.trim() };
				List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, args);

				allData.addAll(data);
			}
	        
	        model.addAttribute("reportdetails", allData);
	        model.addAttribute("formmode", "detail");
	        model.addAttribute("rowid", rowid);
	    }
	    // 2. LIST MODE (Main Dashboard Table)
	    else if ("list".equalsIgnoreCase(formmode)) {
	        model.addAttribute("branchList", LiquidityRiskDashboardRepo.getAlldetails());
	        model.addAttribute("formmode", "list");
	    } 
	    // 3. EDIT MODE
	    else if ("edit".equalsIgnoreCase(formmode) && SI_NO != null) {
	        RT_Liquidity_Risk_Dashboard_Template data = LiquidityRiskDashboardRepo.getParticularDataBySI_NO(SI_NO);
	        model.addAttribute("liquidityriskdashboard", data);
	        model.addAttribute("formmode", "edit");
	    }
	    // 4. ADD MODE (Default Data Controls)
	    else {
	    	Timestamp lastdatetimestamp = LiquidityRiskDashboardRepo.findLastReportDate();
			Timestamp secondlastdatetimestamp = LiquidityRiskDashboardRepo.findSecondLastReportDate();			
			LocalDate lastDate=lastdatetimestamp.toLocalDateTime().toLocalDate();		
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String lastDateString = (lastdatetimestamp == null) ? null
					: lastdatetimestamp.toLocalDateTime().format(formatter);
			String secondLastDateString =(secondlastdatetimestamp == null) ? null
					:  secondlastdatetimestamp.toLocalDateTime().format(formatter);
			RT_DataControl data= RT_DatacontrolRepository.getdata(lastDateString,"CBUAE_Liquidity_Risk_Dashboard_Template");
			RT_DataControl secondlastdata= RT_DatacontrolRepository.getdata(secondLastDateString,"CBUAE_Liquidity_Risk_Dashboard_Template");
			if (data != null && !data.equals(null)) {
				model.addAttribute("data", data);
				model.addAttribute("formmode", "exist");
			}
			else if(secondlastdata != null && !secondlastdata.equals(null)){
				model.addAttribute("data", secondlastdata);
				model.addAttribute("formmode", "exist");
			}else {				
				model.addAttribute("formmode", "null");
				model.addAttribute("formmode", "add");
			}
			model.addAttribute("lastDate", lastDate);
			model.addAttribute("bankname", "Bank of Baroda");
	    }
	    model.addAttribute("bankList", bankRepo.findAllByOrderByBankNameAsc());
	    return "RT/Liquidity_Risk_Dashboard_Template";
	}

	@GetMapping("/startBackgroundJob")
	@ResponseBody
	public String startBackgroundJob(@RequestParam String report_date, @RequestParam String rowid, @RequestParam String tablename) {
	    String jobId = UUID.randomUUID().toString();
	    new Thread(() -> {
	        try {
	        	System.out.println("rowid : " + rowid);

	        	String labelColumn = "BRF8_MAPPING_TABLE".equals(tablename) ? "REPORT_LABEL_1" : "REPORT_LABLE_1";
	        	String balanceColumn = "BRF8_MAPPING_TABLE".equals(tablename) ? "ACT_BALANCE_AMT_LC AS ACCT_BALANCE_LC" : "ACCT_BALANCE_LC";

	        	String sql = "SELECT CUST_ID, FORACID, ACCT_NAME, " + balanceColumn + ", "
	        	           + "ROWIDTOCHAR(ROWID) AS RID, REPORT_DATE "
	        	           + "FROM " + tablename
	        	           + " WHERE TO_CHAR(REPORT_DATE, 'DD-MM-YYYY') = ?";

	        	List<Map<String, Object>> allData = new ArrayList<>();

	        	if ("ALL".equalsIgnoreCase(rowid)) {
	        	    allData = jdbcTemplate.queryForList(sql, new Object[]{report_date});
	        	} else {
	        	    sql += " AND " + labelColumn + " = ?";
	        	    String[] rowidarray = rowid.replace(" ", "").split(",");
	        	    for (String singlerowid : rowidarray) {
	        	        Object[] args = new Object[]{report_date, singlerowid.trim()};
	        	        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, args);
	        	        allData.addAll(data);
	        	    }
	        	}

	        	if (allData.isEmpty()) {
	        	    backgroundFileStore.put(jobId, "NODATA".getBytes());
	        	    return;
	        	}

	            SXSSFWorkbook workbook = new SXSSFWorkbook(100);
	            Sheet sheet = workbook.createSheet("Detail");
	            ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();

	            /* --- STYLES --- */
	            Font headFont = workbook.createFont(); headFont.setBold(true);
	            CellStyle headStyle = workbook.createCellStyle(); headStyle.setFont(headFont);
	            setBorders(headStyle);

	            CellStyle dataStyle = workbook.createCellStyle(); setBorders(dataStyle);
	            
	            CellStyle dateStyle = workbook.createCellStyle(); setBorders(dateStyle);
	            dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd-MM-yyyy"));

	            /* --- HEADER --- */
	            String[] h = {"CUST ID", "ACCOUNT NO", "NAME", "BALANCE (AED)", "DATE"};
	            Row hr = sheet.createRow(0);
	            for(int i=0; i<h.length; i++) { Cell c = hr.createCell(i); c.setCellValue(h[i]); c.setCellStyle(headStyle); }

	            /* --- DATA --- */
				int rx = 1;
				for (Map<String, Object> m : allData) {
					Row r = sheet.createRow(rx++);
					Object custId = m.get("CUST_ID");
					fillCell(r, 0, custId != null ? custId.toString() : "", dataStyle);
					Object foracid = m.get("FORACID");
					fillCell(r, 1, foracid != null ? foracid.toString() : "", dataStyle);
					Object acctName = m.get("ACCT_NAME");
					fillCell(r, 2, acctName != null ? acctName.toString() : "", dataStyle);
					Cell c3 = r.createCell(3);
					Object balance = m.get("ACCT_BALANCE_LC");
					if (balance != null) {
						c3.setCellValue(Double.parseDouble(balance.toString()));
					}
					c3.setCellStyle(dataStyle);

					Cell c4 = r.createCell(4);
					Object reportDate = m.get("REPORT_DATE");
					if (reportDate instanceof Date) {
						c4.setCellValue((Date) reportDate);
					}
					c4.setCellStyle(dateStyle);
				}

	            for(int i=0; i<h.length; i++) sheet.autoSizeColumn(i);

	            ByteArrayOutputStream bos = new ByteArrayOutputStream();
	            workbook.write(bos);
	            backgroundFileStore.put(jobId, bos.toByteArray());
	            workbook.dispose();
	        } catch (Exception e) { e.printStackTrace(); }
	    }).start();
	    return jobId;
	}

	private void setBorders(CellStyle s) {
	    s.setBorderTop(BorderStyle.THIN); s.setBorderBottom(BorderStyle.THIN);
	    s.setBorderLeft(BorderStyle.THIN); s.setBorderRight(BorderStyle.THIN);
	}
	private void fillCell(Row r, int i, String v, CellStyle s) { Cell c = r.createCell(i); c.setCellValue(v); c.setCellStyle(s); }

	@GetMapping("/checkJobStatus")
	@ResponseBody
	public String checkJobStatus(@RequestParam String jobId) {
	    if (!backgroundFileStore.containsKey(jobId)) return "PROCESSING";
	    return new String(backgroundFileStore.get(jobId)).equals("NODATA") ? "ERROR" : "READY";
	}

	@GetMapping("/getBackgroundFile")
	public ResponseEntity<byte[]> getFile(@RequestParam String jobId) {
	    byte[] data = backgroundFileStore.remove(jobId);
	    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Detail_Report.xlsx").body(data);
	}

	@PostMapping("/updateliquidityriskdashboard")
	@ResponseBody
	public String updateliquidityriskdashboard(
			@ModelAttribute RT_Liquidity_Risk_Dashboard_Template liquidityriskdashboard) {
		boolean updated = liquidityriskdashboardService.updateliquidityriskdashboard(liquidityriskdashboard);

		if (updated) {
			return "Updated successfully";
		} else {
			return "Record not found for update";
		}

	}

	@RequestMapping(value = "/downloadLiquidityriskdashboardExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadLiquidityriskdashboardExcel(HttpServletRequest req) {
		logger.info("Controller: Received request for Liquidity Risk Dashboard Template Excel download.");

		try {
			byte[] excelData = liquidityriskdashboardService.generateLiquidityriskdashboardExcel();

			if (excelData.length == 0) {
				logger.warn(
						"Controller: No data found for Liquidity Risk Dashboard Template Excel report. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			String filename = "Liquidityriskdashboard.xls";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			
			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "LIQUIDITY_RISK_DASHBOARD_TEMPLATE_EXCEL", null, "BCBUAE_LIQUIDITY_RISK_DASHBOARD_TEMPLATE");

			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (FileNotFoundException e) {
			logger.error("Controller ERROR: Liquidity Risk Dashboard Template Excel template file not found.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			logger.error("Controller ERROR: Unexpected error occurred during file generation.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/updateirrbear")
	@ResponseBody
	public String updateirrbear(@ModelAttribute RT_IRRBB_Data_EAR irrbbear) {
		boolean updated = irrbbearService.updateirrbbear(irrbbear);

		if (updated) {
			return "Updated successfully";
		} else {
			return "Record not found for update";
		}

	}

	@PostMapping("/updateirrbeve")
	@ResponseBody
	public String updateirrbeve(@ModelAttribute RT_IRRBB_Data_EVE_Template irrbbeve) {
		boolean updated = irrbbeveService.updateirrbbeve(irrbbeve);

		if (updated) {
			return "Updated successfully";
		} else {
			return "Record not found for update";
		}

	}

	@PostMapping("/updateirrbdiscount")
	@ResponseBody
	public String updateirrbdiscount(@ModelAttribute RT_IRRBB_Data_Discount_Rates irrbbdiscount) {
		boolean updated = discountratesService.updateirrbbdiscount(irrbbdiscount);

		if (updated) {
			return "Updated successfully";
		} else {
			return "Record not found for update";
		}

	}

	@RequestMapping(value = "/downloadIrrbbeveExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadIrrbbeveExcel(HttpServletRequest req) {
		logger.info("Controller: Received request for IRRBB Data EVE Excel download.");

		try {
			byte[] excelData = irrbbeveService.generateIrrbbeveExcel();

			if (excelData.length == 0) {
				logger.warn("Controller: No data found for IRRBB Data EVE report. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			String filename = "IrrbbDataEVE.xlsx";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			
			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "IRRBB_DATA_TEMPLATE_EVE_EXCEL", null, "BCBUAE_IRRBB_DATA_TEMPLATE");

			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (FileNotFoundException e) {
			logger.error("Controller ERROR: IRRBB Data EVE Excel template file not found.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			logger.error("Controller ERROR: Unexpected error occurred during file generation.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@RequestMapping(value = "/downloadIrrbbExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadIrrbbExcel(HttpServletRequest req) {
		logger.info("Controller: Received request for IRRBB Data EVE Excel download.");

		try {
			byte[] excelData = irrbbeveService.generateIrrbbExcel();

			if (excelData.length == 0) {
				logger.warn("Controller: No data found for IRRBB Data EVE report. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			String filename = "IrrbbDataEVE.xlsx";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			
			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "IRRBB_DATA_TEMPLATE_EVE_EXCEL", null, "BCBUAE_IRRBB_DATA_TEMPLATE");

			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (FileNotFoundException e) {
			logger.error("Controller ERROR: IRRBB Data EVE Excel template file not found.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			logger.error("Controller ERROR: Unexpected error occurred during file generation.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	

	@RequestMapping(value = "/downloadIrrbbearExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadIrrbbearExcel(HttpServletRequest req) {
		logger.info("Controller: Received request for IRRBB Data EAR Excel download.");

		try {
			byte[] excelData = irrbbearService.generateIrrbbearExcel();

			if (excelData.length == 0) {
				logger.warn("Controller: No data found for IRRBB Data EAR report. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			String filename = "IrrbbDataEAR.xlsx";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			
			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "IRRBB_DATA_TEMPLATE_EAR_EXCEL", null, "BCBUAE_IRRBB_EAR");

			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (FileNotFoundException e) {
			logger.error("Controller ERROR: IRRBB Data EAR Excel template file not found.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			logger.error("Controller ERROR: Unexpected error occurred during file generation.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@RequestMapping(value = "/downloadIrrbbdiscountrateExcel", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadIrrbbdiscountrateExcel(HttpServletRequest req) {
		logger.info("Controller: Received request for IRRBB Data EVE Excel download.");

		try {
			byte[] excelData = discountratesService.generateIrrbbdiscountrateExcel();

			if (excelData.length == 0) {
				logger.warn(
						"Controller: No data found for IRRBB Data Discount Rate report. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			String filename = "IrrbbDataDiscountRate.xls";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);
			

			String userid = (String) req.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "DOWNLOAD", "IRRBB_DATA_TEMPLATE_DISCOUNT_RATE _EXCEL", null, "BCBUAE_IRRBB_DISCOUNT_RATES");

			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (FileNotFoundException e) {
			logger.error("Controller ERROR: IRRBB Data Discount Rate Excel template file not found.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			logger.error("Controller ERROR: Unexpected error occurred during file generation.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@RequestMapping(value = "RT", method = { RequestMethod.GET, RequestMethod.POST })
	public String RT(Model md, HttpServletRequest req) {
		List<RT_SLS_ENTITIES> slslist = rt_sls_repository.rtslslist();
		md.addAttribute("slslist", slslist);
		return "RT/RT_SLS";
	}

	@RequestMapping(value = "IRS", method = { RequestMethod.GET, RequestMethod.POST })
	public String IRS(Model md, HttpServletRequest req) {
		List<RT_IRS_ENTITY> irsList = RT_irs_repository.rtirslist();
		md.addAttribute("irsList", irsList);
		return "RT/RT_IRS";
	}

	@RequestMapping(value = "SLSREPORT", method = { RequestMethod.GET, RequestMethod.POST })
	public String SLSREPORT(@RequestParam(required = false) String currency,
			@RequestParam(required = false) String reportdate, @RequestParam(required = false) String formmode,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size,
			@RequestParam(required = false) String Rowid, Model md, HttpServletRequest req) {

		// Match the input format: 30/04/2025
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date reportdatefor = null;

		try {
			if (reportdate != null) {
				reportdatefor = dateFormat.parse(reportdate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			md.addAttribute("error", "Invalid date format. Expected dd/MM/yyyy");
			return "RT/RT_SLSREPORT";
		}

		if (formmode == null || formmode.equals("summary")) {
			// --- Summary Mode ---
			List<RT_SLS_ENTITIES> slslist = rt_sls_repository.rtslslistbydate(reportdatefor, currency);
			md.addAttribute("slslist", slslist);

			List<RT_SLS_ENTITIES> currencylist = rt_sls_repository.rtslslistonlydate(reportdatefor);
			md.addAttribute("currencylist", currencylist);

			md.addAttribute("currency", currency);
			md.addAttribute("reportdate", reportdate);
			md.addAttribute("formmode", "summary");

		} else if (formmode.equals("Detail")) {
			// --- Detail Mode ---
			int pageSize = size;
			int offset = page * pageSize;
			int totalRows=0;
			int totalPages=0;
			

			if (Rowid != null && !Rowid.isEmpty()) {
				
				
				String[] arrcurr=currency.split("_");
				List<RT_SLS_Detail_Enitity> slsdetaillist=null;
				String currency1=null;
				if(arrcurr[0].equals("All")) {
					currency1=arrcurr[4];
					totalRows = rt_sls_detail_repository.slsdetaillistcountROWID(reportdatefor, Rowid);
					totalPages = (int) Math.ceil((double) totalRows / pageSize);
					slsdetaillist = rt_sls_detail_repository.slsdetaillistrowid(reportdatefor,Rowid, offset, pageSize);
				}else if(arrcurr[0].equals("ONLY")) {
					currency1=arrcurr[1];
					totalRows = rt_sls_detail_repository.slsdetaillistcountROWID(reportdatefor, Rowid,currency1);
					totalPages = (int) Math.ceil((double) totalRows / pageSize);
					slsdetaillist = rt_sls_detail_repository.slsdetaillistrowid(reportdatefor,Rowid, currency1,offset, pageSize);
				}
				
				System.out.println(currency1);
				

				
				md.addAttribute("slsdetaillist", slsdetaillist);
				md.addAttribute("reportdate", reportdate);
				md.addAttribute("formmode", "Detail");
				md.addAttribute("pagination", "YES");
				md.addAttribute("currency", currency);
				md.addAttribute("currentPage", page);
				md.addAttribute("totalPages", totalPages);

			} else {
				// int totalRows = rt_sls_detail_repository.slsdetaillistcount(reportdatefor);
				
				String[] arrcurr=currency.split("_");
				List<RT_SLS_Detail_Enitity> slsdetaillist=null;
				String currency1=null;
				if(arrcurr[0].equals("All")) {
					currency1=arrcurr[4];
					totalRows = rt_sls_detail_repository.slsdetaillistcount(reportdatefor);
					totalPages = (int) Math.ceil((double) totalRows / pageSize);
					slsdetaillist = rt_sls_detail_repository.slsdetaillist(reportdatefor, offset, pageSize);
				}else if(arrcurr[0].equals("ONLY")) {
					currency1=arrcurr[1];
					totalRows = rt_sls_detail_repository.slsdetaillistcount(reportdatefor, currency1);
					totalPages = (int) Math.ceil((double) totalRows / pageSize);
					slsdetaillist = rt_sls_detail_repository.slsdetaillist(reportdatefor,currency1, offset, pageSize);
					 
				}
				System.out.println(currency);

				System.out.println("offset==" + offset);
				
				md.addAttribute("slsdetaillist", slsdetaillist);
				md.addAttribute("reportdate", reportdate);
				md.addAttribute("formmode", "Detail");
				md.addAttribute("currency", currency);
				md.addAttribute("pagination", "YES");
				md.addAttribute("currentPage", page);
				md.addAttribute("totalPages", totalPages);
			}
			
		}

		return "RT/RT_SLSREPORT";
	}

	@RequestMapping(value = "IRSREPORT", method = { RequestMethod.GET, RequestMethod.POST })
	public String IRSREPORT(@RequestParam String reportdate, @RequestParam String currency,
			@RequestParam(defaultValue = "summary") String formmode, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "100") int size, @RequestParam(required = false) String rowid, Model md) {

		Date reportDateFor;

		try {
			reportDateFor = new SimpleDateFormat("dd/MM/yyyy").parse(reportdate);
		} catch (Exception e) {
			md.addAttribute("error", "Invalid date format. Expected dd/MM/yyyy");
			return "RT/RT_IRSREPORT";
		}

		/* ===================== COMMON ATTRIBUTES ===================== */
		md.addAttribute("reportdate", reportdate);
		md.addAttribute("currency", currency);
		md.addAttribute("formmode", formmode);

		/* ===================== SUMMARY ===================== */
		if ("summary".equalsIgnoreCase(formmode)) {

			List<RT_IRS_ENTITY> irslist = RT_irs_repository.rtirslistbydate(reportDateFor, currency);

			List<RT_IRS_ENTITY2> irsList2 = RT_IRS2_REPOSITORY.rtirslistbydate(reportDateFor, currency);

			List<RT_IRS_ENTITY> currencyList = RT_irs_repository.getIrsCurrencyByDate(reportDateFor);

			md.addAttribute("irslist", irslist);
			md.addAttribute("irsList2", irsList2);
			md.addAttribute("currencylist", currencyList);
		}

		/* ===================== DETAIL ===================== */
		else if ("Detail".equalsIgnoreCase(formmode)) {

			if (rowid != null && !rowid.trim().isEmpty()) {

				// 🔹 Detail by ROWID
				List<RT_IRS_DETAIL_ENTITY> detailsList = RT_IRS_DETAIL_REPO.IRSdetaillistrowid(reportDateFor, rowid);

				int totalCount = RT_IRS_DETAIL_REPO.IRSdetaillistcountROWID(reportDateFor, rowid);

				md.addAttribute("detailsList", detailsList);
				md.addAttribute("rowid", rowid);
				md.addAttribute("currentPage", page);
				md.addAttribute("totalPages", (int) Math.ceil((double) totalCount / size));
			} else {

				// 🔹 Normal paginated detail
				List<RT_IRS_DETAIL_ENTITY> detailsList = RT_IRS_DETAIL_REPO.irsdetaillist(reportDateFor, page, size);

				int totalCount = RT_IRS_DETAIL_REPO.IRSdetaillistcount(reportDateFor);

				md.addAttribute("detailsList", detailsList);
				md.addAttribute("pagination", "YES");
				md.addAttribute("currentPage", page);
				md.addAttribute("totalPages", (int) Math.ceil((double) totalCount / size));
			}
		}

		return "RT/RT_IRSREPORT"; // SAME SCREEN
	}

	@RequestMapping(value = "downloadExcel", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<ByteArrayResource> summaryDownload(HttpServletResponse response,
			@RequestParam("reportdate") String reportdate, @RequestParam("currency") String currency,
			@RequestParam("type") String type, @RequestParam("report") String report, // ✅ ADD THIS
			@RequestParam(value = "version", required = false) String version,
			@RequestParam(value = "filename", required = false) String filename) {

		response.setContentType("application/octet-stream");

		try {
			// ✅ Date format conversion
			DateFormat outFormat = new SimpleDateFormat("dd-MMM-yyyy");
			reportdate = outFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(reportdate));

			byte[] excelData = null;

			if ("summary".equalsIgnoreCase(type)) {

				if ("SLS".equalsIgnoreCase(report)) {
					excelData = RT_SLSServices.getSlsExcel(filename, reportdate, currency, version);

				} else if ("IRS".equalsIgnoreCase(report)) {
					excelData = rtIrsService.getIrsExcel(filename, reportdate, currency, version);
				}
			}

			// ✅ NO DATA
			if (excelData == null || excelData.length == 0) {
				logger.warn("No data available for Excel download");
				return ResponseEntity.noContent().build(); // 204
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + ".xls");

			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (Exception e) {
			logger.error("Excel download error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@RequestMapping(value = "downloaddetailExcel", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<ByteArrayResource> detailDownload(HttpServletResponse response,
			@RequestParam("jobId") String jobId, @RequestParam("filename") String filename,
			@RequestParam(value = "type", required = false) String type) { // Added type param

		try {
			byte[] excelData = null;

			// Routing logic based on type
			if ("IRS".equalsIgnoreCase(type)) {
				excelData = rtIrsService.getReport(jobId);
			} else {
				// Default to SLS if no type or type is SLS
				excelData = RT_SLSServices.getReport(jobId);
			}

			if (excelData == null || excelData.length == 0) {
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);
			HttpHeaders headers = new HttpHeaders();

			// Ensure filename has extension
			String finalFileName = filename.endsWith(".xls") ? filename : filename + ".xls";

			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + finalFileName);

			return ResponseEntity.ok().headers(headers).contentLength(excelData.length)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);

		} catch (Exception e) {
			logger.error("Controller ERROR: Detail download failed", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@RequestMapping(value = "/startreport", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody // forces raw text instead of HTML view
	public String startReport(@RequestParam String filename, @RequestParam String reportdate,
			@RequestParam String currency, @RequestParam(value = "version", required = false) String version) {
		String jobId = UUID.randomUUID().toString();
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		System.out.println("startreport inside....");
		try {

			reportdate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(reportdate));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		RT_SLSServices.generateReportAsync(jobId, filename, reportdate, currency, version);
		return jobId;
	}

	// 2️⃣ Check if report is ready
	@RequestMapping(value = "/checkreport", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody // forces raw text instead of HTML view
	public ResponseEntity<String> checkReport(@RequestParam String jobId) {
		System.out.println(jobId);
		byte[] report = RT_SLSServices.getReport(jobId);
		System.out.println("Report generation completed for: " + jobId + "    the reoport   " + report);
		if (report == null) {
			return ResponseEntity.ok("PROCESSING");
		}
		return ResponseEntity.ok("READY");
	}

	@RequestMapping(value = "/startIRSDetailReport", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody // forces raw text instead of HTML view
	public String startIRSDetailReport(@RequestParam String filename, @RequestParam String reportdate,
			@RequestParam String currency, @RequestParam(value = "version", required = false) String version) {
		String jobId = UUID.randomUUID().toString();
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		try {

			reportdate = dateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse(reportdate));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		rtIrsService.generateReportAsync(jobId, filename, reportdate, currency, version);
		return jobId;
	}

	@RequestMapping(value = "/checkirsreport", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody // forces raw text instead of HTML view
	public ResponseEntity<String> checkirsreport(@RequestParam String jobId) {
		byte[] report = rtIrsService.getReport(jobId);
		// System.out.println("Report generation completed for: " + jobId);
		if (report == null) {
			return ResponseEntity.ok("PROCESSING");
		}
		return ResponseEntity.ok("READY");
	}
	///// Credit Risk Analysis Report

	@RequestMapping(value = "Credit_risk", method = { RequestMethod.GET, RequestMethod.POST })
	public String Credit_risk_analysis(@RequestParam(required = false) String formmode, Model md,
			@RequestParam(value = "Report_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date Report_date,
			HttpServletRequest req) {

		System.out.println("Initial Report date : " + Report_date);

		LocalDate today = LocalDate.now(); // Get today's date

		if (Report_date != null) {
			Report_date = java.sql.Date.valueOf(normalizeDate(Report_date.toString()));
		} else {
			Report_date = java.sql.Date.valueOf(today.minusDays(42));
		}

		System.out.println("After validation Todaydate : " + Report_date);

		if (formmode.equals("Portfolio_analysis")) {
			List<Object[]> portfolioData = RT_RWA_Fund_base_data_rep.Getbranchwiseportfolio(Report_date);
			List<RT_Chart_pojo> chartList = new ArrayList<>();

			for (Object[] row : portfolioData) {

				RT_Chart_pojo chart = new RT_Chart_pojo();

				chart.setClassification(row[0] != null ? row[0].toString() : "Un Mapped Accounts");

				chart.setExposurebal(row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO);

				chart.setExposureperc(row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO);

				System.out.println("Classification > " + row[0] + " and Exposure Balance > " + row[1]
						+ " Exposure Perc > " + row[2]);
				chartList.add(chart); // ✔ IMPORTANT
			}

			md.addAttribute("Report_date", Report_date);
			md.addAttribute("Analysistabledata", chartList);
			md.addAttribute("formmode", "Portfolio_analysis");
			md.addAttribute("menuname", "Total Portfolio Exposure");

		}

		return "RT/RT_Credit_Risk_analysis.html";
	}

	@RequestMapping(value = "CustomerGrp_Maintenance", method = { RequestMethod.GET, RequestMethod.POST })
	public String CustomerGrp_Maintenance(@RequestParam(required = false) String formmode, Model md,
			@RequestParam(value = "Group_id", required = false) String Group_id, HttpServletRequest req) {

		if (formmode.equals("list")) {

			List<Groupexp_cust_maintain_entity> Groupdetails = Groupexp_cust_maintain_rep.Getactivegroupdetails();
			md.addAttribute("Groupdetails", Groupdetails);
			md.addAttribute("formmode", "list");
			md.addAttribute("menuname", "Group Exposure - list");
		} else if (formmode.equals("add")) {
			md.addAttribute("Groupdetail", new Groupexp_cust_maintain_entity());
			md.addAttribute("formmode", "add");
			md.addAttribute("menuname", "Group Exposure - Add");
			md.addAttribute("customerList", RT_RWA_Fund_base_data_rep.getcustomerdetail());
		} else if (formmode.equals("edit")) {
			md.addAttribute("formmode", formmode);
			md.addAttribute("Groupdetail", Groupexp_cust_maintain_rep.Getgroupdetails(Group_id));
			md.addAttribute("customerList", RT_RWA_Fund_base_data_rep.getcustomerdetail());
			md.addAttribute("menuname", "Group Exposure - Modify");
		}

		return "RT/RT_Cust_group_exposure.html";
	}

	public static String normalizeDate(String input) {
		DateTimeFormatter targetFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// already in correct format?
		try {
			LocalDate.parse(input, targetFormat);
			return input; // return as-is
		} catch (Exception ignore) {
		}

		// try other known formats
		DateTimeFormatter[] formats = { DateTimeFormatter.ofPattern("dd/MM/yyyy"),
				DateTimeFormatter.ofPattern("MM-dd-yyyy"), DateTimeFormatter.ofPattern("dd-MM-yyyy"),
				DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH) // Tue Sep 30 00:00:00 GST
																							// 2025
		};

		for (DateTimeFormatter f : formats) {
			try {
				LocalDate date = LocalDate.parse(input, f);
				return date.format(targetFormat); // convert to yyyy-MM-dd
			} catch (Exception ignore) {
			}
		}

		throw new IllegalArgumentException("Invalid date format: " + input);
	}

	@Autowired
	private RT_SLS_BEHAVIOURAL_PER_SERVICES rtSlsService;

	@Autowired
	private RT_FX_Position_Service rtFxPositionService;

	@Autowired
	RwaDataUploadService rwaService;

	@Autowired
	private RT_ACPR_SERVICE acprService;

	@Autowired
	RT_MID_FX_DEAL_SERVICE rtmidFxDealservice;

	// Page for SLS
	@RequestMapping(value = "SLSUPLOAD", method = { RequestMethod.GET, RequestMethod.POST })
	public String getSlsPage(Model model) {
		return "RT_SLS_Upload";
	}

	// Page for FX
	@RequestMapping(value = "FXUPLOAD", method = { RequestMethod.GET, RequestMethod.POST })
	public String getFxPage(Model model) {
		return "RT_FX_Upload";
	}

	@GetMapping("/getUploadedDates")
	@ResponseBody
	public List<String> getUploadedDates(@RequestParam String reportType) {

	    if (reportType == null || reportType.trim().isEmpty()) {
	        return new ArrayList<>();
	    }

	    if ("MFD".equals(reportType)) {
	        return rtmidFxDealservice.getUploadedDates();
	    } else if ("SLS".equals(reportType)) {
	        return rtSlsService.getUploadedDates();
	    } else if ("ACPR".equals(reportType)) {
	        return acprService.getUploadedDates();
	    } else if ("ACPRNF".equals(reportType)) {
	        return acprService.getUploadedDate();
	    } else if ("RWAFUND".equals(reportType)) {
	        return rwaService.getUploadedFundDates();
	    } else if ("RWANONFUND".equals(reportType)) {
	        return rwaService.getUploadedNonFundDates();
	    } else if ("RWABILLDETAIL".equals(reportType)) {
	        return rwaService.getUploadedBillDates();
	    } else if ("FXP".equals(reportType)) {
	        return rtFxPositionService.getUploadedDates();
	    } else if ("TR_PLC".equals(reportType)) {
	        return rtmidFxDealservice.getTreasuryPlacementUploadedDates();
	    } else if ("TR_TB".equals(reportType)) {
	        return rtmidFxDealservice.getTreasuryTbUploadedDates();
	    } else if ("TR_SWD".equals(reportType)) {
	        return rtmidFxDealservice.getTreasurySwdUploadedDates();
	    } else if ("FWD_RVL".equals(reportType)) {
	        return rtmidFxDealservice.getForwardRevealUploadedDates();
	    } else if ("TR_INV_DEAL_DUMP".equals(reportType)) {
	        return rtinvestmentdealdatadump_service.getInvestmentDealDumpUploadedDates();
	    } else if ("plcdealdump".equals(reportType)) {
	        return rtinvestmentdealdatadump_service.getPlacementDealDumpUploadedDates();
	    } else if ("GAMDATADUMP".equals(reportType) || "ONLY_EAB_TABLE_DATA".equals(reportType)
	            || "TR_INV".equals(reportType)) {
	        return uploadMonitorService.getSuccessfulUploadDatesForReportType(reportType);
	    }

	    return new ArrayList<>();
	}
	@PostMapping("/commonUploadFile")
	@ResponseBody
	public ResponseEntity<String> commonUploadFile(
	        @RequestParam("file") MultipartFile file,
	        @RequestParam("reportType") String reportType,
	        @RequestParam(value = "fromDate", required = false) String fromDateStr,
	        @RequestParam("toDate") String toDateStr,
	        @RequestParam(value = "forceUpload", defaultValue = "false") boolean forceUpload,
	        HttpServletRequest request) {

	    String username = (String) request.getSession().getAttribute("USERNAME");
	    if (username == null)
	        username = "SYSTEM";

	    String uploadId = null;
	    try {

	        if (reportType != null && reportType.contains(",")) {
	            reportType = reportType.split(",")[0].trim();
	        }

	        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	        Date toDate = null;
	        Date fromDate = null;

	        if (toDateStr != null && !toDateStr.trim().isEmpty()) {
	            toDate = sdf.parse(toDateStr.trim());
	        } else {
	            return ResponseEntity.badRequest().body("Reference Date is required.");
	        }

	        if (fromDateStr != null && !fromDateStr.trim().isEmpty()) {
	            fromDate = sdf.parse(fromDateStr.trim());
	        }
	        logger.info("Initiating Common Upload. Type: {}, User: {}, Reference Date: {}", reportType, username, toDate);

	        if (file.isEmpty()) {
	            return ResponseEntity.badRequest().body("Please select a valid file.");
	        }

	        UploadMonitorEntity startedMonitor = uploadMonitorService.startUpload(
	                reportType, toDate, fromDate, file, username, forceUpload);
	        uploadId = startedMonitor.getUploadId();

	        String resultMsg = "";

	        if ("ACPR".equals(reportType)) {

	            resultMsg = acprService.uploadAcprFile(file, toDate, username, forceUpload);

	        } else if ("ACPRNF".equals(reportType)) {

	            resultMsg = acprService.uploadAcprnfFile(file, fromDate, toDate, username, forceUpload);

	        } else if ("RWAFUND".equals(reportType) || "RWANONFUND".equals(reportType)) {

	            resultMsg = rwaService.uploadRwaTextFile(file, reportType, toDate, forceUpload);

	        } else if ("FXP".equals(reportType)) {

	            resultMsg = rtFxPositionService.uploadFxData(file, fromDate, toDate, username);

	        } else if ("SLS".equals(reportType)) {

	            resultMsg = rtSlsService.processAndSaveFile(file, fromDate, toDate, username);

	        } else if ("MFD".equals(reportType)) {

	            resultMsg = rtmidFxDealservice.uploadMidFxDealData(file, toDate, username);

	        } else if ("RWABILLDETAIL".equals(reportType)) {

	            resultMsg = rwaService.Uploadrwadata(file, reportType, toDate, forceUpload);

	        }else if ("GAMDATADUMP".equals(reportType)) {

	            resultMsg = rwaService.UploadEabandGamdata(file, reportType, toDate);

	        } else if ("ONLY_EAB_TABLE_DATA".equals(reportType)) {

	            resultMsg = rwaService.UploadEabdata(file, reportType, toDate);

	        } else if ("TR_PLC".equals(reportType) || "TR_TB".equals(reportType) || "TR_SWD".equals(reportType)
	        		|| "FWD_RVL".equals(reportType)) {

	            resultMsg = rtmidFxDealservice.UploadTrplorTb(file, toDate, username,reportType); //Mid Service used as upload Service treasury details

	        }else if("TR_INV_DEAL_DUMP".equals(reportType) || "plcdealdump".equals(reportType)) {
	        	resultMsg =rtinvestmentdealdatadump_service.Uploadinvdump(file, toDate, username,reportType);
	        }
	        
	        else if("ECL".equals(reportType)) {
	        	resultMsg =ecldatauploadservice.uploadECLdata(file, toDate,forceUpload);
	        }
	        
	        else if("SMA".equals(reportType)) {
	        	resultMsg =SMAFileUploadService.SMAFileUpload(file, reportType, toDate, forceUpload);
	        }
	        else if("Provisioning".equals(reportType)) {
	        	resultMsg =ProvisioningFileUploadService.ProvisioningFileUpload(file, reportType, toDate, forceUpload);
	        }
	        else if("VARFILE".equals(reportType)) {
	        	resultMsg =varportfoliouploadservice.uploadVarPortfolio(file, reportType, toDate, forceUpload);
	        }
	        else {
	        	uploadMonitorService.completeFailure(uploadId, "Unsupported Report Type: " + reportType);
	            return ResponseEntity.badRequest().body("Unsupported Report Type: " + reportType);
	        }

	        Map<String, Long> metrics = extractUploadMetrics(resultMsg);
	        uploadMonitorService.completeSuccess(
	                uploadId,
	                resultMsg,
	                metrics.get("total"),
	                metrics.get("loaded"),
	                metrics.get("failed"));
	        return ResponseEntity.ok(resultMsg);

	    } catch (RuntimeException e) {
	        if (uploadId != null) {
	            uploadMonitorService.completeFailure(uploadId, e.getMessage());
	        }
	        return ResponseEntity.badRequest().body(e.getMessage());
	    } catch (Exception e) {
	        if (uploadId != null) {
	            uploadMonitorService.completeFailure(uploadId, e.getMessage());
	        }
	        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
	    }
	}

	@GetMapping("/upload-monitor/summary")
	@ResponseBody
	public ResponseEntity<?> getUploadMonitorSummary(
	        @RequestParam("reportType") String reportType,
	        @RequestParam(value = "reportDate", required = false) String reportDateStr,
	        @RequestParam(value = "fromDate", required = false) String fromDateStr,
	        @RequestParam(value = "toDate", required = false) String toDateStr) {
	    try {
	        Date reportDate = parseDdMmYyyyDate(reportDateStr);
	        Date fromDate = parseDdMmYyyyDate(fromDateStr);
	        Date toDate = parseDdMmYyyyDate(toDateStr);
	        return ResponseEntity.ok(uploadMonitorService.getSummary(reportType, reportDate, fromDate, toDate));
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("Error fetching summary: " + e.getMessage());
	    }
	}

	@GetMapping("/upload-monitor/history")
	@ResponseBody
	public ResponseEntity<?> getUploadMonitorHistory(
	        @RequestParam("reportType") String reportType,
	        @RequestParam(value = "reportDate", required = false) String reportDateStr,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "size", defaultValue = "10") int size) {
	    try {
	        Date reportDate = parseDdMmYyyyDate(reportDateStr);
	        return ResponseEntity.ok(uploadMonitorService.getHistory(reportType, reportDate, page, size));
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("Error fetching history: " + e.getMessage());
	    }
	}

	@GetMapping("/upload-monitor/details/{uploadId}")
	@ResponseBody
	public ResponseEntity<?> getUploadMonitorDetails(@PathVariable("uploadId") String uploadId) {
	    UploadMonitorEntity details = uploadMonitorService.getDetails(uploadId);
	    if (details == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Upload monitor record not found.");
	    }
	    return ResponseEntity.ok(details);
	}

	private Date parseDdMmYyyyDate(String value) throws ParseException {
	    if (value == null || value.trim().isEmpty()) {
	        return null;
	    }
	    return new SimpleDateFormat("dd-MM-yyyy").parse(value.trim());
	}

	private Map<String, Long> extractUploadMetrics(String message) {
	    Map<String, Long> metrics = new HashMap<String, Long>();
	    metrics.put("total", 0L);
	    metrics.put("loaded", 0L);
	    metrics.put("failed", 0L);
	    if (message == null || message.trim().isEmpty()) {
	        return metrics;
	    }

	    Long total = extractMetricValue(message, "(?i)(total\\s*(records|rows)?|records\\s*total)\\D*(\\d+)");
	    Long loaded = extractMetricValue(message, "(?i)(loaded|inserted|processed\\s*success|success\\s*count)\\D*(\\d+)");
	    Long failed = extractMetricValue(message, "(?i)(failed|rejected|error\\s*count)\\D*(\\d+)");

	    if (total != null) {
	        metrics.put("total", total);
	    }
	    if (loaded != null) {
	        metrics.put("loaded", loaded);
	    }
	    if (failed != null) {
	        metrics.put("failed", failed);
	    }
	    if (metrics.get("total") == 0L && (metrics.get("loaded") > 0 || metrics.get("failed") > 0)) {
	        metrics.put("total", metrics.get("loaded") + metrics.get("failed"));
	    }
	    return metrics;
	}

	private Long extractMetricValue(String message, String regex) {
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(message);
	    if (!matcher.find()) {
	        return null;
	    }

	    for (int i = matcher.groupCount(); i >= 1; i--) {
	        String group = matcher.group(i);
	        if (group != null && group.matches("\\d+")) {
	            return Long.parseLong(group);
	        }
	    }
	    return null;
	}

	@RequestMapping(value = "rt-sls/downloadTemplate", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadTemplate() {
		logger.info("Generating dynamic SLS Excel Template");

		try {
			byte[] excelContent = rtSlsService.generateSlsTemplate();
			ByteArrayResource resource = new ByteArrayResource(excelContent);

			return ResponseEntity.ok()
					.contentType(MediaType
							.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"SLS_Scorecard_Template.xlsx\"")
					.body(resource);

		} catch (Exception e) {
			logger.error("Error generating template", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@RequestMapping(value = "/getAdhocDetails", method = RequestMethod.GET)
	@ResponseBody
	public List<MIS_COUNTER_PARTY_LIMIT_DETAILS_ENTITY> getAdhocDetails(@RequestParam String srlNo) {
		return misCounterPartyLimitDetailsRepo.findBySrlNo(srlNo);
	}

	// 2. Updated Save Details with Alert Logic
	@RequestMapping(value = "/saveDetailsRecord", method = RequestMethod.POST)
	@ResponseBody
	public String saveDetailsRecord(@RequestParam("adhocDetailsJson") String json, @RequestParam("srlNo") String srlNo,
			@RequestParam("bankName") String bankName, HttpServletRequest request) {
		try {
			String userId = (String) request.getSession().getAttribute("USER_ID");
			if (userId == null)
				userId = "SYSTEM";

			ObjectMapper mapper = new ObjectMapper();
			List<MIS_COUNTER_PARTY_LIMIT_DETAILS_ENTITY> details = mapper.readValue(json,
					new TypeReference<List<MIS_COUNTER_PARTY_LIMIT_DETAILS_ENTITY>>() {
					});

			// Delete existing details for this SrlNo to perform a clean update
			List<MIS_COUNTER_PARTY_LIMIT_DETAILS_ENTITY> existing = misCounterPartyLimitDetailsRepo.findBySrlNo(srlNo);
			if (!existing.isEmpty()) {
				misCounterPartyLimitDetailsRepo.deleteAll(existing);
			}

			if (details != null) {
				for (MIS_COUNTER_PARTY_LIMIT_DETAILS_ENTITY detail : details) {
					detail.setSrlNo(srlNo);
					detail.setCounterPartyBank(bankName);
					detail.setCreateUser(userId);
					detail.setCreateTime(new Date());
					detail.setEntityFlg("N");
					detail.setModifyFlg("N");
					detail.setDelFlg("N");
					misCounterPartyLimitDetailsRepo.save(detail);
				}
			}
			return "SUCCESS";
		} catch (Exception e) {
			return "ERROR: " + e.getMessage();
		}
	}

	@Autowired
	MIS_SBLC_Maintenance_Repo sblcRepo;

	@RequestMapping(value = "/sblc_maintenance", method = RequestMethod.GET)
	public String sblcMaint(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) BigDecimal id, Model model) {

		String mode = (formmode == null) ? "list" : formmode;
		model.addAttribute("formmode", mode);

		if (mode.equals("list")) {
			model.addAttribute("listall", sblcRepo.getalllist());
		} else if (mode.equals("add")) {
			model.addAttribute("nextId", sblcRepo.getNextId());
			// Add an empty entity to the model for form binding
			model.addAttribute("list", new MIS_SBLC_Maintenance_Entity());
		} else if (mode.equals("edit") || mode.equals("view")) {
			model.addAttribute("list", sblcRepo.findById(id).orElse(new MIS_SBLC_Maintenance_Entity()));
		}
		return "MIS/SBLC_Maintenance";
	}

	@RequestMapping(value = "/sblc_save", method = RequestMethod.POST)
	@ResponseBody
	public String sblcSave(@ModelAttribute("list") MIS_SBLC_Maintenance_Entity sblc, @RequestParam String formmode,
			@RequestParam(required = false) BigDecimal id, // ID comes from query param for delete
			HttpSession session) {

		String user = (String) session.getAttribute("USERID");

		// Case 1: DELETE (Soft Delete)
		if ("delete".equals(formmode)) {
			if (id == null)
				return "ID is required for deletion";
			Optional<MIS_SBLC_Maintenance_Entity> existing = sblcRepo.findById(id);
			if (existing.isPresent()) {
				MIS_SBLC_Maintenance_Entity entity = existing.get();
				entity.setDel_flg("Y");
				entity.setModify_user(user);
				entity.setModify_time(new Date());
				sblcRepo.save(entity);
				return "Record Deleted Successfully";
			}
			return "Record Not Found";
		}

		// Case 2: ADD
		if ("add".equals(formmode)) {
			sblc.setCreate_user(user);
			sblc.setCreate_time(new Date());
			sblc.setDel_flg("N");
			sblcRepo.save(sblc);
			return "Record Added Successfully";
		}

		// Case 3: EDIT
		if ("edit".equals(formmode)) {
			// In JPA, save() updates if the ID property is set in the entity
			sblc.setModify_user(user);
			sblc.setModify_time(new Date());
			sblc.setDel_flg("N"); // Ensure flag stays N
			sblcRepo.save(sblc);
			return "Record Modified Successfully";
		}

		return "Invalid Request";
	}
	
	@RequestMapping(value = "/ASLCheck",  method = RequestMethod.GET)
	public String ASLCheckS(@RequestParam(required = false) String formmode,Model md,HttpServletRequest request)  {
		String roleId = (String) request.getSession().getAttribute("ROLEID");
		String BRANCHCODE = (String) request.getSession().getAttribute("BRANCHCODE");
		md.addAttribute("roleId", roleId);
		md.addAttribute("branchcode", BRANCHCODE);
		if(formmode==null||formmode.equals("ASLCheck")) {
			md.addAttribute("menu", "Current Bank Limit - ASL");
			md.addAttribute("formmode", "ASLChecklist");
		}else if(formmode.equals("Branchrequestadd")) {
			md.addAttribute("menu", "Limit Request - ASL");
			md.addAttribute("formmode", "Branchrequestadd");
			md.addAttribute("currentDate", new Date());
		}else if(formmode.equals("Branchrequestlistactive")) {
			md.addAttribute("menu", "Active Limit Request List - ASL");
			md.addAttribute("formmode", "Branchrequestlistactive");
			md.addAttribute("Requestlist", limit_request_rep.getAllLimitRequestList());
			
		}else if(formmode.equals("Branchrequestlisthistory")) {
			md.addAttribute("menu", "History Limit Request List - ASL");
			md.addAttribute("formmode", "Branchrequestlisthistory");
			md.addAttribute("Requestlist", limit_request_rep.getAllLimitRequestListhis());
			
			
		}
		
		List<BankLimit_Entity> BankLimitdata =banklimit_rep.getallbanknamemaxdate();
		List<Limit_Request_Entity> tempreqlimit=limit_request_rep.getAllLimitRequestList();
		for(Limit_Request_Entity reqlimit:tempreqlimit) {
			BankLimit_Entity tembanklimitdata=new BankLimit_Entity();
			String RequestType =reqlimit.getRequestType();
			if(RequestType.equals("TradeFinance")) {
				tembanklimitdata.setTrade_finance_exposure(reqlimit.getProposedAmt());
				tembanklimitdata.setBank_name(reqlimit.getBankName());
			}
			else if(RequestType.equals("MoneyMarket")) {
				tembanklimitdata.setMoney_market_exposure(reqlimit.getProposedAmt());
				tembanklimitdata.setBank_name(reqlimit.getBankName());
			}else if(RequestType.equals("SettlementLimit")) {
				tembanklimitdata.setSettlement_exposure(reqlimit.getProposedAmt());
				tembanklimitdata.setBank_name(reqlimit.getBankName());
			}
			
			BankLimitdata.add(tembanklimitdata);
		}
		
		md.addAttribute("BankLimitdata",BankLimitdata);
	return "RT/RT_ASLCheck";
		
	}
	
	@RequestMapping(value = "AddLimitRequest", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String AddLimitRequest(@ModelAttribute Limit_Request_Entity Limit_Request_Entity,
			@RequestParam(required = false) String formmode, Model md, HttpServletRequest rq) {
		logger.info("Add counter party...");
		String userid = (String) rq.getSession().getAttribute("USERID");
		String msg = counter_servicess.AddLimitRequest(Limit_Request_Entity, userid, formmode);
		return msg;

	}
	
	@RequestMapping(value = "LRDT_loansAdvancesConvertedAed", method = RequestMethod.GET)
	public String Liquidity_Risk_Dashboard_Template_loansAdvancesConvertedAed(
	        @RequestParam(required = false) String report_date, @RequestParam(required = false) String formmode, 
	        @RequestParam(required = false) String rowcurrency, 
	        Model model) {

	    List<Map<String, Object>> allData = new ArrayList<>();

	    boolean filterByCurrency = rowcurrency != null && !"ALL".equalsIgnoreCase(rowcurrency.trim());

	    String tablename1 = "BRF7_MAPPING_TABLE";
	    String rowid1 = "ROW101,ROW102";
	    
	    String sql1 = "SELECT CUST_ID, FORACID, ACCT_NAME, ACT_BALANCE_AMT_LC AS ACCT_BALANCE_LC, "
	            + "ROWIDTOCHAR(ROWID) AS RID, REPORT_DATE FROM " + tablename1
	            + " WHERE TO_CHAR(REPORT_DATE, 'DD-MM-YYYY') = ? AND REPORT_LABEL_1 = ? ";

	    if (filterByCurrency) {
	        sql1 += " AND ACCT_CRNCY_CODE = ?";
	    }

	    String[] rowidarray1 = rowid1.replace(" ", "").split(",");
	    for (String singlerowid : rowidarray1) {
	        Object[] args = filterByCurrency 
	            ? new Object[] { report_date, singlerowid.trim(), rowcurrency.trim() }
	            : new Object[] { report_date, singlerowid.trim() };
	            
	        allData.addAll(jdbcTemplate.queryForList(sql1, args));
	    }


	    String tablename2 = "brf9_detailtable";
	    String rowid2 = "ROW108";
	    String report_crit2 = "ROW108G,ROW108H,ROW108I,ROW108J";
	    
	    String sql2 = "SELECT CUST_ID, FORACID, ACCT_NAME, ACT_BALANCE_AMT_LC AS ACCT_BALANCE_LC, "
	            + "ROWIDTOCHAR(ROWID) AS RID, REPORT_DATE FROM " + tablename2
	            + " WHERE TO_CHAR(REPORT_DATE, 'DD-MM-YYYY') = ? AND REPORT_LABEL_1 = ? AND report_addl_criteria_1 = ? ";
	    
	    if (filterByCurrency) {
	        sql2 += " AND ACCT_CRNCY_CODE = ?";
	    }

	    String[] report_critarray2 = report_crit2.replace(" ", "").split(",");
	    for (String singlereport_crit : report_critarray2) {
	        Object[] args = filterByCurrency
	            ? new Object[] { report_date, rowid2, singlereport_crit.trim(), rowcurrency.trim() }
	            : new Object[] { report_date, rowid2, singlereport_crit.trim() };
	            
	        allData.addAll(jdbcTemplate.queryForList(sql2, args));
	    }

	    String tablename3 = "brf9_detailtable";
	    String rowid3 = "ROW106";
	    String report_crit3 = "ROW106G,ROW106H,ROW106I,ROW106J";

	    String sql3 = "SELECT CUST_ID, FORACID, ACCT_NAME, ACT_BALANCE_AMT_LC AS ACCT_BALANCE_LC, "
	            + "ROWIDTOCHAR(ROWID) AS RID, REPORT_DATE FROM " + tablename3
	            + " WHERE TO_CHAR(REPORT_DATE, 'DD-MM-YYYY') = ? AND REPORT_LABEL_1 = ? AND report_addl_criteria_1 = ? ";
	    
	    if (filterByCurrency) {
	        sql3 += " AND ACCT_CRNCY_CODE = ?";
	    }

	    String[] report_critarray3 = report_crit3.replace(" ", "").split(",");
	    for (String singlereport_crit : report_critarray3) {
	        Object[] args = filterByCurrency
	            ? new Object[] { report_date, rowid3, singlereport_crit.trim(), rowcurrency.trim() }
	            : new Object[] { report_date, rowid3, singlereport_crit.trim() };
	            
	        allData.addAll(jdbcTemplate.queryForList(sql3, args));
	    }


	    model.addAttribute("reportdetails", allData);
	    model.addAttribute("formmode", formmode);

	    return "RT/Liquidity_Risk_Dashboard_Template";
	}

	@RequestMapping(value = "LRDT_stableResourcesConvertedAed", method = RequestMethod.GET)
	public String Liquidity_Risk_Dashboard_Template_stableResourcesConvertedAed(
			@RequestParam(required = false) String report_date, @RequestParam(required = false) String formmode,
			@RequestParam(required = false) String rowcurrency, Model model) {

		List<Map<String, Object>> allData = new ArrayList<>();

		boolean filterByCurrency = rowcurrency != null && !"ALL".equalsIgnoreCase(rowcurrency.trim());

		String tablename1 = "brf2_detailtable";
		String rowid1 = "ROW151";

		String sql1 = "SELECT CUST_ID, FORACID, ACCT_NAME, ACT_BALANCE_AMT_LC AS ACCT_BALANCE_LC, "
				+ "ROWIDTOCHAR(ROWID) AS RID, REPORT_DATE FROM " + tablename1
				+ " WHERE TO_CHAR(REPORT_DATE, 'DD-MM-YYYY') = ? AND REPORT_LABEL_1 = ? ";

		if (filterByCurrency) {
			sql1 += " AND ACCT_CRNCY_CODE = ?";
		}

		String[] rowidarray1 = rowid1.replace(" ", "").split(",");
		for (String singlerowid : rowidarray1) {
			Object[] args = filterByCurrency ? new Object[] { report_date, singlerowid.trim(), rowcurrency.trim() }
					: new Object[] { report_date, singlerowid.trim() };

			allData.addAll(jdbcTemplate.queryForList(sql1, args));
		}

		String tablename2 = "BRF7_MAPPING_TABLE";
		String rowid2 = "ROW111";

		String sql2 = "SELECT CUST_ID, FORACID, ACCT_NAME, ACT_BALANCE_AMT_LC AS ACCT_BALANCE_LC, "
				+ "ROWIDTOCHAR(ROWID) AS RID, REPORT_DATE FROM " + tablename2
				+ " WHERE TO_CHAR(REPORT_DATE, 'DD-MM-YYYY') = ? AND REPORT_LABEL_1 = ? ";

		if (filterByCurrency) {
			sql2 += " AND ACCT_CRNCY_CODE = ?";
		}

		String[] rowidarray2 = rowid2.replace(" ", "").split(",");
		for (String singlerowid : rowidarray2) {
			Object[] args = filterByCurrency ? new Object[] { report_date, singlerowid.trim(), rowcurrency.trim() }
					: new Object[] { report_date, singlerowid.trim() };

			allData.addAll(jdbcTemplate.queryForList(sql2, args));
		}

		String tablename3 = "brf9_detailtable";
		String rowid3 = "ROW143";
		String report_crit3 = "ROW143H,ROW143I,ROW143J";

		String sql3 = "SELECT CUST_ID, FORACID, ACCT_NAME, ACT_BALANCE_AMT_LC AS ACCT_BALANCE_LC, "
				+ "ROWIDTOCHAR(ROWID) AS RID, REPORT_DATE FROM " + tablename3
				+ " WHERE TO_CHAR(REPORT_DATE, 'DD-MM-YYYY') = ? AND REPORT_LABEL_1 = ? AND report_addl_criteria_1 = ? ";

		if (filterByCurrency) {
			sql3 += " AND ACCT_CRNCY_CODE = ?";
		}

		String[] report_critarray3 = report_crit3.replace(" ", "").split(",");
		for (String singlereport_crit : report_critarray3) {
			Object[] args = filterByCurrency
					? new Object[] { report_date, rowid3, singlereport_crit.trim(), rowcurrency.trim() }
					: new Object[] { report_date, rowid3, singlereport_crit.trim() };

			allData.addAll(jdbcTemplate.queryForList(sql3, args));
		}

		rowid3 = "ROW149";
		report_crit3 = "ROW149H,ROW149I,ROW149J";

		report_critarray3 = report_crit3.replace(" ", "").split(",");
		for (String singlereport_crit : report_critarray3) {
			Object[] args = filterByCurrency
					? new Object[] { report_date, rowid3, singlereport_crit.trim(), rowcurrency.trim() }
					: new Object[] { report_date, rowid3, singlereport_crit.trim() };

			allData.addAll(jdbcTemplate.queryForList(sql3, args));
		}
		rowid3 = "ROW150";
		report_crit3 = "ROW150H";

		report_critarray3 = report_crit3.replace(" ", "").split(",");
		for (String singlereport_crit : report_critarray3) {
			Object[] args = filterByCurrency
					? new Object[] { report_date, rowid3, singlereport_crit.trim(), rowcurrency.trim() }
					: new Object[] { report_date, rowid3, singlereport_crit.trim() };

			allData.addAll(jdbcTemplate.queryForList(sql3, args));
		}

		rowid3 = "ROW151";
		report_crit3 = "ROW151H,ROW151I,ROW151J";

		report_critarray3 = report_crit3.replace(" ", "").split(",");
		for (String singlereport_crit : report_critarray3) {
			Object[] args = filterByCurrency
					? new Object[] { report_date, rowid3, singlereport_crit.trim(), rowcurrency.trim() }
					: new Object[] { report_date, rowid3, singlereport_crit.trim() };

			allData.addAll(jdbcTemplate.queryForList(sql3, args));
		}

		rowid3 = "ROW152";
		report_crit3 = "ROW152H,ROW152I,ROW152J";

		report_critarray3 = report_crit3.replace(" ", "").split(",");
		for (String singlereport_crit : report_critarray3) {
			Object[] args = filterByCurrency
					? new Object[] { report_date, rowid3, singlereport_crit.trim(), rowcurrency.trim() }
					: new Object[] { report_date, rowid3, singlereport_crit.trim() };

			allData.addAll(jdbcTemplate.queryForList(sql3, args));
		}

		String tablename4 = "brf9_laa_cash_flow";
		String rowid4 = "ROW131";

		String[] columns = { "SIX_MON_12_MON", "ZERO_7_DAYS", "EIGHT_14_DAYS", "FIFTEEN_DAYS_1_MON", "ONE_MON_3_MON",
				"THREE_MON_6_MON" };

		String[] rowidarray4 = rowid4.replace(" ", "").split(",");

		for (String column : columns) {

			String sql = "SELECT CUST_ID, ACCT_NUMBER AS FORACID, ACCT_NAME, " + column + " AS ACCT_BALANCE_LC, "
					+ "ROWIDTOCHAR(ROWID) AS RID, REPORT_DATE FROM " + tablename4
					+ " WHERE TO_CHAR(REPORT_DATE, 'DD-MM-YYYY') = ? AND REPORT_LABEL = ?";

			if (filterByCurrency) {
				sql += " AND ACCT_CRNCY_CODE = ?";
			}

			for (String singlerowid : rowidarray4) {

				Object[] args = filterByCurrency ? new Object[] { report_date, singlerowid.trim(), rowcurrency.trim() }
						: new Object[] { report_date, singlerowid.trim() };

				allData.addAll(jdbcTemplate.queryForList(sql, args));
			}
		}

		model.addAttribute("reportdetails", allData);
		model.addAttribute("formmode", formmode);

		return "RT/Liquidity_Risk_Dashboard_Template";
	}
	
	
	@RequestMapping(value = "RT_MC_Reports", method = RequestMethod.GET)
	public String RT_MC_Reports(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String branch,@RequestParam(required = false) String deptvalid, Model md, HttpServletRequest req) {

		String BRANCHCODE = (String) req.getSession().getAttribute("BRANCHCODE");
		String ROLEID = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("ROLEID", ROLEID);
		System.out.println("ROLEID : " + ROLEID);
		if (deptvalid == null) {
		    deptvalid = "NO";
		}
		System.out.println("DEPARTMENT VALIDATION : " + deptvalid);

		String DEPARTMENT = (String) req.getSession().getAttribute("DEPARTMENT");
		md.addAttribute("DEPARTMENT", DEPARTMENT);
		System.out.println("DEPARTMENT : " + DEPARTMENT);

		if (branch == null || branch.isEmpty()) {
			branch = BRANCHCODE;
			md.addAttribute("BRANCHCODE", BRANCHCODE);
		} else {
			md.addAttribute("BRANCHCODE", branch);
		}
		List<String> dropdownOptions = Arrays.asList("IT", "Risk", "HR", "Operations");
		md.addAttribute("md", dropdownOptions);
		System.out.println("branch : " + branch);

		if ("bankinformation".equalsIgnoreCase(formmode) || formmode == null || "null".equalsIgnoreCase(formmode)) {
			if (deptvalid == "YES" || deptvalid.equals("YES")) {
				List<RT_MC_TABLE1_ENTITY> reportlist = RT_MC_TABLE1_REPO.findBybranchcode("DEPT");
				
				System.out.println("size : " + reportlist.size());
				md.addAttribute("reportlist", reportlist);
				md.addAttribute("DEPARTMENTVALIDATION", "YES");
			} else {
				List<RT_MC_TABLE1_ENTITY> reportlist = RT_MC_TABLE1_REPO.findBybranchcode(branch);
				System.out.println("size : " + reportlist.size());
				md.addAttribute("reportlist", reportlist);
				
				List<RT_MC_TABLE1_ENTITY> deptreportlist = RT_MC_TABLE1_REPO.findBybranchcode("DEPT");
				
				if(deptreportlist==null || deptreportlist.isEmpty()) {
					System.out.println("Report Date : "+formatDate(reportlist.get(0).getREPORT_DATE()));
					executeprocedure("RT_MC_TABLE1_PROCEDURE('" + formatDate(reportlist.get(0).getREPORT_DATE()) + "', 'DEPT')");
					deptreportlist = RT_MC_TABLE1_REPO.findBybranchcode("DEPT");
				}
				
				System.out.println("size : " + deptreportlist.size());
				md.addAttribute("deptreportlist", deptreportlist.get(0));
				
				md.addAttribute("DEPARTMENTVALIDATION", "NO");
			}
			md.addAttribute("formmode", "bankinformation");
		}

		else if ("bankconsumers".equalsIgnoreCase(formmode)) {
			if (deptvalid == "YES" || deptvalid.equals("YES")) {
				List<RT_MC_TABLE2_1_ENTITY> reportlist1 = RT_MC_TABLE2_1_REPO.findBybranchcode("DEPT");
				List<RT_MC_TABLE2_2_ENTITY> reportlist2 = RT_MC_TABLE2_2_REPO.findBybranchcode("DEPT");

				md.addAttribute("reportlist1", reportlist1);
				md.addAttribute("reportlist2", reportlist2);
				md.addAttribute("DEPARTMENTVALIDATION", "YES");

			} else {

				List<RT_MC_TABLE2_1_ENTITY> reportlist1 = RT_MC_TABLE2_1_REPO.findBybranchcode(branch);
				List<RT_MC_TABLE2_2_ENTITY> reportlist2 = RT_MC_TABLE2_2_REPO.findBybranchcode(branch);
				md.addAttribute("reportlist1", reportlist1);
				md.addAttribute("reportlist2", reportlist2);

				List<RT_MC_TABLE2_1_ENTITY> deptreportlist1 = RT_MC_TABLE2_1_REPO.findBybranchcode("DEPT");
				List<RT_MC_TABLE2_2_ENTITY> deptreportlist2 = RT_MC_TABLE2_2_REPO.findBybranchcode("DEPT");
				
				if(deptreportlist1==null || deptreportlist1.isEmpty()) {
					//System.out.println("Report Date : "+formatDate(reportlist.get(0).getREPORT_DATE()));
					executeprocedure("RT_MC_TABLE2_PROCEDURE('" + formatDate(reportlist1.get(0).getREPORT_DATE()) + "', 'DEPT')");
					deptreportlist1 = RT_MC_TABLE2_1_REPO.findBybranchcode("DEPT");
					deptreportlist2 = RT_MC_TABLE2_2_REPO.findBybranchcode("DEPT");
				}
				
				md.addAttribute("deptreportlist", deptreportlist1.get(0));
				md.addAttribute("deptreportlist2", deptreportlist2.get(0));
				md.addAttribute("DEPARTMENTVALIDATION", "NO");

			}

			md.addAttribute("formmode", "bankconsumers");
		}

		else if ("complaints".equalsIgnoreCase(formmode)) {
			List<RT_MC_TABLE3_ENTITY> reportlist = RT_MC_TABLE3_REPO.findBybranchcode(branch);
			System.out.println("Branch : " + branch);
			System.out.println("TABLE3 Size : " + reportlist.size());
			for (RT_MC_TABLE3_ENTITY r : reportlist) {
				System.out.println(r.getR6_BANK());
			}
			md.addAttribute("reportlist", reportlist);
			md.addAttribute("formmode", "complaints");
		} else if ("retailproducts".equalsIgnoreCase(formmode)) {
			List<RT_MC_TABLE4_1_ENTITY> reportlist = RT_MC_TABLE4_1_REPO.findBybranchcode(branch);
			System.out.println("Branch : " + branch);
			System.out.println("TABLE4 Size : " + reportlist.size());
			List<RT_MC_TABLE4_2_ENTITY> reportlist1 = RT_MC_TABLE4_2_REPO.findBybranchcode(branch);
			md.addAttribute("reportlist", reportlist);
			md.addAttribute("reportlist1", reportlist1);
			md.addAttribute("formmode", "retailproducts");
		} else if ("bankemployee".equalsIgnoreCase(formmode)) {
			List<RT_MC_TABLE5_ENTITY> reportlist = RT_MC_TABLE5_REPO.findBybranchcode(branch);
			System.out.println("size : " + reportlist.size());
			md.addAttribute("reportlist", reportlist);
			md.addAttribute("formmode", "bankemployee");
		} else if ("trainings".equalsIgnoreCase(formmode)) {
			List<RT_MC_TABLE6_ENTITY> reportlist = RT_MC_TABLE6_REPO.findBybranchcode(branch);
			System.out.println("size : " + reportlist.size());
			md.addAttribute("reportlist", reportlist);
			md.addAttribute("formmode", "trainings");
		} else if ("additionalinformation".equalsIgnoreCase(formmode)) {
			List<RT_MC_TABLE7_1_ENTITY> reportlist1 = RT_MC_TABLE7_1_REPO.findBybranchcode(branch);
			List<RT_MC_TABLE7_2_ENTITY> reportlist2 = RT_MC_TABLE7_2_REPO.findBybranchcode(branch);
			md.addAttribute("reportlist1", reportlist1);
			md.addAttribute("reportlist2", reportlist2);
			md.addAttribute("formmode", "additionalinformation");
		} else if ("islamicbanking".equalsIgnoreCase(formmode)) {
			List<RT_MC_TABLE8_ENTITY> reportlist = RT_MC_TABLE8_REPO.findBybranchcode(branch);
			System.out.println("size : " + reportlist.size());
			md.addAttribute("reportlist", reportlist);
			md.addAttribute("formmode", "islamicbanking");
		} else if ("conductcultureassessment".equalsIgnoreCase(formmode)) {
			List<RT_MC_TABLE9_ENTITY> reportlist = RT_MC_TABLE9_REPO.findBybranchcode(branch);
			System.out.println("size : " + reportlist.size());
			md.addAttribute("reportlist", reportlist);
			md.addAttribute("formmode", "conductcultureassessment");
		}

		return "RBS_MC_Reports";
	}
	public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

	
	public void executeprocedure(String procString) {        
        String sql = "{call " + procString + "}";         
        jdbcTemplate.execute(sql);
    }


	@PostMapping("/saveMcReport_bankinformation")
	@ResponseBody
	public ResponseEntity<String> saveReport_bankinformation(@ModelAttribute RT_MC_TABLE1_ENTITY reportData,
			HttpServletRequest req) {

		try {
			System.out.println("branch: " + reportData.getBRANCH_CODE());
			String userid = (String) req.getSession().getAttribute("USERID");
			rT_MC_TABLE_Service.MC_TABLE1_Modify(reportData);
			reportData.setMODIFY_USERID(userid);
			RT_MC_TABLE1_REPO.save(reportData);
			return ResponseEntity.ok("Success");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving data");
		}
	}

	@PostMapping("/verifyMcReport_bankinformation")
	@ResponseBody
	public ResponseEntity<String> verifyReport_bankinformation(@ModelAttribute RT_MC_TABLE1_ENTITY reportData,
			HttpServletRequest req) {
		try {
			String userid = (String) req.getSession().getAttribute("USERID");
			if (reportData.getMODIFY_USERID() == userid || userid.equals(reportData.getMODIFY_USERID())) {
				return ResponseEntity.status(500).body("Same User can not Verify");
			} else {
				reportData.setVERIFY_FLG("Y");
				reportData.setVERIFY_USERID(userid);
				auditService.createBusinessAudit(reportData.getREPORT_DATE() + " - " + reportData.getBRANCH_CODE(), "VERIFY", "RBS_MC_TABLE1_Bank_Information", null,"RT_MC_TABLE1");
				RT_MC_TABLE1_REPO.save(reportData);
				return ResponseEntity.ok("Verified");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error verifying data");
		}
	}

	@PostMapping("/saveMcReport_bankconsumers")
	@ResponseBody
	public ResponseEntity<String> saveReport_bankconsumers(@ModelAttribute RT_MC_TABLE2_1_ENTITY reportData1,
			RT_MC_TABLE2_2_ENTITY reportData2, HttpServletRequest req) {

		try {
			System.out.println("branch: " + reportData1.getBRANCH_CODE());
			String userid = (String) req.getSession().getAttribute("USERID");
			rT_MC_TABLE_Service.MC_TABLE2_1_Modify(reportData1);
			rT_MC_TABLE_Service.MC_TABLE2_2_Modify(reportData2);
			reportData1.setMODIFY_USERID(userid);
			reportData2.setMODIFY_USERID(userid);
			RT_MC_TABLE2_1_REPO.save(reportData1);
			RT_MC_TABLE2_2_REPO.save(reportData2);
			return ResponseEntity.ok("Success");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving data");
		}
	}

	@PostMapping("/verifyMcReport_bankconsumers")
	@ResponseBody
	public ResponseEntity<String> verifyReport_bankconsumers(@ModelAttribute RT_MC_TABLE2_1_ENTITY reportData1,
			RT_MC_TABLE2_2_ENTITY reportData2, HttpServletRequest req) {
		try {
			String userid = (String) req.getSession().getAttribute("USERID");
			if (reportData1.getMODIFY_USERID() == userid || userid.equals(reportData1.getMODIFY_USERID())) {
				return ResponseEntity.status(500).body("Same User can not Verify");
			} else {
				reportData1.setVERIFY_FLG("Y");
				reportData1.setVERIFY_USERID(userid);
				reportData2.setVERIFY_FLG("Y");
				reportData2.setVERIFY_USERID(userid);
				auditService.createBusinessAudit(reportData1.getREPORT_DATE() + " - " + reportData1.getBRANCH_CODE(), "VERIFY", "RBS_MC_TABLE2_Bank_Consumers", null,"RT_MC_TABLE2_1");
				auditService.createBusinessAudit(reportData2.getREPORT_DATE() + " - " + reportData2.getBRANCH_CODE(), "VERIFY", "RBS_MC_TABLE2_Bank_Consumers", null,"RT_MC_TABLE2_2");
				RT_MC_TABLE2_1_REPO.save(reportData1);
				RT_MC_TABLE2_2_REPO.save(reportData2);
				return ResponseEntity.ok("Verified");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error verifying data");
		}
	}

	@PostMapping("/saveMcReport_bankemployee")
	@ResponseBody
	public ResponseEntity<String> saveReport_bankemployee(@ModelAttribute RT_MC_TABLE5_ENTITY reportData,
			HttpServletRequest req) {

		try {
			System.out.println("branch: " + reportData.getBRANCH_CODE());
			String userid = (String) req.getSession().getAttribute("USERID");
			reportData.setMODIFY_USERID(userid);
			rT_MC_TABLE_Service.MC_TABLE5_Modify(reportData);
			RT_MC_TABLE5_REPO.save(reportData);

			return ResponseEntity.ok("Success");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving data");
		}
	}

	@PostMapping("/verifyMcReport_bankemployee")
	@ResponseBody
	public ResponseEntity<String> verifyReport_bankemployee(@ModelAttribute RT_MC_TABLE5_ENTITY reportData,
			HttpServletRequest req) {
		try {
			String userid = (String) req.getSession().getAttribute("USERID");
			if (reportData.getMODIFY_USERID() == userid || userid.equals(reportData.getMODIFY_USERID())) {
				return ResponseEntity.status(500).body("Same User can not Verify");
			} else {
				reportData.setVERIFY_FLG("Y");
				reportData.setVERIFY_USERID(userid);
				auditService.createBusinessAudit(reportData.getREPORT_DATE() + " - " + reportData.getBRANCH_CODE(), "VERIFY", "RBS_MC_TABLE5_Bank_Employee", null,"RT_MC_TABLE5");
				RT_MC_TABLE5_REPO.save(reportData);
				return ResponseEntity.ok("Verified");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error verifying data");
		}
	}

	@PostMapping("/saveMcReport_trainings")
	@ResponseBody
	public ResponseEntity<String> saveReport_trainings(@ModelAttribute RT_MC_TABLE6_ENTITY reportData,
			HttpServletRequest req) {

		try {
			System.out.println("branch: " + reportData.getBRANCH_CODE());
			String userid = (String) req.getSession().getAttribute("USERID");
			reportData.setMODIFY_USERID(userid);
			rT_MC_TABLE_Service.MC_TABLE6_Modify(reportData);
			RT_MC_TABLE6_REPO.save(reportData);

			return ResponseEntity.ok("Success");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving data");
		}
	}

	@PostMapping("/verifyMcReport_trainings")
	@ResponseBody
	public ResponseEntity<String> verifyReport_trainings(@ModelAttribute RT_MC_TABLE6_ENTITY reportData,
			HttpServletRequest req) {
		try {
			String userid = (String) req.getSession().getAttribute("USERID");
			if (reportData.getMODIFY_USERID() == userid || userid.equals(reportData.getMODIFY_USERID())) {
				return ResponseEntity.status(500).body("Same User can not Verify");
			} else {
				reportData.setVERIFY_FLG("Y");
				reportData.setVERIFY_USERID(userid);
				auditService.createBusinessAudit(reportData.getREPORT_DATE() + " - " + reportData.getBRANCH_CODE(), "VERIFY", "RBS_MC_TABLE6_Trainings", null,"RT_MC_TABLE6");
				RT_MC_TABLE6_REPO.save(reportData);
				return ResponseEntity.ok("Verified");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error verifying data");
		}
	}
	

	@PostMapping("/saveMcReport_complaints")
	@ResponseBody
	public ResponseEntity<String> saveReport_complaints(@ModelAttribute RT_MC_TABLE3_ENTITY reportData,
			HttpServletRequest req) {
		try {
			System.out.println("branch: " + reportData.getBRANCH_CODE());
			String userid = (String) req.getSession().getAttribute("USERID");
			reportData.setMODIFY_USERID(userid);
			rT_MC_TABLE_Service.MC_TABLE3_Modify(reportData);
			RT_MC_TABLE3_REPO.save(reportData);
			return ResponseEntity.ok("Success");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving data");
		}
	}

	@PostMapping("/verifyMcReport_complaints")
	@ResponseBody
	public ResponseEntity<String> verifyReport_complaints(@ModelAttribute RT_MC_TABLE3_ENTITY reportData,
			HttpServletRequest req) {
		try {
			String userid = (String) req.getSession().getAttribute("USERID");
			if (reportData.getMODIFY_USERID() == userid || userid.equals(reportData.getMODIFY_USERID())) {
				return ResponseEntity.status(500).body("Same User can not Verify");
			} else {
				reportData.setVERIFY_FLG("Y");
				reportData.setVERIFY_USERID(userid);
				auditService.createBusinessAudit(reportData.getREPORT_DATE() + " - " + reportData.getBRANCH_CODE(), "VERIFY", "RBS_MC_TABLE3_Complaints", null,"RT_MC_TABLE3");
				RT_MC_TABLE3_REPO.save(reportData);
				return ResponseEntity.ok("Verified");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error verifying data");
		}
	}
	
	@PostMapping("/saveMcReport_retailproducts")
	@ResponseBody
	public ResponseEntity<String> saveReport_retailproducts(@ModelAttribute RT_MC_TABLE4_1_ENTITY reportData1,
			RT_MC_TABLE4_2_ENTITY reportData2, HttpServletRequest req) {

		try {
			System.out.println("branch: " + reportData1.getBRANCH_CODE());
			System.out.println("R6_BANK: " + reportData1.getR6_BANK());
			String userid = (String) req.getSession().getAttribute("USERID");
			rT_MC_TABLE_Service.MC_TABLE4_1_Modify(reportData1);
			rT_MC_TABLE_Service.MC_TABLE4_2_Modify(reportData2);
			reportData1.setMODIFY_USERID(userid);
			reportData2.setMODIFY_USERID(userid);
			RT_MC_TABLE4_1_REPO.save(reportData1);
			RT_MC_TABLE4_2_REPO.save(reportData2);
			return ResponseEntity.ok("Success");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving data");
		}
	}

	@PostMapping("/verifyMcReport_retailproducts")
	@ResponseBody
	public ResponseEntity<String> verifyReport_retailproducts(@ModelAttribute RT_MC_TABLE4_1_ENTITY reportData1,
			RT_MC_TABLE4_2_ENTITY reportData2, HttpServletRequest req) {
		try {
			String userid = (String) req.getSession().getAttribute("USERID");
			if (reportData1.getMODIFY_USERID() == userid || userid.equals(reportData1.getMODIFY_USERID())) {
				return ResponseEntity.status(500).body("Same User can not Verify");
			} else {
				reportData1.setVERIFY_FLG("Y");
				reportData1.setVERIFY_USERID(userid);
				reportData2.setVERIFY_FLG("Y");
				reportData2.setVERIFY_USERID(userid);
				auditService.createBusinessAudit(reportData1.getREPORT_DATE() + " - " + reportData1.getBRANCH_CODE(), "VERIFY", "RBS_MC_TABLE4_Retail_Products", null,"RT_MC_TABLE4_1");
				auditService.createBusinessAudit(reportData2.getREPORT_DATE() + " - " + reportData2.getBRANCH_CODE(), "VERIFY", "RBS_MC_TABLE4_Retail_Products", null,"RT_MC_TABLE2_2");
				RT_MC_TABLE4_1_REPO.save(reportData1);
				RT_MC_TABLE4_2_REPO.save(reportData2);

				return ResponseEntity.ok("Verified");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error verifying data");
		}
	}
	
	@PostMapping("/saveMcReport_islamicbanking")
	@ResponseBody
	public ResponseEntity<String> saveReport_islamicbanking(@ModelAttribute RT_MC_TABLE8_ENTITY reportData,
			HttpServletRequest req) {

		try {
			System.out.println("branch: " + reportData.getBRANCH_CODE());
			String userid = (String) req.getSession().getAttribute("USERID");
			reportData.setMODIFY_USERID(userid);
			rT_MC_TABLE_Service.MC_TABLE8_Modify(reportData);
			RT_MC_TABLE8_REPO.save(reportData);
			return ResponseEntity.ok("Success");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving data");
		}
	}

	@PostMapping("/verifyMcReport_islamicbanking")
	@ResponseBody
	public ResponseEntity<String> verifyReport_islamicbanking(@ModelAttribute RT_MC_TABLE8_ENTITY reportData,
			HttpServletRequest req) {
		try {
			String userid = (String) req.getSession().getAttribute("USERID");
			if (reportData.getMODIFY_USERID() == userid || userid.equals(reportData.getMODIFY_USERID())) {
				return ResponseEntity.status(500).body("Same User can not Verify");
			} else {
				reportData.setVERIFY_FLG("Y");
				reportData.setVERIFY_USERID(userid);
				auditService.createBusinessAudit(reportData.getREPORT_DATE() + " - " + reportData.getBRANCH_CODE(), "VERIFY", "RBS_MC_TABLE8_Islamic_Banking", null,"RT_MC_TABLE8");
				RT_MC_TABLE8_REPO.save(reportData);
				return ResponseEntity.ok("Verified");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error verifying data");
		}
	}
	@PostMapping("/saveMcReport_additionalinformation")
	@ResponseBody
	public ResponseEntity<String> saveReport_additionalinformation(@ModelAttribute RT_MC_TABLE7_1_ENTITY reportData1,
			RT_MC_TABLE7_2_ENTITY reportData2, HttpServletRequest req) {

		try {
			System.out.println("branch: " + reportData1.getBRANCH_CODE());
			String userid = (String) req.getSession().getAttribute("USERID");
			rT_MC_TABLE_Service.MC_TABLE7_1_Modify(reportData1);
			rT_MC_TABLE_Service.MC_TABLE7_2_Modify(reportData2);
			reportData1.setMODIFY_USERID(userid);
			reportData2.setMODIFY_USERID(userid);
			RT_MC_TABLE7_1_REPO.save(reportData1);
			RT_MC_TABLE7_2_REPO.save(reportData2);
			return ResponseEntity.ok("Success");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving data");
		}
	}

	@PostMapping("/verifyMcReport_additionalinformation")
	@ResponseBody
	public ResponseEntity<String> verifyReport_additionalinformation(@ModelAttribute RT_MC_TABLE7_1_ENTITY reportData1,
			RT_MC_TABLE7_2_ENTITY reportData2, HttpServletRequest req) {
		try {
			String userid = (String) req.getSession().getAttribute("USERID");
			if (reportData1.getMODIFY_USERID() == userid || userid.equals(reportData1.getMODIFY_USERID())) {
				return ResponseEntity.status(500).body("Same User can not Verify");
			} else {
				reportData1.setVERIFY_FLG("Y");
				reportData1.setVERIFY_USERID(userid);
				reportData2.setVERIFY_FLG("Y");
				reportData2.setVERIFY_USERID(userid);
				auditService.createBusinessAudit(reportData1.getREPORT_DATE() + " - " + reportData1.getBRANCH_CODE(), "VERIFY", "RBS_MC_TABLE7_Additional_Information", null,"RT_MC_TABLE7_1");
				auditService.createBusinessAudit(reportData2.getREPORT_DATE() + " - " + reportData2.getBRANCH_CODE(), "VERIFY", "RBS_MC_TABLE7_Additional_Information", null,"RT_MC_TABLE7_2");
				RT_MC_TABLE7_1_REPO.save(reportData1);
				RT_MC_TABLE7_2_REPO.save(reportData2);
				return ResponseEntity.ok("Verified");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error verifying data");
		}
	}
	@PostMapping("/saveMcReport_conductcultureassessment")
	@ResponseBody
	public ResponseEntity<String> saveReport_conductcultureassessment(@ModelAttribute RT_MC_TABLE9_ENTITY reportData,
			HttpServletRequest req) {

		try {
			System.out.println("branch: " + reportData.getBRANCH_CODE());
			String userid = (String) req.getSession().getAttribute("USERID");
			reportData.setMODIFY_USERID(userid);
			rT_MC_TABLE_Service.MC_TABLE9_Modify(reportData);
			RT_MC_TABLE9_REPO.save(reportData);
			return ResponseEntity.ok("Success");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving data");
		}
	}

	@PostMapping("/verifyMcReport_conductcultureassessment")
	@ResponseBody
	public ResponseEntity<String> verifyReport_conductcultureassessment(@ModelAttribute RT_MC_TABLE9_ENTITY reportData,
			HttpServletRequest req) {
		try {
			String userid = (String) req.getSession().getAttribute("USERID");
			if (reportData.getMODIFY_USERID() == userid || userid.equals(reportData.getMODIFY_USERID())) {
				return ResponseEntity.status(500).body("Same User can not Verify");
			} else {
				reportData.setVERIFY_FLG("Y");
				reportData.setVERIFY_USERID(userid);
				auditService.createBusinessAudit(reportData.getREPORT_DATE() + " - " + reportData.getBRANCH_CODE(), "VERIFY", "RBS_MC_TABLE9_Conduct_Culture_Assessment", null,"RT_MC_TABLE9");
				RT_MC_TABLE9_REPO.save(reportData);
				return ResponseEntity.ok("Verified");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error verifying data");
		}
	}
	
	private final Map<String, byte[]> newTaskFileStore = new ConcurrentHashMap<>();
	private final Map<String, Integer> newTaskProgress = new ConcurrentHashMap<>();

	@GetMapping("/startMcReportJob")
	@ResponseBody
	public String startMcReportJob(@RequestParam("branch") String branch,@RequestParam("formmode") String formmode,HttpServletRequest req) {
		String jobId = UUID.randomUUID().toString();
		newTaskProgress.put(jobId, 0);
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		new Thread(() -> {
			try {				
				String userid = (String) req.getSession().getAttribute("USERID");
				byte[] fileData = rT_MC_TABLE_Service.generateReportFile(branch, jobId, newTaskProgress,formmode,userid,attr);
				System.out.println("File : "+((fileData==null)?"fail":"pass"));
				System.out.println("Formmode : "+formmode);
				newTaskFileStore.put(jobId, fileData);
				newTaskProgress.put(jobId, 100);
			} catch (Exception e) {
				e.printStackTrace();
				newTaskProgress.put(jobId, -1);
			}
		}).start();

		return jobId;
	}


	@GetMapping("/checkNewTaskStatus")
	@ResponseBody
	public Map<String, Object> checkNewTaskStatus(@RequestParam String jobId) {
		Map<String, Object> response = new HashMap<>();
		response.put("percent", newTaskProgress.getOrDefault(jobId, 0));
		return response;
	}

	@GetMapping("/getNewTaskFile")
	public ResponseEntity<byte[]> getNewTaskFile(@RequestParam String jobId, @RequestParam String fileName) {
		byte[] fileData = newTaskFileStore.remove(jobId);

		if (fileData == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(fileData);
	}
	
	@RequestMapping(value = "RT_Key_Risk_Indicator", method = RequestMethod.GET)
	public String RT_Key_Risk_Indicator(
	        @RequestParam(required = false) String formmode,
	        @RequestParam(required = false) String Report_date,
	        @RequestParam(required = false) String branch,
	        Model md, HttpServletRequest req) {

	    String ROLEID = (String) req.getSession().getAttribute("ROLEID");
	    md.addAttribute("roleId", ROLEID);

	    String DEPARTMENT = (String) req.getSession().getAttribute("DEPARTMENT");
	    md.addAttribute("DEPARTMENT", DEPARTMENT);
	    System.out.println("DEPARTMENT="+DEPARTMENT);

	    if (formmode == null || "null".equalsIgnoreCase(formmode)) {

	        md.addAttribute("menu", "Key Risk Indicator");

	        List<KriMasterTable> krslist = new ArrayList<>();

	        try {
	            Date reportDateObj = null;
	            

	            if (Report_date != null && !Report_date.isEmpty()) {
	                reportDateObj = new SimpleDateFormat("yyyy-MM-dd").parse(Report_date);
	            }

	            krslist = krimastertablerep.getalllist(reportDateObj);
	           
	            if(krslist.size()>0) {
	            	 md.addAttribute("formmode", "edit");
	            }
	            else {
	            	md.addAttribute("formmode", "list");
	            }
	            
	            md.addAttribute("Report_date",reportDateObj);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        md.addAttribute("krsdata", krslist);
	       
	       
	        
	    }

	    return "RT/keyRiskIndicator";
	}
	
	
	
	
	@PostMapping("/saveKriData")
	@ResponseBody
	public String saveKriData(@RequestBody List<KriMasterTable> list, HttpServletRequest req) {
		String userid = (String) req.getSession().getAttribute("USERID");
		for(KriMasterTable krs:list) {
			krs.setDelFlg("N");
			krs.setVerifyFlg("N");
			krs.setCreateUser(userid);
		}
		krimastertablerep.saveAll(list);
		System.out.println("saveKriData");
	    return "Key Risk Indicator Add Sucessfully";
	}
	
	@PostMapping("/modifyKriData")
	@ResponseBody
	public String modifyKriData(@RequestBody List<KriMasterTable> list, HttpServletRequest req) {
		String userid = (String) req.getSession().getAttribute("USERID");
		for(KriMasterTable krs:list) {
			krs.setDelFlg("N");
			krs.setVerifyFlg("N");
			krs.setModifyUser(userid);
			krs.setModifyFlg("Y");
		}
		krimastertablerep.saveAll(list);
		System.out.println("saveKriData");
		return "Key Risk Indicator Modified Successfully";
	}

	
	@PostMapping("/verifyKriData")
	@ResponseBody
	public String verifyKriData(
	        @RequestParam(required = false) String Report_date,
	        HttpServletRequest req) {

	    String userid = (String) req.getSession().getAttribute("USERID");

	    try {
	        Date reportDateObj = null;

	        if (Report_date != null && !Report_date.isEmpty()) {
	            reportDateObj = new SimpleDateFormat("yyyy-MM-dd").parse(Report_date);
	        }

	        List<KriMasterTable> krslist = krimastertablerep.getalllist(reportDateObj);

	        if (krslist != null && !krslist.isEmpty()) {

	            for (KriMasterTable krs : krslist) {
	                krs.setVerifyFlg("Y"); 
	                krs.setVerifyUser(userid);
	                krs.setVerifyTime(new Date());
	            }

	            krimastertablerep.saveAll(krslist);

	            return "Key Risk Indicator verified Sucessfully";
	        } else {
	            return "NO DATA FOUND";
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "ERROR";
	    }
	}
	
	@PostMapping("/saveInvestmentLimits")
	public ResponseEntity<?> saveData(
	        @RequestBody RtVarReportLimits data,
	        @RequestParam(defaultValue = "false") boolean confirmReplace) {

	    Optional<RtVarReportLimits> existing =rtvarreportlimits_rep.findById(data.getReportDate());

	    if (existing.isPresent() && !confirmReplace) {
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body("Record already exists for this date");
	    }

	    data.setEntryDate(new Date());
	    data.setEntryFlg("Y");

	    rtvarreportlimits_rep.save(data);

	    return ResponseEntity.ok("Limits Saved Successfully");
	}
	
	

}