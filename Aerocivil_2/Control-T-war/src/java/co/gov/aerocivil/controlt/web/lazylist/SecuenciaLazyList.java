/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.Secuencia;
import co.gov.aerocivil.controlt.services.SecuenciaService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Viviana
 */
public class SecuenciaLazyList  extends LazyDataModel<Secuencia>{
    
    private SecuenciaService secuenciaService; 

   
    private List<Secuencia> lista;
    private Secuencia secuenciaFiltro;
    
    
     public SecuenciaLazyList(SecuenciaService secuenciaService, Secuencia secuenciaFiltro) {
        this.secuenciaFiltro = secuenciaFiltro;
        this.secuenciaService = secuenciaService;
        
    }
    public SecuenciaService getSecuenciaService() {
        return secuenciaService;
    }

    public void setSecuenciaService(SecuenciaService secuenciaService) {
        this.secuenciaService = secuenciaService;
    }

     @Override
    public List<Secuencia> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, Object> filters) {
        lista = secuenciaService.getLista(secuenciaFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(secuenciaService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }

    public Secuencia getSecuenciaFiltro() {
        return secuenciaFiltro;
    }

    public void setSecuenciaFiltro(Secuencia secuenciaFiltro) {
        this.secuenciaFiltro = secuenciaFiltro;
    }
 
}
