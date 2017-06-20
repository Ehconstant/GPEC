package com.cci.gpec.icefaces.component.calendar;

import java.util.Date;
import java.util.TimeZone;

import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;

import javax.faces.event.ValueChangeEvent;

/**
 * <p>
 * The SelectInputDateBean Class is used to store the selected dates from the
 * selectinputdate components.
 * </p>
 * 
 * @since 0.3
 */
public class SelectInputDateBean {
	/**
	 * Variables to store the selected dates.
	 */
	private Date date1 = new Date();

	// effect is fired when dat2 value is changed.
	protected Effect valueChangeEffect;

	public SelectInputDateBean() {
		super();
		valueChangeEffect = new Highlight("#fda505");
		valueChangeEffect.setFired(true);
	}

	/**
	 * Gets the first selected date.
	 * 
	 * @return the first selected date
	 */
	public Date getDate1() {
		return date1;
	}

	/**
	 * Sets the first selected date.
	 * 
	 * @param date
	 *            the first selected date
	 */
	public void setDate1(Date date) {
		date1 = date;
	}

	/**
	 * Gets the default timezone of the host server. The timezone is needed by
	 * the convertDateTime for formatting the time dat values.
	 * 
	 * @return timezone for the current JVM
	 */
	public TimeZone getTimeZone() {
		return java.util.TimeZone.getDefault();
	}

    public void effectChangeListener(ValueChangeEvent event){
        valueChangeEffect.setFired(false);
    }

     public Effect getValueChangeEffect() {
        return valueChangeEffect;
    }

    public void setValueChangeEffect(Effect valueChangeEffect) {
        this.valueChangeEffect = valueChangeEffect;
    }
}
