/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.PosicionJornada;
import co.gov.aerocivil.controlt.entities.Solicitud;
import co.gov.aerocivil.controlt.entities.Turno;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
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
import javax.persistence.TemporalType;

/**
 *
 * @author Viviana
 */
@Stateless
public class SolicitudServiceBean implements SolicitudService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    @EJB
    private AuditoriaService auditoria;

    @Override
    public List<Solicitud> getLista(Solicitud solicitud, int first, int pageSize,
            String sortField, String sortOrder) {

        Query query = createQueryFilter(solicitud, sortField, sortOrder);
        if (first > 0) {
            query.setFirstResult(first).setMaxResults(pageSize);
        }
        try {
            return (List<Solicitud>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    private Query createQueryFilter(Solicitud solicitud, String sortField, String sortOrder) {

        Map<String, Object> params = new HashMap<String, Object>();

        List<String> condiciones = new ArrayList<String>();


        if (solicitud.getSolFechaCambio() != null && !"".equals(solicitud.getSolFechaCambio())) {
            condiciones.add("d.solFechaCambio = :fc ");
            params.put("fc", solicitud.getSolFechaCambio());
        }
        if (solicitud.getSolFechaRegistro() != null && !"".equals(solicitud.getSolFechaRegistro())) {
            condiciones.add("d.solFechaRegistro = :fr ");
            params.put("fr", solicitud.getSolFechaRegistro());
        }
        if (solicitud.getFuncionario_sol() != null && solicitud.getFuncionario_sol().getFunId() != null) {
            condiciones.add("d.funcionario_sol.funId = :fs ");
            params.put("fs", solicitud.getFuncionario_sol().getFunId());
        }
        if (solicitud.getFuncionario_reem() != null && solicitud.getFuncionario_reem().getFunId() != null) {
            condiciones.add("d.funcionario_reem.funId = :free ");
            params.put("free", solicitud.getFuncionario_reem().getFunId());
        }
        if (solicitud.getSolTipo() != null) {
            condiciones.add("d.solTipo = :tip ");
            params.put("tip", solicitud.getSolTipo());
        }
        if (solicitud.getTurno() != null && solicitud.getTurno().getPjId() != null) {
            condiciones.add("d.turno.pjId = :tur ");
            params.put("tur", solicitud.getTurno().getPjId());
        }

        if (solicitud.getDependencia() != null && solicitud.getDependencia().getDepId() != null) {
            condiciones.add("d.dependencia.depId = :dep ");
            params.put("dep", solicitud.getDependencia().getDepId());
        }

        if (solicitud.getDependencia() != null && solicitud.getDependencia().getDepcategoria().getDcId() != null && !"".equals(solicitud.getDependencia().getDepcategoria().getDcId())) {
            condiciones.add("d.dependencia.depcategoria.dcId = :dc ");
            params.put("dc", solicitud.getDependencia().getDepcategoria().getDcId());
        }
        if (solicitud.getDependencia() != null && solicitud.getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("d.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero", solicitud.getDependencia().getAeropuerto().getAeId());
        }

        if (solicitud.getDependencia() != null && solicitud.getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("d.dependencia.aeropuerto.regional.regId = :regional ");
            params.put("regional", solicitud.getDependencia().getAeropuerto().getRegional().getRegId());
        }
        if (solicitud.getSolEstado() != null && !"".equals(solicitud.getSolEstado())) {
            condiciones.add("d.solEstado = :estado ");
            params.put("estado", solicitud.getSolEstado());
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

        Query query = em.createQuery("Select count(d) from Solicitud d  " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("Select d from Solicitud d ").
                append(strQry.toString());
        if (sortField != null && sortOrder != null) {
            strQryFinal.append(" order by d.").append(sortField).append(" ").append(sortOrder);
        }
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;

    }

    @Override
    public Long contarAprobados(Solicitud solicitud, Date Finicial, Date Ffinal) {

        StringBuilder strQry = new StringBuilder();
        List<String> condiciones = new ArrayList<String>();

        Map<String, Object> params = new HashMap<String, Object>();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        condiciones.add("d.funcionario_sol.funId= :fsol");
        params.put("fsol", solicitud.getFuncionario_sol().getFunId());

        condiciones.add("d.solFechaCambio between :fini and :ffin");
        params.put("fini", Finicial);
        params.put("ffin", Ffinal);




        /*   condiciones.add("TO_CHAR(df_fecha,'M')=1");
         params.put("1", solicitud.getSolFechaCambio().getMonth());*/

        condiciones.add("d.solEstado = :estado");
        params.put("estado", "Aprobado");




        if (condiciones.size() > 0) {
            strQry.append("where ");
        }
        for (Iterator<String> it = condiciones.iterator(); it.hasNext();) {
            strQry.append(it.next());
            if (it.hasNext()) {
                strQry.append(" and ");
            }
        }
        Query query = em.createQuery("Select count(d) from Solicitud d  ".concat(strQry.toString()));
        QueryUtil.setParameters(query, params);


        return (Long) query.getSingleResult();

    }

    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public Solicitud guardar(Solicitud solicitud, Funcionario f) {
        solicitud.setDependencia(em.find(Dependencia.class, solicitud.getDependencia().getDepId()));
        solicitud.setFuncionario_reem(em.find(Funcionario.class, solicitud.getFuncionario_reem().getFunId()));
        solicitud.setFuncionario_sol(em.find(Funcionario.class, solicitud.getFuncionario_sol().getFunId()));
        solicitud.setTurno(em.find(PosicionJornada.class, solicitud.getTurno().getPjId()));
//        solicitud.setTurno_pago(em.find(PosicionJornada.class, solicitud.getTurno_pago().getPjId()));
        solicitud.setSolFechaRegistro(new Date());
        return (Solicitud) auditoria.auditar(solicitud, f);
    }

    @Override
    public Solicitud actualizar(Solicitud solicitud, Funcionario f) {

        return (Solicitud) auditoria.auditar(solicitud, f);

    }

    @Override
    public List<PosicionJornada> obtenerPJenProgramacion(Date fecha, Funcionario fun) {
        Query qturno = em.createQuery("Select t From Turno t Where t.turFecha = :fecha and t.funcionario.funId = :funId and t.turTipo in (1,2)");
        qturno.setParameter("fecha", fecha, TemporalType.DATE);
        qturno.setParameter("funId", fun.getFunId());
        try {
            List<Turno> turnos = (List<Turno>) qturno.getResultList();
            if (!turnos.isEmpty()) {
                ArrayList<Long> ts = new ArrayList<Long>();
                for (Turno turno : turnos) {
                    ts.add(turno.getTurPosicionJornada());
                }
                Query query = em.createQuery("Select p From PosicionJornada p Where p.pjId in :pjs ");
                query.setParameter("pjs", ts);
                return (List<PosicionJornada>) query.getResultList();
            } else {
                return null;
            }


        } catch (NoResultException nre) {
            return null;
        }


    }

    @Override
    public Solicitud getId(Long id) {
        return em.find(Solicitud.class, id);
    }
}
