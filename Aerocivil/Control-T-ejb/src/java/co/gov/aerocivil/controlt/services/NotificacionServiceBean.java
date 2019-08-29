/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Notificacion;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
public class NotificacionServiceBean implements NotificacionService {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;

    public List<Notificacion> getLista(Notificacion notificacion, int first, int pageSize,
            String sortField, String sortOrder) {

        Query query = createQueryFilter(notificacion, sortField, sortOrder);
        if (first > 0) {
            query.setFirstResult(first).setMaxResults(pageSize);
        }
        try {
            return (List<Notificacion>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    private Query createQueryFilter(Notificacion notificacion, String sortField, String sortOrder) {

        Map<String, Object> params = new HashMap<String, Object>();

        List<String> condiciones = new ArrayList<String>();


        if (notificacion.getNotFuncionario().getDependencia().getDepId() != null) {
            condiciones.add("d.not_Funcionario.dependencia.depId = :dep ");
            params.put("dep", notificacion.getNotFuncionario().getDependencia().getDepId());
        }
        if (notificacion.getNotFuncionario().getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("d.not_Funcionario.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero", notificacion.getNotFuncionario().getDependencia().getAeropuerto().getAeId());
        }
        if (notificacion.getFechaini() != null && notificacion.getFechafin() != null) {
            condiciones.add("d.notFecha between :fini and :ffin ");
            params.put("fini", notificacion.getFechaini());
            params.put("ffin", notificacion.getFechafin());
        }
        if (notificacion.getFechaini() != null && notificacion.getFechafin() == null) {
            condiciones.add("d.notFecha = :fini ");
            params.put("fini", notificacion.getFechaini());

        }

        if (notificacion.getFechaini() == null && notificacion.getFechafin() != null) {
            condiciones.add("d.notFecha = :ffin ");
            params.put("ffin", notificacion.getFechafin());
        }

        if (notificacion.getNotFuncionario().getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("d.not_Funcionario.dependencia.aeropuerto.regional.regId = :regional ");
            params.put("regional", notificacion.getNotFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
        }

        if (notificacion.getNotTipo() != null) {
            condiciones.add("d.notTipo = :tipo ");
            params.put("tipo", notificacion.getNotTipo());
        }
        if (notificacion.getNotFuncionario().getFunId() != null) {
            condiciones.add("d.not_Funcionario.funId = :fId ");
            params.put("fId", notificacion.getNotFuncionario().getFunId());
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

        Query query = em.createQuery("Select count(d) from Notificacion d " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("Select d from Notificacion d ").
                append(strQry.toString());
        if (sortField != null && sortOrder != null) {
            strQryFinal.append("order by d.").append(sortField).append(" ").append(sortOrder);
        }
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;

    }

    @Override
    public Long getCount() {
        return count;
    }
}
