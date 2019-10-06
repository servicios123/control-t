/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.DsTipo;
import co.gov.aerocivil.controlt.services.DsTipoService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author juancamilo
 */
public class DsTipoLazyList extends LazyDataModel<DsTipo>{
    
    private DsTipoService tipoService; 

    private List<DsTipo> lista;
    private DsTipo tipoFiltro;

    public List<DsTipo> getLista() {
        return lista;
    }

    public void setLista(List<DsTipo> lista) {
        this.lista = lista;
    }

    public DsTipoLazyList(DsTipoService tipoService, DsTipo tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
        this.tipoService = tipoService;
        
    }

    @Override
    public List<DsTipo> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, Object> filters) {
        lista = tipoService.getLista(tipoFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(tipoService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }

}
