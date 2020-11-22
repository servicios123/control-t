/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.PosNoAsig;
import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import co.gov.aerocivil.controlt.services.ModificarTurnoService;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Viviana
 */
@ManagedBean
@SessionScoped
public class VistaProgramacionBBean {

    @EJB
    private ModificarTurnoService modificarTurnoServiceBean;
    private Date mes_actual, date_cambio_1, date_asignar, date_anular, date_asignar_esp, date_anular_ord;
    private Funcionario fun_cambio_1, fun_cambio_2, fun_asignar, fun_anular, fun_asignar_esp, fun_anular_ord;
    private List<Funcionario> list_fun_cambio_1, list_fun_cambio_2, list_fun_asignar, list_fun_anular, list_fun_asignar_esp, list_fun_anular_ord;
    private Vistaprogramacion turn_cambio_1, turn_cambio_2, turn_anular, turn_anular_ord;
    private List<Vistaprogramacion> list_turn_cambio_1, list_turn_cambio_2, list_turn_anular, list_turn_anular_ord;
    private PosNoAsig turn_asignar;
    private PosNoAsig turn_asignar_no_asig;
    private List<PosNoAsig> list_turn_asignar;
    private TurnoEspecial turn_asignar_esp;
    private List<TurnoEspecial> list_turn_asignar_esp;

    public VistaProgramacionBBean() {
    }

    public String listar() {

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);

        fun_cambio_1 = null;
        fun_cambio_2 = null;
        fun_asignar = null;
        fun_anular = null;
        fun_asignar_esp = null;
        turn_asignar = null;
        turn_asignar_esp = null;
        turn_cambio_1 = null;
        turn_cambio_2 = null;
        turn_anular = null;


        list_fun_cambio_1 = null;
        list_fun_cambio_2 = modificarTurnoServiceBean.getFuncionarios(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
        //list_fun_asignar= modificarTurnoServiceBean.getFuncionarios(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
        list_fun_asignar = new ArrayList<Funcionario>();
        list_fun_asignar_esp = new ArrayList<Funcionario>();
        list_fun_anular = null;
        list_fun_anular_ord = null;
        list_turn_asignar = null;
        list_turn_cambio_1 = null;
        list_turn_cambio_2 = null;
        list_turn_anular = null;
        list_turn_anular_ord = null;
        list_turn_asignar_esp = modificarTurnoServiceBean.getTurnoEspecial(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());

        date_cambio_1 = null;
        turn_anular_ord = null;
        date_asignar = null;
        date_anular = null;
        date_anular_ord = null;
        fun_anular_ord = null;
        date_asignar_esp = null;
        
        if(turn_asignar_no_asig != null){
            date_asignar = turn_asignar_no_asig.getFecha();
            onDateAsignar(null);
            turn_asignar = turn_asignar_no_asig;
            onTurnoChange();
        }

        return init();
    }

