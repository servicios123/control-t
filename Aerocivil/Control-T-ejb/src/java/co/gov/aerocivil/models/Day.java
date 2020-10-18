/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrador
 */
public class Day {
    private Date date;
    private int totalTrop;
    private int TotalDesc;
    private ArrayList<Turn> turns;

    public Day(Date date) 
    {
        this.date = date;  
        
    }
    public Day(Date date, ArrayList<Turn> turns) {
        this.date = date;
        this.turns = turns;
        
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the turns
     */
    public ArrayList<Turn> getTurns() {
        return turns;
    }

    /**
     * @param turns the turns to set
     */
    public void setTurns(ArrayList<Turn> turns) {
        this.turns = turns;
    }
    
    public void addTurn(Turn turn)
    {
        this.turns.add(turn);
    }

    public int getTotalTrop() {
        return totalTrop;
    }

    public void setTotalTrop(int totalTrop) {
        this.totalTrop = totalTrop;
    }

    public int getTotalDesc() {
        return TotalDesc;
    }

    public void setTotalDesc(int TotalDesc) {
        this.TotalDesc = TotalDesc;
    }
    
    

  
}
