/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;
 
import co.gov.aerocivil.controlt.entities.Auditoria;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.DiarioPosicion;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.PermisoEspecial;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import co.gov.aerocivil.controlt.services.AuditoriaService;
import co.gov.aerocivil.controlt.web.lazylist.AuditoriaLazyList;
import co.gov.aerocivil.controlt.web.lazylist.PermisoLazyList;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class AuditoriaBBean {
    
    @EJB
    private AuditoriaService service;
    
    private Dependencia dependenciaFiltro;
    private Auditoria auditoriaFiltro;

    private LazyDataModel<Auditoria> lista;

    private void precargarFiltros() {
        FuncionarioBBean funcBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        funcBBean.inicializarPickList();
        dependenciaFiltro = funcBBean.getFuncionarioFiltro().getDependencia();
        Funcionario fun = new Funcionario();
        fun.setDependencia(dependenciaFiltro);
        auditoriaFiltro = new Auditoria();
        auditoriaFiltro.setFuncionario(fun);
        //System.out.println("dep::::::"+auditoriaFiltro.getFuncionario().getDependencia().getDepNombre());
        //System.out.println("dep::::::"+dependenciaFiltro.getDepNombre());
    }
    
    public String listar(){
        precargarFiltros();
        return filtrar();
    }
    public String listarFuncionarios(){
        FuncionarioBBean funcBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        funcBBean.inicializarPickList();
        dependenciaFiltro = funcBBean.getFuncionarioFiltro().getDependencia();
        Funcionario fun = new Funcionario();
        fun.setDependencia(dependenciaFiltro);
        auditoriaFiltro = new Auditoria();
        auditoriaFiltro.setFuncionario(fun);
        return filtrarFuncionarios();
    }
    public String filtrarFuncionarios(){
    
        FuncionarioBBean funcBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        auditoriaFiltro.setFuncionario(funcBBean.getFuncionarioFiltro());
        auditoriaFiltro.setAudTabla("FUNCIONARIO");
 /*       //System.out.println("dep::::::"+auditoriaFiltro.getFuncionario().getDependencia().getDepNombre());
        //System.out.println("dep::::::"+dependenciaFiltro.getDepNombre());*/
        lista = new AuditoriaLazyList(service, auditoriaFiltro);
        return "listarFuncionarioAud";
    }
    
    public String filtrar() {
        FuncionarioBBean funcBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        auditoriaFiltro.setFuncionario(funcBBean.getFuncionarioFiltro());
        //System.out.println("dep::::::"+auditoriaFiltro.getFuncionario().getDependencia().getDepNombre());
        //System.out.println("dep::::::"+dependenciaFiltro.getDepNombre());
        lista = new AuditoriaLazyList(service, auditoriaFiltro);
        return "listarAuditoria";
    }
    
    public Auditoria getAuditoriaFiltro() {
        return auditoriaFiltro;
    }

    public void setAuditoriaFiltro(Auditoria auditoriaFiltro) {
        this.auditoriaFiltro = auditoriaFiltro;
    }

    public LazyDataModel<Auditoria> getLista() {
        return lista;
    }

    public void setLista(LazyDataModel<Auditoria> lista) {
        this.lista = lista;
    }

    public Dependencia getDependenciaFiltro() {
        return dependenciaFiltro;
    }

    public void setDependenciaFiltro(Dependencia dependenciaFiltro) {
        this.dependenciaFiltro = dependenciaFiltro;
    }    

}
