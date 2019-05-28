/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Notificacion;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.NotificacionService;
import co.gov.aerocivil.controlt.web.lazylist.NotificacionLazyList;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.Date;
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
public class NotificacionBBean {

    /**
     * Creates a new instance of NotificacionBBean
     */
    @EJB
    private NotificacionService notificacionService;
    private Notificacion notificacion;
    private Notificacion notificacionFiltro;
    private LazyDataModel<Notificacion> lista;
    private List<Aeropuerto> listAeropuerto;
    private List<Dependencia> listDependencia;
    private Date fechaInicial;
    private Date fechaFinal;

    public String listar() {
        notificacionFiltro = new Notificacion();

        Regional regional = new Regional();
        Dependencia dependencia = new Dependencia();
        Aeropuerto aeropuerto = new Aeropuerto();
        Funcionario funcionario = new Funcionario();
        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);
        funcionario.setDependencia(dependencia);
        notificacionFiltro.setNotFuncionario(funcionario);

        listAeropuerto = null;
        listDependencia = null;

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A2)) {

            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());

            funcionario.setDependencia(dependencia);
            notificacionFiltro.setNotFuncionario(funcionario);
            cargarDependenciaListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A3)) {


            regional.setRegId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
            aeropuerto.setRegional(regional);
            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            dependencia.setAeropuerto(aeropuerto);
            funcionario.setDependencia(dependencia);
            notificacionFiltro.setNotFuncionario(funcionario);
            cargarAeropuertoListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A4)) {

            dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
            funcionario.setDependencia(dependencia);
            notificacionFiltro.setNotFuncionario(funcionario);


        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_U1) || logbbean.isFuncionarioNivel(RolEnum.NIVEL_U2)) {


            funcionario = logbbean.getFuncionarioTO().getFuncionario();
            notificacionFiltro.setNotFuncionario(funcionario);


        }


        return filtrar();

    }

    public String filtrar() {

        lista = new NotificacionLazyList(notificacionService, notificacionFiltro);
        return "listarNotificacion";
    }

    public void cargarAeropuerto() {
        cargarAeropuerto(notificacion);
    }

    public void cargarAeropuertoListado() {

        cargarAeropuerto(notificacionFiltro);
    }

    private void cargarAeropuerto(Notificacion f) {
        listDependencia = null;
        listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(f.getNotFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
    }

    public void cargarDependencia() {
        cargarDependencia(notificacion);
    }

    public void cargarDependenciaListado() {
        cargarDependencia(notificacionFiltro);
    }

    private void cargarDependencia(Notificacion f) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getNotFuncionario().getDependencia().getAeropuerto().getAeId(),
                    JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria().getDcId());
        } else {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getNotFuncionario().getDependencia().getAeropuerto().getAeId());
        }

    }

    public NotificacionService getNotificacionService() {
        return notificacionService;
    }

    public void setNotificacionService(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    public Notificacion getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Notificacion notificacion) {
        this.notificacion = notificacion;
    }

    public Notificacion getNotificacionFiltro() {
        return notificacionFiltro;
    }

    public void setNotificacionFiltro(Notificacion notificacionFiltro) {
        this.notificacionFiltro = notificacionFiltro;
    }

    public LazyDataModel<Notificacion> getLista() {
        return lista;
    }

    public void setLista(LazyDataModel<Notificacion> lista) {
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

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public NotificacionBBean() {
    }
}
