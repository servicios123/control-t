/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "DIARIO_POSICIONES_INDIVIDUAL")
@NamedQueries({
    @NamedQuery(name = "DiarioPosicionesIndividualVista.findAll", query = "SELECT d FROM DiarioPosicionesIndividualVista d")})
public class DiarioPosicionesIndividualVista implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "TUR_ID")
    private Long id;
    @Column(name = "DIA")
    private Integer dia;
    @Column(name = "JORN")
    private String jorn;
    @Column(name = "PJ_ALIAS")
    private String pjAlias;
    @Column(name = "HORAS")
    private Integer horas;
    @Column(name = "DPOS_TIPO_REALIZACION")
    private String dposTipoRealizacion;
    @Column(name = "FUN_REALIZA")
    private String funRealiza;
    @Column(name = "DPOS_MIN_RETARDO")
    private Integer dposMinRetardo;
    @Column(name = "TUR_TIPO")
    private Integer turTipo;
    @Column(name = "TUR_ESTADO")
    private Integer turEstado;
    @Column(name = "DPOS_OBSERVACIONES")
    private String dposObservaciones;
    
    
    @Column(name = "FUN_ID")
    private Long funcionario;

    @Column(name = "TUR_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;    
    
    @Column(name = "supervisor")
    private String supervisor;

    public DiarioPosicionesIndividualVista() {
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public String getJorn() {
        return jorn;
    }

    public void setJorn(String jorn) {
        this.jorn = jorn;
    }

    public String getPjAlias() {
        return pjAlias;
    }

    public void setPjAlias(String pjAlias) {
        this.pjAlias = pjAlias;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }

    public String getDposTipoRealizacion() {
        return dposTipoRealizacion;
    }

    public void setDposTipoRealizacion(String dposTipoRealizacion) {
        this.dposTipoRealizacion = dposTipoRealizacion;
    }

    public String getFunRealiza() {
        return funRealiza;
    }

    public void setFunRealiza(String funRealiza) {
        this.funRealiza = funRealiza;
    }

    public Integer getDposMinRetardo() {
        return dposMinRetardo;
    }

    public void setDposMinRetardo(Integer dposMinRetardo) {
        this.dposMinRetardo = dposMinRetardo;
    }

    public Integer getTurTipo() {
        return turTipo;
    }

    public void setTurTipo(Integer turTipo) {
        this.turTipo = turTipo;
    }

    public Integer getTurEstado() {
        return turEstado;
    }

    public void setTurEstado(Integer turEstado) {
        this.turEstado = turEstado;
    }

    public String getDposObservaciones() {
        return dposObservaciones;
    }

    public void setDposObservaciones(String dposObservaciones) {
        this.dposObservaciones = dposObservaciones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Long funcionario) {
        this.funcionario = funcionario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
    
}
