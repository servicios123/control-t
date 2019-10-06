/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.services.JornadaService;
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
public class JornadaLazyList  extends LazyDataModel<Jornada> {

    private JornadaService jornadaService; 

    private List<Jornada> lista;
    private Jornada jornadaFiltro;

    public List<Jornada> getLista() {
        return lista;
    }

    public void setLista(List<Jornada> lista) {
        this.lista = lista;
    }

    public JornadaLazyList(JornadaService jornadaService, Jornada jornadaFiltro) {
        this.jornadaFiltro = jornadaFiltro;
        this.jornadaService = jornadaService;
        
    }

    @Override
    public List<Jornada> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, Object> filters) {
        lista = jornadaService.getListaPag(jornadaFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(jornadaService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }

}
