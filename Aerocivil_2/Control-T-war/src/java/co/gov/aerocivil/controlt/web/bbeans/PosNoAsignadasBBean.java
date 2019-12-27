/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.PosNoAsig;
import co.gov.aerocivil.controlt.entities.Turno;
import co.gov.aerocivil.controlt.services.FuncionarioService;
import co.gov.aerocivil.controlt.services.ModificarTurnoService;
import co.gov.aerocivil.controlt.services.TurnoService;
import co.gov.aerocivil.controlt.services.VistaProgramacionService;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
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
public class PosNoAsignadasBBean {
    @EJB
    private FuncionarioService funcionarioServiceBean;
    
    @EJB
    private ModificarTurnoService modificarTurnoServiceBean;
    
    @EJB
    VistaProgramacionService vistaProgramacionService;

    @EJB
    TurnoService turnoService;
    
    
    private List<PosNoAsig> posiciones;
    private PosNoAsig posicion;
    private Turno turno;
    private Funcionario nuevoFuncionario;
    private List<Funcionario> listFuncionarios;
    public String guardar(){
        //posicion.set
        String resultado = modificarTurnoServiceBean.asignarPosNoAsig(posicion, posicion.getFecha(), funcionarioServiceBean.getFuncionarioById(nuevoFuncionario), JsfUtil.getFuncionarioSesion());
        if( resultado == null)
        {
            JsfUtil.addSuccessMessage("posicionAsignada");
            TempVerProgramacionBBean vistaProgBBean = (TempVerProgramacionBBean) 
                JsfUtil.getManagedBean(TempVerProgramacionBBean.class);
            return vistaProgBBean.filtrar();
        }
        else
        {
            JsfUtil.addManualWarningMessage(resultado);
            return null;
        }
        /*
       TempVerProgramacionBBean vistaProgBBean = (TempVerProgramacionBBean) 
                JsfUtil.getManagedBean(TempVerProgramacionBBean.class);
        nuevoFuncionario=(Funcionario) JsfUtil.getListadosBBean().obtenerObjById(Funcionario.class, nuevoFuncionario.getFunId());
        Long turTipo = vistaProgramacionService.validarPosicionNoAsignada(nuevoFuncionario.getFunId(),
                posicion.getDependencia(),
                posicion.getPosicion(),
                posicion.getJornada(),
                posicion.getFecha());
        if (turTipo > 0L) {
            turno = new Turno();
            turno.setFuncionario(nuevoFuncionario);            
            turno.setProgramacion(vistaProgBBean.getProgramacion());
            turno.setTurEstado(0L);
            turno.setTurFecha(posicion.getFecha());
            turno.setTurHFin(posicion.getPosicionJornada().getJornada().getJoHoraFin().longValue());
            turno.setTurHInicio(posicion.getPosicionJornada().getJornada().getJoHoraInicio().longValue());
            turno.setTurMFin(posicion.getPosicionJornada().getJornada().getJoMinutoFin().longValue());
            turno.setTurMInicio(posicion.getPosicionJornada().getJornada().getJoMinutoInicio().longValue());
            turno.setTurPosicionJornada(posicion.getPosicionJornada().getPjId());
            turno.setTurTipo(turTipo);
            turnoService.guardar(turno, JsfUtil.getFuncionarioSesion());    
            vistaProgramacionService.eliminarPosNoAsignada(posicion);
            JsfUtil.addSuccessMessage("posicionAsignada");
            return vistaProgBBean.filtrar();            
        }
        else{
            turTipo *=-1;          
            JsfUtil.addSuccessMessage("validacionNoPasa"+turTipo);     
            
        }*/
    }
    
    public String asignarPosicion(){
        nuevoFuncionario = new Funcionario();
        /*listFuncionarios=JsfUtil.getListadosBBean().
                getListFuncionariosXPosicion(posicion.getPosicionJornada().getPjId(), null);*/
        listFuncionarios=JsfUtil.getListadosBBean().
                obtenerFuncionariosXPosicionXFecha(posicion.getPosicionJornada().getPjId(), posicion.getFecha());
        return "asignarPosicionNoAsignada";
    }

    public List<PosNoAsig> getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(List<PosNoAsig> posiciones) {
        this.posiciones = posiciones;
    }

    public PosNoAsig getPosicion() {
        return posicion;
    }

    public void setPosicion(PosNoAsig posicion) {
        this.posicion = posicion;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Funcionario getNuevoFuncionario() {
        return nuevoFuncionario;
    }

    public void setNuevoFuncionario(Funcionario nuevoFuncionario) {
        this.nuevoFuncionario = nuevoFuncionario;
    }

    public List<Funcionario> getListFuncionarios() {
        return listFuncionarios;
    }

    public void setListFuncionarios(List<Funcionario> listFuncionarios) {
        this.listFuncionarios = listFuncionarios;
    }
  
    
    
}
