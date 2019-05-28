/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Funcionario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Viviana
 */
@Local
public interface DepCategoriaService {
    
    public List<DepCategoria> getLista();
    public List<DepCategoria> getLista(DepCategoria depcategoria);
    public DepCategoria guardar(DepCategoria depcategoria, Funcionario f);
    
}
