/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.enums;

/**
 *
 * @author Administrador
 */
public enum EstadoPeticionEnum {
    PENDIENTE("Pendiente"),
    APROBADA("Aprobado"),
    NEGADA("Negado");
    
    private String label;

    private EstadoPeticionEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}