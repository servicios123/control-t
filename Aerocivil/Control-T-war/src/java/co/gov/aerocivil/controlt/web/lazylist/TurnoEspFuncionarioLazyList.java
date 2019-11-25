/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.TurnoEspFuncionario;
import co.gov.aerocivil.controlt.enums.EstadoProgramacion;
import co.gov.aerocivil.controlt.services.ProgramacionTurnosSession;
import co.gov.aerocivil.controlt.services.TurnoEspFuncionarioService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Viviana
 */
public class TurnoEspFuncionarioLazyList  extends LazyDataModel<TurnoEspFuncionario> {
    
     private TurnoEspFuncionarioService turnoEspFuncionarioService; 
     private ProgramacionTurnosSession programacionService; 
     

    private List<TurnoEspFuncionario> lista;
    private TurnoEspFuncionario turnoEspFuncionarioFiltro;

    public List<TurnoEspFuncionario> getLista() {
        return lista;
    }

    public void setLista(List<TurnoEspFuncionario> lista) {
        this.lista = lista;
    }

    public TurnoEspFuncionarioLazyList(TurnoEspFuncionarioService turnoEspFuncionarioService,
            TurnoEspFuncionario turnoEspFuncionarioFiltro) {
        this.turnoEspFuncionarioFiltro = turnoEspFuncionarioFiltro;
        this.turnoEspFuncionarioService = turnoEspFuncionarioService;
        
    }

    @Override
    public List<TurnoEspFuncionario> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, String> filters) {
        lista = turnoEspFuncionarioService.getLista(turnoEspFuncionarioFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(turnoEspFuncionarioService.getCount().toString());
        this.setRowCount(dataSize);  
        
        return lista;
    }
    
}
