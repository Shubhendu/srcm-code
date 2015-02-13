package org.srcm.gems.regapp.web;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("registrationWebBeanProto")
@Scope("prototype")
public class RegistrationWebBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9029232885453722689L;

	private Long seminarId;
	
	public Long getSeminarId() {
		return seminarId;
	}

	public void setSeminarId(Long seminarId) {
		this.seminarId = seminarId;
	}

	public RegistrationWebBean(){
	}

	
}
