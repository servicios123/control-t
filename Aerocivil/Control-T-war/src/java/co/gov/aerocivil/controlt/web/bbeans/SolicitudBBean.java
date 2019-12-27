/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.PosicionJornada;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.entities.Solicitud;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import co.gov.aerocivil.controlt.enums.ParametrosEnum;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.FuncionarioService;
import co.gov.aerocivil.controlt.services.ModificarTurnoService;
import co.gov.aerocivil.controlt.services.ProgramacionTurnosSession;
import co.gov.aerocivil.controlt.services.RestriccionesService;
import co.gov.aerocivil.controlt.services.SolicitudService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import co.gov.aerocivil.controlt.web.util.DateUtil;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import co.gov.aerocivil.controlt.web.util.MailUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;


/**
 *
 * @author Viviana
 */
@ManagedBean
@SessionScoped
public class SolicitudBBean {
    @EJB
    private ModificarTurnoService modificarTurnoServiceBean;

    @EJB
    private SolicitudService solicitudService;
    
    @EJB
    private ProgramacionTurnosSession programacionService;
    
    
    @EJB
    private FuncionarioService funcionarioService;
    
    @EJB
    private RestriccionesService restriccionService;
    
    List<Funcionario> listFuncionariosDependencia;

    public FuncionarioService getFuncionarioService() {
        return funcionarioService;
    }

