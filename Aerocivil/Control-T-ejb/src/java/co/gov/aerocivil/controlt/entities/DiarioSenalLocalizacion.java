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
@Table(name = "DIARIO_SENAL_LOCALIZACION")
public class DiarioSenalLocalizacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "DSL_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dslId;
    
    @Basic(optional = false)
    @Column(name = "DSL_NOMBRE")
    private String dslNombre;
    
    @Basic(optional = false)
    @Column(name = "DSL_ALIAS")
    private String dslAlias;
    
    @Basic(optional = false)
    @Column(name = "DSL_MUNICIPIO")
    private String dslMunicipio;
    

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getDslId() != null ? getDslId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the dslId fields are not set
        if (!(object instanceof DiarioSenalLocalizacion)) {
            return false;
        }
        DiarioSenalLocalizacion other = (DiarioSenalLocalizacion) object;
        if ((this.getDslId() == null && other.getDslId() != null) || (this.getDslId() != null && !this.dslId.equals(other.dslId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.DiarioSenalLocalizacion[ id=" + getDslId() + " ]";
    }

    /**
     * @return the dslId
     */
    public Long getDslId() {
        return dslId;
    }

    /**
     * @param dslId the dslId to set
     */
    public void setDslId(Long dslId) {
        this.dslId = dslId;
    }

    /**
     * @return the dslNombre
     */
    public String getDslNombre() {
        return dslNombre;
    }

    /**
     * @param dslNombre the dslNombre to set
     */
    public void setDslNombre(String dslNombre) {
        this.dslNombre = dslNombre;
    }

    /**
     * @return the dslAlias
     */
    public String getDslAlias() {
        return dslAlias;
    }

    /**
     * @param dslAlias the dslAlias to set
     */
    public void setDslAlias(String dslAlias) {
        this.dslAlias = dslAlias;
    }

    /**
     * @return the dslMunicipio
     */
    public String getDslMunicipio() {
        return dslMunicipio;
    }

    /**
     * @param dslMunicipio the dslMunicipio to set
     */
    public void setDslMunicipio(String dslMunicipio) {
        this.dslMunicipio = dslMunicipio;
    }
    
}
