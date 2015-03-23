package org.srcm.gems.regapp.dao;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.srcm.gems.regapp.domain.Role;
import org.srcm.gems.regapp.domain.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/root-spring-ctx.xml")
public class RolesDAOTest {

	@Autowired
	private RolesDAO rolesDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Test
	public void testAddRole() {
		Role role=new Role();
		role.setRoleName("Role3");
		role.setActive("Y");
		role.setCreationTimeStamp(new Date());
		role.setLastUpdatedTimeStamp(new Date());
		
		rolesDAO.addRole(role);
		
	}
	
	@Test
	public void testAddUser() {
		User user=new User();
		user.setFirstName("shubcsdffffffffffffffffffffff");
		user.setMiddleName("sharadfffffffffffffffff");
		user.setLastName("singhfffffffffffffffffffffffffffffff");
		
		user.setCreationDate(new Date());
		user.setLastUpdatedDate(new Date());
		
		userDAO.addUser(user);
		
	}
	
//	@Test
//	public void testAddUserToRole() {
//		rolesDAO.addUserRoleMapping(1,1);
//		
//	}
	
	
//	@Test
//	public void testGetAllRoles(){
//		List<Role> roles = rolesDAO.selectAllRoles();
//		if(roles != null){
//			System.out.println(roles.size());
//		}else{
//			System.out.println("Roles is null");
//		}
//	}
}
