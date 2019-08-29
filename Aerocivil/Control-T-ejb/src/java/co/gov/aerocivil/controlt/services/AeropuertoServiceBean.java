/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Ciudad;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.util.QueryUtil;
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
public class AeropuertoServiceBean implements AeropuertoService {
    
    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;

    @EJB
    private AuditoriaService auditoria;

    private Long count;
             
    @Override
    public Aeropuerto guardar(Aeropuerto aeropuerto, Funcionario func) {
        aeropuerto.setCiudad(em.find(Ciudad.class, aeropuerto.getCiudad().getCiuId()));
        aeropuerto.setRegional(em.find(Regional.class, aeropuerto.getRegional().getRegId()));
        return (Aeropuerto) auditoria.auditar(aeropuerto, func);
    }

    @Override
    public List<Aeropuerto> getLista(Aeropuerto aeropuerto, int first, int pageSize,
            String sortField, String sortOrder) {

        Query query = createQueryFilter(aeropuerto, sortField, sortOrder);
        query.setFirstResult(first).setMaxResults(pageSize);
        try{
            return (List<Aeropuerto>) query.getResultList();    
        }
        catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }        
    }
    
    private Query createQueryFilter(Aeropuerto aeropuerto, String sortField, String sortOrder){
        List<String> condiciones = new ArrayList<String>();
        
        Map<String,Object> params = new HashMap<String, Object>();
        if (aeropuerto.getAeNombre() != null) {
            condiciones.add("upper(a.aeNombre) like :nombre ");
           params.put("nombre", "%" + aeropuerto.getAeNombre().toUpperCase() + "%"); 
        }


        if (aeropuerto.getCiudad()!=null &&
                aeropuerto.getCiudad().getCiuId() != null) {
            condiciones.add("a.ciudad.ciuId = :ciudad ");
            params.put("ciudad", aeropuerto.getCiudad().getCiuId());
        }
        if (aeropuerto.getRegional()!=null&&
                aeropuerto.getRegional().getRegId() != null) {
            condiciones.add("a.regional.regId = :reg ");
            params.put("reg", aeropuerto.getRegional().getRegId());
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

        Query query = em.createQuery("Select count(a) from Aeropuerto a "+strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long)query.getSingleResult();
        
        StringBuilder strQryFinal = new StringBuilder("Select a from Aeropuerto a ").
                append(strQry.toString());
        if(sortField!=null && sortOrder!=null){
            strQryFinal.append("order by a."+sortField+" "+sortOrder);
        }
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;
        
    }
    
    @Override
    public List<Aeropuerto> getLista(Aeropuerto aeropuerto) {

        Query query = createQueryFilter(aeropuerto);
        try{
            return (List<Aeropuerto>) query.getResultList();    
        }
        catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }        
    }
    
    private Query createQueryFilter(Aeropuerto aeropuerto){
        List<String> condiciones = new ArrayList<String>();
        
        Map<String,Object> params = new HashMap<String, Object>();
        if (aeropuerto.getAeNombre() != null) {
            condiciones.add("upper(a.aeNombre) like :nombre ");
           params.put("nombre", "%" + aeropuerto.getAeNombre().toUpperCase() + "%"); 
        }


        if (aeropuerto.getCiudad()!=null &&
                aeropuerto.getCiudad().getCiuId() != null) {
            condiciones.add("a.ciudad.ciuId = :ciudad ");
            params.put("ciudad", aeropuerto.getCiudad().getCiuId());
        }
        if (aeropuerto.getRegional()!=null&&
                aeropuerto.getRegional().getRegId() != null) {
            condiciones.add("a.regional.regId = :reg ");
            params.put("reg", aeropuerto.getRegional().getRegId());
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

        Query query = em.createQuery("Select count(a) from Aeropuerto a "+strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long)query.getSingleResult();
        
        StringBuilder strQryFinal = new StringBuilder("Select a from Aeropuerto a ").
                append(strQry.toString());
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;
        
    }
    
    @Override
    public Long getCount(){
        return count;
    }

    @Override
    public Aeropuerto findOneById(Long id) {
        Query q = em.createNamedQuery("Aeropuerto.findById");
        q.setParameter("aeId", id);
        return (Aeropuerto) q.getSingleResult();    
        
    }


}
