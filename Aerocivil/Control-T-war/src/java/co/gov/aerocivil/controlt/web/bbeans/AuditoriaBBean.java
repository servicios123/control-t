/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;
 
import co.gov.aerocivil.controlt.entities.Auditoria;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.services.AuditoriaService;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.List;
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

    private List<Auditoria> lista;

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
        lista = service.getLista(auditoriaFiltro);
        return "listarFuncionarioAud";
    }
    
    public String filtrar() {
        FuncionarioBBean funcBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        auditoriaFiltro.setFuncionario(funcBBean.getFuncionarioFiltro());
        //System.out.println("dep::::::"+auditoriaFiltro.getFuncionario().getDependencia().getDepNombre());
        //System.out.println("dep::::::"+dependenciaFiltro.getDepNombre());
        lista = service.getLista(auditoriaFiltro);
        return "listarAuditoria";
    }
    
    public Auditoria getAuditoriaFiltro() {
        return auditoriaFiltro;
    }

    public void setAuditoriaFiltro(Auditoria auditoriaFiltro) {
        this.auditoriaFiltro = auditoriaFiltro;
    }

    public List<Auditoria> getLista() {
        return lista;
    }

    public void setLista(List<Auditoria> lista) {
        this.lista = lista;
    }

    public Dependencia getDependenciaFiltro() {
        return dependenciaFiltro;
    }

    public void setDependenciaFiltro(Dependencia dependenciaFiltro) {
        this.dependenciaFiltro = dependenciaFiltro;
    }    

}
