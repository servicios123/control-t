/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

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
 * @author Administrador
 */
@Entity
@Table(name = "RESTRICCION_DEPENDENCIA")
@NamedQueries({
    @NamedQuery(name = "RestriccionDependencia.findAll", query = "SELECT r FROM RestriccionDependencia r"),
    @NamedQuery(name = "RestriccionDependencia.findByRdDependencia", query = "SELECT r FROM RestriccionDependencia r WHERE r.dependencia = :dependencia"),
    @NamedQuery(name = "RestriccionDependencia.findByRdValor", query = "SELECT r FROM RestriccionDependencia r WHERE r.rdValor = :rdValor"),
    @NamedQuery(name = "RestriccionDependencia.findByRpIDDependencia", query = "SELECT r.rdValor FROM RestriccionDependencia r WHERE r.restriccionProgramacion.rpId = :rpID and r.dependencia.depId= :depId ")})
public class RestriccionDependencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "RD_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RESTRICCIONES_DEPENDENCIA")
    @SequenceGenerator(name = "SEQ_RESTRICCIONES_DEPENDENCIA", sequenceName = "SEQ_RESTRICCIONES_DEPENDENCIA", allocationSize = 1)
    private Long rdId;

    @JoinColumn(name = "RD_DEPENDENCIA", referencedColumnName = "DEP_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Dependencia dependencia;

    @Basic(optional = false)
    @Column(name = "RD_VALOR")
    private Long rdValor;
    
    @JoinColumn(name = "RD_RESTRICCION", referencedColumnName = "RP_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private RestriccionProgramacion restriccionProgramacion;

    public Long getRdId() {
        return rdId;
    }

    public void setRdId(Long rdId) {
        this.rdId = rdId;
    }

    public Long getRdValor() {
        return rdValor;
    }

    public void setRdValor(Long rdValor) {
        this.rdValor = rdValor;
    }

    public RestriccionProgramacion getRestriccionProgramacion() {
        return restriccionProgramacion;
    }

    public void setRestriccionProgramacion(RestriccionProgramacion rdRestriccion) {
        this.restriccionProgramacion = rdRestriccion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rdId != null ? rdId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RestriccionDependencia)) {
            return false;
        }
        RestriccionDependencia other = (RestriccionDependencia) object;
        if ((this.rdId == null && other.rdId != null) || (this.rdId != null && !this.rdId.equals(other.rdId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.RestriccionDependencia[ rdId=" + rdId + " ]";
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

}
