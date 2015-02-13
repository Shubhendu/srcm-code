package org.srcm.gems.regapp.dao;

import java.util.List;

import org.srcm.gems.regapp.domain.SeminarCustomField;

public interface SeminarCustomFieldDAO {

	public List<SeminarCustomField> getCustomFieldsForSeminar(Long seminarId);
}
