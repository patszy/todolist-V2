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
	
	public void create(Todolist tdlist) {
		em.persist(tdlist);
	}

	public Todolist merge(Todolist tdlist) {
		return em.merge(tdlist);
	}

	public void remove(Todolist tdlist) {
		em.remove(em.merge(tdlist));
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
		if (title != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "tdl.title like :title ";
		}
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (title != null) {
			query.setParameter("login", title+"%");
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
