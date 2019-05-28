/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.CursoRecurrente;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.ParametrosDependencia;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.CursoRecurrenteService;
import co.gov.aerocivil.controlt.services.FuncionarioService;
import co.gov.aerocivil.controlt.services.ParametroDependenciaService;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Administrador
 */

@ManagedBean
@SessionScoped
public class CursoRecurrenteBBean {
    
    @EJB 
    CursoRecurrenteService cursoRecService;
    
    @EJB
    FuncionarioService funService;
    
    @EJB
    private ParametroDependenciaService dependenciaService;
    private List<ParametrosDependencia> parametros;
    @ManagedProperty(value = "#{menuBBean.selectedOption.menLabel}")
    private String idMenu;
    
    
    CursoRecurrente curso;

    public CursoRecurrente getCurso() {
        return curso;
    }
    public void actualiza(DateSelectEvent evento)
    {
       Calendar c = Calendar.getInstance();
       c.setTime(evento.getDate());
       c.add(Calendar.YEAR, 2);        
       Date date= evento.getDate();  
       curso.setCurFechaVencimiento(c.getTime());      
    }
            
    public void setCurso(CursoRecurrente curso) {
        this.curso = curso;
    }
    public String crear(){
        listarCursos();
        curso = new CursoRecurrente();
        FuncionarioBBean funBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);        
        funBBean.inicializarPickList();
        if(JsfUtil.getFuncionarioSesion().getRol().equals(RolEnum.NIVEL_A4)){
            funBBean.filtrarSinPaginar();
        }
        return "editCursoRecurrente";
    }
    
    public String guardar(){        
        FuncionarioBBean funBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);        
        List<Funcionario> disponibles = funBBean.getFuncionariosDisponibles();
        List<Funcionario> seleccionados = new ArrayList<Funcionario>();
        for (Funcionario f : disponibles){
            if (f.getSeleccionado()){
                seleccionados.add(f);
                f.setFunFvCurso(curso.getCurFechaVencimiento());
                try{
                    funService.guardar(f, 
                JsfUtil.getFuncionarioSesion());
                }catch(Exception e){
                    
                }
            }
        }
        if (seleccionados.size()<=0){
            JsfUtil.addErrorMessage("errorSeleccionFuncionarios");
            return null;
        }
        curso.setListaFuncionarios(seleccionados);
        curso = cursoRecService.guardar(curso, 
                JsfUtil.getFuncionarioSesion());
        JsfUtil.addSuccessMessage("genericSingleSaveSuccess"); 
        return crear();
    }
    
    public void listarCursos(){
        parametros = dependenciaService.listarTiposParametrosDependencia(JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria(), idMenu);
    }

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public List<ParametrosDependencia> getParametros() {
        return parametros;
    }

    public void setParametros(List<ParametrosDependencia> parametros) {
        this.parametros = parametros;
    }
    
   
}
