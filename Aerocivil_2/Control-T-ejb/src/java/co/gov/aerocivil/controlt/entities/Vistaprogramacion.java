/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import co.gov.aerocivil.controlt.util.StringDateUtil;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Viviana
 */
@Entity
@Table(name = "VISTAPROGRAMACION")
@NamedQueries({
    @NamedQuery(name = "Vistaprogramacion.findAll", query = "SELECT v FROM Vistaprogramacion v")})
public class Vistaprogramacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id    
    @Column(name = "TUR_ID")
    private Long turId;
    @Column(name = "FUN_ID")
    private Long funId;
    @Column(name = "FUN_GRADO")
    private Long funGrado;
    @Column(name = "FUN_ALIAS")
    private String funAlias;
   
    @Column(name = "TUR_POSICION_JORNADA")
    private Long turPosicionJornada;
    @Column(name = "TUR_TIPO")
    private Long turTipo;
    @Column(name = "TUR_H_INICIO")
    private Long turHInicio;
    @Column(name = "TUR_H_FIN")
    private Long turHFin;
    @Column(name = "TUR_ESTADO")
    private Long turEstado;
    @Column(name = "TUR_TURNO_ORIGINAL")
    private Long turTurnoOriginal;
    @Column(name = "TUR_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date turFecha;
    @Column(name = "TUR_M_INICIO")
    private Long turMInicio;
    @Column(name = "TUR_M_FIN")
    private Long turMFin;
    @Column(name = "PJ_ID")
    private Long pjId;
    @Column(name = "PJ_ALIAS")
    private String pjAlias;
    @Column(name = "PJ_JORNADA_OP")
    private Long jornadaId;

    
    @JoinColumn(name = "TUR_PROGRAMACION", referencedColumnName = "PRO_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Programacion programacion;
    
    @Transient
    private Long[] tipos;

    public Long[] getTipos() {
        return tipos;
    }

    public void setTipos(Long[] tipos) {
        this.tipos = tipos;
    }

    

    @OneToOne(mappedBy="turno") 
    private DiarioPosicion diarioPosicion;

    public DiarioPosicion getDiarioPosicion() {
        return diarioPosicion;
    }

    public void setDiarioPosicion(DiarioPosicion diarioPosicion) {
        this.diarioPosicion = diarioPosicion;
    }
   
    public Vistaprogramacion() {
    }

    public Vistaprogramacion(Long turId, String funAlias, String pjAlias) {
        this.turId = turId;
        this.funAlias = funAlias;
        this.pjAlias = pjAlias;
    }

    public Vistaprogramacion(Long turId, Long funId, Long funGrado, String funAlias, Long turTipo, Date turFecha, String pjAlias, DiarioPosicion diarioPosicion) {
        this.turId = turId;
        this.funId = funId;
        this.funGrado = funGrado;
        this.funAlias = funAlias;
        this.turTipo = turTipo;
        this.turFecha = turFecha;
        this.pjAlias = pjAlias;
        this.diarioPosicion = diarioPosicion;
    }

    public Long getFunId() {
        return funId;
    }

    public void setFunId(Long funId) {
        this.funId = funId;
    }

    public Long getFunGrado() {
        return funGrado;
    }

    public void setFunGrado(Long funGrado) {
        this.funGrado = funGrado;
    }

    public String getFunAlias() {
        return funAlias;
    }

    public void setFunAlias(String funAlias) {
        this.funAlias = funAlias;
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

    public Long getPjId() {
        return pjId;
    }

    public void setPjId(Long pjId) {
        this.pjId = pjId;
    }

    public String getPjAlias() {
        return pjAlias;
    }

    public void setPjAlias(String pjAlias) {
        this.pjAlias = pjAlias;
    }
    
     public Programacion getProgramacion() {
        return programacion;
    }

    public void setProgramacion(Programacion programacion) {
        this.programacion = programacion;
    }

    public Long getJornadaId() {
        return jornadaId;
    }

    public void setJornadaId(Long jornadaId) {
        this.jornadaId = jornadaId;
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
        if (!(object instanceof Vistaprogramacion)) {
            return false;
        }
        Vistaprogramacion other = (Vistaprogramacion) object;
        if ((this.turId == null && other.turId != null) || (this.turId != null && !this.turId.equals(other.turId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy/MM/dd").format(turFecha);
    }
    
    
}
