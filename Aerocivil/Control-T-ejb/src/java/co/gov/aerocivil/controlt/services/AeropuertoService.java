/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Funcionario;
import java.util.List;
import javax.ejb.Local;
 
/**
 *
 * @author Viviana
 */
@Local
public interface AeropuertoService{
    public Aeropuerto guardar(Aeropuerto aeropuerto, Funcionario func);
    
    public List<Aeropuerto> getLista(Aeropuerto aeropuerto, int first, int pageSize, 
            String sortField, String sortOrder);
    
    public Long getCount();
    
    public Aeropuerto findOneById(Long id);
}
