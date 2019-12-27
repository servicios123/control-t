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

/**
 *
 * @author Viviana
 */
@Entity
@Table(name = "SOLICITUD")
@NamedQueries({
    @NamedQuery(name = "Solicitud.findAll", query = "SELECT s FROM Solicitud s"),
    @NamedQuery(name = "Solicitud.findBySolId", query = "SELECT s FROM Solicitud s WHERE s.solId = :solId"),
    @NamedQuery(name = "Solicitud.findBySolFechaCambio", query = "SELECT s FROM Solicitud s WHERE s.solFechaCambio = :solFechaCambio"),
    @NamedQuery(name = "Solicitud.findBySolEstado", query = "SELECT s FROM Solicitud s WHERE s.solEstado = :solEstado"),
    @NamedQuery(name = "Solicitud.findBySolTipo", query = "SELECT s FROM Solicitud s WHERE s.solTipo = :solTipo"),
    @NamedQuery(name = "Solicitud.findBySolFechaPago", query = "SELECT s FROM Solicitud s WHERE s.solFechaPago = :solFechaPago"),
    @NamedQuery(name = "Solicitud.findBySolFechaRegistro", query = "SELECT s FROM Solicitud s WHERE s.solFechaRegistro = :solFechaRegistro")})
public class Solicitud implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    
    @Id
    @Basic(optional = false)
    @Column(name = "SOL_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SOLICITUD")
    @SequenceGenerator(name = "SEQ_SOLICITUD", sequenceName = "SEQ_SOLICITUD", allocationSize = 1)
    private Long solId;
    
    @Column(name = "SOL_FECHA_CAMBIO")
    @Temporal(TemporalType.DATE)
    private Date solFechaCambio;
    @Column(name = "SOL_ESTADO")
    private String solEstado;
    @Column(name = "SOL_TIPO")
    private String solTipo;
    @Column(name = "SOL_FECHA_PAGO")
    @Temporal(TemporalType.DATE)
    private Date solFechaPago;
    @Column(name = "SOL_FECHA_REGISTRO")
    @Temporal(TemporalType.DATE)
    private Date solFechaRegistro;
    
     @Column(name = "SOL_FECHA_APROBACION")
    @Temporal(TemporalType.DATE)
    private Date solFechaAprobacion;

         
    @JoinColumn(name = "SOL_FUNCIONARIO_SOL", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionario_sol;
    
    @JoinColumn(name = "SOL_FUNCIONARIO_REEM", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionario_reem;
    
     @JoinColumn(name = "SOL_APROBADO_POR", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario solAprobadoPor;

  
    
    @JoinColumn(name = "SOL_TURNO", referencedColumnName = "PJ_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PosicionJornada turno;
    
    /*@JoinColumn(name = "SOL_TURNO_PAGO", referencedColumnName = "PJ_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PosicionJornada turno_pago;*/
    
    @JoinColumn(name = "SOL_DEPENDENCIA", referencedColumnName = "DEP_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Dependencia dependencia;

    

   

    public Solicitud() {
    }

    public Solicitud(Long solId) {
        this.solId = solId;
    }

    public Long getSolId() {
        return solId;
    }

    public void setSolId(Long solId) {
        this.solId = solId;
    }

    public Date getSolFechaCambio() {
        return solFechaCambio;
    }

    public void setSolFechaCambio(Date solFechaCambio) {
        this.solFechaCambio = solFechaCambio;
    }

    public String getSolEstado() {
        return solEstado;
    }

    public void setSolEstado(String solEstado) {
        this.solEstado = solEstado;
    }

    

    public String getSolTipo() {
        return solTipo;
    }

    public void setSolTipo(String solTipo) {
        this.solTipo = solTipo;
    }

    public Date getSolFechaPago() {
        return solFechaPago;
    }

    public void setSolFechaPago(Date solFechaPago) {
        this.solFechaPago = solFechaPago;
    }

    public Date getSolFechaRegistro() {
        return solFechaRegistro;
    }

    public void setSolFechaRegistro(Date solFechaRegistro) {
        this.solFechaRegistro = solFechaRegistro;
    }

     public Funcionario getFuncionario_sol() {
        return funcionario_sol;
    }

    public void setFuncionario_sol(Funcionario funcionario_sol) {
        this.funcionario_sol = funcionario_sol;
    }

    public Funcionario getFuncionario_reem() {
        return funcionario_reem;
    }

    public void setFuncionario_reem(Funcionario funcionario_reem) {
        this.funcionario_reem = funcionario_reem;
    }

    public PosicionJornada getTurno() {
        return turno;
    }

    public void setTurno(PosicionJornada turno) {
        this.turno = turno;
    }

    /*public PosicionJornada getTurno_pago() {
        return turno_pago;
    }

    public void setTurno_pago(PosicionJornada turno_pago) {
        this.turno_pago = turno_pago;
    }*/
    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }
    public Date getSolFechaAprobacion() {
        return solFechaAprobacion;
    }

    public void setSolFechaAprobacion(Date solFechaAprobacion) {
        this.solFechaAprobacion = solFechaAprobacion;
    }
    
      public Funcionario getSolAprobadoPor() {
        return solAprobadoPor;
    }

    public void setSolAprobadoPor(Funcionario solAprobadoPor) {
        this.solAprobadoPor = solAprobadoPor;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (solId != null ? solId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Solicitud)) {
            return false;
        }
        Solicitud other = (Solicitud) object;
        if ((this.solId == null && other.solId != null) || (this.solId != null && !this.solId.equals(other.solId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.Solicitud[ solId=" + solId + " ]";
    }
    
}
