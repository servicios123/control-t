/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.lazylist;

import co.gov.aerocivil.controlt.entities.Emergencias;
import co.gov.aerocivil.controlt.services.EmergenciasService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Viviana
 */
public class EmergenciasLazyList extends LazyDataModel<Emergencias>{
    private EmergenciasService emergenciasService; 

    private List<Emergencias> lista;
    private Emergencias emergenciasFiltro;

     public EmergenciasLazyList(EmergenciasService emergenciasService, Emergencias emergenciasFiltro) {
        this.emergenciasFiltro = emergenciasFiltro;
        this.emergenciasService = emergenciasService;
        
    }
     
     @Override
    public List<Emergencias> load(int first, int pageSize, String sortField, 
        SortOrder sortOrder, Map<String, Object> filters) {
        lista = emergenciasService.getListaPag(emergenciasFiltro, first,first + pageSize,
                sortField, SortOrderEnum.getSortOrder(sortOrder));
        Integer dataSize = Integer.valueOf(emergenciasService.getCount().toString());
        this.setRowCount(dataSize);  
        return lista;
    }
     
    public EmergenciasService getEmergenciasService() {
        return emergenciasService;
    }

    public void setEmergenciasService(EmergenciasService emergenciasService) {
        this.emergenciasService = emergenciasService;
    }

    public List<Emergencias> getLista() {
        return lista;
    }

    public void setLista(List<Emergencias> lista) {
        this.lista = lista;
    }

    public Emergencias getEmergenciasFiltro() {
        return emergenciasFiltro;
    }

    public void setEmergenciasFiltro(Emergencias emergenciasFiltro) {
        this.emergenciasFiltro = emergenciasFiltro;
    }

}
