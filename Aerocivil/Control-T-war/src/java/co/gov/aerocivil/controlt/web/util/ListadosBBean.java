/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.util;

/*import co.edu.udistrital.bolsa.business.facade.ListasFacade;
 import co.edu.udistrital.bolsa.enums.OpcionFechaEnum;
 import co.edu.udistrital.bolsa.persistence.entities.Ciudad;
 import co.edu.udistrital.bolsa.persistence.entities.Departamento;
 import co.edu.udistrital.bolsa.persistence.entities.EstadoCivil;
 import co.edu.udistrital.bolsa.persistence.entities.PersonaNatural;
 import co.edu.udistrital.bolsa.persistence.entities.Sector;
 import co.edu.udistrital.bolsa.persistence.entities.TipoIdentificacion;

 import co.edu.udistrital.bolsa.persistence.entities.Disponibilidad;
 import co.edu.udistrital.bolsa.persistence.entities.EstadoCivil;
 import co.edu.udistrital.bolsa.persistence.entities.EstadoEstudio;
 import co.edu.udistrital.bolsa.persistence.entities.Idioma;
 import co.edu.udistrital.bolsa.persistence.entities.Institucion;
 import co.edu.udistrital.bolsa.persistence.entities.NivelEstudio;
 import co.edu.udistrital.bolsa.persistence.entities.Sector;
 import co.edu.udistrital.bolsa.persistence.entities.TipoContrato;
 import co.edu.udistrital.bolsa.persistence.entities.TipoEmpleo;*/
import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Ciudad;
import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Departamento;
import co.gov.aerocivil.controlt.entities.Dependencia;

import co.gov.aerocivil.controlt.entities.DsTipo;
import co.gov.aerocivil.controlt.entities.DiaFestivo;


import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;

import co.gov.aerocivil.controlt.entities.Posicion;

import co.gov.aerocivil.controlt.entities.Menu;
import co.gov.aerocivil.controlt.entities.PosNoAsig;
import co.gov.aerocivil.controlt.entities.PosicionJornada;
import co.gov.aerocivil.controlt.entities.PosicionNacional;
import co.gov.aerocivil.controlt.entities.Programacion;

