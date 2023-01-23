package tdl.tdl;

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

import jpa_tdl.dao.TodolistDAO;
import jpa_tdl.entities.Todolist;
import jpa_tdl.entities.User;

@Named
@RequestScoped
public class TodoListBB {
	private static final String PAGE_TDL_EDIT = "listEdit?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String title;
	private String date;
	
	@Inject
	ExternalContext extcontext;
	
	@Inject
	Flash flash;
	
	@EJB
	TodolistDAO tdlDAO;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Todolist> getFullList(){
		return tdlDAO.getFullList();
	}
	
	public List<Todolist> getList(){
		List<Todolist> list = null;
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
		
		User u = (User) RemoteClient.load(request.getSession()).getDetails();
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (title != null && title.length() > 0){
			searchParams.put("user", u);
			searchParams.put("title", title);
			searchParams.put("date", date);
		}
		
		//2. Get list
		//getiUser  from listDAO
		list = tdlDAO.getList(searchParams);
		
		return list;
	}
	
	public String newList(){
		Todolist tdl = new Todolist();		
		//1. Pass object through session
		//HttpSession session = (HttpSession) extcontext.getSession(true);
		//session.setAttribute("person", person);
		
		//2. Pass object through flash	
		flash.put("tdl", tdl);
		
		return PAGE_TDL_EDIT;
	}

	public String editList(Todolist tdl){
		//1. Pass object through session
		//HttpSession session = (HttpSession) extcontext.getSession(true);
		//session.setAttribute("person", person);
		
		//2. Pass object through flash 
		flash.put("tdl", tdl);
		
		return PAGE_TDL_EDIT;
	}

	public String deleteList(Todolist tdl){
		tdlDAO.remove(tdl);
		return PAGE_STAY_AT_THE_SAME;
	}
}
