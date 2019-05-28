/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.to;

/**
 *
 * @author Administrador
 */
public class FuncionarioTransporteTO {
    private Long funId;
    private String funNombre;
    private String funDireccion;
    private String funAlias;
    private boolean seleccionado;

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }
    public Long getFunId() {
        return funId;
    }

    public void setFunId(Long funId) {
        this.funId = funId;
    }

    public String getFunNombre() {
        return funNombre;
    }

    public void setFunNombre(String funNombre) {
        this.funNombre = funNombre;
    }

    public String getFunDireccion() {
        return funDireccion;
    }

    public void setFunDireccion(String funDireccion) {
        this.funDireccion = funDireccion;
    }

    public String getFunAlias() {
        return funAlias;
    }

    public void setFunAlias(String funAlias) {
        this.funAlias = funAlias;
    }
    
    public FuncionarioTransporteTO(Long funId, String funAlias, String funNombre, String funDireccion) {
        this.funId = funId;
        this.funNombre = funNombre;
        this.funDireccion = funDireccion;
        this.funAlias = funAlias;
    }


    
}
