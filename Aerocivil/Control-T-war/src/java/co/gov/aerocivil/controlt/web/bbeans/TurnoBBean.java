/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.entities.Turno;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.TurnoService;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Viviana
 */
@ManagedBean
@SessionScoped
public class TurnoBBean {

    /**
     * Creates a new instance of TurnoBBean
     */
    
    
    @EJB
     private TurnoService turnoService;
    private Turno turno;
    private Turno turnoFiltro;
    private Dependencia dependencia;
    private Funcionario funcionario;
    private Programacion programacion;
    private List<Dependencia> listDependencia;
    private List<Turno> lista;
    private List<Aeropuerto> listAeropuerto;
    private Aeropuerto aeropuerto;
    private Regional regional;
  
    public TurnoBBean() {
    }
    
    public String listar() {
        turnoFiltro = new Turno();

        regional = new Regional();
        dependencia = new Dependencia();
        aeropuerto = new Aeropuerto();
        programacion= new Programacion();
        funcionario=new Funcionario();        
       
        
        DepCategoria depCategoria= new DepCategoria();
        funcionario = new Funcionario();

        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);
        dependencia.setDepcategoria(depCategoria);
        programacion.setDependencia(dependencia);
        turnoFiltro.setProgramacion(programacion);
        turnoFiltro.setFuncionario(funcionario);
        
        
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2})) {
  
        dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
       programacion.setDependencia(dependencia);
        
        turnoFiltro.setProgramacion(programacion);
        cargarDependenciaListado();
   
        }
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A3})) {
        
            
        regional.setRegId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
        aeropuerto.setRegional(regional);
        dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
        dependencia.setAeropuerto(aeropuerto);
         programacion.setDependencia(dependencia);
        
        turnoFiltro.setProgramacion(programacion);
        
        cargarAeropuertoListado();
   
        }
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A4})) {
       
            dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
             programacion.setDependencia(dependencia);
        
        turnoFiltro.setProgramacion(programacion);
            
        }
      
        
        
      
        //
        return filtrar();
      
    }
    
    
   public String filtrar() {
       /*cargarAeropuerto();
       cargarDependencia();*/
       
       
       
        lista = turnoService.getLista(turnoFiltro, 0, 0, null, null);
        return "listarTurnoEspFuncionario";
    }

   
    public void cargarAeropuerto() {
        cargarAeropuerto(turno);
    }

    public void cargarAeropuertoListado() {

        cargarAeropuerto(turnoFiltro);
    }

    private void cargarAeropuerto(Turno f) {
        listDependencia = null;
        listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(f.getProgramacion().getDependencia().getAeropuerto().getRegional().getRegId());
    }

    public void cargarDependencia() {
        cargarDependencia(turno);
    }

    public void cargarDependenciaListado() {
        cargarDependencia(turnoFiltro);
    }

    private void cargarDependencia(Turno f) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getProgramacion().getDependencia().getAeropuerto().getAeId(),
                    JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria().getDcId());
        } else {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getProgramacion().getDependencia().getAeropuerto().getAeId());
        }

    }
    
    public TurnoService getTurnoService() {
        return turnoService;
    }

    public void setTurnoService(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Turno getTurnoFiltro() {
        return turnoFiltro;
    }

    public void setTurnoFiltro(Turno turnoFiltro) {
        this.turnoFiltro = turnoFiltro;
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Programacion getProgramacion() {
        return programacion;
    }

    public void setProgramacion(Programacion programacion) {
        this.programacion = programacion;
    }

    public List<Dependencia> getListDependencia() {
        return listDependencia;
    }

    public void setListDependencia(List<Dependencia> listDependencia) {
        this.listDependencia = listDependencia;
    }

    public List<Turno> getLista() {
        return lista;
    }

    public void setLista(List<Turno> lista) {
        this.lista = lista;
    }

    public List<Aeropuerto> getListAeropuerto() {
        return listAeropuerto;
    }

    public void setListAeropuerto(List<Aeropuerto> listAeropuerto) {
        this.listAeropuerto = listAeropuerto;
    }

    public Aeropuerto getAeropuerto() {
        return aeropuerto;
    }

    public void setAeropuerto(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
    }

    public Regional getRegional() {
        return regional;
    }

    public void setRegional(Regional regional) {
        this.regional = regional;
    }
}
