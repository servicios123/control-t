/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.services.FuncionarioService;
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
public class FuncionarioLazyList  extends LazyDataModel<Funcionario> {

    private FuncionarioService funcionarioService; 

    private List<Funcionario> lista;
    private Funcionario funcionarioFiltro;

    public List<Funcionario> getLista() {
        return lista;
    }

    public void setLista(List<Funcionario> lista) {
        this.lista = lista;
    }

    /**
     *
     * @param funcionarioService
     * @param funcionarioFiltro
     */
    public FuncionarioLazyList(FuncionarioService funcionarioService, Funcionario funcionarioFiltro) {
        this.funcionarioFiltro = funcionarioFiltro;
        this.funcionarioService = funcionarioService;
        
    }

    @Override
    public List<Funcionario> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, Object> filters) {
        lista = funcionarioService.getListaPag(funcionarioFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(funcionarioService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }

}
