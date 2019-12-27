/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "REGIONAL")
@NamedQueries({
    @NamedQuery(name = "Regional.findAll", query = "SELECT r FROM Regional r"),
    @NamedQuery(name = "Regional.findById", query = "SELECT r FROM Regional r WHERE r.regId = :regId"),
    @NamedQuery(name = "Regional.findByRegNombre", query = "SELECT r FROM Regional r WHERE r.regNombre = :regNombre")})
public class Regional implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "REG_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_REGIONAL")
    @SequenceGenerator(name = "SEQ_REGIONAL", sequenceName = "SEQ_REGIONAL", allocationSize = 1)
    private Long regId;
    @Basic(optional = false)
    @Column(name = "REG_NOMBRE")
    private String regNombre;

    public Regional() {
    }

    public Regional(Long regId) {
        this.regId = regId;
    }

    public Regional(Long regId, String regNombre) {
        this.regId = regId;
        this.regNombre = regNombre;
    }

    public Long getRegId() {
        return regId;
    }

    public void setRegId(Long regId) {
        this.regId = regId;
    }

    public String getRegNombre() {
        return regNombre;
    }

    public void setRegNombre(String regNombre) {
        this.regNombre = regNombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (regId != null ? regId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Regional)) {
            return false;
        }
        Regional other = (Regional) object;
        if ((this.regId == null && other.regId != null) || (this.regId != null && !this.regId.equals(other.regId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.Regional[ regId=" + regId + " ]";
    }
    
}
