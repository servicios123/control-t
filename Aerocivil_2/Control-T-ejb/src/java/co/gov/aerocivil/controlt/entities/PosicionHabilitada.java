/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Viviana
 */
@Entity
@Table(name = PosicionHabilitada.TABLE_NAME)
@NamedQueries({
    @NamedQuery(name = "PosicionHabilitada.findAll", query = "SELECT p FROM PosicionHabilitada p"),
    @NamedQuery(name = "PosicionHabilitada.findByPhId", query = "SELECT p FROM PosicionHabilitada p WHERE p.phId = :phId"),
    @NamedQuery(name = "PosicionHabilitada.findByPhFvencimiento", query = "SELECT p FROM PosicionHabilitada p WHERE p.phFvencimiento = :phFvencimiento"),
    @NamedQuery(name = "PosicionHabilitada.findByFuncionario", query = "SELECT p.phId FROM PosicionHabilitada p WHERE p.funcionario.funId = :funcionario")})
public class PosicionHabilitada implements Serializable {
    
    public static final String TABLE_NAME = "POSICION_HABILITADA";

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PH_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_POSICIONHABILITADA")
    @SequenceGenerator(name = "SEQ_POSICIONHABILITADA", sequenceName = "SEQ_POSICIONHABILITADA", allocationSize = 1)
    private Long phId;
    @Basic(optional = false)
    @Column(name = "PH_FVENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date phFvencimiento;
    @JoinColumn(name = "PH_FUNCIONARIO", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionario;
    @JoinColumn(name = "PH_POSICION", referencedColumnName = "POS_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Posicion posicion;
    @Transient
    private Date fechaini;
    @Transient
    private Date fechafin;

    public PosicionHabilitada() {
    }

    public PosicionHabilitada(Long phId) {
        this.phId = phId;
    }

    public PosicionHabilitada(Long phId, Date phFvencimiento) {
        this.phId = phId;
        this.phFvencimiento = phFvencimiento;
    }

    public Long getPhId() {
        return phId;
    }

    public void setPhId(Long phId) {
        this.phId = phId;
    }

    public Date getPhFvencimiento() {
        return phFvencimiento;
    }

    public void setPhFvencimiento(Date phFvencimiento) {
        this.phFvencimiento = phFvencimiento;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public Date getFechaini() {
        return fechaini;
    }

    public void setFechaini(Date fechaini) {
        this.fechaini = fechaini;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (phId != null ? phId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PosicionHabilitada)) {
            return false;
        }
        PosicionHabilitada other = (PosicionHabilitada) object;
        if ((this.phId == null && other.phId != null) || (this.phId != null && !this.phId.equals(other.phId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.PosicionHabilitada[ phId=" + phId + " ]";
    }
}
