/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.PosNoAsig;
import co.gov.aerocivil.controlt.entities.Turno;
import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface ModificarTurnoService {

    public List<Funcionario> getFuncionarioTurnoPorFecha(Date date, long dep);

    public List<PosNoAsig> getPosNoAsigPorFecha(Date date, long dep);

    public List<Funcionario> getFuncionarios(long dep);

    public List<TurnoEspecial> getTurnoEspecial(long dep);

    public List<Vistaprogramacion> getTurnoPorFunFecha(Date date, long fun, boolean special);
//    public boolean anularTurno(Vistaprogramacion vp);

    public String asignarEspecial(Date date, Funcionario f, TurnoEspecial t, Funcionario mod);

    public String cambiarTurnos(Date date1, Funcionario f1, Vistaprogramacion turno1, Date date2, Funcionario f2, Vistaprogramacion turno2, Funcionario mod);

    public String asignarPosNoAsig(PosNoAsig pn, Date date, Funcionario fun, Funcionario mod);

    public Vistaprogramacion getVp(Date fecha, Funcionario fun, Long pos_jor);

    public java.util.List<co.gov.aerocivil.controlt.entities.Funcionario> getFunctionariesAvaible(java.util.Date date, co.gov.aerocivil.controlt.entities.Dependencia dependencia);

    public boolean anularTurno(co.gov.aerocivil.controlt.entities.Vistaprogramacion vp, co.gov.aerocivil.controlt.entities.Funcionario f);

    public java.util.List<co.gov.aerocivil.controlt.entities.Funcionario> getFunctionariesAvaibleSpecial(java.util.Date date, co.gov.aerocivil.controlt.entities.Dependencia dependencia);

    public java.util.List<co.gov.aerocivil.controlt.entities.Funcionario> getFuncionarioTurnoEspecialPorFecha(java.util.Date date, long dep);

    public String validateTurn(Turno turno, Long cambio);
}
