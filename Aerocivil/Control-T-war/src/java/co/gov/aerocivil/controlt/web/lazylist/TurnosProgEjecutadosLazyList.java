/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.DiarioPosicion;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
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
public class TurnosProgEjecutadosLazyList  extends LazyDataModel<Vistaprogramacion> {

    private ControlDiarioPosicionesService service; 

    private List<Vistaprogramacion> lista;
    private Programacion filtro;
    private Funcionario funcionario;

    public List<Vistaprogramacion> getLista() {
        return lista;
    }

    public void setLista(List<Vistaprogramacion> lista) {
        this.lista = lista;
    }

    /**
     *
     * @param service
     * @param filtro
     */
    public TurnosProgEjecutadosLazyList(ControlDiarioPosicionesService service, 
            Programacion filtro, Funcionario func) {
        this.filtro = filtro;
        this.service = service;        
        this.funcionario = func;
    }

    @Override
    public List<Vistaprogramacion> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, String> filters) {
        lista = service.getListaProgramadoVsEjecutado(filtro, funcionario, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(service.getCount().toString());
        this.setRowCount(dataSize);
        return lista;
    }

}
