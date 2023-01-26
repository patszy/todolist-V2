package tdl.role;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.simplesecurity.RemoteClient;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import jpa_tdl.dao.RoleDAO;
import jpa_tdl.dao.UserRoleDAO;
import jpa_tdl.entities.Role;
import jpa_tdl.entities.Todolist;
import jpa_tdl.entities.User;
import jpa_tdl.entities.UserRole;

@Named
@ManagedBean
@ViewScoped
public class RoleListBB implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final String PAGE_ROLE_EDIT = "roleEdit?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private User user = new User();
	private User loaded = null;
	
	@EJB
	RoleDAO roleDAO;
	
	@EJB
	UserRoleDAO urDAO;
	
	@Inject
	ExternalContext extcontext;
	
	@Inject
	FacesContext context;
	
	@Inject
	Flash flash;
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void onLoad() throws IOException {
		// 1. load person passed through session
		// HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		// loaded = (Person) session.getAttribute("person");

		// 2. load person passed through flash
		loaded = (User) flash.get("user");

		// cleaning: attribute received => delete it from session
		if (loaded != null) {
			user = loaded;
			// session.removeAttribute("person");
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
			// if (!context.isPostback()) { //possible redirect
			// context.getExternalContext().redirect("personList.xhtml");
			// context.responseComplete();
			// }
		}
	}

	public List<Role> getFullList(){
		return roleDAO.getFullList();
	}
	
	public List<UserRole> getList(){
		List<UserRole> urList = null;
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();
		searchParams.put("user", user);
		
		//2. Get list
		urList = urDAO.getList(searchParams);
		
		return urList;
	}
	
	public String newRole(){
		Role role = new Role();		
		//1. Pass object through session
		//HttpSession session = (HttpSession) extcontext.getSession(true);
		//session.setAttribute("person", person);
		
		//2. Pass object through flash	
		flash.put("role", role);
		flash.put("user", user);
		
		return PAGE_ROLE_EDIT;
	}

	public String deleteRole(UserRole ur){
		urDAO.remove(ur);
		
		return PAGE_STAY_AT_THE_SAME;
	}
}
