/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.exceptions;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 *
 * @author Administrador
 */
public class DuplicadoException extends SQLIntegrityConstraintViolationException{
    
    public DuplicadoException (String message){
        super(message);
    }
    
    public DuplicadoException (){
        super();
    }
}
