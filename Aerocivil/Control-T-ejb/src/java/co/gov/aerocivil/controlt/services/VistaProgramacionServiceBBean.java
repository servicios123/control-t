/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.PosNoAsig;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import co.gov.aerocivil.controlt.util.QueryUtil;
import co.gov.aerocivil.controlt.util.StringDateUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
public class VistaProgramacionServiceBBean implements VistaProgramacionService {

   @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;

   
    
    @Override
    public List<Vistaprogramacion> getLista(Vistaprogramacion vistaProgramacion, int first, int pageSize,
            String sortField, String sortOrder) {

        Query query = createQueryFilter(vistaProgramacion, sortField, sortOrder);
        query.setFirstResult(first).setMaxResults(pageSize);
        try {
            List<Vistaprogramacion> lista = (List<Vistaprogramacion>) query.getResultList();
            for (Vistaprogramacion vistaprogramacion : lista) {
                em.refresh(vistaprogramacion);                
            }
            return lista;
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    private Query createQueryFilter(Vistaprogramacion vistaProgramacion, String sortField, String sortOrder) {

        Map<String, Object> params = new HashMap<String, Object>();

        List<String> condiciones = new ArrayList<String>();


      
        
        if (vistaProgramacion.getFunAlias() != null && !"".equals(vistaProgramacion.getFunAlias())) {
            condiciones.add("upper(d.funAlias) like :fAlias");
            params.put("fAlias", "%"+vistaProgramacion.getFunAlias().toUpperCase()+"%");
        }
        if (vistaProgramacion.getFunId() != null && !"".equals(vistaProgramacion.getFunId())) {
            condiciones.add("d.funId = :fun ");
            params.put("fun", vistaProgramacion.getFunId());
        }
        
       
        
        if (vistaProgramacion.getProgramacion().getDependencia().getDepId() != null) {
            condiciones.add("d.programacion.dependencia.depId = :dep ");
            params.put("dep", vistaProgramacion.getProgramacion().getDependencia().getDepId());
        }
        
         if (vistaProgramacion.getProgramacion().getDependencia().getDepcategoria().getDcId() != null && !"".equals(vistaProgramacion.getProgramacion().getDependencia().getDepcategoria().getDcId())) {
            condiciones.add("d.programacion.dependencia.depcategoria.dcId = :dc ");
            params.put("dc", vistaProgramacion.getProgramacion().getDependencia().getDepcategoria().getDcId());
        }
        if (vistaProgramacion.getProgramacion().getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("d.programacion.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero", vistaProgramacion.getProgramacion().getDependencia().getAeropuerto().getAeId());
        }

        if (vistaProgramacion.getProgramacion().getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("d.programacion.dependencia.aeropuerto.regional.regId = :regional ");
            params.put("regional", vistaProgramacion.getProgramacion().getDependencia().getAeropuerto().getRegional().getRegId());
        }
        if (vistaProgramacion.getTurEstado()!= null && !"".equals(vistaProgramacion.getTurEstado())) {
            condiciones.add("d.turEstado = :estado ");
            params.put("estado", vistaProgramacion.getTurEstado());
        }
        if(vistaProgramacion.getTurFecha()!=null ){
            
            condiciones.add("d.turFecha = :fec ");
            params.put("fec", vistaProgramacion.getTurFecha());
        }
        //condiciones.add("d.programacion.proEstado = 1 ");                
        
        if( vistaProgramacion.getTipos()!=null){
            
             condiciones.add("d.turTipo in  :tip  ");
            params.put("tip", Arrays.asList(vistaProgramacion.getTipos()));
        
        }
        
        
        StringBuilder strQry = new StringBuilder();
        if (condiciones.size() > 0) {
            strQry.append("where ");
        }
        for (Iterator<String> it = condiciones.iterator(); it.hasNext();) {
            strQry.append(it.next());
            if (it.hasNext()) {
                strQry.append(" and ");
            }
        }

        Query query = em.createQuery("Select count(d) from Vistaprogramacion d   " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("Select d from Vistaprogramacion d  ").
                append(strQry.toString());
        if (sortField != null && sortOrder != null) {
            strQryFinal.append(" order by d.").append(sortField).append(" ").append(sortOrder);
        }
        //em.getEntityManagerFactory().getCache().evictAll();
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;

    }

     @Override
    public Long validarCambio(Long TurnoId, Long nuevoFuncionario, Long DepId, boolean jornadaExtra){
        StringBuilder strQry = new StringBuilder();
      //   = em.createNativeQuery(strQry.toString());
       
    if (!jornadaExtra){
        strQry.append("select  VALIDARCAMBIOPROGRAMACIONSH (");
     }
     else{
        strQry.append("select  VALIDARCAMBIOPROGRAMACION (");
    }
                strQry.append(TurnoId)
                .append(" ,")
                .append(nuevoFuncionario)
                .append(" ,")
                .append(DepId)
                .append(" ) from dual") ;
        Query q = em.createNativeQuery(strQry.toString());
         //System.out.println("Funcion: "+strQry.toString());
       return QueryUtil.getLongNativeQuery(q);
    }
     
     @Override
     public Long validarPosicionNoAsignada(Long funcId, Long depId,Long posId, Long jornId, Date fechaTurno) {
        StringBuilder strQry = new StringBuilder();
        //   = em.createNativeQuery(strQry.toString());

        strQry.append("select VALIDAR_POS_NO_ASIGNADA (");
        strQry.append(funcId).append(", ")
                .append(depId).append(", ")
                .append(posId).append(", ")
                .append(jornId).append(", to_date('")
                .append(StringDateUtil.formatDate(fechaTurno)).append("','dd/mm/yy') ) from dual");
        Query q = em.createNativeQuery(strQry.toString());
        return QueryUtil.getLongNativeQuery(q);

    }
     
     @Override
     public Long validarAsignarEspecial(Long funcId,  Date fechaTurno, Long turnoEspecial) {
        StringBuilder strQry = new StringBuilder();
        //   = em.createNativeQuery(strQry.toString());

        strQry.append("select AsignarTurnoEspecial  (");
        strQry.append(funcId).append(", to_date('")
                .append(StringDateUtil.formatDate(fechaTurno)).append("','dd/mm/yy'),")
                .append(turnoEspecial).append(" ) from dual");
        Query q = em.createNativeQuery(strQry.toString());
        return QueryUtil.getLongNativeQuery(q);

    }

    @Override
    public Long getCount() {
        return count;
    }

   @Override 
   public void eliminarPosNoAsignada(PosNoAsig pos){
       Query q = em.createQuery("delete FROM PosNoAsig p where p.pna_id = :posId");
       q.setParameter("posId", pos.getPna_id());
       q.executeUpdate();
   }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    

}
