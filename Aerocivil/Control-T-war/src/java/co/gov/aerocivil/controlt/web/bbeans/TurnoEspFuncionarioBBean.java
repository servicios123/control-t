/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Comision;
import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.DiaFestivo;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.entities.Turno;
import co.gov.aerocivil.controlt.entities.TurnoEspFuncionario;
import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import co.gov.aerocivil.controlt.enums.EstadoProgramacion;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.ProgramacionTurnosSession;
import co.gov.aerocivil.controlt.services.TurnoEspFuncionarioService;
import co.gov.aerocivil.controlt.services.TurnoService;
import co.gov.aerocivil.controlt.services.VistaProgramacionService;
import co.gov.aerocivil.controlt.web.lazylist.TurnoEspFuncionarioLazyList;
import co.gov.aerocivil.controlt.web.util.DateUtil;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.schedule.Schedule;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.ScheduleEntrySelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Viviana
 */
@ManagedBean
@SessionScoped
public class TurnoEspFuncionarioBBean {

    /**
     * Creates a new instance of TurnoEspFuncionarioBBean
     */
    @EJB
    private ProgramacionTurnosSession serviceProg;
    @EJB
    private TurnoEspFuncionarioService turnoEspFuncionarioService;
    @EJB
    private VistaProgramacionService vistaProgramacionService;
    @EJB
    private TurnoService turnoService;
    private Turno turnoNuevo;
    private TurnoEspFuncionario turnoEspFuncionario;
    private TurnoEspFuncionario turnoEspFuncionarioFiltro;
    private Dependencia dependencia;
    private Funcionario funcionario;
    private TurnoEspecial turnoEspecial;
    private List<TurnoEspecial> listTurnoEspecial;
    private List<Dependencia> listDependencia;
    private LazyDataModel<TurnoEspFuncionario> lista;
    private List<Aeropuerto> listAeropuerto;
    private Aeropuerto aeropuerto;
    private Regional regional;
    private boolean dependenciaVisible;
    private boolean editable;
    private boolean ver;
    private Comision comision;
    private boolean editando;
    private boolean asignando;
    private boolean showPrev;
    private List<Funcionario> listaFuncionario;
    private List<TurnoEspFuncionario> turnos;
    private DataTable dataTable;
    private ScheduleModel eventModel;
    private Date iniDate;
    int moveValue;
    private int corrimiento;
    private int[] festivos;
    private ScheduleEvent event = new DefaultScheduleEvent();
    Map<String, Integer[]> mapa;
    Map<String, Integer[]> mapaToSave;
    private Schedule schedule;

    public Aeropuerto getAeropuerto() {
        return aeropuerto;
    }

    public void setAeropuerto(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
    }

    public Regional getRegional() {
        return regional;
    }

    public void setRegional(Regional regional) {
        this.regional = regional;
    }

    public List<Funcionario> getListaFuncionario() {
        return listaFuncionario;
    }

