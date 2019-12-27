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
@Table(name = "EVALUACION_COMPETENCIA")
@NamedQueries({
    @NamedQuery(name = "EvaluacionCompetencia.findAll", query = "SELECT e FROM EvaluacionCompetencia e"),
    @NamedQuery(name = "EvaluacionCompetencia.findByEvId", query = "SELECT e FROM EvaluacionCompetencia e WHERE e.evId = :evId"),
    @NamedQuery(name = "EvaluacionCompetencia.findByEvDescripcion", query = "SELECT e FROM EvaluacionCompetencia e WHERE e.evDescripcion = :evDescripcion"),
    @NamedQuery(name = "EvaluacionCompetencia.findByEvResultado", query = "SELECT e FROM EvaluacionCompetencia e WHERE e.evResultado = :evResultado"),
    @NamedQuery(name = "EvaluacionCompetencia.findByEvFechaRealiza", query = "SELECT e FROM EvaluacionCompetencia e WHERE e.evFechaRealiza = :evFechaRealiza"),
    @NamedQuery(name = "EvaluacionCompetencia.findByEvFechaVence", query = "SELECT e FROM EvaluacionCompetencia e WHERE e.evFechaVence = :evFechaVence")})
public class EvaluacionCompetencia implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "EV_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EVALUACION_COMPETENCIAS")
    @SequenceGenerator(name = "SEQ_EVALUACION_COMPETENCIAS", sequenceName = "SEQ_EVALUACION_COMPETENCIAS", allocationSize = 1)
    private Long evId;
    @Column(name = "EV_DESCRIPCION")
    private String evDescripcion;
    @Column(name = "EV_RESULTADO")
    private String evResultado;
    @Column(name = "EV_FECHA_REALIZA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date evFechaRealiza;
    @Column(name = "EV_FECHA_VENCE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date evFechaVence;

    @Transient
    public String getEvFechaVenceStr() {
        return evFechaVence.toString();
    }

    @JoinColumn(name = "EV_FUNCIONARIO", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionario;

    public EvaluacionCompetencia() {
    }

    public Long getEvId() {
        return evId;
    } 

    public void setEvId(Long evId) {
        this.evId = evId;
    }

    public String getEvDescripcion() {
        return evDescripcion;
    }

    public void setEvDescripcion(String evDescripcion) {
        this.evDescripcion = evDescripcion;
    }

    public String getEvResultado() {
        return evResultado;
    }

    public void setEvResultado(String evResultado) {
        this.evResultado = evResultado;
    }

    public Date getEvFechaRealiza() {
        return evFechaRealiza;
    }

    public void setEvFechaRealiza(Date evFechaRealiza) {
        this.evFechaRealiza = evFechaRealiza;
    }

    public Date getEvFechaVence() {
        return evFechaVence;
    }

    public void setEvFechaVence(Date evFechaVence) {
        this.evFechaVence = evFechaVence;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evId != null ? evId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionCompetencia)) {
            return false;
        }
        EvaluacionCompetencia other = (EvaluacionCompetencia) object;
        if ((this.evId == null && other.evId != null) || (this.evId != null && !this.evId.equals(other.evId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.EvaluacionCompetencia[ evId=" + evId + " ]";
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    
}
