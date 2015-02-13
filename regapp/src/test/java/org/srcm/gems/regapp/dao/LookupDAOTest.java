package org.srcm.gems.regapp.dao;


import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.srcm.gems.regapp.domain.Country;
import org.srcm.gems.regapp.domain.SeminarStatusType;
import org.srcm.gems.regapp.domain.SeminarType;
import org.srcm.gems.regapp.domain.VolunteerWorkType;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/root-spring-ctx.xml")
public class LookupDAOTest {

	@Autowired
	private LookupDAO lookupDao;
	
	@Test
	public void testGetAllCountries() {
		List<Country> regs = lookupDao.getAllCountries();
		assertTrue(regs.size() >= 1);
	}
	

	@Test
	public void testGetAllSeminarStatusTypes() {
		List<SeminarStatusType> regs = lookupDao.getAllSeminarStatusTypes();
		assertTrue(regs.size() >= 1);
	}
	

	@Test
	public void testGetAllSeminarTypes() {
		List<SeminarType> regs = lookupDao.getAllSeminarTypes();
		assertTrue(regs.size() >= 1);
	}
	

	@Test
	public void testGetAllVolunteerWorkTypes() {
		List<VolunteerWorkType> regs = lookupDao.getAllVolunteerWorkTypes();
		assertTrue(regs.size() >= 1);
	}
	
	
}
