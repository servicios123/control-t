/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.enums;

/**
 *
 * @author Administrador
 */
public enum CategoriaDependenciaEnum {
    ATS(1),
    SEI(2),
    SAR(3),
    AIS(4);
    
    private int val;
    
    CategoriaDependenciaEnum(int val){
        this.val= val;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

}
