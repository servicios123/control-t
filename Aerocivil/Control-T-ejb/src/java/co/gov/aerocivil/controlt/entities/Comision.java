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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "COMISION")
@NamedQueries({
    @NamedQuery(name = "Comision.findAll", query = "SELECT c FROM Comision c"),
    @NamedQuery(name = "Comision.findByComId", query = "SELECT c FROM Comision c WHERE c.comId = :comId"),
    @NamedQuery(name = "Comision.findByComFechaInicial", query = "SELECT c FROM Comision c WHERE c.comFechaInicial = :comFechaInicial"),
    @NamedQuery(name = "Comision.findByComFechaFinal", query = "SELECT c FROM Comision c WHERE c.comFechaFinal = :comFechaFinal"),
    @NamedQuery(name = "Comision.findByEstado", query = "SELECT c FROM Comision c WHERE c.estado = :estado")})
public class Comision implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "COM_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COMISION")
    @SequenceGenerator(name = "SEQ_COMISION", sequenceName = "SEQ_COMISION", allocationSize = 1)
    private Long comId;

    @JoinColumn(name = "COM_TESP_FUNCIONARIO", referencedColumnName = "TEF_ID")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private TurnoEspFuncionario turnoEspFuncionario;

    @JoinColumn(name = "COM_NVA_DEPENDENCIA", referencedColumnName = "DEP_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Dependencia dependencia;

    @Basic(optional = false)
    @Column(name = "COM_FECHA_INICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date comFechaInicial;
    @Basic(optional = false)
    @Column(name = "COM_FECHA_FINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date comFechaFinal;
    
    @Column(name = "COM_ESTADO")
    private String estado;
    
    @Column(name = "com_h_ini")
    private Byte comHIni;
    @Column(name = "com_m_ini")
    private Byte comMIni;

    @Column(name = "com_h_fin")
    private Byte comHFin;
    @Column(name = "com_m_fin")
    private Byte comMFin;
    

    public Long getComId() {
        return comId;
    }

    public void setComId(Long comId) {
        this.comId = comId;
    }

    public Date getComFechaInicial() {
        return comFechaInicial;
    }

    public void setComFechaInicial(Date comFechaInicial) {
        this.comFechaInicial = comFechaInicial;
    }

    public Date getComFechaFinal() {
        return comFechaFinal;
    }

    public void setComFechaFinal(Date comFechaFinal) {
        this.comFechaFinal = comFechaFinal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (comId != null ? comId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comision)) {
            return false;
        }
        Comision other = (Comision) object;
        if ((this.comId == null && other.comId != null) || (this.comId != null && !this.comId.equals(other.comId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.Comision[ comId=" + comId + " ]";
    }

    public TurnoEspFuncionario getTurnoEspFuncionario() {
        return turnoEspFuncionario;
    }

    public void setTurnoEspFuncionario(TurnoEspFuncionario turnoEspFuncionario) {
        this.turnoEspFuncionario = turnoEspFuncionario;
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Byte getComHIni() {
        return comHIni;
    }

    public void setComHIni(Byte comHIni) {
        this.comHIni = comHIni;
    }

    public Byte getComMIni() {
        return comMIni;
    }

    public void setComMIni(Byte comMIni) {
        this.comMIni = comMIni;
    }

    public Byte getComHFin() {
        return comHFin;
    }

    public void setComHFin(Byte comHFin) {
        this.comHFin = comHFin;
    }

    public Byte getComMFin() {
        return comMFin;
    }

    public void setComMFin(Byte comMFin) {
        this.comMFin = comMFin;
    }
    
}
