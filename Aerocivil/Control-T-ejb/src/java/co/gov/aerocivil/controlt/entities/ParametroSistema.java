/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "PARAMETRO_SISTEMA")
@NamedQueries({
    @NamedQuery(name = "ParametroSistema.findAll", query = "SELECT p FROM ParametroSistema p")})
public class ParametroSistema implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PAR_ID")
    private BigDecimal parId;
    @Basic(optional = false)
    @Column(name = "PAR_ATRIBUTO")
    private String parAtributo;
    @Basic(optional = false)
    @Column(name = "PAR_VALOR")
    private String parValor;
    @Column(name = "PAR_DESCRIPCION")
    private String parDescripcion;

    public ParametroSistema() {
    }

    public ParametroSistema(BigDecimal parId) {
        this.parId = parId;
    }

    public BigDecimal getParId() {
        return parId;
    }

    public void setParId(BigDecimal parId) {
        this.parId = parId;
    }

    public String getParAtributo() {
        return parAtributo;
    }

    public void setParAtributo(String parAtributo) {
        this.parAtributo = parAtributo;
    }

    public String getParValor() {
        return parValor;
    }

    public void setParValor(String parValor) {
        this.parValor = parValor;
    }

    public String getParDescripcion() {
        return parDescripcion;
    }

    public void setParDescripcion(String parDescripcion) {
        this.parDescripcion = parDescripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (parId != null ? parId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParametroSistema)) {
            return false;
        }
        ParametroSistema other = (ParametroSistema) object;
        if ((this.parId == null && other.parId != null) || (this.parId != null && !this.parId.equals(other.parId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.ParametroSistema[ parId=" + parId + " ]";
    }
    
}
