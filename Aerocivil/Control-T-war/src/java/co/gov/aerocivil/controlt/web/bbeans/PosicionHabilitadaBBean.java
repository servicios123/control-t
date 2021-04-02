/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.JornadaNoLaborable;
import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.entities.PosicionHabilitada;
import co.gov.aerocivil.controlt.entities.PosicionNacional;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.FuncionarioService;
import co.gov.aerocivil.controlt.services.JornadaNoLaboralService;
import co.gov.aerocivil.controlt.services.JornadaService;
import co.gov.aerocivil.controlt.services.ListasServiceBean;
import co.gov.aerocivil.controlt.services.PosicionHabilitadaService;
import co.gov.aerocivil.controlt.services.PosicionService;
import co.gov.aerocivil.controlt.services.PosicionServiceBean;
import co.gov.aerocivil.controlt.web.lazylist.PosicionHabilitadaLazyList;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class PosicionHabilitadaBBean {

    /**
     * Creates a new instance of PosicionHabilitada
     */
    @EJB
    private PosicionHabilitadaService posicionHabilitadaService;
    @EJB
    private FuncionarioService funcionarioService;
    @EJB
    private PosicionService posicionServiceBean;
    @EJB
    private JornadaNoLaboralService jornadaNoLaboralService;
    @EJB
    private JornadaService jornadaService;
    private PosicionHabilitada posicionHabilitadaFiltro;
    private Posicion posicionFiltro;
    private Funcionario funcionario;
    private PosicionHabilitada posicionHabilitada;
    private List<Aeropuerto> listAeropuerto;
    private List<Dependencia> listDependencia;
    private List<Posicion> listPosicion;
    private Posicion[] listPosicionSelected;
    private Long[] listadoPosiciones;
    private ListasServiceBean listar;
    private LazyDataModel<PosicionHabilitada> lista;
    private Long[] jornadas;
    private List<Jornada> listJornada;

    public PosicionHabilitadaBBean() {
        posicionFiltro = new Posicion();
    }

    public String listar() {
        posicionHabilitadaFiltro = new PosicionHabilitada();

        Regional regional = new Regional();
        Dependencia dependencia = new Dependencia();
        Aeropuerto aeropuerto = new Aeropuerto();
        Posicion posicion = new Posicion();
        PosicionNacional posicionNac = new PosicionNacional();
        posicion.setPosicionNacional(posicionNac);
        Funcionario funcionario = new Funcionario();
        funcionario.setFunEstado("Activo");
        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);
        posicion.setDependencia(dependencia);

        listAeropuerto = null;
        listDependencia = null;

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A1)) {
            dependencia.setDepcategoria(new DepCategoria());
            posicion.setDependencia(dependencia);
            funcionario.setDependencia(dependencia);
            posicionHabilitadaFiltro.setPosicion(posicion);
            posicionHabilitadaFiltro.setFuncionario(funcionario);
        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A2)) {

            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            posicion.setDependencia(dependencia);
            funcionario.setDependencia(dependencia);
            posicionHabilitadaFiltro.setPosicion(posicion);
            posicionHabilitadaFiltro.setFuncionario(funcionario);
            cargarDependenciaListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A3)) {


            regional.setRegId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
            aeropuerto.setRegional(regional);
            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            dependencia.setAeropuerto(aeropuerto);
            posicion.setDependencia(dependencia);
            funcionario.setDependencia(dependencia);
            posicionHabilitadaFiltro.setPosicion(posicion);
            posicionHabilitadaFiltro.setFuncionario(funcionario);
            cargarAeropuertoListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A4)) {

            dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
            posicion.setDependencia(dependencia);
            funcionario.setDependencia(dependencia);

            posicionHabilitadaFiltro.setPosicion(posicion);
            posicionHabilitadaFiltro.setFuncionario(funcionario);

        }

        posicionHabilitadaFiltro.setPosicion(posicion);
        posicionHabilitadaFiltro.setFuncionario(funcionario);

        return filtrar();

    }

    public String listarVencimiento() {
        preparar();
        Calendar c = Calendar.getInstance();
        posicionHabilitadaFiltro.setFechafin(c.getTime());
        return filtrarVencimientos();

    }

    public void preparar() {
        posicionHabilitadaFiltro = new PosicionHabilitada();
        Regional regional = new Regional();
        Dependencia dependencia = new Dependencia();
        Aeropuerto aeropuerto = new Aeropuerto();
        Posicion posicion = new Posicion();
        Funcionario funcionario = new Funcionario();
        funcionario.setFunEstado("Activo");
        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);
        posicion.setDependencia(dependencia);

        listAeropuerto = null;
        listDependencia = null;

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A2)) {

            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            posicion.setDependencia(dependencia);
            funcionario.setDependencia(dependencia);
            posicionHabilitadaFiltro.setPosicion(posicion);
            posicionHabilitadaFiltro.setFuncionario(funcionario);
            cargarDependenciaListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A3)) {


            regional.setRegId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
            aeropuerto.setRegional(regional);
            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            dependencia.setAeropuerto(aeropuerto);
            posicion.setDependencia(dependencia);
            funcionario.setDependencia(dependencia);
            posicionHabilitadaFiltro.setPosicion(posicion);
            posicionHabilitadaFiltro.setFuncionario(funcionario);
            cargarAeropuertoListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A4)) {

            dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
            posicion.setDependencia(dependencia);
            funcionario.setDependencia(dependencia);

            posicionHabilitadaFiltro.setPosicion(posicion);
            posicionHabilitadaFiltro.setFuncionario(funcionario);

        }

        posicionHabilitadaFiltro.setPosicion(posicion);
        posicionHabilitadaFiltro.setFuncionario(funcionario);
    }

    public String filtrar() {

        lista = new PosicionHabilitadaLazyList(posicionHabilitadaService, posicionHabilitadaFiltro);
        return "listarPosicionHabilitada";
    }

    public String filtrarVencimientos() {
        lista = new PosicionHabilitadaLazyList(posicionHabilitadaService, posicionHabilitadaFiltro);
        return "listarVencimientoPosicionHabilitada";
    }

    public String guardarAsignacion() {
        try {


            funcionarioService.guardar(funcionario, JsfUtil.getFuncionarioSesion());

            posicionHabilitadaService.eliminar(listadoPosiciones, funcionario, JsfUtil.getFuncionarioSesion());

            Calendar actual = Calendar.getInstance();
            actual.add(Calendar.DATE, 90);
            //System.out.println("Todos Vence\t" + actual.get(Calendar.YEAR) + "/" + actual.get(Calendar.MONTH) + "/" + actual.get(Calendar.DATE));

            for (Long p : listadoPosiciones) {

                posicionHabilitada = new PosicionHabilitada();
                posicionHabilitada.setFuncionario(funcionario);
                posicionHabilitada.setPhFvencimiento(actual.getTime());
                posicionHabilitada.setPosicion(new Posicion(p));

                try {
                    posicionHabilitadaService.guardar(posicionHabilitada,
                            JsfUtil.getFuncionarioSesion());
                    posicionHabilitada = null;
                } catch (Exception ex) {
                    //Logger.getLogger(PosicionHabilitadaBBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");


        } catch (SQLIntegrityConstraintViolationException ex) {
            Logger.getLogger(PosicionHabilitadaBBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "listarFuncionario";
    }

    public Long[] getListadoPosiciones() {
        return listadoPosiciones;
    }

    public void setListadoPosiciones(Long[] listadoPosiciones) {
        this.listadoPosiciones = listadoPosiciones;
    }

    public String asignarPosicion() {
        funcionario = funcionarioService.getFuncionarioById(funcionario);
        ((FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class)).setFuncionario(funcionario);
        cargarPosiciones();
        int i = 0;
        listadoPosiciones = new Long[listPosicion.size()];
        listPosicionSelected = new Posicion[listPosicion.size()];
        for (PosicionHabilitada ph : funcionario.getListaPosicionesHabilitadas()) {
            if (i < listadoPosiciones.length) {
                listPosicionSelected[i] = ph.getPosicion();
                listadoPosiciones[i] = ph.getPosicion().getPosId();
                i++;
            }
        }

        return "asignarPosicion";

    }

    public String renovarPosicionesFuncionario() {
        funcionario = funcionarioService.getFuncionarioById(funcionario);
        ((FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class)).setFuncionario(funcionario);
        posicionHabilitadaService.renovar(funcionario, JsfUtil.getFuncionarioSesion());
        JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        return "listarFuncionario";
    }

    public String renovarPosicionesFuncionarioVencimiento() {
        funcionario = funcionarioService.getFuncionarioById(funcionario);
        ((FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class)).setFuncionario(funcionario);
        posicionHabilitadaService.renovar(funcionario, JsfUtil.getFuncionarioSesion());
        JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        return "listarVencimientoPosicionHabilitada";
    }

    public String listarPosicionDependencia() {
        Regional regional = new Regional();
        Dependencia dependencia = new Dependencia();
        Aeropuerto aeropuerto = new Aeropuerto();
        Posicion posicion = new Posicion();
        PosicionNacional posicionNac = new PosicionNacional();
        posicion.setPosicionNacional(posicionNac);
        Funcionario funcionario = new Funcionario();
        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);
        posicion.setDependencia(dependencia);

        listAeropuerto = null;
        listDependencia = null;

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A1)) {
            dependencia.setDepcategoria(new DepCategoria());
            posicion.setDependencia(dependencia);
            funcionario.setDependencia(dependencia);
            posicionFiltro.setDependencia(dependencia);
            posicionFiltro.setPosicionNacional(posicionNac);
        }
        return filtrarDep();
    }

    public String filtrarDep() {
        listPosicion = posicionServiceBean.getListaPosicionesDependencia(posicionFiltro);
        return "listarPosicionHabilitadaDep";
    }

    public void cargarPosicionesVencimientoFiltro() {
        cargarPosicionesVencimiento(posicionHabilitadaFiltro);
    }

    private void cargarPosicionesVencimiento(PosicionHabilitada f) {
        listPosicion = null;
        listPosicion = JsfUtil.getListadosBBean().getListaPosicionXDependencia(f.getPosicion().getDependencia().getDepId());
    }

    public void cargarPosiciones() {
        cargarPosiciones(funcionario);
    }

    private void cargarPosiciones(Funcionario f) {
        listPosicion = null;
        listPosicion = JsfUtil.getListadosBBean().getListaPosicionXDependencia(f.getDependencia().getDepId());
    }

    public PosicionHabilitadaService getPosicionHabilitadaService() {
        return posicionHabilitadaService;
    }

    public void setPosicionHabilitadaService(PosicionHabilitadaService posicionHabilitadaService) {
        this.posicionHabilitadaService = posicionHabilitadaService;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public PosicionHabilitada getPosicionHabilitada() {
        return posicionHabilitada;
    }

    public void setPosicionHabilitada(PosicionHabilitada posicionHabilitada) {
        this.posicionHabilitada = posicionHabilitada;
    }

    public List<Posicion> getListPosicion() {
        return listPosicion;
    }

    public void setListPosicion(List<Posicion> listPosicion) {
        this.listPosicion = listPosicion;
    }

    public PosicionHabilitada getPosicionHabilitadaFiltro() {
        return posicionHabilitadaFiltro;
    }

    public void setPosicionHabilitadaFiltro(PosicionHabilitada posicionHabilitadaFiltro) {
        this.posicionHabilitadaFiltro = posicionHabilitadaFiltro;
    }

    public LazyDataModel<PosicionHabilitada> getLista() {
        return lista;
    }

    public void setLista(LazyDataModel<PosicionHabilitada> lista) {
        this.lista = lista;
    }

    public ListasServiceBean getListar() {
        return listar;
    }

    public void setListar(ListasServiceBean listar) {
        this.listar = listar;
    }

    public void cargarAeropuerto() {
        cargarAeropuerto(posicionHabilitada);
    }

    public void cargarAeropuertoListado() {
        if (posicionHabilitadaFiltro != null) {
            cargarAeropuerto(posicionHabilitadaFiltro);
        } else {
            listDependencia = null;
            listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(posicionFiltro.getDependencia().getAeropuerto().getRegional().getRegId());
        }
    }

    private void cargarAeropuerto(PosicionHabilitada f) {
        listDependencia = null;
        listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(f.getPosicion().getDependencia().getAeropuerto().getRegional().getRegId());
    }

    public void cargarDependencia() {
        cargarDependencia(posicionHabilitada);
    }

    public void cargarDependenciaListado() {
        if (posicionHabilitadaFiltro != null) {
            cargarDependencia(posicionHabilitadaFiltro);
        } else {
            cargarDependencia(posicionFiltro);
        }
    }

    private void cargarDependencia(PosicionHabilitada f) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getPosicion().getDependencia().getAeropuerto().getAeId(),
                    JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria().getDcId());
        } else {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getPosicion().getDependencia().getAeropuerto().getAeId());
        }

    }

    private void cargarDependencia(Posicion f) {
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

    public List<Dependencia> getListDependencia() {
        return listDependencia;
    }

    public void setListDependencia(List<Dependencia> listDependencia) {
        this.listDependencia = listDependencia;
    }

    public Posicion[] getListPosicionSelected() {
        return listPosicionSelected;
    }

    public void setListPosicionSelected(Posicion[] listPosicionSelected) {
        this.listPosicionSelected = listPosicionSelected;
    }

    public Posicion getPosicionFiltro() {
        return posicionFiltro;
    }

    public void setPosicionFiltro(Posicion posicionFiltro) {
        this.posicionFiltro = posicionFiltro;
    }

    public Long[] getJornadas() {
        return jornadas;
    }

    public void setJornadas(Long[] jornadas) {
        this.jornadas = jornadas;
    }

    public List<Jornada> getListJornada() {
        return listJornada;
    }

    public void setListJornada(List<Jornada> listJornada) {
        this.listJornada = listJornada;
    }
}
