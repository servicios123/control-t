/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.converters;

import co.gov.aerocivil.controlt.entities.DiarioSenalTipo;
import co.gov.aerocivil.controlt.services.DiarioSenalEspecialService;
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
@FacesConverter(value="DiarioSenalTipoConverter")
@ManagedBean(name="DiarioSenalTipoConverter")
@SessionScoped
public class DiarioSenalTipoConverter implements Converter {
    @EJB
    DiarioSenalEspecialService diarioSenalEspecialService;
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        Long id= Long.valueOf(string);
        if(string==null || string.isEmpty())
        {
            return null;
        }
        return diarioSenalEspecialService.obtenerTipoPorId(id);
        
        
        
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if(o==null){
            return null;
        }
       try{
            long id= ((DiarioSenalTipo) o).getDstId();           
            String s = String.valueOf(id);
            return s;
       }catch(ClassCastException ex)
       {
           return null;
       }
       
      
    }
    
}