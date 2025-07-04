package com.bornfire.xbrl.services;

import java.math.BigDecimal;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bornfire.xbrl.entities.AccessAndRoles;
import com.bornfire.xbrl.entities.AccessandRolesRepository;


@Service
@ConfigurationProperties("output")
@Transactional
public class AccessAndRolesServices {

	@Autowired
	AccessandRolesRepository accessandrolesrepository;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	Environment env;
	
	@Autowired
	DataSource srcdataSource;
	
	@SuppressWarnings("unchecked")
	public List<AccessAndRoles> gettingaccessDetails(String roleid) {

		List<AccessAndRoles> list = (List<AccessAndRoles>) sessionFactory.getCurrentSession()
				.createQuery("from AccessAndRoles where role_id ='" + roleid + "'").getResultList();
		System.out.print("df" + list);
		return list;

	}

	public String addPARAMETER(AccessAndRoles alertparam, String formmode,
	        String adminValue, String RT_ReportsValue, String aslValue, String aslUploadValue,
	        String auditUsValue, String finalString, String USERID) {

	    String msg = "";

	    if (formmode.equals("add")) {
	        AccessAndRoles up = alertparam;
	        up.setDelFlg("N");
	        up.setModifyFlg("N");
	        up.setEntityFlg("N");
	        up.setAdmin(adminValue);
	        up.setRtReports(RT_ReportsValue);
	        up.setAsl(aslValue);
	        up.setAsl_upload(aslUploadValue);
	        up.setAudit_us(auditUsValue);
	        up.setEntryUser(USERID);
	        up.setEntryTime(new Date());
	        up.setMenulist(finalString);
	        accessandrolesrepository.save(up);
	        msg = "Role Created Successfully";

	    } else if (formmode.equals("edit")) {
	        Optional<AccessAndRoles> user = accessandrolesrepository.findById(alertparam.getRoleId());
	        AccessAndRoles user1 = user.get();

	        AccessAndRoles up = alertparam;
	        up.setAdmin(adminValue);
	        up.setRtReports(RT_ReportsValue);
	        up.setAsl(aslValue);
	        up.setAsl_upload(aslUploadValue);
	        up.setAudit_us(auditUsValue);

	        if (!finalString.equals("")) {
	            up.setMenulist(finalString);
	        } else {
	            up.setMenulist(user1.getMenulist());
	        }

	        up.setDelFlg("N");
	        up.setModifyFlg("Y");
	        up.setEntityFlg("N");
	        up.setEntryUser(user1.getEntryUser());
	        up.setEntryTime(user1.getEntryTime());
	        up.setModifyUser(USERID);
	        up.setModifyTime(new Date());

	        accessandrolesrepository.save(up);
	        msg = "Role Edited Successfully";

	    } else if (formmode.equals("delete")) {
	        Optional<AccessAndRoles> user = accessandrolesrepository.findById(alertparam.getRoleId());
	        AccessAndRoles accessRole = user.get();
	        accessRole.setDelFlg("Y");
	        accessRole.setEntityFlg("N");
	        accessandrolesrepository.save(accessRole);
	        msg = "Role Deleted Successfully";

	    } else if (formmode.equals("verify")) {
	        Optional<AccessAndRoles> user = accessandrolesrepository.findById(alertparam.getRoleId());
	        AccessAndRoles user1 = user.get();
	        user1.setDelFlg("N");
	        user1.setModifyFlg("N");
	        user1.setEntityFlg("Y");
	        user1.setAuthUser(USERID);
	        user1.setAuthTime(new Date());
	        accessandrolesrepository.save(user1);
	        msg = "Role Verified Successfully";
	    }

	    return msg;
	}

	public AccessAndRoles getRoleId(String id) {
		Session session = sessionFactory.getCurrentSession();
		Query<AccessAndRoles> query = session
				.createQuery(" from AccessAndRoles where role_id=?1 and NVL(DEL_FLG,1) <> 'Y'", AccessAndRoles.class);
		query.setParameter(1, id);
		List<AccessAndRoles> result = query.getResultList();
		if (!result.isEmpty()) {
			return result.get(0);
		} else {
			return new AccessAndRoles();
		}

	}

	public AccessAndRoles getRoleMenu(String id) {
		Session session = sessionFactory.getCurrentSession();
		Query<AccessAndRoles> query = session.createQuery(" from AccessAndRoles where role_id=?1", AccessAndRoles.class);
		query.setParameter(1, id);
		List<AccessAndRoles> result = query.getResultList();
		if (!result.isEmpty()) {
			return result.get(0);
		} else {
			return new AccessAndRoles();
		}

	}

	public String deleteRole(String userid) {
		Session hs = sessionFactory.getCurrentSession();
		Query qr;
		qr = hs.createQuery("select count(*) from UserProfile where role_id= ?1");
		qr.setParameter(1, userid);
		long count = (long) qr.getSingleResult();
		String msg = "";
		if (count == 0) {
			Optional<AccessAndRoles> user = accessandrolesrepository.findById(userid);
			AccessAndRoles reg = user.get();
			reg.setDelFlg("Y");
			accessandrolesrepository.save(reg);
			msg = "Role Deleted Successfully";
		} else {
			msg = "This role has been assigned to an User.Cannot Delete ";
		}
		return msg;
	}
}