/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.PosicionNacional;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Local;
 
/**
 *
 * @author Viviana
 */
@Local
public interface PosicionNacionalService {
    public PosicionNacional guardar(PosicionNacional posicion, Funcionario f) throws SQLIntegrityConstraintViolationException;
    
    public List<PosicionNacional> getLista(PosicionNacional posicion, Integer first, Integer pageSize, 
            String sortField, String sortOrder);
    
    public Long getCount();

    public boolean validarInactivacion(PosicionNacional posicion);

    public PosicionNacional getPosNalById(Long posNal);

}
