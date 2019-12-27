/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;

import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.entities.RestriccionProgramacion;
import co.gov.aerocivil.controlt.enums.ParametrosEnum;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.FuncionarioService;
import co.gov.aerocivil.controlt.services.PermisoService;
import co.gov.aerocivil.controlt.services.ProgramacionTotalService;
import co.gov.aerocivil.controlt.services.ProgramacionTurnosSession;
import co.gov.aerocivil.controlt.services.RestriccionesService;
import co.gov.aerocivil.controlt.web.lazylist.ProgramacionLazyList;
import co.gov.aerocivil.controlt.web.manager.ProgrammeHandler;
import co.gov.aerocivil.controlt.web.util.DateUtil;

import co.gov.aerocivil.controlt.web.util.JsfUtil;
import co.gov.aerocivil.controlt.web.util.MailUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ProgramacionTurnosBBean {

    @EJB
    private PermisoService permisoServiceBean;
    @EJB
    private ProgramacionTotalService programacionTotalService;
    @EJB
    private ProgramacionTurnosSession programacionService;
    @EJB
    private FuncionarioService funcService;
    @EJB
    private RestriccionesService restriccionesService;
    private Programacion programacion;
    private LazyDataModel<Programacion> lista;
    private Programacion programacionFiltro;
    private List<RestriccionProgramacion> restriccionesFaltantes;
    private List<Aeropuerto> listAeropuerto;
    private List<Dependencia> listDependencia;
    private boolean flag;
    private Date minDate;
    private Date maxDate;
    private boolean debug;
    private double progress = 0d;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    //Paso 0 seleccionar fechas de generación de programación
    public String selecionarFechas() {
        programacion = new Programacion();
        programacion.setDependencia(JsfUtil.getFuncionarioSesion().getDependencia());

        //System.out.println("Entra a seleccionar fechas");

        minDate = programacionService.fechaUltimaProgramacion(programacion.getDependencia());
        Calendar max = Calendar.getInstance();
        max.setTime(minDate);
        max.add(Calendar.MONTH, 1);
        max.add(Calendar.DATE, -1);
        maxDate = max.getTime();
        programacion.setProFechaInicio(minDate);
        setDebug(false);
        return "seleccionarFechasProg";
    }

    //Paso 1 generar tablas temporales de posicionJornada y FuncionarioPosicionHabilitada
    public Programacion guardar(Programacion programacion) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        programacion.setDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia());
        programacion.setFuncionarioGenera(logbbean.getFuncionarioTO().getFuncionario());
        return programacionService.guardar(programacion,
                JsfUtil.getFuncionarioSesion());

    }

    public String resultadoProgramacion() {
        return "programacionGenerada";
    }

    public String generandoProgramacion() {

        debug = true;
        Calendar start = Calendar.getInstance();
        Calendar finish = Calendar.getInstance();

        start.setTime(programacion.getProFechaInicio());
        start.set(Calendar.DATE, 1);
        finish.setTime(start.getTime());
        finish.add(Calendar.MONTH, 1);
        finish.add(Calendar.DATE, -1);
        programacion.setProFechaInicio(start.getTime());
        programacion.setProFechaFin(finish.getTime());



        programacionService.borrarPendientes(programacion);
        programacion.setProEstado(0);
        programacion = guardar(programacion);

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        programacion.setDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia());


        //System.out.println("\n\nFechas\t" + start.get(Calendar.DATE) + "/" + start.get(Calendar.MONTH) + "/" + start.get(Calendar.YEAR) + "\t" + finish.get(Calendar.DATE) + "/" + finish.get(Calendar.MONTH) + "/" + finish.get(Calendar.YEAR));

