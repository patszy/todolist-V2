package jpa_tdl.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa_tdl.entities.Todolist;

@Stateless
public class TodolistDAO {
	@PersistenceContext
	protected EntityManager em;
	
	public void create(Todolist tdl) {
		em.persist(tdl);
	}

	public Todolist merge(Todolist tdl) {
		return em.merge(tdl);
	}

	public void remove(Todolist tdl) {
		em.remove(em.merge(tdl));
	}

	public Todolist find(Object id) {
		return em.find(Todolist.class, id);
	}
	
	public List<Todolist> getFullList() {
		List<Todolist> list = null;

		Query query = em.createQuery("select tdl from Todolist tdl");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List<Todolist> getList(Map<String, Object> searchParams) {
		List<Todolist> list = null;

		// 1. Build query string with parameters
		String select = "select tdl ";
		String from = "from Todolist tdl ";
		String where = "";
		String orderby = "order by tdl.title asc, tdl.date";

		// search for surname
		String title = (String) searchParams.get("title");
		String user = (String) searchParams.get("user");
		
		if (title != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
//			where += "tdl.title like :title ";
			where += "tdl.idUser like :user ";
		}
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (title != null) {
			query.setParameter("title", title+"%");
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
