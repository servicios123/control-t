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
@Table(name = "POSICION")
@NamedQueries({
    @NamedQuery(name = "Posicion.findAll", query = "SELECT p FROM Posicion p"),
    @NamedQuery(name = "Posicion.findById", query = "SELECT p FROM Posicion p WHERE p.posId = :posId"),
    @NamedQuery(name = "Posicion.findByPosTiempoVence", query = "SELECT p FROM Posicion p WHERE p.posTiempoVence = :posTiempoVence"),
    //@NamedQuery(name = "Posicion.findByPosAlias", query = "SELECT p FROM Posicion p WHERE p.posAlias = :posAlias"),
    @NamedQuery(name = "Posicion.findByDependencia", query = "SELECT p FROM Posicion p WHERE p.dependencia.depId = :dep and p.posEstado='Activo' order by p.posicionNacional.pnAlias")})

public class Posicion implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    
    @Id
    @Basic(optional = false)
    @Column(name = "POS_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_POSICION")
    @SequenceGenerator(name = "SEQ_POSICION", sequenceName = "SEQ_POSICION", allocationSize = 1)
    private Long posId;
    
    @Basic(optional = false)
    @Column(name = "POS_TIEMPO_VENCE")
    private Long posTiempoVence;
    /*@Column(name = "POS_ALIAS")
    private String posAlias;*/
    @Column(name = "POS_ESTADO")
    private String posEstado;

   
    @JoinColumn(name = "pos_posicion_nacional", referencedColumnName = "PN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PosicionNacional posicionNacional;
    
    @JoinColumn(name = "POS_DEPENDENCIA", referencedColumnName = "DEP_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Dependencia dependencia;

    public Posicion() {
    }

    public Posicion(Long id) {
        this.posId=id;
    }


    /*public String getPosAlias() {
        return posAlias;
    }

    public void setPosAlias(String posAlias) {
        this.posAlias = posAlias;
    }*/
    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (posId != null ? posId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Posicion)) {
            return false;
        }
        Posicion other = (Posicion) object;
        if ((this.posId == null && other.posId != null) || (this.posId != null && !this.posId.equals(other.posId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.Posicion[ posId=" + posId + " ]";
    }

        public Long getPosId() {
        return posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    public Long getPosTiempoVence() {
        return posTiempoVence;
    }

    public void setPosTiempoVence(Long posTiempoVence) {
        this.posTiempoVence = posTiempoVence;
    }
    public String getPosEstado() {
        return posEstado;
    }

    public void setPosEstado(String posEstado) {
        this.posEstado = posEstado;
    }
    
    public PosicionNacional getPosicionNacional() {
        return posicionNacional;
    }

    public void setPosicionNacional(PosicionNacional posicionNacional) {
        this.posicionNacional = posicionNacional;
    }
    

}
