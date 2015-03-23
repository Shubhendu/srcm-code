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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="ROLES")

@NamedQueries({
	@NamedQuery(name="Role.getAllRoles", query="SELECT roles FROM Role roles"),
})

//@JsonManagedReference is used to annotate the inverse side while @JsonBackReference maps the owning side of the relationship.

public class Role implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2021155623856335726L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ROLE_ID")
	private int id;

	@Column(name="ROLE_NAME")
	private String roleName;
	
	@Column(name="ACTIVE")
	private String active;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_TIMESTAMP")
	private Date creationTimeStamp;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATED_TIMESTAMP")
	private Date lastUpdatedTimeStamp;
	
    
	
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "USER_ROLE_MAPPING", 
	joinColumns = { @JoinColumn(name = "ROLE_ID") }, 
	inverseJoinColumns = { @JoinColumn(name = "USER_ID") }, 
	uniqueConstraints = { @UniqueConstraint(columnNames = {"ROLE_ID", "USER_ID" }) })
	@JsonBackReference
	private Set<User> users = new HashSet<User>();
	
	
	@OneToMany(mappedBy="role")
	@JsonManagedReference
	private Set<SeminarUserRoleMapping> seminarUserRoleMappings;
    
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public Date getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(Date creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}

	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}
}
