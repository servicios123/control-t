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
@Table(name = "POSICION_JORNADA")
@NamedQueries({
    @NamedQuery(name = "PosicionJornada.findAll", query = "SELECT p FROM PosicionJornada p order by p.pjAlias"),
    @NamedQuery(name = "PosicionJornada.findByPjAlias", query = "SELECT p FROM PosicionJornada p WHERE p.pjAlias = :pjAlias order by p.pjAlias"),
    @NamedQuery(name = "PosicionJornada.findByPjId", query = "SELECT p FROM PosicionJornada p WHERE p.pjId = :pjId order by p.pjAlias"),
    @NamedQuery(name = "PosicionJornada.findByPjEstado", query = "SELECT p FROM PosicionJornada p WHERE p.pjEstado = :pjEstado order by p.pjAlias")})
public class PosicionJornada implements Serializable {
    private static final long serialVersionUID = 1L;
   
    @Column(name = "PJ_ALIAS")
    private String pjAlias;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    
    @Id
    @Basic(optional = false)
    @Column(name = "PJ_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_POSJORNADA")
    @SequenceGenerator(name = "SEQ_POSJORNADA", sequenceName = "SEQ_POSJORNADA", allocationSize = 1)
    private Long pjId;
    
   
    
    @Basic(optional = false)
    @Column(name = "PJ_ESTADO")
    private String pjEstado;
    
    @JoinColumn(name = "PJ_JORNADA_OP", referencedColumnName = "JO_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Jornada jornada;

    @JoinColumn(name = "PJ_POSICION", referencedColumnName = "POS_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Posicion posicion;
    
    @JoinColumn(name = "PJ_DEPENDENCIA", referencedColumnName = "DEP_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Dependencia dependencia;

    

    
    public PosicionJornada() {
    }

    public PosicionJornada(Long pjId) {
        this.pjId = pjId;
    }

    public PosicionJornada(Long pjId, String pjEstado) {
        this.pjId = pjId;
        this.pjEstado = pjEstado;
    }

    public String getPjAlias() {
        return pjAlias;
    }

    public void setPjAlias(String pjAlias) {
        this.pjAlias = pjAlias;
    }

    public Long getPjId() {
        return pjId;
    }

    public void setPjId(Long pjId) {
        this.pjId = pjId;
    }

    public String getPjEstado() {
        return pjEstado;
    }

    public void setPjEstado(String pjEstado) {
        this.pjEstado = pjEstado;
    }

    public Jornada getJornada() {
        return jornada;
    }
    
    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }
    
    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pjId != null ? pjId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PosicionJornada)) {
            return false;
        }
        PosicionJornada other = (PosicionJornada) object;
        if ((this.pjId == null && other.pjId != null) || (this.pjId != null && !this.pjId.equals(other.pjId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.PosicionJornada[ pjId=" + pjId + " ]";
    }
    
}
