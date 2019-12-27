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
import javax.persistence.Table;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "DIARIO_SENAL_TIPO")
public class DiarioSenalTipo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "DST_ID")   
    private Long dstId;
    
    @Basic(optional = false)
    @Column(name = "DST_NOMBRE")
    private String dstNombre;

    public Long getId() {
        return getDstId();
    }

    public void setId(Long id) {
        this.setDstId(id);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getDstId() != null ? getDstId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the dstId fields are not set
        if (!(object instanceof DiarioSenalTipo)) {
            return false;
        }
        DiarioSenalTipo other = (DiarioSenalTipo) object;
        if ((this.getDstId() == null && other.getDstId() != null) || (this.getDstId() != null && !this.dstId.equals(other.dstId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.DiarioSenalTipo[ id=" + getDstId() + " ]";
    }

    /**
     * @return the dstId
     */
    public Long getDstId() {
        return dstId;
    }

    /**
     * @param dstId the dstId to set
     */
    public void setDstId(Long dstId) {
        this.dstId = dstId;
    }

    /**
     * @return the dstNombre
     */
    public String getDstNombre() {
        return dstNombre;
    }

    /**
     * @param dstNombre the dstNombre to set
     */
    public void setDstNombre(String dstNombre) {
        this.dstNombre = dstNombre;
    }
    
}
