package org.srcm.gems.regapp.web.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;
import org.srcm.gems.regapp.domain.SeminarRegistrant;

public class SeminarRegistrantDataModel extends
		ListDataModel<SeminarRegistrant> implements
		SelectableDataModel<SeminarRegistrant>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SeminarRegistrantDataModel(){
		
	}
	
	public SeminarRegistrantDataModel(List<SeminarRegistrant> data){
		super(data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SeminarRegistrant getRowData(String rowKey) {

		List<SeminarRegistrant> registrants = (List<SeminarRegistrant>)getWrappedData();
		for(SeminarRegistrant registrant : registrants){
			if(this.getRowKey(registrant).equals(rowKey)){
				return registrant;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(SeminarRegistrant registrant) {
		return registrant;
	}

}
