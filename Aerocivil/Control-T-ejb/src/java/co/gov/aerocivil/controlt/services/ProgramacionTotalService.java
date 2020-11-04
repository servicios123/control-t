/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.Turno;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface ProgramacionTotalService {
    public void run(Programacion programacion, Funcionario fun, Boolean debug);
    public String validateTurn(Turno turno,Long cambio);
}
