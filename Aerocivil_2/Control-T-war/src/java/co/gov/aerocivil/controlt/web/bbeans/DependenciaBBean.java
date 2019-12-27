/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.DependenciaService;
import co.gov.aerocivil.controlt.web.lazylist.DependenciaLazyList;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import co.gov.aerocivil.controlt.web.util.ListadosBBean;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Viviana
 */
@ManagedBean
@SessionScoped
public class DependenciaBBean {

    /**
     * Creates a new instance of DependenciaBBean
     */
     @EJB
    private DependenciaService dependenciaService; 
    
    private Dependencia dependencia;
    private Dependencia dependenciaFiltro;

   private  List<Aeropuerto>  listAeropuerto;
   
   
    
    private LazyDataModel<Dependencia> lista;

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }
    
     public String crear() {
         DepCategoria depcategoria = new DepCategoria();
         Aeropuerto aeropuerto = new Aeropuerto();
         Regional regional=new Regional();
         dependencia = new Dependencia();
         listAeropuerto = null;
         aeropuerto.setRegional(regional);
         dependencia.setAeropuerto(aeropuerto);
         dependencia.setDepcategoria(depcategoria);
         return "editarDependencia";
    }

    public String editar() {
        cargarAeropuerto();
        return "editarDependencia";
    }
    public String guardar() {
        try {
            dependenciaService.guardar(dependencia, 
                JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
            ((ListadosBBean) JsfUtil.getListadosBBean()).setListaDependencias(null);
            return filtrar();
        } catch (Exception  ex) {
            JsfUtil.addWarningMessage("SQLIntegrityConstraintViolationException");
        }
        return null;
    }
     /**
     *
     * @return
     */
    public String listar() {
        
        Aeropuerto aeropuerto = new Aeropuerto();
        Regional regional= new Regional();
        DepCategoria depcategoria=new DepCategoria();
        dependenciaFiltro = new Dependencia();
        listAeropuerto = null;
        
        aeropuerto.setRegional(regional);
        
        dependenciaFiltro.setAeropuerto(aeropuerto);
        dependenciaFiltro.setDepcategoria(depcategoria);
        
        FuncionarioBBean funcBBean = (FuncionarioBBean)JsfUtil.getManagedBean(FuncionarioBBean.class);
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
         
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
  
            dependenciaFiltro=funcBBean.getDependenciaXNivelUsuario();
            listAeropuerto = funcBBean.getListAeropuerto();
        }
        
        
        return filtrar();
    }
    
    public String filtrar() {       
        lista = new DependenciaLazyList(dependenciaService, dependenciaFiltro);
        return "listarDependencia";
    }
    
     public Dependencia getDependenciaFiltro() {
        return dependenciaFiltro;
    }

    public void setDependenciaFiltro(Dependencia dependenciaFiltro) {
        this.dependenciaFiltro = dependenciaFiltro;
    }

    public LazyDataModel<Dependencia> getLista() {
        return lista;
    }

    public void cargarAeropuertoLista(){
    
        cargarAeropuerto(dependenciaFiltro);
        
    }
    
    public void cargarAeropuerto(){
    
        cargarAeropuerto(dependencia);
    }

    private void cargarAeropuerto( Dependencia dep){
    
        listAeropuerto= JsfUtil.getListadosBBean().getListaAeroXRegional(dep.getAeropuerto().getRegional().getRegId());
    }
    
    public List<Aeropuerto> getListAeropuerto() {
        return listAeropuerto;
    }
    
}
