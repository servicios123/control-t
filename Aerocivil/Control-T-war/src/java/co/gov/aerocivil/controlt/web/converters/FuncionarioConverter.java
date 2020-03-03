/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.converters;

import co.gov.aerocivil.controlt.entities.Funcionario;
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
@FacesConverter(value="FuncionarioConverter")
@ManagedBean(name="FuncionarioConverter")
@SessionScoped
public class FuncionarioConverter implements Converter {

    @EJB
    ListasService listasService;
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value==null || value.isEmpty()|| value.equalsIgnoreCase("--Seleccione--"))
        {
            return null;
        }
        Long id= Long.valueOf(value);   
        return listasService.obtenerObjById(Funcionario.class, Long.parseLong(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
      if(value==null){
          return null;
      }
       try{
                     
            long id= ((Funcionario) value).getFunId();          
            String s = String.valueOf(id);
            return s;
       }catch(Exception ex)
       {
           return null;
       }        
    }
}