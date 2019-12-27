/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.RepHorasExtras;
import co.gov.aerocivil.controlt.services.ControlDiarioPosicionesService;
import co.gov.aerocivil.controlt.services.DependenciaService;
import co.gov.aerocivil.controlt.services.FuncionarioService;
import co.gov.aerocivil.controlt.services.ListasService;
import co.gov.aerocivil.controlt.web.enums.Months;
import co.gov.aerocivil.controlt.web.util.DateUtil;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ReportesBBean {

    @EJB
    private ControlDiarioPosicionesService controlDiarioService;
    private RepHorasExtras repHExtFiltro;
    @EJB
    private FuncionarioService funService;
    @EJB
    private ListasService listasService;
    private int mes;
    private Integer anio;

    public String irReporteHorasExtra() {
        anio = Calendar.getInstance().get(Calendar.YEAR);
        repHExtFiltro = new RepHorasExtras();
        repHExtFiltro.setFuncionario(new Funcionario());
        FuncionarioBBean funBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        funBBean.inicializarPickList();
        return "reporteHorasExtra";
    }

    public String generarRepHorasExtra() {
        FuncionarioBBean funBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        repHExtFiltro.setDependencia(funBBean.getFuncionarioFiltro().getDependencia());
        repHExtFiltro.getFuncionario().setDependencia(repHExtFiltro.getDependencia());
        Funcionario funAux = funService.getFuncionarioById(repHExtFiltro.getFuncionario());
        if (funAux == null) {
            JsfUtil.addWarningMessage("funcionarioNoEncontrado");
            return null;
        }
        repHExtFiltro.setFuncionario(funAux);

        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes, 1);
        repHExtFiltro.setFecha(DateUtil.setCeroHoras(cal.getTime()));
        repHExtFiltro.setFechaFinReporte(
                DateUtil.getLastDayMonth(cal.getTime()));
        if (repHExtFiltro.getDependencia() != null && repHExtFiltro.getDependencia().getDepId() != null) {
            repHExtFiltro.setDependencia((Dependencia) listasService.obtenerObjById(Dependencia.class, repHExtFiltro.getDependencia().getDepId()));
        }
        List<RepHorasExtras> lista = controlDiarioService.getReporteHorasExtras(repHExtFiltro);
        if (lista == null || lista.isEmpty()) {
            JsfUtil.addWarningMessage("descargaFallida");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            return null;
        } 
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("funcionario_id", repHExtFiltro.getFuncionario().getFunId());

        /*Calendar c = Calendar.getInstance();
         c.setTime(repHExtFiltro.getFecha());*/
        map.put("anio", anio);
        map.put("mes", Months.getMonth(mes).getLabel().toUpperCase());
        map.put("aeropuerto", repHExtFiltro.getDependencia().getAeropuerto().getAeNombre());
        try {
            map.put("jefe", funService.getJefeDependencia(repHExtFiltro.getDependencia().getDepId()).getFunNombre());
            map.put("jefeId", funService.getJefeDependencia(repHExtFiltro.getDependencia().getDepId()).getFunId());
        } catch (Exception e) {
            map.put("jefe", funService.getJefeDependencia(null));
        }
        map.put("funcionario", repHExtFiltro.getFuncionario().getFunNombre());
        map.put("cargo", repHExtFiltro.getFuncionario().getFunCargo());
        map.put("grado", repHExtFiltro.getFuncionario().getFunGrado());
        if (repHExtFiltro.getDependencia() != null && repHExtFiltro.getDependencia().getDepId() != null) {
            Dependencia dep = (Dependencia) listasService.obtenerObjById(Dependencia.class, repHExtFiltro.getDependencia().getDepId());
            map.put("depAbrev", dep.getDepAbreviatura());
            map.put("dependencia", dep.getDepNombre());
        }

        try {
            map.put("sueldo", new Double(repHExtFiltro.getFuncionario().getFunSueldo().doubleValue()));
        } catch (Exception e) {
            map.put("jefe", new Double("0"));
        }
        for (RepHorasExtras repHX : lista) {
            repHX.setCol14(DateUtil.formatJornada(repHX.getCol14()));
            repHX.setCol15(DateUtil.formatJornada(repHX.getCol15()));
        }
        JsfUtil.generaReporte("horas_extras_ats", map, lista);
        return null;
    }

    public RepHorasExtras getRepHExtFiltro() {
        return repHExtFiltro;
    }

    public void setRepHExtFiltro(RepHorasExtras repHExtFiltro) {
        this.repHExtFiltro = repHExtFiltro;
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
}
