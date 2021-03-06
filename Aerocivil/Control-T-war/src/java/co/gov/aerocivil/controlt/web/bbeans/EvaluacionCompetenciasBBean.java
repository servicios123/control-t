/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.EvaluacionCompetencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.ParametrosDependencia;
import co.gov.aerocivil.controlt.services.EvaluacionCompetenciaService;
import co.gov.aerocivil.controlt.services.ParametroDependenciaService;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class EvaluacionCompetenciasBBean {
    
    @EJB
    private EvaluacionCompetenciaService evaluacionService;
    private EvaluacionCompetencia evaluacion=new EvaluacionCompetencia();
    private Funcionario funcionario;
    
    
    @EJB
    private ParametroDependenciaService dependenciaService;
    private List<ParametrosDependencia> parametros;
    private String idMenu;

    @PostConstruct
    public void cargar() {
        idMenu = "Evaluación Competencias";
        listarCursos();
    }

    
    
    public String listarFuncionarios(){
        FuncionarioBBean funBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);        
        String ret = funBBean.listar("funFvEvaluacion");
        funBBean.setColumns(new boolean[]{false,false,true});
        return "listarEvaluacionFuncionario";
    }
    public void actualiza(SelectEvent evento)
    {
       Calendar c = Calendar.getInstance();
        c.setTime((Date) evento.getObject());
        c.add(Calendar.YEAR, 1);
        Date date = (Date) evento.getObject();
        evaluacion.setEvFechaVence(c.getTime());
    }
    public String crear(){
        listarCursos();
        evaluacion = new EvaluacionCompetencia();
        evaluacion.setFuncionario(funcionario);        
        return "editEvaluacionCompetencias";
    }
    
    public String guardar(){
        evaluacion = evaluacionService.guardar(evaluacion, 
                JsfUtil.getFuncionarioSesion());
        JsfUtil.addSuccessMessage("genericSingleSaveSuccess"); 
        FuncionarioBBean funBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);        
        return funBBean.filtrar();        
    }
    
    public void guardarEvaluacion(){
        evaluacion = evaluacionService.guardar(evaluacion, 
                JsfUtil.getFuncionarioSesion());
        JsfUtil.addSuccessMessage("genericSingleSaveSuccess");      
    }
    
    public void listarCursos(){
        parametros = dependenciaService.listarTiposParametrosDependencia(JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria(), idMenu);
    }
    
    public EvaluacionCompetencia getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(EvaluacionCompetencia evaluacion) {
        this.evaluacion = evaluacion;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public List<ParametrosDependencia> getParametros() {
        return parametros;
    }

    public void setParametros(List<ParametrosDependencia> parametros) {
        this.parametros = parametros;
    }

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }
    
    

}
