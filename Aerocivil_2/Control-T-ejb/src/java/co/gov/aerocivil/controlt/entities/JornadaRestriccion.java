/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Viviana
 */
@Entity
@Table(name = "JORNADA_RESTRICCION")
@NamedQueries({
    @NamedQuery(name = "JornadaRestriccion.findAll", query = "SELECT j FROM JornadaRestriccion j")   , 
    @NamedQuery(name = "JornadaRestriccion.getRestrictions", query = "SELECT j FROM JornadaRestriccion j WHERE j.jornada.joId = :prev AND j.jornadaEv.joId = :current ")    
    })
public class JornadaRestriccion implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    
    //SEQ_JORNADA_RESTRICCION
    
       
  
    @Id
    @JoinColumn(name = "JR_JORNADA", referencedColumnName = "JO_ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Jornada jornada;
    
    @Id
    @JoinColumn(name = "JR_JORNADA_EV", referencedColumnName = "JO_ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Jornada jornadaEv;

   


  
  
 public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public Jornada getJornadaEv() {
        return jornadaEv;
    }

    public void setJornadaEv(Jornada jornadaEv) {
        this.jornadaEv = jornadaEv;
    }

    public JornadaRestriccion() {
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jornada.getJoId() != null ? jornada.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JornadaRestriccion)) {
            return false;
        }
        JornadaRestriccion other = (JornadaRestriccion) object;
        if ((this.jornada.getJoId() == null && other.jornada.getJoId() != null) || (this.jornada.getJoId() != null && !this.jornada.getJoId().equals(other.jornada.getJoId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.JornadaRestriccion[ jrId=" + jornada.getJoId() + " ]";
    }
    
}