import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.ListasService;
import co.gov.aerocivil.controlt.web.enums.EstadoPeticionEnum;
import co.gov.aerocivil.controlt.web.enums.Months;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class ListadosBBean {

    private List<Regional> listaRegionales;
    private List<Aeropuerto> listaAeropuertos;
    private List<Dependencia> listaDependencias;
    private List<Departamento> listaDepartamentos;
    private List<Ciudad> listaCiudades;
    private List<DepCategoria> listaDepCategoria;
    private List<Posicion> listaPosicion;
    private List<PosicionNacional> listaPosicionNacional;
    private List<Funcionario> listFuncionario;
    private List<DsTipo> listaTipoSenal;
    private List<PosicionJornada> listaPosicionJornada;
    private EstadoPeticionEnum[] arrayEstadosPermiso;
    @EJB
    private ListasService listasService;
    private EstadoPeticionEnum[] arrayEstadosPermisoAprobacion;

    public List<Regional> getListaRegionales() {
        if (listaRegionales == null) {
            listaRegionales = listasService.obtenerLista(Regional.class);
        }
        return listaRegionales;
    }

    public List<Aeropuerto> getListaAeroXRegional(Long regional) {
        //System.out.println("consultando aeropuertos de regional con id:" + regional);
        return listasService.obtenerAeropuertosxRegional(regional);
        //return null;
    }

    public List<PosNoAsig> getListaPosNoAsig(Long dependencia, Long programacion) {
        return listasService.obtenerPosNoAsigXdependencia(dependencia, programacion);
        //return null;
    }

    public List<Funcionario> getListFuncionariosXPosicion(Long posicionJornadaId, Long FuncId) {
        return listasService.obtenerFuncionariosXPosicion(posicionJornadaId, FuncId);
        //return null;
    }

    public List<Funcionario> obtenerFuncionariosXPosicionXFecha(Long posicionJornadaId, Date fecha) {
        return listasService.obtenerFuncionariosXPosicionXFecha(posicionJornadaId, fecha);
        //return null;
    }

    public Programacion obtenerProgramacionXFecha(Date fecha) {
        return listasService.obtenerProgramacionXFecha(fecha);
        //return null;
    }

    public List<Funcionario> getListFuncionariosXDependencia(Long depId) {
        return listasService.obtenerFuncionariosXDependencia(depId);
        //return null;
    }

    public List<TurnoEspecial> getListaTurnoEspeciaXDependencia(Long dependencia) {
        return listasService.obtenerTurnoEspeciaXDependencia(dependencia);
        //return null;
    }

    public TurnoEspecial obtenerTurnoXDepAlias(TurnoEspecial te) {
        return listasService.obtenerTurnoXDepAlias(te);
    }

    public List<DsTipo> getListaTipoSenal() {
        return listasService.obtenerTipoSenal();
        //return null;
    }

    public List<Posicion> getListaPosicionXDependencia(Long dependencia) {
        return listasService.obtenerPosicionXDependencia(dependencia);
        //return null;
    }

    public List<PosicionJornada> getListaPosicionJornadaXDependencia(Long dependencia) {
        // if(listaPosicionJornada == null){
        listaPosicionJornada = listasService.obtenerPosicionJornadaXDependencia(dependencia);
        // }
        return listaPosicionJornada;
    }

    public List<Dependencia> getListaDepenXAerop(Long aeropuerto) {
        //System.out.println("consultando dependencias de aeropuerto con id:" + aeropuerto);
        return getListaDepenXAerop(aeropuerto, null);
    }

    public List<Dependencia> getListaDepenXAerop(Long aeropuerto, Long dcId) {
        if (dcId != null) {
            return listasService.obtenerDepenXAeropXDepCat(aeropuerto, dcId);
        } else {
            return listasService.obtenerDepenXAerop(aeropuerto);
        }

    }

    public List<Aeropuerto> getListaAeropuertos() {
        if (listaAeropuertos == null) {
            listaAeropuertos = listasService.obtenerLista(Aeropuerto.class);
        }
        return listaAeropuertos;
    }

    public List<Jornada> getListaJornadaXDependencia(Long dependencia) {
        return listasService.obtenerJornadaXDependencia(dependencia);
    }

    public void setListaRegionales(List<Regional> listaRegionales) {
        this.listaRegionales = listaRegionales;
    }

    public void setListaAeropuertos(List<Aeropuerto> listaAeropuertos) {
        this.listaAeropuertos = listaAeropuertos;
    }

    public List<Dependencia> getListaDependencias() {
        if (listaDependencias == null) {
            listaDependencias = listasService.obtenerLista(Dependencia.class);
        }
        return listaDependencias;
    }

    public void setListaDependencias(List<Dependencia> listaDependencias) {
        this.listaDependencias = listaDependencias;
    }

    public List<Departamento> getListaDepartamentos() {
        if (listaDepartamentos == null) {
            listaDepartamentos = listasService.obtenerLista(Departamento.class);
        }
        return listaDepartamentos;
    }

    public void setListaDepartamentos(List<Departamento> listaDepartamentos) {
        this.listaDepartamentos = listaDepartamentos;
    }

    public List<Ciudad> getListaCiudades() {
        if (listaCiudades == null) {
            listaCiudades = listasService.obtenerLista(Ciudad.class);
        }
        return listaCiudades;
    }

    public void setListaCiudades(List<Ciudad> listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public List<DepCategoria> getListaDepCategoria() {
        if (listaDepCategoria == null) {
            listaDepCategoria = listasService.obtenerLista(DepCategoria.class);
        }
        return listaDepCategoria;
    }

    public void setListaDepCategoria(List<DepCategoria> listaDepCategoria) {
        this.listaDepCategoria = listaDepCategoria;
    }

    public List<Funcionario> getListFuncionario() {
        return listFuncionario;
    }

    public void setListFuncionario(List<Funcionario> listFuncionario) {
        this.listFuncionario = listFuncionario;
    }

    public List<PosicionJornada> getListaPosicionJornada() {
        return listaPosicionJornada;
    }

    public void setListaPosicionJornada(List<PosicionJornada> listaPosicionJornada) {
        this.listaPosicionJornada = listaPosicionJornada;
    }

    public Object obtenerObjById(Class klass, Long id) {
        return listasService.obtenerObjById(klass, id);
    }

    List<Menu> getListaMenu(long rolId) {
        return listasService.getListaMenu(rolId);
    }

    public RolEnum[] getListaRoles() {
        return RolEnum.values();
    }

    public List<Posicion> getListaPosicion() {
        return listaPosicion;
    }

    public void setListaPosicion(List<Posicion> listaPosicion) {
        this.listaPosicion = listaPosicion;
    }

    public ListasService getListasService() {
        return listasService;
    }

    public void setListasService(ListasService listasService) {
        this.listasService = listasService;
    }

    public List<DiaFestivo> getListaFestivos(Date start, Date end) {
        return listasService.getListaFestivos(start, end,
                JsfUtil.getFuncionarioSesion());
    }

    public EstadoPeticionEnum[] getArrayEstadosPermisoAprobacion() {
        if (arrayEstadosPermisoAprobacion == null) {
            arrayEstadosPermisoAprobacion = new EstadoPeticionEnum[2];
            arrayEstadosPermisoAprobacion[0] = EstadoPeticionEnum.APROBADA;
            arrayEstadosPermisoAprobacion[1] = EstadoPeticionEnum.NEGADA;
        }
        return arrayEstadosPermisoAprobacion;
    }

    public EstadoPeticionEnum[] getArrayEstadosPermiso() {
        if (arrayEstadosPermiso == null) {
            arrayEstadosPermiso = new EstadoPeticionEnum[EstadoPeticionEnum.values().length];
            arrayEstadosPermiso = EstadoPeticionEnum.values();
        }
        return arrayEstadosPermiso;
    }

    public List<PosicionNacional> getListaPosicionNacional() {
        if (listaPosicionNacional == null) {
            listaPosicionNacional = listasService.obtenerLista(PosicionNacional.class);
        }
        return listaPosicionNacional;
    }
    private List<Months> meses;

    public List<Months> getMeses() {
        if (meses == null) {
            meses = new ArrayList<Months>(Arrays.asList(Months.values()));
        }
        return meses;
    }

    public Object getConnection() {
        try {
            return listasService.getConecction();
        } catch (SQLException ex) {
            Logger.getLogger(ListadosBBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Boolean isDiaFestivo(Date fecha) {
        return listasService.isDiaFestivo(fecha);
    }
    private HashMap<String, String> parametrosSistema;

    public HashMap<String, String> getParametrosSistema() {
        if (parametrosSistema == null) {
            parametrosSistema = listasService.getParametrosSistema();
        }
        return parametrosSistema;
    }
}
