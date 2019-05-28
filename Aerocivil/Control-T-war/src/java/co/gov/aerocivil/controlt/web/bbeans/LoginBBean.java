/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Menu;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.FuncionarioService;
import co.gov.aerocivil.controlt.services.ListasService;
import co.gov.aerocivil.controlt.to.FuncionarioTO;
import co.gov.aerocivil.controlt.enums.ParametrosEnum;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import co.gov.aerocivil.controlt.web.util.MailUtil;
import co.gov.aerocivil.controlt.web.util.MenuBBean;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
@SessionScoped
public class LoginBBean {
    
    
    
    @EJB
    private ListasService listasService;

    private FuncionarioTO funcionarioTO = new FuncionarioTO();
    //private
    private boolean logged = false;
    
    @EJB
    private FuncionarioService funcionarioSession;
    
    

    public String iniciarSesion() {
        
        Funcionario funcionario = funcionarioSession.getPersonaByLoginPassword(funcionarioTO.getLogin(), funcionarioTO.getClave());
        if (funcionario != null) {
            funcionarioTO.setClave(null);
            funcionarioTO.setFuncionario(funcionario);
            JsfUtil.getServletRequest().getSession().setAttribute("FuncionarioTO", funcionarioTO);            
            //return "logedIn";
            JsfUtil.getServletRequest().getSession().setAttribute("menuBBean", null);
            MenuBBean menuBBean = (MenuBBean) JsfUtil.getManagedBean(MenuBBean.class);            
            return menuBBean.gotoInicio();
        }
        funcionario = funcionarioSession.bloquearUsuario(funcionarioTO.getLogin());
        if (funcionario != null && funcionario.getIntentosFallidos()==3L) {
            JsfUtil.addWarningMessage("maxIntentosFallidos");    
        }
        else if (funcionario != null && funcionario.getIntentosFallidos()>3L) {
            JsfUtil.addWarningMessage("cuentaBloqueada");                
        }
        else{
            JsfUtil.addWarningMessage("sesionNoIniciada");
        }

        funcionarioTO = new FuncionarioTO();
        return null;
    }

    public void finalizarSesion(ActionEvent ev) {
        funcionarioTO = new FuncionarioTO();
        //usuario.setLoggedIn(false);
        JsfUtil.getServletRequest().getSession().setAttribute("FuncionarioTO", null);
        //FacesUtils.getMB().setError(false);
        JsfUtil.getServletRequest().getSession(false).invalidate();

        try {
            JsfUtil.getFacesContext().getExternalContext().redirect(JsfUtil.getServletRequest().getContextPath() + "/logout.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * Evalua si el funcionario que está en sesion tiene el niveles @param rol
     * @param rol
     * @return 
     */
    public boolean isFuncionarioNivel(RolEnum rol){
        return funcionarioTO.getFuncionario().getFuNivel() == rol.getRolId();
    }

    /**
     * Evalua si el funcionario que está en sesion tiene alguno de los niveles que ingresan por 
     * el parametro rol
     * @param rol
     * @return 
     */
    public boolean isFuncionarioEnNivel(RolEnum[] rol){
        Long nivel = funcionarioTO.getFuncionario().getFuNivel();
        for(RolEnum e :rol){
            if (nivel == e.getRolId()){
                return true;
            }
        }
        return false;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public boolean isLoggedIn() {
        return (funcionarioTO != null);
    }
    
    public FuncionarioTO getFuncionarioTO() {
        return funcionarioTO;
    }
    public Long getFunNivel() {
        return funcionarioTO.getFuncionario().getFuNivel();
    }
    public void setFuncionarioTO(FuncionarioTO funcionarioTO) {
        this.funcionarioTO = funcionarioTO;
    }

}
