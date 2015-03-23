/**
 * 
 */
package org.srcm.gems.regapp.domain;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.srcm.gems.regapp.dao.UserDAOImpl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author singh_sh
 *
 */
@ManagedBean
@RequestScoped
@FacesConverter("userConverter")
public class UserConverter implements Converter {

//	private UserDAOImpl userService;
	ObjectMapper mapper = new ObjectMapper();

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if(value == null || value.equals("")) {
			return null;
		}
			try {
				User userObj = mapper.readValue(value, User.class);
				System.out.println("First name: "+userObj.getFirstName());
				return userObj;
			} catch (JsonParseException e) {
				e.printStackTrace();
				return null;
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
//			return userService.getUserById(Integer.valueOf(value));
		catch (NumberFormatException e) {
			throw new ConverterException(new FacesMessage(String.format("%s is not a valid User ID", value)), e);
		}
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}
		if(value instanceof User){
			try {
				String userValue =  mapper.writeValueAsString(value);
				System.out.println("User json:"+userValue);
				return userValue;
			} catch (JsonProcessingException e) {
				throw new ConverterException(new FacesMessage(String.format("%s is not a valid User", value)).toString());
			}
//			return String.valueOf(((User)value).getId());
		}else {
			throw new ConverterException(new FacesMessage(String.format("%s is not a valid User", value)).toString());
		}

	}

}
