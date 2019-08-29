/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.PermisoEspecial;
import co.gov.aerocivil.controlt.entities.RestriccionDependencia;
import co.gov.aerocivil.controlt.entities.RestriccionProgramacion;
import co.gov.aerocivil.controlt.to.RestriccionTO;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.math.BigDecimal;
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
 * @author Administrador
 */
@Stateless
public class RestriccionesServiceBean implements RestriccionesService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    @EJB
    private AuditoriaService auditoria;

    @Override
    public Long obtenerValor(Long rpId, Long dependenciaID) {
        Query query = em.createQuery("SELECT r.rdValor FROM RestriccionDependencia r WHERE r.dependencia.depId = :dependencia and r.restriccionProgramacion.rpId=:rpId");
        query.setParameter("dependencia", dependenciaID);
        query.setParameter("rpId", rpId);
        try {
            Object s = (Object) query.getSingleResult();
            if (s == null) {
                return (long) 0;
            } else {
                return (Long) s;
            }
        } catch (NoResultException nre) {
            return (long) 0;
        }

    }

    @Override
    public List<RestriccionTO> guardarListaRestricciones(List<RestriccionTO> restricciones, Dependencia dep, Funcionario f) {
        StringBuilder strQry = null;
        Query query = null;
        for (RestriccionTO r : restricciones) {
            //System.out.println(r);
            if (r.getRdId() != null) { //update
                /*strQry = new StringBuilder();
                 strQry.append("UPDATE RESTRICCION_DEPENDENCIA SET RD_VALOR= ").
                 append(r.getRdValor()).append(" WHERE RD_ID= ").append(r.getRdId());
                 //System.out.println("UpdQry: " + strQry.toString());
                 query = em.createNativeQuery(strQry.toString());
                 query.executeUpdate();
                 auditoria.auditar("RESTRICCION_DEPENDENCIA", r.getRdId(), f);*/

                RestriccionDependencia rd = em.find(RestriccionDependencia.class, r.getRdId());
                rd.setRdValor(r.getRdValor());
                auditoria.auditar(rd, f);
            } else {//insert
                if (r.getRdValor() != null) {
                    RestriccionDependencia rd = new RestriccionDependencia();
                    rd.setDependencia(dep);
                    rd.setRestriccionProgramacion(new RestriccionProgramacion(r.getRpId()));
                    rd.setRdValor(r.getRdValor());
                    auditoria.auditar(rd, f);
                }
            }
        }
        return getListaRestriccionesProgramacion(dep);
    }

    @Override
    public List<RestriccionTO> getListaRestriccionesProgramacion(Dependencia dep) {
        StringBuilder strQry = new StringBuilder();
        List<RestriccionTO> listaRestricciones = new ArrayList<RestriccionTO>();
        strQry.append(" Select rp_Id, rp_Descripcion, rd_Id, rd_Valor  ");
        strQry.append(" from Restriccion_Progr rp   ");
        strQry.append(" Left Join Restriccion_dependencia rd  on rd.rd_restriccion=rp.rp_id and rd.rd_dependencia=").append(dep.getDepId());
        strQry.append(" order by rp_Id");
        Query query = em.createNativeQuery(strQry.toString());

        if (query.getResultList() != null && !query.getResultList().isEmpty()) {
            Iterator<Object[]> resultado = query.getResultList().iterator();
            while (resultado.hasNext()) {
                Object[] rs = resultado.next();
                RestriccionTO registro = new RestriccionTO();
                registro.setRpId(((BigDecimal) rs[0]).longValue());
                registro.setRpDescripcion((String) rs[1]);
                if (rs[2] != null) {
                    registro.setRdId(((BigDecimal) rs[2]).longValue());
                    if (rs[3] != null) {
                        registro.setRdValor(((BigDecimal) rs[3]).longValue());
                    }

                }

                listaRestricciones.add(registro);
            }
        }

        return listaRestricciones;
    }

    @Override
    public List<RestriccionDependencia> getListaRestriccionesDependencia(RestriccionDependencia rd) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<RestriccionDependencia> getLista(RestriccionDependencia restriccionFiltro, int first, int pageSize, String sortField, String sortOrder) {
        Query query = createQueryFilter(restriccionFiltro, sortField, sortOrder);
        if (first > 0) {
            query.setFirstResult(first).setMaxResults(pageSize);
        }
        try {
            return (List<RestriccionDependencia>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    private Query createQueryFilter(RestriccionDependencia permiso, String sortField, String sortOrder) {
        List<String> condiciones = new ArrayList<String>();

        Map<String, Object> params = new HashMap<String, Object>();
        if (permiso.getRestriccionProgramacion() != null
                && permiso.getRestriccionProgramacion().getRpDescripcion() != null) {
            condiciones.add("upper(p.restriccionProgramacion.rpDescripcion) like :descripcion ");
            params.put("descripcion", "%" + permiso.getRestriccionProgramacion().getRpDescripcion().toUpperCase() + "%");
        }
        if (permiso.getDependencia() != null
                && permiso.getDependencia().getDepcategoria() != null
                && permiso.getDependencia().getDepcategoria().getDcId() != null) {
            condiciones.add("p.dependencia.depcategoria.dcId = :depCat ");
            params.put("depCat", permiso.getDependencia().getDepcategoria().getDcId());
        }
        if (permiso.getDependencia() != null && permiso.getDependencia().getDepId() != null) {
            condiciones.add("p.dependencia.depId = :dep ");
            params.put("dep", permiso.getDependencia().getDepId());
        }

        if (permiso.getDependencia() != null
                && permiso.getDependencia().getAeropuerto() != null
                && permiso.getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("p.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero", permiso.getDependencia().getAeropuerto().getAeId());
        } else if (permiso.getDependencia() != null
                && permiso.getDependencia().getAeropuerto().getRegional() != null
                && permiso.getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("p.dependencia.aeropuerto.regional.regId = :reg ");
            params.put("reg", permiso.getDependencia().getAeropuerto().getRegional().getRegId());
        }

        StringBuilder strQry = new StringBuilder();

        if (condiciones.size() > 0) {
            strQry.append("where ");
        }
        strQry.append(QueryUtil.joinWithAnd(condiciones));
        Query query = em.createQuery("Select count(p) from RestriccionDependencia p ".concat(strQry.toString()));
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("Select p from RestriccionDependencia p ").
                append(strQry.toString());
        if (sortField != null && sortOrder != null) {
            strQryFinal.append("order by p.").append(sortField).append(" ").append(sortOrder);
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
    public List<RestriccionProgramacion> getListaFaltantesDep(Dependencia dep) {

        Query query = em.createQuery("SELECT r FROM RestriccionProgramacion r WHERE r.rpId NOT IN (SELECT rd.restriccionProgramacion.rpId FROM RestriccionDependencia rd WHERE rd.dependencia.depId = :depId)");
        query.setParameter("depId", dep.getDepId());
        try {
            return (List<RestriccionProgramacion>) query.getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public Jornada obtenerJornada(long dependencia) {
        Query query = em.createQuery("SELECT r FROM RestriccionDependencia r WHERE r.dependencia.depId = :dependencia and r.restriccionProgramacion.rpId=4");
        query.setParameter("dependencia", dependencia);
        try {
            RestriccionDependencia rd = (RestriccionDependencia) query.getSingleResult();
            Query query2 = em.createQuery("SELECT j FROM Jornada j WHERE j.joId = :joId");
            //System.out.println("Valor: "+rd.getRdValor());
            query2.setParameter("joId", rd.getRdValor());
            return (Jornada) query2.getSingleResult();
        } catch (NoResultException nre) {
            return new Jornada();
        }
    }
}
