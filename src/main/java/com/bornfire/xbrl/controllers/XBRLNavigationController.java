package com.bornfire.xbrl.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.bornfire.xbrl.entities.Capitaladequacyratio_rep;
import com.bornfire.xbrl.entities.Counterparty_Entity;
import com.bornfire.xbrl.entities.Counterparty_Rep;
import com.bornfire.xbrl.entities.Groupexp_cust_maintain_entity;
import com.bornfire.xbrl.entities.Groupexp_cust_maintain_rep;
import com.bornfire.xbrl.entities.MIS_COUNTER_PARTY_LIMIT_DETAILS_ENTITY;
import com.bornfire.xbrl.entities.MIS_COUNTER_PARTY_LIMIT_DETAILS_REPO;
import com.bornfire.xbrl.entities.MIS_SBLC_Maintenance_Entity;
import com.bornfire.xbrl.entities.MIS_SBLC_Maintenance_Repo;
import com.bornfire.xbrl.entities.MIS_SETTLEMENT_ENTITY;
import com.bornfire.xbrl.entities.MIS_SETTLEMENT_ENTITY_REP;
import com.bornfire.xbrl.entities.MIS_TREASURY_LIMITS_ENTITY;
import com.bornfire.xbrl.entities.MIS_TREASURY_LIMITS_ENTITY_REP;
import com.bornfire.xbrl.entities.MIS_TREASURY_PLACEMENT_ENTITY;
import com.bornfire.xbrl.entities.Mis_exposure_bill_detail_entity;
import com.bornfire.xbrl.entities.Mis_exposure_bill_detail_rep;
import com.bornfire.xbrl.entities.Mis_treasury_placement_repo;
import com.bornfire.xbrl.entities.RT_BankNameMaster;
import com.bornfire.xbrl.entities.RT_BankNameMasterRepository;
import com.bornfire.xbrl.entities.RT_CCR_DATA_TEMPLATE;
import com.bornfire.xbrl.entities.RT_CCR_DATA_TEMPLATE_REPOSITORY;
import com.bornfire.xbrl.entities.RT_Chart_pojo;
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
import com.bornfire.xbrl.entities.RT_IRRBB_Data_EVE_Template;
import com.bornfire.xbrl.entities.RT_IRRBB_Data_EVE_Template_Repository;
import com.bornfire.xbrl.entities.RT_IRS2_REPOSITORY;
import com.bornfire.xbrl.entities.RT_IRS_DETAIL_ENTITY;
import com.bornfire.xbrl.entities.RT_IRS_DETAIL_REPO;
import com.bornfire.xbrl.entities.RT_IRS_ENTITY;
import com.bornfire.xbrl.entities.RT_IRS_ENTITY2;
import com.bornfire.xbrl.entities.RT_IRS_REPOSITORY;
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
import com.bornfire.xbrl.entities.RT_Matrix_monitoring_entity;
import com.bornfire.xbrl.entities.RT_Matrix_monitoring_rep;
import com.bornfire.xbrl.entities.RT_MmData;
import com.bornfire.xbrl.entities.RT_MmDataRepository;
import com.bornfire.xbrl.entities.RT_NostroAccBalData;
import com.bornfire.xbrl.entities.RT_NostroAccBalDataRepository;
import com.bornfire.xbrl.entities.RT_RWA_Fund_base_data_rep;
import com.bornfire.xbrl.entities.RT_RepoDataTemplate;
import com.bornfire.xbrl.entities.RT_RepoDataTemplateRepository;
import com.bornfire.xbrl.entities.RT_SLS_Detail_Enitity;
import com.bornfire.xbrl.entities.RT_SLS_Detail_Repository;
import com.bornfire.xbrl.entities.RT_SLS_ENTITIES;
import com.bornfire.xbrl.entities.RT_SLS_Repository;
import com.bornfire.xbrl.entities.RT_TradeLevelDataDerivatives;
import com.bornfire.xbrl.entities.RT_TradeLevelDataDerivativesRepository;
import com.bornfire.xbrl.entities.RT_TradeLevelDataDerivativesSimplified;
import com.bornfire.xbrl.entities.RT_TradeLevelDataDerivativesSimplifiedRepository;
import com.bornfire.xbrl.entities.RT_TradeMarketRiskData;
import com.bornfire.xbrl.entities.RT_TradeMarketriskDataRepository;
import com.bornfire.xbrl.entities.RT_TreasuryCreditEntity;
import com.bornfire.xbrl.entities.RT_TreasuryCreditRepo;
import com.bornfire.xbrl.entities.UserProfile;
import com.bornfire.xbrl.entities.UserProfileRep;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@ConfigurationProperties("default")
public class XBRLNavigationController {