    public void setFuncionarioService(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    public RestriccionesService getRestriccionService() {
        return restriccionService;
    }

    public void setRestriccionService(RestriccionesService restriccionService) {
        this.restriccionService = restriccionService;
    }
    private PosicionJornada turno;
    //private PosicionJornada turno_reem;
    private Solicitud solicitud;
    private Solicitud solicitudFiltro;
    private List<Solicitud> lista;
    private List<PosicionJornada> listTurno;
    private List<PosicionJornada> listTurnoReemp;
    private Funcionario funcionario_reem;
    private List<Aeropuerto> listAeropuerto;
    private String funcionarioValidacion;
    private List<Dependencia> listDependencia;
    
    private Date current;
    
     private Funcionario funcionario_sol ;

    public Funcionario getFuncionario_sol() {
        return funcionario_sol;
    }

    public void setFuncionario_sol(Funcionario funcionario_sol) {
        this.funcionario_sol = funcionario_sol;
    }

    /**
     * Creates a new instance of SolicitudBBean
     */
    public SolicitudBBean() {
    }

    public String crear() {
        Calendar c = Calendar.getInstance();
        current=c.getTime();
        funcionarioValidacion = "";
        listTurno = null;
        listTurnoReemp = null;
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        Dependencia dependencia = logbbean.getFuncionarioTO().getFuncionario().getDependencia();
        turno = new PosicionJornada();
        //turno_reem = new PosicionJornada();
        funcionario_reem = new Funcionario();
        funcionario_reem.setDependencia(dependencia);
        funcionario_reem.setFunEstado("Activo");
        funcionario_reem.setRoles(new Long[]{5L,6L,4L});
        listFuncionariosDependencia = funcionarioService.getListaPag(funcionario_reem, null, null, "funAlias", SortOrderEnum.ASC.getOrder());
        
        solicitud = new Solicitud();
        solicitud.setDependencia(dependencia);
        solicitud.setFuncionario_sol(logbbean.getFuncionarioTO().getFuncionario());
        solicitud.setFuncionario_reem(funcionario_reem);
        //System.out.println("NEW " + solicitud.getFuncionario_reem());
        solicitud.setTurno(turno);
        //solicitud.setTurno_pago(turno_reem);
        solicitud.setSolEstado("Pendiente");
        solicitud.setSolFechaCambio(current);
        
        listTurno = solicitudService.obtenerPJenProgramacion(solicitud.getSolFechaCambio(), logbbean.getFuncionarioTO().getFuncionario());
        //cargarPosicionJornada();

        return "crearSolicitud";
    }
    
    public void cambioFechaSol(SelectEvent ev)
    {
        solicitud.setSolFechaCambio((Date)ev.getObject());
        Calendar c = Calendar.getInstance();
        c.setTime(solicitud.getSolFechaCambio());
        //System.out.println("Entra\t"+c.get(Calendar.DATE)+"/"+c.get(Calendar.MONTH)); 
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        listTurno = solicitudService.obtenerPJenProgramacion(solicitud.getSolFechaCambio(), logbbean.getFuncionarioTO().getFuncionario());
        
    }

    public String guardar() {
        
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        Dependencia dependencia = logbbean.getFuncionarioTO().getFuncionario().getDependencia();
        
        if(programacionService.existeProgramacion(dependencia, solicitud.getSolFechaCambio()))           
        {
            Funcionario funcionario = new Funcionario();
        funcionario = funcionarioService.getFuncionarioById(solicitud.getFuncionario_reem());
        if (funcionario != null) {
            solicitud.setFuncionario_reem(funcionario);
                            MailUtil mailUtil = new MailUtil(
                        JsfUtil.getListadosBBean().getParametrosSistema().get(ParametrosEnum.mail_server.name()),
                        JsfUtil.getListadosBBean().getParametrosSistema().get(ParametrosEnum.mail_port.name()),
                        JsfUtil.getListadosBBean().getParametrosSistema().get(ParametrosEnum.mail_from.name())
                        );            

                if (funcionario.getFunCorreoElectronico()!=null && !"".equals(funcionario.getFunCorreoElectronico().trim())){
                    mailUtil.sendEmail(funcionario.getFunCorreoElectronico(),
                            "Solicitud de cambio","Solicitud de cambio registrada");
                }
                if (solicitud.getFuncionario_sol().getFunCorreoElectronico()!=null && 
                        !"".equals(solicitud.getFuncionario_sol().getFunCorreoElectronico().trim())){
                    mailUtil.sendEmail(solicitud.getFuncionario_sol().getFunCorreoElectronico(),
                            "Solicitud de cambio","Solicitud de cambio registrada");
                }

                funcionarioValidacion = "";
            } else {
                funcionarioValidacion = JsfUtil.getMessageFromBundle("funcionarioNoEncontrado");
                solicitud.setFuncionario_reem(new Funcionario());
                //System.out.println("new vce " + solicitud.getFuncionario_reem());
            }

            solicitudService.guardar(solicitud, 
                    JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        
        }
        else
        {
            JsfUtil.addWarningMessage("validaFechaProgramacion");
        }
        
        
        return crear();

    }

    public String actualizar() {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        Date Finicial =DateUtil.getFirstDayMonth(solicitud.getSolFechaCambio());
        Date Ffinal=DateUtil.getLastDayMonth(solicitud.getSolFechaCambio());
        Long numAprobados = solicitudService.contarAprobados(solicitud,Finicial, Ffinal);
        
       Long numMaxApr=restriccionService.obtenerValor(8L, solicitud.getDependencia().getDepId());
        //System.out.println("num aprobados= "+numAprobados);
        //System.out.println("num maximo= "+numMaxApr);
        if (!"Aprobado Funcionario".equals(solicitud.getSolEstado())) {
            if (numAprobados >= numMaxApr && logbbean.isFuncionarioNivel(RolEnum.NIVEL_A4) && "Aprobado".equals(solicitud.getSolEstado())) {
                JsfUtil.addWarningMessage("solicitudesAprobadas+10");
                solicitud.setSolEstado("Aprobado Funcionario");
                //System.out.println("No es aprobado");
                return editar();
            } else {
                
                
                Vistaprogramacion v1 = modificarTurnoServiceBean.getVp(solicitud.getSolFechaCambio(), solicitud.getFuncionario_sol(), solicitud.getTurno().getPjId());
                Vistaprogramacion v2 = modificarTurnoServiceBean.getVp(solicitud.getSolFechaPago(), solicitud.getFuncionario_reem(), null);
                
                String resultado = modificarTurnoServiceBean.cambiarTurnos(solicitud.getSolFechaCambio(), solicitud.getFuncionario_sol(), v1, solicitud.getSolFechaCambio(), solicitud.getFuncionario_reem(), v2, JsfUtil.getFuncionarioSesion());
                                
                 if( resultado == null)
                {
                    solicitud.setSolAprobadoPor(logbbean.getFuncionarioTO().getFuncionario());
                    solicitud.setSolFechaAprobacion(Calendar.getInstance().getTime());
                    //System.out.println("aprobado");
                    solicitudService.guardar(solicitud, 
                    JsfUtil.getFuncionarioSesion());
                    JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
                    return listar();

                }
                else
                {
                    solicitud = solicitudService.getId(solicitud.getSolId());
                    JsfUtil.addManualWarningMessage(resultado);
                    return editar();
                }
              
            }
        } else {
            //System.out.println("Normal");
            solicitudService.guardar(solicitud, 
                JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
            return listar();
        }


    }

    public String actualizarFuncReem() {
                
                solicitudService.guardar(solicitud, 
                JsfUtil.getFuncionarioSesion());
                JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
                return listarPropias();
 
    }
    
    public String editar() {

        return "editSolicitud";
    }
    
public String editarPropias() {

        return "editarSolicitudPropias";
    }
    public String listar() {

        solicitudFiltro = new Solicitud();
        
        Regional regional = new Regional();
        Dependencia dependencia = new Dependencia();
        Aeropuerto aeropuerto = new Aeropuerto();
        Funcionario funcionario_sol = new Funcionario();


        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);

        DepCategoria depCategoria = new DepCategoria();
        dependencia.setDepcategoria(depCategoria);

        solicitudFiltro.setDependencia(dependencia);
        solicitudFiltro.setFuncionario_sol(funcionario_sol);

        listAeropuerto = null;
        listDependencia = null;
        listTurno = null;
        listTurnoReemp = null;

        turno = new PosicionJornada();
        solicitudFiltro.setTurno(turno);


        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_U1) || logbbean.isFuncionarioNivel(RolEnum.NIVEL_U2)) 
        {
           solicitudFiltro .setDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia()); 
        }
        
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A2)) {

            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            solicitudFiltro.setDependencia(dependencia);

        }

        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A3)) {


            regional.setRegId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
            aeropuerto.setRegional(regional);
            dependencia.setAeropuerto(aeropuerto);
            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());

            solicitudFiltro.setDependencia(dependencia);
            cargarAeropuertoListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A4)) {


            dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
            solicitudFiltro.setDependencia(dependencia);

            cargarPosicionJornadaLista();


        }
        //  


        // cargarPosicionJornadaLista();

        return filtrar();

    }

    
     public String listarPropias() {

        solicitudFiltro = new Solicitud();

       

        funcionario_sol = new Funcionario();
       
        solicitudFiltro.setFuncionario_sol(funcionario_sol);

        listTurno = null;
        listTurnoReemp = null;

        turno = new PosicionJornada();
        solicitudFiltro.setTurno(turno);
  LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
  
        solicitudFiltro.setFuncionario_reem(logbbean.getFuncionarioTO().getFuncionario());
        
        solicitudFiltro.setDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia());

        cargarPosicionJornadaLista();


        return filtrarPropias();

    }
     
    public String filtrar() {
        /*cargarAeropuerto();
         cargarDependencia();*/
        lista = solicitudService.getLista(solicitudFiltro, 0, 0, null, null);

        return "listarSolicitud";
    }
    
    public String filtrarPropias() {
        /*cargarAeropuerto();
         cargarDependencia();*/
        lista = solicitudService.getLista(solicitudFiltro, 0, 0, null, null);

        return "listarSolicitudPropias";
    }

    public void cargarPosicionJornadaLista() {


        cargarPosicionJornada(solicitudFiltro);


    }

    public void cargarPosicionJornada() {

        cargarPosicionJornada(solicitud);

    }

    private void cargarPosicionJornada(Solicitud sol) {

        listTurno = null;
        listTurnoReemp = null;
        listTurno = JsfUtil.getListadosBBean().getListaPosicionJornadaXDependencia(sol.getDependencia().getDepId());
        listTurnoReemp = JsfUtil.getListadosBBean().getListaPosicionJornadaXDependencia(sol.getDependencia().getDepId());
    }

    public void cargarAeropuerto() {
        cargarAeropuerto(solicitud);
    }

    public void cargarAeropuertoListado() {

        cargarAeropuerto(solicitudFiltro);
    }

    private void cargarAeropuerto(Solicitud f) {
        listDependencia = null;
        listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(f.getDependencia().getAeropuerto().getRegional().getRegId());
    }

    public void cargarDependencia() {
        cargarDependencia(solicitud);
    }

    public void cargarDependenciaListado() {
        cargarDependencia(solicitudFiltro);
    }

    private void cargarDependencia(Solicitud f) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getDependencia().getAeropuerto().getAeId(),
                    JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria().getDcId());
        } else {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getDependencia().getAeropuerto().getAeId());
        }

    }

    public PosicionJornada getTurno() {
        return turno;
    }

    public void setTurno(PosicionJornada turno) {
        this.turno = turno;
    }

