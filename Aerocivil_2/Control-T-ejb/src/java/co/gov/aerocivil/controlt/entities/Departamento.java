/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Viviana
 */
@Entity
@Table(name = "DEPARTAMENTO")
@NamedQueries({
    @NamedQuery(name = "Departamento.findAll", query = "SELECT d FROM Departamento d"),
    @NamedQuery(name = "Departamento.findById", query = "SELECT d FROM Departamento d WHERE d.deptoId = :deptoId"),
    @NamedQuery(name = "Departamento.findByDeptoNombre", query = "SELECT d FROM Departamento d WHERE d.deptoNombre = :deptoNombre")})
public class Departamento implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "DEPTO_ID")
    private Long deptoId;
    @Basic(optional = false)
    @Column(name = "DEPTO_NOMBRE")
    private String deptoNombre;

    public Departamento() {
    }

    public String getDeptoNombre() {
        return deptoNombre;
    }

    public void setDeptoNombre(String deptoNombre) {
        this.deptoNombre = deptoNombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deptoId != null ? deptoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Departamento)) {
            return false;
        }
        Departamento other = (Departamento) object;
        if ((this.deptoId == null && other.deptoId != null) || (this.deptoId != null && !this.deptoId.equals(other.deptoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.Departamento[ deptoId=" + deptoId + " ]";
    }

    public Long getDeptoId() {
        return deptoId;
    }

    public void setDeptoId(Long deptoId) {
        this.deptoId = deptoId;
    }
}
