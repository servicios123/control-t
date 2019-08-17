/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.CursoRecurrente;
import co.gov.aerocivil.controlt.entities.Funcionario;
import java.math.BigDecimal;
import java.util.Collections;
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
public class CursoRecurrenteServiceBean implements CursoRecurrenteService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    @EJB
    private AuditoriaService auditoria;

    @Override
    public CursoRecurrente guardar(CursoRecurrente cursoRecurrente, Funcionario f) {
        return (CursoRecurrente) auditoria.auditar(cursoRecurrente, f);
    }

    @Override
    public CursoRecurrente consultarMaxima(Long funId) {
        String query = "select max(cr.cur_id) from curso_recurrente_funcionario c join funcionario f on f.fun_id = c.crc_funcionario join curso_recurrente cr on c.crc_curso_recurrente = cr.cur_id where f.fun_id = " + funId;
        Query q = em.createNativeQuery(query);
        BigDecimal id = (BigDecimal) q.getSingleResult();

        if (id != null) {
            Query q1 = em.createNamedQuery("CursoRecurrente.findByCurId");
            q1.setParameter("curId", id.longValue());
            return (CursoRecurrente) q1.getSingleResult();
        }
        return null;
    }
}
