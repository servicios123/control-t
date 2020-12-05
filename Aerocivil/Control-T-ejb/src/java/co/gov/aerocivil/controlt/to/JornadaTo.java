/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.to;

import co.gov.aerocivil.controlt.entities.Jornada;

/**
 *
 * @author Admin
 */
public class JornadaTo {

    private Jornada jornada;
    private String mensaje;

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
