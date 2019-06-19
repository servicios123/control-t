/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.EvaluacionCompetencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface EvaluacionCompetenciaService {
    public EvaluacionCompetencia guardar(EvaluacionCompetencia evaluacion, Funcionario f);
    public EvaluacionCompetencia consultarEvaluacion(Funcionario f);
}
