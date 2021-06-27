/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.to;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class TurnTO {

    private Funcionario funcionario;
    private Date date;
    private TurnoEspecial turnoEspecial;
    private Integer type;

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TurnoEspecial getTurnoEspecial() {
        return turnoEspecial;
    }

    public void setTurnoEspecial(TurnoEspecial turnoEspecial) {
        this.turnoEspecial = turnoEspecial;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
