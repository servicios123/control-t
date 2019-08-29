/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Viviana
 */
@Local
public interface DependenciaService {
    
    public Dependencia guardar(Dependencia dependencia, Funcionario f) throws SQLIntegrityConstraintViolationException;
    
    public List<Dependencia> getLista(Dependencia dependencia, int first, int pageSize,
            String sortField, String sortOrder);    
    
    public Long getCount();
    
    public Dependencia findByd(Long id);

    public java.util.List<co.gov.aerocivil.controlt.entities.Dependencia> getDepsAts(Aeropuerto aeropuerto);

    public java.util.List<co.gov.aerocivil.controlt.entities.Dependencia> getLista(co.gov.aerocivil.controlt.entities.Dependencia dependencia);
    
}
