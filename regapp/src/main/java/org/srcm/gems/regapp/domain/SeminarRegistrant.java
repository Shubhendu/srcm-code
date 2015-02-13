package org.srcm.gems.regapp.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.srcm.gems.regapp.web.util.AgeRange;

import java.sql.Timestamp;
import java.sql.Time;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the SEMINAR_REGISTRANT database table.
 * 
 */
@Component("seminarRegistrant")
@Scope("prototype")
@Entity
@Table(name="SEMINAR_REGISTRANT")
@NamedQueries({
	@NamedQuery(name="Registration.findByLastnameAndEmailId", query="SELECT reg FROM SeminarRegistrant reg where lower(trim(reg.email)) = lower(trim(:email)) and lower(trim(reg.lastName)) = lower(trim(:lastName)) and reg.seminarOrig.seminarId = :seminarId and reg.seminarRegistrant is null"),
	@NamedQuery(name="Registration.findByRegistrationId", query="SELECT reg FROM SeminarRegistrant reg where reg.seminarRegistrantId = :seminarRegistrantId"),
	@NamedQuery(name="Registration.deleteSeminarRegistrant", query="delete SeminarRegistrant reg where reg.seminarRegistrantId = :seminarRegistrantId"),
	@NamedQuery(name="Registration.getAllRegistrations", query="SELECT reg FROM SeminarRegistrant reg"),
	
	@NamedQuery(name="Registration.getAllRegistrationsBySeminar", 
				query="SELECT reg FROM SeminarRegistrant reg where reg.seminarOrig.seminarId = :seminarId and reg.seminarRegistrant = null"),
				
	@NamedQuery(name="registrant.search", query="select reg from SeminarRegistrant reg where reg.lastName = :lastName and reg.email = :email and reg.seminarRegistrant is null" ),
	@NamedQuery(name="admin.regSearch", query="select reg from SeminarRegistrant reg where reg.lastName = :lastName and reg.email = :email and reg.firstName = :firstName and reg.abhyasiId = :regId and reg.seminarRegistrant is null")
})
public class SeminarRegistrant implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final String volunteerNotSelected = "99";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="seminar_registrant_id")
	private Long seminarRegistrantId;

	@Column(name="abhyasi_id")
	private String abhyasiId;

	@Column(name="accomodation_allotment_no")
	private String accomodationAllotmentNo;

	@Column(name="accomodaton_notes")
	private String accomodatonNotes;
	
	@Column(name="age")
	private Short age;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="arrival_date")
	private Date arrivalDate;

	@Column(name="arrival_mo")
	private String arrivalMo;

    @Temporal( TemporalType.DATE)
	@Column(name="arrival_pickup_date")
	private Date arrivalPickupDate;

	@Column(name="arrival_pickup_location")
	private String arrivalPickupLocation;

	@Column(name="arrival_pickup_needed")
	private String arrivalPickupNeeded;

	@Column(name="arrival_pickup_notes")
	private String arrivalPickupNotes;

	@Column(name="arrival_pickup_time")
	private Time arrivalPickupTime;

	@Column(name="arrival_time")
	private Time arrivalTime;

	@Column(name="bedding_needed")
	private String beddingNeeded="N";

	@Column(name="cc_city_detail")
	private String ccCityDetail;

	@Column(name="cc_state")
	private String ccState;
	
	@Column(name="cc_zip")
	private String ccZip;
	
	@Column(name="cc_country")
	private String ccCountry;

	@Column(name="cc_street_address")
	private String ccStreetAddress;

	@Column(name="closest_center_name")
	private String closestCenterName;

	@Column(name="create_date")
	private Timestamp createDate;

	@Column(name="create_user")
	private String createUser;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="departure_date")
	private Date departureDate;

    @Temporal( TemporalType.DATE)
	@Column(name="departure_dropoff_date")
	private Date departureDropoffDate;

	@Column(name="departure_dropoff_location")
	private String departureDropoffLocation;

	@Column(name="departure_dropoff_needed")
	private String departureDropoffNeeded;

	@Column(name="departure_dropoff_notes")
	private String departureDropoffNotes;

	@Column(name="departure_dropoff_time")
	private Time departureDropoffTime;

	@Column(name="departure_mot")
	private String departureMot;

	@Column(name="departure_time")
	private Time departureTime;

	@Column(name="email")
	private String email;

	@Column(name="emergency_ph")
	private String emergencyPh;

	@Column(name="first_name")
	private String firstName;

	@Column(name="first_visit")
	private String firstVisit;

	private String gender;

	@Column(name="group_name")
	private String groupName = "";

	@Column(name="last_name")
	private String lastName;

	@Column(name="middle_name")
	private String middleName;

	@Column(name="other_ph")
	private String otherPh;

	@Column(name="prefect_email")
	private String prefectEmail;

	@Column(name="prefect_name")
	private String prefectName;

	@Column(name="preferred_accomodation")
	private String preferredAccomodation;

	@Column(name="primary_ph")
	private String primaryPh;

	@Column(name="special_notes_1")
	private String specialNotes1;

	@Column(name="special_notes_2")
	private String specialNotes2;

	@Column(name="special_notes_3")
	private String specialNotes3;

	@Column(name="special_notes_4")
	private String specialNotes4;

	@Column(name="special_notes_5")
	private String specialNotes5;

	@Column(name="volunteer")
	private String volunteer = volunteerNotSelected;

	@Column(name="accept_disclaimer")
	private String acceptDisclaimer;
	
	@Column(name="paid_by_card")
	private String payThruWeb="No";
	
	
	@Column(name="amount_paid")
	private Double amountToPay;
	
	@Column(name="credit_card_trans_no")
	private String ccTransactionNo;
	
	
	
	//bi-directional many-to-one association to SeminarRegistrant
    @ManyToOne
	@JoinColumn(name="parent_registrant_id",nullable=true)
	private SeminarRegistrant seminarRegistrant;

	//bi-directional many-to-one association to SeminarRegistrant
	@OneToMany(mappedBy="seminarRegistrant", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<SeminarRegistrant> seminarRegistrants;

	//bi-directional many-to-one association to SeminarOrig
    @ManyToOne
	@JoinColumn(name="seminar_id")
	private Seminar seminarOrig;

	//bi-directional many-to-one association to SeminarRegistrantCustFld
	@OneToMany(mappedBy="seminarRegistrant", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<SeminarRegistrantCustFld> seminarRegistrantCustFlds;
	
	@ManyToOne
	@JoinColumn(name="volunteer",nullable=true,insertable=false,updatable=false)
	private VolunteerWorkType volunteerType;

    public SeminarRegistrant() {
    }

	public Long getSeminarRegistrantId() {
		return this.seminarRegistrantId;
	}

	public void setSeminarRegistrantId(Long seminarRegistrantId) {
		this.seminarRegistrantId = seminarRegistrantId;
	}

	public String getAbhyasiId() {
		return this.abhyasiId;
	}

	public void setAbhyasiId(String abhyasiId) {
		this.abhyasiId = abhyasiId;
	}

	public String getAccomodationAllotmentNo() {
		return this.accomodationAllotmentNo;
	}

	public void setAccomodationAllotmentNo(String accomodationAllotmentNo) {
		this.accomodationAllotmentNo = accomodationAllotmentNo;
	}

	public String getAccomodatonNotes() {
		return this.accomodatonNotes;
	}

	public void setAccomodatonNotes(String accomodatonNotes) {
		this.accomodatonNotes = accomodatonNotes;
	}

	public Short getAge() {
		return this.age;
	}

	public void setAge(Short age) {
		this.age = age;
	}

	public Date getArrivalDate() {
		return this.arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getArrivalMo() {
		return this.arrivalMo;
	}

	public void setArrivalMo(String arrivalMo) {
		this.arrivalMo = arrivalMo;
	}

	public Date getArrivalPickupDate() {
		return this.arrivalPickupDate;
	}

	public void setArrivalPickupDate(Date arrivalPickupDate) {
		this.arrivalPickupDate = arrivalPickupDate;
	}

	public String getArrivalPickupLocation() {
		return this.arrivalPickupLocation;
	}

	public void setArrivalPickupLocation(String arrivalPickupLocation) {
		this.arrivalPickupLocation = arrivalPickupLocation;
	}

	public String getArrivalPickupNeeded() {
		return this.arrivalPickupNeeded;
	}

	public void setArrivalPickupNeeded(String arrivalPickupNeeded) {
		this.arrivalPickupNeeded = arrivalPickupNeeded;
	}

	public String getArrivalPickupNotes() {
		return this.arrivalPickupNotes;
	}

	public void setArrivalPickupNotes(String arrivalPickupNotes) {
		this.arrivalPickupNotes = arrivalPickupNotes;
	}

	public Time getArrivalPickupTime() {
		return this.arrivalPickupTime;
	}

	public void setArrivalPickupTime(Time arrivalPickupTime) {
		this.arrivalPickupTime = arrivalPickupTime;
	}

	public Time getArrivalTime() {
		return this.arrivalTime;
	}

	public void setArrivalTime(Time arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getBeddingNeeded() {
		return this.beddingNeeded;
	}

	public void setBeddingNeeded(String beddingNeeded) {
		this.beddingNeeded = beddingNeeded;
	}

	public String getCcCityDetail() {
		return this.ccCityDetail;
	}

	public void setCcCityDetail(String ccCityDetail) {
		this.ccCityDetail = ccCityDetail;
	}

	public String getCcState() {
		return ccState;
	}

	public void setCcState(String ccState) {
		this.ccState = ccState;
	}

	public String getCcZip() {
		return ccZip;
	}

	public void setCcZip(String ccZip) {
		this.ccZip = ccZip;
	}
	
	public String getCcCountry() {
		return this.ccCountry;
	}

	public void setCcCountry(String ccCountry) {
		this.ccCountry = ccCountry;
	}

	public String getCcStreetAddress() {
		return this.ccStreetAddress;
	}

	public void setCcStreetAddress(String ccStreetAddress) {
		this.ccStreetAddress = ccStreetAddress;
	}

	public String getClosestCenterName() {
		return this.closestCenterName;
	}

	public void setClosestCenterName(String closestCenterName) {
		this.closestCenterName = closestCenterName;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getDepartureDate() {
		return this.departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public Date getDepartureDropoffDate() {
		return this.departureDropoffDate;
	}

	public void setDepartureDropoffDate(Date departureDropoffDate) {
		this.departureDropoffDate = departureDropoffDate;
	}

	public String getDepartureDropoffLocation() {
		return this.departureDropoffLocation;
	}

	public void setDepartureDropoffLocation(String departureDropoffLocation) {
		this.departureDropoffLocation = departureDropoffLocation;
	}

	public String getDepartureDropoffNeeded() {
		return this.departureDropoffNeeded;
	}

	public void setDepartureDropoffNeeded(String departureDropoffNeeded) {
		this.departureDropoffNeeded = departureDropoffNeeded;
	}

	public String getDepartureDropoffNotes() {
		return this.departureDropoffNotes;
	}

	public void setDepartureDropoffNotes(String departureDropoffNotes) {
		this.departureDropoffNotes = departureDropoffNotes;
	}

	public Time getDepartureDropoffTime() {
		return this.departureDropoffTime;
	}

	public void setDepartureDropoffTime(Time departureDropoffTime) {
		this.departureDropoffTime = departureDropoffTime;
	}

	public String getDepartureMot() {
		return this.departureMot;
	}

	public void setDepartureMot(String departureMot) {
		this.departureMot = departureMot;
	}

	public Time getDepartureTime() {
		return this.departureTime;
	}

	public void setDepartureTime(Time departureTime) {
		this.departureTime = departureTime;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmergencyPh() {
		return this.emergencyPh;
	}

	public void setEmergencyPh(String emergencyPh) {
		this.emergencyPh = emergencyPh;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstVisit() {
		return this.firstVisit;
	}

	public void setFirstVisit(String firstVisit) {
		this.firstVisit = firstVisit;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getOtherPh() {
		return this.otherPh;
	}

	public void setOtherPh(String otherPh) {
		this.otherPh = otherPh;
	}

	public String getPrefectEmail() {
		return this.prefectEmail;
	}

	public void setPrefectEmail(String prefectEmail) {
		this.prefectEmail = prefectEmail;
	}

	public String getPrefectName() {
		return this.prefectName;
	}

	public void setPrefectName(String prefectName) {
		this.prefectName = prefectName;
	}

	public String getPreferredAccomodation() {
		return this.preferredAccomodation;
	}

	public void setPreferredAccomodation(String preferredAccomodation) {
		this.preferredAccomodation = preferredAccomodation;
	}

	public String getPrimaryPh() {
		return this.primaryPh;
	}

	public void setPrimaryPh(String primaryPh) {
		this.primaryPh = primaryPh;
	}

	public String getSpecialNotes1() {
		return this.specialNotes1;
	}

	public void setSpecialNotes1(String specialNotes1) {
		this.specialNotes1 = specialNotes1;
	}

	public String getSpecialNotes2() {
		return this.specialNotes2;
	}

	public void setSpecialNotes2(String specialNotes2) {
		this.specialNotes2 = specialNotes2;
	}

	public String getSpecialNotes3() {
		return this.specialNotes3;
	}

	public void setSpecialNotes3(String specialNotes3) {
		this.specialNotes3 = specialNotes3;
	}

	public String getSpecialNotes4() {
		return this.specialNotes4;
	}

	public void setSpecialNotes4(String specialNotes4) {
		this.specialNotes4 = specialNotes4;
	}

	public String getSpecialNotes5() {
		return this.specialNotes5;
	}

	public void setSpecialNotes5(String specialNotes5) {
		this.specialNotes5 = specialNotes5;
	}

	public String getVolunteer() {
		return this.volunteer;
	}

	public void setVolunteer(String volunteer) {
		this.volunteer = volunteer;
	}

	public SeminarRegistrant getSeminarRegistrant() {
		return this.seminarRegistrant;
	}

	public void setSeminarRegistrant(SeminarRegistrant seminarRegistrant) {
		this.seminarRegistrant = seminarRegistrant;
	}
	
	public List<SeminarRegistrant> getSeminarRegistrants() {
		return this.seminarRegistrants;
	}

	public void setSeminarRegistrants(List<SeminarRegistrant> seminarRegistrants) {
		this.seminarRegistrants = seminarRegistrants;
	}
	
	public Seminar getSeminarOrig() {
		return this.seminarOrig;
	}

	public void setSeminarOrig(Seminar seminarOrig) {
		this.seminarOrig = seminarOrig;
	}
	
	public List<SeminarRegistrantCustFld> getSeminarRegistrantCustFlds() {
		return this.seminarRegistrantCustFlds;
	}

	public void setSeminarRegistrantCustFlds(List<SeminarRegistrantCustFld> seminarRegistrantCustFlds) {
		this.seminarRegistrantCustFlds = seminarRegistrantCustFlds;
	}
	
	public boolean getRenderCustomFields(){
		if(seminarRegistrantCustFlds == null || seminarRegistrantCustFlds.size() == 0){
			return false;
		}
		return true;
	}
	
	public Date getArrivalTimeAsDate(){
		return arrivalTime;
	}
	public void setArrivalTimeAsDate(Date time){
		if(time != null && !time.equals("")){
			arrivalTime = new Time(time.getTime());
		}
	}

	public Date getDepartureTimeAsDate(){
		return departureTime;
	}
	public void setDepartureTimeAsDate(Date time){
		if(time != null && !time.equals("")){
			departureTime = new Time(time.getTime());
		}
	}
	
	public Date getArrivalPickupTimeAsDate(){
		return arrivalPickupTime;
	}
	public void setArrivalPickupTimeAsDate(Date time){
		if(time != null && !time.equals("")){
			arrivalPickupTime = new Time(time.getTime());
		}
	}
	
	
	public Date getDepartureDropoffTimeAsDate(){
		return departureDropoffTime;
	}
	
	public void setDepartureDropoffTimeAsDate(Date time){
		if(time != null && !time.equals("")){
			departureDropoffTime = new Time(time.getTime());
		}
	}
		
	public VolunteerWorkType getVolunteerType() {
		return volunteerType;
	}

	public void setVolunteerType(VolunteerWorkType volunteerType) {
		this.volunteerType = volunteerType;
	}

	public String getAcceptDisclaimer() {
		return acceptDisclaimer;
	}

	public void setAcceptDisclaimer(String acceptDisclaimer) {
		this.acceptDisclaimer = acceptDisclaimer;
	}

	public int getRegistrantsCount(){
		
		if(this.seminarRegistrants == null || this.seminarRegistrants.size() == 0){
			return 1;
		}
		
		return this.seminarRegistrants.size()+1;
	}
	
	public String getCustFieldsAsString(){
		
		StringBuffer bffr = new StringBuffer("");
		
		for(SeminarRegistrantCustFld src : seminarRegistrantCustFlds){
			bffr.append(src.getSeminarCustomField().getFieldLabel()+":"+src.getValue()+" ");
		}
		
		return bffr.toString();
		
	}
	
	
	public String getPrimaryRegistrant(){
		
		return ((seminarRegistrant == null)?"Yes":"");
	}
	
	public String getAgeRange()
	{
		if (age < AgeRange.values().length)
			return ((AgeRange[])AgeRange.values())[age].getLabel();
		else return "UNKONWN";
	}

	public SeminarRegistrant copyPrimaryInfo(SeminarRegistrant dependent) {
		dependent.setAccomodatonNotes(this.accomodatonNotes); 
		dependent.setArrivalDate(this.arrivalDate);
		dependent.setArrivalPickupDate(this.arrivalPickupDate);
		dependent.setArrivalPickupLocation(this.arrivalPickupLocation);
		dependent.setArrivalPickupNeeded(this.arrivalPickupNeeded);
		dependent.setArrivalPickupNotes(this.arrivalPickupNotes);
		dependent.setArrivalPickupTime(this.arrivalPickupTime);
		dependent.setArrivalTime(this.arrivalTime);
		dependent.setBeddingNeeded(this.beddingNeeded);
		dependent.setCcCityDetail(this.ccCityDetail);
		dependent.setCcCountry(this.ccCountry);
		dependent.setCcStreetAddress(this.ccStreetAddress);
		dependent.setClosestCenterName(this.closestCenterName);
		dependent.setCreateDate(this.createDate);
		dependent.setCreateUser(this.createUser);
		dependent.setDepartureDate(this.departureDate);
		dependent.setDepartureDropoffDate(this.departureDropoffDate);
		dependent.setDepartureDropoffLocation(this.departureDropoffLocation);
		dependent.setDepartureDropoffNeeded(this.departureDropoffNeeded);
		dependent.setDepartureDropoffNotes(this.departureDropoffNotes);
		dependent.setDepartureDropoffTime(this.departureDropoffTime);
		dependent.setDepartureTime(this.departureTime);
		dependent.setEmergencyPh(this.emergencyPh); 
		dependent.setLastName(this.lastName);
		dependent.setOtherPh(this.otherPh);
		dependent.setPrefectEmail(this.prefectEmail); 
		dependent.setPrefectName(this.prefectName);
		dependent.setPreferredAccomodation(this.preferredAccomodation);
		dependent.setPrimaryPh(this.primaryPh);
		dependent.setSpecialNotes1(this.specialNotes1);
		dependent.setSpecialNotes2(this.specialNotes2);
		dependent.setSpecialNotes3(this.specialNotes3);
		dependent.setSpecialNotes4(this.specialNotes4);
		dependent.setSpecialNotes5(this.specialNotes5);
		dependent.setVolunteer(this.volunteer);
		
		dependent.setCcState(this.ccState);
		dependent.setCcZip(this.ccZip);
		return dependent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((abhyasiId == null) ? 0 : abhyasiId.hashCode());
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SeminarRegistrant other = (SeminarRegistrant) obj;
		if (abhyasiId == null) {
			if (other.abhyasiId != null)
				return false;
		} else if (!abhyasiId.equals(other.abhyasiId))
			return false;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (ccTransactionNo == null) {
			if (other.ccTransactionNo != null)
				return false;
		} else if (!ccTransactionNo.equals(other.ccTransactionNo))
			return false;
		return true;
	}

	public String getPayThruWeb() {
		return payThruWeb;
	}

	public void setPayThruWeb(String payThruWeb) {
		this.payThruWeb = payThruWeb;
	}

	public Double getAmountToPay() {
		return amountToPay;
	}

	public void setAmountToPay(Double amountToPay) {
		this.amountToPay = amountToPay;
	}

	public String getCcTransactionNo() {
		return ccTransactionNo;
	}

	public void setCcTransactionNo(String ccTransactionNo) {
		this.ccTransactionNo = ccTransactionNo;
	}

	
}