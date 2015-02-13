package org.srcm.gems.regapp.web.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.srcm.gems.regapp.dao.LookupDAO;
import org.srcm.gems.regapp.dao.SeminarDAO;
import org.srcm.gems.regapp.domain.Country;
import org.srcm.gems.regapp.domain.SeminarStatusType;
import org.srcm.gems.regapp.domain.SeminarType;
import org.srcm.gems.regapp.domain.VolunteerWorkType;
import org.srcm.gems.regapp.web.helper.SeminarWebHelper;

@Component("lookupBean")
@Scope("session")
public class LookupBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3385133933377950681L;

	//@Autowired
	//private LookupDAO lookupDao;
	
	private List<SeminarType> seminarTypes;
	
	private List<Country> countries;
	
	private List<VolunteerWorkType> volunteerWorkTypes;

	private List<SeminarStatusType> seminarStatusTypes;
	
	@PostConstruct
	public void init(){
		
		seminarTypes = getLookupDao().getAllSeminarTypes();
		countries = getLookupDao().getAllCountries();
		volunteerWorkTypes = getLookupDao().getAllVolunteerWorkTypes();
		seminarStatusTypes = getLookupDao().getAllSeminarStatusTypes();
	}
	
	public LookupDAO getLookupDao(){
		
		return (LookupDAO)SeminarWebHelper.getBeanFromElContext("#{lookupDao}");
	}

	public List<SeminarType> getSeminarTypes() {
		return seminarTypes;
	}

	public void setSeminarThemes(List<SeminarType> seminarTypes) {
		this.seminarTypes = seminarTypes;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public List<VolunteerWorkType> getVolunteerWorkTypes() {
		return volunteerWorkTypes;
	}

	public void setVolunteerWorkTypes(List<VolunteerWorkType> volunteerWorkTypes) {
		this.volunteerWorkTypes = volunteerWorkTypes;
	}

	public List<SeminarStatusType> getSeminarStatusTypes() {
		return seminarStatusTypes;
	}

	public void setSeminarStatusTypes(List<SeminarStatusType> seminarStatusTypes) {
		this.seminarStatusTypes = seminarStatusTypes;
	}

	
}