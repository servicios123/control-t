/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.PermisoEspecial;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.TurnoEspFuncionario;
import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.math.BigDecimal;
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
 * @author Administrador
 */
@Stateless
public class PermisoServiceBean implements PermisoService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    @EJB
    private AuditoriaService auditoria;

    @Override
    public PermisoEspecial guardar(PermisoEspecial permiso, Funcionario f) {
        return (PermisoEspecial) auditoria.auditar(permiso, f);
    }

    @Override
    public List<Jornada> getJornadasPermiso(PermisoEspecial permiso) {
        PermisoEspecial find = em.find(PermisoEspecial.class, permiso.getPeId());
        return find.getListaJornadas();
    }

    @Override
    public List<PermisoEspecial> getLista(PermisoEspecial permisoFiltro, int first, int pageSize, String sortField, String sortOrder) {
        Query query = createQueryFilter(permisoFiltro, sortField, sortOrder);
        if (first > 0) {
            query.setFirstResult(first).setMaxResults(pageSize);
        }
        try {
            return (List<PermisoEspecial>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    private Query createQueryFilter(PermisoEspecial permiso, String sortField, String sortOrder) {
        List<String> condiciones = new ArrayList<String>();

        Map<String, Object> params = new HashMap<String, Object>();
        if (permiso.getFuncionario() != null) {
            if (permiso.getFuncionario().getFunNombre() != null) {
                condiciones.add(" (p.funcionario.funNombre) like :nombre");
                params.put("nombre", "%" + permiso.getFuncionario().getFunNombre().toUpperCase() + "%");
            }
            if (permiso.getFuncionario().getFunAlias() != null) {
                condiciones.add(" (p.funcionario.funAlias) like :nombre");
                params.put("nombre", "%" + permiso.getFuncionario().getFunAlias().toUpperCase() + "%");
            }
            if (permiso.getFuncionario().getFunId() != null) {
                condiciones.add(" (p.funcionario.funId) = :funId");
                params.put("funId", permiso.getFuncionario().getFunId());
            }
            /*if(permiso.getFuncionario().getFuNivel()!=null ){
             //ej: nivel 3 ve a los de nivel 4
             condiciones.add(" (p.funcionario.fuNivel) > :nivel");
             params.put("nivel",  permiso.getFuncionario().getFuNivel()); 
             }*/
        }
        if (permiso.getPeFechaPermiso() != null) {
            condiciones.add(" (p.peFechaPermiso) = :fechaPermiso");
            params.put("fechaPermiso", permiso.getPeFechaPermiso());
        }
        if (permiso.getPeFechaRegistro() != null) {
            condiciones.add(" (p.peFechaRegistro) = :fechaRegistro");
            params.put("fechaRegistro", permiso.getPeFechaRegistro());
        }
        if (permiso.getPeEstado() != null) {
            condiciones.add(" (p.peEstado) = :estado");
            params.put("estado", permiso.getPeEstado());
        }

        if (permiso.getDependencia() != null
                && permiso.getDependencia().getDepcategoria() != null
                && permiso.getDependencia().getDepcategoria().getDcId() != null) {
            condiciones.add("p.funcionario.dependencia.depcategoria.dcId = :depCat ");
            params.put("depCat", permiso.getDependencia().getDepcategoria().getDcId());
        }
        if (permiso.getDependencia() != null && permiso.getDependencia().getDepId() != null) {
            condiciones.add("p.funcionario.dependencia.depId = :dep ");
            params.put("dep", permiso.getDependencia().getDepId());
        }

        if (permiso.getDependencia() != null
                && permiso.getDependencia().getAeropuerto() != null
                && permiso.getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("p.funcionario.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero", permiso.getDependencia().getAeropuerto().getAeId());
        } else if (permiso.getDependencia() != null
                && permiso.getDependencia().getAeropuerto().getRegional() != null
                && permiso.getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("p.funcionario.dependencia.aeropuerto.regional.regId = :reg ");
            params.put("reg", permiso.getDependencia().getAeropuerto().getRegional().getRegId());
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

        Query query = em.createQuery("Select count(p) from PermisoEspecial p ".concat(strQry.toString()));
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("Select p from PermisoEspecial p ").
                append(strQry.toString());
        if (sortField != null && sortOrder != null) {
            strQryFinal.append(" order by p.").append(sortField).append(" ").append(sortOrder);
        }
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;

    }

    /**
     *
     * @return
     */
    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public boolean existeProgramacion(PermisoEspecial permiso) {
        BigDecimal count;
        StringBuilder strQry = new StringBuilder();
        strQry.append(" select count(*) from programacion where pro_dependencia = ").
                append(permiso.getDependencia().getDepId()).append(" and '").
                append(dateToString(permiso.getPeFechaPermiso())).
                append("' between pro_fecha_inicio and pro_fecha_fin");
        Query q = em.createNativeQuery(strQry.toString());
        count = (BigDecimal) q.getSingleResult();
        //System.out.println(strQry.toString()+" - "+count);
        return count != null && count.longValue() > 0;

    }

    private String dateToString(Date d) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(d);
    }

    @Override
    public void solvePetitions(Programacion programacion) {
        TurnoEspecial te = getTurnoEspecial(programacion.getDependencia());
        List<PermisoEspecial> pes = getPetitions(programacion.getDependencia(), programacion.getProFechaInicio(), programacion.getProFechaFin());
        if (pes != null) {
            List<TurnoEspFuncionario> tefs = getSpecialTurns(programacion.getDependencia(), programacion.getProFechaInicio(), programacion.getProFechaFin(), te.getTeId());
            for (PermisoEspecial pe : pes) {
                boolean asignado = false;
                if (tefs != null) {
                    for (TurnoEspFuncionario tef : tefs) {
                        if (tef.getFuncionario().getFunId() == pe.getFuncionario().getFunId()
                                && pe.getPeFechaPermiso().compareTo(tef.getTefFini()) >= 0
                                && pe.getPeFechaPermiso().compareTo(tef.getTefFfin()) <= 0) {
                            asignado = true;
                            break;
                        }
                    }
                }
                if (!asignado) {
                    saveTEF(pe, te);
                }
            }

        }

    }

    private void saveTEF(PermisoEspecial pe, TurnoEspecial te) {
        TurnoEspFuncionario tef = new TurnoEspFuncionario();
        tef.setComision(null);
        tef.setEditable(true);
        tef.setFuncionario(pe.getFuncionario());
        tef.setTefEstado("Programado");
        tef.setTefFfin(pe.getPeFechaPermiso());
        tef.setTefFini(pe.getPeFechaPermiso());
        tef.setTurnoEspecial(te);
        em.persist(tef);
    }

    private TurnoEspecial getTurnoEspecial(Dependencia dep) {

        try {

            Query query = em.createQuery("SELECT t FROM TurnoEspecial t WHERE t.dependencia.depId=:depId AND t.teSigla = :sigla ");

            query.setParameter("depId", dep.getDepId());
            query.setParameter("sigla", "PET");
            query.setMaxResults(1);

            return (TurnoEspecial) query.getSingleResult();

        } catch (NoResultException nre) {

            TurnoEspecial te = new TurnoEspecial();
            te.setDependencia(dep);
            te.setTeEstado("Activo");
            te.setTeHfin((byte) 23);
            te.setTeHinicio((byte) 0);
            te.setTeHoraLaborada(Boolean.TRUE);
            te.setTeMfin((byte) 59);
            te.setTeMinicio((byte) 0);
            te.setTeNombre("Peticiones");
            te.setTeSigla("PET");

            em.persist(te);

            if (te.getTeId() != null) {
                //System.out.println("Si guardo :)");
            } else {
                //System.out.println("No guardo :(");
            }

            return te;

        }

    }

    private List<TurnoEspFuncionario> getSpecialTurns(Dependencia dep, Date fechaIni, Date fechaFin, long te) {
        try {
            Calendar actual = Calendar.getInstance();
            Query query = em.createQuery("SELECT t FROM TurnoEspFuncionario t WHERE t.turnoEspecial.teId = :te AND t.turnoEspecial.dependencia.depId= :depId AND t.tefEstado= :estado AND t.funcionario.dependencia.depId = :depId AND t.funcionario.funFvCertmedico > :fecha AND t.funcionario.funFvCurso > :fecha AND t.funcionario.funFvEvaluacion > :fecha AND ((t.tefFini between :start and :finish) or (t.tefFini between :start and :finish)) ");
            query.setParameter("te", te);
            query.setParameter("depId", dep.getDepId());
            query.setParameter("estado", "Programado");
            query.setParameter("start", fechaIni, TemporalType.DATE);
            query.setParameter("finish", fechaFin, TemporalType.DATE);
            query.setParameter("fecha", actual.getTime(), TemporalType.DATE);

            return (List<TurnoEspFuncionario>) query.getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    private List<PermisoEspecial> getPetitions(Dependencia dep, Date fechaIni, Date fechaFin) {

        try {
            Calendar actual = Calendar.getInstance();
            Query query = em.createQuery("SELECT p FROM PermisoEspecial p WHERE p.funcionario.dependencia.depId= :depId AND p.peEstado = :estado and p.peFechaPermiso between :start and :finish");
            query.setParameter("depId", dep.getDepId());
            query.setParameter("estado", "Aprobado");
            query.setParameter("start", fechaIni, TemporalType.DATE);
            query.setParameter("finish", fechaFin, TemporalType.DATE);


            return (List<PermisoEspecial>) query.getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
