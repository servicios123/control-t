/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.DetSecuencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Secuencia;
import co.gov.aerocivil.controlt.util.QueryUtil;
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
public class SecuenciaServiceBean implements SecuenciaService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    @EJB
    private AuditoriaService auditoria;

    public List<Secuencia> getLista(Secuencia secuencia, int first, int pageSize,
            String sortField, String sortOrder) {

        Query query = createQueryFilter(secuencia, sortField, sortOrder);
        if (first > 0) {
            query.setFirstResult(first).setMaxResults(pageSize);
        }
        try {
            return (List<Secuencia>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    @Override
    public void borrarDetalle(Secuencia secuencia) {

        StringBuilder strQry = new StringBuilder();
        Query q = em.createNativeQuery(strQry.toString());

        strQry.append("delete from det_secuencia d where  d.ds_secuencia = ")
                .append(secuencia.getSecuId());


        q = em.createNativeQuery(strQry.toString());
        q.executeUpdate();


    }

    private Query createQueryFilter(Secuencia secuencia, String sortField, String sortOrder) {

        Map<String, Object> params = new HashMap<String, Object>();

        List<String> condiciones = new ArrayList<String>();


        if (secuencia.getDependencia().getDepId() != null) {
            condiciones.add("d.dependencia.depId = :dep ");
            params.put("dep", secuencia.getDependencia().getDepId());
        }
        if (secuencia.getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("d.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero", secuencia.getDependencia().getAeropuerto().getAeId());
        }

        if (secuencia.getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("d.dependencia.aeropuerto.regional.regId = :regional ");
            params.put("regional", secuencia.getDependencia().getAeropuerto().getRegional().getRegId());
        }
        if (secuencia.getSecuEstado() != null && !"".equals(secuencia.getSecuEstado())) {
            condiciones.add("d.secuEstado = :estado ");
            params.put("estado", secuencia.getSecuEstado());
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

        Query query = em.createQuery("Select count(d) from Secuencia d " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("Select d from Secuencia d ").
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
    public Secuencia guardar(Secuencia secuencia, Funcionario f) {
        List<DetSecuencia> detSecuencias = secuencia.getDetSecuencias();
        secuencia = (Secuencia) auditoria.auditar(secuencia, f);
        for (DetSecuencia ds : detSecuencias) {
            ds.setSecuencia(secuencia);
            auditoria.auditar(ds, f);
        }
        return secuencia;


    }
}
