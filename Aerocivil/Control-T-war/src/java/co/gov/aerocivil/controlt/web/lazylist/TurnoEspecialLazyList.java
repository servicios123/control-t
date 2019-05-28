/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import co.gov.aerocivil.controlt.services.TurnoEspecialService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Viviana
 */
public class TurnoEspecialLazyList extends LazyDataModel<TurnoEspecial> {
    private TurnoEspecialService turnoEspecialService; 

    private List<TurnoEspecial> lista;
    private TurnoEspecial turnoEspecialFiltro;

    public List<TurnoEspecial> getLista() {
        return lista;
    }

    public void setLista(List<TurnoEspecial> lista) {
        this.lista = lista;
    }

    public TurnoEspecialLazyList(TurnoEspecialService turnoEspecialService, TurnoEspecial turnoEspecialFiltro) {
        this.turnoEspecialFiltro = turnoEspecialFiltro;
        this.turnoEspecialService = turnoEspecialService;
        
    }

    @Override
    public List<TurnoEspecial> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, String> filters) {
        lista = turnoEspecialService.getLista(turnoEspecialFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(turnoEspecialService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }
}
