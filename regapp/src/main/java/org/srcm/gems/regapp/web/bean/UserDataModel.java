package org.srcm.gems.regapp.web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.srcm.gems.regapp.domain.User;


@ManagedBean
@SessionScoped
public class UserDataModel implements Serializable{

	private static final long serialVersionUID = 1L;
	public List<User> users;
	
	public List<User> getUsers(){
		return users= new ArrayList<User>();
	}
}
