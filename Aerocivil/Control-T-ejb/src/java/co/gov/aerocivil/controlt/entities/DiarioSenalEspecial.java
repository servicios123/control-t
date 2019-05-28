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
import javax.persistence.Transient;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name="DIARIO_SENAL_ESPECIAL")
@NamedQueries({
    @NamedQuery(name = "DiarioSenalEspecial.findAll", query = "SELECT d FROM DiarioSenalEspecial d"),
    @NamedQuery(name = "DiarioSenalEspecial.findByDseId", query = "SELECT d FROM DiarioSenalEspecial d WHERE d.dseId = :dseId"),
    @NamedQuery(name = "DiarioSenalEspecial.findByDseFecha", query = "SELECT d FROM DiarioSenalEspecial d WHERE d.dseFecha = :dseFecha"),
    @NamedQuery(name = "DiarioSenalEspecial.findByDseEstado", query = "SELECT d FROM DiarioSenalEspecial d WHERE d.dseEstado = :dseEstado"),
    @NamedQuery(name = "DiarioSenalEspecial.findByDseDependencia", query = "SELECT d FROM DiarioSenalEspecial d WHERE d.dseDependencia.depId = :dseDependencia"),
    @NamedQuery(name = "DiarioSenalEspecial.findByDseJornadaOp", query = "SELECT d FROM DiarioSenalEspecial d WHERE d.dseJornadaOp.joId = :dseJornadaOp")    ,
    @NamedQuery(name = "DiarioSenalEspecial.findByJopFecha", query = "SELECT d FROM DiarioSenalEspecial d WHERE d.dseJornadaOp.joId = :dseJornadaOp and d.dseFecha = :dseFecha")
})

public class DiarioSenalEspecial implements Serializable {
    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @Id
    @Basic(optional = false)
    @Column(name = "DSE_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DIARIO_SENAL_ESPECIAL")
    @SequenceGenerator(name = "SEQ_DIARIO_SENAL_ESPECIAL", sequenceName = "SEQ_DIARIO_SENAL_ESPECIAL", allocationSize = 1)
    private Long dseId;
    
    @Column(name="DSE_FECHA")
    @Basic(optional = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dseFecha;
    
    @Column(name="DSE_HORA_CREADO")
    @Basic(optional = false)
    private String dseHoraCreado;
    
    @Column(name="DSE_ESTADO")
    @Basic(optional = false)
    private String dseEstado;
    
    
    @Column(name="DSE_HORA_CERRADO")
    private String dseHoraCerrado;
    
    @JoinColumn(name = "DSE_FUNCIONARIO_CIERRA", referencedColumnName = "FUN_ID")
    @ManyToOne
    private Funcionario dseFuncionarioCierra;
    
    
    @JoinColumn(name = "DSE_DEPENDENCIA", referencedColumnName = "DEP_ID")
    @ManyToOne(optional = false)
    private Dependencia dseDependencia;
    
    @JoinColumn(name = "DSE_JORNADA_OP", referencedColumnName = "JO_ID")
    @ManyToOne(optional = false)
    private Jornada dseJornadaOp;
    
    @Transient
    private Date dseFechaFinal;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getDseId() != null ? getDseId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the dseId fields are not set
        if (!(object instanceof DiarioSenalEspecial)) {
            return false;
        }
        DiarioSenalEspecial other = (DiarioSenalEspecial) object;
        if ((this.getDseId() == null && other.getDseId() != null) || (this.getDseId() != null && !this.dseId.equals(other.dseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.DiarioSenalEspecial[ id=" + getDseId() + " ]";
    }

    /**
     * @return the dseId
     */
    public Long getDseId() {
        return dseId;
    }

    /**
     * @param dseId the dseId to set
     */
    public void setDseId(Long dseId) {
        this.dseId = dseId;
    }

    /**
     * @return the dseFecha
     */
    public Date getDseFecha() {
        return dseFecha;
    }

    /**
     * @param dseFecha the dseFecha to set
     */
    public void setDseFecha(Date dseFecha) {
        this.dseFecha = dseFecha;
    }

    /**
     * @return the dseHoraCreado
     */
    public String getDseHoraCreado() {
        return dseHoraCreado;
    }

    /**
     * @param dseHoraCreado the dseHoraCreado to set
     */
    public void setDseHoraCreado(String dseHoraCreado) {
        this.dseHoraCreado = dseHoraCreado;
    }

    /**
     * @return the dseHoraCerrado
     */
    public String getDseHoraCerrado() {
        return dseHoraCerrado;
    }

    /**
     * @param dseHoraCerrado the dseHoraCerrado to set
     */
    public void setDseHoraCerrado(String dseHoraCerrado) {
        this.dseHoraCerrado = dseHoraCerrado;
    }

   
    

    /**
     * @return the dseDependencia
     */
    public Dependencia getDseDependencia() {
        return dseDependencia;
    }

    /**
     * @param dseDependencia the dseDependencia to set
     */
    public void setDseDependencia(Dependencia dseDependencia) {
        this.dseDependencia = dseDependencia;
    }

    /**
     * @return the dseJornadaOp
     */
    public Jornada getDseJornadaOp() {
        return dseJornadaOp;
    }

    /**
     * @param dseJornadaOp the dseJornadaOp to set
     */
    public void setDseJornadaOp(Jornada dseJornadaOp) {
        this.dseJornadaOp = dseJornadaOp;
    }

    /**
     * @return the dseEstado
     */
    public String getDseEstado() {
        return dseEstado;
    }

    /**
     * @param dseEstado the dseEstado to set
     */
    public void setDseEstado(String dseEstado) {
        this.dseEstado = dseEstado;
    }

    /**
     * @return the dseFuncionarioCierra
     */
    public Funcionario getDseFuncionarioCierra() {
        return dseFuncionarioCierra;
    }

    /**
     * @param dseFuncionarioCierra the dseFuncionarioCierra to set
     */
    public void setDseFuncionarioCierra(Funcionario dseFuncionarioCierra) {
        this.dseFuncionarioCierra = dseFuncionarioCierra;
    }

    /**
     * @return the dseFechaFinal
     */
    public Date getDseFechaFinal() {
        return dseFechaFinal;
    }

    /**
     * @param dseFechaFinal the dseFechaFinal to set
     */
    public void setDseFechaFinal(Date dseFechaFinal) {
        this.dseFechaFinal = dseFechaFinal;
    }
    
}
