/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.JornadaService;
import co.gov.aerocivil.controlt.web.lazylist.JornadaLazyList;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.math.BigDecimal;
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
public class JornadaBBean {

    /**
     * Creates a new instance of JornadaBBean
     */
    @EJB
    private JornadaService jornadaService;
    private Jornada jornada;
    private Jornada jornadaFiltro;
    private Jornada jornadaObligatoria;
    private List<Jornada> listaJornadaRestriccion;
    private LazyDataModel<Jornada> lista;
    private List<Aeropuerto> listAeropuerto;
    private List<Dependencia> listDependencia;
    private boolean cons;
    private boolean jornadaOpAnteriorChecked;
    private boolean editando;
    private List<Jornada> jorDisponibles;
    private Jornada jorOpAnterior;

    public Jornada getJorOpAnterior() {
        return jorOpAnterior;
    }

    public String getMsgJornadaAnteriorRequerida() {
        return msgJornadaAnteriorRequerida;
    }
    private String msgJornadaAnteriorRequerida;

    public List<Jornada> getJorDisponibles() {
        return jorDisponibles;
    }
    private Long[] jorRestricciones;

    public Long[] getJorRestricciones() {
        return jorRestricciones;
    }

    public void setJorRestricciones(Long[] jorRestricciones) {
        this.jorRestricciones = jorRestricciones;
    }

    public boolean isEditando() {
        return editando;
    }

    public boolean isJornadaOpAnteriorChecked() {
        return jornadaOpAnteriorChecked;
    }

    public void setJornadaOpAnteriorChecked(boolean jornadaOpAnteriorChecked) {
        this.jornadaOpAnteriorChecked = jornadaOpAnteriorChecked;
    }

    public String crear() {

        jorRestricciones = null;
        jornadaOpAnteriorChecked = false;
        editando = false;


        Dependencia dependencia = new Dependencia();

        Regional regional = new Regional();
        Aeropuerto aeropuerto = new Aeropuerto();
        jornada = new Jornada();
        listAeropuerto = null;
        listDependencia = null;
        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);
        jornada.setDependencia(dependencia);

