package tdl.user;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jpa_tdl.dao.RoleDAO;
import jpa_tdl.dao.UserDAO;
import jpa_tdl.entities.Role;
import jpa_tdl.entities.User;

@Named
@ViewScoped
public class UserEditBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_USER_LIST = "userList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private User user = new User();
	private User loaded = null;
	private String name;

	@EJB
	UserDAO userDAO;
	
	@EJB
	RoleDAO roleDAO;

	@Inject
	FacesContext context;

	@Inject
	Flash flash;

	public User getUser() {
		return user;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
	
	public List<Role> getRoleList(){
		List<Role> role = null;
		
		role = roleDAO.getFullList();
		
		return role;
	}

	public String saveData() {
		// no Person object passed
		if (loaded == null) {
			return PAGE_STAY_AT_THE_SAME;
		}
		
		System.out.println(user.getLogin() + "(1):" + user.getIdUser());

		System.out.println(name);
		try {
			if (user.getIdUser() == null) {
				// new record
				userDAO.create(user, name);
				
			} else {
				// existing record
				userDAO.merge(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_USER_LIST;
	}
	
}
