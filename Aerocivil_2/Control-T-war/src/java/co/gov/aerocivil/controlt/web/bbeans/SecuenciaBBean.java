/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.DetSecuencia;

import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.entities.Secuencia;
import co.gov.aerocivil.controlt.enums.RolEnum;

import co.gov.aerocivil.controlt.services.SecuenciaService;
import co.gov.aerocivil.controlt.web.lazylist.SecuenciaLazyList;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.ArrayList;
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
public class SecuenciaBBean {

    @EJB
    private SecuenciaService secuenciaService;
    private Secuencia secuencia;
    private Secuencia secuenciaFiltro;
    private Jornada jornada;
    private DetSecuencia detSecuencia;
    private List<Aeropuerto> listAeropuerto;
    private List<Jornada> listJornada;
    private List<Dependencia> listDependencia;
    private List<Jornada> jornadas = new ArrayList<Jornada>();
    private List<DetSecuencia> detSecuencias = new ArrayList<DetSecuencia>();
    private LazyDataModel<Secuencia> lista;

    public String listar() {
        secuenciaFiltro = new Secuencia();

        Regional regional = new Regional();
        Dependencia dependencia = new Dependencia();
        Aeropuerto aeropuerto = new Aeropuerto();

        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);


        listAeropuerto = null;
        listDependencia = null;

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A2)) {

            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());

            secuenciaFiltro.setDependencia(dependencia);
            cargarDependenciaListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A3)) {


            regional.setRegId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
            aeropuerto.setRegional(regional);
            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            dependencia.setAeropuerto(aeropuerto);
            secuenciaFiltro.setDependencia(dependencia);
            cargarAeropuertoListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A4)) {

            dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
            secuenciaFiltro.setDependencia(dependencia);


        }
        secuenciaFiltro.setDependencia(dependencia);

        return filtrar();

    }

    public String editar() {

        detSecuencias= new ArrayList<DetSecuencia>();
        detSecuencias = secuencia.getDetSecuencias();
        jornada = new Jornada();
        detSecuencia = new DetSecuencia();

        detSecuencia.setJornada(jornada);
        cargarJornadas(secuencia);
        return "editSecuencia";
    }

    public String ver() {


        detSecuencias = secuencia.getDetSecuencias();
        jornada = new Jornada();
        detSecuencia = new DetSecuencia();

        detSecuencia.setJornada(jornada);
        cargarJornadas(secuencia);
        return "verSecuencia";
    }

    public String guardar() {

        secuenciaService.borrarDetalle(secuencia);
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        Dependencia dependencia = new Dependencia();
        dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
        secuencia.setDependencia(dependencia);
        secuencia.setDetSecuencias(detSecuencias);

        secuencia= secuenciaService.guardar(secuencia, 
                JsfUtil.getFuncionarioSesion());

       


        return listar();

    }

    public String filtrar() {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        secuenciaFiltro.setDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia());
        lista = new SecuenciaLazyList(secuenciaService, secuenciaFiltro);
        return "listarSecuencia";
    }

    public String crear() {


        detSecuencias.clear();
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        Dependencia dependencia = new Dependencia();
        dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
        jornada = new Jornada();
        detSecuencia = new DetSecuencia();

        detSecuencia.setJornada(jornada);
        secuencia = new Secuencia();
        secuencia.setDependencia(dependencia);

        cargarJornadas(secuencia);

        return "editSecuencia";
    }

    public String reinit() {


        Jornada jornadaAct = (Jornada) JsfUtil.getListadosBBean().obtenerObjById(Jornada.class, detSecuencia.getJornada().getJoId());
        detSecuencia.setJornada(jornadaAct);
        detSecuencias.add(detSecuencia);

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        Dependencia dependencia = new Dependencia();
        dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
        detSecuencia = new DetSecuencia();
        jornada = new Jornada();
        detSecuencia = new DetSecuencia();
        detSecuencia.setJornada(jornada);

        secuencia.setDependencia(dependencia);


        cargarJornadas(secuencia);
        return null;
    }

    public void cargarJornadas() {
        cargarJornadas(secuencia);
    }

    private void cargarJornadas(Secuencia f) {
        listJornada = null;
        listJornada = JsfUtil.getListadosBBean().getListaJornadaXDependencia(f.getDependencia().getDepId());
    }

    public void cargarAeropuerto() {
        cargarAeropuerto(secuencia);
    }

    public void cargarAeropuertoListado() {

        cargarAeropuerto(secuenciaFiltro);
    }

    private void cargarAeropuerto(Secuencia f) {
        listDependencia = null;
        listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(f.getDependencia().getAeropuerto().getRegional().getRegId());
    }

    public void cargarDependencia() {
        cargarDependencia(secuencia);
    }

    public void cargarDependenciaListado() {
        cargarDependencia(secuenciaFiltro);
    }

    private void cargarDependencia(Secuencia f) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getDependencia().getAeropuerto().getAeId(),
                    JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria().getDcId());
        } else {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getDependencia().getAeropuerto().getAeId());
        }

    }

    public SecuenciaService getSecuenciaService() {
        return secuenciaService;
    }

    public void setSecuenciaService(SecuenciaService secuenciaService) {
        this.secuenciaService = secuenciaService;
    }

    public Secuencia getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Secuencia secuencia) {
        this.secuencia = secuencia;
    }

    public Secuencia getSecuenciaFiltro() {
        return secuenciaFiltro;
    }

    public void setSecuenciaFiltro(Secuencia secuenciaFiltro) {
        this.secuenciaFiltro = secuenciaFiltro;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public List<Aeropuerto> getListAeropuerto() {
        return listAeropuerto;
    }

    public void setListAeropuerto(List<Aeropuerto> listAeropuerto) {
        this.listAeropuerto = listAeropuerto;
    }

    public List<Jornada> getListJornada() {
        return listJornada;
    }

    public void setListJornada(List<Jornada> listJornada) {
        this.listJornada = listJornada;
    }

    public List<Dependencia> getListDependencia() {
        return listDependencia;
    }

    public void setListDependencia(List<Dependencia> listDependencia) {
        this.listDependencia = listDependencia;
    }

    public LazyDataModel<Secuencia> getLista() {
        return lista;
    }

    public void setLista(LazyDataModel<Secuencia> lista) {
        this.lista = lista;
    }

    public List<Jornada> getJornadas() {
        return jornadas;
    }

    public void setJornadas(List<Jornada> jornadas) {
        this.jornadas = jornadas;
    }

    public DetSecuencia getDetSecuencia() {
        return detSecuencia;
    }

    public void setDetSecuencia(DetSecuencia detSecuencia) {
        this.detSecuencia = detSecuencia;
    }

    public List<DetSecuencia> getDetSecuencias() {
        return detSecuencias;
    }

    public void setDetSecuencias(List<DetSecuencia> detSecuencias) {
        this.detSecuencias = detSecuencias;
    }
}
