/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.entities.PosicionNacional;
import co.gov.aerocivil.controlt.services.PosicionNacionalService;
import co.gov.aerocivil.controlt.services.PosicionService;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.math.BigDecimal;
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
public class PosicionBBean {

    /**
     * Creates a new instance of PosicionBBean
     */
    @EJB
    private PosicionService posicionService;
    @EJB
    private PosicionNacionalService posicionNacionalService;
    private Posicion posicion;
    private Posicion posicionFiltro;
    private List<Posicion> lista;
    private List<Aeropuerto> listAeropuerto;
    private List<Dependencia> listDependencia;
    private List<PosicionNacional> listPosicionNacional;
    private Long[] posicionesSeleccionadas;

    public String crear() {

        posicion = new Posicion();

        Dependencia dependencia = new Dependencia();

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        dependencia = logbbean.getFuncionarioTO().getFuncionario().getDependencia();
        posicion.setDependencia(dependencia);
        cargarFuncion();
        cargarSector();

        return "editarPosicion";
    }

    public String editar() {

        cargarSector(posicion);
        cargarFuncion(posicion);
        return "editarPosicion";
    }

    public String guardar() {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        Dependencia dep = logbbean.getFuncionarioTO().getFuncionario().getDependencia();

        posicionService.inactivarPosiciones(posicionesSeleccionadas, dep);

        for (Long posNal : posicionesSeleccionadas) {
            posicion = posicionService.getPosicionByPosNal(posNal, dep);
            PosicionNacional posNalAux = posicionNacionalService.getPosNalById(posNal);
            if (posicion == null) {
                posicion = new Posicion();
                posicion.setDependencia(dep);
                posicion.setPosicionNacional(posNalAux);
            }
            if (posNalAux.getPnVencimiento().equals(posicion.getPosTiempoVence())
                    && "Activo".equals(posicion.getPosEstado())) {
                //si el tiempo de vencimiento no ha cambiado y
                //se encuetra activa la posicion, continúo iterando
                continue;
            }
            posicion.setPosTiempoVence(posNalAux.getPnVencimiento());
            posicion.setPosEstado("Activo");
            posicionService.guardar(posicion, JsfUtil.getFuncionarioSesion());
        }
        JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        return null;
    }

    /**
     *
     * @return
     */
    public String listar() {
        posicionFiltro = new Posicion();

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        Dependencia dependencia = logbbean.getFuncionarioTO().getFuncionario().getDependencia();
        posicionFiltro.setDependencia(dependencia);
        cargarFuncionListado();
        cargarSectorListado();

        return filtrar();
    }

    public String filtrar() {

        lista = posicionService.getLista(posicionFiltro, null, null, null, null);
        return "listarPosicion";
    }

    public void cargarAeropuerto() {

        cargarAeropuerto(posicion);
    }

    public void cargarAeropuertoListado() {
        cargarAeropuerto(posicionFiltro);
    }

    private void cargarAeropuerto(Posicion f) {
        listDependencia = null;

        listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(f.getDependencia().getAeropuerto().getRegional().getRegId());
    }

    public void cargarDependencia() {
        cargarDependencia(posicion);
    }

    public void cargarDependenciaListado() {
        cargarDependencia(posicionFiltro);
    }

    private void cargarDependencia(Posicion f) {
        listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getDependencia().getAeropuerto().getAeId());
    }

    public void cargarSector() {
        cargarSector(posicion);
    }

    public void cargarSectorListado() {
        cargarSector(posicionFiltro);
    }

    private void cargarSector(Posicion f) {

        cargarFuncion(f);
    }

    public void cargarFuncion() {
        cargarFuncion(posicion);
    }

    public void cargarFuncionListado() {
        cargarFuncion(posicionFiltro);
    }

    private void cargarFuncion(Posicion f) {
    }

    public void cargarAliasPos() {
    }

    public String configurarPosicionesDependencia() {
        posicion = new Posicion();

        PosicionNacional posNal = new PosicionNacional();
        posNal.setDepCategoria(JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria());
        posNal.setPnEstado("Activo");
        /*Dependencia dep = new Dependencia();
        
         posicion.setDependencia(dep);
         posicion.setPosEstado("Activo");*/
        listPosicionNacional = posicionNacionalService.getLista(posNal, null, null, null, null);
        posicionesSeleccionadas = new Long[listPosicionNacional.size()];
        int i = 0;
        List<BigDecimal> list = posicionService.getListaPosicionesDependencia(JsfUtil.getFuncionarioSesion().getDependencia().getDepId());
        for (BigDecimal id : list) {
            posicionesSeleccionadas[i++] = id.longValue();
        }
        return "configurarPosicionesDependencia";
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public Posicion getPosicionFiltro() {
        return posicionFiltro;
    }

    public void setPosicionFiltro(Posicion posicionFiltro) {
        this.posicionFiltro = posicionFiltro;
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

    public List<PosicionNacional> getListPosicionNacional() {
        return listPosicionNacional;
    }

    public void setListPosicionNacional(List<PosicionNacional> listPosicionNacional) {
        this.listPosicionNacional = listPosicionNacional;
    }

    public Long[] getPosicionesSeleccionadas() {
        return posicionesSeleccionadas;
    }

    public void setPosicionesSeleccionadas(Long[] posicionesSeleccionadas) {
        this.posicionesSeleccionadas = posicionesSeleccionadas;
    }

    public List<Posicion> getLista() {
        return lista;
    }

    public void setLista(List<Posicion> lista) {
        this.lista = lista;
    }
}
