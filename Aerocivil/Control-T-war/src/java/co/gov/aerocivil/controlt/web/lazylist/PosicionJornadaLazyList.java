/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.PosicionJornada;
import co.gov.aerocivil.controlt.services.PosicionJornadaService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Viviana
 */
public class PosicionJornadaLazyList extends LazyDataModel<PosicionJornada>{

    private PosicionJornadaService posicionJornadaService; 

    private List<PosicionJornada> lista;
    private PosicionJornada posicionJornadaFiltro;

    public List<PosicionJornada> getLista() {
        return lista;
    }

    public void setLista(List<PosicionJornada> lista) {
        this.lista = lista;
    }

    public PosicionJornadaLazyList(PosicionJornadaService posicionJornadaService, PosicionJornada posicionJornadaFiltro) {
        this.posicionJornadaFiltro = posicionJornadaFiltro;
        this.posicionJornadaService = posicionJornadaService;
        
    }

    @Override
    public List<PosicionJornada> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, Object> filters) {
        lista = posicionJornadaService.getLista(posicionJornadaFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(posicionJornadaService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }
    
}
