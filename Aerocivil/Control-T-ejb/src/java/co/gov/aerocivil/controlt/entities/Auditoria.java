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
@Table(name = "AUDITORIA")
@NamedQueries({
    @NamedQuery(name = "Auditoria.findAll", query = "SELECT a FROM Auditoria a")})
public class Auditoria implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "AUD_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AUDITORIA")
    @SequenceGenerator(name = "SEQ_AUDITORIA", sequenceName = "SEQ_AUDITORIA", allocationSize = 1)
    private Long audId;
    @Column(name = "AUD_ACCION")
    private String audAccion;
    @Column(name = "AUD_TABLA")
    private String audTabla;
    @Column(name = "AUD_PK")
    private Long audPk;
     @Column(name = "AUD_DATOS")
    private String audDatos;

    
    @Column(name = "AUD_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date audFecha;
    
    @Transient
    private Date audFechaFin;

    @JoinColumn(name = "AUD_FUNCIONARIO", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)    
    private Funcionario funcionario;

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario func) {
        this.funcionario = func;
    }

    public Auditoria() {
    }


    public Long getAudId() {
        return audId;
    }

    public void setAudId(Long audId) {
        this.audId = audId;
    }

    public String getAudAccion() {
        return audAccion;
    }

    public void setAudAccion(String audAccion) {
        this.audAccion = audAccion;
    }

    public String getAudTabla() {
        return audTabla;
    }

    public void setAudTabla(String audTabla) {
        this.audTabla = audTabla;
    }

    public Long getAudPk() {
        return audPk;
    }

    public void setAudPk(Long audPk) {
        this.audPk = audPk;
    }

    public Date getAudFecha() {
        return audFecha;
    }

    public void setAudFecha(Date audFecha) {
        this.audFecha = audFecha;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (audId != null ? audId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Auditoria)) {
            return false;
        }
        Auditoria other = (Auditoria) object;
        if ((this.audId == null && other.audId != null) || (this.audId != null && !this.audId.equals(other.audId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.Auditoria[ audId=" + audId + " ]";
    }

    public Date getAudFechaFin() {
        return audFechaFin;
    }

    public void setAudFechaFin(Date audFechaFin) {
        this.audFechaFin = audFechaFin;
    }
    public String getAudDatos() {
        return audDatos;
    }

    public void setAudDatos(String audDatos) {
        this.audDatos = audDatos;
    }

}
