/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import co.gov.aerocivil.controlt.util.StringDateUtil;
import java.io.Serializable;

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

/**
 *
 * @author Viviana
 */
@Entity
@Table(name = "TURNO_ESPECIAL")
@NamedQueries({
    @NamedQuery(name = "TurnoEspecial.findAll", query = "SELECT t FROM TurnoEspecial t"),
    @NamedQuery(name = "TurnoEspecial.findByTeId", query = "SELECT t FROM TurnoEspecial t WHERE t.teId = :teId"),
    @NamedQuery(name = "TurnoEspecial.findByTeNombre", query = "SELECT t FROM TurnoEspecial t WHERE t.teNombre = :teNombre"),
    @NamedQuery(name = "TurnoEspecial.findByTeHinicio", query = "SELECT t FROM TurnoEspecial t WHERE t.teHinicio = :teHinicio"),
    @NamedQuery(name = "TurnoEspecial.findByTeMinicio", query = "SELECT t FROM TurnoEspecial t WHERE t.teMinicio = :teMinicio"),
    @NamedQuery(name = "TurnoEspecial.findByTeHfin", query = "SELECT t FROM TurnoEspecial t WHERE t.teHfin = :teHfin"),
    @NamedQuery(name = "TurnoEspecial.findByTeMfin", query = "SELECT t FROM TurnoEspecial t WHERE t.teMfin = :teMfin"),
    @NamedQuery(name = "TurnoEspecial.findByTeEstado", query = "SELECT t FROM TurnoEspecial t WHERE t.teEstado = :teEstado"),
    @NamedQuery(name = "TurnoEspecial.findByTeSigla", query = "SELECT t FROM TurnoEspecial t WHERE t.teSigla = :teSigla and t.dependencia.depId = :depId"),
    @NamedQuery(name = "TurnoEspecial.findByDependencia", query = "SELECT t FROM TurnoEspecial t WHERE t.dependencia.depId = :dep order by t.teSigla asc"),
    @NamedQuery(name = "TurnoEspecial.findByDependenciaAndEstado", query = "SELECT t FROM TurnoEspecial t WHERE t.dependencia.depId = :dep and t.teEstado = :teEstado order by t.teSigla asc")})
public class TurnoEspecial implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "TE_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TURNOESPECIAL")
    @SequenceGenerator(name = "SEQ_TURNOESPECIAL", sequenceName = "SEQ_TURNOESPECIAL", allocationSize = 1)
    private Long teId;
    @Basic(optional = false)
    @Column(name = "TE_NOMBRE")
    private String teNombre;
    @Basic(optional = false)
    @Column(name = "TE_HINICIO")
    private Byte teHinicio;
    @Basic(optional = false)
    @Column(name = "TE_MINICIO")
    private Byte teMinicio;
    @Basic(optional = false)
    @Column(name = "TE_HFIN")
    private Byte teHfin;
    @Basic(optional = false)
    @Column(name = "TE_MFIN")
    private Byte teMfin;
    @Basic(optional = false)
    @Column(name = "TE_ESTADO")
    private String teEstado;
    @Basic(optional = false)
    @Column(name = "TE_SIGLA")
    private String teSigla;
    @JoinColumn(name = "TE_DEPENDENCIA", referencedColumnName = "DEP_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Dependencia dependencia;
    @Column(name = "TE_COMISION")
    private Long teComision;
    @Column(name = "TE_HORA_LABORADA")
    private Boolean teHoraLaborada;
    @Column(name = "TE_PERMITE_EXTRAS")
    private Boolean tePermiteExtras;

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }
    /*public TurnoEspecial() {
     }

     public TurnoEspecial(Long teId) {
     this.teId = teId;
     }

     public TurnoEspecial(Long teId, String teNombre, Long teHinicio, Long teMinicio, Long teHfin, Long teMfin, String teEstado) {
     this.teId = teId;
     this.teNombre = teNombre;
     this.teHinicio = teHinicio;
     this.teMinicio = teMinicio;
     this.teHfin = teHfin;
     this.teMfin = teMfin;
     this.teEstado = teEstado;
     }*/

    public Long getTeId() {
        return teId;
    }

    public void setTeId(Long teId) {
        this.teId = teId;
    }

    public String getTeNombre() {
        return teNombre;
    }

    public void setTeNombre(String teNombre) {
        this.teNombre = teNombre;
    }

    public Byte getTeHinicio() {
        return teHinicio;
    }

    public void setTeHinicio(Byte teHinicio) {
        this.teHinicio = teHinicio;
    }

    public Byte getTeMinicio() {
        return teMinicio;
    }

    public void setTeMinicio(Byte teMinicio) {
        this.teMinicio = teMinicio;
    }

    public Byte getTeHfin() {
        return teHfin;
    }

    public void setTeHfin(Byte teHfin) {
        this.teHfin = teHfin;
    }

    public Byte getTeMfin() {
        return teMfin;
    }

    public void setTeMfin(Byte teMfin) {
        this.teMfin = teMfin;
    }

    public String getTeEstado() {
        return teEstado;
    }

    public void setTeEstado(String teEstado) {
        this.teEstado = teEstado;
    }

    public String getTeSigla() {
        return teSigla;
    }

    public void setTeSigla(String teSigla) {
        this.teSigla = teSigla;
    }

    public Boolean getTePermiteExtras() {
        return tePermiteExtras;
    }

    public void setTePermiteExtras(Boolean tePermiteExtras) {
        this.tePermiteExtras = tePermiteExtras;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (teId != null ? teId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TurnoEspecial)) {
            return false;
        }
        TurnoEspecial other = (TurnoEspecial) object;
        if ((this.teId == null && other.teId != null) || (this.teId != null && !this.teId.equals(other.teId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.TurnoEspecial[ teId=" + teId + " ]";
    }

    public Long getTeComision() {
        return teComision;
    }

    public void setTeComision(Long teComision) {
        this.teComision = teComision;
    }

    public String getHoraInicioStr() {
        return StringDateUtil.getFormatoHora(this.teHinicio, teMinicio);
    }

    public String getHoraFinStr() {
        return StringDateUtil.getFormatoHora(this.teHfin, teMfin);
    }

    public Boolean getTeHoraLaborada() {
        return teHoraLaborada;
    }

    public void setTeHoraLaborada(Boolean teHoraLaborada) {
        this.teHoraLaborada = teHoraLaborada;
    }
}
