/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.DiarioPosicion;
import co.gov.aerocivil.controlt.entities.DiarioPosicionesIndividualVista;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.RepHorasExtras;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.swing.SortOrder;

/**
 *
 * @author Administrador
 */
@Local
public interface ControlDiarioPosicionesService {

    DiarioPosicion guardar(DiarioPosicion diarioPosicion, Funcionario f);

    public List<DiarioPosicion> getLista(DiarioPosicion diarioPosicionFiltro,
            Integer first, Integer pageSize, String sortField, 
        String sortOrder);

    public List<Vistaprogramacion> getListaTurnos(DiarioPosicion diarioPosicionFiltro,
            Integer first, Integer pageSize, String sortField, 
        String sortOrder);

    public Long getCount();

    public Funcionario getFuncionarioByAlias(String funAlias, Long idDependencia, Date fecha, 
            Long idJornada, Long idFunc);
    
    public void flush(DiarioPosicion diario);
    
    public List<Vistaprogramacion> getListaProgramadoVsEjecutado(Programacion filtro, 
            Funcionario funcionario,
            Integer first, Integer i, String sortField, String sortOrder);
    
    public List<DiarioPosicionesIndividualVista> getListaDiarioPosicionesIndividual(Funcionario f, Date fIni, Date fFin);
     
    public List<RepHorasExtras> getReporteHorasExtras(RepHorasExtras filtroReporte);
    
}
