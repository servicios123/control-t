
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.enums.EstadoProgramacion;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class ProgramacionTurnosSessionBean implements ProgramacionTurnosSession {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    
    @EJB
    private AuditoriaService auditoria;


    
    private Programacion ultimaFecha(Long dep)
    {
        Query q = em.createQuery("SELECT p FROM Programacion p WHERE p.dependencia.depId = :depId ORDER BY p.proFechaFin DESC ");
        q.setMaxResults(1);       
        q.setParameter("depId", dep);
        try 
        {
         return (Programacion) q.getSingleResult();
        }
        catch(NoResultException nre)
        {
            return null;
        }
    }
    
    
    
    
    @Override
    public Date fechaUltimaProgramacion(Dependencia dependencia) {
       
        Calendar c = Calendar.getInstance();
        Programacion p = ultimaFecha(dependencia.getDepId());
        
        if(p!=null)
        {
            c.setTime(p.getProFechaFin());  
            
            if(p.getProEstado()==1)
            {                       
                c.add(Calendar.MONTH, 1);
            }
            //System.out.println("Ultima fecha ->>> "+c.get(Calendar.DATE)+"/"+((c.get(Calendar.MONTH))+1));
            
            
        }
        else
        {
            //System.out.println("No existe programacion");
        }
        c.set(Calendar.DATE, 1);
        return c.getTime();
     
        
    }
    
    @Override
    public void borrarPendientes(Programacion programacion){
        
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder strQry = new StringBuilder();
        Query q = em.createNativeQuery(strQry.toString());
       
         strQry.append("delete turno t where  t.tur_fecha >=to_date(' ")
               .append(df.format(programacion.getProFechaInicio())).append("','dd/mm/yy') ")
               .append(" and t.tur_programacion in ( ")
               .append("Select te.pro_id from programacion te where te.pro_estado in (0,2) and ")
                .append("  te.pro_fecha_inicio=   to_date('")
                .append(df.format(programacion.getProFechaInicio())).append("','dd/mm/yy') and ")
                .append(" te.pro_dependencia = ")
                .append(programacion.getDependencia().getDepId())
                .append(" )");
    
        q = em.createNativeQuery(strQry.toString());
        q.executeUpdate();
        
        strQry = new StringBuilder();
        strQry.append("delete from programacion te where te.pro_estado in (0,2) and ")
                .append(" te.pro_fecha_inicio= to_date('")
                .append(df.format(programacion.getProFechaInicio())).append("','dd/mm/yy') ")
                .append(" and te.pro_dependencia = ")
                .append(programacion.getDependencia().getDepId());;
    
        q = em.createNativeQuery(strQry.toString());
        q.executeUpdate();
        
        strQry = new StringBuilder();
        strQry.append("delete from pos_no_asig where (fecha between to_date('")
                    .append(df.format(programacion.getProFechaInicio()))
                    .append("','dd/mm/yy') and  to_date('")
                    .append(df.format(programacion.getProFechaFin()))
                    .append("','dd/mm/yy')) and dependencia=").append(programacion.getDependencia().getDepId());
               
    
        q = em.createNativeQuery(strQry.toString());
        q.executeUpdate();
    }
   
    @Override
    public Programacion guardar(Programacion programacion, Funcionario f) {  
        em.createNamedQuery("PosNoAsig.delete").setParameter("dep", f.getDependencia().getDepId()).executeUpdate();
        return (Programacion) auditoria.auditar(programacion,f);

    }
    /**
     *
     * @param d
     * @param depId
     * @param estado
     * @return
     */
    @Override
    public boolean isFechaProgramada(Date d, Long depId, EstadoProgramacion estado){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder strQry = new StringBuilder();
        strQry.append("Select count(*) from programacion te where ")
                .append("     to_date('").append(df.format(d)).append("','dd/mm/yy') ")
                .append("       between te.pro_fecha_inicio and te.pro_fecha_fin ")
                .append(" and te.pro_dependencia = ").append(depId)
                .append(" and te.pro_estado='").append(estado.getVal()).append("' ");
        Query q = em.createNativeQuery(strQry.toString());
        BigDecimal count = (BigDecimal) q.getSingleResult();
        //System.out.println(strQry.toString() + " -cnt: " + count);
        if (count != null && count.longValue() > 0) {
            return true;
        }
        return false;
    } 
   
    @Override
    public void generarProgramacion(Programacion programacion) throws SQLException{
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder strQry = new StringBuilder();
        Query q = em.createNativeQuery(strQry.toString());
       
        strQry.append("begin fillProgramacion(").append("dependencia => ")
            
            .append( programacion.getDependencia().getDepId())
            .append(", programacionId => ")
            .append( programacion.getProId())
            .append(", fIni => to_date('")
            .append( df.format(programacion.getProFechaInicio()))
            .append("','dd/mm/yy'),").append( "fFin => to_date('")
            .append(df.format(programacion.getProFechaFin())).append( "','dd/mm/yy'));").append("end;");
    
        q = em.createNativeQuery(strQry.toString());
        q.executeUpdate();
    }
    
    
     @Override
    public List<Programacion> getListaPag(Programacion programacion, int first, int pageSize,
            String sortField, String sortOrder) {

        Query query = createQueryFilter(programacion, sortField, sortOrder);
         //System.out.println("queryProg: "+query.toString());
        query.setFirstResult(first).setMaxResults(pageSize);
        try{
            return (List<Programacion>) query.getResultList();    
        }
        catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }        
    }
    
    
    private Query createQueryFilter(Programacion programacion, String sortField, String sortOrder){
        
        List<String> condiciones = new ArrayList<String>();
            StringBuilder strQry = new StringBuilder();
         Map<String,Object> params = new java.util.HashMap<String, Object>();
        
        if (programacion.getProFechaInicio() != null  && programacion.getProFechaFin()!= null) {
            condiciones.add("((t.proFechaInicio between :fini and :ffin) or (t.proFechaFin between :fini and :ffin)) ");
            params.put("fini",  programacion.getProFechaInicio());
            params.put("ffin",  programacion.getProFechaFin());
        }
        if (programacion.getProFechaInicio() != null  && programacion.getProFechaFin()== null) {
            condiciones.add(" :fini between t.proFechaInicio and t.proFechaFin ");
            params.put("fini",  programacion.getProFechaInicio());
        }
        if (programacion.getProFechaInicio() == null  && programacion.getProFechaFin()!= null) {
            condiciones.add(" :ffin between t.proFechaInicio and t.proFechaFin ");
            params.put("ffin",  programacion.getProFechaFin());
        }
        if (programacion.getProEstado() != null) {
            condiciones.add("t.proEstado = :estado ");
            params.put("estado",  programacion.getProEstado() );
        }
        if (programacion.getDependencia().getDepId() != null) {
            condiciones.add("t.dependencia.depId = :dep ");
            params.put("dep",  programacion.getDependencia().getDepId() );
        }
        if (programacion.getDependencia().getDepcategoria() != null && programacion.getDependencia().getDepcategoria().getDcId() != null) {
            condiciones.add("t.dependencia.depcategoria.dcId = :dcId ");
            params.put("dcId",  programacion.getDependencia().getDepcategoria().getDcId() );
            //System.out.println("Filtro por\t"+programacion.getDependencia().getDepcategoria().getDcId());
        }
//        else{
            if (programacion.getDependencia().getAeropuerto().getAeId() != null ) {
                condiciones.add("t.dependencia.aeropuerto.aeId = :aero ");
                params.put("aero",  programacion.getDependencia().getAeropuerto().getAeId() );
            }
            if (programacion.getDependencia().getAeropuerto().getRegional().getRegId() != null ) {
                condiciones.add("t.dependencia.aeropuerto.regional.regId = :reg ");
                params.put("reg",  programacion.getDependencia().getAeropuerto().getRegional().getRegId() );
            }
//        }

            if(programacion.isReporte()){
                condiciones.add("(t.proId IN :ids) ");
                params.put("ids",  obtenerMaximos() );
            }
        if(condiciones.size()>0){
            strQry.append("where ");
        }
        int limit = 1;
        for (String it : condiciones) {
                strQry.append(it);
                if(limit<condiciones.size()){
                    strQry.append(" and ");
                }
                limit++;
        }
        
       
        
        
        
        
        Query query = em.createQuery("Select count(t) from Programacion t "+strQry.toString());
        //System.out.println("Countquery   = " + query.toString());
        QueryUtil.setParameters(query, params);
        count = (Long)query.getSingleResult();
        
        StringBuilder strQryFinal = new StringBuilder("Select t from Programacion t  ").
                append(strQry.toString());
        if(sortField!=null && sortOrder!=null){
            strQryFinal.append("order by t.").append(sortField).append(" ").append(sortOrder);
        }else{
            strQryFinal.append("order by t.proFechaInicio desc");
        }
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);
        return query;
    }
    
    
    public List<Long> obtenerMaximos(){
        Query q = em.createNamedQuery("Programacion.findMaximos");
        List<Object[]> resultList = q.getResultList();
        List<Long> ids = new ArrayList<Long>();
        for(Object[] pro : resultList){
            ids.add((Long)pro[0]);
        }
        return ids;
    }
    
      @Override
    public Long getCount() {
        return count;
    }

    @Override
    public boolean existeProgramacion(Dependencia dependencia, Date fecha) {
        try
        {
    
                 
            Query query= em.createQuery("Select p From Programacion p Where p.dependencia.depId = :depId and :fecha between p.proFechaInicio and p.proFechaFin ");   
            query.setParameter("depId", dependencia.getDepId());        
            query.setParameter("fecha", fecha, TemporalType.DATE);
            query.setMaxResults(1);
            Programacion programacion = (Programacion) query.getSingleResult();
            if(programacion.getProId()!=null)
            {
                return true;
            }
            else
            {
                return false;
            }                  
            
           
        }catch (NoResultException nre) {
            return false;
        }    
       
    }

}
