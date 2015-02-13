package org.srcm.gems.regapp.dao;

import java.util.List;

import org.srcm.gems.regapp.domain.Country;
import org.srcm.gems.regapp.domain.SeminarStatusType;
import org.srcm.gems.regapp.domain.SeminarType;
import org.srcm.gems.regapp.domain.VolunteerWorkType;

public interface LookupDAO {

	List<Country> getAllCountries();
	
	List<SeminarType> getAllSeminarTypes();

	List<VolunteerWorkType> getAllVolunteerWorkTypes();

	List<SeminarStatusType> getAllSeminarStatusTypes();
}
