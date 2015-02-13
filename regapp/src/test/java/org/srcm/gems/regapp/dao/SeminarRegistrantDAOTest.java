package org.srcm.gems.regapp.dao;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.srcm.gems.regapp.domain.Seminar;
import org.srcm.gems.regapp.domain.SeminarRegistrant;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/root-spring-ctx.xml")
public class SeminarRegistrantDAOTest {

	@Autowired
	private SeminarRegistrantDAO semRegDao;
	
	@Test
	public void testGetAllRegistrationsBySeminar() {
		Seminar sem = new Seminar();
		sem.setSeminarId(new Long(13));
		List<SeminarRegistrant> regs = semRegDao.getAllRegistrationsBySeminar(sem);
		//assertTrue(regs.size() == 1);
	}

}
