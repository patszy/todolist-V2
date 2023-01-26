package tdl.item;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import jpa_tdl.dao.TodoitemDAO;
import jpa_tdl.entities.Todoitem;
import jpa_tdl.entities.Todolist;


@Named
@ViewScoped
public class ItemListBB  implements Serializable {
	private static final String PAGE_ITEM_EDIT = "itemEdit?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String title;
	private String message;
	private String deadline;
	private Todolist tdl = new Todolist();
	private Todolist loaded = null;
	
	@Inject
	ExternalContext extcontext;
	
	@Inject
	FacesContext context;
	
	@Inject
	Flash flash;
	
	@EJB
	TodoitemDAO itemDAO;
	
	public void onLoad() throws IOException {
		// 1. load person passed through session
		// HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		// loaded = (Person) session.getAttribute("person");

		// 2. load person passed through flash
		loaded = (Todolist) flash.get("tdl");

		// cleaning: attribute received => delete it from session
		if (loaded != null) {
			tdl = loaded;
			// session.removeAttribute("person");
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
			// if (!context.isPostback()) { //possible redirect
			// context.getExternalContext().redirect("personList.xhtml");
			// context.responseComplete();
			// }
		}
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getMessage() {
		return message;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	
	public String getDeadline() {
		return deadline;
	}

	public List<Todoitem> getFullList(){
		return itemDAO.getFullList();
	}
	
	public List<Todoitem> getList(){
		List<Todoitem> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		
		if (title != null && title.length() > 0){
			searchParams.put("title", title);
		}
		
		if (tdl != null){
			searchParams.put("tdl", tdl);
		}
		
		System.out.println("GetList: " + tdl.getIdList() + ":" + tdl.getTitle());
		//2. Get list
		list = itemDAO.getList(searchParams);
		
		return list;
	}
	
	public String newItem(){
		Todoitem item = new Todoitem();
		item.setTodolist(tdl);
		//1. Pass object through session
		//HttpSession session = (HttpSession) extcontext.getSession(true);
		//session.setAttribute("person", person);
		flash.put("item", item);
		//2. Pass object through flash	
		//flash.put("tdl", tdl);
		
		return PAGE_ITEM_EDIT;
	}

	public String editItem(Todoitem item){
		//1. Pass object through session
		//HttpSession session = (HttpSession) extcontext.getSession(true);
		//session.setAttribute("person", person);
		item.setTodolist(tdl);
		//2. Pass object through flash 
		flash.put("item", item);
		
		return PAGE_ITEM_EDIT;
	}

	public String deleteItem(Todoitem item){
		itemDAO.remove(item);
		return PAGE_STAY_AT_THE_SAME;
	}
}
