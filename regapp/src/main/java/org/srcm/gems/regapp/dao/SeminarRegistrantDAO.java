package org.srcm.gems.regapp.dao;

import java.util.List;

import org.srcm.gems.regapp.domain.Seminar;
import org.srcm.gems.regapp.domain.SeminarRegistrant;

public interface SeminarRegistrantDAO {

	public void saveResitration(SeminarRegistrant registration);
	
	public SeminarRegistrant findRegistration(String lastName, String email, Long seminarId);
	
	public SeminarRegistrant findRegistration(String registrationId);
	
	public List<Seminar> getAllSeminars();
	
	public List<SeminarRegistrant> getAllRegistrationsBySeminar(Seminar seminar);

	public void remove(SeminarRegistrant registrant);
	
}
