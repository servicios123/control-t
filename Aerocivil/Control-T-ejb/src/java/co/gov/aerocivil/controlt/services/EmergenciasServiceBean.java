/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;


import co.gov.aerocivil.controlt.entities.Emergencias;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
public class EmergenciasServiceBean implements EmergenciasService {

     @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public List<Emergencias> getLista(Emergencias emergencias, int first, int pageSize, 
            String sortField, String sortOrder){
        
        try {            
            Query query = createQueryFilter(emergencias, sortField, sortOrder);
            query.setFirstResult(first).setMaxResults(pageSize);
            return (List<Emergencias>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }

    }
    
    
    private Query createQueryFilter(Emergencias emergencias, String sortField, String sortOrder){
        
        List<String> condiciones = new ArrayList<String>();
            StringBuilder strQry = new StringBuilder();
        Map<String,Object> params = new HashMap<String, Object>();
        
        if (emergencias.getEmFechaini()!= null && emergencias.getEmFechafin()!= null ) {
            condiciones.add("s.emFechaini between :fini and :ffin ");
            params.put("fini", emergencias.getEmFechaini() );
            params.put("ffin", emergencias.getEmFechafin() );
        }
        if (emergencias.getEmFechaini()!= null && emergencias.getEmFechafin()== null ) {
            condiciones.add("s.emFechaini = :fini ");
            params.put("fini", emergencias.getEmFechaini() );
           
        }
        if (emergencias.getEmFechaini()== null && emergencias.getEmFechafin()!= null ) {
            condiciones.add("s.emFechaini = :ffin ");
            
            params.put("ffin", emergencias.getEmFechafin() );
        }
        
        if (emergencias.getFuncionario().getFunAlias() != null && !"".equals(emergencias.getFuncionario().getFunAlias()) ) {
            condiciones.add("s.funcionario.funAlias = :falias ");
            params.put("falias",  emergencias.getFuncionario().getFunAlias() );
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

        Query query = em.createQuery("Select count(s) from Emergencias s "+strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long)query.getSingleResult();
        
        StringBuilder strQryFinal = new StringBuilder("Select s from Emergencias s ").
                append(strQry.toString());
        if(sortField!=null && sortOrder!=null){
            strQryFinal.append("order by s.").append(sortField).append(" ").append(sortOrder);
        }
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);
        return query;
    }
    @Override
    public Long getCount(){
        return count;
    }
    
     @Override
    public Emergencias guardar(Emergencias emergencias) {
         
        emergencias.setFuncionario(em.find(Funcionario.class, emergencias.getFuncionario().getFunId()));      
         return em.merge(emergencias);

    }

    @Override
    public List<Emergencias> getListaPag(Emergencias emergencias, Integer first, Integer pageSize,
            String sortField, String sortOrder) {

        Query query = createQueryFilter(emergencias, sortField, sortOrder);
        if(first!=null && pageSize!=null){
            query.setFirstResult(first).setMaxResults(pageSize);
        }
        try{
            return (List<Emergencias>) query.getResultList();    
        }
        catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }        
    }
}