        return "configurarJornada";
    }

    public String editar() {

        jorDisponibles = jornadaService.getListaJornadasDisponibles(this.jornada.getDependencia(), jornada.getJoId());
        jorRestricciones = new Long[jorDisponibles.size()];
        jornadaOpAnteriorChecked = false;
        if (jornada.getJornadaObligatoria() != null) {
            this.jornadaOpAnteriorChecked = true;
        }
        //System.out.println("jorDisponibles.size(): " + jorDisponibles.size());
        List<BigDecimal> lista = jornadaService.getListaJornadasRestriccion(this.jornada);
        int i = 0;
        for (BigDecimal bd : lista) {
            try {
                jorRestricciones[i++] = bd.longValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        editando = true;
        if (this.jornada != null && this.jornada.getDependencia() != null && this.jornada.getDependencia().getDepId() == 290) {
            jorOpAnterior = jornadaService.getJornadaAnteriorRionegro(jornada);
        } else {
            jorOpAnterior = jornadaService.getJornadaAnterior(this.jornada);
        }
        //normal
        if (jorOpAnterior != null) {
            msgJornadaAnteriorRequerida = JsfUtil.formatMessage("jornadaAnteriorRequerida",
                    jorOpAnterior.getJoAlias() + " (" + jorOpAnterior.getJoHoraInicio() + "-" + jorOpAnterior.getJoHoraFin() + ")",
                    this.jornada.getJoAlias());
        } else {
            msgJornadaAnteriorRequerida = "";
        }
        //rionegro 290

        /*cargarAeropuerto(jornada);
         cargarDependencia(jornada);*/

//        if (cons == false) {
        return "configurarJornada";
        /*} else {
         return "configurarJornada";
         }*/

        /*cargarAeropuerto(jornada);
         cargarDependencia(jornada);*/

        /*if (cons == false) {
         return "editarJornada";
         } else {
         return "configurarJornada";
         }*/
    }

    public String guardar() {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        jornada.setDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia());
        jornada.setJoAlias(jornada.getJoAlias().toUpperCase());
        try {
            this.listaJornadaRestriccion = new ArrayList<Jornada>();
            if (jorRestricciones != null) {
                for (Long i : jorRestricciones) {
                    Jornada j = new Jornada(i);
                    j = (Jornada) JsfUtil.getListadosBBean().obtenerObjById(Jornada.class, j.getJoId());
                    this.listaJornadaRestriccion.add(j);
                }
            }
            if (jornadaOpAnteriorChecked) {
                jornada.setJornadaObligatoria(this.jorOpAnterior);
            } else {
                jornada.setJornadaObligatoria(null);
            }
            jornada.setJornadasRestriccion(this.listaJornadaRestriccion);
            jornada = jornadaService.guardar(jornada,
                    JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
            return editar();
        } catch (Exception ex) {
            JsfUtil.addWarningMessage("jornadaDependenciaAsignada");
            return null;
        }
    }

    /**
     *
     * @return
     */
    public String listar() {

        jornadaFiltro = new Jornada();

        Regional regional = new Regional();
        Dependencia dependencia = new Dependencia();
        Aeropuerto aeropuerto = new Aeropuerto();

        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);
        listAeropuerto = null;
        listDependencia = null;
        jornadaFiltro.setJoEstado("Activo");

        // FuncionarioBBean funcBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2})) {

            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            jornadaFiltro.setDependencia(dependencia);
            cargarDependenciaListado();

        }
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A3})) {


            regional.setRegId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
            aeropuerto.setRegional(regional);
            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            dependencia.setAeropuerto(aeropuerto);
            jornadaFiltro.setDependencia(dependencia);
            cargarAeropuertoListado();

        }
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A4})) {

            dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
            jornadaFiltro.setDependencia(dependencia);

        }

        cons = false;
        jornadaFiltro.setDependencia(dependencia);


        //
        return filtrar();
    }

    public String listarConf() {
        jornadaFiltro = new Jornada();

        Regional regional = new Regional();
        Dependencia dependencia = new Dependencia();
        Aeropuerto aeropuerto = new Aeropuerto();

        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);
        listAeropuerto = null;
        listDependencia = null;

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);


        dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
        jornadaFiltro.setDependencia(dependencia);

        cons = true;
        jornadaFiltro.setDependencia(dependencia);
        jornadaFiltro.setJoEstado("Activo");


        //
        return filtrar();
    }

    public String filtrar() {
        lista = new JornadaLazyList(jornadaService, jornadaFiltro);
        return "listarJornada";
    }

    public void cargarAeropuerto() {
        cargarAeropuerto(jornada);
    }

    public void cargarAeropuertoListado() {

        cargarAeropuerto(jornadaFiltro);
    }

    private void cargarAeropuerto(Jornada f) {
        listDependencia = null;
        listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(f.getDependencia().getAeropuerto().getRegional().getRegId());
    }

    public void cargarDependencia() {
        cargarDependencia(jornada);
    }

    public void cargarDependenciaListado() {
        cargarDependencia(jornadaFiltro);
    }

    private void cargarDependencia(Jornada f) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getDependencia().getAeropuerto().getAeId(),
                    JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria().getDcId());
        } else {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getDependencia().getAeropuerto().getAeId());
        }

    }

    public JornadaService getJornadaService() {
        return jornadaService;
    }

    public void setJornadaService(JornadaService jornadaService) {
        this.jornadaService = jornadaService;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public Jornada getJornadaFiltro() {
        return jornadaFiltro;
    }

    public void setJornadaFiltro(Jornada jornadaFiltro) {
        this.jornadaFiltro = jornadaFiltro;
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

    public LazyDataModel<Jornada> getLista() {
        return lista;
    }

    public void setLista(LazyDataModel<Jornada> lista) {
        this.lista = lista;
    }

    public boolean isCons() {
        return cons;
    }

    public void setCons(boolean cons) {
        this.cons = cons;
    }

    public Jornada getJornadaObligatoria() {
        return jornadaObligatoria;
    }

    public void setJornadaObligatoria(Jornada jornadaObligatoria) {
        this.jornadaObligatoria = jornadaObligatoria;
    }

    public List<Jornada> getListaJornadaRestriccion() {
        return listaJornadaRestriccion;
    }

    public void setListaJornadaRestriccion(List<Jornada> listaJornadaRestriccion) {
        this.listaJornadaRestriccion = listaJornadaRestriccion;
    }
}