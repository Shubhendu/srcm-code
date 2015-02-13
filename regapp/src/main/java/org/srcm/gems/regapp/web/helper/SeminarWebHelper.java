package org.srcm.gems.regapp.web.helper;

import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Component;
import org.srcm.gems.regapp.dao.SeminarDAO;
import org.srcm.gems.regapp.domain.Seminar;
import org.srcm.gems.regapp.domain.SeminarCustomField;
import org.srcm.gems.regapp.web.bean.SeminarWebBean;

@Component("seminarHelper")
public class SeminarWebHelper implements PhaseListener{

	@Autowired
	private SeminarDAO seminarDao;
	
	public void initBeans(SeminarWebBean seminarWebBean){
		
		//Load relation so that it won't throw LazyInitializationException
		seminarWebBean.setSelectedSeminar(seminarDao.loadSeminarCustFields(seminarWebBean.getSelectedSeminar()));
		
		//Needed a non-null selected field for edit-seminat.xhtml (popup included in the screen)
		if(seminarWebBean.getSelectedField() == null){
			seminarWebBean.setSelectedField(new SeminarCustomField());
		}
	}
	
	public SeminarWebBean initSelectedField(SeminarWebBean seminarWebBean){
		
		seminarWebBean.setSelectedField(new SeminarCustomField());
		return seminarWebBean;
		
	}
	
	
	
	public static Object getBeanFromElContext(String beanName){
		
		
		final ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		final Application application = FacesContext.getCurrentInstance().getApplication();
		ExpressionFactory expressionFactory = application.getExpressionFactory();
		ValueExpression exp = expressionFactory.createValueExpression(elContext, beanName, Object.class);
		Object result = exp.getValue(elContext);
		
		return result;
		
	}

	@Override
	public void afterPhase(PhaseEvent phaseEvent) {
		System.out.println("afterPhase = "+phaseEvent.getPhaseId());
		Iterator<FacesMessage> fm = phaseEvent.getFacesContext().getMessages();
		while(fm.hasNext()){
			System.out.println(fm.next().getDetail());
		}
		
	}

	@Override
	public void beforePhase(PhaseEvent phaseEvent) {
		System.out.println("beforePhase = "+phaseEvent.getPhaseId());
		Iterator<FacesMessage> fm = phaseEvent.getFacesContext().getMessages();
		while(fm.hasNext()){
			System.out.println(fm.next().getDetail());
		}
		
	}

	@Override
	public PhaseId getPhaseId() {
		// TODO Auto-generated method stub
		return PhaseId.ANY_PHASE;
	}
}
