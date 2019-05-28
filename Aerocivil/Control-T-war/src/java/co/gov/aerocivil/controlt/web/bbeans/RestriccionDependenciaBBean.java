/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.entities.RestriccionDependencia;
import co.gov.aerocivil.controlt.entities.RestriccionProgramacion;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.JornadaService;
import co.gov.aerocivil.controlt.services.RestriccionesService;
import co.gov.aerocivil.controlt.to.RestriccionTO;
import co.gov.aerocivil.controlt.web.lazylist.RestriccionesLazyList;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class RestriccionDependenciaBBean {
    
    @EJB 
    RestriccionesService service;
    
    @EJB
    JornadaService jornadaService;

    private List<RestriccionTO> listaRestricciones;
    private Dependencia dep;
    private Jornada jornada;
    
    //4 Paginatorade
    private String jornadaAlias;
    private RestriccionDependencia restriccionFiltro;
    private LazyDataModel<RestriccionDependencia> listaRestriccionesLazy;
    //private Dependencia dependenciaFiltro;
    private List<Dependencia> listDependencia;
    
  
    

    /*public Dependencia getDependenciaFiltro() {
        return dependenciaFiltro;
    }*/
    
    private List<Jornada> listJornada;

    
    private List<Aeropuerto> listAeropuerto;

    public List<Aeropuerto> getListAeropuerto() {
        return listAeropuerto;
    }

    public RestriccionDependencia getRestriccionFiltro() {
        return restriccionFiltro;
    }

    public void setRestriccionFiltro(RestriccionDependencia restriccionFiltro) {
        this.restriccionFiltro = restriccionFiltro;
    }
    
    public String configurar(){
        listJornada=null;
        dep = JsfUtil.getFuncionarioSesion().getDependencia();
        listaRestricciones = service.
                getListaRestriccionesProgramacion(dep);
        for(RestriccionTO r : listaRestricciones){
            //System.out.println(r);
        }
         cargarJornadaLista();  
        return "verRestriccionesDependencia";
    }

    public List<RestriccionTO> getListaRestricciones() {
        return listaRestricciones;
    }

    public void setListaRestricciones(List<RestriccionTO> listaRestricciones) {
        this.listaRestricciones = listaRestricciones;
    }    
    
    public String guardar(){
        
        this.listaRestricciones = service.guardarListaRestricciones(listaRestricciones, dep, 
                JsfUtil.getFuncionarioSesion());
        JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        return "";
    }
    
    
    public String listar() {
        
        restriccionFiltro = new RestriccionDependencia();
        Aeropuerto aeropuerto = new Aeropuerto();
        Regional regional= new Regional();
        DepCategoria depcategoria=new DepCategoria();        
        //f.setFuNivel(JsfUtil.getFuncionarioSesion().getFuNivel());
        Dependencia dependencia = new Dependencia();
        listAeropuerto = null;
        
        
        aeropuerto.setRegional(regional);
        
        dependencia.setAeropuerto(aeropuerto);
        dependencia.setDepcategoria(depcategoria);
        
        
        
        FuncionarioBBean funcBBean = (FuncionarioBBean)JsfUtil.getManagedBean(FuncionarioBBean.class);
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
         
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3, RolEnum.NIVEL_A4})) {  
            dependencia=funcBBean.getDependenciaXNivelUsuario();            
        }
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {  
            listAeropuerto = funcBBean.getListAeropuerto();
        }
        restriccionFiltro.setDependencia(dependencia);
        restriccionFiltro.setRestriccionProgramacion(new RestriccionProgramacion());
        
        long jor_id = service.obtenerValor((long) 4, dependencia.getDepId());
        if(jor_id==0)
        {
            jornadaAlias="";
        }
        else
        {
            Jornada jor = jornadaService.getPorId(jor_id);
            if(jor==null)
            {
                jornadaAlias="";
            }
            else
            {
                jornadaAlias=jor.getJoAlias();
            }
        }
        
        
    /*    //System.out.println(regional);
        //System.out.println(aeropuerto);
        //System.out.println(dependencia);*/
        /*jornada = new Jornada();
        jornada = service.obtenerJornada(dependencia.getDepId());*/
        /*jornada = (Jornada) jornadaService.getPorId(service.obtenerValor( _valor,dependencia.getDepId()));*/
        
        
        return filtrar();
    }

    
    public String filtrar() {
        listaRestriccionesLazy = new RestriccionesLazyList(service, restriccionFiltro);        
        return "listarRestricciones";
    }
    
    public LazyDataModel<RestriccionDependencia> getListaRestriccionesLazy() {
        return listaRestriccionesLazy;
    }

    public void setListaRestriccionesLazy(LazyDataModel<RestriccionDependencia> listaRestriccionesLazy) {
        this.listaRestriccionesLazy = listaRestriccionesLazy;
    }

    public void cargarAeropuertoLista(){
        listAeropuerto= JsfUtil.getListadosBBean().getListaAeroXRegional(restriccionFiltro.getDependencia().getAeropuerto().getRegional().getRegId());        
    }

    public void cargarDependenciaLista() {
     LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(restriccionFiltro.getDependencia().getAeropuerto().getAeId(),
                    JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria().getDcId());
        }
        else{
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(restriccionFiltro.getDependencia().getAeropuerto().getAeId());
        }
           
    }
    
    public void cargarJornadaLista() {
     LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        
            listJornada = JsfUtil.getListadosBBean().getListaJornadaXDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
       
           
    }

    public List<Dependencia> getListDependencia() {
        return listDependencia;
    }

    public void setListDependencia(List<Dependencia> listDependencia) {
        this.listDependencia = listDependencia;
    }
    public List<Jornada> getListJornada() {
        return listJornada;
    }

    public void setListJornada(List<Jornada> listJornada) {
        this.listJornada = listJornada;
    }

    /**
     * @return the jornada
     */
    public Jornada getJornada() {
      
        return jornada;
    }

    /**
     * @param jornada the jornada to set
     */
    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    /**
     * @return the jornadaAlias
     */
    public String getJornadaAlias() {
        return jornadaAlias;
    }

    /**
     * @param jornadaAlias the jornadaAlias to set
     */
    public void setJornadaAlias(String jornadaAlias) {
        this.jornadaAlias = jornadaAlias;
    }
    
   
}