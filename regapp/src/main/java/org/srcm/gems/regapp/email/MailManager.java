package org.srcm.gems.regapp.email;

import org.srcm.gems.regapp.domain.SeminarRegistrant;

public interface MailManager {
	
	public static final String SUBJECT = "Registration Confirmation";
	public static final String FROM = "donotreply.srcm@gmail.com";
	
	public void sendMail(String from, String to, String subject, String msg);
	
	public void sendConfirmationEmail(SeminarRegistrant registrant);
			
}
