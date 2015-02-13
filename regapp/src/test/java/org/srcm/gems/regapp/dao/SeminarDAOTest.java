package org.srcm.gems.regapp.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.srcm.gems.regapp.domain.Seminar;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/root-spring-ctx.xml")
public class SeminarDAOTest {

	@Autowired
	private SeminarDAO semDao;
	
	@Test
	public void testGetAllSeminars() {
		List<Seminar> regs = semDao.getAllSeminars();
		assertTrue(regs.size() >= 1);
	}
	
	@Test
	public void testGetSeminarById() {		
		Seminar sem = semDao.getSeminarByID(new Long(1));
		assertNotNull(sem);
	}

	@Test
	public void testGetCustomFieldsBySeminar() {
		Seminar sem = new Seminar();
		sem.setSeminarId(new Long(13));
		Seminar sem1 = semDao.loadSeminarCustFields(sem);
		assertTrue(sem1.getSeminarCustomFields().size() >= 1);
	}
}
