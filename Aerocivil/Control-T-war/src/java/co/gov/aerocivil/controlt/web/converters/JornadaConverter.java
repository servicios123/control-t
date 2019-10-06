/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.converters;

import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.services.JornadaService;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Administrador
 */
@FacesConverter(value="JornadaConverter")
@ManagedBean(name="JornadaConverter")
@SessionScoped
public class JornadaConverter implements Converter {
     @EJB
    JornadaService jornadaService;
     
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        try{
            Long id= Long.valueOf(string);
            if(string==null || string.isEmpty())
        {
            return null;
        }
        return jornadaService.getPorId(id);  
        }catch(NumberFormatException e){
            return null;
        }
        
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o == null) {
            return null;
        }
        try{
            long id= ((Jornada) o).getJoId();          
            String s = String.valueOf(id);
            return s;
       }catch(ClassCastException ex)
       {
           return null;
       }
    }
    
}
