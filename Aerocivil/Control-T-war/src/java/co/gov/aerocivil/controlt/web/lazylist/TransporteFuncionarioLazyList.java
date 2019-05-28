/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.Transporte;
import co.gov.aerocivil.controlt.services.TransporteFuncionarioService;
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
public class TransporteFuncionarioLazyList  extends LazyDataModel<Transporte> {

    private TransporteFuncionarioService transporteFuncionarioService; 

    private List<Transporte> lista;
    private Transporte transpFuncionarioFiltro;

    public List<Transporte> getLista() {
        return lista;
    }

    public void setLista(List<Transporte> lista) {
        this.lista = lista;
    }

    /**
     *
     * @param funcionarioService
     * @param transpFuncionarioFiltro
     */
    public TransporteFuncionarioLazyList(TransporteFuncionarioService funcionarioService, Transporte transpFuncionarioFiltro) {
        this.transpFuncionarioFiltro = transpFuncionarioFiltro;
        this.transporteFuncionarioService = funcionarioService;
        
    }

    @Override
    public List<Transporte> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, String> filters) {
        lista = transporteFuncionarioService.getLista(transpFuncionarioFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(transporteFuncionarioService.getCount().toString());
        this.setRowCount(dataSize);
        return lista;
    }

}
