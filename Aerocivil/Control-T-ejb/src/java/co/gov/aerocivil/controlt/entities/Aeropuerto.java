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
@Table(name = "AEROPUERTO")
@NamedQueries({
    @NamedQuery(name = "Aeropuerto.findAll", query = "SELECT a FROM Aeropuerto a"),
    @NamedQuery(name = "Aeropuerto.findById", query = "SELECT a FROM Aeropuerto a WHERE a.aeId = :aeId"),
    @NamedQuery(name = "Aeropuerto.findByAeNombre", query = "SELECT a FROM Aeropuerto a WHERE a.aeNombre = :aeNombre"),
    @NamedQuery(name = "Aeropuerto.findByAeRegional", query = "SELECT a FROM Aeropuerto a WHERE a.regional.regId = :regId order by a.aeNombre")})
public class Aeropuerto implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "AE_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AEROPUERTO")
    @SequenceGenerator(name = "SEQ_AEROPUERTO", sequenceName = "SEQ_AEROPUERTO", allocationSize = 1)
    private Long aeId;
    @Basic(optional = false)
    @Column(name = "AE_NOMBRE")
    private String aeNombre;
    @JoinColumn(name = "AE_REGIONAL", referencedColumnName = "REG_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Regional regional;
    @JoinColumn(name = "AE_CIUDAD", referencedColumnName = "CIU_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Ciudad ciudad;

    public Aeropuerto() {
    }

    public String getAeNombre() {
        return aeNombre;
    }

    public void setAeNombre(String aeNombre) {
        this.aeNombre = aeNombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aeId != null ? aeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aeropuerto)) {
            return false;
        }
        Aeropuerto other = (Aeropuerto) object;
        if ((this.aeId == null && other.aeId != null) || (this.aeId != null && !this.aeId.equals(other.aeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.Aeropuerto[ aeId=" + aeId + " ]";
    }

    public Regional getRegional() {
        return regional;
    }

    public void setRegional(Regional regional) {
        this.regional = regional;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public Long getAeId() {
        return aeId;
    }

    public void setAeId(Long aeId) {
        this.aeId = aeId;
    }
}
