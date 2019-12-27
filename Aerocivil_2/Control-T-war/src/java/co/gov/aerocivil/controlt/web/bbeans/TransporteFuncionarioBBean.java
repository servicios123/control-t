/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Transporte;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.enums.ParametrosEnum;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.TransporteFuncionarioService;
import co.gov.aerocivil.controlt.services.FuncionarioService;
import co.gov.aerocivil.controlt.services.ListasService;
import co.gov.aerocivil.controlt.to.FuncionarioTransporteTO;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import co.gov.aerocivil.controlt.web.lazylist.TransporteFuncionarioLazyList;
import co.gov.aerocivil.controlt.web.util.DateUtil;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import co.gov.aerocivil.controlt.web.util.ListadosBBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Administrador
 */

@ManagedBean
@SessionScoped
public class TransporteFuncionarioBBean {
    
    @EJB 
    TransporteFuncionarioService transpFuncService;
    
    @EJB
    FuncionarioService funService;
    
    @EJB
    ListasService listasService;
    
    private List<Transporte> funcionariosTranspLista;
    private Dependencia dependenciaFiltro;
    private Transporte transporteFiltro;
    private LazyDataModel<Transporte> lazyList;
    private List<Jornada> listJornada;
    private Date fechaTransporte;
    private Jornada jornadaTransporte;
    private List<Transporte> lista;

    public Jornada getJornadaTransporte() {
        return jornadaTransporte;
    }

    public void setJornadaTransporte(Jornada jornadaTransporte) {
        this.jornadaTransporte = jornadaTransporte;
    }

    public List<Jornada> getListJornada() {
        return listJornada;
    }

    public void setListJornada(List<Jornada> listJornada) {
        this.listJornada = listJornada;
    }

    public void setLazyList(LazyDataModel<Transporte> lazyList) {
        this.lazyList = lazyList;
    }

    public LazyDataModel<Transporte> getLazyList() {
        return lazyList;
    }

    public List<Transporte> getFuncionariosTranspLista() {
        return funcionariosTranspLista;
    }

    public void setFuncionariosTranspLista(List<Transporte> funcionariosTranspLista) {
        this.funcionariosTranspLista = funcionariosTranspLista;
    }
   

    public Date getFechaTransporte() {
        return fechaTransporte;
    }

    public void setFechaTransporte(Date fechaTransporte) {
        this.fechaTransporte = fechaTransporte;
    }

    public String crear(){
        
        jornadaTransporte= new Jornada();
          listJornada=null;
        listJornada=JsfUtil.getListadosBBean().getListaJornadaXDependencia(JsfUtil.getFuncionarioSesion().getDependencia().getDepId());
      
        funcionariosTranspLista = null;
        return "configTransporte";
        
    }
    
    public String listarFuncionarios(){
        funcionariosTranspLista = new ArrayList<Transporte>();   
        
        jornadaTransporte= (Jornada) JsfUtil.getListadosBBean().obtenerObjById(Jornada.class, jornadaTransporte.getJoId());
        List<Funcionario> funcionarios = funService.getFuncionarios600am(JsfUtil.getFuncionarioSesion().getDependencia(),fechaTransporte, jornadaTransporte.getJoHoraInicio().longValue(), jornadaTransporte.getJoMinutoInicio().longValue(), jornadaTransporte.getJoHoraFin().longValue(), jornadaTransporte.getJoMinutoFin().longValue());
        for(Funcionario f:funcionarios){
            Transporte t = new Transporte();
           
            try{
               
            t=transpFuncService.obtenerObjeto(f, fechaTransporte, jornadaTransporte);
            t.getFuncionario().setSeleccionado(true);
            
            } catch(Exception e){
              t.setFuncionario(f);
            t.setTraFecha(DateUtil.setCeroHoras(fechaTransporte));
            }
            
           /* if(f.getSectorTransporte()!=null){
                t.setTraSector(f.getSectorTransporte());
            }*/
            //Funcionario func = new Funcionario(f.getFunId(),f.getFunAlias(),f.getFunNombre(), f.getFunDireccion());
            
          
            funcionariosTranspLista.add(t);
        }
        if(funcionariosTranspLista.size()<=0){
            JsfUtil.addWarningMessage("fechaNoProgramada");
        }
        
         
        return "configTransporte";
    }
    
