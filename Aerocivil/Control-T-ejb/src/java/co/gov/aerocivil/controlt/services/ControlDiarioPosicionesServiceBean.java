/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.DiarioPosicion;
import co.gov.aerocivil.controlt.entities.DiarioPosicionesIndividualVista;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Notificacion;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.RepHorasExtras;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import co.gov.aerocivil.controlt.enums.CategoriaDependenciaEnum;
import co.gov.aerocivil.controlt.util.QueryUtil;
import co.gov.aerocivil.controlt.util.StringDateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
 * @author Administrador
 */
@Stateless
public class ControlDiarioPosicionesServiceBean implements ControlDiarioPosicionesService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    @EJB
    private AuditoriaService auditoria;
    private Long count;

    @Override
    public DiarioPosicion guardar(DiarioPosicion diario, Funcionario f) {
        diario = (DiarioPosicion) auditoria.auditar(diario, f);
        if (diario.getDposNotificacion() != null) {
            Notificacion notif = new Notificacion();
            notif.setNotFuncionario(em.find(Funcionario.class, diario.getFuncionario().getFunId()));
            notif.setNotFecha(new Date());
            notif.setNotTipo(diario.getDposNotificacion());
            auditoria.auditar(notif, f);
        }
        return diario;
    }

    @Override
    public void flush(DiarioPosicion transporte) {
        em.refresh(transporte);
        em.flush();
    }

    @Override
    public List<DiarioPosicion> getLista(DiarioPosicion diarioPosicion,
            Integer first, Integer pageSize, String sortField, String sortOrder) {
        Query query = createQueryFilter(diarioPosicion, sortField, sortOrder);
        if (first != null && pageSize != null) {
            query.setFirstResult(first).setMaxResults(pageSize);
        }
        try {
            return (List<DiarioPosicion>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Vistaprogramacion> getListaTurnos(DiarioPosicion diarioPosicion,
            Integer first, Integer pageSize, String sortField, String sortOrder) {

        StringBuilder strQry = new StringBuilder("Select f from Vistaprogramacion f");
        strQry.append(" where f.turFecha =:fecha ");
        strQry.append(" and f.jornadaId=:jorId");
        strQry.append(" and f.programacion.proEstado=1 and f.turTipo in (1,2) ");


        if (sortField != null && sortOrder != null) {
            strQry.append("order by f.").append(sortField).append(" ").append(sortOrder);
        }
        //System.out.println("strQry: "+strQry);
        Query query = em.createQuery(strQry.toString());
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("jorId", diarioPosicion.getTurno().getJornadaId());
        params.put("fecha", diarioPosicion.getTurno().getTurFecha());
        QueryUtil.setParameters(query, params);
        return query.getResultList();

    }

    @Override
    public List<Vistaprogramacion> getListaProgramadoVsEjecutado(Programacion programacion,
            Funcionario funcionario,
            Integer first, Integer pageSize, String sortField, String sortOrder) {

        StringBuilder strQry = new StringBuilder();
        //strQry.append(" where f.turFecha between :fechaIni and :fechaFin ");        
        strQry.append(" where f.turFecha = :fechaIni ");
        strQry.append(" and f.programacion.dependencia.depId=:depId ");
        strQry.append(" and f.programacion.proEstado=1 and f.turTipo in (1,2) ");
        /*Vistaprogramacion vp=new Vistaprogramacion();
         vp.getFun*/
        Map<String, Object> params = new HashMap<String, Object>();
        List<String> condiciones = new ArrayList<String>();


        /*      if (funcionario.getFunNombre() != null && !"".equals(funcionario.getFunNombre())) {
         condiciones.add("upper(f.funNombre) like :nombre ");
         params.put("nombre", "%" + funcionario.getFunNombre().toUpperCase() + "%");
         }*/
        if (funcionario != null && funcionario.getFunAlias() != null && !"".equals(funcionario.getFunAlias())) {
            condiciones.add("upper(f.funAlias) like :alias ");
            params.put("alias", "%" + funcionario.getFunAlias().toUpperCase() + "%");
        }

        if (funcionario != null && funcionario.getFunId() != null && !"".equals(funcionario.getFunId())) {
            condiciones.add("f.funId = :id ");
            params.put("id", funcionario.getFunId());
        }



        params.put("fechaIni", programacion.getProFechaInicio());
        //params.put("fechaFin",programacion.getProFechaFin());
        params.put("depId", programacion.getDependencia().getDepId());

        Query query = em.createQuery("Select count(f) from Vistaprogramacion f " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();
        if (sortField != null && sortOrder != null) {
            strQry.append("order by f.").append(sortField).append(" ").append(sortOrder).append(", ");
        }
        strQry.append(" f.turHInicio, f.pjAlias");

        //System.out.println("strQry: "+strQry);
        query = em.createQuery("Select f from Vistaprogramacion f " + strQry.toString());
        QueryUtil.setParameters(query, params);
        if (first != null && pageSize != null && first > 0 && pageSize > 0) {
            query.setFirstResult(first).setMaxResults(pageSize);
        }

        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        return query.getResultList();

    }

    private Query createQueryFilter(DiarioPosicion diarioPosicion, String sortField, String sortOrder) {
        List<String> condiciones = new ArrayList<String>();

        Map<String, Object> params = new HashMap<String, Object>();
        condiciones.add("f.turno.programacion.proEstado=1 and f.turno.turTipo in (1,2) ");
        if (diarioPosicion.getTurno().getTurFecha() != null) {
            condiciones.add("f.turno.turFecha = :fecha ");
            params.put("fecha", diarioPosicion.getTurno().getTurFecha());
        }
        if (diarioPosicion.getFuncionario().getDependencia() != null
                && diarioPosicion.getFuncionario().getDependencia().getDepcategoria() != null
                && diarioPosicion.getFuncionario().getDependencia().getDepcategoria().getDcId() != null) {
            condiciones.add("f.funcionario.dependencia.depcategoria.dcId = :depCat ");
            params.put("depCat", diarioPosicion.getFuncionario().getDependencia().getDepcategoria().getDcId());
        }
        if (diarioPosicion.getFuncionario().getDependencia() != null && diarioPosicion.getFuncionario().getDependencia().getDepId() != null) {
            condiciones.add("f.funcionario.dependencia.depId = :dep ");
            params.put("dep", diarioPosicion.getFuncionario().getDependencia().getDepId());
        }

        if (diarioPosicion.getFuncionario().getDependencia().getAeropuerto() != null && diarioPosicion.getFuncionario().getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("f.funcionario.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero", diarioPosicion.getFuncionario().getDependencia().getAeropuerto().getAeId());
        } else if (diarioPosicion.getFuncionario().getDependencia().getAeropuerto().getRegional() != null && diarioPosicion.getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("f.funcionario.dependencia.aeropuerto.regional.regId = :reg ");
            params.put("reg", diarioPosicion.getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
        }

        StringBuilder strQry = new StringBuilder();

        if (condiciones.size() > 0) {
            strQry.append(" where ");
            strQry.append(QueryUtil.joinWithAnd(condiciones));
        }

        Query query = em.createQuery("Select count(f) from DiarioPosicion f " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("Select f from DiarioPosicion f ").
                append(strQry.toString());
        if (sortField != null && sortOrder != null) {
            strQryFinal.append("order by f.").append(sortField).append(" ").append(sortOrder);
        }
        //System.out.println("strQry: "+strQryFinal);
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;

    }

    
    @Override
    public List<DiarioPosicion> getLista(DiarioPosicion diarioPosicion) {
        Query query = createQueryFilter(diarioPosicion);
        try {
            return (List<DiarioPosicion>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Vistaprogramacion> getListaTurnos(DiarioPosicion diarioPosicion) {

        StringBuilder strQry = new StringBuilder("Select f from Vistaprogramacion f");
        strQry.append(" where f.turFecha =:fecha ");
        strQry.append(" and f.jornadaId=:jorId");
        strQry.append(" and f.programacion.proEstado=1 and f.turTipo in (1,2) ");



        //System.out.println("strQry: "+strQry);
        Query query = em.createQuery(strQry.toString());
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("jorId", diarioPosicion.getTurno().getJornadaId());
        params.put("fecha", diarioPosicion.getTurno().getTurFecha());
        QueryUtil.setParameters(query, params);
        return query.getResultList();

    }

    @Override
    public List<Vistaprogramacion> getListaProgramadoVsEjecutado(Programacion programacion,
            Funcionario funcionario) {

        StringBuilder strQry = new StringBuilder();
        //strQry.append(" where f.turFecha between :fechaIni and :fechaFin ");        
        strQry.append(" where f.turFecha = :fechaIni ");
        strQry.append(" and f.programacion.dependencia.depId=:depId ");
        strQry.append(" and f.programacion.proEstado=1 and f.turTipo in (1,2) ");
        /*Vistaprogramacion vp=new Vistaprogramacion();
         vp.getFun*/
        Map<String, Object> params = new HashMap<String, Object>();
        List<String> condiciones = new ArrayList<String>();


        /*      if (funcionario.getFunNombre() != null && !"".equals(funcionario.getFunNombre())) {
         condiciones.add("upper(f.funNombre) like :nombre ");
         params.put("nombre", "%" + funcionario.getFunNombre().toUpperCase() + "%");
         }*/
        if (funcionario != null && funcionario.getFunAlias() != null && !"".equals(funcionario.getFunAlias())) {
            condiciones.add("upper(f.funAlias) like :alias ");
            params.put("alias", "%" + funcionario.getFunAlias().toUpperCase() + "%");
        }

        if (funcionario != null && funcionario.getFunId() != null && !"".equals(funcionario.getFunId())) {
            condiciones.add("f.funId = :id ");
            params.put("id", funcionario.getFunId());
        }



        params.put("fechaIni", programacion.getProFechaInicio());
        //params.put("fechaFin",programacion.getProFechaFin());
        params.put("depId", programacion.getDependencia().getDepId());

        Query query = em.createQuery("Select count(f) from Vistaprogramacion f " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        strQry.append("order by").append(" ");

        strQry.append(" f.turHInicio, f.pjAlias");

        //System.out.println("strQry: "+strQry);
        query = em.createQuery("Select f from Vistaprogramacion f " + strQry.toString());
        QueryUtil.setParameters(query, params);

        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        return query.getResultList();

    }

    private Query createQueryFilter(DiarioPosicion diarioPosicion) {
        List<String> condiciones = new ArrayList<String>();

        Map<String, Object> params = new HashMap<String, Object>();
        condiciones.add("f.turno.programacion.proEstado=1 and f.turno.turTipo in (1,2) ");
        if (diarioPosicion.getTurno().getTurFecha() != null) {
            condiciones.add("f.turno.turFecha = :fecha ");
            params.put("fecha", diarioPosicion.getTurno().getTurFecha());
        }
        if (diarioPosicion.getFuncionario().getDependencia() != null
                && diarioPosicion.getFuncionario().getDependencia().getDepcategoria() != null
                && diarioPosicion.getFuncionario().getDependencia().getDepcategoria().getDcId() != null) {
            condiciones.add("f.funcionario.dependencia.depcategoria.dcId = :depCat ");
            params.put("depCat", diarioPosicion.getFuncionario().getDependencia().getDepcategoria().getDcId());
        }
        if (diarioPosicion.getFuncionario().getDependencia() != null && diarioPosicion.getFuncionario().getDependencia().getDepId() != null) {
            condiciones.add("f.funcionario.dependencia.depId = :dep ");
            params.put("dep", diarioPosicion.getFuncionario().getDependencia().getDepId());
        }

        if (diarioPosicion.getFuncionario().getDependencia().getAeropuerto() != null && diarioPosicion.getFuncionario().getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("f.funcionario.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero", diarioPosicion.getFuncionario().getDependencia().getAeropuerto().getAeId());
        } else if (diarioPosicion.getFuncionario().getDependencia().getAeropuerto().getRegional() != null && diarioPosicion.getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("f.funcionario.dependencia.aeropuerto.regional.regId = :reg ");
            params.put("reg", diarioPosicion.getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
        }

        StringBuilder strQry = new StringBuilder();

        if (condiciones.size() > 0) {
            strQry.append(" where ");
            strQry.append(QueryUtil.joinWithAnd(condiciones));
        }

        Query query = em.createQuery("Select count(f) from DiarioPosicion f " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("Select f from DiarioPosicion f ").
                append(strQry.toString());
        //System.out.println("strQry: "+strQryFinal);
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;

    }

    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public Funcionario getFuncionarioByAlias(String funAlias, Long idDependencia,
            Date fecha, Long idJornada, Long idFunc) {
        StringBuilder strQryFinal = new StringBuilder("Select f from Funcionario f ");
        strQryFinal.append(" where upper(f.funAlias) like :falias ");
        strQryFinal.append(" and f.dependencia.depId=:fdepId ");
        strQryFinal.append(" and f.funId not in ( ");
        strQryFinal.append(" Select dp.funcionario.funId from DiarioPosicion dp ");
        strQryFinal.append(" where dp.turno.turFecha=:turFecha ");
        strQryFinal.append(" and dp.turno.jornadaId=:jorId ");
        strQryFinal.append(" and dp.funcionario.funId=:funId )");
        //System.out.println(strQryFinal+"-"+funAlias+"-"+idDependencia);
        Query query = em.createQuery(strQryFinal.toString());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("falias", funAlias.toUpperCase());
        params.put("fdepId", idDependencia);
        params.put("turFecha", fecha);
        params.put("jorId", idJornada);
        params.put("funId", idFunc);
        QueryUtil.setParameters(query, params);
        try {
            return (Funcionario) query.getSingleResult();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }

    }

    @Override
    public List<DiarioPosicionesIndividualVista> getListaDiarioPosicionesIndividual(Funcionario f, Date fIni, Date fFin) {
        StringBuilder strQry = new StringBuilder("select d from DiarioPosicionesIndividualVista d ");
        strQry.append(" where d.funcionario =:funId and d.fecha between :fIni and :fFin");
        //System.out.println(strQry+" funId:"+f.getFunId()+ " fIni:"+fIni + " fFin"+ fFin);
        Query q = em.createQuery(strQry.toString());
        q.setParameter("fIni", fIni);
        q.setParameter("fFin", fFin);
        q.setParameter("funId", f.getFunId());
        return q.getResultList();
    }

    @Override
    public List<RepHorasExtras> getReporteHorasExtras(RepHorasExtras filtroReporte) {
        StringBuilder strQry = new StringBuilder();
        if (filtroReporte.getDependencia().getDepcategoria().getDcId()
                == CategoriaDependenciaEnum.ATS.getVal()) {
            strQry.append("begin HORASEXTRASATS ");
        } else {
            strQry.append("begin HORASEXTRAS_SAR ");
        }

        strQry.append("(FINICIO => to_date('").append(StringDateUtil.formatDate(filtroReporte.getFecha()))
                .append("','dd/mm/yy'),").append(" FFIN => to_date('")
                .append(StringDateUtil.formatDate(filtroReporte.getFechaFinReporte()))
                .append("','dd/mm/yy'),").append(" FUNCIONARIO_ID => ")
                .append(filtroReporte.getFuncionario().getFunId()).append(",DEPENDENCIA_ID =>")
                .append(filtroReporte.getDependencia().getDepId())
                .append(");end;");

        Query q = em.createNativeQuery(strQry.toString());
        q.executeUpdate();

        strQry = new StringBuilder("select d from RepHorasExtras d ");
        strQry.append(" where d.funcionario.funId =:funId and d.fecha between :fIni and :fFin ");
        strQry.append(" and d.dependencia.depId=:depId order by d.fecha ");
        //System.out.println("strQry::::::::"+strQry);
        q = em.createQuery(strQry.toString());
        q.setParameter("fIni", filtroReporte.getFecha());
        q.setParameter("fFin", filtroReporte.getFechaFinReporte());
        q.setParameter("funId", filtroReporte.getFuncionario().getFunId());
        q.setParameter("depId", filtroReporte.getDependencia().getDepId());
        return q.getResultList();
    }
}
