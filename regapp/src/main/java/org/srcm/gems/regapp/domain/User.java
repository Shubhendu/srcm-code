package org.srcm.gems.regapp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="USERS")
@NamedQueries({
	@NamedQuery(name="User.getAllUsers", query="SELECT users FROM User users"),
})
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8061951293939101208L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="USER_ID")
	private int id;
	
	@Column(name="FIRST_NAME")
	private String firstName;
	
	@Column(name="LAST_NAME")
	private String lastName;
	
	@Column(name="MIDDLE_NAME")
	private String middleName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_TIMESTAMP")
	private Date creationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATED_TIMESTAMP")
	private Date lastUpdatedDate;
	
	
	@ManyToMany(cascade = {CascadeType.PERSIST},mappedBy="users")
	@JsonManagedReference
    private Set<Role> roles = new HashSet<Role>();
	
	@OneToMany(mappedBy="user")
	@	JsonManagedReference
	private Set<SeminarUserRoleMapping> seminarRoleUserMapping;
    
	@Transient
	private String roleNames;

	public User() {
    }
 
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Set<SeminarUserRoleMapping> getSeminarRoleUserMapping() {
		return seminarRoleUserMapping;
	}

	public void setSeminarRoleUserMapping(
			Set<SeminarUserRoleMapping> seminarRoleUserMapping) {
		this.seminarRoleUserMapping = seminarRoleUserMapping;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	
}
