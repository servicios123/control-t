/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.enums.EstadoProgramacion;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface ProgramacionTurnosSession {

 public boolean isFechaProgramada(Date d, Long depId, EstadoProgramacion estado);
    public void generarProgramacion(Programacion programacion) throws SQLException;
    public Programacion guardar(Programacion programacion, Funcionario f);
    public void borrarPendientes(Programacion programacion);
     public Date fechaUltimaProgramacion(Dependencia dependencia);
     public List<Programacion> getListaPag(Programacion programacion, int first, int pageSize,
            String sortField, String sortOrder);
     public boolean existeProgramacion(Dependencia dependecia, Date fecha);
         
    public Long getCount();
}
