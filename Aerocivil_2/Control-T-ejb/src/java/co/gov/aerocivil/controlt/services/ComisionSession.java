/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Comision;
import co.gov.aerocivil.controlt.entities.Funcionario;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface ComisionSession {
    
    public Comision guardar(Comision c, Funcionario f);
    
}
