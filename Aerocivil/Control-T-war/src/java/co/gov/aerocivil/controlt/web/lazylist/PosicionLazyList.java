/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.services.PosicionService;
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
public class PosicionLazyList  extends LazyDataModel<Posicion> {

    private PosicionService posicionService; 

    private List<Posicion> lista;
    private Posicion posicionFiltro;

    public List<Posicion> getLista() {
        return lista;
    }

    public void setLista(List<Posicion> lista) {
        this.lista = lista;
    }

    public PosicionLazyList(PosicionService posicionService, Posicion posicionFiltro) {
        this.posicionFiltro = posicionFiltro;
        this.posicionService = posicionService;
        
    }

    @Override
    public List<Posicion> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, String> filters) {
        lista = posicionService.getLista(posicionFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(posicionService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }

}
