/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.PosicionInactiva;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface PosicionInactivaService {
    public List<PosicionInactiva> getLista(Dependencia dep, Date fIni, Date fFin);

    public void guardarPosiciones(Map<String, Integer[]> mapa, Funcionario f);
}
