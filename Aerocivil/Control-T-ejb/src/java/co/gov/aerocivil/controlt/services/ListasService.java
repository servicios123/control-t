/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.DsTipo;
import co.gov.aerocivil.controlt.entities.DsTipo;
import co.gov.aerocivil.controlt.entities.DiaFestivo;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.Menu;
import co.gov.aerocivil.controlt.entities.PosNoAsig;
import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.entities.PosicionJornada;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
@Local
public interface ListasService {
    public List obtenerLista(Class klass);
    public List<Regional> obtenerListaRegionales();
    public List<Aeropuerto> obtenerAeropuertosxRegional( Long regional);
    public List<Dependencia> obtenerDepenXAerop( Long aeropuerto);
     public List<Dependencia> obtenerDepenXAeropXDepCat( Long aeropuerto,  Long dcId);    
    public List<Posicion> obtenerPosicionXDependencia(Long dependencia);
    public List<Jornada> obtenerJornadaXDependencia(Long dependencia);
    public Object obtenerObjById(Class klass, Long id);
    public List<Menu> getListaMenu(Long rolId);
    public List<DsTipo> obtenerTipoSenal();


    public void guardarFestivos(List<Date> festivos, List<Date> diasEliminar, Funcionario f);

    public List<DiaFestivo> getListaFestivos(Date start, Date end, Funcionario f);

    public List<PosicionJornada> obtenerPosicionJornadaXDependencia(Long dependencia);
    public List<TurnoEspecial> obtenerTurnoEspeciaXDependencia(Long dependencia);
    public List<Funcionario> obtenerFuncionariosXPosicion( Long posicionJornada, Long FuncId);
    public List<Funcionario> obtenerFuncionariosXDependencia( Long dependencia);

    public List<PosNoAsig> obtenerPosNoAsigXdependencia(Long dependencia, Long programacion);
    public HashMap<String, String> getParametrosSistema();
    public Connection getConecction() throws SQLException;
    public EntityManager getEntManager();

    public Boolean isDiaFestivo(Date fecha);
    
    public List<Funcionario> obtenerFuncionariosXPosicionXFecha(Long posicionJornada, Date fecha);
    public Programacion obtenerProgramacionXFecha(Date Fecha);
    
    public TurnoEspecial obtenerTurnoXDepAlias(TurnoEspecial te);

    public java.util.List<TurnoEspecial> obtenerTurnoEspeciaXDependenciaActivo(Long dependencia);
    
}
