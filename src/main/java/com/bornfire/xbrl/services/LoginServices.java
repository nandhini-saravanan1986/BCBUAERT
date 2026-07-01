package com.bornfire.xbrl.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.bornfire.xbrl.config.PasswordEncryption;
import com.bornfire.xbrl.entities.AccessAndRoles;
import com.bornfire.xbrl.entities.AccessandRolesRepository;
import com.bornfire.xbrl.entities.Smsserviceotp;
import com.bornfire.xbrl.entities.UserProfile;
import com.bornfire.xbrl.entities.UserProfileRep;
import com.bornfire.xbrl.entities.XBRLSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
@ConfigurationProperties("output")
@Transactional
public class LoginServices {

	private static final Logger logger = LoggerFactory.getLogger(LoginServices.class);

	@Autowired
	UserProfileRep userProfileRep;
	
	@Autowired
	AccessandRolesRepository accessandRolesRepository;

	@Autowired
	private HttpSession session;
	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DataSource srcdataSource;

	@Autowired
	AuditService auditservice;
	@NotNull
	private String exportpath;

	@Value("${default.password}")
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getExportpath() {
		return exportpath;
	}

	public void setExportpath(String exportpath) {
		this.exportpath = exportpath;
	}

	/*
	 * Getting 3 inputs -
	 * 
	 * UserProfile Object, Formmode - Valid values : add, edit, inputuser - user who
	 * edited the data
	 * 
	 * if formmode is add - Get password from application.properties create the user
	 * 
	 * if formmode is edit - Get password from database for that user and use other
	 * fields came from front end.
	 * 
	 * 
	 */

	public Iterable<UserProfile> getUsersList() {

		Iterable<UserProfile> users = userProfileRep.findAll();

		return users;

	}

	public UserProfile getUser(String id) {
		logger.info(id);
		if (userProfileRep.existsById(id)) {
			UserProfile up = userProfileRep.findById(id).get();
			logger.info(up.getEntity_flg());
			return up;
		} else {
			UserProfile UserProfile = new UserProfile();
			UserProfile.setLogin_low("09:00");
			UserProfile.setLogin_high("19:00");
			return UserProfile;
		}

	};

	public List<UserProfile> getFinUsersList() {

		Session hs = sessionFactory.getCurrentSession();
		return hs.createQuery("from UserProfile ", UserProfile.class).getResultList();

	}

