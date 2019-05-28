/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Emergencias;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Viviana
 */
@Local
public interface EmergenciasService {
    
    public List<Emergencias> getLista(Emergencias emergencias, int first, int pageSize, 
            String sortField, String sortOrder);
       public Emergencias guardar(Emergencias emergencias);
  public Long getCount();

    List<Emergencias> getListaPag(Emergencias emergencias, Integer first, Integer pageSize,
            String sortField, String sortOrder);
}
