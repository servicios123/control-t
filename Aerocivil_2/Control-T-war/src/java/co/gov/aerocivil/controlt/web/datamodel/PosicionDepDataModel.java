/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.datamodel;

import co.gov.aerocivil.controlt.entities.Posicion;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author juancamilo
 */
public class PosicionDepDataModel extends ListDataModel<Posicion> implements SelectableDataModel<Posicion> {

    public PosicionDepDataModel(List<Posicion> list) {
        super(list);
    }

    @Override
    public Object getRowKey(Posicion t) {
        return t.getPosId();
    }

    @Override
    public Posicion getRowData(String string) {
        List<Posicion> list = (List<Posicion>) getWrappedData();
        for (Posicion nacional : list) {
            if (nacional.getPosId().toString().equals(string)) {
                return nacional;
            }
        }
        return null;
    }
}
