/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.entities.PosicionJornada;
import co.gov.aerocivil.controlt.entities.PosicionNacional;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.enums.RolEnum;


import co.gov.aerocivil.controlt.services.PosicionJornadaService;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
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
public class PosicionJornadaBBean {

    /**
     * Creates a new instance of TurnoEspecialBBean
     */
    @EJB
    private PosicionJornadaService posicionJornadaService;
    private PosicionJornada posicionJornada;
    private PosicionJornada posicionJornadaFiltro;
    private List<PosicionJornada> lista;
    private List<Dependencia> listDependencia;
    private List<Aeropuerto> listAeropuerto;
    private List<Posicion> listPosicion;
    private List<Jornada> listJornada;

    public PosicionJornadaBBean() {
    }

    public String crear() {
        Dependencia dependencia = new Dependencia();
        listPosicion = null;
        listJornada = null;
        Posicion posicion = new Posicion();
        Jornada jornada = new Jornada();
        Regional regional = new Regional();
        Aeropuerto aeropuerto = new Aeropuerto();
        posicionJornada = new PosicionJornada();

        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        posicionJornada.setDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia());
        posicionJornada.setJornada(jornada);
        posicionJornada.setPosicion(posicion);
        cargarPosicion();
        cargarJornada();
        return "editarPosicionJornada";
    }

    public String editar() {
        cargarPosicion();
        cargarJornada();
        return "editarPosicionJornada";
    }

    public String guardar() {

        try {
            LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
            posicionJornada.setDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia());
            posicionJornada.setPosicion((Posicion) JsfUtil.getListadosBBean().
                    obtenerObjById(Posicion.class, posicionJornada.getPosicion().getPosId()));
            posicionJornada.setJornada((Jornada) JsfUtil.getListadosBBean().
                    obtenerObjById(Jornada.class, posicionJornada.getJornada().getJoId()));
            posicionJornada.setPjAlias(posicionJornada.getJornada().getJoAlias()
                    + posicionJornada.getPosicion().getPosicionNacional().getPnAlias());
            posicionJornadaService.guardar(posicionJornada,
                    JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
            //((ListadosBBean)JsfUtil.getListadosBBean()).setListaSectores(null);

            return listar();
        } catch (Exception ex) {
            JsfUtil.addWarningMessage("posicionJornadaDependenciaAsignada");
            ex.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @return
     */
    public String listar() {
        posicionJornadaFiltro = new PosicionJornada();
        posicionJornadaFiltro.setPjEstado("Activo");
        Regional regional = new Regional();
        Dependencia dependencia = new Dependencia();
        Aeropuerto aeropuerto = new Aeropuerto();

        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);
        listAeropuerto = null;
        listDependencia = null;

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A1)) {
            dependencia.setDepcategoria(new DepCategoria());
            posicionJornadaFiltro.setDependencia(dependencia);

        }

        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A2)) {

            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());

            posicionJornadaFiltro.setDependencia(dependencia);
            cargarDependenciaListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A3)) {


            regional.setRegId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
            aeropuerto.setRegional(regional);
            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            dependencia.setAeropuerto(aeropuerto);
            posicionJornadaFiltro.setDependencia(dependencia);
            cargarAeropuertoListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A4)) {

            dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
            posicionJornadaFiltro.setDependencia(dependencia);

        }
        posicionJornadaFiltro.setDependencia(dependencia);
        Posicion posicion = new Posicion();
        posicion.setPosicionNacional(new PosicionNacional());
        posicionJornadaFiltro.setPosicion(posicion);
        return filtrar();

    }

    public String filtrar() {

        lista = posicionJornadaService.getLista(posicionJornadaFiltro);
        return "listarPosicionJornada";
    }

    public PosicionJornadaService getPosicionJornadaService() {
        return posicionJornadaService;
    }

    public void setPosicionJornadaService(PosicionJornadaService posicionJornadaService) {
        this.posicionJornadaService = posicionJornadaService;
    }

    public PosicionJornada getPosicionJornada() {
        return posicionJornada;
    }

    public void setTurnoEspecial(PosicionJornada posicionJornada) {
        this.posicionJornada = posicionJornada;
    }

    public PosicionJornada getPosicionJornadaFiltro() {
        return posicionJornadaFiltro;
    }

    public void setPosicionJornadaFiltro(PosicionJornada posicionJornadaFiltro) {
        this.posicionJornadaFiltro = posicionJornadaFiltro;
    }

    public List<PosicionJornada> getLista() {
        return lista;
    }

    public void setLista(List<PosicionJornada> lista) {
        this.lista = lista;
    }

    public List<Dependencia> getListDependencia() {
        return listDependencia;
    }

    public void setListDependencia(List<Dependencia> listDependencia) {
        this.listDependencia = listDependencia;
    }

    public void cargarAeropuerto() {
        cargarAeropuerto(posicionJornada);
    }

    public void cargarAeropuertoListado() {

        cargarAeropuerto(posicionJornadaFiltro);
    }

    private void cargarAeropuerto(PosicionJornada f) {
        listDependencia = null;
        listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(f.getDependencia().getAeropuerto().getRegional().getRegId());
    }

    public void cargarPosicion() {
        cargarPosicion(posicionJornada);
    }

    public void cargarPosicionListado() {

        cargarPosicion(posicionJornadaFiltro);
    }

    private void cargarPosicion(PosicionJornada f) {

        listPosicion = JsfUtil.getListadosBBean().getListaPosicionXDependencia(f.getDependencia().getDepId());
    }

    public void cargarJornada() {
        cargarJornada(posicionJornada);
    }

    public void cargarJornadaListado() {

        cargarJornada(posicionJornadaFiltro);
    }

    private void cargarJornada(PosicionJornada f) {

        listJornada = JsfUtil.getListadosBBean().getListaJornadaXDependencia(f.getDependencia().getDepId());
    }

    public void cargarDependencia() {
        cargarDependencia(posicionJornada);
    }

    public void cargarDependenciaListado() {
        cargarDependencia(posicionJornadaFiltro);
    }

    private void cargarDependencia(PosicionJornada f) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getDependencia().getAeropuerto().getAeId(),
                    JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria().getDcId());
        } else {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getDependencia().getAeropuerto().getAeId());
        }

    }

    public List<Aeropuerto> getListAeropuerto() {
        return listAeropuerto;
    }

    public void setListAeropuerto(List<Aeropuerto> listAeropuerto) {
        this.listAeropuerto = listAeropuerto;
    }

    public List<Posicion> getListPosicion() {
        return listPosicion;
    }

    public void setListPosicion(List<Posicion> listPosicion) {
        this.listPosicion = listPosicion;
    }

    public List<Jornada> getListJornada() {
        return listJornada;
    }

    public void setListJornada(List<Jornada> listJornada) {
        this.listJornada = listJornada;
    }

    public void setPosicionJornada(PosicionJornada posicionJornada) {
        this.posicionJornada = posicionJornada;
    }
}
