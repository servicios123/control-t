/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.DiarioSenal;
import co.gov.aerocivil.controlt.entities.Funcionario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Viviana
 */
@Local
public interface DiarioSenalService {
 
      public List<DiarioSenal> getLista(DiarioSenal diarioSenal, Integer first, Integer pageSize, 
            String sortField, String sortOrder);
       public DiarioSenal guardar(DiarioSenal diarioSenal, Funcionario f);
  public Long getCount();
}
