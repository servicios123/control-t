/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.EvaluacionCompetencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

    @Override
    public EvaluacionCompetencia consultarEvaluacion(Funcionario f) {
        Query q = em.createQuery("select max(e.evId) from EvaluacionCompetencia e where e.funcionario.funId = :funId");
        q.setParameter("funId", f.getFunId());
        Long maxEvId = (Long) q.getSingleResult();
        
        Query q2 = em.createQuery("Select e from EvaluacionCompetencia e where e.evId = :evId");
        q2.setParameter("evId", maxEvId);
        
        EvaluacionCompetencia competencia = (EvaluacionCompetencia) q2.getSingleResult();
        
        return competencia;
    }
    
}
