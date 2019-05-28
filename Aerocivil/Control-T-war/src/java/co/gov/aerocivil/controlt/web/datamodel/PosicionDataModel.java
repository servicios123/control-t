/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.datamodel;

import co.gov.aerocivil.controlt.entities.PosicionNacional;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author juancamilo
 */
public class PosicionDataModel extends ListDataModel<PosicionNacional> implements SelectableDataModel<PosicionNacional> {

    public PosicionDataModel(List<PosicionNacional> list) {
        super(list);
    }

    @Override
    public Object getRowKey(PosicionNacional t) {
        return t.getPnId();
    }

    @Override
    public PosicionNacional getRowData(String string) {
        List<PosicionNacional> list = (List<PosicionNacional>) getWrappedData();
        for (PosicionNacional nacional : list) {
            if (nacional.getPnNombre().equals(string)) {
                return nacional;
            }
        }
        return null;
    }
}
