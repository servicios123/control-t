/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.PosicionNacional;
import co.gov.aerocivil.controlt.services.PosicionNacionalService;
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
public class PosicionNacionalBBean {

    /**
     * Creates a new instance of PosicionNacionalBBean
     */
    @EJB
    private PosicionNacionalService posicionService;
    private PosicionNacional posicion;
    private PosicionNacional posicionFiltro;
    private List<PosicionNacional> lista;
    private List<PosicionNacional> listPosicionNacional;
    private boolean activo;
    private boolean editando;

    public boolean isEditando() {
        return editando;
    }

   

    public String crear() {
        editando = false;
        posicion = new PosicionNacional();

        DepCategoria dependencia = new DepCategoria();

        /*LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
         dependencia = logbbean.getFuncionarioTO().getFuncionario().getDependencia();*/
        posicion.setDepCategoria(dependencia);

        return "editarPosicionNacional";
    }

    public String editar() {
        editando = true;
        activo = this.posicion.getPnEstado().equals("Activo");
        return "editarPosicionNacional";
    }

    public String guardar() {
        if(!this.posicion.getPnEstado().equals("Activo") && activo){
            //verificar si puede inactivar
            if(!posicionService.validarInactivacion(posicion)){
                JsfUtil.addWarningMessage("posicionNacionalAsignada");
                return null;
            }
            
        }
        return guardarDB();
    }
    
    private String guardarDB(){
        //System.out.println("posicion.getDepCategoria().getDcId(): " + posicion.getDepCategoria().getDcId());
        try {
            /*LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
             posicion.setDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia());*/
            posicion.setPnAlias(posicion.getPnAlias().toUpperCase());
            posicionService.guardar(posicion, 
                JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
            return listar();
        } 
        catch (Exception ex) {
            JsfUtil.addWarningMessage("aliasPosicionNacionalUnico");
        }
        return null;
    }

    /**
     *
     * @return
     */
    public String listar() {
        posicionFiltro = new PosicionNacional();
        posicionFiltro.setDepCategoria(new DepCategoria());
        return filtrar();
    }

    public String filtrar() {

        lista = posicionService.getLista(posicionFiltro, null, null, null, null);
        return "listarPosicionNacional";
    }

    public PosicionNacional getPosicionNacional() {
        return posicion;
    }

    public void setPosicionNacional(PosicionNacional posicion) {
        this.posicion = posicion;
    }

    public PosicionNacional getPosicionNacionalFiltro() {
        return posicionFiltro;
    }

    public void setPosicionNacionalFiltro(PosicionNacional posicionFiltro) {
        this.posicionFiltro = posicionFiltro;
    }

    public List<PosicionNacional> getListPosicionNacional() {
        return listPosicionNacional;
    }

    public void setListPosicionNacional(List<PosicionNacional> listPosicionNacional) {
        this.listPosicionNacional = listPosicionNacional;
    }
    
     public List<PosicionNacional> getLista() {
        return lista;
    }

    public void setLista(List<PosicionNacional> lista) {
        this.lista = lista;
    }
}
