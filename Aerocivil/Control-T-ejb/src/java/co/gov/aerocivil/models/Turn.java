/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.models;

/**
 *
 * @author Administrador
 */
public class Turn {

    private Period period;
    private Position position;
    private Functionary functionary;
    private long positionPeriod;
    private int type;
    private Boolean haveExtra;
    private Boolean permiteHorasExtra;
    private Boolean permiteDescanso;
    private int numEnables;
    private String[] progs;

    public Turn(Period period, Position position, Functionary functionary, long positionPeriod) {
        this.period = period;
        this.position = position;
        this.functionary = functionary;
        this.positionPeriod = positionPeriod;
        this.type = 1;
        this.haveExtra = false;
        this.numEnables = 1;
    }

    /**
     * @return the period
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * @param period the period to set
     */
    public void setPeriod(Period period) {
        this.period = period;
    }

    /**
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * @return the functionary
     */
    public Functionary getFunctionary() {
        return functionary;
    }

    /**
     * @param functionary the functionary to set
     */
    public void setFunctionary(Functionary functionary) {
        this.functionary = functionary;
    }

    /**
     * @return the positionPeriod
     */
    public long getPositionPeriod() {
        return positionPeriod;
    }

    /**
     * @param positionPeriod the positionPeriod to set
     */
    public void setPositionPeriod(long positionPeriod) {
        this.positionPeriod = positionPeriod;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the haveExtra
     */
    public Boolean getHaveExtra() {
        return haveExtra;
    }

    /**
     * @param haveExtra the haveExtra to set
     */
    public void setHaveExtra(Boolean haveExtra) {
        this.haveExtra = haveExtra;
    }

    /**
     * @return the numEnables
     */
    public int getNumEnables() {
        return numEnables;
    }

    /**
     * @param numEnables the numEnables to set
     */
    public void setNumEnables(int numEnables) {
        this.numEnables = numEnables;
    }

    public Boolean getPermiteHorasExtra() {
        if (permiteHorasExtra == null) {
            permiteHorasExtra = false;
        }
        return permiteHorasExtra;
    }

    public void setPermiteHorasExtra(Boolean permiteHorasExtra) {
        this.permiteHorasExtra = permiteHorasExtra;
    }

    public Boolean getPermiteDescanso() {
        if (permiteDescanso != null) {
            return permiteDescanso;
        } else {
            return true;
        }
    }

    public void setPermiteDescanso(Boolean permiteDescanso) {
        this.permiteDescanso = permiteDescanso;
    }

    public String[] getProgs() {
        return progs;
    }

    public void setProgs(String[] progs) {
        this.progs = progs;
    }
    
        
}
