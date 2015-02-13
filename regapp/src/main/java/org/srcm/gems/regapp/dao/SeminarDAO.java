package org.srcm.gems.regapp.dao;

import java.util.List;

import org.srcm.gems.regapp.domain.Seminar;

public interface SeminarDAO {

	public List<Seminar> getAllSeminars();
	
	public Seminar createOrSaveSeminar(Seminar seminar);
	
	public Seminar loadSeminarCustFields(Seminar seminar);
	
	public boolean isRegitrationsForSeminar(Seminar seminar);
	
	public Seminar getSeminarByID(Long seminarId);
}
