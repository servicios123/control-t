/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.enums;

/**
 *
 * @author Administrador
 */
public enum EstadoProgramacion {

    GENERADA(0),
    PROGRAMADA(1);
    
    private int val;
    
    EstadoProgramacion(int val){
        this.val= val;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
}