    public String guardar(){        
        boolean chequeados = false;
        int i=0;
        for (Transporte t : funcionariosTranspLista){
            t.setJornada(jornadaTransporte);
            if (t.getFuncionario().getSeleccionado()){
                chequeados = true;
                try{
                    transpFuncService.guardar(t, 
                        JsfUtil.getFuncionarioSesion());
                }catch(Exception e){
                    
                }
            }else{
            
                   if(t.getTraId() != null){
                  transpFuncService.borrar(t);
                   }
            }
            i++;
        }
        //if (seleccionados.size()<=0){
        if(!chequeados){
            JsfUtil.addWarningMessage("errorSeleccionFuncionarios");
            return null;
        }
        /*curso.setListaFuncionarios(seleccionados);
        curso = transpFuncService.guardar(curso);*/
        JsfUtil.addSuccessMessage("genericSingleSaveSuccess"); 
        return crear();
    }
    
    public String listar(){
        FuncionarioBBean funcBBean = (FuncionarioBBean)JsfUtil.getManagedBean(FuncionarioBBean.class);
        funcBBean.inicializarPickList();
        
        dependenciaFiltro = funcBBean.getFuncionarioFiltro().getDependencia();
        transporteFiltro = new Transporte();
        Funcionario fun = new Funcionario();
        fun.setDependencia(dependenciaFiltro);
        Jornada jornada = new Jornada();
        
        transporteFiltro.setFuncionario(fun);
        transporteFiltro.setJornada(jornada);
        
        
        listJornada=null;
        listJornada=JsfUtil.getListadosBBean().getListaJornadaXDependencia(JsfUtil.getFuncionarioSesion().getDependencia().getDepId());
      
        return "listTransporte";
    }

    public Transporte getTransporteFiltro() {
        return transporteFiltro;
    }

    public void setTransporteFiltro(Transporte transporteFiltro) {
        this.transporteFiltro = transporteFiltro;
    }
    
    public String filtrar(){        
        lazyList = new TransporteFuncionarioLazyList(transpFuncService, transporteFiltro);
        return "listTransporte";
    }

    public Dependencia getDependenciaFiltro() {
        return dependenciaFiltro;
    }

    public void setDependenciaFiltro(Dependencia dependenciaFiltro) {
        this.dependenciaFiltro = dependenciaFiltro;
    }

    public String exportPDF() {
        HashMap<String, String> params = listasService.getParametrosSistema();
        HashMap<String, Object> map = new HashMap<String, Object>();
        lista = transpFuncService.getLista(transporteFiltro, null,null,
                "funcionario.funNombre", SortOrderEnum.ASC.getOrder());
        ListadosBBean listBBean = JsfUtil.getListadosBBean();
        Dependencia dependencia = null;
        String dep="";
        if (transporteFiltro.getFuncionario().getDependencia()!= null && transporteFiltro.getFuncionario().getDependencia().getDepId()!=null){
            dependencia = (Dependencia)listBBean.obtenerObjById(Dependencia.class, transporteFiltro.getFuncionario().getDependencia().getDepId());
            dep = dependencia.getDepNombre();
        }
        String jor = ((Jornada)listBBean.obtenerObjById(Jornada.class, transporteFiltro.getJornada().getJoId())).getJoAlias();
        map.put("dependencia", dep);
        map.put("jornadaStr",  jor);
        map.put("depAbrev", dependencia.getDepAbreviatura());

        map.put("fecha", transporteFiltro.getTraFecha());
        /*map.put("claveFormato", params.get(ParametrosEnum.rep_diario_sen_clave.name()));
        map.put("versFormato", params.get(ParametrosEnum.rep_diario_sen_vers.name()));
        map.put("fechaFormato", params.get(ParametrosEnum.rep_diario_sen_date.name()));
        map.put("piePagina", params.get(ParametrosEnum.rep_diario_sen_pie.name()));*/
                
        JsfUtil.generaReporte("Transporte", map, lista);
        return null;
    }
}
