/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.DiarioPosicion;
import co.gov.aerocivil.controlt.entities.DiarioPosicionesIndividualVista;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.Notificacion;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.Transporte;
import co.gov.aerocivil.controlt.entities.Turno;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import co.gov.aerocivil.controlt.enums.ParametrosEnum;
import co.gov.aerocivil.controlt.enums.UnitTimeEnum;
import co.gov.aerocivil.controlt.services.ControlDiarioPosicionesService;
import co.gov.aerocivil.controlt.services.FuncionarioService;
import co.gov.aerocivil.controlt.services.ListasService;
import co.gov.aerocivil.controlt.services.TurnoService;
import co.gov.aerocivil.controlt.services.VistaProgramacionService;
import co.gov.aerocivil.controlt.to.ControlDiarioFuncionarioTO;
import co.gov.aerocivil.controlt.web.enums.Months;
import co.gov.aerocivil.controlt.web.lazylist.DiarioPosicionesLazyList;
import co.gov.aerocivil.controlt.web.lazylist.TransporteFuncionarioLazyList;
import co.gov.aerocivil.controlt.web.lazylist.TurnosProgEjecutadosLazyList;
import co.gov.aerocivil.controlt.web.util.DateUtil;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import co.gov.aerocivil.controlt.web.util.MailUtil;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import java.io.File;
import java.io.IOException;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.servlet.ServletContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ControlDiarioPosicionesBBean {

    private boolean diarioCerrado;

    public ControlDiarioPosicionesBBean() {
        /*ListDataModel obj = new ListDataModel();
         for (String s: obj.get){
         //System.out.println(s);
         }*/
    }
    @EJB
    private ControlDiarioPosicionesService service;
    @EJB
    private ListasService listasService;
    @EJB
    private FuncionarioService funcService;
    @EJB
    private TurnoService turnoService;
    private DiarioPosicion diario;
    private DiarioPosicion diarioFiltro;
    private Dependencia dependenciaFiltro;
    private LazyDataModel<DiarioPosicion> lazyList;
    private Jornada jornadaFiltro;
    private List<Jornada> jornadas;
    private List<Vistaprogramacion> listaTurnos;
    private ListDataModel<Vistaprogramacion> dataModel;
    public static final String TURNO_NORMAL = "TN";

    public ListDataModel<Vistaprogramacion> getDataModel() {
        return dataModel;
    }

    public void setDataModel(ListDataModel<Vistaprogramacion> dataModel) {
        this.dataModel = dataModel;
    }

    public String registrar() {
        precargarFiltros();
        jornadas = JsfUtil.getListadosBBean().getListaJornadaXDependencia(
                JsfUtil.getFuncionarioSesion().getDependencia().getDepId());
        jornadaFiltro = new Jornada();
        listaTurnos = null;
        return "registrarDiario";
    }

    public String consultar() {
        jornadaFiltro = null;
        precargarFiltros();
        return "consultarDiario";
    }

    public String generarReporteDiarioGeneral() {
        TurnosProgVsEjecutadosBBean turnosBBean =
                (TurnosProgVsEjecutadosBBean) JsfUtil.getManagedBean(TurnosProgVsEjecutadosBBean.class);
        turnosBBean.precargarFiltros();
        turnosBBean.getProgramacionFiltro().setDependencia(dependenciaFiltro);
        turnosBBean.getProgramacionFiltro().setProFechaInicio(diarioFiltro.getTurno().getTurFecha());
        turnosBBean.generarReporte();
        return null;
    }

    public String filtrar() {
        diarioCerrado = false;
        if (jornadaFiltro == null) {
            lazyList = new DiarioPosicionesLazyList(service, diarioFiltro);
            return "consultarDiario";
        } else {
            jornadaFiltro = (Jornada) JsfUtil.getListadosBBean().obtenerObjById(Jornada.class, jornadaFiltro.getJoId());
            diarioFiltro.getTurno().setJornadaId(jornadaFiltro.getJoId());
            listaTurnos = service.getListaTurnos(diarioFiltro, null, null, null, null);
            //System.out.println("SIZE" + listaTurnos.size());
            for (Vistaprogramacion vp : listaTurnos) {
                if (vp.getDiarioPosicion() == null) {
                    DiarioPosicion dp = new DiarioPosicion();
                    dp.setDposTipoRealizacion(TURNO_NORMAL);
                    dp.setDposHoraIngreso(jornadaFiltro.getJoHoraInicio());
                    dp.setDposMinutoIngreso(jornadaFiltro.getJoMinutoInicio());
                    dp.setDposHoraSalida(jornadaFiltro.getJoHoraFin());
                    dp.setDposMinutoSalida(jornadaFiltro.getJoMinutoFin());
                    dp.setTurno(vp);
                    vp.setDiarioPosicion(dp);
                } else {
                    diarioCerrado = vp.getDiarioPosicion().getDposCerrado() != null ? vp.getDiarioPosicion().getDposCerrado() : false;
                    //System.out.println("diarioCerrado:::"+diarioCerrado + " en: "+vp.getDiarioPosicion().getDposId());
                }
                if (vp.getDiarioPosicion().getFuncionario() == null) {
                    Funcionario f = new Funcionario();
                    f.setFunId(vp.getFunId());
                    f = funcService.getFuncionarioById(f);
                    vp.getDiarioPosicion().setFuncionario(f);
                    //vp.getDiarioPosicion().getFuncionario().setFunAlias(vp.getFunAlias());
                }
            }
            dataModel = new ListDataModel<Vistaprogramacion>(listaTurnos);
            return "registrarDiario";
        }
    }

    private void precargarFiltros() {
        FuncionarioBBean funcBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        funcBBean.inicializarPickList();
        dependenciaFiltro = funcBBean.getFuncionarioFiltro().getDependencia();
        Funcionario fun = new Funcionario();
        fun.setDependencia(dependenciaFiltro);
        diarioFiltro = new DiarioPosicion();
        diarioFiltro.setFuncionario(fun);
        diarioFiltro.setTurno(new Vistaprogramacion());
        dataModel = new ListDataModel<Vistaprogramacion>();
    }

    public void actualizar() {
        ////System.out.println(((Vistaprogramacion) event.getObject()).getTurId());
        //System.out.println(dataModel.getRowData() + "- " + dataModel.getRowData().getTurId());
        Vistaprogramacion vp = dataModel.getRowData();
        DiarioPosicion diario2 = vp.getDiarioPosicion();
        if (diario2.getFuncionario().getFunAlias() == null || "".equals(diario2.getFuncionario().getFunAlias().trim())) {
            diario2.setFuncionario(new Funcionario());
            JsfUtil.addWarningMessage("funcionarioAliasRequerido");
            return;
        }
        diario2.getFuncionario().setDependencia(JsfUtil.getFuncionarioSesion().getDependencia());
        Funcionario f = funcService.getFuncionarioById(diario2.getFuncionario());
        if (f == null) {
            JsfUtil.forceRefresh();
            diario2.setFuncionario(new Funcionario());
            JsfUtil.addWarningMessage("funcionarioNoEncontrado");
            return;
        }
        f = service.getFuncionarioByAlias(
                diario2.getFuncionario().getFunAlias(), dependenciaFiltro.getDepId(),
                diarioFiltro.getTurno().getTurFecha(), jornadaFiltro.getJoId(),
                vp.getFunId());
        if (f == null) {
            //JsfUtil.getFacesContext().addMessage(null, new FacesMessage("usuarioNoDisponible", "" ));

            //diario2 = new DiarioPosicion();
            diario2.setFuncionario(new Funcionario());
            diario2.setDposTipoRealizacion(TURNO_NORMAL);
            vp.setDiarioPosicion(diario2);
            JsfUtil.forceRefresh();
            JsfUtil.addWarningMessage("usuarioNoDisponible");
            return;
        }
        diario2.setFuncionario(f);
        diario2.setTurno((Vistaprogramacion) JsfUtil.getListadosBBean().obtenerObjById(Vistaprogramacion.class, vp.getTurId()));
        if (diario2.getDposFechaRegistroSalida() == null && diario2.getDposHoraSalida() != null && diario2.getDposMinutoSalida() != null) {
            diario2.setDposFechaRegistroSalida(new Date());
        }
        if (diario2.getDposFechaRegistroInicio() == null && diario2.getDposHoraIngreso() != null && diario2.getDposMinutoIngreso() != null) {
            diario2.setDposFechaRegistroInicio(new Date());
        }

        diario2.setDposRetardo(DateUtil.getTimeDiff(
                diario2.getDposHoraIngreso(), diario2.getDposMinutoIngreso(),
                vp.getTurHInicio(), vp.getTurMInicio(),
                UnitTimeEnum.MINUTES));

        diario2.setSupervisor(JsfUtil.getFuncionarioSesion());
        if ("HE".equals(diario2.getDposTipoRealizacion())) {
            Turno turno = new Turno();
            turno.setTurTurnoOriginal(vp.getTurId());
            turno.setTurTipo(10L);
            turno.setFuncionario(f);
            turno.setProgramacion(vp.getProgramacion());
            turno.setTurEstado(1L);
            turno.setTurFecha(vp.getTurFecha());
            turno.setTurHFin(vp.getTurHFin());
            turno.setTurHInicio(vp.getTurHInicio());
            turno.setTurMFin(vp.getTurMFin());
            turno.setTurMInicio(vp.getTurMInicio());
            turno.setTurPosicionJornada(vp.getTurPosicionJornada());
            turnoService.guardar(turno, JsfUtil.getFuncionarioSesion());
        }
        diario2 = service.guardar(diario2,
                JsfUtil.getFuncionarioSesion());
        if (diario2.getDposNotificacion() != null && diario2.getFuncionario().getFunCorreoElectronico() != null
                && !"".equals(diario2.getFuncionario().getFunCorreoElectronico().trim())) {
            MailUtil mailUtil = new MailUtil(
                    JsfUtil.getListadosBBean().getParametrosSistema().get(ParametrosEnum.mail_server.name()),
                    JsfUtil.getListadosBBean().getParametrosSistema().get(ParametrosEnum.mail_port.name()),
                    JsfUtil.getListadosBBean().getParametrosSistema().get(ParametrosEnum.mail_from.name()));
            //Cambiar condicion
            mailUtil.sendEmail(diario2.getFuncionario().getFunCorreoElectronico(),
                    "Notificación", "I".equals(diario2.getDposNotificacion()) ? "Incumplimiento" : "Retardo");
        }

        //service.flush(diario2);
        vp.setDiarioPosicion(diario2);
    }

    public void onCancel() {
        Vistaprogramacion vp = dataModel.getRowData();
        if (vp.getDiarioPosicion().getDposId() == null) {
            vp.setDiarioPosicion(new DiarioPosicion());
        }
        if (vp.getDiarioPosicion().getFuncionario().getFunId() == null) {
            vp.getDiarioPosicion().setFuncionario(new Funcionario());
        }

    }

    public void onSelect() {
        //System.out.println(dataModel.getRowData() + "- " + dataModel.getRowData().getTurId());
    }

    public String cerrarDiario() {
        List<DiarioPosicion> listaDPos = new ArrayList<DiarioPosicion>();

        //recorrer diarios y dposCerrado
        if (listaTurnos != null) {
            for (Vistaprogramacion vp : listaTurnos) {
                DiarioPosicion dp = vp.getDiarioPosicion();

                if (dp.getDposFechaRegistroSalida() == null && dp.getDposHoraSalida() != null && dp.getDposMinutoSalida() != null) {
                    dp.setDposFechaRegistroSalida(new Date());
                }
                if (dp.getDposFechaRegistroInicio() == null && dp.getDposHoraIngreso() != null && dp.getDposMinutoIngreso() != null) {
                    dp.setDposFechaRegistroInicio(new Date());
                }

                //dp.setDposCerrado(true);            
                /*service.guardar(dp,
                 JsfUtil.getFuncionarioSesion());*/

                if (dp.getFuncionario() != null && dp.getFuncionario().getFunId() != null) {
                    listaDPos.add(dp);
                } /*else {
                 diarioCerrado=false;
                 JsfUtil.addWarningMessage("noCierreControlDiario");
                 return null;
                 }*/
            }
        }
        diarioCerrado = true;
        for (DiarioPosicion dp : listaDPos) {
            dp.setDposCerrado(true);
            //if (dp.getDposId() != null) {
            service.guardar(dp,
                    JsfUtil.getFuncionarioSesion());
            //}
        }
        return "";
    }

    public DiarioPosicion getDiario() {
        return diario;
    }

    public void setDiario(DiarioPosicion diario) {
        this.diario = diario;
    }

    public DiarioPosicion getDiarioFiltro() {
        return diarioFiltro;
    }

    public void setDiarioFiltro(DiarioPosicion diarioFiltro) {
        this.diarioFiltro = diarioFiltro;
    }

    public Dependencia getDependenciaFiltro() {
        return dependenciaFiltro;
    }

    public void setDependenciaFiltro(Dependencia dependenciaFiltro) {
        this.dependenciaFiltro = dependenciaFiltro;
    }

    public LazyDataModel<DiarioPosicion> getLazyList() {
        return lazyList;
    }

    public void setLazyList(LazyDataModel<DiarioPosicion> lazyList) {
        this.lazyList = lazyList;
    }

    public Jornada getJornadaFiltro() {
        return jornadaFiltro;
    }

    public void setJornadaFiltro(Jornada jornadaFiltro) {
        this.jornadaFiltro = jornadaFiltro;
    }

    public List<Jornada> getJornadas() {
        return jornadas;
    }

    public void setJornadas(List<Jornada> jornadas) {
        this.jornadas = jornadas;
    }

    public List<Vistaprogramacion> getListaTurnos() {
        return listaTurnos;
    }

    public void setListaTurnos(List<Vistaprogramacion> listaTurnos) {
        this.listaTurnos = listaTurnos;
    }

    public boolean isDiarioCerrado() {
        return diarioCerrado;
    }
    private List<DiarioPosicionesIndividualVista> listaDiarioPosIndiv;
    private int mes;
    private Integer anio;

    public String consultarDiarioIndividual() {
        anio = Calendar.getInstance().get(Calendar.YEAR);
        precargarFiltros();
        return "consultarDiarioIndividual";
    }
    List<DiarioPosicionesIndividualVista[]> lista;
    List<ControlDiarioFuncionarioTO> listaReporte;

    public String filtrarDiarioIndividual() {
        lista = new ArrayList<DiarioPosicionesIndividualVista[]>();
        Funcionario f = ((FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class)).getFuncionarioFiltro();
        Calendar c = Calendar.getInstance();
        //The first month of the year in the Gregorian and Julian calendars is JANUARY which is 0; 
        c.set(Calendar.MONTH, mes);
        c.set(Calendar.YEAR, anio);
        Map<Integer, DiarioPosicionesIndividualVista[]> mapa = new HashMap<Integer, DiarioPosicionesIndividualVista[]>();
        for (int i = 1; i < 31; i++) {
            mapa.put(i, new DiarioPosicionesIndividualVista[2]);
        }
        listaDiarioPosIndiv = service.getListaDiarioPosicionesIndividual(f, DateUtil.getFirstDayMonth(c.getTime()),
                DateUtil.getLastDayMonth(c.getTime()));
        for (DiarioPosicionesIndividualVista dp : listaDiarioPosIndiv) {
            try {
                DiarioPosicionesIndividualVista[] dposArr = mapa.get(dp.getDia());
                dposArr[dp.getTurTipo() - 1] = dp;
                mapa.put(dp.getDia(), dposArr);
            } catch (Exception e) {
                mapa.put(dp.getDia(), new DiarioPosicionesIndividualVista[2]);
            }
        }
        for (int i = 1; i < 31; i++) {
            DiarioPosicionesIndividualVista[] arr = mapa.get(i);
            if (arr == null || arr[0] == null) {
                arr = new DiarioPosicionesIndividualVista[2];
                arr[0] = new DiarioPosicionesIndividualVista();
                arr[0].setDia(i);
            }
            lista.add(arr);
        }

        return "consultarDiarioIndividual";
    }

    public List<DiarioPosicionesIndividualVista> getListaDiarioPosIndiv() {
        return listaDiarioPosIndiv;
    }

    public void setListaDiarioPosIndiv(List<DiarioPosicionesIndividualVista> listaDiarioPosIndiv) {
        this.listaDiarioPosIndiv = listaDiarioPosIndiv;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public List<DiarioPosicionesIndividualVista[]> getLista() {
        return lista;
    }

    public void setLista(List<DiarioPosicionesIndividualVista[]> lista) {
        this.lista = lista;
    }

    public String exportPDF() {

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);

        java.util.HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, String> params = listasService.getParametrosSistema();

        Funcionario f = ((FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class)).getFuncionarioFiltro();
        f = funcService.getFuncionarioById(f);
        map.put("funcionario", f.getFunNombre());


        map.put("clave", logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria().getDcClaveDp());
        map.put("version", logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria().getDcVersionDp());
        map.put("fechaFormato", DateUtil.formatDate(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria().getDcFechaDp()));

        map.put("dependencia", f.getDependencia().getDepNombre());
        map.put("depAbrev", f.getDependencia().getDepAbreviatura());

        map.put("aeropuerto", f.getDependencia().getAeropuerto().getAeNombre());
        String periodo = Months.getMonth(this.mes).getLabel().toUpperCase() + " DEL " + anio;
        map.put("periodo", periodo);
        map.put("sigla", f.getFunAlias());
        map.put("jefe", funcService.getJefeDependencia(f.getDependencia().getDepId()).getFunNombre());
        map.put("cargoJefe", funcService.getJefeDependencia(f.getDependencia().getDepId()).getFunCargo());
        listaReporte = loadListaReporte();
        JsfUtil.generaReporte("ctrlDiarioPosicionesIndiv", map, listaReporte);
        return null;
    }

    private List<ControlDiarioFuncionarioTO> loadListaReporte() {
        listaReporte = new ArrayList<ControlDiarioFuncionarioTO>();
        for (DiarioPosicionesIndividualVista[] obj : lista) {
            ControlDiarioFuncionarioTO item = new ControlDiarioFuncionarioTO();
            /*if(obj[0]==null){
             obj[0] = new DiarioPosicionesIndividualVista();
             }*/
            if (obj[1] == null) {
                obj[1] = new DiarioPosicionesIndividualVista();
            }
            item.setJorOrdinaria(obj[0]);
            item.setJorExtra(obj[1]);
            listaReporte.add(item);
        }
        return listaReporte;

    }
}
