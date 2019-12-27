/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.delegate;

import co.gov.aerocivil.controlt.services.RegionalService;
import javax.ejb.EJB;

/**
 *
 * @author Administrador
 */
public class RegionalDelegate {
    @EJB
    private RegionalService regionalService;    
    private static RegionalDelegate delegate;

    public static RegionalDelegate getInstance(){
        if (delegate==null){
            delegate = new RegionalDelegate();
        }
        return delegate;
    }
    
    public RegionalService getRegionalService(){
        return getInstance().regionalService;
    }
}
