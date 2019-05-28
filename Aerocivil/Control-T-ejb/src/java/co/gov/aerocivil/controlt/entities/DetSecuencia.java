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
@Table(name = "DET_SECUENCIA")
@NamedQueries({
    @NamedQuery(name = "DetSecuencia.findAll", query = "SELECT d FROM DetSecuencia d order by d.dsOrden"),
    @NamedQuery(name = "DetSecuencia.findByDsId", query = "SELECT d FROM DetSecuencia d WHERE d.dsId = :dsId order by d.dsOrden"),
    @NamedQuery(name = "DetSecuencia.findByDsOrden", query = "SELECT d FROM DetSecuencia d WHERE d.dsOrden = :dsOrden order by d.dsOrden")})
public class DetSecuencia implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    
    @Id
    @Basic(optional = false)
    @Column(name = "DS_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DET_SECUENCIA")
    @SequenceGenerator(name = "SEQ_DET_SECUENCIA", sequenceName = "SEQ_DET_SECUENCIA", allocationSize = 1)
    private Long dsId;
    
    @Basic(optional = false)
    @Column(name = "DS_ORDEN")
    private Long dsOrden;

    
    @JoinColumn(name = "DS_SECUENCIA", referencedColumnName = "SECU_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Secuencia secuencia;
    
    @JoinColumn(name = "DS_JORNADA", referencedColumnName = "JO_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Jornada jornada;

    
    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public Secuencia getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Secuencia secuencia) {
        this.secuencia = secuencia;
    }
     
    public DetSecuencia() {
    }

    public DetSecuencia(Long dsId) {
        this.dsId = dsId;
    }

    public DetSecuencia(Long dsId, Long dsOrden) {
        this.dsId = dsId;
        this.dsOrden = dsOrden;
    }

    public Long getDsId() {
        return dsId;
    }

    public void setDsId(Long dsId) {
        this.dsId = dsId;
    }

    public Long getDsOrden() {
        return dsOrden;
    }

    public void setDsOrden(Long dsOrden) {
        this.dsOrden = dsOrden;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dsId != null ? dsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetSecuencia)) {
            return false;
        }
        DetSecuencia other = (DetSecuencia) object;
        if ((this.dsId == null && other.dsId != null) || (this.dsId != null && !this.dsId.equals(other.dsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.DetSecuencia[ dsId=" + dsId + " ]";
    }
    
}
