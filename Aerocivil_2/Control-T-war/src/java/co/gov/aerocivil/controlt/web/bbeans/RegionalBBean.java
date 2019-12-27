/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.services.RegionalService;
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
public class RegionalBBean {

    //private RegionalDelegate regionalDelegate = RegionalDelegate.getInstance();
    //private RegionalService regionalService = RegionalDelegate.getInstance().getRegionalService();
    @EJB
    private RegionalService regionalService; 
    
    private Regional regional;
    private List<Regional> lista;

    public List<Regional> getLista() {
        return lista;
    }

    public void setLista(List<Regional> lista) {
        this.lista = lista;
    }

    public Regional getRegional() {
        return regional;
    }

    public void setRegional(Regional regional) {
        this.regional = regional;
    }
    
     public String crear() {
         regional = new Regional();
         return "editarRegional";
    }
     
     public String editar() {
        return "editarRegional";
    }
     public String guardar() {
        regional = regionalService.guardar(regional, 
                JsfUtil.getFuncionarioSesion());
        JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        ((ListadosBBean)JsfUtil.getListadosBBean()).setListaRegionales(null);        
        return listar(); 
    }
     
     public String listar() {
        lista = regionalService.getLista();
        return "listarRegional";
    }

}
