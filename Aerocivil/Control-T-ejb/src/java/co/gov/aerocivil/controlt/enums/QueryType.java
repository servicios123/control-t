/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.enums;

/**
 *
 * @author Administrador
 */
public enum QueryType {
    
    QUERY_COUNT(1),
    QUERY_SELECT(2);
    
    private int queryType;

    QueryType(int queryType){
        this.queryType = queryType;
    }
    

    public int getQueryType() {
        return queryType;
    }

    public void setQueryType(int queryType) {
        this.queryType = queryType;
    }
    
}
