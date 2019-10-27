/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.converters;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
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
@FacesConverter(value="VistaProgramacionConverter")
@ManagedBean(name="VistaProgramacionConverter")
@SessionScoped
public class VistaProgramacionConverter implements Converter {
    
    @EJB
    ListasService listasService;
       
       
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        
        if(value==null || value.isEmpty() || value.equalsIgnoreCase("--Seleccione--"))
        {
            //System.out.println("getAsObject null");
            return null;
        }
           
        return listasService.obtenerObjById(Vistaprogramacion.class, Long.parseLong(value));
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
       try{
            if(o==null)         
            {
                //System.out.println("getAsString null o==null ");
                return null;
            }
            long id= ((Vistaprogramacion) o).getTurId();
            //System.out.println("ID  - "+id);
            String s = String.valueOf(id);
            return s;
       }catch(Exception ex)
       {
           //System.out.println("getAsString null exception ");
           return null;
       }        
    }
    
}
