/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Auditoria;
import co.gov.aerocivil.controlt.entities.Funcionario;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
@Local
public interface AuditoriaService {
    public Object auditar(Object entity, Funcionario func);
    public void auditarDelete(String tabla, Funcionario func, Long posId); 
    public Long getCount();
    public List<Auditoria> getLista(Auditoria auditoriaFiltro, Integer first, Integer pageSize, String sortField, String sortOrder);

    public void auditarDelete(java.lang.String tabla, co.gov.aerocivil.controlt.entities.Funcionario func, java.lang.Long posId, java.lang.Long id);
}
