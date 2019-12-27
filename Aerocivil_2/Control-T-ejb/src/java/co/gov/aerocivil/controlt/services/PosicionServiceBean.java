/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.entities.PosicionNacional;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
public class PosicionServiceBean implements PosicionService {
    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    
    @EJB
    private AuditoriaService auditoria;

    @Override
    public Posicion guardar(Posicion posicion, Funcionario fun) {
        return (Posicion) auditoria.auditar(posicion, fun);
    }
    
    
    @Override
    public List<Posicion> getLista(Posicion posicion, Integer first, Integer pageSize,
            String sortField, String sortOrder) {

        Query query = createQueryFilter(posicion, sortField, sortOrder);
        if (first != null && pageSize != null) {
            query.setFirstResult(first).setMaxResults(pageSize);
        }
        try{
            return (List<Posicion>) query.getResultList();    
        }
        catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }        
    }

        
    
    private Query createQueryFilter(Posicion posicion, String sortField, String sortOrder){

        Map<String,Object> params = new HashMap<String, Object>();

        List<String> condiciones = new ArrayList<String>();
        StringBuilder strQry = new StringBuilder();
        if (posicion.getDependencia().getAeropuerto().getRegional().getRegId()!= null ) {
            condiciones.add("f.dependencia.aeropuerto.regional.regId =:regional ");
           params.put("regional",  posicion.getDependencia().getAeropuerto().getRegional().getRegId() );
        }
        if (posicion.getDependencia().getAeropuerto().getAeId() != null ) {
            condiciones.add("f.dependencia.aeropuerto.aeId =:aero ");
           params.put("aero",  posicion.getDependencia().getAeropuerto().getAeId());
        }
       
        /*if (posicion.getPosAlias() != null && !"".equals(posicion.getPosAlias())) {
            condiciones.add("upper(f.posAlias) like :alias ");
             params.put("alias", "%" + posicion.getPosAlias().toUpperCase() + "%");
        }*/
        if (posicion.getPosicionNacional().getPnAlias() != null && !"".equals(posicion.getPosicionNacional().getPnAlias())) {
            condiciones.add("upper(f.posicionNacional.pnAlias) like :alias ");
             params.put("alias", "%" + posicion.getPosicionNacional().getPnAlias().toUpperCase() + "%");
        }
        if (posicion.getPosEstado() != null && !"".equals(posicion.getPosEstado())) {
            condiciones.add("f.posEstado = :estado ");
             params.put("estado", posicion.getPosEstado() );
        }
/*        if (posicion.getFuncion().getFuId() != null && !"".equals(posicion.getFuncion().getFuId())) {
            condiciones.add("f.funcion.fuId = :fun ");
             params.put("fun", posicion.getFuncion().getFuId() );
        }
      if (posicion.getSector().getSecId() != null && !"".equals(posicion.getSector().getSecId())) {
            condiciones.add("f.sector.secId = :sec ");
             params.put("sec", posicion.getSector().getSecId() );
        }*/
        if (posicion.getDependencia().getDepId() != null) {
            condiciones.add("f.dependencia.depId = :dep ");
           params.put("dep",  posicion.getDependencia().getDepId() );
        }
        if (posicion.getDependencia().getDepId() != null) {
            condiciones.add("f.dependencia.depId = :dep ");
           params.put("dep",  posicion.getDependencia().getDepId() );
        }
        

        if(condiciones.size()>0){
            strQry.append("where ");
        }

        strQry.append(QueryUtil.joinWithAnd(condiciones));

        Query query = em.createQuery("Select count(f) from Posicion f "+strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long)query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("Select f from Posicion f ").
                append(strQry.toString());
        if(sortField!=null && sortOrder!=null){
            strQryFinal.append("order by f.").append(sortField).append(" ").append(sortOrder);
        }else{
             strQryFinal.append("order by f.posicionNacional.pnAlias");
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
    public List<Posicion> getListaPosicionesDependencia(Posicion posicion){
        Map<String, Object> params = new java.util.HashMap<String, Object>();
        StringBuilder strQry = new StringBuilder();
        List<String> condiciones = new ArrayList<String>();
        String sql = "Select p from Posicion p ";
        if(posicion.getDependencia()!=null && posicion.getDependencia().getDepId()!=null){
            condiciones.add(" p.dependencia.depId = :depId ");
            params.put("depId", posicion.getDependencia().getDepId());
        }
        if(posicion.getDependencia().getDepcategoria() != null && posicion.getDependencia().getDepcategoria().getDcId() != null){
            condiciones.add(" p.dependencia.depcategoria.dcId = :dcId ");
            params.put("dcId", posicion.getDependencia().getDepcategoria().getDcId());
        }
        if(posicion.getDependencia().getAeropuerto().getRegional()!=null && posicion.getDependencia().getAeropuerto().getRegional().getRegId()!=null){
            condiciones.add(" p.dependencia.aeropuerto.regional.regId = :regId ");
            params.put("regId", posicion.getDependencia().getAeropuerto().getRegional().getRegId());
        }
        if(posicion.getDependencia().getAeropuerto()!=null && posicion.getDependencia().getAeropuerto().getAeId()!=null){
            condiciones.add("p.dependencia.aeropuerto.aeId = :aeId ");
            params.put("aeId", posicion.getDependencia().getAeropuerto().getAeId());
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
        
        Query query = em.createQuery("Select count(p) from Posicion p " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();
        
        StringBuilder strQryFinal = new StringBuilder("Select p from Posicion p ").
                append(strQry.toString()).append(" order by p.posId");
         
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);
        return query.getResultList();
    }

    @Override
    public List<BigDecimal> getListaPosicionesDependencia(Long dependenciaId) {
        String strQry = "Select pos_posicion_nacional from Posicion p where p.pos_dependencia="+
                dependenciaId+" and p.pos_estado='Activo'";//and p.pos_posicion_nacional is not null
        //System.out.println(strQry);
        Query query = em.createNativeQuery(strQry);
        List<BigDecimal>  lista = query.getResultList();
        //System.out.println(lista.size());
        return lista;
        
    }
    
    @Override
    public List<PosicionNacional> getListaPosicionesDependenciaObject(Long dependenciaId) {
        String strQry = "Select p.posicionNacional from Posicion p where p.dependencia.depId = :depId "+
                " and p.posEstado = :estado";//and p.pos_posicion_nacional is not null
        //System.out.println(strQry);
        Query query = em.createQuery(strQry);
        query.setParameter("depId", dependenciaId);
        query.setParameter("estado", "Activo");
        List<PosicionNacional>  lista = query.getResultList();
        //System.out.println(lista.size());
        return lista;
        
    }

    @Override
    public Posicion getPosicionByPosNal(Long posNacId, Dependencia dep) {
        StringBuilder strQry = new StringBuilder( "Select p from Posicion p where p.posicionNacional.pnId=:posNal ");
        strQry.append(" and p.dependencia.depId=:depId ");
        try{
            return (Posicion) em.createQuery(strQry.toString()).
                    setParameter("posNal", posNacId).setParameter("depId",dep.getDepId()).
                    getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    @Override
    public void inactivarPosiciones(Long[] arrayPosiciones, Dependencia dep){    
        StringBuilder strQry = new StringBuilder();
        strQry.append(" update Posicion p set p.posEstado='Inactivo' where p.posicionNacional.pnId not in :posiciones ");
        strQry.append(" and p.dependencia.depId=:depId ");
        Query query = em.createQuery(strQry.toString());
        query.setParameter("depId",dep.getDepId());
        query.setParameter("posiciones", Arrays.asList(arrayPosiciones));
        query.executeUpdate();
        
        StringBuilder strQry2 = new StringBuilder();
        strQry2.append(" update PosicionJornada P SET p.pjEstado='Inactivo' where p.posicion.posicionNacional.pnId not in :posiciones ");
        strQry2.append(" and p.posicion.dependencia.depId=:depId ");
        Query query2 = em.createQuery(strQry2.toString());
        query2.setParameter("depId",dep.getDepId());
        query2.setParameter("posiciones", Arrays.asList(arrayPosiciones));
        query2.executeUpdate();
        
        
    }

    @Override
    public Object getPorId(long id) {
        //System.out.println("Entreeee!!");
        Posicion pos= (Posicion) em.find(Posicion.class, id);
        //System.out.println("Alias\t"+pos.getPosicionNacional().getPnAlias());
        return em.find(Posicion.class, id);
       
    }
    
}