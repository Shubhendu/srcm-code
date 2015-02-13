package org.srcm.gems.regapp.email;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.srcm.gems.regapp.domain.Seminar;
import org.srcm.gems.regapp.domain.SeminarRegistrant;
import org.srcm.gems.regapp.web.RegistrantWebBean;

public class MailManagerImpl implements MailManager {

	private static Logger logger = LoggerFactory
			.getLogger(MailManagerImpl.class);
	
	private JavaMailSender mailSender;
	private VelocityEngine velocityEngine;
	
	public void sendConfirmationEmail(final SeminarRegistrant registrant){
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                Seminar seminar = registrant.getSeminarOrig();
                
                SimpleDateFormat sf = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm a z");
                
            	MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(registrant.getEmail());
                if(registrant.getSeminarOrig() != null && registrant.getSeminarOrig().getEmail() != null){
                	message.setCc(registrant.getSeminarOrig().getEmail());
                }
                message.setFrom(FROM); 
                message.setSubject(SUBJECT);
                
                Map<String, Object> model = new HashMap<String, Object>();
                model.put("seminarName", registrant.getSeminarOrig().getDesc());
                model.put("registrationNumber", registrant.getSeminarRegistrantId().toString());
                model.put("address", seminar.getAddress());
                model.put("city", seminar.getCity());
                model.put("zip", seminar.getZip());
                model.put("coordinator", seminar.getCoordinator());
                model.put("firstName", registrant.getFirstName());
                model.put("lastName", registrant.getLastName());
                model.put("arrivalDate",registrant.getArrivalDate()!=null?sf.format(registrant.getArrivalDate()):"");
                model.put("departureDate", registrant.getDepartureDate()!=null?sf.format(registrant.getDepartureDate()):"");
                if(registrant.getArrivalTime() != null){
                	model.put("arrivalTime",registrant.getArrivalTime().toString());
                }
                else{
                	model.put("arrivalTime","");
                }
                if(registrant.getDepartureTime() != null){
                	model.put("departureTime", registrant.getDepartureTime().toString());
                }
                else{
                	model.put("departureTime","");
                }
                model.put("otherRegistrants", registrant.getSeminarRegistrants());
                model.put("coordinatorPhone", seminar.getPhone());
                model.put("coordinatorEmail", seminar.getEmail());
                model.put("seminarLocation", seminar.getLocation());
                model.put("amountPaid", registrant.getAmountToPay());
                model.put("ccTransactionNumber", registrant.getCcTransactionNo());
                model.put("paidThruWeb", registrant.getPayThruWeb());
                
                String emailTemplate = "default-email-template.vm";
                
                if(seminar.getType() == 4){
                	emailTemplate = "ashram-visit-email-template.vm";
                	model.put("seminarDescription", seminar.getDesc());
                }
                String text = VelocityEngineUtils.mergeTemplateIntoString(
                        velocityEngine, emailTemplate, model);
                message.setText(text, true);
            }
        };
        
        
        	try {
				this.mailSender.send(preparator);
			} catch (Exception e) {
				logger.warn("Cannot send mail using mail sender: ",e.getLocalizedMessage());
			}
        
        
	}
	
	public void sendMail(String from, String to, String subject, String msg) {
		SimpleMailMessage message = new SimpleMailMessage();
 
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(msg);
	}
	
	
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
}
