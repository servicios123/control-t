/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name="DIARIO_SENAL_FUNCIONARIO")
public class DiarioSenalFuncionario implements Serializable {
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
    @Column(name = "DSF_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DIARIO_SENAL_FUNC")
    @SequenceGenerator(name = "SEQ_DIARIO_SENAL_FUNC", sequenceName = "SEQ_DIARIO_SENAL_FUNC", allocationSize = 1)
    private Long dsfId;

    @Column(name="DSF_HORA_ENTRADA")
    @Basic(optional = false)
    private String dsfHoraEntrada;
    
    @Column(name="DSF_HORA_SALIDA")    
    private String dsfHoraSalida;
    
    @Column(name="DSF_HORA_RECESO")    
    private String dsfHoraReceso;
    
    @Column(name="DSF_HORA_REGRESO")    
    private String dsfHoraRegreso;
    
    @Column(name="DSF_OBSERVACION_ENTRADA")    
    private String dsfObservacionEntrada;
    
    @Column(name="DSF_OBSERVACION_SALIDA")    
    private String dsfObservacionSalida;
    
    
    
    @Column(name="DSF_ESTADO")
    @Basic(optional = false)
    private String dsfEstado;    
    
    @JoinColumn(name = "DSF_DIARIO_SENAL_ESPECIAL", referencedColumnName = "DSE_ID")
    @ManyToOne(optional = false)
    private DiarioSenalEspecial dsfDiarioSenalEspecial;
    
    @JoinColumn(name = "DSF_POSICION", referencedColumnName = "POS_ID")
    @ManyToOne(optional = false)
    private Posicion dsfPosicion;
    
    @JoinColumn(name = "DSF_FUNCIONARIO", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false)
    private Funcionario dsfFuncionario;
    
    
    
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getDsfId() != null ? getDsfId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the dsfId fields are not set
        if (!(object instanceof DiarioSenalFuncionario)) {
            return false;
        }
        DiarioSenalFuncionario other = (DiarioSenalFuncionario) object;
        if ((this.getDsfId() == null && other.getDsfId() != null) || (this.getDsfId() != null && !this.dsfId.equals(other.dsfId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.DiarioSenalFuncionario[ id=" + getDsfId() + " ]";
    }

    /**
     * @return the dsfId
     */
    public Long getDsfId() {
        return dsfId;
    }

    /**
     * @param dsfId the dsfId to set
     */
    public void setDsfId(Long dsfId) {
        this.dsfId = dsfId;
    }

    /**
     * @return the dsfHoraEntrada
     */
    public String getDsfHoraEntrada() {
        return dsfHoraEntrada;
    }

    /**
     * @param dsfHoraEntrada the dsfHoraEntrada to set
     */
    public void setDsfHoraEntrada(String dsfHoraEntrada) {
        this.dsfHoraEntrada = dsfHoraEntrada;
    }

    /**
     * @return the dsfHoraSalida
     */
    public String getDsfHoraSalida() {
        return dsfHoraSalida;
    }

    /**
     * @param dsfHoraSalida the dsfHoraSalida to set
     */
    public void setDsfHoraSalida(String dsfHoraSalida) {
        this.dsfHoraSalida = dsfHoraSalida;
    }

    /**
     * @return the dsfHoraReceso
     */
    public String getDsfHoraReceso() {
        return dsfHoraReceso;
    }

    /**
     * @param dsfHoraReceso the dsfHoraReceso to set
     */
    public void setDsfHoraReceso(String dsfHoraReceso) {
        this.dsfHoraReceso = dsfHoraReceso;
    }

    /**
     * @return the dsfHoraRegreso
     */
    public String getDsfHoraRegreso() {
        return dsfHoraRegreso;
    }

    /**
     * @param dsfHoraRegreso the dsfHoraRegreso to set
     */
    public void setDsfHoraRegreso(String dsfHoraRegreso) {
        this.dsfHoraRegreso = dsfHoraRegreso;
    }

    /**
     * @return the dsfEstado
     */
    public String getDsfEstado() {
        return dsfEstado;
    }

    /**
     * @param dsfEstado the dsfEstado to set
     */
    public void setDsfEstado(String dsfEstado) {
        this.dsfEstado = dsfEstado;
    }

    /**
     * @return the dsfDiarioSenalEspecial
     */
    public DiarioSenalEspecial getDsfDiarioSenalEspecial() {
        return dsfDiarioSenalEspecial;
    }

    /**
     * @param dsfDiarioSenalEspecial the dsfDiarioSenalEspecial to set
     */
    public void setDsfDiarioSenalEspecial(DiarioSenalEspecial dsfDiarioSenalEspecial) {
        this.dsfDiarioSenalEspecial = dsfDiarioSenalEspecial;
    }

    /**
     * @return the dsfPosicion
     */
    public Posicion getDsfPosicion() {
        return dsfPosicion;
    }

    /**
     * @param dsfPosicion the dsfPosicion to set
     */
    public void setDsfPosicion(Posicion dsfPosicion) {
        this.dsfPosicion = dsfPosicion;
    }

    /**
     * @return the dsfFuncionario
     */
    public Funcionario getDsfFuncionario() {
        return dsfFuncionario;
    }

    /**
     * @param dsfFuncionario the dsfFuncionario to set
     */
    public void setDsfFuncionario(Funcionario dsfFuncionario) {
        this.dsfFuncionario = dsfFuncionario;
    }

    /**
     * @return the dsfObservacionEntrada
     */
    public String getDsfObservacionEntrada() {
        return dsfObservacionEntrada;
    }

    /**
     * @param dsfObservacionEntrada the dsfObservacionEntrada to set
     */
    public void setDsfObservacionEntrada(String dsfObservacionEntrada) {
        this.dsfObservacionEntrada = dsfObservacionEntrada;
    }

    /**
     * @return the dsfObservacionSalida
     */
    public String getDsfObservacionSalida() {
        return dsfObservacionSalida;
    }

    /**
     * @param dsfObservacionSalida the dsfObservacionSalida to set
     */
    public void setDsfObservacionSalida(String dsfObservacionSalida) {
        this.dsfObservacionSalida = dsfObservacionSalida;
    }
    
}
