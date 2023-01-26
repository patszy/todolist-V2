package tdl.tdl;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import jpa_tdl.dao.TodolistDAO;
import jpa_tdl.entities.Todolist;
import jpa_tdl.entities.User;

@Named
@ViewScoped
public class ListEditBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_TDL_LIST = "todoList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private Todolist tdl = new Todolist();
	private User user = new User();
	private Todolist loadedList = null;
	private User loadedUser = null;

	@EJB
	TodolistDAO tdlDAO;

	@Inject
	FacesContext context;

	@Inject
	Flash flash;

	public Todolist getTdl() {
		return tdl;
	}

	public void onLoad() throws IOException {
		// 1. load person passed through session
		// HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		// loaded = (Person) session.getAttribute("person");

		// 2. load person passed through flash
		loadedList = (Todolist) flash.get("tdl");
		loadedUser = (User) flash.get("user");
		
		// cleaning: attribute received => delete it from session
		if (loadedList != null && loadedUser != null) {
			tdl = loadedList;
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

	public String saveData() {
		// no Person object passed
		if (loadedList == null) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if (tdl.getIdList() == null) {
				tdl.setUser(user);
				// new record
				tdlDAO.create(tdl);	
			} else {
				// existing record
				tdlDAO.merge(tdl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_TDL_LIST;
	}
	
}
