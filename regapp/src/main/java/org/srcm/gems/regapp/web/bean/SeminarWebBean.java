package org.srcm.gems.regapp.web.bean;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.primefaces.model.UploadedFile;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import org.srcm.gems.regapp.dao.SeminarDAO;
import org.srcm.gems.regapp.dao.SeminarRegistrantDAO;
import org.srcm.gems.regapp.dao.UserDAO;
import org.srcm.gems.regapp.domain.Seminar;
import org.srcm.gems.regapp.domain.SeminarCustomField;
import org.srcm.gems.regapp.domain.SeminarRegistrant;
import org.srcm.gems.regapp.domain.User;
import org.srcm.gems.regapp.web.helper.SeminarWebHelper;
import org.srcm.gems.regapp.web.util.AgeRange;
import org.srcm.gems.regapp.web.util.CustomFieldType;
import org.srcm.gems.regapp.web.util.DonationAccountType;

@Component("seminarWebBeanProto")
@Scope("prototype")
public class SeminarWebBean implements Serializable {

	private Seminar selectedSeminar;

	private SeminarCustomField selectedField;

	private SeminarCustFieldDataModel semCustFldDataModel;

	private List<SeminarCustomField> customFields;
	
	private UploadedFile  uploadFile1;
	
	private UploadFile selectedFile;

	private List<String> allUsers;
	
	private List<String> selectedUsersForSeminar;
	
	public static final short donationAccountTypeNotSelectedConst = 999;
	
	public static final short customFieldTypeNotSelectedConst = 999;
	
	public SeminarCustomField getSelectedField() {

		if (this.selectedField == null) {
			this.selectedField = new SeminarCustomField();
		}
		return selectedField;
	}

	public void setSelectedField(SeminarCustomField selectedField) {
		this.selectedField = selectedField;
	}

	public Seminar getSelectedSeminar() {

		if (this.selectedSeminar == null) {
			selectedSeminar = new Seminar();
		}

		return selectedSeminar;
	}
	
	

	public UploadedFile  getUploadFile1() {
		return uploadFile1;
	}

	public void setUploadFile1(UploadedFile  uploadFile1) {
		
		this.uploadFile1 = uploadFile1;
	}

	public void setSelectedSeminar(Seminar selectedSeminar) {
		this.selectedSeminar = selectedSeminar;
	}
	
	

	public UploadFile getSelectedFile() {
		return selectedFile;
	}

	public void setSelectedFile(UploadFile selectedFile) {
		this.selectedFile = selectedFile;
	}

	public SeminarCustFieldDataModel getSelectedSeminarCustomFields() {


		if (this.customFields == null) {
			this.customFields = this.selectedSeminar.getSeminarCustomFields();
		}
		return new SeminarCustFieldDataModel(customFields);

	}

	protected boolean isFieldExists() {

		for (SeminarCustomField custFld : this.customFields) {
			if (custFld.getFieldLabel()
					.trim()
					.equalsIgnoreCase(this.selectedField.getFieldLabel().trim()))
				return true;
		}
		return false;
	}

	public Seminar addCustField(Seminar seminar, MessageContext messageContext) {

		if (this.customFields == null) {
			this.customFields = this.selectedSeminar.getSeminarCustomFields();
		}

		if (!this.isFieldExists()) {
			this.customFields.add(this.getSelectedField());
		}

		this.setSelectedField(new SeminarCustomField());

		return seminar;
	}
	
	public void deleteCustField(MessageContext messageContext) {
		
		SeminarDAO seminarDao = (SeminarDAO)SeminarWebHelper.getBeanFromElContext("#{seminarDao}");
		
		if(seminarDao.isRegitrationsForSeminar(this.selectedSeminar)){
			messageContext.addMessage(new MessageBuilder().error().source("successMsg").code("seminar.has.registrants").build());
			return;
		}
		
		if (this.customFields == null) {
			this.customFields = this.selectedSeminar.getSeminarCustomFields();
		}

		if (this.isFieldExists()) {
			this.customFields.remove(this.getSelectedField());
		}

	}
	
	protected Seminar addCustomFieldsToSeminar(Seminar seminar){
		
		seminar.setSeminarCustomFields(null);
		for (SeminarCustomField custFld : this.customFields) {
			
			custFld.setSeminar(seminar);
		}
		
		seminar.setSeminarCustomFields(this.customFields);
		
		return seminar;
	}
	
	public Seminar saveSeminar(MessageContext messageContext,Seminar seminar){
		
		SeminarDAO seminarDao = (SeminarDAO)SeminarWebHelper.getBeanFromElContext("#{seminarDao}");
		Seminar seminar1 = this.addCustomFieldsToSeminar(seminar);
		if(seminar.getEndDt().before(seminar.getStartDt())){
			messageContext.addMessage(new MessageBuilder().error().source("regForm:endDtId").code("seminar.endt.before.startdt").build());
			return seminar;
		}
		
		seminar = seminarDao.createOrSaveSeminar(seminar1);
		
		messageContext.addMessage(new MessageBuilder().info().source("successMsg").code("seminar.save.success").build());
		if(uploadFile1 != null){
		File dirToStore = (File)SeminarWebHelper.getBeanFromElContext("#{fileStorageDir}");
		
		if(!dirToStore.exists()){
			dirToStore.mkdir();
		}
		String dir = (String)SeminarWebHelper.getBeanFromElContext("#{fileStorageDirStr}");
		String destFile = dir+"/"+seminar.getSeminarId()+"_"+this.uploadFile1.getFileName();
		try{
			FileUtils.copyInputStreamToFile(this.uploadFile1.getInputstream(), new File(destFile));
		}catch(Exception e){
			e.printStackTrace();
			messageContext.addMessage(new MessageBuilder().error().source("regForm:endDtId").code("seminar.file.failed").build());
		}
		}
		return seminar;
		
	}
	
