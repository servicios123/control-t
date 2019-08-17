/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.EvaluacionCompetencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.entities.PosicionHabilitada;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.FuncionarioService;
import co.gov.aerocivil.controlt.services.PosicionHabilitadaService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class VencimientosBBean {

    @EJB
    FuncionarioService funcionarioService;
    @EJB
    PosicionHabilitadaService posicionService;
    private int evaluacionesCnt;
    private int cursosCnt;
    private int certMedicosCnt;
    private int posicionesCnt;
    private List<Funcionario> evaluaciones;
    private List<Funcionario> cursos;
    private List<Funcionario> certMedicos;
    private List<PosicionHabilitada> posiciones;
    private Long avisoVencimiento;

    public String listar() {


        Dependencia dep = JsfUtil.getFuncionarioSesion().getDependencia();
        avisoVencimiento = dep.getDepAvisoVencimiento();
        Funcionario funcFiltro = new Funcionario();
        PosicionHabilitada posHab = new PosicionHabilitada();
        posHab.setFuncionario(funcFiltro);


        funcFiltro.setDependencia(dep);

        Calendar c = Calendar.getInstance();
        funcFiltro.setFechaini(c.getTime());
        posHab.setFechaini(c.getTime());
        c.add(Calendar.DATE, avisoVencimiento.intValue());
        posHab.setFechafin(c.getTime());
        funcFiltro.setFechafin(c.getTime());
        Posicion pos = new Posicion();
        pos.setDependencia(dep);
        posHab.setPosicion(pos);
        posiciones = posicionService.getLista(posHab, null, null, null, null);
        if (posiciones != null) {
            posicionesCnt = posiciones.size();
        }

        funcFiltro.setFunEstado("Activo");
        funcFiltro.setEvalFecha(1);//funFvCertmedico


        certMedicos = funcionarioService.getListaPag(funcFiltro, null, null, "funAlias", SortOrderEnum.ASC.getOrder());
        if (certMedicos != null) {
            certMedicosCnt = certMedicos.size();
        }

        funcFiltro.setEvalFecha(2);//funFvCurso
        cursos = funcionarioService.getListaPag(funcFiltro, null, null, "funAlias", SortOrderEnum.ASC.getOrder());
        if (cursos != null) {
            cursosCnt = cursos.size();
        }

        funcFiltro.setEvalFecha(3);//funFvEvaluacion
        evaluaciones = funcionarioService.getListaPag(funcFiltro, null, null, "funAlias", SortOrderEnum.ASC.getOrder());
        if (evaluaciones != null) {
            evaluacionesCnt = evaluaciones.size();
        }


        return "listarVencimientos";
    }

    public String enviarCertMedico() {

        Dependencia dep = JsfUtil.getFuncionarioSesion().getDependencia();
        avisoVencimiento = dep.getDepAvisoVencimiento();
        Funcionario funcFiltro = new Funcionario();

        funcFiltro.setDependencia(dep);
        Calendar c = Calendar.getInstance();
        // funcFiltro.setFechaini(c.getTime());     
        c.add(Calendar.DATE, avisoVencimiento.intValue());
        funcFiltro.setFechafin(c.getTime());


        funcFiltro.setFunEstado("Activo");
        funcFiltro.setEvalFecha(1);//funFvCertmedico


        FuncionarioBBean funcbbean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        funcbbean.setFuncionarioFiltro(funcFiltro);
        funcbbean.setColumns(new boolean[]{true, false, false});
        return funcbbean.filtrar_vencimiento_certificado();
    }

    public String enviarVencimientoCurso() {

        Dependencia dep = JsfUtil.getFuncionarioSesion().getDependencia();
        avisoVencimiento = dep.getDepAvisoVencimiento();
        Funcionario funcFiltro = new Funcionario();

        funcFiltro.setDependencia(dep);
        Calendar c = Calendar.getInstance();
        // funcFiltro.setFechaini(c.getTime());     
        c.add(Calendar.DATE, avisoVencimiento.intValue());
        funcFiltro.setFechafin(c.getTime());


        funcFiltro.setFunEstado("Activo");
        funcFiltro.setEvalFecha(2);

        FuncionarioBBean funcbbean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        funcbbean.setFuncionarioFiltro(funcFiltro);
        funcbbean.setColumns(new boolean[]{true, false, false});


        return funcbbean.filtrar_vencimiento_curso();
    }

    public String enviarVencimientosPosicionHabilitadas() {
        Dependencia dep = JsfUtil.getFuncionarioSesion().getDependencia();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, avisoVencimiento.intValue());

        avisoVencimiento = dep.getDepAvisoVencimiento();
        PosicionHabilitadaBBean posicionhabilitadabbean = (PosicionHabilitadaBBean) JsfUtil.getManagedBean(PosicionHabilitadaBBean.class);
        posicionhabilitadabbean.preparar();

        posicionhabilitadabbean.getPosicionHabilitadaFiltro().setFechafin(c.getTime());

        return posicionhabilitadabbean.filtrarVencimientos();




    }

    public String enviarVencimientoEvaluacion() {

        Dependencia dep = JsfUtil.getFuncionarioSesion().getDependencia();
        avisoVencimiento = dep.getDepAvisoVencimiento();
        Funcionario funcFiltro = new Funcionario();

        funcFiltro.setDependencia(dep);
        Calendar c = Calendar.getInstance();
        // funcFiltro.setFechaini(c.getTime());     
        c.add(Calendar.DATE, avisoVencimiento.intValue());
        funcFiltro.setFechafin(c.getTime());


        funcFiltro.setFunEstado("Activo");
        funcFiltro.setEvalFecha(3);

        FuncionarioBBean funcbbean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        funcbbean.setFuncionarioFiltro(funcFiltro);
        funcbbean.setColumns(new boolean[]{true, false, false});


        return funcbbean.filtrar_vencimiento_evaluacion();
    }

    public int getEvaluacionesCnt() {
        return evaluacionesCnt;
    }

    public void setEvaluacionesCnt(int evaluacionesCnt) {
        this.evaluacionesCnt = evaluacionesCnt;
    }

    public int getCursosCnt() {
        return cursosCnt;
    }

    public void setCursosCnt(int cursosCnt) {
        this.cursosCnt = cursosCnt;
    }

    public int getCertMedicosCnt() {
        return certMedicosCnt;
    }

    public void setCertMedicosCnt(int certMedicosCnt) {
        this.certMedicosCnt = certMedicosCnt;
    }

    public int getPosicionesCnt() {
        return posicionesCnt;
    }

    public void setPosicionesCnt(int posicionesCnt) {
        this.posicionesCnt = posicionesCnt;
    }

    public List<Funcionario> getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(List<Funcionario> evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    public List<Funcionario> getCursos() {
        return cursos;
    }

    public void setCursos(List<Funcionario> cursos) {
        this.cursos = cursos;
    }

    public List<Funcionario> getCertMedicos() {
        return certMedicos;
    }

    public void setCertMedicos(List<Funcionario> certMedicos) {
        this.certMedicos = certMedicos;
    }

    public List<PosicionHabilitada> getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(List<PosicionHabilitada> posiciones) {
        this.posiciones = posiciones;
    }
}
