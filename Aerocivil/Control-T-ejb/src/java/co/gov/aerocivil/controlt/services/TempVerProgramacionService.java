/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.Resumen;
import co.gov.aerocivil.controlt.entities.TempVerProgramacion;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Viviana
 */
@Local
public interface TempVerProgramacionService {

    public List<TempVerProgramacion> getLista(Programacion programacion);

    public void consultarTurnos(Programacion programacion);

    public List<Vistaprogramacion> comprobacion(long programacion);

    public Long totalHrEx(long programacion, Funcionario funcionario);

    public java.lang.Long totalHrExNoc(long programacion, java.lang.String funcionario);

    public java.util.List<co.gov.aerocivil.controlt.entities.Vistaprogramacion> fechasProgramacion(long programacion);
    
    public List<Resumen> obtenerResumen(Long programacion);
}
