package org.srcm.gems.regapp.web.helper;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.srcm.gems.regapp.web.RegistrantWebBean;
import org.srcm.gems.regapp.web.util.AgeRange;
import org.srcm.gems.regapp.web.util.DonationAccountType;

public class CreditCardHelper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8737243932727963997L;
	
	private static Logger logger = LoggerFactory
			.getLogger(CreditCardHelper.class);

	public AuthNetBean buildAuthNetParams(String amountToPay, short donationAccountType)
	{
		//Perform Credit Card Transaction
		//Read the defaulted required properties	
		ResourceBundle rb = ResourceBundle.getBundle("authnet");
		
		AuthNetBean authNetBean = new AuthNetBean();
		
		logger.debug("Donation Account Type - " + donationAccountType);
		authNetBean.gatewayUrl		= rb.getString("GATEWAY_URL").trim();
		authNetBean.loginID 		= rb.getString("x_Login").trim();
		authNetBean.transactionKey	= rb.getString("x_Tran_Key").trim();
		authNetBean.currency  		= rb.getString("CURRENCY_CODE").trim();	
		authNetBean.testMode		= rb.getString("x_Test_Request");
		authNetBean.version			= rb.getString("x_Version");
		
		if (donationAccountType == DonationAccountType.SMSF.getValue())
		{
			authNetBean.gatewayUrl		= rb.getString("SMSF_GATEWAY_URL").trim();
			authNetBean.loginID 		= rb.getString("SMSF_x_Login").trim();
			authNetBean.transactionKey	= rb.getString("SMSF_x_Tran_Key").trim();
			authNetBean.testMode		= rb.getString("SMSF_x_Test_Request");
			authNetBean.version			= rb.getString("SMSF_x_Version");
		}

		
		// a sequence number is randomly generated
		Random generator = new Random();
		authNetBean.sequence = generator.nextInt(1000);
		// a timestamp is generated
		authNetBean.timeStamp = System.currentTimeMillis()/1000;
		
		//This section uses Java Cryptography functions to generate a fingerprint
		// First, the Transaction key is converted to a "SecretKey" object
		try {
			KeyGenerator kg = KeyGenerator.getInstance("HmacMD5");
			SecretKey key = new SecretKeySpec(authNetBean.transactionKey.getBytes(), "HmacMD5");
			// A MAC object is created to generate the hash using the HmacMD5 algorithm
			Mac mac = Mac.getInstance("HmacMD5");
			mac.init(key);
			String inputstring = authNetBean.loginID + "^" + authNetBean.sequence + "^" + authNetBean.timeStamp + "^" + amountToPay + "^" + authNetBean.currency;
			byte[] result = mac.doFinal(inputstring.getBytes());
			// Convert the result from byte[] to hexadecimal format
			StringBuffer strbuf = new StringBuffer(result.length * 2);
			for(int i=0; i< result.length; i++)
			{
				if(((int) result[i] & 0xff) < 0x10)
					strbuf.append("0");
				strbuf.append(Long.toString((int) result[i] & 0xff, 16));
			}
			authNetBean.fingerPrint = strbuf.toString();
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// end of fingerprint generation
		
		
		Locale currentLocale = java.util.Locale.getDefault();
		System.out.println("currentLocale:"+currentLocale.toString());
		if(currentLocale != null){
			authNetBean.languageCode=currentLocale.getLanguage();
		}
		
		return authNetBean;
	}
	
	public class AuthNetBean {
		private String gatewayUrl;
		private String loginID;
		private String transactionKey;
		private String currency;	
		private String testMode;
		private String version;
		private String fingerPrint;
		private String languageCode;
		private long timeStamp;
		private int sequence;
		private String returnUrl;

		public String getGatewayUrl() {
			return gatewayUrl;
		}
		public void setGatewayUrl(String gatewayUrl) {
			this.gatewayUrl = gatewayUrl;
		}
		public String getLoginID() {
			return loginID;
		}
		public void setLoginID(String loginID) {
			this.loginID = loginID;
		}
		public String getTransactionKey() {
			return transactionKey;
		}
		public void setTransactionKey(String transactionKey) {
			this.transactionKey = transactionKey;
		}
		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public String getTestMode() {
			return testMode;
		}
		public void setTestMode(String testMode) {
			this.testMode = testMode;
		}
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public String getFingerPrint() {
			return fingerPrint;
		}
		public void setFingerPrint(String fingerprint) {
			this.fingerPrint = fingerprint;
		}
		public String getLanguageCode() {
			return languageCode;
		}
		public void setLanguageCode(String languageCode) {
			this.languageCode = languageCode;
		}
		public long getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(long timeStamp) {
			this.timeStamp = timeStamp;
		}
		public String getReturnUrl() {
			return returnUrl;
		}
		public void setReturnUrl(String returnUrl) {
			this.returnUrl = returnUrl;
		}
		public int getSequence() {
			return sequence;
		}
		public void setSequence(int sequence) {
			this.sequence = sequence;
		}

	}
	
}
