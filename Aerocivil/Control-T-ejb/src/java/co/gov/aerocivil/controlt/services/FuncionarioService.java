/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.CursoRecurrente;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.to.FuncionarioTransporteTO;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */

@Local
public interface FuncionarioService {
    
    public Funcionario getPersonaByLoginPassword(String login, String clave); 
    public Funcionario guardar(Funcionario funcionario, Funcionario fSession)throws SQLIntegrityConstraintViolationException;
    public Funcionario getFuncionarioById(Funcionario funcionario);
        
    public List<Funcionario> getListaPag(Funcionario funcionario, Integer first, Integer pageSize, 
            String sortField, String sortOrder);
    
    public Long getCount();

    public List<Funcionario> getFuncionarios600am(Dependencia dependencia, Date d, Long hInicio, Long mInicio, Long hFin, Long mFin);

    public Funcionario buscarSITAH(Long funId);
    public Funcionario bloquearUsuario(String login);    
    public Funcionario getJefeDependencia(Long depId);
    
    public Funcionario getJefeRegional(Long regId);
    public boolean validarAlias(Funcionario func, Dependencia dep);
}
