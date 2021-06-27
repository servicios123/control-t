/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Viviana
 */
@Local
public interface TurnoEspecialService {
   public TurnoEspecial guardar(TurnoEspecial turnoEspecial, Funcionario func) throws SQLIntegrityConstraintViolationException;
    
    public List<TurnoEspecial> getLista(TurnoEspecial turnoEspecial, int first, int pageSize, 
            String sortField, String sortOrder);
    public TurnoEspecial getTropByDep(Long depId);
    public Long getCount(); 
    public co.gov.aerocivil.controlt.entities.TurnoEspecial findBySigla(co.gov.aerocivil.controlt.entities.TurnoEspecial especial);
}
