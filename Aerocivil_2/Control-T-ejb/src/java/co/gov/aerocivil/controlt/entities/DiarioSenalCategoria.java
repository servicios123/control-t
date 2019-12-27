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
@Table(name="DIARIO_SENAL_CATEGORIA")
public class DiarioSenalCategoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "DSC_ID")
    private Long dscId;
    @Basic(optional = false)
    @Column(name = "DSC_NOMBRE")
    private String dscNombre;
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getDscId() != null ? getDscId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiarioSenalCategoria)) {
            return false;
        }
        DiarioSenalCategoria other = (DiarioSenalCategoria) object;
        if ((this.getDscId() == null && other.getDscId() != null) || (this.getDscId() != null && !this.dscId.equals(other.dscId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.DiarioSenalCategoria[ id=" + getDscId() + " ]";
    }

    /**
     * @return the dscId
     */
    public Long getDscId() {
        return dscId;
    }

    /**
     * @param dscId the dscId to set
     */
    public void setDscId(Long dscId) {
        this.dscId = dscId;
    }

    /**
     * @return the dscNombre
     */
    public String getDscNombre() {
        return dscNombre;
    }

    /**
     * @param dscNombre the dscNombre to set
     */
    public void setDscNombre(String dscNombre) {
        this.dscNombre = dscNombre;
    }
    
}
