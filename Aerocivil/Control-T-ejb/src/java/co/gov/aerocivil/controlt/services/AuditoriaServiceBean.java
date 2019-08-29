/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Auditoria;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.util.QueryUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;

/**
 *
 * @author Administrador
 */
@Stateless
public class AuditoriaServiceBean implements AuditoriaService{
    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;   
    private Long count;
    
    @Override
    public Object auditar(Object entity, Funcionario func){
        String accion = "update";
        Object objId=getObjId(entity);
        if(entity instanceof Funcionario ){
            Funcionario f = em.find(Funcionario.class, Long.valueOf(objId.toString()));
            if(f==null){
                accion = "insert";
            }
        }
        else if(objId == null){
            accion = "insert";
        }
        String data = "";
        if(accion.equalsIgnoreCase("update")){
            data = diferencias(entity);
        }
        entity = em.merge(entity);
        objId=getObjId(entity);
        if(accion.equalsIgnoreCase("insert")){
            data = "Se inserta registro en "+entity.getClass().getAnnotation(Table.class).name()+" con Id: "+String.valueOf(objId);
        }
        if(objId!=null){
            //System.out.println(entity.getClass().getAnnotation(Table.class).name() + " f:" + (func!=null?func.getFunId():"") + "-" + accion+" -id:"+ objId);
            Auditoria aud = new Auditoria();
            if(entity instanceof Funcionario ){
                aud.setAudDatos(((Funcionario)entity).toString());       
            }else{
                aud.setAudDatos(data);
            }            
            aud.setAudTabla(entity.getClass().getAnnotation(Table.class).name());
            aud.setAudFecha(new Date());
            aud.setAudPk(Long.valueOf(objId.toString()));
            aud.setFuncionario(func); 
            aud.setAudAccion(accion);
            em.merge(aud);
        }
        return entity;    
    }
    
