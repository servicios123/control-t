/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.PosicionJornada;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Viviana
 */
@Local
public interface PosicionJornadaService {
      public PosicionJornada guardar(PosicionJornada posicionJornada, Funcionario f);
    
    public List<PosicionJornada> getLista(PosicionJornada posicionJornada, int first, int pageSize, 
            String sortField, String sortOrder);
    public List<PosicionJornada> getLista(PosicionJornada posicionJornada);
    
    public Long getCount(); 
}
