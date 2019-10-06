/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.DiarioSenal;
import co.gov.aerocivil.controlt.services.DiarioSenalService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Viviana
 */
public class DiarioSenalLazyList extends LazyDataModel<DiarioSenal>{

    private DiarioSenalService diarioSenalService; 

    private List<DiarioSenal> lista;
    private DiarioSenal diarioSenalFiltro;

    public List<DiarioSenal> getLista() {
        return lista;
    }

    public void setLista(List<DiarioSenal> lista) {
        this.lista = lista;
    }

    public DiarioSenalLazyList(DiarioSenalService diarioSenalService, DiarioSenal diarioSenalFiltro) {
        this.diarioSenalFiltro = diarioSenalFiltro;
        this.diarioSenalService = diarioSenalService;
        
    }

    @Override
    public List<DiarioSenal> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, Object> filters) {
        //System.out.println("sortField = " + sortField);
        lista = diarioSenalService.getLista(diarioSenalFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(diarioSenalService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }
    
}
