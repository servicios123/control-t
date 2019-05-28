/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.services.IncapacidadService;
import co.gov.aerocivil.controlt.services.PermisoService;
import co.gov.aerocivil.controlt.to.IncapacidadTO;
import co.gov.aerocivil.controlt.to.IncapacidadVista;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
/**
 *
 * @author Administrador
 */
public class IncapacidadLazyList extends LazyDataModel<IncapacidadVista> {

    private IncapacidadService service; 

    private List<IncapacidadVista> lista;
    private IncapacidadTO incapacidadFiltro;
    private String groupBy;
    private Long total;

    @Override
    public String toString() {
        return total!=null?total.toString():super.toString();
    }

    public IncapacidadLazyList(IncapacidadService auditService, IncapacidadTO auditFiltro, String groupBy) {
        this.incapacidadFiltro = auditFiltro;
        this.service = auditService;
        this.groupBy=groupBy;
    }
    
    @Override
    public List<IncapacidadVista> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, String> filters) {
        lista = service.getLista(incapacidadFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder), groupBy);
        this.setRowCount(service.getCount().intValue());
        this.total = service.getTotal(incapacidadFiltro, groupBy);
        //System.out.println("this.total: "+this.total);
        return lista;
    }
    
}
 