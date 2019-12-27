/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.ParametrosDependencia;
import co.gov.aerocivil.controlt.services.ParametroDependenciaService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author juancamilo
 */
public class ParametroDependenciaLazyList extends LazyDataModel<ParametrosDependencia>{
    
    private ParametroDependenciaService paramDependenciaService; 

    private List<ParametrosDependencia> lista;
    private ParametrosDependencia dependenciaFiltro;

    public List<ParametrosDependencia> getLista() {
        return lista;
    }

    public void setLista(List<ParametrosDependencia> lista) {
        this.lista = lista;
    }

    public ParametroDependenciaLazyList(ParametroDependenciaService aeropuertoService, ParametrosDependencia parametroDependenciaFiltro) {
        this.dependenciaFiltro = parametroDependenciaFiltro;
        this.paramDependenciaService = aeropuertoService;
        
    }

    @Override
    public List<ParametrosDependencia> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, Object> filters) {
        lista = paramDependenciaService.getLista(dependenciaFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(paramDependenciaService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }

}
