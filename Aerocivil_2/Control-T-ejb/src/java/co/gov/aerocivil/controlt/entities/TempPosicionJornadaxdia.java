/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "TEMP_POSICION_JORNADAXDIA")
@NamedQueries({
    @NamedQuery(name = "TempPosicionJornadaxdia.findAll", query = "SELECT t FROM TempPosicionJornadaxdia t"),
    @NamedQuery(name = "TempPosicionJornadaxdia.findByPosJornadaId", query = "SELECT t FROM TempPosicionJornadaxdia t WHERE t.posJornadaId = :posJornadaId"),
    @NamedQuery(name = "TempPosicionJornadaxdia.findByPosicionId", query = "SELECT t FROM TempPosicionJornadaxdia t WHERE t.posicionId = :posicionId"),
    @NamedQuery(name = "TempPosicionJornadaxdia.findByFecha", query = "SELECT t FROM TempPosicionJornadaxdia t WHERE t.fecha = :fecha"),
    @NamedQuery(name = "TempPosicionJornadaxdia.findById", query = "SELECT t FROM TempPosicionJornadaxdia t WHERE t.id = :id")})
public class TempPosicionJornadaxdia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "POS_JORNADA_ID")
    private BigInteger posJornadaId;
    @Column(name = "POSICION_ID")
    private BigInteger posicionId;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;

    public TempPosicionJornadaxdia() {
    }

    public TempPosicionJornadaxdia(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getPosJornadaId() {
        return posJornadaId;
    }

    public void setPosJornadaId(BigInteger posJornadaId) {
        this.posJornadaId = posJornadaId;
    }

    public BigInteger getPosicionId() {
        return posicionId;
    }

    public void setPosicionId(BigInteger posicionId) {
        this.posicionId = posicionId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TempPosicionJornadaxdia)) {
            return false;
        }
        TempPosicionJornadaxdia other = (TempPosicionJornadaxdia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TempPosicionJornadaxdia{" + "posJornadaId=" + posJornadaId + ", posicionId=" + posicionId + ", fecha=" + fecha + ", id=" + id + '}';
    }
    
}
