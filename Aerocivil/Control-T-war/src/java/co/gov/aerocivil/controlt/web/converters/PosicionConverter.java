/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.converters;


import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.services.ListasService;
import co.gov.aerocivil.controlt.services.PosicionService;
import co.gov.aerocivil.controlt.services.PosicionServiceBean;
import co.gov.aerocivil.controlt.web.bbeans.PosicionBBean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Administrador
 */

@FacesConverter(value="PosicionConverter")
@ManagedBean(name="PosicionConverter")
@SessionScoped
public class PosicionConverter implements Converter {

     
  @EJB
  PosicionService posicionService;
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        Long id= Long.valueOf(string);
        if(string==null || string.isEmpty())
        {
            return null;
        }
        return posicionService.getPorId(id);    
        
             
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
       try{
            long id= ((Posicion) o).getPosId();           
            String s = String.valueOf(id);
            return s;
       }catch(ClassCastException ex)
       {
           return null;
       }
    }
    
}
