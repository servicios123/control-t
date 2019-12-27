/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.Notificacion;
import co.gov.aerocivil.controlt.services.NotificacionService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Viviana
 */

public class NotificacionLazyList extends LazyDataModel<Notificacion>{
    
    private NotificacionService notificacionService; 

   
    private List<Notificacion> lista;
    private Notificacion notificacionFiltro;

    public NotificacionLazyList(NotificacionService notificacionService, Notificacion notificacionFiltro) {
        this.notificacionFiltro = notificacionFiltro;
        this.notificacionService = notificacionService;
        
    }
    public NotificacionService getSecuenciaService() {
        return notificacionService;
    }

    public void setSecuenciaService(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

     @Override
    public List<Notificacion> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, Object> filters) {
        lista = notificacionService.getLista(notificacionFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(notificacionService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }

    public Notificacion getNotificacionFiltro() {
        return notificacionFiltro;
    }

    public void setSecuenciaFiltro(Notificacion secuenciaFiltro) {
        this.notificacionFiltro = secuenciaFiltro;
    }
}
