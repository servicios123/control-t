/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.models;

import co.gov.aerocivil.controlt.entities.Funcionario;

/**
 *
 * @author Administrador
 */
public class Functionary {

    private long id;
    private String alias;
    private Boolean available;
    private Boolean restWeek;
    private Boolean canExtra;
    private int maxHoursExtraExceeded;
    private int maxHoursExtra;
    private int initialMaxHoursExtra;
    private int initialMaxHoursExtraExceeded;
    private int numHabilities;
    private int countExtras;
    private Boolean restedLastWeek;
    private int countRestSunday;
    private int countExtrasNocturnas;

    public Functionary(Funcionario funcionario) {
        this.id = funcionario.getFunId();
        this.alias = funcionario.getFunAlias();
        this.available = true;
        this.numHabilities = 0;
        this.countExtras = 0;
        this.countExtrasNocturnas = 0;
        this.restWeek = false;
        if (funcionario.getFunDescansoSemana() != null && funcionario.getFunDescansoSemana()) {
            this.restWeek = true;
        }

        this.canExtra = false;
        if (funcionario.getFunHorasExtras() != null && funcionario.getFunHorasExtras()) {
            this.canExtra = true;
        }

        this.maxHoursExtra = 0;
        if (funcionario.getFunCantMaxHE() != null) {
            this.maxHoursExtra = funcionario.getFunCantMaxHE();
        }

        this.maxHoursExtraExceeded = 0;
        if (funcionario.getFunHEExcedente() != null) {
            this.maxHoursExtraExceeded = funcionario.getFunHEExcedente();
        }

        this.countRestSunday = 0;
        if (funcionario.getFunCatDom() != null) {
            this.countRestSunday = funcionario.getFunCatDom();
        }
        this.initialMaxHoursExtra = this.maxHoursExtra;
        this.initialMaxHoursExtraExceeded = this.maxHoursExtraExceeded;
        this.restedLastWeek = false;
    }

    public Functionary(long id, String alias) {
        this.id = id;
        this.alias = alias;
    }

    public void decreaseMaxHoursExtra(int amount) {
        this.maxHoursExtra -= amount;
    }

    public void decreaseMaxHoursExtraExceeded(int amount) {
        this.maxHoursExtraExceeded -= amount;
    }

//    public void addCoeuntExtrasNocturnas() {
//        this.countExtrasNocturnas++;
//    }

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
     * @return the available
     */
    public Boolean isAvailable() {
        return available;
    }

    /**
     * @param available the available to set
     */
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    /**
     * @return the restWeek
     */
    public Boolean isRestWeek() {
        return restWeek;
    }

    /**
     * @param restWeek the restWeek to set
     */
    public void setRestWeek(Boolean restWeek) {
        this.restWeek = restWeek;
    }

    /**
     * @return the maxHoursExtra
     */
    public int getMaxHoursExtra() {
        return maxHoursExtra;
    }

    /**
     * @param maxHoursExtra the maxHoursExtra to set
     */
    public void setMaxHoursExtra(int maxHoursExtra) {
        this.maxHoursExtra = maxHoursExtra;
    }

    /**
     * @return the canExtra
     */
    public Boolean getCanExtra() {
        return canExtra;
    }

    /**
     * @param canExtra the canExtra to set
     */
    public void setCanExtra(Boolean canExtra) {
        this.canExtra = canExtra;
    }

    /**
     * @return the maxHoursExtraExceeded
     */
    public int getMaxHoursExtraExceeded() {
        return maxHoursExtraExceeded;
    }

    /**
     * @param maxHoursExtraExceeded the maxHoursExtraExceeded to set
     */
    public void setMaxHoursExtraExceeded(int maxHoursExtraExceeded) {
        this.maxHoursExtraExceeded = maxHoursExtraExceeded;
    }

    /**
     * @return the initialMaxHoursExtra
     */
    public int getInitialMaxHoursExtra() {
        return initialMaxHoursExtra;
    }

    /**
     * @param initialMaxHoursExtra the initialMaxHoursExtra to set
     */
    public void setInitialMaxHoursExtra(int initialMaxHoursExtra) {
        this.initialMaxHoursExtra = initialMaxHoursExtra;
    }

    /**
     * @return the initialMaxHoursExtraExceeded
     */
    public int getInitialMaxHoursExtraExceeded() {
        return initialMaxHoursExtraExceeded;
    }

    /**
     * @param initialMaxHoursExtraExceeded the initialMaxHoursExtraExceeded to
     * set
     */
    public void setInitialMaxHoursExtraExceeded(int initialMaxHoursExtraExceeded) {
        this.initialMaxHoursExtraExceeded = initialMaxHoursExtraExceeded;
    }

    /**
     * @return the numHabilities
     */
    public int getNumHabilities() {
        return numHabilities;
    }

    /**
     * @param numHabilities the numHabilities to set
     */
    public void setNumHabilities(int numHabilities) {
        this.numHabilities = numHabilities;
    }

    /**
     * @return the countExtras
     */
    public int getCountExtras() {
        return countExtras;
    }

    /**
     * @param countExtras the countExtras to set
     */
    public void setCountExtras(int countExtras) {
        this.countExtras = countExtras;
    }

    public void addCountExtras() {
        this.countExtras++;
    }

    /**
     * @return the restedLastWeek
     */
    public Boolean getRestedLastWeek() {
        return restedLastWeek;
    }

    /**
     * @param restedLastWeek the restedLastWeek to set
     */
    public void setRestedLastWeek(Boolean restedLastWeek) {
        this.restedLastWeek = restedLastWeek;
    }

    /**
     * @return the countRestSunday
     */
    public int getCountRestSunday() {
        return countRestSunday;
    }

    /**
     * @param countRestSunday the countRestSunday to set
     */
    public void setCountRestSunday(int countRestSunday) {
        this.countRestSunday = countRestSunday;
    }

    public int getCountExtrasNocturnas() {
        return countExtrasNocturnas;
    }

    public void setCountExtrasNocturnas(int countExtrasNocturnas) {
        this.countExtrasNocturnas = countExtrasNocturnas;
    }
}
