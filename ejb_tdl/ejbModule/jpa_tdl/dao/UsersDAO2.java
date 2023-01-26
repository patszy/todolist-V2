package jpa_tdl.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa_tdl.entities.Role;
import jpa_tdl.entities.User;
import jpa_tdl.entities.UserRole;

@Stateless
public class UsersDAO2 {
	@PersistenceContext
	protected EntityManager em;
	
	
	@EJB
	private RoleDAO roleDAO;
	
	public void create(User user) {
		System.out.println(user.getLogin() + ":" + user.getIdUser());
		
		em.persist(user);
		em.flush();
		
		// 1. Pobranie obiektu roli "user".
		Role r =  roleDAO.getRoleByName("user");
		
		System.out.println(r);
		
		if(r != null) {
			// 2. Stworzenie nowego obiektu UserRole;
			
			UserRole ur = new UserRole();
			
			// 3. Powiązanie roli z userem 
			
			ur.setRole(r);
			ur.setUser(user);
			
			//4. Utrwalenie obiektu UserRole.
			
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
		
		if (login != null && pass != null) {
			query.setParameter("login", login+"%");
			query.setParameter("pass", pass+"%");
		}
		
		try {
			u = (User) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return u;
	}

	// simulate retrieving roles of a User from DB
	public List<String> getUserRolesByUser(User user) {
		
		ArrayList<String> roles = new ArrayList<String>();
		UserRole ur = null;
		int idUser = user.getIdUser();
		
		Query query = em.createQuery("select ur from UserRole ur where ur.user.idUser like :idUser");
		
		query.setParameter("idUser", idUser);
		
		try {
			ur = (UserRole) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		roles.add(ur.getRole().getName());
		
		return roles;
	}

}
