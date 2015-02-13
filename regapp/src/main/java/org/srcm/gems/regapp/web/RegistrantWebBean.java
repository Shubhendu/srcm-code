package org.srcm.gems.regapp.web;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import org.srcm.gems.regapp.dao.SeminarCustomFieldDAO;
import org.srcm.gems.regapp.dao.SeminarDAO;
import org.srcm.gems.regapp.dao.SeminarDAOImpl;
import org.srcm.gems.regapp.dao.SeminarRegistrantDAO;
import org.srcm.gems.regapp.domain.Seminar;
import org.srcm.gems.regapp.domain.SeminarCustomField;
import org.srcm.gems.regapp.domain.SeminarRegistrant;
import org.srcm.gems.regapp.domain.SeminarRegistrantCustFld;
import org.srcm.gems.regapp.email.MailManager;
import org.srcm.gems.regapp.web.bean.SeminarRegistrantDataModel;
import org.srcm.gems.regapp.web.bean.UploadFile;
import org.srcm.gems.regapp.web.helper.CreditCardHelper;
import org.srcm.gems.regapp.web.helper.SeminarWebHelper;
import org.srcm.gems.regapp.web.util.AgeRange;

@Component("registrantWebBeanProto")
@Scope("prototype")
public class RegistrantWebBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -863163480284007481L;

	private static Logger logger = LoggerFactory
			.getLogger(RegistrantWebBean.class);

	private Seminar selectedSeminar;

	private String searchLastName;

	private String searchEmailAddress;

	private SeminarRegistrant primaryRegistrant;

	private SeminarRegistrant selectedRegistrant;

	private SeminarRegistrantDataModel seminarRegistrantDataModel;

	private SeminarRegistrant dependent;

	public static final short ageNotSelectedConst = 999;
	
	public boolean completedAdd = false;

	@Autowired
	private CreditCardHelper ccHelper;

	public RegistrantWebBean() {
	}

	public Seminar getSelectedSeminar() {
		return selectedSeminar;
	}

	public void setSelectedSeminar(Seminar selectedSeminar) {
		this.selectedSeminar = selectedSeminar;
	}

	public String getSearchLastName() {
		return searchLastName;
	}

	public void setSearchLastName(String searchLastName) {
		this.searchLastName = searchLastName;
	}

	public String getSearchEmailAddress() {
		return searchEmailAddress;
	}

	public void setSearchEmailAddress(String searchEmailAddress) {
		this.searchEmailAddress = searchEmailAddress;
	}

	public SeminarRegistrant getPrimaryRegistrant() {
		return primaryRegistrant;
	}

	public void setPrimaryRegistrant(SeminarRegistrant primaryRegistrant) {
		this.primaryRegistrant = primaryRegistrant;
	}

	public SeminarRegistrant getSelectedRegistrant() {
		return selectedRegistrant;
	}

	public void setSelectedRegistrant(SeminarRegistrant selectedRegistrant) {
		this.selectedRegistrant = selectedRegistrant;
	}

	public SeminarRegistrantDataModel getSeminarRegistrantDataModel() {
		if (primaryRegistrant == null
				|| primaryRegistrant.getSeminarRegistrants() == null
				|| primaryRegistrant.getSeminarRegistrants().size() == 0) {
			return null;
		}
		return new SeminarRegistrantDataModel(
				primaryRegistrant.getSeminarRegistrants());
	}

	public boolean getShowDependents() {
		if (getSeminarRegistrantDataModel() != null
				&& getSeminarRegistrantDataModel().getRowCount() > 0) {
			return true;
		}
		return false;
	}

	public SeminarRegistrant getDependent() {
		return dependent;
	}

	public void setDependent(SeminarRegistrant dependent) {
		this.dependent = dependent;
	}

	public SeminarRegistrant saveRegistrant(
			SeminarRegistrantDAO registrationDao, MailManager mailManager,
			SeminarRegistrant registration, MessageContext messageContext) {
		if (!"Yes".equals(registration.getAcceptDisclaimer())) {
			messageContext.addMessage(new MessageBuilder().error()
					.defaultText("Please accept the terms and conditions.")
					.build());
			return registration;
		}

		if ((registration.getPayThruWeb() == null)) {
			messageContext.addMessage(new MessageBuilder().error()
					.defaultText("Please choose Pay Now or Pay Later").build());
			return registration;
		}

		registration.setSeminarOrig(this.selectedSeminar);
		SeminarRegistrant dbregistration = registrationDao.findRegistration(
				registration.getLastName(), registration.getEmail(),
				this.selectedSeminar.getSeminarId());
		if ((registration.getSeminarRegistrantId() != null
				&& dbregistration != null && !dbregistration
				.getSeminarRegistrantId().equals(
						registration.getSeminarRegistrantId()))
				|| (registration.getSeminarRegistrantId() == null && dbregistration != null)) {
			messageContext.addMessage(new MessageBuilder()
					.error()
					.defaultText(
							"You have already registered for this seminar.")
					.build());
			return registration;
		}
		registration.setCreateDate(new Timestamp(System.currentTimeMillis()));
		registration.setAmountToPay(registration.getAmountToPay());
		registrationDao.saveResitration(registration);
		registration = registrationDao.findRegistration(
				registration.getLastName(), registration.getEmail(),
				this.selectedSeminar.getSeminarId());

		seminarRegistrantDataModel = new SeminarRegistrantDataModel(
				registration.getSeminarRegistrants());

		if ("yes".equalsIgnoreCase(registration.getPayThruWeb())) {
			try {
				HttpServletRequest req = (HttpServletRequest) FacesContext
						.getCurrentInstance().getExternalContext().getRequest();
				String url = req.getRequestURL().toString();
				url = url.substring(0, url.length()
						- req.getRequestURI().length())
						+ req.getContextPath() + "/";
				CreditCardHelper.AuthNetBean authNetBean = ccHelper
						.buildAuthNetParams(registration.getAmountToPay()
								.toString(), selectedSeminar.getDonationAccount());

				authNetBean.setReturnUrl(url + "authNetReturn.jsp?trackingNo="
						+ registration.getSeminarRegistrantId().toString()
						+ "&languageCode=" + authNetBean.getLanguageCode()
						+ "&semId=" + selectedSeminar.getSeminarId()
						+ "&lName=" + registration.getLastName() + "&country="
						+ selectedSeminar.getCountryObj().getCountryName()
						+ "&faces-redirect=true");

				Map<String, Object> sessionMap = FacesContext
						.getCurrentInstance().getExternalContext()
						.getSessionMap();
				sessionMap.put("registration", registration);
				sessionMap.put("selectedSeminar", selectedSeminar);
				sessionMap.put("authNetBean", authNetBean);

				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(url + "authNetForward.jsp");
				logger.info("Forwarding to " + url + "authNetForward.jsp");
			} catch (IOException ex) {
				mailManager.sendConfirmationEmail(registration);
				messageContext
						.addMessage(new MessageBuilder()
								.error()
								.defaultText(
										"Could not be forwarded to credit card processor.")
								.build());
			}
		} else {
			mailManager.sendConfirmationEmail(registration);
			messageContext.addMessage(new MessageBuilder().info()
					.defaultText("Registration saved successfully for "+ registration.getFirstName() + " " + registration.getLastName()).build());
			logger.info("Registration saved successfully for "
					+ registration.getLastName());
		}

		return registration;
	}

	public SeminarRegistrant findRegistrant(
			SeminarRegistrantDAO registrationDao, MessageContext messageContext) {

		SeminarRegistrant registration = registrationDao.findRegistration(
				this.searchLastName, this.searchEmailAddress,
				this.selectedSeminar.getSeminarId());
		if (registration == null) {
			messageContext
					.addMessage(new MessageBuilder()
							.error()
							.defaultText(
									"Registration not found, check the \"Last Name\" and \"Email Address\"")
							.build());
		} else {
			registration.setSeminarOrig(this.selectedSeminar);
			this.primaryRegistrant = registration;
			this.seminarRegistrantDataModel = new SeminarRegistrantDataModel(
					registration.getSeminarRegistrants());
		}

		return registration;
	}

	public SeminarRegistrant getNewRegistrationInstance(
			SeminarCustomFieldDAO seminarCustomFieldDAO) {
		SeminarRegistrant registrant = new SeminarRegistrant();
		registrant.setSeminarOrig(selectedSeminar);
		selectedSeminar.setSeminarCustomFields(seminarCustomFieldDAO
				.getCustomFieldsForSeminar(selectedSeminar.getSeminarId()));

		if (selectedSeminar.getSeminarCustomFields() != null
				&& selectedSeminar.getSeminarCustomFields().size() > 0) {
			List<SeminarRegistrantCustFld> srCustFields = new ArrayList<SeminarRegistrantCustFld>();
			for (SeminarCustomField sCustField : selectedSeminar
					.getSeminarCustomFields()) {
				SeminarRegistrantCustFld srCustField = new SeminarRegistrantCustFld();
				srCustField.setSeminarCustomField(sCustField);
				srCustField.setSeminarRegistrant(registrant);
				srCustFields.add(srCustField);
			}
			registrant.setSeminarRegistrantCustFlds(srCustFields);
		}
		return registrant;
	}

	public SeminarRegistrant addRegistrant(
			SeminarCustomFieldDAO seminarCustomFieldDAO,
			SeminarRegistrant registration) {
		registration.setSeminarOrig(this.selectedSeminar);
		SeminarRegistrant registrant = getNewRegistrationInstance(seminarCustomFieldDAO);
		this.setPrimaryRegistrant(registration);
		registrant = primaryRegistrant.copyPrimaryInfo(registrant);
		
		//registrant.setAmountToPay(calculatePayment());
		return registrant;
	}

	public SeminarRegistrant addMoreRegistrant(
			SeminarCustomFieldDAO seminarCustomFieldDAO,
			SeminarRegistrant registration) {
		addToPrimary(registration);
		SeminarRegistrant registrant = getNewRegistrationInstance(seminarCustomFieldDAO);
		registrant = primaryRegistrant.copyPrimaryInfo(registrant);
		//registrant.setAmountToPay(calculatePayment());
		return registrant;
	}
	
	public SeminarRegistrant completeAddingRegistrant(
			SeminarRegistrant registration) {
		registration.setSeminarOrig(this.selectedSeminar);
		if (getPrimaryRegistrant()== null)
			this.setPrimaryRegistrant(registration);
		else
			addToPrimary(registration);
		
		completedAdd = true;
		
		getPrimaryRegistrant().setAmountToPay(calculatePayment());
		return getPrimaryRegistrant();
	}

	private void addToPrimary(SeminarRegistrant dependent) {
		dependent.setSeminarOrig(this.selectedSeminar);
		
		if(dependent != getPrimaryRegistrant())
			dependent.setSeminarRegistrant(getPrimaryRegistrant());
		List<SeminarRegistrant> dependents = getPrimaryRegistrant()
				.getSeminarRegistrants();
		if (dependents == null) {
			dependents = new ArrayList<SeminarRegistrant>();
			getPrimaryRegistrant().setSeminarRegistrants(dependents);
		}
		synchronized (dependents) {
			if (!dependents.contains(dependent) && dependent != getPrimaryRegistrant()) {
				dependents.add(dependent);
			}
		}
	}

	public SeminarRegistrant saveRegistrants(
			SeminarRegistrantDAO registrationDao, MailManager mailManager,
			SeminarRegistrant dependent, MessageContext messageContext) {
		addToPrimary(dependent);
		if (!"Yes".equals(dependent.getAcceptDisclaimer())) {
			messageContext.addMessage(new MessageBuilder().error()
					.defaultText("Please accept the terms and conditions.")
					.build());
			return primaryRegistrant;
		}

		if ((dependent.getPayThruWeb() == null)) {
			messageContext.addMessage(new MessageBuilder().error()
					.defaultText("Please choose Pay Now or Pay Later").build());
			return primaryRegistrant;
		}

		// Set the primary Registrant amount to pay from the dependent form
		primaryRegistrant.setAmountToPay(calculatePayment());
		primaryRegistrant.setPayThruWeb(dependent.getPayThruWeb());
		primaryRegistrant.setAcceptDisclaimer(dependent.getAcceptDisclaimer());

		SeminarRegistrant dbregistration = registrationDao.findRegistration(
				primaryRegistrant.getLastName(), primaryRegistrant.getEmail(),
				this.selectedSeminar.getSeminarId());
		if ((primaryRegistrant.getSeminarRegistrantId() != null
				&& dbregistration != null && !dbregistration
				.getSeminarRegistrantId().equals(
						primaryRegistrant.getSeminarRegistrantId()))
				|| (primaryRegistrant.getSeminarRegistrantId() == null && dbregistration != null)) {
			messageContext.addMessage(new MessageBuilder()
					.error()
					.defaultText(
							"You have already registered for this seminar.")
					.build());
			return primaryRegistrant;
		}
		primaryRegistrant.setCreateDate(new Timestamp(System
				.currentTimeMillis()));
		registrationDao.saveResitration(primaryRegistrant);
		primaryRegistrant = registrationDao.findRegistration(
				primaryRegistrant.getLastName(), primaryRegistrant.getEmail(),
				this.selectedSeminar.getSeminarId());
		seminarRegistrantDataModel = new SeminarRegistrantDataModel(
				primaryRegistrant.getSeminarRegistrants());
		if ("yes".equalsIgnoreCase(dependent.getPayThruWeb())) {
			try {
				HttpServletRequest req = (HttpServletRequest) FacesContext
						.getCurrentInstance().getExternalContext().getRequest();
				String url = req.getRequestURL().toString();
				url = url.substring(0, url.length()
						- req.getRequestURI().length())
						+ req.getContextPath() + "/";

				CreditCardHelper.AuthNetBean authNetBean = ccHelper
						.buildAuthNetParams(calculatePayment()
								.toString(),selectedSeminar.getDonationAccount());

				authNetBean.setReturnUrl(url + "authNetReturn.jsp?trackingNo="
						+ primaryRegistrant.getSeminarRegistrantId().toString()
						+ "&languageCode=" + authNetBean.getLanguageCode()
						+ "&semId=" + selectedSeminar.getSeminarId()
						+ "&lName=" + primaryRegistrant.getLastName()
						+ "&country="
						+ selectedSeminar.getCountryObj().getCountryName()
						+ "&faces-redirect=true");

				Map<String, Object> sessionMap = FacesContext
						.getCurrentInstance().getExternalContext()
						.getSessionMap();
				sessionMap.put("registration", primaryRegistrant);
				sessionMap.put("selectedSeminar", selectedSeminar);
				sessionMap.put("authNetBean", authNetBean);

				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(url + "authNetForward.jsp");
			} catch (IOException ex) {
				messageContext
						.addMessage(new MessageBuilder()
								.error()
								.defaultText(
										"Could not be forwarded to credit card processor.")
								.build());
				mailManager.sendConfirmationEmail(primaryRegistrant);
				logger.info("Could not be forwarded to credit card processor");
			}
		} else {
			mailManager.sendConfirmationEmail(primaryRegistrant);
			messageContext.addMessage(new MessageBuilder().info()
					.defaultText("Registration saved successfully for "+ primaryRegistrant.getFirstName() + " " + primaryRegistrant.getLastName()).build());			
			logger.info("Registration saved successfully for "
					+ primaryRegistrant.getLastName());
		}

		return primaryRegistrant;
	}

	public SeminarRegistrant removeDependent(
			SeminarRegistrantDAO registrationDao, SeminarRegistrant registrant,
			MessageContext messageContext) {
		if (dependent != null && dependent.getSeminarRegistrantId() != null) {
			registrationDao.remove(dependent);
		}
		if (registrant != null && registrant.getSeminarRegistrants() != null) {
			registrant.getSeminarRegistrants().remove(dependent);
			primaryRegistrant = registrant;
		}
		seminarRegistrantDataModel = new SeminarRegistrantDataModel(
				registrant.getSeminarRegistrants());
		//registrant.setAmountToPay(calculatePayment());
		return registrant;
	}

	public SeminarRegistrant removeDependent(
			SeminarRegistrantDAO registrationDao, MessageContext messageContext) {
		if (dependent != null && dependent.getSeminarRegistrantId() != null) {
			registrationDao.remove(dependent);
		}
		if (primaryRegistrant != null
				&& primaryRegistrant.getSeminarRegistrants() != null) {
			primaryRegistrant.getSeminarRegistrants().remove(dependent);
		}
		seminarRegistrantDataModel = new SeminarRegistrantDataModel(
				primaryRegistrant.getSeminarRegistrants());
		
		primaryRegistrant.setAmountToPay(calculatePayment());
		return primaryRegistrant;
	}

	public SeminarRegistrant findRegistrant(String seminarRegistrationId) {
		SeminarRegistrantDAO registrationDao = (SeminarRegistrantDAO) SeminarWebHelper
				.getBeanFromElContext("#{registrationDao}");

		SeminarRegistrant registrant = registrationDao
				.findRegistration(seminarRegistrationId);
		return registrant;
	}

	public void validateAge(FacesContext context, UIComponent component,
			Object value) {
		Short age = null;
		boolean invalid = false;

		try {
			age = (Short) value;
		} catch (NumberFormatException nfe) {
			invalid = true;
			nfe.printStackTrace();
		}
		if (invalid || (age != null && (age <= 0 || age > 100))) {
			((UIInput) component).setValid(false);
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Age should be an integer between 0 and 99", "");
			context.addMessage(component.getClientId(context), message);
		}
		// validateDateRange(context, component, (Date)
		// component.getAttributes().get("arrivalDate"), (Date)
		// component.getAttributes().get("departureDate"));
	}

	public void validateAgeRange(FacesContext context, UIComponent component,
			Object value) {
		Short age = null;
		boolean invalid = false;
		logger.debug("++++validateAgeRange");
		try {
			age = (Short) value;
		} catch (NumberFormatException nfe) {
			invalid = true;
			nfe.printStackTrace();
		} catch (NullPointerException npe) {
			invalid = true;
			npe.printStackTrace();
		}
		if (invalid || (age == ageNotSelectedConst)) {
			logger.debug("++++Age Range should be choosen ");
			((UIInput) component).setValid(false);
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Age Range should be choosen",
					"");
			context.addMessage(component.getClientId(context), message);
		}

	}

	public void validateAccomodation(FacesContext context,
			UIComponent component, Object value) {
		boolean invalid = false;

		UIComponent component1 = findComponentInRoot("preferredAccomodationId");
		Object otherValue = ((UIInput) component1).getSubmittedValue();

		String thisVal = (String) value;

		if (thisVal instanceof String
				&& StringUtils.isNotBlank((String) thisVal)
				&& (((String) thisVal).equalsIgnoreCase("Y"))) {
			if (StringUtils.isBlank((String) otherValue))
				invalid = true;
		}

		if (invalid) {
			logger.debug("++++Preferred Accomodation should be choosen ");
			// ((UIInput) component1).setValid(false);
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"You selected yes to accomodation needed, but missed to choose preferred accomodation",
					"");
			context.addMessage(component.getClientId(context), message);
		}

	}

	public void validateAmountPaid(FacesContext context, UIComponent component,
			Object value) {
		boolean invalid = false;

		UIComponent component1 = findComponentInRoot("paymentAmount");
		Object otherValue = ((UIInput) component1).getSubmittedValue();

		String thisVal = (String) value;

		if (thisVal instanceof String
				&& StringUtils.isNotBlank((String) thisVal)
				&& (((String) thisVal).equalsIgnoreCase("Yes"))) {
			if (StringUtils.isBlank((String) otherValue))
				invalid = true;
		}

		if (invalid) {
			logger.debug("++++Payment amount should be entered ");
			((UIInput) component1).setValid(false);
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Payment amount should be entered", "");
			context.addMessage(component.getClientId(context), message);
		}

	}

	public SeminarRegistrant refreshPayment(SeminarRegistrant registration) {
		boolean invalid = false;
		logger.debug("++++refreshPayment");
		try {
			
			//UIComponent component1 = findComponentInRoot("age");
			//Object value = ((UIInput) component1).getValue();
			
			if(getPrimaryRegistrant()==null){
				SeminarCustomFieldDAO registrationDao = (SeminarCustomFieldDAO) SeminarWebHelper
						.getBeanFromElContext("#{seminarCustomFieldDAO}");

				SeminarRegistrant registrant = getNewRegistrationInstance(registrationDao);
				this.setPrimaryRegistrant(registration);

				registrant = primaryRegistrant.copyPrimaryInfo(registrant);
			}
			else
				addToPrimary(registration);
			

			registration.setAmountToPay(calculatePayment());
				
			
		} catch (NumberFormatException nfe) {
			invalid = true;
			nfe.printStackTrace();
		}
		catch (NullPointerException npe) {
			invalid = true;
			npe.printStackTrace();
		}
		
		return registration;

	}
	
	private Double calculatePayment()
	{
		Short age = null;
		Double newAmount =  0.0;

		if (getPrimaryRegistrant() == null) return newAmount;

		List<SeminarRegistrant> registrants = new ArrayList<SeminarRegistrant>();

		if (getPrimaryRegistrant().getSeminarRegistrants() != null)
			registrants.addAll(getPrimaryRegistrant().getSeminarRegistrants());

		//Set primary registrant also in the list to include it in payment calculation
		if (!registrants.contains(getPrimaryRegistrant())) {
			registrants.add(getPrimaryRegistrant());
		}


		for (SeminarRegistrant iterregistrant: registrants)
		{
			age = iterregistrant.getAge();

			//Double newAmount = primaryRegistrant!=null&&primaryRegistrant.getAmountToPay()!=null?primaryRegistrant.getAmountToPay():0;

			if (age == AgeRange.BETWEEN_6AND11.getValue())
			{
				newAmount += selectedSeminar.getChildDonation();
			}
			else if (age != AgeRange.UNDER_5.getValue())
			{
				newAmount += selectedSeminar.getAdultDonation();
			}
		}

		return newAmount;
	}

	@SuppressWarnings("deprecation")
	public void validateDateRange(FacesContext context, UIComponent component,
			Object value) {
		Date toDate = (Date) value;
		
	
		/*UIComponent otherComponent = context.getViewRoot().findComponent(
				"registrationForm:arrivalDate");
		Object otherValue = ((UIInput) otherComponent).getValue();*/
		
		UIComponent otherComponent = findComponentInRoot("arrivalDate");
		Object otherValue = ((UIInput) otherComponent).getValue();

		
		Date fromDate = (Date) otherValue;
/*
		UIComponent fromTimeComponent = context.getViewRoot().findComponent("registrationForm:arrivalTime");
		logger.debug("fromTimeComponent " + fromTimeComponent.toString() +" " + (Date) ((UIInput) fromTimeComponent).getValue());	
		Date arrTime = (Date) ((UIInput) fromTimeComponent).getValue();

		UIComponent depTimeComponent = context.getViewRoot().findComponent("registrationForm:departureTime");
		logger.debug("depTimeComponent " + depTimeComponent.toString() +" " + (Date) ((UIInput) depTimeComponent).getValue());
		Date depTime = (Date) ((UIInput) depTimeComponent).getValue();
*/
		logger.debug("validateDateRange fromDate " + fromDate!=null?fromDate.toString():"null" );
		logger.debug("validateDateRange toDate " + toDate!=null?toDate.toString():"null");
		if (fromDate == null || toDate == null) {
			logger.debug("validateDateRange" + fromDate + "::" + toDate);

			return;
		}

		
		// Hack..Skip testing time, if 2 time componenets are in a form, the second component returns null ie:depTime is null
		/*if (arrTime != null) {
			fromDate.setHours(arrTime.getHours());
			fromDate.setMinutes(arrTime.getMinutes());
			fromDate.setSeconds(0);
		}
		if (depTime != null) {
			toDate.setHours(depTime.getHours());
			toDate.setMinutes(depTime.getMinutes());
			toDate.setSeconds(0);
		}*/
		
		//if (toDate.before(fromDate) && fromDate.getTime() >= toDate.getTime()) {
			if (toDate.before(fromDate)) {
			FacesMessage message = new FacesMessage(
					"Arrival Date & Time should be before departure Date & Time.");
			((UIInput) component).setValid(false);
			((UIInput) otherComponent).setValid(false);
			//((UIInput) fromTimeComponent).setValid(false);
			//((UIInput) depTimeComponent).setValid(false);
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(component.getClientId(context), message);
		}
	}

	public static UIComponent findComponentInRoot(String id) {
		UIComponent ret = null;

		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			UIComponent root = context.getViewRoot();
			ret = findComponent(root, id);
		}

		return ret;
	}

	public static UIComponent findComponent(UIComponent base, String id) {

		// Is the "base" component itself the match we are looking for?
		if (id.equals(base.getId())) {
			return base;
		}

		// Search through our facets and children
		UIComponent kid = null;
		UIComponent result = null;
		Iterator kids = base.getFacetsAndChildren();
		while (kids.hasNext() && (result == null)) {
			kid = (UIComponent) kids.next();
			if (id.equals(kid.getId())) {
				result = kid;
				break;
			}
			result = findComponent(kid, id);
			if (result != null) {
				break;
			}
		}
		return result;
	}

	public List<UploadFile> getFilesForSeminar() {

		return this.getFilesForSeminar(this.getSelectedSeminar());
	}

	public List<UploadFile> getFilesForSeminar(Seminar seminar) {

		if (seminar.getSeminarId() == null) {
			return null;
		}

		try {
			PrefixFileFilter fileFilter = new PrefixFileFilter(
					seminar.getSeminarId() + "_");
			Collection<File> filesAttahced = FileUtils.listFiles(
					(File) SeminarWebHelper
							.getBeanFromElContext("#{fileStorageDir}"),
					fileFilter, null);
			List<UploadFile> uploadedFiles = new ArrayList<UploadFile>(
					filesAttahced.size());
			for (File file : filesAttahced) {

				uploadedFiles.add(new UploadFile(file));
			}
			return uploadedFiles;
		} catch (Exception e) {
			logger.warn("Error finding attached files");
		}
		return null;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/registration.html")
	public ModelAndView editCentre(
			@RequestParam(required = true, value = "id") Long seminarId) {
		ModelAndView mav = new ModelAndView();
		SeminarDAO dao = new SeminarDAOImpl();
		Seminar seminar = dao.getSeminarByID(seminarId);
		mav.setViewName("/WEB-INF/flows/main/main.faces");

		return mav;
	}

	public Boolean retrieveSeminar(SeminarDAO seminarDao,
			MessageContext messageContext) {
		try {
			RequestContext rc = RequestContextHolder.getRequestContext();

			ExternalContext ec = rc.getExternalContext();
			HttpServletRequest request = (HttpServletRequest) ec
					.getNativeRequest();
			Long seminarID = new Long(request.getParameter("seminarID"));
			this.selectedSeminar = seminarDao.getSeminarByID((Long) seminarID);
		} catch (Exception ex) {
			return null;
		}
		return Boolean.TRUE;
	}

	public Boolean updatePaymentStatus(String status, String statusMsg,
			String transId, String seminarRegistrationId) {

		SeminarRegistrantDAO registrationDao = (SeminarRegistrantDAO) SeminarWebHelper
				.getBeanFromElContext("#{registrationDao}");
		MailManager mailManager = (MailManager) SeminarWebHelper
				.getBeanFromElContext("#{mailManager}");
		SeminarRegistrant registrant = registrationDao
				.findRegistration(seminarRegistrationId);

		if (registrant != null) {
			Map<String, Object> sessionMap = FacesContext.getCurrentInstance()
					.getExternalContext().getSessionMap();
			sessionMap.put("registrant", registrant);
		}

		if ("1".equals(status)) {

			if (registrant != null) {

				logger.info("Credit Card Transaction SUCCESS. Registrant "
						+ registrant.getLastName() + " status=" + status
						+ " transid " + transId);

				registrant.setCcTransactionNo(transId);
				registrationDao.saveResitration(registrant);

				mailManager.sendConfirmationEmail(registrant);
				return true;
			}

		} else {

			if (registrant != null) {
				mailManager.sendConfirmationEmail(registrant);
			}

			logger.info("ERROR in Credit Card Transaction for - " + status
					+ "\n Error Message: " + statusMsg);
		}
		return false;

	}

	public String getFileName(String fileName) {
		return fileName;
	}

	public AgeRange[] getAgeRanges() {
		return AgeRange.values();
	}

	public short getAgeNotSelectedConst() {
		return ageNotSelectedConst;
	}

	public SeminarRegistrant cancelDependent(
			SeminarRegistrantDAO registrationDao, MessageContext messageContext) {
		return primaryRegistrant;
	}

	public CreditCardHelper getCcHelper() {
		return ccHelper;
	}

	public void setCcHelper(CreditCardHelper ccHelper) {
		this.ccHelper = ccHelper;
	}

	public boolean isCompletedAdd() {
		return completedAdd;
	}

}
