/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.CursoRecurrente;
import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.EvaluacionCompetencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.JornadaNoLaborable;
import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.entities.TurnoEspFuncionario;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.AeropuertoService;
import co.gov.aerocivil.controlt.services.DependenciaService;
import co.gov.aerocivil.controlt.services.FuncionarioService;
import co.gov.aerocivil.controlt.services.JornadaNoLaboralService;
import co.gov.aerocivil.controlt.services.JornadaService;
import co.gov.aerocivil.controlt.services.RegionalService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import co.gov.aerocivil.controlt.web.lazylist.FuncionarioLazyList;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
//import com.itextpdf.text.pdf.ArabicLigaturizer;
import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.DualListModel;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Viviana
 */
@ManagedBean
@SessionScoped
public class FuncionarioBBean {

    /**
     * Creates a new instance of FuncionarioBBean
     */
    @EJB
    private FuncionarioService funcionarioService;
    @EJB
    private RegionalService regionalService;
    @EJB
    private AeropuertoService aeropuertoService;
    @EJB
    private JornadaNoLaboralService jornadaNoLaboralService;
    @EJB
    private JornadaService jornadaService;
    private DependenciaService dependenciaService;
    private Funcionario funcionario;
    private Funcionario funcionarioSeleccionado;
    private Funcionario funcionarioFiltro;
    private LazyDataModel<Funcionario> lazyList;
    private List<Aeropuerto> listAeropuerto;
    private List<Dependencia> listDependencia;
    private List<Posicion> listPosicion;
    private List<Funcionario> funcionariosDisponibles;
    private List<Funcionario> funcionariosSeleccionados;
    private String heightList;
    private boolean[] columns;
    private ArrayList<Posicion> posicionesSeleccionados;
    private Date fechaini;
    private Date fechafin;
    private List<CursoRecurrente> cursosRecurrentes;
    private boolean editando;
    private Long[] jornadas;
    private List<Jornada> listJornada;
    private DataTable table;
    private Date fechaVenceEvaluacion;
    private Date fechaRealizaEvaluacion;
    private String evaluacion;
    private String resultado;

    public String crear() {
        funcionario = new Funcionario();
        funcionario.setFunEstado("Activo");
        editando = false;
        return "crearFuncionario";
    }

    public String actCertMedico() {
        return "actCertMedico";
    }

    public String buscarSITAH() {
        Long funId = funcionario.getFunId();
        if (funcionarioService.getFuncionarioById(funcionario) != null) {
            //ya existe en nuestra DB
            JsfUtil.addWarningMessage("usuarioYaExisteAeroDB", String.valueOf(funId));
            return null;
        }
        funcionario = funcionarioService.buscarSITAH(funId);
        if (funcionario == null) {
            JsfUtil.addWarningMessage("usuarioNoExistenteSITAH", String.valueOf(funId));
            funcionario = new Funcionario();
            return null;
        }
        funcionario.setFunEstado("Activo");
        Regional r = new Regional();
        Aeropuerto a = new Aeropuerto();
        Dependencia d = new Dependencia();
        a.setRegional(r);
        d.setAeropuerto(a);
        funcionario.setDependencia(d);
        funcionario.setFunClave("123456");
        return "editarFuncionario";
    }

    public FuncionarioBBean() {
        org.primefaces.component.selectonemenu.SelectOneMenu obj = new SelectOneMenu();
        for (String s : obj.getEventNames()) {
            //System.out.println(s);
        }
    }

    public String editar() {
        editando = true;
        cargarAeropuerto(funcionario);
        cargarDependencia(funcionario);
        cargarPosiciones(funcionario);
        //cargarCursosRecurrentes();

        return "editarFuncionario";
    }

    public String editarFuncCuenta() {
        editando = true;
        cargarAeropuerto(funcionario);
        cargarDependencia(funcionario);
        cargarPosiciones(funcionario);
        return "editFuncCuenta";
    }

    public String editCondEsp() {
        jornadas = null;
        listJornada = JsfUtil.getListadosBBean().getListaJornadaXDependencia(funcionario.getDependencia().getDepId());
        List<JornadaNoLaborable> noLaborables = jornadaNoLaboralService.getListaJornadasFuncionario(funcionario.getFunId());
        if (noLaborables != null && !noLaborables.isEmpty()) {
            jornadas = new Long[noLaborables.size()];
            int j = 0;
            for (JornadaNoLaborable jornadaNoLaborable : noLaborables) {
                jornadas[j] = jornadaNoLaborable.getJornada().getJoId();
                j++;
            }
        }

        return "editarCondEsp";
    }

