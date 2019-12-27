/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.Solicitud;
import co.gov.aerocivil.controlt.services.SolicitudService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Viviana
 */
public class SolicitudLazyList extends LazyDataModel<Solicitud> {

    private SolicitudService solicitudService;
    private List<Solicitud> lista;
    private Solicitud solicitudFiltro;

    public List<Solicitud> getLista() {
        return lista;
    }

    public void setLista(List<Solicitud> lista) {
        this.lista = lista;
    }

    public SolicitudLazyList(SolicitudService solicitudService, Solicitud solicitudFiltro) {
        this.solicitudFiltro = solicitudFiltro;
        this.solicitudService = solicitudService;

    }

    @Override
    public List<Solicitud> load(int first, int pageSize, String sortField,
            SortOrder sortOrder, Map<String, Object> filters) {
        lista = solicitudService.getLista(solicitudFiltro, first, pageSize, sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(solicitudService.getCount().toString());
        this.setRowCount(dataSize);
        return lista;
    }
}
