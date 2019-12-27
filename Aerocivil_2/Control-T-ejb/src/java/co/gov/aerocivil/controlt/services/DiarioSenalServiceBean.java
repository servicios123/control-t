/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.DiarioSenal;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.util.QueryUtil;
import co.gov.aerocivil.controlt.util.StringDateUtil;
import java.util.ArrayList;
import java.util.Calendar;
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
public class DiarioSenalServiceBean implements DiarioSenalService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    @EJB
    private AuditoriaService auditoria;

    @Override
    public List<DiarioSenal> getLista(DiarioSenal diarioSenal, Integer first, Integer pageSize,
            String sortField, String sortOrder) {
        /*try{
         Query qryAux = em.createNativeQuery("update diario_senal set dsen_fecha_suceso = TO_DATE(to_char(dsen_fecha_suceso,'DD/MM/YYYY')||' '||dsen_hora_suceso||':'||dsen_min_suceso,'DD/MM/YYYY HH24:MI')");
         qryAux.executeUpdate();
         } catch (Exception nre) {
         nre.printStackTrace();
         }*/
        try {
            Query query = createQueryFilter(diarioSenal, sortField, sortOrder);
            if (first != null && pageSize != null) {
                query.setFirstResult(first).setMaxResults(pageSize);
            }
            return (List<DiarioSenal>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }

    }

    private Query createQueryFilter(DiarioSenal diarioSenal, String sortField, String sortOrder) {

        List<String> condiciones = new ArrayList<String>();
        StringBuilder strQry = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();


        if (diarioSenal.getDsenFechaRegistro() != null && diarioSenal.getDsenFechaRegistroFinal() != null) {
            condiciones.add("s.dsenFechaRegistro between :freg and :fregFin ");
            Calendar c = Calendar.getInstance();
            c.setTime(diarioSenal.getDsenFechaRegistro());
            //c.set(Calendar.ZONE_OFFSET, 0);
            c = StringDateUtil.setCeroHoras(c);
            //c.add(Calendar.HOUR_OF_DAY, -5);
            params.put("freg", c.getTime());
            //System.out.println("c.getTime()::" + c.getTime());
            Calendar cf = Calendar.getInstance();
            cf.setTime(diarioSenal.getDsenFechaRegistroFinal());
            //cf.set(Calendar.ZONE_OFFSET, 0);
            cf.set(Calendar.HOUR_OF_DAY, 23);
            cf.set(Calendar.MINUTE, 59);
            cf.set(Calendar.SECOND, 59);
            params.put("fregFin", cf.getTime());
            //System.out.println("cf.getTime()::" + cf.getTime());
        } else if (diarioSenal.getDsenFechaRegistro() != null) {
            condiciones.add("s.dsenFechaRegistro between :freg and :fregFin ");
            Calendar c = Calendar.getInstance();
            c.setTime(diarioSenal.getDsenFechaRegistro());
            //c.set(Calendar.ZONE_OFFSET, 0);
            c = StringDateUtil.setCeroHoras(c);
            //c.add(Calendar.HOUR_OF_DAY, -5);
            params.put("freg", c.getTime());
            //System.out.println("c.getTime()::" + c.getTime());
            Calendar cf = Calendar.getInstance();
            cf.setTime(diarioSenal.getDsenFechaRegistro());
            //cf.set(Calendar.ZONE_OFFSET, 0);
            cf.set(Calendar.HOUR_OF_DAY, 23);
            cf.set(Calendar.MINUTE, 59);
            cf.set(Calendar.SECOND, 59);
            params.put("fregFin", cf.getTime());
            //System.out.println("cf.getTime()::" + cf.getTime());
        }





        if (diarioSenal.getDsenFechaSuceso() != null && diarioSenal.getDsenFechaSucesoFinal() != null) {
            condiciones.add("s.dsenFechaSuceso between :fsuce and :fsuceFin ");
            Calendar c = Calendar.getInstance();
            c.setTime(diarioSenal.getDsenFechaSuceso());
            //c.set(Calendar.ZONE_OFFSET, 0);
            c = StringDateUtil.setCeroHoras(c);
            //c.add(Calendar.HOUR_OF_DAY, -5);
            params.put("fsuce", c.getTime());
            //System.out.println("c.getTime()::" + c.getTime());
            Calendar cf = Calendar.getInstance();
            cf.setTime(diarioSenal.getDsenFechaSucesoFinal());
            //cf.set(Calendar.ZONE_OFFSET, 0);
            cf.set(Calendar.HOUR_OF_DAY, 18);
            cf.set(Calendar.MINUTE, 59);
            cf.set(Calendar.SECOND, 59);
            params.put("fsuceFin", cf.getTime());
            //System.out.println("cf.getTime()::" + cf.getTime());
        } else if (diarioSenal.getDsenFechaSuceso() != null) {
            condiciones.add("s.dsenFechaSuceso between :fsuce and :fsuceFin ");
            Calendar c = Calendar.getInstance();
            c.setTime(diarioSenal.getDsenFechaSuceso());
            //c.set(Calendar.ZONE_OFFSET, -5);
            c = StringDateUtil.setCeroHoras(c);
            //c.add(Calendar.HOUR_OF_DAY, -5);
            params.put("fsuce", c.getTime());
            //System.out.println("c.getTime():" + c.getTime());
            Calendar cf = Calendar.getInstance();
            cf.setTime(diarioSenal.getDsenFechaSuceso());
            //cf.set(Calendar.ZONE_OFFSET, -5);
            cf.set(Calendar.HOUR_OF_DAY, 18);
            cf.set(Calendar.MINUTE, 59);
            cf.set(Calendar.SECOND, 59);
            params.put("fsuceFin", cf.getTime());
            //System.out.println("cf.getTime():" + cf.getTime());
        }
        if (diarioSenal.getDsenLugarSuceso() != null && !"".equals(diarioSenal.getDsenLugarSuceso())) {
            condiciones.add("upper(s.dsenLugarSuceso) like :lugar ");
            params.put("lugar", "%" + diarioSenal.getDsenLugarSuceso().toUpperCase() + "%");
        }
        if (diarioSenal.getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("s.dependencia.aeropuerto.regional.regId = :reg ");
            params.put("reg", diarioSenal.getDependencia().getAeropuerto().getRegional().getRegId());
        }

        if (diarioSenal.getDsTipo().getDstId() != null) {
            condiciones.add("s.dsTipo.dstId = :ds ");
            params.put("ds", diarioSenal.getDsTipo().getDstId());
        }

        if (diarioSenal.getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("s.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero", diarioSenal.getDependencia().getAeropuerto().getAeId());
        }

        if (diarioSenal.getDependencia().getDepId() != null) {
            condiciones.add("s.dependencia.depId = :dep ");
            params.put("dep", diarioSenal.getDependencia().getDepId());
        }
        if (diarioSenal.getDependencia().getDepcategoria() != null && diarioSenal.getDependencia().getDepcategoria().getDcId() != null) {
            condiciones.add("s.dependencia.depcategoria.dcId = :depcat ");
            params.put("depcat", diarioSenal.getDependencia().getDepcategoria().getDcId());
        }

        if (condiciones.size() > 0) {
            strQry.append("where ");
        }
        for (Iterator<String> it = condiciones.iterator(); it.hasNext();) {
            strQry.append(it.next());
            if (it.hasNext()) {
                strQry.append(" and ");
            }
        }

        Query query = em.createQuery("Select count(s) from DiarioSenal s  " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("Select s from DiarioSenal s ").
                append(strQry.toString());
        if (sortField != null && sortOrder != null) {
            strQryFinal.append("order by s.").append(sortField).append(" ").append(sortOrder);
        } else {
            strQryFinal.append(" order by s.dsenFechaSuceso desc");
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
    public DiarioSenal guardar(DiarioSenal diarioSenal, Funcionario f) {
        diarioSenal.setDependencia(em.find(Dependencia.class, diarioSenal.getDependencia().getDepId()));
        return (DiarioSenal) auditoria.auditar(diarioSenal, f);

    }
}
