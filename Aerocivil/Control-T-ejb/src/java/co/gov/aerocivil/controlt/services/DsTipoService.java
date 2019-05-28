/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.DsTipo;
import co.gov.aerocivil.controlt.entities.Funcionario;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Viviana
 */
@Local
public interface DsTipoService {
    
    public DsTipo guardar(DsTipo dependencia, Funcionario f) throws SQLIntegrityConstraintViolationException;
    
    public List<DsTipo> getLista(DsTipo tipo, int first, int pageSize,
            String sortField, String sortOrder);    
    
    public List<DsTipo> getLista(DsTipo tipo);    
    
    public Long getCount();
    
}
