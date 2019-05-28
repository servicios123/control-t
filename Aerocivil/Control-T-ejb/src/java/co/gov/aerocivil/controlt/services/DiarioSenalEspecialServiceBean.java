/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.DiarioSenalCategoria;
import co.gov.aerocivil.controlt.entities.DiarioSenalEspecial;
import co.gov.aerocivil.controlt.entities.DiarioSenalFuncionario;
import co.gov.aerocivil.controlt.entities.DiarioSenalInfo;
import co.gov.aerocivil.controlt.entities.DiarioSenalLocalizacion;
import co.gov.aerocivil.controlt.entities.DiarioSenalTipo;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.entities.Turno;
import co.gov.aerocivil.controlt.util.QueryUtil;
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
public class DiarioSenalEspecialServiceBean implements DiarioSenalEspecialService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    @EJB
    private AuditoriaService auditoria;

    @Override
    public Jornada obtenerJornadaPorHora(int hora, long dependencia) {

        Query query = em.createQuery("Select j From Jornada j Where j.dependencia.depId = :depId and j.joHoraInicio <= :hora  and j.joHoraFin >= :hora order by j.joId asc");
        query.setParameter("hora", hora);
        query.setParameter("depId", dependencia);
        try {
            List list = query.getResultList();
            return (Jornada) list.get(0);
        } catch (NoResultException nre) {
            return new Jornada();

        }

    }

    @Override
    public DiarioSenalEspecial obtenerDsePorFecha(Date fecha, long jornada) {

        Query query = em.createQuery("Select d From DiarioSenalEspecial d Where d.dseFecha = :fecha and d.dseJornadaOp.joId = :jornadaId");
        query.setParameter("fecha", fecha, TemporalType.DATE);
        query.setParameter("jornadaId", jornada);
        try {
            return (DiarioSenalEspecial) query.getSingleResult();
        } catch (NoResultException nre) {
            return new DiarioSenalEspecial();
        }
    }

    @Override
    public DiarioSenalFuncionario obtenerDsfPorDse(long dse, long funcionario) {
        Query query = em.createQuery("Select d From DiarioSenalFuncionario d Where d.dsfDiarioSenalEspecial.dseId=:dseCod and d.dsfFuncionario.funId=:funId");
        query.setParameter("dseCod", dse);
        query.setParameter("funId", funcionario);
        try {
            return (DiarioSenalFuncionario) query.getSingleResult();
        } catch (NoResultException nre) {
            return new DiarioSenalFuncionario();
        }
    }

    @Override
    public DiarioSenalEspecial guardarDse(DiarioSenalEspecial diarioSenalEspecial, Funcionario funcionario) throws Exception {
        //SELECT d FROM DiarioSenalEspecial d WHERE d.dseJornadaOp.joId = :dseJornadaOp and d.dseFecha = :dseFecha
        Query q = em.createNamedQuery("DiarioSenalEspecial.findByJopFecha");
        q.setParameter("dseJornadaOp", diarioSenalEspecial.getDseJornadaOp().getJoId());
        q.setParameter("dseFecha", diarioSenalEspecial.getDseFecha(), TemporalType.DATE);
        List diarios = null;
        if (!diarioSenalEspecial.getDseEstado().equalsIgnoreCase("CERRADO")) {
            try {
                diarios = q.getResultList();
            } catch (NoResultException e) {
                throw new Exception("Ya existe un diario de señal para la Jornada y Fecha establecida");
            }
            if (diarios != null && diarios.size() > 0) {
                throw new Exception("Ya existe un diario de señal para la Jornada y Fecha establecida");
            }
        }
        return (DiarioSenalEspecial) auditoria.auditar(diarioSenalEspecial, funcionario);
    }

    @Override
    public Posicion obtenerPosicionPorTurnoFunc(Date fecha, long jornada, long funcionario) {
        /* Aqui obtengo la posicion por el turno del funcionario gracias a la fecha sin emabrgo 
         * como posicion jornada del turno no esta familizarizada entonces se vuelve un poco mas complicado buscar
         */
        Query query = em.createQuery("SELECT t FROM Turno t WHERE t.funcionario.funId = :funId and t.turFecha = :fecha and t.turTipo=1 and t.programacion.proEstado=1");
        query.setParameter("fecha", fecha, TemporalType.DATE);
        query.setParameter("funId", funcionario);


        try {
            Turno turno = (Turno) query.getSingleResult();
            /*//System.out.println("Posicion Jornada\t"+turno.getTurPosicionJornada()+"\tjornada\t"+jornada+"\t");*/
            Query resultado = em.createQuery("SELECT p.posicion FROM PosicionJornada p WHERE p.pjId = :pjID and p.jornada.joId =:joId");
            resultado.setParameter("pjID", turno.getTurPosicionJornada());
            resultado.setParameter("joId", jornada);

            try {
                return (Posicion) resultado.getSingleResult();
            } catch (NoResultException nre) {
                return new Posicion();
            }


        } catch (NoResultException nre) {
            return new Posicion();
        }



    }

    @Override
    public List<Posicion> obtenerPosicionesDelFunc(long funcionario) {
        Query query = em.createQuery("SELECT p FROM Posicion p WHERE p.dependencia.depId = :depId and p.posEstado = :estado");
        query.setParameter("depId", 306);
        query.setParameter("estado", "Activo");
        try {
            return (List<Posicion>) query.getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public DiarioSenalFuncionario guardarDsf(DiarioSenalFuncionario diarioSenalFuncionario, Funcionario funcionario) {
        return (DiarioSenalFuncionario) auditoria.auditar(diarioSenalFuncionario, funcionario);
    }

    @Override
    public DiarioSenalEspecial obtenerPorId(long dse) {
        Query query = em.createQuery("Select d From DiarioSenalEspecial d Where d.dseId=:dseId");
        query.setParameter("dseId", dse);
        try {
            return (DiarioSenalEspecial) query.getSingleResult();
        } catch (NoResultException nre) {
            return new DiarioSenalEspecial();
        }
    }

    @Override
    public List<DiarioSenalTipo> obtenerTipos() {
        try {

            Query query = em.createQuery("SELECT d FROM DiarioSenalTipo d");
            return (List<DiarioSenalTipo>) query.getResultList();

        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public List<DiarioSenalInfo> diarioSenalInfosPorDse(long diarioSenalEspecial) {
        try {
            Query query = em.createQuery("SELECT d FROM DiarioSenalInfo d WHERE d.dsiDiarioSenalFuncionario.dsfDiarioSenalEspecial.dseId= :dseId");
            query.setParameter("dseId", diarioSenalEspecial);
            return (List<DiarioSenalInfo>) query.getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public List<DiarioSenalLocalizacion> obtenerDSL() {
        try {
            Query query = em.createQuery("SELECT d FROM DiarioSenalLocalizacion d ORDER BY d.dslAlias");
            query.setMaxResults(10);
            return (List<DiarioSenalLocalizacion>) query.getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public DiarioSenalTipo obtenerTipoPorId(long id) {
        try {
            Query query = em.createQuery("SELECT d FROM DiarioSenalTipo d WHERE d.dstId = :dstId");
            query.setParameter("dstId", id);
            //System.out.println("ID QUE ENTRA A BEAN\t" + id);

            return (DiarioSenalTipo) query.getSingleResult();
        } catch (NoResultException nre) {
            return new DiarioSenalTipo();
        }
    }

    @Override
    public DiarioSenalInfo guardarDsi(DiarioSenalInfo diarioSenalInfo, Funcionario funcionario) {
        return (DiarioSenalInfo) auditoria.auditar(diarioSenalInfo, funcionario);
    }

    @Override
    public DiarioSenalLocalizacion obtenerDslPorId(long id) {
        Query query = em.createQuery("Select d From DiarioSenalLocalizacion d Where d.dslId = :dslId");
        query.setParameter("dslId", id);
        try {
            return (DiarioSenalLocalizacion) query.getSingleResult();
        } catch (NoResultException nre) {
            return new DiarioSenalLocalizacion();
        }
    }

    @Override
    public List<DiarioSenalInfo> diarioSenalInfosPorDseDst(long diarioSenalEspecial, long diarioSenalTipo) {
        try {
            Query query = em.createQuery("SELECT d FROM DiarioSenalInfo d WHERE d.dsiDiarioSenalFuncionario.dsfDiarioSenalEspecial.dseId= :dseId and d.dsiTipo.dstId =:dstId order by d.dsiId");
            query.setParameter("dseId", diarioSenalEspecial);
            query.setParameter("dstId", diarioSenalTipo);
            return (List<DiarioSenalInfo>) query.getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public DiarioSenalFuncionario obtenerDsfPorId(long dsf) {
        Query query = em.createQuery("Select d From DiarioSenalFuncionario d Where d.dsfId=:dsfId");
        query.setParameter("dsfId", dsf);
        try {
            return (DiarioSenalFuncionario) query.getSingleResult();
        } catch (NoResultException nre) {
            return new DiarioSenalFuncionario();
        }
    }

    @Override
    public List<DiarioSenalCategoria> obtenerCategorias() {
        try {
            Query query = em.createQuery("SELECT d FROM DiarioSenalCategoria d ");

            return (List<DiarioSenalCategoria>) query.getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public DiarioSenalCategoria obtenerCategoriaPorId(long id) {
        Query query = em.createQuery("Select d From DiarioSenalCategoria d Where d.dscId=:dscId");
        query.setParameter("dscId", id);
        try {
            return (DiarioSenalCategoria) query.getSingleResult();
        } catch (NoResultException nre) {
            return new DiarioSenalCategoria();
        }
    }

    @Override
    public List<Object[]> cierreTurno(long dse) {
        String sql = "SELECT ID,PRODUCTO,SERIE,COUNT(SERIE) PUBLICADAS,COUNT(EI) EI,COUNT(EE) EE FROM VISTA_DS_CIERRE_TURNO WHERE DIARIO_SENAL_ESPECIAL=?1 GROUP BY ID,PRODUCTO,SERIE ORDER BY ID,PRODUCTO,SERIE";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, dse);
        return (List<Object[]>) query.getResultList();

    }

    @Override
    public void eliminarDSI(DiarioSenalInfo dsi) {
        StringBuilder strQry = new StringBuilder();
        Query q = em.createNativeQuery(strQry.toString());

        strQry.append("DELETE FROM DIARIO_SENAL_INFO d WHERE d.dsi_id=")
                .append(dsi.getDsiId());


        q = em.createNativeQuery(strQry.toString());
        q.executeUpdate();

    }

    @Override
    public List<DiarioSenalLocalizacion> filtroLocalizaciones(String nombre) {

        try {
            Query query = em.createQuery("SELECT d FROM DiarioSenalLocalizacion d WHERE d.dslAlias LIKE :alias or d.dslAlias LIKE :aliasMinus or d.dslAlias LIKE :aliasMayus ORDER BY d.dslAlias");
            query.setParameter("alias", "%" + nombre + "%");
            query.setParameter("aliasMinus", "%" + nombre.toLowerCase() + "%");
            query.setParameter("aliasMayus", "%" + nombre.toUpperCase() + "%");
            query.setMaxResults(10);
            List<DiarioSenalLocalizacion> lista = (List<DiarioSenalLocalizacion>) query.getResultList();

            return lista;
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public List<DiarioSenalEspecial> getLista(DiarioSenalEspecial diarioSenalEspecial, Integer first, Integer pageSize, String sortField, String sortOrder) {
        try {
            Query query = createQueryFilter(diarioSenalEspecial, sortField, sortOrder);
            if (first != null && pageSize != null) {
                query.setFirstResult(first).setMaxResults(pageSize);
            }
            return (List<DiarioSenalEspecial>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    private Query createQueryFilter(DiarioSenalEspecial diarioSenalEspecial, String sortField, String sortOrder) {
        List<String> condiciones = new ArrayList<String>();
        StringBuilder strQry = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();

        if (diarioSenalEspecial.getDseEstado() != null) {
            condiciones.add("d.dseEstado = :dseEstado");
            params.put("dseEstado", diarioSenalEspecial.getDseEstado());
        }
        if (diarioSenalEspecial.getDseFecha() != null) {
            if (diarioSenalEspecial.getDseFechaFinal() != null) {
                condiciones.add("d.dseFecha between :dseFecha and :dseFechaFinal");
                params.put("dseFecha", diarioSenalEspecial.getDseFecha());
                params.put("dseFechaFinal", diarioSenalEspecial.getDseFechaFinal());
            } else {
                condiciones.add("d.dseFecha = :dseFecha");
                params.put("dseFecha", diarioSenalEspecial.getDseFecha());
            }


        }
        if (diarioSenalEspecial.getDseDependencia() != null) {
            condiciones.add("d.dseDependencia.depId = :dseDependencia");
            params.put("dseDependencia", diarioSenalEspecial.getDseDependencia().getDepId());
        }
        if (diarioSenalEspecial.getDseJornadaOp() != null) {
            condiciones.add("d.dseJornadaOp.joId = :dseJornadaOp");
            params.put("dseJornadaOp", diarioSenalEspecial.getDseJornadaOp().getJoId());
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

        Query query = em.createQuery("Select count(d) from DiarioSenalEspecial d  " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("Select d from DiarioSenalEspecial d ").append(strQry.toString());


        if (sortField != null && sortOrder != null) {
            strQryFinal.append(" order by d.").append(sortField).append(" ").append(sortOrder);

        } else {
            strQryFinal.append(" order by d.dseFecha desc");

        }

        query = em.createQuery(strQryFinal.toString());
        //System.out.println("DiarioSenalEspecialService\t" + strQryFinal.toString());
        QueryUtil.setParameters(query, params);
        return query;

    }

    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public List<DiarioSenalFuncionario> obtenerDsfsPorDse(long diarioSenalEspecial, String estado) {

        String jpql = "Select d From DiarioSenalFuncionario d Where d.dsfDiarioSenalEspecial.dseId=:dseCod ";
        if (estado != null) {
            jpql += " and d.dsfEstado=:estado";
        }


        Query query = em.createQuery(jpql);

        if (estado != null) {
            query.setParameter("estado", estado);
        }
        query.setParameter("dseCod", diarioSenalEspecial);

        try {
            return (List<DiarioSenalFuncionario>) query.getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public DiarioSenalInfo obtenerDsiPorId(long id) {
        Query query = em.createQuery("Select d From DiarioSenalInfo d Where d.dsiId= :dsiId");
        query.setParameter("dsiId", id);
        try {
            return (DiarioSenalInfo) query.getSingleResult();
        } catch (NoResultException nre) {
            return new DiarioSenalInfo();
        }
    }

    @Override
    public DiarioSenalFuncionario obtenerDsfPorDseAnterior(long fun) {
        Calendar hoy = Calendar.getInstance();
        Calendar ayer = Calendar.getInstance();
        int hora = hoy.get(Calendar.HOUR_OF_DAY);
        ayer.add(Calendar.DATE, -1);

        Query query = em.createQuery("SELECT d FROM DiarioSenalFuncionario d WHERE NOT (  :hora >= d.dsfDiarioSenalEspecial.dseJornadaOp.joHoraInicio and :hora <= d.dsfDiarioSenalEspecial.dseJornadaOp.joHoraFin) and (d.dsfDiarioSenalEspecial.dseFecha between :ayer and :hoy) and d.dsfFuncionario.funId=:funId and d.dsfDiarioSenalEspecial.dseEstado = :estado ORDER BY d.dsfDiarioSenalEspecial.dseId DESC ");
        query.setParameter("funId", fun);
        query.setParameter("estado", "ABIERTO");
        query.setParameter("hoy", hoy.getTime(), TemporalType.DATE);
        query.setParameter("ayer", ayer.getTime(), TemporalType.DATE);
        query.setParameter("hora", hora);

        query.setMaxResults(1);
        try {
            return (DiarioSenalFuncionario) query.getSingleResult();
        } catch (NoResultException nre) {
            return new DiarioSenalFuncionario();
        }
    }
}
