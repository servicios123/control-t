/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import co.gov.aerocivil.controlt.services.VistaProgramacionService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Viviana
 */
public class VistaProgramacionLazyList extends LazyDataModel<Vistaprogramacion>  {
    
    private VistaProgramacionService vistaProgramacionService; 

    private List<Vistaprogramacion> lista;
    private Vistaprogramacion vistaProgramacionFiltro;

    public List<Vistaprogramacion> getLista() {
        return lista;
    }

    public void setLista(List<Vistaprogramacion> lista) {
        this.lista = lista;
    }

    public VistaProgramacionLazyList(VistaProgramacionService vistaProgramacionService, Vistaprogramacion vistaProgramacionFiltro) {
        this.vistaProgramacionFiltro = vistaProgramacionFiltro;
        this.vistaProgramacionService = vistaProgramacionService;
        
    }

    @Override
    public List<Vistaprogramacion> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, String> filters) {
        lista = vistaProgramacionService.getLista(vistaProgramacionFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(vistaProgramacionService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }
}