	public String addUser(UserProfile userProfile, String formmode, String inputUser, String username, String mob,
			String role) {
		String msg = "";

		try {
			if ("add".equalsIgnoreCase(formmode)) {
				UserProfile up = userProfile;
				String roleSyncError = syncRoleAccessFromMaster(up);
				if (roleSyncError != null) {
					return roleSyncError;
				}
				if (up.getLogin_status() == null || up.getLogin_status().trim().isEmpty()) {
					up.setLogin_status("Active");
				}
				if (up.getUser_status() == null || up.getUser_status().trim().isEmpty()) {
					up.setUser_status("Active");
				}
				if (up.getPass_exp_days() == null || up.getPass_exp_days().trim().isEmpty()) {
					up.setPass_exp_days("90");
				}
				if (up.getPassword() == null || up.getPassword().trim().isEmpty()) {
					return "Password is required";
				}

				// System.out.println("password is : " + up.getPassword());

				// Encrypt password
				String encryptedPassword = PasswordEncryption.getEncryptedPassword(up.getPassword());

				// Login and User Status Flags
				up.setUser_locked_flg("Active".equalsIgnoreCase(up.getLogin_status()) ? "N" : "Y");
				up.setDisable_flg("Active".equalsIgnoreCase(up.getUser_status()) ? "N" : "Y");

				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				Date date1 = formatter.parse("28-02-2027"); // <-- note full year!
				Date Disabledate = formatter.parse("21-12-23"); // <-- note full year!

				up.setAcc_exp_date(date1);

				up.setLogin_low("00:00");
				up.setLogin_high("23:59");
				up.setDisable_start_date(Disabledate);
				up.setDisable_end_date(Disabledate);

				up.setEntity_flg("N");
				up.setEntry_time(new Date());
				up.setEntry_user(inputUser);
				up.setLogin_flg("N");
				up.setNo_of_attmp(0);
				up.setLog_in_count("0");
				up.setEmp_name(up.getUsername());
				// System.out.println("user name is: "+ up.getUsername());

				// Password expiry date
				if (up.getPass_exp_days() != null && !up.getPass_exp_days().trim().isEmpty()) {
					String localdateval = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					LocalDate date = LocalDate.parse(localdateval);
					BigDecimal passexpdays = new BigDecimal(up.getPass_exp_days());
					LocalDate date2 = date.plusDays(passexpdays.intValue());
					up.setPass_exp_date(new SimpleDateFormat("yyyy-MM-dd").parse(date2.toString()));
				} else {
					// Default expiry period: 90 days (optional)
					String localdateval = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					LocalDate date = LocalDate.parse(localdateval);
					LocalDate date2 = date.plusDays(90);
					up.setPass_exp_date(new SimpleDateFormat("yyyy-MM-dd").parse(date2.toString()));
				}

				// Final password set
				up.setPassword(encryptedPassword);
				auditservice.createBusinessAudit(userProfile.getUserid(), "ADD", "ADD", null, "BRF_USER_PROFILE_TABLE");
				// Save the user
				userProfileRep.save(up);

				// Optional: Audit logging
				if (mob != null && !mob.trim().isEmpty()) {
					try {
						BigDecimal mobile = new BigDecimal(mob); // only if needed
					} catch (Exception e) {
						System.out.println("Invalid mobile number for audit");
					}
				}

				// Example audit log ID fetch
				try {
					Session hs = sessionFactory.getCurrentSession();
					BigDecimal srlno = (BigDecimal) hs
							.createNativeQuery("SELECT USER_AUDIT_SRL_NO.NEXTVAL AS SRL_NO FROM DUAL")
							.getSingleResult();
					System.out.println("Audit SRL_NO: " + srlno);
					// you can insert audit data here
				} catch (Exception e) {
					System.out.println("Audit logging failed: " + e.getMessage());
				}

				msg = "User Created Successfully";

			} else {
				// Form mode is not add, update flow
				Optional<UserProfile> upOptional = userProfileRep.findById(userProfile.getUserid());
				Map<String, String> changeMap = new HashMap<>();
				if (upOptional.isPresent()) {
					UserProfile up = upOptional.get();

					userProfile.setPassword(up.getPassword());
					String roleSyncError = syncRoleAccessFromMaster(userProfile);
					if (roleSyncError != null) {
						return roleSyncError;
					}
					if (userProfile.getLogin_status() == null || userProfile.getLogin_status().trim().isEmpty()) {
						userProfile.setLogin_status(up.getLogin_status());
					}
					if (userProfile.getUser_status() == null || userProfile.getUser_status().trim().isEmpty()) {
						userProfile.setUser_status(up.getUser_status());
					}

					userProfile
							.setUser_locked_flg("Active".equalsIgnoreCase(userProfile.getLogin_status()) ? "N" : "Y");
					userProfile.setDisable_flg("Active".equalsIgnoreCase(userProfile.getUser_status()) ? "N" : "Y");

					// Handle pass_exp_days and pass_exp_date
					if (userProfile.getPass_exp_days() != null
							&& userProfile.getPass_exp_days().equals(up.getPass_exp_days())) {
						userProfile.setPass_exp_date(up.getPass_exp_date());
					} else {
						if (userProfile.getPass_exp_days() != null
								&& !userProfile.getPass_exp_days().trim().isEmpty()) {
							String localdateval = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
							LocalDate date = LocalDate.parse(localdateval);
							BigDecimal passexpdays = new BigDecimal(userProfile.getPass_exp_days());
							LocalDate date2 = date.plusDays(passexpdays.intValue());
							userProfile.setPass_exp_date(new SimpleDateFormat("yyyy-MM-dd").parse(date2.toString()));
						}
					}

					userProfile.setLog_in_count(up.getLog_in_count() != null ? up.getLog_in_count() : "1");
					userProfile.setEntry_user(up.getEntry_user());
					userProfile.setEntry_time(up.getEntry_time());
					userProfile.setNo_of_attmp(0);
					userProfile.setEntity_flg("N");
					userProfile.setModify_user(inputUser);
					userProfile.setModify_time(new Date());
					List<String> ignoreFields = Arrays.asList("password", "user_locked_flg", "disable_flg",
							"pass_exp_date", "log_in_count", "entry_user", "entry_time", "no_of_attmp", "entity_flg",
							"modify_user", "modify_time");

					UserProfile dbUser = upOptional.get();

					Map<String, String> changes = new LinkedHashMap<>();

					for (Field field : UserProfile.class.getDeclaredFields()) {
						field.setAccessible(true);
						try {
							Object oldValue = field.get(dbUser);
							Object newValue = field.get(userProfile);
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
					auditservice.createBusinessAudit(userProfile.getUserid(), // or whatever id you track
							"MODIFY", "USER_PROFILE_SCREEN", changes, "User profile updated");

					userProfileRep.save(userProfile);
					msg = "User Edited Successfully";
				} else {
					msg = "User Not found to edit";
				}
			}

		} catch (Exception e) {
			msg = "Error Occurred. Please contact Administrator";
			e.printStackTrace();
			logger.info(e.getMessage());
		}

		return msg;
	}
	
	private String syncRoleAccessFromMaster(UserProfile up) {
		if (up == null || up.getRole_id() == null || up.getRole_id().trim().isEmpty()) {
			return "Role ID is required";
		}
		Optional<AccessAndRoles> roleOpt = accessandRolesRepository.findById(up.getRole_id().trim());
		if (!roleOpt.isPresent()) {
			return "Invalid Role ID";
		}
		AccessAndRoles roleMaster = roleOpt.get();
		if ("Y".equalsIgnoreCase(roleMaster.getDelFlg())) {
			return "Selected role is deleted";
		}
		if (!"Y".equalsIgnoreCase(roleMaster.getEntityFlg())) {
			return "Selected role is not verified";
		}
		up.setRole_desc(roleMaster.getRoleDesc());
		up.setWork_class(roleMaster.getWorkClass());
		up.setPermissions(roleMaster.getPermissions());
		String menuAccess = roleMaster.getMenulist();
		if (menuAccess == null || menuAccess.trim().isEmpty()) {
			menuAccess = roleMaster.getDomainId();
		}
		up.setDomain_id(menuAccess);
		return null;
	}

	public String verifyUser(UserProfile userProfile, String inputUser) {

		String msg = "";

		try {

			Optional<UserProfile> upOpt = userProfileRep.findById(userProfile.getUserid());

			if (!upOpt.isPresent()) {
				return "User not found";
			}

			UserProfile existingUser = upOpt.get();

			// Preserve existing password
			userProfile.setPassword(existingUser.getPassword());

			// Default status handling
			if (userProfile.getLogin_status() == null) {
				userProfile.setLogin_status("Active");
			}

			if (userProfile.getUser_status() == null) {
				userProfile.setUser_status("Active");
			}
			if (userProfile.getDepartment() == null) {
				userProfile.setDepartment(upOpt.get().getDepartment());
			}
			if (userProfile.getRole_id() == null) {
				userProfile.setRole_id(upOpt.get().getRole_id());
			}
			// Login lock flag
			if ("Active".equalsIgnoreCase(userProfile.getLogin_status())) {
				userProfile.setUser_locked_flg("N");
			} else {
				userProfile.setUser_locked_flg("Y");
			}

			// Disable flag
			if ("Active".equalsIgnoreCase(userProfile.getUser_status())) {
				userProfile.setDisable_flg("N");
			} else {
				userProfile.setDisable_flg("Y");
			}

			// Reset login related flags
			userProfile.setNo_of_attmp(0);
			userProfile.setEntity_flg("Y");
			userProfile.setLogin_flg("N");

			// Login time window
			userProfile.setLogin_low("00:00");
			userProfile.setLogin_high("23:59");

			// Disable dates → today
			Date today = new Date();
			userProfile.setDisable_start_date(today);
			userProfile.setDisable_end_date(today);

			// Employee name
			userProfile.setEmp_name(userProfile.getUsername());

			// Account expiry date → 3 years from today
			Calendar accExpCal = Calendar.getInstance();
			accExpCal.set(Calendar.HOUR_OF_DAY, 0);
			accExpCal.set(Calendar.MINUTE, 0);
			accExpCal.set(Calendar.SECOND, 0);
			accExpCal.set(Calendar.MILLISECOND, 0);
			accExpCal.add(Calendar.YEAR, 3);

			userProfile.setAcc_exp_date(accExpCal.getTime());

			// Password expiry calculation
			int expiryDays = 90; // default

			if (userProfile.getPass_exp_days() != null && !userProfile.getPass_exp_days().trim().isEmpty()) {
				expiryDays = Integer.parseInt(userProfile.getPass_exp_days());
			}

			LocalDate passExpDate = LocalDate.now().plusDays(expiryDays);
			userProfile.setPass_exp_date(java.sql.Date.valueOf(passExpDate));

			// Audit info
			userProfile.setAuth_user(inputUser);
			userProfile.setAuth_time(new Date());

			auditservice.createBusinessAudit(userProfile.getUserid(), "Verify", "userProfile-verify", null,
					"BRF_USER_PROFILE_TABLE");

			// Save
			userProfileRep.save(userProfile);

			msg = "User Verified Successfully";

		} catch (Exception e) {
			msg = "Error occurred. Please contact Administrator";
		}

		return msg;
	}

	public String passwordReset(UserProfile userprofile, String userid) {

		String msg = "";

		try {
			String encryptedPassword = PasswordEncryption.getEncryptedPassword(this.password);

			Optional<UserProfile> up = userProfileRep.findById(userprofile.getUserid());

			if (up.isPresent()) {
				UserProfile user = up.get();
				user.setPassword(encryptedPassword);
				user.setNo_of_attmp(0);
				user.setLogin_flg("N");
				user.setUser_locked_flg("N");
				auditservice.createBusinessAudit(user.getUserid(), "Password Reset", "UserProfile-Password Reset", null,
						"BRF_USER_PROFILE_TABLE");
				userProfileRep.save(user);
			}

			msg = "Password Resetted Successfully";

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {

			e.printStackTrace();

			msg = "Error Occured. Please contact Administrator";
		}

		return msg;
	}

	/*
	 * Getting LoginFlg -
	 * 
	 * If loginFlg = 'N' - User should be prompted to change password. else thats
	 * not required.
	 * 
	 * Loginflg ='N' will be updated at the time of new user creation and at the
	 * time of password reset by admin.
	 * 
	 */
	public String checkPasswordChangeReq(String userid) {

		Optional<UserProfile> up = userProfileRep.findById(userid);
		String loginflg = up.get().getLogin_flg();

		return loginflg;
	}
	
	public String sendclientotp(String otp, String roleType, UserProfile userProfile) {
		logger.info("Start Sending OTP");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		Smsserviceotp smsserviceotp = new Smsserviceotp();
		smsserviceotp.setWrapperApiKey("LA6m0");
		smsserviceotp.setSmssenderid("BOBAlert");
		smsserviceotp.setSmsmobilenumber(userProfile.getMob_number());
		smsserviceotp.setSmstext("RiskInsights verification code: " + otp+". Enter this code to continue.For your security never share this code to anyone.");
		smsserviceotp.setToemail(userProfile.getEmail_id());
		smsserviceotp.setEmailsubject("OTP Verification");
		smsserviceotp.setEmailtemplateid("BOBAlert");
		smsserviceotp.setEmailtext("RiskInsights verification code: " + otp+". Enter this code to continue.For your security never share this code to anyone.");
		HttpEntity<Smsserviceotp> entity = new HttpEntity<>(smsserviceotp, httpHeaders);

		ResponseEntity<String> response = null;
		try {
			logger.info("Ready to Call URL for OTP");
			RestTemplate restTemplate = new RestTemplate();
			response = restTemplate.postForEntity("https://wrap.smshub.live/api/APIWrapper", entity, String.class);
			logger.info(response.toString());

			if (response.getStatusCode() == HttpStatus.OK) {
				logger.info("Send Successfully");
				return "Otpsendsuccessfully";
			} else {
				logger.info("Send Failed");
				return "Something went wrong at server end";
			}

		} catch (HttpClientErrorException ex) {
			logger.info("Exception -" + ex.getMessage());
			return "Something went wrong at server end";
		} catch (HttpServerErrorException ex) {
			logger.info("Exception -" + ex.getMessage());
			return "Something went wrong at server end";
		} catch (Exception ex) {
			logger.info("Exception -" + ex.getMessage());
			return "Something went wrong at server end";
		}
	}

	public int checkAcctexpirty(String userid) {

		Optional<UserProfile> up = userProfileRep.findById(userid);
		Date expDate = up.get().getAcc_exp_date();
		Date currDate = new Date();

		DateTime dt1 = new DateTime(currDate);
		DateTime dt2 = new DateTime(expDate);

		int remaindays = Days.daysBetween(dt1, dt2).getDays();

		logger.info("Account Expired in:" + remaindays);
		return remaindays;
	}

	public int checkpassexpirty(String userid) {

		Optional<UserProfile> up = userProfileRep.findById(userid);
		Date expDate = up.get().getPass_exp_date();
		Date currDate = new Date();

		DateTime dt1 = new DateTime(currDate);
		DateTime dt2 = new DateTime(expDate);

		int remaindays = Days.daysBetween(dt1, dt2).getDays();

		logger.info("Password Expired in:" + remaindays);
		return remaindays;
	}

	public String changePassword(String oldpass, String newpass, String userid) {
		String msg = "";

		Optional<UserProfile> up = userProfileRep.findById(userid);

		try {
			if (up.isPresent()) {
				UserProfile user = up.get();
				if (PasswordEncryption.validatePassword(oldpass, user.getPassword())) {

					if (!PasswordEncryption.validatePassword(newpass, user.getPassword())) {

						String encryptedPassword = PasswordEncryption.getEncryptedPassword(newpass);
						user.setPassword(encryptedPassword);
						user.setLogin_flg("Y");

						LocalDateTime localDateTime = user.getPass_exp_date().toInstant().atZone(ZoneId.systemDefault())
								.toLocalDateTime();
						user.setPass_exp_date(
								Date.from(localDateTime.plusDays(365).atZone(ZoneId.systemDefault()).toInstant()));
						auditservice.createBusinessAudit(user.getUserid(), "Password Change",
								"Userprofile - Password Change", null, "BRF_USER_PROFILE_TABLE");
						userProfileRep.save(user);
						msg = "Password Changed Successfully";

					} else {

						msg = "New password cannot be Same as Old password";
					}

				} else {
					msg = "Incorrect Old Password!";
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			msg = "Error Occured. Please contact Administrator";
		}
		logger.info(msg);
		return msg;
	};

	public void SessionLogging(String menuname, String menuid, String sessionid, String userid, String ip,
			String status) {
		Session hs = sessionFactory.getCurrentSession();

		try {

			if (menuname.equals("LOGOUT")) {

				hs.createQuery("update XBRLSession set status='IN-ACTIVE' where session_id = ?1")
						.setParameter(1, sessionid).executeUpdate();

			} else {

				hs.save(new XBRLSession(menuname, menuid, sessionid, userid, ip, new Date(), status));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String deleteuser(String userid) {
		String msg = "";

		try {
			Optional<UserProfile> user = userProfileRep.findById(userid);
			if (user.isPresent()) {

				userProfileRep.deleteById(userid);
				auditservice.createBusinessAudit(user.get().getUserid(), "Delete User", "UserProfile - Delete User",
						null, "BRF_USER_PROFILE_TABLE");
				msg = "User Id Rejected";

			} else {
				msg = "Invalid Data";
			}

		} catch (Exception e) {
			msg = "Please contact Administrator";
			// TODO: handle exception
		}
		return msg;
	}

	public File getUserLogFile(Date fromdate, Date todate) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

		String path = exportpath;
		String fileName = "USER_LOGS_" + dateFormat.format(new Date()) + ".xlsx";
		File outputFile;

		File jasperFile;

		File folders = new File(path);
		if (!folders.exists()) {
			folders.mkdirs();
		}

		try {
			jasperFile = ResourceUtils.getFile("classpath:static/jasper/USER_LOGS/UserLogs.jasper");
			JasperReport jr = (JasperReport) JRLoader.loadObject(jasperFile);
			HashMap<String, Object> map = new HashMap<String, Object>();

			logger.info("Assigning Parameters for Jasper");
			map.put("FromDate", dateFormat.format(fromdate));
			map.put("ToDate", dateFormat.format(todate));

			path = path + "/" + fileName;
			JasperPrint jp = JasperFillManager.fillReport(jr, map, srcdataSource.getConnection());
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jp));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(path));
			exporter.exportReport();
			logger.info("Excel File exported");

		} catch (FileNotFoundException | JRException | SQLException e) {

			e.printStackTrace();
		}

		outputFile = new File(path);

		return outputFile;
	}

	public List<XBRLSession> getUserLog(Date fromdate, Date todate) {

		Session hs = sessionFactory.getCurrentSession();

		List<XBRLSession> ls = hs.createQuery(
				"from XBRLSession where trunc(entry_time,'DD') between ?1 and ?2 and menu in ('LOGIN','LOGOUT') order by entry_time desc ",
				XBRLSession.class).setParameter(1, fromdate).setParameter(2, todate).getResultList();

		return ls;
	}

}