    public void setListaFuncionario(List<Funcionario> listaFuncionario) {
        this.listaFuncionario = listaFuncionario;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public TurnoEspFuncionarioBBean() {
        //System.out.println("listando-...");
        turnos = new ArrayList<TurnoEspFuncionario>();
        
        funcionario = new Funcionario();
        inicializaVariables();
        org.primefaces.component.calendar.Calendar obj = new org.primefaces.component.calendar.Calendar();
        for (String s : obj.getEventNames()) {
            //System.out.println(s);
        }

    }

    public String listar() {
        turnos = new ArrayList<TurnoEspFuncionario>();
        asignando = false;
        turnoEspFuncionarioFiltro = new TurnoEspFuncionario();

        regional = new Regional();
        dependencia = new Dependencia();
        aeropuerto = new Aeropuerto();
        comision = new Comision();

        turnoEspecial = new TurnoEspecial();

        DepCategoria depCategoria = new DepCategoria();
        funcionario = new Funcionario();

        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);
        dependencia.setDepcategoria(depCategoria);
        turnoEspecial.setDependencia(dependencia);


        turnoEspFuncionarioFiltro.setTurnoEspecial(turnoEspecial);
        turnoEspFuncionarioFiltro.setFuncionario(funcionario);

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2})) {

            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            turnoEspecial.setDependencia(dependencia);
            turnoEspFuncionarioFiltro.setTurnoEspecial(turnoEspecial);
            cargarDependenciaListado();
        }
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A3})) {


            regional.setRegId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
            aeropuerto.setRegional(regional);
            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            dependencia.setAeropuerto(aeropuerto);
            turnoEspecial.setDependencia(dependencia);

            turnoEspFuncionarioFiltro.setTurnoEspecial(turnoEspecial);
            cargarAeropuertoListado();

        }
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A4})) {

            dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
            turnoEspecial.setDependencia(dependencia);

            turnoEspFuncionarioFiltro.setTurnoEspecial(turnoEspecial);

        }
        turnos = new ArrayList<TurnoEspFuncionario>();
        return filtrar();

    }

    public String asignar() {
        asignando = true;
        turnos = new ArrayList<TurnoEspFuncionario>();
        FuncionarioBBean funcionarioBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        return funcionarioBBean.listarAsignacionesEspeciales();
    }

    public String filtrar() {
        /*cargarAeropuerto();
         cargarDependencia();*/
        ver = false;
        lista = new TurnoEspFuncionarioLazyList(turnoEspFuncionarioService, turnoEspFuncionarioFiltro);
        return "listarTurnoEspFuncionario";
    }

    public boolean turnoEspFunEnProgramacion() {
        return turnoEspFuncionarioService.turnoEspFunEnProgramacion(turnoEspFuncionario);
    }

    public boolean turnoEspFunEnProgramacion(TurnoEspFuncionario tef) {
        return turnoEspFuncionarioService.turnoEspFunEnProgramacion(tef);
    }

    public void inicializaVariables() {
        turnoEspFuncionario = new TurnoEspFuncionario();
        turnoEspecial = new TurnoEspecial();
        editando = false;
        editable = true;
        turnoEspFuncionario.setTurnoEspecial(turnoEspecial);
        turnoEspFuncionario.setFuncionario(funcionario);
        dependenciaVisible = false;
        dependencia = JsfUtil.getFuncionarioSesion().getDependencia();
        listaFuncionario = JsfUtil.getListadosBBean().getListFuncionariosXDependencia(JsfUtil.getFuncionarioSesion().getDependencia().getDepId());

        cargarTurnoEspecial();
    }

    public String crearTurnoProgramacion() {
        turnoEspFuncionario = new TurnoEspFuncionario();
        funcionario = new Funcionario();
        editando = false;
        editable = true;
        turnoEspecial = new TurnoEspecial();
        turnoEspFuncionario.setTurnoEspecial(turnoEspecial);
        turnoEspFuncionario.setFuncionario(funcionario);
        dependenciaVisible = false;
        listaFuncionario = JsfUtil.getListadosBBean().getListFuncionariosXDependencia(JsfUtil.getFuncionarioSesion().getDependencia().getDepId());
        cargarTurnoEspecialList();
        showPrev = false;
        /*  cargarAeropuertoComision();    
         cargarDependenciaComision();*/
        return "crearTurnosEspFunProgramacion";


    }

    public String eliminar() {
        try {
            turnoEspFuncionarioService.eliminar(turnoEspFuncionario);
            updateListaTurnos();
            turnoEspFuncionario.setTefFfin(null);
            turnoEspFuncionario.setTefFini(null);
//            dataTable.setValue(turnos);
        } catch (Exception ex) {
            JsfUtil.addManualWarningMessage(ex.getMessage());
        }

        return "crearTurnosEspFuncionario";
    }

    public String eliminarTef() {
        try {
            turnoEspFuncionarioService.eliminar(turnoEspFuncionario);
            updateListaTurnos();
            turnoEspFuncionario.setTefFfin(null);
            turnoEspFuncionario.setTefFini(null);
//            dataTable.setValue(turnos);
        } catch (Exception ex) {
            JsfUtil.addManualWarningMessage(ex.getMessage());
        }

        return filtrar();
    }

    public String eliminarTefTrop() {
        try {
            turnoEspFuncionarioService.eliminar(turnoEspFuncionario);
            turnoEspFuncionario.setTefFfin(null);
            turnoEspFuncionario.setTefFini(null);
            Long funId = this.turnoEspFuncionario.getFuncionario().getFunId();
            funcionario = new Funcionario();
            funcionario.setFunId(funId);
            this.turnoEspFuncionario.setFuncionario(funcionario);
            loadEvents(iniDate, iniDate);
//            dataTable.setValue(turnos);
        } catch (Exception ex) {
            JsfUtil.addManualWarningMessage(ex.getMessage());
        }

        return irAsignarTrop();
    }

    public void updateListaTurnos() {
        turnoEspFuncionario.setTurnoEspecial(new TurnoEspecial());
        turnos = turnoEspFuncionarioService.listarTurnosAsignacion(turnoEspFuncionario.getFuncionario());
        for (TurnoEspFuncionario tef : turnos) {
            tef.setEditable(!turnoEspFunEnProgramacion(tef));
        }
    }

    public void updateListaTurnostrop() {
        Long funId = this.turnoEspFuncionario.getFuncionario().getFunId();
        funcionario = new Funcionario();
        funcionario.setFunId(funId);
        this.turnoEspFuncionario.setFuncionario(funcionario);
        Calendar c = Calendar.getInstance();
        turnoEspFuncionario.setTurnoEspecial(new TurnoEspecial());
        turnoEspFuncionario.getTurnoEspecial().setTeSigla("TROP");
        List<TurnoEspFuncionario> listarTurnosAsignacion = turnoEspFuncionarioService.listarTurnosAsignacion(turnoEspFuncionario.getFuncionario());
        turnos = new ArrayList<TurnoEspFuncionario>();
        eventModel = new DefaultScheduleModel();
        for (TurnoEspFuncionario tef : listarTurnosAsignacion) {
            if (tef.getTurnoEspecial().getTeSigla().equalsIgnoreCase("TROP")) {
                if (tef.getTefFini().getMonth() == c.get(Calendar.MONTH) || tef.getTefFfin().getMonth() == c.get(Calendar.MONTH)) {
                    turnos.add(tef);
                }
            }
        }
        loadEvents(iniDate, iniDate);
    }

    public String crear() {
        if (turnoEspFuncionario == null) {
            turnoEspFuncionario = new TurnoEspFuncionario();
        }
        turnos = new ArrayList<TurnoEspFuncionario>();
        editable = true;
        turnoEspecial = new TurnoEspecial();
        turnoEspFuncionario.setTurnoEspecial(turnoEspecial);
        turnoEspFuncionario.setFuncionario(new Funcionario());
        dependenciaVisible = false;
        cargarTurnoEspecial();
        listaFuncionario = JsfUtil.getListadosBBean().getListFuncionariosXDependencia(JsfUtil.getFuncionarioSesion().getDependencia().getDepId());
        /*  cargarAeropuertoComision();    
         cargarDependenciaComision();*/
        turnos = new ArrayList<TurnoEspFuncionario>();
        return limpiar();



    }

    public String guardar() {//*-*
        TurnoEspFuncionario res = null;
        try {
            if (turnoEspFuncionario.getTefFfin().before(turnoEspFuncionario.getTefFini())) {
                JsfUtil.addWarningMessage("turnoEspecialFechas");
                return "crearTurnosEspFuncionario";
            }

            turnoEspFuncionario.setComision(comision);
            funcionario = turnoEspFuncionario.getFuncionario();
            turnoEspecial = turnoEspFuncionario.getTurnoEspecial();
            if (editando) {
                turnoEspFuncionario.setAccion("Edicion");
            }
            if (!turnoEspFuncionarioService.isRangoTurnoDisponible(turnoEspFuncionario)) {
                JsfUtil.addWarningMessage("turnoEspecialFechasRango");
                return "crearTurnosEspFuncionario";
            }
            
            res = turnoEspFuncionarioService.guardar(turnoEspFuncionario,
                    JsfUtil.getFuncionarioSesion());
        } catch (Exception e) {
            JsfUtil.addManualWarningMessage(e.getMessage());
        }
        if (res == null) {
            JsfUtil.addWarningMessage("turnoEspecialRepetido");
            return "crearTurnosEspFuncionario";
        }
        FuncionarioBBean funcionarioBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        funcionarioBBean.setFuncionario(turnoEspFuncionario.getFuncionario());
//        JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        turnos = turnoEspFuncionarioService.listarTurnosAsignacion(turnoEspFuncionario.getFuncionario());
        for (TurnoEspFuncionario tef : turnos) {
            tef.setEditable(!turnoEspFunEnProgramacion(tef));
        }
        cargarTurnoEspecial();
        inicializaVariables();
//        dataTable.setValue(turnos);
//        return funcionarioBBean.listarAsignacionesEspeciales();
        return "crearTurnosEspFuncionario";

    }

    public String limpiar() {
        turnoEspFuncionario = new TurnoEspFuncionario();
        turnoEspFuncionario.setTurnoEspecial(new TurnoEspecial());
        turnoEspFuncionario.setFuncionario(new Funcionario());
        listaFuncionario = JsfUtil.getListadosBBean().getListFuncionariosXDependencia(JsfUtil.getFuncionarioSesion().getDependencia().getDepId());
        cargarTurnoEspecial();
        turnos = new ArrayList<TurnoEspFuncionario>();
        try {
            dataTable.setValue(turnos);
        } catch (Exception e) {
        }
        return "crearTurnosEspFuncionario";
    }

    public String guardarProgramado() {
        Long HorasLaboradas = vistaProgramacionService.validarAsignarEspecial(turnoEspFuncionario.getFuncionario().getFunId(), turnoEspFuncionario.getTefFini(), turnoEspFuncionario.getTurnoEspecial().getTeId());
        Programacion programacion = new Programacion();
        programacion = JsfUtil.getListadosBBean().obtenerProgramacionXFecha(turnoEspFuncionario.getTefFini());
        if (HorasLaboradas == 1L) {
            if (programacion != null) {
                try {
                    turnoEspFuncionario.setComision(comision);
                    turnoEspFuncionario.setTefFfin(turnoEspFuncionario.getTefFini());
                    turnoEspFuncionario = turnoEspFuncionarioService.guardar(turnoEspFuncionario,
                            JsfUtil.getFuncionarioSesion());
                    turnoEspecial = (TurnoEspecial) JsfUtil.getListadosBBean().obtenerObjById(TurnoEspecial.class, turnoEspFuncionario.getTurnoEspecial().getTeId());
                    turnoEspFuncionario.setTurnoEspecial(turnoEspecial);
                    turnoNuevo = new Turno();
                    turnoNuevo.setFuncionario(turnoEspFuncionario.getFuncionario());
                    turnoNuevo.setProgramacion(programacion);
                    turnoNuevo.setTurEstado(0L);
                    turnoNuevo.setTurFecha(turnoEspFuncionario.getTefFini());
                    turnoNuevo.setTurHFin(turnoEspFuncionario.getTurnoEspecial().getTeHfin().longValue());
                    turnoNuevo.setTurHInicio(turnoEspFuncionario.getTurnoEspecial().getTeHinicio().longValue());
                    turnoNuevo.setTurMFin(turnoEspFuncionario.getTurnoEspecial().getTeMfin().longValue());
                    turnoNuevo.setTurMInicio(turnoEspFuncionario.getTurnoEspecial().getTeMinicio().longValue());
                    turnoNuevo.setTurPosicionJornada(turnoEspFuncionario.getTefId());
                    //turnoNuevo.setTurTipo(turnoActualizar.getTurTipo());
                    turnoNuevo.setTurTipo(7L);
                    //turnoNuevo.setTurTurnoOriginal(turnoActualizar.getTurId());
                    turnoService.guardar(turnoNuevo,
                            JsfUtil.getFuncionarioSesion());
                    JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
                } catch (Exception e) {
                    JsfUtil.addManualWarningMessage(e.getMessage());
                }
            } else {
                JsfUtil.addWarningMessage("noExisteProgramacionParaLaFecha");
            }
        } else {
            JsfUtil.addWarningMessage("ExcedeHorasDiarias");
        }
        VistaProgramacionBBean vistaProgramacionBBean = (VistaProgramacionBBean) JsfUtil.getManagedBean(VistaProgramacionBBean.class);
        JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        return vistaProgramacionBBean.listarEspeciales();
    }

    public String actualizar() {
        try {
            turnoEspFuncionario.setComision(comision);
            turnoEspFuncionario.setAccion("Edicion");
            turnoEspFuncionarioService.guardar(turnoEspFuncionario,
                    JsfUtil.getFuncionarioSesion());


            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        } catch (Exception e) {
            JsfUtil.addManualWarningMessage(e.getMessage());
        }
        return filtrar();

    }

    public String editar() {
        editando = true;
        editable = true;
        ver = false;
        dependenciaVisible = turnoEspFuncionario.getComision() != null;
        comision = turnoEspFuncionario.getComision();
        listaFuncionario = JsfUtil.getListadosBBean().getListFuncionariosXDependencia(JsfUtil.getFuncionarioSesion().getDependencia().getDepId());
        if (comision != null) {
            cargarAeropuertoComision();
            cargarDependenciaComision();
        }
        cargarTurnoEspecial();
        return "EditTurnosEspFuncionario";
    }

    public boolean isEditable() {
        return editable;
    }

    public String ver() {
        dependenciaVisible = turnoEspFuncionario.getComision() != null;
        comision = turnoEspFuncionario.getComision();
        editable = false;
        ver = true;
        editando = false;
        //cargarTurnoEspecial();
        return "EditTurnosEspFuncionario";
    }

    public void cargarTurnoEspecial() {
        cargarTurnoEspecial(turnoEspFuncionario);
    }

    public void cargarTurnoEspecialListado() {
        cargarTurnoEspecial(turnoEspFuncionarioFiltro);
    }

    private void cargarTurnoEspecial(TurnoEspFuncionario f) {

        listTurnoEspecial = JsfUtil.getListadosBBean().getListaTurnoEspeciaXDependencia(JsfUtil.getFuncionarioSesion().getDependencia().getDepId());

        TurnoEspecial comision = new TurnoEspecial();
        int i = 0;
        for (TurnoEspecial te : listTurnoEspecial) {
            if (te.getTeComision() != null && te.getTeComision() == 1) {
                comision = te;
                break;
            }
            i++;
        }
        if (JsfUtil.getFuncionarioSesion().getFuNivel() == 3) {
            listTurnoEspecial = new ArrayList<TurnoEspecial>();
            listTurnoEspecial.add(comision);
        }
        /*else{
         listTurnoEspecial.remove(i);
         }*/

    }

    public void asignarTrop(Date date) {
        TurnoEspecial te = new TurnoEspecial();
        te.setDependencia(JsfUtil.getFuncionarioSesion().getDependencia());
        te.setTeSigla("TROP");
        te = JsfUtil.getListadosBBean().obtenerTurnoXDepAlias(te);
        try {
            turnoEspFuncionario.setTefEstado("Programado");
            turnoEspFuncionario.setTurnoEspecial(te);
            turnoEspFuncionario.setTefFini(date);
            turnoEspFuncionario.setTefFfin(date);
            turnoEspFuncionarioService.guardar(turnoEspFuncionario,
                    JsfUtil.getFuncionarioSesion());
            loadEvents(iniDate, iniDate);
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        } catch (Exception e) {
            JsfUtil.addManualWarningMessage(e.getMessage());
        }
    }

    private void cargarFestivos(Date start2, Date end) {
        List<DiaFestivo> listaDiaFestivo = JsfUtil.getListadosBBean().getListaFestivos(start2, end);
        festivos = new int[listaDiaFestivo.size()];
        int i = 0;
        for (DiaFestivo df : listaDiaFestivo) {
            festivos[i] = df.getDfFecha().getDate() + corrimiento - 1;
            //System.out.println(festivos[i]);
            //jqueryScript+="$(\".fc-day"+festivos[i]+"\").css(\"background-color\",\"lightBlue\"); ";
            i++;
        }
    }

    public String irAsignarTrop() {
        eventModel = new DefaultScheduleModel();
        turnos = new ArrayList<TurnoEspFuncionario>();
        if (iniDate == null) {
            iniDate = Calendar.getInstance().getTime();
        }
        loadEvents(iniDate, iniDate);
        return "listTurnosTrop";
    }

    public void loadEvents(Date start, Date end) {
        eventModel = new DefaultScheduleModel();
        if (start == null) {
            start = Calendar.getInstance().getTime();
        }
        Date start2 = JsfUtil.getFirstDayMonth(start);
        iniDate = start2;
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(start2);
            corrimiento = cal.get(Calendar.DAY_OF_WEEK) - 1;
        }
        end = JsfUtil.getLastDayMonth(start2);
        List<TurnoEspFuncionario> schedulerTurns = new ArrayList<TurnoEspFuncionario>();
        turnoEspFuncionario.setTefFini(start2);

        turnoEspFuncionario.setTefFfin(end);
        turnoEspFuncionario.getTurnoEspecial().setDependencia(JsfUtil.getFuncionarioSesion().getDependencia());
        if (turnoEspFuncionario.getFuncionario().getFunId() != null) {
            turnoEspFuncionario.getFuncionario().setFunAlias(null);
            turnoEspecial = new TurnoEspecial();
            turnoEspecial.setDependencia(dependencia);
            turnoEspFuncionario.setTurnoEspecial(turnoEspecial);
            schedulerTurns = turnoEspFuncionarioService.getLista(turnoEspFuncionario);
            turnoEspFuncionario.getTurnoEspecial().setTeSigla("TROP");
            turnos = turnoEspFuncionarioService.getLista(turnoEspFuncionario);
        }
        for (TurnoEspFuncionario tef : schedulerTurns) {
            DefaultScheduleEvent defaultScheduleEvent = new DefaultScheduleEvent(tef.getTurnoEspecial().getTeSigla(),
                    tef.getTefFini(), tef.getTefFfin(), true);
            defaultScheduleEvent.setEditable(false);
            defaultScheduleEvent.setStyleClass("asigned");
            eventModel.addEvent(defaultScheduleEvent);
        }
    }

    public void onDateSelect(DateSelectEvent selectEvent) {
        Calendar hoy = DateUtil.setCeroHoras(Calendar.getInstance());

        Calendar evDate = Calendar.getInstance();
        evDate.setTime(selectEvent.getDate());
        evDate = DateUtil.setCeroHoras(evDate);

        if (!turnoEspFuncionarioService.validarDisponibilidadTurno(evDate.getTime(), funcionario).equals("OK")) {
            JsfUtil.addWarningMessage("fechaOcupada");
            return;
        }
        boolean assigned = false;

        for (ScheduleEvent ev : eventModel.getEvents()) {
            //System.out.println("entra");
            Calendar c = Calendar.getInstance();
            c.setTime(ev.getStartDate());
            c = DateUtil.setCeroHoras(c);

            if (evDate.getTime().equals(c.getTime())) {
                event = ev;
                assigned = true;
                //System.out.println("exit");
                break;
            }
        }
        if (!assigned) {
            //popupVisible = true;
            asignarTrop(selectEvent.getDate());
        }
    }

    public void onEventSelect(ScheduleEntrySelectEvent selectEvent) {
        Calendar hoy = DateUtil.setCeroHoras(Calendar.getInstance());
        hoy.set(Calendar.HOUR_OF_DAY, 12);

        Calendar evDate = Calendar.getInstance();
        evDate.setTime(selectEvent.getScheduleEvent().getStartDate());
        evDate = DateUtil.setCeroHoras(evDate);
        evDate.set(Calendar.HOUR_OF_DAY, 12);

        //System.out.println(evDate.getTime());
        //System.out.println("hoy: " + hoy.getTime());
        //System.out.println("evDate.compareTo(hoy)<0:" + (evDate.compareTo(hoy) < 0));
        if (evDate.compareTo(hoy) < 0
                || serviceProg.isFechaProgramada(evDate.getTime(), dependencia.getDepId(), EstadoProgramacion.PROGRAMADA)) {
            return;
        }
        event = selectEvent.getScheduleEvent();
    }

    public String nextPrevious() {
        eventModel = new DefaultScheduleModel();
        if (iniDate == null) {
            iniDate = Calendar.getInstance().getTime();
        }
        iniDate = DateUtil.addMonth(iniDate, moveValue);
        loadEvents(iniDate, iniDate);
        showPrev = !(Calendar.getInstance().get(Calendar.MONTH) == iniDate.getMonth());
        return "listTurnosTrop";
    }

    private void cargarTurnoEspecialList() {

        listTurnoEspecial = JsfUtil.getListadosBBean().getListaTurnoEspeciaXDependencia(JsfUtil.getFuncionarioSesion().getDependencia().getDepId());

        TurnoEspecial comision = new TurnoEspecial();
        int i = 0;
        if (listTurnoEspecial != null) {
            for (TurnoEspecial te : listTurnoEspecial) {
                if (te.getTeComision() == 1) {
                    comision = te;
                    break;
                }
                i++;
            }

        }



    }

    public void cargarTurnoEspecialXte() {
        cargarTurnoEspecialXte(turnoEspFuncionario);
    }

    public void cargarTurnoEspecialXteListado() {
        cargarTurnoEspecialXte(turnoEspFuncionarioFiltro);
    }

    private void cargarTurnoEspecialXte(TurnoEspFuncionario f) {

        listTurnoEspecial = JsfUtil.getListadosBBean().getListaTurnoEspeciaXDependencia(f.getTurnoEspecial().getDependencia().getDepId());
    }

    public void cargarAeropuerto() {
        cargarAeropuerto(turnoEspFuncionario);
    }

    public void cargarAeropuertoListado() {

        cargarAeropuerto(turnoEspFuncionarioFiltro);
    }

    private void cargarAeropuerto(TurnoEspFuncionario f) {
        listDependencia = null;
        listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(f.getTurnoEspecial().getDependencia().getAeropuerto().getRegional().getRegId());
    }

    public void cargarDependencia() {
        cargarDependencia(turnoEspFuncionario);
    }

    public void cargarDependenciaListado() {
        cargarDependencia(turnoEspFuncionarioFiltro);
    }

    private void cargarDependencia(TurnoEspFuncionario f) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getTurnoEspecial().getDependencia().getAeropuerto().getAeId(),
                    JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria().getDcId());
        } else {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getTurnoEspecial().getDependencia().getAeropuerto().getAeId());
        }

    }
    //private Comision comision;

    public void listenerFIni() {
        //this.turnoEspFuncionario.setTefFini((Date)ve.getNewValue());
        //System.out.println(this.turnoEspFuncionario.getTefFini());
    }

    public void activarDependencia() {
        dependenciaVisible = false;
        comision = null;
        Long id = turnoEspFuncionario.getTurnoEspecial().getTeId();// Long.valueOf(ve.getNewValue().toString());
        turnoEspFuncionario.setTurnoEspecial((TurnoEspecial) JsfUtil.getListadosBBean().obtenerObjById(TurnoEspecial.class, id));
        //System.out.println(turnoEspFuncionario.getTurnoEspecial());
        //System.out.println(turnoEspFuncionario.getTurnoEspecial().getTeComision());
        if (turnoEspFuncionario.getTurnoEspecial().getTeComision() != null
                && turnoEspFuncionario.getTurnoEspecial().getTeComision() == 1) {
            dependenciaVisible = true;
            crearComision();
        }
    }

    private void crearComision() {
        comision = new Comision();
        Dependencia dep = new Dependencia();
        Aeropuerto aer = new Aeropuerto();
        Regional r = new Regional();
        aer.setRegional(r);
        if (JsfUtil.getFuncionarioSesion().getFuNivel() > 3) {
            aer.setRegional(JsfUtil.getFuncionarioSesion().getDependencia().getAeropuerto().getRegional());
        }

        /* if(JsfUtil.getFuncionarioSesion().getFuNivel()<3){
         aer.setRegional(JsfUtil.getFuncionarioSesion().getDependencia().getAeropuerto().getRegional());
         }*/
        dep.setAeropuerto(aer);
        comision.setDependencia(dep);
        comision.setEstado("");
        comision.setTurnoEspFuncionario(turnoEspFuncionario);
        cargarAeropuertoComision();
    }

    public boolean isDependenciaVisible() {
        return dependenciaVisible;
    }

    public TurnoEspFuncionarioService getTurnoEspFuncionarioService() {
        return turnoEspFuncionarioService;
    }

    public void setTurnoEspFuncionarioService(TurnoEspFuncionarioService turnoEspFuncionarioService) {
        this.turnoEspFuncionarioService = turnoEspFuncionarioService;
    }

    public TurnoEspFuncionario getTurnoEspFuncionario() {
        return turnoEspFuncionario;
    }

    public void setTurnoEspFuncionario(TurnoEspFuncionario turnoEspFuncionario) {
        this.turnoEspFuncionario = turnoEspFuncionario;
    }

    public TurnoEspFuncionario getTurnoEspFuncionarioFiltro() {
        return turnoEspFuncionarioFiltro;
    }

    public void setTurnoEspFuncionarioFiltro(TurnoEspFuncionario turnoEspFuncionarioFiltro) {
        this.turnoEspFuncionarioFiltro = turnoEspFuncionarioFiltro;
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public TurnoEspecial getTurnoEspecial() {
        return turnoEspecial;
    }

    public void setTurnoEspecial(TurnoEspecial turnoEspecial) {
        this.turnoEspecial = turnoEspecial;
    }

    public List<TurnoEspecial> getListTurnoEspecial() {
        return listTurnoEspecial;
    }

    public void setListTurnoEspecial(List<TurnoEspecial> listTurnoEspecial) {
        this.listTurnoEspecial = listTurnoEspecial;
    }

    public List<TurnoEspFuncionario> getTurnos() {
        return turnos;
    }

    public void setTurnos(List<TurnoEspFuncionario> turnos) {
        this.turnos = turnos;
    }

    public List<Dependencia> getListDependencia() {
        return listDependencia;
    }

    public void setListDependencia(List<Dependencia> listDependencia) {
        this.listDependencia = listDependencia;
    }

    public List<Aeropuerto> getListAeropuerto() {
        return listAeropuerto;
    }

    public void setListAeropuerto(List<Aeropuerto> listAeropuerto) {
        this.listAeropuerto = listAeropuerto;
    }

    public LazyDataModel<TurnoEspFuncionario> getLista() {
        return lista;
    }

    public void setLista(LazyDataModel<TurnoEspFuncionario> lista) {
        this.lista = lista;
    }

    public Comision getComision() {
        return comision;
    }

    public void setComision(Comision comision) {
        this.comision = comision;
    }

    public void cargarAeropuertoComision() {
        listDependencia = null;
        listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(comision.getDependencia().getAeropuerto().getRegional().getRegId());
    }

    public void cargarDependenciaComision() {

        listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(comision.getDependencia().getAeropuerto().getAeId(),
                JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria().getDcId());
        //listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(comision.getDependencia().getAeropuerto().getAeId());
    }

    public boolean isVisibleEstado() {
        return turnoEspFuncionario != null
                && turnoEspFuncionario.getTefId() != null
                && !"Programado".equals(turnoEspFuncionario.getTefEstado());
    }

    public boolean isEditando() {
        return editando;
    }

    public boolean isAsignando() {
        return asignando;
    }

    public VistaProgramacionService getVistaProgramacionService() {
        return vistaProgramacionService;
    }

    public void setVistaProgramacionService(VistaProgramacionService vistaProgramacionService) {
        this.vistaProgramacionService = vistaProgramacionService;
    }

    public TurnoService getTurnoService() {
        return turnoService;
    }

    public void setTurnoService(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    public Turno getTurnoNuevo() {
        return turnoNuevo;
    }

    public void setTurnoNuevo(Turno turnoNuevo) {
        this.turnoNuevo = turnoNuevo;
    }

    public String activar() {

        try {
            turnoEspFuncionario.setTefEstado("Programado");
            turnoEspFuncionario.setAccion("Edicion");
            turnoEspFuncionarioService.guardar(turnoEspFuncionario,
                    JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        } catch (Exception e) {
            JsfUtil.addManualWarningMessage(e.getMessage());
        }


        return filtrar();

    }

    public String desactivar() {

        try {
            turnoEspFuncionario.setTefEstado("Anulado");
            turnoEspFuncionario.setAccion("Edicion");
            turnoEspFuncionarioService.guardar(turnoEspFuncionario,
                    JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        } catch (Exception e) {
            JsfUtil.addManualWarningMessage(e.getMessage());
        }

        return filtrar();



    }

    public boolean isVer() {
        return ver;
    }

    public void setVer(boolean ver) {
        this.ver = ver;
    }

    public ProgramacionTurnosSession getServiceProg() {
        return serviceProg;
    }

    public void setServiceProg(ProgramacionTurnosSession serviceProg) {
        this.serviceProg = serviceProg;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public Date getIniDate() {
        return iniDate;
    }

    public void setIniDate(Date iniDate) {
        this.iniDate = iniDate;
    }

    public int getMoveValue() {
        return moveValue;
    }

    public void setMoveValue(int moveValue) {
        this.moveValue = moveValue;
    }

    public int getCorrimiento() {
        return corrimiento;
    }

    public void setCorrimiento(int corrimiento) {
        this.corrimiento = corrimiento;
    }

    public int[] getFestivos() {
        return festivos;
    }

    public void setFestivos(int[] festivos) {
        this.festivos = festivos;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public boolean isShowPrev() {
        return showPrev;
    }

    public void setShowPrev(boolean showPrev) {
        this.showPrev = showPrev;
    }
}