/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Comision;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.TurnoEspFuncionario;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Viviana
 */
@Local
public interface TurnoEspFuncionarioService {

    public List<TurnoEspFuncionario> getLista(TurnoEspFuncionario turnoEspFuncionario, int first, int pageSize,
            String sortField, String sortOrder);
    
    public List<TurnoEspFuncionario> getLista(TurnoEspFuncionario turnoEspFuncionarior);

    public Long getCount();

    public TurnoEspFuncionario guardar(TurnoEspFuncionario turnoEspFuncionario, Funcionario f) throws Exception;

    public List<TurnoEspFuncionario> listarTurnos(Funcionario funcionario);

    public void eliminar(TurnoEspFuncionario turno) throws Exception;

   public List<TurnoEspFuncionario> listarTurnosAsignacion(Funcionario funcionario, Date fecha);

    public boolean turnoEspFunEnProgramacion(TurnoEspFuncionario turnoEspFuncionario);
    
    public String validarDisponibilidadTurno(Date date, Funcionario f);
    
    public boolean isRangoTurnoDisponible(TurnoEspFuncionario tef);

    public java.util.List<TurnoEspFuncionario> listarTurnosAsignacion(TurnoEspFuncionario turnoEspFuncionario);
}
