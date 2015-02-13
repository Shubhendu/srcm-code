package org.srcm.gems.regapp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.srcm.gems.regapp.domain.Country;
import org.srcm.gems.regapp.domain.SeminarStatusType;
import org.srcm.gems.regapp.domain.SeminarType;
import org.srcm.gems.regapp.domain.VolunteerWorkType;

@Repository("lookupDao")
public class LookupDAOImpl implements LookupDAO {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Country> getAllCountries() {
		return em.createNamedQuery("Country.getAllCountries",Country.class).getResultList();
	}

	@Override
	public List<SeminarType> getAllSeminarTypes() {
		
		return em.createNamedQuery("SeminarTheme.getAllTypes",SeminarType.class).getResultList();
	}

	@Override
	public List<VolunteerWorkType> getAllVolunteerWorkTypes() {
		return em.createNamedQuery("VolunteerWorkType.getAllTypes",VolunteerWorkType.class).getResultList();
	}

	@Override
	public List<SeminarStatusType> getAllSeminarStatusTypes() {
		return em.createNamedQuery("SeminarStatusType.getAllTypes",SeminarStatusType.class).getResultList();
	}

	
}
