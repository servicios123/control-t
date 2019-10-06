/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.services.AeropuertoService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
//import org.primefaces.model.SortOrder;

/**
 *
 * @author Administrador
 */
public class AeropuertoLazyList  extends LazyDataModel<Aeropuerto> {

    private AeropuertoService aeropuertoService; 

    private List<Aeropuerto> lista;
    private Aeropuerto aeropuertoFiltro;

    public List<Aeropuerto> getLista() {
        return lista;
    }

    public void setLista(List<Aeropuerto> lista) {
        this.lista = lista;
    }

    public AeropuertoLazyList(AeropuertoService aeropuertoService, Aeropuerto aeropuertoFiltro) {
        this.aeropuertoFiltro = aeropuertoFiltro;
        this.aeropuertoService = aeropuertoService;
        
    }

    @Override
    public List<Aeropuerto> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, Object> filters) {
        lista = aeropuertoService.getLista(aeropuertoFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(aeropuertoService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }

}
