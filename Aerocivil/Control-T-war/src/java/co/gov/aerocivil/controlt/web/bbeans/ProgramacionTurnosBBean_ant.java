/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.services.ProgramacionTurnosSession;
import co.gov.aerocivil.controlt.web.util.DateUtil;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ProgramacionTurnosBBean_ant {
    
    @EJB 
    private ProgramacionTurnosSession service;
    
    private Programacion programacion;

    //Paso 0 seleccionar fechas de generación de programación
    public String selecionarFechas(){
        programacion = new Programacion();
        programacion.setDependencia(JsfUtil.getFuncionarioSesion().getDependencia());
        return "seleccionarFechasProg";
    }
    
    //Paso 1 generar tablas temporales de posicionJornada y FuncionarioPosicionHabilitada
    /*public String validarProgramacion(){
        
       
      if(!service.validarProgramacionAprobada(programacion)){
            
            JsfUtil.addWarningMessage("programacionYaExistenteAprobada");
            
        }else if(!service.validarProgramacionNoAprobada(programacion)){
            
            JsfUtil.addWarningMessage("programacionYaExistenteNoAprobada");
            
            return reGenerarProgramacion(programacion);
            
        }else {
        
            programacion.setProEstado(0);
            programacion=guardar(programacion);
             generarProgramacion(programacion);        
            
             JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
              selecionarFechas();
        }
        return "";
      
    }*/
    
    
    public String reGenerarProgramacion(Programacion programacion){
    
            return "reGenerarProgramacion";
    }
    
    public String generarProgramacionPendiente (){
        
        service.borrarPendientes(programacion);
        programacion.setProEstado(0);
        programacion=guardar(programacion);
    
        generarProgramacion(programacion);
        
         return "programacionGenerada";
    }
    
    public Programacion guardar(Programacion programacion) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        programacion.setDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia());

       return service.guardar(programacion, 
                JsfUtil.getFuncionarioSesion());
      
    }

    public void generarProgramacion(Programacion programacion){
        try {
            service.generarProgramacion(programacion);
        } catch (SQLException ex) {
            Logger.getLogger(ProgramacionTurnosBBean_ant.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    public Programacion getProgramacion() {
        return programacion;
    }

    public void setProgramacion(Programacion programacion) {
        this.programacion = programacion;
    }
    
}
