/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Viviana
 */
@Local
public interface JornadaService{
     public Jornada guardar(Jornada jornada, Funcionario f) throws SQLIntegrityConstraintViolationException ;
     
    public List<Jornada> getListaPag(Jornada jornada, Integer first, Integer pageSize,
            String sortField, String sortOrder);    
    public Long getCount();

    public Jornada getJornadaAnterior(Jornada jornada);
    public Jornada getJornadaAnteriorRionegro(Jornada jornada);
    
    public List<BigDecimal> getListaJornadasRestriccion(Jornada jornada);
    
    public List<Jornada> getListaJornadasDisponibles(Dependencia dep, Long jorId);
    
     public Jornada getPorId(long id);
}
