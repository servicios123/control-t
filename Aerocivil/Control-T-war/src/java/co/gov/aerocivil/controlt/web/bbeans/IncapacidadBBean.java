/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.services.IncapacidadService;
import co.gov.aerocivil.controlt.to.IncapacidadTO;
import co.gov.aerocivil.controlt.to.IncapacidadVista;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class IncapacidadBBean {

    @EJB
    private IncapacidadService service;
    private Dependencia dependenciaFiltro;
    private IncapacidadTO filtro;
    private int groupBy;
    private List<IncapacidadVista> lista;
    private Long total;

    private void precargarFiltros() {
        FuncionarioBBean funcBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        funcBBean.inicializarPickList();
        dependenciaFiltro = funcBBean.getFuncionarioFiltro().getDependencia();
        Funcionario fun = new Funcionario();
        fun.setDependencia(dependenciaFiltro);
        filtro = new IncapacidadTO();
        filtro.setFuncionario(fun);
        //System.out.println("dep::::::"+filtro.getFuncionario().getDependencia().getDepNombre());
        //System.out.println("dep::::::"+dependenciaFiltro.getDepNombre());
    }
    private String[] groupArray = new String[]{"regional", "aeropuerto", "dependencia"};

    public String listar() {
        lista = null;
        total = null;
        precargarFiltros();
        return "listarIncapacidades";
    }

    public String filtrar() {

        FuncionarioBBean funcBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        filtro.setFuncionario(funcBBean.getFuncionarioFiltro());
        //System.out.println("dep::::::"+filtro.getFuncionario().getDependencia().getDepNombre());
        //System.out.println("dep::::::"+dependenciaFiltro.getDepNombre());
        lista = service.getLista(filtro, null, null,
                null, null, groupArray[groupBy]);
        try {
            this.total = service.getTotal(filtro, groupArray[groupBy]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "listarIncapacidades";
    }

    public IncapacidadTO getIncapacidadFiltro() {
        return filtro;
    }

    public void setIncapacidadFiltro(IncapacidadTO auditoriaFiltro) {
        this.filtro = auditoriaFiltro;
    }

    public List<IncapacidadVista> getLista() {
        return lista;
    }

    public void setLista(List<IncapacidadVista> lista) {
        this.lista = lista;
    }

    public Dependencia getDependenciaFiltro() {
        return dependenciaFiltro;
    }

    public void setDependenciaFiltro(Dependencia dependenciaFiltro) {
        this.dependenciaFiltro = dependenciaFiltro;
    }

    public int getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(int groupBy) {
        this.groupBy = groupBy;
    }

    public Long getTotal() {
        return total;
    }
}