    public String asignarPosicion() {
        /* cargarAeropuerto(funcionario);
         cargarDependencia(funcionario);*/
        cargarPosiciones();
        return "asignarPosicion";
    }

    public String guardar() {
        funcionario.setFunAlias(funcionario.getFunAlias().toUpperCase());
        Dependencia dep = (Dependencia) JsfUtil.getListadosBBean().obtenerObjById(Dependencia.class,
                funcionario.getDependencia().getDepId());
        funcionario.setDependencia(dep);
        if (!funcionarioService.validarAlias(funcionario, funcionario.getDependencia())) {
            //System.out.println("Alias ya asignado");
            JsfUtil.addWarningMessage("aliasFuncionarioDependenciaUnica");
            return null;
        }
        try {
            /*if(!editando){
             funcionario.setFunEstado("Activo");
             }*/
            if (funcionario.getFunUsuario() == null || "".equals(funcionario.getFunUsuario().trim())) {
                funcionario.setFunUsuario(funcionario.getFunId().toString());
            }
            funcionarioService.guardar(funcionario,
                    JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
            return listar();
        } catch (Exception ex) {
            //System.out.println("Ocurrió un error al guardar en DB");
            JsfUtil.addWarningMessage("aliasFuncionarioDependenciaUnica");
        }
        return null;

    }

    public String guardarCondicionesEsp() {
        try {
            if (!funcionario.getFunHorasExtras()) {
                funcionario.setFunHEExcedente(0);
                funcionario.setFunCantMaxHE(0);
            }
            funcionarioService.guardar(funcionario,
                    JsfUtil.getFuncionarioSesion());

            jornadaNoLaboralService.eliminar(funcionario.getFunId());
            for (Long j : jornadas) {
                JornadaNoLaborable jornadaNoLaborable = new JornadaNoLaborable();
                Jornada jornada = jornadaService.getPorId(j);
                Funcionario f = funcionarioService.getFuncionarioById(funcionario);

                jornadaNoLaborable.setFuncionario(f);
                jornadaNoLaborable.setJornada(jornada);
                jornadaNoLaboralService.guardar(jornadaNoLaborable, funcionario);
            }

            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
            return listarCondicionesEsp();
        } catch (Exception ex) {
            JsfUtil.addWarningMessage("aliasFuncionarioDependenciaUnica");
        }
        return null;
    }

    public String guardarCertMedico() {
        try {
            funcionarioService.guardar(funcionarioSeleccionado,
                    JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
            return listarCertMedico();
        } catch (SQLIntegrityConstraintViolationException ex) {
            Logger.getLogger(FuncionarioBBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String guardarAsignacion() {
        try {
            funcionarioService.guardar(funcionario,
                    JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
            return listar();
        } catch (Exception ex) {
            JsfUtil.addWarningMessage("aliasFuncionarioDependenciaUnica");
        }
        return null;
    }

    public String listar() {
        funcionarioFiltro = new Funcionario();
        funcionarioFiltro.setDependencia(getDependenciaXNivelUsuario());
        funcionarioFiltro.getDependencia().setDepcategoria(new DepCategoria());
        //columns = new boolean[3];
        columns = new boolean[]{true, false, false};
        if (JsfUtil.getFuncionarioSesion().getFuNivel().equals(RolEnum.NIVEL_A4.getRolId())) {
            columns[1] = true;
        }
        return filtrar();
    }

    public String listar_vencimiento_certificado() {
        funcionarioFiltro = new Funcionario();
        funcionarioFiltro.setDependencia(getDependenciaXNivelUsuario());
        funcionarioFiltro.getDependencia().setDepcategoria(new DepCategoria());
        funcionarioFiltro.setEvalFecha(1);
        Calendar c = Calendar.getInstance();
        funcionarioFiltro.setFechafin(c.getTime());
        funcionarioFiltro.setFunEstado("Activo");
        //columns = new boolean[3];

        columns = new boolean[]{true, false, false};
        return filtrar_vencimiento_certificado();
    }

    public String listarCertMedico() {
        funcionarioSeleccionado = new Funcionario();
        funcionarioFiltro = new Funcionario();
        funcionarioFiltro.setDependencia(getDependenciaXNivelUsuario());
        funcionarioFiltro.getDependencia().setDepcategoria(new DepCategoria());
        Calendar c = Calendar.getInstance();
        funcionarioFiltro.setFechafin(c.getTime());
        return filtrar_Cert_Medico();
    }

    public String filtrar_Cert_Medico() {

        lazyList = new FuncionarioLazyList(funcionarioService, funcionarioFiltro);
        return "listarCertMedico";
    }

    public String filtrar_vencimiento_certificado() {
        lazyList = new FuncionarioLazyList(funcionarioService, funcionarioFiltro);
        return "listarVencimientoCertificado";
    }

    public String listar_vencimiento_curso() {
        funcionarioFiltro = new Funcionario();
        funcionarioFiltro.setDependencia(getDependenciaXNivelUsuario());
        funcionarioFiltro.getDependencia().setDepcategoria(new DepCategoria());
        funcionarioFiltro.setEvalFecha(2);
        Calendar c = Calendar.getInstance();
        funcionarioFiltro.setFechafin(c.getTime());
        funcionarioFiltro.setFunEstado("Activo");
        //columns = new boolean[3];

        columns = new boolean[]{true, false, false};
        return filtrar_vencimiento_curso();
    }

    public String filtrar_vencimiento_curso() {
        lazyList = new FuncionarioLazyList(funcionarioService, funcionarioFiltro);
        return "listarVencimientoCurso";
    }

    public String listar_vencimiento_evaluacion() {
        funcionarioFiltro = new Funcionario();
        funcionarioFiltro.setDependencia(getDependenciaXNivelUsuario());
        funcionarioFiltro.getDependencia().setDepcategoria(new DepCategoria());
        funcionarioFiltro.setEvalFecha(3);
        Calendar c = Calendar.getInstance();
        funcionarioFiltro.setFechafin(c.getTime());
        funcionarioFiltro.setFunEstado("Activo");
        //columns = new boolean[3];

        columns = new boolean[]{true, false, false};
        return filtrar_vencimiento_evaluacion();
    }

    public String filtrar_vencimiento_evaluacion() {
        lazyList = new FuncionarioLazyList(funcionarioService, funcionarioFiltro);
        return "listarVencimientoEvaluacion";
    }

    public String listarAsignacionesEspeciales() {
        funcionarioFiltro = new Funcionario();
        funcionarioFiltro.setDependencia(getDependenciaXNivelUsuario());
        funcionarioFiltro.getDependencia().setDepcategoria(new DepCategoria());
        if (funcionario != null) {
            funcionarioFiltro.setFunId(funcionario.getFunId());
        }
        /*funcionarioFiltro.setEvalFecha(1);
         Calendar c = Calendar.getInstance();
         funcionarioFiltro.setFechafin(c.getTime());*/
        //columns = new boolean[3];



        columns = new boolean[]{true, false, false};

        TurnoEspFuncionario turnoEspFuncionario = new TurnoEspFuncionario();
        TurnoEspFuncionarioBBean turnoEspFuncionarioBBean = (TurnoEspFuncionarioBBean) JsfUtil.getManagedBean(TurnoEspFuncionarioBBean.class);

        turnoEspFuncionarioBBean.setTurnoEspFuncionario(turnoEspFuncionario);
        return filtrarAsignacionesEspeciales();
    }

    public String filtrarAsignacionesEspeciales() {
        lazyList = new FuncionarioLazyList(funcionarioService, funcionarioFiltro);
        return "listarAsignacionesEspeciales";
    }

    public String listarCondicionesEsp() {
        funcionarioFiltro = new Funcionario();
        funcionarioFiltro.setDependencia(getDependenciaXNivelUsuario());
        funcionarioFiltro.getDependencia().setDepcategoria(new DepCategoria());
        funcionarioFiltro.setEvalFecha(1);
        //columns = new boolean[3];



        columns = new boolean[]{true, false, false};

        TurnoEspFuncionario turnoEspFuncionario = new TurnoEspFuncionario();
        TurnoEspFuncionarioBBean turnoEspFuncionarioBBean = (TurnoEspFuncionarioBBean) JsfUtil.getManagedBean(TurnoEspFuncionarioBBean.class);

        turnoEspFuncionarioBBean.setTurnoEspFuncionario(turnoEspFuncionario);
        return filtrarCondicionesEsp();
    }

    public String filtrarCondicionesEsp() {
        lazyList = new FuncionarioLazyList(funcionarioService, funcionarioFiltro);
        return "listarCondicionesEspeciales";
    }

    public void inicializarPickList() {
        funcionarioFiltro = new Funcionario();
        funcionarioFiltro.setDependencia(
                getDependenciaXNivelUsuario());
        funcionarioFiltro.getDependencia().setDepcategoria(new DepCategoria());

        this.funcionariosDisponibles = new ArrayList<Funcionario>();
        funcionariosSeleccionados = new ArrayList<Funcionario>();
    }

    public Dependencia getDependenciaXNivelUsuario() {
        Dependencia dependencia = new Dependencia();
        Aeropuerto aeropuerto = new Aeropuerto();
        Regional regional = new Regional();
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        Funcionario funcionarioSesion = logbbean.getFuncionarioTO().getFuncionario();

        listAeropuerto = null;
        listDependencia = null;

        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
            DepCategoria depCat = new DepCategoria(
                    funcionarioSesion.getDependencia().getDepcategoria().getDcId());
            dependencia.setDepcategoria(depCat);
        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A3)) {
            regional = new Regional(
                    funcionarioSesion.getDependencia().getAeropuerto().getRegional().getRegId());
            listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(funcionarioSesion.getDependencia().getAeropuerto().getRegional().getRegId());
        } else if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A4, RolEnum.NIVEL_U1})) {
            dependencia = new Dependencia(funcionarioSesion.getDependencia().getDepId());
        }



        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);
        return dependencia;
    }