/*    public PosicionJornada getTurno_reem() {
        return turno_reem;
    }
    public void setTurno_reem(PosicionJornada turno_reem) {
        this.turno_reem = turno_reem;
    }*/

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    public List<PosicionJornada> getListTurno() {
        return listTurno;
    }

    public void setListTurno(List<PosicionJornada> listTurno) {
        this.listTurno = listTurno;
    }

    public List<PosicionJornada> getListTurnoReemp() {
        return listTurnoReemp;
    }

    public void setListTurnoReemp(List<PosicionJornada> listTurnoReemp) {
        this.listTurnoReemp = listTurnoReemp;
    }

    public SolicitudService getSolicitudService() {
        return solicitudService;
    }

    public void setSolicitudService(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    public List<Solicitud> getLista() {
        return lista;
    }

    public void setLista(List<Solicitud> lista) {
        this.lista = lista;
    }

    public Solicitud getSolicitudFiltro() {
        return solicitudFiltro;
    }

    public void setSolicitudFiltro(Solicitud solicitudFiltro) {
        this.solicitudFiltro = solicitudFiltro;
    }

    public Funcionario getFuncionario_reem() {
        return funcionario_reem;
    }

    public void setFuncionario_reem(Funcionario funcionario_reem) {
        this.funcionario_reem = funcionario_reem;
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

    public String getFuncionarioValidacion() {
        return funcionarioValidacion;
    }

    public List<Funcionario> getListFuncionariosDependencia() {
        return listFuncionariosDependencia;
    }

    /**
     * @return the current
     */
    public Date getCurrent() {     
        
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(Date current) {
        this.current = current;
    }
    
    
}