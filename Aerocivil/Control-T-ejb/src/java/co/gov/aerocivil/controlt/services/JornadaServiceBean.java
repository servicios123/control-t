/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
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
 * @author Viviana
 */
@Stateless
public class JornadaServiceBean implements JornadaService {
    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    @EJB
    private AuditoriaService auditoria;

    @Override
    public Jornada guardar(Jornada jornada, Funcionario f) throws SQLIntegrityConstraintViolationException {
        return (Jornada) auditoria.auditar(jornada, f);
    }
    
    @Override
    public List<Jornada> getListaPag(Jornada jornada, Integer first, Integer pageSize,
            String sortField, String sortOrder) {

        Query query = createQueryFilter(jornada, sortField, sortOrder);
        if(first!=null && pageSize!=null){
            query.setFirstResult(first).setMaxResults(pageSize);
        }
        try{
            return (List<Jornada>) query.getResultList();    
        }
        catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }        
    }
    
    @Override
    public List<Jornada> getListaPag(Jornada jornada) {

        Query query = createQueryFilter(jornada);
        try{
            return (List<Jornada>) query.getResultList();    
        }
        catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }        
    }

        
    
    private Query createQueryFilter(Jornada jornada, String sortField, String sortOrder){

        Map<String,Object> params = new HashMap<String, Object>();

        List<String> condiciones = new ArrayList<String>();
        StringBuilder strQry = new StringBuilder();
        if (jornada.getJoAlias() != null && !"".equals(jornada.getJoAlias())) {
            condiciones.add("upper(f.joAlias) like :nombre ");
            params.put("nombre", "%" + jornada.getJoAlias().toUpperCase() + "%");

        }
        if (jornada.getJoEstado() != null && !"".equals(jornada.getJoEstado())) {
            condiciones.add("f.joEstado = :estado ");
            params.put("estado", jornada.getJoEstado());

        }
        if (jornada.getDependencia().getDepcategoria()!= null) {
            condiciones.add("f.dependencia.depcategoria.dcId = :depcateg ");
           params.put("depcateg",  jornada.getDependencia().getDepcategoria().getDcId() );
        }
        if (jornada.getDependencia().getDepId() != null) {
            condiciones.add("f.dependencia.depId = :dep ");
           params.put("dep",  jornada.getDependencia().getDepId() );
        }
        else{
            if (jornada.getDependencia().getAeropuerto() != null &&
                    jornada.getDependencia().getAeropuerto().getAeId()!=null) {
                condiciones.add("f.dependencia.aeropuerto.aeId = :aero ");
               params.put("aero",  jornada.getDependencia().getAeropuerto().getAeId() );
            }

            else if (jornada.getDependencia().getAeropuerto() != null
                    && jornada.getDependencia().getAeropuerto().getRegional()!=null
                    && jornada.getDependencia().getAeropuerto().getRegional().getRegId()!=null) {
                condiciones.add("f.dependencia.aeropuerto.regional.regId = :regId ");
               params.put("regId",  jornada.getDependencia().getAeropuerto().getRegional().getRegId() );
            }
        }

        if(condiciones.size()>0){
            strQry.append("where ");
            strQry.append(QueryUtil.joinWithAnd(condiciones));
        }

        Query query = em.createQuery("Select count(f) from Jornada f "+strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long)query.getSingleResult();
        
        StringBuilder strQryFinal = new StringBuilder("Select f from Jornada f ").
                append(strQry.toString());
        if(sortField!=null && sortOrder!=null){
            strQryFinal.append(" order by f.").append(sortField).append(" ").append(sortOrder);
        }
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;
    }
    
    private Query createQueryFilter(Jornada jornada){

        Map<String,Object> params = new HashMap<String, Object>();

        List<String> condiciones = new ArrayList<String>();
        StringBuilder strQry = new StringBuilder();
        if (jornada.getJoAlias() != null && !"".equals(jornada.getJoAlias())) {
            condiciones.add("upper(f.joAlias) like :nombre ");
            params.put("nombre", "%" + jornada.getJoAlias().toUpperCase() + "%");

        }
        if (jornada.getJoEstado() != null && !"".equals(jornada.getJoEstado())) {
            condiciones.add("f.joEstado = :estado ");
            params.put("estado", jornada.getJoEstado());

        }
        if (jornada.getDependencia().getDepcategoria()!= null) {
            condiciones.add("f.dependencia.depcategoria.dcId = :depcateg ");
           params.put("depcateg",  jornada.getDependencia().getDepcategoria().getDcId() );
        }
        if (jornada.getDependencia().getDepId() != null) {
            condiciones.add("f.dependencia.depId = :dep ");
           params.put("dep",  jornada.getDependencia().getDepId() );
        }
        else{
            if (jornada.getDependencia().getAeropuerto() != null &&
                    jornada.getDependencia().getAeropuerto().getAeId()!=null) {
                condiciones.add("f.dependencia.aeropuerto.aeId = :aero ");
               params.put("aero",  jornada.getDependencia().getAeropuerto().getAeId() );
            }

            else if (jornada.getDependencia().getAeropuerto() != null
                    && jornada.getDependencia().getAeropuerto().getRegional()!=null
                    && jornada.getDependencia().getAeropuerto().getRegional().getRegId()!=null) {
                condiciones.add("f.dependencia.aeropuerto.regional.regId = :regId ");
               params.put("regId",  jornada.getDependencia().getAeropuerto().getRegional().getRegId() );
            }
        }

        if(condiciones.size()>0){
            strQry.append("where ");
            strQry.append(QueryUtil.joinWithAnd(condiciones));
        }

        Query query = em.createQuery("Select count(f) from Jornada f "+strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long)query.getSingleResult();
        
        StringBuilder strQryFinal = new StringBuilder("Select f from Jornada f ").
                append(strQry.toString());
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;
    }
    
    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public Jornada getJornadaAnterior(Jornada jornada) {
        StringBuilder strQry = new StringBuilder();
        strQry.append("Select j.jo_id from jornada_op j where ");
        strQry.append(" to_char(to_date('").append(jornada.getJoHoraInicio()).append("','HH24:MI'),'HH24:MI') = ");
        strQry.append("to_char(to_date(j.jo_hora_fin||':'||j.jo_min_fin,'HH24:MI')+1/1440,'HH24:MI') ");
        strQry.append(" and j.jo_estado='Activo' and j.jo_dependencia=").
                append(jornada.getDependencia().getDepId());
        Query q = em.createNativeQuery(strQry.toString());
        //System.out.println(strQry.toString());
        Long jorId = QueryUtil.getLongNativeQuery(q);
        if(jorId!=null){
            return em.find(Jornada.class, jorId);
        }
        return null;
    }    
    
    /**
     *
     * @param dependenciaId
     * @param jornadaId
     * @return
     */
    @Override
    public List<BigDecimal> getListaJornadasRestriccion(Jornada jornada) {
        String strQry = "Select jr_jornada_ev as restriccion from jornada_op p, jornada_restriccion jr where p.jo_dependencia="+
                jornada.getDependencia().getDepId()+" and jr.jr_jornada=p.jo_id and p.jo_estado='Activo' and jr_jornada =  "+jornada.getJoId();
        Query query = em.createNativeQuery(strQry);        
        return (List<BigDecimal>)query.getResultList();
        
    }

    @Override
    public List<Jornada> getListaJornadasDisponibles(Dependencia dep, Long jorId) {
        Map<String,Object> params = new HashMap<String, Object>();

      //  List<String> condiciones = new ArrayList<String>();
        StringBuilder strQry = new StringBuilder("Select f from Jornada f where ");
        
        strQry.append("f.dependencia.depId = :dep ");
        strQry.append("and f.joId != :jorId ");   
        strQry.append("and f.joEstado='Activo' ");   
        params.put("dep",  dep.getDepId() );
        params.put("jorId",  jorId );
        
        Query query = em.createQuery(strQry.toString());
        QueryUtil.setParameters(query, params);
        return query.getResultList();        
    }

    @Override
    public Jornada getPorId(long id) {
        Query query = em.createNamedQuery("Jornada.findByJoId");
        query.setParameter("joId", id);
        try
        {
            return (Jornada) query.getSingleResult();
        }
        catch(NoResultException nre)
        {
            return null;
        }           
        
    }

    


}
