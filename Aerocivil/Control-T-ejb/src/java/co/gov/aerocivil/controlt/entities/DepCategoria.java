/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Viviana
 */
@Entity
@Table(name = "DEP_CATEGORIA")
@NamedQueries({
    @NamedQuery(name = "DepCategoria.findAll", query = "SELECT d FROM DepCategoria d"),
    @NamedQuery(name = "DepCategoria.findByDcId", query = "SELECT d FROM DepCategoria d WHERE d.dcId = :dcId"),
    @NamedQuery(name = "DepCategoria.findByDcNombre", query = "SELECT d FROM DepCategoria d WHERE d.dcNombre = :dcNombre"),
    @NamedQuery(name = "DepCategoria.findByDcRop", query = "SELECT d FROM DepCategoria d WHERE d.dcRop = :dcRop"),
    @NamedQuery(name = "DepCategoria.findByDcRopGrado", query = "SELECT d FROM DepCategoria d WHERE d.dcRopGrado = :dcRopGrado"),
    @NamedQuery(name = "DepCategoria.findByDcCantRop", query = "SELECT d FROM DepCategoria d WHERE d.dcCantRop = :dcCantRop"),
    @NamedQuery(name = "DepCategoria.findByDcTransporte", query = "SELECT d FROM DepCategoria d WHERE d.dcTransporte = :dcTransporte")})
