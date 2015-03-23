package org.srcm.gems.regapp.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.srcm.gems.regapp.web.util.DonationAccountType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the SEMINAR_ORIG database table.
 * 
 */
@Component("seminarProto")
@Scope("prototype")
@Entity
@Table(name="SEMINAR_ORIG")
@NamedQueries({
	@NamedQuery(name="Seminar.getAllSeminars", query="SELECT sem FROM Seminar sem ORDER BY sem.endDt DESC"),
	@NamedQuery(name="Seminar.getAll", query="SELECT sem FROM Seminar sem where sem.endDt > current_timestamp() and sem.status <> 2 ORDER BY sem.endDt DESC"),
	@NamedQuery(name="Seminar.loadCustFields", query="SELECT sem FROM Seminar sem LEFT JOIN fetch sem.seminarCustomFields where sem.seminarId = :semId"),
	@NamedQuery(name="Seminar.seminarHasRegistrants", query="SELECT count(semReg) FROM SeminarRegistrant semReg where semReg.seminarOrig.seminarId = :semId"),
	@NamedQuery(name="Seminar.getSeminarByID", query="SELECT sem FROM Seminar sem where sem.seminarId = :semId")
})
@JsonIgnoreProperties({"seminarCustomFields","seminarRegistrants","countryEntry","countryObj","seminarUserRoleMappings","userRoles"})
public class Seminar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SEMINAR_ID")
	private Long seminarId;

	@NotEmpty
	@Size(max=100)
	@Column(name="ADDRESS")
	private String address;

	@NotEmpty
	@Size(max=45)
	@Column(name="CITY")
	private String city;

	@NotEmpty
	@Size(max=45)
	@Column(name="COORDINATOR")
	private String coordinator;

	@Column(name="COUNTRY")
	private Long country;

	@Column(name="CREATE_DT")
	private Timestamp createDt;

	@Column(name="CREATE_USR")
	private String createUsr;

	@NotEmpty
	@Size(max=255)
	@Column(name="`DESC`")
	private String desc;

	@NotNull
    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="END_DT")
	private Date endDt;

	@NotEmpty
	@Size(max=45)
	@Column(name="LOCATION")
	private String location;

	@NotNull
    @Temporal( TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name="START_DT")
	private Date startDt;

	@NotEmpty
	@Size(max=75)
	@Column(name="THEME")
	private String theme;

	@NotEmpty
	@Size(max=45)
	@Column(name="TITLE")
	private String title;

	@Column(name="TYPE")
	private Long type;
	
	@Column(name="ADULT_DONATION_AMT")
	private Double adultDonation;
	
	@Column(name="CHILD_DONATION_AMT")
	private Double childDonation;

	@NotEmpty
	@Size(max=15)
	@Column(name="ZIP")
	private String zip;

	@Column(name="STATUS")
	private String status;
	
	@Column(name="ADDRESS_NEEDED")
	private String addressNeeded;
	
	//bi-directional many-to-one association to SeminarCustomField
	@OneToMany(mappedBy="seminar",cascade={CascadeType.ALL},orphanRemoval=true)
	private List<SeminarCustomField> seminarCustomFields = new ArrayList<SeminarCustomField>();

	//bi-directional many-to-one association to SeminarRegistrant
	@OneToMany(mappedBy="seminarOrig")
	private List<SeminarRegistrant> seminarRegistrants;

	@ManyToOne
	@JoinColumn(name="country",nullable=true,insertable=false,updatable=false)
	private Country countryEntry;
	
	@NotEmpty
	@Size(max=20)
	@Column(name="coordinator_phone")
	private String phone;
	
	@NotEmpty
	@Size(max=45)
	@Column(name="coordinator_email")
	private String email;
	
	@Column(name="donation_account_type")
	private Short donationAccount;
	
	@OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="country",nullable=true,insertable=false,updatable=false)
	private Country countryObj;
	
