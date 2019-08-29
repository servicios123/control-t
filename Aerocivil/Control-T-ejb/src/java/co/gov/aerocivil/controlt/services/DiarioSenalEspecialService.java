/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.DiarioSenalCategoria;
import co.gov.aerocivil.controlt.entities.DiarioSenalCierreTurno;
import co.gov.aerocivil.controlt.entities.DiarioSenalEspecial;
import co.gov.aerocivil.controlt.entities.DiarioSenalFuncionario;
import co.gov.aerocivil.controlt.entities.DiarioSenalInfo;
import co.gov.aerocivil.controlt.entities.DiarioSenalLocalizacion;
import co.gov.aerocivil.controlt.entities.DiarioSenalTipo;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.Posicion;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface DiarioSenalEspecialService {
    
    /*CONSULTAS*/
    
    public Jornada obtenerJornadaPorHora(int hora, long dependencia);
    public DiarioSenalEspecial obtenerDsePorFecha(Date fecha,long jornada);
    public DiarioSenalEspecial obtenerPorId(long dse);
    public DiarioSenalFuncionario obtenerDsfPorDse(long dse, long funcionario);
    public DiarioSenalFuncionario obtenerDsfPorId(long dsf);
    
    public Posicion obtenerPosicionPorTurnoFunc(Date fecha,long jornada,long funcionario);
    public List<Posicion> obtenerPosicionesDelFunc(long funcionario);
    public List<DiarioSenalTipo> obtenerTipos();
    public List<DiarioSenalInfo> diarioSenalInfosPorDse(long diarioSenalEspecial);
    public List<DiarioSenalInfo> diarioSenalInfosPorDseDst(long diarioSenalEspecial,long diarioSenalTipo);
    public DiarioSenalInfo obtenerDsiPorId(long id);
    public List<DiarioSenalCategoria> obtenerCategorias();
    public DiarioSenalCategoria obtenerCategoriaPorId(long id);
    public DiarioSenalFuncionario obtenerDsfPorDseAnterior(long fun);
    
    public List<DiarioSenalLocalizacion> obtenerDSL();
    public DiarioSenalTipo obtenerTipoPorId(long id);
    public DiarioSenalLocalizacion obtenerDslPorId(long id);
    public List<Object[]> cierreTurno(long dse);
    
    public List<DiarioSenalLocalizacion> filtroLocalizaciones(String nombre);
    
    public List<DiarioSenalEspecial> getLista(DiarioSenalEspecial diarioSenalEspecial, Integer first, Integer pageSize,String sortField, String sortOrder);
    public Long getCount();
    
    public List<DiarioSenalFuncionario> obtenerDsfsPorDse(long diarioSenalEspecial, String estado);
            
    
    
    /*ACCIONES*/
     
    public DiarioSenalEspecial guardarDse(DiarioSenalEspecial diarioSenalEspecial,Funcionario funcionario) throws Exception;
    public DiarioSenalFuncionario guardarDsf(DiarioSenalFuncionario diarioSenalFuncionario, Funcionario funcionario);
    public DiarioSenalInfo guardarDsi(DiarioSenalInfo diarioSenalInfo, Funcionario funcionario);
    public void eliminarDSI(DiarioSenalInfo dsi);

    public java.util.List<co.gov.aerocivil.controlt.entities.DiarioSenalEspecial> getLista(co.gov.aerocivil.controlt.entities.DiarioSenalEspecial diarioSenalEspecial);
}
