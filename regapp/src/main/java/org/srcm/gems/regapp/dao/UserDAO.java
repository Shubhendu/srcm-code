/**
 * 
 */
package org.srcm.gems.regapp.dao;

import java.util.List;
import java.util.Set;

import org.srcm.gems.regapp.domain.User;

/**
 * @author singh_sh
 *
 */
public interface UserDAO {
	
	public void addUser(User user);
	public List<User> getAllUsers();
	public User getUserByName(String firstName,String lastName);
	public Set<User> gerUsersBySeminarId(Long seminarId);
//	public User getUserById(int id);
	void addUserToRole(int userId, int roleId);
	void removeRole(int userId, int roleId);
	User getUserById(int userId);

}
