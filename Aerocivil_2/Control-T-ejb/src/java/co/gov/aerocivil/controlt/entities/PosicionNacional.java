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
import javax.persistence.Transient;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "POSICION_NACIONAL")
@NamedQueries({
    @NamedQuery(name = "PosicionNacional.findAll", query = "SELECT p FROM PosicionNacional p"),
    @NamedQuery(name = "PosicionNacional.findByPnId", query = "SELECT p FROM PosicionNacional p WHERE p.pnId = :pnId"),
    @NamedQuery(name = "PosicionNacional.findByPnNombre", query = "SELECT p FROM PosicionNacional p WHERE p.pnNombre = :pnNombre"),
    @NamedQuery(name = "PosicionNacional.findByPnAlias", query = "SELECT p FROM PosicionNacional p WHERE p.pnAlias = :pnAlias"),
    @NamedQuery(name = "PosicionNacional.findByPnEstado", query = "SELECT p FROM PosicionNacional p WHERE p.pnEstado = :pnEstado"),
    @NamedQuery(name = "PosicionNacional.findByPnVencimiento", query = "SELECT p FROM PosicionNacional p WHERE p.pnVencimiento = :pnVencimiento")})
public class PosicionNacional implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PN_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_POSICION_NAL")
    @SequenceGenerator(name = "SEQ_POSICION_NAL", sequenceName = "SEQ_POSICION_NAL", allocationSize = 1)
    private Long pnId;
    @JoinColumn(name = "PN_DEP_CATEGORIA", referencedColumnName = "DC_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DepCategoria depCategoria;

    @Column(name = "PN_NOMBRE")
    private String pnNombre;
    @Column(name = "PN_ALIAS")
    private String pnAlias;
    @Column(name = "PN_ESTADO")
    private String pnEstado;
    @Column(name = "PN_VENCIMIENTO")
    private Long pnVencimiento;
    
    @Transient
    private Boolean seleccionada;


    public PosicionNacional() {
    }

    public PosicionNacional(Long pnId) {
        this.pnId = pnId;
    }

    public Long getPnId() {
        return pnId;
    }

    public void setPnId(Long pnId) {
        this.pnId = pnId;
    }

    public String getPnNombre() {
        return pnNombre;
    }

    public void setPnNombre(String pnNombre) {
        this.pnNombre = pnNombre;
    }

    public String getPnAlias() {
        return pnAlias;
    }

    public void setPnAlias(String pnAlias) {
        this.pnAlias = pnAlias;
    }

    public String getPnEstado() {
        return pnEstado;
    }

    public void setPnEstado(String pnEstado) {
        this.pnEstado = pnEstado;
    }

    public Long getPnVencimiento() {
        return pnVencimiento;
    }

    public void setPnVencimiento(Long pnVencimiento) {
        this.pnVencimiento = pnVencimiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pnId != null ? pnId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PosicionNacional)) {
            return false;
        }
        PosicionNacional other = (PosicionNacional) object;
        if ((this.pnId == null && other.pnId != null) || (this.pnId != null && !this.pnId.equals(other.pnId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.PosicionNacional[ pnId=" + pnId + " ]";
    }
    
    public String getSiglaPosicion(){
        return "<b>"+this.pnAlias+"</b> - "+this.pnNombre;
     }

    public DepCategoria getDepCategoria() {
        return depCategoria;
    }

    public void setDepCategoria(DepCategoria depCategoria) {
        this.depCategoria = depCategoria;
    }

    public Boolean getSeleccionada() {
        return seleccionada;
    }

    public void setSeleccionada(Boolean seleccionada) {
        this.seleccionada = seleccionada;
    }

}
