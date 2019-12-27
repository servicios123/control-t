/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.services.DepartamentoService;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Viviana
 */
@ManagedBean
@SessionScoped
public class DepartamentoBBean {

    /**
     * Creates a new instance of DepartamentoBBean
     */
    @EJB
    private DepartamentoService departamentodService;

    public DepartamentoService getDepartamentodService() {
        return departamentodService;
    }

    public void setDepartamentodService(DepartamentoService departamentodService) {
        this.departamentodService = departamentodService;
    }
   
}
