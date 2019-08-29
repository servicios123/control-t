/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Emergencias;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.EmergenciasService;
import co.gov.aerocivil.controlt.services.FuncionarioService;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.Date;
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
public class EmergenciasBBean {

    @EJB
    private EmergenciasService emergenciasService;
    
     @EJB
    private FuncionarioService funcionarioService;

   
     
    private Emergencias emergencias;
    private Emergencias emergenciasFiltro;
    
    private List<Emergencias> lista;
    private List<Aeropuerto> listAeropuerto;
    private List<Dependencia> listDependencia;
    private List<Funcionario> listFuncionario;
    private Funcionario funcionario;
    private Date fecha_final;
    private Date current;

   

    public String crear() {
       emergencias = new Emergencias();
        Dependencia dependencia = new Dependencia();
        
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        
        listFuncionario=JsfUtil.getListadosBBean().getListFuncionariosXDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
        funcionario=new Funcionario();
        dependencia=logbbean.getFuncionarioTO().getFuncionario().getDependencia();
        funcionario.setDependencia(dependencia);
        emergencias.setFuncionario(funcionario);
        
       

        return "editarEmergencia";
    }

    public String editar() {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        listFuncionario=JsfUtil.getListadosBBean().getListFuncionariosXDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
        return "editarEmergencia";
    }

    public String guardar() {
        
        funcionario.setDependencia(JsfUtil.getFuncionarioSesion().getDependencia());
        funcionario=funcionarioService.getFuncionarioById(funcionario);
        emergencias.setFuncionario(funcionario);
        emergenciasService.guardar(emergencias);
        JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        return listar();

    }

    public String listar() {
        emergenciasFiltro = new Emergencias();

        Regional regional = new Regional();
        Dependencia dependencia = new Dependencia();
        Aeropuerto aeropuerto = new Aeropuerto();
        funcionario = new Funcionario();

        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);
        funcionario.setDependencia(dependencia);


        listAeropuerto = null;
        listDependencia = null;


        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A2)) {

            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());

            funcionario.setDependencia(dependencia);
            emergenciasFiltro.setFuncionario(funcionario);
            cargarDependenciaListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A3)) {

            regional.setRegId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
            aeropuerto.setRegional(regional);
            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            dependencia.setAeropuerto(aeropuerto);
            funcionario.setDependencia(dependencia);
            emergenciasFiltro.setFuncionario(funcionario);
            cargarAeropuertoListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A4) || logbbean.isFuncionarioNivel(RolEnum.NIVEL_U1) ) {

            dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
            funcionario.setDependencia(dependencia);
            emergenciasFiltro.setFuncionario(funcionario);

        }
        funcionario.setDependencia(dependencia);
        emergenciasFiltro.setFuncionario(funcionario);
        // programacionFiltro.setProEstado(1);

        return filtrar();
    }

    public String filtrar() {
        lista = emergenciasService.getLista(emergenciasFiltro, 0, 0, null, null);
        return "listarEmergencia";
    }

    public void cargarAeropuerto() {
        cargarAeropuerto(emergencias);
    }

    public void cargarAeropuertoListado() {

        cargarAeropuerto(emergenciasFiltro);
    }

    private void cargarAeropuerto(Emergencias f) {
        listDependencia = null;
        listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(f.getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
    }

    public void cargarDependencia() {
        cargarDependencia(emergencias);
    }

    public void cargarDependenciaListado() {
        cargarDependencia(emergenciasFiltro);
    }
    
     public FuncionarioService getFuncionarioService() {
        return funcionarioService;
    }

    public void setFuncionarioService(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    private void cargarDependencia(Emergencias f) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getFuncionario().getDependencia().getAeropuerto().getAeId(),
                    JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria().getDcId());
        } else {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getFuncionario().getDependencia().getAeropuerto().getAeId());
        }

    }

    public EmergenciasService getEmergenciasService() {
        return emergenciasService;
    }

    public void setEmergenciasService(EmergenciasService emergenciasService) {
        this.emergenciasService = emergenciasService;
    }

    public Emergencias getEmergencias() {
        return emergencias;
    }

    public void setEmergencias(Emergencias emergencias) {
        this.emergencias = emergencias;
    }

    public Emergencias getEmergenciasFiltro() {
        return emergenciasFiltro;
    }

    public void setEmergenciasFiltro(Emergencias emergenciasFiltro) {
        this.emergenciasFiltro = emergenciasFiltro;
    }

    public List<Emergencias> getLista() {
        return lista;
    }

    public void setLista(List<Emergencias> lista) {
        this.lista = lista;
    }

    public List<Aeropuerto> getListAeropuerto() {
        return listAeropuerto;
    }

    public void setListAeropuerto(List<Aeropuerto> listAeropuerto) {
        this.listAeropuerto = listAeropuerto;
    }

    public List<Dependencia> getListDependencia() {
        return listDependencia;
    }

    public void setListDependencia(List<Dependencia> listDependencia) {
        this.listDependencia = listDependencia;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public List<Funcionario> getListFuncionario() {
        return listFuncionario;
    }

    public void setListFuncionario(List<Funcionario> listFuncionario) {
        this.listFuncionario = listFuncionario;
    }
    
    public Date getFecha_final() {
        return fecha_final;
    }

    public void setFecha_final(Date fecha_final) {
        this.fecha_final = fecha_final;
    }
    /**
     * Creates a new instance of EmergenciasBBean
     */
    public EmergenciasBBean() {
    }

    /**
     * @return the current
     */
    public Date getCurrent() {
        current= new Date();
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(Date current) {
        this.current = current;
    }
}
