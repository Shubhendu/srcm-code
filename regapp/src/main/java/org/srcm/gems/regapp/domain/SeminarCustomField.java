package org.srcm.gems.regapp.domain;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * The persistent class for the SEMINAR_CUSTOM_FIELD database table.
 * 
 */

@Entity
@NamedQueries( {
	@NamedQuery(name = "findCustomFieldsForSeminar", query = "select s from SeminarCustomField s where s.seminar.seminarId = :semId")
})
@Table(name="SEMINAR_CUSTOM_FIELD")
public class SeminarCustomField implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String DELIMITER = ":";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="seminar_custom_field_id")
	private Long seminarCustomFieldId;

	@NotEmpty
	@Size(max=100)
	@Column(name="field_desc")
	private String fieldDesc;

	@NotEmpty
	@Size(max=45)
	@Column(name="field_label")
	private String fieldLabel;

	@Column(name="required")
	private Integer required;
	
	@Column(name="field_type")
	private Short fieldType;
	
	@Size(max=512)
	@Column(name="`possible_field_values`")
	private String possibleFieldValues;
	
	@Transient
	private List<String> fieldValueChoices;

	//bi-directional many-to-one association to SeminarOrig
    @ManyToOne
	@JoinColumn(name="seminar_id")
	private Seminar seminar;

	//bi-directional many-to-one association to SeminarRegistrantCustFld
	@OneToMany(mappedBy="seminarCustomField")
	private List<SeminarRegistrantCustFld> seminarRegistrantCustFlds;

    public SeminarCustomField() {
    }

	public Long getSeminarCustomFieldId() {
		return this.seminarCustomFieldId;
	}

	public String getElementId() {
		return this.fieldLabel.replace(' ', '_') + this.seminarCustomFieldId;
	}
	
	public void setSeminarCustomFieldId(Long seminarCustomFieldId) {
		this.seminarCustomFieldId = seminarCustomFieldId;
	}

	public String getFieldDesc() {
		return this.fieldDesc;
	}

	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}

	public String getFieldLabel() {
		return this.fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public Integer getRequired() {
		return this.required;
	}

	public void setRequired(Integer required) {
		this.required = required;
	}
	
	public String getRequiredText(){
		
		return (required == 1)? "Yes" : "No";
	}

	public String getRequiredBoolean(){
		
		return (required == 1)? "true" : "false";
	}

	public Seminar getSeminar() {
		return this.seminar;
	}

	public void setSeminar(Seminar seminar) {
		this.seminar = seminar;
	}
	
	public List<SeminarRegistrantCustFld> getSeminarRegistrantCustFlds() {
		return this.seminarRegistrantCustFlds;
	}

	public void setSeminarRegistrantCustFlds(List<SeminarRegistrantCustFld> seminarRegistrantCustFlds) {
		this.seminarRegistrantCustFlds = seminarRegistrantCustFlds;
	}

	public String getFieldLabelText() {
		if(required == 1){
			return fieldLabel + ": *";
		}
		return this.fieldLabel + ": ";
	}
	
	public Short getFieldType() {
		return fieldType;
	}

	public void setFieldType(Short fieldType) {
		this.fieldType = fieldType;
	}

	public String getPossibleFieldValues() {
		return possibleFieldValues;
	}

	public void setPossibleFieldValues(String possibleFieldValues) {
		this.possibleFieldValues = possibleFieldValues;
	}
	
	public List<String> getFieldValueChoices() {
		//List<String> fieldValueChoices = new ArrayList<String>();
		if ( this.possibleFieldValues!=null)
			fieldValueChoices = Arrays.asList(possibleFieldValues.split(DELIMITER));
		return fieldValueChoices;	
	}
	
	public void setFieldValueChoices(List<String> anyList) {
		fieldValueChoices = anyList;
		
	}
	
	public boolean equals(Object obj)
	{
		if (obj instanceof SeminarCustomField)
		{
			SeminarCustomField scf = (SeminarCustomField) obj;
			if (fieldDesc.equalsIgnoreCase(scf.fieldDesc) &&
					(this.fieldType == scf.getFieldType()) &&
					(this.possibleFieldValues.equalsIgnoreCase(scf.getPossibleFieldValues())))
				return true;

		}
		return false;

	}

}