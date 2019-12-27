/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Ciudad;
import co.gov.aerocivil.controlt.entities.Departamento;
import co.gov.aerocivil.controlt.services.CiudadService;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Viviana
 */
@ManagedBean
@SessionScoped
public class CiudadBBean {

    
     @EJB
    private CiudadService ciudadService; 
    private Ciudad ciudad;
    private List<Ciudad> lista;
    
    public CiudadService getCiudadService() {
        return ciudadService;
    }

    public void setCiudadService(CiudadService ciudadService) {
        this.ciudadService = ciudadService;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public List<Ciudad> getLista() {
        return lista;
    }

    public void setLista(List<Ciudad> lista) {
        this.lista = lista;
    }
    
    
    
    public String listar() {
        ciudad = new Ciudad();
        Departamento departamento = new Departamento();
        ciudad.setDepartamento(departamento);
        return filtrar();
    }
    
     public String filtrar() {
       
        lista = ciudadService.getLista(ciudad);
        return "listarCiudad";
    }
}
