/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.models;

/**
 *
 * @author Administrador
 */
public class Period {
    private long id;
    private String alias;
    private int start;
    private int end;
    private long requiredPeriod;
    private int total;

    public Period(long id, String alias, int start, int end) {
        this.id = id;
        this.alias = alias;
        this.start = start;
        this.end = end;
        this.requiredPeriod=-1;
        this.total = end-start;
    }  

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public int getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * @return the requiredPeriod
     */
    public long getRequiredPeriod() {
        return requiredPeriod;
    }

    /**
     * @param requiredPeriod the requiredPeriod to set
     */
    public void setRequiredPeriod(long requiredPeriod) {
        this.requiredPeriod = requiredPeriod;
    }

    /**
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(int total) {
        this.total = total;
    }
}
