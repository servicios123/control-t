/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Menu;
import co.gov.aerocivil.controlt.entities.ParametrosDependencia;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author juancamilo
 */
@Local
public interface ParametroDependenciaService {
    
    List<ParametrosDependencia> listarTiposParametros();
    List<ParametrosDependencia> listarTiposParametrosDependencia(DepCategoria categoria,String menuId);
    List<ParametrosDependencia> listarParametrosPorTipoDependencia(ParametrosDependencia padre);
    ParametrosDependencia guardaParametroDependencia(ParametrosDependencia parametrosDependencia, Funcionario funcionario);
    boolean borrarParametroDependencia(ParametrosDependencia parametrosDependencia);
    public List<ParametrosDependencia> getLista(ParametrosDependencia dependencia, int first, int pageSize,
            String sortField, String sortOrder);   

    public java.lang.Long getCount();
    public List<ParametrosDependencia> getLista(Funcionario funcionario);
    public List<Menu> listarModulos();
}
