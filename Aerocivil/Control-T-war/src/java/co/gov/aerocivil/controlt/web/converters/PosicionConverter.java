/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.converters;


import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.services.PosicionService;
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
        if(o==null){
            return null;
        }
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
