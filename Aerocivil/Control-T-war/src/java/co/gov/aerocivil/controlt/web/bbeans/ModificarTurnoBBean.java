/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.services.ProgramacionTotalService;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Administrador
 */

@ManagedBean
@SessionScoped
public class ModificarTurnoBBean {
    @EJB
    private ProgramacionTotalService programacionTotalServiceBean;

    /**
     * Creates a new instance of ModificarTurnoBBean
     */
    public ModificarTurnoBBean() {
    }
    
    public String index()
    {
        return "modificarTurno";
    }
}
