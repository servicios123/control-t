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
 * @author Administrador
 */
@Entity
@Table(name = "NOTIFICACION")
@NamedQueries({
    @NamedQuery(name = "Notificacion.findAll", query = "SELECT n FROM Notificacion n")})
public class Notificacion implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "NOT_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NOTIFICACION")
    @SequenceGenerator(name = "SEQ_NOTIFICACION", sequenceName = "SEQ_NOTIFICACION", allocationSize = 1)
    private Long notId;
    
    @Basic(optional = false)
    @Column(name = "NOT_TIPO")
    private String notTipo;

    @JoinColumn(name = "NOT_FUNCIONARIO", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario not_Funcionario;

    @Basic(optional = false)
    @Column(name = "NOT_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notFecha;

    
     @Transient
    private Date fechaini;

    
    @Transient
    private Date fechafin;
    
    public Notificacion() {
    }

    public String getNotTipo() {
        return notTipo;
    }

    public void setNotTipo(String notTipo) {
        this.notTipo = notTipo;
    }

    public Date getNotFecha() {
        return notFecha;
    }

    public void setNotFecha(Date notFecha) {
        this.notFecha = notFecha;
    }
    
    public Funcionario getNot_Funcionario() {
        return not_Funcionario;
    }

    public void setNot_Funcionario(Funcionario not_Funcionario) {
        this.not_Funcionario = not_Funcionario;
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
        hash += (notId != null ? notId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notificacion)) {
            return false;
        }
        Notificacion other = (Notificacion) object;
        if ((this.notId == null && other.notId != null) || (this.notId != null && !this.notId.equals(other.notId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.Notificacion[ notId=" + notId + " ]";
    }

    public Long getNotId() {
        return notId;
    }

    public void setNotId(Long notId) {
        this.notId = notId;
    }
    
    public Funcionario getNotFuncionario() {
        return not_Funcionario;
    }

    public void setNotFuncionario(Funcionario notFuncionario) {
        this.not_Funcionario = notFuncionario;
    }
        
}
