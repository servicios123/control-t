/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.PosicionHabilitada;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Viviana
 */
@Local
public interface PosicionHabilitadaService {
    
    public PosicionHabilitada guardar(PosicionHabilitada posicionHabilitada, Funcionario f) throws SQLIntegrityConstraintViolationException;
    public List<PosicionHabilitada> getLista(PosicionHabilitada posicionJornada, Integer first, Integer pageSize, 
            String sortField, String sortOrder);
    public List<PosicionHabilitada> getLista(PosicionHabilitada posicionJornada);
    public void renovar(Funcionario funcionario,Funcionario funcionario_session);
    public void eliminar (Long[] listPosiciones, Funcionario funcionario, Funcionario funcSesion);
    public Object getCount();

   
  
}