	private static final Logger logger = LoggerFactory.getLogger(XBRLNavigationController.class);
	/*
	 * @PersistenceContext private EntityManager entityManager;
	 */

	@Autowired
	RT_SLSServices RT_SLSServices;

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
		auditService.createBusinessAudit(userid, "ADD", "DATACONTROL", null, "BCBUAE_DATACONTROL");

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
		headersResponse.setContentDispositionFormData("attachment", "CBUAE_Repo_Data_Template.xls");

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
		logger.info("Controller: Received request for Treasury credit Excel download.");

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
			@RequestParam(required = false) String formmode, Model model) {

		if ("edit".equalsIgnoreCase(formmode) && siNo != null) {
			model.addAttribute("formmode", "edit");
			model.addAttribute("InvestmentData", investmentSecuritiesDataTemplateRepo.findById(siNo)
					.orElse(new RT_Investment_Securities_Data_Template()));
		} else if ("list".equalsIgnoreCase(formmode)) {
			List<RT_Investment_Securities_Data_Template> list = investmentSecuritiesDataTemplateRepo.getsecDatalist();

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

	@RequestMapping(value = "/downloadLiquidityRiskData", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadLiquidityRiskData() {
		logger.info("Controller: Received request for Trade Market Risk Excel download.");

		try {
			byte[] excelData = liquidityRiskDataService.generateLiquidityDataExcel();

			if (excelData.length == 0) {
				logger.warn("Controller: Service returned no data. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			HttpHeaders headers = new HttpHeaders();
			String filename = "CBUAE_Liquidity Risk_Data_Template.xls";
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
	// -------------------------------------Liquidity_Risk_Data
	// End---------------------------------------

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
			logger.error(
					"Controller ERROR: The CBUAE_Investment_Securities_Data_Template_Pillar2.xls template file was not found.",
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
			@RequestParam(required = false) String SI_NO, // changed from accountNo to slNo
			Model md, HttpServletRequest req) {
		if ("edit".equalsIgnoreCase(formmode) && SI_NO != null && !SI_NO.isEmpty()) {
			Long Serialnumber = Long.parseLong(SI_NO);
			RT_Investment_Risk_Data_Dashboard_Template data = RT_Investment_Risk_Data_Dashboard_TemplateRepositoryS
					.getParticularDataBySI_NO(Serialnumber);
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
			String filename = "CBUAE_Trade_Market_Risk_Data_Template.xls";
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
			List<String> getcode = Counterparty_Reps.getcodes();
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

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("MIS_REPORT_WORKING");
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
	public ResponseEntity<ByteArrayResource> downloadTradeleveldataderivativeExcel() {
		logger.info("Controller: Received request for Trade Level Data Derivative Excel download.");

		try {
			byte[] excelData = tradeleveldataderivativeService.generateTradeleveldataderivativeExcel();

			if (excelData.length == 0) {
				logger.warn(
						"Controller: No data found for Trade Level Data Derivative report. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			String filename = "TradeLevelDataDerivative.xls";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);

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
			md.addAttribute("formmode", "add");

			if ("template".equalsIgnoreCase(tab)) {
				md.addAttribute("tab", "template");
			} else {
				md.addAttribute("tab", "datacontrols"); // fallback default
			}
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
			String filename = "CBUAE_CCR_Data_Template.xls";
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);

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
	public String IRRBB_data_Template(@RequestParam(required = false) String SI_NO,
			@RequestParam(required = false) String formmode, Model md) {

		if ("edit".equalsIgnoreCase(formmode) || "editear".equalsIgnoreCase(formmode)) {
			RT_IRRBB_Data_EAR data = IRRBB_EAR_Repository.getParticularDataBySI_NO(SI_NO);
			md.addAttribute("irrbbear", data);
			System.out.println("edit is formmode");
			md.addAttribute("formmode", "editear");

		}

		else if ("editeve".equalsIgnoreCase(formmode)) {
			System.out.println("EVE is formmode");
			RT_IRRBB_Data_EVE_Template data = IRRB_EVE_Repo.getParticularDataBySI_NO(SI_NO);
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

		/*
		 * else { model.addAttribute("formmode", "add");
		 * model.addAttribute("securityData", new
		 * RT_Investment_Securities_Data_Template()); }
		 */else {
			md.addAttribute("formmode", "add");
		}

		List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
		List<RT_CountryRiskDropdown> countryList = countryRepo.findAllByOrderByCountryOfRiskAsc();

		md.addAttribute("bankList", bankList);
		md.addAttribute("countryList", countryList);
		return "RT/IRRBB_data_Template";
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

			String filename = "CBUAE_Foreign_Currency_Deposit_Template.xls";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);

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
		boolean updated = tradeleveldataderivativesimplifiedService
				.updatetradeleveldataderivative(tradeleveldataderivative);

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
			byte[] excelData = tradeleveldataderivativesimplifiedService
					.generateTradeleveldataderivativesimplifiedExcel();

			if (excelData.length == 0) {
				logger.warn(
						"Controller: No data found for Trade Level Data Derivative Simplified report. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			String filename = "TradeLevelDataDerivativeSimplified.xls";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);

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
	public ResponseEntity<ByteArrayResource> downloadInvestmentriskdatadashboardExcel() {
		logger.info("Controller: Received request for Investment Risk Data Dashboard Excel download.");

		try {
			byte[] excelData = investmentriskdatadictionaryService.generateInvestmentriskdataExcel();

			if (excelData.length == 0) {
				logger.warn(
						"Controller: No data found for Investment Risk Data Dashboard report. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			String filename = "InvestmentRiskDataDashboard.xls";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);

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
	@RequestMapping(value = "Liquidity_Risk_Dashboard_Template", method = RequestMethod.GET)
	public String Liquidity_Risk_Dashboard_Template(@RequestParam(required = false) String SI_NO,
			@RequestParam(required = false) String formmode, Model model, Model md) {

		if ("edit".equalsIgnoreCase(formmode) && SI_NO != null && !SI_NO.isEmpty()) {
			RT_Liquidity_Risk_Dashboard_Template data = LiquidityRiskDashboardRepo.getParticularDataBySI_NO(SI_NO);
			md.addAttribute("liquidityriskdashboard", data);
			System.out.println("edit is formmode");
			md.addAttribute("formmode", "edit");

		} else if ("list".equalsIgnoreCase(formmode)) {
			md.addAttribute("branchList", LiquidityRiskDashboardRepo.getAlldetails());
			System.out.println("list is formmode");
			md.addAttribute("formmode", "list");
		} else {
			model.addAttribute("formmode", "add");
		}
		List<RT_BankNameMaster> bankList = bankRepo.findAllByOrderByBankNameAsc();
		List<RT_CountryRiskDropdown> countryList = countryRepo.findAllByOrderByCountryOfRiskAsc();

		md.addAttribute("bankList", bankList);
		md.addAttribute("countryList", countryList);

		return "RT/Liquidity_Risk_Dashboard_Template";
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
	public ResponseEntity<ByteArrayResource> downloadLiquidityriskdashboardExcel() {
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
	public ResponseEntity<ByteArrayResource> downloadIrrbbeveExcel() {
		logger.info("Controller: Received request for IRRBB Data EVE Excel download.");

		try {
			byte[] excelData = irrbbeveService.generateIrrbbeveExcel();

			if (excelData.length == 0) {
				logger.warn("Controller: No data found for IRRBB Data EVE report. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			String filename = "IrrbbDataEVE.xls";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);

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
	public ResponseEntity<ByteArrayResource> downloadIrrbbearExcel() {
		logger.info("Controller: Received request for IRRBB Data EAR Excel download.");

		try {
			byte[] excelData = irrbbearService.generateIrrbbearExcel();

			if (excelData.length == 0) {
				logger.warn("Controller: No data found for IRRBB Data EAR report. Responding with 204 No Content.");
				return ResponseEntity.noContent().build();
			}

			ByteArrayResource resource = new ByteArrayResource(excelData);

			String filename = "IrrbbDataEAR.xls";
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			logger.info("Controller: Sending file '{}' to client ({} bytes).", filename, excelData.length);

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
	public ResponseEntity<ByteArrayResource> downloadIrrbbdiscountrateExcel() {
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

			if (Rowid != null && !Rowid.isEmpty()) {
				// List<RT_SLS_Detail_Enitity> slsdetaillist =
				// rt_sls_detail_repository.slsdetaillistrowid(reportdatefor, Rowid);

				List<RT_SLS_Detail_Enitity> slsdetaillist = rt_sls_detail_repository.slsdetaillistrowid(reportdatefor,
						Rowid, currency);

				int totalRows = rt_sls_detail_repository.slsdetaillistcountROWID(reportdatefor, currency, Rowid);
				int totalPages = (int) Math.ceil((double) totalRows / pageSize);

				md.addAttribute("slsdetaillist", slsdetaillist);
				md.addAttribute("reportdate", reportdate);
				md.addAttribute("formmode", "Detail");
				md.addAttribute("currency", currency);
				md.addAttribute("currentPage", page);
				md.addAttribute("totalPages", totalPages);

			} else {
				// int totalRows = rt_sls_detail_repository.slsdetaillistcount(reportdatefor);

				int totalRows = rt_sls_detail_repository.slsdetaillistcount(reportdatefor, currency);
				int totalPages = (int) Math.ceil((double) totalRows / pageSize);

				// List<RT_SLS_Detail_Enitity> slsdetaillist =
				// rt_sls_detail_repository.slsdetaillist(reportdatefor, offset, pageSize);
				System.out.println("offset==" + offset);
				List<RT_SLS_Detail_Enitity> slsdetaillist = rt_sls_detail_repository.slsdetaillist(reportdatefor,
						currency, offset, pageSize);
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

	// THE COMMON UPLOAD METHOD
	@PostMapping("/commonUploadFile")
	@ResponseBody
	public ResponseEntity<String> commonUploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("reportType") String reportType,
			@RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate, HttpServletRequest request) {

		String username = (String) request.getSession().getAttribute("USERNAME");
		if (username == null)
			username = "SYSTEM";

		logger.info("Initiating Common Upload. Type: {}, User: {}, Reference Date: {}", reportType, username, toDate);

		try {
			if (file.isEmpty()) {
				return ResponseEntity.badRequest().body("Please select a valid file.");
			}

			String resultMsg = "";

			if ("RWAFUND".equals(reportType) || "RWANONFUND".equals(reportType)) {
				resultMsg = rwaService.uploadRwaTextFile(file, reportType, toDate);
			} else if ("FXP".equals(reportType)) {
				// 1. Process FX (The image data)
				resultMsg = rtFxPositionService.uploadFxData(file, fromDate, toDate, username);
			} else if ("SLS".equals(reportType)) {
				// 2. Process SLS (Your existing service)
				// Note: Using toDate as the reportDate for your SLS service
				rtSlsService.processAndSaveFile(file, toDate, username);
				resultMsg = "SLS data uploaded and saved successfully!";
			} else if ("ACPR".equals(reportType)) {
				resultMsg = acprService.uploadAcprFile(file, fromDate, toDate, username);
			} else if ("ACPRNF".equals(reportType)) {
				resultMsg = acprService.uploadAcprnfFile(file, fromDate, toDate, username);
			} else {
				return ResponseEntity.badRequest().body("Unsupported Report Type: " + reportType);
			}

			return ResponseEntity.ok(resultMsg);

		} catch (Exception e) {
			logger.error("Upload Error", e);
			return ResponseEntity.badRequest().body("Error: " + e.getMessage());
		}
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
}