    public String diferencias(Object entity) {
        Object objId=getObjId(entity);
        Object current = em.find(entity.getClass(), Long.valueOf(objId.toString()));
        Field[] fields = entity.getClass().getDeclaredFields();
        String msj = "Entidad: " + entity.getClass().getSimpleName()+" Id: "+String.valueOf(objId)+", Campos: [";
        for (Field f : fields) {
            try {
                Method getterMethod = getterMethod(entity.getClass(), f.getName(), false);
                Object fieldValue = (getterMethod==null)?null:getterMethod.invoke(entity);
                Object fieldValueCurrent = (getterMethod==null)?null:getterMethod.invoke(current);
                if(fieldValue!=null && fieldValueCurrent!=null){
                    if(!fieldValueCurrent.equals(fieldValue)){
                        if(fieldValueCurrent.getClass().getAnnotation(Entity.class)!=null){
                            msj += " Campo: " + f.getName() + " {Valor Antiguo: " + getObjId(fieldValueCurrent) + ", Valor actual: " + getObjId(fieldValue)+"}; ";
                        }else{
                            msj += " Campo: " + f.getName() + " {Valor Antiguo: " + fieldValueCurrent + ", Valor actual: " + fieldValue+"}; ";
                        }
                        
                    }
                }
            } catch (IllegalAccessException ex) {
                Logger.getLogger(AuditoriaServiceBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(AuditoriaServiceBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(AuditoriaServiceBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        msj+="]";
        return msj;
    }

    public Method getterMethod(Class clazz, String fieldName, boolean recursive) {
        fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method methodGet1 = null;
        Method methodGet2 = null;
        try {
            String methodName = "get" + fieldName;
            methodGet1 = recursive ? clazz.getMethod(methodName) : clazz.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
        }
        if (methodGet1 != null && Modifier.isPublic(methodGet1.getModifiers())) {
            return methodGet1;
        }
        try {
            String methodName = "is" + fieldName;
            methodGet2 = recursive ? clazz.getMethod(methodName) : clazz.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
        }
        if (methodGet2 != null && Modifier.isPublic(methodGet2.getModifiers())) {
            return methodGet2;
        }
        return null;
    }
    
    @Override
    public void auditarDelete(String tabla, Funcionario func, Long posId){
        //System.out.println(tabla + " f:" + func.getFunId() + "- delete -id:"+ posId);
    }
    
    
    
    @Override
    public void auditarDelete(String tabla, Funcionario func, Long posId, Long id){
        Auditoria aud = new Auditoria();
            aud.setAudTabla(tabla);
            aud.setAudFecha(new Date());
            aud.setAudPk(id);
            aud.setFuncionario(func); 
            aud.setAudAccion("Borrado");
            aud.setAudDatos(tabla + " f:" + func.getFunId() + "- delete -id:"+ posId);
            em.merge(aud);
        //System.out.println(tabla + " f:" + func.getFunId() + "- delete -id:"+ posId);
    }

    @Override
    public List<Auditoria> getLista(Auditoria auditoriaFiltro, Integer first, Integer pageSize, String sortField, String sortOrder) {
        Query query = createQueryFilter(auditoriaFiltro, sortField, sortOrder);
        query.setFirstResult(first).setMaxResults(pageSize);
        try{
            return (List<Auditoria>) query.getResultList();    
        }
        catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }        
    }
    
    private Query createQueryFilter(Auditoria auditoria, String sortField, String sortOrder){
        List<String> condiciones = new ArrayList<String>();
        //verificar los campos de 
                
        Map<String,Object> params = new HashMap<String, Object>();
        if(auditoria.getFuncionario()!=null ){
            if(auditoria.getFuncionario().getFunNombre()!=null && 
                    !"".equals(auditoria.getFuncionario().getFunNombre())){
                condiciones.add(" (p.funcionario.funNombre) like :nombre ");
                params.put("nombre", "%" +  auditoria.getFuncionario().getFunNombre().toUpperCase() + "%"); 
            }
            if(auditoria.getFuncionario().getFunAlias()!=null &&
                    !"".equals(auditoria.getFuncionario().getFunAlias())){
                condiciones.add(" (p.funcionario.funAlias) like :alias ");
                params.put("alias", "%" +  auditoria.getFuncionario().getFunAlias().toUpperCase() + "%"); 
            }
            if(auditoria.getFuncionario().getFunId()!=null ){
                condiciones.add(" (p.funcionario.funId) = :funId ");
                params.put("funId",  auditoria.getFuncionario().getFunId()); 
            }
        }
        if(auditoria.getAudFecha()!=null ){
            condiciones.add(" (p.audFecha) between :fechaIni and :fechaFin ");
            params.put("fechaIni", auditoria.getAudFecha()); 
            params.put("fechaFin", auditoria.getAudFechaFin()); 
        }
        
        if(auditoria.getAudPk()!=null ){
            condiciones.add(" p.audPk = :apk");
            params.put("apk", auditoria.getAudPk()); 
           
        }
        if(auditoria.getAudAccion()!=null ){
            condiciones.add(" p.audAccion = :accion");
            params.put("accion", auditoria.getAudAccion()); 
           
        }
        
        if (auditoria.getFuncionario().getDependencia() != null
                && auditoria.getFuncionario().getDependencia().getDepcategoria() != null
                && auditoria.getFuncionario().getDependencia().getDepcategoria().getDcId() != null) {
            condiciones.add("p.funcionario.dependencia.depcategoria.dcId = :depCat ");
            params.put("depCat", auditoria.getFuncionario().getDependencia().getDepcategoria().getDcId());
        }
        if (auditoria.getFuncionario().getDependencia() != null && auditoria.getFuncionario().getDependencia().getDepId() != null) {
            condiciones.add("p.funcionario.dependencia.depId = :dep ");
            params.put("dep", auditoria.getFuncionario().getDependencia().getDepId());
        } 
        
        if (auditoria.getFuncionario().getDependencia() != null && 
                auditoria.getFuncionario().getDependencia().getAeropuerto() != null && 
                auditoria.getFuncionario().getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("p.funcionario.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero", auditoria.getFuncionario().getDependencia().getAeropuerto().getAeId());
        } 
        else if (auditoria.getFuncionario().getDependencia() != null && 
                auditoria.getFuncionario().getDependencia().getAeropuerto().getRegional() != null && 
                auditoria.getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("p.funcionario.dependencia.aeropuerto.regional.regId = :reg ");
            params.put("reg", auditoria.getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
        }

        StringBuilder strQry = new StringBuilder();
        
        if(condiciones.size()>0){
            strQry.append(" where ");
            strQry.append(QueryUtil.joinWithAnd(condiciones));
        }        
        
        Query query = em.createQuery("Select count(p) from Auditoria p ".concat(strQry.toString()));
        QueryUtil.setParameters(query, params);
        count = (Long)query.getSingleResult();
        
        StringBuilder strQryFinal = new StringBuilder("Select p from Auditoria p ").
                append(strQry.toString());
        if(sortField!=null && sortOrder!=null){
            strQryFinal.append(" order by p.").append(sortField).append(" ").append(sortOrder);
        }
        //System.out.println("strQryFinal::: "+strQryFinal);
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;
        
    }
    
    @Override
    public List<Auditoria> getLista(Auditoria auditoriaFiltro) {
        Query query = createQueryFilter(auditoriaFiltro);
        try{
            return (List<Auditoria>) query.getResultList();    
        }
        catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }        
    }
    
    private Query createQueryFilter(Auditoria auditoria){
        List<String> condiciones = new ArrayList<String>();
        //verificar los campos de 
                
        Map<String,Object> params = new HashMap<String, Object>();
        if(auditoria.getFuncionario()!=null ){
            if(auditoria.getFuncionario().getFunNombre()!=null && 
                    !"".equals(auditoria.getFuncionario().getFunNombre())){
                condiciones.add(" (p.funcionario.funNombre) like :nombre ");
                params.put("nombre", "%" +  auditoria.getFuncionario().getFunNombre().toUpperCase() + "%"); 
            }
            if(auditoria.getFuncionario().getFunAlias()!=null &&
                    !"".equals(auditoria.getFuncionario().getFunAlias())){
                condiciones.add(" (p.funcionario.funAlias) like :alias ");
                params.put("alias", "%" +  auditoria.getFuncionario().getFunAlias().toUpperCase() + "%"); 
            }
            if(auditoria.getFuncionario().getFunId()!=null ){
                condiciones.add(" (p.funcionario.funId) = :funId ");
                params.put("funId",  auditoria.getFuncionario().getFunId()); 
            }
        }
        if(auditoria.getAudFecha()!=null ){
            condiciones.add(" (p.audFecha) between :fechaIni and :fechaFin ");
            params.put("fechaIni", auditoria.getAudFecha()); 
            params.put("fechaFin", auditoria.getAudFechaFin()); 
        }
        
        if(auditoria.getAudPk()!=null ){
            condiciones.add(" p.audPk = :apk");
            params.put("apk", auditoria.getAudPk()); 
           
        }
        if(auditoria.getAudAccion()!=null ){
            condiciones.add(" p.audAccion = :accion");
            params.put("accion", auditoria.getAudAccion()); 
           
        }
        
        if (auditoria.getFuncionario().getDependencia() != null
                && auditoria.getFuncionario().getDependencia().getDepcategoria() != null
                && auditoria.getFuncionario().getDependencia().getDepcategoria().getDcId() != null) {
            condiciones.add("p.funcionario.dependencia.depcategoria.dcId = :depCat ");
            params.put("depCat", auditoria.getFuncionario().getDependencia().getDepcategoria().getDcId());
        }
        if (auditoria.getFuncionario().getDependencia() != null && auditoria.getFuncionario().getDependencia().getDepId() != null) {
            condiciones.add("p.funcionario.dependencia.depId = :dep ");
            params.put("dep", auditoria.getFuncionario().getDependencia().getDepId());
        } 
        
        if (auditoria.getFuncionario().getDependencia() != null && 
                auditoria.getFuncionario().getDependencia().getAeropuerto() != null && 
                auditoria.getFuncionario().getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("p.funcionario.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero", auditoria.getFuncionario().getDependencia().getAeropuerto().getAeId());
        } 
        else if (auditoria.getFuncionario().getDependencia() != null && 
                auditoria.getFuncionario().getDependencia().getAeropuerto().getRegional() != null && 
                auditoria.getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("p.funcionario.dependencia.aeropuerto.regional.regId = :reg ");
            params.put("reg", auditoria.getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
        }

        StringBuilder strQry = new StringBuilder();
        
        if(condiciones.size()>0){
            strQry.append(" where ");
            strQry.append(QueryUtil.joinWithAnd(condiciones));
        }        
        
        Query query = em.createQuery("Select count(p) from Auditoria p ".concat(strQry.toString()));
        QueryUtil.setParameters(query, params);
        count = (Long)query.getSingleResult();
        
        StringBuilder strQryFinal = new StringBuilder("Select p from Auditoria p ").
                append(strQry.toString());
        //System.out.println("strQryFinal::: "+strQryFinal);
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;
        
    }

    /**
     *
     * @return
     */
    @Override
    public Long getCount() {
        return count;
    }

    public Object getObjId(Object entity){
        try {
            //for(Method metodo: entity.getClass().getMethods()){
            for (Field f : entity.getClass().getDeclaredFields()){
                if (f.isAnnotationPresent(Id.class)){
                    String methodNameId = "get"+capitalize(f.getName());
                    Method metodo = entity.getClass().getMethod(methodNameId);
                    Object ret = metodo.invoke(entity);
                    if (ret != null && ret instanceof Number){
                        return ret;
                    }
                }
            }
            //return em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(AuditoriaServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(AuditoriaServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }  catch (IllegalAccessException ex) {  
            Logger.getLogger(AuditoriaServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(AuditoriaServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalArgumentException ex) {
            Logger.getLogger(AuditoriaServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String capitalize(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
    }

}
