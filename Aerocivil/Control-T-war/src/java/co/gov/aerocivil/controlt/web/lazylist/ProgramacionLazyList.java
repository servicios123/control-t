/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.services.ProgramacionTurnosSession;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Viviana
 */
public class ProgramacionLazyList extends LazyDataModel<Programacion> {
    
     private ProgramacionTurnosSession programacionService; 

    private List<Programacion> lista;

    
    private Programacion programacionFiltro;

    

    public ProgramacionLazyList(ProgramacionTurnosSession programacionService, Programacion programacionFiltro) {
        this.programacionFiltro = programacionFiltro;
        this.programacionService = programacionService;
        
    }

    @Override
    public List<Programacion> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, Object> filters) {
        lista = programacionService.getListaPag(programacionFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        
        Integer dataSize = Integer.valueOf(programacionService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }

    public ProgramacionTurnosSession getProgramacionService() {
        return programacionService;
    }

    public void setProgramacionService(ProgramacionTurnosSession programacionService) {
        this.programacionService = programacionService;
    }

    public List<Programacion> getLista() {
        return lista;
    }

    public void setLista(List<Programacion> lista) {
        this.lista = lista;
    }

    public Programacion getProgramacionFiltro() {
        return programacionFiltro;
    }

    public void setProgramacionFiltro(Programacion programacionFiltro) {
        this.programacionFiltro = programacionFiltro;
    }
}
