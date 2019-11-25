/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.entities.PosicionJornada;
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
public class PosicionJornadaServiceBBean implements PosicionJornadaService {

     // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    
    @EJB
    private AuditoriaService auditoria;


    @Override
    public PosicionJornada guardar(PosicionJornada posicionJornada, Funcionario f) {
         posicionJornada.setDependencia(em.find(Dependencia.class, posicionJornada.getDependencia().getDepId()));        
/*         posicionJornada.setJornada(em.find(Jornada.class, posicionJornada.getJornada().getJoId()));        
         posicionJornada.setPosicion(em.find(Posicion.class, posicionJornada.getPosicion().getPosId())); 
         posicionJornada.setPjAlias(posicionJornada.getJornada().getJoAlias()+posicionJornada.getPosicion().getPosAlias());*/
         posicionJornada.setPjAlias(posicionJornada.getJornada().getJoAlias()+posicionJornada.getPosicion().getPosicionNacional().getPnAlias());
         return (PosicionJornada) auditoria.auditar(posicionJornada, f);
    }
    
    @Override
    public List<PosicionJornada> getLista(PosicionJornada posicionJornada, int first, int pageSize, 
            String sortField, String sortOrder){
        
        try {            
            Query query = createQueryFilter(posicionJornada, sortField, sortOrder);
            query.setFirstResult(first).setMaxResults(pageSize);
            return (List<PosicionJornada>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }

    }
    
    
    private Query createQueryFilter(PosicionJornada posicionJornada, String sortField, String sortOrder){
        
        List<String> condiciones = new ArrayList<String>();
            StringBuilder strQry = new StringBuilder();
         Map<String,Object> params = new java.util.HashMap<String, Object>();
        
         if (posicionJornada.getPosicion().getPosicionNacional().getPnAlias() != null && !"".equals(posicionJornada.getPosicion().getPosicionNacional().getPnAlias())) {
            condiciones.add("upper(t.posicion.posicionNacional.pnAlias) = :alias ");
            params.put("alias", posicionJornada.getPosicion().getPosicionNacional().getPnAlias().toUpperCase());
        }
        if (posicionJornada.getPjEstado()!= null  && !"".equals(posicionJornada.getPjEstado())) {
            condiciones.add("t.pjEstado = :estado ");
            params.put("estado",  posicionJornada.getPjEstado());
        }
       
        if (posicionJornada.getDependencia().getAeropuerto().getRegional().getRegId() != null ) {
            condiciones.add("t.dependencia.aeropuerto.regional.regId = :reg ");
            params.put("reg",  posicionJornada.getDependencia().getAeropuerto().getRegional().getRegId() );
        }
        
        if (posicionJornada.getDependencia().getAeropuerto().getAeId() != null ) {
            condiciones.add("t.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero",  posicionJornada.getDependencia().getAeropuerto().getAeId() );
        }
        
        if (posicionJornada.getDependencia().getDepId() != null ) {
            condiciones.add("t.dependencia.depId = :dep ");
            params.put("dep",  posicionJornada.getDependencia().getDepId() );
        }
        if (posicionJornada.getDependencia().getDepcategoria() != null && posicionJornada.getDependencia().getDepcategoria().getDcId() !=null && !"".equals(posicionJornada.getDependencia().getDepcategoria().getDcId())) {
            condiciones.add("t.dependencia.depcategoria.dcId = :depcat ");
            params.put("depcat",  posicionJornada.getDependencia().getDepcategoria().getDcId() );
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

        Query query = em.createQuery("Select count(t) from PosicionJornada t "+strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long)query.getSingleResult();
        
        StringBuilder strQryFinal = new StringBuilder("Select t from PosicionJornada t ").
                append(strQry.toString());
        if(sortField!=null && sortOrder!=null){
            strQryFinal.append("order by t.").append(sortField).append(" ").append(sortOrder);
        }
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);
        return query;
    }
    
    @Override
    public Long getCount() {
        return count;
    }
}
