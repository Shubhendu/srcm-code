package org.srcm.gems.regapp.dao;

import static org.junit.Assert.assertNotNull;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.srcm.gems.regapp.domain.SeminarCustomField;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/root-spring-ctx.xml")
public class SeminarCustomFieldDAOTest {

	@Autowired
	private SeminarCustomFieldDAO semCustFieldDao;
	
	
	@Test
	public void testGetSeminarById() {		
		List<SeminarCustomField> custFldList = semCustFieldDao.getCustomFieldsForSeminar(new Long(1));
		assertNotNull(custFldList);
	}

}
