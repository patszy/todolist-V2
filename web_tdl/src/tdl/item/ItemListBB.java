package tdl.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.simplesecurity.RemoteClient;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import jpa_tdl.dao.TodoitemDAO;
import jpa_tdl.entities.Todoitem;


@Named
@RequestScoped
public class ItemListBB {
	private static final String PAGE_ITEM_EDIT = "itemEdit?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String title;
	private String message;
	private String deadline;
	
	@Inject
	ExternalContext extcontext;
	
	@Inject
	Flash flash;
	
	@EJB
	TodoitemDAO itemDAO;
	
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
		
		//2. Get list
		list = itemDAO.getList(searchParams);
		
		return list;
	}
	
	public String newItem(){
		Todoitem item = new Todoitem();		
		//1. Pass object through session
		//HttpSession session = (HttpSession) extcontext.getSession(true);
		//session.setAttribute("person", person);
		
		//2. Pass object through flash	
		flash.put("item", item);
		
		return PAGE_ITEM_EDIT;
	}

	public String editItem(Todoitem item){
		//1. Pass object through session
		//HttpSession session = (HttpSession) extcontext.getSession(true);
		//session.setAttribute("person", person);
		
		//2. Pass object through flash 
		flash.put("item", item);
		
		return PAGE_ITEM_EDIT;
	}
	
	public String openItem(Todoitem item){
		//1. Pass object through session
		//HttpSession session = (HttpSession) extcontext.getSession(true);
		//session.setAttribute("person", person);
		
		//2. Pass object through flash 
		flash.put("item", item);
		
		return PAGE_ITEM_EDIT;
	}

	public String deleteItem(Todoitem item){
		itemDAO.remove(item);
		return PAGE_STAY_AT_THE_SAME;
	}
}