//	@OneToMany(mappedBy="seminar")
////	@JsonBackReference
//	private Set<SeminarUserRoleMapping> seminarUserRoleMappings;
	
//	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinTable(name = "SEMINAR_USER_ROLE_MAPPING",
//        joinColumns = @JoinColumn(name = "SEMINAR_ID"),
//        inverseJoinColumns = @JoinColumn(name = "USER_ID"))
//    @MapKeyJoinColumn(name = "ROLE_ID")
//    private Map<Role, User> userRoles = new HashMap<Role, User>();
//	
//    public Map<Role, User> getUserRoles() {
//		return userRoles;
//	}
//
//	public void setUserRoles(Map<Role, User> userRoles) {
//		this.userRoles.clear();
//		this.userRoles.putAll(userRoles);
//	}
	@ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinTable(name="SEMINAR_USER_MAPPING",
				joinColumns=@JoinColumn(name="seminar_id"),
				inverseJoinColumns=@JoinColumn(name="user_id"),
				uniqueConstraints=@UniqueConstraint(columnNames={"seminar_id","user_id"}))
	private Set<User> users = new HashSet<User>();
	
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users.clear();
		this.users.addAll(users);
	}
	

	public Seminar() {
    }

	public Long getSeminarId() {
		return this.seminarId;
	}

	public void setSeminarId(Long seminarId) {
		this.seminarId = seminarId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCoordinator() {
		return this.coordinator;
	}

	public void setCoordinator(String coordinator) {
		this.coordinator = coordinator;
	}

	public Long getCountry() {
		return this.country;
	}

	public void setCountry(Long country) {
		this.country = country;
	}

	public Timestamp getCreateDt() {
		return this.createDt;
	}

	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}

	public String getCreateUsr() {
		return this.createUsr;
	}

	public void setCreateUsr(String createUsr) {
		this.createUsr = createUsr;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getEndDt() {
		return this.endDt;
	}

	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getStartDt() {
		return this.startDt;
	}

	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}

	public String getTheme() {
		return this.theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getType() {
		return this.type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public List<SeminarCustomField> getSeminarCustomFields() {
		return this.seminarCustomFields;
	}

	public void setSeminarCustomFields(List<SeminarCustomField> seminarCustomFields) {
		this.seminarCustomFields = seminarCustomFields;
	}
	
	public List<SeminarRegistrant> getSeminarRegistrants() {
		return this.seminarRegistrants;
	}

	public void setSeminarRegistrants(List<SeminarRegistrant> seminarRegistrants) {
		this.seminarRegistrants = seminarRegistrants;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Country getCountryEntry() {
		return countryEntry;
	}

	public void setCountryEntry(Country countryEntry) {
		this.countryEntry = countryEntry;
	}
	

	public boolean getAllowNewRegistration(){
		if(status != null && status.equals("1")){
			return true;
		}
		return false;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Country getCountryObj() {
		return countryObj;
	}

	public void setCountryObj(Country countryObj) {
		this.countryObj = countryObj;
	}

	public Double getAdultDonation() {
		return adultDonation;
	}

	public void setAdultDonation(Double adultDonation) {
		this.adultDonation = adultDonation;
	}

	public Double getChildDonation() {
		return childDonation;
	}

	public void setChildDonation(Double childDonation) {
		this.childDonation = childDonation;
	}

	public Short getDonationAccount() {
		return donationAccount;
	}

	public void setDonationAccount(Short donationAccount) {
		this.donationAccount = donationAccount;
	}
	
	public String getDonationAccountTypes()
	{
		if (donationAccount != null && donationAccount < DonationAccountType.values().length)
			return (DonationAccountType.values())[donationAccount].getLabel();
		else return "";
	}

	public String getAddressNeeded() {
		return addressNeeded;
	}

	public void setAddressNeeded(String addressNeeded) {
		this.addressNeeded = addressNeeded;
	}

	
}