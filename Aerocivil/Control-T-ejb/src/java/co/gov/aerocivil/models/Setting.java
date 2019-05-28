/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.models;

/**
 *
 * @author Administrador
 */
public class Setting {

    private long periodId;
    private int timeRecess;
    private boolean maxHoursExtra;
    private int maxWorkedHoursByDay;
    private int maxContinuosDaysExtra;
    private int maxContinuosDaysExtraModification;
    private int maxContinuosPeriod;

    public Setting() {
        this.periodId = -1;
        this.timeRecess = -1;
        this.maxHoursExtra = false;
        this.maxWorkedHoursByDay = -1;
        this.maxContinuosDaysExtra = -1;
        this.maxContinuosPeriod = -1;
    }

    /**
     * @return the periodId
     */
    public long getPeriodId() {
        return periodId;
    }

    /**
     * @param periodId the periodId to set
     */
    public void setPeriodId(long periodId) {
        this.periodId = periodId;
    }

    /**
     * @return the timeRecess
     */
    public int getTimeRecess() {
        return timeRecess;
    }

    /**
     * @param timeRecess the timeRecess to set
     */
    public void setTimeRecess(int timeRecess) {
        this.timeRecess = timeRecess;
    }

    /**
     * @return the maxHoursExtra
     */
    public boolean isMaxHoursExtra() {
        return maxHoursExtra;
    }

    /**
     * @param maxHoursExtra the maxHoursExtra to set
     */
    public void setMaxHoursExtra(boolean maxHoursExtra) {
        this.maxHoursExtra = maxHoursExtra;
    }

    /**
     * @return the maxWorkedHoursByDay
     */
    public int getMaxWorkedHoursByDay() {
        return maxWorkedHoursByDay;
    }

    /**
     * @param maxWorkedHoursByDay the maxWorkedHoursByDay to set
     */
    public void setMaxWorkedHoursByDay(int maxWorkedHoursByDay) {
        this.maxWorkedHoursByDay = maxWorkedHoursByDay;
    }

    /**
     * @return the maxContinuosDaysExtra
     */
    public int getMaxContinuosDaysExtra() {
        return maxContinuosDaysExtra;
    }

    /**
     * @param maxContinuosDaysExtra the maxContinuosDaysExtra to set
     */
    public void setMaxContinuosDaysExtra(int maxContinuosDaysExtra) {
        this.maxContinuosDaysExtra = maxContinuosDaysExtra;
    }

    /**
     * @return the maxContinuosPeriod
     */
    public int getMaxContinuosPeriod() {
        return maxContinuosPeriod;
    }

    /**
     * @param maxContinuosPeriod the maxContinuosPeriod to set
     */
    public void setMaxContinuosPeriod(int maxContinuosPeriod) {
        this.maxContinuosPeriod = maxContinuosPeriod;
    }

    public int getMaxContinuosDaysExtraModification() {
        return maxContinuosDaysExtraModification;
    }

    public void setMaxContinuosDaysExtraModification(int maxContinuosDaysExtraModification) {
        this.maxContinuosDaysExtraModification = maxContinuosDaysExtraModification;
    }
}
