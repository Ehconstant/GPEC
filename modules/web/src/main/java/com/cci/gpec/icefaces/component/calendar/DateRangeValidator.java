package com.cci.gpec.icefaces.component.calendar;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class DateRangeValidator implements Validator {

	public void validate(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {
		Date myDate = (Date)value;
		Date today = new Date();
		if (myDate.before(today)) {
            FacesMessage message = new FacesMessage();
            message.setDetail("Date is in the past");
            message.setSummary("Date is in the past");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
		}
	}
	
	public boolean estValide(int annee, int mois, int jour){
		Calendar c = Calendar.getInstance();
		c.setLenient(false);
		c.set(annee,mois,jour);        
		try{
		  c.getTime();  
		}
		catch(IllegalArgumentException iAE){
		  return false;
		}
		
		return true;
	}
}
