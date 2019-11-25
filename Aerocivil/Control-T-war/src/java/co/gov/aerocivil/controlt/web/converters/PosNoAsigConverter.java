/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.converters;
import co.gov.aerocivil.controlt.entities.PosNoAsig;
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
@FacesConverter(value="PosNoAsigConverter")
@ManagedBean(name="PosNoAsigConverter")
@SessionScoped
public class PosNoAsigConverter implements Converter{

    @EJB
    ListasService listasService;
       
       
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        Long id= Long.valueOf(value);
        if(value==null || value.isEmpty())
        {
            return null;
        }
           
        return listasService.obtenerObjById(PosNoAsig.class, Long.parseLong(value));
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
         try{
                     
            long id= ((PosNoAsig) o).getPna_id();          
            String s = String.valueOf(id);
            return s;
       }catch(Exception ex)
       {
           return null;
       }        
    }
    
}
