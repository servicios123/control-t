/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Comision;
import co.gov.aerocivil.controlt.services.ComisionSession;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ComisionBBean {

    @EJB
    private ComisionSession service;
    
    private Comision comision;
    
    public String guardar(){
        JsfUtil.addSuccessMessage("");
        service.guardar(comision, 
                JsfUtil.getFuncionarioSesion());        
        return "";
    }
    
    public String listarFuncionarios(){
        FuncionarioBBean funBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        funBBean.setRetorno("comisionBBean.crear");
        return funBBean.listar();
    }
    
    public String crear(){
        comision = new Comision();
        comision.setDependencia(JsfUtil.getFuncionarioSesion().getDependencia());
        //FuncionarioBBean funBBean = (FuncionarioBBean) JsfUtil.getManagedBean(FuncionarioBBean.class);
        //comision.setFuncionario(funBBean.getFuncionario());
        return "crearComision";
    }

    public Comision getComision() {
        return comision;
    }

    public void setComision(Comision comision) {
        this.comision = comision;
    }
    
}