//        permisoServiceBean.solvePetitions(programacion);

        programacionTotalService.run(programacion, logbbean.getFuncionarioTO().getFuncionario(), isDebug());

        JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        TempVerProgramacionBBean bbean = ((TempVerProgramacionBBean) JsfUtil.getManagedBean(TempVerProgramacionBBean.class));
        bbean.setProgramacion(programacion);
        return bbean.filtrar();


    }

    public String descargarUltimoLog() throws IOException {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        File file = new File("out_" + programacion.getDependencia().getDepAbreviatura() + ".csv");

        if (file.exists()) {

            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();


            String filename = file.getName();
            String contentType = ec.getMimeType(filename);
            int contentLength = (int) file.length();

            String meta = "attachment; filename=\"" + filename + "\"";


            ec.responseReset();
            ec.setResponseContentType(contentType);
            ec.setResponseContentLength(contentLength);
            ec.setResponseHeader("Content-Disposition", meta);



            OutputStream out = ec.getResponseOutputStream();

            FileInputStream fis = new FileInputStream(file);
            byte[] buf = new byte[contentLength];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                out.write(buf);
                out.flush();
            }

            fis.close();
            fc.responseComplete();


        } else {
            JsfUtil.addWarningMessage("archivoNoExiste");
        }

        return null;
    }

    public String generarProgramacion() {

        programacionService.borrarPendientes(programacion);
        programacion.setProEstado(0);
        programacion = guardar(programacion);
        try {
            programacionService.generarProgramacion(programacion);
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
            TempVerProgramacionBBean bbean = ((TempVerProgramacionBBean) JsfUtil.getManagedBean(TempVerProgramacionBBean.class));
            bbean.setProgramacion(programacion);
            return bbean.filtrar();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage(ex, null);
            return null;
        } catch (Exception e) {

            e.printStackTrace();
            JsfUtil.addErrorMessage(e, null);
            return null;
        }





    }

    public String validar() {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        programacion.setProEstado(1);
        programacion.setProFechaAprobacion(Calendar.getInstance().getTime());
        programacion.setFuncionarioAprueba(logbbean.getFuncionarioTO().getFuncionario());
        programacion = programacionService.guardar(programacion,
                JsfUtil.getFuncionarioSesion());
        JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        return listarValidacion();
    }

    public String listar() {

        programacionFiltro = new Programacion();
        programacionFiltro.setReporte(false);
        Regional regional = new Regional();
        Dependencia dependencia = new Dependencia();
        Aeropuerto aeropuerto = new Aeropuerto();


        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);


        listAeropuerto = null;
        listDependencia = null;
        flag = true;

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioNivel(RolEnum.AUDITORIA)) {
            flag = false;
        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A1)) {
            dependencia.setDepcategoria(new DepCategoria());
            programacionFiltro.setDependencia(dependencia);
        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A2)) {

            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());

            programacionFiltro.setDependencia(dependencia);
            cargarDependenciaListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A3)) {


            regional.setRegId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
            aeropuerto.setRegional(regional);
            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            dependencia.setAeropuerto(aeropuerto);
            programacionFiltro.setDependencia(dependencia);
            cargarAeropuertoListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A4) || logbbean.isFuncionarioNivel(RolEnum.NIVEL_U2)) {

            dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
            programacionFiltro.setDependencia(dependencia);


        }
        programacionFiltro.setDependencia(dependencia);



        return filtrar();

    }

    public String listarUltimos() {

        programacionFiltro = new Programacion();
        programacionFiltro.setReporte(true);

        Regional regional = new Regional();
        Dependencia dependencia = new Dependencia();
        Aeropuerto aeropuerto = new Aeropuerto();

        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);

        listAeropuerto = null;
        listDependencia = null;
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        dependencia.setDepcategoria(new DepCategoria());
        programacionFiltro.setDependencia(dependencia);

        flag = true;
        return filtrarUltimos();

    }

    public String listarValidacion() {

        programacionFiltro = new Programacion();

        Regional regional = new Regional();
        Dependencia dependencia = new Dependencia();
        Aeropuerto aeropuerto = new Aeropuerto();


        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);


        listAeropuerto = null;
        listDependencia = null;




        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_U1)) {
            dependencia.setDepcategoria(new DepCategoria());
        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A2)) {

            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());

            programacionFiltro.setDependencia(dependencia);
            cargarDependenciaListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A3)) {


            regional.setRegId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
            aeropuerto.setRegional(regional);
            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            dependencia.setAeropuerto(aeropuerto);
            programacionFiltro.setDependencia(dependencia);
            cargarAeropuertoListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A4) || logbbean.isFuncionarioNivel(RolEnum.NIVEL_U2)) {

            dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
            programacionFiltro.setDependencia(dependencia);


        }
        programacionFiltro.setDependencia(dependencia);
        programacionFiltro.setProEstado(2);

        flag = false;
        return filtrar();

    }

    public String filtrar() {
        /*cargarAeropuerto();
         cargarDependencia();*/
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (!logbbean.isFuncionarioNivel(RolEnum.NIVEL_A1) && !logbbean.isFuncionarioNivel(RolEnum.AUDITORIA)) {
            programacionFiltro.getDependencia().setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
        }
        lista = new ProgramacionLazyList(programacionService, programacionFiltro);

        return "listarProgramacion";



    }

    public String filtrarUltimos() {
        /*cargarAeropuerto();
         cargarDependencia();*/
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        programacionFiltro.setReporte(true);
        lista = new ProgramacionLazyList(programacionService, programacionFiltro);

        return "listarUltimaProgramacion";



    }

    public void cargarAeropuerto() {
        cargarAeropuerto(programacion);
    }

    public void cargarAeropuertoListado() {

        cargarAeropuerto(programacionFiltro);
    }

    private void cargarAeropuerto(Programacion f) {
        listDependencia = null;
        listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(f.getDependencia().getAeropuerto().getRegional().getRegId());
    }

    public void cargarDependencia() {
        cargarDependencia(programacion);
    }

    public void cargarDependenciaListado() {
        cargarDependencia(programacionFiltro);
    }

    private void cargarDependencia(Programacion f) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getDependencia().getAeropuerto().getAeId(),
                    JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria().getDcId());
        } else {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getDependencia().getAeropuerto().getAeId());
        }

    }

    public Programacion getProgramacion() {
        return programacion;
    }

    public void setProgramacion(Programacion programacion) {
        this.programacion = programacion;
    }

    public LazyDataModel<Programacion> getLista() {
        return lista;
    }

    public void setLista(LazyDataModel<Programacion> lista) {
        this.lista = lista;
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

    public ProgramacionTurnosSession getService() {
        return programacionService;
    }

    public void setService(ProgramacionTurnosSession service) {
        this.programacionService = service;
    }

    public ProgramacionTurnosSession getProgramacionService() {
        return programacionService;
    }

    public void setProgramacionService(ProgramacionTurnosSession programacionService) {
        this.programacionService = programacionService;
    }

    public Programacion getProgramacionFiltro() {
        return programacionFiltro;
    }

    public void setProgramacionFiltro(Programacion programacionFiltro) {
        this.programacionFiltro = programacionFiltro;
    }

    /**
     * @return the restriccionesFaltantes
     */
    public List<RestriccionProgramacion> getRestriccionesFaltantes() {
        return restriccionesFaltantes;
    }

    /**
     * @param restriccionesFaltantes the restriccionesFaltantes to set
     */
    public void setRestriccionesFaltantes(List<RestriccionProgramacion> restriccionesFaltantes) {
        this.restriccionesFaltantes = restriccionesFaltantes;
    }

    /**
     * @return the minDate
     */
    public Date getMinDate() {
        return minDate;
    }

    /**
     * @param minDate the minDate to set
     */
    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    /**
     * @return the maxDate
     */
    public Date getMaxDate() {
        return maxDate;
    }

    /**
     * @param maxDate the maxDate to set
     */
    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    /**
     * @return the debug
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * @param debug the debug to set
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
