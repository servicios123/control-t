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
 * @author Viviana
 */
@Entity
@Table(name = "EMERGENCIAS")
@NamedQueries({
    @NamedQuery(name = "Emergencias.findAll", query = "SELECT e FROM Emergencias e")})
public class Emergencias implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "EM_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EMERGENCIA")
    @SequenceGenerator(name = "SEQ_EMERGENCIA", sequenceName = "SEQ_EMERGENCIA", allocationSize = 1)    
    private Long emId;
    
    @Basic(optional = false)
    @Column(name = "EM_FECHAINI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date emFechaini;
    @Basic(optional = false)
    @Column(name = "EM_HINICIO")
    private BigInteger emHinicio;
    @Basic(optional = false)
    @Column(name = "EM_MINICIO")
    private BigInteger emMinicio;
    @Basic(optional = false)
    @Column(name = "EM_HFIN")
    private BigInteger emHfin;
    @Column(name = "EM_MFIN")
    private BigInteger emMfin;
    @Column(name = "EM_DESCRIPCION")
    private String emDescripcion;
    @Column(name = "EM_FECHAFIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date emFechafin;
    @Column(name = "EM_ESTADO")
    private String emEstado;

     @JoinColumn(name = "EM_FUNCIONARIO", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionario;

   

    
    public Emergencias() {
    }

    public Emergencias(Long emId) {
        this.emId = emId;
    }

    public Emergencias(Long emId, Date emFechaini, BigInteger emHinicio, BigInteger emMinicio, BigInteger emHfin) {
        this.emId = emId;
        this.emFechaini = emFechaini;
        this.emHinicio = emHinicio;
        this.emMinicio = emMinicio;
        this.emHfin = emHfin;
    }

    public Long getEmId() {
        return emId;
    }

    public void setEmId(Long emId) {
        this.emId = emId;
    }

    public Date getEmFechaini() {
        return emFechaini;
    }

    public void setEmFechaini(Date emFechaini) {
        this.emFechaini = emFechaini;
    }

    public BigInteger getEmHinicio() {
        return emHinicio;
    }

    public void setEmHinicio(BigInteger emHinicio) {
        this.emHinicio = emHinicio;
    }

    public BigInteger getEmMinicio() {
        return emMinicio;
    }

    public void setEmMinicio(BigInteger emMinicio) {
        this.emMinicio = emMinicio;
    }

    public BigInteger getEmHfin() {
        return emHfin;
    }

    public void setEmHfin(BigInteger emHfin) {
        this.emHfin = emHfin;
    }

    public BigInteger getEmMfin() {
        return emMfin;
    }

    public void setEmMfin(BigInteger emMfin) {
        this.emMfin = emMfin;
    }

    public String getEmDescripcion() {
        return emDescripcion;
    }

    public void setEmDescripcion(String emDescripcion) {
        this.emDescripcion = emDescripcion;
    }

    public Date getEmFechafin() {
        return emFechafin;
    }

    public void setEmFechafin(Date emFechafin) {
        this.emFechafin = emFechafin;
    }

    public String getEmEstado() {
        return emEstado;
    }

    public void setEmEstado(String emEstado) {
        this.emEstado = emEstado;
    }
    
     public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (emId != null ? emId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Emergencias)) {
            return false;
        }
        Emergencias other = (Emergencias) object;
        if ((this.emId == null && other.emId != null) || (this.emId != null && !this.emId.equals(other.emId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.Emergencias[ emId=" + emId + " ]";
    }
    
}
