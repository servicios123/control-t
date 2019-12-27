/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.PosicionJornada;
import co.gov.aerocivil.controlt.entities.Solicitud;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.swing.SortOrder;


/**
 *
 * @author Viviana
 */
@Local
public interface SolicitudService {
    
    public Solicitud guardar(Solicitud solicitud, Funcionario f) ;
    public Solicitud actualizar(Solicitud solicitud, Funcionario f) ;
    
    public List<Solicitud> getLista(Solicitud solicitud, int first, int pageSize, 
            String sortField, String sortOrder);   
    public Long contarAprobados(Solicitud solicitud, Date Finicial, Date Ffinal);
    public List<PosicionJornada> obtenerPJenProgramacion(Date fecha, Funcionario fun);
            
    public Long getCount();
    public Solicitud getId(Long id);
}
