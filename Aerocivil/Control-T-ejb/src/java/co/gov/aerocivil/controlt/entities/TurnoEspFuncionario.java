/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;

import java.util.Date;
import javax.annotation.Generated;
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
import javax.persistence.Transient;

/**
 *
 * @author Viviana
 */
@Entity
@Table(name = "TURNO_ESP_FUNCIONARIO")
@NamedQueries({
    @NamedQuery(name = "TurnoEspFuncionario.findAll", query = "SELECT t FROM TurnoEspFuncionario t"),
    @NamedQuery(name = "TurnoEspFuncionario.findByTefId", query = "SELECT t FROM TurnoEspFuncionario t WHERE t.tefId = :tefId"),
    @NamedQuery(name = "TurnoEspFuncionario.findByTefFini", query = "SELECT t FROM TurnoEspFuncionario t WHERE t.tefFini = :tefFini"),
    @NamedQuery(name = "TurnoEspFuncionario.findByTefFfin", query = "SELECT t FROM TurnoEspFuncionario t WHERE t.tefFfin = :tefFfin"),
    @NamedQuery(name = "TurnoEspFuncionario.findByFuncionario", query = "SELECT t FROM TurnoEspFuncionario t WHERE t.funcionario.funId = :funId order by t.tefFini desc"),
    @NamedQuery(name = "TurnoEspFuncionario.findRepeat", query = "SELECT t FROM TurnoEspFuncionario t WHERE t.funcionario.funId = :funId and t.turnoEspecial.teId = :teId and t.tefFini = :ini and t.tefFfin = :fin "),
    @NamedQuery(name = "TurnoEspFuncionario.findByFechaRango", query = "SELECT t FROM TurnoEspFuncionario t WHERE t.funcionario.funId = :funId and ((:fechaIni BETWEEN  t.tefFini and t.tefFfin) or (:fechaFin BETWEEN  t.tefFini and t.tefFfin))"),
    @NamedQuery(name = "TurnoEspFuncionario.findByRango", query = "SELECT t FROM TurnoEspFuncionario t WHERE t.funcionario.funId = :funId and ((t.tefFini BETWEEN  :fechaIni and :fechaFin) or (t.tefFfin between :fechaIni and :fechaFin)) "),
    @NamedQuery(name = "TurnoEspFuncionario.findByTefEstado", query = "SELECT t FROM TurnoEspFuncionario t WHERE t.tefEstado = :tefEstado")})
public class TurnoEspFuncionario implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "TEF_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TURNO_ESPECIAL_FUN")
    @SequenceGenerator(name = "SEQ_TURNO_ESPECIAL_FUN", sequenceName = "SEQ_TURNO_ESPECIAL_FUN", allocationSize = 1)
    private Long tefId;
    @Basic(optional = false)
    @Column(name = "GROUP_ID")
    private Long groupId;
    @Basic(optional = false)
    @Column(name = "TEF_FINI")
    @Temporal(TemporalType.DATE)
    private Date tefFini;
    @Basic(optional = false)
    @Column(name = "TEF_FFIN")
    @Temporal(TemporalType.DATE)
    private Date tefFfin;
    @Basic(optional = false)
    @Column(name = "TEF_ESTADO")
    private String tefEstado;
    @JoinColumn(name = "TEF_FUNCIONARIO", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionario;
    @JoinColumn(name = "TEF_TURNO_ESPECIAL", referencedColumnName = "TE_ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private TurnoEspecial turnoEspecial;
    
    @OneToOne(mappedBy="turnoEspFuncionario")  //,cascade=CascadeType.ALL
    private Comision comision;
    
    @Transient
    private boolean editable=true;
    
    @Transient
    private boolean anullable;
    
    @Transient
    private String accion;

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }
    
        

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    
    public Comision getComision() {
        return comision;
    }

    public void setComision(Comision comision) {
        this.comision = comision;
    }

    public TurnoEspFuncionario() {
    }

    public TurnoEspFuncionario(Long tefId) {
        this.tefId = tefId;
    }

    public TurnoEspFuncionario(Long tefId, Date tefFini, Date tefFfin, String tefEstado) {
        this.tefId = tefId;
        this.tefFini = tefFini;
        this.tefFfin = tefFfin;
        this.tefEstado = tefEstado;
    }

    public Long getTefId() {
        return tefId;
    }

    public void setTefId(Long tefId) {
        this.tefId = tefId;
    }

    public Date getTefFini() {
        return tefFini;
    }

    public void setTefFini(Date tefFini) {
        this.tefFini = tefFini;
    }

    public Date getTefFfin() {
        return tefFfin;
    }

    public void setTefFfin(Date tefFfin) {
        this.tefFfin = tefFfin;
    }

    public String getTefEstado() {
        return tefEstado;
    }

    public void setTefEstado(String tefEstado) {
        this.tefEstado = tefEstado;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public TurnoEspecial getTurnoEspecial() {
        return turnoEspecial;
    }

    public void setTurnoEspecial(TurnoEspecial turnoEspecial) {
        this.turnoEspecial = turnoEspecial;
    }

    public boolean isAnullable() {
        return anullable;
    }

    public void setAnullable(boolean anullable) {
        this.anullable = anullable;
    }
        
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tefId != null ? tefId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TurnoEspFuncionario)) {
            return false;
        }
        TurnoEspFuncionario other = (TurnoEspFuncionario) object;
        if ((this.tefId == null && other.tefId != null) || (this.tefId != null && !this.tefId.equals(other.tefId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.TurnoEspFuncionario[ tefId=" + tefId + " ]";
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

}
