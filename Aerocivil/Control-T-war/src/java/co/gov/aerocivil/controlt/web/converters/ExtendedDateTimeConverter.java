/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.converters;

import java.util.TimeZone;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Administrador
 */
@FacesConverter("extendedDateTimeConverter")
public class ExtendedDateTimeConverter extends DateTimeConverter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        setPattern("dd/MM/yyyy HH:mm");
        if (component.getAttributes().get("pattern")!=null){
            setPattern((String) component.getAttributes().get("pattern"));
        }
        //setTimeZone(TimeZone.getTimeZone((String) component.getAttributes().get("timeZone")));
        setTimeZone(TimeZone.getTimeZone("America/Bogota"));
        return super.getAsObject(context, component, value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        //setPattern((String) component.getAttributes().get("pattern"));
        setPattern("dd/MM/yyyy HH:mm");
        if (component.getAttributes().get("pattern")!=null){
            setPattern((String) component.getAttributes().get("pattern"));
        }
        setTimeZone(TimeZone.getTimeZone("America/Bogota"));
        //setTimeZone(TimeZone.getTimeZone((String) component.getAttributes().get("timeZone")));
        return super.getAsString(context, component, value);
    }

}