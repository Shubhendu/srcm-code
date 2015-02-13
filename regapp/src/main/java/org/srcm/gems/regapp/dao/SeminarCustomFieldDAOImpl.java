package org.srcm.gems.regapp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.srcm.gems.regapp.domain.SeminarCustomField;

@Repository("seminarCustomFieldDAO")
public class SeminarCustomFieldDAOImpl implements SeminarCustomFieldDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<SeminarCustomField> getCustomFieldsForSeminar(Long seminarId) {
		if(seminarId == null){
			return null;
		}
		return em.createNamedQuery("findCustomFieldsForSeminar",SeminarCustomField.class).setParameter("semId", seminarId).getResultList();
	}

	
}
