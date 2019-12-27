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
@Table(name = "PROGRAMACION")
@NamedQueries({
    @NamedQuery(name = "Programacion.findAll", query = "SELECT p FROM Programacion p"),
    @NamedQuery(name = "Programacion.findByProId", query = "SELECT p FROM Programacion p WHERE p.proId = :proId"),
    @NamedQuery(name = "Programacion.findByProFechaInicio", query = "SELECT p FROM Programacion p WHERE p.proFechaInicio = :proFechaInicio"),
    @NamedQuery(name = "Programacion.findByProFechaFin", query = "SELECT p FROM Programacion p WHERE p.proFechaFin = :proFechaFin"),
    @NamedQuery(name = "Programacion.findByFecha", query = "SELECT p FROM Programacion p WHERE :proFecha between p.proFechaInicio and p.proFechaFin "),
    @NamedQuery(name = "Programacion.findByProFuncionarioAprueba", query = "SELECT p FROM Programacion p WHERE p.funcionarioAprueba = :proFuncionarioAprueba"),
    @NamedQuery(name = "Programacion.findByProFechaAprobacion", query = "SELECT p FROM Programacion p WHERE p.proFechaAprobacion = :proFechaAprobacion"),
    @NamedQuery(name = "Programacion.findMaximos", query = "SELECT max(p.proId),p.dependencia.depId FROM Programacion p group by p.dependencia.depId"),
    @NamedQuery(name = "Programacion.findByProEstado", query = "SELECT p FROM Programacion p WHERE p.proEstado = :proEstado")})
    
public class Programacion implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    
    
    @Id
    @Basic(optional = false)
    @Column(name = "PRO_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PROGRAMACION")
    @SequenceGenerator(name = "SEQ_PROGRAMACION", sequenceName = "SEQ_PROGRAMACION", allocationSize = 1)
    private Long proId;
    
    @Basic(optional = false)
    @Column(name = "PRO_FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date proFechaInicio;
    
    @Basic(optional = false)
    @Column(name = "PRO_FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date proFechaFin;
    
    @JoinColumn(name = "PRO_FUNCIONARIO_APRUEBA", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionarioAprueba;

    @Column(name = "PRO_FECHA_APROBACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date proFechaAprobacion;
    
    @JoinColumn(name = "PRO_DEPENDENCIA", referencedColumnName = "DEP_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Dependencia dependencia;

    @Column(name = "PRO_ESTADO")
    private Integer proEstado;

    @JoinColumn(name = "PRO_FUNCIONARIO_GENERA", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Funcionario funcionarioGenera;
    
    @Transient
    private boolean reporte;

    public boolean isReporte() {
        return reporte;
    }

    public void setReporte(boolean reporte) {
        this.reporte = reporte;
    }
    
            
    public Long getProId() {
        return proId;
    }

    public void setProId(Long proId) {
        this.proId = proId;
    }

    public Date getProFechaInicio() {
        return proFechaInicio;
    }

    public void setProFechaInicio(Date proFechaInicio) {
        this.proFechaInicio = proFechaInicio;
    }

    public Date getProFechaFin() {
        return proFechaFin;
    }

    public void setProFechaFin(Date proFechaFin) {
        this.proFechaFin = proFechaFin;
    }

    public Funcionario getFuncionarioAprueba() {
        return funcionarioAprueba;
    }

    public void setFuncionarioAprueba(Funcionario funcionarioAprueba) {
        this.funcionarioAprueba = funcionarioAprueba;
    }

    public Date getProFechaAprobacion() {
        return proFechaAprobacion;
    }

    public void setProFechaAprobacion(Date proFechaAprobacion) {
        this.proFechaAprobacion = proFechaAprobacion;
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }
    public Integer getProEstado() {
        return proEstado;
    }

    public void setProEstado(Integer proEstado) {
        this.proEstado = proEstado;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (proId != null ? proId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Programacion)) {
            return false;
        }
        Programacion other = (Programacion) object;
        if ((this.proId == null && other.proId != null) || (this.proId != null && !this.proId.equals(other.proId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.Programacion[ proId=" + proId + " ]";
    }

    /**
     * @return the funcionarioGenera
     */
    public Funcionario getFuncionarioGenera() {
        return funcionarioGenera;
    }

    /**
     * @param funcionarioGenera the funcionarioGenera to set
     */
    public void setFuncionarioGenera(Funcionario funcionarioGenera) {
        this.funcionarioGenera = funcionarioGenera; 
    }
    
}
