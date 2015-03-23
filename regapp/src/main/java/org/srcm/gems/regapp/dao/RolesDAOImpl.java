package org.srcm.gems.regapp.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.srcm.gems.regapp.domain.Role;
import org.srcm.gems.regapp.domain.User;

@Repository("rolesDao")
public class RolesDAOImpl implements RolesDAO{
	
	private SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");

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
	public void addRole(Role role) {
		
		em.persist(role);
		em.flush();
	}

	@Override
	@Transactional
	public void deleteRole(Role role) {
		em.createQuery("delete Role r where r.id like :roleId")
		.setParameter("roleId", role.getId()).executeUpdate();
//		em.remove(role);
		em.flush();
	}
	
	@Override
	public List<Role> selectAllRoles() {
		List<Role> roles = em.createNamedQuery("Role.getAllRoles", Role.class).getResultList();
		System.out.println("Number of records: "+roles.size());
		return roles;
	}

	@Override
	public Role getRoleByName(String roleName) {
		List<Role> roles = em.createQuery("select r from Role r where r.roleName like :roleName")
				.setParameter("roleName", roleName)
				.getResultList();

		if(roles !=null && !roles.isEmpty()){
			return roles.get(0);
		}else{
			return null;
		}
	}
	
//	@Override
//	public Role getRoleById(int roleId) {
//		List<Role> roles = em.createQuery("select r from Role r where r.id like :roleId")
//				.setParameter("roleId", roleId)
//				.getResultList();
//
//		if(roles !=null && !roles.isEmpty()){
//			return roles.get(0);
//		}else{
//			return null;
//		}
//	}

	@Override
	@Transactional
	public void addUserRoleMapping(int roleId, int userId) {
		
			Role role = (Role) em.createQuery("select r from Role r where r.id like :roleId")
					             .setParameter("roleId", roleId)
					             .getSingleResult();
			
			User user = (User) em.createQuery("select u from User u where u.id like :userId")
		             .setParameter("userId", userId)
		             .getSingleResult();
			
			role.getUsers().add(user);
			em.persist(role);
			em.flush();
	}
	
	@Override
	@Transactional
	public void removeUser(int roleId, int userId) {
			Role role = (Role) em.createQuery("select r from Role r where r.id=:roleId")
					             .setParameter("roleId", roleId)
					             .getSingleResult();
			
			User user = (User) em.createQuery("select u from User u where u.id=:userId")
		             .setParameter("userId", userId)
		             .getSingleResult();
			
			role.getUsers().remove(user);
			em.persist(role);
			em.flush();
	}
	
}
