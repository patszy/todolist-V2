package jpa_tdl.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa_tdl.entities.Todoitem;
import jpa_tdl.entities.Todolist;
import jpa_tdl.entities.User;

@Stateless
public class TodoitemDAO {
	@PersistenceContext
	protected EntityManager em;
	
	public void create(Todoitem tditem) {
		em.persist(tditem);
	}

	public Todoitem merge(Todoitem tditem) {
		return em.merge(tditem);
	}

	public void remove(Todoitem tditem) {
		em.remove(em.merge(tditem));
	}

	public Todoitem find(Object id) {
		return em.find(Todoitem.class, id);
	}
	
	public List<Todoitem> getFullList() {
		List<Todoitem> list = null;

		Query query = em.createQuery("select tdi from Todoitem tdi");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List<Todoitem> getList(Map<String, Object> searchParams) {
		List<Todoitem> list = null;

		// 1. Build query string with parameters
		String select = "select i ";
		String from = "from Todoitem i ";
		String where = "";
		String orderby = "order by i.deadline";

		// search for surname
		String title = (String) searchParams.get("title");
		Todolist tdl = (Todolist) searchParams.get("tdl");
		
		if (title != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "i.title like :title ";
		}
		
		if (title != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "i.todolist like :tdl ";
		}
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (title != null) {
			query.setParameter("title", title+"%");
			query.setParameter("tdl", tdl+"%");
		}

		// ... other parameters ... 

		// 4. Execute query and retrieve list of Person objects
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
