/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.services.DepCategoriaService;
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
public class DepCategoriaBBean {

    @EJB
    private DepCategoriaService depcategoriaService; 
    private DepCategoria depcategoria;
    private DepCategoria depcategoriaFiltro;

    
    private List<DepCategoria> lista;

    public DepCategoriaService getDepcategoriaService() {
        return depcategoriaService;
    }

    public void setDepcategoriaService(DepCategoriaService depcategoriaService) {
        this.depcategoriaService = depcategoriaService;
    }

    public DepCategoria getDepcategoria() {
        return depcategoria;
    }

    public void setDepcategoria(DepCategoria depcategoria) {
        this.depcategoria = depcategoria;
    }

    public List<DepCategoria> getLista() {
        return lista;
    }

    public void setLista(List<DepCategoria> lista) {
        this.lista = lista;
    }
    
     public String listar() {
        depcategoriaFiltro = new DepCategoria();
        return filtrar();
    }
    
     public String filtrar() {
       
        lista = depcategoriaService.getLista(depcategoriaFiltro);
        return "listarDepCategoria";
    }
     
     public String editar() {
        return "editarDepCategoria";
    }
     public String guardar() {
        depcategoriaService.guardar(depcategoria, 
                JsfUtil.getFuncionarioSesion()); 
        JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        return filtrar();
    }
     
     public DepCategoria getDepcategoriaFiltro() {
        return depcategoriaFiltro;
    }

    public void setDepcategoriaFiltro(DepCategoria depcategoriaFiltro) {
        this.depcategoriaFiltro = depcategoriaFiltro;
    }
}
