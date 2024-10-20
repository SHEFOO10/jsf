package tech.shefoo.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import tech.shefoo.dao.ActivityDAO;
import tech.shefoo.dao.LoginDAO;
import tech.shefoo.dao.MemberDAO;

@ManagedBean
@SessionScoped
public class Login implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;
	
	private String pwd;
	private String msg;
	private String user;
	private long membersCount;
	private long activitiesCount;

    
    public long getActivitiesCount() {
		return activitiesCount;
	}

	public void setActivitiesCount(long activitiesCount) {
		this.activitiesCount = activitiesCount;
	}

	private MemberDAO memberdoa = new MemberDAO();
	private ActivityDAO activitydoa = new ActivityDAO();
	
	@PostConstruct
	public void init() {
		setMembersCount(memberdoa.membersCount());
		setActivitiesCount(activitydoa.activitiesCount());
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	//validate login
	public String validateUsernamePassword() {
		boolean valid = LoginDAO.validate(user, pwd);
		if (valid) {
			HttpSession session = SessionUtils.getSession();
			session.setAttribute("username", user);
			return "admin";
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Incorrect Username and Passowrd",
							"Please enter correct username and Password"));
			return "login";
		}
	}

	//logout event, invalidate session
	public String logout() {
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		return "login";
	}

	public long getMembersCount() {
		return membersCount;
	}

	public void setMembersCount(long membersCount) {
		this.membersCount = membersCount;
	}
}