    public String filtrar() {
        lazyList = new FuncionarioLazyList(funcionarioService, funcionarioFiltro);
        return "listarFuncionario";
    }

    /**
     * Este método filtra sin realizar paginación, útil para listas de selección
     * múltiple
     *
     * @return
     */
    public String filtrarSinPaginar() {
        this.funcionariosDisponibles = funcionarioService.getListaPag(funcionarioFiltro, null, null,
                "funAlias", SortOrderEnum.ASC.getOrder());
        heightList = this.funcionariosDisponibles.size() >= 20 ? "404px"
                : ((this.funcionariosDisponibles.size() * 28 + 58) + "px");
        JsfUtil.forceRefresh();
        return "editCursoRecurrente";
    }

    public void cargarAeropuerto() {
        cargarAeropuerto(funcionario);
    }

    public void seleccionRol() {
        if (funcionario.getFuNivel() == RolEnum.LINEA_3000.getRolId()
                || funcionario.getFuNivel() == RolEnum.AUDITORIA.getRolId()
                || funcionario.getFuNivel() == RolEnum.NIVEL_SEG.getRolId()
                || funcionario.getFuNivel() == RolEnum.NIVEL_A1.getRolId()) {
            Regional regional = regionalService.getRegional(0L);
            funcionario.getDependencia().getAeropuerto().setRegional(regional);
            cargarAeropuerto(funcionario);
            Aeropuerto aeropuerto = aeropuertoService.findOneById(0L);
            funcionario.getDependencia().setAeropuerto(aeropuerto);
            cargarDependencia(funcionario);
            Dependencia d = dependenciaService.findByd(1L);
            funcionario.setDependencia(d);
        }

    }