	public List<UploadFile> getFilesForSeminar(){
		
		return this.getFilesForSeminar(this.getSelectedSeminar());
	}
	
	public List<UploadFile> getFilesForSeminar(Seminar seminar){

		if(seminar.getSeminarId() == null){
			return null;
		}

		PrefixFileFilter fileFilter = new PrefixFileFilter(seminar.getSeminarId()+"_");
		File storageDir = (File)SeminarWebHelper.getBeanFromElContext("#{fileStorageDir}");

		if (storageDir != null)
		{
			Collection<File> filesAttahced = FileUtils.listFiles(storageDir,fileFilter , null);

			List<UploadFile> uploadedFiles = new ArrayList<UploadFile>(filesAttahced.size());

			for(File file : filesAttahced){

				uploadedFiles.add(new UploadFile(file));
			}
			return uploadedFiles;
		}
		else return null;
	}
	
	public void deleteAttachment(){
		
		if(FileUtils.deleteQuietly(this.getSelectedFile().getFile())){
			//System.out.println("File deleted successfully...");
		}else{
			//System.out.println("Error: File was not deleted ...");
		}
		
	}
	
	public List<SeminarRegistrant> getRegistrationsForSeminar(Seminar seminar){
		
		SeminarRegistrantDAO seminarRegDao = (SeminarRegistrantDAO)SeminarWebHelper.getBeanFromElContext("#{registrationDao}");
		
		return seminarRegDao.getAllRegistrationsBySeminar(seminar);
		
	}

	public String getApplicationURL(){
		RequestContext rc = RequestContextHolder.getRequestContext();
		
		ExternalContext ec = rc.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) ec.getNativeRequest();
		return request.getRequestURL().toString().split("/app")[0];
	}
	
	public void validateDonationAccountType(FacesContext context, UIComponent component,
			Object value) {
		Short donationAccountType = null;
		boolean invalid = false;
		try {
			donationAccountType = (Short) value;
		} catch (NumberFormatException nfe) {
			invalid = true;
			nfe.printStackTrace();
		} catch (NullPointerException npe) {
			invalid = true;
			npe.printStackTrace();
		}
		if (invalid || (donationAccountType == donationAccountTypeNotSelectedConst)) {
			((UIInput) component).setValid(false);
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Donation Account Type should be choosen",
					"");
			context.addMessage(component.getClientId(context), message);
		}

	}
	
	
	public void validateCustomFieldType(FacesContext context, UIComponent component,
			Object value) {
		Short customFieldType = null;
		boolean invalid = false;
		try {
			customFieldType = (Short) value;
		} catch (NumberFormatException nfe) {
			invalid = true;
			nfe.printStackTrace();
		} catch (NullPointerException npe) {
			invalid = true;
			npe.printStackTrace();
		}
		if (invalid || (customFieldType == customFieldTypeNotSelectedConst)) {
			((UIInput) component).setValid(false);
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Custom Field Type should be choosen",
					"");
			context.addMessage(component.getClientId(context), message);
		}

	}
	
	public DonationAccountType[] getDonationAccountTypes() {
		return DonationAccountType.values();
	}

	public short getDonationAccountTypeNotSelectedConst(){
		return donationAccountTypeNotSelectedConst;
	}
	
	public CustomFieldType[] getCustomFieldType() {
		return CustomFieldType.values();
	}

	public short getCustomFieldTypeNotSelectedConst() {
		return customFieldTypeNotSelectedConst;
	}
	

	public List<String> getAllUsers(){
		this.allUsers=new ArrayList<String>();
		UserDAO userDao = (UserDAO)SeminarWebHelper.getBeanFromElContext("#{userDAO}");
		List<User> userList = userDao.getAllUsers();
		if(userList != null && !userList.isEmpty()){
			for(User user: userList){
				String username=user.getFirstName()+" "+user.getLastName();
				this.allUsers.add(username);
			}
		}
		return this.allUsers;
	}
	
	@PostConstruct
	void init()
	{
		if (selectedSeminar==null)selectedSeminar=new Seminar();
		selectedSeminar.setAdultDonation(0.0);
		selectedSeminar.setChildDonation(0.0);
		
	}

	public List<String> getSelectedUsersForSeminar() {
		this.selectedUsersForSeminar=new ArrayList<String>();
		UserDAO userDao = (UserDAO)SeminarWebHelper.getBeanFromElContext("#{userDAO}");
		List<User> userList = userDao.getAllUsers();
		if(userList != null && !userList.isEmpty()){
			for(User user: userList){
				String username=user.getFirstName()+" "+user.getLastName();
				this.selectedUsersForSeminar.add(username);
			}
		}
		return this.selectedUsersForSeminar;
	}

	public void setSelectedUsersForSeminar(List<String> selectedUsersForSeminar) {
		this.selectedUsersForSeminar = selectedUsersForSeminar;
	}
}
