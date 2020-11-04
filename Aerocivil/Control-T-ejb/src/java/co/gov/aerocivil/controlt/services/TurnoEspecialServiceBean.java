/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
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
public class TurnoEspecialServiceBean implements TurnoEspecialService {
    @EJB
    private AuditoriaService audit;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    

  @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;

    @Override
    public TurnoEspecial guardar(TurnoEspecial turnoEspecial, Funcionario func) throws SQLIntegrityConstraintViolationException{
         turnoEspecial.setDependencia(em.find(Dependencia.class, turnoEspecial.getDependencia().getDepId()));        
         return (TurnoEspecial) audit.auditar(turnoEspecial, func);

    }
    
    @Override
    public TurnoEspecial findBySigla(TurnoEspecial especial) {
        try {
            Query query = em.createNamedQuery("TurnoEspecial.findByTeSigla");
            query.setParameter("teSigla", especial.getTeSigla());
            query.setParameter("depId", especial.getDependencia().getDepId());
            TurnoEspecial singleResult = (TurnoEspecial) query.getSingleResult();
            return singleResult;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<TurnoEspecial> getLista(TurnoEspecial turnoEspecial, int first, int pageSize, 
            String sortField, String sortOrder){
        
        try {            
            Query query = createQueryFilter(turnoEspecial, sortField, sortOrder);
            query.setFirstResult(first).setMaxResults(pageSize);
            return (List<TurnoEspecial>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }

    }
    
    
    private Query createQueryFilter(TurnoEspecial turnoEspecial, String sortField, String sortOrder){
        
        List<String> condiciones = new ArrayList<String>();
            StringBuilder strQry = new StringBuilder();
         Map<String,Object> params = new java.util.HashMap<String, Object>();
        if (turnoEspecial.getTeNombre() != null && !"".equals(turnoEspecial.getTeNombre())) {
            condiciones.add("upper(t.teNombre) like :nombre ");
            params.put("nombre", "%" + turnoEspecial.getTeNombre().toUpperCase() + "%");
        }
        if (turnoEspecial.getTeEstado() != null  && !"".equals(turnoEspecial.getTeEstado())) {
            condiciones.add("t.teEstado = :estado ");
            params.put("estado",  turnoEspecial.getTeEstado());
        }
        if (turnoEspecial.getTeSigla() != null  && !"".equals(turnoEspecial.getTeSigla())) {
            condiciones.add("upper(t.teSigla) like :sigla ");
            params.put("sigla","%" + turnoEspecial.getTeSigla().toUpperCase() +"%");
        }
        if (turnoEspecial.getDependencia().getDepId() != null) {
            condiciones.add("t.dependencia.depId = :dep ");
            params.put("dep",  turnoEspecial.getDependencia().getDepId() );
        }
        else{
            if (turnoEspecial.getDependencia().getAeropuerto().getAeId() != null ) {
                condiciones.add("t.dependencia.aeropuerto.aeId = :aero ");
                params.put("aero",  turnoEspecial.getDependencia().getAeropuerto().getAeId() );
            }
            else if (turnoEspecial.getDependencia().getAeropuerto().getRegional().getRegId() != null ) {
                condiciones.add("t.dependencia.aeropuerto.regional.regId = :reg ");
                params.put("reg",  turnoEspecial.getDependencia().getAeropuerto().getRegional().getRegId() );
            }
        }
        
        if (turnoEspecial.getDependencia().getDepcategoria() != null && turnoEspecial.getDependencia().getDepcategoria().getDcId() !=  null) {
            condiciones.add("t.dependencia.depcategoria.dcId = :cat ");
            params.put("cat",  turnoEspecial.getDependencia().getDepcategoria().getDcId() );
        }

        if(condiciones.size()>0){
            strQry.append("where ");
        }
        for (Iterator<String> it = condiciones.iterator(); it.hasNext();) {
            strQry.append(it.next());
            if(it.hasNext()){
                strQry.append(" and ");
            }
        }

        Query query = em.createQuery("Select count(t) from TurnoEspecial t "+strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long)query.getSingleResult();
        
        StringBuilder strQryFinal = new StringBuilder("Select t from TurnoEspecial t ").
                append(strQry.toString());
        if(sortField!=null && sortOrder!=null){
            strQryFinal.append("order by t.").append(sortField).append(" ").append(sortOrder);
        }
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);
        return query;
    }
    
    public TurnoEspecial getTropByDep(Long depId){
        Query q = em.createQuery("Select te from TurnoEspecial te where te.dependencia.depId=:depId and te.teSigla = 'TROP'");
        q.setParameter("depId", depId);
        return (TurnoEspecial) q.getSingleResult();
    }
    
    @Override
    public Long getCount() {
        return count;
    }
}
