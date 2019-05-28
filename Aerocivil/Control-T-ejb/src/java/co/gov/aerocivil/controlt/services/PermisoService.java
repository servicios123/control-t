/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.PermisoEspecial;
import co.gov.aerocivil.controlt.entities.Programacion;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface PermisoService {
    public PermisoEspecial guardar (PermisoEspecial permiso, Funcionario f);
    public List<PermisoEspecial> getLista(PermisoEspecial permisoFiltro, int first, int i, String sortField, String sortOrder);
    public Long getCount();
    
    public void solvePetitions(Programacion programacion);

    public boolean existeProgramacion(PermisoEspecial permiso);

    public java.util.List<co.gov.aerocivil.controlt.entities.Jornada> getJornadasPermiso(co.gov.aerocivil.controlt.entities.PermisoEspecial permiso);
}
