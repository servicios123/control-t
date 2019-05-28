/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.JornadaNoLaborable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author JUAN CAMILO
 */
@Stateless
public class JornadaNoLaboralServiceBean implements JornadaNoLaboralService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    @EJB
    private AuditoriaService auditoria;
    
    @EJB
    private FuncionarioService funcionarioService;

    @Override
    public JornadaNoLaborable guardar(JornadaNoLaborable jornada, Funcionario f) throws SQLIntegrityConstraintViolationException {
        return (JornadaNoLaborable) auditoria.auditar(jornada, f);
    }

    @Override
    public JornadaNoLaborable getJornadasFuncionario(Long jornada, Long funcionario) {
        Query q = em.createQuery("select j from JornadaNoLaborable j where j.jornada.joId = :jornada and j.funcionario.funId = :funcionario");
        q.setParameter("funcionario", funcionario);
        q.setParameter("jornada", jornada);
        
        JornadaNoLaborable jornadaNoLaborable =  (JornadaNoLaborable) q.getSingleResult();
        return jornadaNoLaborable;
    }

    @Override
    public List<JornadaNoLaborable> getListaJornadasFuncionario(Long funcionario) {
        Query q = em.createQuery("select j from JornadaNoLaborable j where j.funcionario.funId = :funcionario");
        q.setParameter("funcionario", funcionario);
        
        List<JornadaNoLaborable> jornadaNoLaborableList = q.getResultList();
        return jornadaNoLaborableList;
    }

    @Override
    public void eliminar(Long funcionario) {
        StringBuilder strQry = new StringBuilder();
        strQry.append("delete from JornadaNoLaborable j where j.funcionario.funId = '").
                append(funcionario).append("'");   
        //System.out.println("DelQry: " + strQry.toString());
        Query query = em.createQuery(strQry.toString());         
        query.executeUpdate();
    }
}
