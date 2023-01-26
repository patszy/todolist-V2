package jpa_tdl.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa_tdl.entities.Role;
import jpa_tdl.entities.User;
import jpa_tdl.entities.UserRole;

@Stateless
public class UserDAO {
	@PersistenceContext
	protected EntityManager em;
	
	
	@EJB
	private RoleDAO roleDAO;
	
	public void create(User user, String name) {
		em.persist(user);
		em.flush();
		
		// 1. Get role object by name.
		Role r =  roleDAO.getRoleByName(name);
		
		if(r != null) {
			// 2. Create new UserRole;
			
			UserRole ur = new UserRole();
			
			// 3. Connect role with user 
			
			ur.setRole(r);
			ur.setUser(user);
			
			//4. Create object UserRole.
			
			em.persist(ur);
		}
	}

	public User merge(User user) {
		return em.merge(user);
	}

	public void remove(User user) {
		em.remove(em.merge(user));
	}

	public User find(Object id) {
		return em.find(User.class, id);
	}
	
	public List<User> getFullList() {
		List<User> list = null;

		Query query = em.createQuery("select u from User u");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List<User> getList(Map<String, Object> searchParams) {
		List<User> list = null;

		// 1. Build query string with parameters
		String select = "select u ";
		String from = "from User u ";
		String where = "";
		String orderby = "order by u.login asc";

		// search for surname
		String login = (String) searchParams.get("login");
		if (login != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "u.login like :login ";
		}
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (login != null) {
			query.setParameter("login", login+"%");
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
	
	public User getUserByLoginPassword(String login, String pass) {
		
		User u = null;
		
		String select = "select u ";
		String from = "from User u ";
		String where = "";
		
		if (login != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += " and ";
			}
			where += "u.login like :login";
		}
		
		if (pass != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += " and ";
			}
			where += "u.password like :pass";
		}
		
		Query query = em.createQuery(select + from + where);
		
		if (login != null) {
			query.setParameter("login", login+"%");
		}
		
		if (pass != null) {
			query.setParameter("pass", pass+"%");
		}
		
		try {
			u = (User) query.getSingleResult();
		} catch (NoResultException e) {
//			e.printStackTrace();
		}

		return u;
	}

	// simulate retrieving roles of a User from DB
	public List<String> getUserRolesByUser(User user) {
		
		ArrayList<String> roles = new ArrayList<String>();
		List<UserRole> ur = null;
		int idUser = user.getIdUser();
		
		Query query = em.createQuery("select ur from UserRole ur where ur.user.idUser like :idUser");
		
		query.setParameter("idUser", idUser);
		
		try {
			ur = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (UserRole uRole: ur) {
			roles.add(uRole.getRole().getName());
		}
		
		return roles;
	}

}
