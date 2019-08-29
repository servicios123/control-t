/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.DsTipo;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
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
public class DsTipoServiceBean implements DsTipoService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    @EJB
    private AuditoriaService auditoria;

    @Override
    public DsTipo guardar(DsTipo tipo, Funcionario f) throws SQLIntegrityConstraintViolationException {
        return (DsTipo) auditoria.auditar(tipo, f);
    }

    public List<DsTipo> getLista(DsTipo tipo, int first, int pageSize,
            String sortField, String sortOrder) {

        Query query = createQueryFilter(tipo, sortField, sortOrder);
        if (first > 0) {
            query.setFirstResult(first).setMaxResults(pageSize);
        }
        try {
            return (List<DsTipo>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    private Query createQueryFilter(DsTipo tipo, String sortField, String sortOrder) {

        Map<String, Object> params = new HashMap<String, Object>();

        List<String> condiciones = new ArrayList<String>();

        if (tipo.getDstNombre() != null) {
            condiciones.add("upper(d.dstNombre) like :nombre ");
            params.put("nombre", "%" + tipo.getDstNombre().toUpperCase() + "%");
        }
        if (tipo.getCategoria().getDcId() != null) {
            condiciones.add("d.categoria.dcId = :categoria ");
            params.put("categoria", tipo.getCategoria().getDcId());
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

        Query query = em.createQuery("Select count(d) from DsTipo d " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("Select d from DsTipo d ").
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

    @Override
    public List<DsTipo> getLista(DsTipo tipo) {

        StringBuilder strQry = new StringBuilder();
        strQry.append("Select d from DsTipo d ");
        strQry.append("where ");
        strQry.append("d.categoria.dcId = :categoria ");

        Query query = em.createQuery(strQry.toString());
        query.setParameter("categoria", tipo.getCategoria().getDcId());

        return query.getResultList();

    }
}
