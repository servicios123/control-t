/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.PermisoEspecial;
import co.gov.aerocivil.controlt.services.PermisoService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
/**
 *
 * @author Administrador
 */
public class PermisoLazyList extends LazyDataModel<PermisoEspecial> {

    private PermisoService service; 

    private List<PermisoEspecial> lista;
    private PermisoEspecial permisoFiltro;

    public PermisoLazyList(PermisoService aeropuertoService, PermisoEspecial permisoFiltro) {
        this.permisoFiltro = permisoFiltro;
        this.service = aeropuertoService;
    }
    
    @Override
    public List<PermisoEspecial> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, String> filters) {
        lista = service.getLista(permisoFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        this.setRowCount(service.getCount().intValue());
        return lista;
    }
    
}
 