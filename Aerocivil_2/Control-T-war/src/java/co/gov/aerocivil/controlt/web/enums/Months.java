/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.enums;

/**
 *
 * @author Administrador
 */
public enum Months {

    Enero(0),
    Febrero(1),
    Marzo(2),
    Abril(3),
    Mayo(4),
    Junio(5),
    Julio(6),
    Agosto(7),
    Septiembre(8),
    Octubre(9),
    Noviembre(10),
    Diciembre(11);
    
    private int month;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    private Months(int month) {
        this.month = month;
    }

    public String getLabel() {
        return this.name();
    }
    
    public static Months getMonth(int monthId) {
        for (Months r : Months.values()) {
            if (r.getMonth()== monthId) {
                return r;
            }
        }
        return null;
    }


    
}
