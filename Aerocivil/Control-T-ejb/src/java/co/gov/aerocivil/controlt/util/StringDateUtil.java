/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Administrador
 */
public class StringDateUtil {

    private StringDateUtil() {
        // Utility class, hide the constructor.
    }
    
    
    public static String getFormatoHora(Byte hora, Byte minutos){
        if(hora==null || minutos==null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hora);
        c.set(Calendar.MINUTE, minutos);
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(c.getTime());
    }

    public static String formatDate(Date d){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(d);
    }
    
    public static <T>T getValue(Object obj, Class <T> klass){
	if(obj!=null){
            if (obj instanceof String || obj instanceof Character[]){
                    return (T) obj.toString();
            }
            if (obj instanceof Long){
                    return (T) Long.valueOf(obj.toString());
            }
            if (obj instanceof BigDecimal){
                    return (T) Long.valueOf(obj.toString());
            }
            if (obj instanceof Date){
                    return (T)(Date)obj;
            }
            
	}
        return null;
    }
    
    public static Long getLongFromBigDecimal(BigDecimal bd){
        if(bd == null){
            return null;
        }
        return bd.longValue();
    }
    public static Boolean getBoolFromString(String bool){
        if(bool == null){
            return null;
        }
        return Boolean.valueOf(bool);
    }
    
    public static <T>T getFirst(List<T> list, Class T){
        return list.get(0);
    }
    
    public static String join(List list){
        StringBuilder sb = new StringBuilder();
        Iterator it = list.iterator();
        while(it.hasNext()){
            sb.append(it.next().toString());
            if(it.hasNext()){
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
    public static Calendar setCeroHoras(Calendar c){
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;        
    }

    public static Date setCeroHoras(Date d){
        Calendar c = Calendar.getInstance();
        c.setTime(d);        
        c = setCeroHoras(c);
        d = new Date();
        d.setTime(c.getTimeInMillis());
        return d;        
    }


}