    public void cargarAeropuertoListado() {
        cargarAeropuerto(funcionarioFiltro);
    }

    private void cargarAeropuerto(Funcionario f) {
        listDependencia = null;
        listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(f.getDependencia().getAeropuerto().getRegional().getRegId());
        if (listAeropuerto != null) {
            //System.out.println("listAeropuerto.size:"+listAeropuerto.size());
        }
        JsfUtil.forceRefresh();
    }

    public void cargarPosiciones() {
        cargarPosiciones(funcionario);
    }

    private void cargarPosiciones(Funcionario f) {
        listPosicion = null;
        listPosicion = JsfUtil.getListadosBBean().getListaPosicionXDependencia(f.getDependencia().getDepId());
    }

    public void cargarDependencia() {
        cargarDependencia(funcionario);
    }

    public void cargarDependenciaListado() {
        cargarDependencia(funcionarioFiltro);
    }

    private void cargarDependencia(Funcionario f) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getDependencia().getAeropuerto().getAeId(),
                    JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria().getDcId());
        } else {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getDependencia().getAeropuerto().getAeId());
        }
        if (listDependencia != null) {
            //System.out.println("listDependencia.size:"+listDependencia.size());
        }
        JsfUtil.forceRefresh();
    }

    public void listenerRol() {
        if (JsfUtil.isFuncionarioDepInformatica(funcionario.getRol().getRolId())) {
            Dependencia dep = (Dependencia) JsfUtil.getListadosBBean().obtenerObjById(Dependencia.class, 1L);
            listDependencia = new ArrayList<Dependencia>();
            listDependencia.add(dep);
            listAeropuerto = null;
            funcionario.setDependencia(dep);
        } else {


            Regional r = new Regional();
            Aeropuerto a = new Aeropuerto();
            Dependencia d = new Dependencia();
            a.setRegional(r);
            d.setAeropuerto(a);
            funcionario.setDependencia(d);

            listDependencia = null;
        }
    }

    public boolean getFuncionarioDepInformatica() {
        return funcionario.getRol() != null ? JsfUtil.isFuncionarioDepInformatica(funcionario.getRol().getRolId()) : false;
    }

    public String listarUsuariosBloqueados() {
        funcionarioFiltro = new Funcionario();
        funcionarioFiltro.setDependencia(getDependenciaXNivelUsuario());
        funcionarioFiltro.getDependencia().setDepcategoria(new DepCategoria());
        funcionarioFiltro.setFunEstado("Bloqueado");
        //columns = new boolean[3];
        //columns = new boolean[]{true,false,false};
        return filtrar();
    }

    public String desbloquearUsuario() {
        try {
            funcionario.setFunEstado("Activo");
            funcionario.setIntentosFallidos(0L);
            funcionario = funcionarioService.guardar(funcionario, JsfUtil.getFuncionarioSesion());
            funcionarioFiltro.setFunEstado("Bloqueado");
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        } catch (Exception ex) {
            Logger.getLogger(FuncionarioBBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return filtrar();
    }

    public void onEditEvent(RowEditEvent event) {
        try {
            funcionarioService.guardar((Funcionario) event.getObject(),
                    JsfUtil.getFuncionarioSesion());
        } catch (SQLIntegrityConstraintViolationException ex) {
            Logger.getLogger(FuncionarioBBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onCancel(RowEditEvent event) {
        this.funcionarioSeleccionado = new Funcionario();
        JsfUtil.addManualSuccessMessage("Operacion Cancelada");
    }

    //EventoActualiza evaluacion
    public void onEditEvaluacion(RowEditEvent event) {
        funcionarioSeleccionado = (Funcionario) event.getObject();
        EvaluacionCompetenciasBBean evalBbean = (EvaluacionCompetenciasBBean) JsfUtil.getManagedBean(EvaluacionCompetenciasBBean.class);
        EvaluacionCompetencia ec = new EvaluacionCompetencia();
        ec.setEvDescripcion(this.funcionarioSeleccionado.getEvaluacionCompetenciaEdicion().getEvDescripcion());
        ec.setEvFechaRealiza(this.funcionarioSeleccionado.getEvaluacionCompetenciaEdicion().getEvFechaRealiza());
        ec.setEvFechaVence(this.fechaVenceEvaluacion);
        ec.setEvResultado(this.funcionarioSeleccionado.getEvaluacionCompetenciaEdicion().getEvResultado());
        ec.setFuncionario(funcionarioSeleccionado);
        evalBbean.setEvaluacion(ec);
        evalBbean.guardarEvaluacion();

        funcionarioSeleccionado.setEvaluacionCompetencia(ec);
        funcionarioSeleccionado.setEvaluacionCompetenciaEdicion(new EvaluacionCompetencia());
        this.fechaVenceEvaluacion = null;
        this.fechaRealizaEvaluacion = null;
        this.evaluacion = null;
        this.resultado = null;
        evalBbean.setEvaluacion(new EvaluacionCompetencia());
    }

    public void onCancelEvaluacion(RowEditEvent event) {
        this.funcionarioSeleccionado = new Funcionario();
        JsfUtil.addManualSuccessMessage("Operacion Cancelada");
    }

    public void actualiza(DateSelectEvent evento) {
        Calendar c = Calendar.getInstance();
        c.setTime(evento.getDate());
        c.add(Calendar.YEAR, 1);
        this.fechaVenceEvaluacion = (c.getTime());
        this.fechaRealizaEvaluacion = evento.getDate();
    }

    public void cargarFuncionario() {
        this.funcionarioSeleccionado = (Funcionario) this.table.getRowData();
    }

    public List<Posicion> getListPosicion() {
        return listPosicion;
    }

    public void setListPosicion(List<Posicion> listPosicion) {
        this.listPosicion = listPosicion;
    }

    public List<Funcionario> getFuncionariosDisponibles() {
        return funcionariosDisponibles;
    }

    public void setFuncionariosDisponibles(List<Funcionario> funcionariosDisponibles) {
        this.funcionariosDisponibles = funcionariosDisponibles;
    }

    public List<Funcionario> getFuncionariosSeleccionados() {
        return funcionariosSeleccionados;
        //return funcionariosPickList.getTarget();
    }

    public void setFuncionariosSeleccionados(List<Funcionario> funcionariosSeleccionados) {
        this.funcionariosSeleccionados = funcionariosSeleccionados;
    }

    public String getHeightList() {
        return heightList;
    }

    public boolean[] getColumns() {
        return columns;
    }

    public void setColumns(boolean[] columns) {
        this.columns = columns;
    }

    public ArrayList<Posicion> getPosicionesSeleccionados() {
        return posicionesSeleccionados;
    }

    public void setPosicionesSeleccionados(ArrayList<Posicion> posicionesSeleccionados) {
        this.posicionesSeleccionados = posicionesSeleccionados;
    }

    public Date getFechaini() {
        return fechaini;
    }

    public void setFechaini(Date fechaini) {
        this.fechaini = fechaini;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }
    private String metodoRetorno;

    public void setRetorno(String metodoRetorno) {
        this.metodoRetorno = metodoRetorno;
    }

    public String retornar() {
        String[] regla = metodoRetorno.split("\\.");
        return JsfUtil.ejecutarNavegacion(regla[0], regla[1]);
    }

    public void listenerHExtras() {
        //System.out.println("entro: " + this.funcionario.getFunHorasExtras());
        this.funcionario.setFunHEExcedente(null);
        this.funcionario.setFunCantMaxHE(null);
    }

    public List<CursoRecurrente> getCursosRecurrentes() {
        return cursosRecurrentes;
    }

    public void setCursosRecurrentes(List<CursoRecurrente> cursosRecurrentes) {
        this.cursosRecurrentes = cursosRecurrentes;
    }

    public boolean isEditando() {
        return editando;
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

    public DataTable getTable() {
        return table;
    }

    public void setTable(DataTable table) {
        this.table = table;
    }

    public Funcionario getFuncionarioSeleccionado() {
        return funcionarioSeleccionado;
    }

    public void setFuncionarioSeleccionado(Funcionario funcionarioSeleccionado) {
        this.funcionarioSeleccionado = funcionarioSeleccionado;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Funcionario getFuncionarioFiltro() {
        return funcionarioFiltro;
    }

    public void setFuncionarioFiltro(Funcionario funcionarioFiltro) {
        this.funcionarioFiltro = funcionarioFiltro;
    }

    public LazyDataModel<Funcionario> getLazyList() {
        return lazyList;
    }

    public List<Aeropuerto> getListAeropuerto() {
        return listAeropuerto;
    }

    public List<Dependencia> getListDependencia() {
        return listDependencia;
    }

    public Date getFechaVenceEvaluacion() {
        return fechaVenceEvaluacion;
    }

    public void setFechaVenceEvaluacion(Date fechaVenceEvaluacion) {
        this.fechaVenceEvaluacion = fechaVenceEvaluacion;
    }

    public Date getFechaRealizaEvaluacion() {
        return fechaRealizaEvaluacion;
    }

    public void setFechaRealizaEvaluacion(Date fechaRealizaEvaluacion) {
        this.fechaRealizaEvaluacion = fechaRealizaEvaluacion;
    }

    public String getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(String evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
