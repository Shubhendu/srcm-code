package org.srcm.gems.regapp.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="SEMINAR_TYPE")
@NamedQueries({
	@NamedQuery(name="SeminarTheme.getAllTypes", query="SELECT st FROM SeminarType st")
})
public class SeminarType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1801160052436167903L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="TYPE")
	private String type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
