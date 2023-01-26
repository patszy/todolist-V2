package tdl.item;

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

import jpa_tdl.dao.TodoitemDAO;
import jpa_tdl.entities.Todoitem;
import jpa_tdl.entities.Todolist;

@Named
@ViewScoped
public class ItemEditBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_ITEM_LIST = "itemList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private Todoitem item = new Todoitem();
	private Todoitem loaded = null;
 
	@EJB
	TodoitemDAO itemDAO;

	@Inject
	FacesContext context;

	@Inject
	Flash flash;

	public Todoitem getItem() {
		return item;
	}

	public void onLoad() throws IOException {
		// 1. load person passed through session
		// HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		// loaded = (Person) session.getAttribute("person");

		// 2. load person passed through flash
		loaded = (Todoitem) flash.get("item");
		System.out.println("Item: " + loaded.getTodolist().getIdList());
		// cleaning: attribute received => delete it from session
		if (loaded != null) {
			item = loaded;
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
		if (loaded == null) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if (item.getIdItem() == null) {
				// new record
				itemDAO.create(item);	
			} else {
				// existing record
				itemDAO.merge(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
			return PAGE_STAY_AT_THE_SAME;
		}
		//Pass object through flash
		flash.put("tdl", item.getTodolist());
		
		return PAGE_ITEM_LIST;
	}
	
}
