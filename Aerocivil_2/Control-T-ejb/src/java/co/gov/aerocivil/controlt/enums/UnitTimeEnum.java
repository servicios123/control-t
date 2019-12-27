/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.enums;

import java.util.Calendar;

/**
 *
 * @author Administrador
 */
public enum UnitTimeEnum {
    DAYS(Calendar.DAY_OF_MONTH,1000*60*60*24),
    HOURS(Calendar.HOUR_OF_DAY,1000*60*60),
    MINUTES(Calendar.MINUTE,1000*60),
    SECONDS(Calendar.SECOND,1000);
    
    private int unit;
    private int millisecs;

    private UnitTimeEnum(int unit, int millisecs) {
        this.unit = unit;
        this.millisecs = millisecs;
    }    

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getMillisecs() {
        return millisecs;
    }

    public void setMillisecs(int millisecs) {
        this.millisecs = millisecs;
    }
    
}
