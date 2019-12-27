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
 * @author Viviana
 */
@Entity
@Table(name = "DEPENDENCIA")
@NamedQueries({
    @NamedQuery(name = "Dependencia.findAll", query = "SELECT d FROM Dependencia d"),
    @NamedQuery(name = "Dependencia.findById", query = "SELECT d FROM Dependencia d WHERE d.depId = :depId"),
    @NamedQuery(name = "Dependencia.findByDepNombre", query = "SELECT d FROM Dependencia d WHERE d.depNombre = :depNombre"),
    @NamedQuery(name = "Dependencia.findByDepAbreviatura", query = "SELECT d FROM Dependencia d WHERE d.depAbreviatura = :depAbreviatura"),
    @NamedQuery(name = "Dependencia.findByDepAeropuerto", query = "SELECT d FROM Dependencia d WHERE d.aeropuerto.aeId = :depAeropuerto order by d.depNombre"),
    @NamedQuery(name = "Dependencia.findByDepAeropuertoDepCat", query = "SELECT d FROM Dependencia d WHERE d.aeropuerto.aeId = :depAeropuerto and d.depcategoria.dcId = :categoria order by d.depNombre")})
public class Dependencia implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "DEP_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DEPENDENCIA")
    @SequenceGenerator(name = "SEQ_DEPENDENCIA", sequenceName = "SEQ_DEPENDENCIA", allocationSize = 1)
    private Long depId;
    @Basic(optional = false)
    @Column(name = "DEP_NOMBRE")
    private String depNombre;
    
    
    @Basic(optional = false)
    @Column(name = "DEP_ABREVIATURA")
    private String depAbreviatura;
    
    @Basic(optional = false)
    @Column(name = "DEP_AVISO_VENCIMIENTO")
    private Long depAvisoVencimiento;
    
    @Column(name = "DEP_PROCEDENCIA")
    private String depProcedencia;

    public Long getDepAvisoVencimiento() {
        return depAvisoVencimiento;
    }

    public void setDepAvisoVencimiento(Long depAvisoVencimiento) {
        this.depAvisoVencimiento = depAvisoVencimiento;
    }
    
    @JoinColumn(name = "DEP_AEROPUERTO", referencedColumnName = "AE_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Aeropuerto aeropuerto;
     
      @JoinColumn(name = "DEP_CATEGORIA", referencedColumnName = "DC_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DepCategoria depcategoria;

   
    

    public Dependencia() {
    }

    public Dependencia(Long depId) {
        this.depId = depId;
    }

    public String getDepNombre() {
        return depNombre;
    }

    public void setDepNombre(String depNombre) {
        this.depNombre = depNombre;
    }

    public String getDepAbreviatura() {
        return depAbreviatura;
    }

    public void setDepAbreviatura(String depAbreviatura) {
        this.depAbreviatura = depAbreviatura;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (depId != null ? depId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dependencia)) {
            return false;
        }
        Dependencia other = (Dependencia) object;
        if ((this.depId == null && other.depId != null) || (this.depId != null && !this.depId.equals(other.depId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.Dependencia[ depId=" + depId + " ]";
    }

    public Aeropuerto getAeropuerto() {
        return aeropuerto;
    }

    public void setAeropuerto(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
    }

    public Long getDepId() {
        return depId;
    }

    public void setDepId(Long depId) {
        this.depId = depId;
    }
     public DepCategoria getDepcategoria() {
        return depcategoria;
    }

    public void setDepcategoria(DepCategoria depcategoria) {
        this.depcategoria = depcategoria;
    }

    /**
     * @return the depProcedencia
     */
    public String getDepProcedencia() {
        return depProcedencia;
    }

    /**
     * @param depProcedencia the depProcedencia to set
     */
    public void setDepProcedencia(String depProcedencia) {
        this.depProcedencia = depProcedencia;
    }

    
}
