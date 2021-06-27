/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.JornadaNoLaborable;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
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

        JornadaNoLaborable jornadaNoLaborable = (JornadaNoLaborable) q.getSingleResult();
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
    public HashMap<Long, List<Long>> getJornadasNoLaborales() {
        String query = "select jn.id_funcionario, listagg(jn.id_jornada,',') within group( order by jn.id_jornada ) "
                + " from jornada_no_laborable jn, funcionario f, dependencia d "
                + "  where jn.id_funcionario = f.fun_id "
                + "  and f.fun_dependencia = d.dep_id "
                + "  group by jn.id_funcionario";
        HashMap<Long, List<Long>> jornadas = new HashMap<Long, List<Long>>();
        Query q = em.createNativeQuery(query);
        List<Object[]> result = q.getResultList();
        for (Object[] row : result) {
            List<Long> ids = new ArrayList<Long>();
            String[] data = ((String) row[1]).split(",");
            for (String id : data) {
                ids.add(new BigDecimal(id).longValue());
            }
            jornadas.put(((BigDecimal)row[0]).longValue(), ids);
        }
        return jornadas;
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
