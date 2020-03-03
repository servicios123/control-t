/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.converters;
import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import co.gov.aerocivil.controlt.services.ListasService;
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
@FacesConverter(value="TurnoEspecialConverter")
@ManagedBean(name="TurnoEspecialConverter")
@SessionScoped
public class TurnoEspecialConverter implements Converter{

    @EJB
    ListasService listasService;
       
       
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        
        if(value==null || value.isEmpty()|| value.equalsIgnoreCase("--Seleccione--"))
        {
            return null;
        }
        Long id= Long.valueOf(value);   
        return listasService.obtenerObjById(TurnoEspecial.class, Long.parseLong(value));
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if(o==null){
            return null;
        }
         try{
            long id= ((TurnoEspecial) o).getTeId();          
            String s = String.valueOf(id);
            return s;
       }catch(Exception ex)
       {
           return null;
       }        
    }
    
}