    public String init() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);
        setMes_actual(c.getTime());
        return "modificarTurno";
    }

    public String cambiar() {
        String resultado = modificarTurnoServiceBean.cambiarTurnos(date_cambio_1, fun_cambio_1, turn_cambio_1, date_cambio_1, fun_cambio_2, turn_cambio_2, JsfUtil.getFuncionarioSesion());

        if (resultado == null) {
            JsfUtil.addSuccessMessage("turnosCambiadosExitosamente");
            return listar();

        } else {
            JsfUtil.addManualWarningMessage(resultado);
            return init();
        }

    }

    public String asignar() {
        String resultado = modificarTurnoServiceBean.asignarPosNoAsig(turn_asignar, date_asignar, fun_asignar, JsfUtil.getFuncionarioSesion());
        if (resultado == null) {
            JsfUtil.addSuccessMessage("posicionAsignada");
            return listar();
        } else {
            JsfUtil.addManualWarningMessage(resultado);
            return init();
        }

    }

    public String anular() {
        if (modificarTurnoServiceBean.anularTurno(turn_anular, JsfUtil.getFuncionarioSesion())) {
            JsfUtil.addSuccessMessage("turnoAnulacionExitosamente");
            return listar();
        } else {
            JsfUtil.addWarningMessage("turnoAnulacionFallida");
            return init();
        }

    }

    public String asignarEspecial() {
        String resultado = modificarTurnoServiceBean.asignarEspecial(date_asignar_esp, fun_asignar_esp, turn_asignar_esp, JsfUtil.getFuncionarioSesion());
        if (resultado == null) {
            JsfUtil.addSuccessMessage("turnoEspecialAsignado");
            return listar();

        } else {
            JsfUtil.addManualWarningMessage(resultado);
            return init();
        }


    }

    public void onTurnoChange() {
        list_fun_asignar = JsfUtil.getListadosBBean().obtenerFuncionariosXPosicionXFecha(turn_asignar.getPosicionJornada().getPjId(), turn_asignar.getFecha());
        //System.out.println("event = " + turn_asignar.toString());
    }

    public void onDateCambio1(SelectEvent ev) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        fun_cambio_1 = null;
        list_turn_cambio_1 = null;
        turn_cambio_1 = null;
        list_turn_cambio_2 = null;
        fun_cambio_2 = null;
        turn_cambio_2 = null;
        list_fun_cambio_1 = modificarTurnoServiceBean.getFuncionarioTurnoPorFecha((Date) ev.getObject(), logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
        //System.out.println("Entra a onDateCambio1 \t N. "+list_fun_cambio_1.size());
    }

    public void onDateCambio4(SelectEvent ev) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        list_fun_asignar_esp = modificarTurnoServiceBean.getFunctionariesAvaibleSpecial((Date) ev.getObject(), JsfUtil.getFuncionarioSesion().getDependencia());
        //System.out.println("Entra a onDateCambio1 \t N. "+list_fun_asignar.size());
    }

    public void onDateCambio2(SelectEvent ev) {
        list_turn_cambio_2 = null;
        fun_cambio_2 = null;
        turn_cambio_2 = null;
    }

    public void onDateAnularOrdinary(SelectEvent ev) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        fun_anular = null;
        list_fun_anular_ord = null;
        turn_anular = null;
        list_fun_anular_ord = modificarTurnoServiceBean.getFuncionarioTurnoPorFecha((Date) ev.getObject(), logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
        //System.out.println("Entra a onDateAnular \t N. "+list_fun_anular.size());
    }

    public void onDateAsignar(SelectEvent ev) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        turn_asignar = new PosNoAsig();
        Date fechaNoAsig;
        if(ev!=null){
            fechaNoAsig = (Date) ev.getObject();
        }else{
            fechaNoAsig = turn_asignar_no_asig.getFecha();
        }
        list_turn_asignar = modificarTurnoServiceBean.getPosNoAsigPorFecha(fechaNoAsig, logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
        //System.out.println("Entra a onDateAsignar \t N. "+list_turn_asignar.size());
    }

    public void onDateAnular(SelectEvent ev) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        fun_anular = null;
        list_turn_anular = null;
        turn_anular = null;
        list_fun_anular = modificarTurnoServiceBean.getFuncionarioTurnoEspecialPorFecha((Date) ev.getObject(), logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
        //System.out.println("Entra a onDateAnular \t N. "+list_fun_anular.size());
    }

    public void onFunCambio1(final AjaxBehaviorEvent ev) {
        turn_cambio_1 = null;
        Calendar c = Calendar.getInstance();
        c.setTime(date_cambio_1);
        //System.out.println("Fun onFunCambio1 \t "+fun_cambio_1.getFunId()+"\t"+c.get(Calendar.DATE)+"/"+c.get(Calendar.MONTH));

        list_turn_cambio_1 = modificarTurnoServiceBean.getTurnoPorFunFecha(date_cambio_1, fun_cambio_1.getFunId(), false);
        //System.out.println("Entra a onFunCambio1 \t N. "+list_turn_cambio_1.size());
    }

    public void onFunCambio2(final AjaxBehaviorEvent ev) {
        turn_cambio_2 = null;
        Calendar c = Calendar.getInstance();
        c.setTime(date_cambio_1);
        //System.out.println("Fun onFunCambio2 \t "+fun_cambio_2.getFunId()+"\t"+c.get(Calendar.DATE)+"/"+c.get(Calendar.MONTH));        

        list_turn_cambio_2 = modificarTurnoServiceBean.getTurnoPorFunFecha(date_cambio_1, fun_cambio_2.getFunId(), false);
        //System.out.println("Entra a onFunCambio2 \t N. "+list_turn_cambio_2.size());
    }

    public void onFunAnular(final AjaxBehaviorEvent ev) {
        turn_anular = null;
        Calendar c = Calendar.getInstance();
        c.setTime(date_anular);
        //System.out.println("Fun onFunCambio2 \t "+fun_anular.getFunId()+"\t"+c.get(Calendar.DATE)+"/"+c.get(Calendar.MONTH));        
        list_turn_anular = modificarTurnoServiceBean.getTurnoPorFunFecha(date_anular, fun_anular.getFunId(), true);
        //System.out.println("Entra a onFunAnular \t N. "+list_turn_anular.size());
    }

    public void onFunAnularOrd(final AjaxBehaviorEvent ev) {
        turn_anular = null;
        Calendar c = Calendar.getInstance();
        c.setTime(date_anular_ord);
        //System.out.println("Fun onFunCambio2 \t "+fun_anular.getFunId()+"\t"+c.get(Calendar.DATE)+"/"+c.get(Calendar.MONTH));        
        list_turn_anular_ord = modificarTurnoServiceBean.getTurnoPorFunFecha(date_anular_ord, fun_anular_ord.getFunId(), false);
        //System.out.println("Entra a onFunAnular \t N. "+list_turn_anular.size());
    }

    public String listarEspeciales() {
        return listar();
    }

    public Date getMes_actual() {
        return mes_actual;
    }

    public Date getMes_actualActivo() {
        Calendar c = Calendar.getInstance();
        c.setTime(mes_actual);
        //c.add(Calendar.MONTH, -2);
        return c.getTime();
    }

    public void setMes_actual(Date mes_actual) {
        this.mes_actual = mes_actual;
    }

    /**
     * @return the list_fun_cambio_1
     */
    public List<Funcionario> getList_fun_cambio_1() {
        return list_fun_cambio_1;
    }

    /**
     * @param list_fun_cambio_1 the list_fun_cambio_1 to set
     */
    public void setList_fun_cambio_1(List<Funcionario> list_fun_cambio_1) {
        this.setList_fun_cambio_1(list_fun_cambio_1);
    }

    /**
     * @return the date_cambio_1
     */
    public Date getDate_cambio_1() {
        return date_cambio_1;
    }

    /**
     * @param date_cambio_1 the date_cambio_1 to set
     */
    public void setDate_cambio_1(Date date_cambio_1) {
        this.date_cambio_1 = date_cambio_1;
    }

    /**
     * @return the date_asignar
     */
    public Date getDate_asignar() {
        return date_asignar;
    }

    /**
     * @param date_asignar the date_asignar to set
     */
    public void setDate_asignar(Date date_asignar) {
        this.date_asignar = date_asignar;
    }

    /**
     * @return the date_anular
     */
    public Date getDate_anular() {
        return date_anular;
    }

    /**
     * @param date_anular the date_anular to set
     */
    public void setDate_anular(Date date_anular) {
        this.date_anular = date_anular;
    }

    /**
     * @return the date_asignar_esp
     */
    public Date getDate_asignar_esp() {
        return date_asignar_esp;
    }

    /**
     * @param date_asignar_esp the date_asignar_esp to set
     */
    public void setDate_asignar_esp(Date date_asignar_esp) {
        this.date_asignar_esp = date_asignar_esp;
    }

    /**
     * @return the list_fun_asignar
     */
    public List<Funcionario> getList_fun_asignar() {
        return list_fun_asignar;
    }

    /**
     * @param list_fun_asignar the list_fun_asignar to set
     */
    public void setList_fun_asignar(List<Funcionario> list_fun_asignar) {
        this.setList_fun_asignar(list_fun_asignar);
    }

    /**
     * @return the list_fun_cambio_2
     */
    public List<Funcionario> getList_fun_cambio_2() {
        return list_fun_cambio_2;
    }

    /**
     * @param list_fun_cambio_2 the list_fun_cambio_2 to set
     */
    public void setList_fun_cambio_2(List<Funcionario> list_fun_cambio_2) {
        this.list_fun_cambio_2 = list_fun_cambio_2;
    }

    /**
     * @return the list_fun_anular
     */
    public List<Funcionario> getList_fun_anular() {
        return list_fun_anular;
    }

    /**
     * @param list_fun_anular the list_fun_anular to set
     */
    public void setList_fun_anular(List<Funcionario> list_fun_anular) {
        this.list_fun_anular = list_fun_anular;
    }

    /**
     * @return the list_fun_asignar_esp
     */
    public List<Funcionario> getList_fun_asignar_esp() {
        return list_fun_asignar_esp;
    }

    /**
     * @param list_fun_asignar_esp the list_fun_asignar_esp to set
     */
    public void setList_fun_asignar_esp(List<Funcionario> list_fun_anular_esp) {
        this.list_fun_asignar_esp = list_fun_anular_esp;
    }

    /**
     * @return the fun_cambio_1
     */
    public Funcionario getFun_cambio_1() {
        return fun_cambio_1;
    }

    /**
     * @param fun_cambio_1 the fun_cambio_1 to set
     */
    public void setFun_cambio_1(Funcionario fun_cambio_1) {
        this.fun_cambio_1 = fun_cambio_1;
    }

    /**
     * @return the fun_cambio_2
     */
    public Funcionario getFun_cambio_2() {
        return fun_cambio_2;
    }

    /**
     * @param fun_cambio_2 the fun_cambio_2 to set
     */
    public void setFun_cambio_2(Funcionario fun_cambio_2) {
        this.fun_cambio_2 = fun_cambio_2;
    }

    /**
     * @return the fun_asignar
     */
    public Funcionario getFun_asignar() {
        return fun_asignar;
    }

    /**
     * @param fun_asignar the fun_asignar to set
     */
    public void setFun_asignar(Funcionario fun_asignar) {
        this.fun_asignar = fun_asignar;
    }

    /**
     * @return the fun_anular
     */
    public Funcionario getFun_anular() {
        return fun_anular;
    }

    /**
     * @param fun_anular the fun_anular to set
     */
    public void setFun_anular(Funcionario fun_anular) {
        this.fun_anular = fun_anular;
    }

    /**
     * @return the fun_asignar_esp
     */
    public Funcionario getFun_asignar_esp() {
        return fun_asignar_esp;
    }

    /**
     * @param fun_asignar_esp the fun_asignar_esp to set
     */
    public void setFun_asignar_esp(Funcionario fun_asignar_esp) {
        this.fun_asignar_esp = fun_asignar_esp;
    }

    /**
     * @return the turn_asignar
     */
    public PosNoAsig getTurn_asignar() {
        return turn_asignar;
    }

    /**
     * @param turn_asignar the turn_asignar to set
     */
    public void setTurn_asignar(PosNoAsig turn_asignar) {
        this.turn_asignar = turn_asignar;
    }

    /**
     * @return the list_turn_asignar
     */
    public List<PosNoAsig> getList_turn_asignar() {
        return list_turn_asignar;
    }

    /**
     * @param list_turn_asignar the list_turn_asignar to set
     */
    public void setList_turn_asignar(List<PosNoAsig> list_turn_asignar) {
        this.list_turn_asignar = list_turn_asignar;
    }

    /**
     * @return the turn_asignar_esp
     */
    public TurnoEspecial getTurn_asignar_esp() {
        return turn_asignar_esp;
    }

    /**
     * @param turn_asignar_esp the turn_asignar_esp to set
     */
    public void setTurn_asignar_esp(TurnoEspecial turn_asignar_esp) {
        this.turn_asignar_esp = turn_asignar_esp;
    }

    /**
     * @return the list_turn_asignar_esp
     */
    public List<TurnoEspecial> getList_turn_asignar_esp() {
        return list_turn_asignar_esp;
    }

    /**
     * @param list_turn_asignar_esp the list_turn_asignar_esp to set
     */
    public void setList_turn_asignar_esp(List<TurnoEspecial> list_turn_asignar_esp) {
        this.list_turn_asignar_esp = list_turn_asignar_esp;
    }

    /**
     * @return the turn_cambio_1
     */
    public Vistaprogramacion getTurn_cambio_1() {
        return turn_cambio_1;
    }

    /**
     * @param turn_cambio_1 the turn_cambio_1 to set
     */
    public void setTurn_cambio_1(Vistaprogramacion turn_cambio_1) {
        this.turn_cambio_1 = turn_cambio_1;
    }

    /**
     * @return the turn_cambio_2
     */
    public Vistaprogramacion getTurn_cambio_2() {
        return turn_cambio_2;
    }

    /**
     * @param turn_cambio_2 the turn_cambio_2 to set
     */
    public void setTurn_cambio_2(Vistaprogramacion turn_cambio_2) {
        this.turn_cambio_2 = turn_cambio_2;
    }

    /**
     * @return the turn_anular
     */
    public Vistaprogramacion getTurn_anular() {
        return turn_anular;
    }

    /**
     * @param turn_anular the turn_anular to set
     */
    public void setTurn_anular(Vistaprogramacion turn_anular) {
        this.turn_anular = turn_anular;
    }

    /**
     * @return the list_turn_cambio_1
     */
    public List<Vistaprogramacion> getList_turn_cambio_1() {
        return list_turn_cambio_1;
    }

    /**
     * @param list_turn_cambio_1 the list_turn_cambio_1 to set
     */
    public void setList_turn_cambio_1(List<Vistaprogramacion> list_turn_cambio_1) {
        this.list_turn_cambio_1 = list_turn_cambio_1;
    }

    /**
     * @return the list_turn_cambio_2
     */
    public List<Vistaprogramacion> getList_turn_cambio_2() {
        return list_turn_cambio_2;
    }

    /**
     * @param list_turn_cambio_2 the list_turn_cambio_2 to set
     */
    public void setList_turn_cambio_2(List<Vistaprogramacion> list_turn_cambio_2) {
        this.list_turn_cambio_2 = list_turn_cambio_2;
    }

    /**
     * @return the list_turn_anular
     */
    public List<Vistaprogramacion> getList_turn_anular() {
        return list_turn_anular;
    }

    public Funcionario getFun_anular_ord() {
        return fun_anular_ord;
    }

    public void setFun_anular_ord(Funcionario fun_anular_ord) {
        this.fun_anular_ord = fun_anular_ord;
    }

    public List<Funcionario> getList_fun_anular_ord() {
        return list_fun_anular_ord;
    }

    public void setList_fun_anular_ord(List<Funcionario> list_fun_anular_ord) {
        this.list_fun_anular_ord = list_fun_anular_ord;
    }

    /**
     * @param list_turn_anular the list_turn_anular to set
     */
    public void setList_turn_anular(List<Vistaprogramacion> list_turn_anular) {
        this.list_turn_anular = list_turn_anular;
    }

    public Date getDate_anular_ord() {
        return date_anular_ord;
    }

    public void setDate_anular_ord(Date date_anular_ord) {
        this.date_anular_ord = date_anular_ord;
    }

    public Vistaprogramacion getTurn_anular_ord() {
        return turn_anular_ord;
    }

    public void setTurn_anular_ord(Vistaprogramacion turn_anular_ord) {
        this.turn_anular_ord = turn_anular_ord;
    }

    public List<Vistaprogramacion> getList_turn_anular_ord() {
        return list_turn_anular_ord;
    }

    public void setList_turn_anular_ord(List<Vistaprogramacion> list_turn_anular_ord) {
        this.list_turn_anular_ord = list_turn_anular_ord;
    }

    public PosNoAsig getTurn_asignar_no_asig() {
        return turn_asignar_no_asig;
    }

    public void setTurn_asignar_no_asig(PosNoAsig turn_asignar_no_asig) {
        this.turn_asignar_no_asig = turn_asignar_no_asig;
    }
}