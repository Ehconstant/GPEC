package com.cci.gpec.web.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


public class SelectOneMenuValidator implements Validator{

	public void validate(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {
		

		int itemValueSelectOneMenu = (Integer) value;
		
		 if(itemValueSelectOneMenu == -1){
            FacesMessage message = new FacesMessage();
            message.setDetail("Veuillez s\u00E9lectionner une valeur.");
            message.setSummary("Veuillez s\u00E9lectionner une valeur.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
	}

}
