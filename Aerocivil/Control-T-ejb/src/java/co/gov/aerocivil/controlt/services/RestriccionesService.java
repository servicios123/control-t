/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.RestriccionDependencia;
import co.gov.aerocivil.controlt.entities.RestriccionProgramacion;
import co.gov.aerocivil.controlt.to.RestriccionTO;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface RestriccionesService {
    
    public List<RestriccionDependencia> getListaRestriccionesDependencia(RestriccionDependencia rd);

    public List<RestriccionTO> getListaRestriccionesProgramacion(Dependencia dep);

    public List<RestriccionTO> guardarListaRestricciones(List<RestriccionTO> restricciones, Dependencia dep, Funcionario f);

    public List<RestriccionDependencia> getLista(RestriccionDependencia restriccionFiltro, int first, int i, String sortField, String sortOrder);

    public Long obtenerValor(Long rpId, Long dependenciaID);

    public List<RestriccionProgramacion> getListaFaltantesDep(Dependencia dep);
    
    public Long getCount();
    
    public Jornada obtenerJornada(long dependencia);
}
