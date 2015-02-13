package org.srcm.gems.regapp.web.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;
import org.srcm.gems.regapp.domain.SeminarCustomField;

public class SeminarCustFieldDataModel extends
		ListDataModel<SeminarCustomField> implements
		SelectableDataModel<SeminarCustomField>,Serializable {

	public SeminarCustFieldDataModel(){
		
	}
	
	public SeminarCustFieldDataModel(List<SeminarCustomField> data){
		super(data);
	}

	@Override
	public SeminarCustomField getRowData(String rowKey) {

		List<SeminarCustomField> custFields = (List<SeminarCustomField>)getWrappedData();
		for(SeminarCustomField custFld : custFields){
			if(this.getRowKey(custFld).equals(rowKey)){
				return custFld;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(SeminarCustomField semCustField) {

		if(semCustField.getSeminarCustomFieldId() != null)
			return semCustField.getSeminarCustomFieldId().toString();
		return semCustField.getFieldLabel();
	}

}
