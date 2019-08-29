/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Turno;
import co.gov.aerocivil.controlt.entities.TurnoEspFuncionario;
import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import co.gov.aerocivil.controlt.enums.EstadoProgramacion;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.math.BigDecimal;
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

/**
 *
 * @author Viviana
 */
@Stateless
public class TurnoEspFuncionarioServiceBean implements TurnoEspFuncionarioService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    @EJB
    private AuditoriaService auditoria;
    @EJB
    private ProgramacionTurnosSession programacionService;

    @Override
    public List<TurnoEspFuncionario> getLista(TurnoEspFuncionario turnoEspFuncionario, int first, int pageSize,
            String sortField, String sortOrder) {

        Query query = createQueryFilter(turnoEspFuncionario, sortField, sortOrder);
        //System.out.println("queryEspUsu " + query.toString());
        if (first > 0) {
            query.setFirstResult(first).setMaxResults(pageSize);
        }
        try {
            List<TurnoEspFuncionario> lista = new ArrayList<TurnoEspFuncionario>();
            List<Object[]> resultList = query.getResultList();
            if (resultList != null && !resultList.isEmpty()) {
                List<TurnoEspFuncionario> turnos = new ArrayList<TurnoEspFuncionario>();
                for (Object[] o : resultList) {
                    TurnoEspFuncionario tef = new TurnoEspFuncionario();
                    tef.setTefFini((Date) o[0]);
                    tef.setTefFfin((Date) o[1]);
                    tef.setFuncionario((Funcionario) o[2]);
                    tef.setTurnoEspecial((TurnoEspecial) o[3]);
                    tef.setTefEstado((String) o[4]);
                    tef.setGroupId((Long) o[5]);
                    lista.add(tef);
                }
            }

            for (TurnoEspFuncionario tef : lista) {
                if (programacionService.
                        isFechaProgramada(tef.getTefFini(), tef.getFuncionario().getDependencia().getDepId(),
                        EstadoProgramacion.PROGRAMADA)) {
                    tef.setEditable(false);
                    tef.setAnullable(false);//anulables
                }
                //tef.setAnullable(turnoEspFunEnProgramacion(tef));
            }
            return lista;

        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    @Override
    public List<TurnoEspFuncionario> getLista(TurnoEspFuncionario turnoEspFuncionario) {

        Query query = createQueryFilter(turnoEspFuncionario, null, null);
        try {
            List<TurnoEspFuncionario> lista = new ArrayList<TurnoEspFuncionario>();
            List<Object[]> resultList = query.getResultList();
            if (resultList != null && !resultList.isEmpty()) {
                List<TurnoEspFuncionario> turnos = new ArrayList<TurnoEspFuncionario>();
                for (Object[] o : resultList) {
                    TurnoEspFuncionario tef = new TurnoEspFuncionario();
                    tef.setTefFini((Date) o[0]);
                    tef.setTefFfin((Date) o[1]);
                    tef.setFuncionario((Funcionario) o[2]);
                    tef.setTurnoEspecial((TurnoEspecial) o[3]);
                    tef.setTefEstado((String) o[4]);
                    tef.setGroupId((Long) o[5]);
                    lista.add(tef);
                }
            }

            for (TurnoEspFuncionario tef : lista) {
                if (programacionService.
                        isFechaProgramada(tef.getTefFini(), tef.getFuncionario().getDependencia().getDepId(),
                        EstadoProgramacion.PROGRAMADA)) {
                    tef.setEditable(false);
                    tef.setAnullable(false);//anulables
                }
                //tef.setAnullable(turnoEspFunEnProgramacion(tef));
            }
            return lista;

        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    private Query createQueryFilter(TurnoEspFuncionario turnoEspFuncionario, String sortField, String sortOrder) {

        Map<String, Object> params = new HashMap<String, Object>();

        List<String> condiciones = new ArrayList<String>();




        if (turnoEspFuncionario.getTurnoEspecial().getTeId() != null) {
            condiciones.add("d.turnoEspecial.teId = :turEs ");
            params.put("turEs", turnoEspFuncionario.getTurnoEspecial().getTeId());
        }
        if (turnoEspFuncionario.getFuncionario().getFunId() != null && !"".equals(turnoEspFuncionario.getFuncionario().getFunId())) {
            condiciones.add("d.funcionario.funId = :fun ");
            params.put("fun", turnoEspFuncionario.getFuncionario().getFunId());
        }
        if (turnoEspFuncionario.getFuncionario().getFunAlias() != null && !"".equals(turnoEspFuncionario.getFuncionario().getFunAlias())) {
            condiciones.add("upper(d.funcionario.funAlias) like :funAlias ");
            params.put("funAlias", "%" + turnoEspFuncionario.getFuncionario().getFunAlias().toUpperCase() + "%");
        }
        if (turnoEspFuncionario.getFuncionario().getFunNombre() != null && !"".equals(turnoEspFuncionario.getFuncionario().getFunNombre())) {
            condiciones.add("upper(d.funcionario.funNombre) like :funNom ");
            params.put("funNom", "%" + turnoEspFuncionario.getFuncionario().getFunNombre().toUpperCase() + "%");
        }

        if (turnoEspFuncionario.getTurnoEspecial().getDependencia().getDepId() != null) {
            condiciones.add("d.turnoEspecial.dependencia.depId = :dep ");
            params.put("dep", turnoEspFuncionario.getTurnoEspecial().getDependencia().getDepId());
        }

        if (turnoEspFuncionario.getTurnoEspecial().getDependencia().getDepcategoria().getDcId() != null && !"".equals(turnoEspFuncionario.getTurnoEspecial().getDependencia().getDepcategoria().getDcId())) {
            condiciones.add("d.turnoEspecial.dependencia.depcategoria.dcId = :dc ");
            params.put("dc", turnoEspFuncionario.getTurnoEspecial().getDependencia().getDepcategoria().getDcId());
        }
        if (turnoEspFuncionario.getTurnoEspecial().getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("d.turnoEspecial.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero", turnoEspFuncionario.getTurnoEspecial().getDependencia().getAeropuerto().getAeId());
        }

        if (turnoEspFuncionario.getTurnoEspecial().getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("d.turnoEspecial.dependencia.aeropuerto.regional.regId = :regional ");
            params.put("regional", turnoEspFuncionario.getTurnoEspecial().getDependencia().getAeropuerto().getRegional().getRegId());
        }
        if (turnoEspFuncionario.getTurnoEspecial().getTeSigla() != null) {
            condiciones.add("d.turnoEspecial.teSigla = :sigla ");
            params.put("sigla", turnoEspFuncionario.getTurnoEspecial().getTeSigla());
        }
        if (turnoEspFuncionario.getTefEstado() != null && !"".equals(turnoEspFuncionario.getTefEstado())) {
            condiciones.add("d.tefEstado = :estado ");
            params.put("estado", turnoEspFuncionario.getTefEstado());
        }

        if (turnoEspFuncionario.getTefFini() != null && turnoEspFuncionario.getTefFfin() != null) {

            condiciones.add("((d.tefFini >= :fini and d.tefFfin <= :ffin) or (d.tefFini>= :fini and d.tefFini<= :ffin) or (d.tefFfin>= :fini and d.tefFfin<= :ffin) or ((:fini between d.tefFini and d.tefFfin) AND (:ffin BETWEEN d.tefFini  AND d.tefFfin)))");
            params.put("fini", turnoEspFuncionario.getTefFini());
            params.put("ffin", turnoEspFuncionario.getTefFfin());
        }

        if (turnoEspFuncionario.getTefFini() != null && turnoEspFuncionario.getTefFfin() == null) {

            condiciones.add("d.tefFini = :fini ");
            params.put("fini", turnoEspFuncionario.getTefFini());

        }
        if (turnoEspFuncionario.getTefFini() == null && turnoEspFuncionario.getTefFfin() != null) {

            condiciones.add(" d.tefFfin = :ffin");
            params.put("ffin", turnoEspFuncionario.getTefFfin());
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

        Query query = em.createQuery("Select count(d) from TurnoEspFuncionario d  " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("select MIN(d.tefFini), MAX(d.tefFfin), d.funcionario, d.turnoEspecial, d.tefEstado, d.groupId "
                + " from TurnoEspFuncionario d ").append(strQry.toString()).append(" GROUP BY d.groupId, d.funcionario , d.turnoEspecial , d.tefEstado ");
        /*StringBuilder strQryFinal = new StringBuilder("Select d from TurnoEspFuncionario d ").
         append(strQry.toString());*/
        if (sortField != null && sortOrder != null) {
            if (sortField.equalsIgnoreCase("tefFini")) {
                strQryFinal.append(" order by Min(d.").append(sortField).append(") ").append(sortOrder);
            } else if (sortField.equalsIgnoreCase("tefFfin")) {
                strQryFinal.append(" order by Max(d.").append(sortField).append(") ").append(sortOrder);
            } else {
                strQryFinal.append(" order by d.").append(sortField).append(" ").append(sortOrder);
            }
        } else {
            strQryFinal.append(" order by MIN(d.tefFini) desc ");
        }
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        count = new Long(query.getResultList().size());

        return query;

    }

    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public boolean isRangoTurnoDisponible(TurnoEspFuncionario tef) {
        Query query = em.createQuery("select t from TurnoEspFuncionario t where t.funcionario.funId = :funId and ((t.tefFini between :inicio and :final)  or (t.tefFfin between :inicio and :final))");
        query.setParameter("funId", tef.getFuncionario().getFunId());
        query.setParameter("inicio", tef.getTefFini());
        query.setParameter("final", tef.getTefFfin());
        List resultList = query.getResultList();
        return !(resultList != null && !resultList.isEmpty());
    }

    @Override
    public TurnoEspFuncionario guardar(TurnoEspFuncionario turnoEspFuncionario, Funcionario f) throws Exception {
        if (turnoEspFuncionario.getTefId() == null && turnoEspFuncionario.getGroupId() == null) {
            turnoEspFuncionario.setTefEstado("Programado");
        } else {
            if (turnoEspFuncionario.getTefEstado().equalsIgnoreCase("Anulado")) {
//                if (turnoEspFuncionario.getTefId() != null) {
//                    String sql = "Select t from Turno t where t.turPosicionJornada = :turId";
//                    Query q = em.createQuery(sql);
//                    q.setParameter("turId", turnoEspFuncionario.getTefId());
//                    List resultList = q.getResultList();
//                    if (resultList != null && !resultList.isEmpty()) {
//                        throw new Exception("Este turno ya se encuentra en una programacion");
//                    }
//                } else {
//                String jpql = "select t "
//                        + " from TurnoEspFuncionario t "
//                        + " where t.groupId = :groupId ";
//                Query q = em.createQuery(jpql);
//                q.setParameter("groupId", turnoEspFuncionario.getGroupId());
//                List<TurnoEspFuncionario> resultList = q.getResultList();


//                for (TurnoEspFuncionario t : resultList) {
//                    Query query = em.createQuery(
//                            "DELETE FROM Turno t WHERE t.turPosicionJornada = :tpj");
//                    query.setParameter("tpj", t.getTefId()).executeUpdate();
//                    String sql = "Select t from Turno t where t.turPosicionJornada = :turId";
//                    Query q1 = em.createQuery(sql);
//                    q1.setParameter("turId", t.getTefId());
//                    List resultList1 = q1.getResultList();
////                        if (resultList1 != null && !resultList1.isEmpty()) {
////                            throw new Exception("Este turno ya se encuentra en una programacion");
////                        }
//                }
//                }
                System.out.println("f = " + f);
            }
        }
        List resultList = null;

        try {
            Query q = em.createNamedQuery("TurnoEspFuncionario.findRepeat");
            q.setParameter("funId", turnoEspFuncionario.getFuncionario().getFunId());
            q.setParameter("teId", turnoEspFuncionario.getTurnoEspecial().getTeId());
            q.setParameter("ini", turnoEspFuncionario.getTefFini());
            q.setParameter("fin", turnoEspFuncionario.getTefFfin());
            resultList = q.getResultList();
        } catch (Exception e) {
        }
        if (resultList
                != null && resultList.size()
                > 0 && !turnoEspFuncionario.getTefEstado().equalsIgnoreCase("Anulado")) {
            for (Object obj : resultList) {
                TurnoEspFuncionario aux = (TurnoEspFuncionario) obj;
                if (turnoEspFuncionario.getTurnoEspecial().getTeHinicio() >= aux.getTurnoEspecial().getTeHinicio()
                        && (turnoEspFuncionario.getTurnoEspecial().getTeHinicio() <= aux.getTurnoEspecial().getTeHfin()
                        || turnoEspFuncionario.getTurnoEspecial().getTeHfin() >= aux.getTurnoEspecial().getTeHinicio())
                        && turnoEspFuncionario.getTurnoEspecial().getTeHfin() <= aux.getTurnoEspecial().getTeHfin()
                        && turnoEspFuncionario.getGroupId() != aux.getGroupId()) {
                    return null;
                }
            }
        } else {
            Query q = em.createNamedQuery("TurnoEspFuncionario.findByFechaRango");
            q.setParameter("funId", turnoEspFuncionario.getFuncionario().getFunId());
            q.setParameter("fechaIni", turnoEspFuncionario.getTefFini());
            q.setParameter("fechaFin", turnoEspFuncionario.getTefFfin());
            resultList = q.getResultList();
            if (resultList != null && resultList.size() > 0 && !turnoEspFuncionario.getTefEstado().equalsIgnoreCase("Anulado")) {
                for (Object obj : resultList) {
                    TurnoEspFuncionario aux = (TurnoEspFuncionario) obj;
                    if (turnoEspFuncionario.getTurnoEspecial().getTeHinicio() >= aux.getTurnoEspecial().getTeHinicio()
                            && (turnoEspFuncionario.getTurnoEspecial().getTeHinicio() <= aux.getTurnoEspecial().getTeHfin()
                            || turnoEspFuncionario.getTurnoEspecial().getTeHfin() >= aux.getTurnoEspecial().getTeHinicio())
                            && turnoEspFuncionario.getTurnoEspecial().getTeHfin() <= aux.getTurnoEspecial().getTeHfin()
                            && turnoEspFuncionario.getGroupId() != aux.getGroupId()) {
                        return null;
                    }
                }
            } else {
                Query q1 = em.createNamedQuery("TurnoEspFuncionario.findByRango");
                q1.setParameter("funId", turnoEspFuncionario.getFuncionario().getFunId());
                q1.setParameter("fechaIni", turnoEspFuncionario.getTefFini());
                q1.setParameter("fechaFin", turnoEspFuncionario.getTefFfin());
                resultList = q1.getResultList();
                if (resultList != null && resultList.size() > 0 && !turnoEspFuncionario.getTefEstado().equalsIgnoreCase("Anulado")) {
                    for (Object obj : resultList) {
                        TurnoEspFuncionario aux = (TurnoEspFuncionario) obj;
                        if (turnoEspFuncionario.getTurnoEspecial().getTeHinicio() >= aux.getTurnoEspecial().getTeHinicio()
                                && (turnoEspFuncionario.getTurnoEspecial().getTeHinicio() <= aux.getTurnoEspecial().getTeHfin()
                                || turnoEspFuncionario.getTurnoEspecial().getTeHfin() >= aux.getTurnoEspecial().getTeHinicio())
                                && turnoEspFuncionario.getTurnoEspecial().getTeHfin() <= aux.getTurnoEspecial().getTeHfin()
                                && turnoEspFuncionario.getGroupId() != aux.getGroupId()) {
                            return null;
                        }
                    }
                }
            }
        }

        System.out.println(
                "");
        if (turnoEspFuncionario.getAccion() != null && turnoEspFuncionario.getAccion().equalsIgnoreCase("Edicion")) {
            Query query = em.createQuery(
                    "DELETE FROM TurnoEspFuncionario t WHERE t.groupId = :p");
            query.setParameter("p", turnoEspFuncionario.getGroupId()).executeUpdate();
        }
        turnoEspFuncionario.setTurnoEspecial(em.find(TurnoEspecial.class, turnoEspFuncionario.getTurnoEspecial().getTeId()));
        turnoEspFuncionario.setFuncionario(em.find(Funcionario.class, turnoEspFuncionario.getFuncionario().getFunId()));

        if (validarRangoMismoMes(turnoEspFuncionario)) {
            turnoEspFuncionario.setGroupId(obtenerGrupoId());
            turnoEspFuncionario = (TurnoEspFuncionario) auditoria.auditar(turnoEspFuncionario, f);
        } else {
            int meses = cantidadMesesRango(turnoEspFuncionario);
            Long grupo = obtenerGrupoId();
            for (int i = 0; i <= meses; i++) {
                if (i == 0) {
                    Calendar fin = Calendar.getInstance();
                    fin.setTime(turnoEspFuncionario.getTefFini());
                    fin.set(Calendar.DAY_OF_MONTH, fin.getActualMaximum(Calendar.DAY_OF_MONTH));
                    TurnoEspFuncionario tef = crearTurnoEspecial(turnoEspFuncionario);
                    tef.setTefFfin(fin.getTime());
                    tef.setGroupId(grupo);
                    auditoria.auditar(tef, f);
                } else if (i == meses) {
                    Calendar inicio = Calendar.getInstance();
                    inicio.setTime(turnoEspFuncionario.getTefFfin());
                    inicio.set(Calendar.DAY_OF_MONTH, inicio.getActualMinimum(Calendar.DAY_OF_MONTH));
                    TurnoEspFuncionario tef = crearTurnoEspecial(turnoEspFuncionario);
                    tef.setTefFini(inicio.getTime());
                    tef.setGroupId(grupo);
                    turnoEspFuncionario = (TurnoEspFuncionario) auditoria.auditar(tef, f);
                } else {
                    Calendar actual = Calendar.getInstance();
                    Calendar fin = Calendar.getInstance();

                    actual.setTime(turnoEspFuncionario.getTefFini());
                    actual.set(Calendar.DAY_OF_MONTH, actual.getActualMinimum(Calendar.DAY_OF_MONTH));
                    actual.add(Calendar.MONTH, i);

                    fin.setTime(actual.getTime());
                    fin.set(Calendar.DAY_OF_MONTH, fin.getActualMaximum(Calendar.DAY_OF_MONTH));

                    TurnoEspFuncionario tef = crearTurnoEspecial(turnoEspFuncionario);
                    tef.setTefFini(actual.getTime());
                    tef.setTefFfin(fin.getTime());
                    tef.setGroupId(grupo);
                    auditoria.auditar(tef, f);
                }
            }
        }
        return turnoEspFuncionario;
    }

    @Override
    public boolean turnoEspFunEnProgramacion(TurnoEspFuncionario turnoEspFuncionario) {
        String sql = "";
        Query q1 = null;
        Query q2 = null;
        List<Turno> resultList2 = null;

        if (turnoEspFuncionario.getGroupId() != null) {
            sql = "select t from TurnoEspFuncionario t where t.groupId = :groupId";
            q1 = em.createQuery(sql);
            q1.setParameter("groupId", turnoEspFuncionario.getGroupId());

            List<TurnoEspFuncionario> resultList1 = q1.getResultList();

            for (TurnoEspFuncionario t : resultList1) {
                q2 = em.createQuery("select t from Turno t where t.turPosicionJornada = :tefId");
                q2.setParameter("tefId", t.getTefId());
                resultList2 = q2.getResultList();
                if (resultList2 != null && !resultList2.isEmpty()) {
                    return true;
                }
            }
            return false;
        } else {
            q1 = em.createQuery("select t from Turno t where t.turPosicionJornada = :tefId and t.programacion.proEstado = 1");
            q1.setParameter("tefId", turnoEspFuncionario.getTefId());
            resultList2 = q1.getResultList();
            return (resultList2 != null && !resultList2.isEmpty());
        }
    }

    private boolean validarRangoMismoMes(TurnoEspFuncionario turnoEspFuncionario) {
        Calendar inicio = Calendar.getInstance();
        inicio.setTime(turnoEspFuncionario.getTefFini());
        Calendar fin = Calendar.getInstance();
        fin.setTime(turnoEspFuncionario.getTefFfin());

        return (inicio.get(Calendar.MONTH) == fin.get(Calendar.MONTH));
    }

    private int cantidadMesesRango(TurnoEspFuncionario turnoEspFuncionario) {
        Calendar inicio = Calendar.getInstance();
        inicio.setTime(turnoEspFuncionario.getTefFini());
        Calendar fin = Calendar.getInstance();
        fin.setTime(turnoEspFuncionario.getTefFfin());

        return (fin.get(Calendar.MONTH) - inicio.get(Calendar.MONTH));
    }

    private TurnoEspFuncionario crearTurnoEspecial(TurnoEspFuncionario te) {
        TurnoEspFuncionario tef = new TurnoEspFuncionario();
        tef.setComision(te.getComision());
        tef.setEditable(te.isEditable());
        tef.setFuncionario(te.getFuncionario());
        tef.setTefEstado(te.getTefEstado());
        tef.setTefFfin(te.getTefFfin());
        tef.setTefFini(te.getTefFini());
        tef.setTurnoEspecial(te.getTurnoEspecial());
        return tef;
    }

    private Long obtenerGrupoId() {
        Query q = em.createNativeQuery("SELECT SEQ_TUR_ESPEC_FUN_GROUP.nextval from DUAL");
        BigDecimal result = (BigDecimal) q.getSingleResult();
        return result.longValue();
    }

    @Override
    public List<TurnoEspFuncionario> listarTurnos(Funcionario funcionario) {
        Query q = em.createNamedQuery("TurnoEspFuncionario.findByFuncionario");
        q.setParameter("funId", funcionario.getFunId());
        List resultList = q.getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            return resultList;
        }
        return new ArrayList<TurnoEspFuncionario>();
    }

    @Override
    public List<TurnoEspFuncionario> listarTurnosAsignacion(Funcionario funcionario) {
        try {
            String jpql = "select MIN(t.tefFini), MAX(t.tefFfin), t.funcionario, t.turnoEspecial, t.tefEstado, t.groupId "
                    + " from TurnoEspFuncionario t "
                    + " where t.funcionario.funId = :funId "
                    + " GROUP BY t.groupId, t.funcionario , t.turnoEspecial , t.tefEstado "
                    + " order by MIN(t.tefFini) desc";
            Query q = em.createQuery(jpql);
            q.setParameter("funId", funcionario.getFunId());
            List<Object[]> resultList = q.getResultList();
            if (resultList != null && !resultList.isEmpty()) {
                List<TurnoEspFuncionario> turnos = new ArrayList<TurnoEspFuncionario>();
                for (Object[] o : resultList) {
                    TurnoEspFuncionario tef = new TurnoEspFuncionario();
                    tef.setTefFini((Date) o[0]);
                    tef.setTefFfin((Date) o[1]);
                    tef.setFuncionario((Funcionario) o[2]);
                    tef.setTurnoEspecial((TurnoEspecial) o[3]);
                    tef.setTefEstado((String) o[4]);
                    tef.setGroupId((Long) o[5]);
                    turnos.add(tef);
                }
                return turnos;
            }
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
        return new ArrayList<TurnoEspFuncionario>();
    }

    @Override
    public void eliminar(TurnoEspFuncionario turno) throws Exception {
        String jpql = "select t "
                + " from TurnoEspFuncionario t "
                + " where t.groupId = :groupId ";
        Query q = em.createQuery(jpql);
        q.setParameter("groupId", turno.getGroupId());
        List<TurnoEspFuncionario> resultList = q.getResultList();


        for (TurnoEspFuncionario t : resultList) {
            String sql = "Select t from Turno t where t.turPosicionJornada = :turId";
            Query q1 = em.createQuery(sql);
            q1.setParameter("turId", t.getTefId());
            List resultList1 = q1.getResultList();
            if (resultList1 != null && !resultList1.isEmpty()) {
                throw new Exception("Este turno ya se encuentra en una programacion");
            }
            TurnoEspFuncionario tef = em.find(TurnoEspFuncionario.class, t.getTefId());
            em.remove(tef);
        }
    }

    @Override
    public String validarDisponibilidadTurno(Date date, Funcionario f) {
        String result = "OK";
        //1. turno asignado el mismo dia
        Query q = em.createQuery("Select t from TurnoEspFuncionario t where :fecha >= t.tefFini and :fecha <= t.tefFfin and t.funcionario.funId = :funId");
        q.setParameter("fecha", date);
        q.setParameter("funId", f.getFunId());
        List resultList = q.getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            result = "La fecha seleccionada ya tiene un turno especial programado";
        }
        return result;
    }
}
