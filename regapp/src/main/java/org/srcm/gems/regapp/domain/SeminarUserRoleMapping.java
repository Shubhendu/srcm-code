/**
 * 
 */
package org.srcm.gems.regapp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author singh_sh
 *
 */
@Entity
@Table(name="SEMINAR_USER_ROLE_MAPPING")
public class SeminarUserRoleMapping {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="SEMINAR_USER_ROLE_MAPPING_ID",nullable=false)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="USER_ID")
	private User user;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ROLE_ID")
	private Role role;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEMINAR_ID")
	private Seminar seminar;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Seminar getSeminar() {
		return seminar;
	}

	public void setSeminar(Seminar seminar) {
		this.seminar = seminar;
	}
	
	
	

}
