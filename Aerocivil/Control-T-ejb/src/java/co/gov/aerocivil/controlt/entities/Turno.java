/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import co.gov.aerocivil.controlt.util.StringDateUtil;
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
@Table(name = "TURNO")
@NamedQueries({
    @NamedQuery(name = "Turno.findAll", query = "SELECT t FROM Turno t"),
@NamedQuery(name = "Turno.hrex", query = "SELECT t FROM Turno t where t.funcionario.funAlias = :alias and t.programacion.proId = :proId and t.turTipo = 2")})
public class Turno implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
   
    
    @Id
    @Basic(optional = false)
    @Column(name = "TUR_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TURNO")
    @SequenceGenerator(name = "SEQ_TURNO", sequenceName = "SEQ_TURNO", allocationSize = 1)
    private Long turId;
    
    @Column(name = "TUR_POSICION_JORNADA")
    private Long turPosicionJornada;
    @Column(name = "TUR_TIPO")
    private Long turTipo;
    @Basic(optional = false)
    @Column(name = "TUR_H_INICIO")
    private Long turHInicio;
    @Basic(optional = false)
    @Column(name = "TUR_H_FIN")
    private Long turHFin;
    @Column(name = "TUR_ESTADO")
    private Long turEstado;
    @Column(name = "TUR_TURNO_ORIGINAL")
    private Long turTurnoOriginal;
    @Basic(optional = false)
    @Column(name = "TUR_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date turFecha;
    @Column(name = "TUR_M_INICIO")
    private Long turMInicio;
    @Column(name = "TUR_M_FIN")
    private Long turMFin;

    @JoinColumn(name = "TUR_FUNCIONARIO", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionario;

    
    @JoinColumn(name = "TUR_PROGRAMACION", referencedColumnName = "PRO_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Programacion programacion;

  
    
    
    public Turno() {
    }

    public Turno(Long turId) {
        this.turId = turId;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
    
      public Programacion getProgramacion() {
        return programacion;
    }

    public void setProgramacion(Programacion programacion) {
        this.programacion = programacion;
    }
    
    public Turno(Long turId, Long turHInicio, Long turHFin, Date turFecha) {
        this.turId = turId;
        this.turHInicio = turHInicio;
        this.turHFin = turHFin;
        this.turFecha = turFecha;
    }

    public Long getTurId() {
        return turId;
    }

    public void setTurId(Long turId) {
        this.turId = turId;
    }

    public Long getTurPosicionJornada() {
        return turPosicionJornada;
    }

    public void setTurPosicionJornada(Long turPosicionJornada) {
        this.turPosicionJornada = turPosicionJornada;
    }

    public Long getTurTipo() {
        return turTipo;
    }

    public void setTurTipo(Long turTipo) {
        this.turTipo = turTipo;
    }

    public Long getTurHInicio() {
        return turHInicio;
    }

    public void setTurHInicio(Long turHInicio) {
        this.turHInicio = turHInicio;
    }

    public Long getTurHFin() {
        return turHFin;
    }

    public void setTurHFin(Long turHFin) {
        this.turHFin = turHFin;
    }

    public Long getTurEstado() {
        return turEstado;
    }

    public void setTurEstado(Long turEstado) {
        this.turEstado = turEstado;
    }

    public Long getTurTurnoOriginal() {
        return turTurnoOriginal;
    }

    public void setTurTurnoOriginal(Long turTurnoOriginal) {
        this.turTurnoOriginal = turTurnoOriginal;
    }

    public Date getTurFecha() {
        return turFecha;
    }

    public void setTurFecha(Date turFecha) {
        this.turFecha = turFecha;
    }

    public Long getTurMInicio() {
        return turMInicio;
    }

    public void setTurMInicio(Long turMInicio) {
        this.turMInicio = turMInicio;
    }

    public Long getTurMFin() {
        return turMFin;
    }

    public void setTurMFin(Long turMFin) {
        this.turMFin = turMFin;
    }
    
    public String getHoraInicioStr(){
        return StringDateUtil.getFormatoHora(this.turHInicio.byteValue(), this.turMInicio.byteValue());
    }
    
    public String getHoraFinStr(){
        return StringDateUtil.getFormatoHora(this.turHFin.byteValue(), this.turMFin.byteValue());
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (turId != null ? turId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Turno)) {
            return false;
        }
        Turno other = (Turno) object;
        if ((this.turId == null && other.turId != null) || (this.turId != null && !this.turId.equals(other.turId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.Turno[ turId=" + turId + " ]";
    }
    
}
