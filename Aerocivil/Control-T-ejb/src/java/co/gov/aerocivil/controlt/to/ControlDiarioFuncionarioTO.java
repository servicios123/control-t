/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.to;

import co.gov.aerocivil.controlt.entities.DiarioPosicionesIndividualVista;

/**
 *
 * @author Viviana
 */
public class ControlDiarioFuncionarioTO {
    private int dia;
    private DiarioPosicionesIndividualVista jorOrdinaria;
    private DiarioPosicionesIndividualVista jorExtra;

    public DiarioPosicionesIndividualVista getJorOrdinaria() {
        return jorOrdinaria;
    }

    public void setJorOrdinaria(DiarioPosicionesIndividualVista jorOrdinaria) {
        this.jorOrdinaria = jorOrdinaria;
    }

    public DiarioPosicionesIndividualVista getJorExtra() {
        return jorExtra;
    }

    public void setJorExtra(DiarioPosicionesIndividualVista jorExtra) {
        this.jorExtra = jorExtra;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }
    
    
}
