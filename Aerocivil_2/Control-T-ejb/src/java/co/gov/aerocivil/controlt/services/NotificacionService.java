/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Notificacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Viviana
 */
@Local
public interface NotificacionService {
    
    public List<Notificacion> getLista(Notificacion notificacion, int first, int pageSize,
            String sortField, String sortOrder);    
    
    public Long getCount();
}
