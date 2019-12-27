/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.to;

/**
 *
 * @author juancamilo
 */
public class OptionTO {
    Object value;
    String label;

    public OptionTO(Object value, String label) {
        this.value = value;
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        try {
            return value+" - "+label;
        } catch (Exception e) {
            return "";
        }
    }
    
    
}
