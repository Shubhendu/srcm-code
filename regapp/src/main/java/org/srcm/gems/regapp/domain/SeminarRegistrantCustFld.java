package org.srcm.gems.regapp.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;


/**
 * The persistent class for the SEMINAR_REGISTRANT_CUST_FLD database table.
 * 
 */
@Entity
@Table(name="SEMINAR_REGISTRANT_CUST_FLD")
@NamedQueries({
	@NamedQuery(name="SeminarRegistrantCustFld.deleteSeminarRegistrantCustFld", query="delete SeminarRegistrantCustFld reg where reg.seminarRegistrant.seminarRegistrantId = :seminarRegistrantId")
})
public class SeminarRegistrantCustFld implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String DELIMITER = ":";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="srcf_id")
	private int srcfId;

	@Column(name="value")
	private String value;
	
	@Transient
	private List<String> selectedChkBoxOptions;

	//bi-directional many-to-one association to SeminarCustomField
    @ManyToOne
	@JoinColumn(name="sem_cust_fld_id")
	private SeminarCustomField seminarCustomField;

	//bi-directional many-to-one association to SeminarRegistrant
    @ManyToOne
	@JoinColumn(name="sem_reg_id")
	private SeminarRegistrant seminarRegistrant;

    public SeminarRegistrantCustFld() {
    }

	public int getSrcfId() {
		return this.srcfId;
	}

	public void setSrcfId(int srcfId) {
		this.srcfId = srcfId;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SeminarCustomField getSeminarCustomField() {
		return this.seminarCustomField;
	}

	public void setSeminarCustomField(SeminarCustomField seminarCustomField) {
		this.seminarCustomField = seminarCustomField;
	}
	
	public SeminarRegistrant getSeminarRegistrant() {
		return this.seminarRegistrant;
	}

	public void setSeminarRegistrant(SeminarRegistrant seminarRegistrant) {
		this.seminarRegistrant = seminarRegistrant;
	}

	public List<String> getSelectedChkBoxOptions() {
		if(this.value!=null){
			String[] optionList = this.value.split(DELIMITER);
			this.selectedChkBoxOptions = Arrays.asList(optionList);
		}
		return this.selectedChkBoxOptions;
	}

	public void setSelectedChkBoxOptions(List<String> selectedChkBoxOptions) {
		this.selectedChkBoxOptions = selectedChkBoxOptions;

		StringBuilder sb = new StringBuilder();
		for (String s : selectedChkBoxOptions)
		{
			sb.append(s);
			sb.append(DELIMITER);
		}
		this.value = sb.toString();
	}
	

	public boolean equals(Object obj)
	{
		if (obj instanceof SeminarRegistrantCustFld)
		{
			SeminarRegistrantCustFld scf = (SeminarRegistrantCustFld) obj;
			if (value.equalsIgnoreCase(scf.value) )
				return true;

		}
		return false;

	}
}