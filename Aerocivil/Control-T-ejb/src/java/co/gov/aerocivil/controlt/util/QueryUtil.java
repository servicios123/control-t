/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
public class QueryUtil {

    public static void setParameters(Query query, Map<String, Object> params) {
        for (Map.Entry e: params.entrySet()) {
            query.setParameter(String.valueOf(e.getKey()), e.getValue());
        }
    }
    
    /**
     * Concatena una lista de condiciones por medio de la conjuncion AND
     * @param conditions lista de condiciones
     * @return String builder con la lista
     */
    public static StringBuilder joinWithAnd(List<String> conditions){
        return join(conditions, "and");
    }
    
    /**
     * Concatena una lista de Strings @param list con un @param separator
     * @param list
     * @param separator
     * @return 
     */
    public static StringBuilder join(List<String> list, String separator){
        StringBuilder string = new StringBuilder();
        if (list!=null){
            for (Iterator<String> it = list.iterator(); it.hasNext();) {
                string.append(it.next());
                if(it.hasNext()){
                    string.append(" ").append(separator).append(" ");
                }
            }
        }
        return string;
    }
    
    public static Long getLongNativeQuery(Query q){        
        Long vLong=null;
        try{
            q.setMaxResults(1);
            BigDecimal count = (BigDecimal)q.getSingleResult();        
            if(count!=null){
                vLong = count.longValue();
            }   
        }
        catch(NoResultException nre){
            return null;
        }
        return vLong;
    }
    
    public static List<BigDecimal> getLongListNativeQuery(Query q){        
        List<BigDecimal> ids=new ArrayList<BigDecimal>();
        try{
            ids = q.getResultList();
        }
        catch(NoResultException nre){
            return null;
        }
        return ids;
    }
    
}
