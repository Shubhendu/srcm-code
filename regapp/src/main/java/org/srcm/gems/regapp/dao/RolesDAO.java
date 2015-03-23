package org.srcm.gems.regapp.dao;

import java.util.List;

import org.srcm.gems.regapp.domain.Role;


public interface RolesDAO {
	
	public void addRole(Role role);
	public List<Role> selectAllRoles();
	public Role getRoleByName(String roleName);
//	public Role getRoleById(int id);
	public void addUserRoleMapping(int roleId, int userId);
	public void removeUser(int roleId, int userId);
	public void deleteRole(Role role);
	
}
