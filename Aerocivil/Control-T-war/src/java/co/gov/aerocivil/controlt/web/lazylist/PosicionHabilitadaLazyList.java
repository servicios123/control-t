/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.PosicionHabilitada;
import co.gov.aerocivil.controlt.services.PosicionHabilitadaService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Viviana
 */
public class PosicionHabilitadaLazyList  extends LazyDataModel<PosicionHabilitada>{
    
    
    private PosicionHabilitadaService posicionHabilitadaService; 

    private List<PosicionHabilitada> lista;
    private PosicionHabilitada posicionHabilitadaFiltro;

    public List<PosicionHabilitada> getLista() {
        return lista;
    }

    public void setLista(List<PosicionHabilitada> lista) {
        this.lista = lista;
    }

    public PosicionHabilitadaLazyList(PosicionHabilitadaService posicionHabilitadaService, PosicionHabilitada posicionHabilitadaFiltro) {
        this.posicionHabilitadaFiltro = posicionHabilitadaFiltro;
        this.posicionHabilitadaService = posicionHabilitadaService;
        
    }

    @Override
    public List<PosicionHabilitada> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, Object> filters) {
        lista = posicionHabilitadaService.getLista(posicionHabilitadaFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(posicionHabilitadaService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }

   
}
