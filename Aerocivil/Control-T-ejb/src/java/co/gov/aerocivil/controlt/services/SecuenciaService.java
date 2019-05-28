/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.DetSecuencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Secuencia;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Viviana
 */
@Local
public interface SecuenciaService {
    
    public Secuencia guardar(Secuencia secuencia, Funcionario f) ;
    
    public List<Secuencia> getLista(Secuencia secuencia, int first, int pageSize,
            String sortField, String sortOrder);    
    public void borrarDetalle(Secuencia secuencia);
    
    public Long getCount();
}
