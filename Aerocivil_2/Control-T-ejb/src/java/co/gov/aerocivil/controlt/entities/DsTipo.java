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
@Table(name = "DS_TIPO")
@NamedQueries({
    @NamedQuery(name = "DsTipo.findAll", query = "SELECT d FROM DsTipo d"),
    @NamedQuery(name = "DsTipo.findByDstId", query = "SELECT d FROM DsTipo d WHERE d.dstId = :dstId"),
    @NamedQuery(name = "DsTipo.findByDstNombre", query = "SELECT d FROM DsTipo d WHERE d.dstNombre = :dstNombre")})
public class DsTipo implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "DST_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TIPO")
    @SequenceGenerator(name = "SEQ_TIPO", sequenceName = "SEQ_TIPO", allocationSize = 1)
    private Long dstId;
    @Column(name = "DST_NOMBRE")
    private String dstNombre;
    @JoinColumn(name = "DST_CATEGORIA", referencedColumnName = "DC_ID")
    @ManyToOne(optional = false)
    private DepCategoria categoria;

    public DsTipo() {
    }

    public DsTipo(Long dstId) {
        this.dstId = dstId;
    }

    public Long getDstId() {
        return dstId;
    }

    public void setDstId(Long dstId) {
        this.dstId = dstId;
    }

    public String getDstNombre() {
        return dstNombre;
    }

    public void setDstNombre(String dstNombre) {
        this.dstNombre = dstNombre;
    }

    public DepCategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(DepCategoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dstId != null ? dstId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DsTipo)) {
            return false;
        }
        DsTipo other = (DsTipo) object;
        if ((this.dstId == null && other.dstId != null) || (this.dstId != null && !this.dstId.equals(other.dstId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.DsTipo[ dstId=" + dstId + " ]";
    }
    
}
