/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Turno;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Viviana
 */
@Local
public interface TurnoService {
    
    public Turno guardar(Turno turno, Funcionario f);
    public List<Turno> getLista(Turno turno, int first, int pageSize, 
            String sortField, String sortOrder);
    
    public Long getCount(); 
    
    public void corregirTipos(Date turFecha, Long proId, Long funId, Funcionario f);
    
}
