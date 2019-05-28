/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "POSICION_INACTIVA")
@NamedQueries({
    @NamedQuery(name = "PosicionInactiva.findAll", query = "SELECT p FROM PosicionInactiva p "),
    @NamedQuery(name = "PosicionInactiva.findByPiId", query = "SELECT p FROM PosicionInactiva p WHERE p.piId = :piId"),
    @NamedQuery(name = "PosicionInactiva.findByPiFecha", query = "SELECT p FROM PosicionInactiva p WHERE p.fecha = :fecha"),
    @NamedQuery(name = "PosicionInactiva.findInactivea", query = "SELECT p FROM PosicionInactiva p WHERE p.fecha = :fecha and p.posicionJornada.pjId = :pjId")})
public class PosicionInactiva implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PI_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_POSINACTIVA")
    @SequenceGenerator(name = "SEQ_POSINACTIVA", sequenceName = "SEQ_POSINACTIVA", allocationSize = 1)
    private Long piId;
    
    @JoinColumn(name = "PI_POSICION_JORNADA", referencedColumnName = "PJ_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PosicionJornada posicionJornada;

    @Column(name = "PI_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    public Long getPiId() {
        return piId;
    }

    public void setPiId(Long piId) {
        this.piId = piId;
    }

    public PosicionJornada getPosicionJornada() {
        return posicionJornada;
    }

    public void setPosicionJornada(PosicionJornada posicionJornada) {
        this.posicionJornada = posicionJornada;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (piId != null ? piId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PosicionInactiva)) {
            return false;
        }
        PosicionInactiva other = (PosicionInactiva) object;
        if ((this.piId == null && other.piId != null) || (this.piId != null && !this.piId.equals(other.piId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.PosicionInactiva[ piId=" + piId + " ]";
    }
    
}
