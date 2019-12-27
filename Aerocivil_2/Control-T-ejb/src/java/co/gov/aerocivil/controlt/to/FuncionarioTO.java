/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.gov.aerocivil.controlt.to;

import co.gov.aerocivil.controlt.entities.Funcionario;

/**
 *
 * @author Administrator
 */
public class FuncionarioTO {

    private String login;
    private String clave;
    private Funcionario funcionario;
    private boolean isLoggedIn;

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario; 
    }


    //private RolUsuarioEnum rol;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean isLoggedIn() {
	    return isLoggedIn;
	}
	/**
	 * Sustituye  el valor actual de isLoggedIn por el valor  ingresado en isLoggedIn (setter)
	 * @param isLoggedIn valor a  actualizar 
	 */
	public void setLoggedIn(boolean isLoggedIn) {
	    this.isLoggedIn = isLoggedIn;
	}

}
