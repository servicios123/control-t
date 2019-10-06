/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.DiaFestivo;
import co.gov.aerocivil.controlt.services.ListasService;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ScheduleController {

    @EJB
    private ListasService listasService;
    private ScheduleModel eventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private List<Date> festivos;
    private Map<String, List<Date>> map;
    private List<Date> diasEliminar;

    public ScheduleController() {
        map = new HashMap<String, List<Date>>();
        festivos = new ArrayList<Date>();
        diasEliminar = new ArrayList<Date>();
        eventModel = new LazyScheduleModel() {
            @Override
            public void loadEvents(Date start, Date end) {
                Calendar c = Calendar.getInstance();
                c.setTime(start);
                //Esto es para garantizar que va a guardar en la clave el mes-año que se está visualizando
                c.add(Calendar.DATE, 10);
                String yearMonthKey = (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
                boolean setearFestivos = true;

                if (!modificando) {
                    if (!map.containsKey(yearMonthKey)) {

                        c.set(Calendar.DATE, 1);
                        start = c.getTime();

                        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);

                        c.set(Calendar.DATE, maxDay);
                        end = c.getTime();

                        List<DiaFestivo> lista = JsfUtil.getListadosBBean().getListaFestivos(start, end);
                        List<Date> listaDate = new ArrayList<Date>();
                        for (DiaFestivo d : lista) {
                            addEvent(new DefaultScheduleEvent("Festivo", d.getDfFecha(), d.getDfFecha(),true));
                            listaDate.add(d.getDfFecha());
                        }
                        //Se pone en el map la lista de festivos del mes actual
                        map.put(yearMonthKey, listaDate);
                        setearFestivos = false;

                    }
                    /*for (Date d:festivos){
                     addEvent(new DefaultScheduleEvent("Festivo", d, d));
                     }*/
                }
                if (setearFestivos) {
                    List<Date> listaDate = map.get(yearMonthKey);
                    for (Date d : listaDate) {
                        addEvent(new DefaultScheduleEvent("Festivo", d, d, true));
                    }
                }
                modificando = false;
            }
        };

        //lazyEventModel.
        /* {  
              
         @Override  
         public void fetchEvents(Date start, Date end) {  
         clear();  
                  
         Date random = Calendar.getInstance().getTime();  
         addEvent(new DefaultScheduleEvent("Lazy Event 1", random, random));  
                  
                
         }     
         };  */
    }
    private boolean modificando = false;
    /*public void addEvent(ActionEvent actionEvent) {  
     modificando = true;
     if(event.getId() == null) {
     eventModel.addEvent(event);
     }  
     else {
     eventModel.updateEvent(event);
     }  
          
     event = new DefaultScheduleEvent();  
     } */

    public void onEventSelect(SelectEvent selectEvent) {
        Calendar hoy = Calendar.getInstance();
        hoy.set(Calendar.HOUR_OF_DAY, 12);
        hoy.set(Calendar.MINUTE, 0);
        hoy.set(Calendar.SECOND, 0);
        hoy.set(Calendar.MILLISECOND, 0);
        Calendar evDate = Calendar.getInstance();
        evDate.setTime(((ScheduleEvent)selectEvent.getObject()).getStartDate());
        evDate.set(Calendar.HOUR_OF_DAY, 12);
        evDate.set(Calendar.MINUTE, 0);
        evDate.set(Calendar.SECOND, 0);
        evDate.set(Calendar.MILLISECOND, 0);
        evDate.add(Calendar.DATE, 1);
        //System.out.println(evDate.getTime());
        //System.out.println("hoy: " + hoy.getTime());
        //System.out.println("evDate.compareTo(hoy)<0:" + (evDate.compareTo(hoy) < 0));
        if (evDate.compareTo(hoy) < 0 || evDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return;
        }
        modificando = true;
        eventModel.deleteEvent((ScheduleEvent)selectEvent.getObject());

        Calendar c = Calendar.getInstance();
        c.setTime(((ScheduleEvent)selectEvent.getObject()).getStartDate());
        String yearMonthKey = (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
        map.get(yearMonthKey).remove(c.getTime());
        diasEliminar.add(c.getTime());
    }

    public void onDateSelect(SelectEvent selectEvent) {
        Calendar hoy = Calendar.getInstance();
        hoy.set(Calendar.HOUR_OF_DAY, 0);
        hoy.set(Calendar.MINUTE, 0);
        hoy.set(Calendar.SECOND, 0);
        hoy.set(Calendar.MILLISECOND, 0);

        Calendar evDate = Calendar.getInstance();
        evDate.setTime((Date)selectEvent.getObject());
        evDate.set(Calendar.HOUR_OF_DAY, 0);
        evDate.set(Calendar.MINUTE, 0);
        evDate.set(Calendar.SECOND, 0);
        evDate.set(Calendar.MILLISECOND, 0);

        //System.out.println(evDate.getTime());
        //System.out.println("hoy: " + hoy.getTime());

        if (evDate.before(hoy)) {
            return;
        }
        modificando = true;
        boolean assigned = false;
        for (ScheduleEvent ev : eventModel.getEvents()) {
            Date fecha = (Date) selectEvent.getObject();
            if (fecha.equals(ev.getStartDate())) {
                assigned = true;
                break;
            }
        }
        if (!assigned) {
            //festivos.add(selectEvent.getDate());
            /*event = new DefaultScheduleEvent("Festivo", selectEvent.getDate(), selectEvent.getDate());  
             eventModel.addEvent(event);*/

            Calendar c = Calendar.getInstance();
            c.setTime((Date)selectEvent.getObject());
            String yearMonthKey = (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
            map.get(yearMonthKey).add((Date)selectEvent.getObject());
        }

    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    //Getters and Setters  
    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public String guardarFestivos() {
        List<Date> festivosList = new ArrayList<Date>();
        //for (ScheduleEvent event : eventModel.getEvents()){

        for (Entry e : map.entrySet()) {
            ////System.out.println(e.getKey() + " " + e.getValue());
            festivosList.addAll((List<Date>) e.getValue());
        }
        listasService.guardarFestivos(festivosList, diasEliminar, 
                JsfUtil.getFuncionarioSesion());
        diasEliminar = new ArrayList<Date>();
        return "editFestivos";
    }
    
    public String listarFestivos(){
        return "editFestivos";
    }

}