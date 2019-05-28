/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.EvaluacionCompetencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrador
 */
@Stateless
public class EvaluacionCompetenciaServiceBean implements EvaluacionCompetenciaService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    
    @EJB
    private AuditoriaService auditoria;

    
    @Override
    public EvaluacionCompetencia guardar(EvaluacionCompetencia evaluacion, Funcionario f) {
        evaluacion.getFuncionario().setFunFvEvaluacion(evaluacion.getEvFechaVence());
        auditoria.auditar(evaluacion.getFuncionario(),f);
        return (EvaluacionCompetencia) auditoria.auditar(evaluacion,f);
    }
    
}
