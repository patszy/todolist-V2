package jpa_tdl.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa_tdl.entities.Role;
import jpa_tdl.entities.UserRole;

@Stateless
public class UserRoleDAO {
	@PersistenceContext
	protected EntityManager em;
	
	public void create(UserRole userrole) {
		em.persist(userrole);
	}

	public UserRole merge(UserRole userrole) {
		return em.merge(userrole);
	}

	public void remove(UserRole userrole) {
		em.remove(em.merge(userrole));
	}

	public UserRole find(Object id) {
		return em.find(UserRole.class, id);
	}
	
	public List<UserRole> getFullList() {
		List<UserRole> list = null;

		Query query = em.createQuery("select ur from UserRole ur");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List<Role> getList(Map<String, Object> searchParams) {
		List<Role> list = null;

		// 1. Build query string with parameters
		String select = "select ur ";
		String from = "from UserRole ur ";
		String where = "";
		String orderby = "order by ur.id_user asc, ur.id_role";

		// search for surname
		String id_user = (String) searchParams.get("id_user");
		if (id_user != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "ur.id_user like :id_user ";
		}
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (id_user != null) {
			query.setParameter("id_user", id_user+"%");
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
