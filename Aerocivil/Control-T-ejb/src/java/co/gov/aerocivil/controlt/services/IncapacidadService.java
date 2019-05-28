/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.to.IncapacidadTO;
import co.gov.aerocivil.controlt.to.IncapacidadVista;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface IncapacidadService {
    public Long getCount();
    public List<IncapacidadVista> getLista(IncapacidadTO incapacidadFiltro, Integer first, Integer pageSize, String sortField, String sortOrder, String groupBy);
    public Long getTotal(IncapacidadTO incapacidadFiltro, String groupBy);
}
