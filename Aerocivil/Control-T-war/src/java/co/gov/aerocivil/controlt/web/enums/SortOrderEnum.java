/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.enums;

import org.primefaces.model.SortOrder;

/**
 *
 * @author Administrador
 */
public enum SortOrderEnum {
    //ASC,DESC;
    ASC("asc"),
    DESC("desc");
    String order;

    SortOrderEnum(String order){
        this.order = order;
    }

    public String getOrder() {
        return order;
    }    
    
    public static String getSortOrder(SortOrder so){
        return so.equals(SortOrder.ASCENDING)?"asc":"desc";
    }
}
