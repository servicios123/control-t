/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Ciudad;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.services.AeropuertoService;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import co.gov.aerocivil.controlt.web.util.ListadosBBean;
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
public class AeropuertoBBean {

   //private RegionalDelegate regionalDelegate = RegionalDelegate.getInstance();
    //private RegionalService regionalService = RegionalDelegate.getInstance().getRegionalService();
    @EJB
    private AeropuertoService aeropuertoService; 
    
    private Aeropuerto aeropuerto;
    private Aeropuerto aeropuertoFiltro;

    
    private List<Aeropuerto> lazyList;

    public Aeropuerto getAeropuerto() {
        return aeropuerto;
    }

    public void setAeropuerto(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
    }
    
     public String crear() {
         //aeropuertoFiltro = new Aeropuerto();
         Regional regional = new Regional();
         Ciudad ciudad = new Ciudad();
         aeropuerto = new Aeropuerto();
         
         aeropuerto.setRegional(regional);
          aeropuerto.setCiudad(ciudad);
         return "editarAeropuerto";
    }
     
     public String editar() {
         //System.out.println(aeropuerto);
        return "editarAeropuerto";
    }
     public String guardar() {
        aeropuerto = aeropuertoService.guardar(aeropuerto, 
                JsfUtil.getFuncionarioSesion());
        JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        ((ListadosBBean)JsfUtil.getListadosBBean()).setListaAeropuertos(null);
        return listar();
    }
     /**
     *
     * @return
     */
   public String listar() {
        aeropuertoFiltro = new Aeropuerto();
        Regional regional = new Regional();
        Ciudad ciudad = new Ciudad();
        aeropuertoFiltro.setRegional(regional);
        aeropuertoFiltro.setCiudad(ciudad);
        return filtrar();
    }
    
    public String filtrar() {
       
        lazyList= aeropuertoService.getLista(aeropuertoFiltro);
        return "listarAeropuerto";
    }
    
    public Aeropuerto getAeropuertoFiltro() {
        return aeropuertoFiltro;
    }

    public void setAeropuertoFiltro(Aeropuerto aeropuertoFiltro) {
        this.aeropuertoFiltro = aeropuertoFiltro;
    }

    public List<Aeropuerto> getLazyList() {
        return lazyList;
    }


}
