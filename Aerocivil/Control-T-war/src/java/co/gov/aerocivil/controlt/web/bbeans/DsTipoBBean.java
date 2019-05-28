/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.DsTipo;
import co.gov.aerocivil.controlt.services.DepCategoriaService;
import co.gov.aerocivil.controlt.services.DsTipoService;
import co.gov.aerocivil.controlt.web.lazylist.DsTipoLazyList;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author juancamilo
 */
@ManagedBean
@SessionScoped
public class DsTipoBBean {

    @EJB
    private DsTipoService tipoService;
    @EJB
    private DepCategoriaService categoriaService;
    private DsTipo tipo;
    private DsTipo tipoFiltro;
    private LazyDataModel<DsTipo> lista;
    private List<DepCategoria> listaCategorias;

    /**
     * Creates a new instance of ParametrosDependenciaBBean
     */
    public DsTipoBBean() {
        tipo = new DsTipo();
        tipoFiltro = new DsTipo();
    }

    public String guardar() {
        try {
            tipo = this.tipoService.guardar(tipo, JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
            return filtrar();
        } catch (Exception ex) {
            JsfUtil.addWarningMessage("SQLIntegrityConstraintViolationException");
        }
        return null;
    }

    public String listar() {
        tipoFiltro = new DsTipo();
        DepCategoria categoria = new DepCategoria();
        tipoFiltro.setCategoria(categoria);
        cargarFiltrosDependencia();
        return filtrar();
    }

    public void cargarFiltrosDependencia() {
        cargarCategorias();
    }

    public String editar() {
        cargarFiltrosDependencia();
        return "crearDsTipo";
    }

    public String crear() {
        tipo = new DsTipo();
        DepCategoria categoria = new DepCategoria();
        tipo.setCategoria(categoria);
        cargarFiltrosDependencia();
        return "crearDsTipo";
    }

    public void cargarCategorias() {
        listaCategorias = categoriaService.getLista();
    }

    public String filtrar() {
        tipoFiltro.setCategoria(new DepCategoria());
        lista = new DsTipoLazyList(this.tipoService, tipoFiltro);
        return "listarDsTipo";
    }

    public DsTipo getTipo() {
        return tipo;
    }

    public void setTipo(DsTipo tipo) {
        this.tipo = tipo;
    }

    public DsTipo getTipoFiltro() {
        return tipoFiltro;
    }

    public void setTipoFiltro(DsTipo tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
    }

    public LazyDataModel<DsTipo> getLista() {
        return lista;
    }

    public void setLista(LazyDataModel<DsTipo> lista) {
        this.lista = lista;
    }

    public List<DepCategoria> getListaCategorias() {
        return listaCategorias;
    }

    public void setListaCategorias(List<DepCategoria> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }
    
    
    
}
