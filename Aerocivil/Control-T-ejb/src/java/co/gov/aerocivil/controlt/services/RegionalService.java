/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Regional;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface RegionalService {

    public Regional guardar(Regional regional, Funcionario f);
    
    public List<Regional> getLista();
    
    public Regional getRegional(Long regId);
}
