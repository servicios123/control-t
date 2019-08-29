/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.TurnoEspecialService;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.sql.SQLIntegrityConstraintViolationException;
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
public class TurnoEspecialBBean {

    /**
     * Creates a new instance of TurnoEspecialBBean
     */
    @EJB
    private TurnoEspecialService turnoEspecialService;
    private boolean crear;
    private TurnoEspecial turnoEspecial;
    private TurnoEspecial turnoEspecialFiltro;
    private List<TurnoEspecial> lista;
    private List<Dependencia> listDependencia;
    private List<Aeropuerto> listAeropuerto;

    public TurnoEspecialBBean() {
    }

    public String crear() {
        crear = false;
        Dependencia dependencia = new Dependencia();

        Regional regional = new Regional();
        Aeropuerto aeropuerto = new Aeropuerto();
        turnoEspecial = new TurnoEspecial();
        listAeropuerto = null;
        listDependencia = null;
        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);
        turnoEspecial.setDependencia(dependencia);
        return "editarTurnoEspecial";
    }

    public String editar() {
        /* cargarAeropuerto(sector);
         cargarDependencia(sector);*/
        crear = false;
        return "editarTurnoEspecial";
    }

    public String guardar() {
        try {
            crear = false;
            LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
            turnoEspecial.setDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia());
            turnoEspecial.setTeSigla(turnoEspecial.getTeSigla().toUpperCase());
            turnoEspecial.setTePermiteExtras(turnoEspecial.getTeHfin() <= 17 ? true : false);
            turnoEspecialService.guardar(turnoEspecial,
                    JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
            //((ListadosBBean)JsfUtil.getListadosBBean()).setListaSectores(null);

            return listar();
        } catch (Exception ex) {
            JsfUtil.addWarningMessage("siglaTurnoEspecialDependenciaAsignada");
            return null;
        }
    }

    /**
     *
     * @return
     */
    public String listar() {
        turnoEspecialFiltro = new TurnoEspecial();

        Regional regional = new Regional();
        Dependencia dependencia = new Dependencia();
        Aeropuerto aeropuerto = new Aeropuerto();

        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);
        listAeropuerto = null;
        listDependencia = null;
        turnoEspecialFiltro.setDependencia(dependencia);
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A1})) {

            dependencia.setDepcategoria(new DepCategoria());
            turnoEspecialFiltro.setDependencia(dependencia);

        }
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2})) {

            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            turnoEspecialFiltro.setDependencia(dependencia);
            cargarDependenciaListado();

        }
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A3})) {


            regional.setRegId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
            aeropuerto.setRegional(regional);
            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            dependencia.setAeropuerto(aeropuerto);
            turnoEspecialFiltro.setDependencia(dependencia);
            cargarAeropuertoListado();

        }
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A4})) {

            dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
            turnoEspecialFiltro.setDependencia(dependencia);

        }




        //
        return filtrar();

    }

    public String activar() {
        try {

            turnoEspecial.setTeEstado("Activo");
            turnoEspecialService.guardar(turnoEspecial,
                    JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");

        } catch (SQLIntegrityConstraintViolationException ex) {
            JsfUtil.addWarningMessage("siglaTurnoEspecialDependenciaAsignada");
        } finally {
            return filtrar();
        }
    }

    public String desactivar() {
        try {

            turnoEspecial.setTeEstado("Inactivo");
            turnoEspecialService.guardar(turnoEspecial,
                    JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");

        } catch (SQLIntegrityConstraintViolationException ex) {
            JsfUtil.addWarningMessage("siglaTurnoEspecialDependenciaAsignada");
        } finally {
            return filtrar();
        }


    }

    public String filtrar() {
        /*cargarAeropuerto();
         cargarDependencia();*/
        lista = turnoEspecialService.getLista(turnoEspecialFiltro, 0, 0, null, null);
        return "listarTurnoEspecial";
    }

    public TurnoEspecialService getTurnoEspecialService() {
        return turnoEspecialService;
    }

    public void setTurnoEspecialService(TurnoEspecialService turnoEspecialService) {
        this.turnoEspecialService = turnoEspecialService;
    }

    public TurnoEspecial getTurnoEspecial() {
        return turnoEspecial;
    }

    public void setTurnoEspecial(TurnoEspecial turnoEspecial) {
        this.turnoEspecial = turnoEspecial;
    }

    public TurnoEspecial getTurnoEspecialFiltro() {
        return turnoEspecialFiltro;
    }

    public void setTurnoEspecialFiltro(TurnoEspecial turnoEspecialFiltro) {
        this.turnoEspecialFiltro = turnoEspecialFiltro;
    }

    public List<TurnoEspecial> getLista() {
        return lista;
    }

    public void setLista(List<TurnoEspecial> lista) {
        this.lista = lista;
    }

    public List<Dependencia> getListDependencia() {
        return listDependencia;
    }

    public void setListDependencia(List<Dependencia> listDependencia) {
        this.listDependencia = listDependencia;
    }

    public void cargarAeropuerto() {
        cargarAeropuerto(turnoEspecial);
    }

    public void cargarAeropuertoListado() {

        cargarAeropuerto(turnoEspecialFiltro);
    }

    private void cargarAeropuerto(TurnoEspecial f) {
        listDependencia = null;
        listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(f.getDependencia().getAeropuerto().getRegional().getRegId());
    }

    public void cargarDependencia() {
        cargarDependencia(turnoEspecial);
    }

    public void cargarDependenciaListado() {
        cargarDependencia(turnoEspecialFiltro);
    }

    private void cargarDependencia(TurnoEspecial f) {
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

    public boolean isCrear() {
        return crear;
    }

    public void setCrear(boolean crear) {
        this.crear = crear;
    }
}
