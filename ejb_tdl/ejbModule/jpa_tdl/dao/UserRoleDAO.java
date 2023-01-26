package jpa_tdl.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa_tdl.entities.Role;
import jpa_tdl.entities.User;
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
	
	public List<UserRole> getList(Map<String, Object> searchParams) {
		List<UserRole> list = null;

		// 1. Build query string with parameters
		String select = "select ur ";
		String from = "from UserRole ur ";
		String where = "";

		// search for surname
		User user = (User) searchParams.get("user");
		if (user.getIdUser() != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "ur.user.idUser like :id_user ";
		}
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where);

		// 3. Set configured parameters
		if (user.getIdUser() != null) {
			query.setParameter("id_user", user.getIdUser());
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
	
	public UserRole getUserRoleByUserRole(User user, Role role) {
		UserRole ur = null;
		
		String select = "select ur ";
		String from = "from UserRole ur ";
		String where = "";

		// search for surname
		if (user.getIdUser() != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "ur.user.idUser like :iduser ";
		}
		
		if (role.getIdRole() != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "ur.role.idRole like :idrole ";
		}
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where);

		// 3. Set configured parameters
		if (user.getIdUser() != null) {
			query.setParameter("iduser", user.getIdUser());
		}

		if (role.getIdRole() != null) {
			query.setParameter("idrole", role.getIdRole());
		}
		// ... other parameters ... 

		// 4. Execute query and retrieve list of Person objects
		try {
			ur = (UserRole) query.getSingleResult();
		} catch (NoResultException e) {
//			e.printStackTrace();
		}
		
		return ur;
	}

}
