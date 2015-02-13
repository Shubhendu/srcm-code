package org.srcm.gems.regapp.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.srcm.gems.regapp.domain.Seminar;
import org.srcm.gems.regapp.domain.SeminarRegistrant;

@Repository("registrationDao")
public class SeminarRegistrantDAOImpl implements SeminarRegistrantDAO{

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public void saveResitration(SeminarRegistrant registration) {
		
		if(registration.getSeminarRegistrantId() != null){
			em.merge(registration);
		}else{
			em.persist(registration);
		}
		
		em.flush();
		
	}

	@Override
	@Transactional
	public SeminarRegistrant findRegistration(String lastName, String email, Long seminarId) {
		
		try{
			return em.createNamedQuery("Registration.findByLastnameAndEmailId", SeminarRegistrant.class)
						.setParameter("email", email).setParameter("lastName", lastName)
						.setParameter("seminarId", seminarId).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	@Override
	@Transactional
	public SeminarRegistrant findRegistration(String registrationNo) {
		
		try{
			return em.createNamedQuery("Registration.findByRegistrationId", SeminarRegistrant.class)
						.setParameter("seminarRegistrantId", Long.parseLong(registrationNo)).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	@Override
	public List<Seminar> getAllSeminars() {
		
		return em.createNamedQuery("Seminar.getAll", Seminar.class).getResultList();
	}

	@Override
	public List<SeminarRegistrant> getAllRegistrationsBySeminar(Seminar seminar) {
		
		List<SeminarRegistrant> regs = em.createNamedQuery("Registration.getAllRegistrationsBySeminar", SeminarRegistrant.class)
											.setParameter("seminarId", seminar.getSeminarId()).getResultList();
		
		List<SeminarRegistrant> regsWithChildren = new ArrayList<SeminarRegistrant>();
		
		for(SeminarRegistrant sr : regs){
			regsWithChildren.add(sr);
			regsWithChildren.addAll(sr.getSeminarRegistrants());
		}
		
		return regsWithChildren;
	}
	
	
	@Override
	@Transactional
	public void remove(SeminarRegistrant registrant){
		Query q = em.createNamedQuery("SeminarRegistrantCustFld.deleteSeminarRegistrantCustFld");
		q.setParameter("seminarRegistrantId", registrant.getSeminarRegistrantId()).executeUpdate();
		
		Query query = em.createNamedQuery("Registration.deleteSeminarRegistrant");
		query.setParameter("seminarRegistrantId", registrant.getSeminarRegistrantId()).executeUpdate();
	}

}
