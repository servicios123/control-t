/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Turno;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Viviana
 */
@Stateless
public class TurnoServiceBean implements TurnoService {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    @EJB
    private AuditoriaService auditoria;

    @Override
    public List<Turno> getLista(Turno turno, int first, int pageSize,
            String sortField, String sortOrder) {

        Query query = createQueryFilter(turno, sortField, sortOrder);
        if (first > 0) {
            query.setFirstResult(first).setMaxResults(pageSize);
        }
        try {
            return (List<Turno>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    private Query createQueryFilter(Turno turno, String sortField, String sortOrder) {

        Map<String, Object> params = new HashMap<String, Object>();

        List<String> condiciones = new ArrayList<String>();




        if (turno.getFuncionario().getFunAlias() != null && !"".equals(turno.getFuncionario().getFunAlias())) {
            condiciones.add("d.funcionario.funAlias like :fAlias");
            params.put("fAlias", "%" + turno.getFuncionario().getFunAlias() + "%");
        }
        if (turno.getFuncionario().getFunId() != null && !"".equals(turno.getFuncionario().getFunId())) {
            condiciones.add("d.funcionario.funId = :fun ");
            params.put("fun", turno.getFuncionario().getFunId());
        }



        if (turno.getProgramacion().getDependencia().getDepId() != null) {
            condiciones.add("d.programacion.dependencia.depId = :dep ");
            params.put("dep", turno.getProgramacion().getDependencia().getDepId());
        }

        if (turno.getProgramacion().getDependencia().getDepcategoria().getDcId() != null && !"".equals(turno.getProgramacion().getDependencia().getDepcategoria().getDcId())) {
            condiciones.add("d.programacion.dependencia.depcategoria.dcId = :dc ");
            params.put("dc", turno.getProgramacion().getDependencia().getDepcategoria().getDcId());
        }
        if (turno.getProgramacion().getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("d.programacion.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero", turno.getProgramacion().getDependencia().getAeropuerto().getAeId());
        }

        if (turno.getProgramacion().getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("d.programacion.dependencia.aeropuerto.regional.regId = :regional ");
            params.put("regional", turno.getProgramacion().getDependencia().getAeropuerto().getRegional().getRegId());
        }
        if (turno.getTurEstado() != null && !"".equals(turno.getTurEstado())) {
            condiciones.add("d.turEstado = :estado ");
            params.put("estado", turno.getTurEstado());
        }

        if (turno.getTurFecha() != null) {

            condiciones.add("d.turFecha = :fec ");
            params.put("fec", turno.getTurFecha());
        }



        StringBuilder strQry = new StringBuilder();
        if (condiciones.size() > 0) {
            strQry.append("where ");
        }
        for (Iterator<String> it = condiciones.iterator(); it.hasNext();) {
            strQry.append(it.next());
            if (it.hasNext()) {
                strQry.append(" and ");
            }
        }

        Query query = em.createQuery("Select count(d) from Turno d  " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("Select d from Turno d ").
                append(strQry.toString());
        if (sortField != null && sortOrder != null) {
            strQryFinal.append(" order by d.").append(sortField).append(" ").append(sortOrder);
        }
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;

    }

    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public Turno guardar(Turno turno, Funcionario f) {

        turno = (Turno) auditoria.auditar(turno, f);
        /*Comision comision = turnoEspFuncionario.getComision(); 
         if(comision != null){
         comision.setTurnoEspFuncionario(turnoEspFuncionario);
         auditoria.auditar(comision);
         }*/
        return turno;

    }

    @Override
    public void corregirTipos(Date turFecha, Long proId, Long funId, Funcionario f) {
        Query q = em.createQuery("select t from Turno t where t.programacion.proId = :proId and t.funcionario.funId = :funId and t.turFecha = :turFecha");
        q.setParameter("turFecha", turFecha);
        q.setParameter("proId", proId);
        q.setParameter("funId", funId);

        List<Turno> turnos = q.getResultList();

        if (turnos != null && !turnos.isEmpty()) {
            if (turnos.size() == 1) {
                Turno t = turnos.get(0);
                t.setTurTipo(1L);
                auditoria.auditar(t, f);
            } else if (turnos.size() == 2) {
                Turno t1 = turnos.get(0);
                Turno t2 = turnos.get(1);

                if (t2.getTurHFin() < t1.getTurHInicio()) {
                    t2.setTurTipo(1L);
                    t1.setTurTipo(2L);
                } else {
                    t2.setTurTipo(2L);
                    t1.setTurTipo(1L);
                }

                auditoria.auditar(t1, f);
                auditoria.auditar(t2, f);
            }
        }
    }
}
