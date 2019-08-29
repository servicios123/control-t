/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import co.gov.aerocivil.controlt.services.ControlDiarioPosicionesService;
import co.gov.aerocivil.controlt.services.ListasService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import co.gov.aerocivil.controlt.web.util.DateUtil;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class TurnosProgVsEjecutadosBBean {

    private Programacion programacionFiltro;
    private Funcionario funcionario;
    @EJB
    private ControlDiarioPosicionesService service;
    @EJB
    private ListasService listasService;
    private Dependencia dependenciaFiltro;
    private List<Vistaprogramacion> lazyList;
    private List<Vistaprogramacion> lista;

    public String filtrarProgVsEjecutado() {
        lazyList = service.getListaProgramadoVsEjecutado(programacionFiltro, funcionario, 0, 0,
                null, null);
        return "listarProgVsEjecutados";
    }

    public String filtrarProgVsEjecutadoSinPaginar() {
        lazyList = service.getListaProgramadoVsEjecutado(programacionFiltro, funcionario, 0, 0,
                null, null);
        return "listarProgVsEjecutadosNoPag";
    }

    public void precargarFiltros() {
        FuncionarioBBean funcBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        funcBBean.inicializarPickList();
        dependenciaFiltro = funcBBean.getFuncionarioFiltro().getDependencia();
        programacionFiltro = new Programacion();
        programacionFiltro.setDependencia(dependenciaFiltro);
        /*Funcionario fun = new Funcionario();
         fun.setDependencia(dependenciaFiltro);
         diarioFiltro = new DiarioPosicion();
         diarioFiltro.setFuncionario(fun);
         diarioFiltro.setTurno(new Vistaprogramacion());*/
    }

    public String listarProgramadoVsEjecutados() {
        precargarFiltros();
        return "listarProgVsEjecutados";
    }

    public Dependencia getDependenciaFiltro() {
        return dependenciaFiltro;
    }

    public void setDependenciaFiltro(Dependencia dependenciaFiltro) {
        this.dependenciaFiltro = dependenciaFiltro;
    }

    public Programacion getProgramacionFiltro() {
        return programacionFiltro;
    }

    public void setProgramacionFiltro(Programacion programacionFiltro) {
        this.programacionFiltro = programacionFiltro;
    }

    public List<Vistaprogramacion> getLazyList() {
        return lazyList;
    }

    public void setLazyList(List<Vistaprogramacion> lazyList) {
        this.lazyList = lazyList;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public String generarReporte() {
        lista = service.getListaProgramadoVsEjecutado(programacionFiltro, funcionario, null, null,
                "turFecha", SortOrderEnum.ASC.getOrder());
        java.util.HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, String> params = listasService.getParametrosSistema();

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);



        map.put("clave", logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria().getDcClaveDp());
        map.put("version", logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria().getDcVersionDp());
        map.put("fechaFormato", DateUtil.formatDate(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria().getDcFechaDp()));
        if (programacionFiltro.getDependencia() != null && programacionFiltro.getDependencia().getDepId() != null) {
            Dependencia dep = (Dependencia) listasService.obtenerObjById(Dependencia.class, programacionFiltro.getDependencia().getDepId());
            map.put("depAbrev", dep.getDepAbreviatura());
        }

        JsfUtil.generaReporte("ctrlDiarioPosiciones", map, lista);
        return null;
    }
}
