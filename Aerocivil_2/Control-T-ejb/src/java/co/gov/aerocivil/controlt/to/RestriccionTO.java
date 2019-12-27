/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.to;

import co.gov.aerocivil.controlt.entities.RestriccionDependencia;
import co.gov.aerocivil.controlt.entities.RestriccionProgramacion;
import java.io.Serializable;

/**
 *
 * @author Administrador
 */
public class RestriccionTO implements Serializable{
    
    private Long rpId;
    private String rpDescripcion;
    private Long rdId;
    private Long rdValor;

    public Long getRpId() {
        return rpId;
    }

    public void setRpId(Long rpId) {
        this.rpId = rpId;
    }

    public String getRpDescripcion() {
        return rpDescripcion;
    }

    public void setRpDescripcion(String rpDescripcion) {
        this.rpDescripcion = rpDescripcion;
    }

    public Long getRdId() {
        return rdId;
    }

    public void setRdId(Long rdId) {
        this.rdId = rdId;
    }

    public Long getRdValor() {
        return rdValor;
    }

    public void setRdValor(Long rdValor) {
        this.rdValor = rdValor;
    }
    
    @Override
    public String toString(){
        return "restrProg:"+rpId + "-Descr:" + rpDescripcion + "-Valor:" + rdValor;
    }
    
}
