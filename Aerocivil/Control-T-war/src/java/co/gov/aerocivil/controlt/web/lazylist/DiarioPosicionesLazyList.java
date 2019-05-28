/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.DiarioPosicion;
import co.gov.aerocivil.controlt.services.ControlDiarioPosicionesService;
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
public class DiarioPosicionesLazyList  extends LazyDataModel<DiarioPosicion> {

    private ControlDiarioPosicionesService service; 

    private List<DiarioPosicion> lista;
    private DiarioPosicion filtro;

    public List<DiarioPosicion> getLista() {
        return lista;
    }

    public void setLista(List<DiarioPosicion> lista) {
        this.lista = lista;
    }

    /**
     *
     * @param service
     * @param filtro
     */
    public DiarioPosicionesLazyList(ControlDiarioPosicionesService service, DiarioPosicion filtro) {
        this.filtro = filtro;
        this.service = service;        
    }

    @Override
    public List<DiarioPosicion> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, String> filters) {
        lista = service.getLista(filtro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(service.getCount().toString());
        this.setRowCount(dataSize);
        return lista;
    }

}
