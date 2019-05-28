/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.DiarioSenalEspecial;
import co.gov.aerocivil.controlt.services.DiarioSenalEspecialService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Administrador
 */
public class DiarioSenalEspecialLazyList extends LazyDataModel<DiarioSenalEspecial> {
    private List<DiarioSenalEspecial> lista;
    private DiarioSenalEspecialService diarioSenalEspecialService;
    private DiarioSenalEspecial diarioSenalEspecialFiltro;
    
    public DiarioSenalEspecialLazyList(DiarioSenalEspecialService diarioSenalEspecialService, DiarioSenalEspecial diarioSenalEspecialFiltro)
    {
        this.diarioSenalEspecialFiltro= diarioSenalEspecialFiltro;
        this.diarioSenalEspecialService= diarioSenalEspecialService;
    }
    
    
    
    @Override
    public List<DiarioSenalEspecial> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        lista = diarioSenalEspecialService.getLista(diarioSenalEspecialFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(diarioSenalEspecialService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }

    /**
     * @return the lista
     */
    public List<DiarioSenalEspecial> getLista() {
        return lista;
    }

    /**
     * @param lista the lista to set
     */
    public void setLista(List<DiarioSenalEspecial> lista) {
        this.lista = lista;
    }
    
}
