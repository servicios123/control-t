/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.sql.SQLIntegrityConstraintViolationException;
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
public class DependenciaServiceBean implements DependenciaService{

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;

    @EJB
    private AuditoriaService auditoria;

    @Override
    public Dependencia guardar(Dependencia dependencia, Funcionario f) throws SQLIntegrityConstraintViolationException {
        
        dependencia.setAeropuerto(em.find(Aeropuerto.class, dependencia.getAeropuerto().getAeId()));        
        dependencia.setDepcategoria(em.find(DepCategoria.class, dependencia.getDepcategoria().getDcId()));  
        return (Dependencia) auditoria.auditar(dependencia,f);
    }
    
    public List<Dependencia> getLista(Dependencia dependencia, int first, int pageSize,
            String sortField, String sortOrder) {

        Query query = createQueryFilter(dependencia, sortField, sortOrder);
        query.setFirstResult(first).setMaxResults(pageSize);
        try{
            return (List<Dependencia>) query.getResultList();    
        }
        catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }        
    }
    
    private Query createQueryFilter(Dependencia dependencia, String sortField, String sortOrder){
        
        Map<String,Object> params = new HashMap<String, Object>();
        
            List<String> condiciones = new ArrayList<String>();
            

            if (dependencia.getDepNombre() != null) {
                condiciones.add("upper(d.depNombre) like :nombre ");
                params.put("nombre", "%" + dependencia.getDepNombre().toUpperCase() + "%");
            }
            if (dependencia.getAeropuerto().getAeId() != null) {
                condiciones.add("d.aeropuerto.aeId = :aero ");
                params.put("aero", dependencia.getAeropuerto().getAeId());
            }
            if (dependencia.getDepAbreviatura() != null) {
                condiciones.add("upper(d.depAbreviatura) like :abr ");
                params.put("abr", "%" + dependencia.getDepAbreviatura().toUpperCase() + "%");
            }
            if (dependencia.getDepcategoria().getDcId() != null) {
                condiciones.add("d.depcategoria.dcId = :categoria ");
                params.put("categoria", dependencia.getDepcategoria().getDcId() );
            }
            
            if (dependencia.getAeropuerto().getRegional().getRegId() != null) {
                condiciones.add("d.aeropuerto.regional.regId = :regional ");
                params.put("regional", dependencia.getAeropuerto().getRegional().getRegId() );
            }

            StringBuilder strQry = new StringBuilder();
            if(condiciones.size()>0){
                strQry.append("where ");
            }
            for (Iterator<String> it = condiciones.iterator(); it.hasNext();) {
                strQry.append(it.next());
                if(it.hasNext()){
                    strQry.append(" and ");
                }
            }

        Query query = em.createQuery("Select count(d) from Dependencia d "+strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long)query.getSingleResult();
        
        StringBuilder strQryFinal = new StringBuilder("Select d from Dependencia d ").
                append(strQry.toString());
        if(sortField!=null && sortOrder!=null){
            strQryFinal.append("order by d.").append(sortField).append(" ").append(sortOrder);
        }
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;
        
    }
    
    @Override
    public List<Dependencia> getDepsAts(Aeropuerto aeropuerto){
        Query q = em.createQuery("Select d from Dependencia d where d.depcategoria.dcId = 1 and d.aeropuerto.aeId = :aeId");
        q.setParameter("aeId", aeropuerto.getAeId());
        return q.getResultList();
    }
    
    @Override
    public Long getCount(){
        return count;
    }

    @Override
    public Dependencia findByd(Long id) {
        Query q = em.createNamedQuery("Dependencia.findById");
        q.setParameter("depId", id);
        return (Dependencia) q.getSingleResult();
    }

}
