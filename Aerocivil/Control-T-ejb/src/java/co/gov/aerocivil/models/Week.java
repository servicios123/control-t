/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.models;

import java.util.ArrayList;

/**
 *
 * @author Administrador
 */
public class Week {

    private ArrayList<Day> days;
    private int totalDays;

    public Week() {
        this.days = new ArrayList<Day>();
    }

    public Week(ArrayList<Day> days) {
        this.days = days;
    }

    public void addDay(Day day) {
        this.days.add(day);
    }

    public void setRandomDays() {
        ArrayList<Day> aux = new ArrayList<Day>();
        if (days.size() > 0) {
            for (int i = 1; i < days.size(); i++) {
                aux.add(days.get(i));
            }
            aux.add(days.get(0));
        }
        this.setDays(aux);
    }

    /**
     * @return the days
     */
    public ArrayList<Day> getDays() {
        return this.days;
    }

    /**
     * @param days the days to set
     */
    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }
}
