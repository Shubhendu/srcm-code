/**
 * 
 */
package org.srcm.gems.regapp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.srcm.gems.regapp.domain.Role;
import org.srcm.gems.regapp.domain.User;

/**
 * @author singh_sh
 *
 */
@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

	@PersistenceContext
	private EntityManager em;

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	@Override
	@Transactional
	public void addUser(User user) {
		em.persist(user);
		em.flush();

	}

	@Override
	public List<User> getAllUsers() {
		List<User> users =  em.createNamedQuery("User.getAllUsers", User.class).getResultList();
		
		System.out.println("Number of user records: "+users.size());
		
		return users;
	}

	@Override
	public User getUserByName(String firstName,String lastName) {
		List<User> users = em.createQuery("select u from User u where u.firstName like :firstName and u.lastName like :lastName")
				.setParameter("firstName", firstName)
				.getResultList();

		if(users !=null && !users.isEmpty()){
			return users.get(0);
		}else{
			return null;
		}
	}
	
//	@Override
//	public User getUserById(int userId) {
//		List<User> users = em.createQuery("select u from User u where u.id like :userId")
//				.setParameter("userId", userId)
//				.getResultList();
//
//		if(users !=null && !users.isEmpty()){
//			return users.get(0);
//		}else{
//			return null;
//		}
//	}
	
	@Override
	@Transactional
	public void addUserToRole(int userId, int roleId) {
		
			Role role = (Role) em.createQuery("select r from Role r where r.id like :roleId")
					             .setParameter("roleId", roleId)
					             .getSingleResult();
			
			User user = (User) em.createQuery("select u from User u where u.id like :userId")
		             .setParameter("userId", userId)
		             .getSingleResult();
			
			user.getRoles().add(role);
			em.persist(user);
			em.flush();
	}
	
	@Override
	@Transactional
	public void removeRole(int userId, int roleId) {
			Role role = (Role) em.createQuery("select r from Role r where r.id=:roleId")
					             .setParameter("roleId", roleId)
					             .getSingleResult();
			
			User user = (User) em.createQuery("select u from User u where u.id=:userId")
		             .setParameter("userId", userId)
		             .getSingleResult();
			
			user.getRoles().remove(role);
			em.persist(user);
			em.flush();
	}

}
