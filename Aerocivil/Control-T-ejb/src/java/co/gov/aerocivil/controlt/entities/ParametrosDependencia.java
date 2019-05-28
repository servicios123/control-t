/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author juancamilo
 */
@Entity
@Table(name = "PARAMETROS_DEPENDENCIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParametrosDependencia.findAll", query = "SELECT p FROM ParametrosDependencia p"),
    @NamedQuery(name = "ParametrosDependencia.findByPdId", query = "SELECT p FROM ParametrosDependencia p WHERE p.pdId = :pdId"),
    @NamedQuery(name = "ParametrosDependencia.findByNombre", query = "SELECT p FROM ParametrosDependencia p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "ParametrosDependencia.findByDescripcion", query = "SELECT p FROM ParametrosDependencia p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "ParametrosDependencia.findByModulo", query = "SELECT p FROM ParametrosDependencia p WHERE p.modulo = :modulo"),
    @NamedQuery(name = "ParametrosDependencia.findByCategoryModule", query = "SELECT p FROM ParametrosDependencia p WHERE p.categoria.dcId = :catId and p.modulo = :menId"),
    @NamedQuery(name = "ParametrosDependencia.findByDescripcion", query = "SELECT p FROM ParametrosDependencia p WHERE p.descripcion = :descripcion")})
public class ParametrosDependencia implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PD_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PARA_DEP")
    @SequenceGenerator(name = "SEQ_PARA_DEP", sequenceName = "SEQ_PARA_DEP", allocationSize = 1)
    private Long pdId;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "MODULO")
    private String modulo;
    @JoinColumn(name = "ID_DEP_CATEGORIA", referencedColumnName = "DC_ID")
    @ManyToOne(optional = false)
    private DepCategoria categoria;

    public ParametrosDependencia() {
    }

    public ParametrosDependencia(Long pdId) {
        this.pdId = pdId;
    }

    public ParametrosDependencia(Long pdId, String nombre) {
        this.pdId = pdId;
        this.nombre = nombre;
    }

    public Long getPdId() {
        return pdId;
    }

    public void setPdId(Long pdId) {
        this.pdId = pdId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public DepCategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(DepCategoria idDepCategoria) {
        this.categoria = idDepCategoria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pdId != null ? pdId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParametrosDependencia)) {
            return false;
        }
        ParametrosDependencia other = (ParametrosDependencia) object;
        if ((this.pdId == null && other.pdId != null) || (this.pdId != null && !this.pdId.equals(other.pdId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ParametrosDependencia[ pdId=" + pdId + " ]";
    }
}
