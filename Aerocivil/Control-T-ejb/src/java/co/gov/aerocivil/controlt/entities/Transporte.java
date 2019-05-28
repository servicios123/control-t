/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "TRANSPORTE")
@NamedQueries({
    @NamedQuery(name = "Transporte.findAll", query = "SELECT t FROM Transporte t")})
public class Transporte implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "TRA_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TRANSPORTE")
    @SequenceGenerator(name = "SEQ_TRANSPORTE", sequenceName = "SEQ_TRANSPORTE", allocationSize = 1)
    private Long traId;
    @Basic(optional = false)
    @Column(name = "TRA_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date traFecha;
    @Basic(optional = false)
    @Column(name = "TRA_SECTOR")
    private String traSector;
    
    @JoinColumn(name = "TRA_FUNCIONARIO", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionario;
    
    @JoinColumn(name = "TRA_JORNADA", referencedColumnName = "JO_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Jornada jornada;

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Transporte() {
    }

    public Transporte(Long traId) {
        this.traId = traId;
    }

    public Long getTraId() {
        return traId;
    }

    public void setTraId(Long traId) {
        this.traId = traId;
    }

    public Date getTraFecha() {
        return traFecha;
    }

    public void setTraFecha(Date traFecha) {
        this.traFecha = traFecha;
    }

    public String getTraSector() {
        return traSector;
    }

    public void setTraSector(String traSector) {
        this.traSector = traSector;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (traId != null ? traId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transporte)) {
            return false;
        }
        Transporte other = (Transporte) object;
        if ((this.traId == null && other.traId != null) || (this.traId != null && !this.traId.equals(other.traId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.Transporte[ traId=" + traId + " ]";
    }
    
}
