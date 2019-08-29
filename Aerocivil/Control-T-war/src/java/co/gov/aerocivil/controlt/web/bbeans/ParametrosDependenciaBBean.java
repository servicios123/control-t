/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.ParametrosDependencia;
import co.gov.aerocivil.controlt.services.DepCategoriaService;
import co.gov.aerocivil.controlt.services.ListasService;
import co.gov.aerocivil.controlt.services.ParametroDependenciaService;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author juancamilo
 */
@ManagedBean
@SessionScoped
public class ParametrosDependenciaBBean {

    @EJB
    private ParametroDependenciaService dependenciaService;
    @EJB
    private DepCategoriaService categoriaService;
    @EJB
    private ListasService listaService;
    private ParametrosDependencia dependencia;
    private ParametrosDependencia dependenciaFiltro;
    private List<ParametrosDependencia> lista;
    private List<DepCategoria> listaCategorias;
    private List<ParametrosDependencia> listaParametros;
    private List<String> listaModulos;

    /**
     * Creates a new instance of ParametrosDependenciaBBean
     */
    public ParametrosDependenciaBBean() {
        dependencia = new ParametrosDependencia();
        dependenciaFiltro = new ParametrosDependencia();
    }

    public List<ParametrosDependencia> listarTiposParametrosDependencias() {
        return dependenciaService.listarTiposParametros();

    }

    public ParametrosDependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(ParametrosDependencia dependencia) {
        this.dependencia = dependencia;
    }

    public ParametrosDependencia getDependenciaFiltro() {
        return dependenciaFiltro;
    }

    public void setDependenciaFiltro(ParametrosDependencia dependenciaFiltro) {
        this.dependenciaFiltro = dependenciaFiltro;
    }

    public List<ParametrosDependencia> getLista() {
        return lista;
    }

    public void setLista(List<ParametrosDependencia> lista) {
        this.lista = lista;
    }

    public List<DepCategoria> getListaCategorias() {
        return listaCategorias;
    }

    public void setListaCategorias(List<DepCategoria> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

    public List<String> getListaModulos() {
        return listaModulos;
    }

    public void setListaModulos(List<String> listaModulos) {
        this.listaModulos = listaModulos;
    }

    public List<ParametrosDependencia> getListaParametros() {
        return listaParametros;
    }

    public void setListaParametros(List<ParametrosDependencia> listaParametros) {
        this.listaParametros = listaParametros;
    }

    public String guardar() {
        try {
            dependencia = this.dependenciaService.guardaParametroDependencia(dependencia, JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
            return filtrar();
        } catch (Exception ex) {
            JsfUtil.addWarningMessage("SQLIntegrityConstraintViolationException");
        }
        return null;
    }

    public String listar() {
        dependenciaFiltro = new ParametrosDependencia();
        DepCategoria categoria = new DepCategoria();
        dependenciaFiltro.setCategoria(categoria);
        cargarFiltrosDependencia();
        return filtrar();
    }

    public void cargarFiltrosDependencia() {
        cargarCategorias();
        cargarMenu();
    }

    public String editar() {
        cargarFiltrosDependencia();
        return "crearParametroDependencia";
    }

    public String crear() {
        dependencia = new ParametrosDependencia();
        DepCategoria categoria = new DepCategoria();
        dependencia.setCategoria(categoria);
        cargarFiltrosDependencia();
        return "crearParametroDependencia";
    }

    public void cargarCategorias() {
        listaCategorias = categoriaService.getLista();
    }

    public void cargarMenu() {
        listaModulos = new ArrayList<String>();
        listaModulos.add("Curso Recurrente");
        listaModulos.add("Evaluación Competencias");
    }

    public String filtrar() {
        dependenciaFiltro.setCategoria(new DepCategoria());
        lista = this.dependenciaService.getLista(dependenciaFiltro, 0, 0, null, null);
        return "listarParametrosDependencia";
    }
}
