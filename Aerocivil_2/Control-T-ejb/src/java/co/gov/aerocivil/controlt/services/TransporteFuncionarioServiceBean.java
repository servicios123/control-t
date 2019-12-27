/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.Transporte;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.util.ArrayList;
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
import javax.swing.SortOrder;

/**
 *
 * @author Administrador
 */
@Stateless
public class TransporteFuncionarioServiceBean implements TransporteFuncionarioService {
    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    
    private Long count;

    @EJB
    private AuditoriaService auditoria;

    @Override
    public Transporte guardar(Transporte transporte, Funcionario f) {
        
        return (Transporte) auditoria.auditar(transporte, f);
    }

    @Override
    public void borrar(Transporte transporte){
    
            Query query = em.createQuery("delete from Transporte f  where f.funcionario.funId =:fun "
            + "and f.jornada.joId = :jor and f.traFecha = :fecha")
            .setParameter("fun", transporte.getFuncionario().getFunId())
            .setParameter("jor", transporte.getJornada().getJoId())
            .setParameter("fecha", transporte.getTraFecha());
            
            query.executeUpdate();
    }
    
    @Override
    public List<Transporte> getLista(Transporte transporteFiltro, 
        Integer first, Integer pageSize, String sortField, String sortOrder) {
        Query query = createQueryFilter(transporteFiltro, sortField, sortOrder);
        if(first!=null && pageSize != null){
            query.setFirstResult(first).setMaxResults(pageSize);
        }
        try{
            return (List<Transporte>) query.getResultList();    
        }
        catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }        
    }
    
    @Override
    public Transporte obtenerObjeto(Funcionario funcionario, Date fecha, Jornada jornada){
    Query query = em.createQuery("Select f from Transporte f  where f.funcionario.funId =:fun "
            + "and f.jornada.joId = :jor and f.traFecha = :fecha")
            .setParameter("fun", funcionario.getFunId())
            .setParameter("jor", jornada.getJoId())
            .setParameter("fecha", fecha);
        
        
        return (Transporte) query.getSingleResult();
    }

        private Query createQueryFilter(Transporte transporteFiltro, String sortField, String sortOrder){
        List<String> condiciones = new ArrayList<String>();
        
        Map<String,Object> params = new HashMap<String, Object>();
        if (transporteFiltro.getTraFecha()  != null ) {
            condiciones.add("f.traFecha = :fecha ");
            params.put("fecha", transporteFiltro.getTraFecha());
        }
        if (transporteFiltro.getJornada().getJoId()  != null ) {
            condiciones.add("f.jornada.joId = :jo ");
            params.put("jo", transporteFiltro.getJornada().getJoId());
        }
        if (transporteFiltro.getFuncionario().getDependencia() != null
                && transporteFiltro.getFuncionario().getDependencia().getDepcategoria() != null
                && transporteFiltro.getFuncionario().getDependencia().getDepcategoria().getDcId() != null) {
            condiciones.add("f.funcionario.dependencia.depcategoria.dcId = :depCat ");
            params.put("depCat", transporteFiltro.getFuncionario().getDependencia().getDepcategoria().getDcId());
        }
        if (transporteFiltro.getFuncionario().getDependencia() != null && transporteFiltro.getFuncionario().getDependencia().getDepId() != null) {
            condiciones.add("f.funcionario.dependencia.depId = :dep ");
            params.put("dep", transporteFiltro.getFuncionario().getDependencia().getDepId());
        }

        if (transporteFiltro.getFuncionario().getDependencia().getAeropuerto() != null && transporteFiltro.getFuncionario().getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("f.funcionario.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero", transporteFiltro.getFuncionario().getDependencia().getAeropuerto().getAeId());
        } 
        else if (transporteFiltro.getFuncionario().getDependencia().getAeropuerto().getRegional() != null && transporteFiltro.getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("f.funcionario.dependencia.aeropuerto.regional.regId = :reg ");
            params.put("reg", transporteFiltro.getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
        }
        
        StringBuilder strQry = new StringBuilder();
        
        if(condiciones.size()>0){
            strQry.append(" where ");
            strQry.append(QueryUtil.joinWithAnd(condiciones));
        }        
        
        Query query = em.createQuery("Select count(f) from Transporte f "+strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long)query.getSingleResult();
        
        StringBuilder strQryFinal = new StringBuilder("Select f from Transporte f ").
                append(strQry.toString());
        if(sortField!=null && sortOrder!=null){
            strQryFinal.append("order by f.").append(sortField).append(" ").append(sortOrder);
        }
        //System.out.println("strQry: "+strQryFinal);
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;
        
    }
    
    @Override
    public Long getCount() {
        return count;
    }


}
