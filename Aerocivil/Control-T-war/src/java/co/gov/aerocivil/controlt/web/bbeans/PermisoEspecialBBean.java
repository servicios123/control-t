/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.PermisoEspecial;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.entities.TurnoEspFuncionario;
import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.PermisoService;
import co.gov.aerocivil.controlt.services.TurnoEspFuncionarioService;
import co.gov.aerocivil.controlt.services.TurnoEspecialService;
import co.gov.aerocivil.controlt.web.enums.EstadoPeticionEnum;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class PermisoEspecialBBean {

    @EJB
    PermisoService service;
    @EJB
    private TurnoEspecialService turnoEspecialService;
    @EJB
    private TurnoEspFuncionarioService turnoEspFuncionarioService;
    private PermisoEspecial permiso;
    private PermisoEspecial permisoFiltro;
    private List<PermisoEspecial> lista;
    private Dependencia dependenciaFiltro;
    private List<Aeropuerto> listAeropuerto;
    private List<Dependencia> listDependencia;
    private List<Jornada> listJornada;
    private List<Jornada> listJornadaRequiere;
    private String jornadasMostrar;
    private Long[] jornadas;
    private boolean flagPermiso;
    private boolean aprobando;
    private Date current;

    public String crear() {
        jornadas = null;
        permiso = new PermisoEspecial();
        permiso.setFuncionario(JsfUtil.getFuncionarioSesion());
        permiso.setPeEstado(EstadoPeticionEnum.PENDIENTE.getLabel());
        permiso.setDependencia(permiso.getFuncionario().getDependencia());
        listJornada = JsfUtil.getListadosBBean().getListaJornadaXDependencia(permiso.getFuncionario().getDependencia().getDepId());
        return "crearPermiso";
    }

    public String guardarAprobacion() {
        TurnoEspFuncionario res = crearTurnoEspecialPeticion(permiso);
//        if (res == null) {
//            JsfUtil.addManualWarningMessage("No se puede aprobar debido a que ya presente un turno especial en la fecha seleccionada");
//            permiso = null;
//            return filtrar();
//        }
        PermisoEspecial result = service.guardar(permiso,
                JsfUtil.getFuncionarioSesion());

        permiso = null;
        JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
        return filtrar();
    }

    public TurnoEspFuncionario crearTurnoEspecialPeticion(PermisoEspecial especial) {
        Funcionario funSesion = JsfUtil.getFuncionarioSesion();
        List<Jornada> listaJornadaXDependencia = JsfUtil.getListadosBBean().getListaJornadaXDependencia(permiso.getFuncionario().getDependencia().getDepId());
        List<Jornada> jornadasPermiso = service.getJornadasPermiso(especial);
        if (listaJornadaXDependencia != null && jornadasPermiso != null) {
            if (listaJornadaXDependencia.size() > 0 && jornadasPermiso.size() > 0 && listaJornadaXDependencia.size() == jornadasPermiso.size()) {
                TurnoEspecial turnoEspecial = new TurnoEspecial();
                turnoEspecial.setTeSigla("DESC");
                turnoEspecial.setDependencia(especial.getFuncionario().getDependencia());
                TurnoEspecial result = turnoEspecialService.findBySigla(turnoEspecial);
                if (result == null) {
                    try {
                        turnoEspecial.setTeEstado("Activo");
                        turnoEspecial.setTeHinicio((byte) 0);
                        turnoEspecial.setTeMinicio((byte) 0);
                        turnoEspecial.setTeHfin((byte) 23);
                        turnoEspecial.setTeMfin((byte) 59);
                        turnoEspecial.setTeNombre("DESCANSO");
                        turnoEspecial.setTeHoraLaborada(true);
                        turnoEspecial.setTePermiteExtras(false);
                        result = turnoEspecialService.guardar(turnoEspecial, funSesion);
                    } catch (SQLIntegrityConstraintViolationException ex) {
                        Logger.getLogger(PermisoEspecialBBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                TurnoEspFuncionario tef = new TurnoEspFuncionario();
                tef.setFuncionario(especial.getFuncionario());
                tef.setTefEstado("Programado");
                tef.setTefFini(especial.getPeFechaPermiso());
                tef.setTefFfin(especial.getPeFechaPermiso());
                tef.setTurnoEspecial(result);
                try {
                    return turnoEspFuncionarioService.guardar(tef, especial.getFuncionario());
                } catch (Exception e) {
                    return null;
                }

            }
        }
        return null;
    }

    public String guardar() {
        Date today = new Date();
        if (permiso.getPeId() == null) {
            permiso.setPeFechaRegistro(new Date());
        }

        this.listJornadaRequiere = new ArrayList<Jornada>();
        if (jornadas == null || jornadas.length == 0) {
            JsfUtil.addWarningMessage("jornadaRequerida");
            return "crearPermiso";
        } else {
            //if (jornadas != null && jornadas.length>0) {
            for (Long i : jornadas) {
                Jornada j = new Jornada(i);
                j = (Jornada) JsfUtil.getListadosBBean().obtenerObjById(Jornada.class, j.getJoId());
                this.listJornadaRequiere.add(j);
            }
            if (permiso.getPeFechaPermiso().before(today)) {
                JsfUtil.addWarningMessage("peticioNoPermitePasado");
                return "crearPermiso";
            } else {
                permiso.setListaJornadas(listJornadaRequiere);
                service.guardar(permiso,
                        JsfUtil.getFuncionarioSesion());
                JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
                return crear();
            }
        }
    }

    public String listarXX() {

        FuncionarioBBean funBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);

        permisoFiltro = new PermisoEspecial();
        Funcionario funcionario = new Funcionario();
        funcionario.setDependencia(funBBean.getDependenciaXNivelUsuario());
        return "listarPermisos";
    }

    public String listar() {
        aprobando = false;
        return precargar();
    }

    public String precargar() {
        permisoFiltro = new PermisoEspecial();
        Aeropuerto aeropuerto = new Aeropuerto();
        Regional regional = new Regional();
        DepCategoria depcategoria = new DepCategoria();
        Funcionario f = new Funcionario();
        if (aprobando) {
            permisoFiltro.setPeEstado("Pendiente");
        }
        //f.setFuNivel(JsfUtil.getFuncionarioSesion().getFuNivel());
        permisoFiltro.setFuncionario(f);
        dependenciaFiltro = new Dependencia();
        listAeropuerto = null;

        aeropuerto.setRegional(regional);

        dependenciaFiltro.setAeropuerto(aeropuerto);
        dependenciaFiltro.setDepcategoria(depcategoria);

        FuncionarioBBean funcBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);

        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3, RolEnum.NIVEL_A4})) {
            dependenciaFiltro = funcBBean.getDependenciaXNivelUsuario();
        }
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
            listAeropuerto = funcBBean.getListAeropuerto();
        }
        permisoFiltro.setDependencia(dependenciaFiltro);
        flagPermiso = false;
        return filtrar();
    }

    public String listarVer() {
        aprobando = false;
        String ret = precargar();
        flagPermiso = true;
        return ret;
    }

    public String filtrar() {
        lista = service.getLista(permisoFiltro, 0, 0, null, null);
        return "listarPermisos";
    }

    public List<PermisoEspecial> getLista() {
        return lista;
    }

    public void setLista(List<PermisoEspecial> lista) {
        this.lista = lista;
    }

    public PermisoEspecial getPermisoFiltro() {
        return permisoFiltro;
    }

    public void setPermisoFiltro(PermisoEspecial permisoFiltro) {
        this.permisoFiltro = permisoFiltro;
    }

    public Dependencia getDependenciaFiltro() {
        return dependenciaFiltro;
    }

    public void setDependenciaFiltro(Dependencia dependenciaFiltro) {
        this.dependenciaFiltro = dependenciaFiltro;
    }

    public PermisoEspecial getPermiso() {
        return permiso;
    }

    public void setPermiso(PermisoEspecial permiso) {
        this.permiso = permiso;
    }

    public void cargarAeropuertoLista() {
        listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(dependenciaFiltro.getAeropuerto().getRegional().getRegId());
    }

    public List<Aeropuerto> getListAeropuerto() {
        return listAeropuerto;
    }

    public String aprobar() {
        aprobando = true;
        String ret = precargar();
        return ret;
    }

    public String seleccionar() {

        jornadasMostrar = new String();

        for (Jornada j : permiso.getListaJornadas()) {

            jornadasMostrar = jornadasMostrar + " " + j.getJoAlias();
        }



        return "aprobarPermiso";
    }

    public void cargarDependenciaLista() {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(permisoFiltro.getDependencia().getAeropuerto().getAeId(),
                    JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria().getDcId());
        } else {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(permisoFiltro.getDependencia().getAeropuerto().getAeId());
        }

    }

    public List<Dependencia> getListDependencia() {
        return listDependencia;
    }

    public void setListDependencia(List<Dependencia> listDependencia) {
        this.listDependencia = listDependencia;
    }

    public List<Jornada> getListJornada() {
        return listJornada;
    }

    public Long[] getJornadas() {
        return jornadas;
    }

    public void setJornadas(Long[] jornadas) {
        this.jornadas = jornadas;
    }

    public String getJornadasMostrar() {
        return jornadasMostrar;
    }

    public void setJornadasMostrar(String jornadasMostrar) {
        this.jornadasMostrar = jornadasMostrar;
    }

    public List<Jornada> getListJornadaRequiere() {
        return listJornadaRequiere;
    }

    public void setListJornadaRequiere(List<Jornada> listJornadaRequiere) {
        this.listJornadaRequiere = listJornadaRequiere;
    }

    public boolean isFlagPermiso() {
        return flagPermiso;
    }

    public void setFlagPermiso(boolean flagPermiso) {
        this.flagPermiso = flagPermiso;
    }

    public boolean isAprobando() {
        return aprobando;
    }

    /**
     * @return the current
     */
    public Date getCurrent() {
        if (current == null) {
            Calendar c = Calendar.getInstance();
            current = c.getTime();
        }
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(Date current) {
        this.current = current;
    }
}