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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Viviana
 */
@Entity
@Table(name = "CIUDAD")
@NamedQueries({
    @NamedQuery(name = "Ciudad.findAll", query = "SELECT c FROM Ciudad c order by c.ciuNombre"),
    @NamedQuery(name = "Ciudad.findById", query = "SELECT c FROM Ciudad c WHERE c.ciuId = :ciuId"),
    @NamedQuery(name = "Ciudad.findByCiuNombre", query = "SELECT c FROM Ciudad c WHERE c.ciuNombre = :ciuNombre")})
public class Ciudad implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "CIU_ID")
    private Long ciuId;
    @Basic(optional = false)
    @Column(name = "CIU_NOMBRE")
    private String ciuNombre;
    
    @JoinColumn(name = "CIU_DEPTO", referencedColumnName = "DEPTO_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Departamento  departamento;

    

    public Ciudad() {
    }

    public Ciudad(Long ciuId) {
        this.ciuId = ciuId;
    }

    public Ciudad(Long ciuId, String ciuNombre) {
        this.ciuId = ciuId;
        this.ciuNombre = ciuNombre;
    }

    public Long getCiuId() {
        return ciuId;
    }

    public void setCiuId(Long ciuId) {
        this.ciuId = ciuId;
    }

    public String getCiuNombre() {
        return ciuNombre;
    }

    public void setCiuNombre(String ciuNombre) {
        this.ciuNombre = ciuNombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ciuId != null ? ciuId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ciudad)) {
            return false;
        }
        Ciudad other = (Ciudad) object;
        if ((this.ciuId == null && other.ciuId != null) || (this.ciuId != null && !this.ciuId.equals(other.ciuId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.Ciudad[ ciuId=" + ciuId + " ]";
    }
    
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
    
}
