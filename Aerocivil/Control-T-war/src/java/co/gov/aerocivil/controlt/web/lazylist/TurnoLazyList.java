/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.Turno;
import co.gov.aerocivil.controlt.services.TurnoService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Viviana
 */
public class TurnoLazyList extends LazyDataModel<Turno>  {
    
    private TurnoService turnoService; 

    private List<Turno> lista;
    private Turno turnoFiltro;

    public List<Turno> getLista() {
        return lista;
    }

    public void setLista(List<Turno> lista) {
        this.lista = lista;
    }

    public TurnoLazyList(TurnoService turnoService, Turno turnoFiltro) {
        this.turnoFiltro = turnoFiltro;
        this.turnoService = turnoService;
        
    }

    @Override
    public List<Turno> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, Object> filters) {
        lista = turnoService.getLista(turnoFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(turnoService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }
}
