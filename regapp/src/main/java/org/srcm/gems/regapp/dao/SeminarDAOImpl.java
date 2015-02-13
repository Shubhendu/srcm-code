package org.srcm.gems.regapp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.srcm.gems.regapp.domain.Seminar;

@Repository("seminarDao")
public class SeminarDAOImpl implements SeminarDAO {

	@PersistenceContext
	private EntityManager em;

	
	@Override
	@Transactional
	public List<Seminar> getAllSeminars() {
		
		return em.createNamedQuery("Seminar.getAllSeminars", Seminar.class).getResultList();
		
	}


	@Override
	@Transactional
	public Seminar createOrSaveSeminar(Seminar seminar) {
		
		if(seminar.getSeminarId() != null){
			seminar = em.merge(seminar);
		}else{
			em.persist(seminar);
		}
		
		em.flush();
		
		return seminar;
	}


	@Override
	@Transactional
	public Seminar loadSeminarCustFields(Seminar seminar) {
		
		if(seminar.getSeminarId() == null){
			return seminar;
		}
		return em.createNamedQuery("Seminar.loadCustFields",Seminar.class).setParameter("semId", seminar.getSeminarId()).getSingleResult();
	}


	@Override
	@Transactional
	public boolean isRegitrationsForSeminar(Seminar seminar) {
		
		return em.createNamedQuery("Seminar.seminarHasRegistrants",Long.class).setParameter("semId", seminar.getSeminarId()).getSingleResult().longValue() > 0;
	}


	@Override
	public Seminar getSeminarByID(Long seminarId) {
		if(seminarId == null){
			return null;
		}
		return em.createNamedQuery("Seminar.getSeminarByID",Seminar.class).setParameter("semId", seminarId).getSingleResult();
	}

}
