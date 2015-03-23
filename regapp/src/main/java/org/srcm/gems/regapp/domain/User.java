package org.srcm.gems.regapp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Component("userProto")
@Scope("prototype")
@Entity
@Table(name="USERS")
@NamedQueries({
	@NamedQuery(name="User.getAllUsers", query="SELECT users FROM User users"),
})
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonIgnoreProperties({"roles","seminarRoleUserMapping"})
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
	
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "USER_ROLE_MAPPING", 
	joinColumns = { @JoinColumn(name = "USER_ID") }, 
	inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") }, 
	uniqueConstraints = { @UniqueConstraint(columnNames = {"ROLE_ID", "USER_ID" }) })
//	@JsonBackReference
    private Set<Role> roles = new HashSet<Role>();
	
//	@OneToMany(mappedBy="user")
////	@JsonBackReference
//	private Set<SeminarUserRoleMapping> seminarRoleUserMapping;
    
	@Transient
	private String roleNames;
	
//	@ManyToOne
//	@JoinColumn(name="seminar_id")
//	private Seminar seminarOrig ;

	

//	public Seminar getSeminarOrig() {
//		return seminarOrig;
//	}
//
//	public void setSeminarOrig(Seminar seminarOrig) {
//		this.seminarOrig = seminarOrig;
//	}

	public User() {
    }
 
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles.clear();
		this.roles.addAll(roles);
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

	


	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
	
	@Override
	public int hashCode() {
		int x = 97;
		int hashCode = x * this.getId();
		hashCode += x * this.firstName.hashCode();
		return hashCode;

	}
	
	@Override
	public boolean equals( Object obj )
	{
		boolean flag = false;
		User user = ( User )obj;
		if( user.getId() == getId() ){
			if(user.getFirstName().equals(this.firstName)){
				flag = true;
			}
		}
		return flag;
	}
	
}