public class DepCategoria implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "DC_ID")
    private Long dcId;
    @Basic(optional = false)
    @Column(name = "DC_NOMBRE")
    private String dcNombre;
    @Column(name = "DC_ROP")
    private Boolean dcRop;
    @Column(name = "DC_ROP_GRADO")
    private Long dcRopGrado;
    @Column(name = "DC_CANT_ROP")
    private Long dcCantRop;
    @Column(name = "DC_TRANSPORTE")
    private Boolean dcTransporte;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<ParametrosDependencia> parametrosDependencias;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<DsTipo> tipos;
    
    @Column(name = "DC_VERSION_DS")
    private String dcVersionDs;
    
    @Column(name = "DC_CLAVE_DS")
    private String dcClaveDs;
    
    @Column(name = "DC_FECHA_DS")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dcFechaDs;
    
    @Column(name = "DC_VERSION_DSE")
    private String dcVersionDse;
    
    @Column(name = "DC_CLAVE_DSE")
    private String dcClaveDse;
    
    @Column(name = "DC_FECHA_DSE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dcFechaDse;
    
    @Column(name = "DC_VERSION_P")
    private String dcVersionP;
    
    @Column(name = "DC_CLAVE_P")
    private String dcClaveP;
    
    @Column(name = "DC_FECHA_P")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dcFechaP;
    
    @Column(name = "DC_VERSION_DP")
    private String dcVersionDp;
    
    @Column(name = "DC_CLAVE_DP")
    private String dcClaveDp;
    
    @Column(name = "DC_FECHA_DP")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dcFechaDp;

    public List<DsTipo> getTipos() {
        return tipos;
    }

    public void setTipos(List<DsTipo> tipos) {
        this.tipos = tipos;
    }

    
    public List<ParametrosDependencia> getParametrosDependencias() {
        return parametrosDependencias;
    }

    public void setParametrosDependencias(List<ParametrosDependencia> parametrosDependencias) {
        this.parametrosDependencias = parametrosDependencias;
    }  

    public DepCategoria() {
    }

    public DepCategoria(Long dcId) {
        this.dcId = dcId;
    }

    public DepCategoria(Long dcId, String dcNombre) {
        this.dcId = dcId;
        this.dcNombre = dcNombre;
    }

    public Long getDcId() {
        return dcId;
    }

    public void setDcId(Long dcId) {
        this.dcId = dcId;
    }

    public String getDcNombre() {
        return dcNombre;
    }

    public void setDcNombre(String dcNombre) {
        this.dcNombre = dcNombre;
    }

    public Boolean getDcRop() {
        return dcRop;
    }
     public String getRop() {
         if(dcRop!=null){
        return dcRop?"Si":"No";
         }else{ return null;}
    }
    public void setDcRop(Boolean dcRop) {
        this.dcRop = dcRop;
    }

    public Long getDcRopGrado() {
        return dcRopGrado;
    }

    public void setDcRopGrado(Long dcRopGrado) {
        this.dcRopGrado = dcRopGrado;
    }

    public Long getDcCantRop() {
        return dcCantRop;
    }

    public void setDcCantRop(Long dcCantRop) {
        this.dcCantRop = dcCantRop;
    }

    public Boolean getDcTransporte() {
        return dcTransporte;
    }
    public String getTransporte() {
        if(dcTransporte!=null){
        return dcTransporte?"Si":"No";
        }else{
        return null;
        }
    }
    public void setDcTransporte(Boolean dcTransporte) {
        this.dcTransporte = dcTransporte;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dcId != null ? dcId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DepCategoria)) {
            return false;
        }
        DepCategoria other = (DepCategoria) object;
        if ((this.dcId == null && other.dcId != null) || (this.dcId != null && !this.dcId.equals(other.dcId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.DepCategoria[ dcId=" + dcId + " ]";
    }

    /**
     * @return the dcVersionDs
     */
    public String getDcVersionDs() {
        return dcVersionDs;
    }

    /**
     * @param dcVersionDs the dcVersionDs to set
     */
    public void setDcVersionDs(String dcVersionDs) {
        this.dcVersionDs = dcVersionDs;
    }

    /**
     * @return the dcClaveDs
     */
    public String getDcClaveDs() {
        return dcClaveDs;
    }

    /**
     * @param dcClaveDs the dcClaveDs to set
     */
    public void setDcClaveDs(String dcClaveDs) {
        this.dcClaveDs = dcClaveDs;
    }

    /**
     * @return the dcFechaDs
     */
    public Date getDcFechaDs() {
        return dcFechaDs;
    }

    /**
     * @param dcFechaDs the dcFechaDs to set
     */
    public void setDcFechaDs(Date dcFechaDs) {
        this.dcFechaDs = dcFechaDs;
    }

    /**
     * @return the dcVersionDse
     */
    public String getDcVersionDse() {
        return dcVersionDse;
    }

    /**
     * @param dcVersionDse the dcVersionDse to set
     */
    public void setDcVersionDse(String dcVersionDse) {
        this.dcVersionDse = dcVersionDse;
    }

    /**
     * @return the dcClaveDse
     */
    public String getDcClaveDse() {
        return dcClaveDse;
    }

    /**
     * @param dcClaveDse the dcClaveDse to set
     */
    public void setDcClaveDse(String dcClaveDse) {
        this.dcClaveDse = dcClaveDse;
    }

    /**
     * @return the dcFechaDse
     */
    public Date getDcFechaDse() {
        return dcFechaDse;
    }

    /**
     * @param dcFechaDse the dcFechaDse to set
     */
    public void setDcFechaDse(Date dcFechaDse) {
        this.dcFechaDse = dcFechaDse;
    }

    /**
     * @return the dcVersionP
     */
    public String getDcVersionP() {
        return dcVersionP;
    }

    /**
     * @param dcVersionP the dcVersionP to set
     */
    public void setDcVersionP(String dcVersionP) {
        this.dcVersionP = dcVersionP;
    }

    /**
     * @return the dcClaveP
     */
    public String getDcClaveP() {
        return dcClaveP;
    }

    /**
     * @param dcClaveP the dcClaveP to set
     */
    public void setDcClaveP(String dcClaveP) {
        this.dcClaveP = dcClaveP;
    }

    /**
     * @return the dcFechaP
     */
    public Date getDcFechaP() {
        return dcFechaP;
    }

    /**
     * @param dcFechaP the dcFechaP to set
     */
    public void setDcFechaP(Date dcFechaP) {
        this.dcFechaP = dcFechaP;
    }

    /**
     * @return the dcVersionDp
     */
    public String getDcVersionDp() {
        return dcVersionDp;
    }

    /**
     * @param dcVersionDp the dcVersionDp to set
     */
    public void setDcVersionDp(String dcVersionDp) {
        this.dcVersionDp = dcVersionDp;
    }

    /**
     * @return the dcClaveDp
     */
    public String getDcClaveDp() {
        return dcClaveDp;
    }

    /**
     * @param dcClaveDp the dcClaveDp to set
     */
    public void setDcClaveDp(String dcClaveDp) {
        this.dcClaveDp = dcClaveDp;
    }

    /**
     * @return the dcFechaDp
     */
    public Date getDcFechaDp() {
        return dcFechaDp;
    }

    /**
     * @param dcFechaDp the dcFechaDp to set
     */
    public void setDcFechaDp(Date dcFechaDp) {
        this.dcFechaDp = dcFechaDp;
    }

  
    
}
