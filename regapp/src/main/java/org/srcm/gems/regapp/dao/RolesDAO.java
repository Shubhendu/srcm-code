package org.srcm.gems.regapp.dao;

import java.util.List;

import org.srcm.gems.regapp.domain.Role;


public interface RolesDAO {
	
	public void addRole(Role role);
	public List<Role> selectAllRoles();
	public List<Role> getRoleByName(String roleName);
	public List<Role> getSeminarRoles();
//	public Role getRoleById(int id);
	public void addUserRoleMapping(int roleId, int userId);
	public void addSeminarUserRoleMapping(int seminarId,int roleId, int userId);
	public void removeUser(int roleId, int userId);
	public void deleteRole(Role role);
	
}
