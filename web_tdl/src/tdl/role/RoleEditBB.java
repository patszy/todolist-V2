package tdl.role;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import jpa_tdl.dao.RoleDAO;
import jpa_tdl.dao.TodolistDAO;
import jpa_tdl.dao.UserRoleDAO;
import jpa_tdl.entities.Role;
import jpa_tdl.entities.User;
import jpa_tdl.entities.UserRole;

@Named
@ViewScoped
public class RoleEditBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_ROLE_LIST = "roleList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private User user = new User();
	private Role role = new Role();
	private User loadedUser = new User();
	private String name;

	@EJB
	TodolistDAO tdlDAO;
	
	@EJB
	RoleDAO roleDAO;
	
	@EJB
	UserRoleDAO urDAO;

	@Inject
	FacesContext context;

	@Inject
	Flash flash;

	public Role getRole() {
		return role;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name =  name;
	}

	public void onLoad() throws IOException {
		// 1. load person passed through session
		// HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		// loaded = (Person) session.getAttribute("person");

		// 2. load person passed through flash
		loadedUser = (User) flash.get("user");
		
		// cleaning: attribute received => delete it from session
		if (loadedUser != null) {
			user = loadedUser;
			// session.removeAttribute("person");
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
			// if (!context.isPostback()) { //possible redirect
			// context.getExternalContext().redirect("personList.xhtml");
			// context.responseComplete();
			// }
		}
	}
	
	public List<Role> getList(){
		List <Role> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		//2. Get list
		list = roleDAO.getList(searchParams);
		
		return list;
	}

	public String saveData() {
		// no Person object passed
		if (loadedUser == null) {
			return PAGE_STAY_AT_THE_SAME;
		}

		UserRole ur = new UserRole();
		Role role = roleDAO.getRoleByName(name);
		UserRole isUr = urDAO.getUserRoleByUserRole(user, role);
		
		if(role == null) {
			return PAGE_STAY_AT_THE_SAME;
		}
		
		ur.setUser(user);
		ur.setRole(role);
		
		try {
			if (isUr == null) {
				// new record
				urDAO.create(ur);	
			} else {
				// existing record
				urDAO.merge(isUr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
			return PAGE_STAY_AT_THE_SAME;
		}
		
		flash.put("user", user);

		return PAGE_ROLE_LIST;
	}
	
	public String cancelEditRole() {
		flash.put("user", user);

		return PAGE_ROLE_LIST;
	}
	
}
