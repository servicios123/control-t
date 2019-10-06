/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.services.DependenciaService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
//import org.primefaces.model.SortOrder;

/**
 *
 * @author Administrador
 */
public class DependenciaLazyList  extends LazyDataModel<Dependencia> {

    private DependenciaService dependenciaService; 

    private List<Dependencia> lista;
    private Dependencia dependenciaFiltro;

    public List<Dependencia> getLista() {
        return lista;
    }

    public void setLista(List<Dependencia> lista) {
        this.lista = lista;
    }

    public DependenciaLazyList(DependenciaService aeropuertoService, Dependencia dependenciaFiltro) {
        this.dependenciaFiltro = dependenciaFiltro;
        this.dependenciaService = aeropuertoService;
        
    }

    @Override
    public List<Dependencia> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, Object> filters) {
        lista = dependenciaService.getLista(dependenciaFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(dependenciaService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }

}
