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
@Table(name = "TEMP_FUNCIONARIO_POS_HABILIT")
@NamedQueries({
    @NamedQuery(name = "TempFuncionarioPosHabilit.findAll", query = "SELECT t FROM TempFuncionarioPosHabilit t"),
    @NamedQuery(name = "TempFuncionarioPosHabilit.findByFuncionarioId", query = "SELECT t FROM TempFuncionarioPosHabilit t WHERE t.funcionarioId = :funcionarioId"),
    @NamedQuery(name = "TempFuncionarioPosHabilit.findByPosicionId", query = "SELECT t FROM TempFuncionarioPosHabilit t WHERE t.posicionId = :posicionId"),
    @NamedQuery(name = "TempFuncionarioPosHabilit.findByFecha", query = "SELECT t FROM TempFuncionarioPosHabilit t WHERE t.fecha = :fecha"),
    @NamedQuery(name = "TempFuncionarioPosHabilit.findById", query = "SELECT t FROM TempFuncionarioPosHabilit t WHERE t.id = :id")})
public class TempFuncionarioPosHabilit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "FUNCIONARIO_ID")
    private BigInteger funcionarioId;
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

    public TempFuncionarioPosHabilit() {
    }

    public TempFuncionarioPosHabilit(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(BigInteger funcionarioId) {
        this.funcionarioId = funcionarioId;
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
        if (!(object instanceof TempFuncionarioPosHabilit)) {
            return false;
        }
        TempFuncionarioPosHabilit other = (TempFuncionarioPosHabilit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TempFuncionarioPosHabilit{" + "funcionarioId=" + funcionarioId + ", posicionId=" + posicionId + ", fecha=" + fecha + ", id=" + id + '}';
    }
    
}
