/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.util;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Administrador
 */
public class StringUtil {
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
}
