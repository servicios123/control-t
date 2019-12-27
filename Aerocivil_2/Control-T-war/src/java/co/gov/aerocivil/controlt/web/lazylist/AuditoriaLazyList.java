/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.Auditoria;
import co.gov.aerocivil.controlt.services.AuditoriaService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
/**
 *
 * @author Administrador
 */
public class AuditoriaLazyList extends LazyDataModel<Auditoria> {

    private AuditoriaService service; 

    private List<Auditoria> lista;
    private Auditoria auditoriaFiltro;

    public AuditoriaLazyList(AuditoriaService auditService, Auditoria auditFiltro) {
        this.auditoriaFiltro = auditFiltro;
        this.service = auditService;
    }
    
    @Override
    public List<Auditoria> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, Object> filters) {
        lista = service.getLista(auditoriaFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        this.setRowCount(service.getCount().intValue());
        return lista;
    }
    
}
 