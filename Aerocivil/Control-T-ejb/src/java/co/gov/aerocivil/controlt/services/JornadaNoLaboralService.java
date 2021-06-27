/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.JornadaNoLaborable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Viviana
 */
@Local
public interface JornadaNoLaboralService {

    public JornadaNoLaborable guardar(JornadaNoLaborable jornada, Funcionario f) throws SQLIntegrityConstraintViolationException;

    public JornadaNoLaborable getJornadasFuncionario(Long jornada, Long funcionario);

    public List<JornadaNoLaborable> getListaJornadasFuncionario(Long funcionario);
    
    public HashMap<Long, List<Long>> getJornadasNoLaborales();
    
    public void eliminar(Long funcionario);
}
