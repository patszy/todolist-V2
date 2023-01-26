package tdl.user;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jpa_tdl.dao.UserDAO;
import jpa_tdl.entities.User;

@Named
@ViewScoped
public class UserEditGETBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_USER_LIST = "userList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private User user = new User();
	private User loaded = null;

	@Inject
	FacesContext context;

	@EJB
	UserDAO userDAO;

	public User getUser() {
		return user;
	}

	public void onLoad() throws IOException {
		if (!context.isPostback()) {
			if (!context.isValidationFailed() && user.getIdUser() > 0) {
				loaded = userDAO.find(user.getIdUser());
			}
			if (loaded != null) {
				user = loaded;
			} else {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
				// if (!context.isPostback()) { // possible redirect
				// context.getExternalContext().redirect("personList.xhtml");
				// context.responseComplete();
				// }
			}
		}

	}

	public String saveData() {
		// no Person object passed
		if (loaded == null) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if (user.getIdUser() > 0) {
				// new record
				userDAO.create(user, "user");
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
