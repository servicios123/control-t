/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.PosicionNacional;
import co.gov.aerocivil.controlt.services.PosicionNacionalService;
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
public class PosicionNacionalLazyList  extends LazyDataModel<PosicionNacional> {

    private PosicionNacionalService posicionService; 

    private List<PosicionNacional> lista;
    private PosicionNacional posicionFiltro;

    public List<PosicionNacional> getLista() {
        return lista;
    }

    public void setLista(List<PosicionNacional> lista) {
        this.lista = lista;
    }

    public PosicionNacionalLazyList(PosicionNacionalService posicionService, PosicionNacional posicionFiltro) {
        this.posicionFiltro = posicionFiltro;
        this.posicionService = posicionService;
        
    }

    @Override
    public List<PosicionNacional> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, Object> filters) {
        
        lista = posicionService.getLista(posicionFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(posicionService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }

}
