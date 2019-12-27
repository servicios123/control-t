/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.DiaFestivo;
import co.gov.aerocivil.controlt.entities.PosicionInactiva;
import co.gov.aerocivil.controlt.entities.PosicionJornada;
import co.gov.aerocivil.controlt.enums.EstadoProgramacion;
import co.gov.aerocivil.controlt.services.PosicionInactivaService;
import co.gov.aerocivil.controlt.services.ProgramacionTurnosSession;
import co.gov.aerocivil.controlt.web.util.DateUtil;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.component.schedule.Schedule;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class PosicionInactivaBBean {

    @EJB
    private PosicionInactivaService service;
    @EJB
    private ProgramacionTurnosSession serviceProg;
    private ScheduleModel eventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    List<PosicionInactiva> posInactivas;
    List<PosicionJornada> posJornadas;
    private Integer[] arrayInactivas;
    private String titlePopUp;
    private Map<String, Integer[]> mapa;
    private Map<String, Integer[]> mapaToSave;
    Dependencia dependencia = JsfUtil.getFuncionarioSesion().getDependencia();
    int cantidadPos;
    private boolean modificando;
    //private List<DiaFestivo> listaDiaFestivo;
    private int corrimiento;
    private String jqueryScript;
    private int[] festivos;
    private Date iniDate;
    int moveValue;

    public Date getIniDate() {
        return iniDate;
    }

    public String getJqueryScript() {
        Schedule sc = new Schedule();
        for (String s : sc.getEventNames()) {
            //System.out.println(s);
        }
        return jqueryScript;
    }

    public int[] getFestivos() {
        return festivos;
    }

    public String listarCopia() {
        posJornadas = JsfUtil.getListadosBBean().getListaPosicionJornadaXDependencia(dependencia.getDepId());
        //eventModel = new DefaultScheduleModel();

        eventModel = new LazyScheduleModel() {
            @Override
            public void loadEvents(Date start, Date end) {
                /*boolean setearFestivos = true;
                 if (!modificando) {
                 if (!mapa.containsKey(getKey(start))) {*/
                //setearFestivos=false;
                Date start2 = getFirstDayMonth(start);
                corrimiento = DateUtil.daysBetween(start2, start).intValue();

                end = getLastDayMonth(start2);
                posInactivas = service.getLista(dependencia, start2, end);
                cargarFestivos(start2, end);



                cantidadPos = posJornadas.size();

                mapa = new HashMap<String, Integer[]>();
                mapaToSave = new HashMap<String, Integer[]>();
                //List<PosicionInactiva> listaDate = new ArrayList<PosicionInactiva>();
//ELIMINAR?????????????
                Integer[] arrayInactivas = new Integer[cantidadPos];
                int prevDay = 0;
                Date prevDate = null;
                StringBuilder sb = new StringBuilder();
                int i = 0;
                int c = 0;
                for (PosicionInactiva posIn : posInactivas) {
                    if (prevDate == null) {
                        prevDate = posIn.getFecha();
                    }
                    String yearMonthKey = DateUtil.formatDate(prevDate);
                    //if (!mapa.containsKey(yearMonthKey)) {
                    if (prevDay != posIn.getFecha().getDate()) {
                        i = 0;
                        if (prevDay > 0) {
                            String s = sb.toString();
                            eventModel.addEvent(new DefaultScheduleEvent(s.substring(0, s.length() - 2),
                                    prevDate, prevDate, true));
                            //si no existe la clave en el mapa la crea
                            mapa.put(yearMonthKey, arrayInactivas);
                        }
                        sb = new StringBuilder();
                        arrayInactivas = new Integer[cantidadPos];
                        prevDay = posIn.getFecha().getDate();
                        prevDate = posIn.getFecha();
                    }
                    arrayInactivas[i++] = posIn.getPosicionJornada().getPjId().intValue();
                    sb.append(posIn.getPosicionJornada().getPjAlias()).append(", ");
                    c++;
                    if (c == posInactivas.size()) {
                        eventModel.addEvent(new DefaultScheduleEvent(sb.toString(),
                                posIn.getFecha(), posIn.getFecha(), true));
                        mapa.put(DateUtil.formatDate(posIn.getFecha()), arrayInactivas);
                    }
                }

                //si no existe la clave en el mapa la crea
                //mapa.put(getKey(prevDate), arrayInactivas);
                    /*}
                 }
                 if (setearFestivos) {
                 for (Map.Entry e : mapa.entrySet()) {
                 Date d = convertToDate(e.getKey().toString());
                 addEvent(new DefaultScheduleEvent(e.getValue().toString(), d, d, true));
                 }
                 }
                 modificando = false;*/
                JsfUtil.forceRefresh();
            }
        };
        return "listPosicionesInactivas";
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

    private Date getFirstDayMonth(Date start) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        //Esto es para garantizar que va a guardar en la clave el mes-año que se está visualizando
        c.add(Calendar.DATE, 10);

        c.set(Calendar.DATE, 1);
        c = DateUtil.setCeroHoras(c);
        return c.getTime();
    }

    private Date getLastDayMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        //Esto es para garantizar que va a guardar en la clave el mes-año que se está visualizando
        c.set(Calendar.DATE, maxDay);
        return c.getTime();

    }

    public List<PosicionJornada> getPosJornadas() {
        return posJornadas;
    }

    public void setPosJornadas(List<PosicionJornada> posJornadas) {
        this.posJornadas = posJornadas;
    }

    public void onEventSelect(SelectEvent selectEvent) {
        Calendar hoy = DateUtil.setCeroHoras(Calendar.getInstance());
        hoy.set(Calendar.HOUR_OF_DAY, 12);

        Calendar evDate = Calendar.getInstance();
        evDate.setTime(((ScheduleEvent) selectEvent.getObject()).getStartDate());
        evDate = DateUtil.setCeroHoras(evDate);
        evDate.set(Calendar.HOUR_OF_DAY, 12);

        //System.out.println(evDate.getTime());
        //System.out.println("hoy: " + hoy.getTime());
        //System.out.println("evDate.compareTo(hoy)<0:" + (evDate.compareTo(hoy) < 0));
        if (evDate.compareTo(hoy) < 0
                || serviceProg.isFechaProgramada(evDate.getTime(), dependencia.getDepId(), EstadoProgramacion.PROGRAMADA)) {
            return;
        }
        setPopupVisible(true);
        titlePopUp = DateUtil.formatDate(evDate.getTime());
        //popupVisible = true;
        arrayInactivas = mapa.get(titlePopUp);
        event = ((ScheduleEvent) selectEvent.getObject());
    }

    public void onDateSelect(SelectEvent selectEvent) {
        Calendar seleccionada = Calendar.getInstance();
        seleccionada.setTime((Date) selectEvent.getObject());
        seleccionada = DateUtil.setCeroHoras(seleccionada);

        Calendar evDate = Calendar.getInstance();
        evDate.setTime((Date) selectEvent.getObject());
        evDate = DateUtil.setCeroHoras(evDate);
        /*
         if (evDate.before(hoy)
         || serviceProg.isFechaProgramada(evDate.getTime(), dependencia.getDepId(), EstadoProgramacion.PROGRAMADA)) {            
         return;
         }*/
        setPopupVisible(true);
        boolean assigned = false;

        titlePopUp = DateUtil.formatDate(seleccionada.getTime());

        for (ScheduleEvent ev : eventModel.getEvents()) {
            //System.out.println("entra");
            Calendar c = Calendar.getInstance();
            c.setTime(ev.getStartDate());
            c = DateUtil.setCeroHoras(c);

            if (evDate.getTime().equals(c.getTime())) {
                event = ev;


                arrayInactivas = mapa.get(titlePopUp);
                assigned = true;
                //System.out.println("exit");
                break;
            }
        }
        if (!assigned) {
            //popupVisible = true;
            event = new DefaultScheduleEvent("Festivo", evDate.getTime(), evDate.getTime());
            arrayInactivas = new Integer[this.cantidadPos];
        }
    }

    public Integer[] getArrayInactivas() {
        return arrayInactivas;
    }

    public void setArrayInactivas(Integer[] arrayInactivas) {
        this.arrayInactivas = arrayInactivas;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public String registrarPosiciones() {
        Calendar hoy = DateUtil.setCeroHoras(Calendar.getInstance());

        if (event.getId() == null) {
            eventModel.addEvent(event);
        } else {
            eventModel.updateEvent(event);
        }
        String key = DateUtil.formatDate(event.getStartDate());
        //System.out.println("key: " + key);
        mapa.put(key, arrayInactivas);
        mapaToSave = new HashMap<String, Integer[]>();
        mapaToSave.put(key, arrayInactivas);
        service.guardarPosiciones(mapaToSave, JsfUtil.getFuncionarioSesion());
        arrayInactivas = new Integer[this.cantidadPos];
        modificando = true;

        moveValue = 0;
        nextPrevious();
        if (serviceProg.isFechaProgramada(event.getStartDate(), dependencia.getDepId(), EstadoProgramacion.GENERADA)) {
            JsfUtil.addWarningMessage("warningProgramacionPendiente");
            JsfUtil.forceRefresh();
            //System.out.println("Programación pendiente");
            return null;
        }
        return "listPosicionesInactivas";
    }

    public String guardar() {
        service.guardarPosiciones(mapaToSave, JsfUtil.getFuncionarioSesion());
        return null;
    }

    private Date convertToDate(String toString) {
        Calendar c = Calendar.getInstance();
        String[] arrDate = toString.split("/");
        c.set(Integer.parseInt(arrDate[2]),
                Integer.parseInt(arrDate[1]) - 1,
                Integer.parseInt(arrDate[0]));
        return c.getTime();
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public String getTitlePopUp() {
        return titlePopUp;
    }

    public void setTitlePopUp(String titlePopUp) {
        this.titlePopUp = titlePopUp;
    }

    private void setPopupVisible(boolean visible) {
        if (visible) {
            JsfUtil.getRequestContext().execute("eventDialog.show()");
        } else {
            JsfUtil.getRequestContext().execute("eventDialog.hide()");
        }
    }

    public int getMoveValue() {
        return moveValue;
    }

    public void setMoveValue(int moveValue) {
        this.moveValue = moveValue;
    }

    public String nextPrevious() {
        eventModel = new DefaultScheduleModel();
        iniDate = DateUtil.addMonth(iniDate, moveValue);
        loadEvents(iniDate, iniDate);

        return "listPosicionesInactivas";
    }

    public String listar() {
        posJornadas = JsfUtil.getListadosBBean().getListaPosicionJornadaXDependencia(dependencia.getDepId());
        eventModel = new DefaultScheduleModel();
        Calendar cal = Calendar.getInstance();
        cal.setTime(getFirstDayMonth(cal.getTime()));
        iniDate = cal.getTime();
        //System.out.println("listar():iniDate=> "+iniDate);
        loadEvents(iniDate, iniDate);
        return "listPosicionesInactivas";
    }

    public void loadEvents(Date start, Date end) {

        Date start2 = getFirstDayMonth(start);
        iniDate = start2;
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(start2);
            corrimiento = cal.get(Calendar.DAY_OF_WEEK) - 1;
        }

        end = getLastDayMonth(start2);
        Calendar end2 = Calendar.getInstance();
        end2.setTime(end);
        //end2.add(Calendar.DATE, 1);

        posInactivas = service.getLista(dependencia, start2, end2.getTime());
        cargarFestivos(start2, end);

        cantidadPos = posJornadas.size();

        mapa = new HashMap<String, Integer[]>();
        mapaToSave = new HashMap<String, Integer[]>();
//ELIMINAR?????????????
        Integer[] arrayInactivasAux = new Integer[cantidadPos];
        int prevDay = 0;
        Date prevDate = null;
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int c = 0;
        for (PosicionInactiva posIn : posInactivas) {
            if (prevDate == null) {
                prevDate = posIn.getFecha();
            }
            String yearMonthKey = DateUtil.formatDate(prevDate);
            //if (!mapa.containsKey(yearMonthKey)) {
            if (prevDay != posIn.getFecha().getDate()) {
                i = 0;
                if (prevDay > 0) {
                    String s = sb.toString();
                    eventModel.addEvent(new DefaultScheduleEvent(s.substring(0, s.length() - 2),
                            prevDate, prevDate, true));
                    //si no existe la clave en el mapa la crea
                    mapa.put(yearMonthKey, arrayInactivasAux);
                }
                sb = new StringBuilder();
                arrayInactivasAux = new Integer[cantidadPos];
                prevDay = posIn.getFecha().getDate();
                prevDate = posIn.getFecha();
            }
            arrayInactivasAux[i++] = posIn.getPosicionJornada().getPjId().intValue();
            sb.append(posIn.getPosicionJornada().getPjAlias()).append(", ");
            c++;
            if (c == posInactivas.size()) {
                eventModel.addEvent(new DefaultScheduleEvent(sb.toString(),
                        posIn.getFecha(), posIn.getFecha(), true));
                mapa.put(DateUtil.formatDate(posIn.getFecha()), arrayInactivasAux);
            }
        }
    }
}