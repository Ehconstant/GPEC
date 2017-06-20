package com.cci.gpec.icefaces.component.calendar;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;

public class DateConverter extends DateTimeConverter {

	@Override
	public String getPattern() {
		// TODO Auto-generated method stub
		return super.getPattern();
	}

	/* (non-Javadoc)
	 * @see javax.faces.convert.DateTimeConverter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) throws ConverterException{		
		boolean isValid = true;
		super.setPattern("dd/MM/yyyy");
		super.setTimeZone(java.util.TimeZone.getDefault());
		String valueInputDate = arg2;
		
		if(valueInputDate != null && valueInputDate != ""){
			if(valueInputDate.contains("/")){
				String[] resultSplitInputDate = valueInputDate.split("/");
				
				if(resultSplitInputDate.length == 3){
					String year = resultSplitInputDate[2];
					
					if(year.length() < 4){
						isValid = false;
					}
				}else{
					isValid = false;
				}
			}
		}
		if (!isValid){

            throw new ConverterException();
        }
		
		return super.getAsObject(arg0, arg1, arg2);	
	}

	/* (non-Javadoc)
	 * @see javax.faces.convert.DateTimeConverter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		// TODO Auto-generated method stub
		
		super.setPattern("dd/MM/yyyy");
		super.setTimeZone(java.util.TimeZone.getDefault());
		return super.getAsString(arg0, arg1, arg2);
	}
	
	
	
	
}
