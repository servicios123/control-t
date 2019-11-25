/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.RestriccionDependencia;
import co.gov.aerocivil.controlt.services.RestriccionesService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
/**
 *
 * @author Administrador
 */
public class RestriccionesLazyList extends LazyDataModel<RestriccionDependencia> {

    private RestriccionesService service; 

    private List<RestriccionDependencia> lista;
    private RestriccionDependencia restriccionFiltro;

    public RestriccionesLazyList(RestriccionesService restriccionService, RestriccionDependencia restriccionFiltro) {
        this.restriccionFiltro = restriccionFiltro;
        this.service = restriccionService;
    }
    
    @Override
    public List<RestriccionDependencia> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, String> filters) {
        lista = service.getLista(restriccionFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        this.setRowCount(service.getCount().intValue());
        return lista;
    }
    
}
 