package com.cci.gpec.web.validator;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class ValidateDateTimeRange implements Validator{

	public void validate(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {
		
		Date dateValue = (Date) value;
		Date dateTest = (Date) component.getAttributes().get("toDate");
	}

}
